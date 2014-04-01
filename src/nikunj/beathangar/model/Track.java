package nikunj.beathangar.model;

import nikunj.beathangar.graph.api.QueryUtils;

public class Track {
	public String id; 
	public String duration; 
	public String title; 
	public String artist_name; 
	public String track_album_id; 
	public String track_artist_id; 
	public String album_title;
	public String decade;
	public String toString() { 
		return QueryUtils.print(this);
	}
}
