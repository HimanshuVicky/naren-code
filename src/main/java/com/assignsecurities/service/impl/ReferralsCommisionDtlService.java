package com.assignsecurities.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.assignsecurities.app.AppConstant;
import com.assignsecurities.bean.ReferralsCommisionDtlBean;
import com.assignsecurities.bean.UserLoginBean;
import com.assignsecurities.converter.ReferralsCommisionDtlConverter;
import com.assignsecurities.domain.ReferralsCommisionDtlModel;
import com.assignsecurities.repository.impl.ReferralsCommisionDtlRepo;

@Service
@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
public class ReferralsCommisionDtlService {

	@Autowired
	private ReferralsCommisionDtlRepo referralsCommisionDtlRepo;

	public ReferralsCommisionDtlBean getReferralPartnerCommisionDtlforGivenAppUserId(Long userId) {
		return ReferralsCommisionDtlConverter
				.convert(referralsCommisionDtlRepo.getReferralPartnerCommisionDtlforGivenAppUserId(userId));
	}

	public ReferralsCommisionDtlBean getReferralPartnerCommisionDtlforGivenFranchiseId(Long franchiseId) {
		return ReferralsCommisionDtlConverter
				.convert(referralsCommisionDtlRepo.getReferralPartnerCommisionDtlforGivenFranchiseId(franchiseId));
	}

	public ReferralsCommisionDtlBean getById(Long id) {
		ReferralsCommisionDtlBean refrralCommisionbean = ReferralsCommisionDtlConverter
				.convert(referralsCommisionDtlRepo.getById(id));
		return refrralCommisionbean;
	}

	public List<ReferralsCommisionDtlBean> getByGivenFranchiseId(Long franchiseId) {
		List<ReferralsCommisionDtlModel> referralCommDtlslst = referralsCommisionDtlRepo
				.getByGivenFranchiseId(franchiseId);
		return ReferralsCommisionDtlConverter.convert(referralCommDtlslst);
	}

	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Throwable.class)
	public Long add(ReferralsCommisionDtlBean bean) {

//		bean.setIsRegistrationFeeReceived(Boolean.FALSE);
		bean.setESignAgreementStatus(AppConstant.FRANCHISE_ESIGN_AGREEMENT_STATUS_NEW);
		long referralCommDtlId = referralsCommisionDtlRepo.add(ReferralsCommisionDtlConverter.convert(bean));
		if (referralCommDtlId <= 0)
			return null;

		return referralCommDtlId;

	}

//	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Throwable.class)
//	public void update(ReferralsCommisionDtlBean bean) {
//		referralsCommisionDtlRepo.update(ReferralsCommisionDtlConverter.convert(bean));
//	}
	
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Throwable.class)
	public void updateForProfileEdit(ReferralsCommisionDtlBean bean) {
		referralsCommisionDtlRepo.updateForProfileEdit(ReferralsCommisionDtlConverter.convert(bean));
	}
	
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Throwable.class)
	public void updateFeeReferenceNumber(ReferralsCommisionDtlBean bean, UserLoginBean userLoginBean) {
		if (AppConstant.USER_TYPE_REFERRAL_PARTNER.equalsIgnoreCase(userLoginBean.getApplicationUserBean().getUserType())){
			referralsCommisionDtlRepo.updateFeeReferenceNumberForReferralUser(bean.getFeeReferenceKey(), userLoginBean.getId());
		}else {
			referralsCommisionDtlRepo.updateFeeReferenceNumberForReferralFranchise(bean.getFeeReferenceKey(), userLoginBean.getApplicationUserBean().getFranchiseId());
		}
		
	}
	
}
