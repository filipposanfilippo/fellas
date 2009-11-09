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

	public boolean authenticationMobile() throws RemoteException;

	public MobileUser[] getMobileList(String sqlString) throws RemoteException;

	public boolean addEvent(String name, String psw, MyEvent e)
			throws RemoteException;

	// public boolean registerMobile() throws RemoteException;

	public MyEvent createEvent(String nameEvent, String description)
			throws RemoteException;

	// __________________________

	public String mobileRegistration(String uTel, String username, String psw,
			String uSex, String uAge, String uLocation) throws RemoteException;

	public String eventsList(String senderPhone, String criterion)
			throws RemoteException;

	public boolean joinEvent(String senderPhone, String eventCode)
			throws RemoteException;

	public boolean inviteFriend(String senderPhone, String friendPhone,
			String event) throws RemoteException;

	public String setLocation(String uTel, String uLocation)
			throws RemoteException;

	public String userList(String senderPhone, String criterion)
			throws RemoteException;

	public boolean broadcastMyStatus(String senderPhone, String criterion)
			throws RemoteException;

	public boolean chatUp(String senderPhone, String nickname)
			throws RemoteException;

	public boolean checkRegistration(String phoneNumber) throws RemoteException;
	// __________________________
}
