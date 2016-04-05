package cn.com.test;

import cn.com.test.encrypt.dsa.DSACoder;

public class EncryptTest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			DSACoder.initKey("abcdefv");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

} 