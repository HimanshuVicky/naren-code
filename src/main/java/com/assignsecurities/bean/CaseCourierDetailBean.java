package com.assignsecurities.bean;

import java.time.LocalDate;
import java.util.Date;

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
public class CaseCourierDetailBean implements java.io.Serializable {/**
	 * 
	 */
	private static final long serialVersionUID = -5278375694403638126L;
	private Long id;
	private Long caseId;
	private Long courierId;
	private String referncenumber;
	private LocalDate receiptDate;

}
