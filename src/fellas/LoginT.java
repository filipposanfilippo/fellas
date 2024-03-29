package fellas;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Arrays;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SpringLayout;
import javax.swing.filechooser.FileFilter;

public class LoginT implements Runnable, ActionListener {
	ClientClub currentClub;
	final String TITLE = "FELLAS: Feel Like Doing... (Club Client)";
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
	JTextField clubNameR;
	JTextField userR;
	JPasswordField pwdR;
	JPasswordField confPwdR;
	JLabel registrationStatus;
	JButton selectImgB;
	JTextField cImageURL;
	JLabel img;

	// ------------------- RIGHT PANEL (Login) ---------------------------

	/**
	 * Default Constructor that starts the RMI connection defining a new
	 * ClientClub.
	 * 
	 * @throws RemoteException
	 */
	public LoginT() throws RemoteException {
		try {
			currentClub = new ClientClub();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (NotBoundException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Creates the right Login panel.
	 * 
	 * @return JPanel
	 */
	private JPanel createRightPanel() {
		JPanel rightP = new JPanel();
		rightP.setLayout(new BoxLayout(rightP, BoxLayout.Y_AXIS));

		JPanel loginP = new JPanel(new SpringLayout());
		loginP.setBorder(BorderFactory.createTitledBorder("Login"));

		userL = new JTextField();
		pwdL = new JPasswordField();

		loginB = new JButton("Login");
		loginB.addActionListener(this);
		loginB.setMnemonic(KeyEvent.VK_L);
		loginB.setActionCommand("enable");

		loginP.add(new JLabel("Username:", JLabel.TRAILING));
		loginP.add(userL);
		loginP.add(new JLabel("Password:", JLabel.TRAILING));
		loginP.add(pwdL);
		loginP.add(new JLabel("", JLabel.TRAILING));
		loginP.add(loginB);

		SpringUtilities.makeCompactGrid(loginP, 3, 2, 6, 6, 6, 6);
		// --------------------- CENTER PANEL -------------------------------

		JPanel statusP = new JPanel();
		statusP.setBorder(BorderFactory.createTitledBorder(""));

		registrationStatus = new JLabel(" ", JLabel.LEFT);
		statusP.add(registrationStatus);

		// -------------- BOTTOM PANEL (Club Image) ------------------------

		JPanel imgP = new JPanel();
		imgP.setBorder(BorderFactory.createTitledBorder("Club Image"));

		img = new JLabel(new ImageIcon("default.jpg"));
		img.setPreferredSize(new Dimension(130, 130));
		imgP.add(img);

		cImageURL = new JTextField("default.jpg");
		cImageURL.setEditable(false);
		cImageURL.setVisible(false);
		imgP.add(cImageURL);

		selectImgB = new JButton("Select Club Image");
		selectImgB.addActionListener(this);
		imgP.add(selectImgB);

		rightP.add(loginP);
		rightP.add(statusP);
		rightP.add(imgP);
		return rightP;
	}

	// -------------- LEFT PANEL (Registration) ----------------------------

	/**
	 * Creates the Left Login Panel.
	 * 
	 * @return JPanel
	 */
	private JPanel createLeftPanel() {
		JPanel leftP = new JPanel();
		leftP.setLayout(new GridLayout(11, 2));
		leftP.setBorder(BorderFactory.createTitledBorder("Registration"));

		nameR = new JTextField(10);
		surnameR = new JTextField(10);
		addressR = new JTextField(10);
		telR = new JTextField(10);
		emailR = new JTextField(10);
		typeR = new JTextField(10);
		clubNameR = new JTextField(10);
		userR = new JTextField(10);
		pwdR = new JPasswordField(10);
		confPwdR = new JPasswordField(10);
		registerB = new JButton("Register");
		registerB.addActionListener(this);

		leftP.add(new JLabel("Owner Name:"));
		leftP.add(nameR);
		leftP.add(new JLabel("Owner Surname:"));
		leftP.add(surnameR);
		leftP.add(new JLabel("Club Full Address:"));
		leftP.add(addressR);
		leftP.add(new JLabel("Club Tel.:"));
		leftP.add(telR);
		leftP.add(new JLabel("Club E-Mail:"));
		leftP.add(emailR);
		leftP.add(new JLabel("Club Type:"));
		leftP.add(typeR);
		leftP.add(new JLabel("Club Name:"));
		leftP.add(clubNameR);
		leftP.add(new JLabel("Username:"));
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
		Toolkit tk = Toolkit.getDefaultToolkit();
		Dimension screenSize = tk.getScreenSize();
		int screenHeight = screenSize.height;
		int screenWidth = screenSize.width;
		mainFrame.setLocation(screenWidth / 4, screenHeight / 4);
	}

	public void actionPerformed(ActionEvent e) {
		Object event = e.getSource();

		if (event == registerB) {
			if (!Arrays.equals(pwdR.getPassword(), confPwdR.getPassword())) {
				registrationStatus.setForeground(Color.red);
				registrationStatus
						.setText("Different Passwords, correct and retry!");
				pwdR.setBackground(Color.cyan);
				confPwdR.setBackground(Color.cyan);
			} else if (userR.getText().equals("")) {
				registrationStatus.setForeground(Color.red);
				registrationStatus.setText("Insert a correct username!");
				userR.setBackground(Color.cyan);
			} else if (pwdR.getPassword().length == 0) {
				registrationStatus.setForeground(Color.red);
				registrationStatus.setText("Insert a correct password!");
				pwdR.setBackground(Color.cyan);
			} else if (clubNameR.getText().equals("")) {
				registrationStatus.setForeground(Color.red);
				registrationStatus.setText("Insert a correct Club Name!");
				clubNameR.setBackground(Color.cyan);
			} else {
				boolean isRegistrationCorrect = false;
				try {
					isRegistrationCorrect = currentClub.clubRegistration(nameR
							.getText().replace("'", "\\'"), surnameR.getText()
							.replace("'", "\\'"), addressR.getText().replace(
							"'", "\\'"), telR.getText().replace("'", "\\'"),
							emailR.getText().replace("'", "\\'"), typeR
									.getText().replace("'", "\\'"), clubNameR
									.getText().replace("'", "\\'"), userR
									.getText().replace("'", "\\'"), new String(
									pwdR.getPassword()), cImageURL.getText()
									.replace("'", "\\'"));
					if (isRegistrationCorrect) {
						registrationStatus.setForeground(Color.green);
						registrationStatus.setText(clubNameR.getText()
								+ " Registred!");
					} else {
						registrationStatus.setForeground(Color.red);
						registrationStatus
								.setText("Maybe Club Existing, try again.");
					}
				} catch (RemoteException e1) {
					registrationStatus.setForeground(Color.red);
					registrationStatus.setText("Error: Server Disconnected.");
				}
			}
		}
		if (event == selectImgB) {
			JFileChooser chooser = new JFileChooser();
			chooser.setCurrentDirectory(new java.io.File("."));
			chooser.setDialogTitle("Choose Club Image");
			FileFilter filter = new FileFilter() {
				public boolean accept(File f) {
					if (f.getName().endsWith(".jpg")
							|| f.getName().endsWith(".jpeg")
							|| f.getName().endsWith(".gif")
							|| f.getName().endsWith(".png"))
						return true;
					return false;
				}

				public String getDescription() {
					return "jpg,jpeg,gif,png";
				}
			};
			chooser.setFileFilter(filter);
			if (chooser.showOpenDialog(new JFrame()) == JFileChooser.APPROVE_OPTION) {
				cImageURL.setText(chooser.getSelectedFile().getAbsolutePath());
				updateImage(cImageURL.getText());
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
							"Welcome to Fellas: Feel Like Doing...",
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

	/**
	 * Updates the image visualization when the selection changes.
	 * 
	 * @param String
	 *            imgURL
	 */
	private void updateImage(final String imgURL) {
		if (imgURL.equals("")) {
			img.setIcon(new ImageIcon("default.jpg"));
		} else {
			img.setIcon(new ImageIcon(imgURL));
		}
	}

	public static void main(String[] args) throws RemoteException {
		new Thread(new LoginT()).start();
	}
}