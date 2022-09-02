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
public class CaseCancelChequeBean  implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4713365044122761843L;
	private Long id;
	private String chequeNumber;
	private String bankName;
	private String bankAddress;
	private String accountNumber;
	private String ifscCode;
	private FileBean cancelChequeImage;

}
