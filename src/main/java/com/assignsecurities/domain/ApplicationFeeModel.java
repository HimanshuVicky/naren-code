package com.assignsecurities.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ApplicationFeeModel implements java.io.Serializable {

//	 `Id` bigint(20) NOT NULL AUTO_INCREMENT,
//	  `ApplicationId` bigint(20) NOT NULL,
//	  `FeeFor` varchar(250)  NOT NULL,
//	  `FeeType` varchar(10)  NOT NULL DEFAULT 'FixValue',
//	  `FeeValue` double NOT NULL,
//	  `isGSTApplicable` tinyint(1) DEFAULT 0,

	/**
	 * 
	 */
	private static final long serialVersionUID = -4814850850607311826L;
	private Long id;
	private Long applicationId;
	private String feeFor;
	private String feeType;
	public Double feeValue;
	public Boolean isGSTApplicable;

}
