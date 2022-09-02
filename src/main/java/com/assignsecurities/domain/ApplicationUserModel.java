
package com.assignsecurities.domain;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApplicationUserModel implements java.io.Serializable {
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
	private String madianSurname;
	private String defaultSurname;
	private String gender;
	private String nonCustomerPin;
	
	private AddressModel address;
	
	private Long userId;
	//TODO franchise
	
	private Long referalFranchiseId;
	private Long referalUserId;
	private Long referalPartnerFranchiseId;
	
}

