package com.assignsecurities.domain;

import java.time.LocalDateTime;
import java.util.Objects;

import com.assignsecurities.app.util.ArgumentHelper;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ScriptModel {
	private Long id;
	private String securityCode;
	private String isinCode;
	private String companyName;
	private String investorFirstName;
	private String investorMiddleName;
	private String investorLastName;
	private String fatherName;
	private String address;
	private String country;
	private String state;
	private String city;
	private String pincode;
	private String folioNumber;
	private String dpIdClientId;
	private Double numberOfShare;
	private Double nominalValue;
	private Double marketPrice;
	private LocalDateTime actualDateTransferIEPF;
	
	private Long applicationScriptId;
	
	public String getInvestorName() {
		String investorName = investorFirstName;
		if(ArgumentHelper.isValid(investorMiddleName)) {
			investorName = investorName +" " + investorMiddleName;
		}
		investorName = investorName +" " + investorLastName;
		return investorName;
	}
	
	public String getFolioNumberOrDpIdClientId() {
		if(Objects.nonNull(folioNumber)) {
			return folioNumber;
		}
		return dpIdClientId;
	}
	
	public boolean isFolioNumberOrDpIdClientIdExists() {
		return Objects.nonNull(getFolioNumberOrDpIdClientId());
	}
}
