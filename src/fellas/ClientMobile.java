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
	private static String keyword = "perorapassworddiprova";

	protected ClientMobile() throws RemoteException {
		super();
	}

	public static void main(String args[]) throws NotBoundException,
			IOException {
		// makes lookup to invoke remote methods from RMI Server
		server = (ServerInterface) Naming.lookup("//" + host + "/SvrMobile");

		// UDP Server. Waiting for SMS Parser requestes on port 4444
		DatagramSocket serverSocket = new DatagramSocket(4444);

		System.out.println("UDP SERVER IS WAITING FOR REQUEST");

		boolean served;

		while (true) {
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

			String uTel = new String(sentence.substring(sentence
					.indexOf("from") + 5, sentence.indexOf(':') - 1));
			String temp2split = new String();
			if (sentence.endsWith("$") && sentence.contains("&")) {
				temp2split = sentence.substring(sentence.indexOf(':') + 1,
						sentence.lastIndexOf('$'));
				// escape for '
				temp2split = temp2split.replace("'", "\\'");
				System.out.println(temp2split);
				splittedString = temp2split.split("&");

				switch (splittedString[0].charAt(0)) {
				case 'r' | 'R':
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
					serverAnswer = server.userRegistration(keyword, uTel,
							username, psw, uSex, uAge, uLocation, uPrivacy, "",
							"Appena iscritto in fellas", "", "");

					served = true;
					break;
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
				case 'g' | 'G':
					System.out.println("\nGET EVENT DESCRIPTION: ");
					// check registration by phone
					if (splittedString.length < 2) {
						serverAnswer = "SMS IS MALFORMED%";
						served = true;
						break;
					}

					String eventCodeG = new String(splittedString[1]);
					System.out.println("\nphone: " + uTel);
					System.out.println("\neventCode: " + eventCodeG);
					// invoke remote method
					serverAnswer = server.getEventDescription(keyword, uTel,
							eventCodeG);
					served = true;
					break;
				case 'f' | 'F':
					System.out.println("\nGET USER DESCRIPTION: ");
					// check registration by phone
					if (splittedString.length < 2) {
						serverAnswer = "SMS IS MALFORMED%";
						served = true;
						break;
					}

					String usernameF = new String(splittedString[1]);
					System.out.println("\nphone: " + uTel);
					System.out.println("\nusername: " + usernameF);
					// invoke remote method
					serverAnswer = server.getUserDescription(keyword, uTel,
							usernameF);
					served = true;
					break;
				case 'h' | 'H':
					System.out.println("\nGET CLUB DESCRIPTION: ");
					// check registration by phone
					if (splittedString.length < 2) {
						serverAnswer = "SMS IS MALFORMED%";
						served = true;
						break;
					}
					// TODO METTERE id del club al posto di cName
					String clubId = new String(splittedString[1]);
					System.out.println("\nphone: " + uTel);
					System.out.println("\ncName: " + clubId);
					// invoke remote method
					serverAnswer = server.getClubDescription(keyword, uTel,
							Integer.parseInt(clubId));
					served = true;
					break;
				case 'd' | 'D':
					System.out.println("\nDISJOIN EVENT: ");
					// check registration by phone
					if (splittedString.length < 2) {
						serverAnswer = "SMS IS MALFORMED%";
						served = true;
						break;
					}

					String eventCodeD = new String(splittedString[1]);
					System.out.println("\nphone: " + uTel);
					System.out.println("\neventCode: " + eventCodeD);
					// invoke remote method
					serverAnswer = server.disJoinEvent(keyword, uTel,
							eventCodeD);
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
					serverAnswer = server.inviteFriend(keyword, uTel,
							friendPhone, eventId);

					served = true;
					break;
				case 'l' | 'L':
					System.out.println("\nSET LOCATION: ");
					if (splittedString.length < 2) {
						serverAnswer = "SMS IS MALFORMED%";
						served = true;
						break;
					}

					String uLocationL = new String(splittedString[1]);
					System.out.println("\nphone: " + uTel);
					System.out.println("\nmyLocation: " + uLocationL);
					// invoke remote method
					serverAnswer = server
							.setLocation(keyword, uTel, uLocationL);
					served = true;
					break;
				case 's' | 'S':
					System.out.println("\nSET STATUS: ");
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
					serverAnswer = server.usersList(keyword, uTel, criterionU);
					served = true;
					break;
				case 'n' | 'N':
					System.out.println("\nCLUBS LIST: ");
					// check registration by phone
					if (splittedString.length < 2) {
						serverAnswer = "SMS IS MALFORMED%";
						served = true;
						break;
					}

					String criterionN = new String(splittedString[1]);
					System.out.println("\nphone: " + uTel);
					System.out.println("\ncriterion: " + criterionN);
					// invoke remote method
					serverAnswer = server.clubsList(keyword, uTel, criterionN);
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
					serverAnswer = server.broadcastMyStatus(keyword, uTel,
							criterionB);
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
				InetAddress IPAddress = receivePacket.getAddress();
				int port = receivePacket.getPort();
				String capitalizedSentence = sentence.toUpperCase();
				sendData = capitalizedSentence.getBytes();
				sendData = serverAnswer.getBytes();
				DatagramPacket sendPacket = new DatagramPacket(sendData,
						sendData.length, IPAddress, port);
				serverSocket.send(sendPacket);

			}

		}
	}
}