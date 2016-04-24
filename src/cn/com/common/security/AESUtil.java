package cn.com.common.security;

import java.io.UnsupportedEncodingException;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;

public class AESUtil {

	//生成key
	public static String generateKey(String sKey) throws UnsupportedEncodingException{
		if (sKey == null) {    
			System.out.print("Key为空null");    
			return null;    
		}    
		// 判断Key是否为16位    
		if (sKey.length() != 16) {    
			System.out.print("Key长度不是16位");    
			return null;    
		} 
		byte[] raw = sKey.getBytes();    
		SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
		byte[]b=Base64.encodeBase64URLSafe(skeySpec.getEncoded());
		return new String(b,"UTF-8");
		//return Base64Util.base64EncodeByByte(skeySpec.getEncoded());
	}
	//生成key对象
	public static SecretKeySpec toKey(String sKey) throws Exception{
		if(sKey != null && sKey.length() > 0){
			byte[]b=new Base64().decode(sKey.getBytes());
			//byte[]b=Base64Util.base64Decode(sKey);
			return new SecretKeySpec(b,"AES");
		}
		return null;
	}
	//加密
	public static String encrypt(String sSrc, String sKey) throws Exception{
		SecretKeySpec skeySpec = toKey(sKey);
		if(skeySpec != null){
			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");//"算法/模式/补码方式"
			//IvParameterSpec iv = new IvParameterSpec("0132030305060708".getBytes());//使用CBC模式，需要一个向量iv，可增加加密算法的强度    
			IvParameterSpec iv = new IvParameterSpec(new Base64().decode(sKey.getBytes()));//使用CBC模式，需要一个向量iv，可增加加密算法的强度    
			cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);    
			//byte[] encrypted = cipher.doFinal(sSrc.getBytes());    
			byte[] encrypted = cipher.doFinal(Base64.encodeBase64URLSafe(sSrc.getBytes("UTF-8"))); 
			
			return new String(Base64.encodeBase64URLSafe(encrypted),"UTF-8");//此处使用BAES64做转码功能，同时能起到2次加密的作用。
		}
		return null;
	}
	//解密
	public static String decrypt(String sSrc, String sKey) throws Exception{
		SecretKeySpec skeySpec = toKey(sKey);
		if(skeySpec != null){
			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");    
			//IvParameterSpec iv = new IvParameterSpec("0132030305060708".getBytes());  
			IvParameterSpec iv = new IvParameterSpec(new Base64().decode(sKey.getBytes()));
			cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);    
			byte[] encrypted1 = new Base64().decode(sSrc.getBytes());//先用bAES64解密    
			byte[] original = cipher.doFinal(encrypted1);  
			//String originalString = new String(original,"UTF-8");   
			String originalString = new String(new Base64().decode(original),"UTF-8"); 
			return originalString; 
		}
		return null; 
	}
	//测试
	public static void main(String[]args) throws Exception{
		/*JSONObject rdObj=new JSONObject();//待加密签名数据
		rdObj.put("bibdb", "bxxxxxxx");
		rdObj.put("name", "xxxxxxx");
		System.out.println(rdObj.toString());
		//String en=Base64.encodeBase64String(rdObj.toString().getBytes());
		//System.out.println(en);
		String en=rdObj.toString();
		//en="Base64编/解码";
		System.out.println(en);
		//System.out.println(generateKey(CommonUtil.generateRandomStr(16)));
		//System.out.println(decrypt("hWDU/0CXjVPQ9qvL1S1hUg==","MDEwMjAzMDQwNTA2MDcwOA=="));
		String key="OTcyYmVlNWM5NGI4MjMyNA==";
		String enStr=encrypt(en,key);
		System.out.println(enStr);
		String de=decrypt(enStr,key);
		System.out.println("解密后："+de);
		//System.out.println(new String(Base64.decodeBase64(de)));
*/	
		System.out.println(generateKey(CommonUtil.generateRandomStr(16)));	
	}
}
