package com.assignsecurities.bean;

import java.time.LocalDateTime;

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
public class SearchUserBean implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8734229120712040413L;
	private Long id;
	private String clientName;
	private String contactNumber;
	private String applicationYes;
	private String caseYes;
	private String pin;
	private String franchiseName;
	private String city;
	private LocalDateTime loginDate;
	private String referralName;
}
