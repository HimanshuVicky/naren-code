package com.assignsecurities.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CaseFieldsModel implements java.io.Serializable {


	/**
	 * 
	 */
	private static final long serialVersionUID = -6093225487070779317L;
	private Long id;
	private Long caseId;
	private String field;
	private String fieldValue;

	private String fieldPlaceHolderKay;
}
