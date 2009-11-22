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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
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

import com.toedter.calendar.JDateChooser;

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
	JMenuItem newEvent;
	JMenuItem reloadEvents;
	JMenuItem exit;
	JMenuItem help;
	JMenuItem credits;

	// -------------- Message Items ---------------------------
	JButton sendMessB;
	JButton sendToAllB;
	JTextArea message;

	JList usersJList;
	JList eventJList;

	// -------------- Events Items ----------------------------
	JTextField eName;
	JTextField eShortDescription;
	JTextArea eLongDescription;
	JTextField eLocation;
	JTextField eCategory;
	JDateChooser eStartDate;
	JDateChooser eFinishDate;
	JTextField eStartTime;
	JTextField eFinishTime;
	JTextField eRestriction;
	JTextField eInfoTel;
	JTextField eImageURL;

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
	JLabel profileStatus;

	JButton modifyProfB;
	JButton unregProfB;

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
		newEvent = new JMenuItem("New Event");
		newEvent.addActionListener(this);
		newEvent.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N,
				Event.CTRL_MASK));
		file.add(newEvent);

		// Create a menu item
		reloadEvents = new JMenuItem("Reload Events");
		reloadEvents.addActionListener(this);
		reloadEvents.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R,
				Event.CTRL_MASK));
		file.add(reloadEvents);

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
		messageP.setLayout(new GridLayout(2, 1));

		// ------------------------ TOP ------------------------------------
		JPanel topMsgP = new JPanel();
		topMsgP.setBorder(BorderFactory.createTitledBorder(""));
		topMsgP.setLayout(new BoxLayout(topMsgP, BoxLayout.Y_AXIS));

		// leftMsgP.setLayout(new GridLayout(2, 3));
		JPanel leftUpP = new JPanel();
		leftUpP.setLayout(new BoxLayout(leftUpP, BoxLayout.Y_AXIS));

		leftUpP
				.add(new JLabel(
						"Planned Events                                 "
								+ "                                    Users for Selected Event"));
		JPanel leftDownP = new JPanel(new GridLayout(1, 2));

		eventJList = createEventList();
		JScrollPane eventScrollPane = new JScrollPane(eventJList);
		leftDownP.add(eventScrollPane);

		usersJList = new JList();
		usersJList
				.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		usersJList.setBackground(new Color(153, 204, 255));
		usersJList.setFont(new Font("SansSerif", Font.PLAIN, 16));
		JScrollPane usersScrollPane = new JScrollPane(usersJList);
		leftDownP.add(usersScrollPane);

		topMsgP.add(leftUpP);
		topMsgP.add(leftDownP);

		// ------------------------ BOTTOM -----------------------------------
		JPanel bottomMsgP = new JPanel();
		bottomMsgP.setLayout(new GridLayout(1, 2));
		bottomMsgP.setBorder(BorderFactory.createTitledBorder(""));

		JPanel leftBMsgP = new JPanel();
		leftBMsgP.setLayout(new GridLayout(2, 1));

		sendMessB = new JButton("Send Message");
		sendMessB.addActionListener(this);
		sendMessB.setEnabled(false);
		leftBMsgP.add(sendMessB);

		sendToAllB = new JButton("Send To All");
		sendToAllB.addActionListener(this);
		sendToAllB.setEnabled(false);
		leftBMsgP.add(sendToAllB);

		JPanel rightBMsgP = new JPanel();
		rightBMsgP.setLayout(new GridLayout(1, 1));

		message = new JTextArea("Type here your message...", 13, 28);
		// message.setBackground(new Color(255, 215, 0));
		message.setFont(new Font("SansSerif", Font.PLAIN, 16));
		message.setLineWrap(true);
		JScrollPane messageScrollPane = new JScrollPane(message);
		rightBMsgP.add(messageScrollPane);

		bottomMsgP.add(leftBMsgP);
		bottomMsgP.add(rightBMsgP);

		// ------------------------------------------------------------------
		messageP.add(topMsgP);
		messageP.add(bottomMsgP);

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
		if (eventsList != null) {
			String[] events = new String[eventsList.size()];
			int i = 0;
			for (MyEvent e : eventsList) {
				events[i++] = e.getId() + "]" + e.geteName();
			}
			return events;
		}
		return new String[0];
	}

	private String[] getUsersArray(int eventId) {
		LinkedList<User> userssList = new LinkedList<User>();
		try {
			userssList = currentClub.getUsers4Event(eventId);
		} catch (Exception ex) {
			// TODO add error alert
			ex.printStackTrace();
		}
		if (userssList != null) {
			String[] users = new String[userssList.size()];
			int i = 0;
			for (User u : userssList) {
				users[i++] = u.getId() + "]" + u.getuName() + " "
						+ u.getuSurname() + "( " + u.getusername() + " )";
			}
			return users;
		}
		return new String[0];
	}

	private JList createEventList() {
		JList eJList = new JList(new DefaultListModel());
		eJList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		eJList.setBackground(new Color(153, 204, 255));
		eJList.setFont(new Font("SansSerif", Font.PLAIN, 16));
		eJList.addListSelectionListener(this);
		populateList(eJList, getEventsArray());
		return eJList;
	}

	private void populateList(JList list, String[] elements) {
		DefaultListModel model = (DefaultListModel) list.getModel();
		model.clear();
		for (String e : elements) {
			model.addElement(e);
		}
	}

	private boolean isDateValid(String date) {
		SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");
		Date testDate = null;
		try {
			testDate = df.parse(date);
		} catch (ParseException e) {
			return false;
		}
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		if (cal.getTime().after(testDate))
			return false;
		if (!df.format(testDate).equals(date))
			return false;
		return true;
	}

	private void cleanBoxes() {
		eName.setText("");
		eShortDescription.setText("");
		eLongDescription.setText("");
		eLocation.setText("");
		eCategory.setText("");
		eStartDate.setDate(new Date());
		eFinishDate.setDate(new Date());
		eStartTime.setText("");
		eFinishTime.setText("");
		eRestriction.setText("");
		eInfoTel.setText("");
		eImageURL.setText("");
		eLongDescription.setText("");
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

		eventJList2 = createEventList();
		JScrollPane eventScrollPane = new JScrollPane(eventJList2);
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

		rightEvP.add(new JLabel("Starting Date:"));
		eStartDate = new JDateChooser(new Date());
		eStartDate.setDateFormatString("yyyy/MM/dd");
		eStartDate.setPreferredSize(new Dimension(390, 20));
		eStartDate.setMinSelectableDate(new Date());
		eStartDate.enableInputMethods(false);
		rightEvP.add(eStartDate);

		rightEvP.add(new JLabel("Finishing Date:"));
		eFinishDate = new JDateChooser(new Date());
		eFinishDate.setDateFormatString("yyyy/MM/dd");
		eFinishDate.setPreferredSize(new Dimension(390, 20));
		eFinishDate.setMinSelectableDate(new Date());
		rightEvP.add(eFinishDate);

		rightEvP.add(new JLabel("Starting Time: (hh:mm)"));
		eStartTime = new JTextField();
		eStartTime.setPreferredSize(new Dimension(390, 20));
		rightEvP.add(eStartTime);

		rightEvP.add(new JLabel("Finishing Time: (hh:mm)"));
		eFinishTime = new JTextField();
		eFinishTime.setPreferredSize(new Dimension(390, 20));
		rightEvP.add(eFinishTime);

		rightEvP.add(new JLabel("Restriction:"));
		eRestriction = new JTextField();
		eRestriction.setPreferredSize(new Dimension(390, 20));
		rightEvP.add(eRestriction);

		rightEvP.add(new JLabel("Telephon Info.:"));
		eInfoTel = new JTextField();
		eInfoTel.setPreferredSize(new Dimension(390, 20));
		rightEvP.add(eInfoTel);

		rightEvP.add(new JLabel("Event Image:"));
		eImageURL = new JTextField();
		eImageURL.setPreferredSize(new Dimension(390, 20));
		rightEvP.add(eImageURL);

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
		unregProfB = new JButton("Unregister Club");
		unregProfB.addActionListener(this);
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
		bP.add(unregProfB);
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
		mainFrame.setPreferredSize(new Dimension(800, 750));
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
		if (event == sendMessB) {
			if (usersJList.getSelectedValues() != null) {
				String criterion = "";
				for (Object s : usersJList.getSelectedValues()) {
					criterion += "id=" + s.toString().split("]")[0] + " AND ";
				}
				if (criterion != "") {
					try {
						currentClub.spamMobile(message.getText(), criterion
								.substring(0, criterion.length() - 3));
					} catch (RemoteException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			}
		}
		if (event == sendToAllB) {
			String criterion = "";
			DefaultListModel model = (DefaultListModel) usersJList.getModel();
			String[] ele = new String[model.size()];
			model.copyInto(ele);
			for (String s : ele) {
				criterion += "id=" + s.toString().split("]")[0] + " AND ";
			}
			if (criterion != "") {
				try {
					currentClub.spamMobile(message.getText(), criterion
							.substring(0, criterion.length() - 3));
				} catch (RemoteException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
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
					if (res) {
						profileStatus.setForeground(Color.green);
						profileStatus.setText("Data updated succesfully!");
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
		if (event == unregProfB) {
			try {
				int n = JOptionPane
						.showConfirmDialog(
								mainFrame,
								"Are you sure you want to unsubscribe your club from server?",
								"Club Unsubscription",
								JOptionPane.YES_NO_OPTION);
				if (n == 0) {
					currentClub.clubUnregistration();
					JOptionPane.showMessageDialog(mainFrame,
							"Club Succesfully unregistred!",
							"Club Unsubscription",
							JOptionPane.INFORMATION_MESSAGE);
					System.exit(0);
					new LoginT();
				}
			} catch (RemoteException e1) {
				// TODO add error message
				e1.printStackTrace();
			}
		}
		if (event == createEvB) {
			String newEv = eName.getText();
			if (newEv.equals("")) {
				JOptionPane.showMessageDialog(mainFrame,
						"Type a correct event name to preceed.",
						"Creation Error!", JOptionPane.ERROR_MESSAGE);
			} else if (!eStartDate.isValid() || !eFinishDate.isValid()) {
				JOptionPane.showMessageDialog(mainFrame,
						"Date are not well formatted!", "Creation Error!",
						JOptionPane.ERROR_MESSAGE);
			} else {
				try {
					currentClub
							.createEvent(eName.getText(), eShortDescription
									.getText(), eLongDescription.getText(),
									eLocation.getText(), eCategory.getText(),
									eStartDate.getDate(),
									eFinishDate.getDate(),
									eStartTime.getText(),
									eFinishTime.getText(), eRestriction
											.getText(), eInfoTel.getText(),
									eImageURL.getText());
				} catch (RemoteException e1) {
					// TODO add error message
					e1.printStackTrace();
				}

				populateList(eventJList, getEventsArray());
				populateList(eventJList2, getEventsArray());

				JOptionPane.showMessageDialog(mainFrame, "Created succesfully "
						+ newEv, "Created!", JOptionPane.INFORMATION_MESSAGE);
				cleanBoxes();
				modifyEvB.setEnabled(false);
				deleteEvB.setEnabled(false);
			}
		}
		if (event == modifyEvB && eventJList2.getSelectedValue() != null) {
			String selEv[] = eventJList2.getSelectedValue().toString().split(
					"]");
			try {
				currentClub.updateEvent(new MyEvent(Integer.parseInt(selEv[0]),
						currentClub.getClub().getId(), eName.getText(),
						eShortDescription.getText(),
						eLongDescription.getText(), eLocation.getText(),
						eCategory.getText(), eStartDate.getDate(), eFinishDate
								.getDate(), eStartTime.getText(), eFinishTime
								.getText(), eRestriction.getText(), eInfoTel
								.getText(), eImageURL.getText()));
				populateList(eventJList, getEventsArray());
				populateList(eventJList2, getEventsArray());

				JOptionPane.showMessageDialog(mainFrame,
						"Modified succesfully " + selEv[1], "Modified!",
						JOptionPane.INFORMATION_MESSAGE);
				cleanBoxes();
			} catch (Exception ex) {
				JOptionPane.showMessageDialog(mainFrame, "Not Modified!",
						"Error", JOptionPane.ERROR_MESSAGE);
				ex.printStackTrace();
			}
		}
		if (event == deleteEvB && eventJList2.getSelectedValue() != null) {
			String selEv[] = eventJList2.getSelectedValue().toString().split(
					"]");
			try {
				currentClub.deleteEvent(Integer.parseInt(selEv[0]));
				cleanBoxes();
				JOptionPane
						.showMessageDialog(mainFrame, "Deleted succesfully "
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
			populateList(eventJList2, getEventsArray());
			populateList(eventJList, getEventsArray());
		}

		if (event == credits) {
			JOptionPane
					.showMessageDialog(
							mainFrame,
							"SCIUTO Lorenzo - lorenzo.sciuto@gmail.com\n"
									+ "URZI' Erik - erik.urzi@gmail.com\n"
									+ "SANFILIPPO Filippo - filippo.sanfilippo@gmail.com\n"
									+ "SCIBILIA Giorgio - giorgio.scibilia@gmail.com",

							"DIANA: Authors", JOptionPane.INFORMATION_MESSAGE);
		}
		if (event == newEvent) {
			cleanBoxes();
			modifyEvB.setEnabled(false);
			deleteEvB.setEnabled(false);
		}
		if (event == reloadEvents) {
			cleanBoxes();
			modifyEvB.setEnabled(false);
			deleteEvB.setEnabled(false);
			populateList(eventJList, getEventsArray());
			populateList(eventJList2, getEventsArray());
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
		if (e.getSource() == eventJList
				&& eventJList.getSelectedValue() != null) {
			sendMessB.setEnabled(true);
			sendToAllB.setEnabled(true);
			int sel = Integer.parseInt(eventJList.getSelectedValue().toString()
					.split("]")[0]);
			usersJList.setListData(getUsersArray(sel));
		}
		if (e.getSource() == eventJList2
				&& eventJList2.getSelectedValue() != null) {
			modifyEvB.setEnabled(true);
			deleteEvB.setEnabled(true);
			try {
				int sel = Integer.parseInt(eventJList2.getSelectedValue()
						.toString().split("]")[0]);
				MyEvent event = currentClub.getEvent(sel);

				eName.setText(event.geteName());
				eShortDescription.setText(event.geteShortDescription());
				eLongDescription.setText(event.geteLongDescription());
				eLocation.setText(event.geteLocation());
				eCategory.setText(event.geteCategory());
				eStartDate.setDate(event.geteStartDate());
				eFinishDate.setDate(event.geteFinishDate());
				eStartTime.setText(event.geteStartTime());
				eFinishTime.setText(event.geteFinishTime());
				eRestriction.setText(event.geteRestriction());
				eInfoTel.setText(event.geteInfoTel());
				eImageURL.setText(event.geteImageURL());
				eLongDescription.setText(event.geteLongDescription());
			} catch (RemoteException e1) {
				// TODO add error
				e1.printStackTrace();
			}
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