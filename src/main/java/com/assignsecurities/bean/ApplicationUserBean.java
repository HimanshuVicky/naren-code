package com.assignsecurities.bean;

import java.time.LocalDate;

import com.assignsecurities.app.util.ArgumentHelper;
import com.fasterxml.jackson.annotation.JsonIgnore;
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
public class ApplicationUserBean implements java.io.Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5116072859982291088L;
	private Long id; 
	private String firstName; 
	private String middleName;
	private String lastName;
	private String userType;
	private String emailId;
	private LocalDate dateOfBirth;
	private String alternateMobileNo;
	private Long addressId;
	private Boolean isActive;
	private Long franchiseId;
	private String mobile;
	private String madianSurname;
	private String defaultSurname;
	private String gender;
	
	private Long userId; 
	
	private Long referalFranchiseId;

	private ReferralsCommisionDtlBean referralsCommisionDtlBean;
    private Long referalUserId;
    private Long referalPartnerFranchiseId;
    
    private String requestToken;

	@JsonIgnore
	public String getName() {
		String investorName = firstName;
		if(ArgumentHelper.isValid(middleName)) {
			investorName = investorName +" " + middleName;
		}
		investorName = investorName +" " + lastName;
		return investorName;
	}
	
	private AddressBean address;
	//TODO franchise
	private FranchiseBean franchiseBean;
	private String nonCustomerPin;
//	private ReferralsCommisionDtlBean referralsCommisionDtlBean;

}
