package com.assignsecurities.repository.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.assignsecurities.domain.ApplicationProperties;

@Repository
public class ApplicationPropertiesRepo {
	@Autowired
	private NamedParameterJdbcTemplate template;

	public Map<String, ApplicationProperties> getAllActiveProperties() {
		String query = "select * from property";
		List<ApplicationProperties> applicationProperties = template.query(query, mapApplicationProperties());
		Map<String, ApplicationProperties> applicationPropertiesMap = new HashMap<>();
		if (Objects.nonNull(applicationProperties)) {
			applicationProperties.stream().forEach(applicationPropert -> {
				applicationPropertiesMap.put(applicationPropert.getPropName(), applicationPropert);
			});
		}
		return applicationPropertiesMap;
	}
	
	public void updatePrice(ApplicationProperties model) {
		String query = "update `property` set  `CURRENT_VALUE`=:currentValue, DATE_MODIFIED=:dateModified, MODIFIED_BY=:modifiedBy  where PROP_NAME=:propName";
		MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource()
				.addValue("currentValue", model.getCurrentValue()).addValue("dateModified", model.getDateModified())
				.addValue("modifiedBy", model.getModifiedBy()).addValue("propName", model.getPropName());
		template.update(query, sqlParameterSource);
	}
	

	private RowMapper<ApplicationProperties> mapApplicationProperties() {
		return (rs, i) -> {
			ApplicationProperties applicationProperties = new ApplicationProperties();
			applicationProperties.setId(rs.getLong("Id"));
			applicationProperties.setPropName(rs.getString("PROP_NAME")); // private String propName;
			applicationProperties.setDescription(rs.getString("DESCRIPTION")); // private String description;
			applicationProperties.setCurrentValue(rs.getString("CURRENT_VALUE"));// private String currentValue;
			applicationProperties.setEditable(rs.getBoolean("EDITABLE"));// private Boolean editable;
			applicationProperties.setOrigValue(rs.getString("ORIG_VALUE"));// private String origValue;
			applicationProperties.setDateModified(rs.getTime("DATE_MODIFIED"));// private Date dateModified;
			applicationProperties.setModifiedBy(rs.getString("MODIFIED_BY"));// private String modifiedBy;
			return applicationProperties;
		};
	}
}
