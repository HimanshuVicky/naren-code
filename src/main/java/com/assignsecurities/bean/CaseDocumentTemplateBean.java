package com.assignsecurities.bean;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CaseDocumentTemplateBean  implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -932384000506596630L;
	private Long caseId;
	private List<TemplateBean> templates;

}
