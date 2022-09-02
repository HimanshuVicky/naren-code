package com.assignsecurities.controller;

import java.util.List;

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
import com.assignsecurities.bean.CityMasterBean;
import com.assignsecurities.bean.DocumentMasterBean;
import com.assignsecurities.bean.FeeMasterBean;
import com.assignsecurities.bean.KeyValueBean;
import com.assignsecurities.bean.PropertyBean;
import com.assignsecurities.bean.StateMasterBean;
import com.assignsecurities.bean.StatusMasterBean;
import com.assignsecurities.bean.UserLoginBean;
import com.assignsecurities.bean.ValidationError;
import com.assignsecurities.dm.PropertyKeys;
import com.assignsecurities.service.impl.ApplicationPropertiesService;
import com.assignsecurities.service.impl.UtilityService;

@RestController
public class UtilityController extends BaseController {

	@Autowired
	private UtilityService utilityService;

	@RequestMapping(value = "getMasterFeeList", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public List<FeeMasterBean> getMasterFeeList() {
		return utilityService.getMasterFeeList();
	}

	@RequestMapping(value = "getMasterDocumentList/{uploadedOrGenerated}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public List<DocumentMasterBean> getMasterDocumentList(@PathVariable("uploadedOrGenerated") String uploadedOrGenerated) {
		return utilityService.getMasterDocumentList(uploadedOrGenerated);
	}
	
//	@RequestMapping(value = "getMasterDocumentListStage1/{uploadedOrGenerated}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
//	public List<DocumentMasterBean> getMasterDocumentListStage1(@PathVariable("uploadedOrGenerated") String uploadedOrGenerated) {
//		return utilityService.getMasterDocumentListStage1(uploadedOrGenerated);
//	}

	@RequestMapping(value = "getMasterStatusList/{stage}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public List<StatusMasterBean> getMasterStatusList(@PathVariable("stage") String stage) {
		return utilityService.getMasterStatusList(stage);
	}
	
	@RequestMapping(value = "getMasterStatusListWithAll/{stage}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public List<StatusMasterBean> getMasterStatusListWithAll(@PathVariable("stage") String stage) {
		return utilityService.getMasterStatusListWithAll(stage);
	}
	
	
	@RequestMapping(value = "getMasterStatusList", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public List<StatusMasterBean> getMasterStatusList() {
		UserLoginBean userLoginBean = getUser();
		return utilityService.getMasterStatusList(userLoginBean);
	}
	
	@RequestMapping(value = "getCityList", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public List<CityMasterBean> getCityList() {
		return utilityService.getCityList();
	}
	
	@RequestMapping(value = "getStateList", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public List<StateMasterBean> getStateList() {
		return utilityService.getStateList();
	}
	
	@RequestMapping(value = "getAllPartners", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public List<KeyValueBean> getAllPartners() {
		UserLoginBean userLoginBean = getUser();
		return utilityService.getAllPartners(userLoginBean);
	}
	
	@RequestMapping(value = "getCityListForGivenSateCode/{state_code}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public List<CityMasterBean> getCityListForGivenSateCode(@PathVariable("state_code") String state_code) {
		return utilityService.getCityListForGivenSateCode(state_code);
	}
	
	@RequestMapping(value = "/updateProperty", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Void> updateProperty(
			@Validated @RequestBody PropertyBean propertyBean, BindingResult binding) {
		if(binding.hasErrors()) {
			throw new ValidationException("Invalid input char used, please correct and try.", ValidationError.builder().build());
	      }
		UserLoginBean userLoginBean = getUser();
		if(userLoginBean.getApplicationUserBean().getUserType().equals(AppConstant.USER_TYPE_ADMIN)) {
			utilityService.updateProperty(propertyBean, userLoginBean);
		}else {
			return null;
		}
		return ResponseEntity.ok().build();
	}
	
	@RequestMapping(value = "/getProperty/{propertyName}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public PropertyBean getProperty(@PathVariable("propertyName") String propertyName) {
		UserLoginBean userLoginBean = getUser();
		if(!userLoginBean.getApplicationUserBean().getUserType().equals(AppConstant.USER_TYPE_ADMIN)) {
			return null;
		}
		Boolean isIntegrationRequired = ApplicationPropertiesService
				.getPropertyBooleanValue(PropertyKeys.SURE_PASS_INTEGRATION_REQUIRED);
		PropertyBean propertyBean = new PropertyBean();
		propertyBean.setPropertyValue(isIntegrationRequired.toString());
		return propertyBean;
	}
	
}
