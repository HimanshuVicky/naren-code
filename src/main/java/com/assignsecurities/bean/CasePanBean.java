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
public class CasePanBean implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3684684869804913025L;
	private Long id;
	private String panNumber;
	private FileBean panImage;
	private boolean panVerified; 

}
