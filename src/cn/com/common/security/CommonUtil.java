package cn.com.common.security;

import java.util.UUID;

public class CommonUtil {

	public static String generateUUID(){
		return UUID.randomUUID().toString().replace("-", "");
	}
	//生成指定位数的随机串 digits<=32
	public static String generateRandomStr(int digits){
		String uuid=generateUUID();
		if(digits <1 || digits > 32){
			return null;
		}
		return uuid.substring(32-digits);
	}
	/**
	 * TODO 方法的描述：。 
	 * <pre>
	 *
	 * </pre>
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println(generateUUID().length());
		System.out.println(generateRandomStr(6).length());
	}

}
