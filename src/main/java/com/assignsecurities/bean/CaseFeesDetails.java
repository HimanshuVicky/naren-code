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
public class CaseFeesDetails {
	private Long caseId;
//	private Double feeValueTotal;
//	private Double receivedFeeValueTotal;
	private List<CaseFeeBean> fees;
	
}
