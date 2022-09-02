package com.assignsecurities.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

import com.assignsecurities.app.exception.ValidationException;
import com.assignsecurities.app.util.ArgumentHelper;
import com.assignsecurities.bean.CaseAadharBean;
import com.assignsecurities.bean.CaseAadharGenerateOtpBean;
import com.assignsecurities.bean.CaseCancelChequeBean;
import com.assignsecurities.bean.CaseDeathCertificateDtlBean;
import com.assignsecurities.bean.CaseDocumentBean;
import com.assignsecurities.bean.CaseDocumentUploadBean;
import com.assignsecurities.bean.CaseKYCBean;
import com.assignsecurities.bean.CasePanBean;
import com.assignsecurities.bean.CasePanVerifyBean;
import com.assignsecurities.bean.CustomerCasePendingDetails;
import com.assignsecurities.bean.CustomerCasePendingFolioDetails;
import com.assignsecurities.bean.CustomerKYCConfirmationBean;
import com.assignsecurities.bean.DeleteCaseDocumentBean;
import com.assignsecurities.bean.KycOtpRequestBean;
import com.assignsecurities.bean.ProcessApplicationBean;
import com.assignsecurities.bean.UploadCaseDocument;
import com.assignsecurities.bean.ValidationError;
import com.assignsecurities.domain.CaseModel;

public class CaseValidator {
	public static void validateCustomerCasePendingFolioDetails(CustomerCasePendingFolioDetails details) {
		List<ValidationError> errorList = new ArrayList<>();
		if (!ArgumentHelper.isValid(details.getCaseId() + "")) {
			errorList.add(ValidationError.builder().message("Please provide case Id.").build());
		}
		if (ArgumentHelper.isEmpty(details.getScripts())) {
			errorList.add(ValidationError.builder().message("Please provide Script Details.").build());
		}else {
			AtomicInteger counter = new AtomicInteger(1);
			details.getScripts().forEach(script -> {
				if ((!ArgumentHelper.isValid(script.getScriptId()+""))) {
					errorList.add(ValidationError.builder()
							.message(String.format("Please provide script %d Id.", counter.get())).build());
				}

//				if ((!ArgumentHelper.isValid(script.getCertNo()))) {
//					errorList.add(ValidationError.builder()
//							.message(String.format("Please provide script %d Certificate No.", counter.get())).build());
//				}
//				if ((!ArgumentHelper.isValid(script.getDistinctiveNos()))) {
//					errorList.add(ValidationError.builder()
//							.message(String.format("Please provide script %d Distinctive Nos.", counter.get())).build());
//				}
//
//				if ((!ArgumentHelper.isPositive(script.getFaceValue()))) {
//					errorList.add(ValidationError.builder()
//							.message(String.format("Please appropriate provide script %d Face Value.", counter.get())).build());
//				}

				if ((!ArgumentHelper.isValid(script.getPrimaryCaseHolder()))) {
					errorList.add(ValidationError.builder()
							.message(String.format("Please provide script %d Primary Case Holder Name.", counter.get()))
							.build());
				}
				
				counter.incrementAndGet();
			});
		}
		if (ArgumentHelper.isNotEmpty(errorList)) {
			throw new ValidationException(errorList);
		}

	}

	public static void validateGenerateCaseDocument(CaseDocumentBean caseDocumentBean) {
		List<ValidationError> errorList = new ArrayList<>();
		if (!ArgumentHelper.isValid(caseDocumentBean.getCaseId() + "")) {
			errorList.add(ValidationError.builder().message("Please provide case Id.").build());
		}
		if (!ArgumentHelper.isValid(caseDocumentBean.getTemplateType())) {
			errorList.add(ValidationError.builder().message("Please provide Template Type.").build());
		}
		if (ArgumentHelper.isNotEmpty(errorList)) {
			throw new ValidationException(errorList);
		}

	}

