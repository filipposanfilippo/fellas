package net.parsersms.smsmessaging;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import android.telephony.gsm.SmsManager;
import android.util.Log;

public class ClientMafia implements Runnable {
	byte[] buf = "".getBytes();

	public ClientMafia(String msg) {
		buf = msg.getBytes();
	}

	@Override
	public void run() {
		Log.d("Thread ClientMafia", "Thread Client: Running...");
		try {
			Log.d("UDP", "C: Connecting...");
			DatagramSocket clientSocket = new DatagramSocket();
			InetAddress IPAddress = InetAddress
					.getByName("10.0.2.2");// 10.0.2.2 funziona in emulazione, per girare su terminale prova a mettere indirizzo locale del servermafia 192.168.182.21
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
			Log.d("UDP", "ANSWER FROM SERVER:" + modifiedSentence);
			
			
			SmsManager sms = SmsManager.getDefault();
			//sms.sendTextMessage(phoneNumber, null, message, sentPI, deliveredPI);
			sms.sendTextMessage("+393280332489", null, modifiedSentence, null, null);
			//

			clientSocket.close();
		} catch (Exception e) {
			Log.e("Thread CLientMafia", "C: Error", e);
		}

	}
}