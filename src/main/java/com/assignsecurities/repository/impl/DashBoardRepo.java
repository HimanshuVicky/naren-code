package com.assignsecurities.repository.impl;

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
import com.assignsecurities.bean.UserLoginBean;
import com.assignsecurities.domain.DashBoardCountKayValue;

@Repository
public class DashBoardRepo {
	@Autowired
	private NamedParameterJdbcTemplate template;

	public List<DashBoardCountKayValue> getMyApplicationCount(UserLoginBean userLoginBean, List<String> statuses) {
		try {
			MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource();
			sqlParameterSource.addValue("userId", userLoginBean.getId());
			sqlParameterSource.addValue("statuses", statuses);
			String sql = "";
			if (AppConstant.USER_TYPE_END_USER.equalsIgnoreCase(userLoginBean.getApplicationUserBean().getUserType())) {
//				sql = sql +" select \"Favourite Scripts\" Kay,count(s.id) Total from script s inner join myfavourte m on s.Id =m.ScriptId where m.UserId =:userId "
//						+ " union ";
				sql = sql +" select Status Kay,count(a.id) Total from application a where a.Status in (:statuses) and a.UserId =:userId  group by Status";
			}
			else 
			{
				sql = " select Status Kay,count(a.id) Total from application a where a.Status in (:statuses) group by Status";
			}
				return template.query(sql,
					sqlParameterSource, mapObject());
		} catch (EmptyResultDataAccessException e) {
			return Collections.emptyList();
		}
	}

	public List<DashBoardCountKayValue> getMyFavouriteCount(UserLoginBean userLoginBean) {
		try {
			MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource();
			sqlParameterSource.addValue("userId", userLoginBean.getId());
			String sql = "";
			if (AppConstant.USER_TYPE_END_USER.equalsIgnoreCase(userLoginBean.getApplicationUserBean().getUserType())) {
				sql = sql +" select \"Favourite Scripts\" Kay,count(s.id) Total from script s inner join myfavourte m on s.Id =m.ScriptId where m.UserId =:userId ";
			}
			
				return template.query(sql,
					sqlParameterSource, mapObject());
		} catch (EmptyResultDataAccessException e) {
			return Collections.emptyList();
		}
	}
	
//	select 'My Cases' Kay,count(c.id) Total from  `case` c where (c.UserId=1 or FranchiseId=1) and c.Status in('New')
//	union
//	select  Status Kay,count(c.id) Total from  `case` c where (c.UserId=1 or FranchiseId=1) and c.Status Not in('New') and c.Status in('New') group by Status

	public List<DashBoardCountKayValue> getMyCasesCount(UserLoginBean userLoginBean) {
		try {
			MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource();
			sqlParameterSource.addValue("userId", userLoginBean.getId());
			sqlParameterSource.addValue("franchiseId", userLoginBean.getApplicationUserBean().getFranchiseId());
			String sql = "select Stage Kay,count(c.id) Total from `case` c  inner join statusmaster sm  on c.status=sm.Status ";
			if (AppConstant.USER_TYPE_END_USER.equalsIgnoreCase(userLoginBean.getApplicationUserBean().getUserType())) {
//				Customer
				sql = sql + " where `Customer` <>'NA'";
				sql = sql +" and c.UserId=:userId ";
			}else if (AppConstant.USER_TYPE_CC.equalsIgnoreCase(userLoginBean.getApplicationUserBean().getUserType())) {
//				CustomerCare
				sql = sql + " where `CustomerCare` <>'NA'";
			}else if (AppConstant.USER_TYPE_ADMIN.equalsIgnoreCase(userLoginBean.getApplicationUserBean().getUserType())) {
//				Admin
				sql = sql + " where `Admin` <> 'NA'";
			}else if (AppConstant.USER_TYPE_FRANCHISE.equalsIgnoreCase(userLoginBean.getApplicationUserBean().getUserType())) {
//				FranchiseOwner
				sql = sql + " where `FranchiseOwner` <>'NA'";
				sql = sql +" and (FranchiseId=:franchiseId)";
			}else if (AppConstant.USER_TYPE_FRANCHISE_USER.equalsIgnoreCase(userLoginBean.getApplicationUserBean().getUserType())) {
//				Franchise User
				sql = sql + " where `Franchise User` <>'NA'";
				sql = sql +" and (FranchiseId=:franchiseId)";
			}else if (AppConstant.USER_TYPE_ADVOCATE.equalsIgnoreCase(userLoginBean.getApplicationUserBean().getUserType())) {
//				Lawyer
				sql = sql + " where `Lawyer` <>'NA'";
				sql = sql +" and (AssignLawyerId=:userId)";
			}
			else if (AppConstant.USER_TYPE_NOTARYL_PARTNER.equalsIgnoreCase(userLoginBean.getApplicationUserBean().getUserType())) {
//				Notary
				sql = sql + "";
				sql = sql +" where NotaryPartnerId=:userId";
			}else if (AppConstant.USER_TYPE_CHARTERED_ACCOUNTANT_PARTNER.equalsIgnoreCase(userLoginBean.getApplicationUserBean().getUserType())) {
//				CA
				
				sql = sql + "";
				sql = sql +" where CharteredAccountantId=:userId";
			}
			else {
				throw new ServiceException("Not supported by this current user type.");
			}
			sql = sql + " group by Stage order by Stage";
			return template.query(sql,
					sqlParameterSource, mapObject());
		} catch (EmptyResultDataAccessException e) {
			return Collections.emptyList();
		}
	}
	
	
	private RowMapper<DashBoardCountKayValue> mapObject() {
		return (result, i) -> {
			return DashBoardCountKayValue.builder().key(result.getString("Kay")).count(result.getLong("Total")).build();
		};
	}
}
