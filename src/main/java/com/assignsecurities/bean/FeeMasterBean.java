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
public class FeeMasterBean implements java.io.Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1614178158005577586L;
	private Long id;
	private String feeFor;
	private String feeType;
	private Integer tempOrder;
}
