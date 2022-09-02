package com.assignsecurities.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GeneratedDocumentsRequiredModel implements java.io.Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -9045491894749414689L;

	private Long id;
	private Long caseId;
	private String documentName;
	private Long documentMasterId;
	private Long documentId;
	private String documentMasterType;
	/*
	 * CustomerRequireDocument table Doc Masterid column detials will use for setting selection on UI
	 */
	private Long customerGeneratedDocMasterId;
}
