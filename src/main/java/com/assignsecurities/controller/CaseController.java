package com.assignsecurities.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.assignsecurities.bean.CaseBean;
import com.assignsecurities.bean.CaseCancelChequeBean;
import com.assignsecurities.bean.CaseDeathCertificateDtlBean;
import com.assignsecurities.bean.CaseDocumentBean;
import com.assignsecurities.bean.CaseDocumentTemplateBean;
import com.assignsecurities.bean.CaseFranchiseBean;
import com.assignsecurities.bean.CaseKYCBean;
import com.assignsecurities.bean.CaseStatusUpdateBean;
import com.assignsecurities.bean.CaseUpdateBean;
import com.assignsecurities.bean.CustomerCasePendingDetails;
import com.assignsecurities.bean.CustomerCasePendingFolioDetails;
import com.assignsecurities.bean.CustomerKYCConfirmationBean;
import com.assignsecurities.bean.DeleteCaseDocumentBean;
import com.assignsecurities.bean.DocumentMasterBean;
import com.assignsecurities.bean.FileBean;
import com.assignsecurities.bean.LawyerCaseCommentsDtlBean;
import com.assignsecurities.bean.LawyerCaseDetailsBean;
import com.assignsecurities.bean.ProcessApplicationBean;
import com.assignsecurities.bean.UploadCaseDocument;
import com.assignsecurities.bean.UserLoginBean;
import com.assignsecurities.domain.CaseFieldsModel;
import com.assignsecurities.service.impl.CaseDeathCertificateDtlService;
import com.assignsecurities.service.impl.CaseService;
import com.assignsecurities.service.impl.LawyerCaseCommentsDtlService;

@RestController
public class CaseController extends BaseController {

	@Autowired
	private CaseService caseService;
	
	@Autowired
	private LawyerCaseCommentsDtlService lawyerCaseCommentsDtlService;
	

	
	@Autowired
	private CaseDeathCertificateDtlService caseDeathCertificateDtlService;

