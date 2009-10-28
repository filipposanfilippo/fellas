import java.rmi.Remote;
import java.rmi.RemoteException;


public interface ClientMobileInterface extends Remote{
	public String getPsw() throws RemoteException;		//?
	public boolean register(String senderPhone, String username, String password, String sex, String age) throws RemoteException;
	public String eventsList(String senderPhone, String criterion) throws RemoteException;
	public boolean joinEvent(String senderPhone, String eventCode)throws RemoteException;
	public boolean inviteFriend(String senderPhone, String friendPhone)throws RemoteException;
	public boolean myLocation(String senderPhone, String myLocation)throws RemoteException;
	public String userList(String senderPhone, String criterion)throws RemoteException;
	public boolean broadcastMyStatus	(String senderPhone, String criterion)throws RemoteException;
	public boolean chatUp(String senderPhone, String nickname)throws RemoteException;
	public boolean checkRegistration(String phoneNumber)throws RemoteException;
}
