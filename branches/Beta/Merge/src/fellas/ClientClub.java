package fellas;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.LinkedList;

public class ClientClub extends UnicastRemoteObject {
	private ServerInterface server;
	private String host = "localhost";
	private Club clubLogged;

	public ClientClub() throws RemoteException, MalformedURLException,
			NotBoundException {
		server = (ServerInterface) Naming.lookup("//" + host + "/SvrMobile");
	}

	public boolean clubAccess(String name, String psw) throws RemoteException {
		boolean res = server.clubAccess(name, psw);
		if (res) {
			clubLogged = getClubData(name);
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

	private Club getClubData(String cName) throws RemoteException {
		return server.getClubData(cName);
	}

	public boolean updateClubData(String oName, String oSurname,
			String cAddress, String cTel, String cEMail, String cType,
			String cName, String psw) throws RemoteException {
		final Club tempClub = new Club(clubLogged.getId(), oName, oSurname,
				cAddress, cTel, cEMail, cType, cName, psw);
		if (server.updateClubData(tempClub)) {
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
		return server.getClubEventsList(clubLogged.getId());
	}

	public MyEvent getEvent(int id) throws RemoteException {
		return server.getEvent(id);
	}

	public boolean createEvent(String eName, String eShortDescription,
			String eLongDescription, String eLocation, String eCategory,
			String eDate, String eStartTime, String eFinishTime,
			String eRestriction) throws RemoteException {
		return server.createEvent(clubLogged.getId(), eName, eShortDescription,
				eLongDescription, eLocation, eCategory, eDate, eStartTime,
				eFinishTime, eRestriction);
	}

	public boolean updateEvent(MyEvent event) throws RemoteException {
		return server.updateEvent(event);
	}

	public boolean deleteEvent(int eventId) throws RemoteException {
		return server.deleteEvent(eventId);
	}
}
