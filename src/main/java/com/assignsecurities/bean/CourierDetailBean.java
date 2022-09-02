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
public class CourierDetailBean implements java.io.Serializable {/**
	 * 
	 */
	private static final long serialVersionUID = 2448387372647499441L;
	private Long id; 
	private String name; 
	private String city;
	private String url;
	

}
