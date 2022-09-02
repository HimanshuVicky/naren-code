package com.assignsecurities.repository.impl;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.assignsecurities.domain.CaseFieldsModel;

import lombok.extern.slf4j.Slf4j;

@Repository
@Slf4j
public class CaseFieldsRepo {
	@Autowired
	private NamedParameterJdbcTemplate template;
	
	private static final String BASE_SQL = "SELECT cf.*,FieldPlaceHolder FROM casefields cf left join casefieldmaster cfm on cf.Field=cfm.Field ";

	public CaseFieldsModel getById(Long id) {
		try {
			String query =  BASE_SQL +" WHERE cf.id=:id";
			MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource();
			sqlParameterSource.addValue("id", id);
			return template.queryForObject(query, sqlParameterSource, mapObject());
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}
	
	public CaseFieldsModel getByCaseIdAndFieldName(Long caseId, String fieldName) {
		try {
			String query = BASE_SQL +" where  CaseId=:caseId and cf.Field=:fieldName";
			MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource();
			sqlParameterSource.addValue("caseId", caseId);
			sqlParameterSource.addValue("fieldName", fieldName);
			return template.queryForObject(query, sqlParameterSource, mapObject());
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	public List<CaseFieldsModel> getByCaseId(Long caseId) {
		try {
			String query = BASE_SQL +" WHERE CaseId=:caseId order by cf.Id";
			MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource();
			sqlParameterSource.addValue("caseId", caseId);
			return template.query(query, sqlParameterSource, mapObject());
		} catch (EmptyResultDataAccessException e) {
			return Collections.emptyList();
		}
	}

	public Long add(CaseFieldsModel model) {
		String query = "insert into `casefields` (CaseId,Field,`FieldValue`)  values(:caseId,:field,:fieldValue)";
		KeyHolder keyHolder = new GeneratedKeyHolder();
		MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource("caseId", model.getCaseId())
				.addValue("field", model.getField()).addValue("fieldValue", model.getFieldValue());
		template.update(query, sqlParameterSource, keyHolder);
		return keyHolder.getKey().longValue();
	}
	
	public Long update(CaseFieldsModel model) {
		String query = "update `casefields` set FieldValue=:fieldValue where  CaseId=:caseId and Field=:field ";
		MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource("caseId", model.getCaseId())
				.addValue("field", model.getField()).addValue("fieldValue", model.getFieldValue());
		long updateCount = template.update(query, sqlParameterSource);
		return updateCount;
	}

	private RowMapper<CaseFieldsModel> mapObject() {
		return (result, i) -> {
			return CaseFieldsModel.builder().id(result.getLong("Id")).caseId(result.getLong("CaseId"))
					.field(result.getString("Field"))
					.fieldValue(result.getString("FieldValue"))
					.fieldPlaceHolderKay(result.getString("FieldPlaceHolder")).build();
		};
	}
}
