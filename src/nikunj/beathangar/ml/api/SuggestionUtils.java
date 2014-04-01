package nikunj.beathangar.ml.api;

import static com.googlecode.objectify.ObjectifyService.ofy;

import java.util.ArrayList;
import java.util.List;

import nikunj.beathangar.graph.api.AlbumAPI;
import nikunj.beathangar.graph.api.ArtistAPI;
import nikunj.beathangar.graph.api.IDAPI;
import nikunj.beathangar.model.Album;
import nikunj.beathangar.model.Artist;
import nikunj.beathangar.model.DataBaseUtils;
import nikunj.beathangar.model.Track;
import nikunj.beathangar.model.User;
import nikunj.beathangar.model.Video;
import nikunj.beathangar.youtube.youtubesearch;

public class SuggestionUtils {
	public static <T> T getRandom(List<T> bag) {
		if (bag == null || bag.isEmpty()) { 
			return null;
		}
		int random = (int)(Math.random() * bag.size());
		return bag.get(random);

	}
	public static void fetchArtist(String artist) { 
		ArtistAPI artistAPI = new ArtistAPI(); 
		artistAPI.name = artist;
		try { 
			Artist[] artists = artistAPI.execute();
			Artist temp = artists[0];
			if (temp == null) { 
				System.out.println("API could not fetch artist");
				System.out.println(artistAPI.toString());
			}
			ofy().save().entity(temp).now();
		} catch(Exception e) { 

		}
	}
	public static void fetchAlbums(Artist artist) {
		AlbumAPI albumApi = new AlbumAPI();
		albumApi.artist_name = artist.name;
		Album[] albums = null;
		try {
			albums = albumApi.execute();
			List<String> albumIds = new ArrayList<String>();
			List<String> albumNames = new ArrayList<String>();
			if (albums == null || albums.length == 0) { 
				System.out.println("Could not find any songs for the artist" + artist.name);
			}
			for (Album album : albums) { 
				albumIds.add(album.id);
				albumNames.add(album.title);
			}
			artist.albums = albumIds;
			artist.albumNames = albumNames;
			ofy().save().entity(artist);
			for (Album album : albums) {
				if (DataBaseUtils.getAlbum(album.id) == null) { 
					ofy().save().entity(album).now();
				}
			}
		} catch(Exception e) {
		}
	}
	public static void fetchTracks(Album album) { 
		IDAPI idapi = new IDAPI();
		idapi.id = album.id;
		try { 
			Track[] tracks = idapi.execute();
			List<String> albumTracks = new ArrayList<String>();
			for (Track track : tracks) { 
				albumTracks.add(track.title);
			}
			album.tracks = albumTracks;
			ofy().save().entity(tracks);
		}catch(Exception e) {

		}

	}
	public static Video suggestNext(String userId) {
		User user = DataBaseUtils.getUser(userId);
		String temp = getRandom(user.getMusic());
		System.out.println(temp);
		Artist artist = DataBaseUtils.getArtist(temp);
		if (artist == null) { 
			fetchArtist(temp);
			artist = DataBaseUtils.getArtist(temp);
		}
		if (artist == null) { 
			System.out.println("Artist is null");
			Video randomVideo = getRandom(youtubesearch.get(temp));
			return randomVideo;
		}
		if (artist.albums == null || artist.albums.isEmpty()) { 
			fetchAlbums(artist);
		}
		String randomAlbum = getRandom(artist.albums);
		if (randomAlbum == null || randomAlbum.isEmpty()) { 
			Video randomVideo = getRandom(youtubesearch.get(temp));
			return randomVideo;
		}
		Album dbAlbum = DataBaseUtils.getAlbum(randomAlbum) ;
		if ( dbAlbum.tracks == null || dbAlbum.tracks.isEmpty()) {
			fetchTracks(dbAlbum);
		}
		System.out.println(dbAlbum.title);
		String randomSong = getRandom(dbAlbum.tracks);
		if (randomSong == null) { 
			randomSong = "";
		}
		Video randomVideo = getRandom(youtubesearch.get(randomSong + " " + temp));
		return randomVideo;
	}
}
