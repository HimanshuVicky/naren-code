package com.assignsecurities.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CaseModel implements java.io.Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4073085532670017550L;

	private Long id;
	private String referenceNumber;
	private String firstName;
	private String middleName;
	private String lastName;
	private LocalDate dateOfBirth;
	private String gender;
	private String companyName;
	private String status;
	private Long userId;
	private String feeType;
	private Double feeValue;
	private LocalDateTime createdDate;
	private String remarks;
	private Long addressId;
	private Long commAddressId;
	private Long applicationId;
	private String aadharNumber;// Masking Required
	private String panNumber;// Masking Required

	private Long cancelChequeDoccumentId;
	private Long aadharDocId;
	private Long panDocId;
	private Long aggrmentDcoumentId;
	private Long legalHireCertId;
	private Long franchiseId;
	private Long assignLawyerId;
	private String digitalSignVerificationReference;
	
	private Double processingFee;

	private AddressModel address;
	private AddressModel commAddress;
	private String chequeNumber;
	private String bankName;
	private String bankAddress;
	private String accountNumber;
	private String ifscCode;
	private Boolean panVerified;
	
	private List<CaseScriptModel> scripts;
	
	//Not a DB property
	@Builder.Default
	boolean maskFolio = Boolean.FALSE;
	//Not a DB property
	@Builder.Default
	boolean isStatusAutoUpdate = Boolean.FALSE;
	//Not a DB property
	List<CaseFeeModel> feeModels;
	//Not a DB property
	private Boolean rtaLetter1PrviewOnly;
	//Not a DB property
	private Boolean rtaLetter2PrviewOnly;
	
	//Not a DB property
	private List<String> signedDocList;
	
	private Boolean isFeeProcessed;
	private Boolean isWintessInfoReceived;
	private Boolean isSuretyInfoReceived;
	private Boolean isCustomerDocumentsRequiredVerified;
	private Boolean isGeneratedDocumentsRequiredVerified;
	private Boolean isSignedDocumentsVerified;
	private Boolean isUploadRTAResponseVerified;
	private Boolean iseAdharComplete;
	private Long assignNotaryId;
	private Long charteredAccountantId;

}
