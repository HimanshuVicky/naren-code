package com.assignsecurities.repository.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.assignsecurities.domain.ApplicationSupplementaryDocumentModel;

import lombok.extern.slf4j.Slf4j;

@Repository
@Slf4j
public class ApplicationSupplementaryDocumentRepo {
	@Autowired
	private NamedParameterJdbcTemplate template;

	public ApplicationSupplementaryDocumentModel getById(Long id) {
		try {
			String query = "SELECT * FROM applicationsupplementarydocument WHERE id=:id";
			MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource();
			sqlParameterSource.addValue("id", id);
			return template.queryForObject(query, sqlParameterSource, mapObject());
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	public List<ApplicationSupplementaryDocumentModel> getByApplicationId(Long applicationId) {
		try {
			String query = "SELECT * FROM applicationsupplementarydocument WHERE ApplicationId=:applicationId order by id";
			MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource();
			sqlParameterSource.addValue("applicationId", applicationId);
			return template.query(query, sqlParameterSource, mapObject());
		} catch (EmptyResultDataAccessException e) {
			return Collections.emptyList();
		}
	}

	public Long add(ApplicationSupplementaryDocumentModel model) {
		String query = "insert into `applicationsupplementarydocument` (`ApplicationId`,`DoccumentName`) "
				+ "values(:applicationId,:doccumentName)";
		KeyHolder keyHolder = new GeneratedKeyHolder();
		MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource("applicationId", model.getApplicationId())
				.addValue("doccumentName", model.getDoccumentName());
		template.update(query, sqlParameterSource, keyHolder);
		return keyHolder.getKey().longValue();
	}

	public void addAll(List<ApplicationSupplementaryDocumentModel> models) {
		String query = "insert into `applicationsupplementarydocument` (`ApplicationId`,`DoccumentName`) "
				+ "values(:applicationId,:doccumentName)";
		List<Map<String, Object>> batchValues = new ArrayList<>(models.size());
		models.stream().forEach(model -> {
			batchValues.add(new MapSqlParameterSource("applicationId", model.getApplicationId())
					.addValue("doccumentName", model.getDoccumentName()).getValues());
		});
		template.batchUpdate(query, batchValues.toArray(new Map[models.size()]));
	}

	public int delete(Long applicationId) {
		String query = "delete from  `applicationsupplementarydocument` where ApplicationId = :applicationId ";
		MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource("applicationId", applicationId);
		int updatCount = template.update(query, sqlParameterSource);
		log.info("updatCount===>" + updatCount);
		return updatCount;
	}

	public int update(ApplicationSupplementaryDocumentModel model) {
		String query = "update `applicationsupplementarydocument` set ApplicationId =:applicationId,DoccumentName=:doccumentName where id = :id ";
		MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource("applicationId", model.getApplicationId())
				.addValue("doccumentName", model.getDoccumentName()).addValue("id", model.getId());
		int updatCount = template.update(query, sqlParameterSource);
		log.info("updatCount===>" + updatCount);
		return updatCount;
	}

	private RowMapper<ApplicationSupplementaryDocumentModel> mapObject() {
		return (result, i) -> {
			return ApplicationSupplementaryDocumentModel.builder().id(result.getLong("Id"))
					.applicationId(result.getLong("ApplicationId")).doccumentName(result.getString("DoccumentName"))
					.build();
		};
	}
}
