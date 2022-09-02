package com.assignsecurities.bean;

import java.util.List;

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
public class AddFolioToApplicationBean implements java.io.Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -9033397766216091262L;
	private Long applicationId;
	private List<Long> scriptIds;
}
