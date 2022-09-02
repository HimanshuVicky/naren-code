package com.assignsecurities.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RtaTemplateModel {
	private Long id;
	private String name;
	private String templateType;
	private String templateName;
	private Boolean isFeeRequired;

}
