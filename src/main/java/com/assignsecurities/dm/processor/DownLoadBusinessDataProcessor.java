package com.assignsecurities.dm.processor;

import java.util.List;

import com.assignsecurities.app.exception.ServiceException;
import com.assignsecurities.bean.UserLoginBean;
import com.assignsecurities.domain.dm.ObjectBean;
import com.assignsecurities.domain.dm.ObjectConfigBean;

public interface DownLoadBusinessDataProcessor {
	/**
	 * get all available ma
	 * @param objectConfigModel
	 * @param user
	 * @return
	 */
	public List<Object> getBusinessDataToProcess(ObjectBean objectModel,
												 ObjectConfigBean objectConfigModel,
												 UserLoginBean user) throws ServiceException;
}