	public static void validateCustomerCasePendingDetails(CustomerCasePendingDetails details) {
		List<ValidationError> errorList = new ArrayList<>();
		if (!ArgumentHelper.isValid(details.getCaseId() + "")) {
			errorList.add(ValidationError.builder().message("Please provide case Id.").build());
		}
		if (Objects.isNull(details.getHireCertificate())) {
			errorList.add(ValidationError.builder().message("Please provide Hire Certificate Details.").build());
		} else {
			if (!ArgumentHelper.isValid(details.getHireCertificate().getFileTitle())) {
				errorList.add(ValidationError.builder().message("Please provide Hire Certificate Title.").build());
			}

			if (!ArgumentHelper.isValid(details.getHireCertificate().getFileContentType())) {
				errorList.add(ValidationError.builder().message("Please provide Hire Certificate File Content Type.")
						.build());
			}

			if (!ArgumentHelper.isValid(details.getHireCertificate().getFileContent())) {
				errorList.add(
						ValidationError.builder().message("Please provide Hire Certificate File Content.").build());
			}
		}
		if (ArgumentHelper.isEmpty(details.getWitnesses())) {
			errorList.add(ValidationError.builder().message("Please provide Witnesses Details.").build());
		} else {
			if (details.getWitnesses().size() < 4) {
				errorList.add(ValidationError.builder().message("Please provide all four Witness Details.").build());
			}

			AtomicInteger counter = new AtomicInteger();
			details.getWitnesses().forEach(witness -> {
				if ((!ArgumentHelper.isValid(witness.getName()))) {
					errorList.add(ValidationError.builder()
							.message(String.format("Please provide witness %d name.", counter.get())).build());
				}

				if ((!ArgumentHelper.isValid(witness.getAddress()))) {
					errorList.add(ValidationError.builder()
							.message(String.format("Please provide witness %d address.", counter.get())).build());
				}

				if ((!ArgumentHelper.isValid(witness.getCity()))) {
					errorList.add(ValidationError.builder()
							.message(String.format("Please provide witness %d c.", counter.get())).build());
				}

				if ((!ArgumentHelper.isValid(witness.getContactNumber()))) {
					errorList.add(ValidationError.builder()
							.message(String.format("Please provide witness %d Contact Number.", counter.get()))
							.build());
				}

				counter.incrementAndGet();
			});
		}
		if (ArgumentHelper.isNotEmpty(errorList)) {
			throw new ValidationException(errorList);
		}

	}

	public static void validateCancelChequeDetails(CaseCancelChequeBean caseCancelChequeBean) {
		List<ValidationError> errorList = new ArrayList<>();
		if (!ArgumentHelper.isValid(caseCancelChequeBean.getId() + "")) {
			errorList.add(ValidationError.builder().message("Please provide case Id.").build());
		}
		if (!ArgumentHelper.isValid(caseCancelChequeBean.getChequeNumber())) {
			errorList.add(ValidationError.builder().message("Please provide Cheque Number.").build());
		}
		if (Objects.isNull(caseCancelChequeBean.getCancelChequeImage())) {
			errorList.add(ValidationError.builder().message("Please provide Cancel Check Image Details.").build());
		} else {
			if (!ArgumentHelper.isValid(caseCancelChequeBean.getCancelChequeImage().getFileTitle())) {
				errorList.add(ValidationError.builder().message("Please provide Cancel Check Image Title.").build());
			}

			if (!ArgumentHelper.isValid(caseCancelChequeBean.getCancelChequeImage().getFileContentType())) {
				errorList.add(ValidationError.builder().message("Please provide Cancel Check Image File Content Type.")
						.build());
			}

			if (!ArgumentHelper.isValid(caseCancelChequeBean.getCancelChequeImage().getFileContent())) {
				errorList.add(
						ValidationError.builder().message("Please provide Cancel Check Check File Content.").build());
			}
		}
		if (ArgumentHelper.isNotEmpty(errorList)) {
			throw new ValidationException(errorList);
		}
	}

