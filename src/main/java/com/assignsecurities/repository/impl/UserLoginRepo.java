package com.assignsecurities.repository.impl;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.assignsecurities.bean.UserLoginBean;
import com.assignsecurities.domain.UserLogin;


@Repository
public class UserLoginRepo {
	@Autowired
	NamedParameterJdbcTemplate template;

	public List<UserLogin> getUserLogins() {
		try {
			return template.query("select * from userlogin",
					(result, rowNum) -> new UserLogin(result.getLong("id"), result.getString("mobileNo"),
							result.getString("Pin"), result.getLong("ApplicationUserId"), result.getInt("isActive")));
		} catch (EmptyResultDataAccessException e) {
			return Collections.emptyList();
		}
	}

	public UserLogin getUserLogin(Long userId) {
		String query = "SELECT * FROM userlogin WHERE ID=:userId";
		MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource();
		sqlParameterSource.addValue("userId", userId);
		try {
//			return template.queryForObject(query, sqlParameterSource,
//					new BeanPropertyRowMapper<>(UserLogin.class));
			return template.queryForObject(query, sqlParameterSource, mapObject());
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}
	
	private RowMapper<UserLogin> mapObject() {
		return (result, i) -> {
			return UserLogin.builder().id(result.getLong("id"))
//					Id,MobileNo,Pin,ApplicationUserId,IsActive
					.mobileNo(result.getString("MobileNo"))
					.applicationUserId(result.getLong("ApplicationUserId"))
					.pin(result.getString("Pin"))
					.isActive(result.getInt("IsActive"))
					.build();
		};
	}

	public UserLogin getUserLoginApplicationUserId(Long applicationUserId) {
		String query = "SELECT * FROM userlogin WHERE ApplicationUserId=:applicationUserId";
		MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource();
		sqlParameterSource.addValue("applicationUserId", applicationUserId);
		try {
			return template.queryForObject(query, sqlParameterSource, mapObject());
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	/* Adding an item into database table */
	public Long addUserLogin(UserLogin userLogin) {
		String query = "INSERT INTO userlogin (mobileNo,Pin,ApplicationUserId,isActive) VALUES(:mobileNo,:Pin,:applicationUserId,:isActive)";
		KeyHolder keyHolder = new GeneratedKeyHolder();
		MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource();
		sqlParameterSource.addValue("mobileNo", userLogin.getMobileNo());
		sqlParameterSource.addValue("Pin", userLogin.getPin());
		sqlParameterSource.addValue("applicationUserId", userLogin.getApplicationUserId());
		sqlParameterSource.addValue("isActive", userLogin.getIsActive());
		template.update(query, sqlParameterSource, keyHolder);
		return Objects.requireNonNull(keyHolder.getKey()).longValue();
	}

	public Boolean isMobileNoExists(String mobileNo) {
		try {
			String query = "SELECT mobileNo FROM userlogin WHERE MobileNo=:mobileNo";
			MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource();
			sqlParameterSource.addValue("mobileNo", mobileNo);
			String mobileNoDB = template.queryForObject(query, sqlParameterSource,
					new BeanPropertyRowMapper<>(String.class));
			if (Objects.isNull(mobileNoDB)) {
				return Boolean.FALSE;
			}
			return Boolean.TRUE;
		} catch (EmptyResultDataAccessException e) {
			return Boolean.FALSE;
		}
	}

	public UserLogin getUserLoginByMobileNo(String mobileNo) {
		String query = "SELECT * FROM userlogin WHERE MobileNo=:mobileNo";
		MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource();
		sqlParameterSource.addValue("mobileNo", mobileNo);
		try {
			return template.queryForObject(query, sqlParameterSource, mapObject());
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}
	
	public void updateUserLogin(UserLogin userLogin) {
		String query = "update userlogin set `pin`=:pin, `isActive`=:isActive where `id`=:id and `MobileNo`=:mobileNo";
		MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource();
		sqlParameterSource.addValue("mobileNo", userLogin.getMobileNo());
		sqlParameterSource.addValue("pin", userLogin.getPin());
		sqlParameterSource.addValue("id", userLogin.getId());
		sqlParameterSource.addValue("isActive", userLogin.getIsActive());
		
		template.update(query, sqlParameterSource);
	}
	
	public void addAudit(UserLoginBean userLoginBean, String ipAddress) {
		String query = "INSERT INTO user_audit (UserId,IpAddress) VALUES(:userId,:ipAddress)";
		KeyHolder keyHolder = new GeneratedKeyHolder();
		MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource();
		sqlParameterSource.addValue("userId", userLoginBean.getId());
		sqlParameterSource.addValue("ipAddress", ipAddress);
		template.update(query, sqlParameterSource, keyHolder);
	}
}
