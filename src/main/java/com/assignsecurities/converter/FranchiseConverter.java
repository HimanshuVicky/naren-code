package com.assignsecurities.converter;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;

import com.assignsecurities.bean.FranchiseBean;
import com.assignsecurities.domain.FranchiseModel;

public class FranchiseConverter {
	
	public static FranchiseBean convert(FranchiseModel model) {
		if (model == null) {
			return null;
		} else {
			FranchiseBean bean = new FranchiseBean();
			BeanUtils.copyProperties(model, bean);
//			bean.setAddress(AddressBean.builder().Id(model.getAddressId()).build());
			return bean;
		}
	}

	public static FranchiseModel convert(FranchiseBean bean) {
		if (bean == null) {
			return null;
		} else {
			FranchiseModel model = new FranchiseModel();
			BeanUtils.copyProperties(bean, model);
			model.setAddress(AddressConverter.convert(bean.getAddress()));
			return model;
		}
	}
	
	public static List<FranchiseBean> convert(List<FranchiseModel> models) {
		if (models == null) {
			return null;
		} else {
			List<FranchiseBean> beans = new ArrayList<>();
			models.forEach(model->{
				beans.add(convert(model));
			});
			return beans;
		}
	}

}
