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
public class AddressBean implements java.io.Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3725841266184458328L;
	private Long Id;
	private String address;
	private String city;
	private String pinCode;
	private String state;
	private String stateCode;
	private String country;
}
