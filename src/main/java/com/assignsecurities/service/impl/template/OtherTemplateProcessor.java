package com.assignsecurities.service.impl.template;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.assignsecurities.app.AppConstant;
import com.assignsecurities.app.util.ArgumentHelper;
import com.assignsecurities.bean.ApplicationUserBean;
import com.assignsecurities.bean.CaseDocumentBean;
import com.assignsecurities.bean.FileBean;
import com.assignsecurities.bean.UserLoginBean;
import com.assignsecurities.dm.PropertyKeys;
import com.assignsecurities.domain.CaseDocumentModel;
import com.assignsecurities.domain.CaseFieldsModel;
import com.assignsecurities.domain.CaseModel;
import com.assignsecurities.domain.CaseTemplateBean;
import com.assignsecurities.domain.DocumentMasterModel;
import com.assignsecurities.domain.DocumentModel;
import com.assignsecurities.domain.GeneratedDocumentsRequiredModel;
import com.assignsecurities.domain.RtaDataModel;
import com.assignsecurities.domain.RtaTemplateModel;
import com.assignsecurities.repository.impl.CaseDocumentDao;
import com.assignsecurities.repository.impl.CaseFieldsRepo;
import com.assignsecurities.repository.impl.CaseGeneratedDocumentsRequiredRepo;
import com.assignsecurities.repository.impl.DocumentDao;
import com.assignsecurities.repository.impl.EmailReposatory;
import com.assignsecurities.repository.impl.RtaDataRepo;
import com.assignsecurities.repository.impl.UtilityRepo;
import com.assignsecurities.service.impl.ApplicationPropertiesService;
import com.assignsecurities.service.impl.ApplicationUserService;
import com.assignsecurities.service.impl.CaseService;
import com.assignsecurities.service.impl.LoginService;
import com.assignsecurities.service.impl.doc.processor.DocTemplateEnum;

public class OtherTemplateProcessor extends TemplateProcessorAbstract implements TemplateProcessor {

	@Autowired
	private DocumentDao documentDao;

	@Autowired
	private CaseDocumentDao caseDocumentDao;

	@Autowired
	private CaseService caseService;

	@Autowired
	private RtaDataRepo rtaDataRepo;

	@Autowired
	private CaseGeneratedDocumentsRequiredRepo caseGeneratedDocumentsRequiredRepo;

	@Autowired
	private UtilityRepo utilityRepo;

	@Autowired
	private EmailReposatory emailReposatory;

	@Autowired
	private CaseFieldsRepo caseFieldsRepo;
	
	@Autowired
	private ApplicationUserService  applicationUserService;
	
