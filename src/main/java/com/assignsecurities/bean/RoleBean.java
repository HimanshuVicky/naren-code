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
public class RoleBean implements java.io.Serializable {
	
	
	private static final long serialVersionUID = -993790923162744346L;
	private Long id; 
	private String name; 
	private String code;
	private Boolean isActive;
	
	

}