	public static void validatePanDetails(CasePanBean casePanBean) {
		List<ValidationError> errorList = new ArrayList<>();
		if (!ArgumentHelper.isValid(casePanBean.getId() + "")) {
			errorList.add(ValidationError.builder().message("Please provide case Id.").build());
		}

		if (!ArgumentHelper.isValid(casePanBean.getPanNumber())) {
			errorList.add(ValidationError.builder().message("Please provide Pan Number.").build());
		}

		if (Objects.isNull(casePanBean.getPanImage())) {
			errorList.add(ValidationError.builder().message("Please provide Pan Image Details.").build());
		} else {
			if (!ArgumentHelper.isValid(casePanBean.getPanImage().getFileTitle())) {
				errorList.add(ValidationError.builder().message("Please provide Pan Image Title.").build());
			}

			if (!ArgumentHelper.isValid(casePanBean.getPanImage().getFileContentType())) {
				errorList.add(ValidationError.builder().message("Please provide Pan Image File Content Type.").build());
			}

			if (!ArgumentHelper.isValid(casePanBean.getPanImage().getFileContent())) {
				errorList.add(ValidationError.builder().message("Please provide Pan Image File Content.").build());
			}
		}

		if (ArgumentHelper.isNotEmpty(errorList)) {
			throw new ValidationException(errorList);
		}

	}

	public static void validateAadharDetails(CaseAadharBean caseAadharBean) {
		List<ValidationError> errorList = new ArrayList<>();
		if (!ArgumentHelper.isValid(caseAadharBean.getId() + "")) {
			errorList.add(ValidationError.builder().message("Please provide case Id.").build());
		}
		if (!ArgumentHelper.isValid(caseAadharBean.getAadharFirstName())) {
			errorList.add(ValidationError.builder().message("Please provide Aadhar First Name.").build());
		}

		if (!ArgumentHelper.isValid(caseAadharBean.getAadharLastName())) {
			errorList.add(ValidationError.builder().message("Please provide Aadhar Last Name.").build());
		}

		if (Objects.isNull(caseAadharBean.getDateOfBirth())) {
			errorList.add(ValidationError.builder().message("Please provide Aadhar Date of Birth.").build());
		}

		if (Objects.isNull(caseAadharBean.getAddress())) {
			errorList.add(ValidationError.builder().message("Please provide Aadhar Address Details.").build());
		} else {
			if (!ArgumentHelper.isValid(caseAadharBean.getAddress().getAddress())) {
				errorList.add(ValidationError.builder().message("Please provide Aadhar Address.").build());
			}
			if (!ArgumentHelper.isValid(caseAadharBean.getAddress().getCity())) {
				errorList.add(ValidationError.builder().message("Please provide Aadhar Address City.").build());
			}
			if (!ArgumentHelper.isValid(caseAadharBean.getAddress().getPinCode())) {
				errorList.add(ValidationError.builder().message("Please provide Aadhar Address Pin.").build());
			}

			if (!ArgumentHelper.isValid(caseAadharBean.getAddress().getState())) {
				errorList.add(ValidationError.builder().message("Please provide Aadhar Address State.").build());
			}

			if (!ArgumentHelper.isValid(caseAadharBean.getAddress().getCountry())) {
				errorList.add(ValidationError.builder().message("Please provide Aadhar Address Country.").build());
			}
		}

		if (!ArgumentHelper.isValid(caseAadharBean.getAadharNumber())) {
			errorList.add(ValidationError.builder().message("Please provide Aadhar Number.").build());
		}

		if (Objects.isNull(caseAadharBean.getAadharImage())) {
			errorList.add(ValidationError.builder().message("Please provide Aadhar Image Details.").build());
		} else {
			if (!ArgumentHelper.isValid(caseAadharBean.getAadharImage().getFileTitle())) {
				errorList.add(ValidationError.builder().message("Please provide Aadhar Image Title.").build());
			}

			if (!ArgumentHelper.isValid(caseAadharBean.getAadharImage().getFileContentType())) {
				errorList.add(
						ValidationError.builder().message("Please provide Aadhar Image File Content Type.").build());
			}

			if (!ArgumentHelper.isValid(caseAadharBean.getAadharImage().getFileContent())) {
				errorList.add(ValidationError.builder().message("Please provide Aadhar Image File Content.").build());
			}
		}
		if (ArgumentHelper.isNotEmpty(errorList)) {
			throw new ValidationException(errorList);
		}
	}

