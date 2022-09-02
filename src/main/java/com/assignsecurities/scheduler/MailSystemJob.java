package com.assignsecurities.scheduler;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;

import javax.activation.MimetypesFileTypeMap;
import javax.mail.Address;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.SendFailedException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.InternetHeaders;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.assignsecurities.app.AppConstant;
import com.assignsecurities.app.exception.ServiceException;
import com.assignsecurities.app.util.ArgumentHelper;
import com.assignsecurities.bean.DocumentBean;
import com.assignsecurities.bean.UserLoginBean;
import com.assignsecurities.dm.PropertyKeys;
import com.assignsecurities.domain.EmailAttachmentModel;
import com.assignsecurities.domain.EmailBccModel;
import com.assignsecurities.domain.EmailCcModel;
import com.assignsecurities.domain.EmailModel;
import com.assignsecurities.domain.EmailToModel;
import com.assignsecurities.domain.FileAttachmentModel;
import com.assignsecurities.service.impl.ApplicationPropertiesService;
import com.assignsecurities.service.impl.DocumentService;
import com.assignsecurities.service.impl.EmailService;
import com.assignsecurities.service.impl.LoginService;



@Service("MailSystemJob")
@Component
@EnableAsync
public class MailSystemJob {

	private static final Logger LOG = LogManager.getLogger(MailSystemJob.class);

	@Autowired
	private EmailService emailService;

	@Autowired
	private LoginService loginService;
	
	@Autowired
	private DocumentService documentService;

	@Scheduled(cron = "${email.send.expression}")
	public void sendMail() throws ServiceException {
		LOG.info("sendMail @" + LocalDateTime.now());
		// LOG.info("sendMail @" +
		// applicationPropertiesService.getAllActiveProperties());
		UserLoginBean userBean = loginService.getUserLoginByMobileNo(AppConstant.SYS_USER);
		List<EmailModel> emailBeans = emailService.getAllPendingEmails(userBean);
		Map<String, EmailModel> broadcastEmails = new HashMap<>();
		for (EmailModel mail : emailBeans) {
			try {
				if (!isBroadcastEmail(mail, broadcastEmails)) {
					
//					File[] attachmentFiles = new File[1];
//					File file = new File("C:\\opt\\docTemplate\\ACC_RTA Letter 1_CaseId_8_Lost RTA.pdf");
//					attachmentFiles[0]=file;
//					mail.setAttachmentFiles(attachmentFiles);
					loadFileAttachment(userBean, mail);
					send(mail);
					emailService.updateMailSent(mail);
					// mail.setSent();
					// mail.commit();
				} else {
					processBroadcastEmails(broadcastEmails);
				}
			} catch (Throwable e) {
				LOG.error(e);
				if (e.getMessage().length() > 255) {
					emailService.updateMailFailed(mail, e.getMessage().substring(0, 255));
				} else {
					emailService.updateMailFailed(mail, e.getMessage());
				}
			}
		}
		try {
			processBroadcastEmails(broadcastEmails);
		} catch (Throwable e) {
			LOG.error(e);
		}
	}

	private void loadFileAttachment(UserLoginBean userBean, EmailModel mail) {
		List<EmailAttachmentModel> attachmentModels = emailService.getEmailAttachments(mail.getId(), userBean);
		if(ArgumentHelper.isNotEmpty(attachmentModels)) {
			List<FileAttachmentModel> fileAttachmentModels = new ArrayList<>();
			for(EmailAttachmentModel model : attachmentModels) {
				DocumentBean doc= documentService.getById(model.getAttachmentDocId());
				String fileContent = doc.getFile().getFileContent();
				FileAttachmentModel attachmentModel = new FileAttachmentModel();
				attachmentModel.setContentType(doc.getFile().getFileContentType());
				attachmentModel.setFileName(doc.getFile().getFileTitle());
				attachmentModel.setFileContent(fileContent);
				fileAttachmentModels.add(attachmentModel);
			}
			mail.setFileAttachmentModels(fileAttachmentModels);
			
		}
	}

