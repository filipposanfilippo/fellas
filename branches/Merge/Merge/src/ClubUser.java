import java.util.ArrayList;


// server class
public class ClubUser {
	String name;
	String psw;
	ArrayList<MyEvent> eventList = new ArrayList<MyEvent>();
	
	public ClubUser(String name, String psw){
		this.name = name;
		this.psw = psw;
	}
}
