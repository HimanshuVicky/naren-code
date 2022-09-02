package com.assignsecurities.service.impl.doc.processor.customeragreement;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import com.assignsecurities.app.AppConstant;
import com.assignsecurities.app.util.ArgumentHelper;
import com.assignsecurities.domain.AddressModel;
import com.assignsecurities.domain.CaseModel;
import com.assignsecurities.service.impl.doc.processor.DocPlaceHolderDataProcessor;

public class CustomerAgreemntDocPlaceHolderDataProcessor extends DocPlaceHolderDataProcessor{
	@Override
	public Map<String, String> preparePlaceHolderData(Object obj){
		CaseModel caseModel = (CaseModel) obj;
		Map<String, String> substitutionData = new HashMap<>();
//		substitutionData.put("${name}", userLoginBean.getApplicationUserBean().getName());
		String currentDate = DateTimeFormatter.ofPattern(AppConstant.DD_MMM_YYYY).format(LocalDate.now());
		substitutionData.put("CurrentDate", currentDate);
		String applicantName = caseModel.getFirstName();
		if(ArgumentHelper.isValid(caseModel.getMiddleName())) {
			applicantName = applicantName + " " + caseModel.getMiddleName();
		}
		applicantName = applicantName + " " + caseModel.getLastName();
		substitutionData.put("ApplicantName",applicantName);
		AddressModel communcationAddress = caseModel.getCommAddress();
		String commAddress = communcationAddress.getAddress() + ", " + communcationAddress.getCity() + ", "
				+ communcationAddress.getState() + ", " + communcationAddress.getCountry() + ", "
				+ communcationAddress.getPinCode();
		substitutionData.put("CommunicationAddress", commAddress);
		String applicantGender ="his";
		if(Objects.nonNull(caseModel.getGender())) {
			if(caseModel.getGender().equals("M")) {
				applicantGender ="his";
			}else if(caseModel.getGender().equals("F")) {
				applicantGender ="her";
			}
		}
		substitutionData.put("ApplicantGender",applicantGender);
		
		return substitutionData;

	}
}
