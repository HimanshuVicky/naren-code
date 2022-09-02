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
public class CaseDocumentBean  implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 336849488459564587L;
	private Long caseId;
	private String templateType;
	private String uploadType;
	
	private Long rtaLetter1Id;
	private Long rtaLetter2Id;
	
	private Long documentId;
	
	private Boolean rtaLetter1PrviewOnly;
	
	private Boolean rtaLetter2PrviewOnly;
	
	private Boolean letterPrviewOnly;
	
	private List<String> signedDocList;
}
