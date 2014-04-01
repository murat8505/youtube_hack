package nikunj.beathangar.ml.api;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import nikunj.beathangar.graph.api.ArtistAPI;
import nikunj.beathangar.graph.api.MapUtils;
import nikunj.beathangar.model.Artist;

class ValueComparator implements Comparator<String> {
    Map<String, Integer> base;
    public ValueComparator(Map<String, Integer> base) {
        this.base = base;
    }
    // Note: this comparator imposes orderings that are inconsistent with equals.    
    public int compare(String a, String b) {
        if (base.get(a) >= base.get(b)) {
            return 1;
        } else {
            return -1;
        }
    }
}
public class ArtistUtils {
	public static Map<String, Integer> getArtistsWithConfidence(List<String> artists) { 
		Map<String, Integer> foundArtistsMap = new HashMap<String, Integer>();
		List<Artist> foundArtists = new ArrayList<Artist>();
		for (String artist : artists) {
			ArtistAPI api = new ArtistAPI();
			api.name = artist;
			Artist[] results = null;
			try { 
				results = api.execute();
			} catch(Exception e) { 
				//
			}
			if (results!=null && results.length >= 1) { 
				foundArtistsMap.put(results[0].name, 1);
				foundArtists.add(results[0]);
			}
		}
		for (Artist artist : foundArtists) {
			Artist[] results = null; 
			ArtistAPI api = new ArtistAPI();
			api.similar_to = artist.name;
			try { 
				results = api.execute();
			} catch(Exception e) { 
				//
			}
			if (results != null) { 
				for (Artist related : results) { 
					if (foundArtistsMap.containsKey(related.name)) {
						int score = foundArtistsMap.get(related.name);
						foundArtistsMap.put(related.name, score+1);
					} else { 
					}
				}
			}
		}
		for (String artist : foundArtistsMap.keySet()) { 
			System.out.println(artist + " " + foundArtistsMap.get(artist));
			
		}
        Map<String, Integer> result = MapUtils.sortByValue(foundArtistsMap);
        return result;
	}
}
