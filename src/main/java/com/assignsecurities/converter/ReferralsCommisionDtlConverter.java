package com.assignsecurities.converter;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;

import com.assignsecurities.bean.ReferralsCommisionDtlBean;
import com.assignsecurities.domain.ReferralsCommisionDtlModel;

public class ReferralsCommisionDtlConverter {
	
	public static ReferralsCommisionDtlBean convert(ReferralsCommisionDtlModel model) {
		if (model == null) {
			return null;
		} else {
			ReferralsCommisionDtlBean bean = new ReferralsCommisionDtlBean();
			BeanUtils.copyProperties(model, bean);
//			bean.setAddress(AddressBean.builder().Id(model.getAddressId()).build());
			return bean;
		}
	}

	public static ReferralsCommisionDtlModel convert(ReferralsCommisionDtlBean bean) {
		if (bean == null) {
			return null;
		} else {
			ReferralsCommisionDtlModel model = new ReferralsCommisionDtlModel();
			BeanUtils.copyProperties(bean, model);
//			model.setAddress(AddressConverter.convert(bean.getAddress()));
			return model;
		}
	}
	
	public static List<ReferralsCommisionDtlBean> convert(List<ReferralsCommisionDtlModel> models) {
		if (models == null) {
			return null;
		} else {
			List<ReferralsCommisionDtlBean> beans = new ArrayList<>();
			models.forEach(model->{
				beans.add(convert(model));
			});
			return beans;
		}
	}

}
