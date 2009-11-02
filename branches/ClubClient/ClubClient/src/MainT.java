import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Event;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

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

public class MainT implements Runnable, ActionListener, ListSelectionListener {
	final String club;

	String TITLE = "DIANA: Feel Like Doing... ";
	final String VERSION = " v.1.0";
	final String HELP_ITA = "text/ita.txt"; // TODO creare help
	final String HELP_ENG = "text/eng.txt"; // TODO creare help

	// --------------Default Operations
	JFrame mainFrame;
	JTabbedPane tabPanel;

	// -------------- Menu Items -------------------
	JMenuItem logout;
	JMenuItem exit;
	JMenuItem help;
	JMenuItem credits;

	// -------------- Message Items ---------------
	JList eventJList;
	JList usersJList;
	JButton loadUsersB;
	JButton sendMessB;
	JTextArea message;

	// -------------- Events Items ---------------
	JTextArea evDescription;
	JButton createEvB;
	JButton modifyEvB;
	JButton deleteEvB;
	JTextField evName;
	JList eventJList2;

	// -------------- Profile Items ----------------------------
	JTextField nameR;
	JTextField surnameR;
	JTextField addressR;
	JTextField telR;
	JTextField userR;
	JPasswordField pwdR;
	JPasswordField confPwdR;
	JButton modifyProfB;

	// -------------------------------------------

