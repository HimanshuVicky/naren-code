package com.assignsecurities.repository.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.assignsecurities.domain.CaseCommissionModel;

@Repository
public class CaseCommissionDao {
	@Autowired
	private NamedParameterJdbcTemplate template;

	private static String BASE_SQL ="select ccd.*,CASE WHEN (auRef.UserType ='Franchise') THEN f.Name  \r\n" + 
			"	ELSE \r\n" + 
			"		CONCAT(auRef.`FirstName`, ' ', auRef.`LastName`)\r\n" + 
			"	END ReferralName\r\n" + 
			"from `case` c \r\n" + 
			"inner join casecommissiondtl ccd on ccd.CaseId =c.Id \r\n" + 
			"Left Join VW_Application_Detls auRef on auRef.UserId = ccd.ReferralUserId \r\n" + 
			"left join franchise f on f.Id=auRef.FranchiseId ";
	public CaseCommissionModel getById(Long id) {
		String query =BASE_SQL +  "WHERE ccd.id=:id";
		MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource();
		sqlParameterSource.addValue("id", id);
		try {
			return template.queryForObject(query, sqlParameterSource, mapObjects());
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}
	
	public CaseCommissionModel getCaseById(Long caseId) {
		String query = BASE_SQL +  "WHERE ccd.CaseId=:caseId";
		MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource();
		sqlParameterSource.addValue("caseId", caseId);
		try {
			return template.queryForObject(query, sqlParameterSource, mapObjects());
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	private RowMapper<CaseCommissionModel> mapObjects() {
		return (result, i) -> {
			return CaseCommissionModel.builder().id(result.getLong("Id"))
					.caseId(result.getLong("caseId"))
					.referralUserId((result.getLong("ReferralUserId")))
					.referralProcFeeComm(result.getDouble("ReferralProcFeeComm"))
					.referralAgreFeeComm(result.getDouble("ReferralAgreFeeComm"))
					.referralDocumentProcFeeComm(result.getDouble("ReferralDocumentProcFeeComm"))
					.referralName(result.getString("ReferralName"))
					.build();
		};
	}
	

	public Long add(CaseCommissionModel model) {
		KeyHolder keyHolderAddress = new GeneratedKeyHolder();
		MapSqlParameterSource sqlParameterSourceaddress = new MapSqlParameterSource();
		sqlParameterSourceaddress.addValue("caseId", model.getCaseId());
		sqlParameterSourceaddress.addValue("referralUserId", model.getReferralUserId());
		sqlParameterSourceaddress.addValue("referralProcFeeComm", model.getReferralProcFeeComm());
		sqlParameterSourceaddress.addValue("referralAgreFeeComm", model.getReferralAgreFeeComm());
		sqlParameterSourceaddress.addValue("referralDocumentProcFeeComm", model.getReferralDocumentProcFeeComm());
		String queryaddress = "INSERT INTO casecommissiondtl (CaseId,ReferralUserId,ReferralProcFeeComm,ReferralAgreFeeComm,ReferralDocumentProcFeeComm) "
				+ "VALUES(:caseId,:referralUserId,:referralProcFeeComm,:referralAgreFeeComm,:referralDocumentProcFeeComm)";
		template.update(queryaddress, sqlParameterSourceaddress, keyHolderAddress);
		Long addressId = keyHolderAddress.getKey().longValue();
		return addressId;
	}
	
	public void updateReferralDocumentProcFeeComm(Long caseId, Double referralDocumentProcFeeComm) {
		MapSqlParameterSource sqlParameterSourceaddress = new MapSqlParameterSource();
		sqlParameterSourceaddress.addValue("caseId", caseId);
		sqlParameterSourceaddress.addValue("referralDocumentProcFeeComm", referralDocumentProcFeeComm);
		String queryaddress = "Update casecommissiondtl set ReferralDocumentProcFeeComm=:referralDocumentProcFeeComm where caseId=:caseId ";
		template.update(queryaddress, sqlParameterSourceaddress);
	}
}
