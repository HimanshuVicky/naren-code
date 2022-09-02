package com.assignsecurities.repository.impl;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.assignsecurities.domain.RtaTemplateModel;

import lombok.extern.slf4j.Slf4j;

@Repository
@Slf4j
public class RtaTemplatesDao {
	@Autowired
	private NamedParameterJdbcTemplate template;

	public RtaTemplateModel getRtaTemplate(String rtaName, String templateType) {
		log.info(String.format("rtaName===>%s , templateType ==> %s", rtaName, templateType));
		try {
			String query = "SELECT rt.* FROM rtatemplates rt inner join rta_templates_group rtg  on rt.Name = rtg.RtaGroup  "
					+ "where rtg.RtaName=:rtaName and TemplateType = :templateType ";
			MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource();
			sqlParameterSource.addValue("rtaName", rtaName);
			sqlParameterSource.addValue("templateType", templateType);
			return template.queryForObject(query, sqlParameterSource, mapObject());
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}
	
	public List<RtaTemplateModel> getRtaTemplateS(String rtaName) {
		log.info(String.format("rtaName===>%s", rtaName));
		try {
			String query = "SELECT rt.* FROM rtatemplates rt inner join rta_templates_group rtg  on rt.Name = rtg.RtaGroup  "
					+ "where rtg.RtaName=:rtaName ";
			MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource();
			sqlParameterSource.addValue("rtaName", rtaName);
			return template.query(query, sqlParameterSource, mapObject());
		} catch (EmptyResultDataAccessException e) {
			return Collections.emptyList();
		}
	}


	private RowMapper<RtaTemplateModel> mapObject() {
		return (result, i) -> {
			return RtaTemplateModel.builder().id(result.getLong("id")).name(result.getString("name"))
					.templateType(result.getString("TemplateType")).templateName(result.getString("TemplateName"))
					.isFeeRequired(result.getBoolean("IsFeeRequired"))
					.build();
		};
	}
}