	private static boolean isBroadcastEmail(EmailModel mail, Map<String, EmailModel> broadcastEmails) {
		// for (EmailBccBean bcc : mail.getEmailBccs())
		// {
		// if
		// ((bcc.getAddress().equals(MessageAdministration.ALL_ACTIVE_USERS.getUnTranslatedText()))
		// ||
		// (bcc.getAddress().equals(MessageAdministration.ALL_LOGGED_IN_USERS.getUnTranslatedText())))
		// {
		// broadcastEmails.put(bcc.getAddress(), mail);
		// return true;
		// }
		// }
		return false;
	}

	private static void processBroadcastEmails(Map<String, EmailModel> broadcastEmails) {
		// for (Map.Entry<String, EmailBean> entry : broadcastEmails.entrySet())
		// {
		// _broadcastBccCount = 0;
		// String address = entry.getKey();
		// EmailBean email = entry.getValue();
		//
		// if
		// (address.equals(MessageAdministration.ALL_ACTIVE_USERS.getUnTranslatedText()))
		// {
		// broadcastToAllActiveUsers(email);
		// }
		// else if
		// (address.equals(MessageAdministration.ALL_LOGGED_IN_USERS.getUnTranslatedText()))
		// {
		// broadcastToAllLoggedInUsers(email);
		// }
		// LOG.debug("Broadcast bcc count: " + _broadcastBccCount);
		// }
	}

	public static void send(EmailModel mail) {
		if (mail.getFromEmail() == null) {
			throw new ServiceException("mail message does not specify who it is from!");
		}
		String mailHostPropValue = ApplicationPropertiesService.getPropertyStringValue(PropertyKeys.MAIL_HOST);
		if (mailHostPropValue == null || mailHostPropValue.trim().length() == 0) {
			throw new ServiceException("MAIL_HOST_PROPERTY_IS_NOT_SET");
		}

		// set useAlternate to false so that we can try to resend if the
		// alternate mail host property is set
		sendMail(mail, false);
	}

	private static void sendMail(EmailModel mail, boolean useAlternate) {
		// long t0 = System.currentTimeMillis();

		Long orgGroupId = AppConstant.SYS_USER_ID;
		Long defaultOrgGroupId = null;
		String mailHostPropValue = ApplicationPropertiesService.getPropertyStringValue(PropertyKeys.MAIL_HOST);
		if (mailHostPropValue.equals("None")) {
			defaultOrgGroupId = AppConstant.SYS_USER_ID;
		}

		Session mailSession = getSession(useAlternate, orgGroupId, defaultOrgGroupId);
		Message msg = new MimeMessage(mailSession);
		try {
			// set message headers
			msg.addHeader("Content-type", "text/HTML; charset=UTF-8");
			msg.addHeader("format", "flowed");
			msg.addHeader("Content-Transfer-Encoding", "8bit");

			setFrom(mail, msg);
			setTo(mail, msg);

			setSentBy(mail, msg);
			setCc(mail, msg);
			setBcc(mail, msg);
			setSubject(mail, msg);

			if(Objects.nonNull(mail.getFileAttachmentModels()) && !mail.getFileAttachmentModels().isEmpty()) {
				Multipart multipart = new MimeMultipart();
				setBody(mail, multipart);
	            setAttachments(mail, multipart);
	            setContent(msg, multipart);
			}else {
				setContent(mail, msg);
			}
			setSentDate(msg);

			System.out.println("Message is ready");

			Transport.send(msg);

			System.out.println("EMail Sent Successfully!!");
		} catch (Throwable t) {
			LOG.error(t);
			String alternateMailHostPropValue = ApplicationPropertiesService
					.getPropertyStringValue(PropertyKeys.ALTERNATE_MAIL_HOST);
			if (isNetworkException(t) && (!useAlternate)
					&& (alternateMailHostPropValue != null && alternateMailHostPropValue.trim().length() > 0)) {
				sendMail(mail, true);
			} else {
				throw new ServiceException(t);
			}
		}

		/*
		 * dev 2 end trial code
		 */

		// We should connect using primary mail host first, in which case should
		// retry will be false, the session should be created with primary mail
		// host
		// If connection fails, we should connect using secondary mail host, in
		// which case should retry will be false, the session should be created
		// with secondary mail host
		// Session mailSession = getSession(useAlternate, mail
		// .getPdmsOrganizationGroup().getId());
		// Message message = new MimeMessage(mailSession);
		//
		// setFrom(mail, message);
		// setTo(mail, message);
		//
		// // if (mail.isTextMessage())
		// // {
		// // setText(mail, message);
		// // }
		// // else
		// // {
		// setSentBy(mail, message);
		// setCc(mail, message);
		// setBcc(mail, message);
		// setSubject(mail, message);
		//
		// Multipart multipart = new MimeMultipart();
		//
		// setBody(mail, multipart);
		// setAttachments(mail, multipart);
		// setContent(message, multipart);
		// }

		// setSentDate(message);
		//
		// try {
		// if (LOG.isDebugEnabled())
		// LOG.debug("Before calling Transport.send");
		// Transport.send(message);
		// long t1 = System.currentTimeMillis();
		// if (LOG.isDebugEnabled())
		// LOG.debug("Sent mail in " + (t1 - t0) + "ms");
		// } catch (Throwable t) {
		// LOG.error(t);
		// String alternateMailHostPropValue = ApplicationPropertiesServiceImpl
		// .getPropertyStringValue(PropertyKeys.ALTERNATE_MAIL_HOST,
		// mail.getPdmsOrganizationGroup().getId());
		// if (isNetworkException(t)
		// && (!useAlternate)
		// && (alternateMailHostPropValue != null && alternateMailHostPropValue
		// .trim().length() > 0)) {
		// sendMail(mail, true);
		// } else {
		// throw new ServiceException(t);
		// }
		// }

	}

