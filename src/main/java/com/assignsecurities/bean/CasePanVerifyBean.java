package com.assignsecurities.bean;

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
public class CasePanVerifyBean implements java.io.Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1613205501033832859L;
	private Long caseId;
	private FileBean panImage;

}
