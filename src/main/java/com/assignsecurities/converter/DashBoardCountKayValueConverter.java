package com.assignsecurities.converter;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;

import com.assignsecurities.bean.DashBoardCountKayValueBean;
import com.assignsecurities.domain.DashBoardCountKayValue;

public class DashBoardCountKayValueConverter {
	public static DashBoardCountKayValueBean convert(DashBoardCountKayValue model) {
		if (model == null) {
			return null;
		} else {
			DashBoardCountKayValueBean bean = new DashBoardCountKayValueBean();
			BeanUtils.copyProperties(model, bean);
			return bean;
		}
	}

	public static DashBoardCountKayValue convert(DashBoardCountKayValueBean bean) {
		if (bean == null) {
			return null;
		} else {
			DashBoardCountKayValue model = new DashBoardCountKayValue();
			BeanUtils.copyProperties(bean, model);
			return model;
		}
	}

	public static List<DashBoardCountKayValueBean> convert(List<DashBoardCountKayValue> models) {
		if (models == null) {
			return null;
		} else {
			List<DashBoardCountKayValueBean> beans = new ArrayList<>();
			models.forEach(model -> {
				beans.add(convert(model));
			});
			return beans;
		}
	}
}
