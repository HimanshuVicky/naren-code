package com.assignsecurities.bean;

import java.util.List;

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
public class ApplicationScriptBean  implements java.io.Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4953510105688311885L;
	private Long id;
	private Long applicationId;
	private Long scriptId;
	
	private List<ScriptBean> scripts;

}
