package com.assignsecurities.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ApplicationSupplementaryDocumentModel implements java.io.Serializable {


	/**
	 * 
	 */
	private static final long serialVersionUID = 8424793022505003309L;
	private Long id;
	private Long applicationId;
	private String doccumentName;
}
