package com.assignsecurities.converter;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;

import com.assignsecurities.bean.CityMasterBean;
import com.assignsecurities.domain.CityMasterModel;

public class CityMasterConverter {
	public static CityMasterBean convert(CityMasterModel model) {
		if (model == null) {
			return null;
		} else {
			CityMasterBean bean = new CityMasterBean();
			BeanUtils.copyProperties(model, bean);
			return bean;
		}
	}

	public static CityMasterModel convert(CityMasterBean bean) {
		if (bean == null) {
			return null;
		} else {
			CityMasterModel model = new CityMasterModel();
			BeanUtils.copyProperties(bean, model);
			return model;
		}
	}
	
	public static List<CityMasterBean> convert(List<CityMasterModel> models) {
		if (models == null) {
			return null;
		} else {
			List<CityMasterBean> beans = new ArrayList<>();
			models.forEach(model->{
				beans.add(convert(model));
			});
			return beans;
		}
	}
	
	public static List<CityMasterModel> convertL(List<CityMasterBean> beans) {
		if (beans == null) {
			return null;
		} else {
			List<CityMasterModel> models = new ArrayList<>();
			beans.forEach(bean->{
				models.add(convert(bean));
			});
			return models;
		}
	}
}
