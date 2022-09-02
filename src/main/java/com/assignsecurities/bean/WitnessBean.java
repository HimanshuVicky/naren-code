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
public class WitnessBean  implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6481993400062615601L;
	
	private Long id;
	private Long caseId;
	private String name;
	private String address;
	private String city;
	private String contactNumber;
	private String aadharNumber;
	private String panNumber;
	private Long adharDocumentId;
	private Long panDocumentId;

}
