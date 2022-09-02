package com.assignsecurities.converter;

import org.springframework.beans.BeanUtils;

import com.assignsecurities.bean.AddToFavouriteBean;
import com.assignsecurities.domain.AddToFavouriteModel;

public class AddToFavouriteConvertor {
	public static AddToFavouriteBean convert(AddToFavouriteModel model) {
		if (model == null) {
			return null;
		} else {
			AddToFavouriteBean bean = new AddToFavouriteBean();
			BeanUtils.copyProperties(model, bean);
			return bean;
		}
	}

	public static AddToFavouriteModel convert(AddToFavouriteBean bean) {
		if (bean == null) {
			return null;
		} else {
			AddToFavouriteModel model = new AddToFavouriteModel();
			BeanUtils.copyProperties(bean, model);
			return model;
		}
	}
}
