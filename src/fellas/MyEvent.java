package fellas;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;

public class MyEvent implements Serializable {
	// ___DB access
	final String dbUser = "diana";
	final String dbPass = "password1234";
	final String dbHost = "db4free.net";
	final String dbDriver = "com.mysql.jdbc.Driver";
	final String dbUrl = "jdbc:mysql://db4free.net:3306/";
	
	private int id;
	private int cId;
	private String eName;
	private String eShortDescription;
	private String eLongDescription;
	private String eLocation;
	private String eCategory;
	private Date eStartDate;
	private Date eFinishDate;
	private String eStartTime;
	private String eFinishTime;
	private String eRestriction;
	private String eInfoTel;
	private String eImageURL;
	private String clubName;

	public MyEvent(int id, int cId, String eName, String eShortDescription,
			String eLongDescription, String eLocation, String eCategory,
			Date eStartDate, Date eFinishDate, String eStartTime,
			String eFinishTime, String eRestriction, String eInfoTel,
			String eImageURL) throws SQLException {
		super();
		String query="";
		Connection connection = null;
		Statement statement = null;
		ResultSet rs = null;
		this.id = id;
		this.cId = cId;
		this.eName = eName;
		this.eShortDescription = eShortDescription;
		this.eLongDescription = eLongDescription;
		this.eLocation = eLocation;
		this.eCategory = eCategory;
		this.eStartDate = eStartDate;
		this.eFinishDate = eFinishDate;
		this.eStartTime = eStartTime;
		this.eFinishTime = eFinishTime;
		this.eRestriction = eRestriction;
		this.eInfoTel = eInfoTel;
		this.eImageURL = eImageURL;
		try{
			Class.forName(dbDriver);
			connection = DriverManager.getConnection(dbUrl, dbUser, dbPass);// diana4free
			connection.setCatalog("diana");
			query = "SELECT cName FROM clubs WHERE cId=" + cId;
			statement = connection.createStatement();
			rs = statement.executeQuery(query);
			if(rs.next())
				this.clubName = rs.getString("cName");
			else
				this.clubName = "";
			connection.close();
		}catch(ClassNotFoundException e ){
			e.printStackTrace();
			this.clubName = "";
			connection.close();
		}
	}

	public MyEvent() {
		super();
		// TODO Auto-generated constructor stub
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}

	public void setcId(int cId) {
		this.cId = cId;
	}

	public int getcId() {
		return cId;
	}

	public void seteName(String eName) {
		this.eName = eName;
	}

	public String geteName() {
		return eName;
	}

	public void seteLongDescription(String eLongDescription) {
		this.eLongDescription = eLongDescription;
	}

	public void seteShortDescription(String eShortDescription) {
		this.eShortDescription = eShortDescription;
	}

	public String geteShortDescription() {
		return eShortDescription;
	}

	public String geteLongDescription() {
		return eLongDescription;
	}

	public void seteLocation(String eLocation) {
		this.eLocation = eLocation;
	}

	public String geteLocation() {
		return eLocation;
	}

	public void seteCategory(String eCategory) {
		this.eCategory = eCategory;
	}

	public String geteCategory() {
		return eCategory;
	}

	public void seteStartDate(Date eStartDate) {
		this.eStartDate = eStartDate;
	}

	public Date geteStartDate() {
		return eStartDate;
	}

	public void seteFinishDate(Date eFinishDate) {
		this.eFinishDate = eFinishDate;
	}

	public Date geteFinishDate() {
		return eFinishDate;
	}

	public void seteStartTime(String eStartTime) {
		this.eStartTime = eStartTime;
	}

	public String geteStartTime() {
		return eStartTime;
	}

	public void seteFinishTime(String eFinishTime) {
		this.eFinishTime = eFinishTime;
	}

	public String geteFinishTime() {
		return eFinishTime;
	}

	public void seteRestriction(String eRestriction) {
		this.eRestriction = eRestriction;
	}

	public String geteRestriction() {
		return eRestriction;
	}

	public void seteInfoTel(String eInfoTel) {
		this.eInfoTel = eInfoTel;
	}

	public String geteInfoTel() {
		return eInfoTel;
	}

	public void seteImageURL(String eImageURL) {
		this.eImageURL = eImageURL;
	}

	public String geteImageURL() {
		return eImageURL;
	}
}