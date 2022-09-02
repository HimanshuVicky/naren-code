package com.assignsecurities.app.util;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

import com.assignsecurities.app.exception.ValidationException;
import com.assignsecurities.bean.ValidationError;

public final class ArgumentHelper {
	private ArgumentHelper() {
	}

	public static void requiredPositiveValue(Double arg, String message) {
		if (Objects.isNull(arg) || arg<=0) {
			throw new ValidationException(message,ValidationError.builder().message(message).build());
		}
	}
	
	public static void requiredNonNullAndNonEmptyValue(String arg, String message) {
		if (StringUtils.isBlank(arg)) {
			throw new ValidationException(message,ValidationError.builder().message(message).build());
		}
	}

	public static void requiredNonNullAndNonEmptyList(List list, String message) {
		if (isEmpty(list)) {
			throw new ValidationException(message,ValidationError.builder().message(message).build());
		}
	}

	public static void requiredObjectNonNull(Object object, String message) {
		if (Objects.isNull(object)) {
			throw new ValidationException(message,ValidationError.builder().message(message).build());
		}
	}


	public static boolean isEmpty(List list) {
		return list == null || list.isEmpty();
	}

	public static boolean isNotEmpty(List list) {
		return !isEmpty(list);
	}

	public static void requiredNonNullAndNonEmptyMap(Map map, String message) {
		if (isEmpty(map)) {
			throw new ValidationException(message,ValidationError.builder().message(message).build());
		}
	}

	public static boolean isEmpty(Map map) {
		return map == null || map.isEmpty();
	}

	public static boolean isNotEmpty(Map map) {
		return !isEmpty(map);
	}

	public static boolean isValid(String value) {
		return StringUtils.isNotBlank(value);
	}
	
	public static boolean isPositive(Double arg) {
		return !(Objects.isNull(arg) || arg<=0);
	}
	
	public static boolean isPositive(Long arg) {
		return !(Objects.isNull(arg) || arg<=0);
	}

	public static void requiredNonNullAndNonEmptySet(Set list, String message) {
		if (isEmpty(list)) {
			throw new ValidationException(message,ValidationError.builder().message(message).build());
		}
	}

	public static boolean isEmpty(Set list) {
		return list == null || list.isEmpty();
	}

	public static boolean isNotEmpty(Set list) {
		return !isEmpty(list);
	}
	public static boolean isPositiveWithZero(Double arg) {
		return !(Objects.isNull(arg) || arg<0);
	}
	public static boolean isPositiveWithZero(Long arg) {
		return !(Objects.isNull(arg) || arg<0);
	}
	public static boolean isTrueFAlse(Boolean arg) {
		if(Objects.isNull(arg)) {
			return Boolean.FALSE;
		}
		return arg;
	}
	
	public static void main(String[] args) {
		System.out.println(isTrueFAlse(null));
		System.out.println(isTrueFAlse(true));
		System.out.println(isTrueFAlse(false));
	}
}