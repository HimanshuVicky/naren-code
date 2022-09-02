package com.assignsecurities.domain;
// Generated 20 Jun, 2020 7:07:07 PM by Hibernate Tools 4.3.1.Final

import java.util.Objects;

import com.assignsecurities.app.AppConstant;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Userlogin generated by hbm2java
 */
@Data
@Builder
@NoArgsConstructor
public class UserLogin implements java.io.Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5956523432193097904L;
	
	private Long id;
	private String mobileNo;
	private String pin;
	private Long applicationUserId;
	private Integer isActive;

	public UserLogin(Long id, String mobileNo, String pin, Long applicationUserId,Integer isActive) {
		super();
		this.id = id;
		this.mobileNo = mobileNo;
		this.pin = pin;
		this.applicationUserId = applicationUserId;
		this.isActive = isActive;
	}

	public boolean hasMobileNo() {
		return (Objects.nonNull(mobileNo) && !mobileNo.equalsIgnoreCase(AppConstant.SYS_USER));
	}
	
}
