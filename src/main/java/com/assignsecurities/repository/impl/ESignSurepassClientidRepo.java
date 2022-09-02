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

import com.assignsecurities.domain.ESignSurepassClientidModel;

@Repository
public class ESignSurepassClientidRepo {
	@Autowired
	private NamedParameterJdbcTemplate template;

	public ESignSurepassClientidModel getById(Long id) {
		String query = "select a.* from esign_surepass_clientids a WHERE a.id=:id";
		MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource();
		sqlParameterSource.addValue("id", id);
		try {
			return template.queryForObject(query, sqlParameterSource, mapObjects());
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}
	
	public ESignSurepassClientidModel getByUserId(Long userId) {
		String query = "select a.* from esign_surepass_clientids a WHERE a.UserId=:userId";
		MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource();
		sqlParameterSource.addValue("userId", userId);
		try {
			return template.queryForObject(query, sqlParameterSource, mapObjects());
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}
	
	public ESignSurepassClientidModel getByClientId(String clientId) {
		String query = "select a.* from esign_surepass_clientids a WHERE ClientId=:clientId";
		MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource();
		sqlParameterSource.addValue("clientId", clientId);
		try {
			return template.queryForObject(query, sqlParameterSource, mapObjects());
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	private RowMapper<ESignSurepassClientidModel> mapObjects() {
		return (result, i) -> {
			return ESignSurepassClientidModel.builder().id(result.getLong("Id"))
					.userId(result.getLong("userId"))
					.clientId(result.getString("ClientId"))
					.reqType(result.getString("ReqType"))
					.reqStatus(result.getString("ReqStatus"))
					.remarks(result.getString("Remarks"))
					.createdDate(Objects.isNull(result.getDate("CreatedDate"))?null :result.getTimestamp("CreatedDate").toLocalDateTime())
					.build();
		};
	}
	
	

	public Long add(ESignSurepassClientidModel model) {
		KeyHolder keyHolderAddress = new GeneratedKeyHolder();
		MapSqlParameterSource sqlParameterSourceaddress = new MapSqlParameterSource();
		sqlParameterSourceaddress.addValue("userId", model.getUserId());
		sqlParameterSourceaddress.addValue("clientId", model.getClientId());
		sqlParameterSourceaddress.addValue("reqType", model.getReqType());
		sqlParameterSourceaddress.addValue("reqStatus", model.getReqStatus());
		sqlParameterSourceaddress.addValue("remarks", model.getRemarks());
		
		String queryaddress = "INSERT INTO esign_surepass_clientids (UserId,`ClientId`,`ReqType`,`ReqStatus`,`Remarks`) "
				+ "VALUES(:userId,:clientId,:reqType,:reqStatus,:remarks)";
		template.update(queryaddress, sqlParameterSourceaddress, keyHolderAddress);
		Long addressId = keyHolderAddress.getKey().longValue();
		return addressId;
	}
	
	public Long saveOrUpdate(ESignSurepassClientidModel model) {
		ESignSurepassClientidModel caseClientIdModel =  getByClientId(model.getClientId());
		if(Objects.isNull(caseClientIdModel)) {
			return add(model);
		}else {
			update(caseClientIdModel);
			return caseClientIdModel.getId();
		}
	}
	
	public void update(ESignSurepassClientidModel model) {
		MapSqlParameterSource sqlParameterSourceaddress = new MapSqlParameterSource();
		sqlParameterSourceaddress.addValue("userId", model.getUserId());
		sqlParameterSourceaddress.addValue("reqType", model.getReqType());
		sqlParameterSourceaddress.addValue("reqStatus", model.getReqStatus());
		sqlParameterSourceaddress.addValue("remarks", model.getRemarks());
		sqlParameterSourceaddress.addValue("clientId", model.getClientId());
		
		String queryaddress = "Update caseclientid set UserId=:userId,`ReqType`=:reqType,`ReqStatus`=:reqStatus,`Remarks`=:remarks where ClientId=:clientId ";
		template.update(queryaddress, sqlParameterSourceaddress);
	}
	
}
