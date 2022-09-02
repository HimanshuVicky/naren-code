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
public class AddToFavouriteModel {

	private Long userId;
	private List<Long> scriptIds;
}
