package cn.com.common.instance;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;


public class API extends HttpServlet {
	private static final long serialVersionUID = 1L;
    
	private String[]urlEle;//接口地址解析
	private JSONObject jso;//数据参数
	private String reqURI;//接口地址
	private String httpMethod;//请求方式
	
    public API() {
        super();
    }
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html;charset=utf-8");
		request.setCharacterEncoding("UTF-8");
		String reMsg="";
		if(filter(request) == 0){//过滤器通过
			
			String className="";
			String methodName="";
			if(this.urlEle.length == 3){
				className=this.urlEle[1]+"Service";
				methodName=this.urlEle[2];
			}
			Class classX=null;
			int notFind=1;
			try {
				classX = Class.forName("com.icbc.tdyh.service."+className);
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
			try {
				Map<String,String> mtMap=(Map<String,String>) classX.getDeclaredMethod(methodName, new Class[]{String.class,JSONObject.class}).invoke(classX.newInstance(), new Object[]{this.httpMethod,this.jso});
				notFind=0;
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (SecurityException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
			} catch (InstantiationException e) {
				e.printStackTrace();
			}
			if(notFind != 0){
				response.sendError(HttpServletResponse.SC_NOT_FOUND);
				return;
			}
		}else{
			response.sendError(HttpServletResponse.SC_NOT_FOUND);
			return;
		}
		PrintWriter out = response.getWriter();
		out.write(reMsg);
		out.flush();
		out.close();
	}
	//过滤器
	private int filter(HttpServletRequest request){
		this.urlEle=analyzeURI(request);
		if(this.urlEle == null){
			return 1;//404
		}
		if(this.urlEle.length > 2){
			String jsonStr=request.getParameter("jsonStr");
			String bizid=null;
			try {
				this.jso=new JSONObject(new JSONTokener(jsonStr));
				this.jso.put("reqURI", reqURI);
				this.httpMethod=request.getMethod();
				this.jso.put("httpMethod", this.httpMethod);
			} catch (JSONException e) {
				e.printStackTrace();
				return -1;//500
			}
			return 0;
		}else{
			return 2;//404
		}
	}
	//解析url
	private String[] analyzeURI(HttpServletRequest request){
		this.reqURI=request.getRequestURI();
		if(this.reqURI != null && this.reqURI.indexOf("/") > -1){
			String contextPath=request.getContextPath();
			reqURI=reqURI.replaceFirst(contextPath+"/", "");
			return reqURI.split("/");
		}else{
			return null;
		}
	}
}
