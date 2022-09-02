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
public class CustomerDocumentsRequiredBean implements java.io.Serializable {


	/**
	 * 
	 */
	private static final long serialVersionUID = 9190676477263873178L;
	private Long id;
	private String doccumentName;
	private Long documentId;
	private Long documentMasterId;
	private Boolean isCustomerDocumentRequire;
	private Boolean isCustomerDocumentMandatory;
	private String documentMasterType;
	private Long customerRequireDocMasterId;
}
