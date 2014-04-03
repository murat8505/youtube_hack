package nikunj.beathangar.model;

import static com.googlecode.objectify.ObjectifyService.ofy;

import java.util.List;

import com.googlecode.objectify.Key;
public class DataBaseUtils {
	public static User getUser(String userId) { 
		User user = null;
		user = MemcacheUtils.getUser(userId); 
		if (user != null) { 
			return user;
		}
		Key<User> userKey = Key.create(User.class,userId);
		user = ofy().load().key(userKey).getValue();
		if (user != null) { 
			MemcacheUtils.putUser(user);
		}
		return user;		
	}
	public static void saveUser(User user) { 
		MemcacheUtils.putUser(user);
		ofy().save().entity(user);
	}
	public static boolean hasSongBeenPlayed(String userId, String artist, String track) {
		List <UserHistory> history = 
				ofy().load().type(UserHistory.class).filter("userId",userId).filter("artist",artist).
				filter("track", track).list();
		return history.size() >= 1;
		
	}
	public static Artist getArtist(String artistName) { 
		List <Artist> result = 
				ofy().load().type(Artist.class).filter("name",artistName).list();
		if (result.size() == 0) { 
			return null;
		}
		return result.get(0);
	}
	public static Album getAlbumByName(String albumName) { 
		List <Album> result = 
				ofy().load().type(Album.class).filter("name",albumName).list();
		if (result.size() == 0) { 
			return null;
		}
		return result.get(0);
	}
	public static Album getAlbum(String id) { 
		Album result =  
				ofy().load().type(Album.class).id(id).get();
		return result;
	}
}
