package com.assignsecurities.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoleModel implements java.io.Serializable {
	
		private static final long serialVersionUID = -1451090821383466811L;
		private Long id; 
		private String name; 
		private String code;
		private Boolean isActive;

}
