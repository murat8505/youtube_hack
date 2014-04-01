package nikunj.beathangar.ml.api;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import nikunj.beathangar.graph.api.AlbumAPI;
import nikunj.beathangar.graph.api.ArtistAPI;
import nikunj.beathangar.model.Album;
import nikunj.beathangar.model.Artist;

public class RegexMatchHardCode {
	public class RegexResult {
		public double[] prob;
		public String artist_name; 
		public String track_name; 
		public String album_name;
		public String related_to;
		public String genre;
		public long decade;
		RegexResult() { 
			prob = new double[3];
		}
		public String toString() { 
			return prob[0]+" "+prob[1]+" "+prob[2]+"\n"+ artist_name +"  "+track_name+" "+album_name+" "+related_to+" "+genre+" "+decade;
		}
	};
	public String keyWords[] = { "play", "song", "songs", "title", "number", "track", "tracks", "album", "artist", "from", "by", "before", "after", "related", "similar", "inspired", "like", "sound like", "sound similar", "during", "rock", "blues", "classical", "jazz", "country"};
	public String relationKeyWords[] = {"related", "similar", "inspired", "like", "sound like", "sound similar" };
	public String timeWords[] = {"from", "by", "before", "after", "during"};
	public String trackWords[] = {"song", "songs", "title", "track", "number"};
	public String artistWords[] = {"from", "by", "artist"};
	public String albumWords[] = {"from", "album"};
			
	public String genres[] = {"rock", "blues", "classical", "jazz", "country"};
	public int lastIndex(int l_index, String query) {
		int max_index = l_index;
		for (String key : keyWords) { 
			int index = query.indexOf(key);
			max_index = (max_index > index)?max_index:index;
		}
		if (max_index == l_index) { 
			return query.length();
		}
		return max_index;
	}
	public String extractAlbum(String query) { 
		boolean found = false;
		for ( String key : albumWords) { 
			if (query.contains(key)) { 
				found = true;
			}
		}
		if (!found) { 
			return null;
		}  
		String decade_result = "-100000";
		long temp = extractTimeFrame(query);
		if (temp!= -1) { 
			decade_result = Long.toString(temp);
		}
		int max_index = -1;
		int size = -1;
		
		for (String key : albumWords) {
			int index = query.lastIndexOf(key );
			int t_size = key.length();
			if (max_index < index ) { 
				max_index = index;
				size = t_size;	
			} else if (max_index == index) { 
				size = t_size;
			}
			
		}
		String ans = query.substring(max_index + size, lastIndex(max_index, query)).trim();
		if (ans.contains(decade_result)) { 
			return null;
		} 
		return ans;
		
	}
	public String extractGenre(String query) { 
		for ( String key : genres) { 
			if (query.contains(key)) { 
				return key;
			}
		}
		return null;
	}
	public String extractTrackName(String query) {
		String words[] = query.split("\\s+");
		Set<String> keys = new HashSet<String>();
		for (String key : keyWords)
			keys.add(key);
		String ret = "";
		for (String word : words) {
			if (!keys.contains(word)) {
				ret += word;
			}
		}
		if (ret.isEmpty()) { 
			return null;
		}
		return ret;
	}
	public String extractRelatedTo(String query) { 
		boolean found = false;
		for ( String key : relationKeyWords) { 
			if (query.contains(key)) { 
				found = true;
			}
		}
		if (!found) { 
			return null;
		}
		int max_index = -1;
		int size = -1;
		String shitWords[] = {"", " to", " with", " by"};
		for (String key : relationKeyWords) {
			for ( String aux : shitWords) { 
				int index = query.lastIndexOf(key + aux);
				int t_size = key.length() + aux.length();
				if (max_index < index ) { 
					max_index = index;
					size = t_size;	
				} else if (max_index == index) { 
					size = t_size;
				}
			}
		}
		return query.substring(max_index + size, lastIndex(max_index, query)).trim();
	}
	public String extractArtist(String query) {
		boolean found = false;
		for ( String key : artistWords) { 
			if (query.contains(key)) { 
				found = true;
			}
		}
		if (!found) { 
			return null;
		}	
		String decade_result = "-100000";
		long temp = extractTimeFrame(query);
		if (temp!= -1) { 
			decade_result = Long.toString(temp);
		}
		int max_index = -1;
		int size = -1;
		for (String key : artistWords) { 
			int index = query.lastIndexOf(key);
			if (max_index < index) { 
				max_index = index;
				size = key.length();
			}
		}
		String ans = query.substring(max_index + size, lastIndex(max_index, query)).trim();
		if (ans.contains(decade_result)) { 
			return null;
		} 
		return ans;
	}
	public long extractTimeFrame(String query) {
		boolean found = false;
		Pattern p = Pattern.compile("-?\\d+");
		{
			Matcher m = p.matcher(query);
			if(!m.find()) { 
				return -1;
			}
		}
		for ( String key : timeWords) { 
			if (query.contains(key)) { 
				found = true;
			}
		}
		if (!found) { 
			return -1;
		}
		Matcher m = p.matcher(query);
		while (m.find()) {
		  return new Long(m.group());
		}
		return -1;
	}
	public Artist[] getArtist(RegexResult result) throws Exception {  
			ArtistAPI api = new ArtistAPI();
			api.limit="5";
			if (result.artist_name != null) { 
				api.name = result.artist_name;
			}
			if (result.decade != -1) { 
				//api.decade = Long.toString(result.decade);
			}
			if (result.related_to != null) {
				api.similar_to = result.related_to;

			}
			return api.execute();
		
	}
	public Album[] getAlbum(RegexResult result) throws Exception {  
		AlbumAPI api = new AlbumAPI();
		api.limit="5";
		if (result.album_name != null) { 
			api.name = result.album_name;
		}
		if (result.artist_name != null && !result.artist_name.equalsIgnoreCase(result.album_name)) { 
			api.artist_name = result.artist_name;
		}
		if (result.decade != -1) { 
			api.decade = Long.toString(result.decade);
		}
		if (result.related_to != null) {
			api.similar_to = result.related_to;

		}
		return api.execute();
	
	}
	public void calculateProbabilities(RegexResult result) { 
		if (result.artist_name != null || result.related_to != null) { 
			result.prob[2] = 1.0;
		}
		if (result.album_name != null) { 
			result.prob[1] = 1.0;
			return;
		}
		result.prob[0] = 1.0;
	}
	public RegexResult process(String query) { 
		RegexResult result = new RegexResult();
		result.decade = extractTimeFrame(query);
		result.artist_name = extractArtist(query);
		result.related_to = extractRelatedTo(query);
		result.album_name = extractAlbum(query);
		result.genre = extractGenre(query);
		result.track_name = extractTrackName(query);
		calculateProbabilities(result);
		System.out.println(result);
		return result;

	}
	/*public static void main(String args[]) { 
		System.out.println(new RegexMatchHardCode().extractRelatedTo("song related to Pink floyd"));
	}*/
	
}
