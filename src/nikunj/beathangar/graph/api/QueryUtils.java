package nikunj.beathangar.graph.api;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

public class QueryUtils {
	public static String DEVELOPER_KEY = "c26e63de67a52b71cfcb1b2fb63a14f2";
	public static String HOST_NAME = "http://api.musicgraph.com/api/v1";
	public static String getQuery(Object obj) {
		Field[] fields = obj.getClass().getDeclaredFields();
		String result = "";
		ArrayList<String> ret = new ArrayList<String>();
		try {
			for (Field field : fields) {
				if (field.getName().equalsIgnoreCase("url")) { 
					continue;
				}
				if (field.get(obj) !=null && !field.get(obj).toString().isEmpty()) {
					ret.add(field.getName()+"="+URLEncoder.encode(field.get(obj).toString()));
				}
			}
		}catch(Exception e) { 

		}
		if (ret.size() == 0) { 
			return null;
		}
		result = ret.get(0); 
		for (int j = 1; j < ret.size(); j++) { 
			result+="&"+ret.get(j);
		}
		return result;
	}
	public static String print(Object obj) {
		Field[] fields = obj.getClass().getDeclaredFields();
		String result = "";
		ArrayList<String> ret = new ArrayList<String>();
		try {
			for (Field field : fields) {
				if (field.get(obj) !=null && !field.get(obj).toString().isEmpty()) {
					ret.add(field.getName()+"="+field.get(obj).toString());
				}
			}
		}catch(Exception e) { 

		}
		if (ret.size() == 0) { 
			return null;
		}
		result = ret.get(0); 
		for (int j = 1; j < ret.size(); j++) { 
			result+=" "+ret.get(j);
		}
		return result;
	}
	public static String execute(Object obj) throws Exception { 
		String query = QueryUtils.getQuery(obj);
		if (query == null || query.isEmpty()) { 
			throw new Exception("Malformed query");
		}
		Field field = obj.getClass().getDeclaredField("url");
		String url = field.get(obj).toString();
		query = QueryUtils.HOST_NAME+url+"?api_key="+QueryUtils.DEVELOPER_KEY+"&"+query;
		System.out.println(query);
		URL uri;
		HttpURLConnection conn;
		BufferedReader rd;
		String line;
		String result = "";
		try {
			uri = new URL(query);
			conn = (HttpURLConnection) uri.openConnection();
			conn.setRequestMethod("GET");
			rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			while ((line = rd.readLine()) != null) {
				result += line;
			}
			rd.close();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
}
