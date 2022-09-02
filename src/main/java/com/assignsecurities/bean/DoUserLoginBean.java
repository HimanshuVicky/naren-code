package com.assignsecurities.bean;

import java.io.Serializable;

import com.assignsecurities.app.security.NoHtml;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;
import lombok.NoArgsConstructor;

@JsonInclude(Include.NON_NULL)
@Data
@NoArgsConstructor
public class DoUserLoginBean implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8467860813035274335L;
	
	@NoHtml
	private String mobileNo;
	
	@NoHtml
	private String pin;
	
	
}
