package fellas;

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
import java.util.ArrayList;
import java.util.Iterator;

public class Server extends UnicastRemoteObject implements ServerInterface {
	protected ArrayList<MobileUser> mobileList = new ArrayList<MobileUser>();
	protected ArrayList<ClubUser> clubList = new ArrayList<ClubUser>();
	protected ArrayList<MyEvent> eventList = new ArrayList<MyEvent>();

	Connection connection = null;
	Statement statement = null;
	String query = "";
	ResultSet rs = null;

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

	@Override
	public boolean access(String cName, String psw) throws RemoteException {
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

	@Override
	public ClubUser[] getClubList(String sqlString) throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MobileUser[] getMobileList(String sqlString) throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean clubRegistration(String oName, String oSurname,
			String cAddress, String cTel, String cName, String psw)
			throws RemoteException {
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

	public MobileUser getMobileUser(String name) {
		MobileUser c;
		for (Iterator<MobileUser> i = mobileList.iterator(); i.hasNext();) {
			c = (MobileUser) i.next();
			if (c.name.equals(name))
				return c;
		}
		return null;
	}

	@Override
	public String showClub() throws RemoteException {
		// TODO to change, now it's only for debug
		String clubs = "";
		ClubUser c;
		for (Iterator<ClubUser> i = clubList.iterator(); i.hasNext();) {
			c = (ClubUser) i.next();
			clubs += c.name;
			System.out.println(c.name);
		}
		return clubs;
	}

	public String showEvent() throws RemoteException {
		// TODO to change, now it's only for debug
		String event = "";
		MyEvent c;
		for (Iterator<MyEvent> i = eventList.iterator(); i.hasNext();) {
			c = (MyEvent) i.next();
			event += c.name;
			System.out.println(c.name);
		}
		return event;
	}

	public String showClubEvent(String name, String psw) throws RemoteException {
		// TODO to change, now it's only for debug
		// if (authenticationClub(name,psw)==false)
		// return null; //TODO return an error message instead null
		String event = "";
		MyEvent c;
		for (Iterator<MyEvent> i = eventList.iterator(); i.hasNext();) {
			c = (MyEvent) i.next();
			event += c.name;
			System.out.println(c.name);
		}
		return event;
	}

	public boolean addEvent(String name, String psw, MyEvent e)
			throws RemoteException {
		// TODO Auto-generated method stub
		// if (authenticationClub(name,psw)==false)
		// return false;
		return true;
	}

	public MyEvent createEvent(String nameEvent, String description)
			throws RemoteException {
		return new MyEvent(nameEvent, description);
	}

	@Override
	public boolean broadcastMyStatus(String senderPhone, String criterion)
			throws RemoteException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean chatUp(String senderPhone, String nickname)
			throws RemoteException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean checkRegistration(String phoneNumber) throws RemoteException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String eventsList(String senderPhone, String criterion)
			throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean inviteFriend(String senderPhone, String friendPhone)
			throws RemoteException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean joinEvent(String senderPhone, String eventCode)
			throws RemoteException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean myLocation(String senderPhone, String myLocation)
			throws RemoteException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean mobileRegistration(String senderPhone, String username,
			String password, String sex, String age) throws RemoteException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String userList(String senderPhone, String criterion)
			throws RemoteException {
		// TODO Auto-generated method stub
		return null;
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

}
