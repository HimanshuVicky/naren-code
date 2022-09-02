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
import org.springframework.stereotype.Repository;

import com.assignsecurities.domain.CaseWarrantDetailsModel;

import lombok.extern.slf4j.Slf4j;

@Repository
@Slf4j
public class CaseWarrantDetailsRepo {
	@Autowired
	private NamedParameterJdbcTemplate template;

	public void add(CaseWarrantDetailsModel model) {
		String query = "insert into `case_script_warrant_detls` (CaseScriptId,WarrantNo,Year,Amount) "
				+ "values(:caseScriptId, :warrantNo,:year, :amount)";
		MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource("caseScriptId", model.getCaseScriptId())
				.addValue("warrantNo", model.getWarrantNo()).addValue("year", model.getYear())
				.addValue("amount", model.getAmount());
		template.update(query, sqlParameterSource);
	}

	public void addAll(List<CaseWarrantDetailsModel> models) {
		String query = "insert into `case_script_warrant_detls` (CaseScriptId,WarrantNo,Year,Amount) "
				+ "values(:caseScriptId, :warrantNo,:year, :amount)";

		List<Map<String, Object>> batchValues = new ArrayList<>();
		models.stream().forEach(model -> {
			batchValues.add(new MapSqlParameterSource("caseScriptId", model.getCaseScriptId())
					.addValue("warrantNo", model.getWarrantNo()).addValue("year", model.getYear())
					.addValue("amount", model.getAmount()).getValues());
		});
		template.batchUpdate(query, batchValues.toArray(new Map[batchValues.size()]));

	}

	public void delete(Long caseScriptId) {
		String query = "delete from `case_script_warrant_detls` where caseScriptId =:caseScriptId";
		MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource("caseScriptId", caseScriptId);
		template.update(query, sqlParameterSource);
	}
	
	public void deleteByCaseIdAndScriptId(Long caseId, Long scriptId) {
		String query = "delete from `case_script_warrant_detls`"
				+ " where CaseScriptId in(select Id from casescript where caseId=:caseId and ScriptId=:scriptId)";
		MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource("caseId", caseId)
				.addValue("scriptId", scriptId);
		template.update(query, sqlParameterSource);
	}

	public CaseWarrantDetailsModel getCaseShareCertificateDetails(Long caseScriptId) {
		try {
			String query = "SELECT * FROM case_script_warrant_detls WHERE CaseScriptId=:caseScriptId";
			MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource();
			sqlParameterSource.addValue("caseScriptId", caseScriptId);
			return template.queryForObject(query, sqlParameterSource, mapObject());
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	public List<CaseWarrantDetailsModel> getCaseShareCertificateDetailsByCaseId(Long caseId) {
		try {
			String query = "SELECT csscd.*, sc.ScriptId ScriptId FROM case_script_warrant_detls  csscd "
					+ "inner join casescript sc on csscd.CaseScriptId=sc.id WHERE sc.CaseId=:caseId";
			MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource();
			sqlParameterSource.addValue("caseId", caseId);
			return template.query(query, sqlParameterSource, mapObjectWithScriptId());
		} catch (EmptyResultDataAccessException e) {
			return Collections.emptyList();
		}
	}

	private RowMapper<CaseWarrantDetailsModel> mapObject() {
		return (result, i) -> {
			return CaseWarrantDetailsModel.builder().id(result.getLong("id"))
					.caseScriptId(result.getLong("CaseScriptId")).warrantNo(result.getString("WarrantNo"))
					.year(result.getString("Year"))
					.amount(result.getDouble("Amount")).build();
		};
	}
	private RowMapper<CaseWarrantDetailsModel> mapObjectWithScriptId() {
		return (result, i) -> {
			return CaseWarrantDetailsModel.builder().id(result.getLong("id"))
					.scriptId(result.getLong("ScriptId"))
					.caseScriptId(result.getLong("CaseScriptId")).warrantNo(result.getString("WarrantNo"))
					.year(result.getString("Year"))
					.amount(result.getDouble("Amount")).build();
		};
	}
}
