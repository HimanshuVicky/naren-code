package com.assignsecurities.bean;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

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
public class AddNewCaseScriptBean implements java.io.Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5398257290068970056L;
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
	
	
	private Long caseId;
//	private Double faceValue;
	private String primaryCaseHolder;// Initialy this is copied from Case;
	private String secondayCaseHolder;
	private Boolean isPrimaryCaseHolderDeceased;
	private Boolean isSecondayCaseHolderDeceased;
	
	private String primaryHolderGender;
	private String secondayHolderGender;
	private Integer primaryHolderAge;
	private Integer secondayHolderAge;
	private String primaryHolderFatherHusbandName;
	private String secondayHolderFatherHusbandName;
	
	@SuppressWarnings("unused")
	private String folioNumberOrDpIdClientId;
	
	private Long applicationScriptId;
	private List<QuestionerBean> questioners; 
	
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
