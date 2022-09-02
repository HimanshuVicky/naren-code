package com.assignsecurities.bean;

import java.time.LocalDateTime;
import java.util.List;

import com.assignsecurities.app.AppConstant;
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
public class ApplicationBean  implements java.io.Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4073085532670017550L;
	
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
	
	private Double totalValue;
	
	private List<ApplicationScriptBean> scripts;
	
	
	public static String STATUS_WAITING_FOR_VALIDATION = AppConstant.APPLICATION_STATUS_WAITING_FOR_VALIDATION;
}
