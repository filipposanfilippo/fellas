import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;


public class ClientClub extends UnicastRemoteObject{
	private static ServerInterface server;
	private static String host = "localhost";
	private static String name = "nameDiProva";
	private static String psw = "pswDiProva";

	protected ClientClub() throws RemoteException {
		super();
		// TODO Auto-generated constructor stub
	}
	
	protected boolean firstAccess(String name, String psw) throws RemoteException{
		return server.registerClub(name, psw);
	}
	
	protected boolean addEvent(MyEvent e) throws RemoteException{
		return server.addEvent(name, psw, e);
	}
	
	protected String showClubEvent() throws RemoteException{
		return server.showClubEvent(name, psw);
	}

	public static void setServer(ServerInterface server) {
		ClientClub.server = server;
	}

	public static ServerInterface getServer() {
		return server;
	}
	
	public static void main(String[] args) throws RemoteException,MalformedURLException,NotBoundException{
		setServer((ServerInterface)Naming.lookup("//" + host + "/SvrMobile"));
		ClientClub client = new ClientClub();
		client.firstAccess(name, psw);
		System.out.println(server.showClub());
		
		//MyEvent m = server.createEvent("evento", "descrizione");
		
		MyEvent m = new MyEvent("che evento!","chiu pilu pi tutti");
		client.addEvent(m);
		System.out.println(client.showClubEvent());
	}
	

}
