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
import com.assignsecurities.domain.FranchiseModel;
import com.assignsecurities.domain.UserLogin;
import com.assignsecurities.repository.impl.AddressRepo;
import com.assignsecurities.repository.impl.ApplicationUserRepo;
import com.assignsecurities.repository.impl.FranchiseRepo;
import com.assignsecurities.repository.impl.UserLoginRepo;

@Service
@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
public class FranchiseService {
	
	@Autowired
	private FranchiseRepo franchiseRepo;
    
    @Autowired
    private AddressRepo addressRepo;
    
    @Autowired
    private LoginService loginService;
    
    @Autowired
    private ApplicationUserRepo applicationUserRepo;
    
    @Autowired
    private UserLoginRepo userLoginRepo;
    
    @Autowired
    private ReferralsCommisionDtlService referralsCommisionDtlService;
    
    @Autowired
	private ESignService eSignService;
    
    public FranchiseBean getFranchiseById(Long id) {
    	FranchiseBean fbean = FranchiseConverter.convert(franchiseRepo.getFranchiseById(id));
    	AddressBean addressbean = AddressConverter.convert(addressRepo.getAddressById(fbean.getAddressId()));
    	fbean.setAddress(addressbean);
    	return fbean;
		
	}
    
    public FranchiseBean getFranchiseByName(String franchiseName) {
    	FranchiseBean fbean = FranchiseConverter.convert(franchiseRepo.getFranchiseByName(franchiseName));
    	if(Objects.nonNull(fbean)) {
	    	AddressBean addressbean = AddressConverter.convert(addressRepo.getAddressById(fbean.getAddressId()));
	    	fbean.setAddress(addressbean);
    	}
    	return fbean;
		
	}
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Throwable.class)
	public Long add(FranchiseBean bean) {
		/*
		 * App User Add
		 */
		//String custMobileNumber = bean.getAlternateMobileNo();
		//bean.setAlternateMobileNo("");
		AddressModel address  = AddressConverter.convert(bean.getAddress());
		Long addressId = addressRepo.addAddress(address);
		bean.setAddressId(addressId);
		bean.setIsActive(Boolean.TRUE);
//		bean.setESignAgreementStatus(AppConstant.FRANCHISE_ESIGN_AGREEMENT_STATUS_NEW);
		long franchiseId =  franchiseRepo.add(FranchiseConverter.convert(bean));
		if(franchiseId <= 0)
		 return null;
//		if(!ArgumentHelper.isValid(bean.getApplicationUser().getGender()))
//		{
//			bean.setGender("Male");
//		}
		
		ApplicationUserBean	applicationUserBean = ApplicationUserBean.builder().firstName(bean.getOwnerFirstName()).lastName(bean.getOwnerLastName())
		.addressId(addressId).emailId(bean.getEmailId()).isActive(Boolean.TRUE)
		.userType(AppConstant.USER_TYPE_FRANCHISE).franchiseId(franchiseId).nonCustomerPin(bean.getApplicationUser().getNonCustomerPin()).build();  
		applicationUserBean.setGender("Male");
		
		long appUserId =  applicationUserRepo.add(ApplicationUserConverter.convert(applicationUserBean));
					
		UserLogin userLogin = new UserLogin();
		userLogin.setMobileNo(bean.getContactNumber());
//		String salt = "";
		
//		String otp = "";
//		if(applicationUserBean.getUserType()== AppConstant.USER_TYPE_END_USER)
//		{
//			otp = SessionIdentifierGenerator.getOTP(AppConstant.OTP_DEFAULT_lENGTH);
//			String hashedPin = loginService.getPin(salt, otp);
//			userLogin.setPin(hashedPin);
//		}
//		else
		
//		{  
		
//		}
		
		//String otp = SessionIdentifierGenerator.getOTP(AppConstant.OTP_DEFAULT_lENGTH);
		String	otp = String.valueOf(bean.getApplicationUser().getNonCustomerPin());
		userLogin.setPin(otp);
		String salt = "";
		String hashedPin = loginService.getPin(salt, otp);
		userLogin.setPin(hashedPin);
		userLogin.setIsActive(1);
		userLogin.setApplicationUserId(appUserId);
		long userLoginId = userLoginRepo.addUserLogin(userLogin);
//		loginService.sendResetPinViaSMS(bean.getOwnerFirstName(), userLogin.getMobileNo(), otp);
		
		/*
		 * Insert commission details.
		 */
		ReferralsCommisionDtlBean referralsCommisionDtlBeanObj = bean.getReferralsCommisionDtlBean();
		referralsCommisionDtlBeanObj.setFranchiseId(franchiseId);
		referralsCommisionDtlBeanObj.setUserId(null);

		if(bean.getReferralsCommisionDtlBean().getRegistrationFee().equals(0D))
		{
			referralsCommisionDtlBeanObj.setIsRegistrationFeeReceived(Boolean.TRUE);
		}
		
		referralsCommisionDtlService.add(bean.getReferralsCommisionDtlBean());
		UserLoginBean userLoginBean = loginService.getUserLogin(userLoginId);
		eSignService.generateCustomerAgreement(userLoginBean);
		
			//For Franchise
			loginService.sendReferralUserRegistrationSMS(bean.getOwnerFirstName(), userLogin.getMobileNo(), otp,AppConstant.USER_TYPE_FRANCHISE);
			//For Admin
////			UserLogin userLoginSysobj = userLoginRepo.getUserLoginApplicationUserId(AppConstant.SYS_USER_ID);
////			loginService.sendUserRegistrationSMS(bean.getOwnerFirstName(), userLoginSysobj.getMobileNo(), otp);
////			if(Objects.nonNull(bean.getFranchiseBean()) && Objects.nonNull(bean.getFranchiseBean().getEmailId())) {
////				ApplicationUserModel adminApplicationUser = applicationUserRepo.getApplicationUserById(userLoginSysobj.getApplicationUserId());
////				List<String> toEmailAddresses = new ArrayList<>();
////				toEmailAddresses.add(bean.getFranchiseBean().getEmailId());
////				toEmailAddresses.add(adminApplicationUser.getEmailId());
////				loginService.sendRegEmailFranchise(toEmailAddresses, userLoginSysobj, 
////						bean.getFranchiseBean().getOwnerFirstName() +" " + bean.getFranchiseBean().getOwnerLastName(), bean.getName(),userLogin.getMobileNo());
////			}
//			
//		}
				
		return franchiseId;
	}
	
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Throwable.class)
	public void update(FranchiseBean bean) {
		AddressModel address  = AddressConverter.convert(bean.getAddress());
		addressRepo.updateAddress(address);
		bean.setAddressId(bean.getAddress().getId());
		franchiseRepo.update(FranchiseConverter.convert(bean));
	}
	
	public boolean isFranchiseExists(String sName)
	{
		return true;
//				userLoginRepo.isMobileNoExists(sMobileNumber);
	}
	
	public List<FranchiseBean> getAllFranchises() {
		
		List<FranchiseModel> applicationUsers = franchiseRepo.getAllFranchise();
		return FranchiseConverter.convert(applicationUsers);
	}
	
