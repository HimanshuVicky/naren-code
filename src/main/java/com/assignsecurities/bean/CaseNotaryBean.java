package com.assignsecurities.bean;

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
public class CaseNotaryBean implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1641412103861298466L;
	private Long caseId;
	private Long notaryPartnerId;
	
	private UserLoginBean notaryPartner;
}
