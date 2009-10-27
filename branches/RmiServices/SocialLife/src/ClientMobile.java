import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class ClientMobile extends UnicastRemoteObject{
	private static ServerInterface server;
	private static String host = "localhost";
	private static String psw = "pswDiProva";
	protected ClientMobile() throws RemoteException {
		super();
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) throws RemoteException,MalformedURLException,NotBoundException{
		setServer((ServerInterface)Naming.lookup("//" + host + "/SvrMobile"));
		ClientMobile client = new ClientMobile();
		server.showClub();
	}

	public static void setServer(ServerInterface server) {
		ClientMobile.server = server;
	}

	public static ServerInterface getServer() {
		return server;
	}
}
