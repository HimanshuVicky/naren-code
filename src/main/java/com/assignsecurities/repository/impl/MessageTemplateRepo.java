package com.assignsecurities.repository.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.assignsecurities.domain.MessageTemplate;


@Repository
public class MessageTemplateRepo {
	@Autowired
	private NamedParameterJdbcTemplate template;

	public Map<String, MessageTemplate> getAllMessageTemplates() {
		String query = "select * from message_template";
		List<MessageTemplate> messageTemplates = template.query(query, mapApplicationProperties());
		Map<String, MessageTemplate> messageTemplateMap = new HashMap<>();
		if (Objects.nonNull(messageTemplates)) {
			messageTemplates.stream().forEach(messageTemplate -> {
				messageTemplateMap.put(messageTemplate.getCode(), messageTemplate);
			});
		}
		return messageTemplateMap;
	}

	private RowMapper<MessageTemplate> mapApplicationProperties() {
		return (rs, i) -> {
			MessageTemplate messageTemplate = new MessageTemplate();
			messageTemplate.setId(rs.getLong("Id"));
			messageTemplate.setCode(rs.getString("Code"));
			messageTemplate.setName(rs.getString("Name"));
			messageTemplate.setType(rs.getString("TYPE"));
			messageTemplate.setMessage(rs.getString("Message"));
			return messageTemplate;
		};
	}
}
