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
import com.assignsecurities.domain.ScriptModel;
import com.assignsecurities.service.impl.doc.processor.DocTablePlaceHolderDataProcessor;

public class OtherTablePlaceHolderDataProcessor extends DocTablePlaceHolderDataProcessor {
	@Override
	public Map<String, List<RowBean>> preparePlaceHolderData(Object obj) {
		CaseTemplateBean caseTemplateBean = (CaseTemplateBean) obj;
		List<CaseScriptModel> caseScriptModels = caseTemplateBean.getScripts();

		String templateName = caseTemplateBean.getTemplateName();

		Map<String, List<RowBean>> tableData = new HashMap<>();
		// common table for both the letter
		
		if(templateName.contains("PUBLIC NOTICE") || templateName.contains("Advt format")) {
			prepareTableForJointHolderFolio(caseScriptModels, tableData);
		}else {
			prepareTableForNonPhysicalRtaLetter(caseScriptModels, tableData);
		}
		return tableData;
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
		folioTableHeader(rows);
	
		
		columnHeaders = new ArrayList<>();
		columnHeaders.add(ColumnBean.builder().colValue(" ").build());
		columnHeaders.add(ColumnBean.builder().colValue(" ").build());
		columnHeaders.add(ColumnBean.builder().colValue("From-").hAlognment("Center").build());
		columnHeaders.add(ColumnBean.builder().colValue("To-").hAlognment("Center").build());
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
					if(ArgumentHelper.isValid(sc.getCertificateNo())) {
						columns.add(ColumnBean.builder().colValue(sc.getCertificateNo()).hAlognment("Left").build());
					}else {
						columns.add(ColumnBean.builder().colValue("Required from your end").hAlognment("Left").build());
					}
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
					
					columns.add(ColumnBean.builder().colValue(sc.getQuantity().intValue()+"").hAlognment("Left").build());
					rows.add(RowBean.builder().columns(columns).build());
				});
				
			}
			
		});
		tableData.put("FolioTable", rows);
//		tableData.put("TableFolio", rows);
	}

	public static void folioTableHeader(List<RowBean> rows) {
		List<ColumnBean> columnHeaders;
		columnHeaders = new ArrayList<>();
		columnHeaders.add(ColumnBean.builder().colValue("Folio No.").hAlognment("Center").colWidht(1000).build());
		columnHeaders.add(ColumnBean.builder().colValue("Certificate No.").hAlognment("Center").colWidht(2500).build());
		columnHeaders.add(ColumnBean.builder().colValue("Distinctive Nos.").hAlognment("Center").colWidht(1500).build());
		columnHeaders.add(ColumnBean.builder().colValue("No. Of Shares").hAlognment("Center").colWidht(4500).build());//Shares covered in each certificate
		rows.add(RowBean.builder().columns(columnHeaders).fromCell(3).toCell(2).lastColWidht(1000).build());
	}

	private void prepareTableForJointHolderFolio(List<CaseScriptModel> caseScriptModels,
			Map<String, List<RowBean>> tableData) {
		List<RowBean> rows = new ArrayList<>();
//		List<ColumnBean> columns = new ArrayList<>();
		List<ColumnBean> columnHeaders = new ArrayList<>();
		columnHeaders = new ArrayList<>();
		columnHeaders.add(ColumnBean.builder().colValue("Folio No.").hAlognment("Center").build());
		columnHeaders.add(ColumnBean.builder().colValue("Name of the share holder / Joint holder").hAlognment("Center").build());
		columnHeaders.add(ColumnBean.builder().colValue("Distinctive Nos.").hAlognment("Center").build());
		columnHeaders.add(ColumnBean.builder().colValue("Certificate No.").hAlognment("Center").build());
		rows.add(RowBean.builder().columns(columnHeaders).fromCell(3).toCell(2).build());
	
		
		columnHeaders = new ArrayList<>();
		columnHeaders.add(ColumnBean.builder().colValue(" ").build());
		columnHeaders.add(ColumnBean.builder().colValue(" ").build());
		columnHeaders.add(ColumnBean.builder().colValue("From-").hAlognment("Center").build());
		columnHeaders.add(ColumnBean.builder().colValue("To-").hAlognment("Center").build());
		columnHeaders.add(ColumnBean.builder().colValue(" ").build());
//		columnHeaders.add(ColumnBean.builder().colValue(" ").build());
		rows.add(RowBean.builder().columns(columnHeaders).build());
		
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
					String holderName = caseScript.getPrimaryCaseHolder();
					if(ArgumentHelper.isValid(caseScript.getSecondayCaseHolder())) {
						holderName = holderName +", " + caseScript.getSecondayCaseHolder();
					}
					columns.add(ColumnBean.builder().colValue(holderName).hAlognment("Left").build());
					
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
					rows.add(RowBean.builder().columns(columns).build());
				});
				
			}
			
		});
		tableData.put("JointHolderTable", rows);
	}
	
	private void prepareTableForJointHolderFaceVAlueFolio(List<CaseScriptModel> caseScriptModels,
			Map<String, List<RowBean>> tableData) {
		List<RowBean> rows = new ArrayList<>();
//		List<ColumnBean> columns = new ArrayList<>();
		List<ColumnBean> columnHeaders = new ArrayList<>();
		columnHeaders = new ArrayList<>();
		columnHeaders.add(ColumnBean.builder().colValue("Name of the share holder / Joint holder").hAlognment("Center").build());
		columnHeaders.add(ColumnBean.builder().colValue("Securities").hAlognment("Center").build());
		columnHeaders.add(ColumnBean.builder().colValue("Distinctive Nos.").hAlognment("Center").build());
		columnHeaders.add(ColumnBean.builder().colValue("Certificate No.").hAlognment("Center").build());
		rows.add(RowBean.builder().columns(columnHeaders).fromCell(3).toCell(2).build());
	
		
		columnHeaders = new ArrayList<>();
		columnHeaders.add(ColumnBean.builder().colValue(" ").build());
		columnHeaders.add(ColumnBean.builder().colValue(" ").build());
		columnHeaders.add(ColumnBean.builder().colValue("From-").hAlognment("Center").build());
		columnHeaders.add(ColumnBean.builder().colValue("To-").hAlognment("Center").build());
		columnHeaders.add(ColumnBean.builder().colValue(" ").build());
//		columnHeaders.add(ColumnBean.builder().colValue(" ").build());
		rows.add(RowBean.builder().columns(columnHeaders).build());
		
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
					String holderName = caseScript.getPrimaryCaseHolder();
					if(ArgumentHelper.isValid(caseScript.getSecondayCaseHolder())) {
						holderName = holderName +", " + caseScript.getSecondayCaseHolder();
					}
					columns.add(ColumnBean.builder().colValue(holderName).hAlognment("Left").build());
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
					rows.add(RowBean.builder().columns(columns).build());
				});
				
			}
			
		});
		tableData.put("JointHolderFaceValueTable", rows);
	}
}
