package fellas;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

class ClientMobile extends UnicastRemoteObject {
	private static ServerInterface server;
	private static String host = "localhost";
	private static String keyword ="perorapassworddiprova";

	// comunica con SmsReceiver
	protected ClientMobile() throws RemoteException {
		super();
		// TODO Auto-generated constructor stub
	}

	public static void main(String args[]) throws NotBoundException,
			IOException {
		// invoke remote methods
		server = (ServerInterface) Naming.lookup("//" + host + "/SvrMobile");
		// ClientMobile client = new ClientMobile();
		// to parser gms
		DatagramSocket serverSocket = new DatagramSocket(4444); // crea un
		// datagram
		// socket sulla
		// porta 4444
		// byte[] receiveData = new byte[1024];
		// byte[] sendData = new byte[1024];
		System.out.println("SERVER IS WAITING FOR REQUEST");
		boolean served;

		// server.createEvent(4, "spakkiu di eventu", "eShortDescription",
		// "eLongDescription", "siena", "eCategory",
		// "2009/11/21", "2009/11/26", "17:12", "17:32",
		// "no", "325888", "eImageURL");
		// System.out.println(server.spamMobile("stasera maudit!","uLocation = 'siena'"));//test
		while (true) {
			// server.spamMobile("ciao", "uLocation LIKE '%siena%'");
			served = false;
			byte[] receiveData = new byte[1024];
			byte[] sendData = new byte[1024];

			String[] splittedString = null;
			String serverAnswer = new String();
			DatagramPacket receivePacket = new DatagramPacket(receiveData,
					receiveData.length);
			serverSocket.receive(receivePacket);
			String sentence = new String(receivePacket.getData());

			sentence = sentence.substring(0, sentence.indexOf("%"));
			System.out.println("RECEIVED: " + sentence);

			// _____________
			String uTel = new String(sentence.substring(sentence
					.indexOf("from") + 5, sentence.indexOf(':') - 1));
			String temp2split = new String();
			if (sentence.endsWith("$") && sentence.contains("&")) {
				temp2split = sentence.substring(sentence.indexOf(':') + 1,
						sentence.lastIndexOf('$'));
				splittedString = temp2split.split("&");
				// _____________

				switch (splittedString[0].charAt(0)) {
				case 'r' | 'R': // note: status is not set...but sms is to
					// short!
					System.out.println("\nUSER REGISTRATION: ");
					// check registration by phone
					if (splittedString.length < 7) {
						serverAnswer = "SMS IS MALFORMED%";
						served = true;
						break;
					}

					String username = new String(splittedString[1]);
					String psw = new String(splittedString[2]);
					String uSex = new String(splittedString[3]);
					String uAge = new String(splittedString[4]);
					String uLocation = new String(splittedString[5]);
					String uPrivacy = new String(splittedString[6]);
					System.out.println("\nphone: " + uTel);
					System.out.println("\nusername: " + username);
					System.out.println("\npassword: " + psw);
					System.out.println("\nsex: " + uSex);
					System.out.println("\nage: " + uAge);
					System.out.println("\nlocation: " + uLocation);
					System.out.println("\nprivacy: " + uPrivacy);
					// invoke remote method
					serverAnswer = server.mobileRegistration(keyword, uTel, username,
							psw, uSex, uAge, uLocation, uPrivacy);
					// System.out.println("\nServerAnswer: " + serverAnswer);
					served = true;
					break;
				// TODO NEED TO FIX
				case 'e' | 'E':
					System.out.println("\nEVENTSLIST: ");
					// check registration by phone
					if (splittedString.length < 2) {
						serverAnswer = "SMS IS MALFORMED%";
						served = true;
						break;
					}

					String criterionE = new String(splittedString[1]);
					System.out.println("\nphone: " + uTel);
					System.out.println("\ncriterion: " + criterionE);
					// invoke remote method
					serverAnswer = server.eventsList(keyword, uTel, criterionE);
					System.out.println("\nfinito");
					served = true;
					break;
				case 'j' | 'J':
					System.out.println("\nJOIN EVENT: ");
					// check registration by phone
					if (splittedString.length < 2) {
						serverAnswer = "SMS IS MALFORMED%";
						served = true;
						break;
					}

					String eventCode = new String(splittedString[1]);
					System.out.println("\nphone: " + uTel);
					System.out.println("\neventCode: " + eventCode);
					// invoke remote method
					serverAnswer = server.joinEvent(keyword, uTel, eventCode);
					served = true;
					break;
				case 'i' | 'I':// TODO need to fix
					System.out.println("\nINVITE FRIEND: ");
					// check registration by phone
					if (splittedString.length < 3) {
						serverAnswer = "SMS IS MALFORMED%";
						served = true;
						break;
					}

					String friendPhone = new String(splittedString[1]);
					int eventId = Integer.valueOf(splittedString[2]);
					System.out.println("\nphone: " + uTel);
					System.out.println("\nfriendPhone: " + friendPhone);
					System.out.println("\neventId: " + eventId);
					// invoke remote method
					serverAnswer = server.inviteFriend(keyword, uTel, friendPhone,
							eventId);
					// serverAnswer = server.inviteFriend("+393202186626",
					// "+393280332489", 1);
					served = true;
					break;
				case 'l' | 'L':
					System.out.println("\nSET LOCATION: ");
					// check registration by phone --> it will be done in
					// Server.java
					if (splittedString.length < 2) {
						serverAnswer = "SMS IS MALFORMED%";
						served = true;
						break;
					}

					String uLocationL = new String(splittedString[1]);
					System.out.println("\nphone: " + uTel);
					System.out.println("\nmyLocation: " + uLocationL);
					// invoke remote method
					serverAnswer = server.setLocation(keyword, uTel, uLocationL);
					served = true;
					break;
				case 's' | 'S':
					System.out.println("\nSET STATUS: ");
					// check registration by phone --> it will be done in
					// Server.java
					if (splittedString.length < 2) {
						serverAnswer = "SMS IS MALFORMED%";
						served = true;
						break;
					}

					String uStatusS = new String(splittedString[1]);
					System.out.println("\nphone: " + uTel);
					System.out.println("\nmyStatus: " + uStatusS);
					// invoke remote method
					serverAnswer = server.setStatus(keyword, uTel, uStatusS);
					served = true;
					break;
				case 'p' | 'P':
					System.out.println("\nSET PRYVACY: ");
					// check registration by phone --> it will be done in
					// Server.java
					if (splittedString.length < 2) {
						serverAnswer = "SMS IS MALFORMED%";
						served = true;
						break;
					}

					int privacy = Integer.parseInt(splittedString[1]);
					System.out.println("\nphone: " + uTel);
					System.out.println("\nprivacy: " + privacy);
					// invoke remote method
					serverAnswer = server.setPrivacy(keyword, uTel, privacy);
					served = true;
					break;
				case 'u' | 'U':
					System.out.println("\nUSERS LIST: ");
					// check registration by phone
					if (splittedString.length < 2) {
						serverAnswer = "SMS IS MALFORMED%";
						served = true;
						break;
					}

					String criterionU = new String(splittedString[1]);
					System.out.println("\nphone: " + uTel);
					System.out.println("\ncriterion: " + criterionU);
					// invoke remote method
					serverAnswer = server.userList(keyword, uTel, criterionU);
					served = true;
					break;
				case 'b' | 'B':
					System.out.println("\nBROADCAST MY STATUS: ");
					// check registration by phone
					if (splittedString.length < 2) {
						serverAnswer = "SMS IS MALFORMED%";
						served = true;
						break;
					}

					String criterionB = new String(splittedString[1]);
					System.out.println("\nphone: " + uTel);
					System.out.println("\ncriterion: " + criterionB);
					// invoke remote method
					serverAnswer = server.broadcastMyStatus(keyword, uTel, criterionB);
					served = true;
					break;
				case 'c' | 'C':
					System.out.println("\nCHAT UP: ");
					// check registration by phone
					if (splittedString.length < 2) {
						serverAnswer = "SMS IS MALFORMED%";
						served = true;
						break;
					}

					String nicknameC = new String(splittedString[1]);
					System.out.println("\nphone: " + uTel);
					System.out.println("\nnicknameC: " + nicknameC);
					// invoke remote method
					serverAnswer = server.chatUp(keyword, uTel, nicknameC);
					served = true;
					break;
				case 'x' | 'X':
					System.out.println("\nUNREGISTRATION: ");

					System.out.println("\nphone: " + uTel);
					// invoke remote method
					serverAnswer = server.mobileUnregistration(keyword, uTel);
					served = true;
					break;
				case 'y' | 'Y':
					System.out.println("\nCHATUPANSWER: ");
					if (splittedString.length < 1) {
						serverAnswer = "SMS IS MALFORMED%";
						served = true;
						break;
					}
					String id = new String(splittedString[1]);
					System.out.println("\nphone: " + uTel);
					System.out.println("\nid: " + id);
					// invoke remote method
					serverAnswer = server.chatUpAnswer(keyword, uTel, id);
					served = true;
					break;
				}
				if (served) {
					InetAddress IPAddress = receivePacket.getAddress();
					int port = receivePacket.getPort();
					sendData = serverAnswer.getBytes();
					DatagramPacket sendPacket = new DatagramPacket(sendData,
							sendData.length, IPAddress, port);
					serverSocket.send(sendPacket);
					System.out.println("\nSERVER ANSWER: " + serverAnswer);
				}
			} else {
				serverAnswer = "SMS IS MALFORMED%";
				System.out.println("SMS is malformed!");
				/*
				 * il codice sotto è commentato perchè in fase di test
				 * l'emulatore fa sia da utente che da parser e quindi andrebbe
				 * in loop infinito se arriva un sms malformed. Ricordati di
				 * scommentare quando il sistema sarà finito InetAddress
				 * IPAddress = receivePacket.getAddress(); int port =
				 * receivePacket.getPort(); String capitalizedSentence =
				 * sentence.toUpperCase(); sendData =
				 * capitalizedSentence.getBytes(); sendData =
				 * serverAnswer.getBytes(); DatagramPacket sendPacket = new
				 * DatagramPacket(sendData, sendData.length, IPAddress, port);
				 * serverSocket.send(sendPacket);
				 */

			}

		}
	}
}