package com.assignsecurities.service.impl.template;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.assignsecurities.app.AppConstant;
import com.assignsecurities.app.util.FileCloudUtil;
import com.assignsecurities.bean.CaseDocumentBean;
import com.assignsecurities.bean.FileBean;
import com.assignsecurities.bean.UserLoginBean;
import com.assignsecurities.dm.PropertyKeys;
import com.assignsecurities.domain.AddressModel;
import com.assignsecurities.domain.CaseFeeModel;
import com.assignsecurities.domain.CaseModel;
import com.assignsecurities.domain.CaseScriptModel;
import com.assignsecurities.domain.DocumentMasterModel;
import com.assignsecurities.domain.DocumentModel;
import com.assignsecurities.domain.GeneratedDocumentsRequiredModel;
import com.assignsecurities.domain.RtaTemplateModel;
import com.assignsecurities.domain.ScriptModel;
import com.assignsecurities.repository.impl.AddressRepo;
import com.assignsecurities.repository.impl.CaseDocumentDao;
import com.assignsecurities.repository.impl.CaseFeeDao;
import com.assignsecurities.repository.impl.CaseGeneratedDocumentsRequiredRepo;
import com.assignsecurities.repository.impl.CaseScriptRepo;
import com.assignsecurities.repository.impl.DocumentDao;
import com.assignsecurities.repository.impl.EmailReposatory;
import com.assignsecurities.repository.impl.ScriptRepo;
import com.assignsecurities.repository.impl.UtilityRepo;
import com.assignsecurities.service.impl.ApplicationPropertiesService;
import com.assignsecurities.service.impl.CaseService;

import lombok.extern.slf4j.Slf4j;

@Service
@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
@Slf4j
public class CustomerAgreementTemplateProcessor extends TemplateProcessorAbstract implements TemplateProcessor {

	@Autowired
	private CaseScriptRepo caseScriptRepo;

	@Autowired
	private ScriptRepo scriptRepo;

	@Autowired
	private DocumentDao documentDao;
	
	@Autowired
	private CaseDocumentDao caseDocumentDao;
	
	@Autowired
	private CaseService caseService;
	
	@Autowired
	private CaseGeneratedDocumentsRequiredRepo caseGeneratedDocumentsRequiredRepo;
	
	@Autowired
	private UtilityRepo utilityRepo;
	
	@Autowired
	private AddressRepo documentRepo;
	
	@Autowired
	private CaseFeeDao caseFeeDao;
	
	@Autowired 
	private EmailReposatory emailReposatory;

	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Throwable.class)
	public FileBean processTemplate(CaseDocumentBean caseDocumentBean, UserLoginBean userLoginBean,
			CaseModel caseModel) {
//		RtaTemplateModel rtaTemplateModel = getRtaTemplate(caseModel.getCompanyName(), caseDocumentBean.getTemplateType());
		RtaTemplateModel rtaTemplateModel = RtaTemplateModel.builder()
				.templateName(AppConstant.CUSTOMER_AGREEMENT_TEMPLATE_NAME)
				.templateType(AppConstant.DocumentType.CustomerAgreement.name()).build();
		String docTemplatePath = loadTemplatePath(rtaTemplateModel);
		String finalPdfFileName = generateDestFileName(rtaTemplateModel, userLoginBean, caseModel);
		List<CaseScriptModel> caseScriptModels = caseScriptRepo.getByCaseId(caseModel.getId());
		List<Long> scriptIds = caseScriptModels.stream().map(script -> script.getScriptId()).distinct()
				.collect(Collectors.toList());
		List<ScriptModel> scriptModels = scriptRepo.getScripts(scriptIds);
		caseScriptModels.forEach(caseScript -> {
			Optional<ScriptModel> script = scriptModels.stream().filter(s -> s.getId().equals(caseScript.getScriptId()))
					.findFirst();
			caseScript.setScriptModel(script.orElse(null));
		});
		caseModel.setScripts(caseScriptModels);
		AddressModel addressModel=  documentRepo.getAddressById(caseModel.getCommAddressId());
		caseModel.setCommAddress(addressModel);
		List<CaseFeeModel> feeModels = caseFeeDao.getByCaseId(caseModel.getId());
		caseModel.setFeeModels(feeModels);
		String bucketName = ApplicationPropertiesService
				.getPropertyStringValue(PropertyKeys.AZ_S3_BUCKET_NAME);
//		caseModel.setMaskFolio(maskFolio);
		if (!caseModel.isMaskFolio()) {
			DocumentModel documentModel = DocumentModel.builder().name(finalPdfFileName)
					.createdDate(LocalDateTime.now()).createBy(userLoginBean.getId())
					.type(AppConstant.DocumentType.CustomerAgreement.name())
					.contentType(AppConstant.JAVA_SCRIPT_DATA_PDF_BASE64).bucketName(bucketName).build();
			Long oldDocId = caseModel.getAggrmentDcoumentId();
			
			Long aggrmentDcoumentId = documentDao.add(documentModel);
			caseModel.setAggrmentDcoumentId(aggrmentDcoumentId);
			documentModel.setId(aggrmentDcoumentId);
			caseService.intsertCaseDocument(caseModel.getId(), documentModel, null);
			caseService.update(caseModel, userLoginBean);
			caseDocumentDao.deleteByDocumentId(oldDocId);
			caseGeneratedDocumentsRequiredRepo.deleteDocId(oldDocId);
			emailReposatory.deleteByDocId(oldDocId);
			documentDao.deleteById(oldDocId);
			DocumentMasterModel documentMasterModel =  utilityRepo.getMasterDocument(AppConstant.DocumentType.CustomerAgreement.name());
			caseGeneratedDocumentsRequiredRepo.add(GeneratedDocumentsRequiredModel.builder().caseId(caseModel.getId())
				.documentMasterId(documentMasterModel.getId()).documentId(aggrmentDcoumentId).build());
		}

		return storeAgreementAsPdf(docTemplatePath, rtaTemplateModel, caseModel, caseModel,
				finalPdfFileName,!caseModel.isMaskFolio());
	}
	
}