	/*
	 * Dev2 try for mail sending
	 * 
	 */
	/**
	 * Utility method to send simple HTML email
	 * 
	 * @param session
	 * @param toEmail
	 * @param subject
	 * @param body
	 */
	public static void sendEmail(Session session, String toEmail, String subject, String body) {
		try {
			MimeMessage msg = new MimeMessage(session);
			// set message headers
			msg.addHeader("Content-type", "text/HTML; charset=UTF-8");
			msg.addHeader("format", "flowed");
			msg.addHeader("Content-Transfer-Encoding", "8bit");

			msg.setFrom(new InternetAddress("no_reply@example.com", "NoReply-JD"));

			msg.setReplyTo(InternetAddress.parse("no_reply@example.com", false));

			msg.setSubject(subject, "UTF-8");

			msg.setText(body, "UTF-8");

			msg.setSentDate(new Date());

			msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail, false));
			System.out.println("Message is ready");
			Transport.send(msg);

			System.out.println("EMail Sent Successfully!!");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void setFrom(EmailModel mail, Message message) {
		try {
			Boolean shouldUseAlternateHeaders = ApplicationPropertiesService
					.getPropertyBooleanValue(PropertyKeys.USE_ALTERNATE_HEADERS);
			// // if (mail.isTextMessage())
			// // {
			// // message.setFrom(new
			// InternetAddress(ApplicationProperties.getSendTextAs()));
			// // }
			// // else
			if (shouldUseAlternateHeaders) {
				String mailFrom = mail.getFromEmail();
				String sendEmailAs = ApplicationPropertiesService.getPropertyStringValue(PropertyKeys.SEND_MAIL_AS);
				message.setFrom(new InternetAddress(mailFrom, sendEmailAs));
			} else {
				message.setFrom(new InternetAddress(mail.getFromEmail()));

			}
		} catch (MessagingException e) {
			throw new ServiceException(e);
		} catch (UnsupportedEncodingException e) {
			throw new ServiceException(e);
		}
	}

