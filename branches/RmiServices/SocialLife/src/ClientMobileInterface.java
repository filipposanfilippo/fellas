import java.rmi.Remote;
import java.rmi.RemoteException;


public interface ClientMobileInterface extends Remote{
	public String getPsw() throws RemoteException;		//?
	public boolean register(String username, String password, String sex, String age) throws RemoteException;
	public String eventsList(String criterion) throws RemoteException;
	public boolean joinEvent(String eventCode)throws RemoteException;
	public boolean inviteFriend(String friendPhone)throws RemoteException;
	public boolean myLocation(String myLocation)throws RemoteException;
	public String userList(String criterion)throws RemoteException;
	public boolean broadcastMyStatus	(String criterion)throws RemoteException;
	public boolean chatUp(String nickname)throws RemoteException;
	public boolean checkRegistration(String phoneNumber)throws RemoteException;
}