	@Autowired
	private LoginService  loginService;

	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Throwable.class)
	public FileBean processTemplate(CaseDocumentBean caseDocumentBean, UserLoginBean userLoginBean,
			CaseModel caseModel) {
		Long caseId = caseModel.getId();

//		${FolioTable} Company Name, Folio Number,Distinctive Nos (From-), Distinctive Nos  (To-), Certificate No.
		RtaDataModel rtaDataModel = rtaDataRepo.getRtaDataByCompanyName(caseModel.getCompanyName());
		RtaTemplateModel rtaTemplateModel = getRtaTemplate(rtaDataModel.getRegistrarName(),
				caseDocumentBean.getTemplateType());
		System.out.println("RegistrarName===>"+rtaDataModel.getRegistrarName());
//		RtaTemplateModel rtaTemplateModel = RtaTemplateModel.builder().templateType(caseDocumentBean.getTemplateType())
//				.templateName(caseDocumentBean.getTemplateType()).build();

		String docTemplatePath = getTemplatePath(rtaTemplateModel);
		String templateName = docTemplatePath.substring(docTemplatePath.lastIndexOf("/") + 1);
		rtaTemplateModel.setTemplateName(templateName);

		String finalPdfFileName = generateDestFileName(rtaTemplateModel, userLoginBean, caseModel);
		CaseTemplateBean caseTemplateBean = caseService.getCaseTemplateBean(caseModel, caseId, templateName);
		List<CaseFieldsModel> caseFieldsModels = caseFieldsRepo.getByCaseId(caseId);
		if(ArgumentHelper.isNotEmpty(caseFieldsModels)) {
			Map<String, String> caseFieldsKeyValues = new HashMap<String, String>();
			caseFieldsModels.forEach(field->{
				if(ArgumentHelper.isValid(field.getField())) {
					String key = field.getField().replaceAll(" ", "");
					key = key.replaceAll("null", "");
//					System.out.println("111111111111===>key=>"+key);
//					System.out.println("2222222222222===>key.replaceAll=>"+key.replaceAll(" ", ""));
					caseFieldsKeyValues.put(key, field.getFieldValue());
				}
			});
			caseTemplateBean.setCaseFieldsKeyValues(caseFieldsKeyValues);
		}
		System.out.println("caseFieldsKeyValues===>"+caseTemplateBean.getCaseFieldsKeyValues());
		UserLoginBean userLoginBeanObj = loginService.getUserLogin(caseModel.getUserId());
		ApplicationUserBean applicationUserBean = userLoginBeanObj.getApplicationUserBean();
		if(Objects.isNull(applicationUserBean)) {
			applicationUserBean = applicationUserService.getApplicationUserById(userLoginBeanObj.getApplicationUserId());
		}
		caseTemplateBean.setApplicationUserBean(applicationUserBean);
		
		String bucketName = ApplicationPropertiesService.getPropertyStringValue(PropertyKeys.AZ_S3_BUCKET_NAME);
		DocumentModel documentModel = DocumentModel.builder().name(finalPdfFileName).createdDate(LocalDateTime.now())
				.createBy(userLoginBean.getId()).type(caseDocumentBean.getTemplateType())
				.contentType(AppConstant.JAVA_SCRIPT_DATA_PDF_BASE64).bucketName(bucketName).build();
		List<CaseDocumentModel> caseDocumentModels = caseDocumentDao.getByCaseIdAndDocType(caseId,
				caseDocumentBean.getTemplateType());
		DocumentMasterModel documentMasterModel = utilityRepo.getMasterDocumentAndRtaGroup(caseDocumentBean.getTemplateType(), rtaTemplateModel.getName());
		if (ArgumentHelper.isNotEmpty(caseDocumentModels)) {
			caseDocumentDao.deleteByCaseIdAndType(caseId, caseDocumentBean.getTemplateType());
			List<Long> oldDocIds = caseDocumentModels.stream().map(doc -> doc.getDocumentId())
					.collect(Collectors.toList());
			caseGeneratedDocumentsRequiredRepo.deleteDocIds(oldDocIds);
			emailReposatory.deleteByDocIds(oldDocIds);
			documentDao.deleteByIds(oldDocIds);
		}
		caseGeneratedDocumentsRequiredRepo.deleteDocCaseMasterDocId(caseId,documentMasterModel.getId());
		Long dcoumentId = documentDao.add(documentModel);
		documentModel.setId(dcoumentId);
		caseService.intsertCaseDocument(caseModel.getId(), documentModel, caseDocumentBean.getUploadType());
		caseGeneratedDocumentsRequiredRepo.add(GeneratedDocumentsRequiredModel.builder().caseId(caseId)
				.documentMasterId(documentMasterModel.getId()).documentId(dcoumentId).build());
		
		//Set this for handling all the other doc teplate process
		rtaTemplateModel.setTemplateType(DocTemplateEnum.Other.name());
		FileBean fileBean = storeAgreementAsPdf(docTemplatePath, rtaTemplateModel, caseTemplateBean, caseTemplateBean,
				finalPdfFileName, Boolean.TRUE);
		return fileBean;
	}

	private String getTemplatePath(RtaTemplateModel rtaTemplateModel) {
		String docTemplatePath = getTemplateRootPath();
		docTemplatePath = docTemplatePath + rtaTemplateModel.getTemplateName();
		return docTemplatePath;
	}

}
