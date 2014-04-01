package nikunj.beathangar.model;

import static com.googlecode.objectify.ObjectifyService.ofy;

import java.util.List;
public class DataBaseUtils {
	public static User getUser(String userId) { 
		User user = null; 
		user = ofy().load().type(User.class).id(userId).get();
		return user;		
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
