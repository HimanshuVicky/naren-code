package com.assignsecurities.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RtaDataModel {
	private Long id;
	private String companyName;
	private String registrarName;
	private String registrarAddress;
	private String address;
	private String state;
	private String city;
	private String contactNumber;
	private String email;
	private Double ddAmount;
	private String securityCode;
	private String isinNumber;
	private String securityId;
	private String mps;
}
