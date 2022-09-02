package com.assignsecurities.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CaseWarrantDetailsModel implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3312027480575989735L;
	private Long id;
	private Long caseScriptId;
	private String warrantNo;
	private String year;
	private Double amount;
	private Long scriptId;
	 
}
