package com.assignsecurities.domain;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApplicationScriptModel  implements java.io.Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3869579883810979098L;
	private Long id;
	private Long applicationId;
	private Long scriptId;
	
	private List<ScriptModel> scripts;
}
