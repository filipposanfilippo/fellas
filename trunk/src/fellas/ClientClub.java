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
	private static final long serialVersionUID = 1L;

	private ServerInterface server;
	private final String RMI_HOST = "localhost";
	private Club clubLogged;

	final String _URL = "http://feelslike.netsons.org/";

	public ClientClub() throws RemoteException, MalformedURLException,
			NotBoundException {
		server = (ServerInterface) Naming
				.lookup("//" + RMI_HOST + "/SvrMobile");
	}

	/**
	 * @param username
	 * @param password
	 * @return boolean
	 * @throws RemoteException
	 */
	public boolean clubAccess(final String username, final String password)
			throws RemoteException {
		boolean res = server.clubAuthentication(username, password);
		if (res) {
			clubLogged = getClubData(username, password);
		}
		return res;
	}

	/**
	 * @param oName
	 * @param oSurname
	 * @param cAddress
	 * @param cTel
	 * @param cEMail
	 * @param cType
	 * @param cName
	 * @param username
	 * @param psw
	 * @param imgLocalURL
	 * @return boolean
	 * @throws RemoteException
	 */
	public boolean clubRegistration(final String oName, final String oSurname,
			final String cAddress, final String cTel, final String cEMail,
			final String cType, final String cName, final String username,
			final String psw, final String imgLocalURL) throws RemoteException {

		String imgRemoteURL = "";
		if (!imgLocalURL.equals("")) {
			String ext = imgLocalURL.substring(imgLocalURL.length() - 3);
			imgRemoteURL = _URL + "clubs/" + username + "." + ext;
			FTPConnection connection = new FTPConnection();
			try {
				connection.uploadFile(imgRemoteURL.replace(_URL, ""),
						imgLocalURL);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return server.clubRegistration(oName, oSurname, cAddress, cTel, cEMail,
				cType, cName, username, psw, imgRemoteURL);
	}

	/**
	 * @param cName
	 * @param psw
	 * @return Club
	 * @throws RemoteException
	 */
	private Club getClubData(final String cName, final String psw)
			throws RemoteException {
		return server.getClubData(cName, psw);
	}

	/**
	 * @param oName
	 * @param oSurname
	 * @param cAddress
	 * @param cTel
	 * @param cEMail
	 * @param cType
	 * @param cName
	 * @param username
	 * @param psw
	 * @param imgLocalURL
	 * @return boolean
	 * @throws RemoteException
	 */
	public boolean updateClubData(final String oName, final String oSurname,
			final String cAddress, final String cTel, final String cEMail,
			final String cType, final String cName, final String username,
			final String psw, final String imgLocalURL) throws RemoteException {

		String imgRemoteURL = clubLogged.getcImageURL();
		String ext = "";
		if (!imgLocalURL.equals("")) {
			ext = imgLocalURL.substring(imgLocalURL.length() - 3);
			imgRemoteURL = _URL + "clubs/" + username + "." + ext;
		}

		final Club tempClub = new Club(clubLogged.getId(), oName, oSurname,
				cAddress, cTel, cEMail, cType, cName, username, psw,
				imgRemoteURL);

		if (server.updateClubData(clubLogged.getUsername(),
				clubLogged.getPsw(), tempClub)) {
			clubLogged = tempClub;
			if (!imgLocalURL.equals("")) {
				FTPConnection connection = new FTPConnection();
				try {
					connection.uploadFile(imgRemoteURL.replace(_URL, ""),
							imgLocalURL);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			return true;
		} else
			return false;
	}

	/**
	 * @return Club
	 */
	public Club getClub() {
		return clubLogged;
	}

	/**
	 * @return String
	 */
	public String getClubName() {
		return clubLogged.getcName();
	}

	/**
	 * @return String
	 */
	public String getClubPsw() {
		return clubLogged.getPsw();
	}

	/**
	 * @return LinkedList<MyEvent>
	 * @throws RemoteException
	 */
	public LinkedList<MyEvent> getClubEventsList() throws RemoteException {
		return server.getClubEventsList(clubLogged.getUsername(), clubLogged
				.getPsw(), clubLogged.getId(), "events");
	}

	/**
	 * @return LinkedList<MyEvent>
	 * @throws RemoteException
	 */
	public LinkedList<MyEvent> getOldClubEventsList() throws RemoteException {
		return server.getClubEventsList(clubLogged.getUsername(), clubLogged
				.getPsw(), clubLogged.getId(), "old_events");
	}

	/**
	 * @param int id
	 * @return MyEvent
	 * @throws RemoteException
	 */
	public MyEvent getEvent(final int id) throws RemoteException {
		return server.getEvent(id, "events");
	}

	/**
	 * @param int id
	 * @return MyEvent
	 * @throws RemoteException
	 */
	public MyEvent getOldEvent(final int id) throws RemoteException {
		return server.getEvent(id, "old_events");
	}

	/**
	 * @param eName
	 * @param eShortDescription
	 * @param eLongDescription
	 * @param eLocation
	 * @param eCategory
	 * @param eStartDate
	 * @param eFinishDate
	 * @param eStartTime
	 * @param eFinishTime
	 * @param eRestriction
	 * @param infoTel
	 * @param imageURL
	 * @return boolean
	 * @throws RemoteException
	 */
	public boolean createEvent(final String eName,
			final String eShortDescription, final String eLongDescription,
			String eLocation, final String eCategory, final Date eStartDate,
			final Date eFinishDate, final String eStartTime,
			final String eFinishTime, final String eRestriction,
			final String infoTel, final String imageURL) throws RemoteException {
		return server.createEvent(clubLogged.getUsername(),
				clubLogged.getPsw(), clubLogged.getId(), eName,
				eShortDescription, eLongDescription, eLocation, eCategory,
				eStartDate, eFinishDate, eStartTime, eFinishTime, eRestriction,
				infoTel, imageURL);
	}

	/**
	 * @param MyEvent
	 *            event
	 * @return boolean
	 * @throws RemoteException
	 */
	public boolean updateEvent(final MyEvent event) throws RemoteException {
		return server.updateEvent(clubLogged.getUsername(),
				clubLogged.getPsw(), event);
	}

	/**
	 * @param int eventId
	 * @return boolean
	 * @throws RemoteException
	 */
	public boolean deleteEvent(final int eventId) throws RemoteException {
		return server.deleteEvent(clubLogged.getUsername(),
				clubLogged.getPsw(), eventId);
	}

	/**
	 * @param int eventId
	 * @return LinkedList<User>
	 * @throws RemoteException
	 */
	public LinkedList<User> getEventUsersList(final int eventId)
			throws RemoteException {
		return server.getEventUsersList(clubLogged.getUsername(), clubLogged
				.getPsw(), eventId);
	}

	/**
	 * @param String
	 *            message
	 * @param String
	 *            criterion
	 * @return String
	 * @throws RemoteException
	 */
	public String spamMobile(final String message, final String criterion)
			throws RemoteException {
		return server.spamMobile(clubLogged.getUsername(), clubLogged.getPsw(),
				message, criterion);
	}

	/**
	 * @return boolean
	 * @throws RemoteException
	 */
	public boolean clubUnregistration() throws RemoteException {
		return server.clubUnregistration(clubLogged.getUsername(), clubLogged
				.getPsw());
	}

}