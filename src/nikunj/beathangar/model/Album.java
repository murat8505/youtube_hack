package nikunj.beathangar.model;

import nikunj.beathangar.graph.api.QueryUtils;

public class Album {
	public String id; 
	public String title; 
	public String number_of_tracks;
	public String main_genre;
	public String performer_name;
	public String decade;
	public String release_date;
	public String toString() { 
		return QueryUtils.print(this);
	}
}
