package fellas;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Arrays;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class LoginT implements Runnable, ActionListener {
	ClientClub currentClub;
	final String TITLE = "DIANA: Feel Like Doing... (Club Client)";
	final String VERSION = " v.1.0";

	// --------------Default Operations ------------------------------
	JFrame mainFrame;
	JPanel mainPanel;

	// --------------- Buttons ---------------------------------------
	JButton loginB;
	JButton registerB;
	// ----------------- Login Fields --------------------------------
	JTextField userL;
	JPasswordField pwdL;

	// -------------- Registration Fields ----------------------------
	JTextField nameR;
	JTextField surnameR;
	JTextField addressR;
	JTextField telR;
	JTextField emailR;
	JTextField typeR;
	JTextField userR;
	JPasswordField pwdR;
	JPasswordField confPwdR;
	JLabel registrationStatus1;
	JLabel registrationStatus2;

	// ------------------- RIGHT PANEL (Login) ---------------------------

	public LoginT() throws RemoteException {
		try {
			currentClub = new ClientClub();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NotBoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private JPanel createRightPanel() {
		JPanel loginP = new JPanel();
		loginP.setBorder(BorderFactory.createTitledBorder("Login"));

		JPanel rightP = new JPanel();
		rightP.setLayout(new GridLayout(5, 2));

		userL = new JTextField(10);
		pwdL = new JPasswordField(10);
		loginB = new JButton("Login");
		loginB.addActionListener(this);
		loginB.setMnemonic(KeyEvent.VK_L);
		loginB.setActionCommand("enable");

		rightP.add(new JLabel("Club Name:"));
		rightP.add(userL);
		rightP.add(new JLabel("Password:"));
		rightP.add(pwdL);
		rightP.add(new JLabel(""));
		rightP.add(loginB);

		registrationStatus1 = new JLabel("", JLabel.LEFT);
		rightP.add(registrationStatus1);

		registrationStatus2 = new JLabel("", JLabel.LEFT);
		rightP.add(registrationStatus2);

		rightP.setVisible(true);
		loginP.add(rightP);

		return loginP;
	}

	// -------------- LEFT PANEL (Registration) ----------------------------

	private JPanel createLeftPanel() {
		JPanel leftP = new JPanel();
		leftP.setLayout(new GridLayout(10, 2));
		leftP.setBorder(BorderFactory.createTitledBorder("Registration"));

		nameR = new JTextField(10);
		surnameR = new JTextField(10);
		addressR = new JTextField(10);
		telR = new JTextField(10);
		emailR = new JTextField(10);
		typeR = new JTextField(10);
		userR = new JTextField(10);
		pwdR = new JPasswordField(10);
		confPwdR = new JPasswordField(10);
		registerB = new JButton("Register");
		registerB.addActionListener(this);

		leftP.add(new JLabel("Owner Name:"));
		leftP.add(nameR);
		leftP.add(new JLabel("Owner Surname:"));
		leftP.add(surnameR);
		leftP.add(new JLabel("Club Address:"));
		leftP.add(addressR);
		leftP.add(new JLabel("Club Tel.:"));
		leftP.add(telR);
		leftP.add(new JLabel("Club E-Mail:"));
		leftP.add(emailR);
		leftP.add(new JLabel("Club Type:"));
		leftP.add(typeR);
		leftP.add(new JLabel("Club Name:"));
		leftP.add(userR);
		leftP.add(new JLabel("Password:"));
		leftP.add(pwdR);
		leftP.add(new JLabel("Confirm Pwd:"));
		leftP.add(confPwdR);
		leftP.add(new JLabel(""));
		leftP.add(registerB);

		leftP.setVisible(true);
		return leftP;
	}

	// -----------------------------------------------------------------------------
	public void run() {
		mainFrame = new JFrame(TITLE + VERSION);
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainFrame.setResizable(false);

		mainPanel = new JPanel(new GridLayout(1, 2));

		mainPanel.add(createLeftPanel());
		mainPanel.add(createRightPanel());

		mainFrame.add(mainPanel);
		mainFrame.pack();
		mainFrame.setVisible(true);
	}

	public void actionPerformed(ActionEvent e) {
		Object event = e.getSource();

		if (event == registerB) {
			// Checks whether confirm psw is the same of psw or not
			if (!Arrays.equals(pwdR.getPassword(), confPwdR.getPassword())) {
				registrationStatus1.setForeground(Color.red);
				registrationStatus2.setForeground(Color.red);
				registrationStatus1.setText("Different Passwords,");
				registrationStatus2.setText(" correct and retry!");
			} else {
				boolean isRegistrationCorrect = false;
				try {
					isRegistrationCorrect = currentClub.clubRegistration(nameR
							.getText(), surnameR.getText(), addressR.getText(),
							telR.getText(), emailR.getText(), typeR.getText(),
							userR.getText(), new String(pwdR.getPassword()));
					if (isRegistrationCorrect) {
						registrationStatus1.setForeground(Color.green);
						registrationStatus2.setForeground(Color.green);
						registrationStatus1.setText(userR.getText());
						registrationStatus2.setText("Registered!");
					} else {
						registrationStatus1.setForeground(Color.red);
						registrationStatus2.setForeground(Color.red);
						registrationStatus1.setText("Maybe Club Existing,");
						registrationStatus2.setText(" try again.");
					}
				} catch (RemoteException e1) {
					registrationStatus1.setForeground(Color.red);
					registrationStatus2.setForeground(Color.red);
					registrationStatus1.setText("Error:");
					registrationStatus1.setText(" Server Disconnected.");
				}
			}
		}

		if (event == loginB) {
			boolean isLogged = false;
			try {
				isLogged = currentClub.clubAccess(userL.getText(), new String(
						pwdL.getPassword()));
				// System.out.println("Logged = " + isLogged);
				if (isLogged) {
					JOptionPane.showMessageDialog(null,
							"Welcome to Diana: Feel Like Doing...",
							"Logged-in!", JOptionPane.INFORMATION_MESSAGE);
					mainFrame.setVisible(false);
					new Thread(new MainT(currentClub)).start();
				} else {
					JOptionPane.showMessageDialog(null,
							"Check Club Name or Password and try again.",
							"Connection Refused", JOptionPane.ERROR_MESSAGE);
				}
			} catch (RemoteException e1) {
				JOptionPane.showMessageDialog(null,
						"Connection with server error...", "Login Refused",
						JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	public static void main(String[] args) throws RemoteException {
		new Thread(new LoginT()).start();
	}
}