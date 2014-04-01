package nikunj.beathangar.graph.api;
import nikunj.beathangar.model.Artist;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class ArtistAPI {
	public String url = "/artist/search";
	public String limit; 
	public String name;
	public String similar_to;
	public String influenced_by;
	public String genre; 
	public String decade;
	public Artist[] execute() throws Exception { 		
		String result = QueryUtils.execute(this);
		Gson gson = new Gson();
		JsonParser parser = new JsonParser();
		JsonObject o = (JsonObject)parser.parse(result);
		JsonArray arr = o.getAsJsonArray("data");
		Artist[] artists = gson.fromJson(arr, Artist[].class);
		System.out.println(artists[0]);
		return artists;
	}
	
}
