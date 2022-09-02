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

import com.assignsecurities.domain.CaseScriptModel;

import lombok.extern.slf4j.Slf4j;

@Repository
@Slf4j
public class CaseScriptRepo {
	@Autowired
	private NamedParameterJdbcTemplate template;

	public CaseScriptModel getById(Long id) {
		try {
			String query = "SELECT * FROM casescript WHERE id=:id";
			MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource();
			sqlParameterSource.addValue("id", id);
			return template.queryForObject(query, sqlParameterSource, mapObject());
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	public List<CaseScriptModel> getByCaseId(Long caseId) {
		try {
			String query = "SELECT * FROM casescript WHERE CaseId=:caseId order by ScriptId";
			MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource();
			sqlParameterSource.addValue("caseId", caseId);
			return template.query(query, sqlParameterSource, mapObject());
		} catch (EmptyResultDataAccessException e) {
			return Collections.emptyList();
		}
	}

	public Long add(CaseScriptModel model) {
		String query = "insert into `casescript` (CaseId,ScriptId,FaceValue,PrimaryCaseHolder,SecondayCaseHolder,isPrimaryCaseHolderDeceased,isSecondayCaseHolderDeceased,"
				+ "`PrimaryHolderGender`,`SecondayHolderGender`,`PrimaryHolderAge`,`SecondayHolderAge`,`PrimaryHolderFatherHusbandName`,`SecondayHolderFatherHusbandName`) "
				+ "values(:caseId,:scriptId,:faceValue,:primaryCaseHolder,:secondayCaseHolder,:isPrimaryCaseHolderDeceased,:isSecondayCaseHolderDeceased,"
				+ ":primaryHolderGender,:secondayHolderGender,:primaryHolderAge,:secondayHolderAge,:primaryHolderFatherHusbandName,:secondayHolderFatherHusbandName)";
		KeyHolder keyHolder = new GeneratedKeyHolder();
		MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource("caseId", model.getCaseId())
				.addValue("scriptId", model.getScriptId()).addValue("faceValue", model.getFaceValue())
				.addValue("primaryCaseHolder", model.getPrimaryCaseHolder())
				.addValue("secondayCaseHolder", model.getSecondayCaseHolder())
				.addValue("isPrimaryCaseHolderDeceased", model.getIsPrimaryCaseHolderDeceased())
				.addValue("isSecondayCaseHolderDeceased", model.getIsSecondayCaseHolderDeceased())
				.addValue("primaryHolderGender", model.getPrimaryHolderGender())
				.addValue("secondayHolderGender", model.getSecondayHolderGender())
				.addValue("primaryHolderAge", model.getPrimaryHolderAge())
				.addValue("secondayHolderAge", model.getSecondayHolderAge())
				.addValue("primaryHolderFatherHusbandName", model.getPrimaryHolderFatherHusbandName())
				.addValue("secondayHolderFatherHusbandName", model.getSecondayHolderFatherHusbandName());
		template.update(query, sqlParameterSource, keyHolder);
		return keyHolder.getKey().longValue();
	}

	public void addAll(List<CaseScriptModel> models) {
		String query = "insert into `casescript` (CaseId,ScriptId,FaceValue,PrimaryCaseHolder,SecondayCaseHolder,isPrimaryCaseHolderDeceased,isSecondayCaseHolderDeceased,"
				+ "`PrimaryHolderGender`,`SecondayHolderGender`,`PrimaryHolderAge`,`SecondayHolderAge`,`PrimaryHolderFatherHusbandName`,`SecondayHolderFatherHusbandName`) "
				+ "values(:caseId,:scriptId,:faceValue,:primaryCaseHolder,:secondayCaseHolder,:isPrimaryCaseHolderDeceased,:isSecondayCaseHolderDeceased,"
				+ ":primaryHolderGender,:secondayHolderGender,:primaryHolderAge,:secondayHolderAge,:primaryHolderFatherHusbandName,:secondayHolderFatherHusbandName)";
		List<Map<String, Object>> batchValues = new ArrayList<>(models.size());
		models.stream().forEach(model -> {
			batchValues.add(new MapSqlParameterSource("caseId", model.getCaseId())
					.addValue("scriptId", model.getScriptId())
					.addValue("faceValue", model.getFaceValue())
					.addValue("primaryCaseHolder", model.getPrimaryCaseHolder())
					.addValue("secondayCaseHolder", model.getSecondayCaseHolder())
					.addValue("isPrimaryCaseHolderDeceased", model.getIsPrimaryCaseHolderDeceased())
					.addValue("isSecondayCaseHolderDeceased", model.getIsSecondayCaseHolderDeceased())
					.addValue("primaryHolderGender", model.getPrimaryHolderGender())
					.addValue("secondayHolderGender", model.getSecondayHolderGender())
					.addValue("primaryHolderAge", model.getPrimaryHolderAge())
					.addValue("secondayHolderAge", model.getSecondayHolderAge())
					.addValue("primaryHolderFatherHusbandName", model.getPrimaryHolderFatherHusbandName())
					.addValue("secondayHolderFatherHusbandName", model.getSecondayHolderFatherHusbandName()).getValues());
		});
		template.batchUpdate(query, batchValues.toArray(new Map[models.size()]));
	}

	public int update(CaseScriptModel model) {
		String query = "update `casescript` set CaseId=:caseId,ScriptId=:scriptId,"
				+ " DistinctiveNoTo=FaceValue=:faceValue, PrimaryCaseHolder=:primaryCaseHolder,SecondayCaseHolder=:secondayCaseHolder,"
				+ " isPrimaryCaseHolderDeceased=:isPrimaryCaseHolderDeceased,isSecondayCaseHolderDeceased=:isPrimaryCaseHolderDeceased"
				+ " `PrimaryHolderGender`=:primaryHolderGender,`SecondayHolderGender`=:secondayHolderGender,"
				+ " `PrimaryHolderAge`=:primaryHolderAge,`SecondayHolderAge`=:secondayHolderAge,"
				+ " `PrimaryHolderFatherHusbandName`=:primaryHolderFatherHusbandName,`SecondayHolderFatherHusbandName`=:secondayHolderFatherHusbandName"
				+ " where id = :id ";
		MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource("caseId", model.getCaseId())
				.addValue("scriptId", model.getScriptId())
				.addValue("faceValue", model.getFaceValue())
				.addValue("primaryCaseHolder", model.getPrimaryCaseHolder())
				.addValue("secondayCaseHolder", model.getSecondayCaseHolder())
				.addValue("isPrimaryCaseHolderDeceased", model.getIsPrimaryCaseHolderDeceased())
				.addValue("isSecondayCaseHolderDeceased", model.getIsSecondayCaseHolderDeceased())
				.addValue("primaryHolderGender", model.getPrimaryHolderGender())
				.addValue("secondayHolderGender", model.getSecondayHolderGender())
				.addValue("primaryHolderAge", model.getPrimaryHolderAge())
				.addValue("secondayHolderAge", model.getSecondayHolderAge())
				.addValue("primaryHolderFatherHusbandName", model.getPrimaryHolderFatherHusbandName())
				.addValue("secondayHolderFatherHusbandName", model.getSecondayHolderFatherHusbandName())
				.addValue("id", model.getId());
		int updatCount = template.update(query, sqlParameterSource);
		log.info("updatCount===>" + updatCount);
		return updatCount;
	}

	public int delete(Long caseId) {
		String query = "delete from `casescript` where caseId = :caseId ";
		MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource("caseId",caseId);
		int deleteCount = template.update(query, sqlParameterSource);
		log.info("deleteCount===>" + deleteCount);
		return deleteCount;
	}
	
	public void deleteByIdAndCaseId(Long caseScriptId, Long caseId) {
		String query = "delete from  `casescript` where id=:caseScriptId and CaseId=:caseId ";
		MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource("caseScriptId", caseScriptId)
				.addValue("caseId", caseId);
		int updatCount = template.update(query, sqlParameterSource);
		log.info("deleteByIdAndCaseId===>" + updatCount);
	}
	
	public void deleteByCaseIdAndScriptId(Long caseId, Long scriptId) {
		String query = "delete from  `casescript` where scriptId=:scriptId and CaseId=:caseId ";
		MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource("scriptId", scriptId)
				.addValue("caseId", caseId);
		int updatCount = template.update(query, sqlParameterSource);
		log.info("deleteByIdAndCaseId===>" + updatCount);
	}
	
	private RowMapper<CaseScriptModel> mapObject() {
		return (result, i) -> {
			return CaseScriptModel.builder().id(result.getLong("Id")).caseId(result.getLong("CaseId"))
					.scriptId(result.getLong("ScriptId")).faceValue(result.getDouble("FaceValue"))
					.primaryCaseHolder(result.getString("PrimaryCaseHolder"))
					.secondayCaseHolder(result.getString("SecondayCaseHolder"))
					.isPrimaryCaseHolderDeceased(result.getBoolean("isPrimaryCaseHolderDeceased"))
					.isSecondayCaseHolderDeceased(result.getBoolean("isSecondayCaseHolderDeceased"))
					.primaryHolderGender(result.getString("PrimaryHolderGender"))
					.secondayHolderGender(result.getString("SecondayHolderGender"))
					.primaryHolderAge(result.getInt("PrimaryHolderAge"))
					.secondayHolderAge(result.getInt("SecondayHolderAge"))
					.primaryHolderFatherHusbandName(result.getString("PrimaryHolderFatherHusbandName"))
					.secondayHolderFatherHusbandName(result.getString("SecondayHolderFatherHusbandName")).build();
		};
	}
}
