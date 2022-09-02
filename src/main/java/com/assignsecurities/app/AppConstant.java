package com.assignsecurities.app;

public class AppConstant {
	
	public static final String DD_MMM_YYYY = "dd-MMM-yyyy";
	public static final String DD_MMM_YYYY_HH_MM_SS = "dd-MMM-yyyy hh:mm:ss";
	
	public static String USER_CONTEXT = "loggedInUser";
	public static String USER_TOKEN = "userToken";

	public static String SYS_USER = "1234567890";
	
	public static String SYS_USER2 = "1234567890";

	public static Long SYS_USER_ID = 1L;

	public static final int ID_LENGTH_30 = 30;


	public static Integer OTP_DEFAULT_lENGTH = 4;

	public static final Long EMAIL_STATUS_PENDING = 1201L;
	public static final Long EMAIL_STATUS_PICKED = 1202L;
	public static final Long EMAIL_STATUS_SENT = 1203L;
	public static final Long EMAIL_STATUS_FAILED = 1204L;



	public static final String FILE_STORE_BASE = "MyAdmin";
	public static final String BRANCH_IMG_NAME_PREFIX = "branchImg";
	public static final String ORG_IMG_NAME_PREFIX = "orgImg";
	public static final String JAVA_SCRIPT_DATA_IMAGE_JPEG_BASE64 = "data:image/jpeg;base64,";
	public static final String FILE_TYPE_JPEG = "jpeg";
	public static final String FILE_STORE_APPLICATION = AppConstant.FILE_STORE_BASE+"/"+"Applicants";
	
	public static final String JAVA_SCRIPT_DATA_EXCEL_SHEET_BASE64 = "data:application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;base64";
	
	public static final String JAVA_SCRIPT_DATA_WORD_SHEET_BASE64 = "data:application/vnd.openxmlformats-officedocument.wordprocessingml.document;base64";

	public static final String JAVA_SCRIPT_DATA_PDF_BASE64 = "data:application/pdf;base64";

	
	public static final Integer LOGO_IMG_DEFAULT_SIZE = 660;

	
	public static final Integer DEFAULT_PAGE_SIZE = 5;
	
	
	public static final String USER_TYPE_END_USER = "User";
	public static final String USER_TYPE_ADMIN = "Admin";
	public static final String USER_TYPE_FRANCHISE = "Franchise";
	//	public static final String USER_TYPE_FRANCHISE_OWNER = "Franchise Owner";
	public static final String USER_TYPE_FRANCHISE_USER = "FranchiseUser";
	public static final String USER_TYPE_ADVOCATE = "Advocate";
	public static final String USER_TYPE_CC = "CC";
	
	public static enum FeeType {
	    Percent,
	    FixValue
	  }
	
	public static final String APPLICATION_STATUS_WAITING_FOR_VALIDATION = "Waiting Validation";
	public static final String APPLICATION_STATUS_READY_FOR_PROC = "Waiting Processing";
	public static final String APPLICATION_STATUS_PENDING_FEE_ALLOCATION = "Waiting Fee Assign";
	public static final String APPLICATION_STATUS_IN_PROGRESS = "In Progress";
	public static final String APPLICATION_STATUS_REJECT = "Rejected Applications";
	
	
	
	//TODO Move this to a Template
	public static final String APP_RESET_EMAIL_SUBJECT = "Enrol-Me Reset Password";
	public static final String APP_RESET_EMAIL_BODY= "Dear {0}, %nYour Pin Reset is successful. Your new PIN for login is {1}.PIN has also been sent to your registered mobile number.";
	public static final String APP_RESET_SMS_TEXT= "Dear {0}, %nYour Pin Reset is successful. Your new PIN for login is {1}.PIN has also been sent to your registered email. http:// www.enrol-me.com";
	
