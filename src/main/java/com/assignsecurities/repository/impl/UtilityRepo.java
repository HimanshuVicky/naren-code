package com.assignsecurities.repository.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.assignsecurities.app.AppConstant;
import com.assignsecurities.app.exception.ServiceException;
import com.assignsecurities.bean.KeyValueBean;
import com.assignsecurities.bean.UserLoginBean;
import com.assignsecurities.domain.CityMasterModel;
import com.assignsecurities.domain.DocumentMasterModel;
import com.assignsecurities.domain.FeeMasterModel;
import com.assignsecurities.domain.StateMasterModel;
import com.assignsecurities.domain.StatusMasterModel;

@Repository
public class UtilityRepo {
	@Autowired
	NamedParameterJdbcTemplate template;

	public List<FeeMasterModel> getMasterFeeList() {
		try {
			return template.query("select * from feemaster order by TempOrder", mapMasterFeeList());
		} catch (EmptyResultDataAccessException e) {
			return Collections.emptyList();
		}
	}

	private RowMapper<FeeMasterModel> mapMasterFeeList() {
		return (result, i) -> {
			return FeeMasterModel.builder().id(result.getLong("id")).feeFor(result.getString("feeFor"))
					.feeType(result.getString("feeType")).isGSTApplicable(result.getBoolean("isGSTApplicable"))
					.tempOrder(result.getInt("tempOrder")).build();
		};
	}

