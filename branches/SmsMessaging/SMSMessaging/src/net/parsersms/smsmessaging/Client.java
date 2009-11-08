package net.parsersms.smsmessaging;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import android.util.Log;

public class Client implements Runnable {
	byte[] buf = "".getBytes();

	public Client(String msg) {
		buf = msg.getBytes();
	}

	@Override
	public void run() {
		Log.d("Thread Client", "Thread Client: Running...");
		try {
			// Retrieve the ServerName
			InetAddress serverAddr = InetAddress.getByName("piazzailcampo.doesntexist.com");
			int SERVERPORT = 4444;

			Log.d("UDP", "C: Connecting...");
			/* Create new UDP-Socket */
			DatagramSocket socket = new DatagramSocket();

			/* Prepare some data to be sent. */
			//byte[] buf = ("Hello from Client").getBytes();

			/*
			 * Create UDP-packet with data & destination(url+port)
			 */
			DatagramPacket packet = new DatagramPacket(buf, buf.length,
					serverAddr, SERVERPORT);
			Log.d("UDP", "C: Sending: '" + new String(buf) + "'");

			/* Send out the packet */
			socket.send(packet);
			Log.d("UDP", "C: Sent.");
			Log.d("UDP", "C: Done.");
		} catch (Exception e) {
			Log.e("UDP", "C: Error", e);
		}
	}
}
