package nikunj.beathangar.model;
import java.util.ArrayList;
import java.util.List;

import nikunj.beathangar.graph.api.QueryUtils;

import com.googlecode.objectify.annotation.Cache;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

@Entity
@Cache
public class Artist {
	@Id public String id; 
	public String main_genre;
	@Index public String name;
	@Index public List<String> albums;
	@Index public List<String> albumNames;
	Artist() { 
		albums = new ArrayList<String>();
		albumNames = new ArrayList<String>();
	}
	public String artist_ref_id;
	public String toString() { 
		return QueryUtils.print(this);
	}
}
