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
public class CustomerKYCConfirmationBean implements java.io.Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 512765965657281691L;
	private Long caseId;
	
	private FileBean agreementBean;

}
