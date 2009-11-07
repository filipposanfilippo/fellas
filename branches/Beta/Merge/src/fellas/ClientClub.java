package fellas;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.LinkedList;

public class ClientClub extends UnicastRemoteObject {
	private ServerInterface server;
	private String host = "localhost";
	private String clubName = "";
	private String psw;

	public String getName() {
		return clubName;
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
			this.clubName = name;
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

	protected LinkedList<Club> getClubList() throws RemoteException {
		return server.getClubList();
	}

	protected Club getClubData() throws RemoteException {
		System.out.println("CAZZU CAZZU");
		System.out.println(server.getClubData(clubName));
		return server.getClubData(clubName);
	}

}
