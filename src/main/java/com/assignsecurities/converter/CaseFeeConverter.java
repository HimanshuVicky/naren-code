package com.assignsecurities.converter;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;

import com.assignsecurities.bean.CaseFeeBean;
import com.assignsecurities.domain.CaseFeeModel;

public class CaseFeeConverter {
	public static CaseFeeBean convert(CaseFeeModel model) {
		if (model == null) {
			return null;
		} else {
			CaseFeeBean bean = new CaseFeeBean();
			BeanUtils.copyProperties(model, bean);
			return bean;
		}
	}

	public static CaseFeeModel convert(CaseFeeBean bean) {
		if (bean == null) {
			return null;
		} else {
			CaseFeeModel model = new CaseFeeModel();
			BeanUtils.copyProperties(bean, model);
			return model;
		}
	}
	
	
	
	public static List<CaseFeeBean> convert(List<CaseFeeModel> models) {
		if (models == null) {
			return null;
		} else {
			List<CaseFeeBean> beans = new ArrayList<>();
			models.forEach(model->{
				beans.add(convert(model));
			});
			return beans;
		}
	}
	
	public static List<CaseFeeModel> convertL(List<CaseFeeBean> beans) {
		if (beans == null) {
			return null;
		} else {
			List<CaseFeeModel> models = new ArrayList<>();
			beans.forEach(bean->{
				models.add(convert(bean));
			});
			return models;
		}
	}
}
