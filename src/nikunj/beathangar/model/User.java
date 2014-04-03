package nikunj.beathangar.model;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.googlecode.objectify.annotation.Cache;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import com.googlecode.objectify.annotation.Load;

@Entity
@Cache
public class User {
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public void setEmail(String email) { 
		this.email = email;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}
	public void setAboutInfo(String aboutInfo) {
		this.aboutInfo = aboutInfo;
	}
	public String getUserId() {
		return userId;
	}
	public String getFirstName() {
		return firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public String getMiddleName() {
		return middleName;
	}
	public String getAboutInfo() {
		return aboutInfo;
	}
	public String getEmail() { 
		return email;
	}
	public String getProfilePic() {
		return pictureUrl;
	}
	public void setProfilePic(String url) { 
		this.pictureUrl = url;
	}
	public Date getCreationDate() {
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
		try { 
			Date ret = formatter.parse(formatter.format(creationDate));
			return ret;
		} catch(Exception e) { 
			return creationDate;
		}	
	}
	public List<String> getMusic()   { 
		return music;
	}
	public void setMusic(List<String> music) { 
		this.music = music;
	}
	@Id
	String userId;
	@Load List<String> music;
	String pictureUrl;
	@Index String firstName; 
	@Index String lastName; 
	String middleName;
	@Index String email;
	String aboutInfo; 
	Date creationDate;
	private User() { 
	}	
	public User(String userId) { 
		this.userId = userId;
		creationDate = new Date();
		music = new ArrayList<String>();
	}

	

}