	public static void validateCustomerKycConfirmation(CustomerKYCConfirmationBean customerKycConfirmation) {
		List<ValidationError> errorList = new ArrayList<>();
		if (!ArgumentHelper.isValid(customerKycConfirmation.getCaseId() + "")) {
			errorList.add(ValidationError.builder().message("Please provide case Id.").build());
		}

		if (Objects.isNull(customerKycConfirmation.getAgreementBean())) {
			errorList.add(ValidationError.builder().message("Please provide agreement details.").build());
		} else {
			if (!ArgumentHelper.isValid(customerKycConfirmation.getAgreementBean().getFileTitle())) {
				errorList.add(ValidationError.builder().message("Please provide agreement detail Title.").build());
			}

			if (!ArgumentHelper.isValid(customerKycConfirmation.getAgreementBean().getFileContentType())) {
				errorList.add(
						ValidationError.builder().message("Please provideagreement detail File Content Type.").build());
			}

			if (!ArgumentHelper.isValid(customerKycConfirmation.getAgreementBean().getFileContent())) {
				errorList.add(ValidationError.builder().message("Please provide agreement detail content.").build());
			}
		}

		if (ArgumentHelper.isNotEmpty(errorList)) {
			throw new ValidationException(errorList);
		}

	}

	public static void validateContinueAndSaveCase(CaseKYCBean caseKYCBean) {
		List<ValidationError> errorList = new ArrayList<>();
		if (!ArgumentHelper.isValid(caseKYCBean.getId() + "")) {
			errorList.add(ValidationError.builder().message("Please provide case Id.").build());
		}

		if (Objects.isNull(caseKYCBean.getIsCommAddrSameAsAddharAddr())) {
			errorList.add(ValidationError.builder()
					.message("Please provide Flag to determine Communication Address Is Same as Aadhar Address.")
					.build());
		} else {
			if (!caseKYCBean.getIsCommAddrSameAsAddharAddr()) {
				if (!ArgumentHelper.isValid(caseKYCBean.getCommAddress().getAddress())) {
					errorList.add(ValidationError.builder().message("Please provide Communication Address.").build());
				}
				if (!ArgumentHelper.isValid(caseKYCBean.getCommAddress().getCity())) {
					errorList.add(
							ValidationError.builder().message("Please provide Communication Address City.").build());
				}
				if (!ArgumentHelper.isValid(caseKYCBean.getCommAddress().getPinCode())) {
					errorList.add(
							ValidationError.builder().message("Please provide Communication Address Pin.").build());
				}
				if (!ArgumentHelper.isValid(caseKYCBean.getCommAddress().getState())) {
					errorList.add(
							ValidationError.builder().message("Please provide Communication Address State.").build());
				}
//				if (!ArgumentHelper.isValid(caseKYCBean.getCommAddress().getCountry())) {
//					errorList.add(
//							ValidationError.builder().message("Please provide Communication Address Country.").build());
//				}
			}
		}

		if (ArgumentHelper.isNotEmpty(errorList)) {
			throw new ValidationException(errorList);
		}

	}

	public static void validateProcessApplication(ProcessApplicationBean processApplicationBean) {
		List<ValidationError> errorList = new ArrayList<>();
		if (!ArgumentHelper.isValid(processApplicationBean.getApplicationId() + "")) {
			errorList.add(ValidationError.builder().message("Please provide application Id.").build());
		}
		if (ArgumentHelper.isNotEmpty(errorList)) {
			throw new ValidationException(errorList);
		}
	}

