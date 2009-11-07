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
	private String psw;

	public String getName() {
		return name;
	}

	public String getPsw() {
		return psw;
	}

	protected ClientClub() throws RemoteException, MalformedURLException,
			NotBoundException {
		server = (ServerInterface) Naming.lookup("//" + host + "/SvrMobile");
	}

	protected boolean access(String name, String cs) throws RemoteException {
		boolean res = server.access(name, cs);
		if (res) {
			this.name = name;
			this.psw = cs;
		}
		return res;
	}

	protected boolean clubRegistration(String oName, String oSurname,
			String cAddress, String cTel, String cName, String psw)
			throws RemoteException {
		return server.clubRegistration(oName, oSurname, cAddress, cTel, cName,
				psw);
	}

	protected boolean addEvent(MyEvent e) throws RemoteException {
		return server.addEvent(name, psw, e);
	}

	protected String showClubEvent() throws RemoteException {
		return server.showClubEvent(name, psw);
	}
}
