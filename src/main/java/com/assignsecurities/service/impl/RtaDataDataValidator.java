package com.assignsecurities.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;

import com.assignsecurities.app.exception.ServiceException;
import com.assignsecurities.app.util.MessageConstants;
import com.assignsecurities.bean.RtaDataBean;
import com.assignsecurities.bean.UserLoginBean;
import com.assignsecurities.dm.reader.validator.BasicValidator;
import com.assignsecurities.dm.reader.validator.BusinessValidator;
import com.assignsecurities.domain.dm.DataLoadObjectModel;
import com.assignsecurities.domain.dm.ErrorMessageBean;
import com.assignsecurities.domain.dm.ObjectConfigBean;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RtaDataDataValidator implements BusinessValidator {

	@Autowired
	private RtaDataService rtaDataService;

	@Override
	public List<ErrorMessageBean> validate(DataLoadObjectModel dataLoadObjectModel, ObjectConfigBean objectConfigModel,
			UserLoginBean uam) throws ServiceException {
		ErrorMessageBean errorMessageBean = null;
		List<ErrorMessageBean> errorMessageBeans = new ArrayList<ErrorMessageBean>();
		RtaDataBean rtaDataBean = (RtaDataBean) dataLoadObjectModel.getBusinessObjectModel();
		log.info("Inside ScriptDataValidator");
		log.info("RtaDataDataValidator::CompanyName=======>" + rtaDataBean.getCompanyName());
		if (dataLoadObjectModel.getAction().equalsIgnoreCase("add")) {
			// For Add
			// Validate Duplicate folioNumber
			RtaDataBean bean2 = rtaDataService.getRtaDataByCompanyName(rtaDataBean.getCompanyName());
			if (Objects.nonNull(bean2)) {
				errorMessageBean = new ErrorMessageBean(BasicValidator.getErrorMessage(
						MessageConstants.COMPANY_NAME_ALREDAY_EXISTS_WITH, uam, rtaDataBean.getCompanyName()));
				errorMessageBeans.add(errorMessageBean);
			}
		} else if (dataLoadObjectModel.getAction().equalsIgnoreCase("edit")) {
			// For Update
			// Validate folioNumber should be exists
			RtaDataBean bean2 = rtaDataService.getRtaDataByCompanyName(rtaDataBean.getCompanyName());
			if (Objects.isNull(bean2)) {
				errorMessageBean = new ErrorMessageBean(BasicValidator.getErrorMessage(
						MessageConstants.COMPANY_NAME_NOT_EXISTS_WITH, uam, rtaDataBean.getCompanyName()));
				errorMessageBeans.add(errorMessageBean);
			}
		}
		return errorMessageBeans;
	}

}
