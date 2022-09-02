package com.assignsecurities.converter;

import org.springframework.beans.BeanUtils;

import com.assignsecurities.bean.DocumentBean;
import com.assignsecurities.domain.DocumentModel;

public class DocumentConverter {
	public static DocumentBean convert(DocumentModel model) {
        if (model == null) {
            return null;
        } else {
        	DocumentBean bean = new DocumentBean();
            BeanUtils.copyProperties(model, bean);
            return bean;
        }
    }

    public static DocumentModel convert(DocumentBean bean) {
        if (bean == null) {
            return null;
        } else {
        	DocumentModel model = new DocumentModel();
            BeanUtils.copyProperties(bean, model);
            return model;
        }
    }
}
