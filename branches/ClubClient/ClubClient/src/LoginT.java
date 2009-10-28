import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class LoginT implements Runnable, ActionListener {
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
	JTextField userR;
	JPasswordField pwdR;
	JPasswordField confPwdR;

	// ------------------- RIGHT PANEL (Login) ---------------------------

	private JPanel createRightPanel() {
		JPanel loginP = new JPanel();
		loginP.setBorder(BorderFactory.createTitledBorder("Login"));

		JPanel rightP = new JPanel();
		rightP.setLayout(new GridLayout(3, 2));

		userL = new JTextField(10);
		pwdL = new JPasswordField(10);
		loginB = new JButton("Login");
		loginB.addActionListener(this);

		rightP.add(new JLabel("Club Name:"));
		rightP.add(userL);
		rightP.add(new JLabel("Password:"));
		rightP.add(pwdL);
		rightP.add(new JLabel(""));
		rightP.add(loginB);

		rightP.setVisible(true);
		loginP.add(rightP);

		return loginP;
	}

	// -------------- LEFT PANEL (Registration) ----------------------------

	private JPanel createLeftPanel() {
		JPanel leftP = new JPanel();
		leftP.setLayout(new GridLayout(8, 2));
		leftP.setBorder(BorderFactory.createTitledBorder("Registration"));

		nameR = new JTextField(10);
		surnameR = new JTextField(10);
		addressR = new JTextField(10);
		telR = new JTextField(10);
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
			// TODO collegati al server ed effettua la registrazione
			if (JOptionPane.showConfirmDialog(null, "Registrazione Corretta?",
					"Check Registration", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
				JOptionPane.showMessageDialog(null,
						"Welcome to Diana: Feel Like Doing...", "Registered!",
						JOptionPane.INFORMATION_MESSAGE);
			} else {
				JOptionPane.showMessageDialog(null,
						"Check Data and Try Again.", "Registration Refused",
						JOptionPane.ERROR_MESSAGE);
			}
		}

		if (event == loginB) {
			// TODO collegati al server ed effettua il login (cambia if qui
			// sotto)
			if (JOptionPane.showConfirmDialog(null, "Login Corretto?",
					"Check Login", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
				mainFrame.setVisible(false);
				//TODO passare al mainT in nome corretto del locale / codice locale
				new Thread(new MainT("Barone Rosso")).start();
			} else {
				JOptionPane.showMessageDialog(null,
						"Check Club Name or Password and try again.",
						"Connection Refused", JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	public static void main(String[] args) {
		new Thread(new LoginT()).start();
	}
}