//	public List<ApplicationUserBean> getAllOrganisationAdminUsers(long franchiseId) {
//		List<ApplicationUserModel> applicationUsers = applicationUserRepo
//				.getAllApplicationUsersForGivenFranchiseIdAndUserType(franchiseId, AppConstant.USER_TYPE_ADMIN);
//		return ApplicationUserConverter.convert(applicationUsers);
//	}
//	
//	public List<ApplicationUserBean> getAllLawyerUsersForGivenOrg(long franchiseId) {
//		List<ApplicationUserModel> applicationUsers = applicationUserRepo
//				.getAllApplicationUsersForGivenFranchiseIdAndUserType(franchiseId, AppConstant.USER_TYPE_ADVOCATE);
//		return ApplicationUserConverter.convert(applicationUsers);
//	}
//	
//	public List<ApplicationUserBean> getAllCustomersUsersForGivenOrg(long franchiseId) {
//		List<ApplicationUserModel> applicationUsers = applicationUserRepo
//				.getAllApplicationUsersForGivenFranchiseIdAndUserType(franchiseId, AppConstant.USER_TYPE_END_USER);
//		return ApplicationUserConverter.convert(applicationUsers);
//	}
//	
//	public List<ApplicationUserBean> getAllCustomerCareUsersForGivenOrg(long franchiseId) {
//		List<ApplicationUserModel> applicationUsers = applicationUserRepo
//				.getAllApplicationUsersForGivenFranchiseIdAndUserType(franchiseId, AppConstant.USER_TYPE_ADMIN);
//		return ApplicationUserConverter.convert(applicationUsers);
//	}

}
