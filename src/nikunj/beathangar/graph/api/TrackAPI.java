package nikunj.beathangar.graph.api;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import nikunj.beathangar.model.Track;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class TrackAPI {
	public String url = "/track/search";
	public String limit; 
	public String artist_name;
	public String composed_by; 
	public String featuring_artist;
	public String lyrics_keywords;
	public String name;
	public String decade;
	public String genre;
	public Track[] execute() throws Exception { 		
		String result = QueryUtils.execute(this);
		Gson gson = new Gson();
		JsonParser parser = new JsonParser();
		JsonObject o = (JsonObject)parser.parse(result);
		JsonArray arr = o.getAsJsonArray("data");
		Track[] tracks = gson.fromJson(arr, Track[].class);
		System.out.println(tracks[0]);
		return tracks;
	}
}
