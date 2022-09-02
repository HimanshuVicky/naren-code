package com.assignsecurities.service.impl.doc.processor.customeragreement;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import com.assignsecurities.app.AppConstant;
import com.assignsecurities.app.util.ArgumentHelper;
import com.assignsecurities.app.util.StringUtil;
import com.assignsecurities.bean.ColumnBean;
import com.assignsecurities.bean.RowBean;
import com.assignsecurities.domain.CaseFeeModel;
import com.assignsecurities.domain.CaseModel;
import com.assignsecurities.domain.CaseScriptModel;
import com.assignsecurities.service.impl.doc.processor.DocTablePlaceHolderDataProcessor;

public class CustomerAgreemntDocTablePlaceHolderDataProcessor extends DocTablePlaceHolderDataProcessor {
	@Override
	public Map<String, List<RowBean>> preparePlaceHolderData(Object obj) {
		CaseModel caseModel = (CaseModel) obj;
		List<CaseScriptModel> caseScriptModels = caseModel.getScripts();
//		Name of the Company	
//		Number of Shares	
//		Folio No.	
//		Fee Details (Inclusive of all taxes)	% +GST
//		Dividend Amount	% + GST
//		Advance Payment	 
//		Termination Fees	50% of Fee Details
//		Security	DIS Slip 
//		Nature of Work	Transmission + Duplicate + IEPF

		Map<String, List<RowBean>> tableData = new HashMap<>();
		List<RowBean> feeTableRows = new ArrayList<>();
		List<RowBean> folioTableRows = new ArrayList<>();
		
		List<ColumnBean> feeTableColumns = new ArrayList<>();
		
//		Name of the Company	
		feeTableColumns.add(ColumnBean.builder().colValue("Name of the Company").hAlognment("Left").build());
		feeTableColumns.add(ColumnBean.builder().colValue(caseModel.getCompanyName()).hAlognment("Left").build());
		feeTableRows.add(RowBean.builder().columns(feeTableColumns).build());
		

//		Processing Fees	From Database (Inclusive of GST)
		
		feeTableColumns = new ArrayList<>();
		feeTableColumns.add(ColumnBean.builder().colValue("Processing Fees").hAlognment("Left").build());
		
		List<CaseFeeModel> feeModels = caseModel.getFeeModels();
		Optional<CaseFeeModel> processingFeeOpt = feeModels.stream().filter(fee->fee.getFeeFor().contains("Processing Fees") ).findFirst();
		String processingFeeStr;
		Double processingFee =0D;
		if(processingFeeOpt.isPresent()) {
			Double processingFeeReceived = processingFeeOpt.get().getReceivedFeeValue();
			if(ArgumentHelper.isPositive(processingFeeReceived)) {
				processingFee = processingFeeReceived;
			}else {
				processingFee = processingFeeOpt.get().getFeeValue();
			}
			processingFeeStr = processingFee  +" (Inclusive of GST)";
			
		}else {
			processingFeeStr = "------------ (Inclusive of GST)";
		}
		feeTableColumns.add(ColumnBean.builder().colValue(processingFeeStr).hAlognment("Left").build());
		
		feeTableRows.add(RowBean.builder().columns(feeTableColumns).build());
		
//		Processing Fees Ref No.	From Database (Non-Refundable)
		feeTableColumns = new ArrayList<>();
		feeTableColumns.add(ColumnBean.builder().colValue("Processing Fees Ref No.").hAlognment("Left").build());
		
		String processingFeeRefNoStr;
		if(processingFeeOpt.isPresent()) {
			String refNo =processingFeeOpt.get().getRefNo();
			if(Objects.isNull(refNo)) {
				processingFeeRefNoStr = "- (Non-Refundable)";
			}else {
				processingFeeRefNoStr = refNo +" (Non-Refundable)";
			}
		}else {
			processingFeeRefNoStr = "- (Non-Refundable)";
		}
		feeTableColumns.add(ColumnBean.builder().colValue(processingFeeRefNoStr).hAlognment("Left").build());
		
		feeTableRows.add(RowBean.builder().columns(feeTableColumns).build());
		
		

//		Fee Details (Inclusive of all taxes)	% +GST
		feeTableColumns = new ArrayList<>();
		feeTableColumns.add(ColumnBean.builder().colValue("Fee Details (Inclusive of all taxes)").hAlognment("Left").build());
		Optional<CaseFeeModel> agreementFeeOpt = feeModels.stream().filter(fee->fee.getFeeFor().contains("Agreement Fees") ).findFirst();
		String agreementFeeStr;
		Boolean isAgreementFeePercent = Boolean.FALSE;
		Double agreementFeeValue =0D;
		if(agreementFeeOpt.isPresent()) {
			CaseFeeModel agreementFee = agreementFeeOpt.get();
			if (agreementFee.getFeeType().equals(AppConstant.FeeType.Percent.name())) {
				agreementFeeStr = agreementFee.getFeeValue() + " % +GST";
				isAgreementFeePercent = Boolean.TRUE;
			} else {
//				columns.add(ColumnBean.builder().colValue(agreementFee.getFeeValue() + " +GST").build());
				agreementFeeStr = agreementFee.getFeeValue() + " +GST";
			}
			agreementFeeValue = agreementFee.getFeeValue();
		}else {
			agreementFeeStr = "------------ + GST";
		}
		feeTableColumns.add(ColumnBean.builder().colValue(agreementFeeStr).hAlognment("Left").build());
		
		feeTableRows.add(RowBean.builder().columns(feeTableColumns).build());

//		Dividend Amount	% + GST
		feeTableColumns = new ArrayList<>();
		feeTableColumns.add(ColumnBean.builder().colValue("Dividend Amount").hAlognment("Left").build());
		if(isAgreementFeePercent) {
			feeTableColumns.add(ColumnBean.builder().colValue("5% +GST").hAlognment("Left").build());
		}else {
			if(ArgumentHelper.isPositive(agreementFeeValue)) {
				Double dividendAmount= agreementFeeValue * 10/100;
				dividendAmount = StringUtil.roundDouble(dividendAmount, 2);
				feeTableColumns.add(ColumnBean.builder().colValue(dividendAmount +" +GST").hAlognment("Left").build());
			}else {
				feeTableColumns.add(ColumnBean.builder().colValue(agreementFeeValue+"% +GST").hAlognment("Left").build());
			}
			
		}
		
		feeTableRows.add(RowBean.builder().columns(feeTableColumns).build());

//		Advance Payment	 
		feeTableColumns = new ArrayList<>();
		feeTableColumns.add(ColumnBean.builder().colValue("Advance Payment").hAlognment("Left").build());
		Optional<CaseFeeModel> advanceFeeOpt = feeModels.stream().filter(fee->fee.getFeeFor().contains("Advance Fee") ).findFirst();
		if(advanceFeeOpt.isPresent()) {
			if(Objects.nonNull(advanceFeeOpt.get().getReceivedFeeValue()) && advanceFeeOpt.get().getReceivedFeeValue()>0) {
				feeTableColumns.add(ColumnBean.builder().colValue(advanceFeeOpt.get().getReceivedFeeValue()+"").hAlognment("Left").build());
			}else {
				feeTableColumns.add(ColumnBean.builder().colValue(advanceFeeOpt.get().getFeeValue()+"").hAlognment("Left").build());
			}
		}else {
			feeTableColumns.add(ColumnBean.builder().colValue("").build());
		}
		
		feeTableRows.add(RowBean.builder().columns(feeTableColumns).build());

//		Termination Fees	50% of Fee Details
		feeTableColumns = new ArrayList<>();
		feeTableColumns.add(ColumnBean.builder().colValue("Termination Fees").hAlognment("Left").build());
		feeTableColumns.add(ColumnBean.builder().colValue("50% of Fee Details").hAlognment("Left").build());
		feeTableRows.add(RowBean.builder().columns(feeTableColumns).build());
		

		caseScriptModels.forEach(script -> {
		
			List<ColumnBean> folioTableColumns = new ArrayList<>();
			
//			Folio No.	
			folioTableColumns = new ArrayList<>();
			folioTableColumns.add(ColumnBean.builder().colValue("Folio No").hAlognment("Left").build());
			if (caseModel.isMaskFolio()) {
				folioTableColumns.add(ColumnBean.builder()
						.colValue(
								StringUtil.maskCardNumber(script.getScriptModel().getFolioNumberOrDpIdClientId(), 1, 3)).hAlognment("Left")
						.build());
			} else {
				folioTableColumns.add(
						ColumnBean.builder().colValue(script.getScriptModel().getFolioNumberOrDpIdClientId()).hAlognment("Left").build());
			}
			folioTableRows.add(RowBean.builder().columns(folioTableColumns).build());

//			Number of Shares	
			folioTableColumns = new ArrayList<>();
			folioTableColumns.add(ColumnBean.builder().colValue("Number of Shares").hAlognment("Left").build());
			folioTableColumns.add(ColumnBean.builder().colValue(script.getScriptModel().getNumberOfShare().toString()).hAlognment("Left").build());
			folioTableRows.add(RowBean.builder().columns(folioTableColumns).build());


//			Security	DIS Slip 
			folioTableColumns = new ArrayList<>();
			folioTableColumns.add(ColumnBean.builder().colValue("Security").hAlognment("Left").build());
			folioTableColumns.add(ColumnBean.builder().colValue("DIS Slip").hAlognment("Left").build());
			folioTableRows.add(RowBean.builder().columns(folioTableColumns).build());

//			Nature of Work	Transmission + Duplicate + IEPF
			folioTableColumns = new ArrayList<>();
			folioTableColumns.add(ColumnBean.builder().colValue("Nature of Work").hAlognment("Left").build());
			folioTableColumns.add(ColumnBean.builder().colValue("Transmission + Duplicate + IEPF + Suspense").hAlognment("Left").build());
			folioTableRows.add(RowBean.builder().columns(folioTableColumns).build());

//			Nature of Work	Transmission + Duplicate + IEPF
			folioTableColumns = new ArrayList<>();
			folioTableColumns.add(ColumnBean.builder().colValue("").build());
			folioTableColumns.add(ColumnBean.builder().colValue("").build());
			folioTableRows.add(RowBean.builder().columns(folioTableColumns).build());
		});
//		tableData.put("${SharesTable}", rows);
		

		
		tableData.put("FeeTable", feeTableRows);
		tableData.put("SharesTable", folioTableRows);
		return tableData;
	}

}
