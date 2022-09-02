package com.assignsecurities.domain;

import java.time.LocalDate;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CaseCourierDetailModel implements java.io.Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 407108368411078767L;
	private Long id;
	private Long caseId;
	private Long courierId;
	private String referncenumber;
	private LocalDate receiptDate;
}
