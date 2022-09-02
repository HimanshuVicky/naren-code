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

import com.assignsecurities.domain.SuretyInfoModel;

import lombok.extern.slf4j.Slf4j;

@Repository
@Slf4j
public class CaseSuretyInfoDao {
	@Autowired
	private NamedParameterJdbcTemplate template;

	public SuretyInfoModel getById(Long id) {
		try {
			String query = "SELECT * FROM caseasuretyinfo WHERE id=:id";
			MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource();
			sqlParameterSource.addValue("id", id);
			return template.queryForObject(query, sqlParameterSource, mapObject());
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	public List<SuretyInfoModel> getByCaseId(Long caseId) {
		try {
			String query = "SELECT * FROM caseasuretyinfo WHERE CaseId=:caseId";
			MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource();
			sqlParameterSource.addValue("caseId", caseId);
			return template.query(query, sqlParameterSource, mapObject());
		} catch (EmptyResultDataAccessException e) {
			return Collections.emptyList();
		}
	}

	public Long add(SuretyInfoModel model) {
		String query = "insert into `caseasuretyinfo` (CaseId,Name,Address,City,AadharNo,Phone,PanNo,AadharDocId,PanDocId,ITRRefNo, ITRDocumentId,CreatedDate,CreateBy) "
				+ "values(:caseId,:name,:address,:city,:aadharNumber,:phone,:panNumber,:adharDocumentId,:panDocumentId,:itrRefNo,:itrDocumentId,:createdDate,:createBy)";
		KeyHolder keyHolder = new GeneratedKeyHolder();
		MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource("caseId", model.getCaseId())
				.addValue("name", model.getName()).addValue("address", model.getAddress())
				.addValue("city", model.getCity()).addValue("phone", model.getPhone())
				.addValue("aadharNumber", model.getAadharNumber()).addValue("panNumber", model.getPanNumber())
				.addValue("adharDocumentId", model.getAdharDocumentId())
				.addValue("panDocumentId", model.getPanDocumentId())
				.addValue("itrRefNo", model.getItrRefNo())
				.addValue("itrDocumentId", model.getItrDocumentId())
				.addValue("createdDate", model.getCreatedDate())
				.addValue("createBy", model.getCreateBy());
		template.update(query, sqlParameterSource, keyHolder);
		return keyHolder.getKey().longValue();
	}

	public void addAll(List<SuretyInfoModel> models) {
		String query = "insert into `caseasuretyinfo` (CaseId,Name,Address,City,AadharNo,Phone,PanNo,AadharDocId,PanDocId,ITRRefNo, ITRDocumentId,CreatedDate,CreateBy) "
				+ "values(:caseId,:name,:address,:city,:aadharNumber,:phone,:panNumber,:adharDocumentId,:panDocumentId,:itrRefNo,:itrDocumentId,:createdDate,:createBy)";
		List<Map<String, Object>> batchValues = new ArrayList<>(models.size());
		models.stream().forEach(model -> {
			batchValues.add(new MapSqlParameterSource("caseId", model.getCaseId())
					.addValue("name", model.getName()).addValue("address", model.getAddress())
					.addValue("city", model.getCity()).addValue("phone", model.getPhone())
					.addValue("aadharNumber", model.getAadharNumber()).addValue("panNumber", model.getPanNumber())
					.addValue("adharDocumentId", model.getAdharDocumentId())
					.addValue("panDocumentId", model.getPanDocumentId())
					.addValue("itrRefNo", model.getItrRefNo())
					.addValue("itrDocumentId", model.getItrDocumentId())
					.addValue("createdDate", model.getCreatedDate())
					.addValue("createBy", model.getCreateBy()).getValues());
		});
		template.batchUpdate(query, batchValues.toArray(new Map[models.size()]));
	}
	
	public int updateAdharDocumentId(Long adharDocumentId, Long oldAdharDocumentId, Long caseId) {
		String query = "update `caseasuretyinfo` set AadharDocId=:adharDocumentId where AadharDocId = :oldAdharDocumentId and CaseId=:caseId";
		MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource("caseId", caseId)
				.addValue("oldAdharDocumentId", oldAdharDocumentId).addValue("adharDocumentId", adharDocumentId);
		int updatCount = template.update(query, sqlParameterSource);
		log.info("updatCount===>" + updatCount);
		return updatCount;
	}
	
	public int updatePanDocumentId(Long panDocumentId, Long oldPanDocumentId, Long caseId) {
		String query = "update `caseasuretyinfo` set PanDocId=:panDocumentId where PanDocId = :oldPanDocumentId and CaseId=:caseId";
		MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource("caseId", caseId)
				.addValue("oldPanDocumentId", oldPanDocumentId).addValue("panDocumentId", panDocumentId);
		int updatCount = template.update(query, sqlParameterSource);
		log.info("updatCount===>" + updatCount);
		return updatCount;
	}
	
	public int updateItrDocumentId(Long itrDocumentId, Long oldITRDocumentId, Long caseId) {
		String query = "update `caseasuretyinfo` set ITRDocumentId=:itrDocumentId where ITRDocumentId = :oldITRDocumentId and CaseId=:caseId";
		MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource("caseId", caseId)
				.addValue("oldITRDocumentId", oldITRDocumentId).addValue("itrDocumentId", itrDocumentId);
		int updatCount = template.update(query, sqlParameterSource);
		log.info("updatCount===>" + updatCount);
		return updatCount;
	}

	public int update(SuretyInfoModel model) {
		String query = "update `caseasuretyinfo` set CaseId=:caseId=:,Name=:name,Address=:address,"
				+ "City=:city,Phone=:phone,AadharNo=:aadharNumber,PanNo=:panNumber,AadharDocId=:adharDocumentId,PanDocId=:panDocumentId,"
				+ "ITRRefNo=:itrRefNo, ITRDocumentId=:itrDocumentId, CreatedDate=:createdDate,CreateBy=:createBy" + " where id = :id ";
		MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource("caseId", model.getCaseId())
				.addValue("name", model.getName()).addValue("address", model.getAddress())
				.addValue("city", model.getCity()).addValue("phone", model.getPhone())
				.addValue("aadharNumber", model.getAadharNumber()).addValue("panNumber", model.getPanNumber())
				.addValue("adharDocumentId", model.getAdharDocumentId())
				.addValue("panDocumentId", model.getPanDocumentId())
				.addValue("itrRefNo", model.getItrRefNo())
				.addValue("itrDocumentId", model.getItrDocumentId())
				.addValue("createdDate", model.getCreatedDate())
				.addValue("createBy", model.getCreateBy()).addValue("id", model.getId());
		int updatCount = template.update(query, sqlParameterSource);
		log.info("updatCount===>" + updatCount);
		return updatCount;
	}
	
	public int deleteCaseById(Long caseId) {
		String query = "delete FROM caseasuretyinfo WHERE CaseId=:caseId";
		MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource();
		sqlParameterSource.addValue("caseId", caseId);
		int updatCount = template.update(query, sqlParameterSource);
		log.info("updatCount===>" + updatCount);
		return updatCount;
		
	}
	
	private RowMapper<SuretyInfoModel> mapObject() {
		return (result, i) -> {
			return SuretyInfoModel.builder().id(result.getLong("Id")).caseId(result.getLong("CaseId"))
					.name(result.getString("Name")).address(result.getString("address"))
					.city(result.getString("city")).phone(result.getString("phone"))
					.aadharNumber(result.getString("AadharNo")).panNumber(result.getString("PanNo"))
					.adharDocumentId(result.getLong("AadharDocId")).panDocumentId(result.getLong("PanDocId"))
					.itrRefNo(result.getString("ITRRefNo"))
					.itrDocumentId(result.getLong("ITRDocumentId"))
					.createdDate(Objects.isNull(result.getDate("createdDate")) ? null
							: result.getDate("createdDate").toLocalDate())
					.createBy(result.getLong("createBy")).build();
		};
	}

	
}