	@RequestMapping(value = "/processApplication", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public CaseBean processApplication(@Validated @RequestBody ProcessApplicationBean processApplicationBean) {
		UserLoginBean userLoginBean = getUser();
		CaseValidator.validateProcessApplication(processApplicationBean);
		return caseService.createCaseFromApplication(processApplicationBean, userLoginBean);
	}

//	@RequestMapping(value = "/saveAadharDetails", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
//	public CaseBean saveAadharDetails(@Validated @RequestBody CaseAadharBean caseAadharBean) {
//		UserLoginBean userLoginBean = getUser();
//		CaseValidator.validateAadharDetails(caseAadharBean);
//		return caseService.saveAadharDetails(caseAadharBean, userLoginBean);
//	}

//	@RequestMapping(value = "/savePanDetails", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
//	public CaseBean savePanDetails(@Validated @RequestBody CasePanBean casePanBean) {
//		UserLoginBean userLoginBean = getUser();
//		CaseValidator.validatePanDetails(casePanBean);
//		return caseService.savePanDetails(casePanBean, userLoginBean);
//	}

	@RequestMapping(value = "/saveCancelChequeDetails", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public CaseBean saveCancelChequeDetails(@Validated @RequestBody CaseCancelChequeBean caseCancelChequeBean) {
		UserLoginBean userLoginBean = getUser();
		CaseValidator.validateCancelChequeDetails(caseCancelChequeBean);
		return caseService.saveCancelChequeDetails(caseCancelChequeBean, userLoginBean);
	}

	@RequestMapping(value = "/downloadAggrement/{caseId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public FileBean downloadAggrement(@PathVariable("caseId") Long caseId) {
		UserLoginBean userLoginBean = getUser();
		return caseService.downloadAggrement(caseId, userLoginBean);
	}
	
	
	@RequestMapping(value = "/continueAndSaveCase", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public CaseBean continueAndSaveCase(@Validated @RequestBody CaseKYCBean caseKYCBean) {
		UserLoginBean userLoginBean = getUser();
		CaseValidator.validateContinueAndSaveCase(caseKYCBean);
		return caseService.continueAndSaveCase(caseKYCBean, userLoginBean);
	}

	@RequestMapping(value = "/customerKycConfirmation", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public CaseBean customerKycConfirmation(
			@Validated @RequestBody CustomerKYCConfirmationBean customerKycConfirmation) {
		UserLoginBean userLoginBean = getUser();
		CaseValidator.validateCustomerKycConfirmation(customerKycConfirmation);
		return caseService.customerKycConfirmation(customerKycConfirmation, userLoginBean);
	}

	@RequestMapping(value = "/saveCustomerCasePendingDetails", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public CaseBean saveCustomerCasePendingDetails(@Validated @RequestBody CustomerCasePendingDetails details) {
		UserLoginBean userLoginBean = getUser();
		CaseValidator.validateCustomerCasePendingDetails(details);
		return caseService.saveCustomerCasePendingDetails(details, userLoginBean);
	}

	@RequestMapping(value = "/saveCustomerCasePendingFolioDetails", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Void> saveCustomerCasePendingFolioDetails(
			@Validated @RequestBody CustomerCasePendingFolioDetails details) {
		UserLoginBean userLoginBean = getUser();
		CaseValidator.validateCustomerCasePendingFolioDetails(details);
		caseService.saveCustomerCasePendingFolioDetails(details, userLoginBean);
		return ResponseEntity.ok().build();
	}

	@RequestMapping(value = "/generateCaseDocument", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public FileBean generateCaseDocument(@Validated @RequestBody CaseDocumentBean caseDocumentBean) {
		UserLoginBean userLoginBean = getUser();
		CaseValidator.validateGenerateCaseDocument(caseDocumentBean);
		return caseService.generateCaseDocument(caseDocumentBean, userLoginBean);
	}

	@RequestMapping(value = "/cases/{stage}/{status}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public List<CaseBean> getcases(@PathVariable("stage") String stage,@PathVariable("status") String status) {
		UserLoginBean userLoginBean = getUser();
		return caseService.getCasesByStatuses(stage, status, userLoginBean);
	}

	@RequestMapping(value = "/caseById/{caseId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public CaseBean getCase(@PathVariable("caseId") Long caseId) {
		UserLoginBean userLoginBean = getUser();
		return caseService.getById(caseId, userLoginBean);
	}
	@RequestMapping(value = "/updateCase", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Void> updateCase(@Validated @RequestBody CaseUpdateBean caseUpdateBean) {
		UserLoginBean userLoginBean = getUser();
		caseService.updateCase(caseUpdateBean, userLoginBean);
		return ResponseEntity.ok().build();
	}
	
	@RequestMapping(value = "/cases/ReferenceNumber/{referenceNumber}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public List<CaseBean> getByReferenceNumber(@PathVariable("referenceNumber") String referenceNumber) {
		UserLoginBean userLoginBean = getUser();
		return caseService.getByReferenceNumber(referenceNumber, userLoginBean);
	}
	
	
	@RequestMapping(value = "/assignedFranchise", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public CaseBean assignedFranchise(@Validated @RequestBody CaseFranchiseBean caseFranchiseBean) {
		UserLoginBean userLoginBean = getUser();
		return caseService.saveFranchiseDetails(caseFranchiseBean, userLoginBean);
	}
	
	@RequestMapping(value = "/updateStatus", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Void> updateStatus(@Validated @RequestBody CaseStatusUpdateBean updateBean) {
		UserLoginBean userLoginBean = getUser();
		caseService.updateStatus(updateBean, userLoginBean);
		return ResponseEntity.ok().build();
	}
	
	
	@RequestMapping(value = "/uploadCaseDocument", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Long uploadCaseDocument(@Validated @RequestBody UploadCaseDocument uploadCaseDocument) {
		UserLoginBean userLoginBean = getUser();
		CaseValidator.validateUploadCaseDocument(uploadCaseDocument);
		return caseService.uploadCaseDocument(uploadCaseDocument, userLoginBean);
	}
	
	@RequestMapping(value = "/deleteCaseDocument", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Long deleteCaseDocument(@Validated @RequestBody DeleteCaseDocumentBean deleteCaseDocumentBean) {
		UserLoginBean userLoginBean = getUser();
		CaseValidator.validatedeleteCaseDocument(deleteCaseDocumentBean);
		return caseService.deleteCaseDocument(deleteCaseDocumentBean, userLoginBean);
	}
	
	@RequestMapping(value = "/getCaseDocumentTemplates/{caseId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public CaseDocumentTemplateBean getCaseDocumentTemplates(@PathVariable("caseId") Long caseId) {
		UserLoginBean userLoginBean = getUser();
		return caseService.getCaseDocumentTemplates(caseId, userLoginBean);
	}
	
	/*
	@RequestMapping(value = "/saveMadatoryDocument", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Long saveMadatoryDocument(@Validated @RequestBody CaseDocumentUploadBean caseDocumentUploadBean) {
		UserLoginBean userLoginBean = getUser();
		CaseValidator.validateMadatoryOrSupplementaryDocument(caseDocumentUploadBean);
		return caseService.saveMadatoryDocument(caseDocumentUploadBean, userLoginBean);
	}
	
	@RequestMapping(value = "/saveSupplementaryDocument", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Long saveSupplementaryDocument(@Validated @RequestBody CaseDocumentUploadBean caseDocumentUploadBean) {
		UserLoginBean userLoginBean = getUser();
		CaseValidator.validateMadatoryOrSupplementaryDocument(caseDocumentUploadBean);
		return caseService.saveSupplementaryDocument(caseDocumentUploadBean, userLoginBean);
	}
	*/
	
	@RequestMapping(value = "/addCaseShareHolderDeathCertificateDtl", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Long addCaseShareHolderDeathCertificateDtl(@Validated @RequestBody CaseDeathCertificateDtlBean caseDeathCertificateDtlBean) {
		UserLoginBean userLoginBean = getUser();
		CaseValidator.validateCaseShareHolderDeathCertDtl(caseDeathCertificateDtlBean);
		return caseDeathCertificateDtlService.addCaseShareHolderDeathCertificateDtl(caseDeathCertificateDtlBean, userLoginBean);
	}
	
	@RequestMapping(value = "/getUploadeSignedDocuments/{caseId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public List<UploadCaseDocument> getUploadeSignedDocuments(@PathVariable("caseId") Long caseId) {
		UserLoginBean userLoginBean = getUser();
		return caseService.getUploadeSignedDocuments(caseId, userLoginBean);
	}
	
	@RequestMapping(value = "/getUploadRTAResponse/{caseId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public List<UploadCaseDocument> getUploadRTAResponse(@PathVariable("caseId") Long caseId) {
		UserLoginBean userLoginBean = getUser();
		return caseService.getUploadRTAResponse(caseId, userLoginBean);
	}
	
	@RequestMapping(value = "getMasterDocumentListStage1/{uploadedOrGenerated}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public List<DocumentMasterBean> getMasterDocumentListStage1(@PathVariable("uploadedOrGenerated") String uploadedOrGenerated,
			@RequestParam(value = "caseId", required = false) Long caseId) {
		UserLoginBean userLoginBean = getUser();
		return caseService.getMasterDocumentList(uploadedOrGenerated, caseId, userLoginBean);
	}
	
	@RequestMapping(value = "getMasterDocumentListForRta2Preveiw/{caseId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public List<DocumentMasterBean> getMasterDocumentListForRta2Preveiw(
			@PathVariable("caseId") Long caseId) {
		return caseService.getMasterDocumentListForRta2Preveiw(caseId);
	}
	
	@RequestMapping(value = "getAllLawyerCaseCommentsDtls/{caseId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public List<LawyerCaseCommentsDtlBean> getAllLawyerCaseCommentsDtls(
			@PathVariable("caseId") Long caseId) {
		return lawyerCaseCommentsDtlService.getAllLawyerCaseCommentsDtls(caseId);
	}
	
	@RequestMapping(value = "/addLawyerCaseCommentsDtl", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Long addLawyerCaseCommentsDtl(@Validated @RequestBody LawyerCaseCommentsDtlBean lawyerCaseCommentsDtlBean) {
		UserLoginBean userLoginBean = getUser();
//		CaseValidator.validateCaseShareHolderDeathCertDtl(caseDeathCertificateDtlBean);
		return lawyerCaseCommentsDtlService.add(lawyerCaseCommentsDtlBean,userLoginBean);
	}
	
	@RequestMapping(value = "/getLawyerCaseCommentsDtl/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public LawyerCaseCommentsDtlBean getLawyerCaseCommentsDtl(@PathVariable("id") Long id) {
//		UserLoginBean userLoginBean = getUser();
		return lawyerCaseCommentsDtlService.getLawyerCommentDtlById(id);
	}
	
	
	
	
	@RequestMapping(value = "/updateLawyerCaseDetails", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Void> updateLawyerCaseDetails(@Validated @RequestBody LawyerCaseDetailsBean lawyerCaseDetailsBean) {
		UserLoginBean userLoginBean = getUser();
		caseService.updateLawyerCaseDetails(lawyerCaseDetailsBean, userLoginBean);
		return ResponseEntity.ok().build();
	}
	@RequestMapping(value = "getFieldsByCaseId/{caseId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public List<CaseFieldsModel> getFieldsByCaseId(@PathVariable("caseId") Long caseId) {
		return caseService.getFieldsByCaseId(caseId);
	}
	
	
	@RequestMapping(value = "/getLawyerCaseDtlByCaseId/{caseId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public LawyerCaseDetailsBean getLawyerCaseDtlByCaseId(@PathVariable("caseId") Long caseId) {
		UserLoginBean userLoginBean = getUser();
		return caseService.getLawyerCaseDtlByCaseId(caseId, userLoginBean);
	}
}
