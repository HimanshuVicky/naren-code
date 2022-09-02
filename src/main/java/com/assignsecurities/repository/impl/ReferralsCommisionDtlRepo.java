package com.assignsecurities.repository.impl;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.assignsecurities.domain.ReferralsCommisionDtlModel;

@Repository
public class ReferralsCommisionDtlRepo {
	
	@Autowired
	NamedParameterJdbcTemplate template;
	
	
	public ReferralsCommisionDtlModel getReferralPartnerCommisionDtlforGivenAppUserId(Long userId) {
		String query = "SELECT * FROM referralscommissiondtl WHERE UserId=:userId";
		MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource();
		sqlParameterSource.addValue("userId", userId);
		try {
			return template.queryForObject(query, sqlParameterSource, mapObject());
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	} 
	
	public ReferralsCommisionDtlModel getReferralPartnerCommisionDtlforGivenFranchiseId(Long franchiseId) {
		String query = "SELECT * FROM referralscommissiondtl WHERE FranchiseId=:franchiseId";
		MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource();
		sqlParameterSource.addValue("franchiseId", franchiseId);
		try {
			return template.queryForObject(query, sqlParameterSource, mapObject());
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	} 
	
	public ReferralsCommisionDtlModel getReferralPartnerCommisionDtlforGivenFranchiseUserId(Long franchiseUserId) {
		String query = "select rcd.* from referralscommissiondtl rcd\n" + 
				"inner join VW_Application_Detls au on au.FranchiseId =rcd.FranchiseId and au.UserType ='Franchise'\n" + 
				"inner join franchise f2 on f2.Id = rcd.FranchiseId where au.UserId =:franchiseUserId";
		MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource();
		sqlParameterSource.addValue("franchiseUserId", franchiseUserId);
		try {
			return template.queryForObject(query, sqlParameterSource, mapObject());
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	} 
	
	public ReferralsCommisionDtlModel getById(Long id) {
		String query = "SELECT * FROM referralscommissiondtl WHERE id=:id";
		MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource();
		sqlParameterSource.addValue("id", id);
		try {
			return template.queryForObject(query, sqlParameterSource, mapObject());
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	} 
	
	
	private RowMapper<ReferralsCommisionDtlModel> mapObject() {
		return (result, i) -> {
			return ReferralsCommisionDtlModel.builder().id(result.getLong("id"))
					.UserId(result.getLong("UserId"))
					.franchiseId(result.getLong("franchiseId"))
					.registrationFee(result.getDouble("RegFee"))
					.procesingFeeCommison(result.getDouble("ProcFeeComm"))
					.agreementfeeCommision(result.getDouble("AgreFeeComm"))
					.docuProcessingfeeCommision(result.getDouble("DocProcCommFee"))
					.isRegistrationFeeReceived(result.getBoolean("isRegfeereceived"))
					.eSignAgreementStatus(result.getInt("eSignAgreementStatus"))
					.feeReferenceKey(result.getString("FeeReferenceKey")).build();				
										
		};
	}

	
	public Long add(ReferralsCommisionDtlModel model) {
		String query = "insert into referralscommissiondtl (UserId,FranchiseId,RegFee,ProcFeeComm,AgreFeeComm,DocProcCommFee,isRegfeereceived,eSignAgreementStatus)"
				+ "values (:userId,:franchiseId,:regFee,:procFeeComm,:agreFeeComm,:docProcCommFee,:isRegfeereceived,:eSignAgreementStatus)";
		KeyHolder keyHolderReferralComm = new GeneratedKeyHolder();
		MapSqlParameterSource sqlParameterSourcereferralComm = new MapSqlParameterSource();
		Long franchaiseId = model.getFranchiseId();
		if(Objects.nonNull(franchaiseId) && franchaiseId.longValue()==0) {
			franchaiseId = null;
		}
		
		Long userId = model.getUserId();
		if(Objects.nonNull(userId) && userId.longValue()==0) {
			userId = null;
		}
		sqlParameterSourcereferralComm.addValue("userId",userId);
		sqlParameterSourcereferralComm.addValue("franchiseId", franchaiseId);
		sqlParameterSourcereferralComm.addValue("regFee", model.getRegistrationFee());
		sqlParameterSourcereferralComm.addValue("procFeeComm", model.getProcesingFeeCommison());
		sqlParameterSourcereferralComm.addValue("agreFeeComm", model.getAgreementfeeCommision());
		sqlParameterSourcereferralComm.addValue("docProcCommFee", model.getDocuProcessingfeeCommision());
		sqlParameterSourcereferralComm.addValue("isRegfeereceived",
				(Objects.nonNull(model.getIsRegistrationFeeReceived()) && model.getIsRegistrationFeeReceived()) ? 1
						: 0);
		sqlParameterSourcereferralComm.addValue("eSignAgreementStatus", model.getESignAgreementStatus());

			
		template.update(query, sqlParameterSourcereferralComm, keyHolderReferralComm);
		Long referralCommDtlId = keyHolderReferralComm.getKey().longValue();
		return referralCommDtlId;
	}

	public void update(ReferralsCommisionDtlModel model) {
		String query = "update referralscommissiondtl set UserId=:userId,FranchiseId=:franchiseId,RegFee=:regFee,ProcFeeComm=:procFeeComm,AgreFeeComm=:agreFeeComm,DocProcCommFee=:docProcCommFee,isRegfeereceived=:isRegfeereceived,eSignAgreementStatus=:eSignAgreementStatus"
				+ " where id=:id ";
				
		Long franchaiseId = model.getFranchiseId();
		if(Objects.nonNull(franchaiseId) && franchaiseId.longValue()==0) {
			franchaiseId = null;
		}
		
		Long userId = model.getUserId();
		if(Objects.nonNull(userId) && userId.longValue()==0) {
			userId = null;
		}
		
		
		MapSqlParameterSource sqlParameterSourcereferralComm = new MapSqlParameterSource();
		sqlParameterSourcereferralComm.addValue("userId", userId);
		sqlParameterSourcereferralComm.addValue("franchiseId", franchaiseId);
		sqlParameterSourcereferralComm.addValue("regFee", model.getRegistrationFee());
		sqlParameterSourcereferralComm.addValue("procFeeComm", model.getProcesingFeeCommison());
		sqlParameterSourcereferralComm.addValue("agreFeeComm", model.getAgreementfeeCommision());
		sqlParameterSourcereferralComm.addValue("docProcCommFee", model.getDocuProcessingfeeCommision());
		sqlParameterSourcereferralComm.addValue("isRegfeereceived", model.getIsRegistrationFeeReceived());
		sqlParameterSourcereferralComm.addValue("eSignAgreementStatus", model.getESignAgreementStatus());

		sqlParameterSourcereferralComm.addValue("id", model.getId());
		template.update(query, sqlParameterSourcereferralComm);

	}
	
	public void updateForProfileEdit(ReferralsCommisionDtlModel model) {
		String query = "update referralscommissiondtl set UserId=:userId,FranchiseId=:franchiseId,RegFee=:regFee,ProcFeeComm=:procFeeComm,AgreFeeComm=:agreFeeComm,DocProcCommFee=:docProcCommFee,isRegfeereceived=:isRegfeereceived"
				+ " where id=:id ";
				
		Long franchaiseId = model.getFranchiseId();
		if(Objects.nonNull(franchaiseId) && franchaiseId.longValue()==0) {
			franchaiseId = null;
		}
		
		Long userId = model.getUserId();
		if(Objects.nonNull(userId) && userId.longValue()==0) {
			userId = null;
		}
		
		
		MapSqlParameterSource sqlParameterSourcereferralComm = new MapSqlParameterSource();
		sqlParameterSourcereferralComm.addValue("userId", userId);
		sqlParameterSourcereferralComm.addValue("franchiseId", franchaiseId);
		sqlParameterSourcereferralComm.addValue("regFee", model.getRegistrationFee());
		sqlParameterSourcereferralComm.addValue("procFeeComm", model.getProcesingFeeCommison());
		sqlParameterSourcereferralComm.addValue("agreFeeComm", model.getAgreementfeeCommision());
		sqlParameterSourcereferralComm.addValue("docProcCommFee", model.getDocuProcessingfeeCommision());
		sqlParameterSourcereferralComm.addValue("isRegfeereceived", model.getIsRegistrationFeeReceived());

		sqlParameterSourcereferralComm.addValue("id", model.getId());
		template.update(query, sqlParameterSourcereferralComm);

	}
	
	public List <ReferralsCommisionDtlModel> getByGivenFranchiseId(Long franchiseId) {
		try {
			String query = "SELECT * FROM `referralscommissiondtl` WHERE franchiseId=:franchiseId order by id desc";
			MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource();
			sqlParameterSource.addValue("franchiseId", franchiseId);
			List <ReferralsCommisionDtlModel> referralCommDtllst = template.query(query, sqlParameterSource, mapObject());
			return referralCommDtllst;
		} catch (EmptyResultDataAccessException e) {
			return Collections.emptyList();
		}
	}
	
	public void updateESignAgreementStatusForReferralUser(Integer statusId, Long referralUserId) {
		String query = "update referralscommissiondtl set eSignAgreementStatus=:eSignAgreementStatus where userId=:referralUserId ";
		MapSqlParameterSource sqlParameterSourcereferralComm = new MapSqlParameterSource();
		sqlParameterSourcereferralComm.addValue("referralUserId", referralUserId);
		sqlParameterSourcereferralComm.addValue("eSignAgreementStatus", statusId);
		template.update(query, sqlParameterSourcereferralComm);

	}
	
	public void updateESignAgreementStatusForReferralFranchise(Integer statusId, Long referralFranchiseId) {
		String query = "update referralscommissiondtl set eSignAgreementStatus=:eSignAgreementStatus where franchiseId=:referralFranchiseId ";
		MapSqlParameterSource sqlParameterSourcereferralComm = new MapSqlParameterSource();
		sqlParameterSourcereferralComm.addValue("referralFranchiseId", referralFranchiseId);
		sqlParameterSourcereferralComm.addValue("eSignAgreementStatus", statusId);
		template.update(query, sqlParameterSourcereferralComm);

	}
	
	public void updateFeeReferenceNumberForReferralUser(String feeReferenceKey, Long referralUserId) {
		String query = "update referralscommissiondtl set isRegfeereceived=1,FeeReferenceKey=:feeReferenceKey where userId=:referralUserId ";
		MapSqlParameterSource sqlParameterSourcereferralComm = new MapSqlParameterSource();
		sqlParameterSourcereferralComm.addValue("referralUserId", referralUserId);
		sqlParameterSourcereferralComm.addValue("feeReferenceKey", feeReferenceKey);
		template.update(query, sqlParameterSourcereferralComm);

	}
	
	public void updateFeeReferenceNumberForReferralFranchise(String feeReferenceKey, Long referralFranchiseId) {
		String query = "update referralscommissiondtl set isRegfeereceived=1,FeeReferenceKey=:feeReferenceKey  where franchiseId=:referralFranchiseId ";
		MapSqlParameterSource sqlParameterSourcereferralComm = new MapSqlParameterSource();
		sqlParameterSourcereferralComm.addValue("referralFranchiseId", referralFranchiseId);
		sqlParameterSourcereferralComm.addValue("feeReferenceKey", feeReferenceKey);
		template.update(query, sqlParameterSourcereferralComm);

	}

}
