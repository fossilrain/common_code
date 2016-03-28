package cn.com.test;

public class RestTestService {

	public String testMethod(String jsonStr){
		System.out.println("in..."+jsonStr);
		return "retCode_jsonStr";
	}
}
