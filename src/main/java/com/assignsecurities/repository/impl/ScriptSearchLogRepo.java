package com.assignsecurities.repository.impl;

import java.time.LocalDateTime;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.assignsecurities.domain.ScriptSearchLogModel;

import lombok.extern.slf4j.Slf4j;

@Repository
@Slf4j
public class ScriptSearchLogRepo {
	@Autowired
	private NamedParameterJdbcTemplate template;

	private static String BASE_SQL = "select sl.*,ul.MobileNo,CONCAT(au.`FirstName`, ' ', au.`LastName`) as SearchByName,a.City from scriptsearchlog sl "
			+ "inner join  userlogin ul on sl.SearchBy=ul.Id "
			+ "inner join applicationuser au on au.id=ul.ApplicationUserId "
			+ "inner join address a on a.id = au.AddressId";

	public ScriptSearchLogModel getScriptById(Long id) {
		try {
			String query = BASE_SQL + " WHERE id=:id";
			MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource();
			sqlParameterSource.addValue("id", id);
			return template.queryForObject(query, sqlParameterSource, mapObject());
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	public Long add(ScriptSearchLogModel model) {
		KeyHolder keyHolderAddress = new GeneratedKeyHolder();
		String query = "insert into `scriptsearchlog` ( `FirstName`,`MiddleName`,`LastName`,`CreatedDate`,`SearchBy`,`TotalScriptCost`) "
				+ "values(:firstName,:middleName,:lastName,:createdDate,:searchBy,:totalScriptCost)";
		MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource("firstName", model.getFirstName())
				.addValue("middleName", model.getMiddleName()).addValue("lastName", model.getLastName())
				.addValue("createdDate",
						Objects.isNull(model.getCreatedDate()) ? LocalDateTime.now() : model.getCreatedDate())
				.addValue("searchBy", model.getSearchBy()).addValue("totalScriptCost", model.getTotalScriptCost());

		template.update(query, sqlParameterSource, keyHolderAddress);
		Long scriptSearchLogId = keyHolderAddress.getKey().longValue();
		log.info("scriptSearchLogId===>" + scriptSearchLogId);
		return scriptSearchLogId;
	}

	private RowMapper<ScriptSearchLogModel> mapObject() {
		return (result, i) -> {
			return ScriptSearchLogModel.builder().id(result.getLong("id")).firstName(result.getString("FirstName"))
					.middleName(result.getString("MiddleName")).lastName(result.getString("LastName"))
					.createdDate(Objects.isNull(result.getDate("CreatedDate")) ? null
							: result.getTimestamp("CreatedDate").toLocalDateTime())
					.searchBy(result.getLong("SearchBy")).searchByName(result.getString("SearchByName"))
					.mobileNo(result.getString("MobileNo")).city(result.getString("City")).totalScriptCost(result.getDouble("TotalScriptCost")).build();
		};
	}
}
