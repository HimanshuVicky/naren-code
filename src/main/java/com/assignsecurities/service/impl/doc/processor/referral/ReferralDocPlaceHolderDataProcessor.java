package com.assignsecurities.service.impl.doc.processor.referral;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import com.assignsecurities.app.AppConstant;
import com.assignsecurities.bean.AddressBean;
import com.assignsecurities.bean.ReferralsCommisionDtlBean;
import com.assignsecurities.bean.UserLoginBean;
import com.assignsecurities.service.impl.doc.processor.DocPlaceHolderDataProcessor;

public class ReferralDocPlaceHolderDataProcessor extends DocPlaceHolderDataProcessor{
	@Override
	public Map<String, String> preparePlaceHolderData(Object obj){
		UserLoginBean userLoginBean = (UserLoginBean)obj;
		Map<String, String> substitutionData = new HashMap<>();
//		substitutionData.put("${name}", "Narendra Chouhan");
//		substitutionData.put("${first}", "Dishika Chouhan");
//		substitutionData.put("${second}", "Anvika Chouhan");
//		substitutionData.put("${age}", "26");
//		substitutionData.put("${shareholder}", "Rashmi Chouhan");
		String currentDay = LocalDate.now().getDayOfMonth() + "";
		String currentMonth = LocalDate.now().getMonth().name() + "";
		String currentYear = LocalDate.now().getYear() + "";
		substitutionData.put("CurrentDay", currentDay);
		substitutionData.put("CurrentMonth", currentMonth);
		substitutionData.put("CurrentYear", currentYear);
		
		AddressBean communcationAddress =userLoginBean.getApplicationUserBean().getAddress();
		String pinCode= communcationAddress.getPinCode();
		pinCode = Objects.isNull(pinCode)?"":pinCode;
		String commAddress = Objects.isNull(communcationAddress.getAddress())?"":communcationAddress.getAddress() + ", " + communcationAddress.getCity() + ", "
				+ communcationAddress.getState() + ", "
				+ (Objects.isNull(communcationAddress.getCountry()) ? "India" : communcationAddress.getCountry()) + ", "
				+ pinCode;
		ReferralsCommisionDtlBean referralsCommisionDtlBean = userLoginBean.getApplicationUserBean().getReferralsCommisionDtlBean();
		if(userLoginBean.getApplicationUserBean().getUserType().equalsIgnoreCase(AppConstant.USER_TYPE_REFERRAL_PARTNER)) {
			//Referral User
			
//			${ReferralName}, a ${FirmName}, ${ReferralAddress} and ${ReferralCity}
			substitutionData.put("ReferralName", userLoginBean.getApplicationUserBean().getName());
			substitutionData.put("FirmName", "");
			
			substitutionData.put("ReferralAddress", commAddress);
			substitutionData.put("ReferralCity", communcationAddress.getCity());
			
//			${ReferralName}
//			${ReferralCity}
//			${ReferralCity}
			
			//ReferralRegFee, ReferralProcFee and ${ReferralArgFee},
			
			substitutionData.put("ReferralRegFee", referralsCommisionDtlBean.getRegistrationFee()+"");
			substitutionData.put("ReferralProcFee", referralsCommisionDtlBean.getProcesingFeeCommison()+"");
			substitutionData.put("ReferralArgFee", referralsCommisionDtlBean.getAgreementfeeCommision()+"");
			
		}else if(userLoginBean.getApplicationUserBean().getUserType().equalsIgnoreCase(AppConstant.USER_TYPE_FRANCHISE)) {
			//Franchaise
//			${FranchiseOwnerName}, a ${FranchiseName},${FranchiseOwnerAddress}, and ${FranchiseOwnerCity
			substitutionData.put("FranchiseOwnerName", userLoginBean.getApplicationUserBean().getName());
			substitutionData.put("FranchiseName", userLoginBean.getApplicationUserBean().getFranchiseBean().getName());
			substitutionData.put("FranchiseOwnerAddress", commAddress);
			substitutionData.put("FranchiseOwnerCity", communcationAddress.getCity());
			
			//ReferralRegFee, ReferralProcFee, ReferralArgDocFee, ReferralArgFee and ReferralDocFee
			//ReferralArgDocFee
			substitutionData.put("ReferralRegFee", referralsCommisionDtlBean.getRegistrationFee()+"");
			substitutionData.put("ReferralProcFee", referralsCommisionDtlBean.getProcesingFeeCommison()+"");
			substitutionData.put("ReferralArgDocFee", (referralsCommisionDtlBean.getAgreementfeeCommision()+referralsCommisionDtlBean.getDocuProcessingfeeCommision())+"");
			substitutionData.put("ReferralArgFee", referralsCommisionDtlBean.getAgreementfeeCommision()+"");
			substitutionData.put("ReferralDocFee", referralsCommisionDtlBean.getDocuProcessingfeeCommision()+"");
			
		}
		return substitutionData;

	}
}
