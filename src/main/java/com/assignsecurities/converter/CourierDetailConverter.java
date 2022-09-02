package com.assignsecurities.converter;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;

import com.assignsecurities.bean.CourierDetailBean;
import com.assignsecurities.domain.CourierDetailModel;

public class CourierDetailConverter {
	
	public static CourierDetailBean convert(CourierDetailModel model) {
        if (model == null) {
            return null;
        } else {
        	CourierDetailBean bean = new CourierDetailBean();
            BeanUtils.copyProperties(model, bean);
            return bean;
        }
    }

    public static CourierDetailModel convert(CourierDetailBean bean) {
        if (bean == null) {
            return null;
        } else {
        	CourierDetailModel model = new CourierDetailModel();
            BeanUtils.copyProperties(bean, model);
            return model;
        }
    }
    
    public static List<CourierDetailBean> convert(List<CourierDetailModel> models) {
		if (models == null) {
			return null;
		} else {
			List<CourierDetailBean> beans = new ArrayList<>();
			models.forEach(model->{
				beans.add(convert(model));
			});
			return beans;
		}
	}

}
