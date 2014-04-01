package nikunj.beathangar.model;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
@Entity
public class UserHistory {
	@Id
	public String userId;
	@Index public String artist;
	@Index public String track;
}
