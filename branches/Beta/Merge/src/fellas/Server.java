package fellas;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.Timer;
import java.util.TimerTask;

public class Server extends UnicastRemoteObject implements ServerInterface {
	private Connection connection = null;
	private Statement statement = null;
	private String query = "";
	private ResultSet rs = null;
	private static String keyword = "perorapassworddiprova";
	private ResultSet primaryRs = null;

	public Server() throws RemoteException {
		GrabberThread grabber = new GrabberThread();
		new Thread(grabber).start();
		openConnection();
		closeConnection();
	}

	public boolean openConnection() {
		try {
			// TODO add connection controls whether the connection falls down
			Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
			connection = DriverManager.getConnection("jdbc:odbc:diana4free");
			return true;
		} catch (Exception ex) {
			// handle any errors
			ex.printStackTrace();
			return false;
		}
	}

	public boolean closeConnection() {
		try {
			connection.close();
			return true;
		} catch (Exception e) {
			System.err.println(e);
			return false;
		}
	}

	// TODO to cancel
	public boolean authenticationClub(String name, String psw)
			throws RemoteException {
		// invoke client club method to check user-password
		return false;
	}

	// TODO to cancel
	public boolean authenticationMobile() throws RemoteException {
		// invoke client club method to check user-password
		return false;
	}

	public boolean clubRegistration(String oName, String oSurname,
			String cAddress, String cTel, String cEMail, String cType,
			String cName, String psw, String cImageURL) throws RemoteException {
		openConnection();
		if (isClubExisting(oName))
			return false;
		try {
			// check if there isn't another club with the same name and address
			query = "SELECT id FROM clubs WHERE cName='" + cName
					+ "' AND cAddress='" + cAddress + "'";
			statement = connection.createStatement();
			rs = statement.executeQuery(query);
			if (rs.next())
				return false;
			// adding club in the club's table
			query = "INSERT INTO clubs(oName,oSurname,cAddress,cTel,cEMail,cType,cName,psw,cImageURL)"
					+ "VALUES ('"
					+ oName
					+ "','"
					+ oSurname
					+ "','"
					+ cAddress
					+ "','"
					+ cTel
					+ "','"
					+ cEMail
					+ "','"
					+ cType
					+ "','"
					+ cName + "','" + psw + "','" + cImageURL + "')";
			statement = connection.createStatement();
			statement.execute(query);
			// get id-club
			query = "SELECT id FROM clubs WHERE cName='" + cName
					+ "' AND cAddress='" + cAddress + "'";
			statement = connection.createStatement();
			rs = statement.executeQuery(query);
			rs.next();
			int clubId = rs.getInt("id");
			String[] geo = new String[2];
			geo = address2GEOcoordinates(cAddress);
			// adding info in POI table
			query = "INSERT INTO POI (idItem,attribution,imageURL,lat,lon,line2,line4,title,type) VALUES "
					+ "('"
					+ clubId
					+ "','"
					+ cTel
					+ "','http://diana.netsons.org/clubs/"
					+ cName
					+ ".jpg',"
					+ "'"
					+ geo[0]
					+ "',"
					+ "'"
					+ geo[1]
					+ "','"
					+ cType
					+ "','" + cEMail + "','" + cName + "','2')";
			statement = connection.createStatement();
			statement.execute(query);

			// adding info in action table
			query = "SELECT id FROM POI WHERE type='2' AND idItem='" + clubId
					+ "'";
			statement = connection.createStatement();
			rs = statement.executeQuery(query);
			rs.next();
			int poiId = rs.getInt("id");
			query = "INSERT INTO Action (uri, label,poiId) VALUES ('http://diana.netsons.org/clubs/"
					+ cName + ".php','Visit club page','" + poiId + "')";
			statement = connection.createStatement();
			statement.execute(query);
			System.out.println("club added");
			return true;
		} catch (SQLException e) {
			// e.printStackTrace();
			return false;
		} finally {
			closeConnection();
		}
	}

	public boolean updateClubData(String cName, String psw, Club club)
			throws RemoteException {
		String[] coordinates = new String[2];
		if (!clubAccess(cName, psw))
			return false;
		try {
			openConnection();
			query = "UPDATE clubs SET oName='" + club.getoName()
					+ "', oSurname='" + club.getoSurname() + "', cAddress='"
					+ club.getcAddress() + "', cTel='" + club.getcTel()
					+ "', cEMail='" + club.getcEMail() + "', cType='"
					+ club.getcType() + "', cName='" + club.getcName()
					+ "', psw='" + club.getPsw() + "', cImageURL='"
					+ club.getcImageURL() + "' WHERE id=" + club.getId();
			statement = connection.createStatement();
			statement.execute(query);
			// update POI table
			coordinates = address2GEOcoordinates(club.getcAddress());
			query = "UPDATE POI SET attribution='" + club.getcTel() + "',"
					+ " lat='" + coordinates[0] + "'," + " lon='"
					+ coordinates[1] + "'," + " line2='" + club.getcType()
					+ "'," + " line4='" + club.getcEMail() + "'"
					+ " WHERE type=2 AND idItem=" + club.getId();
			statement = connection.createStatement();
			statement.execute(query);
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		} finally {
			closeConnection();
		}
	}

	@Override
	public boolean clubAccess(String cName, String psw) throws RemoteException {
		boolean res = false;
		System.out.println(cName + " : " + psw);
		try {
			openConnection();
			query = "SELECT psw FROM clubs WHERE cName='" + cName
					+ "' AND psw = '" + psw + "'";
			statement = connection.createStatement();
			rs = statement.executeQuery(query);
			res = rs.next();
			return res;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		} finally {
			closeConnection();
		}
	}

	/*
	 * public LinkedList<Club> getClubList() throws RemoteException {
	 * LinkedList<Club> clubList = new LinkedList<Club>(); try {
	 * openConnection(); query = "SELECT * FROM clubs"; statement =
	 * connection.createStatement(); rs = statement.executeQuery(query); while
	 * (rs.next()) { clubList.add(new Club(rs.getInt("id"),
	 * rs.getString("oName"), rs.getString("oSurname"),
	 * rs.getString("cAddress"), rs .getString("cTel"), rs.getString("cEMail"),
	 * rs .getString("cType"), rs.getString("cName"), rs .getString("psw"))); }
	 * } catch (SQLException e) { e.printStackTrace(); return null; } finally{
	 * closeConnection(); } return clubList; }
	 */

	public Club getClubData(String cName, String psw) throws RemoteException {
		// TODO be careful: if cName & psw are wrong, we return an empty club
		if (!clubAccess(cName, psw))
			return new Club();
		try {
			openConnection();
			query = "SELECT * FROM clubs WHERE cName='" + cName + "'";
			statement = connection.createStatement();
			rs = statement.executeQuery(query);
			if (rs.next())
				return new Club(rs.getInt("id"), rs.getString("oName"), rs
						.getString("oSurname"), rs.getString("cAddress"), rs
						.getString("cTel"), rs.getString("cEMail"), rs
						.getString("cType"), rs.getString("cName"), rs
						.getString("psw"), rs.getString("cImageURL"));
		} catch (SQLException e) {
			System.out.println("ERRORE IN SERVER getClubData: " + cName);
			e.printStackTrace();
			return new Club();
		} finally {
			closeConnection();
		}
		return new Club();
	}

