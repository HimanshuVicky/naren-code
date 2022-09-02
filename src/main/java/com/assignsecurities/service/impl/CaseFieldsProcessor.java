package com.assignsecurities.service.impl;

import java.rmi.RemoteException;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;

import com.assignsecurities.app.exception.ServiceException;
import com.assignsecurities.app.util.ArgumentHelper;
import com.assignsecurities.bean.CaseBean;
import com.assignsecurities.bean.CaseFieldsBean;
import com.assignsecurities.bean.UserLoginBean;
import com.assignsecurities.dm.processor.BusinessDataProcessor;
import com.assignsecurities.domain.CaseFieldsModel;
import com.assignsecurities.domain.dm.DataLoadObjectModel;
import com.assignsecurities.domain.dm.ErrorMessageBean;
import com.assignsecurities.repository.impl.CaseFieldsRepo;

public class CaseFieldsProcessor implements BusinessDataProcessor {
	@Autowired
	private CaseService caseService;

	@Autowired
	private CaseFieldsRepo caseFieldsRepo;

	@Override
	public void processBusinessData(DataLoadObjectModel dataLoadObjectModel,
			List<ErrorMessageBean> processorValidationErrorModels, UserLoginBean user)
			throws ServiceException, RemoteException {
		CaseFieldsBean caseFieldsBean = (CaseFieldsBean) dataLoadObjectModel.getBusinessObjectModel();
		System.out.println(caseFieldsBean.getFieldName()+"===>"+caseFieldsBean.getFieldValue());
		if (ArgumentHelper.isValid(caseFieldsBean.getFieldValue())) {
			if(caseFieldsBean.getFieldValue().equals("*")) {
				caseFieldsBean.setFieldValue(" ");
			}
			List<CaseBean> caseBeans = caseService.getByReferenceNumber(caseFieldsBean.getReferenceNumber(), user);
			CaseBean caseBean = caseBeans.get(0);
			// TODO to save Fields this we will overwrite the existing fields if value
			// presents.
			Long caseId = caseBean.getId();
			CaseFieldsModel fieldsModel = caseFieldsRepo.getByCaseIdAndFieldName(caseId, caseFieldsBean.getFieldName());
			CaseFieldsModel fieldsModelToWork = CaseFieldsModel.builder().caseId(caseId)
					.field(caseFieldsBean.getFieldName()).fieldValue(caseFieldsBean.getFieldValue()).build();
			if (Objects.isNull(fieldsModel)) {
				caseFieldsRepo.add(fieldsModelToWork);
			} else {
				caseFieldsRepo.update(fieldsModelToWork);
			}
		}
	}
}
