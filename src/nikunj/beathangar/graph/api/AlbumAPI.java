package nikunj.beathangar.graph.api;

import java.lang.reflect.Field;
import java.net.URLEncoder;
import java.util.ArrayList;

import nikunj.beathangar.model.Album;
import nikunj.beathangar.model.Track;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class AlbumAPI {
	public String url = "/album/search";
	public String limit; 
	public String name;
	public String artist_name;
	public String similar_to;
	public String genre; 
	public String decade;
	public Album[] execute() throws Exception { 		
		String result = QueryUtils.execute(this);
		Gson gson = new Gson();
		JsonParser parser = new JsonParser();
		JsonObject o = (JsonObject)parser.parse(result);
		JsonArray arr = o.getAsJsonArray("data");
		Album[] albums = gson.fromJson(arr, Album[].class);
		return albums;
	}
}
