package com.assignsecurities.domain;

import java.time.LocalDate;

import com.assignsecurities.bean.FileBean;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CaseDeathCertificateDtlModel implements java.io.Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3303652166573293041L;
	
	private Long id;
	private Long caseId;
	private String DeceasedName;
	private String relation;
	private Long DocumentId;
	private FileBean deathCertDocumet;
	private LocalDate dateOfDeath;
}
