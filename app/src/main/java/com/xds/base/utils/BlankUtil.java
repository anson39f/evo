package com.xds.base.utils;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

public class BlankUtil {

	/**
	 * 判断字符串是否为空
	 * @return 空:true 否则：false
	 */
	public static boolean isBlank(final String str) {
		return (str == null) || (str.trim().length() <= 0);
	}


	/**
	 * 判断字符是否为空
	 * @param cha
	 * @return
	 */
	public static boolean isBlank(final Character cha){
		return (cha==null) || cha.equals(' ');
	}

	/**
	 * 判断对象是否为空
	 */
	public static boolean isBlank(final Object obj) {
		return (obj==null);
	}

	/**
	 * 判断数组是否为空
	 * @param objs
	 * @return
	 */
	public static boolean isBlank(final Object[] objs) {
		return (objs == null) || (objs.length <= 0);
	}

	/**
	 * 判断Collectionj是否为空
	 * @param obj
	 * @return
	 */
	public static boolean isBlank(final Collection<?> obj) {
		return (obj == null) || (obj.size() <= 0);
	}

	/**
	 * 判断Set是否为空
	 * @param obj
	 * @return
	 */
	public static boolean isBlank(final Set<?> obj) {
		return (obj == null) || (obj.size() <= 0);
	}

	/**
	 * 判断Map是否为空
	 * @param obj
	 * @return
	 */
	public static boolean isBlank(final Map<Object, Object> obj) {
		return (obj == null) || (obj.size() <= 0);
	}


}
