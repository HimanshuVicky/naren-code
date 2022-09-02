package com.assignsecurities.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import com.assignsecurities.bean.ApplicationUserBean;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CaseTemplateBean {
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
	
	
	private String eMail;

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
	
	
	
	//Not a DB property
	@Builder.Default
	boolean maskFolio = Boolean.FALSE;
	//Not a DB property
	@Builder.Default
	boolean isStatusAutoUpdate = Boolean.FALSE;
	
	private Boolean rtaLetter1PrviewOnly;
	private Boolean rtaLetter2PrviewOnly;
	private Boolean letterPrviewOnly;
	
	private List<String> signedDocList;
	
	private String templateName;
	
	private Boolean isFeeProcessed;
	private Boolean isWintessInfoReceived;
	private Boolean isSuretyInfoReceived;
	private Boolean isCustomerDocumentsRequiredVerified;
	private Boolean isGeneratedDocumentsRequiredVerified;
	private Boolean isSignedDocumentsVerified;
	private Boolean isUploadRTAResponseVerified;
	private Boolean iseAdharComplete;
	
	private RtaDataModel rtaDataModel;
	
	private List<CaseScriptModel> scripts;
	
	private List<CaseDeathCertificateDtlModel> caseDeathCertificateDtlModels;
	
	private Map<String, String> caseFieldsKeyValues;
	
	private List<WitnessModel> witnesses;
	private List<SuretyInfoModel> suretyInfos;
	
	private ApplicationUserBean applicationUserBean;
}
