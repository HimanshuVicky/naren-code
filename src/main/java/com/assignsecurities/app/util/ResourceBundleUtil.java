package com.assignsecurities.app.util;

import java.util.Locale;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

/**
 * To load the locale resource specific resource bundle
 * 
 * @author admin
 * 
 */
@Service 
public class ResourceBundleUtil {
	

	@Autowired
	private MessageSource messageSource;
	
		
	/**
	 * 
	 * @param labelKey
	 * @param locale
	 * @return
	 */
	public String getLabel(String labelKey, Locale locale) {
		if(Objects.isNull(messageSource)) {
			return labelKey;
		}
		try {
			return messageSource.getMessage(labelKey,null, locale);
		}catch(org.springframework.context.NoSuchMessageException e) {
			return labelKey+"_"+locale;
		}
	}
	/**
	 * Get the localized message with place holder.
	 * @param labelKey
	 * @param locale
	 * @param params
	 * @return
	 */
	public String getLabel(String labelKey, Locale locale, Object... params) {
		if(Objects.isNull(messageSource)) {
			return labelKey;
		}
		return messageSource.getMessage(labelKey,params, locale);
	}
}
