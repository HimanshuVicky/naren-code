package com.assignsecurities.bean;

import java.io.Serializable;
import java.util.Locale;
import java.util.Objects;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonInclude(Include.NON_NULL)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserLoginBean implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -531550814542013207L;
	
	
	private Long id;
	
	@NotNull(message="User Name can not be null")
	@Size(max = 50,message="User Name can not more then 50 characters")
	private String mobileNo;
	
	private String pin;
	
	private Long applicationUserId;
	
	@JsonIgnore
	private Integer isActive;
	
	private String localCode;
	
	private ApplicationUserBean applicationUserBean;
		
	/**
	 * @return the localCode
	 */
	public String getLocalCode() {
		if(Objects.isNull(localCode)) {
			localCode="en_US";
		}
		return localCode;
	}
	/**
	 * @param localCode the localCode to set
	 */
	public void setLocalCode(String localCode) {
		this.localCode = localCode;
	}
	
	public Locale getLocale(){
		if(Objects.isNull(localCode)) {
			localCode="en_US";
		}
		return new Locale(localCode);
	}
	
	@JsonIgnore
	public String getDisplayName(){
		if(Objects.isNull(applicationUserBean)) {
			return "";
		}
		return applicationUserBean.getName();
	}
}