	public LinkedList<MyEvent> getClubEventsList(String cName, String psw,
			int cId, String table) throws RemoteException {
		if (!clubAccess(cName, psw))
			return null;
		try {
			openConnection();
			LinkedList<MyEvent> eventList = new LinkedList<MyEvent>();
			query = "SELECT * FROM " + table + " WHERE cId=" + cId;
			statement = connection.createStatement();
			rs = statement.executeQuery(query);
			while (rs.next())
				eventList.add(new MyEvent(rs.getInt("id"), rs.getInt("cId"), rs
						.getString("eName"), rs.getString("eShortDescription"),
						rs.getString("eLongDescription"), rs
								.getString("eLocation"), rs
								.getString("eCategory"), rs
								.getDate("eStartDate"), rs
								.getDate("eFinishDate"), rs
								.getString("eStartTime"), rs
								.getString("eFinishTime"), rs
								.getString("eRestriction"), rs
								.getString("eInfoTel"), rs
								.getString("eImageURL")));
			return eventList;
		} catch (SQLException e) {
			System.out.println("ERRORE IN SERVER getClubEvents: " + cId);
			e.printStackTrace();
			return null;
		} finally {
			closeConnection();
		}
	}

	public MyEvent getEvent(int id, String table) {
		try {
			openConnection();
			query = "SELECT * FROM " + table + " WHERE id=" + id
					+ " ORDER BY eStartDate DESC LIMIT 1";
			statement = connection.createStatement();
			rs = statement.executeQuery(query);
			if (rs.next()) {
				MyEvent event = new MyEvent(rs.getInt("id"), rs.getInt("cId"),
						rs.getString("eName"), rs
								.getString("eShortDescription"), rs
								.getString("eLongDescription"), rs
								.getString("eLocation"), rs
								.getString("eCategory"), rs
								.getDate("eStartDate"), rs
								.getDate("eFinishDate"), rs
								.getString("eStartTime"), rs
								.getString("eFinishTime"), rs
								.getString("eRestriction"), rs
								.getString("eInfoTel"), rs
								.getString("eImageURL"));
				return event;
			} else
				return null;
		} catch (SQLException e) {
			System.out.println("ERRORE IN SERVER getEvent: " + id);
			e.printStackTrace();
			return null;
		} finally {
			closeConnection();
		}
	}

	public LinkedList<User> getEventUsersList(String cName, String psw,
			int eventId) throws RemoteException {
		// TODO be careful: if cName & psw are wrong, we return an empty list
		if (!clubAccess(cName, psw))
			return null;
		try {
			openConnection();
			LinkedList<User> usersList = new LinkedList<User>();
			query = "SELECT * FROM users RIGHT OUTER JOIN subscription "
					+ "ON users.id = subscription.uId "
					+ "WHERE subscription.eId=" + eventId;
			statement = connection.createStatement();
			rs = statement.executeQuery(query);
			while (rs.next())
				usersList.add(new User(rs.getInt("id"), rs.getString("uTel"),
						rs.getString("uName"), rs.getString("uAge"), rs
								.getString("uSex"), rs.getString("uStatus"), rs
								.getString("username"), rs.getString("psw"), rs
								.getString("uSurname"), rs
								.getString("uLocation"), rs
								.getString("imageURL"), rs.getInt("privacy")));
			return usersList;
		} catch (SQLException e) {
			e.printStackTrace(System.err);
			return null;
		} finally {
			closeConnection();
		}
	}

	@Override
	public MobileUser[] getMobileList(String sqlString) throws RemoteException {
		// TODO IS IT NECESSARY???
		try {
			openConnection();
		}
		// catch(SQLException e){
		catch (Exception e) { // TODO use the line above

		} finally {
			closeConnection();
		}
		return null;
	}

	// TODO check whether it's working or not...
	public boolean isClubExisting(String cName) {
		boolean res = false;
		try {
			// openConnection();
			query = "SELECT * FROM clubs WHERE cName='" + cName + "'";
			statement = connection.createStatement();
			rs = statement.executeQuery(query);
			res = rs.next();
			// closeConnection();
			return res;
		} catch (SQLException e) {
			e.printStackTrace();
			// closeConnection();
			return true;
		}
	}

	public boolean isPOIeventExisting(int eventId) {
		boolean res = false;
		try {
			// openConnection();
			query = "SELECT * FROM POI WHERE idItem='" + eventId
					+ "' AND type=3";
			statement = connection.createStatement();
			rs = statement.executeQuery(query);
			res = rs.next();
			// closeConnection();
			return res;
		} catch (SQLException e) {
			e.printStackTrace();
			// closeConnection();
			return true;
		}
	}

	// BE CAREFUL: before calling this method, you need to open connection
	public int getUserId(String uTel) {
		int userId;
		try {
			// openConnection();
			query = "SELECT * FROM users WHERE uTel='" + uTel + "'";
			statement = connection.createStatement();
			rs = statement.executeQuery(query);
			rs.next();
			userId = rs.getInt("id");
			// closeConnection();
			return userId;
		} catch (SQLException e) {
			e.printStackTrace();
			// closeConnection();
			return -1;
		}
	}

	// BE CAREFUL: before calling this method, you need to open connection
	public boolean isUserExisting(String uTel) {
		boolean res = false;
		try {
			// openConnection();
			query = "SELECT * FROM users WHERE uTel='" + uTel + "'";
			statement = connection.createStatement();
			rs = statement.executeQuery(query);
			res = rs.next();
			// closeConnection();
			return res;
		} catch (SQLException e) {
			e.printStackTrace();
			// closeConnection();
			return true;
		}
	}

	public MobileUser getMobileUser(String name) {
		// TODO IS IT NECESSARY???
		openConnection();
		closeConnection();
		return null;
	}

