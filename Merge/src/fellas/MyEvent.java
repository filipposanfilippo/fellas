package fellas;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;


public class MyEvent implements Serializable{
	String name;
	String description;
	Club club;
	MobileUser mList[];
	
	public MyEvent(String name, String description, Club club){
		super();
		this.name = name;
		this.description = description;
		this.club = club;
	}

	public MyEvent(String name, String description){
		super();
		this.name = name;
		this.description = description;
	}
}