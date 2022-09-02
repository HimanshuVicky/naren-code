package com.assignsecurities.domain;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CaseLogModel implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7867786942761441166L;
	private Long id;
	private Long caseId;
	private Long documentId;
	private String action;
	private LocalDateTime createdDate;
	private String createBy;
	private String remarks;

}
