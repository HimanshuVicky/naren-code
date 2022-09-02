package com.assignsecurities.bean;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SectionAccessFlages implements java.io.Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4877969417316154201L;
	
	private String caseSummaryApplicationSummary;
	private String caseSummaryFolioDetails;
	private String caseSummaryProcessingPaymentInfo;
	private String caseSummaryeAdhar; 
	private String caseSummaryAssignFranchise;
	private String caseSummaryAssignLawyer;
	private String caseSummaryGenerateLetter;
	private String caseSummaryUploadedSignDocument;
	private String caseSummaryUploadRTAResponse;
	private String caseDetailsApplicationSummary; 
	private String caseDetailsFolioDetails;
	private String caseDetailsAdditionalFeesDetails;
	private String caseDetailsAccountDetails;
	private String caseDetailsWitnessInfo;
	private String caseDetailsSuretyInfo ;
	private String requiredDocumentsCustomerDocuments;
	private String requiredDocumentsGeneratedDocuments;

}
