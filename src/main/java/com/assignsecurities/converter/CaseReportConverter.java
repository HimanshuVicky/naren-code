package com.assignsecurities.converter;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;

import com.assignsecurities.bean.CaseReportBean;
import com.assignsecurities.domain.CaseReportModel;

public class CaseReportConverter {
	public static CaseReportBean convert(CaseReportModel model) {
        if (model == null) {
            return null;
        } else {
        	CaseReportBean bean = new CaseReportBean();
            BeanUtils.copyProperties(model, bean);
            return bean;
        }
    }

    public static CaseReportModel convert(CaseReportBean bean) {
        if (bean == null) {
            return null;
        } else {
        	CaseReportModel model = new CaseReportModel();
            BeanUtils.copyProperties(bean, model);
            return model;
        }
    }
    
    public static List<CaseReportBean> convert(List<CaseReportModel> models) {
		if (models == null) {
			return null;
		} else {
			List<CaseReportBean> beans = new ArrayList<>();
			models.forEach(model -> {
				beans.add(convert(model));
			});
			return beans;
		}
	}
}
