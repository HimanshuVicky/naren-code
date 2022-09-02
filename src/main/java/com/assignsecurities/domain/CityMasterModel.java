package com.assignsecurities.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CityMasterModel {
	private Long id;
	private String city;
	private String urbanStatus;
	private String stateCode;
	private String stateUnionTerritory;
	private String districtCode;
	private String district;
}
