package com.assignsecurities.validator;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.assignsecurities.app.exception.ValidationException;
import com.assignsecurities.app.util.ArgumentHelper;
import com.assignsecurities.app.util.MessageConstants;
import com.assignsecurities.bean.ScriptBean;
import com.assignsecurities.bean.UserLoginBean;
import com.assignsecurities.bean.ValidationError;
import com.assignsecurities.dm.reader.validator.BasicValidator;
import com.assignsecurities.service.impl.ScriptService;

@Component
public class FolioValidator {
	
	@Autowired
	private ScriptService scriptService;

	public void validateFolio(ScriptBean script, UserLoginBean userLoginBean) {
		List<ValidationError> errorList = new ArrayList<>();

		if (!ArgumentHelper.isValid(script.getFolioNumberOrDpIdClientId())) {
			errorList.add(ValidationError.builder()
					.message("Please provide value for either Favourite or dp id client id.").build());
		}
		if (!ArgumentHelper.isValid(script.getCompanyName())) {
			errorList.add(ValidationError.builder().message("Please provide value for Company Name.").build());
		}
		
		if (!ArgumentHelper.isValid(script.getIsinCode())) {
			errorList.add(ValidationError.builder().message("Please provide value for Isin Code/Number.").build());
		}
		
		if (!ArgumentHelper.isValid(script.getInvestorFirstName())) {
			errorList.add(ValidationError.builder().message("Please provide value for Investor First Name.").build());
		}

		if (!ArgumentHelper.isValid(script.getInvestorLastName())) {
			errorList.add(ValidationError.builder().message("Please provide value for Investor Last Name.").build());
		}
		
		if (!ArgumentHelper.isPositive(script.getNumberOfShare())) {
			errorList.add(ValidationError.builder().message("Please provide value for Number Of Share.").build());
		}
		if (!ArgumentHelper.isPositive(script.getNominalValue())) {
			errorList.add(ValidationError.builder().message("Please provide value for Nominal Value.").build());
		}
		if (!ArgumentHelper.isPositive(script.getMarketPrice())) {
			errorList.add(ValidationError.builder().message("Please provide value for Market Price.").build());
		}
//		ScriptBean scriptBeanByFolioNumber = scriptService
//				.getFolioNumberOrDpIdClientId(script.getFolioNumberOrDpIdClientId(), userLoginBean);
		List<ScriptBean> scriptBeans = scriptService.getFolioNumberOrDpIdClientIdAndActualDateTransferIEPF(
				script.getFolioNumberOrDpIdClientId(), script.getActualDateTransferIEPF(), userLoginBean);
		if (ArgumentHelper.isNotEmpty(scriptBeans)) {
			errorList.add(ValidationError.builder()
					.message(BasicValidator.getErrorMessage(MessageConstants.FOLIO_NUMBER_ALREDAY_EXISTS_WITH,
							userLoginBean, script.getFolioNumberOrDpIdClientId()))
					.build());

		}
		if (ArgumentHelper.isNotEmpty(errorList)) {
			throw new ValidationException(errorList);
		}
	}
}
