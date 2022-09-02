package com.assignsecurities.bean;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(Include.NON_NULL)
public class SearchESignBean implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6752887233176480595L;
	private Long id;
	private String clientName;
	private String contactNumber;
	private String feePaid;
	private Double feeAmount;
	private String status;
}
