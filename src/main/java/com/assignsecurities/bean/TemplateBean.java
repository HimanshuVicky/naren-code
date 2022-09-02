package com.assignsecurities.bean;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TemplateBean  implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4921872175699532602L;
	
	private String rtaName;
	private String templateType;
	private Boolean isFeeRequired;
	private Double feeValue;

}
