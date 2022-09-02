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

import com.assignsecurities.domain.ESignDocumentModel;

import lombok.extern.slf4j.Slf4j;

@Repository
@Slf4j
public class ESignDocumentRepo {
	@Autowired
	private NamedParameterJdbcTemplate template;
	
	public ESignDocumentModel getByUserId(Long userId) {
		String query = "select * from esign_document WHERE UserId=:userId";
		MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource();
		sqlParameterSource.addValue("userId", userId);
		try {
			return template.queryForObject(query, sqlParameterSource, mapAddress());
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}
	
	public ESignDocumentModel getByUserIdAndType(Long userId, String userType) {
		String query = "select ed.* from esign_document ed inner join userlogin ul on ed.UserId=ul.id " + 
				"inner join applicationuser au on au.id=ul.ApplicationUserId " + 
				" WHERE UserId=:userId and au.UserType=:userType order by Id desc limit 1";
		MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource();
		sqlParameterSource.addValue("userId", userId);
		sqlParameterSource.addValue("userType", userType);
		try {
			return template.queryForObject(query, sqlParameterSource, mapAddress());
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	private RowMapper<ESignDocumentModel> mapAddress() {
		return (result, i) -> {
			return ESignDocumentModel.builder().Id(result.getLong("Id"))
					.userId(result.getLong("UserId"))
					.documentId(result.getLong("DocumentId"))
					.createdDate(Objects.isNull(result.getDate("CreatedDate")) ? null
							: result.getTimestamp("CreatedDate").toLocalDateTime())
					.build();
		};
	}

	public Long add(ESignDocumentModel model) {
		KeyHolder keyHolderAddress = new GeneratedKeyHolder();
		MapSqlParameterSource sqlParameterSourceaddress = new MapSqlParameterSource();
		sqlParameterSourceaddress.addValue("userId", model.getUserId());
		sqlParameterSourceaddress.addValue("documentId", model.getDocumentId());
		
		
		
		String queryaddress = "INSERT INTO esign_document (UserId,DocumentId) "
				+ "VALUES(:userId,:documentId)";
		template.update(queryaddress, sqlParameterSourceaddress, keyHolderAddress);
		Long addressId = keyHolderAddress.getKey().longValue();
		return addressId;
	}
	
	public int deleteByDocumentId(Long documentId) {
		if(Objects.isNull( documentId) || documentId<1) {
			return 0;
		}
		String query = "delete FROM esign_document WHERE DocumentId=:documentId";
		MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource("documentId", documentId);
		return template.update(query, sqlParameterSource);
	}
}