	public List<DocumentMasterModel> getMasterDocumentList(String uploadedOrGenerated) {
		try {
			MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource();
			sqlParameterSource.addValue("uploadedOrGenerated", uploadedOrGenerated);
			return template.query(
					"select * from documentmaster where `UploadedOrGenerated`=:uploadedOrGenerated order by TempOrder",
					sqlParameterSource, mapMasterDocumentList());
		} catch (EmptyResultDataAccessException e) {
			return Collections.emptyList();
		}
	}
	public DocumentMasterModel getMasterDocument(String particulars) {
		try {
			MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource();
			sqlParameterSource.addValue("particulars", particulars);
			return template.queryForObject(
					"select * from documentmaster where `Particulars`=:particulars limit 1",
					sqlParameterSource, mapMasterDocumentList());
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}
	
	public List<DocumentMasterModel> getMasterDocumentParticularsStartWith(String particulars) {
		try {
			MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource();
			sqlParameterSource.addValue("particulars", particulars+"%");
			return template.query(
					"select * from documentmaster where `Particulars` like :particulars",
					sqlParameterSource, mapMasterDocumentList());
		} catch (EmptyResultDataAccessException e) {
			return Collections.emptyList();
		}
	}
	
	public DocumentMasterModel getMasterDocumentAndRtaGroup(String particulars, String rtaGroup) {
		try {
			MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource();
			sqlParameterSource.addValue("particulars", particulars);
			sqlParameterSource.addValue("rtaGroup", rtaGroup);
			return template.queryForObject(
					"select distinct dm.*  from documentmaster dm inner join rta_templates_group rta on rta.RtaGroup =dm.RtaGroup"
					+ " where `Particulars`=:particulars and rta.rtaGroup=:rtaGroup",
					sqlParameterSource, mapMasterDocumentList());
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}


	private RowMapper<DocumentMasterModel> mapMasterDocumentList() {
		return (result, i) -> {
			return DocumentMasterModel.builder().id(result.getLong("id")).particulars(result.getString("Particulars"))
					.tempOrder(result.getInt("TempOrder")).type(result.getString("Type"))
					.uploadedOrGenerated(result.getString("UploadedOrGenerated")).action(result.getString("Action"))
					.process(result.getString("Process")).link(result.getString("Link")).build();
		};
	}
	public List<StatusMasterModel> getMasterStatusListForType(String type) {
		try {
			String sql = "select * from statusmaster ";
			MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource();
			if(type.equalsIgnoreCase("Case")) {
				sql = sql + " where `Stage` like :type";
				sqlParameterSource.addValue("type", "Stage%");
			}else {
				sql = sql + " where `Stage`=:type ";
				sqlParameterSource.addValue("type", type);
			}
			sql = sql + " order by TempOrder";
			
			
			return template.query(sql,
					sqlParameterSource, mapMasterStatusList());
		} catch (EmptyResultDataAccessException e) {
			return Collections.emptyList();
		}
	}
	
	public List<StatusMasterModel> getMasterStatusList(String stage) {
		try {
			MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource();
			sqlParameterSource.addValue("stage", stage);
			return template.query("select * from statusmaster where `Stage`=:stage order by TempOrder",
					sqlParameterSource, mapMasterStatusList());
		} catch (EmptyResultDataAccessException e) {
			return Collections.emptyList();
		}
	}

	public List<StatusMasterModel> getMasterStatusList(UserLoginBean userLoginBean) {
		try {
			String sql ="select * from statusmaster where `Stage`<>'Application'";
			if (AppConstant.USER_TYPE_END_USER.equalsIgnoreCase(userLoginBean.getApplicationUserBean().getUserType())) {
//				Customer
				sql = sql + " and `Customer` <>'NA'";
			}else if (AppConstant.USER_TYPE_CC.equalsIgnoreCase(userLoginBean.getApplicationUserBean().getUserType())) {
//				CustomerCare
				sql = sql + " and `CustomerCare` <>'NA'";
			}else if (AppConstant.USER_TYPE_ADMIN.equalsIgnoreCase(userLoginBean.getApplicationUserBean().getUserType())) {
//				Admin
				sql = sql + " and `Admin` <> 'NA'";
			}else if (AppConstant.USER_TYPE_FRANCHISE.equalsIgnoreCase(userLoginBean.getApplicationUserBean().getUserType())) {
//				FranchiseOwner
				sql = sql + " and `FranchiseOwner` <>'NA'";
//				sql = sql +" and (c.UserId=:userId or FranchiseId=:franchiseId)";
			}else if (AppConstant.USER_TYPE_FRANCHISE_USER.equalsIgnoreCase(userLoginBean.getApplicationUserBean().getUserType())) {
//				Franchise User
				sql = sql + " and `Franchise User` <>'NA'";
//				sql = sql +" and (c.UserId=:userId or FranchiseId=:franchiseId)";
			}else if (AppConstant.USER_TYPE_ADVOCATE.equalsIgnoreCase(userLoginBean.getApplicationUserBean().getUserType())
					|| AppConstant.USER_TYPE_NOTARYL_PARTNER.equalsIgnoreCase(userLoginBean.getApplicationUserBean().getUserType())
					|| AppConstant.USER_TYPE_CHARTERED_ACCOUNTANT_PARTNER.equalsIgnoreCase(userLoginBean.getApplicationUserBean().getUserType())) {
//				Lawyer
				sql = sql + " and `Lawyer` <>'NA'";
//				sql = sql +" and (c.UserId=:userId or AssignLawyerId=:userId)";
			}else {
				System.out.println("userLoginBean.getApplicationUserBean().getUserType()===>"
						+ userLoginBean.getApplicationUserBean().getUserType());
				throw new ServiceException("Not supported by this current user type.");
			}
			sql = sql + " order by TempOrder ";
			MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource();
			sqlParameterSource.addValue("userId", userLoginBean.getId());
			sqlParameterSource.addValue("franchiseId", userLoginBean.getApplicationUserBean().getFranchiseId());
			return template.query( sql, sqlParameterSource, mapMasterStatusList());
		} catch (EmptyResultDataAccessException e) {
			return Collections.emptyList();
		}
	}

	private RowMapper<StatusMasterModel> mapMasterStatusList() {
		return (result, i) -> {
			return StatusMasterModel.builder().id(result.getLong("id")).stage(result.getString("Stage"))
					.status(result.getString("Status")).customer(result.getString("Customer"))
					.customerCare(result.getString("CustomerCare")).admin(result.getString("Admin"))
					.franchiseOwner(result.getString("FranchiseOwner"))
					.franchiseUser(result.getString("Franchise User")).lawyer(result.getString("Lawyer"))
					.autoUpdate(result.getBoolean("AutoUpdate")).flag(result.getString("Flag"))
					.isActive(result.getBoolean("IsActive")).tempOrder(result.getInt("TempOrder")).build();
		};
	}

	public List<CityMasterModel> getCityList() {
		try {
			return template.query("select * from citymaster order by city", mapCityList());
		} catch (EmptyResultDataAccessException e) {
			return Collections.emptyList();
		}
	}

	private RowMapper<CityMasterModel> mapCityList() {
		return (result, i) -> {
			return CityMasterModel.builder().id(result.getLong("id")).city(result.getString("City"))
					.urbanStatus(result.getString("Urban_Status")).stateCode(result.getString("State_Code"))
					.stateUnionTerritory(result.getString("State_Union_Territory"))
					.districtCode(result.getString("District_Code")).district(result.getString("District")).build();
		};
	}

	public List<StateMasterModel> getStateList() {
		try {
			return template.query("select * from statemaster order by State_Union_Territory", mapStateList());
		} catch (EmptyResultDataAccessException e) {
			return Collections.emptyList();
		}
	}

	private RowMapper<StateMasterModel> mapStateList() {
		return (result, i) -> {
			return StateMasterModel.builder().id(result.getLong("id")).stateCode(result.getString("State_Code"))
					.stateUnionTerritory(result.getString("State_Union_Territory")).build();
		};
	}
	
	public List<CityMasterModel> getCityListForGivenSateCode(String state_code) {
		try {
			MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource();
			sqlParameterSource.addValue("state_code", state_code);
			String sql ="SELECT distinct cm.* FROM citymaster cm \r\n"+
            " INNER JOIN `statemaster` sm ON sm.state_code = cm.state_code\r\n "+
             " WHERE cm.state_code =:state_code ORDER BY city";
			return template.query(sql,sqlParameterSource, mapCityList());
		} catch (EmptyResultDataAccessException e) {
			return Collections.emptyList();
		}
	}
	
	public List<KeyValueBean> getAllPartners(UserLoginBean userLoginBean) {
		try {
			MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource();
			String query = "select auRef.UserId id,CASE WHEN (auRef.UserType ='Franchise') THEN f.Name  \n" + 
					"	ELSE \n" + 
					"		CONCAT(auRef.`FirstName`, ' ', auRef.`LastName`)\n" + 
					"	END Name from VW_Application_Detls auRef \n" + 
					"left join franchise f on f.Id=auRef.FranchiseId \n" + 
					"where auRef.UserType in('Referral','Franchise')";
			if (AppConstant.USER_TYPE_FRANCHISE.equalsIgnoreCase(userLoginBean.getApplicationUserBean().getUserType())) {
				query = query +" and f.Id = :franchiseId";
				sqlParameterSource.addValue("franchiseId", userLoginBean.getApplicationUserBean().getFranchiseId());
			}else if (AppConstant.USER_TYPE_REFERRAL_PARTNER.equalsIgnoreCase(userLoginBean.getApplicationUserBean().getUserType())) {
				query = query +" and auRef.UserId = :userId";
				sqlParameterSource.addValue("userId", userLoginBean.getId());
			}else if (AppConstant.USER_TYPE_ADMIN.equalsIgnoreCase(userLoginBean.getApplicationUserBean().getUserType())) {
				
			}else {
				throw new ServiceException("Access denined for this user.");
			}

			query = query +" order by Name ";
			List<KeyValueBean> branches = template.query(query, sqlParameterSource, mapKeyVAlue("id", "Name"));
			return branches;
		} catch (EmptyResultDataAccessException e) {
			return new ArrayList<>();
		}
	}
	
	private RowMapper<KeyValueBean> mapKeyVAlue(String keyColName, String valColName) {
		return (result, i) -> {
			KeyValueBean keyValueBean = new KeyValueBean();
			keyValueBean.setKey(result.getString(keyColName));
			keyValueBean.setValue(result.getString(valColName));
			return keyValueBean;
		};
	}
}
