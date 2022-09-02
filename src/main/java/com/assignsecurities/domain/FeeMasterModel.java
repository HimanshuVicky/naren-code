package com.assignsecurities.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FeeMasterModel {
	private Long id;
	private String feeFor;
	private String feeType;
	private Boolean isGSTApplicable;
	private Integer tempOrder;
}
