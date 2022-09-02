package com.assignsecurities.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.assignsecurities.app.AppConstant;
import com.assignsecurities.app.util.ArgumentHelper;
import com.assignsecurities.app.util.SessionIdentifierGenerator;
import com.assignsecurities.bean.AddressBean;
import com.assignsecurities.bean.ApplicationUserBean;
import com.assignsecurities.bean.FranchiseBean;
import com.assignsecurities.bean.ReferralsCommisionDtlBean;
import com.assignsecurities.bean.UserLoginBean;
import com.assignsecurities.converter.AddressConverter;
import com.assignsecurities.converter.ApplicationUserConverter;
import com.assignsecurities.converter.FranchiseConverter;
import com.assignsecurities.domain.AddressModel;
import com.assignsecurities.domain.ApplicationUserModel;
import com.assignsecurities.domain.ESignDocumentModel;
import com.assignsecurities.domain.UserLogin;
import com.assignsecurities.repository.impl.AddressRepo;
import com.assignsecurities.repository.impl.ApplicationUserRepo;
import com.assignsecurities.repository.impl.ESignDocumentRepo;
import com.assignsecurities.repository.impl.FranchiseRepo;
import com.assignsecurities.repository.impl.UserLoginRepo;

@Service
@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
public class ApplicationUserService {
	
    @Autowired
    private ApplicationUserRepo applicationUserRepo;
    
    @Autowired
	private UserLoginRepo userLoginRepo;
    
    @Autowired 
    private LoginService loginService;
    
    @Autowired
    private AddressRepo addressRepo;
    
    @Autowired
    private FranchiseRepo franchiseRepo;
    
    @Autowired
	private ESignService eSignService;
    
    @Autowired
    private ReferralsCommisionDtlService referralsCommisionDtlService;
    
    @Autowired
	private ESignDocumentRepo eSignDocumentRepo; 
    
	public ApplicationUserBean getApplicationUserById(Long id) {
		ApplicationUserBean bean = ApplicationUserConverter.convert(applicationUserRepo.getApplicationUserById(id));
		UserLogin userLogin = userLoginRepo.getUserLoginApplicationUserId(bean.getId());
		bean.setMobile(userLogin.getMobileNo());
		AddressBean addressbean = AddressConverter.convert(addressRepo.getAddressById(bean.getAddressId()));
    	bean.setAddress(addressbean);
    	FranchiseBean franchiseBean =FranchiseConverter.convert(franchiseRepo.getFranchiseById(bean.getFranchiseId()));
    	bean.setFranchiseBean(franchiseBean);
    	
    	ReferralsCommisionDtlBean referralsCommisionDtlBeanObj = null;
    	if(bean.getUserType().equals(AppConstant.USER_TYPE_FRANCHISE)) {
    		referralsCommisionDtlBeanObj = referralsCommisionDtlService.getReferralPartnerCommisionDtlforGivenFranchiseId(franchiseBean.getId());
    	}else if(bean.getUserType().equals(AppConstant.USER_TYPE_REFERRAL_PARTNER)) {
    		referralsCommisionDtlBeanObj = referralsCommisionDtlService.getReferralPartnerCommisionDtlforGivenAppUserId(userLogin.getId());
    	}
    	if(Objects.nonNull(referralsCommisionDtlBeanObj)) {
    		ESignDocumentModel eSignDocumentModel = eSignDocumentRepo.getByUserIdAndType(userLogin.getId(),
    				bean.getUserType());
    		if (Objects.nonNull(eSignDocumentModel)) {
    			referralsCommisionDtlBeanObj.setDocumentId(eSignDocumentModel.getDocumentId());
    		}
    	}
    	bean.setReferralsCommisionDtlBean(referralsCommisionDtlBeanObj);
    	
    	return bean;
	}
	
