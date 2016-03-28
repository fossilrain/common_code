package cn.com.common;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.net.HttpURLConnection;

/**
 * 调用第三方接口方法
 * @author fossil
 *
 */
public class CommonHttpRequest {

	public String doHttpRequest(String commonURL,String reqMethod,int timeout,String msg,String charset){
		String reMsg=null;
		HttpURLConnection urlConnection=null;
		//建立连接
		try {
			java.net.URL url=new java.net.URL(commonURL);
			urlConnection=(HttpURLConnection) url.openConnection();
			
			urlConnection.setRequestMethod(reqMethod);
			urlConnection.setDoInput(true);
			urlConnection.setDoOutput(true);
			urlConnection.setUseCaches(false);
			urlConnection.setRequestProperty("User-Agent", "MSIE");
			urlConnection.setConnectTimeout(timeout);
			urlConnection.setReadTimeout(timeout);
			urlConnection.connect();
			//发送信息
			if(msg != null && msg.trim().length() != 0){
				if(charset == null){
					urlConnection.getOutputStream().write(msg.getBytes());
				}else{
					urlConnection.getOutputStream().write(msg.getBytes(charset));
				}
			}
			int resCode=urlConnection.getResponseCode();//返回码
			if(resCode != 200){//通信异常
				
			}else{
				BufferedInputStream bs=new BufferedInputStream(urlConnection.getInputStream());
				ByteArrayOutputStream buffer=new ByteArrayOutputStream();
				byte[]buff=new byte[1024];
				int len=0;
				int contentLength=urlConnection.getContentLength();
				if(contentLength == -1){//长度未知
					//可能返回多个通信包，需要多次读取
					while((len=bs.read(buff)) != -1){
						buffer.write(buff,0,len);
					}
				}else{
					int readLen=0;//已读取长度
					while ((len=bs.read(buff)) != -1){
						buffer.write(buff,0,len);
						readLen+=len;
						if(readLen >= contentLength){
							break;
						}
					}
				}
				if(charset == null){
					reMsg=new String(buffer.toByteArray());
				}else{
					reMsg=new String(buffer.toByteArray(),charset);
				}
			}
			return reMsg;
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			if(urlConnection != null){
				try{
					urlConnection.disconnect();
					urlConnection=null;
				}catch(Exception e){}
			}
		}
		return reMsg;
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		CommonHttpRequest chr=new CommonHttpRequest();
		System.out.println(chr.doHttpRequest("https://www.baidu.com", "POST", 300000, null, null));
	}

}
