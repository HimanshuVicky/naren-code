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
public class GeneratedDocumentsRequiredBean implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8053605667670779030L;
	private Long id;
	private String documentName;
	private Long documentId;
	private Long documentMasterId;
	private Boolean isGeneratedDocumentRequire;
	private Boolean isGeneratedDocumentMandatoryForCustomer;
	private String documentMasterType;
	private Long customerRequireDocMasterId;
	private Boolean isGenerateButtonVisible;
}
