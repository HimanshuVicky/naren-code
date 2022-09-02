package com.assignsecurities.converter;

import org.springframework.beans.BeanUtils;

import com.assignsecurities.bean.AddressBean;
import com.assignsecurities.domain.AddressModel;

public class AddressConverter {
//	AddressBean
	public static AddressBean convert(AddressModel model) {
        if (model == null) {
            return null;
        } else {
        	AddressBean bean = new AddressBean();
            BeanUtils.copyProperties(model, bean);
            return bean;
        }
    }

    public static AddressModel convert(AddressBean bean) {
        if (bean == null) {
            return null;
        } else {
        	AddressModel model = new AddressModel();
            BeanUtils.copyProperties(bean, model);
            return model;
        }
    }
}
