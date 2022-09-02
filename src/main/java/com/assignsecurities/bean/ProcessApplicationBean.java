package com.assignsecurities.bean;

import java.io.Serializable;

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
public class ProcessApplicationBean implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -665498433052882280L;
	private Long applicationId;
}
