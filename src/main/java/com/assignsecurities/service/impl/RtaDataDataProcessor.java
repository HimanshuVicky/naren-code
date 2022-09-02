package com.assignsecurities.service.impl;

import java.rmi.RemoteException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.assignsecurities.app.exception.ServiceException;
import com.assignsecurities.bean.RtaDataBean;
import com.assignsecurities.bean.UserLoginBean;
import com.assignsecurities.dm.processor.BusinessDataProcessor;
import com.assignsecurities.domain.dm.DataLoadObjectModel;
import com.assignsecurities.domain.dm.ErrorMessageBean;

import lombok.extern.slf4j.Slf4j;
@Slf4j
public class RtaDataDataProcessor implements BusinessDataProcessor {

	@Autowired
	private RtaDataService rtaDataService;

	@Override
	public void processBusinessData(DataLoadObjectModel dataLoadObjectModel,
			List<ErrorMessageBean> processorValidationErrorModels, UserLoginBean user)
			throws ServiceException, RemoteException {
		RtaDataBean bean = (RtaDataBean) dataLoadObjectModel.getBusinessObjectModel();
//		System.out.println("Inside RtaDataDataProcessor");
//		log.info("CompanyName=======>"+bean.getCompanyName());
		if (dataLoadObjectModel.getAction().equalsIgnoreCase("add")) {
			rtaDataService.save(bean);
		} else if (dataLoadObjectModel.getAction().equalsIgnoreCase("edit")) {
			RtaDataBean bean2 = rtaDataService.getRtaDataByCompanyName(bean.getCompanyName());
			bean.setId(bean2.getId());
			rtaDataService.update(bean);
		}
	}


}