	public boolean createEvent(String cName, String psw, int cId, String eName,
			String eShortDescription, String eLongDescription,
			String eLocation, String eCategory, Date eStartDate,
			Date eFinishDate, String eStartTime, String eFinishTime,
			String eRestriction, String eInfoTel, String eImageURL)
			throws RemoteException {
		String[] coordinates = new String[2];
		coordinates = address2GEOcoordinates(eLocation);
		long startDifference;
		long finishDifference;

		String formattedStartDate = formatDate(eStartDate);
		String formattedFinishDate = formatDate(eFinishDate);
		if (!clubAccess(cName, psw))
			return false;
		try {
			// TODO prima di inserire controlla che eName non esista già
			/*
			 * query = "SELECT id FROM events WHERE eName='" + eName + "'";
			 * statement = connection.createStatement(); rs =
			 * statement.executeQuery(query); if (rs.next()) return false;
			 */
			openConnection();
			query = "INSERT INTO events (cId,eName,eShortDescription,eLongDescription,"
					+ "eLocation,eCategory,eStartDate,eFinishDate,eStartTime,eFinishTime,eRestriction,eInfoTel,eImageURL)"
					+ "VALUES ('"
					+ cId
					+ "','"
					+ eName
					+ "','"
					+ eShortDescription
					+ "','"
					+ eLongDescription
					+ "','"
					+ eLocation
					+ "','"
					+ eCategory
					+ "','"
					+ formattedStartDate
					+ "','"
					+ formattedFinishDate
					+ "','"
					+ eStartTime
					+ "','"
					+ eFinishTime
					+ "','"
					+ eRestriction
					+ "','"
					+ eInfoTel
					+ "','"
					+ eImageURL
					+ "')";
			statement = connection.createStatement();
			statement.execute(query);

			// System.out.println(formattedStartDate + " " + eStartTime);
			// System.out.println(formattedFinishDate + " " + eFinishTime);
			// check start time of event
			// if dayOfStart.getTime() - today.getTime()<7 than immediately
			// adding to POI
			// else wait until 7 days before his starting time

			Date today = new Date();

			DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date dayOfStart = null;
			Date dayOfFinish = null;
			try {
				dayOfStart = df.parse(formattedStartDate + " " + eStartTime);
				dayOfFinish = df.parse(formattedFinishDate + " " + eFinishTime);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			startDifference = dayOfStart.getTime() - today.getTime();

			// retrieve event id
			query = "SELECT id FROM events WHERE eName='" + eName
					+ "' AND cId='" + cId + "' AND eStartDate='"
					+ formattedStartDate + "' AND eFinishDate='"
					+ formattedFinishDate + "'";
			statement = connection.createStatement();
			rs = statement.executeQuery(query);
			rs.next();
			int eventId = rs.getInt("id");

			if (startDifference < 7 * 60 * 60 * 24 * 1000) {
				System.out.println("Immediatily adding to POI");
				query = "INSERT INTO POI(idItem,attribution,imageURL,lat,lon,line2,line3,line4,title,type)"
						+ "VALUES ('"
						+ eventId
						+ "','"
						+ eInfoTel
						+ "','"
						+ eImageURL
						+ "','"
						+ coordinates[0]
						+ "','"
						+ coordinates[1]
						+ "','"
						+ eCategory
						+ "','Starts: "
						+ formattedStartDate
						+ " "
						+ eStartTime
						+ "','Ends: "
						+ formattedFinishDate
						+ " "
						+ eFinishTime
						+ "','"
						+ eName + "',3)";
				statement = connection.createStatement();
				statement.execute(query);
				// add actions to POI
				// retrieve POI id
				query = "SELECT id FROM POI WHERE type=3 AND idItem='"
						+ eventId + "'";
				statement = connection.createStatement();
				rs = statement.executeQuery(query);
				rs.next();
				int poiId = rs.getInt("id");

				query = "INSERT INTO Action (uri,label,poiId)"
						+ "VALUES ('http://fellas.netsons.org/events/event"
						+ poiId + ".php','Join event','" + poiId + "')";
				statement = connection.createStatement();
				statement.execute(query);
			} else {
				startDifference = startDifference - 7 * 60 * 60 * 24 * 1000;
				Timer StartTimer = new Timer();
				StartTimer.schedule(new starterTask(eventId, eInfoTel,
						eImageURL, eLocation, eCategory, eStartDate,
						eFinishDate, eStartTime, eFinishTime, eName),
						startDifference);
			}

			// Start terminatorEvent
			finishDifference = dayOfFinish.getTime() - today.getTime();
			Timer EndTimer = new Timer();
			EndTimer.schedule(new terminatorTask(eventId, eFinishDate,
					eFinishTime), finishDifference);
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		} finally {
			closeConnection();
		}
	}

	class starterTask extends TimerTask {
		private int eventId;
		private String eInfoTel;
		private String eImageURL;
		private String eLocation;
		private String eCategory;
		private Date eStartDate;
		private Date eFinishDate;
		private String eStartTime;
		private String eFinishTime;
		private String eName;

		public starterTask(int eventId, String eInfoTel, String eImageURL,
				String eLocation, String eCategory, Date eStartDate,
				Date eFinishDate, String eStartTime, String eFinishTime,
				String eName) {
			this.eventId = eventId;
			this.eInfoTel = eInfoTel;
			this.eImageURL = eImageURL;
			this.eLocation = eLocation;
			this.eCategory = eCategory;
			this.eStartDate = eStartDate;
			this.eFinishDate = eFinishDate;
			this.eStartTime = eStartTime;
			this.eFinishTime = eFinishTime;
			this.eName = eName;

		}

		public void run() {

			MyEvent oldEvent;

			String[] coordinates = new String[2];
			try {
				openConnection();
				// check if event was modified
				query = "SELECT id, cId, eName, eShortDescription, eLongDescription, eLocation, eCategory, eStartDate, eFinishDate, eStartTime, eFinishTime, eRestriction, eInfoTel, eImageURL FROM events WHERE id="
						+ eventId + "";
				statement = connection.createStatement();
				rs = statement.executeQuery(query);
				rs.next();
				oldEvent = new MyEvent(rs.getInt("id"), rs.getInt("cId"), rs
						.getString("eName"), rs.getString("eShortDescription"),
						rs.getString("eLongDescription"), rs
								.getString("eLocation"), rs
								.getString("eCategory"), rs
								.getDate("eStartDate"), rs
								.getDate("eFinishDate"), rs
								.getString("eStartTime"), rs
								.getString("eFinishTime"), rs
								.getString("eRestriction"), rs
								.getString("eInfoTel"), rs
								.getString("eImageURL"));

				// if nothing was changed

				if (oldEvent.geteName().equals(eName)
						&& oldEvent.geteInfoTel().equals(eInfoTel)
						&& oldEvent.geteImageURL().equals(eImageURL)
						&& oldEvent.geteLocation().equals(eLocation)
						&& oldEvent.geteCategory().equals(eCategory)
						&& oldEvent.geteStartDate().toString().equals(
								formatDate(eStartDate))
						&& oldEvent.geteFinishDate().toString().equals(
								formatDate(eFinishDate))
						&& oldEvent.geteStartTime().equals(eStartTime)
						&& oldEvent.geteFinishTime().equals(eFinishTime))

				{
					System.out.println("Adding event " + eventId
							+ " to POI 7 days before his starting time");
					coordinates = address2GEOcoordinates(rs
							.getString("eLocation"));

					query = "INSERT INTO POI(idItem,attribution,imageURL,lat,lon,line2,line3,line4,title,type)"
							+ "VALUES ('"
							+ eventId
							+ "','"
							+ eInfoTel
							+ "','"
							+ eImageURL
							+ "','"
							+ coordinates[0]
							+ "','"
							+ coordinates[1]
							+ "','"
							+ eCategory
							+ "','Starts: "
							+ formatDate(eStartDate)
							+ " "
							+ eStartTime
							+ "','Ends: "
							+ formatDate(eFinishDate)
							+ " "
							+ eFinishTime + "','" + eName + "',3)";
					statement = connection.createStatement();
					statement.execute(query);
					// add actions to POI
					// retrieve POI id
					query = "SELECT id FROM POI WHERE type=3 AND idItem='"
							+ eventId + "'";
					statement = connection.createStatement();
					rs = statement.executeQuery(query);
					rs.next();
					int poiId = rs.getInt("id");

					query = "INSERT INTO Action (uri,label,poiId)"
							+ "VALUES ('http://fellas.netsons.org/events/event"
							+ poiId + ".php','Join event','" + poiId + "')";
					statement = connection.createStatement();
					statement.execute(query);
				} else
					System.out.println("StartEvent of " + eventId
							+ " was aborted, another starter timer is running");
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				closeConnection();
			}
		}

	}

	class terminatorTask extends TimerTask {
		private int eventId;
		private Date eFinishDate;
		private String eFinishTime;

		public terminatorTask(int eventId, Date eFinishDate, String eFinishTime) {
			this.eventId = eventId;
			this.eFinishDate = eFinishDate;
			this.eFinishTime = eFinishTime;
		}

		public void run() {
			System.out.println("Deleting event " + eventId + " from POI");

			MyEvent oldEvent;
			try {
				// delete item from poi table and from actions too
				// recupera id POI
				openConnection();

				// check if event was modified
				query = "SELECT id, cId, eName, eShortDescription, eLongDescription, eLocation, eCategory, eStartDate, eFinishDate, eStartTime, eFinishTime, eRestriction, eInfoTel, eImageURL FROM events WHERE id="
						+ eventId + "";
				statement = connection.createStatement();
				rs = statement.executeQuery(query);
				rs.next();
				oldEvent = new MyEvent(rs.getInt("id"), rs.getInt("cId"), rs
						.getString("eName"), rs.getString("eShortDescription"),
						rs.getString("eLongDescription"), rs
								.getString("eLocation"), rs
								.getString("eCategory"), rs
								.getDate("eStartDate"), rs
								.getDate("eFinishDate"), rs
								.getString("eStartTime"), rs
								.getString("eFinishTime"), rs
								.getString("eRestriction"), rs
								.getString("eInfoTel"), rs
								.getString("eImageURL"));

				// if nothing was changed
				if (oldEvent.geteFinishDate().toString().equals(
						formatDate(eFinishDate))

						&& oldEvent.geteFinishTime().equals(eFinishTime)) {

					query = "SELECT id FROM POI WHERE type=3 AND idItem='"
							+ eventId + "'";
					statement = connection.createStatement();
					rs = statement.executeQuery(query);
					rs.next();
					int poiId = rs.getInt("id");

					query = "DELETE from POI where idItem='" + eventId
							+ "' AND type=3";
					statement = connection.createStatement();
					statement.execute(query);
					query = "DELETE from Action where poiId='" + poiId + "'";
					statement = connection.createStatement();
					statement.execute(query);
					System.out.println("Event " + eventId
							+ " has been deleted from POI");
				} else
					System.out
							.println("FinishEvent of "
									+ eventId
									+ " was aborted, another terminator timer is running");
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				closeConnection();
			}
		}
	}

	private String formatDate(Date date) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		return new StringBuilder(dateFormat.format(date)).toString();
	}

