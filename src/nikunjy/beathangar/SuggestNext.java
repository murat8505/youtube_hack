package nikunjy.beathangar;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import nikunj.beathangar.ml.api.SuggestionUtils;
import nikunj.beathangar.model.Video;

import com.google.gson.Gson;

public class SuggestNext extends HttpServlet {
	public void service(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		String userId = req.getParameter("user");
		if (userId == null) { 
			HttpSession session = req.getSession();
			userId = (String)session.getAttribute("userid");
		}
		Video video = SuggestionUtils.suggestNext(userId);
		Gson gson = new Gson();
		res.getWriter().println(gson.toJson(video));
	}
}
