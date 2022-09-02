package com.assignsecurities.bean;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;
import lombok.NoArgsConstructor;

@JsonInclude(Include.NON_NULL)
@Data
@NoArgsConstructor
public class ScriptSearchBean  implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4832023800643396949L;
	
	private String firstName;
	
	private String lastName;
	
	private String companyName;
}
