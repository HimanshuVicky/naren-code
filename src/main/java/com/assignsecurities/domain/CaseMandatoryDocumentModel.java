package com.assignsecurities.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CaseMandatoryDocumentModel implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1304850727936618346L;
	private Long id;
	private Long caseId;
	private String doccumentName;
	private Long documentId;
}
