package fellas;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.LinkedList;

public interface ServerInterface extends Remote {
	public boolean authenticationClub(String name, String psw)
			throws RemoteException;

	public boolean clubAccess(String name, String cs) throws RemoteException;

	public boolean clubRegistration(String oName, String oSurname,
			String cAddress, String cTel, String cEMail, String cType,
			String cName, String psw) throws RemoteException;

	public Club getClubData(String clubName) throws RemoteException;

	public boolean updateClubData(Club club) throws RemoteException;

	public LinkedList<Club> getClubList() throws RemoteException;

	public LinkedList<MyEvent> getClubEventsList(int cId)
			throws RemoteException;

	public MyEvent getEvent(int id) throws RemoteException;

	public boolean authenticationMobile() throws RemoteException;

	public MobileUser[] getMobileList(String sqlString) throws RemoteException;

	public boolean createEvent(int cId, String eName, String eShortDescription,
			String eLongDescription, String eLocation, String eCategory,
			String eDate, String eStartTime, String eFinishTime,
			String eRestriction) throws RemoteException;

	public boolean updateEvent(MyEvent event) throws RemoteException;

	public boolean deleteEvent(int eventId) throws RemoteException;

	// public boolean registerMobile() throws RemoteException;
	// __________________________

	public String mobileRegistration(String uTel, String username, String psw,
			String uSex, String uAge, String uLocation) throws RemoteException;

	public String mobileUnregistration(String uTel)
			throws RemoteException;

	public String eventsList(String senderPhone, String criterion)
			throws RemoteException;

	public boolean joinEvent(String senderPhone, String eventCode)
			throws RemoteException;

	public String inviteFriend(String senderPhone, String friendPhone,
			int eventId) throws RemoteException;

	public String setLocation(String uTel, String uLocation)
			throws RemoteException;

	public String setStatus(String uTel, String uStatus) throws RemoteException;

	public String userList(String senderPhone, String criterion)
			throws RemoteException;

	public String broadcastMyStatus(String senderPhone, String criterion)
			throws RemoteException;

	public boolean chatUp(String senderPhone, String nickname)
			throws RemoteException;

	public boolean checkRegistration(String phoneNumber) throws RemoteException;

	public String spamMobile(String message, String criterion)
			throws RemoteException;
	// __________________________
}
