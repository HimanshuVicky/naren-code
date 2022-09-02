package com.assignsecurities.dm.reader.validator;


import com.assignsecurities.app.exception.ServiceException;
import com.assignsecurities.bean.UserLoginBean;
import com.assignsecurities.domain.dm.Document;
import com.assignsecurities.domain.dm.ObjectConfigBean;

/**
 * 
 * @author pkumar
 * 
 */
public interface TemplateValidator {
	/**
	 * This method will do template validation and return the string message.
	 * 
	 * @param doc
	 *            imported document model
	 * 
	 * @param objectConfigModel
	 *            object configuration model
	 * @return null if no validation error otherwise error message.
	 * @throws ServiceException
	 *             for any error
	 */
	public String validate(Document doc, ObjectConfigBean objectConfigModel, UserLoginBean user)
			throws ServiceException;

}