	private static void setSentBy(EmailModel mail, Message message) {
		Boolean shouldUseAlternateHeaders = ApplicationPropertiesService
				.getPropertyBooleanValue(PropertyKeys.USE_ALTERNATE_HEADERS);
		if (shouldUseAlternateHeaders && mail.getCreatedBy() != null) {
			try {
				Address[] replyToAddress = { new InternetAddress(mail.getFromEmail(), mail.getCreatedBy()) };
				message.setReplyTo(replyToAddress);
			} catch (UnsupportedEncodingException e) {
				throw new ServiceException(e);
			} catch (MessagingException e) {
				throw new ServiceException(e);
			}
		}
	}

	private static void setTo(EmailModel mail, Message message) {
		try {
			// if (mail.isTextMessage())
			// {
			// // make sure 1) we allow EmailAddress.createInternetAddress to do
			// whatever it needs to do
			// // based on sandbox mod and 2) that we only specify an email
			// address for text messages (no extra stuff like user name)
			// InternetAddress address =
			// mail.getEmailTos().iterator().next().createInternetAddress();
			// address = new InternetAddress(address.getAddress());
			// message.addRecipient(RecipientType.TO, address);
			// }
			// else
			// {
			for (EmailToModel to : mail.getEmailTos()) {
				message.addRecipient(RecipientType.TO, new InternetAddress(to.getEmail()));
			}
			// }
		} catch (MessagingException e) {
			throw new ServiceException(e);
		}
	}

	private static void setCc(EmailModel mail, Message message) {
		if (Objects.nonNull(mail.getEmailCcs())) {
			for (EmailCcModel cc : mail.getEmailCcs()) {
				try {
					message.addRecipient(RecipientType.CC, new InternetAddress(cc.getEmail()));
				} catch (MessagingException e) {
					throw new ServiceException(e);
				}
			}
		}
	}

	private static void setBcc(EmailModel mail, Message message) {
		if (Objects.nonNull(mail.getEmailBccs())) {
			for (EmailBccModel bcc : mail.getEmailBccs()) {
				try {
					message.addRecipient(RecipientType.BCC, new InternetAddress(bcc.getEmail()));
				} catch (MessagingException e) {
					throw new ServiceException(e);
				}
			}
		}
	}

	private static void setSubject(EmailModel mail, Message message) {
		if (mail.getSubject() != null) {
			try {
				String subject = mail.getSubject().replaceAll("\n", "");
				if (message instanceof MimeMessage) {
					((MimeMessage) message).setSubject(subject, "utf-8");
				} else {
					message.setSubject(subject);
				}
			} catch (MessagingException e) {
				throw new ServiceException(e);
			}
		}
	}

	private static void setBody(EmailModel mail, Multipart multipart) {
		if (mail.getEmailBody() != null) {
			MimeBodyPart bodyPart1 = new MimeBodyPart();
			String contentType = "text/html; charset=utf-8";
			try {
				String str = new String(mail.getEmailBody().geteMailBoday(), StandardCharsets.UTF_8);
				bodyPart1.setContent(str, contentType);
			} catch (MessagingException e) {
				throw new ServiceException(e);
			}

			try {
				multipart.addBodyPart(bodyPart1);
			} catch (MessagingException e) {
				throw new ServiceException(e);
			}
		}
	}

	private static void setContent(EmailModel mail, Message message) {
		StringBuffer msg = new StringBuffer();
		if (mail.getEmailBody() != null && mail.getEmailBody().geteMailBoday().toString().trim().length() > 0) {
			String str = new String(mail.getEmailBody().geteMailBoday(), StandardCharsets.UTF_8);
			msg.append(str);
		}

		String s = msg.toString().trim();

		s = s.replace("%n", "<br/>");

		// if (s.length() > 140) {
		// s = s.substring(0, 135) + "...";
		// }
		if (s.length() > 0) {
			try {
				// message.setContent("This is a test", "text/plain");
				message.setContent(s, "text/html");
			} catch (MessagingException e) {
				throw new ServiceException(e);
			}
		}
	}

