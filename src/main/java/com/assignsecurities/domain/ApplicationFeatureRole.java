package com.assignsecurities.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApplicationFeatureRole implements java.io.Serializable {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6823062297270881558L;
	private Long id;
	private Long roleId;
	private Long applicationfeatureId;
	private Boolean isActive;
	
}
