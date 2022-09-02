package com.assignsecurities.bean;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PropertyBean {
	private Long id;
	private String propertyName;
	private String propertyValue;
}
