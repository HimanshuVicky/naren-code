package com.assignsecurities.service.impl.doc.processor.other;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import com.assignsecurities.app.AppConstant;
import com.assignsecurities.app.util.ArgumentHelper;
import com.assignsecurities.app.util.DateUtil;
import com.assignsecurities.domain.AddressModel;
import com.assignsecurities.domain.CaseDeathCertificateDtlModel;
import com.assignsecurities.domain.CaseScriptModel;
import com.assignsecurities.domain.CaseShareCertificateDetailsModel;
import com.assignsecurities.domain.CaseTemplateBean;
import com.assignsecurities.domain.ScriptModel;
import com.assignsecurities.domain.SuretyInfoModel;
import com.assignsecurities.domain.WitnessModel;
import com.assignsecurities.service.impl.doc.processor.DocPlaceHolderDataProcessor;

public class OtherPlaceHolderDataProcessor extends DocPlaceHolderDataProcessor {

	private static final String NOT_APPLICABLE = "Not Applicable";

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
//		StringBuilder applicantNameBuilder = new StringBuilder();
		String applicantName = "N_A";
		String applicant2Name = "N_A";
		
		String primaryCaseHolder = "N_A";
		String secondayCaseHolder = "N_A";
//		scripts.forEach(sc -> {
//			ScriptModel script = sc.getScriptModel();
//			applicantName = sc.getPrimaryCaseHolder();
//			substitutionData.put("FaceValue", script.getNominalValue().toString());
//			if (ArgumentHelper.isValid(script.getIsinCode())) {
//				substitutionData.put("ISINNumber", script.getIsinCode());
//			}
//			folioNos.append(script.getFolioNumberOrDpIdClientId() + ", ");
//			noOfShares.getAndAdd(script.getNumberOfShare().intValue());
//		});
		String primaryHolderFatherHusbandName = null;
		String secondayHolderFatherHusbandName = null;
		Integer primaryHolderAge = null;
		Integer secondayHolderAge  = null;
		boolean isPrimaryCaseHolderDead = Boolean.FALSE;
		boolean isSecondayCaseHolderDead = Boolean.FALSE;
		for(CaseScriptModel sc : scripts){
			ScriptModel script = sc.getScriptModel();
			if(ArgumentHelper.isValid(sc.getPrimaryCaseHolder())) {
				applicantName = sc.getPrimaryCaseHolder();
				primaryCaseHolder = applicantName;
			}
			if(!isPrimaryCaseHolderDead && sc.getIsPrimaryCaseHolderDeceased()) {
				isPrimaryCaseHolderDead = Boolean.TRUE;
			}
			if(ArgumentHelper.isValid(sc.getPrimaryHolderFatherHusbandName())) {
				primaryHolderFatherHusbandName = sc.getPrimaryHolderFatherHusbandName();
			}
			if(ArgumentHelper.isPositiveWithZero(sc.getPrimaryHolderAge().doubleValue())) {
				primaryHolderAge = sc.getPrimaryHolderAge();
			}
			
			if(!isSecondayCaseHolderDead && sc.getIsSecondayCaseHolderDeceased()) {
				isSecondayCaseHolderDead = Boolean.TRUE;
			}
			
			if(ArgumentHelper.isValid(sc.getSecondayCaseHolder())) {
				applicant2Name = sc.getSecondayCaseHolder();
				secondayCaseHolder = applicant2Name;
			}
			if(ArgumentHelper.isValid(sc.getSecondayHolderFatherHusbandName())) {
				secondayHolderFatherHusbandName = sc.getSecondayHolderFatherHusbandName();
			}
			if(ArgumentHelper.isPositiveWithZero(sc.getSecondayHolderAge().doubleValue())) {
				secondayHolderAge = sc.getSecondayHolderAge();
			}
			
			substitutionData.put("FaceValue", script.getNominalValue().toString());
			if (ArgumentHelper.isValid(script.getIsinCode())) {
				substitutionData.put("ISINNumber", script.getIsinCode());
			}
			folioNos.append(script.getFolioNumberOrDpIdClientId() + ", ");
			noOfShares.getAndAdd(script.getNumberOfShare().intValue());
		}
		
		String folioNosStr = folioNos.toString();
		if (folioNosStr.contains(",")) {
			folioNosStr = folioNosStr.substring(0, folioNosStr.lastIndexOf(","));
		}
		substitutionData.put("FolioNos", folioNosStr);

//		${CompanyName}  
		substitutionData.put("CompanyName", caseTemplateBean.getCompanyName());
		String companyAddress = caseTemplateBean.getRtaDataModel().getAddress();
		if (ArgumentHelper.isValid(caseTemplateBean.getRtaDataModel().getCity())) {
			companyAddress = companyAddress + "," + caseTemplateBean.getRtaDataModel().getCity();
		}