	public boolean updateEvent(String cName, String psw, MyEvent event)
			throws RemoteException {
		if (!clubAccess(cName, psw))
			return false;
		Date today;

		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date dayOfStart = null;
		Date dayOfFinish = null;
		long startDifference;
		long finishDifference;

		MyEvent oldEvent;
		try {
			openConnection();
			// retrieve start and end event
			query = "SELECT id, cId, eName, eShortDescription, eLongDescription, eLocation, eCategory, eStartDate, eFinishDate, eStartTime, eFinishTime, eRestriction, eInfoTel, eImageURL FROM events WHERE id="
					+ event.getId() + "";
			statement = connection.createStatement();
			rs = statement.executeQuery(query);
			rs.next();
			oldEvent = new MyEvent(rs.getInt("id"), rs.getInt("cId"), rs
					.getString("eName"), rs.getString("eShortDescription"), rs
					.getString("eLongDescription"), rs.getString("eLocation"),
					rs.getString("eCategory"), rs.getDate("eStartDate"), rs
							.getDate("eFinishDate"),
					rs.getString("eStartTime"), rs.getString("eFinishTime"), rs
							.getString("eRestriction"), rs
							.getString("eInfoTel"), rs.getString("eImageURL"));

			query = "UPDATE events SET " + "cId='" + event.getcId() + "',"
					+ "eName='" + event.geteName() + "',"
					+ "eShortDescription='" + event.geteShortDescription()
					+ "'," + "eLongDescription='" + event.geteLongDescription()
					+ "'," + "eLocation='" + event.geteLocation() + "',"
					+ "eCategory='" + event.geteCategory() + "',"
					+ "eStartDate='" + formatDate(event.geteStartDate()) + "',"
					+ "eFinishDate='" + formatDate(event.geteFinishDate())
					+ "'," + "eStartTime='" + event.geteStartTime() + "',"
					+ "eFinishTime='" + event.geteFinishTime() + "',"
					+ "eRestriction='" + event.geteRestriction()
					+ "' WHERE id=" + event.getId();
			statement = connection.createStatement();
			statement.execute(query);
			// Update POI table too
			/*
			 * note: in poi table, id has to be the same to event id and action
			 * id. Aggiunti i seguenti attributi alla classe event e alla
			 * tabella: - eInfoTel: che è il telefono dell'organizzatore
			 * dell'evento (può essere diverso dal club) - eImageURL
			 */

			// if something is changed, start starterTask (if it is necessary)

			/*
			 * if (!event.geteName().equals(oldEvent.geteName()) ||
			 * !event.geteInfoTel().equals(oldEvent.geteInfoTel()) ||
			 * !event.geteImageURL().equals(oldEvent.geteImageURL()) ||
			 * !event.geteLocation().equals(oldEvent.geteLocation()) ||
			 * !event.geteCategory().equals(oldEvent.geteCategory()) ||
			 * !event.geteStartDate
			 * ().equals(oldEvent.geteStartDate().toString()) ||
			 * !event.geteFinishDate()
			 * .equals(oldEvent.geteFinishDate().toString()) ||
			 * !event.geteStartTime().equals(oldEvent.geteStartTime()) ||
			 * !event.geteFinishTime() .equals(oldEvent.geteFinishTime())) {
			 * System.out.println(event.geteStartDate() + " " +
			 * event.geteStartTime());
			 */

			try {
				dayOfStart = df.parse(formatDate(event.geteStartDate()) + " "
						+ event.geteStartTime());
				dayOfFinish = df.parse(formatDate(event.geteFinishDate()) + " "
						+ event.geteFinishTime());
			} catch (ParseException e) {
				e.printStackTrace();
			}
			String[] coordinates = address2GEOcoordinates(event.geteLocation());
			today = new Date();
			startDifference = dayOfStart.getTime() - today.getTime();

			if (startDifference < 7 * 60 * 60 * 24 * 1000) {
				// può essere che già c'è e va aggiornato

				if (isPOIeventExisting(event.getId())) {
					System.out.println("Updating POI for event: "
							+ event.getId());
					query = "UPDATE POI SET " + "title='" + event.geteName()
							+ "'," + "attribution='" + event.geteInfoTel()
							+ "'," + "imageURL='" + event.geteImageURL() + "',"
							+ "lat='" + coordinates[0] + "'," + "lon='"
							+ coordinates[1] + "'," + "line2='"
							+ event.geteCategory() + "'," + "line3='Starts: "
							+ formatDate(event.geteStartDate()) + " "
							+ event.geteStartTime() + "'," + "line4='Ends: "
							+ formatDate(event.geteFinishDate()) + " "
							+ event.geteFinishTime() + "'" + " WHERE idItem='"
							+ event.getId() + "' AND type=3";
					statement = connection.createStatement();
					statement.execute(query);
				} else {
					System.out.println("Inserting a new POI for event: "
							+ event.getId());
					query = "INSERT INTO POI(idItem,attribution,imageURL,lat,lon,line2,line3,line4,title,type)"
							+ "VALUES ('"
							+ event.getId()
							+ "','"
							+ event.geteInfoTel()
							+ "','"
							+ event.geteImageURL()
							+ "','"
							+ coordinates[0]
							+ "','"
							+ coordinates[1]
							+ "','"
							+ event.geteCategory()
							+ "','Starts: "
							+ formatDate(event.geteStartDate())
							+ " "
							+ event.geteStartTime()
							+ "','Ends: "
							+ formatDate(event.geteFinishDate())
							+ " "
							+ event.geteFinishTime()
							+ "','"
							+ event.geteName()
							+ "',3)";
					statement = connection.createStatement();
					statement.execute(query);
					// add actions to POI
					// retrieve POI id
					query = "SELECT id FROM POI WHERE type=3 AND idItem='"
							+ event.getId() + "'";
					statement = connection.createStatement();
					rs = statement.executeQuery(query);
					rs.next();
					int poiId = rs.getInt("id");

					query = "INSERT INTO Action (uri,label,poiId)"
							+ "VALUES ('http://fellas.netsons.org/events/event"
							+ poiId + ".php','Join event','" + poiId + "')";
					statement = connection.createStatement();
					statement.execute(query);
				}
			} else {// può essere che già c'è e va cancellato
				if (isPOIeventExisting(event.getId())) {
					query = "SELECT id FROM POI WHERE type=3 AND idItem='"
							+ event.getId() + "'";
					statement = connection.createStatement();
					rs = statement.executeQuery(query);
					rs.next();
					int poiId = rs.getInt("id");

					query = "DELETE from POI where idItem='" + event.getId()
							+ "' AND type=3";
					statement = connection.createStatement();
					statement.execute(query);
					query = "DELETE from Action where poiId='" + poiId + "'";
					statement = connection.createStatement();
					statement.execute(query);
				}
				System.out.println("Starting starterTask for event: "
						+ event.getId());
				startDifference = startDifference - 7 * 60 * 60 * 24 * 1000;
				Timer StartTimer = new Timer();
				StartTimer.schedule(new starterTask(event.getId(), event
						.geteInfoTel(), event.geteImageURL(), event
						.geteLocation(), event.geteCategory(), event
						.geteStartDate(), event.geteFinishDate(), event
						.geteStartTime(), event.geteFinishTime(), event
						.geteName()), startDifference);
			}
			// Start terminatorEvent (if and only if event end was modified)
			if (!event.geteFinishDate().equals(
					oldEvent.geteFinishDate().toString())
					|| !event.geteFinishTime()
							.equals(oldEvent.geteFinishTime())) {
				System.out.println("Starting terminatorTask for event: "
						+ event.getId());
				finishDifference = dayOfFinish.getTime() - today.getTime();
				Timer EndTimer = new Timer();
				EndTimer.schedule(new terminatorTask(event.getId(), event
						.geteFinishDate(), event.geteFinishTime()),
						finishDifference);
			}

			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		} finally {
			closeConnection();
		}
	}

