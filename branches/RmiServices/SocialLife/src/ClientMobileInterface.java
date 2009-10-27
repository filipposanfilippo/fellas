import java.rmi.Remote;
import java.rmi.RemoteException;


public interface ClientMobileInterface extends Remote{
	public String getPsw() throws RemoteException;
}
