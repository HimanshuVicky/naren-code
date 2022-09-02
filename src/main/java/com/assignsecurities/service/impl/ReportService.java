package com.assignsecurities.service.impl;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.assignsecurities.app.AppConstant;
import com.assignsecurities.app.util.ArgumentHelper;
import com.assignsecurities.app.util.DateUtil;
import com.assignsecurities.app.util.StringUtil;
import com.assignsecurities.bean.CaseReportBean;
import com.assignsecurities.bean.MyActionReferralBean;
import com.assignsecurities.bean.ReportBean;
import com.assignsecurities.bean.ScriptSearchLogBean;
import com.assignsecurities.bean.SearchReferralBean;
import com.assignsecurities.bean.SearchUserBean;
import com.assignsecurities.bean.UserLoginBean;
import com.assignsecurities.converter.CaseReportConverter;
import com.assignsecurities.converter.MyActionReferralConverter;
import com.assignsecurities.converter.ScriptSearchLogConverter;
import com.assignsecurities.converter.SearchConvertor;
import com.assignsecurities.converter.SearchReferaalConverter;
import com.assignsecurities.converter.SearchUserConverter;
import com.assignsecurities.dm.PropertyKeys;
import com.assignsecurities.domain.ReportModel;
import com.assignsecurities.domain.SearchReferralModel;
import com.assignsecurities.domain.SearchUserModel;
import com.assignsecurities.repository.impl.ReportRepo;

import lombok.extern.slf4j.Slf4j;

@Service
@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
@Slf4j
public class ReportService {

	@Autowired
	private ReportRepo reportRepo;

	public List<ScriptSearchLogBean> getScriptLogsReport(ReportBean searchBean, UserLoginBean userLoginBean) {
		ReportModel model = SearchConvertor.convert(searchBean);
		String duration = searchBean.getDuration();
		LocalDate fromDate = null;
		LocalDate toDate = null;
		LocalDate currDate = LocalDate.now();
		if (ArgumentHelper.isValid(duration) && !duration.equalsIgnoreCase("All")) {
			if (duration.equalsIgnoreCase("Weekly")) {
				fromDate = DateUtil.getWeekStartDate(currDate);
				toDate = DateUtil.getWeekEndDate(currDate);
			} else if (duration.equalsIgnoreCase("Monthly")) {
				fromDate = DateUtil.getMonthStartDate(currDate);
				toDate = DateUtil.getMonthEndDate(currDate);
			} else if (duration.equalsIgnoreCase("Yearly")) {
				fromDate = DateUtil.getYearStartDate(currDate);
				toDate = DateUtil.getYearEndDate(currDate);
			}
		}
		model.setFromDate(fromDate);
		model.setToDate(toDate);
		return ScriptSearchLogConverter.convert(reportRepo.getScriptLogsReport(model, userLoginBean));
	}

	public List<CaseReportBean> getCaseReport(ReportBean searchBean, UserLoginBean userLoginBean) {
		ReportModel model = SearchConvertor.convert(searchBean);
		String duration = searchBean.getDuration();
		LocalDate fromDate = null;
		LocalDate toDate = null;
		LocalDate currDate = LocalDate.now();
		if (ArgumentHelper.isValid(duration) && !duration.equalsIgnoreCase("All")) {
			if (duration.equalsIgnoreCase("Daily") || duration.equalsIgnoreCase("Today")) {
				fromDate = currDate;
				toDate = currDate.plusDays(1);
			} else if (duration.equalsIgnoreCase("Weekly")) {
				fromDate = DateUtil.getWeekStartDate(currDate);
				toDate = DateUtil.getWeekEndDate(currDate);
			} else if (duration.equalsIgnoreCase("Monthly")) {
				fromDate = DateUtil.getMonthStartDate(currDate);
				toDate = DateUtil.getMonthEndDate(currDate);
			} else if (duration.equalsIgnoreCase("Yearly")) {
				fromDate = DateUtil.getYearStartDate(currDate);
				toDate = DateUtil.getYearEndDate(currDate);
			}
		}
		model.setFromDate(fromDate);
		model.setToDate(toDate);
		List<CaseReportBean> caseReportBeans = CaseReportConverter.convert(reportRepo.getCaseReport(model, userLoginBean));
		caseReportBeans.forEach(cr->{
			if(AppConstant.FeeType.Percent.name().equals(cr.getFeeType())) {
				Double agreementValue = cr.getValue()*cr.getAgreementValue()/100;
				agreementValue = StringUtil.roundDouble(agreementValue, 2);
				cr.setAgreementValue(agreementValue);
			}
		});
		return caseReportBeans;
	}
	
