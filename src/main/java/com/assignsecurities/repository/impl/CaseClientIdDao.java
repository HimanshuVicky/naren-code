package com.assignsecurities.repository.impl;

import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.assignsecurities.domain.CaseClientIdModel;

@Repository
public class CaseClientIdDao {
	@Autowired
	private NamedParameterJdbcTemplate template;

	public CaseClientIdModel getById(Long id) {
		String query = "select a.* from caseclientid a WHERE a.id=:id";
		MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource();
		sqlParameterSource.addValue("id", id);
		try {
			return template.queryForObject(query, sqlParameterSource, mapObjects());
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}
	
	public CaseClientIdModel getCaseById(Long caseId) {
		String query = "select a.* from caseclientid a WHERE a.CaseId=:caseId";
		MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource();
		sqlParameterSource.addValue("caseId", caseId);
		try {
			return template.queryForObject(query, sqlParameterSource, mapObjects());
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	private RowMapper<CaseClientIdModel> mapObjects() {
		return (result, i) -> {
			return CaseClientIdModel.builder().id(result.getLong("Id"))
					.caseId(result.getLong("caseId"))
					.clientId1(result.getString("clientId1"))
					.clientId2(result.getString("clientId2"))
					.build();
		};
	}
	
	public Long saveOrUpdate(CaseClientIdModel model) {
		CaseClientIdModel caseClientIdModel =  getCaseById(model.getCaseId());
		if(Objects.isNull(caseClientIdModel)) {
			return add(model);
		}else {
			if(Objects.nonNull(model.getClientId1())) {
				caseClientIdModel.setClientId1(model.getClientId1());
			}
			caseClientIdModel.setClientId2(model.getClientId2());
			update(caseClientIdModel);
			return caseClientIdModel.getId();
		}
	}

	public Long add(CaseClientIdModel model) {
		KeyHolder keyHolderAddress = new GeneratedKeyHolder();
		MapSqlParameterSource sqlParameterSourceaddress = new MapSqlParameterSource();
		sqlParameterSourceaddress.addValue("caseId", model.getCaseId());
		sqlParameterSourceaddress.addValue("clientId1", model.getClientId1());
		sqlParameterSourceaddress.addValue("clientId2", model.getClientId2());
		
		String queryaddress = "INSERT INTO caseclientid (CaseId,ClientId1,ClientId2) "
				+ "VALUES(:caseId,:clientId1,:clientId2)";
		template.update(queryaddress, sqlParameterSourceaddress, keyHolderAddress);
		Long addressId = keyHolderAddress.getKey().longValue();
		return addressId;
	}
	
	public void update(CaseClientIdModel model) {
		MapSqlParameterSource sqlParameterSourceaddress = new MapSqlParameterSource();
		sqlParameterSourceaddress.addValue("caseId", model.getCaseId());
		sqlParameterSourceaddress.addValue("clientId1", model.getClientId1());
		sqlParameterSourceaddress.addValue("clientId2", model.getClientId2());
		sqlParameterSourceaddress.addValue("id", model.getId());
		
		String queryaddress = "Update caseclientid set CaseId=:caseId,ClientId1=:clientId1,ClientId2=:clientId2 where id=:id ";
		template.update(queryaddress, sqlParameterSourceaddress);
	}
}
