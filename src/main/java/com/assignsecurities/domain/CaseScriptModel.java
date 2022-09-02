package com.assignsecurities.domain;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CaseScriptModel implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2068954010657632297L;
	private Long id;
	private Long caseId;
	private Long scriptId;
//	private String certNo;
//	private String distinctiveNos;
	private Double faceValue;
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

	private ScriptModel scriptModel;

	// Added to Template Processing
	private List<CaseShareCertificateDetailsModel> shareCertificateDetailsModels;

	// Added to Template Processing
	private List<CaseWarrantDetailsModel> caseWarrantDetailsModels;

}
