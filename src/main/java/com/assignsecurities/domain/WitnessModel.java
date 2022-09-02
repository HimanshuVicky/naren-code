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
public class WitnessModel  implements java.io.Serializable {

/**
	 * 
	 */
	private static final long serialVersionUID = -7893933057012389951L;
//	CaseId,FirstName,MiddleName,LastName,DocumentType,DocNumber,CreatedDate,CreateBy
	
	private Long id;
	private Long caseId;
	private String name;
	private String address;
	private String city;
	private String contactNumber;
	private String aadharNumber;
	private String panNumber;
	private Long adharDocumentId;
	private Long panDocumentId;
	private LocalDate createdDate;
	private Long createBy;
}
