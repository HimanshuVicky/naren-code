package com.assignsecurities.repository.impl;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.assignsecurities.domain.QuestionerModel;

import lombok.extern.slf4j.Slf4j;

@Repository
@Slf4j
public class QuestionerRepo {

	@Autowired
	private NamedParameterJdbcTemplate template;

	public List<QuestionerModel> getQuestioners() {
		try {
			return template.query("select * from questioner", mapObject());
		} catch (EmptyResultDataAccessException e) {
			return Collections.emptyList();
		}
	}
	public List<QuestionerModel> getActiveQuestioners() {
		try {
			return template.query("select * from questioner where IsActive=1", mapObject());
		} catch (EmptyResultDataAccessException e) {
			return Collections.emptyList();
		}
	}
	private RowMapper<QuestionerModel> mapObject() {
		return (result, i) -> {
			return QuestionerModel.builder().id(result.getLong("id"))
					.question(result.getString("Question"))
					.isActive(result.getBoolean("IsActive")).build();
		};
	}
}
