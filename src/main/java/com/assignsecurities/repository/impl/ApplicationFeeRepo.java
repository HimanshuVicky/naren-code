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

import com.assignsecurities.domain.ApplicationFeeModel;

import lombok.extern.slf4j.Slf4j;

@Repository
@Slf4j
public class ApplicationFeeRepo {
	@Autowired
	private NamedParameterJdbcTemplate template;

	public ApplicationFeeModel getById(Long id) {
		try {
			String query = "SELECT * FROM applicationfee WHERE id=:id";
			MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource();
			sqlParameterSource.addValue("id", id);
			return template.queryForObject(query, sqlParameterSource, mapObject());
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	public List<ApplicationFeeModel> getByApplicationId(Long applicationId) {
		try {
			String query = "SELECT af.* FROM applicationfee af  " + 
					"inner join  feemaster fm on af.FeeFor=fm.FeeFor " + 
					" WHERE ApplicationId=:applicationId  order by TempOrder";
			MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource();
			sqlParameterSource.addValue("applicationId", applicationId);
			return template.query(query, sqlParameterSource, mapObject());
		} catch (EmptyResultDataAccessException e) {
			return Collections.emptyList();
		}
	}

	public Long add(ApplicationFeeModel model) {
		String query = "insert into `applicationfee` (`ApplicationId`,`FeeFor`, `FeeType`,`FeeValue`,`isGSTApplicable`) "
				+ "values(:applicationId,:feeFor, :feeType,:feeValue,:isGSTApplicable)";
		KeyHolder keyHolder = new GeneratedKeyHolder();
		MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource("applicationId", model.getApplicationId())
				.addValue("feeFor", model.getFeeFor()).addValue("feeType", model.getFeeType())
				.addValue("feeValue", model.getFeeValue()).addValue("isGSTApplicable", model.getIsGSTApplicable());

		template.update(query, sqlParameterSource, keyHolder);
		return keyHolder.getKey().longValue();
	}

	public void addAll(List<ApplicationFeeModel> models) {
		String query = "insert into `applicationfee` (`ApplicationId`,`FeeFor`, `FeeType`,`FeeValue`,`isGSTApplicable`) "
				+ "values(:applicationId,:feeFor, :feeType,:feeValue,:isGSTApplicable)";

		List<Map<String, Object>> batchValues = new ArrayList<>(models.size());
		models.stream().forEach(model -> {
			batchValues.add( new MapSqlParameterSource("applicationId", model.getApplicationId())
					.addValue("feeFor", model.getFeeFor()).addValue("feeType", model.getFeeType())
					.addValue("feeValue", model.getFeeValue()).addValue("isGSTApplicable", model.getIsGSTApplicable())
					.getValues());
		});
		template.batchUpdate(query, batchValues.toArray(new Map[models.size()]));
	}
	
	public int delete(Long applicationId) {
		String query = "delete from  `applicationfee` where ApplicationId = :applicationId ";
		MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource("applicationId", applicationId);
		int updatCount = template.update(query, sqlParameterSource);
		log.info("updatCount===>" + updatCount);
		return updatCount;
	}


	public int update(ApplicationFeeModel model) {
		String query = "update `applicationfee` set ApplicationId =:applicationId,FeeFor=feeFor, FeeType=:feeType,FeeValue=:feeValue,isGSTApplicable=:isGSTApplicable"
				+ " where id = :id ";
		MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource("applicationId", model.getApplicationId())
				.addValue("feeFor", model.getFeeFor()).addValue("feeType", model.getFeeType())
				.addValue("feeValue", model.getFeeValue()).addValue("isGSTApplicable", model.getIsGSTApplicable())
				.addValue("id", model.getId());
		int updatCount = template.update(query, sqlParameterSource);
		log.info("updatCount===>" + updatCount);
		return updatCount;
	}

	private RowMapper<ApplicationFeeModel> mapObject() {
		return (result, i) -> {
			return ApplicationFeeModel.builder().id(result.getLong("Id")).applicationId(result.getLong("ApplicationId"))
					.feeFor(result.getString("FeeFor")).feeType(result.getString("FeeType"))
					.feeValue(result.getDouble("FeeValue")).isGSTApplicable(result.getBoolean("isGSTApplicable"))
					.build();
		};
	}
}
