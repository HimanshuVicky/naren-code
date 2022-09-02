/**
 * 
 */
package com.assignsecurities.app.util;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * @author Narednra
 *
 */
public class ResourceBundleFactory {

	private ResourceBundleFactory() {

	}

	private static Map<Locale, ResourceBundle> resourceBundleMap = new HashMap<Locale, ResourceBundle>();

	public static ResourceBundle getInstance(Locale locale) {
		ResourceBundle bundle = resourceBundleMap.get(locale);
		if (bundle == null) {
			bundle = ResourceBundle.getBundle("messages", locale);
			resourceBundleMap.put(locale, bundle);
		}
		return bundle;
	}
}
