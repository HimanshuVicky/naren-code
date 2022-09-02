package com.assignsecurities.controller;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.assignsecurities.app.AppConstant;
import com.assignsecurities.app.PasswordSHA2EncryptionUtil;
import com.assignsecurities.app.exception.NotAuthorisedException;
import com.assignsecurities.app.exception.ValidationException;
import com.assignsecurities.app.util.ArgumentHelper;
import com.assignsecurities.app.util.SessionIdentifierGenerator;
import com.assignsecurities.bean.ApplicationUserBean;
import com.assignsecurities.bean.DoUserForgotPinBean;
import com.assignsecurities.bean.DoUserLoginBean;
import com.assignsecurities.bean.SecuritySessionBean;
import com.assignsecurities.bean.UserLoginBean;
import com.assignsecurities.bean.ValidationError;
import com.assignsecurities.dm.PropertyKeys;
import com.assignsecurities.service.impl.ApplicationPropertiesService;
import com.assignsecurities.service.impl.LoginService;
import com.assignsecurities.service.impl.core.RequestService;

@RestController
public class LoginController extends BaseController {

	@Autowired
	private HttpServletRequest request;

	@Autowired
	private LoginService loginService;
	
	@Autowired
	private RequestService requestService;

	@RequestMapping(value = "/doAuth", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Void> userLogin(@Validated @RequestBody @Valid  DoUserLoginBean doUserLoginBean, BindingResult binding) {
		if(binding.hasErrors()) {
			throw new ValidationException("Invalid input char used, please correct and try.", ValidationError.builder().build());
	      }
		UserLoginBean userLoginBean = null;
		String CSRFToken = null;
		ArgumentHelper.requiredNonNullAndNonEmptyValue(doUserLoginBean.getMobileNo(), "Please provide mobile Number.");

		ArgumentHelper.requiredNonNullAndNonEmptyValue(doUserLoginBean.getPin(), "Please provide Pin.");

		userLoginBean = loginService.getUserLoginByMobileNo(doUserLoginBean.getMobileNo());
//		loadMasterData.loadMasterData();
		Map<String, Object> jsonResponse = new HashMap<String, Object>();
		String invalidUserPwdMsg = "Invalid PIN.";
		if (Objects.isNull(userLoginBean)) {
			throw new ValidationException(invalidUserPwdMsg, ValidationError.builder().build());
		}

		if (userLoginBean.getIsActive().equals(0)) {
			invalidUserPwdMsg = "User "+ doUserLoginBean.getMobileNo() +" is inactive. Please contact with System Administrator.";
			throw new ValidationException(invalidUserPwdMsg,
					ValidationError.builder().build());
		}

		if(Objects.nonNull(userLoginBean.getApplicationUserBean()) && !userLoginBean.getApplicationUserBean().getIsActive()) {
			throw new ValidationException(invalidUserPwdMsg,
					ValidationError.builder().build());
		}
		
		String smsServiceOnOff =ApplicationPropertiesService.getPropertyStringValue(PropertyKeys.SMS_SERVICE_ON_OFF); // "ADHYTA";
		if (smsServiceOnOff.equalsIgnoreCase("ON") ) {
			String plaintext = doUserLoginBean.getPin();
			String salt = "";
			String hashsedPwd = null;
			if (plaintext != null) {
				hashsedPwd = PasswordSHA2EncryptionUtil.hash(plaintext, salt);
			}
			if (!userLoginBean.getPin().equals(hashsedPwd)) {
				throw new ValidationException(invalidUserPwdMsg, ValidationError.builder().build());
			}
		}
		SessionIdentifierGenerator identifierGenerator = new SessionIdentifierGenerator();
		CSRFToken = identifierGenerator.nextSessionId();
		SecuritySessionBean securitySessionBean = new SecuritySessionBean();
		securitySessionBean.setSessionId(CSRFToken);
		securitySessionBean.setUserId(userLoginBean.getId());
		securitySessionBean.setDateCreated(LocalDate.now());
		securitySessionBean.setValidityPeriod(24);

		loginService.addSecuritySession(securitySessionBean);
		loginService.addAudit(userLoginBean,requestService.getClientIp(request));

		HttpSession session = request.getSession(true);
		userLoginBean.setPin(null);
		session.setAttribute(AppConstant.USER_CONTEXT, userLoginBean);
		jsonResponse.put("CSRFToken", CSRFToken);
//		  if(this.loginUserObj?.loggedInUser?.applicationUserBean?.defaultSurname && this.loginUserObj?.loggedInUser?.applicationUserBean?.defaultSurname !== "") {
//	            obj.defaultSurname = this.loginUserObj?.loggedInUser?.applicationUserBean?.defaultSurname;
//	          } else {
//	            obj.defaultSurname = this.loginUserObj?.loggedInUser?.applicationUserBean?.lastName;
//	          }
//		  
		ApplicationUserBean applicationUserBean = new ApplicationUserBean();
		applicationUserBean.setId(userLoginBean.getApplicationUserBean().getId());
		applicationUserBean.setDefaultSurname(userLoginBean.getApplicationUserBean().getDefaultSurname());
		applicationUserBean.setLastName(userLoginBean.getApplicationUserBean().getLastName());
		userLoginBean.setApplicationUserBean(applicationUserBean);
		jsonResponse.put(AppConstant.USER_CONTEXT, userLoginBean);
		ResponseEntity<Void> responseEntity = getResponseEntity(jsonResponse);
		return responseEntity;
	}

	@RequestMapping(value = "/validateMobileNumber", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public DoUserForgotPinBean validateMobileNumber(@Validated @RequestBody DoUserForgotPinBean doUserForgotPinBean, BindingResult binding) {
		if(binding.hasErrors()) {
			throw new ValidationException("Invalid input char used, please correct and try.", ValidationError.builder().build());
	      }
		String mobileNumber = doUserForgotPinBean.getMobileNo();
		ArgumentHelper.requiredNonNullAndNonEmptyValue(mobileNumber, "Please provide mobile Number.");
		UserLoginBean userLoginBean = loginService.getUserLoginByMobileNo(mobileNumber);
		if (Objects.isNull(userLoginBean)) {
//			throw new ValidationException("Given mobile number is not register.", ValidationError.builder().build());
			return DoUserForgotPinBean.builder().mobileNo(mobileNumber).isRegistered(Boolean.FALSE).build();
		}
		
		if (userLoginBean.getIsActive().equals(0)) {
			String invalidUserPwdMsg = "User "+ mobileNumber +" is inactive. Please contact with System Administrator.";
			throw new ValidationException(invalidUserPwdMsg,
					ValidationError.builder().build());
		}
		
		if (!AppConstant.USER_TYPE_END_USER.equalsIgnoreCase(userLoginBean.getApplicationUserBean().getUserType())) {
			return DoUserForgotPinBean.builder().mobileNo(mobileNumber).isRegistered(Boolean.TRUE).build();
		}
		
		
		String smsServiceOnOff =ApplicationPropertiesService.getPropertyStringValue(PropertyKeys.SMS_SERVICE_ON_OFF); // "ADHYTA";
		if (smsServiceOnOff.equalsIgnoreCase("OFF") ) {
			// NO SMS as service property is off.
			return DoUserForgotPinBean.builder().mobileNo(mobileNumber).isRegistered(Boolean.TRUE).build();
		}
		else {
			// Generate Pin & send to customer.
			loginService.updateUserLoginPin(userLoginBean, Boolean.FALSE);
			return DoUserForgotPinBean.builder().mobileNo(mobileNumber).isRegistered(Boolean.TRUE).build();
		}
	}

	@RequestMapping(value = "/notAuthorised", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Void> notAuthorised() {
		throw new NotAuthorisedException("Your are not authorised or token expired, Please re-login and continue.");
//		HashMap<String, Object> jsonResponse = new HashMap<String, Object>();
//		String invalidUserPwdMsg = "Not Authorised";
//		jsonResponse.put("Result", "NotOK");
//		jsonResponse.put("errorMessage", invalidUserPwdMsg);
//		ResponseEntity<Void> responseEntity = getResponseEntity(jsonResponse);
//		return responseEntity;
	}

	@RequestMapping(value = "/resetPin", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Void> resetAppUserPin(@Validated @RequestBody DoUserForgotPinBean doUserForgotPinBean, BindingResult binding) {
		if(binding.hasErrors()) {
			throw new ValidationException("Invalid input char used, please correct and try.", ValidationError.builder().build());
	      }
		ArgumentHelper.requiredNonNullAndNonEmptyValue(doUserForgotPinBean.getMobileNo(),
				"Please provide mobile Number.");
		UserLoginBean userLoginBean = loginService.getUserLoginByMobileNo(doUserForgotPinBean.getMobileNo());
		Map<String, Object> jsonResponse = new HashMap<String, Object>();
		String invalidUserPwdMsg = "Invalid User name.";
		if (Objects.isNull(userLoginBean)) {
			throw new ValidationException(invalidUserPwdMsg, ValidationError.builder().build());
		}

		loginService.updateUserLoginPin(userLoginBean, Boolean.TRUE);
		userLoginBean.setPin(null);
		ResponseEntity<Void> responseEntity = getResponseEntity(jsonResponse);
		return responseEntity;

	}

//	@RequestMapping(value = "/validateUserGeneratePin", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
//	public ResponseEntity<Void> userLoginUserExists(@Validated @RequestBody DoUserForgotPinBean doUserForgotPinBean) {
//		String CSRFToken = null;
//		UserLoginBean userLoginBean = loginService.getUserLoginByMobileNo(doUserForgotPinBean.getMobileNo());
//
//		Map<String, Object> jsonResponse = new HashMap<String, Object>();
//		String invalidUserPwdMsg = "Invalid UserId/Mobile.";
//		if (Objects.isNull(userLoginBean) || userLoginBean.getIsActive() == 0) {
//			if (Objects.nonNull(userLoginBean) && userLoginBean.getIsActive() == 0) {
//				invalidUserPwdMsg = "Your account currently in-active. Please wait till it gets activated.";
//			}
//			throw new ValidationException(invalidUserPwdMsg, ValidationError.builder().build());
////			jsonResponse.put("Result", "NotOK");
////			jsonResponse.put("errorMessage", invalidUserPwdMsg);
////			ResponseEntity<Void> responseEntity = getResponseEntity(jsonResponse);
////			return responseEntity;
//		}
//
//		SessionIdentifierGenerator identifierGenerator = new SessionIdentifierGenerator();
//		CSRFToken = identifierGenerator.nextSessionId();
//		SecuritySessionBean securitySessionBean = new SecuritySessionBean();
//		securitySessionBean.setSessionId(CSRFToken);
//		securitySessionBean.setUserId(userLoginBean.getId());
//		securitySessionBean.setDateCreated(LocalDate.now());
//		securitySessionBean.setValidityPeriod(24);
//
//		loginService.addSecuritySession(securitySessionBean);
//
//		HttpSession session = request.getSession(true);
//		userLoginBean.setPin(null);
//		session.setAttribute(AppConstant.USER_CONTEXT, userLoginBean);
//		jsonResponse.put("Result", "OK");
//		jsonResponse.put("CSRFToken", CSRFToken);
//		jsonResponse.put(AppConstant.USER_CONTEXT, userLoginBean);
//		ResponseEntity<Void> responseEntity = getResponseEntity(jsonResponse);
//		return responseEntity;
//	}

	@RequestMapping(value = "/mylogout", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Void> logout() {
		HashMap<String, Object> jsonResponse = new HashMap<String, Object>();
		String invalidUserPwdMsg = "Logout";
		jsonResponse.put("Result", "OK");
		jsonResponse.put("errorMessage", invalidUserPwdMsg);
		HttpSession session = request.getSession(false);
//		System.out.println("User=" + getUser());
		UserLoginBean userLoginBean = getUser();
		if(Objects.nonNull(userLoginBean)) {
			loginService.deleteSecuritySEssion(userLoginBean.getId());
		}
		
		if (session != null) {
			session.invalidate();
		}
//		System.out.println("After session.invalidate() ==>User=" + getUser());
		ResponseEntity<Void> responseEntity = getResponseEntity(jsonResponse);
		return responseEntity;
	}

}
