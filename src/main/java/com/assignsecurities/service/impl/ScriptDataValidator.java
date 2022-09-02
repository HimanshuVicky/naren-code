package com.assignsecurities.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;

import com.assignsecurities.app.exception.ServiceException;
import com.assignsecurities.app.exception.ValidationException;
import com.assignsecurities.app.util.MessageConstants;
import com.assignsecurities.bean.ScriptBean;
import com.assignsecurities.bean.UserLoginBean;
import com.assignsecurities.bean.ValidationError;
import com.assignsecurities.dm.reader.validator.BasicValidator;
import com.assignsecurities.dm.reader.validator.BusinessValidator;
import com.assignsecurities.domain.dm.DataLoadObjectModel;
import com.assignsecurities.domain.dm.ErrorMessageBean;
import com.assignsecurities.domain.dm.ObjectConfigBean;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ScriptDataValidator implements BusinessValidator{
	
	@Autowired
	private ScriptService scriptService;

	@Autowired
	private RtaDataService rtaDataService;
	
	@Override
	public List<ErrorMessageBean> validate(DataLoadObjectModel dataLoadObjectModel, ObjectConfigBean objectConfigModel,
			UserLoginBean uam) throws ServiceException {
		ErrorMessageBean errorMessageBean = null;
		List<ErrorMessageBean> errorMessageBeans = new ArrayList<ErrorMessageBean>();
		ScriptBean scriptBean = (ScriptBean) dataLoadObjectModel.getBusinessObjectModel();
//		log.info("Inside ScriptDataValidator");
//		log.info("ScriptDataValidator::getFolioNumberOrDpIdClientId=======>" + scriptBean.getFolioNumberOrDpIdClientId());
		if (!scriptBean.isFolioNumberOrDpIdClientIdExists()) {
			errorMessageBean = new ErrorMessageBean(
					BasicValidator
							.getErrorMessage(
									MessageConstants.FOLIO_NUMBER_OR_DP_ID_CLIENT_ID_REQUIRED,
									uam));
			errorMessageBeans.add(errorMessageBean);
		}
		if (dataLoadObjectModel.getAction().equalsIgnoreCase("add")) {
			// For Add
			// Validate Duplicate folioNumber
			Boolean isFolioNumberOrDpIdClientIdAndActualDateTransferIEPF = scriptService.isFolioNumberOrDpIdClientIdAndActualDateTransferIEPF(
					scriptBean.getFolioNumberOrDpIdClientId(), scriptBean.getActualDateTransferIEPF(), uam);
			if (Objects.nonNull(isFolioNumberOrDpIdClientIdAndActualDateTransferIEPF) 
					&& isFolioNumberOrDpIdClientIdAndActualDateTransferIEPF.booleanValue()) {
				errorMessageBean = new ErrorMessageBean(
						BasicValidator
								.getErrorMessage(
										MessageConstants.FOLIO_NUMBER_ALREDAY_EXISTS_WITH,
										uam,
										scriptBean.getFolioNumberOrDpIdClientId()));
				errorMessageBeans.add(errorMessageBean);
//				log.info("FOLIO_NUMBER_ALREDAY_EXISTS_WITH--->"+scriptBean.getFolioNumberOrDpIdClientId());
			}
		} else if (dataLoadObjectModel.getAction().equalsIgnoreCase("edit")) {
			// For Update
			// Validate folioNumber should be exists
//			ScriptBean scriptBeanByFolioNumber= scriptService.getFolioNumberOrDpIdClientId(scriptBean.getFolioNumberOrDpIdClientId(),uam);
//			if (Objects.isNull(scriptBeanByFolioNumber)) {
//				errorMessageBean = new ErrorMessageBean(
//						BasicValidator
//								.getErrorMessage(
//										MessageConstants.FOLIO_NUMBER_NOT_EXISTS_WITH,
//										uam,
//										scriptBean.getFolioNumberOrDpIdClientId()));
//				errorMessageBeans.add(errorMessageBean);
//			}
			throw new ValidationException("Edit not supported for the script Upload",ValidationError.builder().build());
		}
		
		Boolean isRtaDataByCompanyName = rtaDataService.isRtaDataByCompanyName(scriptBean.getCompanyName().trim());
		if (Objects.nonNull(isRtaDataByCompanyName) && !isRtaDataByCompanyName.booleanValue()) {
//			log.info("ScriptDataValidator::scriptBean.getCompanyName()=======>" + scriptBean.getCompanyName() +"<===");
			errorMessageBean = new ErrorMessageBean(
					BasicValidator
							.getErrorMessage(
									MessageConstants.COMPANY_NAME_NOT_EXISTS_IN_RTA_DATA,
									uam,
									scriptBean.getCompanyName(),scriptBean.getFolioNumberOrDpIdClientId()));
			errorMessageBeans.add(errorMessageBean);
		}
		return errorMessageBeans;
	}

}