	public static final String APP_GENERATE_EMAIL_SUBJECT = "Enrol-Me Generate Password";
	public static final String APP_GENERATE_EMAIL_BODY= "Dear {0}, %nYour Pin generated successfully. Your new PIN for login is {1}.PIN has also been sent to your registered mobile number.";
	public static final String APP_GENERATE_SMS_TEXT= "Dear {0}, %nYour Pin Reset is successful. Your new PIN for login is {1}.PIN has also been sent to your registered email. http:// www.enrol-me.com";
	
	public static final String APP_FRANCHISE_EMAIL_SUBJECT = "User Registered";
	public static final String APP_FRANCHISE_EMAIL_BODY= "Dear {0},<BR/>" + 
			"<BR/>" + 
			"Your reference,  {1} is now registered with Findmymoney. His registered number is <User number>{2}.<BR/>" + 
			"<BR/>" + 
			"Findmymoney Team";

	
	public static final String  DEFAULT_DOC_TEMPLATE_PATH = "/opt/docTemplate";
	
	
	public static final String  CUSTOMER_AGREEMENT_TEMPLATE_NAME = "Client Agreement With Adhyata.docx";
	
	public static final String  FRANCHISE_REFERRAL_AGREEMENT_TEMPLATE_NAME = "Franchise Referral Agreement With Adhyata.docx";
	
	public static final String  REFERRAL_AGREEMENT_TEMPLATE_NAME = "Referral Agreement With Adhyata.docx";
	
	public static enum DocumentType {
	    Pan("PAN"),
	    Aadhar("Aadhar"),
	    CancelCheque("Cancelled Cheque"),
	    CustomerAgreement("Customer Agreement"),
	    RtaLetter1("RTA Letter 1"),
	    RtaLetter2("RTA Letter 2"),
	    ShareHolderDeathCertificate("Death Certificate 1"),
	    FIRCopy("FIR Copy"),
	    IndemnityBond("Indemnity Bond"),
	    NameChangeAffidavit("Name Change Affidavit"),
	    AddressChangeAffidavit("Address Change Affidavit"),
	    SpecimenSignatureAffidavit("Specimen Signature Affidavit"),
	    DuplicateShareCertitficateAffidavit("Duplicate Share Certitficate Affidavit"),
	    TransmissionOfSharesAffidavit("Transmission Of Shares Affidavit"),
	    PaperAdvertisment("Paper Advertisment"),
	    CourierReceipt("Courier Receipt"),
	    Other("Other"),
		ReferralAgreement("ReferralAgreement"),
		FranchiseReferralAgreement("FranchiseReferralAgreement");
	    
		DocumentType(String label) {
			this.label = label;
		}

		public final String label;
		
		public static DocumentType valueOfLabel(String label) {
		    for (DocumentType e : values()) {
		        if (e.label.equals(label)) {
		            return e;
		        }
		    }
		    return null;
		}
	  }
	
