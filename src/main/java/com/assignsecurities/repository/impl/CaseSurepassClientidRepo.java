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
import com.assignsecurities.domain.CaseSurepassClientidModel;

@Repository
public class CaseSurepassClientidRepo {
	@Autowired
	private NamedParameterJdbcTemplate template;

	public CaseSurepassClientidModel getById(Long id) {
		String query = "select a.* from case_surepass_clientids a WHERE a.id=:id";
		MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource();
		sqlParameterSource.addValue("id", id);
		try {
			return template.queryForObject(query, sqlParameterSource, mapObjects());
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}
	
	public CaseSurepassClientidModel getByCaseId(Long caseId) {
		String query = "select a.* from case_surepass_clientids a WHERE a.CaseId=:caseId";
		MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource();
		sqlParameterSource.addValue("caseId", caseId);
		try {
			return template.queryForObject(query, sqlParameterSource, mapObjects());
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}
	
	public CaseSurepassClientidModel getByClientId(String clientId) {
		String query = "select a.* from case_surepass_clientids a WHERE ClientId=:clientId";
		MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource();
		sqlParameterSource.addValue("clientId", clientId);
		try {
			return template.queryForObject(query, sqlParameterSource, mapObjects());
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	private RowMapper<CaseSurepassClientidModel> mapObjects() {
		return (result, i) -> {
			return CaseSurepassClientidModel.builder().id(result.getLong("Id"))
					.caseId(result.getLong("caseId"))
					.clientId(result.getString("ClientId"))
					.reqType(result.getString("ReqType"))
					.reqStatus(result.getString("ReqStatus"))
					.remarks(result.getString("Remarks"))
					.createdDate(Objects.isNull(result.getDate("CreatedDate"))?null :result.getTimestamp("CreatedDate").toLocalDateTime())
					.build();
		};
	}
	
	

	public Long add(CaseSurepassClientidModel model) {
		KeyHolder keyHolderAddress = new GeneratedKeyHolder();
		MapSqlParameterSource sqlParameterSourceaddress = new MapSqlParameterSource();
		sqlParameterSourceaddress.addValue("caseId", model.getCaseId());
		sqlParameterSourceaddress.addValue("clientId", model.getClientId());
		sqlParameterSourceaddress.addValue("reqType", model.getReqType());
		sqlParameterSourceaddress.addValue("reqStatus", model.getReqStatus());
		sqlParameterSourceaddress.addValue("remarks", model.getRemarks());
		
		String queryaddress = "INSERT INTO case_surepass_clientids (CaseId,`ClientId`,`ReqType`,`ReqStatus`,`Remarks`) "
				+ "VALUES(:caseId,:clientId,:reqType,:reqStatus,:remarks)";
		template.update(queryaddress, sqlParameterSourceaddress, keyHolderAddress);
		Long addressId = keyHolderAddress.getKey().longValue();
		return addressId;
	}
	
	public Long saveOrUpdate(CaseSurepassClientidModel model) {
		CaseSurepassClientidModel caseClientIdModel =  getByClientId(model.getClientId());
		if(Objects.isNull(caseClientIdModel)) {
			return add(model);
		}else {
			update(caseClientIdModel);
			return caseClientIdModel.getId();
		}
	}
	
	public void update(CaseSurepassClientidModel model) {
		MapSqlParameterSource sqlParameterSourceaddress = new MapSqlParameterSource();
		sqlParameterSourceaddress.addValue("caseId", model.getCaseId());
		sqlParameterSourceaddress.addValue("reqType", model.getReqType());
		sqlParameterSourceaddress.addValue("reqStatus", model.getReqStatus());
		sqlParameterSourceaddress.addValue("remarks", model.getRemarks());
		sqlParameterSourceaddress.addValue("clientId", model.getClientId());
		
		String queryaddress = "Update caseclientid set CaseId=:caseId,`ReqType`=:reqType,`ReqStatus`=:reqStatus,`Remarks`=:remarks where ClientId=:clientId ";
		template.update(queryaddress, sqlParameterSourceaddress);
	}
	
}