		if (ArgumentHelper.isValid(caseTemplateBean.getRtaDataModel().getState())) {
			companyAddress = companyAddress + "," + caseTemplateBean.getRtaDataModel().getState();
		}
		substitutionData.put("CompanyAddress", companyAddress);

//		(${RelationWithDeceased})
		if (ArgumentHelper.isNotEmpty(caseTemplateBean.getCaseDeathCertificateDtlModels())) {
			CaseDeathCertificateDtlModel deathCertificateDtlModel = caseTemplateBean.getCaseDeathCertificateDtlModels()
					.get(0);
			substitutionData.put("Applicant1RelationshipwithDeceased", deathCertificateDtlModel.getRelation());
//			${DeceasedName1}
			substitutionData.put("DeceasedName", deathCertificateDtlModel.getDeceasedName());
//			${DODOfDeceased1}
			substitutionData.put("DeceasedDateofDeath", DateTimeFormatter.ofPattern(AppConstant.DD_MMM_YYYY)
					.format(deathCertificateDtlModel.getDateOfDeath()));
		}

//		${NoOfShares} --->script.getScriptModel().getNumberOfShare()
		substitutionData.put("NoOfShares", noOfShares.get() + "");

//		${FolioNos}
//		substitutionData.put("${FolioNos}", folioNosStr);

//		${CommunicationAddress}
		AddressModel communcationAddress = caseTemplateBean.getCommAddress();
		String commAddress = communcationAddress.getAddress() + ", " + communcationAddress.getCity() + ", "
				+ communcationAddress.getState() + ", "
				+ (Objects.isNull(communcationAddress.getCountry()) ? "India" : communcationAddress.getCountry()) + ", "
				+ communcationAddress.getPinCode();
		substitutionData.put("CommunicationAddress", commAddress);
		substitutionData.put("Applicant1Address", commAddress);
		
//		${ApplicantName}
		if(!ArgumentHelper.isValid(applicantName) || "N_A".equalsIgnoreCase(applicantName.trim()) ) {
			applicantName = caseTemplateBean.getFirstName();
			if (ArgumentHelper.isValid(caseTemplateBean.getMiddleName())) {
				applicantName = applicantName + " " + caseTemplateBean.getMiddleName();
			}
			applicantName = applicantName + " " + caseTemplateBean.getLastName();
		}
//		if (ArgumentHelper.isValid(caseTemplateBean.getMiddleName())) {
//			substitutionData.put("Applicant1Fathername", caseTemplateBean.getMiddleName());
//		}else {
		substitutionData.put("Applicant1Fathername", primaryHolderFatherHusbandName);
//		}
		
		if(!ArgumentHelper.isValid(secondayHolderFatherHusbandName) || secondayHolderFatherHusbandName.trim().equals("")) {
			secondayHolderFatherHusbandName = NOT_APPLICABLE;
		}
		if (ArgumentHelper.isValid(secondayHolderFatherHusbandName)) {
			substitutionData.put("Applicant2Fathername", secondayHolderFatherHusbandName);
		}
		
		if(Objects.nonNull(primaryHolderAge) 
				&& ArgumentHelper.isPositiveWithZero(primaryHolderAge.doubleValue())) {
			substitutionData.put("Applicant1Age", primaryHolderAge+"");
		}else if (Objects.nonNull(caseTemplateBean.getDateOfBirth())) {
			substitutionData.put("Applicant1Age",DateUtil.calculateAge(caseTemplateBean.getDateOfBirth(), LocalDate.now())+"");
		}
		
		if(Objects.nonNull(secondayHolderAge) 
				&& ArgumentHelper.isPositiveWithZero(secondayHolderAge.doubleValue())) {
			substitutionData.put("Applicant2Age", secondayHolderAge+"");
		}
		if("0".equals(substitutionData.get("Applicant2Age"))) {
			substitutionData.put("Applicant2Age", NOT_APPLICABLE);
		}
		
		if(!ArgumentHelper.isValid(substitutionData.get("Applicant2Address")) || substitutionData.get("Applicant2Address").trim().equals("")) {
			substitutionData.put("Applicant2Address", NOT_APPLICABLE);
		}
		
		
		if(Objects.isNull(applicantName) || applicantName.equals("N_A")) {
			applicantName = NOT_APPLICABLE;
		}
		
		if(Objects.isNull(applicant2Name) || applicant2Name.equals("N_A")) {
			applicant2Name = NOT_APPLICABLE;
		}
		
		if(!ArgumentHelper.isValid(applicant2Name) || applicant2Name.trim().equals("")) {
			applicant2Name = NOT_APPLICABLE;
		}
		
