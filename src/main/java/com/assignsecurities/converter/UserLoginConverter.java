package com.assignsecurities.converter;

import org.springframework.beans.BeanUtils;

import com.assignsecurities.bean.UserLoginBean;
import com.assignsecurities.domain.UserLogin;

import java.util.Objects;

public class UserLoginConverter {

	public static UserLoginBean convert(UserLogin userLogin, UserLoginBean userLoginBean) {
		if (Objects.isNull(userLogin)) {
			return null;
		}
		if (Objects.isNull(userLoginBean)) {
			userLoginBean = UserLoginBean.builder().build();
		}
		BeanUtils.copyProperties(userLogin, userLoginBean);
		return userLoginBean;
	}

	public static UserLogin convert(UserLoginBean userLoginBean, UserLogin userLogin) {
		if (Objects.isNull(userLoginBean)) {
			return null;
		}
		if (Objects.isNull(userLogin)) {
			userLogin = new UserLogin();
		}
		BeanUtils.copyProperties(userLoginBean, userLogin);
		return userLogin;
	}
}
