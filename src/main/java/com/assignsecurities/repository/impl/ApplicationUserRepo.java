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
import com.assignsecurities.bean.UserLoginBean;
import com.assignsecurities.domain.ApplicationUserModel;
 

@Repository
public class ApplicationUserRepo {
	@Autowired
	private NamedParameterJdbcTemplate template;

	public ApplicationUserModel getApplicationUserById(Long id) {
		String query = "SELECT * FROM applicationuser WHERE Id=:id";
		MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource();
		sqlParameterSource.addValue("id", id);
		try {
			return template.queryForObject(query, sqlParameterSource, mapObject());
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}
	public ApplicationUserModel getApplicationUserByIdForAdminFranchise(Long id, UserLoginBean userLoginBean) {
		String query = "SELECT * FROM applicationuser WHERE Id=:id";
		MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource();
		if(userLoginBean.getApplicationUserBean().getUserType().equals(AppConstant.USER_TYPE_FRANCHISE)) {
			query = query + " and FranchiseId=:franchiseId ";
			sqlParameterSource.addValue("franchiseId", userLoginBean.getApplicationUserBean().getFranchiseId());
		}
		sqlParameterSource.addValue("id", id);
		
		try {
			return template.queryForObject(query, sqlParameterSource, mapObject());
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}
	
	
	
	public ApplicationUserModel getApplicationUserByUserId(Long userId) {
		String query = " select au.* from applicationuser au inner join userlogin u ON u.ApplicationUserId =au.Id where u.id=:userId";
		MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource();
		sqlParameterSource.addValue("userId", userId);
		try {
			return template.queryForObject(query, sqlParameterSource, mapObject());
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	
	private RowMapper<ApplicationUserModel> mapObject() {
		return (rs, i) -> {
			return ApplicationUserModel.builder().id(rs.getLong("id")).firstName(rs.getString("firstName"))
					.middleName(rs.getString("middleName")).lastName(rs.getString("lastName"))
					.userType(rs.getString("userType")).emailId(rs.getString("emailId"))
					.dateOfBirth(
							Objects.isNull(rs.getDate("dateOfBirth")) ? null : rs.getDate("dateOfBirth").toLocalDate())
					.alternateMobileNo(rs.getString("alternateMobileNo")).addressId(rs.getLong("addressId"))
					.isActive(rs.getBoolean("isActive")).franchiseId(rs.getLong("franchiseId"))
					.gender(rs.getString("gender"))
					.madianSurname(rs.getString("madian_surname"))
					.defaultSurname(rs.getString("default_surname")).nonCustomerPin(rs.getString("nonCustomerPin"))
					.referalFranchiseId(rs.getLong("ReferalFranchiseId")).referalUserId(rs.getLong("ReferalUserId"))
					.referalPartnerFranchiseId(rs.getLong("referalPartnerFranchiseId")).build();
		};
	}
	

	public Long add(ApplicationUserModel model) {
		String query = "insert into applicationuser (FirstName,MiddleName,LastName,gender,madian_surname,default_surname,UserType,EmailId,DateOfBirth,AlternateMobileNo,AddressId,IsActive,franchiseId,nonCustomerPin,ReferalFranchiseId,ReferalUserId,referalPartnerFranchiseId) "
				+ "values (:firstName,:middleName,:lastName,:gender,:madian_surname,:default_surname,:userType,:emailId,:dateOfBirth,:alternateMobileNo,:addressId,:isActive,:franchiseId,:nonCustomerPin,:referalFranchiseId,:referalUserId,:referalPartnerFranchiseId)";
		KeyHolder keyHolderAddress = new GeneratedKeyHolder();
		MapSqlParameterSource sqlParameterSourceaddress = new MapSqlParameterSource();
		sqlParameterSourceaddress.addValue("firstName", model.getFirstName());
		sqlParameterSourceaddress.addValue("middleName", model.getMiddleName());
		sqlParameterSourceaddress.addValue("lastName", model.getLastName());
		sqlParameterSourceaddress.addValue("gender", model.getGender());
		sqlParameterSourceaddress.addValue("madian_surname", model.getMadianSurname());
		sqlParameterSourceaddress.addValue("default_surname", model.getDefaultSurname());
		sqlParameterSourceaddress.addValue("userType", model.getUserType());
		sqlParameterSourceaddress.addValue("emailId", model.getEmailId());
		sqlParameterSourceaddress.addValue("dateOfBirth", model.getDateOfBirth());
		sqlParameterSourceaddress.addValue("alternateMobileNo", model.getAlternateMobileNo());
		sqlParameterSourceaddress.addValue("addressId", model.getAddressId());
		sqlParameterSourceaddress.addValue("isActive", model.getIsActive());
		sqlParameterSourceaddress.addValue("franchiseId", model.getFranchiseId());
		sqlParameterSourceaddress.addValue("nonCustomerPin", model.getNonCustomerPin());
		sqlParameterSourceaddress.addValue("referalFranchiseId", model.getReferalFranchiseId());
		sqlParameterSourceaddress.addValue("referalUserId", model.getReferalUserId());
		sqlParameterSourceaddress.addValue("referalPartnerFranchiseId", model.getReferalPartnerFranchiseId());
		template.update(query, sqlParameterSourceaddress, keyHolderAddress);
		Long addressId = keyHolderAddress.getKey().longValue();
		return addressId;
	}

	public void update(ApplicationUserModel model) {
		String query = "update applicationuser set FirstName=:firstName,MiddleName=:middleName,LastName=:lastName,gender=:gender,madian_surname=:madian_surname,default_surname=:default_surname,"
				+ "EmailId=:emailId,DateOfBirth=:dateOfBirth,AlternateMobileNo=:alternateMobileNo,AddressId=:addressId,IsActive=:isActive"
				+ ",FranchiseId=:franchiseId, nonCustomerPin=:nonCustomerPin, referalPartnerFranchiseId=:referalPartnerFranchiseId where id=:id ";
		//,ReferalFranchiseId=:referalFranchiseId

		
		MapSqlParameterSource sqlParameterSourceaddress = new MapSqlParameterSource();
		sqlParameterSourceaddress.addValue("firstName", model.getFirstName());
		sqlParameterSourceaddress.addValue("middleName", model.getMiddleName());
		sqlParameterSourceaddress.addValue("lastName", model.getLastName());
		sqlParameterSourceaddress.addValue("gender", model.getGender());
		sqlParameterSourceaddress.addValue("madian_surname", model.getMadianSurname());
		sqlParameterSourceaddress.addValue("default_surname", model.getDefaultSurname());
//		sqlParameterSourceaddress.addValue("userType", model.getUserType());
		sqlParameterSourceaddress.addValue("emailId", model.getEmailId());
		sqlParameterSourceaddress.addValue("dateOfBirth", model.getDateOfBirth());
		sqlParameterSourceaddress.addValue("alternateMobileNo", model.getAlternateMobileNo());
		sqlParameterSourceaddress.addValue("addressId", model.getAddressId());
		sqlParameterSourceaddress.addValue("isActive", model.getIsActive());
		sqlParameterSourceaddress.addValue("franchiseId", model.getFranchiseId());
		sqlParameterSourceaddress.addValue("nonCustomerPin", model.getNonCustomerPin());
//		sqlParameterSourceaddress.addValue("referalFranchiseId", model.getReferalFranchiseId());
		sqlParameterSourceaddress.addValue("referalPartnerFranchiseId", model.getReferalPartnerFranchiseId());
		sqlParameterSourceaddress.addValue("id", model.getId());
		template.update(query, sqlParameterSourceaddress);

	}
	
	public List<ApplicationUserModel> getAllApplicationUsersForGivenFranchiseIdAndUserType(UserLoginBean userLoginBean,long franchiseId, String userType) {
		try {

			MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource();
			sqlParameterSource.addValue("franchiseId", franchiseId);
			sqlParameterSource.addValue("userType", userType);
			String query = "SELECT au.*, a.id address_Id, a.address address,a.city city, a.state state_name, a.pincode pincode "
					+ " from applicationuser au " 
					+ "inner join address a on a.id=au.addressId " 
					+ " WHERE au.usertype=:userType AND au.isActive=1 ";
			if(!userLoginBean.getApplicationUserBean().getUserType() .equals(AppConstant.USER_TYPE_ADMIN)) {
				if(userType.equalsIgnoreCase(AppConstant.USER_TYPE_FRANCHISE_USER)
						|| userType.equalsIgnoreCase(AppConstant.USER_TYPE_FRANCHISE)) {
					query = query + " and au.FranchiseId=:franchiseId ";
				}
			}
			query = query + "order by au.FirstName,au.LastName, au.id asc";
			List<ApplicationUserModel> applicationUser = template.query(query, sqlParameterSource, mapObject());
			return applicationUser;
		} catch (EmptyResultDataAccessException e) {
			return Collections.emptyList();
		}
	}
	
	public List<ApplicationUserModel> getAllApplicationUsers(UserLoginBean userLoginBean) {
		try {
			long franchiseId =userLoginBean.getApplicationUserBean().getFranchiseId();
			String userType  = userLoginBean.getApplicationUserBean().getUserType();
			MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource();
			sqlParameterSource.addValue("franchiseId", franchiseId);
			String query = "SELECT distinct au.*, a.id address_Id, a.address address,a.city city, a.state state_name, a.pincode pincode, ul.MobileNo\r\n" + 
					" from applicationuser au inner join address a on a.id=au.addressId \r\n" + 
					"inner join userlogin ul on ul.ApplicationUserId=au.id\r\n" + 
					"where  ul.IsActive=1 and au.IsActive=1 ";
			if(!(userLoginBean.getApplicationUserBean().getUserType() .equals(AppConstant.USER_TYPE_ADMIN)||
					userLoginBean.getApplicationUserBean().getUserType() .equals(AppConstant.USER_TYPE_CC))) {
				if(userType.equalsIgnoreCase(AppConstant.USER_TYPE_FRANCHISE_USER)
						|| userType.equalsIgnoreCase(AppConstant.USER_TYPE_FRANCHISE)) {
					query = query + " and au.FranchiseId=:franchiseId ";
				}
			}
			query = query + " order by au.FirstName,au.LastName,au.id asc";
			List<ApplicationUserModel> applicationUser = template.query(query, sqlParameterSource, mapObject());
			return applicationUser;
		} catch (EmptyResultDataAccessException e) {
			return Collections.emptyList();
		}
	}
	
	public ApplicationUserModel getApplicationUsersDtlForGivenFranchiseOwnerIdAndUserType(long franchiseId, String userType) {
			MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource();
			sqlParameterSource.addValue("franchiseId", franchiseId);
			sqlParameterSource.addValue("userType", userType);
			String query = "SELECT au.*, a.id address_Id, a.address address,a.city city, a.state state_name, a.pincode pincode "
					+ " from applicationuser au " 
					+ "inner join address a on a.id=au.addressId " 
					+ " WHERE au.usertype=:userType and au.FranchiseId=:franchiseId ";
			try {
				return template.queryForObject(query, sqlParameterSource, mapObject());
			} catch (EmptyResultDataAccessException e) {
				return null;
			}
		
	}
}
