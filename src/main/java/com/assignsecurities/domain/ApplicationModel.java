package com.assignsecurities.domain;

import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ApplicationModel implements java.io.Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6159061725254076110L;
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
	
	//Not a DB Property
	private Double totalValue;
	
	private List<ApplicationScriptModel> scripts;
}
