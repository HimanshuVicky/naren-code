package com.assignsecurities.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserRole implements java.io.Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8719732910993451386L;
	
	private Long id;
	private Long roleId;
	private Long userId;
	private Boolean isActive;

}
