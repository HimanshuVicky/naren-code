package com.assignsecurities.controller;

import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.assignsecurities.app.exception.ValidationException;
import com.assignsecurities.bean.CaseAadharGenerateOtpBean;
import com.assignsecurities.bean.CasePanVerifyBean;
import com.assignsecurities.bean.ESigningResponseBean;
import com.assignsecurities.bean.GetDocumentRequestBean;
import com.assignsecurities.bean.KycOtpRequestBean;
import com.assignsecurities.bean.KycResponseBean;
import com.assignsecurities.bean.UserLoginBean;
import com.assignsecurities.bean.ValidationError;
import com.assignsecurities.service.impl.CaseKycService;
import com.assignsecurities.service.impl.CaseService;

@RestController
public class CaseKycController extends BaseController {

	@Autowired
	private CaseKycService caseKycService;
	
	@Autowired
	private CaseService caseService;

//	Aadhaar OCR, Generate OTP 
//	
	
	@RequestMapping(value = "/generateAadharOtp", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public KycResponseBean generateOtp(@Validated @RequestBody CaseAadharGenerateOtpBean aadharGenerateOtpBean, BindingResult binding) {
		if(binding.hasErrors()) {
			throw new ValidationException("Invalid input char used, please correct and try.", ValidationError.builder().build());
	      }
		UserLoginBean userLoginBean = getUser();
		CaseValidator.validateGenerateOtp(aadharGenerateOtpBean);
		KycResponseBean kycResponseBean=  caseKycService.generateOtp(aadharGenerateOtpBean, userLoginBean);
		return kycResponseBean;
	}
	
	@RequestMapping(value = "/submitAadharOtp", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Void> submitAadharOtp(@Validated @RequestBody KycOtpRequestBean kycOtpRequestBean, BindingResult binding) {
		if(binding.hasErrors()) {
			throw new ValidationException("Invalid input char used, please correct and try.", ValidationError.builder().build());
	      }
		UserLoginBean userLoginBean = getUser();
		CaseValidator.validateGenerateOtp(kycOtpRequestBean);
		caseKycService.submitAadharOtp(kycOtpRequestBean, userLoginBean);
		return ResponseEntity.ok().build();
	}
	
	@RequestMapping(value = "/panVerify", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Void>  panVerify(@Validated @RequestBody CasePanVerifyBean casePanVerifyBean, BindingResult binding) {
		if(binding.hasErrors()) {
			throw new ValidationException("Invalid input char used, please correct and try.", ValidationError.builder().build());
	      }
		UserLoginBean userLoginBean = getUser();
		CaseValidator.panVerify(casePanVerifyBean);
		caseKycService.panVerify(casePanVerifyBean, userLoginBean);
		return ResponseEntity.ok().build();
	}
	
	@RequestMapping(value = "/initializeAndUploadAgreement/{caseId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ESigningResponseBean  initializeAndUploadAgreement(@PathVariable("caseId") Long caseId) {
		UserLoginBean userLoginBean = getUser();
		caseService.generateCustomerAgreement(caseId, userLoginBean);
		ESigningResponseBean eSigningResponseBean = caseKycService.initializeAndUploadAgreement(caseId, userLoginBean);
		return eSigningResponseBean;
	}
	
	@RequestMapping(value = "/saveCustomerAgreement", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Void> saveCustomerAgreement(@Validated @RequestBody GetDocumentRequestBean getDocumentRequestBean, BindingResult binding) {
		if (binding.hasErrors()) {
			throw new ValidationException("Invalid input char used, please correct and try.",
					ValidationError.builder().build());
		}
		UserLoginBean userLoginBean = getUser();
		Long oldDocId = caseKycService.saveCustomerAgreement(getDocumentRequestBean, userLoginBean);
		if (Objects.nonNull(oldDocId) && oldDocId > 0) {
			caseKycService.deleteOldDoc(oldDocId);
		}
		return ResponseEntity.ok().build();
	}
}
