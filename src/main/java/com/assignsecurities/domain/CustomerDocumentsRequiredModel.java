package com.assignsecurities.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CustomerDocumentsRequiredModel implements java.io.Serializable {


	/**
	 * 
	 */
	private static final long serialVersionUID = 2362454249611020172L;
	private Long id;
	private Long caseId;
	private String doccumentName;
	private Long documentMasterId;
	private Long documentId;
	private String documentMasterType;
	private Long customerRequireDocMasterId;
}