	public static enum CaseStatus {
		New("New"),
		WaitingSubmission("Waiting Submission"),
		WaitingProcessingFee("Waiting Processing Fee"),
		WaitingProcessingFeeConfirmation("Waiting Processing Fee Confirmation"),
		WaitingCustomerAadhar("Waiting Customer Aadhar"),
		WaitingAdminAadhar("Waiting Admin Aadhar"),
		WaitingFranchiseAssigment("Waiting Franchise Assigment"),
		WaitingRTALetter1Generation("Waiting RTA Letter 1 Generation"),
		WaitingSignedDocumentsUpload("Waiting Signed Documents Upload"),
		WaitingRTAResponseVeirifcation("Waiting RTA Response Veirifcation"),
		WaitingRTAResponse("Waiting RTA Response"),
		WaitingPostResponseUpdation("Waiting Post Response Updation"),
		WaitingRequiredDocumentList("Waiting Required Document List"),
		WaitingFeesSchedule("Waiting Fees Schedule"),
		WaitingWitnessInfo("Waiting Witness Info"),
		WaitingDocUpload("Waiting Doc Upload"),
		WaitingAdditionalFees("Waiting Additional Fees"),
		WaitingFeesConfirmation("Waiting Fees Confirmation"),
		WaitingDocGeneration("Waiting Doc Generation"),
		WaitingRTALetter2Generation("Waiting RTA Letter 2 Generation"),
		WaitingDocumentsUpload("Waiting Documents Upload"),
//		WaitingRTAResponse("Waiting RTA Response"),
		WaitingIEPFFormUpload("Waiting IEPF Form Upload"),
		WaitingFinalPayment("Waiting Final Payment"),
		WaitingClosure("Waiting Closure"),

//		PANVerified("Pan Verification Complete"),
//		AdharVerified("Adhar Verification Complete"),
//		CancelChequeUpload("Cheque Upload complete"),
//		eAdharDone("Adhar Agreement Done"),
//		PendingSubmission("Pending Submission"),
//		PendingCustomerResponse("Pending Customer Response"),
//		PendingAdminApproval("Approve Cases"),
//		PendingFrenchiseAssignment("Assign Frenchise"),
//		PendingRTALetterCreation("Create RTA Letter"),
//		PendingCaseUpload("Upload Case Document"),
//		PendingRTASubmission("Upload RTA Submit Details"),
//		PendingRTAResponse("Upload RTA Response"),
//		UploadedRTAResponse("RTA Response Received"),
//		PendingCaseResponse("Create Case Response"),
//		PendingLawyerInput("Awaiting Lawyer Response"),
		Closed("Closed");
		
		CaseStatus(String label) {
			this.label = label;
		}

		public final String label;
		
		public static CaseStatus valueOfLabel(String label) {
		    for (CaseStatus e : values()) {
		        if (e.label.equals(label)) {
		            return e;
		        }
		    }
		    return null;
		}
	}
	
	public static enum Gender {
		Male, FeMale
	}
	
	public static enum DefaultSurname {
		LastName, MadianName
	}
	
//	public static final List<String> endUserMyCaseStatuses = Arrays.asList(CaseStatus.New.label, CaseStatus.PANVerified.label
//			,CaseStatus.AdharVerified.label,CaseStatus.CancelChequeUpload.label,CaseStatus.eAdharDone.label,
//			CaseStatus.PendingSubmission.label);
	
	public static final String APP_PENDING_REMINDER_SMS_TEXT= "Dear {0}, %nYour Pin Reset is successful. Your new PIN for login is {1}.PIN has also been sent to your registered email. http:// www.enrol-me.com";

	
	public static final String APP_RTA_DOCUMENTS_UPLOADED_SMS= "The RTA response has been upload for case {0}. Please verify.\r\nADHYATA IMF PVT. LTD";

	public static final String APP_ASSIGNED_CASE_SMS= "Dear {0}\r\nYour case {1} has been assigned to you.\r\nAIPL";
	
	public static final String APP_CASE_CLOSED_SMS= "Dear {0}\r\nYour case {1} has been closed. \r\nAIPL";

	public static final String APP_ADMIN_APPLICATION_REJECTED_SMS = "Dear {0}\r\nYour application {1} has been rejected. Please login to check the details. Click {2}";
	
	public static final Integer FRANCHISE_ESIGN_AGREEMENT_STATUS_NEW = 0;
	public static final Integer FRANCHISE_ESIGN_AGREEMENT_STATUS_PENDING_FOR_ADMIN = 1;
	public static final Integer FRANCHISE_ESIGN_AGREEMENT_STATUS_COMPLETED = 2;
	
	public static final String USER_TYPE_REFERRAL_PARTNER = "Referral";
	
