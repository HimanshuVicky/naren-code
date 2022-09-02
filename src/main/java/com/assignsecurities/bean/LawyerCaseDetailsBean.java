package com.assignsecurities.bean;

import java.time.LocalDate;
import java.util.List;

import com.assignsecurities.bean.CaseCustomerDocumentsRequiredBean.CaseCustomerDocumentsRequiredBeanBuilder;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(Include.NON_NULL)
public class LawyerCaseDetailsBean implements java.io.Serializable {/**
	 * 
	 */
	private static final long serialVersionUID = -8876206996414427813L;
	
    private Long caseId;
    private List <LawyerCaseCommentsDtlBean> lawyerCaseCommentsDtlBean;
	private CaseCustomerDocumentsRequiredBean lawyerUploadedDocuments;
	
	
	/*
	 * case Bean properties
	 */
	
	private String referenceNumber;
	private String firstName;
	private String middleName;
	private String lastName;
	private LocalDate dateOfBirth;
	private String gender;
	private String companyName;
	private String status;
	private Long userId;
	private String mobileNumber;
	private String eMail;
	
//	@JsonIgnore
//	private String feeType;
//	@JsonIgnore
//	private Double feeValue;
////	private LocalDateTime createdDate;
//	private String remarks;
	private Long addressId;
	private Long commAddressId;
	private Long applicationId;
	private String aadharNumber;// Masking Required*
	private String panNumber;// Masking Required*

	private Long cancelChequeDoccumentId;
	private Long aadharDocId;
	private Long panDocId;
	private Long aggrmentDcoumentId;
	@JsonIgnore
//	private Long legalHireCertId;
//	private Long franchiseId;
	private Long assignLawyerId;
	
	private Long dhareHolderDeathCertificate1Id;
//	private String digitalSignVerificationReference;
//	private Double processingFee;

	private AddressBean address;
	private AddressBean commAddress;
//	private String chequeNumber;
//	private String bankName;
//	private String bankAddress;
//	private String accountNumber;
//	private String ifscCode;
//	private Boolean panVerified;
	
//	private Boolean isFeeProcessed;
//	private Boolean isWintessInfoReceived;
//	private Boolean isSuretyInfoReceived;
//	private Boolean isCustomerDocumentsRequiredVerified;
//	private Boolean isGeneratedDocumentsRequiredVerified;
//	private Boolean isSignedDocumentsVerified;
//	private Boolean isUploadRTAResponseVerified;
//	private Boolean iseAdharComplete;
//	private Boolean isShareholderDeathCertificationRequire;
	
//	private Boolean isAdmineAdharComplete;
	
	
//	private Double scriptTotal;
//	
//	private String securityCode;
//	private String  isinNumber;
//	private Double marketPrice;
//	private Double  nominalValue;
	
	private String  stage;
	
	
//	private SectionAccessFlages sectionAccessFlages;
//	
//	//Folio Details
//	private List<CaseScriptBean> scripts;
//	
//	//Processing Payment Info
//	//Only three Fee types copied from application
//	private CaseFeesDetails paymentDetails;
	
	//Additional Fees Details
	private CaseFeesDetails additionalFeesDetails;
	//eAdhar
	
//	//Case Commission 
//	private CaseCommissionBean commission;
//	
//	//Assign Franchise
//	private CaseFranchiseBean franchise;
//	
	//Assign Lawyer
	private CaseLawyerBean lawyer;
	
//	//Assing Notary Partner
//	private CaseNotaryBean notaryPartner;
//	
//	//Assign CA
//	
//	private CaseCharteredAccountantBean charteredAccountant;
//	
//	//Generate Letter
//	private CaseDocumentBean rtaLetter;
	
	//Upload Signed Documents
	private UploadCaseDocumentBean uploadSignedDocuments;
	
//	//Upload RTA Response
//	private UploadCaseDocumentBean uploadRtaResponseDocuments;
//	
//	//Account Details
//	private CaseAccountDetailBean accountDetail;
//	
//	//Witness Info
//	private CaseWintessBean wintess;
//	
//	//Surety Info
//	private CaseSuretyInfoBean suretyInfo;
	
	//Customer Documents Required
//	private CaseCustomerDocumentsRequiredBean customerDocuments; 
	
//	//Generated Documents
//	private CaseGeneratedDocumentsRequiredBean generatedDocuments;
//	
//	private Long assignNotaryId;
//	private Long charteredAccountantId;
	

}
