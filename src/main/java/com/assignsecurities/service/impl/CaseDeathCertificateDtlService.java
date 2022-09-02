package com.assignsecurities.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.assignsecurities.app.AppConstant;
import com.assignsecurities.app.util.ArgumentHelper;
import com.assignsecurities.app.util.FileCloudUtil;
import com.assignsecurities.bean.CaseDeathCertificateDtlBean;
import com.assignsecurities.bean.FileBean;
import com.assignsecurities.bean.UserLoginBean;
import com.assignsecurities.converter.CaseDeathCertificateDtlCoverter;
import com.assignsecurities.dm.PropertyKeys;
import com.assignsecurities.domain.CaseDeathCertificateDtlModel;
import com.assignsecurities.domain.CaseModel;
import com.assignsecurities.domain.CaseShareCertificateDetailsModel;
import com.assignsecurities.domain.CaseWarrantDetailsModel;
import com.assignsecurities.domain.CustomerDocumentsRequiredModel;
import com.assignsecurities.domain.DocumentMasterModel;
import com.assignsecurities.domain.DocumentModel;
import com.assignsecurities.domain.QuestionerModel;
import com.assignsecurities.domain.ScriptModel;
import com.assignsecurities.repository.impl.CaseCustomerDocumentsRequiredRepo;
import com.assignsecurities.repository.impl.CaseDeathCertificateDtlRepo;
import com.assignsecurities.repository.impl.CaseDocumentDao;
import com.assignsecurities.repository.impl.CaseRepo;
import com.assignsecurities.repository.impl.DocumentDao;
import com.assignsecurities.repository.impl.EmailReposatory;
import com.assignsecurities.repository.impl.UtilityRepo;

@Service
@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
public class CaseDeathCertificateDtlService {
	@Autowired
    private CaseDeathCertificateDtlRepo caseDeathCertificateDtlRepo;
	
	@Autowired
	private DocumentDao documentDao;
	
	@Autowired
	private CaseTemplateService caseTemplateService;
	
	@Autowired
	private CaseService caseService;
	
	@Autowired 
	private UtilityRepo utilityRepo;
	
	@Autowired
	private CaseCustomerDocumentsRequiredRepo caseCustomerDocumentsRequiredRepo;
	
	@Autowired
	private CaseRepo caseRepo;
	
	@Autowired
	private CaseDocumentDao caseDocumentDao;
	
	@Autowired
	private EmailReposatory emailReposatory;
	
	public CaseDeathCertificateDtlBean getCaseDeathCertificateDtlById(Long id) {
		return CaseDeathCertificateDtlCoverter.convert(caseDeathCertificateDtlRepo.getById(id));
		
	}
	
	public List<CaseDeathCertificateDtlBean> getByCaseId(Long caseId) {
		List<CaseDeathCertificateDtlBean> casesdeathCertDtlsBeanlst = CaseDeathCertificateDtlCoverter.convert(caseDeathCertificateDtlRepo.getByCaseId(caseId));
//		casesdeathCertDtlsBean.forEach(application->{
//			if(DashBoardService.displayCaseStatusMapping.containsKey(application.getStatus())) {
//				application.setStatus(DashBoardService.displayCaseStatusMapping.get(application.getStatus()));
//			}
//		});
		return casesdeathCertDtlsBeanlst;
	}
	
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Throwable.class)
	public Long addCaseShareHolderDeathCertificateDtl(CaseDeathCertificateDtlBean caseDeathCertificateDtlBean, UserLoginBean userLoginBean) {
		
		long oldDocId = 0L;
		long caseShareHolderDeathCertDtlId =0L;
		String bucketName = ApplicationPropertiesService
				.getPropertyStringValue(PropertyKeys.AZ_S3_BUCKET_NAME);
		FileBean caseShareHolderDeathCertDoc = caseDeathCertificateDtlBean.getDeathCertDocumet();
		caseShareHolderDeathCertDoc.setFileTitle(System.currentTimeMillis() + "_" + caseShareHolderDeathCertDoc.getFileTitle());
		DocumentModel documentModel = DocumentModel.builder().name(caseShareHolderDeathCertDoc.getFileTitle())
				.createdDate(LocalDateTime.now()).createBy(userLoginBean.getId())
				.contentType(caseShareHolderDeathCertDoc.getFileContentType()).type(AppConstant.DocumentType.ShareHolderDeathCertificate.label)
				.bucketName(bucketName).build();
		Long caseShareHolderDeathCertDocId = documentDao.add(documentModel);
		
		caseDeathCertificateDtlBean.setDocumentId(caseShareHolderDeathCertDocId);
		documentModel.setId(caseShareHolderDeathCertDocId);
		caseTemplateService.saveFileToAS3(caseShareHolderDeathCertDoc, bucketName);
		List<CaseDeathCertificateDtlModel> caseDeathCertificateDtlModels = caseDeathCertificateDtlRepo.getByCaseId(caseDeathCertificateDtlBean.getCaseId());
		
		if(ArgumentHelper.isNotEmpty(caseDeathCertificateDtlModels))
		{	caseShareHolderDeathCertDtlId = caseDeathCertificateDtlModels.get(0).getId();
		    oldDocId= caseDeathCertificateDtlModels.get(0).getDocumentId();
			caseDeathCertificateDtlBean.setId(caseShareHolderDeathCertDtlId);
			int caseDeathCertificateDtlIdupdate = caseDeathCertificateDtlRepo.update(CaseDeathCertificateDtlCoverter.convert(caseDeathCertificateDtlBean)); 
			documentDao.deleteById(oldDocId);
			caseDocumentDao.deleteByDocumentId(oldDocId);	
			caseCustomerDocumentsRequiredRepo.delete(caseDeathCertificateDtlBean.getCaseId(),oldDocId);
			
		}else {
			Long caseDeathCertificateDtlId = caseDeathCertificateDtlRepo.add(CaseDeathCertificateDtlCoverter.convert(caseDeathCertificateDtlBean)); 
		}
		caseService.intsertCaseDocument(caseDeathCertificateDtlBean.getCaseId(), documentModel, null);
				
		DocumentMasterModel documentMasterModel =  utilityRepo.getMasterDocument(AppConstant.DocumentType.ShareHolderDeathCertificate.label);
		caseCustomerDocumentsRequiredRepo.add(CustomerDocumentsRequiredModel.builder().caseId(caseDeathCertificateDtlBean.getCaseId())
			.documentMasterId(documentMasterModel.getId()).documentId(caseShareHolderDeathCertDocId).build());		
		return caseShareHolderDeathCertDocId;
	}

	
	
	
//	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Throwable.class)
//	public void update(CaseDeathCertificateDtlBean bean) {
//		courierDetailRepo.update(CourierDetailConverter.convert(bean));
//	}
	
//    public List<CaseDeathCertificateDtlBean> getAllCaseShareHolderDeathCertificates() {
//		
////		List<CaseDeathCertificateDtlModel> roles = caseDeathCertificateDtlRepo.getAllCourierServiceDetails();
////		return CourierDetailConverter.convert(roles);
//	}

}