	private static void setText(EmailModel mail, Message message) {
		StringBuffer msg = new StringBuffer();
		if (mail.getEmailBody() != null && mail.getEmailBody().geteMailBoday().toString().trim().length() > 0) {
			String str = new String(mail.getEmailBody().geteMailBoday(), StandardCharsets.UTF_8);
			msg.append(str);
		}

		String s = msg.toString().trim();

		if (s.length() > 140) {
			s = s.substring(0, 135) + "...";
		}
		if (s.length() > 0) {
			try {
				message.setText(s);
			} catch (MessagingException e) {
				throw new ServiceException(e);
			}
		}
	}

	private static void setAttachments(EmailModel mail, Multipart multipart) {

		List<FileAttachmentModel> files = null;

		try {
			files = mail.getFileAttachmentModels();
			if(Objects.nonNull(files) && !files.isEmpty()) {
				for (FileAttachmentModel file : files) {
					InternetHeaders ih = new InternetHeaders();
					ih.addHeader("Content-type", new MimetypesFileTypeMap().getContentType(file.getFileName()));
					ih.addHeader("Content-Transfer-Encoding", "base64");
					ih.addHeader("Content-Disposition", "attachment");
					try {
//						byte[] bytes = Base64.getDecoder().decode(file.getFileContent().getBytes());
//						String encodedString = new sun.misc.BASE64Encoder().encode(bytes);
						MimeBodyPart bodyPart2 = new MimeBodyPart(ih, file.getFileContent().getBytes());
						// create the attachment and set the file name
						bodyPart2.setFileName(file.getFileName());
						multipart.addBodyPart(bodyPart2);
					} catch (MessagingException e) {
						throw new ServiceException(e);
					}
				}
			}
		} catch (Exception e) {
			throw new ServiceException(e);
		}

	}

	private static void setContent(Message message, Multipart multipart) {
		try {
			message.setContent(multipart);
		} catch (MessagingException e) {
			throw new ServiceException(e);
		}
	}

	private static void setSentDate(Message message) {
		try {
			message.setSentDate(new Date());
		} catch (MessagingException e) {
			throw new ServiceException(e);
		}
	}

	private static boolean isNetworkException(Throwable t) {
		if (t instanceof SendFailedException || t instanceof MessagingException) {
			return true;
		} else {
			return false;
		}
	}

