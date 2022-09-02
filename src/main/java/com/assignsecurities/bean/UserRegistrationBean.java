package com.assignsecurities.bean;

import java.io.Serializable;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;
import lombok.NoArgsConstructor;

@JsonInclude(Include.NON_NULL)
@Data
@NoArgsConstructor
public class UserRegistrationBean implements Serializable {/**
	 * 
	 */
	private static final long serialVersionUID = -4925794139663501022L;
	
	@NotNull(message="User First Name can not be null")
	@Size(max = 50,message="User First Name can not more then 50 characters")
	private String firstName;
	
	@NotNull(message="User Last Name can not be null")
	@Size(max = 50,message="User Last Name can not more then 50 characters")
	private String lastName;
	
	@NotNull(message="User City can not be null")
	@Size(max = 50,message="User city can not more then 50 characters")
	private String city;
	
	@NotNull(message="User Mobile Number can not be null")
	@Size(max = 50,message="User Mobile Number can not more then 50 characters")
	private String mobileNo;
	
	private String state;
	
	private String referralRefrance;
	

}
