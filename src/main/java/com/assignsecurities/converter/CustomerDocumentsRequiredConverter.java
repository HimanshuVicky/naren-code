package com.assignsecurities.converter;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;

import com.assignsecurities.bean.CustomerDocumentsRequiredBean;
import com.assignsecurities.domain.CustomerDocumentsRequiredModel;

public class CustomerDocumentsRequiredConverter {
	public static CustomerDocumentsRequiredBean convert(CustomerDocumentsRequiredModel model) {
		if (model == null) {
			return null;
		} else {
			CustomerDocumentsRequiredBean bean = new CustomerDocumentsRequiredBean();
			BeanUtils.copyProperties(model, bean);
			return bean;
		}
	}

	public static CustomerDocumentsRequiredModel convert(CustomerDocumentsRequiredBean bean) {
		if (bean == null) {
			return null;
		} else {
			CustomerDocumentsRequiredModel model = new CustomerDocumentsRequiredModel();
			BeanUtils.copyProperties(bean, model);
			return model;
		}
	}
	
	public static List<CustomerDocumentsRequiredBean> convert(List<CustomerDocumentsRequiredModel> models) {
		if (models == null) {
			return null;
		} else {
			List<CustomerDocumentsRequiredBean> beans = new ArrayList<>();
			models.forEach(model->{
				beans.add(convert(model));
			});
			return beans;
		}
	}
	
	public static List<CustomerDocumentsRequiredModel> convertL(List<CustomerDocumentsRequiredBean> beans) {
		if (beans == null) {
			return null;
		} else {
			List<CustomerDocumentsRequiredModel> models = new ArrayList<>();
			beans.forEach(bean->{
				models.add(convert(bean));
			});
			return models;
		}
	}
}
