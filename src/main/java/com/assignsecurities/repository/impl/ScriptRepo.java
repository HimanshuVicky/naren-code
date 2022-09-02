package com.assignsecurities.repository.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.assignsecurities.app.util.ArgumentHelper;
import com.assignsecurities.bean.UserLoginBean;
import com.assignsecurities.domain.AddToFavouriteModel;
import com.assignsecurities.domain.Pagination;
import com.assignsecurities.domain.ScriptModel;
import com.assignsecurities.domain.ScriptPriceUpdateModel;
import com.assignsecurities.domain.ScriptSearchModel;

import lombok.extern.slf4j.Slf4j;

@Repository
@Slf4j
public class ScriptRepo {
	@Autowired
	private NamedParameterJdbcTemplate template;

	public ScriptModel getScriptById(Long id) {
		try {
			String query = "SELECT * FROM script WHERE id=:id";
			MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource();
			sqlParameterSource.addValue("id", id);
			return template.queryForObject(query, sqlParameterSource, mapObject());
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	public List<ScriptModel> getScripts() {
		try {
			return template.query("select * from script", mapObject());
		} catch (EmptyResultDataAccessException e) {
			return Collections.emptyList();
		}
	}

	public Pagination<ScriptModel> getScripts(UserLoginBean userLoginBean, String companyName,
			Pagination<ScriptModel> pagination) {
		List<ScriptModel> imports;
		try {
			String query = "select distinct CompanyName from script where CompanyName like :companyName order by CompanyName limit "
					+ (pagination.getCurrPageNumber() - 1) + "," + pagination.getPageSize();
			MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource();
			sqlParameterSource.addValue("companyName", "%" + companyName + "%");
			imports = template.query(query, sqlParameterSource, mapObjectCompanyName());
		} catch (EmptyResultDataAccessException e) {
			imports = new ArrayList<>();
		}
		pagination.setList(imports);
		return pagination;
	}

	public List<ScriptModel> getScripts(List<Long> scriptIds) {
		try {
			MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource();
			sqlParameterSource.addValue("scriptIds", scriptIds);
			return template.query("select * from script s where s.id in(:scriptIds)", sqlParameterSource, mapObject());
		} catch (EmptyResultDataAccessException e) {
			return Collections.emptyList();
		}
	}

	public List<ScriptModel> getScripts(Long applicationId) {
		try {
			MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource();
			sqlParameterSource.addValue("applicationId", applicationId);
			return template.query(
					"select distinct s.*,apps.id applicationScriptId from script s left join applicationscript apps on s.id=apps.ScriptId\r\n"
							+ "where ApplicationId = :applicationId;",
					sqlParameterSource, mapObjectWithApplicationScriptId());
		} catch (EmptyResultDataAccessException e) {
			return Collections.emptyList();
		}
	}

	public ScriptModel getScriptBySecurityCode(String securityCode) {
		try {
			String query = "SELECT * FROM script WHERE securityCode=:securityCode";
			MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource();
			sqlParameterSource.addValue("securityCode", securityCode);
			return template.queryForObject(query, sqlParameterSource, new BeanPropertyRowMapper<>(ScriptModel.class));
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	public ScriptModel getScriptByFolioNumber(String folioNumber) {
		try {
			String query = "SELECT * FROM script WHERE folioNumber=:folioNumber";
			MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource();
			sqlParameterSource.addValue("folioNumber", folioNumber);
			return template.queryForObject(query, sqlParameterSource, new BeanPropertyRowMapper<>(ScriptModel.class));
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}
	
	public ScriptModel getScriptByApplicationScriptId(Long applicationScriptId) {
		try {
			String query = "SELECT s.* FROM script s inner join applicationscript aaps on s.id=aaps.ScriptId where aaps.id=:applicationScriptId";
			MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource();
			sqlParameterSource.addValue("applicationScriptId", applicationScriptId);
			return template.queryForObject(query, sqlParameterSource, new BeanPropertyRowMapper<>(ScriptModel.class));
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	public ScriptModel getFolioNumberOrDpIdClientId(String folioNumberOrDpIdClientId) {
		try {
			String query = "SELECT * FROM script WHERE folioNumber=:folioNumber or DpIDClientID=:dpIDClientID";
			MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource();
			sqlParameterSource.addValue("folioNumber", folioNumberOrDpIdClientId);
			sqlParameterSource.addValue("dpIDClientID", folioNumberOrDpIdClientId);
			return template.queryForObject(query, sqlParameterSource, new BeanPropertyRowMapper<>(ScriptModel.class));
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}
	
	public List<ScriptModel> getFolioNumberOrDpIdClientIdAndActualDateTransferIEPF(String folioNumberOrDpIdClientId, LocalDateTime actualDateTransferIEPF) {
		try {
			String query = "SELECT * FROM script WHERE (folioNumber=:folioNumber or DpIDClientID=:dpIDClientID) ";
			if(Objects.nonNull(actualDateTransferIEPF)) {
				query = query + "and ActualDateTransferIEPF>=:actualDateTransferIEPF and ActualDateTransferIEPF<=:actualDateTransferIEPF";
			}
			MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource();
			sqlParameterSource.addValue("folioNumber", folioNumberOrDpIdClientId);
			sqlParameterSource.addValue("dpIDClientID", folioNumberOrDpIdClientId);
			sqlParameterSource.addValue("actualDateTransferIEPF", actualDateTransferIEPF);
			return template.query(query, sqlParameterSource, new BeanPropertyRowMapper<>(ScriptModel.class));
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}
	
	public Boolean isFolioNumberOrDpIdClientIdAndActualDateTransferIEPF(String folioNumberOrDpIdClientId, LocalDateTime actualDateTransferIEPF) {
		
		try {
			String query = "SELECT Id FROM script WHERE (folioNumber=:folioNumber or DpIDClientID=:dpIDClientID) ";
			if(Objects.nonNull(actualDateTransferIEPF)) {
				query = query + " and ActualDateTransferIEPF>=:actualDateTransferIEPF and ActualDateTransferIEPF<=:actualDateTransferIEPF";
			}
			query = query + " limit 1";
			
			MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource();
			sqlParameterSource.addValue("folioNumber", folioNumberOrDpIdClientId);
			sqlParameterSource.addValue("dpIDClientID", folioNumberOrDpIdClientId);
			sqlParameterSource.addValue("actualDateTransferIEPF", actualDateTransferIEPF);
//			String fileCount = template.queryForObject(query, sqlParameterSource,
//					new BeanPropertyRowMapper<>(String.class));
			Long fileCount = template.queryForObject(query,sqlParameterSource, Long.class);
			if (Objects.nonNull(fileCount) && fileCount>1) {
				return Boolean.TRUE;
			}
			return Boolean.FALSE;
		} catch (EmptyResultDataAccessException e) {
			return Boolean.FALSE;
		}
		
//		try {
//			String query = "SELECT * FROM script WHERE (folioNumber=:folioNumber or DpIDClientID=:dpIDClientID) ";
//			if(Objects.nonNull(actualDateTransferIEPF)) {
//				query = query + " and ActualDateTransferIEPF>=:actualDateTransferIEPF and ActualDateTransferIEPF<=:actualDateTransferIEPF";
//			}
//			query = query + " limit 1";
//			MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource();
//			sqlParameterSource.addValue("folioNumber", folioNumberOrDpIdClientId);
//			sqlParameterSource.addValue("dpIDClientID", folioNumberOrDpIdClientId);
//			sqlParameterSource.addValue("actualDateTransferIEPF", actualDateTransferIEPF);
//			return template.query(query, sqlParameterSource, new BeanPropertyRowMapper<>(ScriptModel.class));
//		} catch (EmptyResultDataAccessException e) {
//			return null;
//		}
	}

	public List<ScriptModel>  getScriptByCompanyCode(String companyName) {
		try {
			String query = "SELECT  * FROM script WHERE CompanyName like :companyName ";
			MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource();
			sqlParameterSource.addValue("companyName", "%"+companyName+"%");
			return template.query(query, sqlParameterSource, new BeanPropertyRowMapper<>(ScriptModel.class));
		} catch (EmptyResultDataAccessException e) {
			return Collections.emptyList();
		}
	}

	public void addAll(List<ScriptModel> ScriptModels) {
		String query = "insert into `script` (`SecurityCode`,`IsinCode`,`CompanyName`,`InvestorFirstName`,`InvestorMiddleName`,`InvestorLastName`,`FatherName`,`Address`,`Country`,`State`,`City`,`Pincode`,`FolioNumber`,`DpIDClientID`, `NumberOfShare`,`NominalValue`,`MarketPrice`,`ActualDateTransferIEPF`) "
				+ "values(:securityCode,:companyName,:investorFirstName,:investorMiddleName,:investorLastName,:fatherName,:address,:country,:state,:city,:pincode,:folioNumber,:dpIDClientID,:numberOfShare,:nominalValue,:marketPrice,:actualDateTransferIEPF)";

		List<Map<String, Object>> batchValues = new ArrayList<>(ScriptModels.size());
		ScriptModels.stream().forEach(model -> {
			batchValues.add(new MapSqlParameterSource("securityCode", model.getSecurityCode())
					.addValue("isinCode", model.getIsinCode()).addValue("companyName", model.getCompanyName())
					.addValue("investorFirstName", model.getInvestorFirstName())
					.addValue("investorMiddleName", model.getInvestorMiddleName())
					.addValue("investorLastName", model.getInvestorLastName())
					.addValue("fatherName", model.getFatherName()).addValue("address", model.getAddress())
					.addValue("country", model.getCountry()).addValue("state", model.getState())
					.addValue("city", model.getCity()).addValue("pincode", model.getPincode())
					.addValue("folioNumber", model.getFolioNumber()).addValue("dpIDClientID", model.getDpIdClientId())
					.addValue("numberOfShare", model.getNumberOfShare())
					.addValue("nominalValue", model.getNominalValue()).addValue("marketPrice", model.getMarketPrice())
					.addValue("actualDateTransferIEPF", model.getActualDateTransferIEPF()).getValues());
		});
		template.batchUpdate(query, batchValues.toArray(new Map[ScriptModels.size()]));
	}

	public Long add(ScriptModel model) {
		KeyHolder keyHolderAddress = new GeneratedKeyHolder();
		String query = "insert into `script` (`SecurityCode`,`IsinCode`,`CompanyName`,`InvestorFirstName`,`InvestorMiddleName`,`InvestorLastName`,`FatherName`,`Address`,`Country`,`State`,`City`,`Pincode`,`FolioNumber`,`DpIDClientID`, `NumberOfShare`,`NominalValue`,`MarketPrice`,`ActualDateTransferIEPF`) "
				+ "values(:securityCode,:isinCode,:companyName,:investorFirstName,:investorMiddleName,:investorLastName,:fatherName,:address,:country,:state,:city,:pincode,:folioNumber,:dpIDClientID,:numberOfShare,:nominalValue,:marketPrice,:actualDateTransferIEPF)";
		MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource("securityCode", model.getSecurityCode())
				.addValue("isinCode", model.getIsinCode()).addValue("companyName", model.getCompanyName().trim())
				.addValue("investorFirstName", model.getInvestorFirstName())
				.addValue("investorMiddleName", model.getInvestorMiddleName())
				.addValue("investorLastName", model.getInvestorLastName()).addValue("fatherName", model.getFatherName())
				.addValue("address", model.getAddress()).addValue("country", model.getCountry())
				.addValue("state", model.getState()).addValue("city", model.getCity())
				.addValue("pincode", model.getPincode()).addValue("folioNumber", model.getFolioNumber())
				.addValue("dpIDClientID", model.getDpIdClientId()).addValue("numberOfShare", model.getNumberOfShare())
				.addValue("nominalValue", model.getNominalValue()).addValue("marketPrice", model.getMarketPrice())
				.addValue("actualDateTransferIEPF", model.getActualDateTransferIEPF());

		template.update(query, sqlParameterSource, keyHolderAddress);
		Long scriptId = keyHolderAddress.getKey().longValue();
//		log.info("scriptId===>" + scriptId);
		return scriptId;
	}

	public void update(ScriptModel model) {
		String query = "update `script` set `SecurityCode`=:securityCode,`IsinCode`=:isinCode,`CompanyName`=:companyName,`InvestorFirstName`=:investorFirstName,"
				+ "`InvestorMiddleName`=:investorMiddleName,`InvestorLastName`=:investorLastName,`FatherName`=:fatherName,"
				+ "`Address`=:address,`Country`=:country,`State`=:state,`City`=:city,`Pincode`=:pincode,`FolioNumber`=:folioNumber,`DpIDClientID`=:dpIDClientID,"
				+ " `NumberOfShare`=:numberOfShare,`NominalValue`=:nominalValue,`MarketPrice`=:marketPrice,`ActualDateTransferIEPF`=:actualDateTransferIEPF where Id=:id ";
		MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource("securityCode", model.getSecurityCode())
				.addValue("isinCode", model.getIsinCode()).addValue("companyName", model.getCompanyName())
				.addValue("investorFirstName", model.getInvestorFirstName())
				.addValue("investorMiddleName", model.getInvestorMiddleName())
				.addValue("investorLastName", model.getInvestorLastName()).addValue("fatherName", model.getFatherName())
				.addValue("address", model.getAddress()).addValue("country", model.getCountry())
				.addValue("state", model.getState()).addValue("city", model.getCity())
				.addValue("pincode", model.getPincode()).addValue("folioNumber", model.getFolioNumber())
				.addValue("dpIDClientID", model.getDpIdClientId()).addValue("numberOfShare", model.getNumberOfShare())
				.addValue("nominalValue", model.getNominalValue()).addValue("marketPrice", model.getMarketPrice())
				.addValue("actualDateTransferIEPF", model.getActualDateTransferIEPF()).addValue("id", model.getId());
		int updatCount = template.update(query, sqlParameterSource);
		System.out.println("updatCount===>" + updatCount);
	}

	public void updatePrice(ScriptModel model) {
		String query = "update `script` set  `MarketPrice`=:marketPrice  where Id=:id)";
		MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource()
				.addValue("marketPrice", model.getMarketPrice()).addValue("id", model.getId());
		template.update(query, sqlParameterSource);
	}

	public void updatePriceForBse(List<ScriptPriceUpdateModel> scripts) {
		String query = "update `script` set  `MarketPrice`=   ROUND((NumberOfShare *:unitPrice),2) where SecurityCode=:securityCode";

		List<Map<String, Object>> batchValues = new ArrayList<>(scripts.size());
		scripts.stream().forEach(model -> {
			batchValues.add(new MapSqlParameterSource("unitPrice", model.getUnitPrice())
					.addValue("securityCode", model.getSecurityCode()).getValues());
		});
		int[] counts = template.batchUpdate(query, batchValues.toArray(new Map[scripts.size()]));
		log.info("updatePriceForBse::::counts===>" + counts);
	}

	public void updatePriceForNse(List<ScriptPriceUpdateModel> scripts) {
		String query = "update `script` set  `MarketPrice`=  ROUND((NumberOfShare *:unitPrice),2) where IsinCode=:isinCode";

		List<Map<String, Object>> batchValues = new ArrayList<>(scripts.size());
		scripts.stream().forEach(model -> {
			batchValues.add(new MapSqlParameterSource("unitPrice", model.getUnitPrice())
					.addValue("isinCode", model.getIsinCode()).getValues());
		});
		int[] counts = template.batchUpdate(query, batchValues.toArray(new Map[scripts.size()]));
		log.info("updatePriceForNse::::counts===>" + counts);
	}

	public List<ScriptModel> searchScript(ScriptSearchModel model) {
		try {
			MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource();
			sqlParameterSource.addValue("firstName", "%" + model.getFirstName() + "%");
			sqlParameterSource.addValue("lastName", model.getLastName());
			String query = "select * from script s where s.InvestorFirstName like :firstName and s.InvestorLastName = :lastName";
			if (ArgumentHelper.isValid(model.getCompanyName())) {
				query = query + " and CompanyName =:companyName ";
				sqlParameterSource.addValue("companyName", model.getCompanyName());
			}
			query = query + " order by CompanyName";
			return template.query(query, sqlParameterSource, mapObject());
		} catch (EmptyResultDataAccessException e) {
			return Collections.emptyList();
		}
	}

	public List<ScriptModel> getMyFavouriteScript(UserLoginBean userLoginBean) {
		try {
			MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource();
			sqlParameterSource.addValue("userId", userLoginBean.getId());
			return template.query(
					"select s.* from script s inner join myfavourte m on s.Id =m.ScriptId where m.UserId =:userId",
					sqlParameterSource, mapObject());
		} catch (EmptyResultDataAccessException e) {
			return Collections.emptyList();
		}
	}

	@SuppressWarnings("unchecked")
	public void addToFavourite(AddToFavouriteModel model) {
		String query = "insert into `myfavourte` (`UserId`,`ScriptId`) " + "values(:userId,:scriptId)";
		List<Map<String, Object>> batchValues = new ArrayList<>(model.getScriptIds().size());
		model.getScriptIds().stream().forEach(scriptId -> {
			batchValues.add(
					new MapSqlParameterSource("scriptId", scriptId).addValue("userId", model.getUserId()).getValues());
		});
		template.batchUpdate(query, batchValues.toArray(new Map[model.getScriptIds().size()]));
	}

	public void removeFromFavourite(List<Long> scriptIds, UserLoginBean userLoginBean) {
		String query = "delete from `myfavourte` where UserId = :userId and ScriptId in (:scriptIds)";

		MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource().addValue("scriptIds", scriptIds)
				.addValue("userId", userLoginBean.getId());
		template.update(query, sqlParameterSource);
	}

	private RowMapper<ScriptModel> mapObject() {
		return (result, i) -> {
			return ScriptModel.builder().id(result.getLong("id")).securityCode(result.getString("securityCode"))
					.isinCode(result.getString("IsinCode")).companyName(result.getString("companyName"))
					.investorFirstName(result.getString("investorFirstName"))
					.investorMiddleName(result.getString("investorMiddleName"))
					.investorLastName(result.getString("investorLastName")).fatherName(result.getString("fatherName"))
					.address(result.getString("address")).country(result.getString("country"))
					.state(result.getString("state")).city(result.getString("city"))
					.pincode(result.getString("pincode")).folioNumber(result.getString("folioNumber"))
					.dpIdClientId(result.getString("dpIdClientId")).numberOfShare(result.getDouble("numberOfShare"))
					.nominalValue(result.getDouble("nominalValue")).marketPrice(result.getDouble("marketPrice"))
					.actualDateTransferIEPF(Objects.isNull(result.getDate("ActualDateTransferIEPF")) ? null
							: result.getTimestamp("ActualDateTransferIEPF").toLocalDateTime())
					.build();
		};
	}

	private RowMapper<ScriptModel> mapObjectWithApplicationScriptId() {
		return (result, i) -> {
			return ScriptModel.builder().id(result.getLong("id")).securityCode(result.getString("securityCode"))
					.isinCode(result.getString("IsinCode")).companyName(result.getString("companyName"))
					.investorFirstName(result.getString("investorFirstName"))
					.investorMiddleName(result.getString("investorMiddleName"))
					.investorLastName(result.getString("investorLastName")).fatherName(result.getString("fatherName"))
					.address(result.getString("address")).country(result.getString("country"))
					.state(result.getString("state")).city(result.getString("city"))
					.pincode(result.getString("pincode")).folioNumber(result.getString("folioNumber"))
					.dpIdClientId(result.getString("dpIdClientId")).numberOfShare(result.getDouble("numberOfShare"))
					.nominalValue(result.getDouble("nominalValue")).marketPrice(result.getDouble("marketPrice"))
					.actualDateTransferIEPF(Objects.isNull(result.getDate("ActualDateTransferIEPF")) ? null
							: result.getTimestamp("ActualDateTransferIEPF").toLocalDateTime())
					.applicationScriptId(result.getLong("applicationScriptId")).build();
		};
	}
	
	private RowMapper<ScriptModel> mapObjectCompanyName() {
		return (result, i) -> {
			return ScriptModel.builder().companyName(result.getString("companyName"))
					.build();
		};
	}
}
