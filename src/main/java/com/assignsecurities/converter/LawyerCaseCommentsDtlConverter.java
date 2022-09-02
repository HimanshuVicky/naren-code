package com.assignsecurities.converter;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;

import com.assignsecurities.bean.LawyerCaseCommentsDtlBean;
import com.assignsecurities.domain.LawyerCaseCommentsDtlModel;

public class LawyerCaseCommentsDtlConverter {
	
	public static LawyerCaseCommentsDtlBean convert(LawyerCaseCommentsDtlModel model) {
        if (model == null) {
            return null;
        } else {
        	LawyerCaseCommentsDtlBean bean = new LawyerCaseCommentsDtlBean();
            BeanUtils.copyProperties(model, bean);
            return bean;
        }
    }

    public static LawyerCaseCommentsDtlModel convert(LawyerCaseCommentsDtlBean bean) {
        if (bean == null) {
            return null;
        } else {
        	LawyerCaseCommentsDtlModel model = new LawyerCaseCommentsDtlModel();
            BeanUtils.copyProperties(bean, model);
            return model;
        }
    }
    
    public static List<LawyerCaseCommentsDtlBean> convert(List<LawyerCaseCommentsDtlModel> models) {
		if (models == null) {
			return null;
		} else {
			List<LawyerCaseCommentsDtlBean> beans = new ArrayList<>();
			models.forEach(model->{
				beans.add(convert(model));
			});
			return beans;
		}
	}

}