	public List<SearchUserBean> getUserReport(ReportBean searchBean, UserLoginBean userLoginBean) {
		ReportModel model = SearchConvertor.convert(searchBean);
		String duration = searchBean.getDuration();
		LocalDate fromDate = null;
		LocalDate toDate = null;
		LocalDate currDate = LocalDate.now();
		if (ArgumentHelper.isValid(duration) && !duration.equalsIgnoreCase("All")) {
			if (duration.equalsIgnoreCase("Daily") || duration.equalsIgnoreCase("Today")) {
				fromDate = currDate;
				toDate = currDate.plusDays(1);
			} else if (duration.equalsIgnoreCase("Weekly")) {
				fromDate = DateUtil.getWeekStartDate(currDate);
				toDate = DateUtil.getWeekEndDate(currDate);
			} else if (duration.equalsIgnoreCase("Monthly")) {
				fromDate = DateUtil.getMonthStartDate(currDate);
				toDate = DateUtil.getMonthEndDate(currDate);
			} else if (duration.equalsIgnoreCase("Yearly")) {
				fromDate = DateUtil.getYearStartDate(currDate);
				toDate = DateUtil.getYearEndDate(currDate);
			}
		}
		model.setFromDate(fromDate);
		model.setToDate(toDate);
		List<SearchUserModel> searchUserModels= reportRepo.getUserReport(model, userLoginBean);
		for(SearchUserModel searchUserModel : searchUserModels) {
			if (!searchUserModel.getUserType().equals(AppConstant.USER_TYPE_END_USER)) {
				searchUserModel.setPin(null);
			}
		}
		
		return SearchUserConverter.convert(searchUserModels);
	}
	
	public List<SearchReferralBean> getReferralReport(ReportBean searchBean, UserLoginBean userLoginBean) {
		ReportModel model = SearchConvertor.convert(searchBean);
		String duration = searchBean.getDuration();
		LocalDate fromDate = null;
		LocalDate toDate = null;
		LocalDate currDate = LocalDate.now();
		if (ArgumentHelper.isValid(duration) && !duration.equalsIgnoreCase("All")) {
			if (duration.equalsIgnoreCase("Daily") || duration.equalsIgnoreCase("Today")) {
				fromDate = currDate;
				toDate = currDate.plusDays(1);
			} else if (duration.equalsIgnoreCase("Weekly")) {
				fromDate = DateUtil.getWeekStartDate(currDate);
				toDate = DateUtil.getWeekEndDate(currDate);
			} else if (duration.equalsIgnoreCase("Monthly")) {
				fromDate = DateUtil.getMonthStartDate(currDate);
				toDate = DateUtil.getMonthEndDate(currDate);
			} else if (duration.equalsIgnoreCase("Yearly")) {
				fromDate = DateUtil.getYearStartDate(currDate);
				toDate = DateUtil.getYearEndDate(currDate);
			}
		}
		model.setFromDate(fromDate);
		model.setToDate(toDate);
		List<SearchReferralModel> searchReferralModels =  reportRepo.getReferralReport(model, userLoginBean);
		String defaultTDSPercent =ApplicationPropertiesService.getPropertyStringValue(PropertyKeys.DEFAULT_TDS_PERCENT);
		String defaultGSTPercent =ApplicationPropertiesService.getPropertyStringValue(PropertyKeys.DEFAULT_GST_PERCENT);
		for(SearchReferralModel referralModel : searchReferralModels) {
			if(Objects.nonNull(defaultTDSPercent) && Double.parseDouble(defaultTDSPercent)>0) {
				Double processingFee = referralModel.getProcessingFee();
				Double aggrementFee = referralModel.getAggrementFee();
				Double documentationFee = referralModel.getDocumentationFee();
				aggrementFee = StringUtil.roundDouble(aggrementFee, 2);
				processingFee = StringUtil.roundDouble(processingFee, 2);
				documentationFee = StringUtil.roundDouble(documentationFee, 2);
				referralModel.setProcessingFee(processingFee);
				referralModel.setAggrementFee(aggrementFee);
				referralModel.setDocumentationFee(documentationFee);
			}
			if(Objects.nonNull(defaultGSTPercent) && Double.parseDouble(defaultGSTPercent)>0) {
				Double defaultGSTPercentDouble = (Double.parseDouble(defaultGSTPercent)/100)+1;
				
				Double processingFee = referralModel.getProcessingFee();
				Double aggrementFee = referralModel.getAggrementFee();
				Double documentationFee = referralModel.getDocumentationFee();
				if(processingFee>0) {
					processingFee = processingFee/defaultGSTPercentDouble;
				}
				if(aggrementFee>0) {
					aggrementFee = aggrementFee/defaultGSTPercentDouble;
				}
				if(documentationFee>0) {
					documentationFee = documentationFee/defaultGSTPercentDouble;
				}
				
				aggrementFee = StringUtil.roundDouble(aggrementFee, 2);
				processingFee = StringUtil.roundDouble(processingFee, 2);
				documentationFee = StringUtil.roundDouble(documentationFee, 2);
				referralModel.setProcessingFee(processingFee);
				referralModel.setAggrementFee(aggrementFee);
				referralModel.setDocumentationFee(documentationFee);
			}
			
		}
		return SearchReferaalConverter.convert(searchReferralModels);
	}
	
	public List<MyActionReferralBean> getMyActionReport(UserLoginBean userLoginBean) {
		return MyActionReferralConverter.convert(reportRepo.getMyActionReport(userLoginBean));
	}
	
	
	
}
