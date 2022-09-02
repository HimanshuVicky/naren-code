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

import com.assignsecurities.app.AppConstant;
import com.assignsecurities.app.exception.ServiceException;
import com.assignsecurities.app.util.ArgumentHelper;
import com.assignsecurities.bean.UserLoginBean;
import com.assignsecurities.domain.CaseModel;

import lombok.extern.slf4j.Slf4j;

@Repository
@Slf4j
public class CaseRepo {
	@Autowired
	private NamedParameterJdbcTemplate template;

	public CaseModel getById(Long id,UserLoginBean userLoginBean) {
		try {
			String query = "SELECT c.* FROM `case` c  inner join statusmaster sm"
					+ " on c.status=sm.Status WHERE c.id=:id";
			query = getSqlForUserType(userLoginBean, query, Boolean.FALSE);
			MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource();
			sqlParameterSource.addValue("id", id);
			sqlParameterSource.addValue("userId", userLoginBean.getId());
			
//			if (AppConstant.USER_TYPE_ADVOCATE.equalsIgnoreCase(userLoginBean.getApplicationUserBean().getUserType())
//				|| AppConstant.USER_TYPE_NOTARYL_PARTNER.equalsIgnoreCase(userLoginBean.getApplicationUserBean().getUserType())
//				|| AppConstant.USER_TYPE_CHARTERED_ACCOUNTANT_PARTNER.equalsIgnoreCase(userLoginBean.getApplicationUserBean().getUserType()))
//			{
//				sqlParameterSource.addValue("userId", userLoginBean.getId());
//			}
			
			if (AppConstant.USER_TYPE_FRANCHISE.equalsIgnoreCase(userLoginBean.getApplicationUserBean().getUserType()))
			{
				sqlParameterSource.addValue("franchiseId", userLoginBean.getApplicationUserBean().getFranchiseId());
			}
			return template.queryForObject(query, sqlParameterSource, mapObject());
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	public List<CaseModel> getByUserId(Long userId,UserLoginBean userLoginBean) {
		try {
//			String query = "SELECT * FROM `case` c WHERE UserId=:userId order by id desc";
			String query = "SELECT c.* FROM `case` c  inner join statusmaster sm  "
					+ " on c.status=sm.Status WHERE  c.UserId=:userId order by id desc";
			query = getSqlForUserType(userLoginBean, query, Boolean.FALSE);
			MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource();
			sqlParameterSource.addValue("userId", userId);
			return template.query(query, sqlParameterSource, mapObject());
		} catch (EmptyResultDataAccessException e) {
			return Collections.emptyList();
		}
	}
	
	public List<CaseModel> getByReferenceNumber(String referenceNumber, UserLoginBean userLoginBean) {
				
		try {
			String tempRefNo = "%" + referenceNumber + "%";
			String query = "SELECT distinct c.* FROM `case` c  inner join statusmaster sm  " + 
					"on c.status=sm.Status WHERE ReferenceNumber like :referenceNumber";
			
			query = getSqlForUserType(userLoginBean, query, Boolean.FALSE);
			
			query = query + " order by c.id desc";
			MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource();
			sqlParameterSource.addValue("referenceNumber", tempRefNo);
			sqlParameterSource.addValue("userId", userLoginBean.getId());
			sqlParameterSource.addValue("franchiseId", userLoginBean.getApplicationUserBean().getFranchiseId());
			return template.query(query, sqlParameterSource, mapObject());
		} catch (EmptyResultDataAccessException e) {
			return Collections.emptyList();
		}
	}
	
	public List<CaseModel>getCasesByStatuses(String stage, List<String> statuses, UserLoginBean userLoginBean) {
		try {
			String query = "SELECT distinct c.* FROM `case` c  inner join statusmaster sm  " + 
					"on c.status=sm.Status WHERE ";
			if(ArgumentHelper.isValid(stage)) {
				query = query + " Stage =:stage";
			}
			if(!(statuses.contains("All") || statuses.contains("all"))) {
				if(query.contains(":stage")) {
					query = query + " AND c.Status in(:statuses)";
				}else {
					query = query + " c.Status in(:statuses)";
				}
			}
			query = getSqlForUserType(userLoginBean, query, Boolean.FALSE);
			
			query = query + " order by c.id desc";
			MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource();
			sqlParameterSource.addValue("stage", stage);
			sqlParameterSource.addValue("statuses", statuses);
			sqlParameterSource.addValue("userId", userLoginBean.getId());
			sqlParameterSource.addValue("franchiseId", userLoginBean.getApplicationUserBean().getFranchiseId());
			return template.query(query, sqlParameterSource, mapObject());
		} catch (EmptyResultDataAccessException e) {
			return Collections.emptyList();
		}
	}

	private String getSqlForUserType(UserLoginBean userLoginBean, String query, boolean excludeStatus) {
		if (AppConstant.USER_TYPE_END_USER.equalsIgnoreCase(userLoginBean.getApplicationUserBean().getUserType())) {
//				Customer
			if(!excludeStatus) {
				query = query + " and `Customer` <>'NA'";
			}
			query = query +" and c.UserId=:userId ";
		}else if (AppConstant.USER_TYPE_CC.equalsIgnoreCase(userLoginBean.getApplicationUserBean().getUserType())) {
//				CustomerCare
			if(!excludeStatus) {
				query = query + " and `CustomerCare` <>'NA'";
			}
		}else if (AppConstant.USER_TYPE_ADMIN.equalsIgnoreCase(userLoginBean.getApplicationUserBean().getUserType())) {
//				Admin
			if(!excludeStatus) {
				query = query + " and `Admin` <> 'NA'";
			}
		}else if (AppConstant.USER_TYPE_FRANCHISE.equalsIgnoreCase(userLoginBean.getApplicationUserBean().getUserType())) {
//				FranchiseOwner
			if(!excludeStatus) {
				query = query + " and `FranchiseOwner` <>'NA'";
			}
			query = query +" and (FranchiseId=:franchiseId)";
		}else if (AppConstant.USER_TYPE_FRANCHISE_USER.equalsIgnoreCase(userLoginBean.getApplicationUserBean().getUserType())) {
//				Franchise User
			if(!excludeStatus) {
				query = query + " and `Franchise User` <>'NA'";
			}
			query = query +" and (FranchiseId=:franchiseId)";
		}else if (AppConstant.USER_TYPE_ADVOCATE.equalsIgnoreCase(userLoginBean.getApplicationUserBean().getUserType())) {
//				Lawyer
			if(!excludeStatus) {
				query = query + " and `Lawyer` <>'NA'";
			}
			query = query +" and (AssignLawyerId=:userId)";
		}else if (AppConstant.USER_TYPE_NOTARYL_PARTNER.equalsIgnoreCase(userLoginBean.getApplicationUserBean().getUserType())) {
//			Notary Partner
			query = query +" and (NotaryPartnerId=:userId)";
		}else if (AppConstant.USER_TYPE_CHARTERED_ACCOUNTANT_PARTNER.equalsIgnoreCase(userLoginBean.getApplicationUserBean().getUserType())) {
	//		Chartered Accountant
			query = query +" and (charteredAccountantId=:userId)";
		}else {
			throw new ServiceException("Not supported by this current user type.");
		}
		return query;
	}
	
	public Long add(CaseModel model) {
		String query = "insert into `case` (ReferenceNumber,FirstName,MiddleName,LastName,DateOfBirth,CompanyName,Status,UserId,FeeType,FeeValue,CreatedDate,Remarks,"
				+ "AddressId,CommAddressId,ApplicationId,AadharNumber,PanNumber,CancelChequeDoccumentId,AadharDocId,PanDocId,AggrmentDcoumentId,LegalHireCertId,"
				+ "FranchiseId,DigitalSignVerificationReference) "
				+ "values(:referenceNumber,:firstName,:middleName,:lastName,:dateOfBirth,:companyName,:status,:userId,:feeType,:feeValue,:createdDate,:remarks,"
				+ ":addressId,:commAddressId,:applicationId,:aadharNumber,:panNumber,:cancelChequeDoccumentId,:aadharDocId,:panDocId,:aggrmentDcoumentId,:legalHireCertId,"
				+ ":franchiseId,:digitalSignVerificationReference)";
		KeyHolder keyHolder = new GeneratedKeyHolder();

		MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource("referenceNumber", model.getReferenceNumber())
				.addValue("firstName", model.getFirstName())
				.addValue("middleName", model.getMiddleName()).addValue("lastName", model.getLastName())
				.addValue("dateOfBirth", model.getDateOfBirth())
				.addValue("companyName", model.getCompanyName()).addValue("status", model.getStatus())
				.addValue("userId", model.getUserId()).addValue("feeType", model.getFeeType())
				.addValue("feeValue", model.getFeeValue()).addValue("createdDate", model.getCreatedDate())
				.addValue("remarks", model.getRemarks())
				.addValue("addressId", model.getAddressId())
				.addValue("commAddressId", model.getCommAddressId())
				.addValue("applicationId", model.getApplicationId())
				.addValue("aadharNumber", model.getAadharNumber())
				.addValue("panNumber", model.getPanNumber())
				.addValue("cancelChequeDoccumentId", model.getCancelChequeDoccumentId())
				.addValue("aadharDocId", model.getAadharDocId())
				.addValue("panDocId", model.getPanDocId())
				.addValue("aggrmentDcoumentId", model.getAggrmentDcoumentId())
				.addValue("legalHireCertId", model.getLegalHireCertId())
				.addValue("franchiseId", model.getFranchiseId())
				.addValue("digitalSignVerificationReference", model.getDigitalSignVerificationReference());

		template.update(query, sqlParameterSource, keyHolder);
		Long caseId = keyHolder.getKey().longValue();
		
		String refNumber = model.getReferenceNumber() + caseId;
		query = "update `case` set ReferenceNumber=:referenceNumber  where id=:id ";
		sqlParameterSource = new MapSqlParameterSource("referenceNumber", refNumber)
				.addValue("id", caseId);
		int updatCount = template.update(query, sqlParameterSource);
		log.info("updatCount===>" + updatCount);
		
		return caseId;
	}
	public int updateCustomerAggrement(CaseModel model) {
		String query = "update `case` set AggrmentDcoumentId=:aggrmentDcoumentId,Status=:status, IseAdharComplete=:iseAdharComplete  where id=:id ";
		MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource()
				.addValue("status", model.getStatus())
				.addValue("aggrmentDcoumentId", model.getAggrmentDcoumentId())
				.addValue("iseAdharComplete",Objects.isNull(model.getIseAdharComplete()) ?Boolean.FALSE : model.getIseAdharComplete())
				.addValue("id", model.getId());
		log.info("query===>" + query);
		int updatCount = template.update(query, sqlParameterSource);
		log.info("updatCount===>" + updatCount);
		return updatCount;
	}
	public int update(CaseModel model) {
		String query = "update `case` set FirstName=:firstName, MiddleName=:middleName, LastName=:lastName,DateOfBirth=:dateOfBirth,Gender=:gender, CompanyName=:companyName,"
				+ "Status=:status, UserId=:userId, FeeType=:feeType, `CreatedDate`=:createdDate, Remarks=:remarks,"
				+ "AddressId=:addressId, CommAddressId=:commAddressId, ApplicationId=:applicationId, AadharNumber=:aadharNumber, PanNumber=:panNumber,"
				+ "CancelChequeDoccumentId=:cancelChequeDoccumentId, AadharDocId=:aadharDocId, PanDocId=:panDocId,"
				+ "AggrmentDcoumentId=:aggrmentDcoumentId, LegalHireCertId=:legalHireCertId, FranchiseId=:franchiseId,AssignLawyerId=:assignLawyerId,NotaryPartnerId=:notaryPartnerId,charteredAccountantId=:charteredAccountantId,"
				+ "DigitalSignVerificationReference=:digitalSignVerificationReference,ProcessingFee=:processingFee,"
				+ " ChequeNumber=:chequeNumber,IsFeeProcessed=:isFeeProcessed,IsWintessInfoReceived=:isWintessInfoReceived,IsSuretyInfoReceived=:isSuretyInfoReceived,"
				+ " IsCustomerDocumentsRequiredVerified=:isCustomerDocumentsRequiredVerified,"
				+ " IsGeneratedDocumentsRequiredVerified=:isGeneratedDocumentsRequiredVerified,"
				+ " IsSignedDocumentsVerified=:isSignedDocumentsVerified,IsUploadRTAResponseVerified=:isUploadRTAResponseVerified,IseAdharComplete=:iseAdharComplete, "
				+ " BankName=:bankName,BankAddress=:bankAddress,AccountNumber=:accountNumber,IfscCode=:ifscCode";
		
		if(Objects.nonNull(model.getPanVerified())) {
			query = query +",PanVerified=:panVerified";
		}
		query = query +" where id=:id ";
		MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource("firstName", model.getFirstName())
				.addValue("middleName", model.getMiddleName()).addValue("lastName", model.getLastName())
				.addValue("dateOfBirth", model.getDateOfBirth()).addValue("gender", model.getGender())
				.addValue("companyName", model.getCompanyName()).addValue("status", model.getStatus())
				.addValue("userId", model.getUserId()).addValue("feeType", model.getFeeType())
				.addValue("feeValue", model.getFeeValue()).addValue("createdDate", model.getCreatedDate())
				.addValue("remarks", model.getRemarks())
				.addValue("addressId", model.getAddressId())
				.addValue("commAddressId", model.getCommAddressId())
				.addValue("applicationId", model.getApplicationId())
				.addValue("aadharNumber", model.getAadharNumber())
				.addValue("panNumber", model.getPanNumber())
				.addValue("cancelChequeDoccumentId", model.getCancelChequeDoccumentId())
				.addValue("aadharDocId", model.getAadharDocId())
				.addValue("panDocId", model.getPanDocId())
				.addValue("aggrmentDcoumentId", model.getAggrmentDcoumentId())
				.addValue("legalHireCertId", model.getLegalHireCertId())
				.addValue("franchiseId", model.getFranchiseId())
				.addValue("assignLawyerId", model.getAssignLawyerId())
				.addValue("notaryPartnerId", model.getAssignNotaryId())	
				.addValue("charteredAccountantId", model.getCharteredAccountantId())				
				.addValue("digitalSignVerificationReference", model.getDigitalSignVerificationReference())
				.addValue("processingFee", model.getProcessingFee())
				.addValue("chequeNumber", model.getChequeNumber())
				.addValue("isFeeProcessed", Objects.isNull(model.getIsFeeProcessed()) ?Boolean.FALSE : model.getIsFeeProcessed())
				.addValue("isWintessInfoReceived",Objects.isNull(model.getIsWintessInfoReceived()) ?Boolean.FALSE : model.getIsWintessInfoReceived())
				.addValue("isSuretyInfoReceived", Objects.isNull(model.getIsSuretyInfoReceived()) ?Boolean.FALSE : model.getIsSuretyInfoReceived())
				.addValue("isCustomerDocumentsRequiredVerified",  Objects.isNull( model.getIsCustomerDocumentsRequiredVerified()) ?Boolean.FALSE : model.getIsCustomerDocumentsRequiredVerified())
				.addValue("isGeneratedDocumentsRequiredVerified",  Objects.isNull( model.getIsGeneratedDocumentsRequiredVerified()) ?Boolean.FALSE : model.getIsGeneratedDocumentsRequiredVerified())
				.addValue("isSignedDocumentsVerified",Objects.isNull(model.getIsSignedDocumentsVerified()) ?Boolean.FALSE : model.getIsSignedDocumentsVerified())
				.addValue("isUploadRTAResponseVerified",Objects.isNull(model.getIsUploadRTAResponseVerified()) ?Boolean.FALSE : model.getIsUploadRTAResponseVerified())
				.addValue("iseAdharComplete",Objects.isNull(model.getIseAdharComplete()) ?Boolean.FALSE : model.getIseAdharComplete())
				.addValue("bankName", model.getBankName())
				.addValue("bankAddress", model.getBankAddress())
				.addValue("accountNumber", model.getAccountNumber())
				.addValue("ifscCode", model.getIfscCode())
				.addValue("panVerified", model.getPanVerified())
				.addValue("id", model.getId());
		log.info("query===>" + query);
		int updatCount = template.update(query, sqlParameterSource);
		log.info("updatCount===>" + updatCount);
		return updatCount;
	}
	
	//Internal used only
	public int updateStatus(Long caseId, String status) {
		String query = "update `case` set Status=:status  where id=:id ";
		MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource().addValue("status", status)
				.addValue("id", caseId);
		log.info("query===>" + query);
		int updatCount = template.update(query, sqlParameterSource);
		log.info("updatCount===>" + updatCount);
		return updatCount;
	}

	private RowMapper<CaseModel> mapObject() {
		return (result, i) -> {
			return CaseModel.builder().id(result.getLong("id")).referenceNumber(result.getString("ReferenceNumber"))
					.firstName(result.getString("FirstName"))
					.middleName(result.getString("MiddleName")).lastName(result.getString("LastName"))
					.gender(result.getString("Gender"))
					.dateOfBirth(Objects.isNull(result.getDate("dateOfBirth")) ? null
							: result.getDate("dateOfBirth").toLocalDate())
					.companyName(result.getString("CompanyName")).status(result.getString("Status"))
					.userId(result.getLong("UserId")).feeType(result.getString("FeeType"))
					.feeValue(result.getDouble("FeeValue"))
					.createdDate(Objects.isNull(result.getDate("CreatedDate")) ? null
							: result.getTimestamp("CreatedDate").toLocalDateTime())
					.remarks(result.getString("Remarks")).addressId(result.getLong("AddressId"))
					.commAddressId(result.getLong("CommAddressId")).applicationId(result.getLong("ApplicationId"))
					.aadharNumber(result.getString("AadharNumber")).panNumber(result.getString("PanNumber"))
					.cancelChequeDoccumentId(result.getLong("CancelChequeDoccumentId"))
					.aadharDocId(result.getLong("AadharDocId")).panDocId(result.getLong("PanDocId"))
					.aggrmentDcoumentId(result.getLong("AggrmentDcoumentId"))
					.legalHireCertId(result.getLong("LegalHireCertId")).franchiseId(result.getLong("FranchiseId"))
					.assignLawyerId(result.getLong("AssignLawyerId")).assignNotaryId(result.getLong("notaryPartnerId"))
					.charteredAccountantId(result.getLong("charteredAccountantId"))
					.processingFee(result.getDouble("ProcessingFee"))
					.digitalSignVerificationReference(result.getString("DigitalSignVerificationReference"))
					.chequeNumber(result.getString("ChequeNumber"))
					.isFeeProcessed(result.getBoolean("IsFeeProcessed"))
					.isWintessInfoReceived(result.getBoolean("IsWintessInfoReceived"))
					.isSuretyInfoReceived(result.getBoolean("IsSuretyInfoReceived"))
					.isCustomerDocumentsRequiredVerified(result.getBoolean("IsCustomerDocumentsRequiredVerified"))
					.isGeneratedDocumentsRequiredVerified(result.getBoolean("IsGeneratedDocumentsRequiredVerified"))
					.isSignedDocumentsVerified(result.getBoolean("IsSignedDocumentsVerified"))
					.isUploadRTAResponseVerified(result.getBoolean("IsUploadRTAResponseVerified"))
					.iseAdharComplete(result.getBoolean("IseAdharComplete"))
					.bankName(result.getString("BankName"))
					.bankAddress(result.getString("BankAddress"))
					.accountNumber(result.getString("AccountNumber"))
					.ifscCode(result.getString("IfscCode")).panVerified(result.getBoolean("PanVerified")).build();
		};
	}
}
