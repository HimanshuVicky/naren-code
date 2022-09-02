package com.assignsecurities.repository.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.assignsecurities.domain.CaseFeeModel;

import lombok.extern.slf4j.Slf4j;

@Repository
@Slf4j
public class CaseFeeDao {
	@Autowired
	private NamedParameterJdbcTemplate template;

	public CaseFeeModel getById(Long id) {
		try {
			String query = "SELECT * FROM casefee WHERE id=:id";
			MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource();
			sqlParameterSource.addValue("id", id);
			return template.queryForObject(query, sqlParameterSource, mapObject());
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	public CaseFeeModel getByCaseId(Long caseId, String templateType) {
		try {
			String query = "SELECT * FROM casefee WHERE CaseId=:caseId and TemplateType=:templateType";
			MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource();
			sqlParameterSource.addValue("caseId", caseId);
			sqlParameterSource.addValue("templateType", templateType);
			return template.queryForObject(query, sqlParameterSource, mapObject());
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}
	
	public CaseFeeModel getByCaseIdAndFeeFor(Long caseId, String feeFor) {
		try {
			String query = "SELECT * FROM casefee WHERE CaseId=:caseId and FeeFor=:feeFor";
			MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource();
			sqlParameterSource.addValue("caseId", caseId);
			sqlParameterSource.addValue("feeFor", feeFor);
			return template.queryForObject(query, sqlParameterSource, mapObject());
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	public List<CaseFeeModel> getByCaseId(Long caseId) {
		try {
//			String query = "SELECT * FROM casefee WHERE CaseId=:caseId";
			String query = "select cf.* from casefee cf inner join feemaster fm  on cf.FeeFor=fm.FeeFor "
					+ "where cf.CaseId=:caseId order by TempOrder";
			MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource();
			sqlParameterSource.addValue("caseId", caseId);
			return template.query(query, sqlParameterSource, mapObject());
		} catch (EmptyResultDataAccessException e) {
			return Collections.emptyList();
		}
	}

	public Long add(CaseFeeModel model) {
		String query = "insert into `casefee` (CaseId,TemplateType,FeeFor,FeeType,FeeValue,RefNo,isGSTApplicable,ReceivedFeeValue) "
				+ "values(:caseId,:templateType,:feeFor,:feeType,:feeValue,:refNo,:isGSTApplicable,:receivedFeeValue)";
		KeyHolder keyHolder = new GeneratedKeyHolder();
		MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource("caseId", model.getCaseId())
				.addValue("templateType", model.getTemplateType()).addValue("FeeFor", model.getFeeFor())
				.addValue("FeeType", model.getFeeType()).addValue("feeValue", Objects.isNull(model.getFeeValue()) ? 0: model.getFeeValue())
				.addValue("isGSTApplicable", model.getIsGSTApplicable()).addValue("receivedFeeValue", model.getReceivedFeeValue());

		template.update(query, sqlParameterSource, keyHolder);
		return keyHolder.getKey().longValue();
	}

	public void addAll(List<CaseFeeModel> models) {
		String query = "insert into `casefee` (CaseId,TemplateType,FeeFor,FeeType,FeeValue,RefNo,isGSTApplicable,ReceivedFeeValue) "
				+ "values(:caseId,:templateType,:feeFor,:feeType,:feeValue,:refNo,:isGSTApplicable,:receivedFeeValue)";

		List<Map<String, Object>> batchValues = new ArrayList<>(models.size());
		models.stream().forEach(model -> {
			batchValues.add(new MapSqlParameterSource("caseId", model.getCaseId())
					.addValue("templateType", model.getTemplateType()).addValue("feeFor", model.getFeeFor())
					.addValue("feeType", model.getFeeType()).addValue("feeValue", Objects.isNull(model.getFeeValue()) ? 0: model.getFeeValue()).addValue("refNo", model.getRefNo())
					.addValue("isGSTApplicable", model.getIsGSTApplicable()).addValue("receivedFeeValue", model.getReceivedFeeValue()).getValues());
		});
		template.batchUpdate(query, batchValues.toArray(new Map[models.size()]));
	}
	
	public void updateAll(List<CaseFeeModel> models) {
		String query = "update `casefee` set CaseId=:caseId,TemplateType=:templateType,FeeFor=:feeFor,FeeType=:feeType,FeeValue=:feeValue,"
				+ " RefNo=:refNo,isGSTApplicable=:isGSTApplicable,ReceivedFeeValue=:receivedFeeValue where Id=:id";

		List<Map<String, Object>> batchValues = new ArrayList<>(models.size());
		models.stream().forEach(model -> {
			batchValues.add(new MapSqlParameterSource("caseId", model.getCaseId()).addValue("id", model.getId())
					.addValue("templateType", model.getTemplateType()).addValue("feeFor", model.getFeeFor())
					.addValue("feeType", model.getFeeType()).addValue("feeValue", Objects.isNull(model.getFeeValue()) ? 0: model.getFeeValue()).addValue("refNo", model.getRefNo())
					.addValue("isGSTApplicable", model.getIsGSTApplicable()).addValue("receivedFeeValue", model.getReceivedFeeValue()).getValues());
		});
		template.batchUpdate(query, batchValues.toArray(new Map[models.size()]));
	}
	

	public int update(CaseFeeModel model) {
		String query = "update `casefee` set CaseId=:caseId=:,TemplateType=:templateType,FeeFor=:feeFor,"
				+ "FeeType=:feeType,FeeValue=:feeValue,RefNo=:refNo,isGSTApplicable=:isGSTApplicable,ReceivedFeeValue=:receivedFeeValue where id = :id ";
		MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource("caseId", model.getCaseId())
				.addValue("templateType", model.getTemplateType()).addValue("feeFor", model.getFeeFor())
				.addValue("feeType", model.getFeeType()).addValue("feeValue", Objects.isNull(model.getFeeValue()) ? 0: model.getFeeValue()).addValue("refNo", model.getRefNo())
				.addValue("isGSTApplicable", model.getIsGSTApplicable()).addValue("receivedFeeValue", model.getReceivedFeeValue())
				.addValue("id", model.getId());
		int updatCount = template.update(query, sqlParameterSource);
		log.info("updatCount===>" + updatCount);
		return updatCount;
	}

	public int delete(Long caseId) {
		String query = "delete FROM casefee WHERE CaseId=:caseId";
		MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource();
		sqlParameterSource.addValue("caseId", caseId);
		int updatCount = template.update(query, sqlParameterSource);
		log.info("updatCount===>" + updatCount);
		return updatCount;
	}
	
	private RowMapper<CaseFeeModel> mapObject() {
		return (result, i) -> {
			return CaseFeeModel.builder().id(result.getLong("Id")).caseId(result.getLong("CaseId"))
					.templateType(result.getString("TemplateType")).feeFor(result.getString("FeeFor"))
					.feeType(result.getString("FeeType")).feeValue(result.getDouble("FeeValue")).refNo(result.getString("RefNo"))
					.isGSTApplicable(result.getBoolean("isGSTApplicable")).receivedFeeValue(result.getDouble("ReceivedFeeValue")).build();
		};
	}
	
	
	public List<CaseFeeModel> getByCaseIdAndUserTypeId(Long caseId, Long userTypeId) {
		try {
//			String query = "SELECT * FROM casefee WHERE CaseId=:caseId";
			String query = "select cf.* from casefee cf inner join feemaster fm  on cf.FeeFor=fm.FeeFor "
					+ "where cf.CaseId=:caseId AND fm.usertypeId=:userTypeId order by TempOrder";
			MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource();
			sqlParameterSource.addValue("caseId", caseId);
			sqlParameterSource.addValue("userTypeId", userTypeId);
			
			return template.query(query, sqlParameterSource, mapObject());
		} catch (EmptyResultDataAccessException e) {
			return Collections.emptyList();
		}
	}
}
