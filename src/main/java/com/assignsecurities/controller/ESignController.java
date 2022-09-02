package com.assignsecurities.controller;

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

import com.assignsecurities.app.AppConstant;
import com.assignsecurities.app.exception.ValidationException;
import com.assignsecurities.bean.ESigningResponseBean;
import com.assignsecurities.bean.GetDocumentRequestBean;
import com.assignsecurities.bean.ReferralsCommisionDtlBean;
import com.assignsecurities.bean.UserLoginBean;
import com.assignsecurities.bean.ValidationError;
import com.assignsecurities.service.impl.ESignService;

@RestController
public class ESignController extends BaseController {
	@Autowired
	private ESignService eSignService;

	@RequestMapping(value = "/eSign/initializeAndUploadAgreement/{referralUserId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ESigningResponseBean initializeAndUploadAgreement(@PathVariable("referralUserId") Long referralUserId) {
		UserLoginBean userLoginBean = getUser();
		if (!(userLoginBean.getApplicationUserBean().getUserType().equals(AppConstant.USER_TYPE_ADMIN)||
				userLoginBean.getApplicationUserBean().getUserType().equals(AppConstant.USER_TYPE_FRANCHISE)
				||userLoginBean.getApplicationUserBean().getUserType().equals(AppConstant.USER_TYPE_REFERRAL_PARTNER))) {
			return null;
		}
		ESigningResponseBean eSigningResponseBean = eSignService.initializeAndUploadAgreement(userLoginBean,
				referralUserId);
		return eSigningResponseBean;
	}

	@RequestMapping(value = "/eSign/initializeAndUploadAgreement", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ESigningResponseBean initializeAndUploadAgreement() {
		UserLoginBean userLoginBean = getUser();
		if (!(userLoginBean.getApplicationUserBean().getUserType().equals(AppConstant.USER_TYPE_ADMIN)||
				userLoginBean.getApplicationUserBean().getUserType().equals(AppConstant.USER_TYPE_FRANCHISE)
				||userLoginBean.getApplicationUserBean().getUserType().equals(AppConstant.USER_TYPE_REFERRAL_PARTNER))) {
			return null;
		}
//		UserLoginBean referralUserBean = userLoginBean;
//		
//		// if Document exist then details from referralUserId
//		if(Objects.nonNull(referralUserId) && referralUserId>0) {
//			referralUserBean = loginService.getUserLogin(referralUserId);
//		}
//		
//		if (Objects.nonNull(referralUserId) && referralUserId>0 &&
//				AppConstant.USER_TYPE_ADMIN.equalsIgnoreCase(userLoginBean.getApplicationUserBean().getUserType())){
//			referralUserBean = loginService.getUserLogin(referralUserId);
//		}

		eSignService.generateCustomerAgreement(userLoginBean);
		ESigningResponseBean eSigningResponseBean = eSignService.initializeAndUploadAgreement(userLoginBean, 0L);
		return eSigningResponseBean;
	}

	@RequestMapping(value = "/eSign/saveCustomerAgreement", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Void> saveCustomerAgreement(
			@Validated @RequestBody GetDocumentRequestBean getDocumentRequestBean, BindingResult binding) {
		if(binding.hasErrors()) {
			throw new ValidationException("Invalid input char used, please correct and try.", ValidationError.builder().build());
	      }
		UserLoginBean userLoginBean = getUser();
		if (!(userLoginBean.getApplicationUserBean().getUserType().equals(AppConstant.USER_TYPE_ADMIN)||
				userLoginBean.getApplicationUserBean().getUserType().equals(AppConstant.USER_TYPE_FRANCHISE)
				||userLoginBean.getApplicationUserBean().getUserType().equals(AppConstant.USER_TYPE_REFERRAL_PARTNER))) {
			return null;
		}
		eSignService.saveCustomerAgreement(getDocumentRequestBean, userLoginBean);
		return ResponseEntity.ok().build();
	}
	
	@RequestMapping(value = "/getReferralPartnerCommisionDtl/{referralUserId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ReferralsCommisionDtlBean getReferralPartnerCommisionDtlforGivenAppUserId(@PathVariable("referralUserId") Long referralUserId) {
		return  eSignService.getReferralPartnerCommisionDtlforGivenAppUserId(referralUserId);
	} 
	
	@RequestMapping(value = "/getReferralFranchisCommisionDtl/{referralFranchiseId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ReferralsCommisionDtlBean getReferralPartnerCommisionDtlforGivenFranchiseId(@PathVariable("referralFranchiseId")  Long referralFranchiseId) {
		return  eSignService.getReferralPartnerCommisionDtlforGivenFranchiseId(referralFranchiseId);
	}
	@RequestMapping(value = "/getReferralPartnerCommisionDtlforGivenFranchiseUserId/{referralFranchiseUserId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ReferralsCommisionDtlBean getReferralPartnerCommisionDtlforGivenFranchiseUserId(@PathVariable("referralFranchiseUserId")  Long referralFranchiseUserId) {
		return  eSignService.getReferralPartnerCommisionDtlforGivenFranchiseUserId(referralFranchiseUserId);
	}
	
}
