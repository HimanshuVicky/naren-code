package com.assignsecurities.bean;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(Include.NON_NULL)
public class CaseGeneratedDocumentsRequiredBean implements java.io.Serializable {
	

	/**
	 * 
	 */
	private static final long serialVersionUID = -6571251468204795732L;

	private Long caseId;
	
	private List<GeneratedDocumentsRequiredBean> generatedDocuments;
}
