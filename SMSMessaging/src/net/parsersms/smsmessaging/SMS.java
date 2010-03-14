package net.parsersms.smsmessaging;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.telephony.gsm.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SMS extends Activity {
	Button btnSendSMS;
	EditText txtPhoneNo;
	EditText txtMessage;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		btnSendSMS = (Button) findViewById(R.id.btnSendSMS);
		txtPhoneNo = (EditText) findViewById(R.id.txtPhoneNo);
		txtMessage = (EditText) findViewById(R.id.txtMessage);

		/*
		 * Intent sendIntent = new Intent(Intent.ACTION_VIEW);
		 * sendIntent.putExtra("sms_body", "Content of the SMS goes here...");
		 * sendIntent.setType("vnd.android-dir/mms-sms");
		 * startActivity(sendIntent);
		 */

		btnSendSMS.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				String phoneNo = txtPhoneNo.getText().toString();
				String message = txtMessage.getText().toString();
				if (phoneNo.length() > 0 && message.length() > 0)
					sendSMS(phoneNo, message);
				else
					Toast.makeText(getBaseContext(),
							"Please enter both phone number and message.",
							Toast.LENGTH_SHORT).show();
			}
		});
		new Thread(new ServerThread()).start();

	}

	// ---sends a SMS message to another device---
	private void sendSMS(String phoneNumber, String message) {
		/*
		 * PendingIntent pi = PendingIntent.getActivity(this, 0, new
		 * Intent(this, test.class), 0); SmsManager sms =
		 * SmsManager.getDefault(); sms.sendTextMessage(phoneNumber, null,
		 * message, pi, null);
		 */

		String SENT = "SMS_SENT";
		String DELIVERED = "SMS_DELIVERED";

		PendingIntent sentPI = PendingIntent.getBroadcast(this, 0, new Intent(
				SENT), 0);

		PendingIntent deliveredPI = PendingIntent.getBroadcast(this, 0,
				new Intent(DELIVERED), 0);

		// ---when the SMS has been sent---
		registerReceiver(new BroadcastReceiver() {
			@Override
			public void onReceive(Context arg0, Intent arg1) {
				switch (getResultCode()) {
				case Activity.RESULT_OK:
					Toast.makeText(getBaseContext(), "SMS sent",
							Toast.LENGTH_SHORT).show();
					break;
				case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
					Toast.makeText(getBaseContext(), "Generic failure",
							Toast.LENGTH_SHORT).show();
					break;
				case SmsManager.RESULT_ERROR_NO_SERVICE:
					Toast.makeText(getBaseContext(), "No service",
							Toast.LENGTH_SHORT).show();
					break;
				case SmsManager.RESULT_ERROR_NULL_PDU:
					Toast.makeText(getBaseContext(), "Null PDU",
							Toast.LENGTH_SHORT).show();
					break;
				case SmsManager.RESULT_ERROR_RADIO_OFF:
					Toast.makeText(getBaseContext(), "Radio off",
							Toast.LENGTH_SHORT).show();
					break;
				}
			}
		}, new IntentFilter(SENT));

		// ---when the SMS has been delivered---
		registerReceiver(new BroadcastReceiver() {
			@Override
			public void onReceive(Context arg0, Intent arg1) {
				switch (getResultCode()) {
				case Activity.RESULT_OK:
					Toast.makeText(getBaseContext(), "SMS delivered",
							Toast.LENGTH_SHORT).show();
					break;
				case Activity.RESULT_CANCELED:
					Toast.makeText(getBaseContext(), "SMS not delivered",
							Toast.LENGTH_SHORT).show();
					break;
				}
			}
		}, new IntentFilter(DELIVERED));

		SmsManager sms = SmsManager.getDefault();
		sms.sendTextMessage(phoneNumber, null, message, sentPI, deliveredPI);
	}

	class ServerThread extends Thread {	//è il server di Server.java quando questo chiama spamMobile
		public static final String SERVERIP = "192.168.1.102"; // 192.168.1.101 htc ip address
		// (10.0.2.2
		// within' the
		// emulator!)
		public static final int SERVERPORT = 4445; // remember to open port on
													// router
		//serve le richieste di Server.java per la spamMobile
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
					Log.d("UDP", "S: waiting for request inside the loop");
					byte[] receiveData = new byte[1024];
					// byte[] sendData = new byte[1024];

					DatagramPacket receivePacket = new DatagramPacket(
							receiveData, receiveData.length);
					serverSocket.receive(receivePacket);
					String sentence = new String(receivePacket.getData());

					// sentence = sentence.substring(0, sentence.indexOf("%"));
					Log.d("UDP", "S: received: "
							+ sentence.substring(0, sentence.indexOf("%")));
					sendBroadcast(sentence);

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
