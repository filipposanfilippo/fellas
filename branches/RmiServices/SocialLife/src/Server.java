import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Iterator;


public class Server extends UnicastRemoteObject implements ServerInterface {
	//protected ArrayList<ClientInterface> clients = new ArrayList<ClientInterface>();
	protected ArrayList<MobileUser> mobileList = new ArrayList<MobileUser>();
	protected ArrayList<ClubUser> clubList = new ArrayList<ClubUser>();
	protected ArrayList<MyEvent> eventList = new ArrayList<MyEvent>();
	
	public Server() throws RemoteException {
	}
	
	public boolean authenticationClub(String name, String psw) throws RemoteException {
		// invoke client club method to check user-password
		return false;
	}

	public boolean authenticationMobile() throws RemoteException {
		// invoke client club method to check user-password
		return false;
	}

	@Override
	public ClubUser[] getClubList(String sqlString) throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MobileUser[] getMobileList(String sqlString) throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean registerClub(String name, String psw) throws RemoteException {
		// TODO check to unique user
		if (getClubUser(name)!=null)
			return false;
		clubList.add(new ClubUser(name, psw));
		return true;
	}
	
	public ClubUser getClubUser(String name){
		ClubUser c;
		for( Iterator<ClubUser> i = clubList.iterator(); i.hasNext(); ){
			c = (ClubUser) i.next();
			if (c.name.equals(name))
				return c;
		}
		return null;
	}
	
	public MobileUser getMobileUser(String name){
		MobileUser c;
		for( Iterator<MobileUser> i = mobileList.iterator(); i.hasNext(); ){
			c = (MobileUser) i.next();
			if (c.name.equals(name))
				return c;
		}
		return null;
	}
	
	@Override
	public String showClub() throws RemoteException {
		// TODO to change, now it's only for debug
		String clubs = "";
		ClubUser c;
		for( Iterator<ClubUser> i = clubList.iterator(); i.hasNext(); ){
			c = (ClubUser) i.next();
			clubs += c.name;
			System.out.println(c.name);
		}
		return clubs;
	}
	
	public String showEvent() throws RemoteException {
		// TODO to change, now it's only for debug
		String event = "";
		MyEvent c;
		for( Iterator<MyEvent> i = eventList.iterator(); i.hasNext(); ){
			c = (MyEvent) i.next();
			event += c.name;
			System.out.println(c.name);
		}
		return event;
	}
	
	public String showClubEvent(String name, String psw) throws RemoteException {
		// TODO to change, now it's only for debug
		//if (authenticationClub(name,psw)==false)
		//	return null;	//TODO return an error message instead null
		String event = "";
		MyEvent c;
		for( Iterator<MyEvent> i = eventList.iterator(); i.hasNext(); ){
			c = (MyEvent) i.next();
			event += c.name;
			System.out.println(c.name);
		}
		return event;
	}
	
	public boolean addEvent(String name, String psw, MyEvent e) throws RemoteException {
		// TODO Auto-generated method stub
		//if (authenticationClub(name,psw)==false)
		//	return false;
		eventList.add(e);					// add in event list
		getClubUser(name).eventList.add(e);	// add in club event list
		return true;
	}

	public MyEvent createEvent(String nameEvent, String description) throws RemoteException{
		return new MyEvent(nameEvent, description);
	}
	
	public static void main(String[] args) throws RemoteException,MalformedURLException,NotBoundException{
		try {
			Naming.rebind("SvrMobile", new Server());
			System.out.println("Server is ready");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
