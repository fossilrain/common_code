package cn.com.common.util;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;

public class IpUtil {
	
	/**
	 * 获取客户端ip地址
	 * @param request
	 * @return
	 */
	public static String getClientIp(HttpServletRequest request){

		String ip = request.getHeader("x-forwarded-for");
		
	    if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
	        ip = request.getHeader("Proxy-Client-IP");
	    }
	    if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
	        ip = request.getHeader("WL-Proxy-Client-IP");
	    }
	    if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
	        ip = request.getHeader("HTTP_CLIENT_IP");
	    }
	    if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
	        ip = request.getHeader("HTTP_X_FORWARDED_FOR");
	    }
	    if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
	        ip = request.getRemoteAddr();
	    }
	    return ip;
	}
	//获取本机ip地址
	public static String getLocalIP() throws SocketException {
		// String ip = InetAddress.getLocalHost().getHostAddress();
		Enumeration<NetworkInterface> allNetInterfaces = NetworkInterface.getNetworkInterfaces();
		InetAddress ip = null;
		String ipStr = "";
		while (allNetInterfaces.hasMoreElements()) {
			NetworkInterface netInterface = (NetworkInterface) allNetInterfaces.nextElement();
			Enumeration<InetAddress> addresses = netInterface.getInetAddresses();
			while (addresses.hasMoreElements()) {
				ip = addresses.nextElement();
				if (ip != null && ip instanceof Inet4Address) {
					//System.out.println("本机的IP = " + ip.getHostAddress());
					ipStr=ip.getHostAddress();
					if("127.0.01".equals(ipStr)){
						continue;
					}
				}
			}
		}
		return ipStr;
	}
	public static String allIp(HttpServletRequest request){
		StringBuffer sb=new StringBuffer();
		sb.append("x-forwarded-for:"+request.getHeader("x-forwarded-for")+"\r\n");
		sb.append("Proxy-Client-IP:"+request.getHeader("Proxy-Client-IP")+"\r\n");
		sb.append("WL-Proxy-Client-IP:"+request.getHeader("WL-Proxy-Client-IP")+"\r\n");
		sb.append("HTTP_CLIENT_IP:"+request.getHeader("HTTP_CLIENT_IP")+"\r\n");
		sb.append("HTTP_X_FORWARDED_FOR:"+request.getHeader("HTTP_X_FORWARDED_FOR")+"\r\n");
		
		sb.append("getRemoteAddr:"+request.getRemoteAddr()+"\r\n");
		return sb.toString();
	}
}
