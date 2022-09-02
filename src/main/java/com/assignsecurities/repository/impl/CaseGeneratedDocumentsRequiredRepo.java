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

import com.assignsecurities.domain.CustomerDocumentsRequiredModel;
import com.assignsecurities.domain.GeneratedDocumentsRequiredModel;

import lombok.extern.slf4j.Slf4j;

@Repository
@Slf4j
public class CaseGeneratedDocumentsRequiredRepo {
	@Autowired
	private NamedParameterJdbcTemplate template;

	public GeneratedDocumentsRequiredModel getById(Long id) {
		try {
			String query = "SELECT cdq.*,dm.Particulars doccumentName,d.id documentId FROM generated_documents_required cdq \r\n" + 
					"inner join documentmaster dm on cdq.DocumentMasterId=dm.id\r\n" + 
					"left join casedocument cd on cdq.CaseId=cd.CaseId\r\n" + 
					"inner join document d on d.id=cd.DocumentId  WHERE cdq.id=:id order by dm.TempOrder";
			MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource();
			sqlParameterSource.addValue("id", id);
			return template.queryForObject(query, sqlParameterSource, mapObject());
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

//	public List<GeneratedDocumentsRequiredModel> getByCaseId(Long caseId) {
//		try {
//			String query = "SELECT cdq.*,dm.Particulars doccumentName,d.id documentId FROM generated_documents_required cdq \r\n" + 
//					" inner join documentmaster dm on cdq.DocumentMasterId=dm.id\r\n" + 
//					" left join casedocument cd on cdq.CaseId=cd.CaseId\r\n" + 
//					" inner join document d on d.id=cd.DocumentId  WHERE cdq.CaseId=:caseId order by dm.TempOrder";
//			MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource();
//			sqlParameterSource.addValue("caseId", caseId);
//			return template.query(query, sqlParameterSource, mapObject());
//		} catch (EmptyResultDataAccessException e) {
//			return null;
//		}
//	}
	
	
	public List<GeneratedDocumentsRequiredModel> getByCaseId(Long caseId) {
		try {

			String query = "SELECT DISTINCT dm.id documentMasterId,dm.particulars documentName,dm.type documentMasterType,cd.caseId caseId,cd.documentId,cdr.id id,  cdr.documentmasterId customerRequireGeneratedDocMasterId\n" + 
					" FROM  `documentmaster` dm  inner join rta_templates_group rta on rta.RtaGroup =dm.RtaGroup \n" + 
					" inner join rtadata rt on rt.RegistrarName=rta.RtaName\n" + 
					" inner join script s on s.CompanyName=rt.CompanyName\n" + 
					" inner join casescript sc on s.id=sc.scriptId AND sc.caseId=:caseId \n" + 
					" LEFT JOIN `generated_documents_required` cdr ON cdr.documentmasterId = dm.id AND cdr.caseId=:caseId\n" + 
					" LEFT JOIN `casedocument` cd ON dm.type=cd.Type and cdr.DocumentId=cd.DocumentId AND cd.caseId=:caseId\n" + 
					" WHERE dm.uploadedOrGenerated = 'Generated' order by dm.TempOrder ";
			
			MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource();
			sqlParameterSource.addValue("caseId", caseId);
			return template.query(query, sqlParameterSource, mapObjectUpdated());
		} catch (EmptyResultDataAccessException e) {
			return Collections.emptyList();
		}
	}
	
	private RowMapper<GeneratedDocumentsRequiredModel> mapObjectUpdated() {
		return (result, i) -> {
			return GeneratedDocumentsRequiredModel.builder().id(result.getLong("Id"))
					.caseId(result.getLong("caseId")).documentMasterId(result.getLong("documentMasterId"))
					.documentId(result.getLong("documentId")).documentName(result.getString("documentName"))
					.documentMasterType(result.getString("documentMasterType"))
					.customerGeneratedDocMasterId(result.getLong("customerRequireGeneratedDocMasterId"))
					.build();
		};
	}
	
	public Long add(GeneratedDocumentsRequiredModel model) {
		String query = "insert into `generated_documents_required` (CaseId,DocumentMasterId,DocumentId) "
				+ "values(:caseId,:documentMasterId,:documentId)";
		KeyHolder keyHolder = new GeneratedKeyHolder();
		MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource("caseId", model.getCaseId())
				.addValue("documentMasterId", model.getDocumentMasterId())
				.addValue("documentId", model.getDocumentId());
		template.update(query, sqlParameterSource, keyHolder);
		return keyHolder.getKey().longValue();
	}

	public void addAll(Long caseId,List<GeneratedDocumentsRequiredModel> models) {
		String query = "insert into `generated_documents_required` (CaseId,DocumentMasterId,DocumentId) "
				+ "values(:caseId,:documentMasterId,:documentId)";
		List<Map<String, Object>> batchValues = new ArrayList<>(models.size());
		models.stream().forEach(model -> {
			Long documentIdtemp = model.getDocumentId();
			if (Objects.isNull(model.getDocumentId()) || model.getDocumentId() < 1) {
				documentIdtemp = null;
			}
			
			batchValues.add(new MapSqlParameterSource("caseId", caseId)
					.addValue("documentMasterId", model.getDocumentMasterId())
					.addValue("documentId", documentIdtemp).getValues());
		});
		template.batchUpdate(query, batchValues.toArray(new Map[models.size()]));
	}

	public int deleteDocIds(List<Long> docIds) {
		String query = "delete from  `generated_documents_required` where DocumentId in(:docIds) ";
		MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource("docIds", docIds);
		int updatCount = template.update(query, sqlParameterSource);
		log.info("updatCount===>" + updatCount);
		return updatCount;
	}
	
	public int deleteDocCaseMasterDocId(Long caseId, Long masterDocId) {
		String query = "delete from  `generated_documents_required` where CaseId=:caseId and DocumentMasterId=:documentMasterId ";
		MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource("caseId", caseId)
				.addValue("documentMasterId", masterDocId);
		int updatCount = template.update(query, sqlParameterSource);
		log.info("updatCount===>" + updatCount);
		return updatCount;
	}
	
	public int deleteDocId(Long docId) {
		String query = "delete from  `generated_documents_required` where DocumentId =:docId ";
		MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource("docId", docId);
		int updatCount = template.update(query, sqlParameterSource);
		log.info("updatCount===>" + updatCount);
		return updatCount;
	}

	public int update(GeneratedDocumentsRequiredModel model) {
		String query = "update `generated_documents_required` set CaseId=:caseId,DocumentMasterId=:documentMasterId,DocumentId=:documentId where id = :id ";
		MapSqlParameterSource sqlParameterSource =new MapSqlParameterSource("caseId", model.getCaseId())
				.addValue("documentMasterId", model.getDocumentMasterId())
				.addValue("documentId", model.getDocumentId());
		int updatCount = template.update(query, sqlParameterSource);
		log.info("updatCount===>" + updatCount);
		return updatCount;
	}
	
	public void updateAll(Long caseId,List<GeneratedDocumentsRequiredModel> models) {
		String query = "update `generated_documents_required` set CaseId=:caseId,DocumentMasterId=:documentMasterId,DocumentId=:documentId where id = :id ";
		List<Map<String, Object>> batchValues = new ArrayList<>(models.size());
		models.stream().forEach(model -> {
			Long documentIdtemp = model.getDocumentId();
			if (Objects.isNull(model.getDocumentId()) || model.getDocumentId() < 1) {
				documentIdtemp = null;
			}
			
			batchValues.add(new MapSqlParameterSource("caseId", caseId)
					.addValue("documentMasterId", model.getDocumentMasterId())
					.addValue("documentId", documentIdtemp)
					.addValue("id", model.getId()).getValues());
		});
		template.batchUpdate(query, batchValues.toArray(new Map[models.size()]));
	}
	
	public void deleteAll(List<Long> ids) {
		String query = "delete from  `generated_documents_required` where Id in(:ids)";
		MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource("ids", ids);
		int updatCount = template.update(query, sqlParameterSource);
		log.info("updatCount===>" + updatCount);
	}

	private RowMapper<GeneratedDocumentsRequiredModel> mapObject() {
		return (result, i) -> {
			return GeneratedDocumentsRequiredModel.builder().id(result.getLong("Id"))
					.caseId(result.getLong("CaseId")).documentMasterId(result.getLong("DocumentMasterId"))
					.documentId(result.getLong("DocumentId")).documentName(result.getString("DoccumentName"))
					.build();
		};
	}
	
	
	
	public int deleteByCaseId(Long caseId) {
		String query = "delete from  `generated_documents_required` where caseId =:caseId ";
		MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource("caseId", caseId);
		int updatCount = template.update(query, sqlParameterSource);
		log.info("updatCount===>" + updatCount);
		return updatCount;
	}
}
