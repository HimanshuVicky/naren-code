package com.assignsecurities.repository.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.assignsecurities.domain.SecuritySession;


@Repository
public class SecuritySessionRepo {
	@Autowired
	NamedParameterJdbcTemplate template;

	public SecuritySession getSecuritySession(String sessionId) {
		try {
			String query = "SELECT * FROM securitysession WHERE SessionId=:SessionId";
			MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource();
			sqlParameterSource.addValue("SessionId", sessionId);
			SecuritySession item = template.queryForObject(query, sqlParameterSource,
					new BeanPropertyRowMapper<>(SecuritySession.class));
			return item;
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	/* Adding an item into database table */
	public Integer addSecuritySession(SecuritySession securitySessionBean) {
	
		String deleteQuery = "delete from securitysession where UserId=:userId";
		MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource();
		sqlParameterSource.addValue("userId", securitySessionBean.getUserId());
//		template.update(deleteQuery, sqlParameterSource);
		
		String queryInsert = "INSERT INTO securitysession (SessionId,UserId,ValidityPeriod) VALUES(:sessionId,:userId,:validityPeriod)";
		KeyHolder keyHolder = new GeneratedKeyHolder();
		sqlParameterSource = new MapSqlParameterSource();
		sqlParameterSource.addValue("sessionId", securitySessionBean.getSessionId());
		sqlParameterSource.addValue("userId", securitySessionBean.getUserId());
		sqlParameterSource.addValue("validityPeriod", securitySessionBean.getValidityPeriod());
		template.update(queryInsert, sqlParameterSource, keyHolder);
		return keyHolder.getKey().intValue();
	}
	
	public Integer deleteExpiredSEcuritySEssion(Integer deletionMinutes) {
		
		String deleteQuery = "delete from securitysession where TIMESTAMPDIFF(MINUTE,DateCreated,NOW()) >= :deletionMinutes";
		MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource();
		sqlParameterSource.addValue("deletionMinutes", deletionMinutes);
		return template.update(deleteQuery, sqlParameterSource);
	}
	
	public Integer deleteSecuritySEssion(Long userId) {
		String deleteQuery = "delete from securitysession where UserId= :userId";
		MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource();
		sqlParameterSource.addValue("userId", userId);
		return template.update(deleteQuery, sqlParameterSource);
	}
}
