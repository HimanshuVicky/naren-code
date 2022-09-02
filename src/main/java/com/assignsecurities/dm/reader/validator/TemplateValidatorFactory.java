package com.assignsecurities.dm.reader.validator;


import com.assignsecurities.app.util.DMConstants;
import com.assignsecurities.app.util.ResourceBundleUtil;
import com.assignsecurities.dm.reader.validator.excel.ExcelTemplateValidator;
import com.assignsecurities.dm.reader.validator.xml.XMLTemplateValidator;

/**
 * This is the factory class which will return the template Validator as per the
 * Object and the document type.
 * 
 * 
 */
public class TemplateValidatorFactory {
	/**
	 * 
	 * @param templateValidatorKey
	 * @return
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 */
	public static TemplateValidator getValidatorInstance(String object,
														 String objectType, ResourceBundleUtil resourceBundleUtil) throws IllegalAccessException,
			InstantiationException {
		if (DMConstants.DOCUMENT_TYPE_FORMAT_XLSX.equals(objectType)) {
			return new ExcelTemplateValidator();
		} else if (DMConstants.DOCUMENT_TYPE_FORMAT_XML.equals(objectType)) {
			return new XMLTemplateValidator(resourceBundleUtil);
		}
		return null;
	}
}
