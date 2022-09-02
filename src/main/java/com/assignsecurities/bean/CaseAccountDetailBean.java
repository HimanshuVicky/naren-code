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
public class CaseAccountDetailBean implements java.io.Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 280843840655339160L;
	
	private Long id;
	private Long CaseId;
	private String dematAccountNumber;
	private String iEPFUserName;
	private String iEPFPassword;
	private String rtaContact;
	private String rtaRefNo;
}
