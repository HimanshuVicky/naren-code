package com.assignsecurities.repository.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.assignsecurities.domain.NSEScriptDetailsModel;

@Repository
public class NSEScriptRepo {
	@Autowired
	private NamedParameterJdbcTemplate template;

	public List<NSEScriptDetailsModel> getScripts() {
		try {
			return template.query("select * from nsescriptdetails",
					(result, rowNum) -> NSEScriptDetailsModel.builder().id(result.getLong("id"))
							.symbol(result.getString("SYMBOL")).series(result.getString("SERIES"))
							.open(result.getDouble("OPEN")).high(result.getDouble("HIGH")).low(result.getDouble("LOW"))
							.close(result.getDouble("CLOSE")).last(result.getDouble("LAST"))
							.prevClose(result.getDouble("PREVCLOSE")).totTrdQty(result.getDouble("TOTTRDQTY"))
							.totTrdVal(result.getDouble("TOTTRDVAL"))
							.timeStamp(Objects.isNull(result.getDate("TIMESTAMP")) ? null
									: result.getTimestamp("TIMESTAMP").toLocalDateTime())
							.totalTrades(result.getDouble("TOTALTRADES")).isIn(result.getString("ISIN")).build());
		} catch (EmptyResultDataAccessException e) {
			return Collections.emptyList();
		}
	}

	public void addAll(List<NSEScriptDetailsModel> NSEScriptDetailsModels) {

		String deleteQuery = "delete from nsescriptdetails where id>0";
		MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource();
		template.update(deleteQuery, sqlParameterSource);
		String query = "insert into `nsescriptdetails` (`ID`,`SYMBOL`, `SERIES`, `OPEN`, `HIGH`, `LOW`, `CLOSE`, `LAST`, `PREVCLOSE`, `TOTTRDQTY`, `TOTTRDVAL`, `TIMESTAMP`, `TOTALTRADES`, `ISIN`) "
				+ "values(:id, :symbol, :series, :open, :high, :low, :close, :last, :prevClose, :totTrdQty, :totTrdVal, :timeStamp, :totalTrades, :isIn)";

		List<Map<String, Object>> batchValues = new ArrayList<>(NSEScriptDetailsModels.size());
		NSEScriptDetailsModels.stream().forEach(model -> {
			batchValues.add(new MapSqlParameterSource("id", model.getId())
					.addValue("symbol", model.getSymbol())
					.addValue("series", model.getSeries())
					.addValue("open", model.getOpen()).addValue("high", model.getHigh()).addValue("low", model.getLow())
					.addValue("close", model.getClose()).addValue("last", model.getLast())
					.addValue("prevClose", model.getPrevClose()).addValue("totTrdQty", model.getTotTrdQty())
					.addValue("totTrdVal", model.getTotTrdVal()).addValue("timeStamp", model.getTimeStamp())
					.addValue("totalTrades", model.getTotalTrades()).addValue("isIn", model.getIsIn()).getValues());
		});
		template.batchUpdate(query, batchValues.toArray(new Map[NSEScriptDetailsModels.size()]));
	}
}
