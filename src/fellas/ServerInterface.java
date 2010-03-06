package fellas;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Date;
import java.util.LinkedList;

public interface ServerInterface extends Remote {

	public boolean clubAuthentication(String name, String cs)
			throws RemoteException;
	
	public String userAuthentication(String username, String psw)
		throws RemoteException;

	public boolean clubRegistration(String oName, String oSurname,
			String cAddress, String cTel, String cEmail, String cType,
			String cName, String username, String psw, String cImageURL)
			throws RemoteException;

	public boolean clubUnregistration(String cName, String psw)
			throws RemoteException;

	public Club getClubData(String cName, String psw) throws RemoteException;

	public Club getClub(String key, String senderTel, String scope, String value)
	throws RemoteException;
	
	public User getUser(String key, String senderTel, String scope, String value)
	throws RemoteException;
	
	public boolean updateClubData(String cName, String psw, Club club)
			throws RemoteException;

	public LinkedList<MyEvent> getClubEventsList(String cName, String psw,
			int cId, String table) throws RemoteException;

	public MyEvent getEvent(int id, String table) throws RemoteException;

	public boolean createEvent(String cName, String psw, int cId, String eName,
			String eShortDescription, String eLongDescription,
			String eLocation, String eCategory, Date eStartDate,
			Date eFinishDate, String eStartTime, String eFinishTime,
			String eRestriction, String infoTel, String imageURL)
			throws RemoteException;

	public boolean updateEvent(String cName, String psw, MyEvent event)
			throws RemoteException;

	public boolean deleteEvent(String cName, String psw, int eventId)
			throws RemoteException;

	public LinkedList<User> getEventUsersList(String cName, String psw,
			int eventId) throws RemoteException;

	public String spamMobile(String cName, String psw, String message,
			String criterion) throws RemoteException;

	// public MobileUser[] getMobileList(String sqlString) throws
	// RemoteException;

	// public LinkedList<Club> getClubList(String key) throws RemoteException;

	// public boolean registerMobile() throws RemoteException;
	// _________________________

	public String mobileRegistration(String key, String uTel, String username,
			String psw, String uSex, String uAge, String uLocation,
			String uPrivacy) throws RemoteException;

	public boolean authenticationMobile() throws RemoteException;

	public String mobileUnregistration(String key, String uTel)
			throws RemoteException;

	public String eventsList(String key, String senderPhone, String criterion)
			throws RemoteException;

	public String disJoinEvent(String key, String senderPhone, String eventCode)
			throws RemoteException;

	public String joinEvent(String key, String senderPhone, String eventCode)
			throws RemoteException;

	public String getEventDescription(String key, String senderPhone,
			String eventCode) throws RemoteException;

	public String getUserDescription(String key, String senderPhone,
			String username) throws RemoteException;

	public String getClubDescription(String key, String senderPhone,
			int id) throws RemoteException;

	public String inviteFriend(String key, String senderPhone,
			String friendPhone, int eventId) throws RemoteException;

	public String setLocation(String key, String uTel, String uLocation)
			throws RemoteException;

	public String setStatus(String key, String uTel, String uStatus)
			throws RemoteException;

	public String setPrivacy(String key, String uTel, int privacy)
			throws RemoteException;

	public String usersList(String key, String senderPhone, String criterion)
			throws RemoteException;

	public String clubsList(String key, String senderPhone, String criterion)
			throws RemoteException;

	public String broadcastMyStatus(String key, String senderPhone,
			String criterion) throws RemoteException;

	public String chatUp(String key, String senderTel, String username)
			throws RemoteException;

	public String chatUpAnswer(String key, String senderTel, String id)
			throws RemoteException;

	public boolean checkRegistration(String key, String phoneNumber)
			throws RemoteException;

	// _________________________
}
