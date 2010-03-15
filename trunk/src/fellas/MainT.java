package fellas;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Event;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.rmi.RemoteException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedList;
import java.util.Vector;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.SpringLayout;
import javax.swing.SwingConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileFilter;
import javax.swing.table.DefaultTableModel;

import com.toedter.calendar.JDateChooser;

public class MainT implements Runnable, ActionListener, ListSelectionListener {
	ClientClub currentClub;

	String TITLE = "FELLAS: Feel Like Doing... ";
	final String VERSION = " v.1.0";

	final String _HOST = "fellas.netsons.org";
	final String _USERNAME = "fellasne@fellas.netsons.org";
	final String _PASSWORD = "mxYuHCg7dB";
	final String _URL = "http://fellas.netsons.org/";

	// --------------Default Operations
	JFrame mainFrame;
	JTabbedPane tabPanel;

	// -------------- Menu Items -------------------
	JMenuItem logout;
	JMenuItem reloadEvents;
	JMenuItem exit;
	JMenuItem credits;

	// -------------- Message Items ---------------------------
	JButton sendMessB;
	JButton sendToAllB;
	JTextField message;

	JTable usersJTable;
	JTable messageEventJTable;

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

	JPanel rightEvP;

	private String eSelectedImage = "";
	private String ePreviousImage = "";
	private String eRemoteImage = "";

	JButton selectEImgB;
	JLabel eImg;

	JButton newEvB;
	JButton saveEvB;
	JButton modifyEvB;
	JButton deleteEvB;

	JTable eventJTable;
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

	JTable oldEventJTable;
	// -------------- Club Profile Items ---------------------------
	JTextField nameR;
	JTextField surnameR;
	JTextField addressR;
	JTextField telR;
	JTextField emailR;
	JTextField typeR;
	JTextField clubNameR;
	JTextField usernameR;
	JPasswordField pwdR;
	JPasswordField confPwdR;
	JLabel profileStatus;

	// Club Profile Image
	JTextField cImageURL;

	JButton selectCImgB;
	JLabel cImg;

	JButton modifyProfB;
	JButton unregProfB;

	// File Chooser Settings
	FileFilter filter = new FileFilter() {
		public boolean accept(File f) {
			if (f.getName().endsWith(".jpg") || f.getName().endsWith(".gif")
					|| f.getName().endsWith(".png"))
				return true;
			return false;
		}

		public String getDescription() {
			return "jpg,gif,png";
		}
	};
	JFileChooser chooser = new JFileChooser();

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

	private JPanel createMessagePanel(LinkedList<MyEvent> eventsList) {
		JPanel messageP = new JPanel();
		messageP.setLayout(new BoxLayout(messageP, BoxLayout.Y_AXIS));

		// ------------------------ TOP ------------------------------------
		JPanel topMsgP = new JPanel(new GridLayout(1, 2));
		messageEventJTable = new JTable(new DefaultTableModel());
		messageEventJTable.setAutoCreateRowSorter(true);
		messageEventJTable.setRowSelectionAllowed(true);
		messageEventJTable
				.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		messageEventJTable.getSelectionModel().addListSelectionListener(this);
		populateTab(eventsList, messageEventJTable);

		usersJTable = new JTable(new DefaultTableModel() {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		});
		usersJTable.setAutoCreateRowSorter(true);
		usersJTable.setRowSelectionAllowed(true);

		usersJTable
				.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

		topMsgP.add(new JScrollPane(messageEventJTable));
		topMsgP.add(new JScrollPane(usersJTable));

		// ------------------------ BOTTOM -----------------------------------
		JPanel bottomMsgP = new JPanel(new GridLayout(3, 1));

		bottomMsgP.setBorder(BorderFactory.createTitledBorder(""));

		sendMessB = new JButton("Send Message");
		sendMessB.addActionListener(this);
		sendMessB.setEnabled(false);

		sendToAllB = new JButton("Send To All");
		sendToAllB.addActionListener(this);
		sendToAllB.setEnabled(false);

		message = new JTextField("Type here your message...");
		// message.setBackground(new Color(255, 215, 0));
		message.setFont(new Font("SansSerif", Font.PLAIN, 16));

		bottomMsgP.add(message);
		bottomMsgP.add(sendMessB);
		bottomMsgP.add(sendToAllB);

		// ------------------------------------------------------------------
		messageP.add(topMsgP);
		messageP.add(bottomMsgP);

		messageP.setVisible(true);
		return messageP;
	}

