package com.assignsecurities.dm.reader;

import java.util.List;

import com.assignsecurities.app.exception.ServiceException;
import com.assignsecurities.bean.UserLoginBean;
import com.assignsecurities.domain.dm.DataLoadObjectModel;
import com.assignsecurities.domain.dm.Document;
import com.assignsecurities.domain.dm.ObjectConfigBean;

/**
 * This is a common interface to read document. There will be different implementor class for different format (ie. csv,xlsx etc) 
 *
 */
public interface DocumentParser {
	/**
	 * This API is used to read document.
	 * 
	 * @return List on object
	 * @throws ServiceException
	 */
	public List<DataLoadObjectModel> readData(ObjectConfigBean objectConfigModel, Document doc, UserLoginBean user) throws ServiceException;

}
