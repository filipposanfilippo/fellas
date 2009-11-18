package fellas;

import java.io.Serializable;

public class User implements Serializable {
	int id;
	String uTel;
	String uName;
	String uAge;
	String uSex;
	String uStatus;
	String username;
	String psw;
	String uSurname;
	String uLocation;
	String uImageURL;
	int privacy;

	public User(int id, String uTel, String uName, String uAge, String uSex,
			String uStatus, String username, String psw, String uSurname,
			String uLocation, String uImageURL, int privacy) {
		super();
		this.id = id;
		this.uTel = uTel;
		this.uName = uName;
		this.uAge = uAge;
		this.uSex = uSex;
		this.uStatus = uStatus;
		this.username = username;
		this.psw = psw;
		this.uSurname = uSurname;
		this.uLocation = uLocation;
		this.uImageURL = uImageURL;
		this.privacy = privacy;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getuTel() {
		return uTel;
	}

	public void setuTel(String uTel) {
		this.uTel = uTel;
	}

	public String getuName() {
		return uName;
	}

	public void setuName(String uName) {
		this.uName = uName;
	}

	public String getuAge() {
		return uAge;
	}

	public void setuAge(String uAge) {
		this.uAge = uAge;
	}

	public String getuSex() {
		return uSex;
	}

	public void setuSex(String uSex) {
		this.uSex = uSex;
	}

	public String getuStatus() {
		return uStatus;
	}

	public void setuStatus(String uStatus) {
		this.uStatus = uStatus;
	}

	public String getusername() {
		return username;
	}

	public void setusername(String username) {
		this.username = username;
	}

	public String getPsw() {
		return psw;
	}

	public void setPsw(String psw) {
		this.psw = psw;
	}

	public String getuSurname() {
		return uSurname;
	}

	public void setuSurname(String uSurname) {
		this.uSurname = uSurname;
	}

	public String getuLocation() {
		return uLocation;
	}

	public void setuLocation(String uLocation) {
		this.uLocation = uLocation;
	}

	public String getuImageURL() {
		return uImageURL;
	}

	public void setuImageURL(String uImageURL) {
		this.uImageURL = uImageURL;
	}

	public int getPrivacy() {
		return privacy;
	}

	public void setPrivacy(int privacy) {
		this.privacy = privacy;
	}

}
