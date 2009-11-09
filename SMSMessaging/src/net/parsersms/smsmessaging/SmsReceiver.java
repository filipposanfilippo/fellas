package net.parsersms.smsmessaging;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.gsm.SmsManager;
import android.telephony.gsm.SmsMessage;
import android.util.Log;
import android.widget.Toast;

public class SmsReceiver extends BroadcastReceiver {
	String uTel;

	@Override
	public void onReceive(Context context, Intent intent) {
		// ---get the SMS message passed in---
		Bundle bundle = intent.getExtras();
		SmsMessage[] msgs = null;
		String str = "";
		if (bundle != null) {
			// ---retrieve the SMS message received---
			Object[] pdus = (Object[]) bundle.get("pdus");
			msgs = new SmsMessage[pdus.length];
			for (int i = 0; i < msgs.length; i++) {
				msgs[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
				uTel = new String(msgs[i].getOriginatingAddress());
				str += "SMS from " + msgs[i].getOriginatingAddress();
				str += " :";
				str += msgs[i].getMessageBody().toString();
				str += "%\n";
			}
			// ---display the new SMS message---
			Toast.makeText(context, str, Toast.LENGTH_SHORT).show();
			Log.d("SMS", "SMS received: I'm lunching the client thread...");
			// Kickoff the Client
			new Thread(new ClientThread(str)).start();
		}
	}

	class ClientThread extends Thread {
		byte[] buf = "".getBytes();

		public ClientThread(String msg) {
			buf = msg.getBytes();
		}

		@Override
		public void run() {
			Log.d("Thread ClientS", "Thread Client: Running...");
			int broadcastCounter = 0;
			String temp2split = new String();
			String[] splittedString = null;
			try {
				Log.d("UDP", "C: Connecting...");
				DatagramSocket clientSocket = new DatagramSocket();
				InetAddress IPAddress = InetAddress.getByName("10.0.2.2");// 10.0.2.2
				// funziona
				// in
				// emulazione,
				// per
				// girare
				// su
				// terminale
				// prova
				// a
				// mettere
				// indirizzo
				// locale
				// del
				// server.java
				// 192.168.182.21
				int SERVERPORT = 4444;
				byte[] sendData = new byte[1024];
				byte[] receiveData = new byte[1024];

				sendData = buf;
				Log.d("UDP", "C: Sending:" + new String(buf));
				DatagramPacket sendPacket = new DatagramPacket(sendData,
						sendData.length, IPAddress, SERVERPORT);
				clientSocket.send(sendPacket);
				Log.d("UDP", "C: Sent.");
				DatagramPacket receivePacket = new DatagramPacket(receiveData,
						receiveData.length);
				clientSocket.receive(receivePacket);
				String modifiedSentence = new String(receivePacket.getData());
				// modifiedSentence = modifiedSentence.substring(0,
				// modifiedSentence.indexOf("%"));
				Log.d("UDP", "ANSWER FROM SERVER:" + modifiedSentence);
				Log.d("UDP", "C: sms from " + uTel);
				
				if (modifiedSentence.charAt(0) == '@') // it is a broadcast sms
					sendBroadcast(modifiedSentence);
				else {
					// is a single sms
					SmsManager sms = SmsManager.getDefault();
					sms.sendTextMessage(uTel, null, modifiedSentence.substring(
							0, modifiedSentence.indexOf("%")), null, null);
				}

				clientSocket.close();
			} catch (Exception e) {
				Log.e("Thread ClientS", "C: Error", e);
			}

		}
	}

	class ServerThread extends Thread {
		public static final String SERVERIP = "10.0.2.2"; // 'Within' the
		// emulator!
		public static final int SERVERPORT = 5555; // remember to open port on

		// router

		public void run() {
			try {
				/* Retrieve the ServerName */
				InetAddress serverAddr = InetAddress.getByName(SERVERIP);

				Log.d("UDP", "S: Connecting...");
				/* Create new UDP-Socket */
				DatagramSocket serverSocket = new DatagramSocket(SERVERPORT,
						serverAddr);
				Log.d("UDP", "S: waiting for request.");

				while (true) {
					byte[] receiveData = new byte[1024];
					// byte[] sendData = new byte[1024];

					DatagramPacket receivePacket = new DatagramPacket(
							receiveData, receiveData.length);
					serverSocket.receive(receivePacket);
					String sentence = new String(receivePacket.getData());

					sentence = sentence.substring(0, sentence.indexOf("%"));
					Log.d("UDP", "S: received: " + sentence);
				}
			} catch (Exception e) {
				Log.e("UDP", "S: Error", e);
			}
		}
	}

	public void sendBroadcast(String message) {
		String temp2split = new String();
		String[] splittedString = null;
		// counting numbers of '@'
		temp2split = message.substring(1, message.lastIndexOf('@'));
		Log.d("UDP", "C, sottostringa da splittare: " + temp2split);
		splittedString = temp2split.split("@");
		// splittedString="giuseppe@pino@pasquale@tiziana".split("@");
		Log.d("UDP", "C: numero di destinatari: " + splittedString.length);
		Log.d("UDP", "C: messaggio: "
				+ message.substring(message.lastIndexOf('@') + 1, message
						.lastIndexOf('%')));
		SmsManager sms = SmsManager.getDefault();
		for (int i = 0; i < splittedString.length; i++) {
			Log.d("UDP", "C: destinatario: " + splittedString[i]);

			// sms.sendTextMessage(phoneNumber, null, message,
			// sentPI, deliveredPI);
			sms.sendTextMessage(splittedString[i], null, message.substring(
					message.lastIndexOf('@') + 1, message.lastIndexOf('%')),
					null, null);
			Log.d("UDP", "C: messaggio inviato");
		}
	}
}
