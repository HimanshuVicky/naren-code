package com.assignsecurities.bean;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;
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
public class CaseAadharBean implements java.io.Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 280843840655339160L;
	
	private Long id;
	private String aadharFirstName;
	private String aadharMiddleName;
	private String aadharLastName;
	@JsonFormat(pattern = "yyyy-MM-dd")
	private LocalDate dateOfBirth;
	private String gender;
	

	private AddressBean address;

	private String aadharNumber;
	private FileBean aadharImage;
}