	public boolean deleteEvent(String cName, String psw, int eventId)
			throws RemoteException {
		if (!clubAccess(cName, psw))
			return false;
		try {
			openConnection();
			// check if there are user subscripted to that event
			query = "SELECT uId from subscription  where eId='" + eventId + "'";
			statement = connection.createStatement();
			rs = statement.executeQuery(query);
			// rs.next();
			String criterion = "";
			while (rs.next()) {
				criterion += "id='" + rs.getInt("uId") + "' OR ";
			}
			// send an abort message to all users that have joined the event
			if (criterion != "") {
				query = "SELECT eName from events  where id='" + eventId + "'";
				statement = connection.createStatement();
				rs = statement.executeQuery(query);
				rs.next();
				String message = "The event " + rs.getString("eName") + "("
						+ eventId + ")" + " of club " + cName + " was abort";
				spamMobile(cName, psw, message, criterion.substring(0,
						criterion.length() - 4));
			}
			query = "DELETE from events where id='" + eventId + "'";
			statement = connection.createStatement();
			statement.execute(query);
			// delete item from poi table and from actions too
			// recupera id POI
			query = "SELECT id FROM POI WHERE type=3 AND idItem='" + eventId
					+ "'";
			statement = connection.createStatement();
			rs = statement.executeQuery(query);
			rs.next();
			int poiId = rs.getInt("id");

			query = "DELETE from POI where idItem='" + eventId + "' AND type=3";
			statement = connection.createStatement();
			statement.execute(query);
			query = "DELETE from Action where poiId='" + poiId + "'";
			statement = connection.createStatement();
			statement.execute(query);
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		} finally {
			closeConnection();
		}
	}

	public String broadcastMyStatus(String key, String senderPhone,
			String criterion) throws RemoteException {
		// if(!checkRegistration())
		// return "You are not registered, please register";
		String answer = "";
		if (!keyword.equals(key))
			return "You are not authorized";
		try {
			openConnection();
			query = "SELECT uTel FROM users WHERE uLocation LIKE '%"
					+ criterion + "%'";
			statement = connection.createStatement();
			rs = statement.executeQuery(query);
			while (rs.next())
				answer += '@' + rs.getString("uTel");
			if (answer.equals(""))
				return "Any users match with criterion%";
			answer += '@';
			System.out.println(answer);

			query = "SELECT username, uStatus FROM users WHERE uTel='"
					+ senderPhone + "'";
			statement = connection.createStatement();
			rs = statement.executeQuery(query);

			if (rs.next()) {
				answer += rs.getString("username") + '+'
						+ rs.getString("uStatus") + "%";
				System.out.println(answer);
				return answer;
			} else
				return "You are not registered, please register%";
		} catch (SQLException e) {
			e.printStackTrace();
			return "broadcastMyStatus error%";
		} finally {
			closeConnection();
		}
	}

	@Override
	public String chatUp(String key, String senderTel, String username)
			throws RemoteException {
		if (!keyword.equals(key))
			return "You are not authorized";
		String answer = "";
		String receiverTel = "";
		try {
			openConnection();
			query = "SELECT uTel FROM users WHERE username='" + username + "'";
			statement = connection.createStatement();
			rs = statement.executeQuery(query);
			rs.next();
			receiverTel = rs.getString("uTel");
			if (receiverTel.equals(""))
				return "Any users with this username found%";
			answer = '@' + receiverTel + '@';

			query = "SELECT username FROM users WHERE uTel='" + senderTel + "'";
			statement = connection.createStatement();
			rs = statement.executeQuery(query);
			if (rs.next())
				answer += "User " + rs.getString("username");
			else
				return "You are not registered, please register%";

			// check if there is already an entry in chatup table

			query = "SELECT id, authorization FROM chatup WHERE senderTel='"
					+ senderTel + "' AND receiverTel='" + receiverTel + "'";
			statement = connection.createStatement();
			rs = statement.executeQuery(query);

			if (rs.next()) {
				if (rs.getString("authorization").equals("0"))
					return "Request already sent. You are waiting for authorization number "
							+ rs.getString("id") + "%";
				else
					return "Request already sent. You are able to chatup with "
							+ receiverTel + "%";
			}

			query = "INSERT INTO chatup (senderTel,receiverTel,authorization)"
					+ "VALUES ('" + senderTel + "','" + receiverTel + "','0')";
			statement = connection.createStatement();
			statement.execute(query);

			query = "SELECT id FROM chatup WHERE senderTel='" + senderTel
					+ "' AND receiverTel='" + receiverTel + "'";
			statement = connection.createStatement();
			rs = statement.executeQuery(query);
			rs.next();
			answer += " asks to chatup with you. If you agree, respond 'y&"
					+ rs.getString("id") + "$'%";
		} catch (SQLException e) {
			e.printStackTrace();
			return "CHATUP ERROR%";
		} finally {
			closeConnection();
		}
		return answer;
	}

	@Override
	public boolean checkRegistration(String key, String phoneNumber)
			throws RemoteException {
		// TODO IS IT NECESSARY?
		if (!keyword.equals(key))
			return false;
		return false;
	}

	@Override
	public String eventsList(String key, String senderPhone, String criterion)
			throws RemoteException {
		String answer = "";
		if (!keyword.equals(key))
			return "You are not authorized";
		try {
			openConnection();
			query = "SELECT username FROM users WHERE uTel='" + senderPhone
					+ "'";
			statement = connection.createStatement();
			rs = statement.executeQuery(query);

			if (!rs.next())
				return "You are not registered, please register%";

			query = "SELECT eName, id FROM events WHERE eLocation LIKE '%"
					+ criterion + "%'";
			statement = connection.createStatement();
			rs = statement.executeQuery(query);
			while (rs.next() && answer.length() < 120)
				answer += rs.getString("eName") + ' ' + rs.getString("id")
						+ ',';
			if (answer.equals(""))
				return "Any events match with criterion%";
			answer = answer.substring(0, answer.lastIndexOf(','));
			answer += '%';
			System.out.println(answer);
			return answer;
		} catch (SQLException e) {
			e.printStackTrace();
			return "EVENTLIST ERROR%";
		} finally {
			closeConnection();
		}
	}

	@Override
	public String inviteFriend(String key, String senderPhone,
			String friendPhone, int eventId) throws RemoteException {
		if (!keyword.equals(key))
			return "You are not authorized";
		String answer = new String('@' + friendPhone + '@');
		try {
			openConnection();
			query = "SELECT username FROM users WHERE uTel='" + senderPhone
					+ "'";
			statement = connection.createStatement();
			rs = statement.executeQuery(query);

			if (rs.next())
				answer += rs.getString("username") + ">invite you at>";
			else
				return "You are not registered, please register%";

			query = "SELECT eShortDescription FROM events WHERE id='" + eventId
					+ "'";
			statement = connection.createStatement();
			rs = statement.executeQuery(query);
			if (rs.next())
				answer += rs.getString("eShortDescription") + '%';
			else
				return "Any events match with id%";
		} catch (SQLException e) {
			e.printStackTrace();
			return "INVITEFRIEND ERROR%";
		} finally {
			closeConnection();
		}
		return answer;
	}