	public static void validateGenerateOtp(CaseAadharGenerateOtpBean aadharGenerateOtpBean) {
		List<ValidationError> errorList = new ArrayList<>();
		if (!ArgumentHelper.isValid(aadharGenerateOtpBean.getCaseId() + "")) {
			errorList.add(ValidationError.builder().message("Please provide case Id.").build());
		}
		if (Objects.isNull(aadharGenerateOtpBean.getAadharFrontImage())) {
			errorList.add(ValidationError.builder().message("Please provide Aadhar Front Image Details.").build());
		} else {
			if (!ArgumentHelper.isValid(aadharGenerateOtpBean.getAadharFrontImage().getFileTitle())) {
				errorList.add(ValidationError.builder().message("Please provide Aadhar Front Image Title.").build());
			}

			if (!ArgumentHelper.isValid(aadharGenerateOtpBean.getAadharFrontImage().getFileContentType())) {
				errorList.add(
						ValidationError.builder().message("Please provide Aadhar Front Image File Content Type.").build());
			}

			if (!ArgumentHelper.isValid(aadharGenerateOtpBean.getAadharFrontImage().getFileContent())) {
				errorList.add(ValidationError.builder().message("Please provide Aadhar Front Image File Content.").build());
			}
		}
		if (ArgumentHelper.isNotEmpty(errorList)) {
			throw new ValidationException(errorList);
		}
		
	}

	public static void validateGenerateOtp(KycOtpRequestBean kycOtpRequestBean) {
		List<ValidationError> errorList = new ArrayList<>();
		if (!ArgumentHelper.isValid(kycOtpRequestBean.getCaseId() + "")) {
			errorList.add(ValidationError.builder().message("Please provide case Id.").build());
		}
		if (!ArgumentHelper.isValid(kycOtpRequestBean.getClientId() + "")) {
			errorList.add(ValidationError.builder().message("Please provide client Id.").build());
		}
		if (!ArgumentHelper.isValid(kycOtpRequestBean.getOtp() + "")) {
			errorList.add(ValidationError.builder().message("Please provide otp.").build());
		}
		if (ArgumentHelper.isNotEmpty(errorList)) {
			throw new ValidationException(errorList);
		}
		
	}

	public static void panVerify(CasePanVerifyBean casePanVerifyBean) {
		List<ValidationError> errorList = new ArrayList<>();
		if (!ArgumentHelper.isValid(casePanVerifyBean.getCaseId() + "")) {
			errorList.add(ValidationError.builder().message("Please provide case Id.").build());
		}
		if (Objects.isNull(casePanVerifyBean.getPanImage())) {
			errorList.add(ValidationError.builder().message("Please provide Pan Image Details.").build());
		} else {
			if (!ArgumentHelper.isValid(casePanVerifyBean.getPanImage().getFileTitle())) {
				errorList.add(ValidationError.builder().message("Please provide Pan Image Title.").build());
			}

			if (!ArgumentHelper.isValid(casePanVerifyBean.getPanImage().getFileContentType())) {
				errorList.add(
						ValidationError.builder().message("Please provide Pan Image File Content Type.").build());
			}

			if (!ArgumentHelper.isValid(casePanVerifyBean.getPanImage().getFileContent())) {
				errorList.add(ValidationError.builder().message("Please provide Pan Image File Content.").build());
			}
		}
		if (ArgumentHelper.isNotEmpty(errorList)) {
			throw new ValidationException(errorList);
		}
		
	}

	public static void validateUploadCaseDocument(UploadCaseDocument uploadCaseDocument) {
		List<ValidationError> errorList = new ArrayList<>();
		if (!ArgumentHelper.isValid(uploadCaseDocument.getId() + "")) {
			errorList.add(ValidationError.builder().message("Please provide case Id.").build());
		}
		if (!ArgumentHelper.isValid(uploadCaseDocument.getDocumentType())) {
			errorList.add(ValidationError.builder().message("Please provide case Document Type.").build());
		}
		if ("0".equals(uploadCaseDocument.getDocumentType())) {
			errorList.add(ValidationError.builder().message("Please provide case Document Type.").build());
		}
		if (Objects.isNull(uploadCaseDocument.getDocument())) {
			errorList.add(ValidationError.builder().message("Please provide Document Details.").build());
		} else {
			if (!ArgumentHelper.isValid(uploadCaseDocument.getDocument().getFileTitle())) {
				errorList.add(ValidationError.builder().message("Please provide Document File Title.").build());
			}

			if (!ArgumentHelper.isValid(uploadCaseDocument.getDocument().getFileContentType())) {
				errorList.add(ValidationError.builder().message("Please provide Document File Content Type.").build());
			}

			if (!ArgumentHelper.isValid(uploadCaseDocument.getDocument().getFileContent())) {
				errorList.add(ValidationError.builder().message("Please provide Document File Content.").build());
			}
		}

		if (ArgumentHelper.isNotEmpty(errorList)) {
			throw new ValidationException(errorList);
		}
		
	}
	
