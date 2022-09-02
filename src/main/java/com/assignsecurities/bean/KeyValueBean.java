package com.assignsecurities.bean;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class KeyValueBean  implements java.io.Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5809002394394288185L;

	private String key;
	
	private String value;
}
