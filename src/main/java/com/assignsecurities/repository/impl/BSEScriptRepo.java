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

import com.assignsecurities.domain.BSEScriptDetailsModel;

@Repository
public class BSEScriptRepo {
	@Autowired
	private NamedParameterJdbcTemplate template;

	public List<BSEScriptDetailsModel> getScripts() {
		try {
			return template.query("select * from bsescriptdetails",
					(result, rowNum) -> BSEScriptDetailsModel.builder().id(result.getLong("id"))
							.scCode(result.getString("SC_CODE")).scName(result.getString("SC_NAME"))
							.scGroup(result.getString("SC_GROUP")).scType(result.getString("SC_TYPE"))
							.open(result.getDouble("OPEN")).high(result.getDouble("HIGH")).low(result.getDouble("LOW"))
							.close(result.getDouble("CLOSE")).last(result.getDouble("LAST"))
							.prevClose(result.getDouble("PREVCLOSE")).noOfShrs(result.getDouble("NO_TRADES"))
							.noOfShrs(result.getDouble("NO_OF_SHRS")).netTurnOv(result.getDouble("NET_TURNOV"))
							.tdcloindi(result.getString("TDCLOINDI")).isnCode(result.getString("ISIN_CODE"))
							.tradingDate(Objects.isNull(result.getDate("TRADING_DATE")) ? null
									: result.getTimestamp("TRADING_DATE").toLocalDateTime())
							.filler2(result.getString("FILLER2")).filler3(result.getString("FILLER3")).build());
		} catch (EmptyResultDataAccessException e) {
			return Collections.emptyList();
		}
	}

	public void addAll(List<BSEScriptDetailsModel> bseScriptDetailsModels) {

		String deleteQuery = "delete from bsescriptdetails where id>0";
		MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource();
		template.update(deleteQuery, sqlParameterSource);
		String query = "insert into `bsescriptdetails` (`ID`,`SC_CODE` , `SC_NAME` , `SC_GROUP` , `SC_TYPE` , `OPEN`, `HIGH`, `LOW`, `CLOSE`, `LAST`, `PREVCLOSE`, `NO_TRADES`, `NO_OF_SHRS`, `NET_TURNOV`, `TDCLOINDI`, `ISIN_CODE` , `TRADING_DATE`, `FILLER2`, `FILLER3`) "
				+ "values(:id, :scCode , :scName , :scGroup , :scType , :open, :high, :low, :close, :last, :prevClose, :noTrades, :noOfShrs, :netTurnOv, :tdcloindi, :isnCode , :tradingDate, :filler2, :filler3)";
		
		List<Map<String, Object>> batchValues = new ArrayList<>(bseScriptDetailsModels.size());
		bseScriptDetailsModels.stream().forEach(model -> {
			batchValues.add(new MapSqlParameterSource("id", model.getId())
					.addValue("scCode", model.getScCode())
					.addValue("scName", model.getScName())
					.addValue("scGroup", model.getScGroup())
					.addValue("scType", model.getScType())
					.addValue("open", model.getOpen())
					.addValue("high", model.getHigh())
					.addValue("low", model.getLow())
					.addValue("close", model.getClose())
					.addValue("last", model.getLast())
					.addValue("prevClose", model.getPrevClose())
					.addValue("noTrades", model.getNoTrades())
					.addValue("noOfShrs", model.getNoOfShrs())
					.addValue("netTurnOv", model.getNetTurnOv())
					.addValue("tdcloindi", model.getTdcloindi())
					.addValue("isnCode", model.getIsnCode())
					.addValue("tradingDate", model.getTradingDate())
					.addValue("filler2", model.getFiller2())
					.addValue("filler3", model.getFiller3())
					.getValues());
		});
		template.batchUpdate(query, batchValues.toArray(new Map[bseScriptDetailsModels.size()]));
	}
}
