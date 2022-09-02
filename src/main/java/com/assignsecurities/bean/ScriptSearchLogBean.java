package com.assignsecurities.bean;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

import com.assignsecurities.app.AppConstant;
import com.assignsecurities.app.util.ArgumentHelper;
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
public class ScriptSearchLogBean implements java.io.Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1895567436162782977L;
	private Long id;
	private String firstName;
	private String middleName;
	private String lastName;
	private LocalDateTime createdDate;
//	private Long searchBy;
	
	private String searchByName;
	private String mobileNo;
	private String city;
	private Double totalScriptCost;
	
	public String getName() {
		String investorName = firstName;
		if(ArgumentHelper.isValid(middleName)) {
			investorName = investorName +" " + middleName;
		}
		investorName = investorName +" " + lastName;
		return investorName;
	}
	
	public String getDateString() {
		if(Objects.nonNull(createdDate)) {
			return DateTimeFormatter.ofPattern(AppConstant.DD_MMM_YYYY_HH_MM_SS).format(createdDate);
//			return createdDate.toLocalDate().format(AppConstant).toString();
		}
		return null;
	}
}
