package com.assignsecurities.service.impl.template;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.assignsecurities.app.AppConstant;
import com.assignsecurities.app.util.ArgumentHelper;
import com.assignsecurities.bean.CaseDocumentBean;
import com.assignsecurities.bean.CaseStatusUpdateBean;
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
import com.assignsecurities.domain.QuestionerModel;
import com.assignsecurities.domain.RtaTemplateModel;
import com.assignsecurities.repository.impl.CaseDocumentDao;
import com.assignsecurities.repository.impl.CaseGeneratedDocumentsRequiredRepo;
import com.assignsecurities.repository.impl.CaseQuestionerRepo;
import com.assignsecurities.repository.impl.DocumentDao;
import com.assignsecurities.repository.impl.EmailReposatory;
import com.assignsecurities.repository.impl.UtilityRepo;
import com.assignsecurities.service.impl.ApplicationPropertiesService;
import com.assignsecurities.service.impl.CaseService;

public class RtaLetterTemplateProcessor extends TemplateProcessorAbstract implements TemplateProcessor {


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
//		${RtaName} 
//		${RtaAddress} 
//		${CurrentDate}  
//		${FolioNo}
//		${CompanyName}  
//		(${RelationWithDeceased})
//		${DeceasedName1}
//		${NoOfShares} --->script.getScriptModel().getNumberOfShare()
//		${CompanyNname}
//		${FolioNos}
//		${DODOfDeceased1}
//		${CommunicationAddress}
//		${ApplicantName}
//		${ApplicantBankName}
//		${ApplicantBankAccountNumber}
//		${ApplicantBankIFSCCode}
//		${ApplicantBankAddress}
//
//		${FolioTable} Company Name, Folio Number,Distinctive Nos (From-), Distinctive Nos  (To-), Certificate No.
//		RtaDataModel rtaDataModel = rtaDataRepo.getRtaDataByCompanyName(caseModel.getCompanyName());
//		RtaTemplateModel rtaTemplateModel1 = getRtaTemplate(rtaDataModel.getRegistrarName(), caseDocumentBean.getTemplateType());
		RtaTemplateModel rtaTemplateModel = RtaTemplateModel.builder().templateType(caseDocumentBean.getTemplateType())
				.templateName(caseDocumentBean.getTemplateType()).build();
		CaseQuestionerModel questionerModel=  caseQuestionerRepo.getDistinctQuestionerAnsByCaseId(caseModel.getId());
		
		String docTemplatePath = getTemplatePath(questionerModel);
		String templateName =docTemplatePath.substring(docTemplatePath.lastIndexOf("/")+1);
		rtaTemplateModel.setTemplateName(templateName);
		
		String finalPdfFileName = generateDestFileName(rtaTemplateModel, userLoginBean, caseModel);
		
		CaseTemplateBean caseTemplateBean = caseService.getCaseTemplateBean(caseModel, caseId, templateName);
		 Boolean storeDoc = !caseTemplateBean.isMaskFolio() && !ArgumentHelper.isTrueFAlse(caseTemplateBean.getRtaLetter2PrviewOnly())
				 && !ArgumentHelper.isTrueFAlse(caseTemplateBean.getRtaLetter1PrviewOnly());
		if (storeDoc) {
			String bucketName = ApplicationPropertiesService
					.getPropertyStringValue(PropertyKeys.AZ_S3_BUCKET_NAME);
			DocumentModel documentModel = DocumentModel.builder().name(finalPdfFileName)
					.createdDate(LocalDateTime.now()).createBy(userLoginBean.getId())
					.type(AppConstant.DocumentType.RtaLetter1.name())
					.contentType(AppConstant.JAVA_SCRIPT_DATA_PDF_BASE64).bucketName(bucketName).build();
			List<CaseDocumentModel> caseDocumentModels = caseDocumentDao
					.getByCaseIdAndDocType(caseId, AppConstant.DocumentType.RtaLetter1.name());
			if(ArgumentHelper.isNotEmpty(caseDocumentModels)) {
				caseDocumentDao.deleteByCaseIdAndType(caseId, AppConstant.DocumentType.RtaLetter1.name());
				List<Long> oldDocIds = caseDocumentModels.stream().map(doc->doc.getDocumentId()).collect(Collectors.toList());
				caseGeneratedDocumentsRequiredRepo.deleteDocIds(oldDocIds);
				emailReposatory.deleteByDocIds(oldDocIds);
				documentDao.deleteByIds(oldDocIds);
			}
			
			Long dcoumentId = documentDao.add(documentModel);
			documentModel.setId(dcoumentId);
			caseService.intsertCaseDocument(caseModel.getId(), documentModel, caseDocumentBean.getUploadType());
			DocumentMasterModel documentMasterModel =  utilityRepo.getMasterDocument(AppConstant.DocumentType.RtaLetter1.name());
			caseGeneratedDocumentsRequiredRepo.add(GeneratedDocumentsRequiredModel.builder().caseId(caseId)
				.documentMasterId(documentMasterModel.getId()).documentId(dcoumentId).build());
			caseService.updateStatus(CaseStatusUpdateBean.builder().caseId(caseId)
					.status(AppConstant.CaseStatus.WaitingSignedDocumentsUpload.label).build(), userLoginBean);
		}

