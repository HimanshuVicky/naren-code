package com.assignsecurities.converter;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;

import com.assignsecurities.bean.WitnessBean;
import com.assignsecurities.domain.WitnessModel;

public class WitnessConverter {
	
	public static WitnessBean convert(WitnessModel model) {
		if (model == null) {
			return null;
		} else {
			WitnessBean bean = new WitnessBean();
			BeanUtils.copyProperties(model, bean);
			return bean;
		}
	}

	public static WitnessModel convert(WitnessBean bean) {
		if (bean == null) {
			return null;
		} else {
			WitnessModel model = new WitnessModel();
			BeanUtils.copyProperties(bean, model);
			return model;
		}
	}
	
	public static List<WitnessBean> convert(List<WitnessModel> models) {
		if (models == null) {
			return null;
		} else {
			List<WitnessBean> beans = new ArrayList<>();
			models.forEach(model->{
				beans.add(convert(model));
			});
			return beans;
		}
	}

	public static List<WitnessModel> convertL(List<WitnessBean> beans) {
		if (beans == null) {
			return null;
		} else {
			List<WitnessModel> models = new ArrayList<>();
			beans.forEach(bean->{
				models.add(convert(bean));
			});
			return models;
		}
	}
}
