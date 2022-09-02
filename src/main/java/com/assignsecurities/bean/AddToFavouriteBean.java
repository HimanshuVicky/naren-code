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
public class AddToFavouriteBean implements java.io.Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5565191933284224360L;

	
	private Long userId;

	private List<Long> scriptIds;

}