		String applicant1NameSignature = applicantName;
		String  applicant1AddressSignature  = commAddress;  
		String  applicant1PhoneSignature  = caseTemplateBean.getApplicationUserBean().getMobile();
		String  applicant1EmailSignature  = caseTemplateBean.getApplicationUserBean().getEmailId();
		String primaryCaseHolderSignature = primaryCaseHolder;
		
		if(isPrimaryCaseHolderDead) {
			applicant1NameSignature = "";
			applicant1AddressSignature  = "";
			applicant1PhoneSignature  = "";
			applicant1EmailSignature = "";
			primaryCaseHolderSignature = "";
		}
		
		substitutionData.put("Applicant1NameSignature", applicant1NameSignature);
		substitutionData.put("Applicant1AddressSignature", applicant1AddressSignature);
		substitutionData.put("Applicant1PhoneSignature", applicant1PhoneSignature);
		substitutionData.put("Applicant1EmailSignature", applicant1EmailSignature);
		
		String applicant2NameSignature = applicant2Name;
		String applicant2AddressSignature = substitutionData.get("Applicant2Address");
		String applicant2PhoneSignature = substitutionData.get("Applicant2Phone");
		String applicant2EmailSignature = substitutionData.get("Applicant2Email");
		String secondayCaseHolderSignature = secondayCaseHolder;

		if(isSecondayCaseHolderDead) {
			applicant2NameSignature = "";
			applicant2AddressSignature  = "";
			applicant2PhoneSignature  = "";
			applicant2EmailSignature = "";
			secondayCaseHolderSignature = "";
		}
		
		substitutionData.put("Applicant2NameSignature", applicant2NameSignature);
		substitutionData.put("Applicant2AddressSignature", applicant2AddressSignature);
		substitutionData.put("Applicant2PhoneSignature", applicant2PhoneSignature);
		substitutionData.put("Applicant2EmailSignature", applicant2EmailSignature);
		
		
		substitutionData.put("ApplicantName", applicantName);
		substitutionData.put("Applicant2Name", applicant2Name);
		substitutionData.put("ApplicantPhone", caseTemplateBean.getApplicationUserBean().getMobile());
		substitutionData.put("ApplicantEmail", caseTemplateBean.getApplicationUserBean().getEmailId());
		
		if(Objects.isNull(primaryCaseHolder) || primaryCaseHolder.equals("N_A")) {
			primaryCaseHolder = "";
		}
		
		if(Objects.isNull(secondayCaseHolder) || secondayCaseHolder.equals("N_A")) {
			secondayCaseHolder = "";
		}

		substitutionData.put("Shareholder1Name", primaryCaseHolder);
		substitutionData.put("Shareholder2Name", secondayCaseHolder);
		
		substitutionData.put("Shareholder1NameSignature", primaryCaseHolderSignature);
		substitutionData.put("Shareholder2NameSignature", secondayCaseHolderSignature);
		

		substitutionData.put("ApplicantCity", communcationAddress.getCity());
		substitutionData.put("ApplicantState", communcationAddress.getState());
		substitutionData.put("ApplicantPin", communcationAddress.getPinCode());

		if (Objects.nonNull(caseTemplateBean.getDateOfBirth())) {
			Period period = Period.between(caseTemplateBean.getDateOfBirth(), LocalDate.now());
			substitutionData.put("Applicant1Age", period.getYears() + "");
		}

//		substitutionData.put("ApplicantFatherName",caseTemplateBean.getF);

//		${ApplicantBankName}
		substitutionData.put("ApplicantBankName", caseTemplateBean.getBankName());

//		${ApplicantBankAccountNumber}
		substitutionData.put("ApplicantBankAccountNumber", caseTemplateBean.getAccountNumber());
//		${ApplicantBankIFSCCode}
		substitutionData.put("ApplicantBankIFSCCode", caseTemplateBean.getIfscCode());
//		${ApplicantBankAddress}
		substitutionData.put("ApplicantBankAddress", caseTemplateBean.getBankAddress());

