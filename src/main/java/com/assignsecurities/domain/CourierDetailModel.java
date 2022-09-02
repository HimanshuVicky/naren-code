package com.assignsecurities.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CourierDetailModel implements java.io.Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1421295013618205712L;
	private Long id; 
	private String name; 
	private String city;
	private String url;
	
}
