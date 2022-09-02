package com.assignsecurities.scheduler;

import javax.mail.PasswordAuthentication;

import org.springframework.beans.factory.annotation.Autowired;

import com.assignsecurities.dm.PropertyKeys;
import com.assignsecurities.service.impl.ApplicationPropertiesService;


public class SMTPAuthenticator extends javax.mail.Authenticator {

	private Long ogrGroupId = null;
	
	@Autowired
	private ApplicationPropertiesService applicationProperties;

	public SMTPAuthenticator() {

	}

	public SMTPAuthenticator(Long ogrGroupId) {
		this.ogrGroupId = ogrGroupId;
	}

	public PasswordAuthentication getPasswordAuthentication() {
		String username = ApplicationPropertiesService
				.getPropertyStringValue(PropertyKeys.SMTP_AUTH_USER);
		String password = ApplicationPropertiesService
				.getPropertyStringValue(PropertyKeys.SMTP_AUTH_PWD);
		return new PasswordAuthentication(username, password);
	}
}