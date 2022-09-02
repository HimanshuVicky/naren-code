package com.assignsecurities.dm.reader.validator;

import java.util.List;

import com.assignsecurities.app.exception.ServiceException;
import com.assignsecurities.bean.UserLoginBean;
import com.assignsecurities.domain.dm.DataLoadObjectModel;
import com.assignsecurities.domain.dm.ErrorMessageBean;
import com.assignsecurities.domain.dm.ObjectConfigBean;

/**
 * @author Narendra Chouhan
 * 
 */
public interface BusinessValidator {
	public List<ErrorMessageBean> validate(
			DataLoadObjectModel dataLoadObjectModel,
			ObjectConfigBean objectConfigModel, UserLoginBean uam)
			throws ServiceException;

}
