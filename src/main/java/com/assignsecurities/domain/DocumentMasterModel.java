package com.assignsecurities.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DocumentMasterModel {
	private Long id;
	private String particulars;
	private Integer tempOrder;
	private String type;
	private String uploadedOrGenerated;
	private String action;
	private String process;
	private String link;
}
