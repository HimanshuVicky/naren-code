package com.assignsecurities.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SupplementaryDocumentMasterModel {
	private Long id;
	private String doccumentName;
	private Integer tempOrder;
}