	public MainT(String club) {
		this.club = club;
		this.TITLE = this.TITLE + "(" + club + " Client)";
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

	// ------------------------- MESSAGE PANEL -------------------------------

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

		leftUpP.add(new JLabel(
				"Planned Events             "));
		leftUpP.add(new JLabel(
				"                 Users for Selected Event"));

		JPanel leftDownP = new JPanel(new GridLayout(1, 2));
		// TODO caricare eventi dal server
		String events[] = { "Erasmus Party", "Cera l'h", "Paolo Bolognesi DJ",
				"Halloween Night" };

		eventJList = new JList(events);
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
		//rightMsgP.setLayout(new BoxLayout(rightMsgP, BoxLayout.Y_AXIS));
		rightMsgP.setBorder(BorderFactory.createTitledBorder(""));

		sendMessB = new JButton("Send Message");
		sendMessB.addActionListener(this);
		sendMessB.setEnabled(false);
		rightMsgP.add(sendMessB);

		message = new JTextArea("Type here your message...",13,28);
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

	// --------------------- EVENTS PANEL --------------------------------------

	private JPanel createEventsPanel() {
		JPanel eventsP = new JPanel();
		eventsP.setLayout(new GridLayout(1, 2));

		// ------------------------ LEFT ------------------------------------
		JPanel leftEvP = new JPanel();
		leftEvP.setBorder(BorderFactory.createTitledBorder(""));
		leftEvP.setLayout(new BoxLayout(leftEvP, BoxLayout.Y_AXIS));

		// TODO caricare eventi dal server
		String events[] = { "Erasmus Party", "Cera l'h", "Paolo Bolognesi DJ",
				"Halloween Night" };

		eventJList2 = new JList(events);
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
		evName = new JTextField();
		evName.setPreferredSize(new Dimension(390, 20));
		rightEvP.add(evName);

		rightEvP.add(new JLabel("Event Description:"));
		evDescription = new JTextArea(13, 35);
		evDescription.setLineWrap(true);
		JScrollPane descrScrollPane = new JScrollPane(evDescription);
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

	// -------------------- CLUB PROFILE PANEL-------------------------------

	public JPanel createProfilePanel() {
		JPanel leftProfileP = new JPanel();
		leftProfileP.setLayout(new BoxLayout(leftProfileP, BoxLayout.Y_AXIS));
		leftProfileP.setBorder(BorderFactory.createTitledBorder("Club Data"));
		
		JPanel nP = new JPanel();
		//nP.setBackground(Color.WHITE);
		JPanel sP = new JPanel();
		//sP.setBackground(Color.WHITE);
		JPanel aP = new JPanel();
		//aP.setBackground(Color.WHITE);
		JPanel tP = new JPanel();
		//tP.setBackground(Color.WHITE);
		JPanel uP = new JPanel();
		//uP.setBackground(Color.WHITE);
		JPanel pP = new JPanel();
		//pP.setBackground(Color.WHITE);
		JPanel cP = new JPanel();
		//cP.setBackground(Color.WHITE);
		JPanel bP = new JPanel();
		//bP.setBackground(Color.WHITE);
		
		nameR = new JTextField(20);
		nameR.setBackground(new Color(255,215,0));
		surnameR = new JTextField(20);
		surnameR.setBackground(Color.yellow);
		addressR = new JTextField(20);
		addressR.setBackground(Color.yellow);
		telR = new JTextField(20);
		telR.setBackground(Color.yellow);
		userR = new JTextField(20);
		userR.setBackground(Color.yellow);
		pwdR = new JPasswordField(20);
		pwdR.setBackground(Color.yellow);
		confPwdR = new JPasswordField(20);
		confPwdR.setBackground(Color.yellow);
		modifyProfB = new JButton("Save Changes");
		modifyProfB.addActionListener(this);

		//TODO load clubs data from server
		nP.add(new JLabel("Owner Name:      "));
		nP.add(nameR);
		leftProfileP.add(nP);

		sP.add(new JLabel("Owner Surname:"));
		sP.add(surnameR);
		leftProfileP.add(sP);
		
		aP.add(new JLabel("Club Address:     "));
		aP.add(addressR);
		leftProfileP.add(aP);
		
		tP.add(new JLabel("Club Tel.:              "));
		tP.add(telR);
		leftProfileP.add(tP);
		
		uP.add(new JLabel("Club Name:          "));
		uP.add(userR);
		leftProfileP.add(uP);
		
		pP.add(new JLabel("Password:            "));
		pP.add(pwdR);
		leftProfileP.add(pP);
		
		cP.add(new JLabel("Confirm Pwd:       "));
		cP.add(confPwdR);
		leftProfileP.add(cP);
		
		bP.add(new JLabel("                     "));
		bP.add(modifyProfB);
		leftProfileP.add(bP);

		leftProfileP.setVisible(true);
		return leftProfileP;
	}

	// ******************************************************************************
	// RUN
	// ******************************************************************************

	public void run() {
		mainFrame = new JFrame(TITLE + VERSION);
		mainFrame.setPreferredSize(new Dimension(800, 400));
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainFrame.setResizable(false);

		mainFrame.setJMenuBar(createMenu());
		
		tabPanel = new JTabbedPane();

		mainFrame.add(tabPanel, BorderLayout.CENTER);

		tabPanel.add("Message", createMessagePanel());
		tabPanel.add("Events", createEventsPanel());
		tabPanel.add("Profile", createProfilePanel());

		mainFrame.pack();
		//mainFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
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
		if (event == createEvB) {
			// TODO sinc with server
			String newEv = evName.getText();
			if (newEv.equals("")) {
				JOptionPane.showMessageDialog(mainFrame,
						"Type a correct event name to preceed.",
						"Creation Error!", JOptionPane.ERROR_MESSAGE);
			} else {
				JOptionPane.showMessageDialog(mainFrame, "Created succesfully "
						+ newEv, "Created!", JOptionPane.INFORMATION_MESSAGE);
			}
		}
		if (event == modifyEvB) {
			// TODO sinc with server
			String selEv = eventJList2.getSelectedValue().toString();
			JOptionPane.showMessageDialog(mainFrame, "Modified succesfully "
					+ selEv, "Modified!", JOptionPane.INFORMATION_MESSAGE);

		}
		if (event == deleteEvB) {
			// TODO sinc with server
			String selEv = eventJList2.getSelectedValue().toString();
			JOptionPane.showMessageDialog(mainFrame, "Deleted succesfully"
					+ selEv, "Deleted!", JOptionPane.INFORMATION_MESSAGE);
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

				JFrame helpFrame = new JFrame("J-MAS : Help");
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

			evName.setText(((JList) e.getSource()).getSelectedValue()
					.toString());
			// TODO inserire i dati dell'evento prelevati dal server
			evDescription.setText("Descrizione di "
					+ ((JList) e.getSource()).getSelectedValue().toString());

		}
	}

	public static void main(String[] args) {
		new Thread(new MainT("Barone Rosso")).start();
	}

}