package fellas;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MalformedURLException;
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
			connection = DriverManager.getConnection("jdbc:odbc:FellasMySQL");
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

	public boolean clubRegistration(String oName, String oSurname,
			String cAddress, String cTel, String cEMail, String cType,
			String cName, String psw) throws RemoteException {
		if (isClubExisting(oName))
			return false;
		try {
			query = "INSERT INTO clubs (oName,oSurname,cAddress,cTel,cName,psw)"
					+ "VALUES ('"
					+ oName
					+ "','"
					+ oSurname
					+ "','"
					+ cAddress
					+ "','" + cTel + "','" + cName + "','" + psw + "')";
			statement = connection.createStatement();
			statement.execute(query);
			return true;
		} catch (SQLException e) {
			// e.printStackTrace();
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

	public boolean updateClubData(Club club) throws RemoteException {
		try {
			query = "UPDATE clubs SET oName='" + club.getoName() + "',"
					+ " oSurname='" + club.getoSurname() + "'," + " cAddress='"
					+ club.getcAddress() + "'," + " cTel='" + club.getcTel()
					+ "'," + " cEMail='" + club.getcEMail() + "'," + " cType='"
					+ club.getcType() + "'," + " cName='" + club.getcName()
					+ "'," + " psw='" + club.getPsw() + "'" + " WHERE id="
					+ club.getId();
			// System.out.println(query);
			statement = connection.createStatement();
			statement.execute(query);
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
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
								.getString("eRestriction")));
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
								.getString("eRestriction"));
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
			String eRestriction) throws RemoteException {
		try {
			query = "INSERT INTO events (cId,eName,eShortDescription,eLongDescription,"
					+ "eLocation,eCategory,eDate,eStartTime,eFinishTime,eRestriction)"
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
					+ "')";
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

			query = "SELECT id, authorization FROM chatup WHERE senderTel='"
					+ senderTel + "' AND receiverTel='" + receiverTel + "'";
			statement = connection.createStatement();
			rs = statement.executeQuery(query);

			if (rs.next()) {
				if (rs.getString("authorization").equals("0"))
					return "Request already sent. You are waiting for authorization number "
							+ rs.getString("id") + "%";
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
	public boolean joinEvent(String senderPhone, String eventCode)
			throws RemoteException {
		// TODO Auto-generated method stub
		return false;
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
			return "Status updated%";
		} catch (SQLException e) {
			// e.printStackTrace();
			return "STATUS UPDATE ERROR%";
		}
	}

	@Override
	public String mobileRegistration(String uTel, String username, String psw,
			String uSex, String uAge, String uLocation) throws RemoteException {
		if (isUserExisting(uTel))
			return "Already registered%";
		try {
			query = "INSERT INTO users (uTel,username,psw,uSex,uAge, uLocation)"
					+ "VALUES ('"
					+ uTel
					+ "','"
					+ username
					+ "','"
					+ psw
					+ "','" + uSex + "','" + uAge + "', '" + uLocation + "')";
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
			query = "DELETE from users WHERE uTel='" + uTel + "'";
			// System.out.println("DELETE from users where uTel='" + uTel +
			// "' AND username='"+username+"' AND psw='"+psw+"'");
			statement = connection.createStatement();
			statement.execute(query);
			return "You have been unregistered%";
		} catch (SQLException e) {
			e.printStackTrace();
			return "MOBILEUNREGISTRATION ERROR%";
		}
	}

	@Override
	public String chatUpAnswer(String senderPhone) throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}

}
