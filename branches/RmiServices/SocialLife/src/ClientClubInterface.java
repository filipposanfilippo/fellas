import java.rmi.Remote;
import java.rmi.RemoteException;


public interface ClientClubInterface extends Remote{
	public String getPsw() throws RemoteException;
}
