package com.assignsecurities.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.assignsecurities.app.AppConstant;
import com.assignsecurities.app.exception.ServiceException;
import com.assignsecurities.app.util.ArgumentHelper;
import com.assignsecurities.bean.ApplicationUserBean;
import com.assignsecurities.bean.UserLoginBean;
import com.assignsecurities.dm.PropertyKeys;
import com.assignsecurities.domain.EmailAttachmentModel;
import com.assignsecurities.domain.EmailBodyModel;
import com.assignsecurities.domain.EmailCcModel;
import com.assignsecurities.domain.EmailModel;
import com.assignsecurities.domain.EmailToModel;
import com.assignsecurities.domain.UserLogin;
import com.assignsecurities.repository.impl.EmailReposatory;



@Service("emailService")
@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
public class EmailService {

	private static final Logger logger = LogManager.getLogger(EmailService.class);

	@Autowired
	private EmailReposatory emailReposatory;

	@Autowired
	private SmsService smsService;

	public List<EmailModel> getAllPendingEmails(UserLoginBean userLoginBean) {
		UserLogin userLogin = new UserLogin();
		if(Objects.nonNull(userLoginBean)){
			BeanUtils.copyProperties(userLoginBean, userLogin);
		}
		return emailReposatory.getAllPendingEmails(userLogin);
	}
	
	public List<EmailAttachmentModel> getEmailAttachments(Long emailId,UserLoginBean userLoginBean) {
		UserLogin userLogin = new UserLogin();
		if(Objects.nonNull(userLoginBean)){
			BeanUtils.copyProperties(userLoginBean, userLogin);
		}
		return emailReposatory.getEmailAttachments(emailId,userLogin);
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false, rollbackFor = Throwable.class)
	public void updateMailSent(EmailModel emailModel) {
		try {
			emailModel.setStatusId(AppConstant.EMAIL_STATUS_SENT);
			emailReposatory.updateMailStatus(emailModel);
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false, rollbackFor = Throwable.class)
	public void updateMailFailed(EmailModel emailModel, String message) {
		try {
			emailModel.setStatusId(AppConstant.EMAIL_STATUS_FAILED);
			emailModel.setComments(message);
			emailReposatory.updateMailStatus(emailModel);
		} catch (Exception e) {
			throw new ServiceException(e);
		}

	}

	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Throwable.class)
	public void sendEmail(ApplicationUserBean applicationUserBean, UserLoginBean userLoginBean, String eMailBoday,
			String subject, List<Long> attachmentDocIds, List<String> ccEmailIds) {
		logger.info("Start of sendEmail");
		EmailModel emailModel = new EmailModel();
		String senderEmailAddress = ApplicationPropertiesService.getPropertyStringValue(PropertyKeys.SEND_MAIL_AS);
		if (Objects.isNull(senderEmailAddress)) {
			senderEmailAddress = "support@tracksoft.com";
		}
		String toEmailAddress = applicationUserBean.getEmailId();

		List<EmailToModel> emailTos = new ArrayList<>();
		EmailToModel emailToModel = new EmailToModel();
		emailToModel.setEmail(toEmailAddress);
		emailTos.add(emailToModel);

		EmailBodyModel emailBody = new EmailBodyModel();
		emailBody.seteMailBoday(eMailBoday.getBytes());

		emailModel.setFromEmail(senderEmailAddress);
		emailModel.setSubject(subject);
		emailModel.setEmailBody(emailBody);
		emailModel.setEmailTos(emailTos);
		 List<EmailAttachmentModel> attachmentModels = new ArrayList<>();
		if(ArgumentHelper.isNotEmpty(attachmentDocIds)) {
			attachmentDocIds.forEach(attachmentDocId ->{
				EmailAttachmentModel am = new EmailAttachmentModel();
				am.setAttachmentDocId(attachmentDocId);
				attachmentModels.add(am);
			});
		}
		
		List<EmailCcModel> emailCcs = new ArrayList<>();
		if(ArgumentHelper.isNotEmpty(ccEmailIds)) {
			ccEmailIds.forEach(ccEmailId ->{
				EmailCcModel emailCcModel = new EmailCcModel();
				emailCcModel.setEmail(ccEmailId);
				emailCcs.add(emailCcModel);
			});
		}
		
		emailModel.setEmailCcs(emailCcs);
		emailModel.setAttachmentModels(attachmentModels);
		emailReposatory.addEmail(emailModel, userLoginBean);
	}
//
//	@Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
//	public void sendEmailWithAttachemnt(ApplicationUserBean applicationUserBean, UserLoginBean userLoginBean,
//			String eMailBoday, String subject, ByteArrayOutputStream btOs, String fileName) {
//		logger.info("Start of sendEmail");
//		EmailModel emailModel = new EmailModel();
//		String toEmailAddress = applicationUserBean.getEmailId();
//		List<EmailToModel> emailTos = new ArrayList<>();
//		EmailToModel emailToModel = new EmailToModel();
//		emailToModel.setEmail(toEmailAddress);
//		emailTos.add(emailToModel);
//
//		EmailBodyModel emailBody = new EmailBodyModel();
//		emailBody.seteMailBoday(eMailBoday.getBytes());
//		String senderEmailAddress = ApplicationPropertiesService.getPropertyStringValue(PropertyKeys.SEND_MAIL_AS,
//				AppConstant.SYS_USER_ID);
//		if (Objects.isNull(senderEmailAddress)) {
//			senderEmailAddress = "support@tracksoft.com";
//		}
//		emailModel.setFromEmail(senderEmailAddress);
//		emailModel.setSubject(subject);
//		emailModel.setEmailBody(emailBody);
//		emailModel.setEmailTos(emailTos);
//		File[] attachmentFiles = new File[1];
//		File destination = new File(fileName);
//
//
//		ByteArrayOutputStream buffer = (ByteArrayOutputStream) btOs;
//		byte[] bytes = buffer.toByteArray();
//		InputStream source = new ByteArrayInputStream(bytes);
//		try {
//			FileUtils.copyInputStreamToFile(source, destination);
//		} catch (IOException e1) {
//			logger.info("Unable to send email", e1);
//		}
//		attachmentFiles[0] = destination;
//		emailModel.setAttachmentFiles(attachmentFiles);
//		if (Objects.nonNull(toEmailAddress)) {
//			try {
//				MailSystemJob.send(emailModel);
//			} catch (Exception e) {
//				// emailReposatory.addEmail(emailModel, userLoginBean);
//				logger.info("Unable to send email", e);
//			}
//		}
//
//	}
}
