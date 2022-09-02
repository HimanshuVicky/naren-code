package com.assignsecurities.dm.processor;

import java.util.List;

import com.assignsecurities.app.exception.ServiceException;
import com.assignsecurities.bean.UserLoginBean;
import com.assignsecurities.domain.dm.ObjectBean;
import com.assignsecurities.domain.dm.ObjectConfigBean;



public class DefaultBusinessDataProcessor implements DownLoadBusinessDataProcessor {

	public List<Object> getBusinessDataToProcess(ObjectBean objectModel,
												 ObjectConfigBean objectConfigModel, UserLoginBean user)
			throws ServiceException {
		
		return null;
	}

	

}
