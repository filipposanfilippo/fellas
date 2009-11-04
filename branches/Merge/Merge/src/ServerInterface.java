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
}
