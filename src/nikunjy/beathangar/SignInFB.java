package nikunjy.beathangar;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import nikunj.beathangar.ml.api.ArtistUtils;
import nikunj.beathangar.model.Album;
import nikunj.beathangar.model.Artist;
import nikunj.beathangar.model.DataBaseUtils;
import nikunj.beathangar.model.MemcacheUtils;
import nikunj.beathangar.model.User;
import nikunj.beathangar.model.UserHistory;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.googlecode.objectify.ObjectifyService;

public class SignInFB extends HttpServlet {
	static {
		ObjectifyService.register(User.class);
		ObjectifyService.register(Artist.class);
		ObjectifyService.register(Album.class);
		ObjectifyService.register(UserHistory.class);
		MemcacheUtils.init();
	}
	public void service(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {            
		String code = req.getParameter("code");
		if (code == null || code.equals("")) {
			req.setAttribute("message","Invalid response sent from facebook. Sorry!");
			RequestDispatcher view = req.getRequestDispatcher("error.jsp");
			try {
			view.forward(req, res);
			} catch(Exception e) { 
				e.printStackTrace();
			}
			return;
		}
		String token = null;
		try {
			String g = "https://graph.facebook.com/oauth/access_token?client_id=1422564348001086&redirect_uri=" + URLEncoder.encode(GlobalConstants.FB_URL, "UTF-8") + "&client_secret=4c0ab29302168ab0db95a654a1a51947&code=" + code;
			URL u = new URL(g);
			URLConnection c = u.openConnection();
			BufferedReader in = new BufferedReader(new InputStreamReader(c.getInputStream()));
			String inputLine;
			StringBuffer b = new StringBuffer();
			while ((inputLine = in.readLine()) != null)
				b.append(inputLine + "\n");            
			in.close();
			token = b.toString();
			if (token.startsWith("{"))
				throw new Exception("error on requesting token: " + token + " with code: " + code);
		} catch (Exception e) {
			// an error occurred, handle this
		}
		String graph = null;
		List<String> music = new ArrayList<String>();
		try {
			String g = "https://graph.facebook.com/me?fields=id,email,first_name,last_name,music.limit(100)&" + token;
			System.out.println(g);
			URL u = new URL(g);
			URLConnection c = u.openConnection();
			BufferedReader in = new BufferedReader(new InputStreamReader(c.getInputStream()));
			String inputLine;
			StringBuffer b = new StringBuffer();
			while ((inputLine = in.readLine()) != null)
				b.append(inputLine + "\n");            
			in.close();
			System.out.println(b.toString());
			graph = b.toString();
		} catch (Exception e) {
		}

		String facebookId = "";
		String firstName = "";
		String middleNames = "";
		String lastName = "";
		String email = "";
		String pictureUrl = "#";
		try {
			JSONObject json = new JSONObject(graph);
			facebookId = json.getString("id");
			firstName = json.getString("first_name");
			if (json.has("middle_name"))
				middleNames = json.getString("middle_name");
			else
				middleNames = null;
			if (middleNames != null && middleNames.equals(""))
				middleNames = null;
			lastName = json.getString("last_name");
			email = json.getString("email");
			pictureUrl ="https://graph.facebook.com/"+facebookId+"/picture?type=large";
			JSONArray musicArray = json.getJSONObject("music").getJSONArray("data");
			
			for (int i = 0; i < musicArray.length(); i++) { 
				JSONObject object = musicArray.getJSONObject(i);
				music.add(object.getString("name"));
			}
		} catch (JSONException e) {
			System.out.println(e);
		}
		HttpSession session = req.getSession();
		session.setAttribute("userid",facebookId);
		res.setContentType("text/plain");
		User foundUser = DataBaseUtils.getUser(facebookId);
		List<String> userArtists = new ArrayList<String>();
		if (foundUser == null) {
			Map<String,Integer> artists = ArtistUtils.getArtistsWithConfidence(music);
			userArtists = new ArrayList<String>();
			for (String a : artists.keySet()) {
				userArtists.add(a);
			}
		}
		if(foundUser == null) {
			User user = new User(facebookId);
			user.setUserId(facebookId);
			user.setFirstName(firstName);
			user.setLastName(lastName);
			user.setMiddleName(middleNames);
			user.setEmail(email);
			user.setProfilePic(pictureUrl);
			user.setMusic(userArtists);
			DataBaseUtils.saveUser(user);
			req.setAttribute("musicList",user.getMusic());
			req.setAttribute("name", user.getFirstName());
			req.setAttribute("image", user.getProfilePic());
		} else {
			req.setAttribute("name", foundUser.getFirstName());
			req.setAttribute("musicList",foundUser.getMusic());
			req.setAttribute("image", foundUser.getProfilePic());
		}
		RequestDispatcher view = req.getRequestDispatcher("player.jsp");
		view.forward(req, res);
	}
}