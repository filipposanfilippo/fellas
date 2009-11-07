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

	public LinkedList<Club> getClubList() throws RemoteException {
		LinkedList<Club> clubList = new LinkedList<Club>();
		try {
			query = "SELECT * FROM clubs";
			statement = connection.createStatement();
			rs = statement.executeQuery(query);
			while (rs.next()) {
				clubList.add(new Club(rs.getString("oName"), rs
						.getString("oSurname"), rs.getString("cAddress"), rs
						.getString("cTel"), rs.getString("cName"), rs
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
				return new Club(rs.getString("oName"),
						rs.getString("oSurname"), rs.getString("cAddress"), rs
								.getString("cTel"), rs.getString("cName"), rs
								.getString("psw"));
		} catch (SQLException e) {
			System.out.println("ERRORE IN SERVER getClubData: " + clubName);
			e.printStackTrace();
			return new Club();
		}
		return new Club();
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
		return null;
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
