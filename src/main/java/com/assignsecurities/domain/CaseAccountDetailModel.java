package com.assignsecurities.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CaseAccountDetailModel implements java.io.Serializable {


	/**
	 * 
	 */
	private static final long serialVersionUID = -4688612470950768000L;
	private Long id;
	private Long caseId;
	private String dematAccountNumber;
	private String iepfUserName;
	private String iepfPassword;
	private String rtaContact;
	private String rtaRefNo;
	 
}
