package com.assignsecurities.repository.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.assignsecurities.domain.AddressModel;

@Repository
public class AddressRepo {
	@Autowired
	private NamedParameterJdbcTemplate template;

	public AddressModel getAddressById(Long addressId) {
		String query = "select a.*,sm.State_Code from address a left join statemaster sm on a.State=sm.State_Union_Territory WHERE a.ID=:id";
		MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource();
		sqlParameterSource.addValue("id", addressId);
		try {
			return template.queryForObject(query, sqlParameterSource, mapAddress());
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	private RowMapper<AddressModel> mapAddress() {
		return (result, i) -> {
			return AddressModel.builder().Id(result.getLong("Id"))
					.address(result.getString("Address"))
					.city(result.getString("City"))
					.pinCode(result.getString("PinCode"))
					.state(result.getString("State"))
					.stateCode(result.getString("State_Code"))
					.country(result.getString("Country"))
					.build();
		};
	}

	public Long addAddress(AddressModel adress) {
		KeyHolder keyHolderAddress = new GeneratedKeyHolder();
		MapSqlParameterSource sqlParameterSourceaddress = new MapSqlParameterSource();
		sqlParameterSourceaddress.addValue("address", adress.getAddress());
		sqlParameterSourceaddress.addValue("city", adress.getCity());
		sqlParameterSourceaddress.addValue("pinCode", adress.getPinCode());
		sqlParameterSourceaddress.addValue("state", adress.getState());
		sqlParameterSourceaddress.addValue("country", adress.getCountry());
		
		String queryaddress = "INSERT INTO address (Address,City,PinCode,State,Country) "
				+ "VALUES(:address,:city,:pinCode,:state,:country)";
		template.update(queryaddress, sqlParameterSourceaddress, keyHolderAddress);
		Long addressId = keyHolderAddress.getKey().longValue();
		return addressId;
	}
	
	public void updateAddress(AddressModel adress) {
		MapSqlParameterSource sqlParameterSourceaddress = new MapSqlParameterSource();
		sqlParameterSourceaddress.addValue("address", adress.getAddress());
		sqlParameterSourceaddress.addValue("city", adress.getCity());
		sqlParameterSourceaddress.addValue("pinCode", adress.getPinCode());
		sqlParameterSourceaddress.addValue("state", adress.getState());
		sqlParameterSourceaddress.addValue("id", adress.getId());
		sqlParameterSourceaddress.addValue("country", adress.getCountry());
		String queryaddress = "Update address set Address=:address, City=:city, PinCode=:pinCode, State=:state, Country=:country where id=:id ";
		template.update(queryaddress, sqlParameterSourceaddress);
	}
}
