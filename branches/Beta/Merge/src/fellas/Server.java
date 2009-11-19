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
import java.util.LinkedList;

public class Server extends UnicastRemoteObject implements ServerInterface {
	private Connection connection = null;
	private Statement statement = null;
	private String query = "";
	private ResultSet rs = null;

	public Server() throws RemoteException {
		try {
			// TODO add connection controlls whether the connection falls down
			Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
			connection = DriverManager.getConnection("jdbc:odbc:diana4free");
		} catch (Exception ex) {
			// handle any errors
			ex.printStackTrace();
		} finally {
			try {
				statement.close();
				connection.close();
			} catch (Exception e) {
				System.err.println(e);
			}
		}
	}

	public boolean authenticationClub(String name, String psw)
			throws RemoteException {
		// invoke client club method to check user-password
		return false;
	}

	public boolean authenticationMobile() throws RemoteException {
		// invoke client club method to check user-password
		return false;
	}

	// TODO MANCA clunUnregistration
	public boolean clubRegistration(String oName, String oSurname,
			String cAddress, String cTel, String cEMail, String cType,
			String cName, String psw) throws RemoteException {
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
			query = "INSERT INTO clubs(oName,oSurname,cAddress,cTel,cEMail,cType,cName,psw)"
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
					+ cName + "','" + psw + "')";
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
		}
	}

	public boolean updateClubData(Club club) throws RemoteException {
		String[] coordinates = new String[2];
		try {
			query = "UPDATE clubs SET oName='" + club.getoName() + "',"
					+ " oSurname='" + club.getoSurname() + "'," + " cAddress='"
					+ club.getcAddress() + "'," + " cTel='" + club.getcTel()
					+ "'," + " cEMail='" + club.getcEMail() + "'," + " cType='"
					+ club.getcType() + "'," + " cName='" + club.getcName()
					+ "'," + " psw='" + club.getPsw() + "'" + " WHERE id="
					+ club.getId();
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
		}
	}

	@Override
	public boolean clubAccess(String cName, String psw) throws RemoteException {
		System.out.println(cName + " : " + psw);
		try {
			query = "SELECT psw FROM clubs WHERE cName='" + cName
					+ "' AND psw = '" + psw + "'";
			statement = connection.createStatement();
			rs = statement.executeQuery(query);
			return rs.next();
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	public LinkedList<Club> getClubList() throws RemoteException {
		LinkedList<Club> clubList = new LinkedList<Club>();
		try {
			query = "SELECT * FROM clubs";
			statement = connection.createStatement();
			rs = statement.executeQuery(query);
			while (rs.next()) {
				clubList.add(new Club(rs.getInt("id"), rs.getString("oName"),
						rs.getString("oSurname"), rs.getString("cAddress"), rs
								.getString("cTel"), rs.getString("cEMail"), rs
								.getString("cType"), rs.getString("cName"), rs
								.getString("psw")));
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		return clubList;
	}

	public Club getClubData(String clubName) throws RemoteException {
		try {
			query = "SELECT * FROM clubs WHERE cName='" + clubName + "'";
			statement = connection.createStatement();
			rs = statement.executeQuery(query);
			if (rs.next())
				return new Club(rs.getInt("id"), rs.getString("oName"), rs
						.getString("oSurname"), rs.getString("cAddress"), rs
						.getString("cTel"), rs.getString("cEMail"), rs
						.getString("cType"), rs.getString("cName"), rs
						.getString("psw"));
		} catch (SQLException e) {
			System.out.println("ERRORE IN SERVER getClubData: " + clubName);
			e.printStackTrace();
			return new Club();
		}
		return new Club();
	}

	public LinkedList<MyEvent> getClubEventsList(int cId) {
		try {
			LinkedList<MyEvent> eventList = new LinkedList<MyEvent>();
			query = "SELECT * FROM events WHERE cId=" + cId;
			statement = connection.createStatement();
			rs = statement.executeQuery(query);

			while (rs.next())
				eventList.add(new MyEvent(rs.getInt("id"), rs.getInt("cId"), rs
						.getString("eName"), rs.getString("eShortDescription"),
						rs.getString("eLongDescription"), rs
								.getString("eLocation"), rs
								.getString("eCategory"), rs.getString("eDate"),
						rs.getString("eStartTime"),
						rs.getString("eFinishTime"), rs
								.getString("eRestriction"), rs
								.getString("eInfoTel"), rs
								.getString("eImageURL")));
			return eventList;
		} catch (SQLException e) {
			System.out.println("ERRORE IN SERVER getClubEvents: " + cId);
			e.printStackTrace();
			return null;
		}
	}

	public MyEvent getEvent(int id) {
		try {
			query = "SELECT * FROM events WHERE id=" + id + " LIMIT 1";
			statement = connection.createStatement();
			rs = statement.executeQuery(query);
			if (rs.next()) {
				MyEvent event = new MyEvent(rs.getInt("id"), rs.getInt("cId"),
						rs.getString("eName"), rs
								.getString("eShortDescription"), rs
								.getString("eLongDescription"), rs
								.getString("eLocation"), rs
								.getString("eCategory"), rs.getString("eDate"),
						rs.getString("eStartTime"),
						rs.getString("eFinishTime"), rs
								.getString("eRestriction"), rs
								.getString("eInfoTel"), rs
								.getString("eImageURL"));
				return event;
			} else {
				return null;
			}
		} catch (SQLException e) {
			System.out.println("ERRORE IN SERVER getEvent: " + id);
			e.printStackTrace();
			return null;
		}
	}

	public LinkedList<User> getUsers4Event(int eventId) throws RemoteException {
		try {
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
		}
	}

	@Override
	public MobileUser[] getMobileList(String sqlString) throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}

	// TODO check whether it's working or not...
	public boolean isClubExisting(String cName) {
		try {
			query = "SELECT * FROM clubs WHERE cName='" + cName + "'";
			statement = connection.createStatement();
			rs = statement.executeQuery(query);
			return rs.next();
		} catch (SQLException e) {
			e.printStackTrace();
			return true;
		}
	}

	// added by Fil
	public boolean isUserExisting(String uTel) {
		try {
			query = "SELECT * FROM users WHERE uTel='" + uTel + "'";
			statement = connection.createStatement();
			rs = statement.executeQuery(query);
			return rs.next();
		} catch (SQLException e) {
			e.printStackTrace();
			return true;
		}
	}

	public MobileUser getMobileUser(String name) {
		return null;
	}

	public boolean createEvent(int cId, String eName, String eShortDescription,
			String eLongDescription, String eLocation, String eCategory,
			String eDate, String eStartTime, String eFinishTime,
			String eRestriction, String eInfoTel, String eImageURL)
			throws RemoteException {
		String[] coordinates = new String[2];
		coordinates = address2GEOcoordinates(eLocation);
		try {
			// TODO prima di inserire controlla che eName non esista gi�
			/*
			 * query = "SELECT id FROM events WHERE eName='" + eName + "'";
			 * statement = connection.createStatement(); rs =
			 * statement.executeQuery(query); if (rs.next()) return false;
			 */

			query = "INSERT INTO events (cId,eName,eShortDescription,eLongDescription,"
					+ "eLocation,eCategory,eDate,eStartTime,eFinishTime,eRestriction,eInfoTel,eImageURL)"
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
					+ eDate
					+ "','"
					+ eStartTime
					+ "','"
					+ eFinishTime
					+ "','"
					+ eRestriction
					+ "','"
					+ eInfoTel
					+ "','"
					+ eImageURL + "')";
			statement = connection.createStatement();
			statement.execute(query);

			// insert event into poi and add it the actions
			// recupera id assegnato con autoincrement
			query = "SELECT id FROM events WHERE eName='" + eName
					+ "' AND cId='" + cId + "' AND eDate='" + eDate + "'";
			statement = connection.createStatement();
			rs = statement.executeQuery(query);
			rs.next();
			int eventId = rs.getInt("id");

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
					+ "','"
					+ eDate
					+ "','" + eStartTime + "','" + eName + "',3)";
			statement = connection.createStatement();
			statement.execute(query);
			// add actions to POI
			// recupera id poi
			query = "SELECT id FROM POI WHERE type=3 AND idItem='" + eventId
					+ "'";
			statement = connection.createStatement();
			rs = statement.executeQuery(query);
			rs.next();
			int poiId = rs.getInt("id");

			query = "INSERT INTO Action (uri,label,poiId)"
					+ "VALUES ('http://fellas.netsons.org/events/event" + poiId
					+ ".php','Join event','" + poiId + "')";
			statement = connection.createStatement();
			statement.execute(query);
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public boolean updateEvent(MyEvent event) throws RemoteException {
		try {
			query = "UPDATE events SET " + "cId='" + event.getcId() + "',"
					+ "eName='" + event.geteName() + "',"
					+ "eShortDescription='" + event.geteShortDescription()
					+ "'," + "eLongDescription='" + event.geteLongDescription()
					+ "'," + "eLocation='" + event.geteLocation() + "',"
					+ "eCategory='" + event.geteCategory() + "'," + "eDate='"
					+ event.geteDate() + "'," + "eStartTime='"
					+ event.geteStartTime() + "'," + "eFinishTime='"
					+ event.geteFinishTime() + "'," + "eRestriction='"
					+ event.geteRestriction() + "' WHERE id=" + event.getId();

			statement = connection.createStatement();
			statement.execute(query);
			// Update POI table too
			/*
			 * note: in poi table, id has to be the same to event id and action
			 * id. Aggiunti i seguenti attributi alla classe event e alla
			 * tabella: - eInfoTel: che � il telefono dell'organizzatore
			 * dell'evento (pu� essere diverso dal club) - eImageURL
			 */

			String[] coordinates = address2GEOcoordinates(event.geteLocation());

			query = "UPDATE POI SET " + "title='" + event.geteName() + "',"
					+ "attribution='" + event.geteInfoTel() + "',"
					+ "imageURL='" + event.geteImageURL() + "'," + "lat='"
					+ coordinates[0] + "'," + "lon='" + coordinates[1] + "',"
					+ "line2='" + event.geteCategory() + "'," + "line3='"
					+ event.geteDate() + "'," + "line4='"
					+ event.geteStartTime() + "'" + " WHERE idItem='"
					+ event.getId() + "' AND type=3";

			statement = connection.createStatement();
			statement.execute(query);

			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean deleteEvent(int eventId) throws RemoteException {
		try {
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
		}
	}

	@Override
	public String broadcastMyStatus(String senderPhone, String criterion)
			throws RemoteException {
		// if(!checkRegistration())
		// return "You are not registered, please register";

		String answer = "";

		try {
			query = "SELECT uTel FROM users WHERE " + criterion + "";
			statement = connection.createStatement();
			rs = statement.executeQuery(query);
			while (rs.next()) {
				// System.out.println("---->"+rs.getString("uTel"));
				answer += '@' + rs.getString("uTel");
			}
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
			// e.printStackTrace();
			return "broadcastMyStatus error%";
		}
		// break;
	}

	@Override
	public String chatUp(String senderTel, String username)
			throws RemoteException {
		String answer = "";
		String receiverTel = "";
		try {
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
		}
		return answer;
	}

	@Override
	public boolean checkRegistration(String phoneNumber) throws RemoteException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String eventsList(String senderPhone, String criterion)
			throws RemoteException {
		String answer = "";

		try {
			query = "SELECT username FROM users WHERE uTel='" + senderPhone
					+ "'";
			statement = connection.createStatement();
			rs = statement.executeQuery(query);

			if (!rs.next())
				return "You are not registered, please register%";

			query = "SELECT eName FROM events WHERE " + criterion + "";
			statement = connection.createStatement();
			rs = statement.executeQuery(query);

			while (rs.next())
				answer += rs.getString("eName") + ',';
			answer = answer.substring(0, answer.lastIndexOf(','));
			answer += '%';
			System.out.println(answer);

			if (answer.equals(""))
				return "Any events match with criterion%";
			return answer;

		} catch (SQLException e) {
			// e.printStackTrace();
			return "EVENTLIST ERROR%";
		}
	}

	@Override
	public String inviteFriend(String senderPhone, String friendPhone,
			int eventId) throws RemoteException {
		String answer = new String('@' + friendPhone + '@');
		try {
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
		}
		return answer;
	}

	@Override
	public String joinEvent(String senderPhone, String eventCode)
			throws RemoteException {
		int userid;
		try {
			// CHECK IF USER EXISTS
			query = "SELECT id FROM users WHERE eName='" + senderPhone + "'";
			rs = statement.executeQuery(query);
			if (!rs.next())
				return "You are not registered, please register%";
			userid = rs.getInt("id");

			// CHECK IF USER IS ALREADY ATTENDING
			query = "SELECT id FROM subscription WHERE id='"
					+ String.valueOf(userid) + "'";
			rs = statement.executeQuery(query);
			if (rs.next())
				return "You are already attendig at this event%";
			// INSERT USER IN EVENT TABLE
			query = "INSERT INTO `" + eventCode + "` (`id`)" + "VALUES (`"
					+ String.valueOf(userid) + "`)";
			rs = statement.executeQuery(query);
		} catch (SQLException e) {
			e.printStackTrace();
			return "JOINEVENT ERROR%";
		}
		return "You are attending at event%"; // insert eShortDescription but
		// dont forget to end sentence
		// with %
	}

	@Override
	public String setLocation(String uTel, String uLocation)
			throws RemoteException {
		// if(!checkRegistration())
		// return "You are not registered, please register";
		if (!isUserExisting(uTel))
			return "You are not registered, please register%";
		try {
			query = "UPDATE users SET uLocation = '" + uLocation
					+ "' WHERE uTel = '" + uTel + "'";
			statement = connection.createStatement();
			statement.execute(query);

			// update location in poi
			String[] coordinates = new String[2];
			coordinates = address2GEOcoordinates(uLocation);

			query = "SELECT id FROM users WHERE uTel='" + uTel + "'";
			statement = connection.createStatement();
			rs = statement.executeQuery(query);
			rs.next();
			int id = rs.getInt("id");

			query = "UPDATE POI SET lat = '" + coordinates[0] + "',lon='"
					+ coordinates[1] + "' WHERE idItem = '" + id
					+ "' AND type=1";
			statement = connection.createStatement();
			statement.execute(query);

			return "Location updated%";
		} catch (SQLException e) {
			// e.printStackTrace();
			return "LOCATIONUPDATE ERROR%";
		}
	}

	@Override
	public String setStatus(String uTel, String uStatus) throws RemoteException {
		// if(!checkRegistration())
		// return "You are not registered, please register";
		if (!isUserExisting(uTel))
			return "You are not registered, please register%";
		try {
			query = "UPDATE users SET uStatus = '" + uStatus
					+ "' WHERE uTel = '" + uTel + "'";
			statement = connection.createStatement();
			statement.execute(query);
			// update status in poi
			query = "SELECT id FROM users WHERE uTel='" + uTel + "'";
			statement = connection.createStatement();
			rs = statement.executeQuery(query);
			rs.next();
			int id = rs.getInt("id");

			query = "UPDATE POI SET line4 = '" + uStatus + "' WHERE idItem = '"
					+ id + "' AND type=1";
			statement = connection.createStatement();
			statement.execute(query);

			return "Status updated%";
		} catch (SQLException e) {
			// e.printStackTrace();
			return "STATUS UPDATE ERROR%";
		}
	}

	@Override
	public String mobileRegistration(String uTel, String username, String psw,
			String uSex, String uAge, String uLocation, String uPrivacy)
			throws RemoteException {
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
			// e.printStackTrace();
			return "REGISTRATION ERROR%";
		}
	}

	@Override
	public String userList(String senderPhone, String criterion)
			throws RemoteException {
		String answer = "";

		try {
			query = "SELECT username FROM users WHERE uTel='" + senderPhone
					+ "'";
			statement = connection.createStatement();
			rs = statement.executeQuery(query);

			if (!rs.next())
				return "You are not registered, please register%";

			query = "SELECT username FROM users WHERE " + criterion + "";
			statement = connection.createStatement();
			rs = statement.executeQuery(query);

			while (rs.next())
				answer += rs.getString("username") + ',';
			answer = answer.substring(0, answer.lastIndexOf(','));
			answer += '%';
			System.out.println(answer);

			if (answer.equals(""))
				return "Any users match with criterion%";
			return answer;

		} catch (SQLException e) {
			// e.printStackTrace();
			return "USERLIST ERROR%";
		}
	}

	@Override
	public String spamMobile(String message, String criterion)
			throws RemoteException {
		String answer = "";
		try {
			query = "SELECT uTel FROM users WHERE " + criterion + "";
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
			InetAddress serverAddr = InetAddress.getByName("192.168.1.104"); // HTC
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

	@Override
	public String mobileUnregistration(String uTel) throws RemoteException {
		if (!isUserExisting(uTel))
			return "You are not registered%";
		try {
			// recupera id
			query = "SELECT id FROM users WHERE uTel='" + uTel + "'";
			statement = connection.createStatement();
			rs = statement.executeQuery(query);
			if (!rs.next())
				return "You are not registered, please register%";
			int id = rs.getInt("id");

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
		}
	}

	@Override
	public String chatUpAnswer(String senderTel, String id)
			throws RemoteException {
		String answer = "";
		try {
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
			// e.printStackTrace();
			return "CHATUPANSWER ERROR%";
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
			coordinates[0] = "11.331800";
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

	@Override
	public boolean clubUnregistration(String cName, String psw)
			throws RemoteException {
		// if (!isClubExisting(cName))
		// return false;
		try {
			// select id club
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
		}
	}

	@Override
	public String setPrivacy(String uTel, int privacy) throws RemoteException {
		if (!isUserExisting(uTel))
			return "You are not registered, please register%";
		try {
			query = "UPDATE users SET privacy  = '" + privacy
					+ "' WHERE uTel = '" + uTel + "'";
			statement = connection.createStatement();
			statement.execute(query);
			// update status in poi
			query = "SELECT id FROM users WHERE uTel='" + uTel + "'";
			statement = connection.createStatement();
			rs = statement.executeQuery(query);
			rs.next();
			int id = rs.getInt("id");

			if (privacy == 1) {
				query = "UPDATE POI SET attribution = '" + uTel
						+ "' WHERE idItem = '" + id + "' AND type=1";
				statement = connection.createStatement();
				statement.execute(query);
			} else {
				query = "UPDATE POI SET attribution = '' WHERE idItem = '" + id + "' AND type=1";
				statement = connection.createStatement();
				statement.execute(query);

			}

			return "Privacy updated%";
		} catch (SQLException e) {
			// e.printStackTrace();
			return "PRIVACY UPDATE ERROR%";
		}
	}
}
