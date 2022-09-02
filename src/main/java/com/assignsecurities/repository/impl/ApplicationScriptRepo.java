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

import com.assignsecurities.domain.ApplicationScriptModel;

import lombok.extern.slf4j.Slf4j;

@Repository
@Slf4j
public class ApplicationScriptRepo {

	@Autowired
	private NamedParameterJdbcTemplate template;

	public ApplicationScriptModel getById(Long id) {
		try {
			String query = "SELECT * FROM applicationscript WHERE id=:id";
			MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource();
			sqlParameterSource.addValue("id", id);
			return template.queryForObject(query, sqlParameterSource, mapObject());
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}
	
	public List<ApplicationScriptModel> getByScriptIds(List<Long> scriptIds) {
		try {
			String query = "SELECT * FROM applicationscript WHERE ScriptId in(:scriptIds)";
			MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource();
			sqlParameterSource.addValue("scriptIds", scriptIds);
			return template.query(query, sqlParameterSource, mapObject());
		} catch (EmptyResultDataAccessException e) {
			return Collections.emptyList();
		}
	}
	
	public List<ApplicationScriptModel> getByScriptByApplicationId(Long applicationId) {
		try {
			String query = "SELECT * FROM applicationscript WHERE ApplicationId = :applicationId";
			MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource();
			sqlParameterSource.addValue("applicationId", applicationId);
			return template.query(query, sqlParameterSource, mapObject());
		} catch (EmptyResultDataAccessException e) {
			return Collections.emptyList();
		}
	}

	public Long add(ApplicationScriptModel model) {
		String query = "insert into `applicationscript` (ApplicationId,ScriptId) " + "values(:applicationId,:scriptId)";
		KeyHolder keyHolder = new GeneratedKeyHolder();
		
		MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource("applicationId", model.getApplicationId())
				.addValue("scriptId", model.getScriptId());
		template.update(query, sqlParameterSource, keyHolder);
		return keyHolder.getKey().longValue();
	}
	
	
	public void addAll(List<ApplicationScriptModel> applicationScriptModels) {
		String query = "insert into `applicationscript` (ApplicationId,ScriptId) " + "values(:applicationId,:scriptId)";

		List<Map<String, Object>> batchValues = new ArrayList<>(applicationScriptModels.size());
		applicationScriptModels.stream().forEach(model -> {
			batchValues.add(new MapSqlParameterSource("applicationId", model.getApplicationId())
					.addValue("scriptId", model.getScriptId()).getValues());
		});
		template.batchUpdate(query, batchValues.toArray(new Map[applicationScriptModels.size()]));
	}

	public void update(ApplicationScriptModel model) {
		String query = "update `applicationscript` set ApplicationId=:applicationId,ScriptId=:scriptId where Id=:id ";
		MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource("applicationId", model.getApplicationId())
				.addValue("scriptId", model.getScriptId()).addValue("id", model.getId());
		int updatCount = template.update(query, sqlParameterSource);
		log.info("updatCount===>" + updatCount);
	}

	public void delete(Long applicationId) {
		String query = "delete from  `applicationscript` where ApplicationId=:applicationId ";
		MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource("applicationId", applicationId);
		int updatCount = template.update(query, sqlParameterSource);
		log.info("deleteCount===>" + updatCount);
	}

	public void deleteByIdAndApplicationId(Long applicationScriptId, Long applicationId) {
		String query = "delete from  `applicationscript` where id=:applicationScriptId and ApplicationId=:applicationId ";
		MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource("applicationScriptId", applicationScriptId)
				.addValue("applicationId", applicationId);
		int updatCount = template.update(query, sqlParameterSource);
		log.info("deleteByApplicationIdAndScriptIdCount===>" + updatCount);
	}
	
	private RowMapper<ApplicationScriptModel> mapObject() {
		return (result, i) -> {
			return ApplicationScriptModel.builder().id(result.getLong("id"))
					.applicationId(result.getLong("ApplicationId")).scriptId(result.getLong("ScriptId")).build();
		};
	}
}
