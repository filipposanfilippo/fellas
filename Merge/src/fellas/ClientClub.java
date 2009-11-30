package fellas;

import java.io.IOException;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Date;
import java.util.LinkedList;

public class ClientClub extends UnicastRemoteObject {
	private ServerInterface server;
	private String host = "localhost";
	private Club clubLogged;

	final String _HOST = "diana.netsons.org";
	final String _USERNAME = "diananet";
	final String _PASSWORD = "password1234";
	final String _URL = "http://diana.netsons.org/";

	public ClientClub() throws RemoteException, MalformedURLException,
			NotBoundException {
		server = (ServerInterface) Naming.lookup("//" + host + "/SvrMobile");
	}

	public boolean clubAccess(String username, String password)
			throws RemoteException {
		boolean res = server.clubAuthentication(username, password);
		if (res) {
			clubLogged = getClubData(username, password);
		}
		return res;
	}

	public boolean clubRegistration(String oName, String oSurname,
			String cAddress, String cTel, String cEMail, String cType,
			String cName, String username, String psw, String imgLocalURL)
			throws RemoteException {

		String imgRemoteURL = "";
		if (!imgLocalURL.equals("")) {
			String ext = imgLocalURL.substring(imgLocalURL.length() - 3);
			imgRemoteURL = _URL + "clubs/" + username + "." + ext;
			FTPConnection connection = new FTPConnection();
			try {
				connection.uploadFile("www/clubs/" + username + "." + ext,
						imgLocalURL);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return server.clubRegistration(oName, oSurname, cAddress, cTel, cEMail,
				cType, cName, username, psw, imgRemoteURL);
	}

	private Club getClubData(String cName, String psw) throws RemoteException {
		return server.getClubData(cName, psw);
	}

	public boolean updateClubData(String oName, String oSurname,
			String cAddress, String cTel, String cEMail, String cType,
			String cName, String username, String psw, String imgLocalURL)
			throws RemoteException {

		String imgRemoteURL = clubLogged.getcImageURL();
		String ext = "";
		if (!imgLocalURL.equals("")) {
			ext = imgLocalURL.substring(imgLocalURL.length() - 3);
			imgRemoteURL = _URL + "clubs/" + username + "." + ext;
		}

		final Club tempClub = new Club(clubLogged.getId(), oName, oSurname,
				cAddress, cTel, cEMail, cType, cName, username, psw,
				imgRemoteURL);

		if (server.updateClubData(clubLogged.getcName(), clubLogged.getPsw(),
				tempClub)) {
			clubLogged = tempClub;
			if (!imgLocalURL.equals("")) {
				FTPConnection connection = new FTPConnection();
				try {
					connection.uploadFile("www/clubs/" + username + "." + ext,
							imgLocalURL);
				} catch (Exception e) {
					// TODO handle I/O exception
				}
			}
			return true;
		} else
			return false;
	}

	public Club getClub() {
		return clubLogged;
	}

	public String getClubName() {
		return clubLogged.getcName();
	}

	public String getClubPsw() {
		return clubLogged.getPsw();
	}

	public LinkedList<MyEvent> getClubEventsList() throws RemoteException {
		return server.getClubEventsList(clubLogged.getUsername(), clubLogged
				.getPsw(), clubLogged.getId(), "events");
	}

	public LinkedList<MyEvent> getOldClubEventsList() throws RemoteException {
		return server.getClubEventsList(clubLogged.getUsername(), clubLogged
				.getPsw(), clubLogged.getId(), "old_events");
	}

	public MyEvent getEvent(int id) throws RemoteException {
		return server.getEvent(id, "events");
	}

	public MyEvent getOldEvent(int id) throws RemoteException {
		return server.getEvent(id, "old_events");
	}

	public boolean createEvent(String eName, String eShortDescription,
			String eLongDescription, String eLocation, String eCategory,
			Date eStartDate, Date eFinishDate, String eStartTime,
			String eFinishTime, String eRestriction, String infoTel,
			String imageURL) throws RemoteException {
		return server.createEvent(clubLogged.getUsername(),
				clubLogged.getPsw(), clubLogged.getId(), eName,
				eShortDescription, eLongDescription, eLocation, eCategory,
				eStartDate, eFinishDate, eStartTime, eFinishTime, eRestriction,
				infoTel, imageURL);
	}

	public boolean updateEvent(MyEvent event) throws RemoteException {
		return server.updateEvent(clubLogged.getUsername(),
				clubLogged.getPsw(), event);
	}

	public boolean deleteEvent(int eventId) throws RemoteException {
		return server.deleteEvent(clubLogged.getUsername(),
				clubLogged.getPsw(), eventId);
	}

	public LinkedList<User> getEventUsersList(int eventId)
			throws RemoteException {
		return server.getEventUsersList(clubLogged.getUsername(), clubLogged
				.getPsw(), eventId);
	}

	public String spamMobile(String message, String criterion)
			throws RemoteException {
		return server.spamMobile(clubLogged.getUsername(), clubLogged.getPsw(),
				message, criterion);
	}

	public boolean clubUnregistration() throws RemoteException {
		return server.clubUnregistration(clubLogged.getUsername(), clubLogged
				.getPsw());
	}

}