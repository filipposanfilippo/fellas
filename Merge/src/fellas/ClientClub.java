package fellas;

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

	public ClientClub() throws RemoteException, MalformedURLException,
			NotBoundException {
		server = (ServerInterface) Naming.lookup("//" + host + "/SvrMobile");
	}

	public boolean clubAccess(String cName, String psw) throws RemoteException {
		boolean res = server.clubAccess(cName, psw);
		if (res) {
			clubLogged = getClubData(cName,psw);
		}
		return res;
	}

	public boolean clubRegistration(String oName, String oSurname,
			String cAddress, String cTel, String cEMail, String cType,
			String cName, String psw) throws RemoteException {
		return server.clubRegistration(oName, oSurname, cAddress, cTel, cEMail,
				cType, cName, psw);
	}

	public LinkedList<Club> getClubList() throws RemoteException {
		return server.getClubList();
	}

	private Club getClubData(String cName, String psw) throws RemoteException {
		return server.getClubData(cName, psw);
	}

	public boolean updateClubData(String oName, String oSurname,
			String cAddress, String cTel, String cEMail, String cType,
			String cName, String psw) throws RemoteException {
		final Club tempClub = new Club(clubLogged.getId(), oName, oSurname,
				cAddress, cTel, cEMail, cType, cName, psw);
		if (server.updateClubData(clubLogged.getcName(), clubLogged.getPsw(), tempClub)) {
			clubLogged = tempClub;
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
		return server.getClubEventsList(clubLogged.getcName(), clubLogged.getPsw(), clubLogged.getId());
	}

	public MyEvent getEvent(int id) throws RemoteException {
		return server.getEvent(id);
	}

	public boolean createEvent(String eName, String eShortDescription,
			String eLongDescription, String eLocation, String eCategory,
			Date eStartDate, Date eFinishDate, String eStartTime,
			String eFinishTime, String eRestriction, String infoTel,
			String imageURL) throws RemoteException {
		return server.createEvent(clubLogged.getcName(), clubLogged.getPsw(), clubLogged.getId(), eName, eShortDescription,
				eLongDescription, eLocation, eCategory, eStartDate,
				eFinishDate, eStartTime, eFinishTime, eRestriction, infoTel,
				imageURL);
	}

	public boolean updateEvent(MyEvent event) throws RemoteException {
		return server.updateEvent(clubLogged.getcName(), clubLogged.getPsw(), event);
	}

	public boolean deleteEvent(int eventId) throws RemoteException {
		return server.deleteEvent(clubLogged.getcName(), clubLogged.getPsw(), eventId);
	}

	public LinkedList<User> getEventUsersList(int eventId) throws RemoteException {
		return server.getEventUsersList(clubLogged.getcName(), clubLogged.getPsw(), eventId);
	}

	public String spamMobile(String message, String criterion)
			throws RemoteException {
		return server.spamMobile(clubLogged.getcName(), clubLogged.getPsw(), message, criterion);
	}

	public boolean clubUnregistration() throws RemoteException {
		return server.clubUnregistration(clubLogged.getcName(), clubLogged
				.getPsw());
	}

}
