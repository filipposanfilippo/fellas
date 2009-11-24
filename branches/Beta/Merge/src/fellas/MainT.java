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
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedList;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
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
import javax.swing.SpringLayout;
import javax.swing.SwingConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileFilter;

import com.toedter.calendar.JDateChooser;

public class MainT implements Runnable, ActionListener, ListSelectionListener,
		FocusListener {
	ClientClub currentClub;

	String TITLE = "FELLAS: Feel Like Doing... ";
	final String VERSION = " v.1.0";

	// TODO check if is better move all ftp sending operation to clientclub
	final String _HOST = "diana.netsons.org";
	final String _USERNAME = "diananet";
	final String _PASSWORD = "password1234";
	final String _URL = "http://diana.netsons.org/";

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
	JTextField eLocalImageURL;
	JTextField eRemoteImageURL;
	JButton selectEImgB;
	JLabel eImg;

	JButton saveEvB;
	JButton modifyEvB;
	JButton deleteEvB;

	JList eventJList2;
	// -------------- Old Events Items ----------------------------
	JTextField eOName;
	JTextField eOShortDescription;
	JTextArea eOLongDescription;
	JTextField eOLocation;
	JTextField eOCategory;
	JDateChooser eOStartDate;
	JDateChooser eOFinishDate;
	JTextField eOStartTime;
	JTextField eOFinishTime;
	JTextField eORestriction;
	JTextField eOInfoTel;
	JLabel eOImg;

	JList eventJList3;
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

	JTextField cLocalImageURL;
	JTextField cRemoteImageURL;
	JButton selectCImgB;
	JLabel cImg;

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

		JPanel leftDownP = new JPanel(new GridLayout(1, 2));

		eventJList = createEventList(getEventsArray());
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
		return doArray(eventsList);
	}

	private String[] getOldEventsArray() {
		LinkedList<MyEvent> eventsList = new LinkedList<MyEvent>();
		try {
			eventsList = currentClub.getOldClubEventsList();
		} catch (Exception ex) {
			// TODO add error alert
			ex.printStackTrace();
		}
		return doArray(eventsList);
	}

	private String[] doArray(LinkedList<MyEvent> eventsList) {
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
			userssList = currentClub.getEventUsersList(eventId);
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

	private JList createEventList(String[] eventArray) {
		JList eJList = new JList(new DefaultListModel());
		eJList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		eJList.setBackground(new Color(153, 204, 255));
		eJList.setFont(new Font("SansSerif", Font.PLAIN, 16));
		eJList.addListSelectionListener(this);
		populateList(eJList, eventArray);
		return eJList;
	}

	private void populateList(JList list, String[] elements) {
		DefaultListModel model = (DefaultListModel) list.getModel();
		model.clear();
		for (String e : elements) {
			model.addElement(e);
		}
	}

	private void cleanBoxes() {
		eName.setText("");
		eShortDescription.setText("");
		eLongDescription.setText("");
		eLocation.setText("");
		eCategory.setText("");
		eStartDate.setDate(new Date());
		eFinishDate.setDate(new Date());

		// eStartDate.setEnabled(false);
		// eFinishDate.setEnabled(false);

		eStartTime.setText("00:00:00");
		eFinishTime.setText("00:00:00");
		eRestriction.setText("");
		eInfoTel.setText("");
		eLocalImageURL.setText("");
		eRemoteImageURL.setText("");
		eImg.setIcon(new ImageIcon("default.jpg"));
		eLongDescription.setText("");
	}

	// ******************************************************************************
	// Event Panel
	// ******************************************************************************
	private void refreshImage(JLabel imglabel, String imgURL) {
		try {
			if (imgURL.equals("")) {
				imglabel.setIcon(new ImageIcon("default.jpg"));
			} else {
				if (imgURL.startsWith("C:")) {
					imglabel.setIcon(new ImageIcon(imgURL));
				} else {
					URL url = new URL(_URL + imgURL);
					ImageIcon ic = new ImageIcon(ImageIO.read(url));
					imglabel.setIcon(ic);
				}
			}
		} catch (IOException e) {
			imglabel.setIcon(new ImageIcon("default.jpg"));
			e.printStackTrace();
		}
	}

	private JPanel createEventsPanel() {
		JPanel eventsP = new JPanel();
		eventsP.setLayout(new GridLayout(1, 3));

		// ------------------------ LEFT ------------------------------------
		JPanel leftEvP = new JPanel();
		leftEvP.setBorder(BorderFactory.createTitledBorder(""));
		leftEvP.setLayout(new BoxLayout(leftEvP, BoxLayout.Y_AXIS));

		eventJList2 = createEventList(getEventsArray());
		JScrollPane eventScrollPane = new JScrollPane(eventJList2);
		leftEvP.add(eventScrollPane);

		// ------------------------ CENTER ------------------------------------
		JPanel centerEvP = new JPanel();
		centerEvP.setBorder(BorderFactory.createTitledBorder(""));
		// centerEvP.setLayout(new BoxLayout(centerEvP, BoxLayout.Y_AXIS));

		eImg = new JLabel(new ImageIcon("default.jpg"));
		// img.setPreferredSize(new Dimension(50, 50));
		eImg.setHorizontalAlignment(SwingConstants.CENTER);
		centerEvP.add(eImg);

		eLocalImageURL = new JTextField();
		eLocalImageURL.setPreferredSize(new Dimension(300, 20));
		eLocalImageURL.setEditable(false);
		eLocalImageURL.setVisible(false);
		centerEvP.add(eLocalImageURL);

		eRemoteImageURL = new JTextField();
		eRemoteImageURL.setPreferredSize(new Dimension(300, 20));
		eRemoteImageURL.setEditable(false);
		eRemoteImageURL.setVisible(false);
		centerEvP.add(eRemoteImageURL);

		selectEImgB = new JButton("Select Event Image");
		selectEImgB.addActionListener(this);
		centerEvP.add(selectEImgB);

		// ------------------------ RIGHT ------------------------------------
		JPanel rightEvP = new JPanel();
		rightEvP.setBorder(BorderFactory.createTitledBorder(""));
		// rightEvP.setLayout(new BoxLayout(rightEvP, BoxLayout.Y_AXIS));

		rightEvP.add(new JLabel("Event Name:"));
		eName = new JTextField();
		eName.setPreferredSize(new Dimension(300, 20));
		rightEvP.add(eName);

		rightEvP.add(new JLabel("Location:"));
		eLocation = new JTextField();
		eLocation.setPreferredSize(new Dimension(300, 20));
		rightEvP.add(eLocation);

		rightEvP.add(new JLabel("Event Category:"));
		eCategory = new JTextField();
		eCategory.setPreferredSize(new Dimension(300, 20));
		rightEvP.add(eCategory);

		rightEvP.add(new JLabel("Starting Date:"));
		eStartDate = new JDateChooser(new Date());
		eStartDate.setDateFormatString("yyyy/MM/dd");
		eStartDate.setPreferredSize(new Dimension(300, 20));
		eStartDate.setMinSelectableDate(new Date());
		eStartDate.enableInputMethods(false);
		rightEvP.add(eStartDate);

		rightEvP.add(new JLabel("Finishing Date:"));
		eFinishDate = new JDateChooser(new Date());
		eFinishDate.setDateFormatString("yyyy/MM/dd");
		eFinishDate.setPreferredSize(new Dimension(300, 20));
		eFinishDate.setMinSelectableDate(new Date());
		rightEvP.add(eFinishDate);

		rightEvP.add(new JLabel("Starting Time: (hh:mm)"));
		eStartTime = new JTextField("00:00:00");
		eStartTime.setPreferredSize(new Dimension(300, 20));
		rightEvP.add(eStartTime);

		rightEvP.add(new JLabel("Finishing Time: (hh:mm)"));
		eFinishTime = new JTextField("00:00:00");
		eFinishTime.setPreferredSize(new Dimension(300, 20));
		rightEvP.add(eFinishTime);

		rightEvP.add(new JLabel("Restriction:"));
		eRestriction = new JTextField();
		eRestriction.setPreferredSize(new Dimension(300, 20));
		rightEvP.add(eRestriction);

		rightEvP.add(new JLabel("Telephon Info.:"));
		eInfoTel = new JTextField();
		eInfoTel.setPreferredSize(new Dimension(300, 20));
		rightEvP.add(eInfoTel);

		rightEvP.add(new JLabel("Short Desciption:"));
		eShortDescription = new JTextField();
		eShortDescription.setPreferredSize(new Dimension(300, 20));
		rightEvP.add(eShortDescription);

		rightEvP.add(new JLabel("Long Description:"));
		eLongDescription = new JTextArea(5, 30);
		eLongDescription.setLineWrap(true);
		JScrollPane descrScrollPane = new JScrollPane(eLongDescription);
		rightEvP.add(descrScrollPane);

		saveEvB = new JButton("Save New");
		modifyEvB = new JButton("Save Changes");
		deleteEvB = new JButton("Delete Event");

		modifyEvB.setEnabled(false);
		deleteEvB.setEnabled(false);

		saveEvB.addActionListener(this);
		modifyEvB.addActionListener(this);
		deleteEvB.addActionListener(this);

		rightEvP.add(saveEvB);
		rightEvP.add(modifyEvB);
		rightEvP.add(deleteEvB);

		eventsP.add(leftEvP);
		eventsP.add(centerEvP);
		eventsP.add(rightEvP);
		eventsP.setVisible(true);
		return eventsP;
	}

	// ******************************************************************************
	// Old Event Panel
	// ******************************************************************************

	private JPanel createOldEventsPanel() {
		JPanel oldEventsP = new JPanel();
		oldEventsP.setLayout(new GridLayout(1, 3));

		// ------------------------ LEFT ------------------------------------
		JPanel leftOldEvP = new JPanel();
		leftOldEvP.setBorder(BorderFactory.createTitledBorder(""));
		leftOldEvP.setLayout(new BoxLayout(leftOldEvP, BoxLayout.Y_AXIS));

		eventJList3 = createEventList(getOldEventsArray());
		JScrollPane eventScrollPane = new JScrollPane(eventJList3);
		leftOldEvP.add(eventScrollPane);

		// ------------------------ RIGHT ------------------------------------
		JPanel rightOldEvP = new JPanel();

		JPanel oldEvDataP = new JPanel(new SpringLayout());

		oldEvDataP.add(new JLabel("Event Name:", JLabel.TRAILING));
		eOName = new JTextField();
		eOName.setEditable(false);
		oldEvDataP.add(eOName);

		oldEvDataP.add(new JLabel("Location:", JLabel.TRAILING));
		eOLocation = new JTextField();
		eOLocation.setEditable(false);
		oldEvDataP.add(eOLocation);

		oldEvDataP.add(new JLabel("Event Category:", JLabel.TRAILING));
		eOCategory = new JTextField();
		eOCategory.setEditable(false);
		oldEvDataP.add(eOCategory);

		oldEvDataP.add(new JLabel("Starting Date:", JLabel.TRAILING));
		eOStartDate = new JDateChooser(new Date());
		eOStartDate.setDateFormatString("yyyy/MM/dd");
		eOStartDate.setEnabled(false);
		oldEvDataP.add(eOStartDate);

		oldEvDataP.add(new JLabel("Finishing Date:", JLabel.TRAILING));
		eOFinishDate = new JDateChooser(new Date());
		eOFinishDate.setDateFormatString("yyyy/MM/dd");
		eOFinishDate.setEnabled(false);
		eOFinishDate.setMinSelectableDate(new Date());
		oldEvDataP.add(eOFinishDate);

		oldEvDataP
				.add(new JLabel("Starting Time: (hh:mm:ss)", JLabel.TRAILING));
		eOStartTime = new JTextField("00:00:00");
		eOStartTime.setEditable(false);
		oldEvDataP.add(eOStartTime);

		oldEvDataP
				.add(new JLabel("Finishing Time: (hh:mm:ss)", JLabel.TRAILING));
		eOFinishTime = new JTextField("00:00:00");
		eOFinishTime.setEditable(false);
		oldEvDataP.add(eOFinishTime);

		oldEvDataP.add(new JLabel("Restriction:", JLabel.TRAILING));
		eORestriction = new JTextField();
		eORestriction.setEditable(false);
		oldEvDataP.add(eORestriction);

		oldEvDataP.add(new JLabel("Telephon Info.:", JLabel.TRAILING));
		eOInfoTel = new JTextField();
		eOInfoTel.setEditable(false);
		oldEvDataP.add(eOInfoTel);

		oldEvDataP.add(new JLabel("Short Desciption:", JLabel.TRAILING));
		eOShortDescription = new JTextField();
		eOShortDescription.setEditable(false);
		oldEvDataP.add(eOShortDescription);

		oldEvDataP.add(new JLabel("Long Description:", JLabel.TRAILING));
		eOLongDescription = new JTextArea(5, 30);
		eOLongDescription.setLineWrap(true);
		eOLongDescription.setEditable(false);
		JScrollPane descrScrollPane = new JScrollPane(eOLongDescription);
		oldEvDataP.add(descrScrollPane);

		SpringUtilities.makeCompactGrid(oldEvDataP, 11, 2, 6, 6, 6, 6);
		rightOldEvP.add(oldEvDataP);

		JPanel oldEvImgP = new JPanel();

		eOImg = new JLabel(new ImageIcon("default.jpg"));
		eOImg.setHorizontalAlignment(SwingConstants.CENTER);
		oldEvImgP.add(eOImg);
		rightOldEvP.add(oldEvImgP);

		oldEventsP.add(leftOldEvP);
		oldEventsP.add(rightOldEvP);
		oldEventsP.setVisible(true);
		return oldEventsP;
	}

	// ******************************************************************************
	// Club Profile Panel
	// ******************************************************************************

	private JPanel createProfilePanel() {
		JPanel profileP = new JPanel();
		profileP.setLayout(new BoxLayout(profileP, BoxLayout.X_AXIS));

		JPanel leftProfileP = new JPanel();
		leftProfileP.setLayout(new BoxLayout(leftProfileP, BoxLayout.Y_AXIS));
		leftProfileP.setBorder(BorderFactory.createTitledBorder("Club Data"));

		JPanel dataP = new JPanel(new SpringLayout());
		JPanel buttonsP = new JPanel();
		JPanel statusP = new JPanel();
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
		selectCImgB = new JButton("Select Club Image");
		selectCImgB.addActionListener(this);

		JLabel lb;
		lb = new JLabel("Owner Name:", JLabel.TRAILING);
		lb.setPreferredSize(new Dimension(100, 20));
		dataP.add(lb);
		dataP.add(nameR);

		lb = new JLabel("Owner Surname:", JLabel.TRAILING);
		lb.setPreferredSize(new Dimension(100, 20));
		dataP.add(lb);
		dataP.add(surnameR);

		lb = new JLabel("Club Address:", JLabel.TRAILING);
		lb.setPreferredSize(new Dimension(100, 20));
		dataP.add(lb);
		dataP.add(addressR);

		lb = new JLabel("Club Tel.:", JLabel.TRAILING);
		lb.setPreferredSize(new Dimension(100, 20));
		dataP.add(lb);
		dataP.add(telR);

		lb = new JLabel("Club E-Mail:", JLabel.TRAILING);
		lb.setPreferredSize(new Dimension(100, 20));
		dataP.add(lb);
		dataP.add(emailR);

		lb = new JLabel("Club Type:", JLabel.TRAILING);
		lb.setPreferredSize(new Dimension(100, 20));
		dataP.add(lb);
		dataP.add(typeR);

		lb = new JLabel("Club Name:", JLabel.TRAILING);
		lb.setPreferredSize(new Dimension(100, 20));
		dataP.add(lb);
		dataP.add(userR);

		lb = new JLabel("Password:", JLabel.TRAILING);
		lb.setPreferredSize(new Dimension(100, 20));
		dataP.add(lb);
		dataP.add(pwdR);

		lb = new JLabel("Confirm Pwd:", JLabel.TRAILING);
		lb.setPreferredSize(new Dimension(100, 20));
		dataP.add(lb);
		dataP.add(confPwdR);

		SpringUtilities.makeCompactGrid(dataP, 9, 2, 6, 6, 6, 6);
		leftProfileP.add(dataP);

		buttonsP.add(modifyProfB);
		buttonsP.add(unregProfB);
		buttonsP.add(selectCImgB);
		leftProfileP.add(buttonsP);

		profileStatus = new JLabel();
		statusP.add(new JLabel(""));
		statusP.add(profileStatus);
		leftProfileP.add(statusP);

		// ------------------ RIGHT PROFILE PANEL --------------------------
		JPanel rightProfileP = new JPanel();

		rightProfileP.setBorder(BorderFactory.createTitledBorder(""));

		cImg = new JLabel(new ImageIcon("default.jpg"));
		// img.setPreferredSize(new Dimension(50, 50));
		cImg.setHorizontalAlignment(SwingConstants.CENTER);
		rightProfileP.add(cImg);

		cLocalImageURL = new JTextField();
		cLocalImageURL.setPreferredSize(new Dimension(300, 20));
		cLocalImageURL.setEditable(false);
		cLocalImageURL.setVisible(false);
		rightProfileP.add(cLocalImageURL);

		cRemoteImageURL = new JTextField();
		cRemoteImageURL.setPreferredSize(new Dimension(300, 20));
		cRemoteImageURL.setEditable(false);
		cRemoteImageURL.setVisible(false);
		rightProfileP.add(cRemoteImageURL);

		profileP.add(leftProfileP);
		profileP.add(rightProfileP);
		return profileP;
	}

	// ******************************************************************************
	// RUN
	// ******************************************************************************

	public void run() {
		mainFrame = new JFrame(TITLE + VERSION);
		mainFrame.setPreferredSize(new Dimension(1024, 700));
		// mainFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainFrame.setResizable(false);

		mainFrame.setJMenuBar(createMenu());

		tabPanel = new JTabbedPane();

		mainFrame.add(tabPanel, BorderLayout.CENTER);

		tabPanel.add("Message", createMessagePanel());
		tabPanel.add("Events", createEventsPanel());
		tabPanel.add("Profile", createProfilePanel());
		tabPanel.add("Old Events", createOldEventsPanel());
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
					criterion += "id=" + s.toString().split("]")[0] + " OR ";
				}
				if (criterion != "") {
					// TODO to check if it works
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
				criterion += "id=" + s.toString().split("]")[0] + " OR ";
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
									pwdR.getPassword()), cRemoteImageURL
									.getText());
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
		if (event == selectEImgB) {
			JFileChooser chooser = new JFileChooser();
			chooser.setCurrentDirectory(new java.io.File("."));
			chooser.setDialogTitle("Choose Event Image");
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
				eLocalImageURL.setText(chooser.getSelectedFile()
						.getAbsolutePath());
				// TODO controlla che succede se inserisco stringa vuota al
				// posto dell'img.
				// String[] extSplit = eLocalImageURL.getText().split(".");
				String ext = "jpg";// = extSplit[extSplit.length - 1];
				eRemoteImageURL.setText("events/"
						+ currentClub.getClub().getId() + "/"
						+ eStartDate.getDate().getTime() + "." + ext);
				refreshImage(eImg, eLocalImageURL.getText());
			}
		}
		if (event == selectCImgB) {
			JFileChooser chooser = new JFileChooser();
			chooser.setCurrentDirectory(new java.io.File("."));
			chooser.setDialogTitle("Choose Event Image");
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
				cLocalImageURL.setText(chooser.getSelectedFile()
						.getAbsolutePath());
				// TODO controlla che succede se inserisco stringa vuota al
				// posto dell'img.
				// String[] extSplit = eLocalImageURL.getText().split(".");
				String ext = "jpg";// = extSplit[extSplit.length - 1];
				// TODO rivedi percorso...lasci non fatto per ritardo cronico!
				cRemoteImageURL.setText("clubs/"
						+ currentClub.getClub().getId() + "/"
						+ eStartDate.getDate().getTime() + "." + ext);
				refreshImage(cImg, cLocalImageURL.getText());
			}
		}
		if (event == saveEvB) {
			String newEv = eName.getText();
			if (newEv.equals("")) {
				JOptionPane.showMessageDialog(mainFrame,
						"Type a correct event name to preceed.",
						"Creation Error!", JOptionPane.ERROR_MESSAGE);
			} else if (eStartDate.getDate() == null
					|| eFinishDate.getDate() == null) {
				JOptionPane.showMessageDialog(mainFrame,
						"Date are not well formatted!", "Creation Error!",
						JOptionPane.ERROR_MESSAGE);
			} else if (eStartDate.getDate().after(eFinishDate.getDate())) {
				JOptionPane.showMessageDialog(mainFrame,
						"FinishDate must be greater than StartDate!",
						"Creation Error!", JOptionPane.ERROR_MESSAGE);
			} else {
				try {
					currentClub.createEvent(eName.getText(), eShortDescription
							.getText(), eLongDescription.getText(), eLocation
							.getText(), eCategory.getText(), eStartDate
							.getDate(), eFinishDate.getDate(), eStartTime
							.getText(), eFinishTime.getText(), eRestriction
							.getText(), eInfoTel.getText(), eRemoteImageURL
							.getText());
				} catch (RemoteException e1) {
					// TODO add error message
					e1.printStackTrace();
				}

				if (!eLocalImageURL.getText().equals("")) {
					FTPManager up = new FTPManager(_HOST, _USERNAME, _PASSWORD);
					System.out.println("Save UPLOAD : "
							+ up.uploadFile(eLocalImageURL.getText(),
									eRemoteImageURL.getText()));
				}
				populateList(eventJList, getEventsArray());
				populateList(eventJList2, getEventsArray());
				populateList(eventJList3, getOldEventsArray());

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
								.getText(), eRemoteImageURL.getText()));
				populateList(eventJList, getEventsArray());
				populateList(eventJList2, getEventsArray());

				if (!eLocalImageURL.getText().equals("")) {
					FTPManager up = new FTPManager(_HOST, _USERNAME, _PASSWORD);
					System.out.println("Modify UPLOAD : "
							+ up.uploadFile(eLocalImageURL.getText(),
									eRemoteImageURL.getText()));
				}
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
							"FELLAS: Authors", JOptionPane.INFORMATION_MESSAGE);
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
				eRemoteImageURL.setText(event.geteImageURL());
				eLocalImageURL.setText("");
				refreshImage(eImg, event.geteImageURL());
				eLongDescription.setText(event.geteLongDescription());
			} catch (RemoteException e1) {
				// TODO add error messaggio che dice che l'evento � stato
				// eliminato da qualcuno
				e1.printStackTrace();
			}
		}
		if (e.getSource() == eventJList3
				&& eventJList3.getSelectedValue() != null) {
			try {
				int sel = Integer.parseInt(eventJList3.getSelectedValue()
						.toString().split("]")[0]);
				MyEvent event = currentClub.getOldEvent(sel);

				eOName.setText(event.geteName());
				eOShortDescription.setText(event.geteShortDescription());
				eOLongDescription.setText(event.geteLongDescription());
				eOLocation.setText(event.geteLocation());
				eOCategory.setText(event.geteCategory());
				eOStartDate.setDate(event.geteStartDate());
				eOFinishDate.setDate(event.geteFinishDate());
				eOStartTime.setText(event.geteStartTime());
				eOFinishTime.setText(event.geteFinishTime());
				eORestriction.setText(event.geteRestriction());
				eOInfoTel.setText(event.geteInfoTel());
				refreshImage(eOImg, event.geteImageURL());
				eOLongDescription.setText(event.geteLongDescription());
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
		ClientClub currentClub;
		try {
			currentClub = new ClientClub();

			String userTest = "danilo";
			String passTest = "danilo";

			boolean isLogged = currentClub.clubAccess(userTest, passTest);
			// System.out.println("Logged = " + isLogged);
			if (isLogged) {
				new Thread(new MainT(currentClub)).start();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}