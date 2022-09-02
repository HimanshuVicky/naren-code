package com.assignsecurities.converter;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;

import com.assignsecurities.bean.QuestionerBean;
import com.assignsecurities.domain.QuestionerModel;

public class QuestionerConverter {
	public static QuestionerBean convert(QuestionerModel model) {
		if (model == null) {
			return null;
		} else {
			QuestionerBean bean = new QuestionerBean();
			BeanUtils.copyProperties(model, bean);
			return bean;
		}
	}

	public static QuestionerModel convert(QuestionerBean bean) {
		if (bean == null) {
			return null;
		} else {
			QuestionerModel model = new QuestionerModel();
			BeanUtils.copyProperties(bean, model);
			return model;
		}
	}

	public static List<QuestionerBean> convert(List<QuestionerModel> models) {
		if (models == null) {
			return null;
		} else {
			List<QuestionerBean> beans = new ArrayList<>();
			models.forEach(model -> {
				beans.add(convert(model));
			});
			return beans;
		}
	}
	
	public static List<QuestionerModel> convertL(List<QuestionerBean> beans) {
		if (beans == null) {
			return null;
		} else {
			List<QuestionerModel> models = new ArrayList<>();
			beans.forEach(model -> {
				models.add(convert(model));
			});
			return models;
		}
	}
}