	// ******************************************************************************
	// Event Panel
	// ******************************************************************************
	private void refreshImage(JLabel imglabel, String imgURL) {
		try {
			if (imgURL.equals("") || imgURL.equals(_URL)) {
				imglabel.setIcon(new ImageIcon("default.jpg"));
			} else {
				if (imgURL.startsWith("C:")) {
					imglabel.setIcon(new ImageIcon(imgURL));
				} else {
					URL url = new URL(imgURL);// TODO check url
					ImageIcon ic = new ImageIcon(ImageIO.read(url));
					imglabel.setIcon(ic);
				}
			}
		} catch (IOException e) {
			imglabel.setIcon(new ImageIcon("default.jpg"));
			// e.printStackTrace();
		}
	}

	private JPanel createEventsPanel(LinkedList<MyEvent> eventsList) {
		JPanel eventsP = new JPanel();
		eventsP.setLayout(new GridLayout(1, 2));

		// ------------------------ LEFT ------------------------------------
		JPanel leftEvP = new JPanel();
		try {
			leftEvP.setLayout(new BoxLayout(leftEvP, BoxLayout.Y_AXIS));
			eventJTable = new JTable(new DefaultTableModel() {
				@Override
				public boolean isCellEditable(int row, int column) {
					return false;
				}
			});
			eventJTable.setAutoCreateRowSorter(true);
			eventJTable.setRowSelectionAllowed(true);
			eventJTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

			populateTab(eventsList, eventJTable);

			eventJTable.getSelectionModel().addListSelectionListener(this);
			leftEvP.add(new JScrollPane(eventJTable));
			eventJTable.setFillsViewportHeight(true);
		} catch (Exception e) {
			// visualizzare l'errore ed uscire dalla grafica
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// ------------------------ RIGHT ------------------------------------

		rightEvP = new JPanel(new SpringLayout());

		rightEvP.add(new JLabel("Event Name:", JLabel.TRAILING));
		eName = new JTextField("New Event");
		rightEvP.add(eName);

		rightEvP.add(new JLabel("Location:", JLabel.TRAILING));
		eLocation = new JTextField(currentClub.getClub().getcAddress());
		rightEvP.add(eLocation);

		rightEvP.add(new JLabel("Event Category:", JLabel.TRAILING));
		eCategory = new JTextField();
		rightEvP.add(eCategory);

		rightEvP.add(new JLabel("Starting Date:", JLabel.TRAILING));
		eStartDate = new JDateChooser(new Date());
		eStartDate.setDateFormatString("yyyy/MM/dd");
		eStartDate.setMinSelectableDate(new Date());
		eStartDate.enableInputMethods(false);
		rightEvP.add(eStartDate);

		rightEvP.add(new JLabel("Finishing Date:", JLabel.TRAILING));
		eFinishDate = new JDateChooser(new Date());
		eFinishDate.setDateFormatString("yyyy/MM/dd");
		eFinishDate.setMinSelectableDate(new Date());
		rightEvP.add(eFinishDate);

		rightEvP.add(new JLabel("Starting Time: (hh:mm:ss)", JLabel.TRAILING));
		eStartTime = new JTextField("00:00:00");
		rightEvP.add(eStartTime);

		rightEvP.add(new JLabel("Finishing Time: (hh:mm:ss)", JLabel.TRAILING));
		eFinishTime = new JTextField("00:00:00");
		rightEvP.add(eFinishTime);

		rightEvP.add(new JLabel("Restriction:", JLabel.TRAILING));
		eRestriction = new JTextField();

		rightEvP.add(eRestriction);

		rightEvP.add(new JLabel("Telephon Info.:", JLabel.TRAILING));
		eInfoTel = new JTextField(currentClub.getClub().getcTel());

		rightEvP.add(eInfoTel);

		rightEvP.add(new JLabel("Short Desciption:", JLabel.TRAILING));
		eShortDescription = new JTextField();

		rightEvP.add(eShortDescription);

		rightEvP.add(new JLabel("Long Description:", JLabel.TRAILING));
		eLongDescription = new JTextArea(5, 27);
		eLongDescription.setLineWrap(true);
		JScrollPane descrScrollPane = new JScrollPane(eLongDescription);
		rightEvP.add(descrScrollPane);

		JPanel evButtonsP = new JPanel();
		newEvB = new JButton("New");
		saveEvB = new JButton("Save As New");
		modifyEvB = new JButton("Save");
		deleteEvB = new JButton("Delete");

		modifyEvB.setEnabled(false);
		deleteEvB.setEnabled(false);

		newEvB.addActionListener(this);
		saveEvB.addActionListener(this);
		modifyEvB.addActionListener(this);
		deleteEvB.addActionListener(this);

		evButtonsP.add(newEvB);
		evButtonsP.add(saveEvB);
		evButtonsP.add(modifyEvB);
		evButtonsP.add(deleteEvB);

		rightEvP.add(new JLabel(""));
		rightEvP.add(evButtonsP);

		// ------------------- Event Img ---------------------------
		eImg = new JLabel(new ImageIcon("default.jpg"));
		eImg.setHorizontalAlignment(SwingConstants.CENTER);
		eImg.setPreferredSize(new Dimension(100, 100));

		selectEImgB = new JButton("Select Event Image");
		selectEImgB.addActionListener(this);
		// -------------------------------------------------------------

		rightEvP.add(new JLabel("Event Image:", JLabel.TRAILING));
		rightEvP.add(eImg);
		rightEvP.add(new JLabel(""));
		rightEvP.add(selectEImgB);

		SpringUtilities.makeCompactGrid(rightEvP, 14, 2, 6, 6, 6, 6);

		eventsP.add(leftEvP);
		eventsP.add(rightEvP);
		eventsP.setVisible(true);
		return eventsP;
	}

	// ******************************************************************************
	// Old Event Panel
	// ******************************************************************************

	private JPanel createOldEventsPanel(LinkedList<MyEvent> oldEventsList) {
		JPanel oldEventsP = new JPanel();
		oldEventsP.setLayout(new GridLayout(1, 2));

		// ------------------------ LEFT ------------------------------------
		JPanel leftOldEvP = new JPanel();
		leftOldEvP.setLayout(new BoxLayout(leftOldEvP, BoxLayout.Y_AXIS));

		oldEventJTable = new JTable(new DefaultTableModel() {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		});
		oldEventJTable.setAutoCreateRowSorter(true);
		oldEventJTable.setRowSelectionAllowed(true);
		oldEventJTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		oldEventJTable.getSelectionModel().addListSelectionListener(this);
		populateTab(oldEventsList, oldEventJTable);

		leftOldEvP.add(new JScrollPane(oldEventJTable));

		// ------------------------ RIGHT ------------------------------------

		JPanel rightOldEvP = new JPanel(new SpringLayout());

		rightOldEvP.add(new JLabel("Event Name:", JLabel.TRAILING));
		eOName = new JTextField();
		eOName.setEditable(false);
		rightOldEvP.add(eOName);

		rightOldEvP.add(new JLabel("Location:", JLabel.TRAILING));
		eOLocation = new JTextField();
		eOLocation.setEditable(false);
		rightOldEvP.add(eOLocation);

		rightOldEvP.add(new JLabel("Event Category:", JLabel.TRAILING));
		eOCategory = new JTextField();
		eOCategory.setEditable(false);
		rightOldEvP.add(eOCategory);

		rightOldEvP.add(new JLabel("Starting Date:", JLabel.TRAILING));
		eOStartDate = new JDateChooser(new Date());
		eOStartDate.setDateFormatString("yyyy/MM/dd");
		eOStartDate.setEnabled(false);
		rightOldEvP.add(eOStartDate);

		rightOldEvP.add(new JLabel("Finishing Date:", JLabel.TRAILING));
		eOFinishDate = new JDateChooser(new Date());
		eOFinishDate.setDateFormatString("yyyy/MM/dd");
		eOFinishDate.setEnabled(false);
		eOFinishDate.setMinSelectableDate(new Date());
		rightOldEvP.add(eOFinishDate);

		rightOldEvP
				.add(new JLabel("Starting Time: (hh:mm:ss)", JLabel.TRAILING));
		eOStartTime = new JTextField("00:00:00");
		eOStartTime.setEditable(false);
		rightOldEvP.add(eOStartTime);

		rightOldEvP.add(new JLabel("Finishing Time: (hh:mm:ss)",
				JLabel.TRAILING));
		eOFinishTime = new JTextField("00:00:00");
		eOFinishTime.setEditable(false);
		rightOldEvP.add(eOFinishTime);

		rightOldEvP.add(new JLabel("Restriction:", JLabel.TRAILING));
		eORestriction = new JTextField();
		eORestriction.setEditable(false);
		rightOldEvP.add(eORestriction);

		rightOldEvP.add(new JLabel("Telephon Info.:", JLabel.TRAILING));
		eOInfoTel = new JTextField();
		eOInfoTel.setEditable(false);
		rightOldEvP.add(eOInfoTel);

		rightOldEvP.add(new JLabel("Short Desciption:", JLabel.TRAILING));
		eOShortDescription = new JTextField();
		eOShortDescription.setEditable(false);
		rightOldEvP.add(eOShortDescription);

		rightOldEvP.add(new JLabel("Long Description:", JLabel.TRAILING));
		eOLongDescription = new JTextArea(5, 30);
		eOLongDescription.setLineWrap(true);
		eOLongDescription.setEditable(false);
		JScrollPane descrScrollPane = new JScrollPane(eOLongDescription);
		rightOldEvP.add(descrScrollPane);

		eOImg = new JLabel(new ImageIcon("default.jpg"));
		eOImg.setPreferredSize(new Dimension(100, 100));
		eOImg.setHorizontalAlignment(SwingConstants.CENTER);
		rightOldEvP.add(new JLabel("Event Image:", JLabel.TRAILING));
		rightOldEvP.add(eOImg);

		SpringUtilities.makeCompactGrid(rightOldEvP, 12, 2, 6, 6, 6, 6);

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
		clubNameR = new JTextField(clubData.getcName(), 20);
		clubNameR.setBackground(colorEnf);
		usernameR = new JTextField(clubData.getUsername(), 20);
		usernameR.setBackground(colorEnf);
		usernameR.setEditable(false);
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
		dataP.add(clubNameR);

		lb = new JLabel("Username:", JLabel.TRAILING);
		lb.setPreferredSize(new Dimension(100, 20));
		dataP.add(lb);
		dataP.add(usernameR);

		lb = new JLabel("Password:", JLabel.TRAILING);
		lb.setPreferredSize(new Dimension(100, 20));
		dataP.add(lb);
		dataP.add(pwdR);

		lb = new JLabel("Confirm Pwd:", JLabel.TRAILING);
		lb.setPreferredSize(new Dimension(100, 20));
		dataP.add(lb);
		dataP.add(confPwdR);

		SpringUtilities.makeCompactGrid(dataP, 10, 2, 6, 6, 6, 6);
		leftProfileP.add(dataP);

		buttonsP.add(modifyProfB);
		buttonsP.add(unregProfB);
		buttonsP.add(selectCImgB);
		leftProfileP.add(buttonsP);

		profileStatus = new JLabel(" ");
		statusP.add(new JLabel(""));
		statusP.add(profileStatus);
		leftProfileP.add(statusP);

		// ------------------ RIGHT PROFILE PANEL --------------------------
		JPanel rightProfileP = new JPanel();

		rightProfileP.setBorder(BorderFactory.createTitledBorder("Club Image"));

		cImg = new JLabel(new ImageIcon("default.jpg"));
		// cImg.setPreferredSize(new Dimension(50, 50));
		cImg.setHorizontalAlignment(SwingConstants.CENTER);
		rightProfileP.add(cImg);

		cImageURL = new JTextField(clubData.getcImageURL());
		cImageURL.setEditable(false);
		cImageURL.setVisible(false);
		rightProfileP.add(cImageURL);

		refreshImage(cImg, cImageURL.getText());

		profileP.add(leftProfileP);
		profileP.add(rightProfileP);
		return profileP;
	}

	// ******************************************************************************
	// Utility
	// ******************************************************************************

	private Vector<Vector<String>> getUsersVector(int eventId) {
		Vector<Vector<String>> usersVector = new Vector<Vector<String>>();

		LinkedList<User> usersList = new LinkedList<User>();
		try {
			usersList = currentClub.getEventUsersList(eventId);
		} catch (Exception ex) {
			// TODO add error alert
			ex.printStackTrace();
		}
		if (usersList != null) {
			for (User u : usersList) {
				Vector<String> v = new Vector<String>();
				v.add(u.getId() + "");
				v.add(u.getuName());
				v.add(u.getuSurname());
				v.add(u.getuAge());
				v.add(u.getuSex());
				usersVector.add(v);
			}
			return usersVector;
		}
		return null;
	}

	private void populateTab(LinkedList<MyEvent> list, JTable table) {
		Vector<String> columnNames = new Vector<String>();
		columnNames.addElement("ID");
		columnNames.addElement("Event Name");
		columnNames.addElement("Start");
		columnNames.addElement("End");

		Vector<Vector<String>> eventsVector = new Vector<Vector<String>>();
		for (MyEvent e : list) {
			Vector<String> row = new Vector<String>();
			row.addElement(e.getId() + "");
			row.addElement(e.geteName());
			row.addElement(e.geteStartDate() + " " + e.geteStartTime());
			row.addElement(e.geteFinishDate() + " " + e.geteFinishTime());
			eventsVector.addElement(row);
		}
		DefaultTableModel tm = (DefaultTableModel) table.getModel();
		tm.setDataVector(eventsVector, columnNames);
		tm.fireTableDataChanged();
		eventJTable.getColumnModel().getColumn(0).setPreferredWidth(0);
		// table.setBackground(new Color(153, 204, 255));
		// table.setFont(new Font("SansSerif", Font.PLAIN, 12));
	}

	private void cleanBoxes() {
		// rightEvP.setVisible(false); // TODO ho messo globale il right per
		// vedere se spostare il new..se tolgo questo metto locale il rightEvP
		eName.setText("New Event");
		eShortDescription.setText("");
		eLongDescription.setText("");
		eLocation.setText(currentClub.getClub().getcAddress());
		eCategory.setText("");
		eStartDate.setDate(new Date());
		eFinishDate.setDate(new Date());
		eStartTime.setText("00:00:00");
		eFinishTime.setText("00:00:00");
		eRestriction.setText("");
		eInfoTel.setText(currentClub.getClub().getcTel());
		eSelectedImage = "";
		ePreviousImage = "";
		eRemoteImage = "";
		eImg.setIcon(new ImageIcon("default.jpg"));
		setEventEditable(true);

		modifyEvB.setEnabled(false);
		deleteEvB.setEnabled(false);

		try {
			populateTab(currentClub.getClubEventsList(), messageEventJTable);
			populateTab(currentClub.getClubEventsList(), eventJTable);
			populateTab(currentClub.getOldClubEventsList(), oldEventJTable);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private boolean checkEvErrors() {
		String errMsg = "";
		Date dayOfStart = null;
		Date dayOfFinish = null;
		boolean flag = true;
		try {
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			dayOfStart = df.parse(new StringBuilder(dateFormat
					.format(eStartDate.getDate())).toString()
					+ " " + eStartTime.getText());
			dayOfFinish = df.parse(new StringBuilder(dateFormat
					.format(eFinishDate.getDate())).toString()
					+ " " + eFinishTime.getText());
		} catch (ParseException e) {
			errMsg = "Date are not well formatted! C:1";
			JOptionPane.showMessageDialog(mainFrame, errMsg, "Creation Error!",
					JOptionPane.ERROR_MESSAGE);
			return false;
		}

		if (eName.getText().equals("")) {
			errMsg = "Type a correct event name to preceed.";
			flag = false;
		} else if (dayOfStart.before(new Date())) {
			errMsg = "Start Date/Time must be later now!";
			flag = false;
		} else if (eStartDate.getDate() == null
				|| eFinishDate.getDate() == null) {
			errMsg = "Date are not well formatted!";
			flag = false;
		} else if (dayOfStart.after(dayOfFinish)) {
			errMsg = "Finish Date-Time must be greater than Start Date-Time!";
			flag = false;
		} else if (eStartTime.getText().equals("")
				|| eFinishTime.getText().equals("")) {
			errMsg = "Insert correct time for selected event!";
			flag = false;
		}
		if (!flag) {
			JOptionPane.showMessageDialog(mainFrame, errMsg, "Creation Error!",
					JOptionPane.ERROR_MESSAGE);
		}
		return flag;
	}

	private void setEventEditable(boolean flag) {
		eFinishDate.setEnabled(flag);
		eName.setEditable(flag);
		if (!flag) {
			eName.setToolTipText("Unmodifiable Ongoing Event");
			eName.setBackground(Color.green);
		} else {
			eName.setToolTipText("Event Name");
			eName.setBackground(Color.white);
		}
		eLocation.setEditable(flag);
		eCategory.setEditable(flag);
		eStartDate.setEnabled(flag);
		eStartTime.setEditable(flag);
		eFinishTime.setEditable(flag);
		eRestriction.setEditable(flag);
		eInfoTel.setEditable(flag);
		eShortDescription.setEditable(flag);
		eLongDescription.setEditable(flag);
		modifyEvB.setEnabled(flag);
		deleteEvB.setEnabled(flag);
		selectEImgB.setEnabled(flag);
		saveEvB.setEnabled(flag);
	}

	// ******************************************************************************
	// RUN
	// ******************************************************************************

	public void run() {
		mainFrame = new JFrame(TITLE + VERSION);

		mainFrame.setJMenuBar(createMenu());

		tabPanel = new JTabbedPane();

		mainFrame.add(tabPanel, BorderLayout.CENTER);

		try {
			LinkedList<MyEvent> eventsClubList = currentClub
					.getClubEventsList();
			LinkedList<MyEvent> oldEventsClubList = currentClub
					.getOldClubEventsList();

			tabPanel.add("Events", createEventsPanel(eventsClubList));
			tabPanel.add("Old Events", createOldEventsPanel(oldEventsClubList));
			tabPanel.add("Message", createMessagePanel(eventsClubList));
			tabPanel.add("Profile", createProfilePanel());
		} catch (Exception e) {
			// TODO: handle exception
		}

		// mainFrame.setPreferredSize(new Dimension(1024, 730));
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainFrame.setResizable(false);

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
			if (usersJTable.getSelectedRowCount() > 0) {
				String criterion = "";
				for (int s : usersJTable.getSelectedRows()) {
					criterion += "id=" + usersJTable.getValueAt(s, 0) + " OR ";
				}
				if (criterion != "") {
					try {
						currentClub.spamMobile(currentClub.getClubName() + "-"
								+ message.getText(), criterion.substring(0,
								criterion.length() - 3));
						JOptionPane.showMessageDialog(mainFrame,
								"Message sended correctly!", "Sended",
								JOptionPane.INFORMATION_MESSAGE);
					} catch (RemoteException e1) {
						JOptionPane.showMessageDialog(mainFrame,
								"Message not sended try again!", "Error",
								JOptionPane.ERROR_MESSAGE);
						e1.printStackTrace();
					}
				}
			}
		}
		if (event == sendToAllB) {
			String criterion = "";
			DefaultListModel model = (DefaultListModel) usersJTable.getModel();
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
					boolean res = currentClub.updateClubData(nameR.getText()
							.replace("'", "\\'"), surnameR.getText().replace(
							"'", "\\'"),
							addressR.getText().replace("'", "\\'"), telR
									.getText().replace("'", "\\'"), emailR
									.getText().replace("'", "\\'"), typeR
									.getText().replace("'", "\\'"), clubNameR
									.getText().replace("'", "\\'"), usernameR
									.getText(), new String(pwdR.getPassword()),
							cImageURL.getText());
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
					new Thread(new LoginT()).start();
				}
			} catch (RemoteException e1) {
				// TODO add error message
				e1.printStackTrace();
			}
		}
		if (event == selectEImgB) {
			chooser.setFileFilter(filter);
			if (chooser.showOpenDialog(new JFrame()) == JFileChooser.APPROVE_OPTION) {
				eSelectedImage = chooser.getSelectedFile().getAbsolutePath();
				refreshImage(eImg, eSelectedImage);
			}
		}
		if (event == selectCImgB) {
			chooser.setFileFilter(filter);
			if (chooser.showOpenDialog(new JFrame()) == JFileChooser.APPROVE_OPTION) {
				cImageURL.setText(chooser.getSelectedFile().getAbsolutePath());
				refreshImage(cImg, cImageURL.getText());
			}
		}
		if (event == saveEvB) {
			if (checkEvErrors()) {
				try {
					if (!eSelectedImage.equals("")) {
						String eventName = eName.getText().replace(" ", "_")
								.replace("'", "_");
						String ext = eSelectedImage.substring(eSelectedImage
								.length() - 3);
						eRemoteImage = _URL + "events/"
								+ currentClub.getClub().getId() + "/"
								+ eventName + eStartDate.getDate().getTime()
								+ "." + ext;
					} else {
						eRemoteImage = ePreviousImage;
					}
					currentClub.createEvent(
							eName.getText().replace("'", "\\'"),
							eShortDescription.getText().replace("'", "\\'"),
							eLongDescription.getText().replace("'", "\\'"),
							eLocation.getText().replace("'", "\\'"), eCategory
									.getText().replace("'", "\\'"), eStartDate
									.getDate(), eFinishDate.getDate(),
							eStartTime.getText(), eFinishTime.getText(),
							eRestriction.getText().replace("'", "\\'"),
							eInfoTel.getText().replace("'", "\\'"),
							eRemoteImage);

					if (!eSelectedImage.equals("")) {
						FTPConnection connection = new FTPConnection();
						connection.uploadFile("www/"
								+ eRemoteImage.replace(_URL, ""),
								eSelectedImage);
					}

					JOptionPane.showMessageDialog(mainFrame,
							"Created succesfully " + eName.getText(),
							"Created!", JOptionPane.INFORMATION_MESSAGE);
					cleanBoxes();

				} catch (Exception e1) {
					JOptionPane.showMessageDialog(mainFrame,
							"An error occured during creation of "
									+ eName.getText(), "Error!",
							JOptionPane.ERROR_MESSAGE);
					e1.printStackTrace();
				}
			}
		}
		if (event == modifyEvB && eventJTable.getSelectedRow() != -1) {
			int sel = Integer.parseInt(eventJTable.getValueAt(
					eventJTable.getSelectedRow(), 0).toString());
			if (checkEvErrors()) {
				try {
					if (!eSelectedImage.equals("")) {
						String ext = eSelectedImage.substring(eSelectedImage
								.length() - 3);
						String eventName = eName.getText().replace(" ", "_")
								.replace("'", "_");
						eRemoteImage = _URL + "events/"
								+ currentClub.getClub().getId() + "/"
								+ eventName + eStartDate.getDate().getTime()
								+ "." + ext;
					} else {
						eRemoteImage = ePreviousImage;
					}
					currentClub.updateEvent(new MyEvent(sel, currentClub
							.getClub().getId(), eName.getText().replace("'",
							"\\'"), eShortDescription.getText().replace("'",
							"\\'"), eLongDescription.getText().replace("'",
							"\\'"), eLocation.getText().replace("'", "\\'"),
							eCategory.getText().replace("'", "\\'"), eStartDate
									.getDate(), eFinishDate.getDate(),
							eStartTime.getText(), eFinishTime.getText(),
							eRestriction.getText().replace("'", "\\'"),
							eInfoTel.getText().replace("'", "\\'"),
							eRemoteImage));

					if (!eSelectedImage.equals("")) {
						FTPConnection connection = new FTPConnection();
						connection.uploadFile("www/"
								+ eRemoteImage.replace(_URL, ""),
								eSelectedImage);
					}
					JOptionPane.showMessageDialog(mainFrame,
							"Modified succesfully " + sel, "Modified!",
							JOptionPane.INFORMATION_MESSAGE);
					cleanBoxes();
				} catch (Exception ex) {
					JOptionPane.showMessageDialog(mainFrame, "Not Modified!",
							"Error", JOptionPane.ERROR_MESSAGE);
					ex.printStackTrace();
				}
			}
		}
		if (event == deleteEvB && eventJTable.getSelectedRow() != -1) {
			int sel = Integer.parseInt(eventJTable.getValueAt(
					eventJTable.getSelectedRow(), 0).toString());
			try {
				currentClub.deleteEvent(sel);
				cleanBoxes();
				JOptionPane.showMessageDialog(mainFrame, "Deleted succesfully "
						+ sel, "Deleted!", JOptionPane.INFORMATION_MESSAGE);
			} catch (NumberFormatException e1) {
				JOptionPane.showMessageDialog(mainFrame, "Not Deleted!" + sel,
						"Error", JOptionPane.ERROR_MESSAGE);
				e1.printStackTrace();
			} catch (RemoteException e1) {
				JOptionPane.showMessageDialog(mainFrame, "Not Deleted!" + sel,
						"Error", JOptionPane.ERROR_MESSAGE);
				e1.printStackTrace();
			}
			cleanBoxes();
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
		if (event == newEvB) {
			cleanBoxes();
		}
		if (event == reloadEvents) {
			cleanBoxes();
		}
		if (event == exit) {
			int answer = JOptionPane.showConfirmDialog(mainFrame,
					"Do you really want to close?", "Close",
					JOptionPane.YES_NO_OPTION);
			if (answer == JOptionPane.YES_OPTION)
				System.exit(0);
		}
		if (event == logout) {
			int answer = JOptionPane.showConfirmDialog(mainFrame,
					"Do you really want to logout?", "Logout",
					JOptionPane.YES_NO_OPTION);
			if (answer == JOptionPane.YES_OPTION) {
				try {
					mainFrame.setVisible(false); // TODO review
					new Thread(new LoginT()).start();
				} catch (RemoteException e1) {
					System.exit(0);
				}
			}
		}
	}

	// ******************************************************************************
	// LIST SELECTION LISTENER
	// ******************************************************************************

	public void valueChanged(ListSelectionEvent e) {
		if (e.getSource() == messageEventJTable.getSelectionModel()
				&& messageEventJTable.getRowSelectionAllowed()) {

			Vector<String> columnNames = new Vector<String>();
			columnNames.addElement("ID");
			columnNames.addElement("Nome");
			columnNames.addElement("Cognome");
			columnNames.addElement("Età");
			columnNames.addElement("Sesso");

			sendMessB.setEnabled(true);
			sendToAllB.setEnabled(true);
			int sel = Integer.parseInt(messageEventJTable.getValueAt(
					messageEventJTable.getSelectedRow(), 0).toString());
			((DefaultTableModel) usersJTable.getModel()).setDataVector(
					getUsersVector(sel), columnNames);
		}
		if (e.getSource() == eventJTable.getSelectionModel()
				&& eventJTable.getRowSelectionAllowed()) {
			int selIndex = eventJTable.getSelectedRow();
			if (selIndex != -1) {
				modifyEvB.setEnabled(true);
				deleteEvB.setEnabled(true);
				try {
					MyEvent event = currentClub.getEvent(Integer
							.parseInt((String) eventJTable.getValueAt(selIndex,
									0)));

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
					eSelectedImage = "";
					eRemoteImage = "";
					ePreviousImage = event.geteImageURL();
					refreshImage(eImg, event.geteImageURL());
					eLongDescription.setText(event.geteLongDescription());

					DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					SimpleDateFormat dateFormat = new SimpleDateFormat(
							"yyyy-MM-dd");
					Date dayOfStart = df.parse(new StringBuilder(dateFormat
							.format(eStartDate.getDate())).toString()
							+ " " + eStartTime.getText());
					if (dayOfStart.before(new Date()))
						setEventEditable(false);
					else
						setEventEditable(true);
				} catch (Exception e1) {
					// TODO add error messaggio che dice che
					// l'evento è stato
					// eliminato da qualcuno
					e1.printStackTrace();
				}
			}
		}

		if (e.getSource() == oldEventJTable.getSelectionModel()
				&& oldEventJTable.getRowSelectionAllowed()) {
			int selectedIndex = oldEventJTable.getSelectedRow();
			if (selectedIndex != -1) {
				try {
					MyEvent event = currentClub.getOldEvent(Integer
							.parseInt(oldEventJTable.getValueAt(selectedIndex,
									0).toString()));

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
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		}
	}

	public static void main(String[] args) {
		ClientClub currentClub;
		try {
			currentClub = new ClientClub();

			String userTest = "rosy";
			String passTest = "rosy";

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