package com.assignsecurities.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApplicationFeatureModel implements java.io.Serializable {
	
	private static final long serialVersionUID = 8027480988369341524L;
	private Long id; 
	private String name; 
	private String code;
	private Boolean isActive;
}
