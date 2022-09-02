package com.assignsecurities.bean;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import com.assignsecurities.bean.CaseBean.CaseBeanBuilder;
import com.fasterxml.jackson.annotation.JsonIgnore;
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
public class LawyerCaseCommentsDtlBean implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -998174753301353468L;
	private Long id;
	private Long caseId;
	private String comment;
	private LocalDateTime createdDate;
	private String caseCommentDate;
	private String lawyerName;
	
	

}
