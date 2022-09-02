package com.assignsecurities.bean;

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
public class CaseUpdateBean implements java.io.Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4073085532670017550L;

	private Long id;
//	private String referenceNumber;
//	private String firstName;
//	private String middleName;
//	private String lastName;
//	private LocalDate dateOfBirth;
//	private String companyName;
	private String status;
//	private Long userId;
//	
//	@JsonIgnore
//	private String feeType;
//	@JsonIgnore
//	private Double feeValue;
//	private LocalDateTime createdDate;
	private String remarks;
//	private Long addressId;
//	private Long commAddressId;
//	private Long applicationId;
//	private String aadharNumber;// Masking Required
//	private String panNumber;// Masking Required
//
//	private Long cancelChequeDoccumentId;
//	private Long aadharDocId;
//	private Long panDocId;
//	private Long aggrmentDcoumentId;
//	@JsonIgnore
//	private Long legalHireCertId;
//	private Long franchiseId;
//	private Long assignLawyerId;
//	private String digitalSignVerificationReference;
//	private Double processingFee;

//	private AddressBean address;
//	private AddressBean commAddress;
//	private String chequeNumber;
	
	private Boolean isFeeProcessed;
	private Boolean isWintessInfoReceived;
	private Boolean isSuretyInfoReceived;
	private Boolean isCustomerDocumentsRequiredVerified;
	private Boolean isGeneratedDocumentsRequiredVerified;
	private Boolean isSignedDocumentsVerified;
	private Boolean isUploadRTAResponseVerified;
	private Boolean iseAdharComplete;
	
	//Folio Details
	private Boolean isFolioScriptModified;
	private CaseScriptUpdateBean script;
	
	//Processing Payment Info
	//Only three Fee types copied from application
	private Boolean isPaymentDetailsModified;
	private CaseFeesDetails paymentDetails;
	
	//Additional Fees Details
	private Boolean isAdditionalFeesDetailsModified;
	private CaseFeesDetails additionalFeesDetails;
	//eAdhar
	
	
	//Assign Franchise
	private Boolean isAssignFranchiseModified;
	private CaseFranchiseBean franchise;
	
	//Assign Lawyer
	private Boolean isAssignLawyerModified;
	private CaseLawyerBean lawyer;
	
	private Boolean isAssignNotaryPartnerModified;
	private CaseNotaryBean notaryPartner;
	
	private Boolean isAssignCharteredAccountantPartnerModified;
	private CaseCharteredAccountantBean charteredAccountant;
	
	
	
	//Generate Letter
	private Boolean isRtaLetterModified;
	private CaseDocumentBean rtaLetter;
	
	//Upload Signed Documents
	//private UploadCaseDocument uploadSignedDocument;
	
	//Upload RTA Response
	//private UploadCaseDocument uploadRTAResponse;
	
	//Account Details
	private Boolean isAccountDetailModified;
	private CaseAccountDetailBean accountDetail;
	
	//Witness Info
	private Boolean isWintessModified;
	private CaseWintessBean wintess;
	
	//Surety Info
	private Boolean isSuretyInfoModified;
	private CaseSuretyInfoBean suretyInfo;
	
	//Add addDetails - Case Pending Foli oDetails
	private Boolean isAddDetailsModified;
	private CustomerCasePendingFolioDetails addDetails;
	
	//Customer Documents Required
	private Boolean isCustomerDocumentsModified;
	private CaseCustomerDocumentsRequiredBean customerDocuments;
	
	//Generated Documents
	private Boolean isGeneratedDocumentsModified;
	private CaseGeneratedDocumentsRequiredBean generatedDocuments;
}
