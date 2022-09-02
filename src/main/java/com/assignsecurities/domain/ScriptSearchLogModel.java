package com.assignsecurities.domain;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ScriptSearchLogModel {
	private Long id;
	private String firstName;
	private String middleName;
	private String lastName;
	private LocalDateTime createdDate;
	private Long searchBy;
	
	private String searchByName;
	private String mobileNo;
	private String city;
	
	private Double totalScriptCost;
}