	public String joinEvent(String key, String senderPhone, String eventCode)
			throws RemoteException {
		int userid;
		String eName = new String();
		if (!keyword.equals(key))
			return "You are not authorized";
		try {
			openConnection();
			// CHECK IF USER EXISTS
			query = "SELECT id FROM users WHERE uTel='" + senderPhone + "'";
			statement = connection.createStatement();
			rs = statement.executeQuery(query);
			if (!rs.next())
				return "You are not registered, please register%";
			userid = rs.getInt("id");

			// CHECK IF USER IS ALREADY ATTENDING
			query = "SELECT uId FROM subscription WHERE uId='" + userid
					+ "' AND eId='" + eventCode + "'";
			statement = connection.createStatement();
			rs = statement.executeQuery(query);
			if (rs.next())
				return "You are already attendig at this event%";
			// INSERT USER IN EVENT TABLE
			query = "INSERT INTO subscription (eId, uId)" + "VALUES ('"
					+ eventCode + "', '" + userid + "')";
			statement = connection.createStatement();
			statement.execute(query);

			query = "SELECT eName FROM events WHERE id='" + eventCode + "'";
			statement = connection.createStatement();
			rs = statement.executeQuery(query);
			rs.next();
			eName = rs.getString("eName");
		} catch (SQLException e) {
			e.printStackTrace();
			return "JOINEVENT ERROR%";
		} finally {
			closeConnection();
		}
		return "You are attending at event " + eName + " (" + eventCode + ")%";
	}

	public String disJoinEvent(String key, String senderPhone, String eventCode)
			throws RemoteException {
		int userid;
		String eName = new String();
		if (!keyword.equals(key))
			return "You are not authorized";
		try {
			openConnection();
			// CHECK IF USER EXISTS
			query = "SELECT id FROM users WHERE uTel='" + senderPhone + "'";
			statement = connection.createStatement();
			rs = statement.executeQuery(query);
			if (!rs.next())
				return "You are not registered, please register%";
			userid = rs.getInt("id");

			// CHECK IF USER IS ALREADY ATTENDING
			query = "SELECT uId FROM subscription WHERE uId='" + userid
					+ "' AND eId='" + eventCode + "'";
			statement = connection.createStatement();
			rs = statement.executeQuery(query);
			if (rs.next()) {
				query = "DELETE from subscription where eId='" + eventCode
						+ "' AND uId='" + userid + "'";
				statement = connection.createStatement();
				statement.execute(query);
			} else
				return "You are not attendig at this event%";

			query = "SELECT eName FROM events WHERE id='" + eventCode + "'";
			statement = connection.createStatement();
			rs = statement.executeQuery(query);
			rs.next();
			eName = rs.getString("eName");
		} catch (SQLException e) {
			e.printStackTrace();
			return "DISJOINEVENT ERROR%";
		} finally {
			closeConnection();
		}
		return "You have disjoined the event " + eName + " (" + eventCode
				+ ")%";
	}

	public String getDescriptionEvent(String key, String senderPhone,
			String eventCode) throws RemoteException {
		String eShortDescription = new String();
		String eName = new String();
		String start = new String();
		String temp2split = new String();
		String[] splittedString = new String[3];
		if (!keyword.equals(key))
			return "You are not authorized";
		try {
			openConnection();
			// CHECK IF USER EXISTS
			query = "SELECT id FROM users WHERE uTel='" + senderPhone + "'";
			statement = connection.createStatement();
			rs = statement.executeQuery(query);
			if (!rs.next())
				return "You are not registered, please register%";

			query = "SELECT eName, eShortDescription, eStartDate, eStartTime FROM events WHERE id='"
					+ eventCode + "'";
			statement = connection.createStatement();
			rs = statement.executeQuery(query);
			if (!rs.next())
				return "Any event match with your code%";
			eShortDescription = rs.getString("eShortDescription");
			eName = rs.getString("eName");
			temp2split = rs.getString("eStartTime");
			splittedString = temp2split.split(":");
			start = rs.getString("eStartDate") + " " + splittedString[0] + ":"
					+ splittedString[1];
		} catch (SQLException e) {
			e.printStackTrace();
			return "GETDESCRIPTIONEVENT ERROR%";
		} finally {
			closeConnection();
		}
		return eName.toUpperCase() + " " + start + ": " + eShortDescription
				+ "%";
	}

	public String setLocation(String key, String uTel, String uLocation)
			throws RemoteException {
		// if(!checkRegistration())
		// return "You are not registered, please register";
		if (!keyword.equals(key))
			return "You are not authorized";
		openConnection();
		int id = getUserId(uTel);
		if (id < 0)
			return "You are not registered, please register%";
		try {
			query = "UPDATE users SET uLocation = '" + uLocation
					+ "' WHERE uTel = '" + uTel + "'";
			statement = connection.createStatement();
			statement.execute(query);

			// update location in poi
			String[] coordinates = new String[2];
			coordinates = address2GEOcoordinates(uLocation);

			query = "UPDATE POI SET lat = '" + coordinates[0] + "',lon='"
					+ coordinates[1] + "' WHERE idItem = '" + id
					+ "' AND type=1";
			statement = connection.createStatement();
			statement.execute(query);
			return "Location updated%";
		} catch (SQLException e) {
			e.printStackTrace();
			return "LOCATIONUPDATE ERROR%";
		} finally {
			closeConnection();
		}
	}

	public String setStatus(String key, String uTel, String uStatus)
			throws RemoteException {
		// if(!checkRegistration())
		// return "You are not registered, please register";
		if (!keyword.equals(key))
			return "You are not authorized";
		openConnection();
		int id = getUserId(uTel);
		if (id < 0)
			return "You are not registered, please register%";
		try {
			query = "UPDATE users SET uStatus = '" + uStatus
					+ "' WHERE uTel = '" + uTel + "'";
			statement = connection.createStatement();
			statement.execute(query);
			// update status in poi
			query = "UPDATE POI SET line4 = '" + uStatus + "' WHERE idItem = '"
					+ id + "' AND type=1";
			statement = connection.createStatement();
			statement.execute(query);
			return "Status updated%";
		} catch (SQLException e) {
			e.printStackTrace();
			return "STATUS UPDATE ERROR%";
		} finally {
			closeConnection();
		}
	}

	public String mobileRegistration(String key, String uTel, String username,
			String psw, String uSex, String uAge, String uLocation,
			String uPrivacy) throws RemoteException {
		if (!keyword.equals(key))
			return "You are not authorized";
		openConnection();
		if (isUserExisting(uTel))
			return "Already registered%";
		try {
			query = "INSERT INTO users (uTel,username,psw,uSex,uAge,uLocation,privacy,uName, uStatus, uSurname, imageURL)"
					+ "VALUES ('"
					+ uTel
					+ "','"
					+ username
					+ "','"
					+ psw
					+ "','"
					+ uSex
					+ "','"
					+ uAge
					+ "','"
					+ uLocation
					+ "','"
					+ uPrivacy + "','','','','')";
			statement = connection.createStatement();
			statement.execute(query);

			// recupera id user
			query = "SELECT id, privacy FROM users WHERE username='" + username
					+ "'";
			statement = connection.createStatement();
			rs = statement.executeQuery(query);
			rs.next();
			int id = rs.getInt("id");
			int privacy = rs.getInt("privacy");

			// insert user in POI
			String[] coordinates = new String[2];
			coordinates = address2GEOcoordinates(uLocation);
			if (privacy == 0)
				query = "INSERT INTO POI (idItem,attribution,lat,lon,line2,line3,title,type,imageURL,line4)"
						+ "VALUES ('"
						+ id
						+ "','','"
						+ coordinates[0]
						+ "','"
						+ coordinates[1]
						+ "','"
						+ uSex
						+ "','"
						+ uAge
						+ "', '"
						+ username + "',1,'','')";
			else
				query = "INSERT INTO POI (idItem,attribution,lat,lon,line2,line3,title,type,imageURL,line4)"
						+ "VALUES ('"
						+ id
						+ "','"
						+ uTel
						+ "','"
						+ coordinates[0]
						+ "','"
						+ coordinates[1]
						+ "','"
						+ uSex
						+ "','"
						+ uAge
						+ "', '"
						+ username
						+ "',1,'','')";

			statement = connection.createStatement();
			statement.execute(query);

			// add action to poi
			// recupera id del poi
			query = "SELECT id FROM POI WHERE type=1 AND idItem='" + id + "'";
			statement = connection.createStatement();
			rs = statement.executeQuery(query);
			rs.next();
			int poiId = rs.getInt("id");

			query = "INSERT INTO Action (uri,label,poiId)"
					+ "VALUES ('http://diana.netsons.org/users/" + username
					+ ".php','Visit user page','" + poiId + "')";
			statement = connection.createStatement();
			statement.execute(query);
			return "Welcome to Diana, you can now use our services%";
		} catch (SQLException e) {
			e.printStackTrace();
			return "REGISTRATION ERROR%";
		} finally {
			closeConnection();
		}
	}

