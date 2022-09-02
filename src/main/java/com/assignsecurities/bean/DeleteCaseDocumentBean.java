package com.assignsecurities.bean;

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
public class DeleteCaseDocumentBean implements java.io.Serializable {

	private static final long serialVersionUID = -8548118262991834262L;
	private Long caseId;
	private Long documentId;

}