	public ApplicationUserBean getApplicationUserByIdForAdminFranchise(Long id,UserLoginBean userLoginBean) {
		ApplicationUserBean bean = ApplicationUserConverter.convert(applicationUserRepo.getApplicationUserByIdForAdminFranchise(id,userLoginBean));
		UserLogin userLogin = userLoginRepo.getUserLoginApplicationUserId(bean.getId());
		bean.setMobile(userLogin.getMobileNo());
		AddressBean addressbean = AddressConverter.convert(addressRepo.getAddressById(bean.getAddressId()));
    	bean.setAddress(addressbean);
    	FranchiseBean franchiseBean =FranchiseConverter.convert(franchiseRepo.getFranchiseById(bean.getFranchiseId()));
    	bean.setFranchiseBean(franchiseBean);
    	
    	ReferralsCommisionDtlBean referralsCommisionDtlBeanObj = null;
    	if(bean.getUserType().equals(AppConstant.USER_TYPE_FRANCHISE)) {
    		referralsCommisionDtlBeanObj = referralsCommisionDtlService.getReferralPartnerCommisionDtlforGivenFranchiseId(franchiseBean.getId());
    	}else if(bean.getUserType().equals(AppConstant.USER_TYPE_REFERRAL_PARTNER)) {
    		referralsCommisionDtlBeanObj = referralsCommisionDtlService.getReferralPartnerCommisionDtlforGivenAppUserId(userLogin.getId());
    	}
    	if(Objects.nonNull(referralsCommisionDtlBeanObj)) {
    		ESignDocumentModel eSignDocumentModel = eSignDocumentRepo.getByUserIdAndType(userLogin.getId(),
    				bean.getUserType());
    		if (Objects.nonNull(eSignDocumentModel)) {
    			referralsCommisionDtlBeanObj.setDocumentId(eSignDocumentModel.getDocumentId());
    		}
    	}
    	
		
    	bean.setReferralsCommisionDtlBean(referralsCommisionDtlBeanObj);
    	
    	return bean;
	}
	
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Throwable.class)
	public Long add(ApplicationUserBean bean) {
		/*
		 * App User Add
		 */
		//String custMobileNumber = bean.getAlternateMobileNo();
		//bean.setAlternateMobileNo("");
		AddressModel address  = AddressConverter.convert(bean.getAddress());
		Long addressId = addressRepo.addAddress(address);
		bean.setAddressId(addressId);
		if(!ArgumentHelper.isValid(bean.getGender()))
		{
			bean.setGender("Male");
		}
		UserLogin userLogin = new UserLogin();
		userLogin.setMobileNo(bean.getMobile());
		String salt = "";
		String otp = "";
		if (bean.getUserType() == AppConstant.USER_TYPE_END_USER) {
			otp = SessionIdentifierGenerator.getOTP(AppConstant.OTP_DEFAULT_lENGTH);
			String hashedPin = loginService.getPin(salt, otp);
			userLogin.setPin(hashedPin);
			bean.setNonCustomerPin(otp);
		} else {
			otp = bean.getNonCustomerPin();
			String hashedPin = loginService.getPin(salt, otp);
			userLogin.setPin(hashedPin);
		}
		
		long appUserId =  applicationUserRepo.add(ApplicationUserConverter.convert(bean));
		if(appUserId <= 0)
		 return null;
					
		userLogin.setIsActive(1);
		userLogin.setApplicationUserId(appUserId);
		long userLoginId  = userLoginRepo.addUserLogin(userLogin);
		if ((bean.getUserType().equalsIgnoreCase(AppConstant.USER_TYPE_REFERRAL_PARTNER))) {
			/*
			 * Insert commission details.
			 */
			ReferralsCommisionDtlBean referralsCommisionDtlBeanObj = bean.getReferralsCommisionDtlBean();
			if(Objects.nonNull(referralsCommisionDtlBeanObj) 
					&& Objects.nonNull(referralsCommisionDtlBeanObj.getRegistrationFee())) {
				referralsCommisionDtlBeanObj.setFranchiseId(bean.getReferalPartnerFranchiseId());
				if (bean.getReferralsCommisionDtlBean().getRegistrationFee().equals(0D)) {
					referralsCommisionDtlBeanObj.setIsRegistrationFeeReceived(Boolean.TRUE);
				}
				referralsCommisionDtlBeanObj.setUserId(userLoginId);
				referralsCommisionDtlBeanObj.setFranchiseId(null);
				referralsCommisionDtlService.add(bean.getReferralsCommisionDtlBean());
//				UserLoginBean userLoginBean) {
//					ESignDocumentModel eSignDocumentModel = eSignDocumentRepo.getByUserIdAndType(userLoginBean.getId(),
//							userLoginBean.getApplicationUserBean().getUserType());
				UserLoginBean userLoginBean = loginService.getUserLogin(userLoginId);
				eSignService.generateCustomerAgreement(userLoginBean);
			}
		}
		
		
		
		
		if(AppConstant.USER_TYPE_END_USER.equalsIgnoreCase(bean.getUserType()))
		{	//For customer
			loginService.sendUserRegistrationSMS(bean.getFirstName(), userLogin.getMobileNo(), otp);
			//For Admin
			UserLogin userLoginSysobj = userLoginRepo.getUserLoginApplicationUserId(AppConstant.SYS_USER_ID);
			loginService.sendUserRegistrationSMS(bean.getFirstName(), userLoginSysobj.getMobileNo(), otp);
			List<String> toEmailAddresses = new ArrayList<>();
			if(Objects.nonNull(bean.getFranchiseBean()) && Objects.nonNull(bean.getFranchiseBean().getEmailId())) {
				toEmailAddresses.add(bean.getFranchiseBean().getEmailId());
			}
			if(Objects.nonNull(bean.getReferalUserId())) {
				UserLoginBean referalUser = loginService.getUserLogin(bean.getReferalUserId());
				if(Objects.nonNull(referalUser) && Objects.nonNull(referalUser.getApplicationUserBean().getEmailId())) {
					toEmailAddresses.add(referalUser.getApplicationUserBean().getEmailId());
				}
			}
			
			ApplicationUserModel adminApplicationUser = applicationUserRepo.getApplicationUserById(userLoginSysobj.getApplicationUserId());
			toEmailAddresses.add(adminApplicationUser.getEmailId());
			loginService.sendRegEmailFranchise(toEmailAddresses, userLoginSysobj, 
					bean.getFranchiseBean().getOwnerFirstName() +" " + bean.getFranchiseBean().getOwnerLastName(), bean.getName(),userLogin.getMobileNo());
			
		}
		else if (bean.getUserType().equalsIgnoreCase(AppConstant.USER_TYPE_REFERRAL_PARTNER)
				||bean.getUserType().equalsIgnoreCase(AppConstant.USER_TYPE_NOTARYL_PARTNER)
				||bean.getUserType().equalsIgnoreCase(AppConstant.USER_TYPE_CHARTERED_ACCOUNTANT_PARTNER))
		{
			//For referral partner SMS
			loginService.sendReferralUserRegistrationSMS(bean.getFirstName(), userLogin.getMobileNo(), otp,bean.getUserType());
		}
//		else if (bean.getUserType().equalsIgnoreCase(AppConstant.USER_TYPE_NOTARYL_PARTNER))
//		{
//			//For referral partner SMS
//			loginService.sendReferralUserRegistrationSMS(bean.getFirstName(), userLogin.getMobileNo(), otp);
//		}
//		else if (bean.getUserType().equalsIgnoreCase(AppConstant.USER_TYPE_CHARTERED_ACCOUNTANT_PARTNER))
//		{
//			//For referral partner SMS
//			loginService.sendReferralUserRegistrationSMS(bean.getFirstName(), userLogin.getMobileNo(), otp);
//		}
				
		return appUserId;
	}
	
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Throwable.class)
	public void update(ApplicationUserBean bean) {
		AddressModel address  = AddressConverter.convert(bean.getAddress());
		addressRepo.updateAddress(address);
		bean.setAddressId(bean.getAddress().getId());
		if(!ArgumentHelper.isValid(bean.getGender()))
		{
			bean.setGender("Male");
		}
		
		applicationUserRepo.update(ApplicationUserConverter.convert(bean));
		UserLogin userLogin = userLoginRepo.getUserLoginApplicationUserId(bean.getId());
		if(bean.getUserType()!= AppConstant.USER_TYPE_END_USER)
		{
			
			String salt = "";
			String otp = "";
			otp = bean.getNonCustomerPin();
			String hashedPin = loginService.getPin(salt, otp);
			userLogin.setPin(hashedPin);
			int isLoginUserActive=0;
			if (bean.getIsActive()) {
				isLoginUserActive = 1;
	        }
	        userLogin.setIsActive(isLoginUserActive);
			userLoginRepo.updateUserLogin(userLogin);
			
			if (Objects.nonNull(bean.getNonCustomerPin()) ) {
				loginService.sendUpdateUserSMS(bean.getFirstName(), userLogin.getMobileNo(), otp,bean.getUserType());
			 }
		}
		if(Objects.nonNull(bean.getReferralsCommisionDtlBean())) {
			if(bean.getReferralsCommisionDtlBean().getRegistrationFee().equals(0D))
			{
				bean.getReferralsCommisionDtlBean().setIsRegistrationFeeReceived(Boolean.TRUE);
			}
			referralsCommisionDtlService.updateForProfileEdit(bean.getReferralsCommisionDtlBean());
			ESignDocumentModel eSignDocumentModel = eSignDocumentRepo.getByUserIdAndType(userLogin.getId(),
					bean.getUserType());
			if(Objects.nonNull(eSignDocumentModel)) {
				eSignDocumentRepo.deleteByDocumentId(eSignDocumentModel.getDocumentId());
			}
			UserLoginBean userLoginBean = loginService.getUserLogin(userLogin.getId());
			eSignService.generateCustomerAgreement(userLoginBean);
			
		}
		
	}
	
	public boolean isUserExists(String sMobileNumber)
	{
		
		return userLoginRepo.isMobileNoExists(sMobileNumber);
	}
	
	public List<ApplicationUserBean> getAllFranchiseMembers(UserLoginBean userLoginBean) {
		long franchiseId = userLoginBean.getApplicationUserBean().getFranchiseId();
		List<ApplicationUserModel> applicationUsers = applicationUserRepo
				.getAllApplicationUsersForGivenFranchiseIdAndUserType(userLoginBean, franchiseId, AppConstant.USER_TYPE_FRANCHISE_USER);
		List<ApplicationUserBean> applicationUserBeanLst = ApplicationUserConverter.convert(applicationUsers);
		List<ApplicationUserBean> appuserBeanlst = new ArrayList<ApplicationUserBean>();
		for (ApplicationUserBean applicationUserBeanobj : applicationUserBeanLst) {
			
			UserLogin userLogin = userLoginRepo.getUserLoginApplicationUserId(applicationUserBeanobj.getId());
			applicationUserBeanobj.setMobile(userLogin.getMobileNo());
			appuserBeanlst.add(applicationUserBeanobj);
			
		}
		return appuserBeanlst;
	}
	
	public List<ApplicationUserBean> getAllFranchiseUser(UserLoginBean userLoginBean) {
		long franchiseId = userLoginBean.getApplicationUserBean().getFranchiseId();
		List<ApplicationUserModel> applicationUsers = applicationUserRepo
				.getAllApplicationUsersForGivenFranchiseIdAndUserType(userLoginBean, franchiseId, AppConstant.USER_TYPE_FRANCHISE);
		List<ApplicationUserBean> applicationUserBeanLst = ApplicationUserConverter.convert(applicationUsers);
		List<ApplicationUserBean> appuserBeanlst = new ArrayList<ApplicationUserBean>();
		for (ApplicationUserBean applicationUserBeanobj : applicationUserBeanLst) {
			
			UserLogin userLogin = userLoginRepo.getUserLoginApplicationUserId(applicationUserBeanobj.getId());
			applicationUserBeanobj.setMobile(userLogin.getMobileNo());
			appuserBeanlst.add(applicationUserBeanobj);
			
		}
		return appuserBeanlst;
	}
	
	public List<ApplicationUserBean> getAllOrganisationAdminUsers(UserLoginBean userLoginBean) {
		long franchiseId = userLoginBean.getApplicationUserBean().getFranchiseId();
		List<ApplicationUserModel> applicationUsers = applicationUserRepo
				.getAllApplicationUsersForGivenFranchiseIdAndUserType(userLoginBean, franchiseId, AppConstant.USER_TYPE_ADMIN);
		
		List<ApplicationUserBean> applicationUserBeanLst = ApplicationUserConverter.convert(applicationUsers);
		List<ApplicationUserBean> appuserBeanlst = new ArrayList<ApplicationUserBean>();
		for (ApplicationUserBean applicationUserBeanobj : applicationUserBeanLst) {
			
			UserLogin userLogin = userLoginRepo.getUserLoginApplicationUserId(applicationUserBeanobj.getId());
			applicationUserBeanobj.setMobile(userLogin.getMobileNo());
			appuserBeanlst.add(applicationUserBeanobj);
			
		}
		return appuserBeanlst;
		

	}
	
	public List<ApplicationUserBean> getAllLawyerUsersForGivenOrg(UserLoginBean userLoginBean) {
		long franchiseId = userLoginBean.getApplicationUserBean().getFranchiseId();
		List<ApplicationUserModel> applicationUsers = applicationUserRepo
				.getAllApplicationUsersForGivenFranchiseIdAndUserType(userLoginBean, franchiseId, AppConstant.USER_TYPE_ADVOCATE);
		List<ApplicationUserBean> applicationUserBeanLst = ApplicationUserConverter.convert(applicationUsers);
		List<ApplicationUserBean> appuserBeanlst = new ArrayList<ApplicationUserBean>();
		for (ApplicationUserBean applicationUserBeanobj : applicationUserBeanLst) {
			
			UserLogin userLogin = userLoginRepo.getUserLoginApplicationUserId(applicationUserBeanobj.getId());
			applicationUserBeanobj.setMobile(userLogin.getMobileNo());
			applicationUserBeanobj.setUserId(userLogin.getId());
			appuserBeanlst.add(applicationUserBeanobj);
			
		}
		return appuserBeanlst;
	}
	
	public List<ApplicationUserBean> getAllCustomersUsersForGivenOrg(UserLoginBean userLoginBean) {
		long franchiseId = userLoginBean.getApplicationUserBean().getFranchiseId();
		List<ApplicationUserModel> applicationUsers = applicationUserRepo
				.getAllApplicationUsersForGivenFranchiseIdAndUserType(userLoginBean, franchiseId, AppConstant.USER_TYPE_END_USER);
		List<ApplicationUserBean> applicationUserBeanLst = ApplicationUserConverter.convert(applicationUsers);
		List<ApplicationUserBean> appuserBeanlst = new ArrayList<ApplicationUserBean>();
		for (ApplicationUserBean applicationUserBeanobj : applicationUserBeanLst) {
			
			UserLogin userLogin = userLoginRepo.getUserLoginApplicationUserId(applicationUserBeanobj.getId());
			applicationUserBeanobj.setMobile(userLogin.getMobileNo());
			appuserBeanlst.add(applicationUserBeanobj);
			
		}
		return appuserBeanlst;
	}
	
	public List<ApplicationUserBean> getAllCustomerCareUsersForGivenOrg(UserLoginBean userLoginBean) {
		long franchiseId =userLoginBean.getApplicationUserBean().getFranchiseId();
		List<ApplicationUserModel> applicationUsers = applicationUserRepo
				.getAllApplicationUsersForGivenFranchiseIdAndUserType(userLoginBean, franchiseId, AppConstant.USER_TYPE_CC);
		List<ApplicationUserBean> applicationUserBeanLst = ApplicationUserConverter.convert(applicationUsers);
		List<ApplicationUserBean> appuserBeanlst = new ArrayList<ApplicationUserBean>();
		for (ApplicationUserBean applicationUserBeanobj : applicationUserBeanLst) {
			
			UserLogin userLogin = userLoginRepo.getUserLoginApplicationUserId(applicationUserBeanobj.getId());
			applicationUserBeanobj.setMobile(userLogin.getMobileNo());
			appuserBeanlst.add(applicationUserBeanobj);
			
		}
		return appuserBeanlst;
	}
	
	public List<ApplicationUserBean> getAllApplicationUsers(UserLoginBean userLoginBean) {
		List<ApplicationUserModel> applicationUsers = applicationUserRepo
				.getAllApplicationUsers(userLoginBean);
		
		return ApplicationUserConverter.convert(applicationUsers);
	}
	
	public ApplicationUserBean getApplicationUsersDtlForGivenFranchiseOwnerIdAndUserType(long franchiseId, String userType) {
		ApplicationUserBean bean = ApplicationUserConverter.convert(applicationUserRepo.getApplicationUsersDtlForGivenFranchiseOwnerIdAndUserType(franchiseId,userType));
		if(Objects.isNull(bean)) {
			return null;
		}
		UserLogin userLogin = userLoginRepo.getUserLoginApplicationUserId(bean.getId());
		bean.setMobile(userLogin.getMobileNo());
		AddressBean addressbean = AddressConverter.convert(addressRepo.getAddressById(bean.getAddressId()));
    	bean.setAddress(addressbean);
    	FranchiseBean franchiseBean =FranchiseConverter.convert(franchiseRepo.getFranchiseById(bean.getFranchiseId()));
    	bean.setFranchiseBean(franchiseBean);
    	return bean;
	}
	
	public List<ApplicationUserBean> getAllReferralPartnerUsers(UserLoginBean userLoginBean) {
		long franchiseId = userLoginBean.getApplicationUserBean().getFranchiseId();
		List<ApplicationUserModel> applicationUsers = applicationUserRepo
				.getAllApplicationUsersForGivenFranchiseIdAndUserType(userLoginBean, franchiseId, AppConstant.USER_TYPE_REFERRAL_PARTNER);
		List<ApplicationUserBean> applicationUserBeanLst = ApplicationUserConverter.convert(applicationUsers);
		List<ApplicationUserBean> appuserBeanlst = new ArrayList<ApplicationUserBean>();
		for (ApplicationUserBean applicationUserBeanobj : applicationUserBeanLst) {
			
			UserLogin userLogin = userLoginRepo.getUserLoginApplicationUserId(applicationUserBeanobj.getId());
			applicationUserBeanobj.setMobile(userLogin.getMobileNo());
			applicationUserBeanobj.setUserId(userLogin.getId());
			appuserBeanlst.add(applicationUserBeanobj);
			
		}
		return appuserBeanlst;
	}
	
	public List<ApplicationUserBean> getAllNotaryPartnerUsers(UserLoginBean userLoginBean) {
		long franchiseId = userLoginBean.getApplicationUserBean().getFranchiseId();
		List<ApplicationUserModel> applicationUsers = applicationUserRepo
				.getAllApplicationUsersForGivenFranchiseIdAndUserType(userLoginBean, franchiseId, AppConstant.USER_TYPE_NOTARYL_PARTNER);
		List<ApplicationUserBean> applicationUserBeanLst = ApplicationUserConverter.convert(applicationUsers);
		List<ApplicationUserBean> appuserBeanlst = new ArrayList<ApplicationUserBean>();
		for (ApplicationUserBean applicationUserBeanobj : applicationUserBeanLst) {
			
			UserLogin userLogin = userLoginRepo.getUserLoginApplicationUserId(applicationUserBeanobj.getId());
			applicationUserBeanobj.setMobile(userLogin.getMobileNo());
			applicationUserBeanobj.setUserId(userLogin.getId());
			appuserBeanlst.add(applicationUserBeanobj);
			
		}
		return appuserBeanlst;
	}
	
	public List<ApplicationUserBean> getAllCharteredAccountantPartnerUsers(UserLoginBean userLoginBean) {
		long franchiseId = userLoginBean.getApplicationUserBean().getFranchiseId();
		List<ApplicationUserModel> applicationUsers = applicationUserRepo
				.getAllApplicationUsersForGivenFranchiseIdAndUserType(userLoginBean, franchiseId, AppConstant.USER_TYPE_CHARTERED_ACCOUNTANT_PARTNER);
		List<ApplicationUserBean> applicationUserBeanLst = ApplicationUserConverter.convert(applicationUsers);
		List<ApplicationUserBean> appuserBeanlst = new ArrayList<ApplicationUserBean>();
		for (ApplicationUserBean applicationUserBeanobj : applicationUserBeanLst) {
			
			UserLogin userLogin = userLoginRepo.getUserLoginApplicationUserId(applicationUserBeanobj.getId());
			applicationUserBeanobj.setMobile(userLogin.getMobileNo());
			applicationUserBeanobj.setUserId(userLogin.getId());
			appuserBeanlst.add(applicationUserBeanobj);
			
		}
		return appuserBeanlst;
	}
	
}
