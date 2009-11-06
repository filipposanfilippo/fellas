package fellas;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;


public class ClientClub extends UnicastRemoteObject {
	private ServerInterface server;
	private String host = "localhost";
	private String name = "";
	private char[] psw;

	public String getName() {
		return name;
	}

	public char[] getPsw() {
		return psw;
	}

	protected ClientClub() throws RemoteException, MalformedURLException,
			NotBoundException {
		server = (ServerInterface) Naming.lookup("//" + host + "/SvrMobile");
	}

	protected boolean access(String name, char[] cs) throws RemoteException {
		boolean res = server.access(name, cs);
		if (res) {
			this.name = name;
			this.psw = cs;
		}
		return res;
	}

	protected boolean clubRegistration(String oName, String oSurname,
			String cAddress, String cTel, String cName, char[] cs)
			throws RemoteException {
		return server.clubRegistration(oName, oSurname, cAddress, cTel, cName,
				cs);
	}

	protected boolean addEvent(MyEvent e) throws RemoteException {
		return server.addEvent(name, psw, e);
	}

	protected String showClubEvent() throws RemoteException {
		return server.showClubEvent(name, psw);
	}
}
