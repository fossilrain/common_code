package cn.com.common.util;

public class PubUtil {
	/**
	 * 首字母转大写
	 * @param str 待转换字符串
	 * @return
	 */
	private static String captureStr(String str) {
		char[] cs = str.toCharArray();
		cs[0] -= 32;
		return String.valueOf(cs);
	}
	/**
	 * 首字母转大写
	 * @param str 待转换字符串
	 * @return
	 */
	public static String firstLetterToUppercase(String str){
		str=str.toLowerCase();
		return captureStr(str);
	}
}
