package com.assignsecurities.converter;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;

import com.assignsecurities.bean.ApplicationQuestionerBean;
import com.assignsecurities.domain.ApplicationQuestionerModel;

public class ApplicationQuestionerConvertor {
	public static ApplicationQuestionerBean convert(ApplicationQuestionerModel model) {
		if (model == null) {
			return null;
		} else {
			ApplicationQuestionerBean bean = new ApplicationQuestionerBean();
			BeanUtils.copyProperties(model, bean);
			bean.setQuestioners(QuestionerConverter.convert(model.getQuestioners()));
			return bean;
		}
	}

	public static ApplicationQuestionerModel convert(ApplicationQuestionerBean bean) {
		if (bean == null) {
			return null;
		} else {
			ApplicationQuestionerModel model = new ApplicationQuestionerModel();
			BeanUtils.copyProperties(bean, model);
			model.setQuestioners(QuestionerConverter.convertL(bean.getQuestioners()));
			return model;
		}
	}

	public static List<ApplicationQuestionerBean> convert(List<ApplicationQuestionerModel> models) {
		if (models == null) {
			return null;
		} else {
			List<ApplicationQuestionerBean> beans = new ArrayList<>();
			models.forEach(model -> {
				beans.add(convert(model));
			});
			return beans;
		}
	}

	public static List<ApplicationQuestionerModel> convertL(List<ApplicationQuestionerBean> beans) {
		if (beans == null) {
			return null;
		} else {
			List<ApplicationQuestionerModel> models = new ArrayList<>();
			models.forEach(bean -> {
				beans.add(convert(bean));
			});
			return models;
		}
	}
}