		String currentDay = LocalDate.now().getDayOfMonth() + "";
		String currentMonth = LocalDate.now().getMonth().name() + "";
		String currentYear = LocalDate.now().getYear() + "";
		substitutionData.put("CurrentDay", currentDay);
		substitutionData.put("CurrentMonth", currentMonth);
		substitutionData.put("CurrentYear", currentYear);
/*
 As this information is coming from the Excel sheet*/
		List<WitnessModel> witnesses = caseTemplateBean.getWitnesses();
		String witnessName = "Witness%dName";
		String witnessAddress = "Witness%dAddress";
		String witnessCity = "Witness%dCity";
		String witnessContactNumber = "Witness%dPhone";
		String witnessAadharNumber = "Witness%dAadhar";
		String witnessPanNumber = "Witness%dPan";
		if (ArgumentHelper.isNotEmpty(witnesses)) {
			AtomicInteger counter = new AtomicInteger(1);
			witnesses.forEach(w -> {
				substitutionData.put(String.format(witnessName, counter.get()), w.getName());
				substitutionData.put(String.format(witnessAddress, counter.get()), w.getAddress());
				substitutionData.put(String.format(witnessCity, counter.get()), w.getCity());
				substitutionData.put(String.format(witnessContactNumber, counter.get()), w.getContactNumber());
				substitutionData.put(String.format(witnessAadharNumber, counter.get()), w.getAadharNumber());
				substitutionData.put(String.format(witnessPanNumber, counter.get()), w.getPanNumber());
				counter.incrementAndGet();
			});
		}
		String suretyName = "Surety%dName";
		String suretyAddress = "Surety%dAddress";
		String suretyCity = "Surety%dCity";
		String suretyPhone = "Surety%dPhone";
		String suretyAadharNo = "Surety%dAadhar";
		String suretyPanNo = "Surety%dPan";
		String suretyITRRefNo = "Surety%dItr";
		List<SuretyInfoModel> suretyInfos = caseTemplateBean.getSuretyInfos();
		if (ArgumentHelper.isNotEmpty(suretyInfos)) {
			AtomicInteger counter = new AtomicInteger(1);
			suretyInfos.forEach(s -> {
				substitutionData.put(String.format(suretyName, counter.get()), s.getName());
				substitutionData.put(String.format(suretyAddress, counter.get()), s.getAddress());
				substitutionData.put(String.format(suretyCity, counter.get()), s.getCity());
				substitutionData.put(String.format(suretyPhone, counter.get()), s.getPhone());
				substitutionData.put(String.format(suretyAadharNo, counter.get()), s.getAadharNumber());
				substitutionData.put(String.format(suretyPanNo, counter.get()), s.getPanNumber());
				substitutionData.put(String.format(suretyITRRefNo, counter.get()), s.getItrRefNo());
				counter.incrementAndGet();
			});
		}
		
		List<CaseScriptModel> caseScriptModels = caseTemplateBean.getScripts();
		AtomicReference<String> shareCertificateNos = new AtomicReference<String>("");
		AtomicReference<String> distinctiveNos = new AtomicReference<String>("");
		caseScriptModels.forEach(caseScript -> {
			List<CaseShareCertificateDetailsModel> shareCertificateDetailsModels = caseScript
					.getShareCertificateDetailsModels();
			if (ArgumentHelper.isNotEmpty(shareCertificateDetailsModels)) {
				shareCertificateDetailsModels.forEach(sc -> {
					shareCertificateNos.getAndSet(shareCertificateNos.get() + sc.getCertificateNo() + ",");
					distinctiveNos.getAndSet(distinctiveNos.get() + sc.getDistinctiveNo() + ",");
				});
			}
		});
		substitutionData.put("ShareCertificateNos", removeLastComma(shareCertificateNos));
		substitutionData.put("DistinctiveNos", removeLastComma(distinctiveNos));

		String deceasedDateofDeath = substitutionData.get("DeceasedDateofDeath");
		// TODO decide the Date Format
		if (ArgumentHelper.isValid(deceasedDateofDeath)) {
			// Parse and load DeceasedDeathDay, DeceasedDeathMonth,DeceasedDeathYear
			try {
				LocalDate deceasedDateofDeathLocal = LocalDate.parse(deceasedDateofDeath,
						DateTimeFormatter.ofPattern("dd/MM/yyyy"));
				currentDay = deceasedDateofDeathLocal.getDayOfMonth() + "";
				currentMonth = deceasedDateofDeathLocal.getMonth().name() + "";
				currentYear = deceasedDateofDeathLocal.getYear() + "";
				substitutionData.put("DeceasedDeathDay", currentDay);
				substitutionData.put("DeceasedDeathMonth", currentMonth);
				substitutionData.put("DeceasedDeathYear", currentYear);
			} catch (Exception e) {
				// ignore
			}
		}
		//To give Preference to the Fields Excel over application data
		if (ArgumentHelper.isNotEmpty(caseTemplateBean.getCaseFieldsKeyValues())) {
			caseTemplateBean.getCaseFieldsKeyValues().forEach((k, v) -> {
//				System.out.println("k=>"+k + "  v=>"+v);
				if(Objects.isNull(v) || v.equalsIgnoreCase(null)) {
					caseTemplateBean.getCaseFieldsKeyValues().put(k, "");
				}
			});
			substitutionData.putAll(caseTemplateBean.getCaseFieldsKeyValues());
		}
		return substitutionData;

	}

	private String removeLastComma(AtomicReference<String> shareCertificateNos) {
		String removeComma = shareCertificateNos.get();
		if(Objects.nonNull(removeComma) && removeComma.length()>1) {
			removeComma = removeComma.substring(0, removeComma.length() - 1);
		}
		return removeComma;
	}
}
