package nikunj.beathangar.graph.api;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.net.HttpURLConnection;
import java.net.URL;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import nikunj.beathangar.model.Track;

public class IDAPI {
	public String id;
	public String getQuery() { 
		return "http://api.musicgraph.com/api/v1/"+id+"/tracks?api_key="+QueryUtils.DEVELOPER_KEY;
	}
	public Track[] execute() throws Exception { 
		String query = getQuery();
		if (query == null || query.isEmpty()) { 
			throw new Exception("Malformed query");
		}
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
		Gson gson = new Gson();
		JsonParser parser = new JsonParser();
		JsonObject o = (JsonObject)parser.parse(result);
		JsonArray arr = o.getAsJsonArray("data");
		Track[] tracks = gson.fromJson(arr, Track[].class);
		return tracks;
	}
}
