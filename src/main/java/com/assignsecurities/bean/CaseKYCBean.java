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
public class CaseKYCBean implements java.io.Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5134617413817187854L;
	
	private Long id;
	
	private AddressBean commAddress;
	
	private Boolean isCommAddrSameAsAddharAddr;
	
	
//	private String remarks;

}
