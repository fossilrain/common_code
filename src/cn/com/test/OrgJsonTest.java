package cn.com.test;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

public class OrgJsonTest {

	public static void main(String[] args) throws JSONException {
		JSONObject obj=new JSONObject();

		obj.put("name", "");
		obj.put("id", "100");
		
		JSONObject obj1=new JSONObject();
		obj1.put("A", "");
		obj1.put("B", "xxx");
		obj1.put("C", "");
		
		obj.put("LETTER", obj1);
		
		JSONArray ja=new JSONArray();
		ja.put(obj1);
		ja.put(obj1);
		obj.put("array", ja);
		
		JSONArray ja1=new JSONArray();
		ja1.put("s1");
		ja1.put("s2");
		obj.put("array1", ja1);
		
		JSONObject obj2=new JSONObject();
		obj2.put("sss", ja);
		System.out.println(ja);
		System.out.println(obj2);
		
		
		
		System.out.println(obj.toString());
		System.out.println(obj);
		
		
		JSONObject j=new JSONObject(new JSONTokener(obj.toString()));
		System.out.println(j.getString("id"));
		System.out.println(j.getJSONObject("LETTER").getString("B"));
		JSONObject j1=new JSONObject(new JSONTokener(obj2.toString()));
		System.out.println(j1.getJSONArray("sss").getJSONObject(1).getString("A"));
		JSONArray j2=new JSONArray(ja);
		System.out.println(j2.getJSONObject(0).getString("B"));
		
		
	}

}
