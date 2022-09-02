package com.assignsecurities.service.impl.doc.processor.test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.assignsecurities.bean.ColumnBean;
import com.assignsecurities.bean.RowBean;
import com.assignsecurities.service.impl.doc.processor.DocTablePlaceHolderDataProcessor;

public class TestDocTablePlaceHolderDataProcessor extends DocTablePlaceHolderDataProcessor {
	@Override
	public Map<String, List<RowBean>> preparePlaceHolderData(Object obj) {
		Map<String, List<RowBean>> tableData = new HashMap<>();
		List<RowBean> rows = new ArrayList<>();
		List<ColumnBean> columns = new ArrayList<>();

		columns.add(ColumnBean.builder().colValue("Share Certificate No").build());
		columns.add(
				ColumnBean.builder().colValue("Distinctive No\r\n" + "From                      To\r\n" + "").build());
		columns.add(ColumnBean.builder().colValue("No of Shares").build());
		rows.add(RowBean.builder().columns(columns).build());

		columns = new ArrayList<>();
		columns.add(ColumnBean.builder().colValue("Cert 1").build());
		columns.add(ColumnBean.builder().colValue("2-Jan-2020                      2-Jan-2021").build());
		columns.add(ColumnBean.builder().colValue("125").build());
		rows.add(RowBean.builder().columns(columns).build());

		columns = new ArrayList<>();
		columns.add(ColumnBean.builder().colValue("Cert 2").build());
		columns.add(ColumnBean.builder().colValue("12-Sep-2019                      11-Sep-2021").build());
		columns.add(ColumnBean.builder().colValue("125").build());
		rows.add(RowBean.builder().columns(columns).build());

		columns = new ArrayList<>();
		columns.add(ColumnBean.builder().colValue("Cert 3").build());
		columns.add(ColumnBean.builder().colValue("2-Mar-2018                      12-Jan-2020").build());
		columns.add(ColumnBean.builder().colValue("125").build());
		rows.add(RowBean.builder().columns(columns).build());

		tableData.put("${SharesTable}", rows);

		rows = new ArrayList<>();
		columns = new ArrayList<>();
		columns.add(ColumnBean.builder().colValue("Name").build());
		columns.add(ColumnBean.builder().colValue("Address").build());
		columns.add(ColumnBean.builder().colValue("City").build());
		columns.add(ColumnBean.builder().colValue("Pin").build());
		rows.add(RowBean.builder().columns(columns).build());

		columns = new ArrayList<>();
		columns.add(ColumnBean.builder().colValue("Niranjan Mantri").build());
		columns.add(ColumnBean.builder().colValue(
				"Sr. No. 78/60, Plot No. 1, Sai Vision, A, Flat N Kunal Icon Rd, Pimple Saudagar PUNE Maharashtra")
				.build());
		columns.add(ColumnBean.builder().colValue("Pune").build());
		columns.add(ColumnBean.builder().colValue("411027").build());
		rows.add(RowBean.builder().columns(columns).build());

		tableData.put("${SharesTable11}", rows);
		
		return tableData;
	}
}
