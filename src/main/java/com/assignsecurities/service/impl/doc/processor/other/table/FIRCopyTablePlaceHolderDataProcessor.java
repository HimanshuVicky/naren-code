package com.assignsecurities.service.impl.doc.processor.other.table;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.assignsecurities.app.util.ArgumentHelper;
import com.assignsecurities.bean.ColumnBean;
import com.assignsecurities.bean.RowBean;
import com.assignsecurities.domain.CaseScriptModel;
import com.assignsecurities.domain.CaseShareCertificateDetailsModel;
import com.assignsecurities.domain.CaseTemplateBean;
import com.assignsecurities.domain.CaseWarrantDetailsModel;
import com.assignsecurities.domain.ScriptModel;
import com.assignsecurities.service.impl.doc.processor.DocTablePlaceHolderDataProcessor;

public class FIRCopyTablePlaceHolderDataProcessor extends DocTablePlaceHolderDataProcessor {
	@Override
	public Map<String, List<RowBean>> preparePlaceHolderData(Object obj) {
		CaseTemplateBean caseTemplateBean = (CaseTemplateBean) obj;
		List<CaseScriptModel> caseScriptModels = caseTemplateBean.getScripts();

		String templateName = caseTemplateBean.getTemplateName();

		Map<String, List<RowBean>> tableData = new HashMap<>();
		// common table for both the letter
		prepareTableForNonPhysicalRtaLetter(caseScriptModels, tableData);
//		if(templateName.contains("Physical") || templateName.contains("Warrant") ) {
		if (templateName.contains("Physical")) {
			prepareTableForPhysicalRtaLetter(caseScriptModels, tableData);
		}
		return tableData;
	}
	
	private void prepareTableForPhysicalRtaLetter(List<CaseScriptModel> caseScriptModels,
			Map<String, List<RowBean>> tableData) {
		List<RowBean> rows = new ArrayList<>();
		List<ColumnBean> columnHeaders = new ArrayList<>();
		columnHeaders = new ArrayList<>();
		columnHeaders.add(ColumnBean.builder().colValue("Folio No.").hAlognment("Center").build());
		columnHeaders.add(ColumnBean.builder().colValue("Warrant No.").hAlognment("Center").build());
		columnHeaders.add(ColumnBean.builder().colValue("Year").hAlognment("Center").build());
		columnHeaders.add(ColumnBean.builder().colValue("Amount").hAlognment("Center").build());
		rows.add(RowBean.builder().columns(columnHeaders).build());
		caseScriptModels.forEach(caseScript -> {
			ScriptModel script = caseScript.getScriptModel();
			
			List<CaseWarrantDetailsModel> caseWarrantDetailsModels = caseScript.getCaseWarrantDetailsModels();
			if(ArgumentHelper.isEmpty(caseWarrantDetailsModels)) {
				List<ColumnBean>columns = new ArrayList<>();
				columns.add(ColumnBean.builder().colValue(script.getFolioNumberOrDpIdClientId()).hAlognment("Left").build());
				columns.add(ColumnBean.builder().colValue("Required from your end").hAlognment("Left").build());
				columns.add(ColumnBean.builder().colValue("Required from your end").hAlognment("Left").build());
				columns.add(ColumnBean.builder().colValue("Required from your end").hAlognment("Left").build());
			}else {
				caseWarrantDetailsModels.forEach(sc->{
					List<ColumnBean>columns = new ArrayList<>();
					columns.add(ColumnBean.builder().colValue(script.getFolioNumberOrDpIdClientId()).hAlognment("Left").build());
					columns.add(ColumnBean.builder().colValue(sc.getWarrantNo()).hAlognment("Left").build());
					columns.add(ColumnBean.builder().colValue(sc.getYear()+"").hAlognment("Left").build());
					columns.add(ColumnBean.builder().colValue(sc.getAmount()+"").hAlognment("Left").build());
					rows.add(RowBean.builder().columns(columns).build());
				});
			}
		});
		tableData.put("WarrantTable", rows);
	}

