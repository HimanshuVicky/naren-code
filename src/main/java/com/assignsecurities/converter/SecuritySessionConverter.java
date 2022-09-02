package com.assignsecurities.converter;

import org.springframework.beans.BeanUtils;

import com.assignsecurities.bean.SecuritySessionBean;
import com.assignsecurities.domain.SecuritySession;

public class SecuritySessionConverter {
    public static SecuritySessionBean convert(SecuritySession securitysession) {
        if (securitysession == null) {
            return null;
        } else {
            SecuritySessionBean securitySessionBean = new SecuritySessionBean();
            BeanUtils.copyProperties(securitysession, securitySessionBean);

            return securitySessionBean;
        }
    }

    public static SecuritySession convert(SecuritySessionBean securitySessionBean) {
        if (securitySessionBean == null) {
            return null;
        } else {
            SecuritySession securitysession = new SecuritySession();
            BeanUtils.copyProperties(securitySessionBean, securitysession);
            return securitysession;
        }
    }
}
