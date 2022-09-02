package com.assignsecurities.dm.processor;

import java.rmi.RemoteException;
import java.util.List;

import com.assignsecurities.app.exception.ServiceException;
import com.assignsecurities.bean.UserLoginBean;
import com.assignsecurities.domain.dm.DataLoadObjectModel;
import com.assignsecurities.domain.dm.ErrorMessageBean;



public interface BusinessDataProcessor {
	public void processBusinessData(DataLoadObjectModel dataLoadObjectModel,
									List<ErrorMessageBean> processorValidationErrorModels, UserLoginBean user)
			throws ServiceException, RemoteException;
}
