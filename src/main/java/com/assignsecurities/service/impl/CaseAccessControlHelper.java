package com.assignsecurities.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import com.assignsecurities.app.AppConstant;
import com.assignsecurities.app.exception.ServiceException;
import com.assignsecurities.bean.CaseBean;
import com.assignsecurities.bean.SectionAccessFlages;
import com.assignsecurities.bean.UserLoginBean;
import com.assignsecurities.domain.StatusMasterModel;

public class CaseAccessControlHelper {

	public static final String STAGE_2 = "Stage 2";
	public static final String STAGE_1 = "Stage 1";

	private CaseAccessControlHelper() {

	}

	public static void setSectionAcessControl(CaseBean bean, List<StatusMasterModel> statusMasterModels,
			UserLoginBean userLoginBean) {
		SectionAccessFlages sectionAccessFlages = new SectionAccessFlages();
		String caseSummaryApplicationSummary = "View";
		String caseSummaryFolioDetails = "View";
		String caseSummaryProcessingPaymentInfo = "View";
		String caseSummaryeAdhar = "View";
		String caseSummaryAssignFranchise = "View";
		String caseSummaryAssignLawyer = "View";
		String caseSummaryGenerateLetter = "View";
		String caseSummaryUploadedSignDocument = "View";
		String caseSummaryUploadRTAResponse = "View";
		String caseDetailsApplicationSummary = "View";
		String caseDetailsFolioDetails = "View";
		String caseDetailsAdditionalFeesDetails = "View";
		String caseDetailsAccountDetails = "View";
		String caseDetailsWitnessInfo = "View";
		String caseDetailsSuretyInfo = "View";
		String requiredDocumentsCustomerDocuments = "View";
		String requiredDocumentsGeneratedDocuments = "View";
		bean.setStage(STAGE_1);
		
		List<String> stage1And2Statues = statusMasterModels.stream()
				.filter(sm -> (sm.getStage().trim().equals(STAGE_1) || sm.getStage().trim().equals(STAGE_2))).map(sm -> sm.getStatus().trim())
				.distinct().collect(Collectors.toList());
		
		List<String> stage2Statues = statusMasterModels.stream()
				.filter(sm -> sm.getStage().trim().equals(STAGE_2)).map(sm -> sm.getStatus().trim())
				.distinct().collect(Collectors.toList());
		
		if (stage2Statues.contains(bean.getStatus().trim())) {
			bean.setStage(STAGE_2);
		}

		if (AppConstant.USER_TYPE_END_USER.equalsIgnoreCase(userLoginBean.getApplicationUserBean().getUserType())) {
//			Customer
			if (bean.getStatus().equals(AppConstant.CaseStatus.WaitingProcessingFee.label)) {
				caseSummaryProcessingPaymentInfo = "Update";
			}
			if (bean.getStatus().equals(AppConstant.CaseStatus.WaitingCustomerAadhar.label)) {
				caseSummaryeAdhar = "Update";
			}
			if (bean.getStatus().equals(AppConstant.CaseStatus.WaitingSignedDocumentsUpload.label)) {
				caseSummaryUploadedSignDocument = "Update";
			}
//			if (bean.getStatus().equals(AppConstant.CaseStatus.WaitingRTALetter1Generation.label)) {
//				caseSummaryUploadRTAResponse = "Update";
//			}
			if (bean.getStatus().equals(AppConstant.CaseStatus.WaitingRTAResponse.label)) {
				caseSummaryUploadRTAResponse = "Update";
			}

			if (stage1And2Statues.contains(bean.getStatus().trim())) {
				requiredDocumentsGeneratedDocuments = "Update";
			}
			if (bean.getStatus().equals(AppConstant.CaseStatus.WaitingWitnessInfo.label)) {
				caseDetailsWitnessInfo = "Update";
				caseDetailsSuretyInfo = "Update";
			}
			
			if(bean.getStatus().trim().equals(AppConstant.CaseStatus.WaitingRequiredDocumentList.label)) {
				requiredDocumentsCustomerDocuments = "Update";
			}
			if(bean.getStatus().trim().equals(AppConstant.CaseStatus.WaitingAdditionalFees.label)) {
				caseDetailsAdditionalFeesDetails = "Update";
			}
			

		} else if (AppConstant.USER_TYPE_CC.equalsIgnoreCase(userLoginBean.getApplicationUserBean().getUserType())) {
//			CustomerCare
			if (bean.getStatus().equals(AppConstant.CaseStatus.WaitingProcessingFee.label)
					|| bean.getStatus().equals(AppConstant.CaseStatus.WaitingProcessingFeeConfirmation.label)) {
				caseSummaryProcessingPaymentInfo = "View";
			}
			if (bean.getStatus().equals(AppConstant.CaseStatus.WaitingRTALetter1Generation.label)) {
				caseSummaryGenerateLetter = "Update";
			}
			if (bean.getStatus().equals(AppConstant.CaseStatus.WaitingSignedDocumentsUpload.label)) {
				caseSummaryUploadedSignDocument = "Update";
			}
//			if (bean.getStatus().equals(AppConstant.CaseStatus.WaitingRTALetter1Generation.label)) {
//				caseSummaryUploadRTAResponse = "Update";
//			}
			if (bean.getStatus().equals(AppConstant.CaseStatus.WaitingRTAResponse.label)||
					bean.getStatus().equals(AppConstant.CaseStatus.WaitingRTAResponseVeirifcation.label)) {
				caseSummaryUploadRTAResponse = "Update";
			}
			caseDetailsFolioDetails = "Update";
			caseDetailsAccountDetails = "Update";
			if (stage1And2Statues.contains(bean.getStatus().trim())) {
				caseDetailsWitnessInfo = "Update";
				caseDetailsSuretyInfo = "Update";
				requiredDocumentsCustomerDocuments = "Update";
				requiredDocumentsGeneratedDocuments = "Update";
			}
			if (stage2Statues.contains(bean.getStatus().trim())) {
				 caseSummaryFolioDetails = "Update";
			}
			
		} else if (AppConstant.USER_TYPE_ADMIN.equalsIgnoreCase(userLoginBean.getApplicationUserBean().getUserType())) {
//			Admin
			if (bean.getStatus().equals(AppConstant.CaseStatus.WaitingProcessingFee.label)
					|| bean.getStatus().equals(AppConstant.CaseStatus.WaitingProcessingFeeConfirmation.label)) {
				caseSummaryProcessingPaymentInfo = "Update";
			}
			if (bean.getStatus().equals(AppConstant.CaseStatus.WaitingAdminAadhar.label)) {
				caseSummaryeAdhar = "Update";
			}
//			if (!(bean.getStatus().equals(AppConstant.CaseStatus.WaitingSubmission.label)
//					|| bean.getStatus().equals(AppConstant.CaseStatus.WaitingProcessingFee.label)
//					|| bean.getStatus().equals(AppConstant.CaseStatus.WaitingProcessingFeeConfirmation.label)
//					|| bean.getStatus().equals(AppConstant.CaseStatus.WaitingCustomerAadhar.label)
//					|| bean.getStatus().equals(AppConstant.CaseStatus.WaitingAdminAadhar.label))) {
				caseSummaryAssignFranchise = "Update";
				caseSummaryAssignLawyer = "Update";
//			}
			if (bean.getStatus().equals(AppConstant.CaseStatus.WaitingRTALetter1Generation.label)) {
				caseSummaryGenerateLetter = "Update";
			}
			if (bean.getStatus().equals(AppConstant.CaseStatus.WaitingSignedDocumentsUpload.label)) {
				caseSummaryUploadedSignDocument = "Update";
			}
//			if (bean.getStatus().equals(AppConstant.CaseStatus.WaitingRTALetter1Generation.label)) {
//				caseSummaryUploadRTAResponse = "Update";
//			}
			if (bean.getStatus().equals(AppConstant.CaseStatus.WaitingRTAResponse.label) ||
					bean.getStatus().equals(AppConstant.CaseStatus.WaitingRTAResponseVeirifcation.label)) {
				caseSummaryUploadRTAResponse = "Update";
			}
			
			caseDetailsFolioDetails = "Update";
			caseDetailsAdditionalFeesDetails = "Update";
			caseDetailsAccountDetails = "Update";
			if (stage1And2Statues.contains(bean.getStatus().trim())) {
				caseDetailsWitnessInfo = "Update";
				caseDetailsSuretyInfo = "Update";
				requiredDocumentsCustomerDocuments = "Update";
				requiredDocumentsGeneratedDocuments = "Update";
			}
			if (stage2Statues.contains(bean.getStatus().trim())) {
				 caseSummaryFolioDetails = "Update";
			}
		} else if (AppConstant.USER_TYPE_FRANCHISE
				.equalsIgnoreCase(userLoginBean.getApplicationUserBean().getUserType())) {
//			FranchiseOwner
			if (bean.getStatus().equals(AppConstant.CaseStatus.WaitingRTALetter1Generation.label)) {
				caseSummaryGenerateLetter = "Update";
			}
			if (bean.getStatus().equals(AppConstant.CaseStatus.WaitingSignedDocumentsUpload.label)) {
				caseSummaryUploadedSignDocument = "Update";
			}
//			if (bean.getStatus().equals(AppConstant.CaseStatus.WaitingRTALetter1Generation.label)) {
//				caseSummaryUploadRTAResponse = "Update";
//			}
			if (bean.getStatus().equals(AppConstant.CaseStatus.WaitingRTAResponse.label)||
					bean.getStatus().equals(AppConstant.CaseStatus.WaitingRTAResponseVeirifcation.label)) {
				caseSummaryUploadRTAResponse = "Update";
			}
			caseDetailsFolioDetails = "Update";
			if (stage1And2Statues.contains(bean.getStatus().trim())) {
				caseDetailsWitnessInfo = "Update";
				caseDetailsSuretyInfo = "Update";
				requiredDocumentsCustomerDocuments = "Update";
				requiredDocumentsGeneratedDocuments = "Update";
			}
			if (stage2Statues.contains(bean.getStatus().trim())) {
				 caseSummaryFolioDetails = "Update";
			}
		} else if (AppConstant.USER_TYPE_FRANCHISE_USER
				.equalsIgnoreCase(userLoginBean.getApplicationUserBean().getUserType())) {
//			Franchise User
			if (bean.getStatus().equals(AppConstant.CaseStatus.WaitingRTALetter1Generation.label)) {
				caseSummaryGenerateLetter = "Update";
			}
			if (bean.getStatus().equals(AppConstant.CaseStatus.WaitingSignedDocumentsUpload.label)) {
				caseSummaryUploadedSignDocument = "Update";
			}
//			if (bean.getStatus().equals(AppConstant.CaseStatus.WaitingRTALetter1Generation.label)) {
//				caseSummaryUploadRTAResponse = "Update";
//			}
			if (bean.getStatus().equals(AppConstant.CaseStatus.WaitingRTAResponse.label)||
					bean.getStatus().equals(AppConstant.CaseStatus.WaitingRTAResponseVeirifcation.label)) {
				caseSummaryUploadRTAResponse = "Update";
			}
			caseDetailsFolioDetails = "Update";
			if (stage1And2Statues.contains(bean.getStatus().trim())) {
				caseDetailsWitnessInfo = "Update";
				caseDetailsSuretyInfo = "Update";
				requiredDocumentsCustomerDocuments = "Update";
				requiredDocumentsGeneratedDocuments = "Update";
			}
			if (stage2Statues.contains(bean.getStatus().trim())) {
				 caseSummaryFolioDetails = "Update";
			}
		} else if (AppConstant.USER_TYPE_ADVOCATE
				.equalsIgnoreCase(userLoginBean.getApplicationUserBean().getUserType())) {
//			Lawyer
			caseDetailsAdditionalFeesDetails = "NA";
			caseDetailsAccountDetails = "NA";
		} else if (AppConstant.USER_TYPE_NOTARYL_PARTNER
				.equalsIgnoreCase(userLoginBean.getApplicationUserBean().getUserType())) {
//			Notary
			caseDetailsAdditionalFeesDetails = "NA";
			//caseDetailsAccountDetails = "NA";
		}else if (AppConstant.USER_TYPE_CHARTERED_ACCOUNTANT_PARTNER
				.equalsIgnoreCase(userLoginBean.getApplicationUserBean().getUserType())) {
//			CA
			//caseDetailsAdditionalFeesDetails = "NA";
			//caseDetailsAccountDetails = "NA";
		}
		else {
			throw new ServiceException("Not supported by this current user type.");
		}

		// Based on Flag
		if (AppConstant.USER_TYPE_END_USER.equalsIgnoreCase(userLoginBean.getApplicationUserBean().getUserType())) {
//			Customer
			if (bean.getIsFeeProcessed()) {
				caseSummaryProcessingPaymentInfo = "View";
			}
			if (bean.getIsWintessInfoReceived()) {
				caseDetailsWitnessInfo = "View";
			}
			if (bean.getIsSuretyInfoReceived()) {
				caseDetailsSuretyInfo = "View";
			}
			if (bean.getIsCustomerDocumentsRequiredVerified()) {
				requiredDocumentsCustomerDocuments = "View";
			}
			if (bean.getIsGeneratedDocumentsRequiredVerified()) {
				requiredDocumentsGeneratedDocuments = "View";
			}
			if (bean.getIsSignedDocumentsVerified()) {
				caseSummaryUploadedSignDocument = "View";
			}
			if (bean.getIsUploadRTAResponseVerified()) {
				caseSummaryUploadRTAResponse = "View";
			}
			if (bean.getIseAdharComplete()) {
				caseSummaryeAdhar = "View";
			}
			if(STAGE_1.equalsIgnoreCase(bean.getStage())) {
				caseDetailsAccountDetails = "View";
				caseDetailsWitnessInfo = "View";
				caseDetailsSuretyInfo = "View";
			}

		}
		
		
		bean.setSectionAccessFlages(sectionAccessFlages);
		sectionAccessFlages.setCaseSummaryApplicationSummary(caseSummaryApplicationSummary);
		sectionAccessFlages.setCaseSummaryFolioDetails(caseSummaryFolioDetails);
		sectionAccessFlages.setCaseSummaryProcessingPaymentInfo(caseSummaryProcessingPaymentInfo);
		sectionAccessFlages.setCaseSummaryeAdhar(caseSummaryeAdhar);
		sectionAccessFlages.setCaseSummaryAssignFranchise(caseSummaryAssignFranchise);
		sectionAccessFlages.setCaseSummaryAssignLawyer(caseSummaryAssignLawyer);
		sectionAccessFlages.setCaseSummaryGenerateLetter(caseSummaryGenerateLetter);
		sectionAccessFlages.setCaseSummaryUploadedSignDocument(caseSummaryUploadedSignDocument);
		sectionAccessFlages.setCaseSummaryUploadRTAResponse(caseSummaryUploadRTAResponse);
		sectionAccessFlages.setCaseDetailsApplicationSummary(caseDetailsApplicationSummary);
		sectionAccessFlages.setCaseDetailsFolioDetails(caseDetailsFolioDetails);
		sectionAccessFlages.setCaseDetailsAdditionalFeesDetails(caseDetailsAdditionalFeesDetails);
		sectionAccessFlages.setCaseDetailsAccountDetails(caseDetailsAccountDetails);
		sectionAccessFlages.setCaseDetailsWitnessInfo(caseDetailsWitnessInfo);
		sectionAccessFlages.setCaseDetailsSuretyInfo(caseDetailsSuretyInfo);
		sectionAccessFlages.setRequiredDocumentsCustomerDocuments(requiredDocumentsCustomerDocuments);
		sectionAccessFlages.setRequiredDocumentsGeneratedDocuments(requiredDocumentsGeneratedDocuments);
	}
}
