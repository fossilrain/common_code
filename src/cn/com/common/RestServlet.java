package cn.com.common;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class RestServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public RestServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see Servlet#init(ServletConfig)
	 */
	public void init(ServletConfig config) throws ServletException {
		System.out.println("init方法");
	}

	/**
	 * @see Servlet#destroy()
	 */
	public void destroy() {
		System.out.println("destroy方法");
	}

	/**
	 * @see Servlet#getServletConfig()
	 */
	public ServletConfig getServletConfig() {
		System.out.println("ServletConfig方法");
		return null;
	}

	/**
	 * @see Servlet#getServletInfo()
	 */
	public String getServletInfo() {
		System.out.println("getServletInfo方法");
		return null; 
	}

	/**
	 * @see HttpServlet#service(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		/*System.out.println("service方法");
		System.out.println(request.getRequestURL()+"#############");
		System.out.println(request.getRequestURI()+"#############");
		System.out.println(request.getContextPath()+"#############");
		PrintWriter out = response.getWriter();
		out.write(request.getRequestURI()+"@@@"+request.getParameter("jsonStr"));
		out.write("xxxxxxx");
		out.flush();
		out.close();*/
		
		String reqURI=request.getRequestURI();
		String contextPath=request.getContextPath();
		String jsonStr=request.getParameter("jsonStr");
		reqURI=reqURI.replaceFirst(contextPath+"/", "");
		String[]urlArgs=reqURI.split("/");
		System.out.println("Method:"+request.getMethod());
		for(String arg:urlArgs){
			System.out.println("url参数:"+arg);
		}
		String reMsg="";
		try {
			Class classX=Class.forName("cn.com.test."+urlArgs[1]+"Service");
			try {
				reMsg=(String) classX.getDeclaredMethod(urlArgs[2], new Class[]{String.class}).invoke(classX.newInstance(), new Object[]{jsonStr});
				//Method me=classX.getDeclaredMethod(urlArgs[2], new Class[]{String.class});
				//me.invoke(classX.newInstance(), new Object[]{jsonStr});
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			} catch (InstantiationException e) {
				e.printStackTrace();
			}
		} catch (NoSuchMethodException | SecurityException | ClassNotFoundException e) {
			e.printStackTrace();
		}
		PrintWriter out = response.getWriter();
		//out.write(request.getRequestURI()+"@@@"+request.getParameter("jsonStr"));
		out.write(reMsg);
		out.flush();
		out.close();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("doGet方法");
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("doPost方法");
		doGet(request, response);
	}

	/**
	 * @see HttpServlet#doPut(HttpServletRequest, HttpServletResponse)
	 */
	protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("doPut方法");
	}

	/**
	 * @see HttpServlet#doDelete(HttpServletRequest, HttpServletResponse)
	 */
	protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("doDelete方法");
	}

	/**
	 * @see HttpServlet#doHead(HttpServletRequest, HttpServletResponse)
	 */
	protected void doHead(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("doHead方法");
	}

	/**
	 * @see HttpServlet#doOptions(HttpServletRequest, HttpServletResponse)
	 */
	protected void doOptions(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("doOptions方法");
	}

	/**
	 * @see HttpServlet#doTrace(HttpServletRequest, HttpServletResponse)
	 */
	protected void doTrace(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("doTrace方法");
	}

}
