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
public class ApplicationFeeBean implements java.io.Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3476038081836414L;
	private Long applicationId;
	private List<FeeBean> fees;
}
