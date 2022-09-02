package com.assignsecurities.converter;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;

import com.assignsecurities.bean.MyActionReferralBean;
import com.assignsecurities.domain.MyActionReferralModel;

public class MyActionReferralConverter {
	public static MyActionReferralBean convert(MyActionReferralModel model) {
        if (model == null) {
            return null;
        } else {
        	MyActionReferralBean bean = new MyActionReferralBean();
            BeanUtils.copyProperties(model, bean);
            return bean;
        }
    }

    public static MyActionReferralModel convert(MyActionReferralBean bean) {
        if (bean == null) {
            return null;
        } else {
        	MyActionReferralModel model = new MyActionReferralModel();
            BeanUtils.copyProperties(bean, model);
            return model;
        }
    }
    
    public static List<MyActionReferralBean> convert(List<MyActionReferralModel> models) {
		if (models == null) {
			return null;
		} else {
			List<MyActionReferralBean> beans = new ArrayList<>();
			models.forEach(model -> {
				beans.add(convert(model));
			});
			return beans;
		}
	}
}
