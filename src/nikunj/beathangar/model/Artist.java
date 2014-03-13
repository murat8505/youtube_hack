package nikunj.beathangar.model;

import nikunj.beathangar.graph.api.QueryUtils;

public class Artist {
	public String id; 
	public String main_genre;
	public String name;
	public String artist_ref_id;
	public String toString() { 
		return QueryUtils.print(this);
	}
}
