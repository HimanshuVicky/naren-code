package com.assignsecurities.bean;

import java.util.List;

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
public class CustomerCasePendingDetails  implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -221691912100235211L;
	private Long caseId;
	private FileBean hireCertificate;
	private List<WitnessBean> witnesses;
//	private List<CaseScriptBean> scripts;

}
