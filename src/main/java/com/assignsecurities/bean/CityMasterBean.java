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
public class CityMasterBean implements java.io.Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2355809209446571084L;
	private Long id;
	private String city;
	private String stateUnionTerritory;
}
