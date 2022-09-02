package com.assignsecurities.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MessageTemplate implements java.io.Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1338186321063236741L;
	private Long id;
	private String code;
	private String name;
	private String type;
	private String message;
}
