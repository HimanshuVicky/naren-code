package com.assignsecurities.service.impl;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.assignsecurities.app.AppConstant;
import com.assignsecurities.app.MessageTemplateService;
import com.assignsecurities.app.PasswordSHA2EncryptionUtil;
import com.assignsecurities.app.util.SessionIdentifierGenerator;
import com.assignsecurities.bean.ApplicationUserBean;
import com.assignsecurities.bean.SecuritySessionBean;
import com.assignsecurities.bean.UserLoginBean;
import com.assignsecurities.converter.ApplicationUserConverter;
import com.assignsecurities.converter.SecuritySessionConverter;
import com.assignsecurities.converter.UserLoginConverter;
import com.assignsecurities.dm.PropertyKeys;
import com.assignsecurities.domain.ApplicationUserModel;
import com.assignsecurities.domain.EmailBodyModel;
import com.assignsecurities.domain.EmailModel;
import com.assignsecurities.domain.EmailToModel;
import com.assignsecurities.domain.SecuritySession;
import com.assignsecurities.domain.UserLogin;
import com.assignsecurities.repository.impl.ApplicationUserRepo;
import com.assignsecurities.repository.impl.EmailReposatory;
import com.assignsecurities.repository.impl.SecuritySessionRepo;
import com.assignsecurities.repository.impl.UserLoginRepo;

import lombok.extern.slf4j.Slf4j;

@Service("loginService")
@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
@Slf4j
public class LoginService {

	@Autowired
	private SecuritySessionRepo securitySessionRepo;

	@Autowired
	private UserLoginRepo userLoginRepo;

	@Autowired
	private ApplicationUserService applicationUserService;

	@Autowired
	private EmailReposatory emailReposatory;
	
	@Autowired
	private ApplicationUserRepo applicationUserRepo;
	

	@Autowired
	private SmsService smsService;
	

	public UserLoginBean getUserLoginByMobileNo(String mobileNo) {
		UserLoginBean userLoginBean = UserLoginConverter.convert(userLoginRepo.getUserLoginByMobileNo(mobileNo), null);
		loadUserDetailsByUserType(userLoginBean);
		return userLoginBean;
	}

	private void loadUserDetailsByUserType(UserLoginBean userLoginBean) {
		if (Objects.nonNull(userLoginBean)) {
			ApplicationUserBean applicationUserBean = applicationUserService
					.getApplicationUserById(userLoginBean.getApplicationUserId());
			userLoginBean.setApplicationUserBean(applicationUserBean);
		}
	}

