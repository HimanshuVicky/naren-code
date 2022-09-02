package com.assignsecurities.bean;

import java.time.LocalDateTime;
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
public class ApplicationDetailsBean implements java.io.Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4085673120121227306L;
	private Long id;
	private String firstName;
	private String middleName;
	private String lastName;
	private String companyName;
	private String status;
	private Long userId;
	private String feeType;
	private Double feeValue;
	private LocalDateTime createdDate;
	private String remarks;
	private String phoneNumber;
	private AddressBean address;
	
	private Double totalValue;
	
	private String securityCode;
	private String  isinNumber;
	
	private List<ScriptBean> scripts;

	List<FeeBean> feeBeans;
}
