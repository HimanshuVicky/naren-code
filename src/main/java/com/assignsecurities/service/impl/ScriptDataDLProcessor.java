package com.assignsecurities.service.impl;

import java.util.List;

import com.assignsecurities.app.exception.ServiceException;
import com.assignsecurities.bean.UserLoginBean;
import com.assignsecurities.dm.processor.DownLoadBusinessDataProcessor;
import com.assignsecurities.domain.dm.ObjectBean;
import com.assignsecurities.domain.dm.ObjectConfigBean;

public class ScriptDataDLProcessor implements DownLoadBusinessDataProcessor{

	@Override
	public List<Object> getBusinessDataToProcess(ObjectBean objectModel, ObjectConfigBean objectConfigModel,
			UserLoginBean user) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

}
