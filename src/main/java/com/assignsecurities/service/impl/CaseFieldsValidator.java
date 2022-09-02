package com.assignsecurities.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.assignsecurities.app.exception.ServiceException;
import com.assignsecurities.app.util.ArgumentHelper;
import com.assignsecurities.app.util.MessageConstants;
import com.assignsecurities.bean.CaseBean;
import com.assignsecurities.bean.CaseFieldsBean;
import com.assignsecurities.bean.UserLoginBean;
import com.assignsecurities.dm.reader.validator.BasicValidator;
import com.assignsecurities.dm.reader.validator.BusinessValidator;
import com.assignsecurities.domain.dm.DataLoadObjectModel;
import com.assignsecurities.domain.dm.ErrorMessageBean;
import com.assignsecurities.domain.dm.ObjectConfigBean;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CaseFieldsValidator implements BusinessValidator {

	@Autowired
	private CaseService caseService;

	@Override
	public List<ErrorMessageBean> validate(DataLoadObjectModel dataLoadObjectModel, ObjectConfigBean objectConfigModel,
			UserLoginBean uam) throws ServiceException {
		ErrorMessageBean errorMessageBean = null;
		List<ErrorMessageBean> errorMessageBeans = new ArrayList<ErrorMessageBean>();
		CaseFieldsBean caseFieldsBean = (CaseFieldsBean) dataLoadObjectModel.getBusinessObjectModel();
		log.info(caseFieldsBean.getSrNo() + "<====Inside CaseFieldsValidator==>"+caseFieldsBean.getReferenceNumber());
		List<CaseBean> caseBeans = caseService.getByReferenceNumber(caseFieldsBean.getReferenceNumber(), uam);
		if (ArgumentHelper.isEmpty(caseBeans)) {
			errorMessageBean = new ErrorMessageBean(
					BasicValidator.getErrorMessage(MessageConstants.CASE_REF_NUMBER_NOT_EXISTS, uam));
			errorMessageBeans.add(errorMessageBean);
		}
		//Field Not Configured 
		//MessageConstants.CASE_FIELD_NOT_EXISTS
		return errorMessageBeans;
	}

}
