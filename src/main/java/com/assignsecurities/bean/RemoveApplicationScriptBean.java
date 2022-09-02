package com.assignsecurities.bean;

import java.util.List;

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
public class RemoveApplicationScriptBean implements java.io.Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7994516174707182281L;
	private Long applicationId;
//	private String feeType;
	private String remarks;
//	private Double feeValue;
	private List<Long> applicationScriptIds;

}