	public SecuritySessionBean getSecuritySession(String sessionId) {
		SecuritySession securitySessionBySession = securitySessionRepo.getSecuritySession(sessionId);
		return SecuritySessionConverter.convert(securitySessionBySession);
	}

	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Throwable.class)
	public Integer addSecuritySession(SecuritySessionBean securitySessionBean) {
		return securitySessionRepo.addSecuritySession(SecuritySessionConverter.convert(securitySessionBean));
	}
	
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Throwable.class)
	public void addAudit(UserLoginBean userLoginBean, String ipAddress) {
		userLoginRepo.addAudit(userLoginBean, ipAddress);
	}

	public UserLoginBean getUserLogin(Long userId) {
		UserLoginBean userLoginBean = UserLoginConverter.convert(userLoginRepo.getUserLogin(userId), null);
		loadUserDetailsByUserType(userLoginBean);
		return userLoginBean;
	}

	public UserLoginBean getUserLoginApplicationUserId(Long applicationUserId) {
		UserLoginBean userLoginBean = UserLoginConverter
				.convert(userLoginRepo.getUserLoginApplicationUserId(applicationUserId), null);
		loadUserDetailsByUserType(userLoginBean);
		return userLoginBean;
	}

	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Throwable.class)
	public void updateUserLoginPin(UserLoginBean userLoginBean, boolean isReset) {
		UserLogin userLogin = UserLoginConverter.convert(userLoginBean, null);
		String salt = "";
		String otp = SessionIdentifierGenerator.getOTP(AppConstant.OTP_DEFAULT_lENGTH);

		String hashsedPwd = getPin(salt, otp);
		userLogin.setPin(hashsedPwd);
		userLoginRepo.updateUserLogin(userLogin);
		userLoginBean = UserLoginConverter.convert(userLoginRepo.getUserLoginByMobileNo(userLogin.getMobileNo()), null);
		loadUserDetailsByUserType(userLoginBean);
		ApplicationUserBean applicationUserBean = userLoginBean.getApplicationUserBean();
		applicationUserBean.setNonCustomerPin(otp);
		ApplicationUserModel model = ApplicationUserConverter.convert(applicationUserBean);
		if (Objects.isNull(model.getReferalFranchiseId()) || model.getReferalFranchiseId() < 1) {
			model.setReferalFranchiseId(null);
		}
		applicationUserRepo.update(model);
		boolean sendSMS = false;
		if (userLogin.hasMobileNo()) {
			sendSMS = true;
		}
//		sendResetPinEmailSMS(userLoginBean.getApplicationUserBean(), userLogin, salt, otp, sendSMS, isReset);
		sendUserResetPinSMS(applicationUserBean, userLogin, otp, sendSMS, isReset);
	}

	public String getPin(String salt, String otp) {
//		System.out.println("otp====>" + otp);
		String hashsedPwd = PasswordSHA2EncryptionUtil.hash(otp, salt);
		return hashsedPwd;
	}

	private void sendResetPinEmailSMS(ApplicationUserBean applicationUserBean, UserLogin userLogin, String userName,
			String pwd, boolean sendSMS, boolean isReset) {
		log.info("Eending ResetPinEmailSMS..");
		// TOD Send SMS/Email to user.
		EmailModel emailModel = new EmailModel();
		String senderEmailAddress = ApplicationPropertiesService.getPropertyStringValue(PropertyKeys.SEND_MAIL_AS);
		if (Objects.isNull(senderEmailAddress)) {
			senderEmailAddress = "support@tracksoft.com";
		}
		String toEmailAddress = applicationUserBean.getEmailId();
		if (Objects.isNull(toEmailAddress)) {
			sendGenerateAndResetSMS(applicationUserBean, userLogin, pwd, sendSMS, isReset);
			return;
		}
		// emailTos
		List<EmailToModel> emailTos = new ArrayList<>();
		EmailToModel emailToModel = new EmailToModel();
		emailToModel.setEmail(toEmailAddress);
		emailTos.add(emailToModel);
		// TODO get From TEmplate
		// subject
		String subject = AppConstant.APP_RESET_EMAIL_SUBJECT;

		// emailBody
		String eMailBoday = AppConstant.APP_RESET_EMAIL_BODY;
		if (!isReset) {
			subject = AppConstant.APP_GENERATE_EMAIL_SUBJECT;
			eMailBoday = AppConstant.APP_GENERATE_EMAIL_BODY;
		}
		eMailBoday = MessageFormat.format(eMailBoday, applicationUserBean.getFirstName(), pwd);

		EmailBodyModel emailBody = new EmailBodyModel();
		emailBody.seteMailBoday(eMailBoday.getBytes());

		emailModel.setFromEmail(senderEmailAddress);
		emailModel.setSubject(subject);
		emailModel.setEmailBody(emailBody);
		emailModel.setEmailTos(emailTos);
		 UserLoginBean userLoginBean = UserLoginConverter.convert(userLogin, null);
		emailReposatory.addEmail(emailModel, userLoginBean);
		log.info("Email registration sucessfull " + emailModel.toString());
		sendGenerateAndResetSMS(applicationUserBean, userLogin, pwd, sendSMS, isReset);
	}

	private void sendGenerateAndResetSMS(ApplicationUserBean applicationUserBean, UserLogin userLogin, String pwd,
			boolean sendSMS, boolean isReset) {
		String eMailBoday;
		if (sendSMS) {
			//String sSMSText= AppConstant.APP_RESET_SMS_TEXT; //MessageTemplateService.getMessage("APP_RESET_SMS_TEXT");
//			eMailBoday = AppConstant.APP_RESET_SMS_TEXT;
			eMailBoday =  MessageTemplateService.getMessage("USER_LOGIN_PIN_SMS");//AppConstant.APP_RESET_SMS_TEXT; //MessageTemplateService.getMessage("APP_RESET_SMS_TEXT");
			if (!isReset) {
				eMailBoday =  MessageTemplateService.getMessage("USER_LOGIN_PIN_SMS"); //AppConstant.APP_GENERATE_SMS_TEXT;
			}
			eMailBoday = MessageFormat.format(eMailBoday, applicationUserBean.getFirstName(), pwd);
			String mobileNo = userLogin.getMobileNo().replaceAll("\\+", "");
			// "9921399990"
			eMailBoday = eMailBoday.replaceAll("<br/>", "");
			eMailBoday = eMailBoday.replaceAll("&nbsp;", "");
			smsService.sendSms(eMailBoday, mobileNo);
		}
	}
	
	private void sendUserResetPinSMS(ApplicationUserBean applicationUserBean, UserLogin userLogin, String pwd,
			boolean sendSMS, boolean isReset) {
		//String eMailBoday;
		if (sendSMS) {
//			String sSMSText= MessageTemplateService.getMessage("APP_RESET_SMS_TEXT"); //USER_LOGIN_PIN_SMS
//			eMailBoday = AppConstant.APP_RESET_SMS_TEXT;
			//String sMsg = MessageTemplateService.getMessage("APP_RESET_SMS_TEXT");
//			if (!isReset) {
//				eMailBoday = AppConstant.APP_GENERATE_SMS_TEXT;
//			}
//			if(Objects.isNull(sSMSText)) {
//				sSMSText = AppConstant.APP_RESET_SMS_TEXT;
//			}
			String sSMSText =  MessageTemplateService.getMessage("USER_LOGIN_PIN_SMS");//AppConstant.APP_RESET_SMS_TEXT;
			sSMSText = MessageFormat.format(sSMSText, applicationUserBean.getFirstName(), pwd);
			String mobileNo = userLogin.getMobileNo().replaceAll("\\+", "");
			// "9921399990"
			sSMSText = sSMSText.replaceAll("<br/>", "");
			sSMSText = sSMSText.replaceAll("&nbsp;", "");
			smsService.sendSms(sSMSText, mobileNo);
		}
	}

	public void sendResetPinViaSMS(String userFirstName, String mobileNumber, String pwd) {
		log.info("Eending ResetPinSMS..");
		// TOD Send SMS/Email to user.
//		String sSMSText = AppConstant.APP_RESET_SMS_TEXT;
		
		String sSMSText= MessageTemplateService.getMessage("USER_LOGIN_PIN_SMS");//AppConstant.APP_RESET_SMS_TEXT;// MessageTemplateService.getMessage("APP_RESET_SMS_TEXT");
		sSMSText = MessageFormat.format(sSMSText, userFirstName, pwd);
		String mobileNo = mobileNumber.replaceAll("\\+", "");
		// "9921399990"
		sSMSText = sSMSText.replaceAll("<br/>", "");
		sSMSText = sSMSText.replaceAll("&nbsp;", "");
		smsService.sendSms(sSMSText, mobileNo);
		return;

	}
	
	public void sendUserRegistrationSMS(String userFirstName, String mobileNumber, String pwd) {
		log.info("Eending ResetPinSMS..");
		// TOD Send SMS/Email to user.
//		String sSMSText = AppConstant.APP_RESET_SMS_TEXT;
		
		String sSMSText= MessageTemplateService.getMessage("USER_REGISTRATION_SMS");//AppConstant.APP_RESET_SMS_TEXT;//// please refer key USER_REGISTRATION_SMS
		sSMSText = MessageFormat.format(sSMSText, userFirstName,"http://www.findmymoney.in", pwd);
		String mobileNo = mobileNumber.replaceAll("\\+", "");
		// "9921399990"
		sSMSText = sSMSText.replaceAll("<br/>", "");
		sSMSText = sSMSText.replaceAll("&nbsp;", "");
		smsService.sendSms(sSMSText, mobileNo);
		return;

	}
	
	public void sendReferralUserRegistrationSMS(String userFirstName, String mobileNumber, String pwd, String userType) {
		log.info("ReferralPinSMS..");
		// TOD Send SMS/Email to user.
//		String sSMSText = AppConstant.APP_RESET_SMS_TEXT;
		String sSMSText="";
		if(userType.equalsIgnoreCase(AppConstant.USER_TYPE_FRANCHISE))
		{
			sSMSText=AppConstant.FRANCHISE_PARTNER_USER_REGISTRATION_SMS;
		}
		else if(userType.equalsIgnoreCase(AppConstant.USER_TYPE_REFERRAL_PARTNER))
		{
			sSMSText=AppConstant.REFERAL_PARTNER_USER_REGISTRATION_SMS;
		}
		else if(userType.equalsIgnoreCase(AppConstant.USER_TYPE_NOTARYL_PARTNER))
		{
			sSMSText=AppConstant.NOTARY_PARTNER_USER_REGISTRATION_SMS;
		}
		else if(userType.equalsIgnoreCase(AppConstant.USER_TYPE_CHARTERED_ACCOUNTANT_PARTNER))
		{
			sSMSText=AppConstant.CHARTERED_ACCOUNTANT_PARTNER_USER_REGISTRATION_SMS;
		}
		
		//String sSMSText= AppConstant.REFERAL_FRANCHISE_USER_REGISTRATION_SMS;//MessageTemplateService.getMessage("REFERAL_FRANCHISE_USER_REGISTRATION_SMS");//AppConstant.REFERAL_FRANCHISE_USER_REGISTRATION_SMS;//// please refer key USER_REGISTRATION_SMS
		sSMSText = MessageFormat.format(sSMSText, userFirstName, pwd);
		String mobileNo = mobileNumber.replaceAll("\\+", "");
		// "9921399990"
		sSMSText = sSMSText.replaceAll("<br/>", "");
		sSMSText = sSMSText.replaceAll("&nbsp;", "");
		smsService.sendSms(sSMSText, mobileNo);
		return;

	}
	
	public void sendUpdateUserSMS(String userFirstName, String mobileNumber, String pwd, String userType) {
//		log.info("ReferralPinSMS..");
		// TOD Send SMS/Email to user.
//		String sSMSText = AppConstant.APP_RESET_SMS_TEXT;
		String sSMSText="";
		sSMSText=AppConstant.USER_PROFILE_UPDATE;
//		if(userType.equalsIgnoreCase(AppConstant.USER_TYPE_FRANCHISE))
//		{
//			sSMSText=AppConstant.PARTNER_USER_DTL_UPDATE_SMS;
//		}
//		else if(userType.equalsIgnoreCase(AppConstant.USER_TYPE_REFERRAL_PARTNER))
//		{
//			sSMSText=AppConstant.PARTNER_USER_DTL_UPDATE_SMS;
//		}
//		else if(userType.equalsIgnoreCase(AppConstant.USER_TYPE_NOTARYL_PARTNER))
//		{
//			sSMSText=AppConstant.PARTNER_USER_DTL_UPDATE_SMS;
//		}
//		else if(userType.equalsIgnoreCase(AppConstant.USER_TYPE_CHARTERED_ACCOUNTANT_PARTNER))
//		{
//			sSMSText=AppConstant.PARTNER_USER_DTL_UPDATE_SMS;
//		}
		
		//String sSMSText= AppConstant.REFERAL_FRANCHISE_USER_REGISTRATION_SMS;//MessageTemplateService.getMessage("REFERAL_FRANCHISE_USER_REGISTRATION_SMS");//AppConstant.REFERAL_FRANCHISE_USER_REGISTRATION_SMS;//// please refer key USER_REGISTRATION_SMS
		sSMSText = MessageFormat.format(sSMSText, userFirstName, pwd);
		String mobileNo = mobileNumber.replaceAll("\\+", "");
		// "9921399990"
		sSMSText = sSMSText.replaceAll("<br/>", "");
		sSMSText = sSMSText.replaceAll("&nbsp;", "");
		smsService.sendSms(sSMSText, mobileNo);
		return;

	}
	
	public void sendReferralFranchiseUsereSignCompletedSMS(String userFirstName, String mobileNumber, String pwd, String userType) {
		log.info("ReferralFranchiseUsereSignCompletedSMS..");
		// TOD Send SMS/Email to user.
//		String sSMSText = AppConstant.APP_RESET_SMS_TEXT;
		String sSMSText="";
		
		
		if(userType.equalsIgnoreCase(AppConstant.USER_TYPE_FRANCHISE))
		{
			sSMSText=AppConstant.FRANCHISE_USER_ESIGNED_COMPLETED_SMS;
		}
		else if(userType.equalsIgnoreCase(AppConstant.USER_TYPE_REFERRAL_PARTNER))
		{
			sSMSText=AppConstant.REFERAL_PARTNER_USER_ESIGNED_COMPLETED_SMS;
		}
		
		//String sSMSText= AppConstant.REFERAL_FRANCHISE_USER_ESIGNED_COMPLETED_SMS;//MessageTemplateService.getMessage("REFERAL_FRANCHISE_USER_ESIGNED_COMPLETED_SMS");//AppConstant.APP_RESET_SMS_TEXT;//// please refer key USER_REGISTRATION_SMS
		sSMSText = MessageFormat.format(sSMSText, userFirstName);
		String mobileNo = mobileNumber.replaceAll("\\+", "");
		// "9921399990"
		sSMSText = sSMSText.replaceAll("<br/>", "");
		sSMSText = sSMSText.replaceAll("&nbsp;", "");
		smsService.sendSms(sSMSText, mobileNo);
		return;

	}
	
	public void sendAdmineSignCompletedSMS(String userFirstName, String mobileNumber, String pwd) {
		log.info("AdmineSignCompletedSMS..");
		// TOD Send SMS/Email to user.
//		String sSMSText = AppConstant.APP_RESET_SMS_TEXT;
		
		String sSMSText= AppConstant.ADMIN_PENDING_ESIGNED_COMPLETED_SMS;//MessageTemplateService.getMessage("ADMIN_PENDING_ESIGNED_COMPLETED_SMS");//AppConstant.APP_RESET_SMS_TEXT;//// please refer key USER_REGISTRATION_SMS
		sSMSText = MessageFormat.format(sSMSText, userFirstName,pwd);
		String mobileNo = mobileNumber.replaceAll("\\+", "");
		// "9921399990"
		sSMSText = sSMSText.replaceAll("<br/>", "");
		sSMSText = sSMSText.replaceAll("&nbsp;", "");
		smsService.sendSms(sSMSText, mobileNo);
		return;

	}
	
	
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Throwable.class)
	public void clearToken(){
		Integer deletionMinutes = ApplicationPropertiesService
				.getPropertyIntegerValue(PropertyKeys.TOKEN_CLEANER_IN_MINUTES); 
		securitySessionRepo.deleteExpiredSEcuritySEssion(deletionMinutes);
	}
	
	public void sendRegEmailFranchise(List<String> toEmailAddresses, UserLogin userLogin, String franchiseName, String userName, String userMobileNo) {
		log.info("Sending Email ..");
		// TODO Send SEmail to user.
		String senderEmailAddress = ApplicationPropertiesService.getPropertyStringValue(PropertyKeys.SEND_MAIL_AS);
		if (Objects.isNull(senderEmailAddress)) {
			senderEmailAddress = "support@tracksoft.com";
		}
		EmailModel emailModel = new EmailModel();
		// emailTos
		List<EmailToModel> emailTos = new ArrayList<>();
		toEmailAddresses.forEach(toEmail->{
			EmailToModel emailToModel = new EmailToModel();
			 emailToModel.setEmail(toEmail);
				emailTos.add(emailToModel);
		});
		
		//TODO get From Template
		// subject
		String subject = AppConstant.APP_FRANCHISE_EMAIL_SUBJECT;
		// emailBody
		String eMailBoday = AppConstant.APP_FRANCHISE_EMAIL_BODY;
		eMailBoday = MessageFormat.format(eMailBoday, franchiseName, userName,userMobileNo);

		EmailBodyModel emailBody = new EmailBodyModel();
		emailBody.seteMailBoday(eMailBoday.getBytes());

		emailModel.setFromEmail(senderEmailAddress);
		emailModel.setSubject(subject);
		emailModel.setEmailBody(emailBody);
		emailModel.setEmailTos(emailTos);
		 UserLoginBean userLoginBean = UserLoginConverter.convert(userLogin, null);
		emailReposatory.addEmail(emailModel, userLoginBean);
		log.info("Email registration sucessfull " + emailModel.toString());
	}
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Throwable.class)
	public Integer deleteSecuritySEssion(Long userId) {
		return securitySessionRepo.deleteSecuritySEssion(userId);
	}

}