	public String userList(String key, String senderPhone, String criterion)
			throws RemoteException {
		String answer = "";
		if (!keyword.equals(key))
			return "You are not authorized";
		try {
			openConnection();
			query = "SELECT username FROM users WHERE uTel='" + senderPhone
					+ "'";
			statement = connection.createStatement();
			rs = statement.executeQuery(query);

			if (!rs.next())
				return "You are not registered, please register%";

			query = "SELECT username FROM users WHERE uLocation LIKE '%"
					+ criterion + "%'";
			statement = connection.createStatement();
			rs = statement.executeQuery(query);

			while (rs.next() && answer.length() < 120)
				answer += rs.getString("username") + ',';
			if (answer.equals(""))
				return "Any users match with criterion%";
			answer = answer.substring(0, answer.lastIndexOf(','));
			answer += '%';
			System.out.println(answer);
			return answer;
		} catch (SQLException e) {
			e.printStackTrace();
			return "USERLIST ERROR%";
		} finally {
			closeConnection();
		}
	}

	public String spamMobile(String cName, String psw, String message,
			String criterion) throws RemoteException {
		String answer = "";
		// TODO be careful: if cName & psw are wrong we return an empty string
		if (!clubAccess(cName, psw))
			return "";
		try {
			openConnection();
			query = "SELECT uTel FROM users WHERE " + criterion + "";
			System.out.println("SPAM QUERY: " + query);
			statement = connection.createStatement();
			rs = statement.executeQuery(query);
			// if (!rs.next())
			// return "Any users match with criterion%";
			// rs.previous();
			while (rs.next())
				answer += '@' + rs.getString("uTel");
			if (answer.equals(""))
				return "Any users match with criterion%";
			answer += '@' + message + '%';
			System.out.println(answer);
			// return answer;

			// ---------

			// Retrieve the ServerName
			InetAddress serverAddr = InetAddress.getByName("192.168.1.101"); // HTC
			// ip
			// address
			// where
			// SMS.java
			// is
			// running

			System.out.println("\nConnecting...");
			/* Create new UDP-Socket */
			DatagramSocket socket = new DatagramSocket();

			/* Prepare some data to be sent. */
			byte[] buf = answer.getBytes();

			// Create UDP-packet with data & destination(url+port)

			DatagramPacket packet = new DatagramPacket(buf, buf.length,
					serverAddr, 4445);

			/* Send out the packet */
			socket.send(packet);
			System.out.println("\nSent...");
			return "Mobile spammed%";

			// ---------

		} catch (Exception e) {
			e.printStackTrace();
			return "SPAMMOBILE error%";
		} finally {
			closeConnection();
		}
	}

