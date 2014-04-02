package nikunj.beathangar.model;
import java.util.ArrayList;
import java.util.List;

import nikunj.beathangar.graph.api.QueryUtils;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import com.googlecode.objectify.annotation.Load;
@Entity
public class Album {
	@Id public String id; 
	@Index public String title;
	@Load public List<String> tracks;
	public String number_of_tracks;
	public String main_genre;
	public String performer_name;
	public String decade;
	public String release_date;
	Album() { 
		tracks = new ArrayList<String>();
	}
	public String toString() { 
		return QueryUtils.print(this);
	}
}
