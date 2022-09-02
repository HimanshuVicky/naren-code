package com.assignsecurities.converter;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;

import com.assignsecurities.bean.CaseDeathCertificateDtlBean;
import com.assignsecurities.domain.CaseDeathCertificateDtlModel;

public class CaseDeathCertificateDtlCoverter {
	public static CaseDeathCertificateDtlBean convert(CaseDeathCertificateDtlModel model) {
        if (model == null) {
            return null;
        } else {
        	CaseDeathCertificateDtlBean bean = new CaseDeathCertificateDtlBean();
            BeanUtils.copyProperties(model, bean);
            return bean;
        }
    }

    public static CaseDeathCertificateDtlModel convert(CaseDeathCertificateDtlBean bean) {
        if (bean == null) {
            return null;
        } else {
        	CaseDeathCertificateDtlModel model = new CaseDeathCertificateDtlModel();
            BeanUtils.copyProperties(bean, model);
            return model;
        }
    }
    
    public static List<CaseDeathCertificateDtlBean> convert(List<CaseDeathCertificateDtlModel> models) {
		if (models == null) {
			return null;
		} else {
			List<CaseDeathCertificateDtlBean> beans = new ArrayList<>();
			models.forEach(model->{
				beans.add(convert(model));
			});
			return beans;
		}
	}

}
