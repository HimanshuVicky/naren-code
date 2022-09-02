package com.assignsecurities.app;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.assignsecurities.domain.MessageTemplate;
import com.assignsecurities.repository.impl.MessageTemplateRepo;


@Service("messageTemplateService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class MessageTemplateService {
	private static final Logger logger = LogManager.getLogger(MessageTemplateService.class);
	@Autowired
	private MessageTemplateRepo messageTemplateRepo;

	private static Map<String, String> appPropertyKeyValues = new HashMap<String, String>();

	public Map<String, MessageTemplate> getAllMessageTemplates() {
		return messageTemplateRepo.getAllMessageTemplates();
	}

	public void loadMessageTemplate() {
		logger.info("Loaing Message Templates");
		appPropertyKeyValues.clear();
		Map<String, MessageTemplate> appProperties = getAllMessageTemplates();
		for (String key : appProperties.keySet()) {
			appPropertyKeyValues.put(key, appProperties.get(key).getMessage());
		}
	}

	public static String getMessage(String messageKey) {
		String message =appPropertyKeyValues.get(messageKey);
		if(Objects.nonNull(message)) {
			return message;
		}
		
		return "Test";
	}

}
