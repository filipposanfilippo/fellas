import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ServerInterface extends Remote {
	public boolean authenticationClub(String name, String psw)
			throws RemoteException;

	public boolean authenticationMobile() throws RemoteException;

	public MobileUser[] getMobileList(String sqlString) throws RemoteException;

	public ClubUser[] getClubList(String sqlString) throws RemoteException;

	public boolean access(String name, String psw) throws RemoteException;

	public boolean clubRegistration(String oName, String oSurname,
			String cAddress, String cTel, String cName, String psw)
			throws RemoteException;

	public String showClub() throws RemoteException;

	public String showEvent() throws RemoteException;

	public String showClubEvent(String name, String psw) throws RemoteException;

	public boolean addEvent(String name, String psw, MyEvent e)
			throws RemoteException;

	// public boolean registerMobile() throws RemoteException;

	public MyEvent createEvent(String nameEvent, String description)
			throws RemoteException;

	// __________________________
	public boolean register(String senderPhone, String username,
			String password, String sex, String age) throws RemoteException;

	public String eventsList(String senderPhone, String criterion)
			throws RemoteException;

	public boolean joinEvent(String senderPhone, String eventCode)
			throws RemoteException;

	public boolean inviteFriend(String senderPhone, String friendPhone)
			throws RemoteException;

	public boolean myLocation(String senderPhone, String myLocation)
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
