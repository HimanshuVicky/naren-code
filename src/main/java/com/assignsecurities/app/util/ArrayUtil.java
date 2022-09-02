package com.assignsecurities.app.util;

import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class ArrayUtil {

	public static <K, V> K getKey(Map<K, V> map, V value) {
		return map.keySet().stream().filter(key -> value.equals(map.get(key))).findFirst().get();
	}

	public static <T, E> Set<T> getKeysByValue(Map<T, E> map, E value) {
		return map.entrySet().stream().filter(entry -> Objects.equals(entry.getValue(), value)).map(Map.Entry::getKey)
				.collect(Collectors.toSet());
	}
}
