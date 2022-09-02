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
import com.assignsecurities.app.Other;
import com.assignsecurities.app.exception.ValidationException;
import com.assignsecurities.app.util.AesUtil;
import com.assignsecurities.app.util.ArgumentHelper;
import com.assignsecurities.app.util.StringUtil;
import com.assignsecurities.bean.AddressBean;
import com.assignsecurities.bean.ApplicationUserBean;
import com.assignsecurities.bean.FranchiseBean;
import com.assignsecurities.bean.ReferralsCommisionDtlBean;
import com.assignsecurities.bean.UserLoginBean;
import com.assignsecurities.bean.UserRegistrationBean;
import com.assignsecurities.bean.ValidationError;
import com.assignsecurities.service.impl.ApplicationUserService;
import com.assignsecurities.service.impl.FranchiseService;
import com.assignsecurities.service.impl.LoginService;
import com.assignsecurities.service.impl.ReferralsCommisionDtlService;

@RestController
public class ApplicationUserController extends BaseController {
	@Autowired
	private ApplicationUserService applicationUserService;
	
	@Autowired
	private FranchiseService franchiseService;
	
	@Autowired
	private LoginService  loginService;
	
    @Autowired
    private ReferralsCommisionDtlService referralsCommisionDtlService;
	
	@RequestMapping(value = "/registerCustomer", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Void> registerIndividual(@Validated @RequestBody UserRegistrationBean userRegistrationBean, BindingResult binding) {
		if(binding.hasErrors()) {
			throw new ValidationException("Invalid input char used, please correct and try.", ValidationError.builder().build());
	      }
	
		Map<String, Object> jsonResponse = new HashMap<String, Object>();
		Map<String, String> warnningMessages = new HashMap<String, String>();
		Map<String, String> errorMessages = new HashMap<String, String>();
		if (Objects.isNull(userRegistrationBean.getMobileNo()) || userRegistrationBean.getMobileNo().length() != 10) {
			throw new ValidationException("Invalid Mobile Number", ValidationError.builder().build());
		}
		
		if (applicationUserService.isUserExists(userRegistrationBean.getMobileNo())) {
			String invalidUserMsg = String.format("Given user mobile number %s is already registered with us.",
					userRegistrationBean.getMobileNo());
			throw new ValidationException(invalidUserMsg, ValidationError.builder().build());
		}
		ApplicationUserBean applicationUserBean= new ApplicationUserBean();
		if(Objects.nonNull(userRegistrationBean.getReferralRefrance())) {
			Long referralFranchiseId = null;
		    Long referralUserId = null;
			if(userRegistrationBean.getReferralRefrance().startsWith("R")) {
				referralUserId = Long.valueOf(userRegistrationBean.getReferralRefrance().substring(1));
				applicationUserBean.setReferalUserId(referralUserId);
				UserLoginBean userLoginBean = loginService.getUserLogin(referralUserId);
				if(Objects.isNull(userLoginBean) || !userLoginBean.getId().equals(referralUserId)) {
					throw new ValidationException("Given Referral is not registered with us.", ValidationError.builder().build());
				}
				applicationUserBean.setFranchiseBean(userLoginBean.getApplicationUserBean().getFranchiseBean());
			}else {
				referralFranchiseId = Long.valueOf(userRegistrationBean.getReferralRefrance().substring(1));
				FranchiseBean franchiseBean= franchiseService.getFranchiseById(referralFranchiseId);
				if(Objects.isNull(franchiseBean) || !franchiseBean.getId().equals(referralFranchiseId)) {
					throw new ValidationException("Given Referral is not registered with us.", ValidationError.builder().build());
				}
				applicationUserBean.setFranchiseBean(franchiseBean);
				applicationUserBean.setReferalFranchiseId(referralFranchiseId);
			}
			
		}else {
			FranchiseBean franchiseBean= franchiseService.getFranchiseById(com.assignsecurities.app.AppConstant.SYS_USER_ID);
			applicationUserBean.setFranchiseBean(franchiseBean);
		}
		
		
		applicationUserBean.setFirstName(userRegistrationBean.getFirstName());
		applicationUserBean.setLastName(userRegistrationBean.getLastName());
		applicationUserBean.setUserType("User");
		applicationUserBean.setIsActive(Boolean.TRUE);
		applicationUserBean.setFranchiseId(com.assignsecurities.app.AppConstant.SYS_USER_ID);
		/*
		 * Below alternate mobile number represent customer primary mobile number.
		 * Below property is just a carrier to provide details in service layer.
		 */
		applicationUserBean.setMobile(userRegistrationBean.getMobileNo());
		applicationUserBean.setAddress(
		AddressBean.builder().city(userRegistrationBean.getCity()).state(userRegistrationBean.getState()).build());

		Long applicationUserId = applicationUserService.add(applicationUserBean);
		jsonResponse.put("Result", "OK");
		jsonResponse.put("warnningMessages", warnningMessages);
		jsonResponse.put("Record", applicationUserId);
		ResponseEntity<Void> responseEntity = getResponseEntity(jsonResponse);
		return responseEntity;
	}
	
	
	/*
	 * Same API will use by admin to create application users with minimum input and further details added by end user through Edit user.
	 */
	@RequestMapping(value = "/addApplicationUser", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Void> addApplicationUser(@Validated @RequestBody ApplicationUserBean applicationUserBean, BindingResult binding) {
		if(binding.hasErrors()) {
			throw new ValidationException("Invalid input char used, please correct and try.", ValidationError.builder().build());
	      }
		UserLoginBean userLoginBean = getUser();
		Map<String, Object> jsonResponse = new HashMap<String, Object>();
		Map<String, String> warnningMessages = new HashMap<String, String>();
		Map<String, String> errorMessages = new HashMap<String, String>();
		if (!(userLoginBean.getApplicationUserBean().getUserType().equals(AppConstant.USER_TYPE_ADMIN)||
				userLoginBean.getApplicationUserBean().getUserType().equals(AppConstant.USER_TYPE_FRANCHISE))) {
			return null;
		}
		if (userLoginBean.getApplicationUserBean().getUserType().equals(AppConstant.USER_TYPE_FRANCHISE)
				&& !applicationUserBean.getFranchiseId().equals(userLoginBean.getApplicationUserBean().getFranchiseId())) {
			return null;
		}
		if (Objects.isNull(applicationUserBean.getMobile()) || applicationUserBean.getMobile().length() != 10) {
			throw new ValidationException("Invalid Mobile Number", ValidationError.builder().build());
		}
		
		if (applicationUserService.isUserExists(applicationUserBean.getMobile()))
		{
			String invalidUserMsg = "Given user mobile number "+ applicationUserBean.getMobile() + " is already registered with us.";
			throw new ValidationException(invalidUserMsg, ValidationError.builder().build());
		}
		if(applicationUserBean.getUserType().equals(AppConstant.USER_TYPE_ADMIN)) {
			String invalidUserMsg = "Invalid User Type.";
			throw new ValidationException(invalidUserMsg, ValidationError.builder().build());
		}
		
//		ApplicationUserBean applicationUserBean= new ApplicationUserBean();
		
		//applicationUserBean.setFirstName(applicationUserBean.getFirstName());
		//applicationUserBean.setLastName(applicationUserBean.getLastName());
		//applicationUserBean.setUserType("User");
		applicationUserBean.setIsActive(Boolean.TRUE);
		applicationUserBean.setFranchiseId(userLoginBean.getApplicationUserBean().getFranchiseId());
				
		
		/*
		 * Below alternate mobile number represent customer primary mobile number.
		 * Below property is just a carrier to provide details in service layer.
		 */
		//applicationUserBean.setAlternateMobileNo(userRegistrationBean.getMobileNo());
//		if(Objects.isNull(applicationUserBean.getAddress()))
//		{		
//		applicationUserBean.setAddress(
//		AddressBean.builder().address("katraj").city(applicationUserBean.getCity()).pinCode("123456").state("MS").build());
//		}
		Long applicationUserId = applicationUserService.add(applicationUserBean);
		jsonResponse.put("Result", "OK");
		jsonResponse.put("warnningMessages", warnningMessages);
		jsonResponse.put("Record", applicationUserId);
		ResponseEntity<Void> responseEntity = getResponseEntity(jsonResponse);
		return responseEntity;
	}
	
	@RequestMapping(value = "/updateApplicationUser", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Void> updateApplicationUser(@Validated @RequestBody ApplicationUserBean applicationUserBean, BindingResult binding) {
		if(binding.hasErrors()) {
			throw new ValidationException("Invalid input char used, please correct and try.", ValidationError.builder().build());
	      }
		Map<String, Object> jsonResponse = new HashMap<String, Object>();
		Map<String, String> warnningMessages = new HashMap<String, String>();
		UserLoginBean userLoginBean = getUser();
		if (!(userLoginBean.getApplicationUserBean().getUserType().equals(AppConstant.USER_TYPE_ADMIN)||
				userLoginBean.getApplicationUserBean().getUserType().equals(AppConstant.USER_TYPE_FRANCHISE)) 
				&& !userLoginBean.getApplicationUserId().equals(applicationUserBean.getId())) {
			return null;
		}
		String invalidUserMsg = "First or Last name cannot be empty.";
		
		String regExp = "^(?=.*[0-9])(?=.*[!@#$%^&*])[a-zA-Z0-9!@#$%^&*]{6,15}$";
		 
		 if (Objects.isNull(applicationUserBean.getFirstName()) || 
				 Objects.isNull(applicationUserBean.getLastName()))
		 {
			 throw new ValidationException(invalidUserMsg, ValidationError.builder().build());
		 }
		 if(AppConstant.Gender.FeMale.name().equalsIgnoreCase(applicationUserBean.getGender()) &&
				 AppConstant.DefaultSurname.MadianName.name().equalsIgnoreCase(applicationUserBean.getDefaultSurname())) {
			 if(!ArgumentHelper.isValid(applicationUserBean.getMadianSurname())){
				 throw new ValidationException("Please provide Maiden Surname.", ValidationError.builder().build());
			 }
		 }
		 if(!userLoginBean.getApplicationUserBean().getUserType().equals(AppConstant.USER_TYPE_END_USER)) {
			 if (Objects.isNull(applicationUserBean.getNonCustomerPin()) ) {
				 throw new ValidationException("Please Provide non pin", ValidationError.builder().build());
			 }
			 String invalidPin ="Password must contain 6-8 characters that include at least 1 character, 1 number and 1 special character in (!@#$%^&*)";
			 if (!(applicationUserBean.getNonCustomerPin().length()>=6 && applicationUserBean.getNonCustomerPin().length()<=8) ) {
				 throw new ValidationException(invalidPin, ValidationError.builder().build());
			 }
			 if (!StringUtil.isValid(regExp,applicationUserBean.getNonCustomerPin()) ) {
				 throw new ValidationException(invalidPin, ValidationError.builder().build());
			 }
		 }
		
		// UserLoginBean userLoginBean=
		// schoolMasterService.getUserLoginByUserName(AppConstant.SYS_USER);

		// applicationUserBean.setTypeOfUserId(AppConstant.Individual_USER_TYPE_ID);
		// applicationUserBean.setCreatedBy(AppConstant.SYS_USER);
		
//		applicationUserBean.setFranchiseId(userLoginBean.getApplicationUserBean().getFranchiseId());
		
		applicationUserService.update(applicationUserBean);
		jsonResponse.put("Result", "OK");
		jsonResponse.put("warnningMessages", warnningMessages);
		// jsonResponse.put("Record", applicationUserBean);
		ResponseEntity<Void> responseEntity = getResponseEntity(jsonResponse);
		return responseEntity;
	}
	
	
	
	@RequestMapping(value = "/getAllFranchiseMembers", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Void> getAllFranchiseMembers() {
		Map<String, Object> jsonResponse = new HashMap<String, Object>();
		Map<String, String> warnningMessages = new HashMap<String, String>();
		Map<String, String> errorMessages = new HashMap<String, String>();
		UserLoginBean userLoginBean = getUser();
		if (!(userLoginBean.getApplicationUserBean().getUserType().equals(AppConstant.USER_TYPE_ADMIN)||
				userLoginBean.getApplicationUserBean().getUserType().equals(AppConstant.USER_TYPE_FRANCHISE))) {
			return null;
		}
		List<ApplicationUserBean> appUsers = applicationUserService.getAllFranchiseMembers(userLoginBean);
		maskNonCustomerPin(userLoginBean, appUsers);
		jsonResponse.put("Result", "OK");
		jsonResponse.put("warnningMessages", warnningMessages);
		jsonResponse.put("Records", appUsers);
		ResponseEntity<Void> responseEntity = getResponseEntity(jsonResponse);
		return responseEntity;
	}
	
	@RequestMapping(value = "/getAllFranchiseUsers", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Void> getAllFranchiseUsers() {
		Map<String, Object> jsonResponse = new HashMap<String, Object>();
		Map<String, String> warnningMessages = new HashMap<String, String>();
		Map<String, String> errorMessages = new HashMap<String, String>();
		UserLoginBean userLoginBean = getUser();
		if (!(userLoginBean.getApplicationUserBean().getUserType().equals(AppConstant.USER_TYPE_ADMIN)||
				userLoginBean.getApplicationUserBean().getUserType().equals(AppConstant.USER_TYPE_FRANCHISE))) {
			return null;
		}
		List<ApplicationUserBean> appUsers = applicationUserService.getAllFranchiseUser(userLoginBean);
		maskNonCustomerPin(userLoginBean, appUsers);
		jsonResponse.put("Result", "OK");
		jsonResponse.put("warnningMessages", warnningMessages);
		jsonResponse.put("Records", appUsers);
		ResponseEntity<Void> responseEntity = getResponseEntity(jsonResponse);
		return responseEntity;
	}
	
	
	@RequestMapping(value = "/getAllAdminUsers", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Void> getAdminUsers() {
		Map<String, Object> jsonResponse = new HashMap<String, Object>();
		Map<String, String> warnningMessages = new HashMap<String, String>();
		Map<String, String> errorMessages = new HashMap<String, String>();
		UserLoginBean userLoginBean = getUser();
		if (!(userLoginBean.getApplicationUserBean().getUserType().equals(AppConstant.USER_TYPE_ADMIN))) {
			return null;
		}
		List<ApplicationUserBean> appUsers = applicationUserService.getAllOrganisationAdminUsers(userLoginBean);
		maskNonCustomerPin(userLoginBean, appUsers);
		jsonResponse.put("Result", "OK");
		jsonResponse.put("warnningMessages", warnningMessages);
		jsonResponse.put("Records", appUsers);
		ResponseEntity<Void> responseEntity = getResponseEntity(jsonResponse);
		return responseEntity;
	}
	
	@RequestMapping(value = "/getAllCustomerUser", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Void> getAllCustomerUser() {
		Map<String, Object> jsonResponse = new HashMap<String, Object>();
		Map<String, String> warnningMessages = new HashMap<String, String>();
		Map<String, String> errorMessages = new HashMap<String, String>();
		UserLoginBean userLoginBean = getUser();
		if (!(userLoginBean.getApplicationUserBean().getUserType().equals(AppConstant.USER_TYPE_ADMIN)
				||userLoginBean.getApplicationUserBean().getUserType().equals(AppConstant.USER_TYPE_CC))) {
			return null;
		}
		List<ApplicationUserBean> appUsers =  applicationUserService.getAllCustomersUsersForGivenOrg(userLoginBean);
		maskNonCustomerPin(userLoginBean, appUsers);
		jsonResponse.put("Result", "OK");
		jsonResponse.put("warnningMessages", warnningMessages);
		jsonResponse.put("Records", appUsers);
		ResponseEntity<Void> responseEntity = getResponseEntity(jsonResponse);
		return responseEntity;
	}
	
	@RequestMapping(value = "/getAllCustomerCareUser", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Void> getAllCustomerCareUser() {
		Map<String, Object> jsonResponse = new HashMap<String, Object>();
		Map<String, String> warnningMessages = new HashMap<String, String>();
		Map<String, String> errorMessages = new HashMap<String, String>();
		UserLoginBean userLoginBean = getUser();
		if (!(userLoginBean.getApplicationUserBean().getUserType().equals(AppConstant.USER_TYPE_ADMIN))) {
			return null;
		}
		List<ApplicationUserBean> appUsers =  applicationUserService.getAllCustomerCareUsersForGivenOrg(userLoginBean);
		maskNonCustomerPin(userLoginBean, appUsers);
		jsonResponse.put("Result", "OK");
		jsonResponse.put("warnningMessages", warnningMessages);
		jsonResponse.put("Records", appUsers);
		ResponseEntity<Void> responseEntity = getResponseEntity(jsonResponse);
		return responseEntity;
	}
	
	@RequestMapping(value = "/getAllLawyerUsers", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Void> getAllLawyerUsers() {
		Map<String, Object> jsonResponse = new HashMap<String, Object>();
		Map<String, String> warnningMessages = new HashMap<String, String>();
		Map<String, String> errorMessages = new HashMap<String, String>();
		UserLoginBean userLoginBean = getUser();
		if (!(userLoginBean.getApplicationUserBean().getUserType().equals(AppConstant.USER_TYPE_ADMIN)||
				userLoginBean.getApplicationUserBean().getUserType().equals(AppConstant.USER_TYPE_FRANCHISE))) {
			return null;
		}
		List<ApplicationUserBean> appUsers =  applicationUserService.getAllLawyerUsersForGivenOrg(userLoginBean);
		maskNonCustomerPin(userLoginBean, appUsers);
		jsonResponse.put("Result", "OK");
		jsonResponse.put("warnningMessages", warnningMessages);
		jsonResponse.put("Records", appUsers);
		ResponseEntity<Void> responseEntity = getResponseEntity(jsonResponse);
		return responseEntity;
	}
	
	
	@RequestMapping(value = "/getUser/{userId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ApplicationUserBean getUser(@PathVariable("userId") Long userId) {
		UserLoginBean userLoginBean = getUser();
		if (!(userLoginBean.getApplicationUserBean().getUserType().equals(AppConstant.USER_TYPE_ADMIN)||
				userLoginBean.getApplicationUserBean().getUserType().equals(AppConstant.USER_TYPE_FRANCHISE)) 
				&& !userLoginBean.getApplicationUserId().equals(userId)) {
			return null;
		}
		ApplicationUserBean applicationUserBean = applicationUserService.getApplicationUserByIdForAdminFranchise(userId,userLoginBean);
		if(!AppConstant.USER_TYPE_END_USER.equalsIgnoreCase(applicationUserBean.getUserType())) {
			applicationUserBean.setNonCustomerPin(null);
		}
		return applicationUserBean;
		 
	}
	
	@RequestMapping(value = "/getInfo/{userId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ApplicationUserBean getInfo(@PathVariable("userId") Long userId) {
		UserLoginBean userLoginBean = getUser();
		if (!(userLoginBean.getApplicationUserBean().getUserType().equals(AppConstant.USER_TYPE_ADMIN)||
				userLoginBean.getApplicationUserBean().getUserType().equals(AppConstant.USER_TYPE_FRANCHISE)) 
				&& !userLoginBean.getApplicationUserId().equals(userId)) {
			return null;
		}
		ApplicationUserBean applicationUserBean = applicationUserService.getApplicationUserByIdForAdminFranchise(userId,userLoginBean);
		if(!AppConstant.USER_TYPE_END_USER.equalsIgnoreCase(applicationUserBean.getUserType())) {
			applicationUserBean.setNonCustomerPin(null);
		}
		return applicationUserBean;
		 
	}
	
	@RequestMapping(value = "/getAllApplicationUsers", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public List<ApplicationUserBean> getAllApplicationUsers() {
		UserLoginBean userLoginBean = getUser();
		List<ApplicationUserBean> appUsers = applicationUserService.getAllApplicationUsers(userLoginBean);
		maskNonCustomerPin(userLoginBean, appUsers);
		return appUsers;
	}


	public void maskNonCustomerPin(UserLoginBean userLoginBean, List<ApplicationUserBean> appUsers) {
		for(ApplicationUserBean bean : appUsers) {
			if(!AppConstant.USER_TYPE_END_USER.equalsIgnoreCase(bean.getUserType())) {
				bean.setNonCustomerPin(null);
			}
		}
	}
	
	@RequestMapping(value = "/getAllReferralPartnerUsers", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Void> getAllReferralPartnerUsers() {
		Map<String, Object> jsonResponse = new HashMap<String, Object>();
		Map<String, String> warnningMessages = new HashMap<String, String>();
		Map<String, String> errorMessages = new HashMap<String, String>();
		UserLoginBean userLoginBean = getUser();
		if (!(userLoginBean.getApplicationUserBean().getUserType().equals(AppConstant.USER_TYPE_ADMIN)||
				userLoginBean.getApplicationUserBean().getUserType().equals(AppConstant.USER_TYPE_FRANCHISE))) {
			return null;
		}
		List<ApplicationUserBean> appUsers = applicationUserService.getAllReferralPartnerUsers(userLoginBean);
		maskNonCustomerPin(userLoginBean, appUsers);
		jsonResponse.put("Result", "OK");
		jsonResponse.put("warnningMessages", warnningMessages);
		jsonResponse.put("Records", appUsers);
		ResponseEntity<Void> responseEntity = getResponseEntity(jsonResponse);
		return responseEntity;
	}
	
	@RequestMapping(value = "/getAllNotaryPartnerUsers", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Void> getAllNotaryPartnerUsers() {
		Map<String, Object> jsonResponse = new HashMap<String, Object>();
		Map<String, String> warnningMessages = new HashMap<String, String>();
		Map<String, String> errorMessages = new HashMap<String, String>();
		UserLoginBean userLoginBean = getUser();
		if (!(userLoginBean.getApplicationUserBean().getUserType().equals(AppConstant.USER_TYPE_ADMIN)||
				userLoginBean.getApplicationUserBean().getUserType().equals(AppConstant.USER_TYPE_FRANCHISE))) {
			return null;
		}
		List<ApplicationUserBean> appUsers = applicationUserService.getAllNotaryPartnerUsers(userLoginBean);
		maskNonCustomerPin(userLoginBean, appUsers);
		//maskNonCustomerPin(userLoginBean, appUsers);
		jsonResponse.put("Result", "OK");
		jsonResponse.put("warnningMessages", warnningMessages);
		jsonResponse.put("Records", appUsers);
		ResponseEntity<Void> responseEntity = getResponseEntity(jsonResponse);
		return responseEntity;
	}
	
	@RequestMapping(value = "/getAllCharteredAccountantPartnerUsers", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Void> getAllCharteredAccountantPartnerUsers() {
		Map<String, Object> jsonResponse = new HashMap<String, Object>();
		Map<String, String> warnningMessages = new HashMap<String, String>();
		Map<String, String> errorMessages = new HashMap<String, String>();
		UserLoginBean userLoginBean = getUser();
		if (!(userLoginBean.getApplicationUserBean().getUserType().equals(AppConstant.USER_TYPE_ADMIN)||
				userLoginBean.getApplicationUserBean().getUserType().equals(AppConstant.USER_TYPE_FRANCHISE))) {
			return null;
		}
		List<ApplicationUserBean> appUsers = applicationUserService.getAllCharteredAccountantPartnerUsers(userLoginBean);
		maskNonCustomerPin(userLoginBean, appUsers);
		jsonResponse.put("Result", "OK");
		jsonResponse.put("warnningMessages", warnningMessages);
		jsonResponse.put("Records", appUsers);
		ResponseEntity<Void> responseEntity = getResponseEntity(jsonResponse);
		return responseEntity;
	}
	
	@RequestMapping(value = "/getCurrentUser", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public UserLoginBean getCurrentUser() {
		UserLoginBean userLoginBean = getUser();
		userLoginBean.getApplicationUserBean().setNonCustomerPin(null);
		userLoginBean.setPin(null);
//		HttpSession session = request.getSession(true);
//		String crsfToken = (String)session.getAttribute(AppConstant.USER_TOKEN);
		String userType = userLoginBean.getApplicationUserBean().getUserType();
		String crsfToken = Other.salt;
//		crsfToken = AesUtil.hex(crsfToken.getBytes());
		AesUtil aesUtil = new AesUtil(Other.keySize, Other.iterationCount);
		String requestToken = aesUtil.encrypt(crsfToken, Other.iv, Other.passphrase, userType);
//		System.out.println("encriptedScript====>" + encriptedScript);
//		String descriptedScript = aesUtil.decrypt(salt, Other.iv, Other.passphrase, encriptedScript);
//		System.out.println("descriptedScript====>" + descriptedScript);
		userLoginBean.getApplicationUserBean().setRequestToken(requestToken);
//		String dummyUserType = SessionIdentifierGenerator.getOTP(8);
//		userLoginBean.getApplicationUserBean().setUserType(dummyUserType);
		return userLoginBean;
		 
	}
	
	@RequestMapping(value = "/updateFeeReferenceNumber", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Void> updateFeeReferenceNumber(@Validated @RequestBody ReferralsCommisionDtlBean bean, BindingResult binding) {
		if(binding.hasErrors()) {
			throw new ValidationException("Invalid input char used, please correct and try.", ValidationError.builder().build());
	      }
		UserLoginBean userLoginBean = getUser();
		if (!(userLoginBean.getApplicationUserBean().getUserType().equals(AppConstant.USER_TYPE_ADMIN)||
				userLoginBean.getApplicationUserBean().getUserType().equals(AppConstant.USER_TYPE_FRANCHISE)||
				userLoginBean.getApplicationUserBean().getUserType().equals(AppConstant.USER_TYPE_REFERRAL_PARTNER))) {
			return null;
		}
		referralsCommisionDtlService.updateFeeReferenceNumber(bean, userLoginBean);
		return ResponseEntity.ok().build();
		 
	}
	
}
