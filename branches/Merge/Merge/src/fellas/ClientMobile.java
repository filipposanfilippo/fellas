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

	protected ClientMobile() throws RemoteException {
		super();
		// TODO Auto-generated constructor stub
	}

	public static void main(String args[]) throws NotBoundException,
			IOException {
		// invoke remote methods
		server = (ServerInterface) Naming.lookup("//" + host + "/SvrMobile");
		ClientMobile client = new ClientMobile();
		// to parser gms
		DatagramSocket serverSocket = new DatagramSocket(4444); // crea un
		// datagram
		// socket sulla
		// porta 4444
		// byte[] receiveData = new byte[1024];
		// byte[] sendData = new byte[1024];
		System.out.println("SERVER IS WAITING FOR REQUEST");
		while (true) {
			byte[] receiveData = new byte[1024];
			byte[] sendData = new byte[1024];
			int pointer = 0;
			String serverAnswer = new String();
			DatagramPacket receivePacket = new DatagramPacket(receiveData,
					receiveData.length);
			serverSocket.receive(receivePacket);
			String sentence = new String(receivePacket.getData());
			System.out.println("RECEIVED: " + sentence);
			switch (sentence.charAt(sentence.indexOf(':') + 1)) {
			case 'r' | 'R':
				System.out.println("\nUSER REGISTRATION: ");
				// check registration by phone
				String phone = new String(sentence.substring(sentence
						.indexOf("from") + 5, sentence.indexOf(':') - 1));
				String username = new String(sentence.substring(sentence
						.indexOf('&') + 1, sentence.indexOf('&', sentence
						.indexOf('&') + 1)));
				pointer += 3 + sentence.indexOf(':') + username.length();
				String password = new String(sentence.substring(pointer + 1,
						sentence.indexOf('&', pointer + 1)));
				pointer += password.length() + 1;
				String sex = new String(sentence.substring(pointer + 1,
						sentence.indexOf('&', pointer + 1)));
				pointer += sex.length() + 1;
				String age = new String(sentence.substring(pointer + 1,
						sentence.indexOf('&', pointer + 1)));
				pointer += age.length() + 1;
				String state = new String(sentence.substring(pointer + 1,
						sentence.indexOf('$')));
				System.out.println("\nphone: " + phone);
				System.out.println("\nusername: " + username);
				System.out.println("\npassword: " + password);
				System.out.println("\nsex: " + sex);
				System.out.println("\nage: " + age);
				System.out.println("\nstate: " + state);
				// invoke remote method
				// serverAnswer = client.register(phone, username, password,
				// sex, age);
				break;
			case 'e' | 'E':
				System.out.println("\nEVENTSLIST: ");
				// check registration by phone
				String phoneCheckE = new String(sentence.substring(sentence
						.indexOf("from") + 5, sentence.indexOf(':') - 1));
				String criterionE = new String(sentence.substring(sentence
						.indexOf('&') + 1, sentence.indexOf('$')));
				System.out.println("\nphone: " + phoneCheckE);
				System.out.println("\ncriterion: " + criterionE);
				// invoke remote method
				// serverAnswer = client.eventsList(phoneCheckE, criterionE);
				break;
			case 'j' | 'J':
				System.out.println("\nJOIN EVENT: ");
				// check registration by phone
				String phoneCheckJ = new String(sentence.substring(sentence
						.indexOf("from") + 5, sentence.indexOf(':') - 1));
				String eventCode = new String(sentence.substring(sentence
						.indexOf('&') + 1, sentence.indexOf('$')));
				System.out.println("\nphone: " + phoneCheckJ);
				System.out.println("\neventCode: " + eventCode);
				// invoke remote method
				// serverAnswer = client.joinEvent(phoneCheckJ, eventCode);
				break;
			case 'i' | 'I':
				System.out.println("\nINVITE FRIEND: ");
				// check registration by phone
				String phoneCheckI = new String(sentence.substring(sentence
						.indexOf("from") + 5, sentence.indexOf(':') - 1));
				String friendPhone = new String(sentence.substring(sentence
						.indexOf('&') + 1, sentence.indexOf('$')));
				System.out.println("\nphone: " + phoneCheckI);
				System.out.println("\nfriendPhone: " + friendPhone);
				// invoke remote method
				// serverAnswer = client.inviteFriend( phoneCheckI,
				// friendPhone);
				break;
			case 'm' | 'M':
				System.out.println("\nMY LOCATION: ");
				// check registration by phone
				String phoneCheckM = new String(sentence.substring(sentence
						.indexOf("from") + 5, sentence.indexOf(':') - 1));
				String myLocation = new String(sentence.substring(sentence
						.indexOf('&') + 1, sentence.indexOf('$')));
				System.out.println("\nphone: " + phoneCheckM);
				System.out.println("\nmyLocation: " + myLocation);
				// invoke remote method
				// serverAnswer = client.myLocation(phoneCheckM, myLocation);
				break;
			case 'u' | 'U':
				System.out.println("\nUSERS LIST: ");
				// check registration by phone
				String phoneCheckU = new String(sentence.substring(sentence
						.indexOf("from") + 5, sentence.indexOf(':') - 1));
				String criterionU = new String(sentence.substring(sentence
						.indexOf('&') + 1, sentence.indexOf('$')));
				System.out.println("\nphone: " + phoneCheckU);
				System.out.println("\ncriterion: " + criterionU);
				// invoke remote method
				// serverAnswer = client.userList(phoneCheckU, criterionU);
				break;
			case 'b' | 'B':
				System.out.println("\nBROADCAST MY STATUS: ");
				// check registration by phone
				String phoneCheckB = new String(sentence.substring(sentence
						.indexOf("from") + 5, sentence.indexOf(':') - 1));
				String criterionB = new String(sentence.substring(sentence
						.indexOf('&') + 1, sentence.indexOf('$')));
				System.out.println("\nphone: " + phoneCheckB);
				System.out.println("\ncriterion: " + criterionB);
				// invoke remote method
				// serverAnswer = client.broadcastMyStatus(phoneCheckB, String
				// criterionB);
				break;
			case 'c' | 'C':
				System.out.println("\nCHAT UP: ");
				// check registration by phone
				String phoneCheckC = new String(sentence.substring(sentence
						.indexOf("from") + 5, sentence.indexOf(':') - 1));
				String nicknameC = new String(sentence.substring(sentence
						.indexOf('&') + 1, sentence.indexOf('$')));
				System.out.println("\nphone: " + phoneCheckC);
				System.out.println("\nnicknameC: " + nicknameC);
				// invoke remote method
				// serverAnswer = client.chatUp(phoneCheckC, nicknameC);
				break;
			}
			InetAddress IPAddress = receivePacket.getAddress();
			int port = receivePacket.getPort();
			// String capitalizedSentence = sentence.toUpperCase();
			// sendData = capitalizedSentence.getBytes();
			sendData = serverAnswer.getBytes();
			DatagramPacket sendPacket = new DatagramPacket(sendData,
					sendData.length, IPAddress, port);
			serverSocket.send(sendPacket);
		}
	}
}