package com.assignsecurities.repository.impl;

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

import com.assignsecurities.domain.DocumentModel;

@Repository
public class DocumentDao {
	@Autowired
	private NamedParameterJdbcTemplate template;

	public DocumentModel getById(Long id) {
		try {
			String query = "SELECT * FROM document WHERE id=:id";
			MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource();
			sqlParameterSource.addValue("id", id);
			return template.queryForObject(query, sqlParameterSource, mapObject());
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}
	
	public DocumentModel getByName(String name) {
		try {
			String query = "SELECT * FROM document WHERE Name=:name";
			MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource();
			sqlParameterSource.addValue("name", name);
			return template.queryForObject(query, sqlParameterSource, mapObject());
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}
	
	public Long add(DocumentModel model) {
		String query = "insert into `document` (Name,`Type`,CreatedDate,CreatedBy,BucketName,ContentType) "
				+ "values(:name,:type,:createdDate,:createdBy,:bucketName,:contentType)";
		KeyHolder keyHolder = new GeneratedKeyHolder();

		MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource("name", model.getName())
				.addValue("type", model.getType())
				.addValue("createdDate", model.getCreatedDate())
				.addValue("createdBy", model.getCreateBy())
				.addValue("bucketName", model.getBucketName())
				.addValue("contentType", model.getContentType());

		template.update(query, sqlParameterSource, keyHolder);
		return keyHolder.getKey().longValue();
	}
	
	public int deleteByIds(List<Long> docIds) {
		String query = "delete FROM document WHERE id in(:docIds)";
		MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource("docIds", docIds);
		return template.update(query, sqlParameterSource);
	}
	
	public int deleteById(Long id) {
		if(Objects.isNull( id) || id<1) {
			return 0;
		}
		String query = "delete FROM document WHERE id=:id";
		MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource("id", id);
		return template.update(query, sqlParameterSource);
	}

	
	private RowMapper<DocumentModel> mapObject() {
		return (result, i) -> {
			return DocumentModel.builder().id(result.getLong("id"))
					.name(result.getString("Name"))
					.createBy(result.getLong("CreatedBy"))
					.createdDate(Objects.isNull(result.getDate("CreatedDate")) ? null
							: result.getTimestamp("CreatedDate").toLocalDateTime())
					.type(result.getString("Type"))
					.bucketName(result.getString("BucketName"))
					.contentType(result.getString("ContentType"))
					.build();
		};
	}
}
