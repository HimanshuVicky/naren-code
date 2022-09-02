package com.assignsecurities.converter;

import org.springframework.beans.BeanUtils;

import com.assignsecurities.bean.ScriptSearchBean;
import com.assignsecurities.domain.ScriptSearchModel;

public class ScriptSearchConvertor {
	public static ScriptSearchBean convert(ScriptSearchModel model) {
		if (model == null) {
			return null;
		} else {
			ScriptSearchBean bean = new ScriptSearchBean();
			BeanUtils.copyProperties(model, bean);
			return bean;
		}
	}

	public static ScriptSearchModel convert(ScriptSearchBean bean) {
		if (bean == null) {
			return null;
		} else {
			ScriptSearchModel model = new ScriptSearchModel();
			BeanUtils.copyProperties(bean, model);
			return model;
		}
	}
}