	private static Session getSession(boolean useAlternateMailHost, Long ogrGroupId, Long defaultOrgGroupId) {
		Properties props = new Properties();
		Boolean shouldUseSMTPAuthProperty = false;
		if (defaultOrgGroupId == null) {
			shouldUseSMTPAuthProperty = ApplicationPropertiesService
					.getPropertyBooleanValue(PropertyKeys.MAIL_SMTP_AUTH);
		} else {
			shouldUseSMTPAuthProperty = ApplicationPropertiesService
					.getPropertyBooleanValue(PropertyKeys.MAIL_SMTP_AUTH);
		}

		if (!useAlternateMailHost) {
			String mailHostPropValue = null;
			if (defaultOrgGroupId == null) {
				mailHostPropValue = ApplicationPropertiesService.getPropertyStringValue(PropertyKeys.MAIL_HOST);
			} else {
				mailHostPropValue = ApplicationPropertiesService.getPropertyStringValue(PropertyKeys.MAIL_HOST);
			}
			props.put("mail.smtp.host", mailHostPropValue.trim());
			LOG.debug("Sending e-mail using primary mail host " + mailHostPropValue);
		} else {
			String alternateMailHostPropValue = null;
			if (defaultOrgGroupId == null) {
				alternateMailHostPropValue = ApplicationPropertiesService
						.getPropertyStringValue(PropertyKeys.ALTERNATE_MAIL_HOST);
			} else {
				alternateMailHostPropValue = ApplicationPropertiesService
						.getPropertyStringValue(PropertyKeys.ALTERNATE_MAIL_HOST);
			}
			props.put("mail.smtp.host", alternateMailHostPropValue.trim());
			LOG.debug("Resending e-mail using secondary mail host " + alternateMailHostPropValue.trim());

		}

		Integer mailConnectionTimeOut = 600000;
		if (defaultOrgGroupId == null) {
			mailConnectionTimeOut = ApplicationPropertiesService
					.getPropertyIntegerValue(PropertyKeys.MAIL_CONNECTION_TIMEOUT);
		} else {
			mailConnectionTimeOut = ApplicationPropertiesService
					.getPropertyIntegerValue(PropertyKeys.MAIL_CONNECTION_TIMEOUT);
		}
		// Socket connection timeout value in milliseconds. Default is infinite
		// timeout. getInteger
		props.put("mail.smtp.connectiontimeout", mailConnectionTimeOut);
		Integer mailSocketTimeOut = 600000;
		if (defaultOrgGroupId == null) {
			mailSocketTimeOut = ApplicationPropertiesService
					.getPropertyIntegerValue(PropertyKeys.MAIL_SOCKET_IO_TIMEOUT);
		} else {
			mailSocketTimeOut = ApplicationPropertiesService
					.getPropertyIntegerValue(PropertyKeys.MAIL_SOCKET_IO_TIMEOUT);
		}
		// Socket I/O timeout value in milliseconds. Default is infinite
		// timeout.
		props.put("mail.smtp.timeout", mailSocketTimeOut);
		// authentication
		props.put("mail.smtp.auth", shouldUseSMTPAuthProperty);

		String smtpPort = null;
		if (defaultOrgGroupId == null) {
			smtpPort = ApplicationPropertiesService.getPropertyStringValue(PropertyKeys.MAIL_SMTP_PORT);
		} else {
			smtpPort = ApplicationPropertiesService.getPropertyStringValue(PropertyKeys.MAIL_SMTP_PORT);
		}
		// This controls the port for which incoming mail connections will be
		// accepted
		if(!"smtpout.secureserver.net".equals(props.get("mail.smtp.host"))) {
			props.put("mail.smtp.port", smtpPort);
		}
		
        props.put("mail.smtp.ssl.enable", "true");
        props.put("mail.smtp.ssl.trust", "*");


		Boolean mailSmtpStarttlesEable = false;
		if (defaultOrgGroupId == null) {
			mailSmtpStarttlesEable = ApplicationPropertiesService
					.getPropertyBooleanValue(PropertyKeys.MAIL_SMTP_STARTTLS_ENABLE);
		} else {
			mailSmtpStarttlesEable = ApplicationPropertiesService
					.getPropertyBooleanValue(PropertyKeys.MAIL_SMTP_STARTTLS_ENABLE);
		}
		// TLS : provide authentication,privacy/confidentiality and integrity
		// (message has not been modified)
		props.put("mail.smtp.starttls.enable", mailSmtpStarttlesEable);
		if (shouldUseSMTPAuthProperty) {
			// Authenticator auth = new SMTPAuthenticator(ogrGroupId);
			// return Session.getDefaultInstance(props, auth);
			String sSMTP_AUTH_USER = null;
			if (defaultOrgGroupId == null) {
				sSMTP_AUTH_USER = ApplicationPropertiesService.getPropertyStringValue(PropertyKeys.SMTP_AUTH_USER);
			} else {
				sSMTP_AUTH_USER = ApplicationPropertiesService.getPropertyStringValue(PropertyKeys.SMTP_AUTH_USER);
			}
			String sSMTP_AUTH_PWD = null;
			if (defaultOrgGroupId == null) {
				sSMTP_AUTH_PWD = ApplicationPropertiesService.getPropertyStringValue(PropertyKeys.SMTP_AUTH_PWD);
			} else {
				sSMTP_AUTH_PWD = ApplicationPropertiesService.getPropertyStringValue(PropertyKeys.SMTP_AUTH_PWD);
			}
			final String finalUserName = sSMTP_AUTH_USER;
			final String finalUserPwd = sSMTP_AUTH_PWD;
			Authenticator auth = new Authenticator() {
				// override the getPasswordAuthentication method
				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication(finalUserName, finalUserPwd);
				}
			};
			return Session.getDefaultInstance(props, auth);

		} else {
			return Session.getInstance(props);
		}
	}
}
