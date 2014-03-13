package nikunjy.beathangar;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nikunj.beathangar.graph.api.IDAPI;
import nikunj.beathangar.graph.api.TrackAPI;
import nikunj.beathangar.ml.api.RegexMatchHardCode;
import nikunj.beathangar.model.Album;
import nikunj.beathangar.model.Artist;
import nikunj.beathangar.model.Track;
import nikunj.beathangar.model.Video;
import nikunj.beathangar.youtube.youtubesearch;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;

@SuppressWarnings("serial")
public class BeathangarServlet extends HttpServlet {
	public void removeExtra(Track track) {
		String title = track.title;
		String [] aux = {"(", "["};
		for (String key : aux ) {
			if (title.contains(key)) {
				track.title = title.substring(0, title.indexOf(key));
			}
		}
	}
	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		resp.setContentType("text/plain");
		try {
			String query = req.getParameter("searchstring");
			RegexMatchHardCode obj = new RegexMatchHardCode();
			RegexMatchHardCode.RegexResult result = obj.process(query);
			Track[] tracks = null;
			if (result.prob[1] == 1.0) { 
				Album[] albums = obj.getAlbum(result);
				System.out.println("Albums found "+albums.length+" ");
				if (albums.length > 0) {
					IDAPI idapi = new IDAPI(); 
					idapi.id = albums[0].id;
					tracks = idapi.execute();
				}
			} if (result.prob[2] == 1.0 && (tracks == null || tracks.length == 0)) { 
				Artist[] artists = obj.getArtist(result);
				TrackAPI api = new TrackAPI();  
				api.artist_name = artists[0].name;
				if (result.decade != -1) {
					api.decade = Long.toString(result.decade).trim();
					api.limit = "5";
				}
				tracks = api.execute();
			} else if(tracks == null) {
				TrackAPI api = new TrackAPI();
				api.limit = "5";
				api.genre = result.genre;
				api.name = result.track_name;
				api.artist_name = result.artist_name;
				if (api.artist_name!=null && api.artist_name.equalsIgnoreCase(api.name)) { 
					api.artist_name = null;
				}
				if (result.decade !=-1) {
					api.decade = Long.toString(result.decade);
				}
				System.out.println(api);
				tracks = api.execute();
			}
			resp.setContentType("application/json");
			if (tracks == null) { 
				return;
			}
			List<Video> videos = new ArrayList<Video>();
			for (int i = 0; i < 5; i++) {
				System.out.println(tracks[i]);
				List<Video> temp = youtubesearch.get(tracks[i].title);
				if(temp.size() == 0 || temp == null)  
					continue;
				int size = temp.size(); 
				if (size > 2) { 
					size = 2;
				}
				for (int j = 0; j < size; j++) { 
					videos.add(temp.get(j));
				}
			}
			Gson gson = new Gson();
			JsonElement element = gson.toJsonTree(videos, new TypeToken<List<Video>>() {}.getType());

			if (! element.isJsonArray()) {
				// fail appropriately
				throw new Exception("ERROR converting to json");
			}
			JsonArray jsonArray = element.getAsJsonArray();
			
			resp.getWriter().println(jsonArray.toString());
		} catch(Exception e) { 
			System.out.println(e);
		}

	}
}
