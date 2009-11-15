package fellas;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Event;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.Arrays;
import java.util.LinkedList;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class MainT implements Runnable, ActionListener, ListSelectionListener,
		FocusListener {
	ClientClub currentClub;

	String TITLE = "DIANA: Feel Like Doing... ";
	final String VERSION = " v.1.0";
	final String HELP_ITA = "text/ita.txt"; // TODO creare ita help
	final String HELP_ENG = "text/eng.txt"; // TODO creare eng help

	// --------------Default Operations
	JFrame mainFrame;
	JTabbedPane tabPanel;

	// -------------- Menu Items -------------------
	JMenuItem logout;
	JMenuItem exit;
	JMenuItem help;
	JMenuItem credits;

	// -------------- Message Items ---------------------------
	JList eventJList;
	JList usersJList;
	JButton loadUsersB;
	JButton sendMessB;
	JTextArea message;

	// -------------- Events Items ----------------------------
	JTextField eName;
	JTextField eShortDescription;
	JTextArea eLongDescription;
	JTextField eLocation;
	JTextField eCategory;
	JTextField eDate;
	JTextField eStartTime;
	JTextField eFinishTime;
	JTextField eRestriction;

	JButton createEvB;
	JButton modifyEvB;
	JButton deleteEvB;
	JList eventJList2;

	// -------------- Profile Items ---------------------------
	JTextField nameR;
	JTextField surnameR;
	JTextField addressR;
	JTextField telR;
	JTextField emailR;
	JTextField typeR;
	JTextField userR;
	JPasswordField pwdR;
	JPasswordField confPwdR;
	JButton modifyProfB;
	JLabel profileStatus;

	// ******************************************************************************
	// MENU
	// ******************************************************************************

	public MainT(ClientClub currentClub) {
		this.currentClub = currentClub;
		this.TITLE = this.TITLE + "(" + currentClub.getClubName() + " Client)";
	}

	private JMenuBar createMenu() {
		// Create the menu bar
		JMenuBar menuBar = new JMenuBar();

		// Create a menu
		JMenu file = new JMenu("File");
		menuBar.add(file);

		// Create a menu item
		logout = new JMenuItem("Logout");
		logout.addActionListener(this);
		logout.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_L,
				Event.CTRL_MASK));
		file.add(logout);

		file.addSeparator();

		// Create a menu item
		exit = new JMenuItem("Exit");
		exit.addActionListener(this);
		exit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E,
				Event.CTRL_MASK));
		file.add(exit);

		// Create a menu
		JMenu about = new JMenu("About");
		menuBar.add(about);

		// Create a menu item
		help = new JMenuItem("Help");
		help.addActionListener(this);
		help.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_H,
				Event.ALT_MASK));
		about.add(help);

		about.addSeparator();

		// Create a menu item
		credits = new JMenuItem("Credits");
		credits.addActionListener(this);
		credits.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C,
				Event.ALT_MASK));
		about.add(credits);

		return menuBar;
	}

	// ******************************************************************************
	// Message Panel
	// ******************************************************************************

	private JPanel createMessagePanel() {
		JPanel messageP = new JPanel();
		messageP.setLayout(new GridLayout(1, 2));

		// ------------------------ LEFT ------------------------------------
		JPanel leftMsgP = new JPanel();
		leftMsgP.setBorder(BorderFactory.createTitledBorder(""));
		leftMsgP.setLayout(new BoxLayout(leftMsgP, BoxLayout.Y_AXIS));

		// leftMsgP.setLayout(new GridLayout(2, 3));
		JPanel leftUpP = new JPanel();
		leftUpP.setLayout(new BoxLayout(leftUpP, BoxLayout.X_AXIS));

		leftUpP.add(new JLabel("Planned Events             "));
		leftUpP.add(new JLabel("                 Users for Selected Event"));

		JPanel leftDownP = new JPanel(new GridLayout(1, 2));

		eventJList = new JList(getEventsArray());
		eventJList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		eventJList.setBackground(new Color(153, 204, 255));
		eventJList.setFont(new Font("SansSerif", Font.PLAIN, 16));
		JScrollPane eventScrollPane = new JScrollPane(eventJList);
		leftDownP.add(eventScrollPane);
		eventJList.addListSelectionListener(this);

		// loadUsersB = new JButton("Load Users");
		// loadUsersB.addActionListener(this);
		// leftDownP.add(loadUsersB);

		usersJList = new JList();
		usersJList
				.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		usersJList.setBackground(new Color(153, 204, 255));
		usersJList.setFont(new Font("SansSerif", Font.PLAIN, 16));
		JScrollPane usersScrollPane = new JScrollPane(usersJList);
		leftDownP.add(usersScrollPane);

		leftMsgP.add(leftUpP);
		leftMsgP.add(leftDownP);

		// ------------------------ RIGHT -----------------------------------
		JPanel rightMsgP = new JPanel();
		// rightMsgP.setLayout(new BoxLayout(rightMsgP, BoxLayout.Y_AXIS));
		rightMsgP.setBorder(BorderFactory.createTitledBorder(""));

		sendMessB = new JButton("Send Message");
		sendMessB.addActionListener(this);
		sendMessB.setEnabled(false);
		rightMsgP.add(sendMessB);

		message = new JTextArea("Type here your message...", 13, 28);
		// message.setBackground(new Color(255, 215, 0));
		message.setFont(new Font("SansSerif", Font.PLAIN, 16));
		message.setLineWrap(true);
		JScrollPane messageScrollPane = new JScrollPane(message);
		rightMsgP.add(messageScrollPane);

		// ------------------------------------------------------------------
		messageP.add(leftMsgP);
		messageP.add(rightMsgP);

		messageP.setVisible(true);
		return messageP;
	}

	// ******************************************************************************
	// Utility
	// ******************************************************************************

	private String[] getEventsArray() {
		LinkedList<MyEvent> eventsList = new LinkedList<MyEvent>();
		try {
			eventsList = currentClub.getClubEventsList();
		} catch (Exception ex) {
			// TODO add error alert
			ex.printStackTrace();
		}

		String[] events = new String[eventsList.size()];
		int i = 0;
		for (MyEvent e : eventsList) {
			events[i++] = e.getId() + "]" + e.geteName();
		}
		return events;
	}

	// ******************************************************************************
	// Event Panel
	// ******************************************************************************

	private JPanel createEventsPanel() {
		JPanel eventsP = new JPanel();
		eventsP.setLayout(new GridLayout(1, 2));

		// ------------------------ LEFT ------------------------------------
		JPanel leftEvP = new JPanel();
		leftEvP.setBorder(BorderFactory.createTitledBorder(""));
		leftEvP.setLayout(new BoxLayout(leftEvP, BoxLayout.Y_AXIS));

		eventJList2 = new JList(getEventsArray());
		eventJList2.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		eventJList2.setBackground(new Color(153, 204, 255));
		eventJList2.setFont(new Font("SansSerif", Font.PLAIN, 16));
		JScrollPane eventScrollPane = new JScrollPane(eventJList2);
		eventJList2.addListSelectionListener(this);
		leftEvP.add(eventScrollPane);

		// ------------------------ RIGHT ------------------------------------
		JPanel rightEvP = new JPanel();
		rightEvP.setBorder(BorderFactory.createTitledBorder(""));
		// rightEvP.setLayout(new BoxLayout(rightEvP, BoxLayout.Y_AXIS));

		rightEvP.add(new JLabel("Event Name:"));
		eName = new JTextField();
		eName.setPreferredSize(new Dimension(390, 20));
		rightEvP.add(eName);

		rightEvP.add(new JLabel("Location:"));
		eLocation = new JTextField();
		eLocation.setPreferredSize(new Dimension(390, 20));
		rightEvP.add(eLocation);

		rightEvP.add(new JLabel("Event Category:"));
		eCategory = new JTextField();
		eCategory.setPreferredSize(new Dimension(390, 20));
		rightEvP.add(eCategory);

		rightEvP.add(new JLabel("Event Date:"));
		eDate = new JTextField();
		eDate.setPreferredSize(new Dimension(390, 20));
		rightEvP.add(eDate);

		rightEvP.add(new JLabel("Starting Time:"));
		eStartTime = new JTextField();
		eStartTime.setPreferredSize(new Dimension(390, 20));
		rightEvP.add(eStartTime);

		rightEvP.add(new JLabel("Finishing Time:"));
		eFinishTime = new JTextField();
		eFinishTime.setPreferredSize(new Dimension(390, 20));
		rightEvP.add(eFinishTime);

		rightEvP.add(new JLabel("Restriction:"));
		eRestriction = new JTextField();
		eRestriction.setPreferredSize(new Dimension(390, 20));
		rightEvP.add(eRestriction);

		rightEvP.add(new JLabel("Short Desciption:"));
		eShortDescription = new JTextField();
		eShortDescription.setPreferredSize(new Dimension(390, 20));
		rightEvP.add(eShortDescription);

		rightEvP.add(new JLabel("Long Description:"));
		eLongDescription = new JTextArea(5, 35);
		eLongDescription.setLineWrap(true);
		JScrollPane descrScrollPane = new JScrollPane(eLongDescription);
		rightEvP.add(descrScrollPane);

		createEvB = new JButton("Create Event");
		modifyEvB = new JButton("Modify Event");
		deleteEvB = new JButton("Delete Event");

		modifyEvB.setEnabled(false);
		deleteEvB.setEnabled(false);

		createEvB.addActionListener(this);
		modifyEvB.addActionListener(this);
		deleteEvB.addActionListener(this);

		rightEvP.add(createEvB);
		rightEvP.add(modifyEvB);
		rightEvP.add(deleteEvB);

		eventsP.add(leftEvP);
		eventsP.add(rightEvP);
		eventsP.setVisible(true);
		return eventsP;
	}

	// ******************************************************************************
	// Club Profile Panel
	// ******************************************************************************

	private JPanel createProfilePanel() {
		JPanel leftProfileP = new JPanel();
		leftProfileP.setLayout(new BoxLayout(leftProfileP, BoxLayout.Y_AXIS));
		leftProfileP.setBorder(BorderFactory.createTitledBorder("Club Data"));

		JPanel nP = new JPanel();
		// nP.setBackground(Color.WHITE);
		JPanel sP = new JPanel();
		// sP.setBackground(Color.WHITE);
		JPanel aP = new JPanel();
		// aP.setBackground(Color.WHITE);
		JPanel tP = new JPanel();
		// tP.setBackground(Color.WHITE);
		JPanel eP = new JPanel();
		// tP.setBackground(Color.WHITE);
		JPanel yP = new JPanel();
		// tP.setBackground(Color.WHITE);
		JPanel uP = new JPanel();
		// uP.setBackground(Color.WHITE);
		JPanel pP = new JPanel();
		// pP.setBackground(Color.WHITE);
		JPanel cP = new JPanel();
		// cP.setBackground(Color.WHITE);
		JPanel bP = new JPanel();
		// bP.setBackground(Color.WHITE);
		JPanel lP = new JPanel();
		// bP.setBackground(Color.WHITE);

		Club clubData = new Club();
		try {
			clubData = currentClub.getClub();
			System.out.println(clubData);
		} catch (Exception ex) {
			// TODO add error alert
		}

		Color color = new Color(255, 215, 0);
		Color colorEnf = new Color(255, 255, 100);
		nameR = new JTextField(clubData.getoName(), 20);
		nameR.setBackground(color);
		surnameR = new JTextField(clubData.getoSurname(), 20);
		surnameR.setBackground(color);
		addressR = new JTextField(clubData.getcAddress(), 20);
		addressR.setBackground(color);
		telR = new JTextField(clubData.getcTel(), 20);
		telR.setBackground(color);
		emailR = new JTextField(clubData.getcEMail(), 20);
		emailR.setBackground(color);
		typeR = new JTextField(clubData.getcType(), 20);
		typeR.setBackground(color);
		userR = new JTextField(clubData.getcName(), 20);
		userR.setBackground(colorEnf);
		pwdR = new JPasswordField(clubData.getPsw(), 20);
		pwdR.setBackground(colorEnf);
		confPwdR = new JPasswordField(clubData.getPsw(), 20);
		confPwdR.setBackground(colorEnf);
		modifyProfB = new JButton("Save Changes");
		modifyProfB.addActionListener(this);
		JLabel lb;

		lb = new JLabel("Owner Name:");
		lb.setPreferredSize(new Dimension(100, 20));
		nP.add(lb);
		nP.add(nameR);
		leftProfileP.add(nP);

		lb = new JLabel("Owner Surname:");
		lb.setPreferredSize(new Dimension(100, 20));
		sP.add(lb);
		sP.add(surnameR);
		leftProfileP.add(sP);

		lb = new JLabel("Club Address:");
		lb.setPreferredSize(new Dimension(100, 20));
		aP.add(lb);
		aP.add(addressR);
		leftProfileP.add(aP);

		lb = new JLabel("Club Tel.:");
		lb.setPreferredSize(new Dimension(100, 20));
		tP.add(lb);
		tP.add(telR);
		leftProfileP.add(tP);

		lb = new JLabel("Club E-Mail:");
		lb.setPreferredSize(new Dimension(100, 20));
		eP.add(lb);
		eP.add(emailR);
		leftProfileP.add(eP);

		lb = new JLabel("Club Type:");
		lb.setPreferredSize(new Dimension(100, 20));
		yP.add(lb);
		yP.add(typeR);
		leftProfileP.add(yP);

		lb = new JLabel("Club Name:");
		lb.setPreferredSize(new Dimension(100, 20));
		uP.add(lb);
		uP.add(userR);
		leftProfileP.add(uP);

		lb = new JLabel("Password:");
		lb.setPreferredSize(new Dimension(100, 20));
		pP.add(lb);
		pP.add(pwdR);
		leftProfileP.add(pP);

		lb = new JLabel("Confirm Pwd:");
		lb.setPreferredSize(new Dimension(100, 20));
		cP.add(lb);
		cP.add(confPwdR);
		leftProfileP.add(cP);

		bP.add(new JLabel("                     "));
		bP.add(modifyProfB);
		leftProfileP.add(bP);

		profileStatus = new JLabel();
		lP.add(new JLabel("                     "));
		lP.add(profileStatus);
		leftProfileP.add(lP);

		leftProfileP.setVisible(true);
		return leftProfileP;
	}

	// ******************************************************************************
	// RUN
	// ******************************************************************************

	public void run() {
		mainFrame = new JFrame(TITLE + VERSION);
		mainFrame.setPreferredSize(new Dimension(800, 600));
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainFrame.setResizable(false);

		mainFrame.setJMenuBar(createMenu());

		tabPanel = new JTabbedPane();

		mainFrame.add(tabPanel, BorderLayout.CENTER);

		tabPanel.add("Message", createMessagePanel());
		tabPanel.add("Events", createEventsPanel());
		tabPanel.add("Profile", createProfilePanel());
		tabPanel.addFocusListener(this);

		mainFrame.pack();
		// mainFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
		mainFrame.setVisible(true);
	}

	// ******************************************************************************
	// ACTION LISTENER
	// ******************************************************************************

	public void actionPerformed(ActionEvent e) {
		Object event = e.getSource();
		if (event == loadUsersB) {
			if (eventJList.getSelectedValue() != null) {
				sendMessB.setEnabled(true);
				String selection = eventJList.getSelectedValue().toString();
				// TODO collegati al server e ritorna una lista di utenti per
				// l'evento selezionato
				String users[] = { "selection: " + selection, "Pippo", "Pluto",
						"Paperino", "Minnie" };
				usersJList.setListData(users);
			}
		}

		if (event == modifyProfB) {
			if (!Arrays.equals(pwdR.getPassword(), confPwdR.getPassword())) {
				profileStatus.setForeground(Color.red);
				profileStatus.setText("Error: Password are different!");
			} else {
				try {
					boolean res = currentClub.updateClubData(nameR.getText(),
							surnameR.getText(), addressR.getText(), telR
									.getText(), emailR.getText(), typeR
									.getText(), userR.getText(), new String(
									pwdR.getPassword()));
					System.out.println("RISULTATO CONNESSIONE : " + res);
					if (res) {
						profileStatus.setForeground(Color.green);
						profileStatus.setText("Data update succesfully!");
					} else {
						profileStatus.setForeground(Color.red);
						profileStatus
								.setText("Connection Error, try again later. CODE: 1");
					}
				} catch (RemoteException e1) {
					e1.printStackTrace();
					profileStatus.setForeground(Color.red);
					profileStatus
							.setText("Connection Error, try again later. CODE: 3");
				}
			}
		}
		if (event == createEvB) {
			String newEv = eName.getText();
			if (newEv.equals("")) {
				JOptionPane.showMessageDialog(mainFrame,
						"Type a correct event name to preceed.",
						"Creation Error!", JOptionPane.ERROR_MESSAGE);
			} else {
				try {
					currentClub.createEvent(eName.getText(), eShortDescription
							.getText(), eLongDescription.getText(), eLocation
							.getText(), eCategory.getText(), eDate.getText(),
							eStartTime.getText(), eFinishTime.getText(),
							eRestriction.getText());
				} catch (RemoteException e1) {
					// TODO add error message
					e1.printStackTrace();
				}
				JOptionPane.showMessageDialog(mainFrame, "Created succesfully "
						+ newEv, "Created!", JOptionPane.INFORMATION_MESSAGE);
			}
		}
		if (event == modifyEvB) {
			// TODO sinc with server
			// TODO create a refresh function to add to the three buttons for
			// update the events! (for update create e delete!!)
			String selEv[] = eventJList2.getSelectedValue().toString().split(
					"]");
			try {
				currentClub.updateEvent(new MyEvent(Integer.parseInt(selEv[0]),
						currentClub.getClub().getId(), eName.getText(),
						eShortDescription.getText(),
						eLongDescription.getText(), eLocation.getText(),
						eCategory.getText(), eDate.getText(), eStartTime
								.getText(), eFinishTime.getText(), eRestriction
								.getText()));
				JOptionPane.showMessageDialog(mainFrame,
						"Modified succesfully " + selEv[1], "Modified!",
						JOptionPane.INFORMATION_MESSAGE);
			} catch (NumberFormatException e1) {
				JOptionPane.showMessageDialog(mainFrame, "Not Modified!",
						"Error", JOptionPane.ERROR_MESSAGE);
				e1.printStackTrace();
			} catch (RemoteException e1) {
				JOptionPane.showMessageDialog(mainFrame, "Not Modified!",
						"Error", JOptionPane.ERROR_MESSAGE);
				e1.printStackTrace();
			}

		}
		if (event == deleteEvB) {
			String selEv[] = eventJList2.getSelectedValue().toString().split(
					"]");
			try {
				currentClub.deleteEvent(Integer.parseInt(selEv[0]));
				JOptionPane
						.showMessageDialog(mainFrame, "Deleted succesfully"
								+ selEv[1], "Deleted!",
								JOptionPane.INFORMATION_MESSAGE);
			} catch (NumberFormatException e1) {
				JOptionPane.showMessageDialog(mainFrame, "Not Deleted!"
						+ selEv[1], "Error", JOptionPane.ERROR_MESSAGE);
				e1.printStackTrace();
			} catch (RemoteException e1) {
				JOptionPane.showMessageDialog(mainFrame, "Not Deleted!"
						+ selEv[1], "Error", JOptionPane.ERROR_MESSAGE);
				e1.printStackTrace();
			}

		}

		if (event == credits) {
			JOptionPane
					.showMessageDialog(
							mainFrame,
							"SCIUTO Lorenzo - sciuto.lorenzo@gmail.com\n"
									+ "URZI' Erik - erik.urzi@gmail.com\n"
									+ "SANFILIPPO Filippo - filippo.sanfilippo@gmail.com\n"
									+ "SCIBILIA Giorgio - giorgio.scibilia@gmail.com",

							"DIANA: Authors", JOptionPane.INFORMATION_MESSAGE);
		}
		if (event == exit) {
			int answer = JOptionPane.showConfirmDialog(mainFrame,
					"Do you really want to close?", "Close",
					JOptionPane.YES_NO_OPTION);
			if (answer == JOptionPane.YES_OPTION)
				System.exit(0);
		}
		if (event == help) {
			try {
				BufferedReader ita = new BufferedReader(
						new FileReader(HELP_ITA));
				BufferedReader eng = new BufferedReader(
						new FileReader(HELP_ENG));
				String line = "";

				JFrame helpFrame = new JFrame("Fellas : Help");
				JTabbedPane tPane = new JTabbedPane();
				tPane.setPreferredSize(new Dimension(600, 500));

				// ------------ Italian Help ---------------------
				JPanel itaPanel = new JPanel();
				itaPanel.setBackground(Color.white);

				itaPanel.setBorder(BorderFactory
						.createTitledBorder("Help - Italiano"));

				JTextArea itaT = new JTextArea();

				itaT.setEditable(false);

				JScrollPane scrollIta = new JScrollPane(itaT);
				scrollIta
						.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

				while (((line = ita.readLine()) != null)) {
					itaT.append(line + "\n");
				}
				itaT.setCaretPosition(0);
				itaPanel.add(scrollIta);
				// ------------ English Help ---------------------
				JPanel engPanel = new JPanel();
				engPanel.setBackground(Color.white);

				engPanel.setBorder(BorderFactory
						.createTitledBorder("Help - English"));

				JTextArea engT = new JTextArea();

				JScrollPane scrollEng = new JScrollPane(engT);
				scrollEng
						.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

				engT.setEditable(false);

				while (((line = eng.readLine()) != null)) {
					engT.append(line + "\n");
				}
				engT.setCaretPosition(0);
				engPanel.add(scrollEng);
				// -----------------------------------------------

				tPane.addTab("ITALIANO", scrollIta);
				tPane.addTab("ENGLISH", scrollEng);

				helpFrame.add(tPane);
				helpFrame.pack();
				helpFrame.setVisible(true);
				ita.close();
				eng.close();
			} catch (FileNotFoundException e1) {
				JOptionPane.showMessageDialog(mainFrame,
						"Could not find the help txt file.", "File Error!",
						JOptionPane.ERROR_MESSAGE);
				e1.printStackTrace();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}

	// ******************************************************************************
	// LIST SELECTION LISTENER
	// ******************************************************************************

	public void valueChanged(ListSelectionEvent e) {
		if (e.getSource() == eventJList) {
			if (eventJList.getSelectedValue() != null) {
				sendMessB.setEnabled(true);
				String selection = eventJList.getSelectedValue().toString();
				// TODO collegati al server e ritorna una lista di utenti per
				// l'evento selezionato
				String users[] = { "selection: " + selection, "Pippo", "Pluto",
						"Paperino", "Minnie" };
				usersJList.setListData(users);
			}
		}
		// TODO sistemare il controllo..
		if (e.getSource() == eventJList2) {
			modifyEvB.setEnabled(true);
			deleteEvB.setEnabled(true);

			int sel = Integer.parseInt(((JList) e.getSource())
					.getSelectedValue().toString());
			
			MyEvent event = new MyEvent();//currentClub.getEvent(sel);

			eName.setText(event.geteName());
			eShortDescription.setText(event.geteShortDescription());
			eLongDescription.setText(event.geteLongDescription());
			eLocation.setText(event.geteLocation());
			eCategory.setText(event.geteCategory());
			eDate.setText(event.geteDate());
			eStartTime.setText(event.geteShortDescription());
			eFinishTime.setText(event.geteFinishTime());
			eRestriction.setText(event.geteRestriction());

			// TODO inserire i dati dell'evento prelevati dal server
			eLongDescription.setText("Descrizione di "
					+ ((JList) e.getSource()).getSelectedValue().toString());
		}
	}

	// ******************************************************************************
	// TABS FOCUS LISTENER
	// ******************************************************************************
	// TODO check whether is useful
	public void focusGained(FocusEvent e) {
		if (tabPanel.getSelectedIndex() == 1) {
			System.out.println("FOCUS 1: " + e.getID());
		} else if (tabPanel.getSelectedIndex() == 2) {
			System.out.println("FOCUS 2: " + e.getID());
		} else if (tabPanel.getSelectedIndex() == 3) {
			System.out.println("FOCUS 3: " + e.getID());
		}
	}

	public void focusLost(FocusEvent e) {
		// TODO Auto-generated method stub

	}

	public static void main(String[] args) {
		// new Thread(new MainT("Barone Rosso")).start();
	}

}