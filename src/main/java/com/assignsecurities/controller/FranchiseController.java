package com.assignsecurities.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

import com.assignsecurities.app.AppConstant;
import com.assignsecurities.app.exception.ValidationException;
import com.assignsecurities.bean.FranchiseBean;
import com.assignsecurities.bean.FranchiseReferralBean;
import com.assignsecurities.bean.UserLoginBean;
import com.assignsecurities.bean.ValidationError;
import com.assignsecurities.service.impl.ApplicationUserService;
import com.assignsecurities.service.impl.FranchiseService;

@RestController
public class FranchiseController extends BaseController {
	
	@Autowired
	private FranchiseService franchiseService;
	
	@Autowired
	private ApplicationUserService appUserService;
	
	@RequestMapping(value = "/addFranchise", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Void> addFranchise(@Validated @RequestBody FranchiseBean franchiseBean, BindingResult binding) {
		if(binding.hasErrors()) {
			throw new ValidationException("Invalid input char used, please correct and try.", ValidationError.builder().build());
	      }
		UserLoginBean userLoginBean = getUser();
		Map<String, Object> jsonResponse = new HashMap<String, Object>();
		Map<String, String> warnningMessages = new HashMap<String, String>();
		Map<String, String> errorMessages = new HashMap<String, String>();
		
		if (!(userLoginBean.getApplicationUserBean().getUserType().equals(AppConstant.USER_TYPE_ADMIN))) {
			return null;
		}
		
		if(appUserService.isUserExists(franchiseBean.getContactNumber()))
		{		
			String invalidUserMsg = "Given franchise user mobile number "+ franchiseBean.getContactNumber() + " is already registered with us.";
			throw new ValidationException(invalidUserMsg, ValidationError.builder().build());
		}
		
			
		Long franchiseId = franchiseService.add(franchiseBean);
		jsonResponse.put("Result", "OK");
		jsonResponse.put("warnningMessages", warnningMessages);
		jsonResponse.put("Record", franchiseId);
		ResponseEntity<Void> responseEntity = getResponseEntity(jsonResponse);
		return responseEntity;
	}
	
	@RequestMapping(value = "/updateFranchise", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Void> updateFranchise(@Validated @RequestBody FranchiseBean franchiseBean, BindingResult binding) {
		if(binding.hasErrors()) {
			throw new ValidationException("Invalid input char used, please correct and try.", ValidationError.builder().build());
	      }
		Map<String, Object> jsonResponse = new HashMap<String, Object>();
		Map<String, String> warnningMessages = new HashMap<String, String>();
//		Map<String, String> errorMessages = new HashMap<String, String>();
//		UserLoginBean userLoginBean = getUser();

		 
//		 if (Objects.isNull(franchiseBean.getFirstName()) || 
//				 Objects.isNull(applicationUserBean.getLastName()))
//		 {
//		 errorMessages.put("firstName", "First or Last name cannot be empty.");
//		 }
//		if (!Objects.isNull(errorMessages) && !errorMessages.isEmpty()) {
//			jsonResponse.put("Result", "NotOK");
//			jsonResponse.put("errorMessages", errorMessages.values());
//			ResponseEntity<Void> responseEntity = getResponseEntity(jsonResponse);
//			return responseEntity;
//		}
		// UserLoginBean userLoginBean=
		// schoolMasterService.getUserLoginByUserName(AppConstant.SYS_USER);

		// applicationUserBean.setTypeOfUserId(AppConstant.Individual_USER_TYPE_ID);
		// applicationUserBean.setCreatedBy(AppConstant.SYS_USER);
		
//		franchiseBean.setFranchiseId(userLoginBean.getApplicationUserBean().getFranchiseId());
		
		franchiseService.update(franchiseBean);
		jsonResponse.put("Result", "OK");
		jsonResponse.put("warnningMessages", warnningMessages);
		// jsonResponse.put("Record", applicationUserBean);
		ResponseEntity<Void> responseEntity = getResponseEntity(jsonResponse);
		return responseEntity;
	}
	
	@RequestMapping(value = "/getAllFranchise", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Void> getAllFranchise() {
		Map<String, Object> jsonResponse = new HashMap<String, Object>();
		Map<String, String> warnningMessages = new HashMap<String, String>();
		Map<String, String> errorMessages = new HashMap<String, String>();
		UserLoginBean userLoginBean = getUser();
		List<FranchiseBean> franchises = franchiseService.getAllFranchises();
		jsonResponse.put("Result", "OK");
		jsonResponse.put("warnningMessages", warnningMessages);
		jsonResponse.put("Records", franchises);
		ResponseEntity<Void> responseEntity = getResponseEntity(jsonResponse);
		return responseEntity;
	}

	@RequestMapping(value = "/getFranchise/{franchiseId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public FranchiseBean getFranchise(@PathVariable("franchiseId") Long franchiseId) {
		FranchiseBean franchisebean = franchiseService.getFranchiseById(franchiseId);
		return franchisebean;
	}
	
	@RequestMapping(value = "/generateFranchiseReferralURL", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public FranchiseReferralBean generateFranchiseReferralURL(@Validated @RequestBody FranchiseReferralBean franchiseReferralBean) {
		UserLoginBean userLoginBean = getUser();
		String url = request.getScheme() + "://" + request.getServerName();
		String franchiseId = franchiseReferralBean.getFranchiseId()+"";
		if(Objects.isNull(franchiseId) || "null".equalsIgnoreCase(franchiseId)) {
			franchiseId = userLoginBean.getApplicationUserBean().getFranchiseBean().getId()+"";
		}
		if(Objects.isNull(franchiseId) ||  Long.parseLong(franchiseId)<=1) {
			FranchiseBean fbean =franchiseService.getFranchiseByName("Adhyata");
			if(Objects.nonNull(fbean)) {
				franchiseId = "F"+fbean.getId();
			}
		}
		
		if(userLoginBean.getApplicationUserBean().getUserType().equals("Referral")) {
			franchiseId = "R"+userLoginBean.getId();
		}else {
			if(!franchiseId.startsWith("F")) {
				franchiseId = "F"+franchiseId;
			}
		}
		if(url.contains("localhost")) {
			url = "https://www.findmymoney.in/AS/#/auth-layout/landing?findMyMoneyId="+franchiseId;
		}else {
			url = url +"/AS/#/auth-layout/landing?findMyMoneyId="+franchiseId;
		}
		return FranchiseReferralBean.builder().referralUrl(url).build();
	}
}