		FileBean fileBean = storeAgreementAsPdf(docTemplatePath, rtaTemplateModel, caseTemplateBean, caseTemplateBean,
				finalPdfFileName,storeDoc);
		//TODO SEnd Email on RTA 2 Letter Generation
		return fileBean;
	}

	

	private String getTemplatePath(CaseQuestionerModel questionerModel) {
//		Q.1 : Are you seeking shares in your name ?
//		Q.1.1 : Are you the legal heir to the shareholder ?
//		Q.1.2 : Are your shares worth less than 2 lakh Rupees ?
//		Q.2 : Are their other joint holders ?
//		Q.3 : Have you updated your KYC with RTA in recent past ?
//		Q.4 : Does the name of the share holder differ on Share Certificate and PAN/Death Certificate ?
//		Q.5 : Have you lost your share certificate(s) ?
//		Q.6 : Have you mistakenly missed ALL your dividends FROM the company IN the LAST 7 years ?
		String docTemplatePath = getTemplateRootPath();
		List<QuestionerModel> questioners = questionerModel.getQuestioners();
//		Boolean dead = Boolean.FALSE;
//		Boolean lost = Boolean.FALSE;
//		Boolean missedDividend = Boolean.FALSE;
//		Boolean joint = Boolean.FALSE;
		
		
		Optional<QuestionerModel> quesQ1DeadOpt = questioners.stream().filter(q->q.getQuestion().contains("Q.1 :")
				&& (q.getAnswer().equalsIgnoreCase("Yes") || q.getAnswer().equalsIgnoreCase("Y"))).findFirst();
		Optional<QuestionerModel> quesQ5LostsOpt = questioners.stream().filter(q->q.getQuestion().contains("Q.5")
				&& (q.getAnswer().equalsIgnoreCase("Yes") || q.getAnswer().equalsIgnoreCase("Y"))).findFirst();
		Optional<QuestionerModel> quesQ6MissedDividendOpt = questioners.stream().filter(q->q.getQuestion().contains("Q.6")
				&& (q.getAnswer().equalsIgnoreCase("Yes") || q.getAnswer().equalsIgnoreCase("Y"))).findFirst();
		Optional<QuestionerModel> quesQ6JointOpt = questioners.stream().filter(q->q.getQuestion().contains("Q.2")
				&& (q.getAnswer().equalsIgnoreCase("Yes") || q.getAnswer().equalsIgnoreCase("Y"))).findFirst();
		
		String finalDocTemplatePath = docTemplatePath + "/B1+A3 Live Lost Shares.docx";
		if(quesQ6JointOpt.isPresent()) {
			//Joint Holder
			if(quesQ1DeadOpt.isPresent()) {
				//Dead
				if(quesQ5LostsOpt.isPresent()) {
					//Lost
					if(quesQ6MissedDividendOpt.isPresent()) {
						//Missed Dividend
						finalDocTemplatePath = docTemplatePath + "/JointB1+A1 Deceased Lost Shares.docx";
					} else {
						//Has Dividend
						finalDocTemplatePath = docTemplatePath + "/JointB2+A1 Deceased Lost Shares & Warrant Expired.docx";
					}
				}else {
					//Physical
					if(quesQ6MissedDividendOpt.isPresent()) {
						//Missed Dividend
						finalDocTemplatePath = docTemplatePath + "/JointB2+A4 Live Physical Shares & Warrant Expired.docx";
					} else {
						//Has Dividend
						finalDocTemplatePath = docTemplatePath + "/JointB1+A2 Deceased Physical Shares.docx";
					}
				}
			} else {
				//Live
				if(quesQ5LostsOpt.isPresent()) {
					//Lost
					if(quesQ6MissedDividendOpt.isPresent()) {
						//Missed Dividend
						finalDocTemplatePath = docTemplatePath + "/JointB2+A3 Live Lost Shares & Warrant Expired.docx";
					} else {
						//Has Dividend
						finalDocTemplatePath = docTemplatePath + "/JointB1+A3 Live Lost Shares.docx";
					}
				}else {
					//Physical
					if(quesQ6MissedDividendOpt.isPresent()) {
						//Missed Dividend
						finalDocTemplatePath = docTemplatePath + "/JointB2+A2 Deceased Physical Shares & Warrant Expired.docx";
					} else {
						//Has Dividend
						finalDocTemplatePath = docTemplatePath + "/JointB1+A4 Live Physical Shares.docx";
					}
				}
			}
		} else {
			//Not a Joint Holder
			if(quesQ1DeadOpt.isPresent()) {
				//Dead
				if(quesQ5LostsOpt.isPresent()) {
					//Lost
					if(quesQ6MissedDividendOpt.isPresent()) {
						//Missed Dividend -- Dead-Lost-Missed Dividend
						finalDocTemplatePath = docTemplatePath + "/B1+A1 Deceased Lost Shares.docx";
					} else {
						//Has Dividend -- Dead-Lost-Has Dividend
						finalDocTemplatePath = docTemplatePath + "/B2+A1 Deceased Lost Shares & Warrant Expired.docx";
					}
				}else {
					//Physical 
					if(quesQ6MissedDividendOpt.isPresent()) {
						//Missed Dividend -- Dead-Physical-Missed Dividend
						finalDocTemplatePath = docTemplatePath + "/B2+A2 Deceased Physical Shares & Warrant Expired.docx";
					} else {
						//Has Dividend -- Dead-Physical-Has Dividend
						finalDocTemplatePath = docTemplatePath + "/B1+A2 Deceased Physical Shares.docx";
					}
				}
			} else {
				//Live
				if(quesQ5LostsOpt.isPresent()) {
					//Lost
					if(quesQ6MissedDividendOpt.isPresent()) {
						//Missed Dividend -- Live-Lost-Missed Dividend
						finalDocTemplatePath = docTemplatePath + "/B2+A3 Live Lost Shares & Warrant Expired.docx";
					} else {
						//Has Dividend -- Live-Lost-Has Dividend
						finalDocTemplatePath = docTemplatePath + "/B1+A3 Live Lost Shares.docx";
					}
				}else {
					//Physical
					if(quesQ6MissedDividendOpt.isPresent()) {
						//Missed Dividend -- Live-Physical-Missed Dividend
						finalDocTemplatePath = docTemplatePath + "/B2+A4 Live Physical Shares & Warrant Expired.docx";
					} else {
						//Has Dividend -- Live-Physical-Has Dividend
						finalDocTemplatePath = docTemplatePath + "/B1+A4 Live Physical Shares.docx";
					}
				}
			}
		}
//		finalDocTemplatePath = docTemplatePath + "/B2+A2 Deceased Physical Shares & Warrant Expired.docx";
		
		return finalDocTemplatePath;
	}

}