	public static void main(String[] args) throws RemoteException,
			MalformedURLException, NotBoundException {
		try {
			Naming.rebind("SvrMobile", new Server());
			System.out.println("Server is ready");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String mobileUnregistration(String key, String uTel)
			throws RemoteException {
		if (!keyword.equals(key))
			return "You are not authorized";
		openConnection();
		int id = getUserId(uTel);
		if (id < 0)
			return "You are not registered%";
		try {
			// delete user from users
			query = "DELETE from users WHERE uTel='" + uTel + "'";
			statement = connection.createStatement();
			statement.execute(query);

			// delete user from poi
			// recupera id poi...mi serve per cancellare la action
			// corrispondente
			query = "SELECT id FROM POI WHERE idItem='" + id + "' AND type=1";
			statement = connection.createStatement();
			rs = statement.executeQuery(query);
			rs.next();
			int poiId = rs.getInt("id");

			query = "DELETE from POI WHERE idItem='" + id + "' AND type=1";
			statement = connection.createStatement();
			statement.execute(query);

			// delete entry from action
			query = "DELETE from Action WHERE poiId='" + poiId + "'";
			statement = connection.createStatement();
			statement.execute(query);
			return "You have been unregistered%";
		} catch (SQLException e) {
			e.printStackTrace();
			return "MOBILEUNREGISTRATION ERROR%";
		} finally {
			closeConnection();
		}
	}

	public String chatUpAnswer(String key, String senderTel, String id)
			throws RemoteException {
		String answer = "";
		if (!keyword.equals(key))
			return "You are not authorized";
		try {
			openConnection();
			query = "SELECT senderTel, authorization FROM chatup WHERE id='"
					+ id + "'";
			statement = connection.createStatement();
			rs = statement.executeQuery(query);
			if (rs.next()) {
				if (rs.getInt("authorization") == 1)
					return "You have already accepted this chatup request%";
				answer += '@' + rs.getString("senderTel") + '@';
			} else
				return "Any chatup requests are pending for you%";

			query = "UPDATE chatup SET authorization='1' WHERE id='" + id + "'";
			// System.out.println(query);
			statement = connection.createStatement();
			statement.execute(query);

			answer += "User has accepted to chatup with you. Here the phone number "
					+ senderTel + "%";
			return answer;

		} catch (SQLException e) {
			e.printStackTrace();
			return "CHATUPANSWER ERROR%";
		} finally {
			closeConnection();
		}
	}

	public static String[] address2GEOcoordinates(String streetAddress) {
		// streetAddress = streetAddress.replace(",", "+");
		// use of regular expression(e kka vi futtii!)
		streetAddress = streetAddress.replaceAll(" {1,}", "+");
		String[] coordinates = new String[2];
		if (streetAddress.equals("")) {
			System.out.println("Default location");
			coordinates[0] = "43.318290";
			coordinates[1] = "11.331800";
			return coordinates;
		}
		String tempCoordinates = new String();
		InputStream ins;
		InputStreamReader isr;
		BufferedReader rdr;
		String tempURL = "http://local.yahooapis.com/MapsService/V1/geocode?appid=YD-9G7bey8_JXxQP6rxl.fBFGgCdNjoDMACQA--&street=";
		tempURL += streetAddress;
		System.out.println(tempURL);
		try {
			URL url = new URL(tempURL);
			URLConnection url_conn = url.openConnection();
			ins = (InputStream) url_conn.getContent();
			isr = new InputStreamReader(ins);
			rdr = new BufferedReader(isr);
			tempCoordinates = rdr.readLine();
			while (!tempCoordinates.endsWith("</ResultSet>"))
				tempCoordinates += new String(rdr.readLine());

			coordinates[0] = tempCoordinates.substring(tempCoordinates
					.indexOf("<Latitude>") + 10, tempCoordinates
					.indexOf("</Latitude>"));
			coordinates[1] = tempCoordinates.substring(tempCoordinates
					.indexOf("<Longitude>") + 11, tempCoordinates
					.indexOf("</Longitude>"));
			System.out.println(coordinates[0]);
			System.out.println(coordinates[1]);

		} catch (Exception e) {
			System.out.println("ERROR in address2GEOcoordinates");
		}
		return coordinates;
	}

	public boolean clubUnregistration(String cName, String psw)
			throws RemoteException {
		// if (!isClubExisting(cName))
		// return false;
		try {
			// select id club
			openConnection();
			query = "SELECT id FROM clubs WHERE cName='" + cName
					+ "' AND psw='" + psw + "'";
			statement = connection.createStatement();
			rs = statement.executeQuery(query);
			if (!rs.next())
				return false;
			int id = rs.getInt("id");

			// delete user from users
			query = "DELETE from clubs WHERE id='" + id + "'";
			statement = connection.createStatement();
			statement.execute(query);

			// delete user from poi
			// recupera id poi...mi serve per cancellare la action
			// corrispondente
			query = "SELECT id FROM POI WHERE idItem='" + id + "' AND type=2";
			statement = connection.createStatement();
			rs = statement.executeQuery(query);
			rs.next();
			int poiId = rs.getInt("id");

			query = "DELETE from POI WHERE idItem='" + id + "' AND type=2";
			statement = connection.createStatement();
			statement.execute(query);

			// delete entry from action
			query = "DELETE from Action WHERE poiId='" + poiId + "'";
			statement = connection.createStatement();
			statement.execute(query);
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		} finally {
			closeConnection();
		}
	}

	public String setPrivacy(String key, String uTel, int privacy)
			throws RemoteException {
		if (!keyword.equals(key))
			return "You are not authorized";
		openConnection();
		int id = getUserId(uTel);
		if (id < 0)
			return "You are not registered, please register%";
		try {
			query = "UPDATE users SET privacy  = '" + privacy
					+ "' WHERE uTel = '" + uTel + "'";
			statement = connection.createStatement();
			statement.execute(query);
			// update status in poi
			if (privacy == 1) {
				query = "UPDATE POI SET attribution = '" + uTel
						+ "' WHERE idItem = '" + id + "' AND type=1";
				statement = connection.createStatement();
				statement.execute(query);
			} else {
				query = "UPDATE POI SET attribution = '' WHERE idItem = '" + id
						+ "' AND type=1";
				statement = connection.createStatement();
				statement.execute(query);
			}
			return "Privacy updated%";
		} catch (SQLException e) {
			e.printStackTrace();
			return "PRIVACY UPDATE ERROR%";
		} finally {
			closeConnection();
		}
	}

	class GrabberThread implements Runnable { // when the server goes down it
		// will
		// guarantee database consistency
		public void run() {
			System.out.println("Grabber is running");
			String[] coordinates = new String[2];
			Date today;

			DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date dayOfStart = null;
			Date dayOfFinish = null;
			long startDifference;
			long finishDifference;
			try {
				openConnection();
				query = "SELECT * FROM events";
				statement = connection.createStatement();
				primaryRs = statement.executeQuery(query);
				while (primaryRs.next()) {
					try {
						dayOfStart = df.parse(primaryRs.getDate("eStartDate")
								+ " " + primaryRs.getTime("eStartTime"));
						dayOfFinish = df.parse(primaryRs.getDate("eFinishDate")
								+ " " + primaryRs.getTime("eFinishTime"));
					} catch (ParseException e) {
						e.printStackTrace();
					}
					today = new Date();
					if (today.after(dayOfFinish)) {
						System.out.println("Event " + primaryRs.getInt("id")
								+ " is expired");
						query = "SELECT id FROM POI WHERE type=3 AND idItem='"
								+ primaryRs.getInt("id") + "'";
						statement = connection.createStatement();
						rs = statement.executeQuery(query);
						if (rs.next()) {
							int poiId = rs.getInt("id");
							System.out.println("Deleting expired event "
									+ primaryRs.getInt("id") + " from POI");

							query = "DELETE from POI where idItem='"
									+ primaryRs.getInt("id") + "' AND type=3";
							statement = connection.createStatement();
							statement.execute(query);
							query = "DELETE from Action where poiId='" + poiId
									+ "'";
							statement = connection.createStatement();
							statement.execute(query);
							System.out
									.println("___________________________________________________________");
						} else {
							System.out.println("Expired event "
									+ primaryRs.getInt("id")
									+ " was already deleted from POI");
							System.out
									.println("___________________________________________________________");
						}

					} else {
						System.out.println("Event " + primaryRs.getInt("id")
								+ " is fresh");
						coordinates = address2GEOcoordinates(primaryRs
								.getString("eLocation"));
						startDifference = dayOfStart.getTime()
								- today.getTime();
						if (startDifference < 7 * 60 * 60 * 24 * 1000) {
							query = "SELECT id FROM POI WHERE idItem='"
									+ primaryRs.getInt("id") + "' AND type=3";
							statement = connection.createStatement();
							rs = statement.executeQuery(query);
							if (!rs.next()) {
								System.out.println("Immediatily adding event "
										+ primaryRs.getInt("id") + "to POI");
								query = "INSERT INTO POI(idItem,attribution,imageURL,lat,lon,line2,line3,line4,title,type)"
										+ "VALUES ('"
										+ primaryRs.getInt("id")
										+ "','"
										+ primaryRs.getString("eInfoTel")
										+ "','"
										+ primaryRs.getString("eImageURL")
										+ "','"
										+ coordinates[0]
										+ "','"
										+ coordinates[1]
										+ "','"
										+ primaryRs.getString("eCategory")
										+ "','Starts: "
										+ primaryRs.getDate("eStartDate")
										+ " "
										+ primaryRs.getTime("eStartTime")
										+ "','Ends: "
										+ primaryRs.getDate("eFinishDate")
										+ " "
										+ primaryRs.getTime("eFinishTime")
										+ "','"
										+ primaryRs.getString("eName")
										+ "',3)";
								statement = connection.createStatement();
								statement.execute(query);
								// add actions to POI
								// retrieve POI id
								query = "SELECT id FROM POI WHERE type=3 AND idItem='"
										+ primaryRs.getInt("id") + "'";
								statement = connection.createStatement();
								rs = statement.executeQuery(query);
								rs.next();
								int poiId = rs.getInt("id");

								query = "INSERT INTO Action (uri,label,poiId)"
										+ "VALUES ('http://fellas.netsons.org/events/event"
										+ poiId + ".php','Join event','"
										+ poiId + "')";
								statement = connection.createStatement();
								statement.execute(query);
							} else
								System.out.println("Event "
										+ primaryRs.getInt("id")
										+ " was already added POI");
						} else {
							System.out
									.println("Starting starterTask for event: "
											+ primaryRs.getInt("id"));
							startDifference = startDifference - 7 * 60 * 60
									* 24 * 1000;
							Timer StartTimer = new Timer();
							StartTimer.schedule(new starterTask(primaryRs
									.getInt("id"), primaryRs
									.getString("eInfoTel"), primaryRs
									.getString("eImageURL"), primaryRs
									.getString("eLocation"), primaryRs
									.getString("eCategory"), primaryRs
									.getDate("eStartDate"), primaryRs
									.getDate("eFinishDate"), primaryRs
									.getString("eStartTime"), primaryRs
									.getString("eFinishTime"), primaryRs
									.getString("eName")), startDifference);
						}

						// Start terminatorEvent
						System.out
								.println("Starting terminatorTask for event: "
										+ primaryRs.getInt("id"));
						finishDifference = dayOfFinish.getTime()
								- today.getTime();
						Timer EndTimer = new Timer();
						EndTimer.schedule(new terminatorTask(primaryRs
								.getInt("id"),
								primaryRs.getDate("eFinishDate"), primaryRs
										.getString("eFinishTime")),
								finishDifference);
						System.out
								.println("___________________________________________________________");
					}
				}
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				closeConnection();
			}
			System.out.println("Grabber has ended");

		}
	}

}
