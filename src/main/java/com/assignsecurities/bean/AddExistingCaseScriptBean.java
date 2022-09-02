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
public class AddExistingCaseScriptBean implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6036357628552493772L;
	private Long caseId;
	private Long scriptId;
//	private Double faceValue;
	private String primaryCaseHolder;// Initialy this is copied from Case;
	private String secondayCaseHolder;
	private Boolean isPrimaryCaseHolderDeceased;
	private Boolean isSecondayCaseHolderDeceased;
	
	private String primaryHolderGender;
	private String secondayHolderGender;
	private Integer primaryHolderAge;
	private Integer secondayHolderAge;
	private String primaryHolderFatherHusbandName;
	private String secondayHolderFatherHusbandName;
	
	private List<QuestionerBean> questioners;
	
}
