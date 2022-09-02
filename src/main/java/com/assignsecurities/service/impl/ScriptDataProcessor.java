package com.assignsecurities.service.impl;

import java.rmi.RemoteException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.assignsecurities.app.exception.ServiceException;
import com.assignsecurities.bean.ScriptBean;
import com.assignsecurities.bean.UserLoginBean;
import com.assignsecurities.dm.processor.BusinessDataProcessor;
import com.assignsecurities.domain.dm.DataLoadObjectModel;
import com.assignsecurities.domain.dm.ErrorMessageBean;

public class ScriptDataProcessor implements BusinessDataProcessor {

	@Autowired
	private ScriptService scriptService;

	@Override
	public void processBusinessData(DataLoadObjectModel dataLoadObjectModel,
			List<ErrorMessageBean> processorValidationErrorModels, UserLoginBean user)
			throws ServiceException, RemoteException {
		ScriptBean scriptBean = (ScriptBean) dataLoadObjectModel.getBusinessObjectModel();
//		System.out.println("Inside OrganizationDataProcessor");
		if (dataLoadObjectModel.getAction().equalsIgnoreCase("add")) {
			scriptService.save(scriptBean);
		} else if (dataLoadObjectModel.getAction().equalsIgnoreCase("edit")) {
			
//			ScriptBean scriptBean2 = scriptService.getFolioNumberOrDpIdClientId(scriptBean.getFolioNumberOrDpIdClientId(),user);
//			scriptBean.setId(scriptBean2.getId());
//			scriptService.update(scriptBean);
		}
	}

}
