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
public class StateMasterBean implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3831106043611463224L;
	private Long id;
	private String stateUnionTerritory;
	private String stateCode;
}
