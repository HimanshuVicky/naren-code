package com.assignsecurities.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import com.assignsecurities.domain.CaseModel.CaseModelBuilder;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LawyerCaseCommentsDtlModel  implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5253439443428202651L;
	private Long id;
	private Long caseId;
	private String comment;
	private LocalDateTime createdDate;
	private String caseCommentDate;
	private String lawyerName;
	
	  

}