	public static void validateCaseExists(Long caseId, CaseModel model) {
		List<ValidationError> errorList = new ArrayList<>();
		if(Objects.isNull(model)) {
			errorList.add(ValidationError.builder().message(String.format("Provide case Id %d not exists. ", caseId)).build());
		}
		if (ArgumentHelper.isNotEmpty(errorList)) {
			throw new ValidationException(errorList);
		}
	}

	public static void validatedeleteCaseDocument(DeleteCaseDocumentBean deleteCaseDocumentBean) {
		List<ValidationError> errorList = new ArrayList<>();
		if (!ArgumentHelper.isValid(deleteCaseDocumentBean.getCaseId() + "")) {
			errorList.add(ValidationError.builder().message("Please provide case Id.").build());
		}
		if (!ArgumentHelper.isValid(deleteCaseDocumentBean.getDocumentId() + "")) {
			errorList.add(ValidationError.builder().message("Please provide case document id.").build());
		}
		if (ArgumentHelper.isNotEmpty(errorList)) {
			throw new ValidationException(errorList);
		}
		
	}

	public static void validateMadatoryOrSupplementaryDocument(CaseDocumentUploadBean caseDocumentUploadBean) {
		List<ValidationError> errorList = new ArrayList<>();
		if (!ArgumentHelper.isValid(caseDocumentUploadBean.getId() + "")) {
			errorList.add(ValidationError.builder().message("Please provide case Id.").build());
		}

		if (!ArgumentHelper.isValid(caseDocumentUploadBean.getDocumentType() + "")) {
			errorList.add(ValidationError.builder().message("Please provide case document type.").build());
		}
		
		if (Objects.isNull(caseDocumentUploadBean.getDocument())) {
			errorList.add(ValidationError.builder().message("Please provide Document Details.").build());
		} else {
			if (!ArgumentHelper.isValid(caseDocumentUploadBean.getDocument().getFileTitle())) {
				errorList.add(ValidationError.builder().message("Please provide Document File Title.").build());
			}

			if (!ArgumentHelper.isValid(caseDocumentUploadBean.getDocument().getFileContentType())) {
				errorList.add(ValidationError.builder().message("Please provide Document File Content Type.").build());
			}

			if (!ArgumentHelper.isValid(caseDocumentUploadBean.getDocument().getFileContent())) {
				errorList.add(ValidationError.builder().message("Please provide Document File Content.").build());
			}
		}

		if (ArgumentHelper.isNotEmpty(errorList)) {
			throw new ValidationException(errorList);
		}
		
	}
	
	
	public static void validateCaseShareHolderDeathCertDtl(CaseDeathCertificateDtlBean caseDeathCertificateDtlBean) {
		List<ValidationError> errorList = new ArrayList<>();
		if (!ArgumentHelper.isValid(caseDeathCertificateDtlBean.getCaseId() + "")) {
			errorList.add(ValidationError.builder().message("Please provide case Id.").build());
		}
		if (!ArgumentHelper.isValid(caseDeathCertificateDtlBean.getDeceasedName())) {
			errorList.add(ValidationError.builder().message("Please provide Deceased Name.").build());
		}
		if (!ArgumentHelper.isValid(caseDeathCertificateDtlBean.getRelation())) {
			errorList.add(ValidationError.builder().message("Please provide relation with Share Holder.").build());
		}
		if (Objects.isNull(caseDeathCertificateDtlBean.getDeathCertDocumet())) {
			errorList.add(ValidationError.builder().message("Please provide Death certificate document Details.").build());
		}
		if (ArgumentHelper.isNotEmpty(errorList)) {
			throw new ValidationException(errorList);
		}
	}
}