	private void prepareTableForNonPhysicalRtaLetter(List<CaseScriptModel> caseScriptModels,
			Map<String, List<RowBean>> tableData) {
		List<RowBean> rows = new ArrayList<>();
//		List<ColumnBean> columns = new ArrayList<>();
		List<ColumnBean> columnHeaders = new ArrayList<>();
		/*
		columnHeaders.add(ColumnBean.builder().colValue(caseTemplateBean.getCompanyName()).hAlognment("Center").build());
		columnHeaders.add(ColumnBean.builder().colValue(" ").hAlognment("Center").build());
		columnHeaders.add(ColumnBean.builder().colValue(" ").hAlognment("Center").build());
		columnHeaders.add(ColumnBean.builder().colValue(" ").hAlognment("Center").build());
		rows.add(RowBean.builder().columns(columnHeaders).fromCell(1).toCell(4).build());
		*/
//		columnHeaders = new ArrayList<>();
//		columnHeaders.add(ColumnBean.builder().colValue("Folio No.").hAlognment("Center").build());
//		columnHeaders.add(ColumnBean.builder().colValue("Distinctive Nos.").hAlognment("Center").build());
//		columnHeaders.add(ColumnBean.builder().colValue("Certificate No.").hAlognment("Center").build());
//		columnHeaders.add(ColumnBean.builder().colValue("No. Of Shares").hAlognment("Center").build());
//		rows.add(RowBean.builder().columns(columnHeaders).fromCell(2).toCell(2).build());
	
		OtherTablePlaceHolderDataProcessor.folioTableHeader(rows);
		
		columnHeaders = new ArrayList<>();
		columnHeaders.add(ColumnBean.builder().colValue(" ").build());
		columnHeaders.add(ColumnBean.builder().colValue("From-").hAlognment("Center").build());
		columnHeaders.add(ColumnBean.builder().colValue("To-").hAlognment("Center").build());
		columnHeaders.add(ColumnBean.builder().colValue(" ").build());
		columnHeaders.add(ColumnBean.builder().colValue(" ").build());
		rows.add(RowBean.builder().columns(columnHeaders).build());
		
//		List<RowBean> table2Rows = new ArrayList<>();
		caseScriptModels.forEach(caseScript -> {
		
			ScriptModel script = caseScript.getScriptModel();
		
			List<CaseShareCertificateDetailsModel> shareCertificateDetailsModels = caseScript.getShareCertificateDetailsModels();
			if(ArgumentHelper.isEmpty(shareCertificateDetailsModels)) {
				List<ColumnBean>columns = new ArrayList<>();
				columns.add(ColumnBean.builder().colValue(script.getFolioNumberOrDpIdClientId()).build());
				columns.add(ColumnBean.builder().colValue("Required from your end").hAlognment("Left").build());
				columns.add(ColumnBean.builder().colValue("Required from your end").hAlognment("Left").build());
				columns.add(ColumnBean.builder().colValue("Required from your end").hAlognment("Left").build());
				columns.add(ColumnBean.builder().colValue("Required from your end").hAlognment("Left").build());
				rows.add(RowBean.builder().columns(columns).build());
			}else {
				shareCertificateDetailsModels.forEach(sc->{
					List<ColumnBean>columns = new ArrayList<>();
					columns.add(ColumnBean.builder().colValue(script.getFolioNumberOrDpIdClientId()).build());
					String distinctiveNo =  sc.getDistinctiveNo();
					if(ArgumentHelper.isValid(distinctiveNo)) {
						String fromDistinctiveNo;
						String toDistinctiveNo="";
						if(distinctiveNo.contains("-")) {
							String[] distinctiveNos =distinctiveNo.split("-");	
							fromDistinctiveNo = distinctiveNos[0];
							toDistinctiveNo = distinctiveNos[1];
						}else {
							fromDistinctiveNo = distinctiveNo;
						}
						columns.add(ColumnBean.builder().colValue(fromDistinctiveNo).hAlognment("Left").build());
						columns.add(ColumnBean.builder().colValue(toDistinctiveNo).hAlognment("Left").build());
					}else {
						columns.add(ColumnBean.builder().colValue("Required from your end").hAlognment("Left").build());
						columns.add(ColumnBean.builder().colValue("Required from your end").hAlognment("Left").build());
					}
					if(ArgumentHelper.isValid(sc.getCertificateNo())) {
						columns.add(ColumnBean.builder().colValue(sc.getCertificateNo()).hAlognment("Left").build());
					}else {
						columns.add(ColumnBean.builder().colValue("Required from your end").hAlognment("Left").build());
					}
					columns.add(ColumnBean.builder().colValue(sc.getQuantity().intValue()+"").hAlognment("Left").build());
					rows.add(RowBean.builder().columns(columns).build());
				});
				
			}
			
		});
		tableData.put("FolioTable", rows);
	}

}
