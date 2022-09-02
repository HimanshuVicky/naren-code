package com.assignsecurities.repository.impl;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.assignsecurities.domain.FranchiseModel;

@Repository
public class FranchiseRepo {
	
	@Autowired
	private NamedParameterJdbcTemplate template;
	
	public FranchiseModel getFranchiseById(Long id) {
		String query = "SELECT * FROM franchise WHERE Id=:id";
		MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource();
		sqlParameterSource.addValue("id", id);
		try {
			return template.queryForObject(query, sqlParameterSource, mapObject());
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	public FranchiseModel getFranchiseByName(String franchiseName) {
		String query = "SELECT * FROM franchise WHERE Name like :franchiseName LIMIT 1";
		MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource();
		sqlParameterSource.addValue("franchiseName", "%"+franchiseName+"%");
		try {
			return template.queryForObject(query, sqlParameterSource, mapObject());
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}
	
	private RowMapper<FranchiseModel> mapObject() {
		return (rs, i) -> {
			return FranchiseModel.builder().id(rs.getLong("id")).name(rs.getString("Name"))
					.ownerFirstName(rs.getString("ownerFirstName")).ownerLastName(rs.getString("ownerLastName")).contactNumber(rs.getString("contactNumber"))
					.emailId(rs.getString("emailId")).isActive(rs.getBoolean("isActive"))
					.addressId(rs.getLong("addressId")).build();
//					.referralRegistrationFee(rs.getDouble("referralregfee"))
//					.referralProcFeecommission(rs.getDouble("referralprocfeecomm"))
//					.referralAgrementFeecommission(rs.getDouble("referralagrefeecomm"))
//					.documentProcssingFeecommission(rs.getDouble("documentprocfeecomm"))
//					.isRegistrationFeeReceived(rs.getBoolean("isregfeereceived"))
//					.eSignAgreementStatus(rs.getInt("esignagreementstatus")).build();
			};
	}
	
	public Long add(FranchiseModel model) {
		String query = "insert into franchise (Name,ownerFirstName,ownerLastName,ContactNumber,EmailId,AddressId,IsActive)"
				+ "values (:name,:ownerFirstName,:ownerLastName,:contactNumber,:emailId,:addressId,:isActive)";
		KeyHolder keyHolderAddress = new GeneratedKeyHolder();
		MapSqlParameterSource sqlParameterSourceaddress = new MapSqlParameterSource();
		sqlParameterSourceaddress.addValue("name", model.getName());
		sqlParameterSourceaddress.addValue("ownerFirstName", model.getOwnerFirstName());
		sqlParameterSourceaddress.addValue("ownerLastName", model.getOwnerLastName());
		sqlParameterSourceaddress.addValue("contactNumber", model.getContactNumber());
		sqlParameterSourceaddress.addValue("emailId", model.getEmailId());
		sqlParameterSourceaddress.addValue("addressId", model.getAddressId());
		sqlParameterSourceaddress.addValue("isActive", model.getIsActive());
//		sqlParameterSourceaddress.addValue("referralregfee", model.getReferralRegistrationFee());
//		sqlParameterSourceaddress.addValue("referralprocfeecomm", model.getReferralProcFeecommission());
//		sqlParameterSourceaddress.addValue("referralagrefeecomm", model.getReferralAgrementFeecommission());
//		sqlParameterSourceaddress.addValue("documentprocfeecomm", model.getDocumentProcssingFeecommission());
//		sqlParameterSourceaddress.addValue("isregfeereceived", model.getIsRegistrationFeeReceived());
//		sqlParameterSourceaddress.addValue("esignagreementstatus", model.getESignAgreementStatus());
		
			
		template.update(query, sqlParameterSourceaddress, keyHolderAddress);
		Long addressId = keyHolderAddress.getKey().longValue();
		return addressId;
		
		//,referralregfee,referralprocfeecomm,referralagrefeecomm,documentprocfeecomm,isregfeereceived,esignagreementstatus
	}

	public void update(FranchiseModel model) {
		String query = "update franchise set Name=:name,ownerFirstName=:ownerFirstName,ownerLastName=:ownerLastName,ContactNumber=:contactNumber,"
				+ "EmailId=:emailId,AddressId=:addressId,IsActive=:isActive"
				+ " where id=:id ";
//				+ "referralregfee=:referralregfee,referralprocfeecomm=:referralprocfeecomm,referralagrefeecomm=:referralagrefeecomm,"
//				+ "documentprocfeecomm=:documentprocfeecomm,isregfeereceived=:isregfeereceived,esignagreementstatus=:esignagreementstatus";
				
		MapSqlParameterSource sqlParameterSourceaddress = new MapSqlParameterSource();
		sqlParameterSourceaddress.addValue("name", model.getName());
		sqlParameterSourceaddress.addValue("ownerFirstName", model.getOwnerFirstName());
		sqlParameterSourceaddress.addValue("ownerLastName", model.getOwnerLastName());
		sqlParameterSourceaddress.addValue("contactNumber", model.getContactNumber());
		sqlParameterSourceaddress.addValue("emailId", model.getEmailId());
		sqlParameterSourceaddress.addValue("addressId", model.getAddressId());
		sqlParameterSourceaddress.addValue("isActive", model.getIsActive());
//		sqlParameterSourceaddress.addValue("referralregfee", model.getReferralRegistrationFee());
//		sqlParameterSourceaddress.addValue("referralprocfeecomm", model.getReferralProcFeecommission());
//		sqlParameterSourceaddress.addValue("referralagrefeecomm", model.getReferralAgrementFeecommission());
//		sqlParameterSourceaddress.addValue("documentprocfeecomm", model.getDocumentProcssingFeecommission());
//		sqlParameterSourceaddress.addValue("isregfeereceived", model.getIsRegistrationFeeReceived());
//		sqlParameterSourceaddress.addValue("esignagreementstatus", model.getESignAgreementStatus());
//		
		
		
		
		sqlParameterSourceaddress.addValue("id", model.getId());
		template.update(query, sqlParameterSourceaddress);

	}
	
	public List<FranchiseModel> getAllFranchise() {
		try {
 /*
  * Address population need to address.
  */
			MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource();
			String query = "SELECT au.*, a.id address_Id, a.address address,a.city city, a.state state_name, a.pincode pincode "
					+ " from franchise au " 
					+ "inner join address a on a.id=au.addressId order by a.id asc";
			List<FranchiseModel> franchiselst = template.query(query, sqlParameterSource, mapObject());
			return franchiselst;
		} catch (EmptyResultDataAccessException e) {
			return Collections.emptyList();
		}
	}

	public Long getFranchiseOwnerUserIdById(Long id) {
		String query = "select ul.id from userlogin ul  inner join applicationuser au on au.id=ul.ApplicationUserId \n" + 
				"WHERE au.UserType='Franchise' and au.FranchiseId =:id";
		MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource();
		sqlParameterSource.addValue("id", id);
		try {
			return template.queryForObject(query,sqlParameterSource, Long.class);
			
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}
}
