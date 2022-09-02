package com.assignsecurities.service.impl.template;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.assignsecurities.app.AppConstant;
import com.assignsecurities.app.util.ArgumentHelper;
import com.assignsecurities.bean.CaseDocumentBean;
import com.assignsecurities.bean.FileBean;
import com.assignsecurities.bean.UserLoginBean;
import com.assignsecurities.dm.PropertyKeys;
import com.assignsecurities.domain.CaseDocumentModel;
import com.assignsecurities.domain.CaseModel;
import com.assignsecurities.domain.CaseQuestionerModel;
import com.assignsecurities.domain.CaseTemplateBean;
import com.assignsecurities.domain.DocumentMasterModel;
import com.assignsecurities.domain.DocumentModel;
import com.assignsecurities.domain.GeneratedDocumentsRequiredModel;
import com.assignsecurities.domain.RtaTemplateModel;
import com.assignsecurities.repository.impl.CaseDocumentDao;
import com.assignsecurities.repository.impl.CaseGeneratedDocumentsRequiredRepo;
import com.assignsecurities.repository.impl.CaseQuestionerRepo;
import com.assignsecurities.repository.impl.DocumentDao;
import com.assignsecurities.repository.impl.EmailReposatory;
import com.assignsecurities.repository.impl.UtilityRepo;
import com.assignsecurities.service.impl.ApplicationPropertiesService;
import com.assignsecurities.service.impl.CaseService;

public class FIRCopyTemplateProcessor extends TemplateProcessorAbstract implements TemplateProcessor {


	@Autowired
	private DocumentDao documentDao;
	
	@Autowired
	private CaseDocumentDao caseDocumentDao;
	
	@Autowired
	private CaseService caseService;
	
	
	@Autowired
	private CaseQuestionerRepo caseQuestionerRepo;
	

	@Autowired
	private CaseGeneratedDocumentsRequiredRepo caseGeneratedDocumentsRequiredRepo;
	
	@Autowired
	private UtilityRepo utilityRepo;
	
	@Autowired 
	private EmailReposatory emailReposatory;
	
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Throwable.class)
	public FileBean processTemplate(CaseDocumentBean caseDocumentBean, UserLoginBean userLoginBean,
			CaseModel caseModel) {
		Long caseId = caseModel.getId();
//		RtaDataModel rtaDataModel = rtaDataRepo.getRtaDataByCompanyName(caseModel.getCompanyName());
//		RtaTemplateModel rtaTemplateModel1 = getRtaTemplate(rtaDataModel.getRegistrarName(), caseDocumentBean.getTemplateType());
//		RtaTemplateModel rtaTemplateModel = RtaTemplateModel.builder().templateType(caseDocumentBean.getTemplateType())
//				.templateName(caseDocumentBean.getTemplateType()).build();
		
		RtaTemplateModel rtaTemplateModel = RtaTemplateModel.builder()
				.templateName(caseDocumentBean.getTemplateType())
				.templateType(caseDocumentBean.getTemplateType()).build();
		String docTemplatePath = loadTemplatePath(rtaTemplateModel);
		String finalPdfFileName = generateDestFileName(rtaTemplateModel, userLoginBean, caseModel);
//		String docTemplatePath = getTemplatePath(questionerModel);
		String templateName =docTemplatePath.substring(docTemplatePath.lastIndexOf("/")+1);
		rtaTemplateModel.setTemplateName(templateName);
//		String finalPdfFileName = generateDestFileName(rtaTemplateModel, userLoginBean, caseModel);
		
		CaseTemplateBean caseTemplateBean = caseService.getCaseTemplateBean(caseModel, caseId, templateName);
		 Boolean storeDoc = !ArgumentHelper.isTrueFAlse(caseTemplateBean.getLetterPrviewOnly());
		if (storeDoc) {
			String bucketName = ApplicationPropertiesService
					.getPropertyStringValue(PropertyKeys.AZ_S3_BUCKET_NAME);
			DocumentModel documentModel = DocumentModel.builder().name(finalPdfFileName)
					.createdDate(LocalDateTime.now()).createBy(userLoginBean.getId())
					.type(AppConstant.DocumentType.FIRCopy.name())
					.contentType(AppConstant.JAVA_SCRIPT_DATA_PDF_BASE64).bucketName(bucketName).build();
			List<CaseDocumentModel> caseDocumentModels = caseDocumentDao
					.getByCaseIdAndDocType(caseId, AppConstant.DocumentType.FIRCopy.name());
			if(ArgumentHelper.isNotEmpty(caseDocumentModels)) {
				caseDocumentDao.deleteByCaseIdAndType(caseId, AppConstant.DocumentType.FIRCopy.name());
				List<Long> oldDocIds = caseDocumentModels.stream().map(doc->doc.getDocumentId()).collect(Collectors.toList());
				caseGeneratedDocumentsRequiredRepo.deleteDocIds(oldDocIds);
				emailReposatory.deleteByDocIds(oldDocIds);
				documentDao.deleteByIds(oldDocIds);
			}
			
			Long dcoumentId = documentDao.add(documentModel);
			documentModel.setId(dcoumentId);
			caseService.intsertCaseDocument(caseModel.getId(), documentModel, caseDocumentBean.getUploadType());
			DocumentMasterModel documentMasterModel =  utilityRepo.getMasterDocument(AppConstant.DocumentType.FIRCopy.label);
			caseGeneratedDocumentsRequiredRepo.add(GeneratedDocumentsRequiredModel.builder().caseId(caseId)
				.documentMasterId(documentMasterModel.getId()).documentId(dcoumentId).build());
//			caseService.updateStatus(CaseStatusUpdateBean.builder().caseId(caseId)
//					.status(AppConstant.CaseStatus.WaitingSignedDocumentsUpload.label).build(), userLoginBean);
		}

		FileBean fileBean = storeAgreementAsPdf(docTemplatePath, rtaTemplateModel, caseTemplateBean, caseTemplateBean,
				finalPdfFileName,storeDoc);
		//TODO SEnd Email on RTA 2 Letter Generation
		return fileBean;
	}

	private String getTemplatePath(CaseQuestionerModel questionerModel) {
		String docTemplatePath = getTemplateRootPath();
		docTemplatePath = docTemplatePath + "/Physical RTA.docx";
		return docTemplatePath;
	}

}
