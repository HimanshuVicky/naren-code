package com.assignsecurities.bean;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Objects;

import com.assignsecurities.app.util.ArgumentHelper;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ScriptBean implements java.io.Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8553113725384307873L;
	@JsonIgnore
	private String dlAction;
	private Long dlId;
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
	@JsonIgnore
	private Date actualDateTransferDl;
	
	@SuppressWarnings("unused")
	private String folioNumberOrDpIdClientId;
	
	private Long applicationScriptId;
	
	@JsonIgnore
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
	
	@JsonIgnore
	public boolean isFolioNumberOrDpIdClientIdExists() {
		return Objects.nonNull(getFolioNumberOrDpIdClientId());
	}
	
	@JsonIgnore
	public boolean isFolioNumberExists() {
		return Objects.nonNull(folioNumber);
	}
	
	@JsonIgnore
	public boolean isDpIdClientIdExists() {
		return Objects.nonNull(dpIdClientId);
	}
}
