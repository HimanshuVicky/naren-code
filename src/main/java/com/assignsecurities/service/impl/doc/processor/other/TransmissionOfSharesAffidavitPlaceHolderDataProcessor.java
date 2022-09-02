package com.assignsecurities.service.impl.doc.processor.other;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

import com.assignsecurities.app.AppConstant;
import com.assignsecurities.app.util.ArgumentHelper;
import com.assignsecurities.domain.AddressModel;
import com.assignsecurities.domain.CaseDeathCertificateDtlModel;
import com.assignsecurities.domain.CaseScriptModel;
import com.assignsecurities.domain.CaseTemplateBean;
import com.assignsecurities.domain.ScriptModel;
import com.assignsecurities.service.impl.doc.processor.DocPlaceHolderDataProcessor;

public class TransmissionOfSharesAffidavitPlaceHolderDataProcessor extends DocPlaceHolderDataProcessor {

	@Override
	public Map<String, String> preparePlaceHolderData(Object obj) {
		CaseTemplateBean caseTemplateBean = (CaseTemplateBean) obj;
		Map<String, String> substitutionData = new HashMap<>();
		List<CaseScriptModel> scripts = caseTemplateBean.getScripts();
		
//		${RtaName} 
//		substitutionData.put("{RtaName}", caseTemplateBean.getRtaDataModel().getRegistrarName());
		substitutionData.put("RtaName", caseTemplateBean.getRtaDataModel().getRegistrarName());


//		${RtaAddress} 
		substitutionData.put("RtaAddress", caseTemplateBean.getRtaDataModel().getRegistrarAddress());

//		${CurrentDate}  
		String currentDate = DateTimeFormatter.ofPattern(AppConstant.DD_MMM_YYYY).format(LocalDate.now());
		substitutionData.put("CurrentDate", currentDate);

//		${FolioNo}
		StringBuilder folioNos = new StringBuilder();
		AtomicInteger noOfShares = new AtomicInteger(0);
		scripts.forEach(sc -> {
			ScriptModel script = sc.getScriptModel();
			folioNos.append(script.getFolioNumberOrDpIdClientId() + ", ");
			noOfShares.getAndAdd(script.getNumberOfShare().intValue());
		});
		String folioNosStr = folioNos.toString();
		if (folioNosStr.contains(",")) {
			folioNosStr = folioNosStr.substring(0, folioNosStr.lastIndexOf(","));
		}
		substitutionData.put("FolioNos", folioNosStr);

//		${CompanyName}  
		substitutionData.put("CompanyName", caseTemplateBean.getCompanyName());
//		substitutionData.put("CompanyName", caseTemplateBean.getCompanyName());

//		(${RelationWithDeceased})
		if(ArgumentHelper.isEmpty(caseTemplateBean.getCaseDeathCertificateDtlModels())) {
			substitutionData.put("RelationWithDeceased1", "NA");
			substitutionData.put("DeceasedRelation1", "NA");
			substitutionData.put("DecRelOne", "NA");
			
//			${DeceasedName1}
			substitutionData.put("DeceasedName1", "NA");
			substitutionData.put("DeceasedNameOne", "NA");
//			${DODOfDeceased1}
			substitutionData.put("DODOfDeceased1","NA");
			substitutionData.put("DODOfDeceasedOne","NA");
		}else {
			CaseDeathCertificateDtlModel deathCertificateDtlModel = caseTemplateBean.getCaseDeathCertificateDtlModels()
					.get(0);
			substitutionData.put("RelationWithDeceased1", deathCertificateDtlModel.getRelation());
			substitutionData.put("DeceasedRelation1", deathCertificateDtlModel.getRelation());
			substitutionData.put("DecRelOne", deathCertificateDtlModel.getRelation());
//			${DeceasedName1}
			substitutionData.put("DeceasedName1", deathCertificateDtlModel.getDeceasedName());
			substitutionData.put("DecNameOne", deathCertificateDtlModel.getDeceasedName());
			substitutionData.put("DeceasedNameOne", deathCertificateDtlModel.getDeceasedName());
//			${DODOfDeceased1}
			substitutionData.put("DODOfDeceased1",
					DateTimeFormatter.ofPattern(AppConstant.DD_MMM_YYYY).format(deathCertificateDtlModel.getDateOfDeath()));
			substitutionData.put("DODOfDeceasedOne",
					DateTimeFormatter.ofPattern(AppConstant.DD_MMM_YYYY).format(deathCertificateDtlModel.getDateOfDeath()));
		}
		

//		${NoOfShares} --->script.getScriptModel().getNumberOfShare()
		substitutionData.put("NoOfShares", noOfShares.get() + "");

//		${FolioNos}
//		substitutionData.put("${FolioNos}", folioNosStr);

//		${CommunicationAddress}
		AddressModel communcationAddress = caseTemplateBean.getCommAddress();
		String commAddress = communcationAddress.getAddress() + ", " + communcationAddress.getCity() + ", "
				+ communcationAddress.getState() + ", " + (Objects.isNull(communcationAddress.getCountry()) ? "India": communcationAddress.getCountry() )+ ", "
				+ communcationAddress.getPinCode();
		substitutionData.put("CommunicationAddress", commAddress);
//		${ApplicantName}
		String applicantName = caseTemplateBean.getFirstName();
		if(ArgumentHelper.isValid(caseTemplateBean.getMiddleName())) {
			applicantName = applicantName + " " + caseTemplateBean.getMiddleName();
		}
		applicantName = applicantName + " " + caseTemplateBean.getLastName();
		substitutionData.put("ApplicantName",applicantName);
		
//		${ApplicantBankName}
		substitutionData.put("ApplicantBankName", caseTemplateBean.getBankName());
		
//		${ApplicantBankAccountNumber}
		substitutionData.put("ApplicantBankAccountNumber", caseTemplateBean.getAccountNumber());
//		${ApplicantBankIFSCCode}
		substitutionData.put("ApplicantBankIFSCCode", caseTemplateBean.getIfscCode());
//		${ApplicantBankAddress}
		substitutionData.put("ApplicantBankAddress",caseTemplateBean.getBankAddress());
		return substitutionData;

	}
}
