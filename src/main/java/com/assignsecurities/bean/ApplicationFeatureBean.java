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
public class ApplicationFeatureBean implements java.io.Serializable {
	
	
	private static final long serialVersionUID = -3416174568831093414L;
	private Long id; 
	private String name; 
	private String code;
	private Boolean isActive;

}
