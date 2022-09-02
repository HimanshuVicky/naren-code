package com.assignsecurities.dm.reader.validator;

import java.util.HashMap;
import java.util.Map;

import com.assignsecurities.app.util.StringUtil;
import com.assignsecurities.domain.dm.ObjectConfigBean;


public class BusinessValidatorFactory {
	private static final Map<String, Class> businessValidatorParserMap = new HashMap<String, Class>();

	/**
	 * 
	 * @param objectConfigModel
	 * @return
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 */
	public static BusinessValidator getValidatorInstance(
			ObjectConfigBean objectConfigModel) throws IllegalAccessException,
			InstantiationException {
		String templateValidatorKey = String.valueOf(objectConfigModel.getId());
		if (businessValidatorParserMap.containsKey(templateValidatorKey
				.toUpperCase())) {
			Class cls = businessValidatorParserMap.get(templateValidatorKey);
			if (cls != null) {
				return (BusinessValidator) cls.newInstance();
			}
		}else if(StringUtil.isValidString(objectConfigModel.getBusinessValidatorClassName())){
			try {
				Class cls = Class.forName(objectConfigModel.getBusinessValidatorClassName());
				businessValidatorParserMap.put(String.valueOf(objectConfigModel.getId()),cls);
				return (BusinessValidator) cls.newInstance();
			} catch (ClassNotFoundException e) {
				return null;
			}
				
		}
		return null;
	}
}
