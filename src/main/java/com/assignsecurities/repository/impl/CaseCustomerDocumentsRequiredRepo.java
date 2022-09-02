package com.assignsecurities.repository.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.assignsecurities.domain.CaseDocumentModel;
import com.assignsecurities.domain.CaseFeeModel;
import com.assignsecurities.domain.CustomerDocumentsRequiredModel;

import lombok.extern.slf4j.Slf4j;

@Repository
@Slf4j
public class CaseCustomerDocumentsRequiredRepo {
	@Autowired
	private NamedParameterJdbcTemplate template;
	
	@Autowired
	private CaseDocumentDao caseDocumentDao;

	public CustomerDocumentsRequiredModel getById(Long id) {
		try {
			String query = "SELECT cdq.*,dm.Particulars doccumentName,d.id documentId FROM customer_documents_required cdq \r\n" + 
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

	public List<CustomerDocumentsRequiredModel> getByCaseIdMain(Long caseId) {
		try {
			String query = "SELECT * from customer_documents_required where caseId=:caseId";
			MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource();
			sqlParameterSource.addValue("caseId", caseId);
			return template.query(query, sqlParameterSource, mapCoreObject());
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}
	
	
	public List<CustomerDocumentsRequiredModel> getByCaseId(Long caseId) {
		try {

			String query = "  SELECT DISTINCT dm.id documentMasterId,dm.particulars doccumentName,dm.type documentMasterType\n" + 
					"  FROM  `documentmaster` dm\n" + 
					"  LEFT JOIN `casedocument` cd ON dm.particulars=cd.type and (UploadType<>'Signed' OR UploadType is null)  AND cd.caseId=:caseId " + 
					" WHERE dm.uploadedOrGenerated = 'Uploaded'  order by dm.TempOrder";
			
//			" #,cdr.caseId caseId,cd.documentId,cdr.id id, \n" + 
//			" #cdr.documentmasterId customerRequireDocMasterId\n" +  
			
			
			MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource();
			sqlParameterSource.addValue("caseId", caseId);
			List<CustomerDocumentsRequiredModel> customerDocumentsRequiredModels= template.query(query, sqlParameterSource, mapMaster());
			
			
			List<CustomerDocumentsRequiredModel> customerDocumentsRequiredModelsMain=  getByCaseIdMain(caseId);
			List<CaseDocumentModel> caseDocumentModels = caseDocumentDao.getByCaseId(caseId);
			
			customerDocumentsRequiredModels.forEach(cdr->{
				
				
				Optional<CustomerDocumentsRequiredModel> customerDocumentsRequiredModelOpt = customerDocumentsRequiredModelsMain.stream()
						.filter(cdr1->cdr.getDocumentMasterId().equals(cdr1.getDocumentMasterId()) ).findFirst();
				if(customerDocumentsRequiredModelOpt.isPresent()) {
					cdr.setId(customerDocumentsRequiredModelOpt.get().getId());
					cdr.setCustomerRequireDocMasterId(customerDocumentsRequiredModelOpt.get().getDocumentMasterId());
				}else {
					cdr.setId(0L);
//					cdr.setCustomerRequireDocMasterId(0L);
				}
//				customerDocumentsRequiredModelOpt.ifPresent(d->{
//					cdr.setId(cdr.getDocumentMasterId());
//					cdr.setCustomerRequireDocMasterId(d.getDocumentMasterId());
//					//cdr.setDocumentId(d.getDocumentId());
//				});
				
				Optional<CaseDocumentModel> caseDocumentModelOpt = caseDocumentModels.stream()
						.filter(cd->(cdr.getDocumentMasterType().equalsIgnoreCase(cd.getType()))).findFirst();
				if(caseDocumentModelOpt.isPresent()) {
					cdr.setCaseId(caseId);
					cdr.setDocumentId(caseDocumentModelOpt.get().getDocumentId());
				}else {
					cdr.setDocumentId(0L);
				}
//				caseDocumentModelOpt.ifPresent(d->{
//					cdr.setCaseId(caseId);
//					cdr.setDocumentId(d.getDocumentId());
//				});
				
			});
			
			return customerDocumentsRequiredModels;
		} catch (EmptyResultDataAccessException e) {
			return Collections.emptyList();
		}
//		return Collections.emptyList();
		
	}
	
	private RowMapper<CustomerDocumentsRequiredModel> mapObjectUpdated() {
		return (result, i) -> {
			return CustomerDocumentsRequiredModel.builder().id(result.getLong("Id"))
					.caseId(result.getLong("caseId")).documentMasterId(result.getLong("documentMasterId"))
					.documentId(result.getLong("documentId")).doccumentName(result.getString("doccumentName"))
					.documentMasterType(result.getString("documentMasterType"))
					.customerRequireDocMasterId(result.getLong("customerRequireDocMasterId"))
					.build();
		};
	}
	
	private RowMapper<CustomerDocumentsRequiredModel> mapMaster() {
		return (result, i) -> {
			return CustomerDocumentsRequiredModel.builder().documentMasterId(result.getLong("documentMasterId"))
					.doccumentName(result.getString("doccumentName"))
					.documentMasterType(result.getString("documentMasterType"))
					.build();
		};
	}
	

	public Long add(CustomerDocumentsRequiredModel model) {
		String query = "insert into `customer_documents_required` (CaseId,DocumentMasterId,DocumentId) "
				+ "values(:caseId,:documentMasterId,:documentId)";
		KeyHolder keyHolder = new GeneratedKeyHolder();
		MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource("caseId", model.getCaseId())
				.addValue("documentMasterId", model.getDocumentMasterId())
				.addValue("documentId", model.getDocumentId());
		template.update(query, sqlParameterSource, keyHolder);
		return keyHolder.getKey().longValue();
	}

	public void addAll(Long caseId,List<CustomerDocumentsRequiredModel> models) {
		String query = "insert into `customer_documents_required` (CaseId,DocumentMasterId,DocumentId) "
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
		int[] counts= template.batchUpdate(query, batchValues.toArray(new Map[models.size()]));
		log.info("updatcounts ===>" + counts);
	}

	public int delete(Long caseId) {
		String query = "delete from  `customer_documents_required` where CaseId = :caseId ";
		MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource("caseId", caseId);
		int updatCount = template.update(query, sqlParameterSource);
		log.info("updatCount===>" + updatCount);
		return updatCount;
	}
	
	public int delete(Long caseId,Long documentId) {
		String query = "delete from  `customer_documents_required` where CaseId = :caseId and DocumentId=:documentId";
		MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource("caseId", caseId)
				.addValue("documentId", documentId);
		int deleteCount = template.update(query, sqlParameterSource);
		log.info("deleteCount===>" + deleteCount);
		return deleteCount;
	}
	
	public int deleteDocId(Long docId) {
		String query = "delete from  `customer_documents_required` where DocumentId =:docId ";
		MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource("docId", docId);
		int updatCount = template.update(query, sqlParameterSource);
		log.info("updatCount===>" + updatCount);
		return updatCount;
	}

	public int update(CustomerDocumentsRequiredModel model) {
		String query = "update `customer_documents_required` set CaseId=:caseId,DocumentMasterId=:documentMasterId,DocumentId=:documentId where id = :id ";
		MapSqlParameterSource sqlParameterSource =new MapSqlParameterSource("caseId", model.getCaseId())
				.addValue("documentMasterId", model.getDocumentMasterId())
				.addValue("documentId", model.getDocumentId());
		int updatCount = template.update(query, sqlParameterSource);
		log.info("updatCount===>" + updatCount);
		return updatCount;
	}
	
	public void updateAll(Long caseId,List<CustomerDocumentsRequiredModel> models) {
		String query = "update `customer_documents_required` set CaseId=:caseId,DocumentMasterId=:documentMasterId,DocumentId=:documentId where id = :id ";
		
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
		int[] counts= template.batchUpdate(query, batchValues.toArray(new Map[models.size()]));
		log.info("updatcounts ===>" + counts);
	}

	public void deleteAll(List<Long> ids) {
		String query = "delete from  `customer_documents_required` where Id in(:ids) ";
		MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource("ids", ids);
		int updatCount = template.update(query, sqlParameterSource);
		log.info("updatCount===>" + updatCount);
	}
	
	private RowMapper<CustomerDocumentsRequiredModel> mapObject() {
		return (result, i) -> {
			return CustomerDocumentsRequiredModel.builder().id(result.getLong("Id"))
					.caseId(result.getLong("CaseId")).documentMasterId(result.getLong("DocumentMasterId"))
					.documentId(result.getLong("DocumentId")).doccumentName(result.getString("DoccumentName"))
					.build();
		};
	}

	private RowMapper<CustomerDocumentsRequiredModel> mapCoreObject() {
		return (result, i) -> {
			return CustomerDocumentsRequiredModel.builder().id(result.getLong("Id"))
					.caseId(result.getLong("CaseId")).documentMasterId(result.getLong("DocumentMasterId"))
					.documentId(result.getLong("DocumentId"))
					.build();
		};
	}
	
	public List<CustomerDocumentsRequiredModel> getCaseCustRequireDocumentsByCaseIdAndUserTypeId(Long caseId, Long userTypeId) {
		try {

			String query = "  SELECT DISTINCT dm.id documentMasterId,dm.particulars doccumentName,dm.type documentMasterType\n" + 
					"  FROM  `documentmaster` dm\n" + 
					"  INNER JOIN `casedocument` cd ON dm.particulars=cd.type and (UploadType<>'Signed' OR UploadType is null)  AND cd.caseId=:caseId " + 
					" INNER JOIN `typeofusers` ut ON dm.usertypeId = ut.id "+
					" WHERE dm.uploadedOrGenerated = 'Uploaded' AND ut.id =:userTypeId order by dm.TempOrder";
			
			
			
//			" #,cdr.caseId caseId,cd.documentId,cdr.id id, \n" + 
//			" #cdr.documentmasterId customerRequireDocMasterId\n" +  
			
			
			MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource();
			sqlParameterSource.addValue("caseId", caseId);
			sqlParameterSource.addValue("userTypeId", userTypeId);
			
			List<CustomerDocumentsRequiredModel> customerDocumentsRequiredModels= template.query(query, sqlParameterSource, mapMaster());
			
			
			List<CustomerDocumentsRequiredModel> customerDocumentsRequiredModelsMain=  getByCaseIdMain(caseId);
			List<CaseDocumentModel> caseDocumentModels = caseDocumentDao.getByCaseId(caseId);
			
			customerDocumentsRequiredModels.forEach(cdr->{
				
				
				Optional<CustomerDocumentsRequiredModel> customerDocumentsRequiredModelOpt = customerDocumentsRequiredModelsMain.stream()
						.filter(cdr1->cdr.getDocumentMasterId().equals(cdr1.getDocumentMasterId()) ).findFirst();
				if(customerDocumentsRequiredModelOpt.isPresent()) {
					cdr.setId(customerDocumentsRequiredModelOpt.get().getId());
					cdr.setCustomerRequireDocMasterId(customerDocumentsRequiredModelOpt.get().getDocumentMasterId());
				}else {
					cdr.setId(0L);
//					cdr.setCustomerRequireDocMasterId(0L);
				}
//				customerDocumentsRequiredModelOpt.ifPresent(d->{
//					cdr.setId(cdr.getDocumentMasterId());
//					cdr.setCustomerRequireDocMasterId(d.getDocumentMasterId());
//					//cdr.setDocumentId(d.getDocumentId());
//				});
				
				Optional<CaseDocumentModel> caseDocumentModelOpt = caseDocumentModels.stream()
						.filter(cd->(cdr.getDocumentMasterType().equalsIgnoreCase(cd.getType()))).findFirst();
				if(caseDocumentModelOpt.isPresent()) {
					cdr.setCaseId(caseId);
					cdr.setDocumentId(caseDocumentModelOpt.get().getDocumentId());
				}else {
					cdr.setDocumentId(0L);
				}
//				caseDocumentModelOpt.ifPresent(d->{
//					cdr.setCaseId(caseId);
//					cdr.setDocumentId(d.getDocumentId());
//				});
				
			});
			
			return customerDocumentsRequiredModels;
		} catch (EmptyResultDataAccessException e) {
			return Collections.emptyList();
		}
//		return Collections.emptyList();
		
	}
	
}
