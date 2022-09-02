package com.assignsecurities.repository.impl;

import java.time.LocalDate;
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

import com.assignsecurities.domain.WitnessModel;

import lombok.extern.slf4j.Slf4j;

@Repository
@Slf4j
public class CaseWitnessDao {
	@Autowired
	private NamedParameterJdbcTemplate template;

	public WitnessModel getById(Long id) {
		try {
			String query = "SELECT * FROM casewitness WHERE id=:id";
			MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource();
			sqlParameterSource.addValue("id", id);
			return template.queryForObject(query, sqlParameterSource, mapObject());
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	public List<WitnessModel> getByCaseId(Long caseId) {
		try {
			String query = "SELECT * FROM casewitness WHERE CaseId=:caseId";
			MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource();
			sqlParameterSource.addValue("caseId", caseId);
			return template.query(query, sqlParameterSource, mapObject());
		} catch (EmptyResultDataAccessException e) {
			return Collections.emptyList();
		}
	}
	
	

	public Long add(WitnessModel model) {
		String query = "insert into `casewitness` (CaseId,Name,Address,City,ContactNumber,AadharNumber,PanNumber,AdharDocumentId,PanDocumentId,CreatedDate,CreateBy) "
				+ "values(:caseId,:name,:address,:city,:contactNumber,:aadharNumber,:panNumber,:adharDocumentId,:panDocumentId,:createdDate,:createBy)";
		KeyHolder keyHolder = new GeneratedKeyHolder();
		MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource("caseId", model.getCaseId())
				.addValue("name", model.getName()).addValue("address", model.getAddress())
				.addValue("city", model.getCity()).addValue("contactNumber", model.getContactNumber())
				.addValue("aadharNumber", model.getAadharNumber()).addValue("panNumber", model.getPanNumber())
				.addValue("adharDocumentId", model.getAdharDocumentId())
				.addValue("panDocumentId", model.getPanDocumentId()).addValue("createdDate", Objects.isNull(model.getCreatedDate()) ? LocalDate.now() : model.getCreatedDate())
				.addValue("createBy", model.getCreateBy());

		template.update(query, sqlParameterSource, keyHolder);
		return keyHolder.getKey().longValue();
	}

	public void addAll(List<WitnessModel> models) {
		String query = "insert into `casewitness` (CaseId,name,Address,City,ContactNumber,AadharNumber,PanNumber,AdharDocumentId,PanDocumentId,CreatedDate,CreateBy) "
				+ "values(:caseId,:name,:address,:city,:contactNumber,:aadharNumber,:panNumber,:adharDocumentId,:panDocumentId,:createdDate,:createBy)";

		List<Map<String, Object>> batchValues = new ArrayList<>(models.size());
		models.stream().forEach(model -> {
			batchValues.add(new MapSqlParameterSource("caseId", model.getCaseId())
					.addValue("name", model.getName()).addValue("address", model.getAddress())
					.addValue("city", model.getCity()).addValue("contactNumber", model.getContactNumber())
					.addValue("aadharNumber", model.getAadharNumber()).addValue("panNumber", model.getPanNumber())
					.addValue("adharDocumentId", model.getAdharDocumentId())
					.addValue("panDocumentId", model.getPanDocumentId()).addValue("createdDate", Objects.isNull(model.getCreatedDate()) ? LocalDate.now() : model.getCreatedDate())
					.addValue("createBy", model.getCreateBy()).getValues());
		});
		template.batchUpdate(query, batchValues.toArray(new Map[models.size()]));
	}

	public int update(WitnessModel model) {
		String query = "update `casewitness` set CaseId=:caseId=:,FirstName=:firstName,MiddleName=:middleName,LastName=:lastName,Address=:address,"
				+ "City=:city,ContactNumber=:contactNumber,AadharNumber=:aadharNumber,PanNumber=:panNumber,AdharDocumentId=:adharDocumentId,PanDocumentId=:panDocumentId,"
				+ "CreatedDate=:createdDate,CreateBy=:createBy" + " where id = :id ";
		MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource("caseId", model.getCaseId())
				.addValue("firstName", model.getName()).addValue("address", model.getAddress())
				.addValue("city", model.getCity()).addValue("contactNumber", model.getContactNumber())
				.addValue("aadharNumber", model.getAadharNumber()).addValue("panNumber", model.getPanNumber())
				.addValue("adharDocumentId", model.getAdharDocumentId())
				.addValue("panDocumentId", model.getPanDocumentId()).addValue("id", model.getId());
		int updatCount = template.update(query, sqlParameterSource);
		log.info("updatCount===>" + updatCount);
		return updatCount;
	}
	//,PanDocumentId=:panDocumentId
	public int updateAdharDocumentId(Long adharDocumentId, Long oldAdharDocumentId, Long caseId) {
		String query = "update `casewitness` set AdharDocumentId=:adharDocumentId where AdharDocumentId = :oldAdharDocumentId and CaseId=:caseId";
		MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource("caseId", caseId)
				.addValue("oldAdharDocumentId", oldAdharDocumentId).addValue("adharDocumentId", adharDocumentId);
		int updatCount = template.update(query, sqlParameterSource);
		log.info("updatCount===>" + updatCount);
		return updatCount;
	}
	
	public int updatePanDocumentId(Long panDocumentId, Long oldPanDocumentId, Long caseId) {
		String query = "update `casewitness` set PanDocumentId=:panDocumentId where PanDocumentId = :oldPanDocumentId and CaseId=:caseId";
		MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource("caseId", caseId)
				.addValue("oldPanDocumentId", oldPanDocumentId).addValue("panDocumentId", panDocumentId);
		int updatCount = template.update(query, sqlParameterSource);
		log.info("updatCount===>" + updatCount);
		return updatCount;
	}
	
	public int deleteCaseById(Long caseId) {
		String query = "delete FROM casewitness WHERE CaseId=:caseId";
		MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource();
		sqlParameterSource.addValue("caseId", caseId);
		int updatCount = template.update(query, sqlParameterSource);
		log.info("updatCount===>" + updatCount);
		return updatCount;
	}

	private RowMapper<WitnessModel> mapObject() {
		return (result, i) -> {
			return WitnessModel.builder().id(result.getLong("Id")).caseId(result.getLong("CaseId"))
					.name(result.getString("Name")).address(result.getString("address"))
					.city(result.getString("city")).contactNumber(result.getString("contactNumber"))
					.aadharNumber(result.getString("AadharNumber")).panNumber(result.getString("PanNumber"))
					.adharDocumentId(result.getLong("AdharDocumentId")).panDocumentId(result.getLong("PanDocumentId"))
					.createdDate(Objects.isNull(result.getDate("createdDate")) ? null
							: result.getDate("createdDate").toLocalDate())
					.createBy(result.getLong("createBy")).build();
		};
	}
}
