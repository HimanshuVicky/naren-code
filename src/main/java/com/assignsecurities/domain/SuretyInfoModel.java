package com.assignsecurities.domain;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SuretyInfoModel  implements java.io.Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4906208008915924555L;
	private Long id;
	private Long caseId;
	private String name;
	private String address;
	private String city;
	private String phone;
	private String aadharNumber;
	private String panNumber;
	private String itrRefNo;
	private Long adharDocumentId;
	private Long panDocumentId;
	private Long itrDocumentId;
	private LocalDate createdDate;
	private Long createBy;
}
