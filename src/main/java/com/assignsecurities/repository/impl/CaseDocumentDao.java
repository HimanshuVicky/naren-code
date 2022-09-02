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

import com.assignsecurities.domain.CaseDocumentModel;
import com.assignsecurities.domain.RoleModel;

import lombok.extern.slf4j.Slf4j;

@Repository
@Slf4j
public class CaseDocumentDao {
	@Autowired
	private NamedParameterJdbcTemplate template;

	public CaseDocumentModel getById(Long id) {
		try {
			String query = "SELECT * FROM casedocument WHERE id=:id";
			MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource();
			sqlParameterSource.addValue("id", id);
			return template.queryForObject(query, sqlParameterSource, mapObject());
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}
	
	public List<CaseDocumentModel> getByCaseIdAndDocType(Long caseId, String type) {
		try {
			String query = "SELECT * FROM casedocument  WHERE CaseId=:caseId and `Type`=:type";
			MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource();
			sqlParameterSource.addValue("caseId", caseId);
			sqlParameterSource.addValue("type", type);
			return template.query(query, sqlParameterSource, mapObject());
		} catch (EmptyResultDataAccessException e) {
			return Collections.emptyList();
		}
	}

	public List<CaseDocumentModel> getByCaseIdAndDocTypeUploadType(Long caseId, String type, String uploadType) {
		try {
			String query = "SELECT * FROM casedocument  WHERE CaseId=:caseId and `Type`=:type and UploadType=:uploadType";
			MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource();
			sqlParameterSource.addValue("caseId", caseId);
			sqlParameterSource.addValue("type", type);
			sqlParameterSource.addValue("uploadType", uploadType);
			return template.query(query, sqlParameterSource, mapObject());
		} catch (EmptyResultDataAccessException e) {
			return Collections.emptyList();
		}
	}
	
	public List<CaseDocumentModel> getByCaseId(Long caseId) {
		try {
			String query = "SELECT * FROM casedocument WHERE CaseId=:caseId";
			MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource();
			sqlParameterSource.addValue("caseId", caseId);
			return template.query(query, sqlParameterSource, mapObject());
		} catch (EmptyResultDataAccessException e) {
			return Collections.emptyList();
		}
	}

	public Long add(CaseDocumentModel model) {
		String query = "insert into `casedocument` (CaseId,DocumentId,`Type`,UploadType,CreatedDate,CreateBy) "
				+ "values(:caseId,:documentId,:type,:uploadType,:createdDate,:createBy)";
		KeyHolder keyHolder = new GeneratedKeyHolder();

		MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource("caseId", model.getCaseId())
				.addValue("documentId", model.getDocumentId()).addValue("type", model.getType())
				.addValue("uploadType", model.getUploadType())
				.addValue("createBy", model.getCreateBy()).addValue("createdDate", model.getCreatedDate());
		
		template.update(query, sqlParameterSource, keyHolder);
		return keyHolder.getKey().longValue();
	}
	

	public void addAll(List<CaseDocumentModel> models) {
		String query = "insert into `casedocument` (CaseId,DocumentId,`Type`,UploadType,CreatedDate,CreateBy) "
				+ "values(:caseId,:documentId,:type,:uploadType,:createdDate,:createBy)";
		
		List<Map<String, Object>> batchValues = new ArrayList<>(models.size());
		models.stream().forEach(model -> {
			batchValues.add(new MapSqlParameterSource("caseId", model.getCaseId())
					.addValue("documentId", model.getDocumentId()).addValue("type", model.getType())
					.addValue("uploadType", model.getUploadType())
					.addValue("createBy", model.getCreateBy()).addValue("createdDate", model.getCreatedDate()).getValues());
		});
		template.batchUpdate(query, batchValues.toArray(new Map[models.size()]));
	}

	public int deleteById(Long id) {
		if(Objects.isNull( id) || id<1) {
			return 0;
		}
		String query = "delete FROM casedocument WHERE id=:id";
		MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource("id", id);
		int updatCount = template.update(query, sqlParameterSource);
		log.info("updatCount===>" + updatCount);
		return updatCount;
	}
	
	public int deleteByDocumentId(Long documentId) {
		if(Objects.isNull( documentId) || documentId<1) {
			return 0;
		}
		String query = "delete FROM casedocument WHERE DocumentId=:documentId";
		MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource("documentId", documentId);
//		return template.update(query, sqlParameterSource);
		int updatCount = template.update(query, sqlParameterSource);
		log.info("updatCount===>" + updatCount);
		return updatCount;
	}
	
	public int deleteByCaseIdAndType(Long caseId, String type) {
		String query = "delete FROM casedocument WHERE CaseId=:caseId  and `Type`=:type ";
		MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource("caseId", caseId)
				.addValue("type", type);
		return template.update(query, sqlParameterSource);
	}

	private RowMapper<CaseDocumentModel> mapObject() {
		return (result, i) -> {
			return CaseDocumentModel.builder().id(result.getLong("id")).caseId(result.getLong("CaseId"))
					.documentId(result.getLong("DocumentId")).createBy(result.getLong("CreateBy"))
					.createdDate(Objects.isNull(result.getDate("CreatedDate")) ? null
							: result.getTimestamp("CreatedDate").toLocalDateTime())
					.type(result.getString("Type")).uploadType(result.getString("UploadType")).build();
		};
	}
	
	public void update(CaseDocumentModel model) {
		String query =" update casedocument set documentId=:documentId where caseId=:caseId and type=:type and id=:id";
		//String query = "update role set name=:name,code=:code,IsActive=:isActive where id=:id ";
		MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource();
		sqlParameterSource.addValue("caseId", model.getCaseId());
		sqlParameterSource.addValue("type",model.getType());
		sqlParameterSource.addValue("documentId", model.getDocumentId());
		sqlParameterSource.addValue("id", model.getId());
		template.update(query, sqlParameterSource);

	}
}
