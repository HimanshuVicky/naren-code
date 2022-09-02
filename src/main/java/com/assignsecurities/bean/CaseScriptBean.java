package com.assignsecurities.bean;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CaseScriptBean implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7358851174405595662L;
	private Long id;
	private Long caseId;
	private Long scriptId;
//	private String certNo;
//	private String distinctiveNos;
//	private Double faceValue;
	private String primaryCaseHolder;
	private Boolean isPrimaryCaseHolderDeceased;
	private String secondayCaseHolder;
	private Boolean isSecondayCaseHolderDeceased;
	
	private String primaryHolderGender;
	private String secondayHolderGender;
	private Integer primaryHolderAge;
	private Integer secondayHolderAge;
	private String primaryHolderFatherHusbandName;
	private String secondayHolderFatherHusbandName;


	private ScriptBean scriptBean;
	private List<QuestionerBean> questioners;
	private List<CaseShareCertificateDetailsBean> caseShareCertificateDetails;
	private List<CaseWarrantDetailsBean> caseWarrantDetails;
	
}
