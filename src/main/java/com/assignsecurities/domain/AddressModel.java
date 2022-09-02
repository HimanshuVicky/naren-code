package com.assignsecurities.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddressModel implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8508712252001128636L;
	private Long Id;
	private String address;
	private String city;
	private String pinCode;
	private String state;
	private String stateCode;
	private String country;
}
