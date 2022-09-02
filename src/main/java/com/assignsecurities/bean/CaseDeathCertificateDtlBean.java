package com.assignsecurities.bean;

import java.time.LocalDate;

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
public class CaseDeathCertificateDtlBean implements java.io.Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -472506796032014489L;
	
	private Long id;
	private Long caseId;
	private String deceasedName;
	private String relation;
	private Long documentId;
	private FileBean deathCertDocumet;
	private LocalDate dateOfDeath;

}
