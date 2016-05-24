package cn.com.common.security;

import java.io.UnsupportedEncodingException;

import org.apache.commons.codec.binary.Base64;
import org.json.JSONException;
import org.json.JSONObject;

public class Base64Util {
	//base64加密
	/*public static String base64Encode(String data) throws UnsupportedEncodingException{  
        return new String(Base64.encodeBase64(data.getBytes()),"UTF-8");
    } 
	public static String base64EncodeByByte(byte[] data) throws UnsupportedEncodingException{  
        return new String(Base64.encodeBase64(data),"UTF-8");  
    } 
	//base64解密
    public static byte[] base64Decode(String data){  
        return Base64.decodeBase64(data.getBytes());  
    }  
    public static byte[] base64DecodeByByte(byte[] data){
        return Base64.decodeBase64(data); 
    }*/
	public static String base64Encode(String data) throws UnsupportedEncodingException{  
        return new String(Base64.encodeBase64URLSafe(data.getBytes("UTF-8")),"UTF-8");
    } 
	public static String base64EncodeByByte(byte[] data) throws UnsupportedEncodingException{  
        return new String(Base64.encodeBase64URLSafe(data),"UTF-8");  
    } 
	//base64解密
    public static byte[] base64Decode(String data) throws UnsupportedEncodingException{  
        return new Base64().decode(data.getBytes("UTF-8"));
    }  
    public static byte[] base64DecodeByByte(byte[] data){
        return Base64.decodeBase64(data); 
    }
	/**
	 * TODO 方法的描述：。 
	 * <pre>
	 *
	 * </pre>
	 * @param args
	 * @throws JSONException 
	 * @throws UnsupportedEncodingException 
	 */
	public static void main(String[] args) throws JSONException, UnsupportedEncodingException {
		// TODO Auto-generated method stub
		JSONObject rdObj=new JSONObject();//待加密签名数据
		rdObj.put("bid", "bxxxxxxx");
		rdObj.put("name", "xxxxxxx");
		String en=base64Encode(rdObj.toString());
		System.out.println(en);
		System.out.println(new String(base64Decode(en)));
	}

}
