package com.assignsecurities.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.assignsecurities.domain.ApplicationProperties;
import com.assignsecurities.repository.impl.ApplicationPropertiesRepo;



@Service("applicationPropertiesService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class ApplicationPropertiesService {
	private static final Logger logger = LogManager.getLogger(ApplicationPropertiesService.class);	
	@Autowired
	private ApplicationPropertiesRepo applicationPropertiesRepo;

	private static Map<String, String> appPropertyKeyValues = new HashMap<String, String>();

	public Map<String, ApplicationProperties> getAllActiveProperties() {
		return applicationPropertiesRepo.getAllActiveProperties();
	}
	
	public void updatePrice(ApplicationProperties model) {
		applicationPropertiesRepo.updatePrice(model);
	}

	public void loadApplicationProperties() {
		logger.info("Loaing Application Properties");
		appPropertyKeyValues.clear();
		Map<String, ApplicationProperties> appProperties = getAllActiveProperties();
		for (String key : appProperties.keySet()) {
			appPropertyKeyValues.put(key, appProperties.get(key).getCurrentValue());
		}
	}

	public static String getPropertyStringValue(String propertyKey) {
		return appPropertyKeyValues.get(propertyKey);
	}

	public static Long getPropertyLongValue(String propertyKey) {
		String value = appPropertyKeyValues.get(propertyKey);

		if (value != null) {
			return Long.parseLong(value);
		}

		return null;
	}

	public static Integer getPropertyIntegerValue(String propertyKey) {
		String value = appPropertyKeyValues.get(propertyKey);

		if (value != null) {
			return Integer.parseInt(value);
		}

		return null;
	}

	public static Boolean getPropertyBooleanValue(String propertyKey) {
		String value = appPropertyKeyValues.get(propertyKey);
		if (value != null && (value.equalsIgnoreCase("true") || value.equalsIgnoreCase("1"))) {
			return true;
		} else {
			return false;
		}
	}
	
	public static void addProperty(String propertyKey, String value) {
		appPropertyKeyValues.put(propertyKey,value);
	}
}
