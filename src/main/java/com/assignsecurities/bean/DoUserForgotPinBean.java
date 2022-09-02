package com.assignsecurities.bean;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonInclude(Include.NON_NULL)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DoUserForgotPinBean implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8467860813035274335L;
	
	private String mobileNo;
	
	private Long userid;
	
	private String pin;

	private Boolean isRegistered;
	
}
