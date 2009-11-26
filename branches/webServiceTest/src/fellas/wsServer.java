package fellas;

import java.rmi.RemoteException;
import java.util.Date;
import java.util.LinkedList;

public class wsServer implements ServerInterface{

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		//some debug code
	}

	
	@Override
	public boolean authenticationClub(String name, String psw)
			throws RemoteException {
		// TODO Auto-generated method stub
		return false;
	}

	
	@Override
	public boolean authenticationMobile() throws RemoteException {
		// TODO Auto-generated method stub
		return false;
	}

	
	@Override
	public String broadcastMyStatus(String key, String senderPhone,
			String criterion) throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}

	
	@Override
	public String chatUp(String key, String senderTel, String username)
			throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}

	
	@Override
	public String chatUpAnswer(String key, String senderTel, String id)
			throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}

	
	@Override
	public boolean checkRegistration(String key, String phoneNumber)
			throws RemoteException {
		// TODO Auto-generated method stub
		return false;
	}

	
	@Override
	public boolean clubAuthentication(String name, String cs)
			throws RemoteException {
		// TODO Auto-generated method stub
		return false;
	}

	
	@Override
	public boolean clubRegistration(String oName, String oSurname,
			String cAddress, String cTel, String cEMail, String cType,
			String cName, String username, String psw, String cImageURL)
			throws RemoteException {
		// TODO Auto-generated method stub
		return false;
	}

	
	@Override
	public boolean clubUnregistration(String cName, String psw)
			throws RemoteException {
		// TODO Auto-generated method stub
		return false;
	}

	
	@Override
	public boolean createEvent(String cName, String psw, int cId, String eName,
			String eShortDescription, String eLongDescription,
			String eLocation, String eCategory, Date eStartDate,
			Date eFinishDate, String eStartTime, String eFinishTime,
			String eRestriction, String infoTel, String imageURL)
			throws RemoteException {
		// TODO Auto-generated method stub
		return false;
	}

	
	@Override
	public boolean deleteEvent(String cName, String psw, int eventId)
			throws RemoteException {
		// TODO Auto-generated method stub
		return false;
	}

	
	@Override
	public String disJoinEvent(String key, String senderPhone, String eventCode)
			throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}

	
	@Override
	public String eventsList(String key, String senderPhone, String criterion)
			throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}

	
	@Override
	public Club getClubData(String cName, String psw) throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}

	
	@Override
	public String getClubDescription(String key, String senderPhone,
			String cName) throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}

	
	@Override
	public LinkedList<MyEvent> getClubEventsList(String cName, String psw,
			int cId, String table) throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}

	
	@Override
	public MyEvent getEvent(int id, String table) throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}

	
	@Override
	public String getEventDescription(String key, String senderPhone,
			String eventCode) throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}

	
	@Override
	public LinkedList<User> getEventUsersList(String cName, String psw,
			int eventId) throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}

	
	@Override
	public MobileUser[] getMobileList(String sqlString) throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}

	
	@Override
	public String getUserDescription(String key, String senderPhone,
			String username) throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}

	
	@Override
	public String inviteFriend(String key, String senderPhone,
			String friendPhone, int eventId) throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}

	
	@Override
	public String joinEvent(String key, String senderPhone, String eventCode)
			throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}

	
	@Override
	public String mobileRegistration(String key, String uTel, String username,
			String psw, String uSex, String uAge, String uLocation,
			String uPrivacy) throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}

	
	@Override
	public String mobileUnregistration(String key, String uTel)
			throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}

	
	@Override
	public String setLocation(String key, String uTel, String uLocation)
			throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}

	
	@Override
	public String setPrivacy(String key, String uTel, int privacy)
			throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}

	
	@Override
	public String setStatus(String key, String uTel, String uStatus)
			throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}

	
	@Override
	public String spamMobile(String cName, String psw, String message,
			String criterion) throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}

	
	@Override
	public boolean updateClubData(String cName, String psw, Club club)
			throws RemoteException {
		// TODO Auto-generated method stub
		return false;
	}

	
	@Override
	public boolean updateEvent(String cName, String psw, MyEvent event)
			throws RemoteException {
		// TODO Auto-generated method stub
		return false;
	}

	
	@Override
	public String userList(String key, String senderPhone, String criterion)
			throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}

	
}