	public static final String REFERAL_FRANCHISE_USER_REGISTRATION_SMS = "Dear {0}\r\nYou have been created as a Referral Partner/Franchise for Findmymoney. Please login using your phone number and complete your agreement. Your PIN is :{1}\r\nAIPL";//"Dear {0}\r\nYour pin for login is {1}.";
	public static final String REFERAL_FRANCHISE_USER_ESIGNED_COMPLETED_SMS = "Dear {0}\r\nReferral Partner/Franchise has signed his agreement. Please sign the agreement.\r\nAIPL";//"Dear {0}\r\nYour pin for login is {1}.";
	public static final String ADMIN_PENDING_ESIGNED_COMPLETED_SMS = "Dear {0}\r\nYour Referral Partner/Franchise agreement for Findmymoney has been signed. System is now available to you. Your PIN is :{1}\r\nAIPL";//"Dear {0}\r\nYour pin for login is {1}.";
	
	public static final String USER_TYPE_NOTARYL_PARTNER = "Notary";
	public static final String USER_TYPE_CHARTERED_ACCOUNTANT_PARTNER = "CA";
	
	public static final String PARTNER_USER_REGISTRATION_SMS="Dear {0}\r\nYou have been created as a Partner for Findmymoney. Please login using your phone number and complete your agreement. Your PIN is :{1}\r\nADHYATA IMF PVT LTD";
	public static final String REFERAL_PARTNER_USER_REGISTRATION_SMS="Dear {0}\r\nYou have been created as a Referral Partner for Findmymoney. Please login using your phone number and complete your agreement. Your PIN is :{1}\r\nADHYATA IMF PVT LTD";
	public static final String FRANCHISE_PARTNER_USER_REGISTRATION_SMS="Dear {0}\r\nYou have been created as a Franchise for Findmymoney. Please login using your phone number and complete your agreement. Your PIN is :{1}\r\nADHYATA IMF PVT LTD";
	public static final String NOTARY_PARTNER_USER_REGISTRATION_SMS="Dear {0}\r\nYou have been created as a Notary Partner for Findmymoney. Please login using your phone number and complete your agreement. Your PIN is :{1}\r\nADHYATA IMF PVT LTD";
	public static final String CHARTERED_ACCOUNTANT_PARTNER_USER_REGISTRATION_SMS= "Dear {0}\r\nYou have been created as a Chartered Accountant for Findmymoney. Please login using your phone number and complete your agreement. Your PIN is :{1}\r\nADHYATA IMF PVT. LTD";
	public static final String FRANCHISE_USER_ESIGNED_COMPLETED_SMS ="Dear {0}\r\nFranchise has signed his agreement. Please sign the agreement.\r\nAIPL";
	public static final String REFERAL_PARTNER_USER_ESIGNED_COMPLETED_SMS ="Dear {0}\r\nReferral Partner has signed his agreement. Please sign the agreement.\r\nAIPL";
	public static final String PARTNER_USER_DTL_UPDATE_SMS="Dear {0}\r\nYou have been created as a Partner for Findmymoney. Please login using your phone number and complete your agreement. Your PIN is :{1}\r\nADHYATA IMF PVT LTD";
	
	
	public static final String E_SIGN_EMAIL_SUBJECT = "ESign agreement has been completed.";

	public static final String E_SINGN_EMAIL = "Dear {0},<BR/><BR/>The eSign agreement has been completed by Adhyata. Please find the signed agreement attached for your reference <BR/><BR/>Adhyata";
	public static final String USER_PROFILE_UPDATE ="Dear {0}\r\nYour Findmymoney account credential updated by admin user.\r\nPlease find your updated PIN {1} to access Findmymoney account.\r\n-ADHYATA IMF PVT LTD";
	public static final String LAWYER_CASE_DOCUMENT_UPLOAD ="Dear {0}\r\nLawyer has uploaded documents to the case# {1}.\r\n-ADHYATA IMF PVT LTD";
	public static final String LAWYER_CASE_COMMENT_ADD ="Dear {0}\r\nLawyer has added comments to the case# {1}.\r\n-ADHYATA IMF PVT LTD";
}
