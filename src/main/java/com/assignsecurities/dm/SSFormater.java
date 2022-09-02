package com.assignsecurities.dm;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.assignsecurities.app.exception.ServiceException;
import com.assignsecurities.bean.UserLoginBean;


/**
 * This class contains api to format data.
 * 
 */
public class SSFormater {

	@Autowired
	private AdhocFilterService adhocFilterService;

	private static SSFormater ssFormater = null;

	Map<String, Map<String, String>> objContainerMap = null;

	private SSFormater() {
		objContainerMap = new HashMap<String, Map<String, String>>();
	}

	public static void init() {
		// if (ssFormater == null) {
		ssFormater = new SSFormater();
		// }
	}

	public static SSFormater getSSFormater() {
		return ssFormater;
	}

	public static void destory() {
		ssFormater.objContainerMap.clear();
		ssFormater.objContainerMap = null;
		ssFormater = null;
	}

	/**
	 * get id for code
	 * 
	 * @param formaterKey
	 * @param key
	 * @param uam
	 * @return
	 * @throws ServiceException
	 */
	public String getIdValue(String formaterKey, String key, UserLoginBean uam)
			throws ServiceException {
		Map<String, String> pulldown = null;
		String value = null;
		if (key.equals("0") || key.equals("")) {
			return "";
		}
		if (objContainerMap.containsKey(formaterKey)) {
			pulldown = objContainerMap.get(formaterKey);

		} else {
			// AdhocFilterService adhocFilterService = adhocFilterService;
			// beanFactory.autowireBean(adhocFilterService);

			pulldown = adhocFilterService.getIdCode(formaterKey, uam);
			objContainerMap.put(formaterKey, pulldown);
		}
		// Map<String, String> itemMap = pulldown.getDisplayNameMapForSS();
		Map<String, String> itemMap = pulldown;
		// System.out.println("formaterKey==>"+formaterKey+"<==itemMap.size()====>"+itemMap.size());
		// long startTime = System.currentTimeMillis();
		for (String code : itemMap.keySet()) {
			String codeValue = itemMap.get(code).trim();
			if (codeValue.equalsIgnoreCase(key.trim())) {
				value = code;
				break;
			}
		}
		// System.out.println("===>"+(System.currentTimeMillis() - startTime));
		if (value == null) {
			throw new ServiceException("Value not found for the key :" + key
					+ " formaterKey:" + formaterKey);
		}
		return value;
	}

	/**
	 * get code for id
	 * 
	 * @param formaterKey
	 * @param key
	 * @param uam
	 * @return
	 * @throws ServiceException
	 */
	public String getCodeValue(String formaterKey, String key, UserLoginBean uam)
			throws ServiceException {
		Map<String, String> pulldown = null;
		String value = null;
		if (objContainerMap.containsKey(formaterKey)) {
			pulldown = objContainerMap.get(formaterKey);
		} else {
			pulldown = adhocFilterService.getIdCode(formaterKey, uam);
			objContainerMap.put(formaterKey, pulldown);
		}
		value = pulldown.get(key);
		if (value == null) {
			throw new ServiceException(formaterKey
					+ "Value not found for the key :" + key);
		}
		return value;

	}

}
