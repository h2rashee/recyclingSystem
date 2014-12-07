import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;
import java.util.*;
import java.text.*;


//-----------------------------------------------------------------------------
// Main Program; This is the password screen that opens up on 
// start-up of the application.
public class Program extends JFrame implements ActionListener
{

   //Main method of program initiates the password screen
	public static void main (String [] args)
	{
		Program EP = new Program("");
		EP.FR.setVisible(true);
	}

	JFrame FR = new JFrame("Ecology Club - Recycling Activity Monitoring System");
	Container Obj1 = getContentPane();
	GridBagLayout GBL = new GridBagLayout();
	GridBagConstraints GBC = new GridBagConstraints();
		
	JMenuBar MB = new JMenuBar();
	JMenu file = new JMenu("File");
	JMenuItem CloseFile = new JMenuItem("Close");

	Font f = new Font("Comic Sans MS", Font.BOLD, 22);
	Color c = new Color(6, 69, 1);

	ImageIcon logo = new ImageIcon("ClubLogo.jpg");
	JLabel lbl1 = new JLabel("",logo,SwingConstants.TRAILING);
	JLabel lbl2 = new JLabel("The EIS-J Ecology Club");
	JLabel lblPass = new JLabel("Password:");
	JButton btnSubmit = new JButton("Submit");
	JButton btnForgotPass = new JButton("Forgot Password?");

	JPasswordField txtPass = new JPasswordField(20);

	int PassCounter = 0;	   	//Counter for failed login attempts
	int sQtionCounter = 0;		//Counter for failed secret question attempts
	
   //Constructor for the Password Screen that places components on the Frame
	public Program(String str)
	{
		super(str);

		getContentPane().setLayout(GBL);
		
		FR.setJMenuBar(MB);
		FR.add(getContentPane());
		MB.add(file);
		file.add(CloseFile);

		GBC.fill = GridBagConstraints.BOTH;
		GBC.anchor = GridBagConstraints.CENTER;
		GBC.gridwidth = 3;
		GBC.gridheight = 1;
		GBC.gridy = 0;
		GBC.gridx = 0;
		GBC.insets = new Insets(10,10,10,10);
		lbl2.setFont(f);
		lbl2.setForeground(Color.white);
		lbl2.setHorizontalAlignment(JLabel.CENTER);
		GBL.setConstraints(lbl2,GBC);
		getContentPane().add(lbl2);

		GBC.gridy = 1;
		GBC.gridheight = 3;
		GBL.setConstraints(lbl1,GBC);
		getContentPane().add(lbl1);

		GBC.gridx = 2;
		GBC.gridy = 6;
		GBC.gridwidth = 1;
		GBC.gridheight = 2;
		GBL.setConstraints(btnSubmit,GBC);
		getContentPane().add(btnSubmit);
		
		GBC.gridx = 4;
		GBC.gridy = 5;
		GBC.gridwidth = 1;
		GBL.setConstraints(btnForgotPass,GBC);
		getContentPane().add(btnForgotPass);

		txtPass.setEchoChar('*');
		lblPass.setLabelFor(txtPass);

		GBC.gridx = 2;
		GBC.gridwidth = 3;
		GBC.gridheight = 1;
		GBC.gridy = 4;
		GBL.setConstraints(lblPass,GBC);
		lblPass.setForeground(Color.white);
		getContentPane().add(lblPass);
		
		GBC.gridy = 5;
		GBC.gridwidth = 1;
		GBL.setConstraints(txtPass,GBC);
		getContentPane().add(txtPass);
		
		getContentPane().setBackground(c);
		FR.setExtendedState(Frame.MAXIMIZED_BOTH);
		
		btnSubmit.addActionListener(this);
		btnForgotPass.addActionListener(this);
		CloseFile.addActionListener(this);
		validate();

		addWindowListener(new WindowAdapter()
		{	
			public void windowClosing(WindowEvent we)
			{
				setVisible(false);
				System.exit(0);
			}
		});
	}

   // This method reads the random access file thats store the system
   // security details. The password stored in the file is returned to
   // the method and this is used in the actionPerformed method.
	private String currentPassword()
	{
      //Creates object of SystemSecurity.dat file that stores the program's password
		File PasswordStore = new File("SystemSecurity.dat");
      //Initialises variable to store the password in the RAF
		String pass = "";
		
      //Checks if the system security file exists in the current directory
		if(!PasswordStore.exists())
		{
         // Error beep sound
			Toolkit.getDefaultToolkit().beep();
			JOptionPane.showMessageDialog(this, "Error. The file that stores the password does not exist.","Error Message", JOptionPane.ERROR_MESSAGE);
			System.exit(1);		//Shuts down the program as a malfunction because login is not possible without the password file
			return null;
		}
		else
		{
			try
			{
				RandomAccessFile RAF = new RandomAccessFile(PasswordStore, "r");	//Creates object to read RAF
				RAF.seek(0);								//Sets pointer to start of file
				for(int i = 0; i < 20; i++)
				{
					byte letter = RAF.readByte();		//Reads each character from the first 20 characters of the file
					pass = pass + (char) letter;		//Adds each character from the password field of the file
				}
				pass = pass.trim();						//Removes spaces from the string
				RAF.close();							//Closes Random Access File
			}
			catch(Exception e)
			{
				Toolkit.getDefaultToolkit().beep();		//Error beep sound
				JOptionPane.showMessageDialog(this, "An error has occured. Please contact Harris Rasheed to deal with this issue\nError Code: " + e,"Error Message", JOptionPane.ERROR_MESSAGE);	//Output any errors caught
			}
		}
		return pass;			//Returns the password stored in the file
	}
//-----------------------------------------------------------------
//This method reads the random access file thats store the system
//security details. The secret question's answer stored in the file
//is returned to the method and this is used in the actionPerformed method.
	private String[] currentSQtionAnswer()
	{
			File PasswordStore = new File("SystemSecurity.dat");	//Creates an object of SystemSecurity.dat

			try
			{
				String[] sQtion = new String[2];
				RandomAccessFile RAF = new RandomAccessFile(PasswordStore, "r");		//Creates object to read Random Access File
				
				RAF.seek(20);			//Goes to the 20th position of the file where the secret question is stored
				sQtion[0] = "";
				
				for(int i = 0; i < 60; i++)		//Loop reads secret question from RAF
				{
					byte c = RAF.readByte();
					sQtion[0] += (char) c;
				}
				
				RAF.seek(80);			//Goes to the 80th position where the secret answer is stored
				sQtion[1] = "";
				
				for(int i = 0; i < 20; i++)		//Loop reads answer to secret question from RAF
				{
					byte c = RAF.readByte();
					sQtion[1] += (char) c;
				}
				
				sQtion[0] = sQtion[0].trim();	//Remove whitespace after and before the secret question
				sQtion[1] = sQtion[1].trim();	//Remove whitespace after and before the secret answer
				RAF.close();
				return sQtion;			//Returns RAF secret question and answer
			}
			catch(Exception e)
			{
				Toolkit.getDefaultToolkit().beep();
				JOptionPane.showMessageDialog(this, "An unexpected error occured. Please contact Harris Rasheed for more information.\nError Code: " + e,"Error Message", JOptionPane.ERROR_MESSAGE);	//Output any errors caught
			}
		return null;
	}
//-----------------------------------------------------------------
//This method is used to execute the appropriate method when the user performs an action event
	public void actionPerformed(ActionEvent ae)
	{
		if(ae.getSource()==btnSubmit)
		{
			if(txtPass.getText().equals(""))
			{
				JOptionPane.showMessageDialog(this, "Error! Please input a password.","Error Message", JOptionPane.ERROR_MESSAGE);
			}
			else if((txtPass.getText()).trim().equals(currentPassword()))	//Condition that input password is correct
			{
				FR.setVisible(false);				//Hides current window
				menuPage MP = new menuPage("");		//Creates object of Menu class and executes constructor
				MP.FR.setVisible(true);				//Makes Menu Page's Frame visible
			}
			else
			{
				PassCounter++;					//Adds one to the counter for the failed attempt
				
				if(PassCounter==5)				//Checks if the counter has reached five failed attempts
				{
					JOptionPane.showMessageDialog(this, "You have exceeded the number of login attempts available.\nThis program will now shut down.","Error Message", JOptionPane.ERROR_MESSAGE);
					Toolkit.getDefaultToolkit().beep();
					System.exit(0);				//Exits program because of excess login attempts
				}
				else
				{
					JOptionPane.showMessageDialog(this, "The password you have input is incorrect. Please retype the correct password.","Error Message", JOptionPane.ERROR_MESSAGE);
					Toolkit.getDefaultToolkit().beep();
					txtPass.setText("");		//Clears the password field
					txtPass.requestFocus();		//Makes the cursor focus on the password field so that the password can be retyped
				}
			}
		}
		
		else if(ae.getSource()==btnForgotPass)
		{
			String gAnswer = JOptionPane.showInputDialog(null, currentSQtionAnswer()[0],"Forgot Password", 3);	//User's given secret answer
			
			if(gAnswer.equals(null)||gAnswer.equals(""))			//Tests if the given answer is blank or null
			{
				JOptionPane.showMessageDialog(this, "Error. Please input an answer!","Error Message", JOptionPane.ERROR_MESSAGE);
				return;
			}
        	
        	if(gAnswer.equalsIgnoreCase(currentSQtionAnswer()[1]))		//Test if the input answer is correct
        	{
        		FR.setVisible(false);
				menuPage MP = new menuPage("");
				MP.FR.setVisible(true);
        	}
        	else
        	{
        		Toolkit.getDefaultToolkit().beep();
        		sQtionCounter++;				//Appends one to the secret question counter because of failed login
        		
        		if(sQtionCounter == 3)			//Tests when the 3 failed secret question attempts have been made
        		{
        			JOptionPane.showMessageDialog(this, "You have exceeded the number of secret question attempts available.\nThis program will now shut down.","Error Message", JOptionPane.ERROR_MESSAGE);
					System.exit(0);				//Exits program because of excess login attempts
        		}
        		else
        		{
        			JOptionPane.showMessageDialog(null, "Error! The answer you have input is incorrect.", "Error", JOptionPane.ERROR_MESSAGE);
        		}
        	}
		}
		
		else if(ae.getSource()==CloseFile)
		{
			System.exit(0);		//Exits program because the close button from the menubar is pressed
		}
	}
}
//----------------------------------------------------------------------------------------------------------------------
//Menu Screen; This is the main menu of the program where all features of the program can be accessed through.
class menuPage extends JFrame implements ActionListener
{
	JFrame FR = new JFrame("Recycling Activity Monitoring System - Main Menu");
	Container Obj1 = getContentPane();
	GridBagLayout GBL = new GridBagLayout();
	GridBagConstraints GBC = new GridBagConstraints();
		
	JMenuBar MB = new JMenuBar();
	JMenu file = new JMenu("File");
	JMenu view = new JMenu("View");
	JMenu help = new JMenu("Help");
	JMenu bgColour = new JMenu("Background Colour");
	JMenuItem logOut = new JMenuItem("Log out");
	JMenuItem Exit = new JMenuItem("Exit");
	JMenuItem About = new JMenuItem("About");
	ButtonGroup rbg = new ButtonGroup();
	JRadioButtonMenuItem bgYellow = new JRadioButtonMenuItem("Yellow", false);
	JRadioButtonMenuItem bgOrange = new JRadioButtonMenuItem("Orange", false);
	JRadioButtonMenuItem bgRed = new JRadioButtonMenuItem("Red", false);
	JRadioButtonMenuItem bgPink = new JRadioButtonMenuItem("Pink", false);
	JRadioButtonMenuItem bgLightGreen = new JRadioButtonMenuItem("Light Green", false);
	JRadioButtonMenuItem bgDarkGreen = new JRadioButtonMenuItem("Dark Green", true);
	JRadioButtonMenuItem bgBlue = new JRadioButtonMenuItem("Dark Blue", false);
	JRadioButtonMenuItem bgCyan = new JRadioButtonMenuItem("Cyan", false);
	JRadioButtonMenuItem bgMagenta = new JRadioButtonMenuItem("Magenta", false);
	JRadioButtonMenuItem bgWhite = new JRadioButtonMenuItem("White", false);
	JRadioButtonMenuItem bgLightGray = new JRadioButtonMenuItem("Light Gray", false);
	JRadioButtonMenuItem bgDarkGray = new JRadioButtonMenuItem("Dark Gray", false);
	JRadioButtonMenuItem bgBlack = new JRadioButtonMenuItem("Black", false);

	JButton btnThursRecycQuota = new JButton("Thursday Recycling Quota");		//Create Menu Screen Buttons
	JButton btnRecyAttendanceReport = new JButton("Recycler Attendance Report");
	JButton btnRecyActivityReport = new JButton("Recycling Activity Report");
	JButton btnRecyRegist = new JButton("Recycler Registration");
	JButton btnRecyMonCrit = new JButton("Recycler of the Month Candidate Criteria");
	JButton btnTeacherClass = new JButton("Teachers & Classrooms Plan");
	JButton btnFormClass = new JButton("Form Class Locations");
	JButton btnSecuritySett = new JButton("Security Settings");
	JButton btnLogOut = new JButton("Log out");
	JLabel lblTitle = new JLabel("Menu");
	
	Color c = new Color(6,69,1);
	Font f = new Font("Comic Sans MS", Font.BOLD, 28);
	
//-----------------------------------------------------------------	
//Constructor for the Menu Screen that places components on the Frame
	public menuPage(String str)
	{
		super(str);
		getContentPane().setLayout(GBL);
		
		FR.setJMenuBar(MB);
		FR.add(getContentPane());
		
		GBC.fill = GridBagConstraints.BOTH;
		GBC.anchor = GridBagConstraints.CENTER;
		GBC.gridwidth = 2;
		GBC.gridheight = 2;
		GBC.gridy = 1;
		GBC.gridx = 1;
		GBC.ipady = 20;
		GBC.insets = new Insets(10,10,10,10);	

		lblTitle.setFont(f);
		lblTitle.setHorizontalAlignment(JLabel.CENTER);
		lblTitle.setForeground(Color.white);
		GBL.setConstraints(lblTitle,GBC);
		getContentPane().add(lblTitle);
		
		GBC.gridy = 3;
		GBL.setConstraints(btnThursRecycQuota,GBC);
		getContentPane().add(btnThursRecycQuota);
		
		GBC.gridy = 5;
		GBL.setConstraints(btnRecyAttendanceReport,GBC);
		getContentPane().add(btnRecyAttendanceReport);
		
		GBC.gridy = 7;
		GBL.setConstraints(btnRecyActivityReport,GBC);
		getContentPane().add(btnRecyActivityReport);
		
		GBC.gridy = 9;
		GBL.setConstraints(btnRecyRegist,GBC);
		getContentPane().add(btnRecyRegist);
		
		GBC.gridy = 11;
		GBL.setConstraints(btnRecyMonCrit,GBC);
		getContentPane().add(btnRecyMonCrit);
		
		GBC.gridy = 13;
		GBL.setConstraints(btnTeacherClass,GBC);
		getContentPane().add(btnTeacherClass);
		
		GBC.gridy = 15;
		GBL.setConstraints(btnFormClass,GBC);
		getContentPane().add(btnFormClass);
		
		GBC.gridy = 17;
		GBC.gridheight = GridBagConstraints.RELATIVE;
		GBL.setConstraints(btnSecuritySett,GBC);
		getContentPane().add(btnSecuritySett);
		
		GBC.gridy = 25;
		GBC.gridx = 2;
		GBC.weighty = 1;
		GBC.gridheight = GridBagConstraints.REMAINDER;
		GBL.setConstraints(btnLogOut,GBC);
		getContentPane().add(btnLogOut);
		
		MB.add(file);
		MB.add(view);
		MB.add(help);
		file.add(logOut);
		file.add(Exit);
		view.add(bgColour);
		help.add(About);

		view.add(bgColour);
		bgColour.add(bgYellow);		//Add Background Colour Radio Buttons
		rbg.add(bgYellow);			//Add Radio Buttons to Button Group
		bgColour.add(bgOrange);
		rbg.add(bgOrange);
		bgColour.add(bgRed);
		rbg.add(bgRed);
		bgColour.add(bgPink);
		rbg.add(bgPink);
		bgColour.add(bgLightGreen);
		rbg.add(bgLightGreen);
		bgColour.add(bgDarkGreen);
		rbg.add(bgDarkGreen);
		bgColour.add(bgCyan);
		rbg.add(bgCyan);
		bgColour.add(bgBlue);
		rbg.add(bgBlue);
		bgColour.add(bgMagenta);
		rbg.add(bgMagenta);
		bgColour.add(bgWhite);
		rbg.add(bgWhite);
		bgColour.add(bgLightGray);
		rbg.add(bgLightGray);
		bgColour.add(bgDarkGray);
		rbg.add(bgDarkGray);
		bgColour.add(bgBlack);
		rbg.add(bgBlack);

		file.setMnemonic('f');		//Add Keyboard Shortcut Keys
		view.setMnemonic('v');
		Exit.setMnemonic('x');
		logOut.setMnemonic('o');
		bgColour.setMnemonic('b');
		bgYellow.setMnemonic('y');
		bgOrange.setMnemonic('o');
		bgRed.setMnemonic('r');
		bgPink.setMnemonic('p');
		bgLightGreen.setMnemonic('l');
		bgDarkGreen.setMnemonic('g');
		bgBlue.setMnemonic('u');
		bgCyan.setMnemonic('c');
		bgMagenta.setMnemonic('m');
		bgWhite.setMnemonic('w');
		bgLightGray.setMnemonic('a');
		bgDarkGray.setMnemonic('d');
		bgBlack.setMnemonic('b');

		getContentPane().setBackground(c);
		FR.setExtendedState(Frame.MAXIMIZED_BOTH);
		
		bgYellow.addActionListener(this);
		bgOrange.addActionListener(this);
		bgRed.addActionListener(this);
		bgPink.addActionListener(this);
		bgLightGreen.addActionListener(this);
		bgDarkGreen.addActionListener(this);
		bgBlue.addActionListener(this);
		bgCyan.addActionListener(this);
		bgMagenta.addActionListener(this);
		bgWhite.addActionListener(this);
		bgLightGray.addActionListener(this);
		bgDarkGray.addActionListener(this);
		bgBlack.addActionListener(this);
		
		btnThursRecycQuota.addActionListener(this);
		btnRecyAttendanceReport.addActionListener(this);
		btnRecyActivityReport.addActionListener(this);
		btnRecyRegist.addActionListener(this);
		btnRecyMonCrit.addActionListener(this);
		btnTeacherClass.addActionListener(this);
		btnFormClass.addActionListener(this);
		btnSecuritySett.addActionListener(this);
		btnLogOut.addActionListener(this);
		
		About.addActionListener(this);
		logOut.addActionListener(this);
		Exit.addActionListener(this);
		validate();
		
		addWindowListener(new WindowAdapter()			//Activate Window 'X' Button
		{	
			public void windowClosing(WindowEvent we)
			{
				setVisible(false);
				System.exit(0);
			}
		});
	}
//-----------------------------------------------------------------
//This method is used to execute the appropriate method when the user performs an action event
	public void actionPerformed(ActionEvent ae)
	{
		if(bgYellow.isSelected()==true)		//ActionListener checks if the user has chosen a yellow background colour
		{
			getContentPane().setBackground(Color.yellow);		//The background colour changes to yellow
		}

		else if(bgOrange.isSelected()==true)
		{
			getContentPane().setBackground(Color.orange);
		}

		else if(bgRed.isSelected()==true)
		{
			getContentPane().setBackground(Color.red);
		}

		else if(bgPink.isSelected()==true)
		{
			getContentPane().setBackground(Color.pink);
		}

		else if(bgLightGreen.isSelected()==true)
		{
			getContentPane().setBackground(Color.green);
		}

		else if(bgDarkGreen.isSelected()==true)
		{
			getContentPane().setBackground(c);
		}

		else if(bgWhite.isSelected()==true)
		{
			getContentPane().setBackground(Color.white);
		}

		else if(bgBlue.isSelected()==true)
		{
			getContentPane().setBackground(Color.blue);
		}

		else if(bgCyan.isSelected()==true)
		{
			getContentPane().setBackground(Color.cyan);
		}

		else if(bgMagenta.isSelected()==true)
		{
			getContentPane().setBackground(Color.magenta);
		}

		else if(bgBlack.isSelected()==true)
		{
			getContentPane().setBackground(Color.black);
		}

		else if(bgDarkGray.isSelected()==true)
		{
			getContentPane().setBackground(Color.darkGray);
		}

		else if(bgLightGray.isSelected()==true)
		{
			getContentPane().setBackground(Color.lightGray);
		}

		if(ae.getSource()==btnThursRecycQuota)
		{
			FR.setVisible(false);
			thursdayRecyclingQuota TRQ = new thursdayRecyclingQuota("");
			TRQ.FR.setVisible(true);
		}

		if(ae.getSource()==btnRecyAttendanceReport)
		{
			FR.setVisible(false);
			recyclerAttendanceReport RAttR = new recyclerAttendanceReport("");
			RAttR.FR.setVisible(true);
		}
		
		if(ae.getSource()==btnRecyActivityReport)
		{
			FR.setVisible(false);
			recyclingActivityReport RActR = new recyclingActivityReport("");
			RActR.FR.setVisible(true);
		}
		
		if(ae.getSource()==btnRecyRegist)
		{
			FR.setVisible(false);
			recyclerRegistration RR = new recyclerRegistration("");
			RR.FR.setVisible(true);
		}
		
		if(ae.getSource()==btnRecyMonCrit)
		{
			FR.setVisible(false);
			RoMCriterion RoMC = new RoMCriterion("");
			RoMC.FR.setVisible(true);
		}
		
		if(ae.getSource()==btnTeacherClass)
		{
			FR.setVisible(false);
			teacherClassPlan TCP = new teacherClassPlan("");
			TCP.FR.setVisible(true);
		}
		
		if(ae.getSource()==btnFormClass)
		{
			FR.setVisible(false);
			formClassroomLocation FCL = new formClassroomLocation("");
			FCL.FR.setVisible(true);
		}
		
		if(ae.getSource()==btnSecuritySett)
		{
			FR.setVisible(false);
			securitySett SS = new securitySett("");
			SS.FR.setVisible(true);
		}
		
		if(ae.getSource()==About)
		{
			JOptionPane.showMessageDialog(this, "Recycling Activity Monitoring System Version 1.0\nDeveloper: Harris Rasheed\nDate Developed: 13th March 2010", "About", JOptionPane.INFORMATION_MESSAGE);
		}
						
		if(ae.getSource()==logOut||ae.getSource()==btnLogOut)
		{
			FR.setVisible(false);
			Program EP = new Program("");
			EP.FR.setVisible(true);
		}
		if(ae.getSource()==Exit)
		{
			System.exit(0);
		}
	}
}
//----------------------------------------------------------------------------------------------------------------------
//Thursday Recycling Quota Menu Screen; This is the Thursday Recycling Quota Menu screen where the Morning Skip Monitor 
//input screen can be accessed or the Lunch Collection Rounds input screen.
class thursdayRecyclingQuota extends JFrame implements ActionListener
{
	JFrame FR = new JFrame("Recycling Activity Monitoring System - Thursday Recycling Quota");
	Container Obj1 = getContentPane();
	GridBagLayout GBL = new GridBagLayout();
	GridBagConstraints GBC = new GridBagConstraints();
	JMenuBar MB = new JMenuBar();
	JMenu file = new JMenu("File");
	JMenuItem logOut = new JMenuItem("Log out");
	JMenuItem Exit = new JMenuItem("Exit");
	JButton MorningSkipbtn = new JButton("Morning Skip Monitor");
	JButton LunchCollbtn = new JButton("Lunch Collection Rounds");
	JButton btnBack = new JButton("Back");
	JLabel lblThursRecycQuota = new JLabel("Thursday Recycling Quota");
	Color c = new Color(6,69,1);
	Font f = new Font("Comic Sans MS", Font.BOLD, 22);
	
//-----------------------------------------------------------------
//Constructor for the Thursday Recycling Quota Screen that places components on the Frame
	public thursdayRecyclingQuota(String str)
	{
		super(str);
		
		FR.setJMenuBar(MB);
		MB.add(file);
		file.add(logOut);
		file.add(Exit);
				
		getContentPane().setLayout(GBL);
		FR.add(Obj1);
		
		GBC.fill = GridBagConstraints.BOTH;
		GBC.anchor = GridBagConstraints.PAGE_START;
		GBC.gridwidth = 2;
		GBC.gridheight = 2;
		GBC.gridy = 1;
		GBC.gridx = 1;
		GBC.insets = new Insets(10,10,10,10);
		GBC.fill = GridBagConstraints.VERTICAL;
		GBL.setConstraints(lblThursRecycQuota,GBC);
		lblThursRecycQuota.setFont(f);
		lblThursRecycQuota.setHorizontalAlignment(JLabel.CENTER);
		lblThursRecycQuota.setForeground(Color.white);
		getContentPane().add(lblThursRecycQuota);
		
		GBC.anchor = GridBagConstraints.CENTER;
		GBC.gridy = 3;
		GBC.ipady = 20;
		GBC.ipadx = 100;
		GBL.setConstraints(MorningSkipbtn,GBC);
		getContentPane().add(MorningSkipbtn);
		
		GBC.gridy = 5;
		GBL.setConstraints(LunchCollbtn,GBC);
		getContentPane().add(LunchCollbtn);
		
		GBC.gridy = 20;
		GBC.anchor = GridBagConstraints.PAGE_END;
		GBC.insets = new Insets(250,10,10,10);
		GBL.setConstraints(btnBack,GBC);
		getContentPane().add(btnBack);
		
		getContentPane().setBackground(c);
		FR.setExtendedState(Frame.MAXIMIZED_BOTH);
		
		MorningSkipbtn.addActionListener(this);
		LunchCollbtn.addActionListener(this);
		logOut.addActionListener(this);
		Exit.addActionListener(this);
		btnBack.addActionListener(this);
		validate();
	}
//-----------------------------------------------------------------	
//This method is used to execute the appropriate method when the user performs an action event
	public void actionPerformed(ActionEvent ae)
	{
		if(ae.getSource()==MorningSkipbtn)
		{
			FR.setVisible(false);
			morningSkipMonitor MSM = new morningSkipMonitor("");
			MSM.FR.setVisible(true);
		}
		if(ae.getSource()==LunchCollbtn)
		{
			FR.setVisible(false);
			lunchCollRounds LCR = new lunchCollRounds("");
			LCR.FR.setVisible(true);
		}
		if(ae.getSource()==btnBack)
		{
			FR.setVisible(false);
			menuPage MP = new menuPage("");
			MP.FR.setVisible(true);
		}
		if(ae.getSource()==logOut)
		{
			FR.setVisible(false);
			Program EP = new Program("");
			EP.FR.setVisible(true);
		}
		if(ae.getSource()==Exit)
		{
			System.exit(0);
		}
	}
}
//----------------------------------------------------------------------------------------------------------------------
//Morning Skip Monitor Screen; This is the screen where the user can input data
//collected by the Recycling Skip Supervisor on Thursday Mornings
class morningSkipMonitor extends JFrame implements ActionListener
{
	JFrame FR = new JFrame("Recycling Activity Monitoring System - Morning Skip Monitor");
	Container Obj1 = getContentPane();
	GridBagLayout GBL = new GridBagLayout();
	GridBagConstraints GBC = new GridBagConstraints();
	JMenuBar MB = new JMenuBar();
	JMenu file = new JMenu("File");
	JMenuItem logOut = new JMenuItem("Log out");
	JMenuItem exit = new JMenuItem("Exit");
	JButton save = new JButton("Save");
	JButton cancel = new JButton("Cancel");
	JLabel lblMorningSkip = new JLabel("Morning Skip Monitor");
	JLabel lblDate = new JLabel("Date: ");
	JTextField txtDate = new JTextField(10);
	Color c = new Color(6,69,1);
	Font f = new Font("Comic Sans MS", Font.BOLD, 26);
	Object records[][] = new Object[38][2];
	String[] colNames = {"Form Class", "Points"};
	JTable table = new JTable(records(38), colNames);
	JScrollPane scroll = new JScrollPane(table);
	public static final String DATE_FORMAT_NOW = "yyyy-MM-dd HH:mm:ss";
	
//-----------------------------------------------------------------
//Constructor for the Morning Skip Monitor Screen that places components on the Frame
	public morningSkipMonitor(String str)
	{
		super(str);
		
		FR.setJMenuBar(MB);
		MB.add(file);
		file.add(logOut);
		file.add(exit);
				
		getContentPane().setLayout(GBL);
		FR.add(Obj1);
		
		GBC.fill = GridBagConstraints.BOTH;
		GBC.anchor = GridBagConstraints.CENTER;
		GBC.gridwidth = 2;
		GBC.gridy = 1;
		GBC.gridx = 1;
		GBC.insets = new Insets(10,10,10,10);
		GBL.setConstraints(lblMorningSkip,GBC);
		lblMorningSkip.setFont(f);
		lblMorningSkip.setHorizontalAlignment(JLabel.CENTER);
		lblMorningSkip.setForeground(Color.white);
		getContentPane().add(lblMorningSkip);
		
		GBC.gridy = 2;
		GBC.gridwidth = 1;
		GBL.setConstraints(lblDate,GBC);
		lblDate.setForeground(Color.white);
		lblDate.setLabelFor(txtDate);
		getContentPane().add(lblDate);
		
		GBC.gridx = 2;
		GBL.setConstraints(txtDate,GBC);
		getContentPane().add(txtDate);
		txtDate.setText(systemDateSet());
		
		GBC.gridy = 4;
		GBC.gridx = 1;
		GBC.gridwidth = 2;
		GBL.setConstraints(scroll,GBC);
		getContentPane().add(scroll);
		
		GBC.gridy = 10;
		GBC.ipady = 20;
		GBC.ipadx = 100;
		GBL.setConstraints(save,GBC);
		getContentPane().add(save);
		
		GBC.gridy = 12;
		GBL.setConstraints(cancel,GBC);
		getContentPane().add(cancel);
		
		getContentPane().setBackground(c);
		FR.setExtendedState(Frame.MAXIMIZED_BOTH);
		
		save.addActionListener(this);
		exit.addActionListener(this);
		logOut.addActionListener(this);
		cancel.addActionListener(this);
		validate();
	}
//-----------------------------------------------------------------	
//This method creates a 2D array with one column blank and the second column filled
//with ones as the default points value and the blank column available for input. This
//is used for JTable initialisation. The first parameter is the number of rows in the table
	protected String[][] records(int length)
	{
		String records[][] = new String[length][2];
		
		for(int i = 0; i < length; i++)
		{
			records[i][1] = "1";
			records[i][0] = "";
		}
		return records;
	}
//-----------------------------------------------------------------	
//This method finds the system date and returns the string in the format "DD/MM/YYYY"
	protected static String systemDateSet()
	{
    	Calendar cal = Calendar.getInstance();	//Access calendar object from utility library
    	SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_NOW);	//Creates an object of the format
    	String a = sdf.format(cal.getTime());		//Retrieves time
    	return (a.substring(8,10) + "/" + a.substring(5,7) + "/" + a.substring(0,4));	//Returns date part of the string
	}
//-----------------------------------------------------------------		
//This method is used to reference the location of each form class. This part of the
//system is now automated.
	private String[][] referenceFormLocation(String[][] tableData, int rows)
	{
		try
		{
			String problemReferences = "";			//problemReferences is used to store records that could not be referenced
			File file = new File("FormClassroomLocation.txt");			//Creates an object of the File
			RandomAccessFile RAF = new RandomAccessFile(file, "r");		//Creates an object of the Random Access File
			int recordSize = 13;					//The size of each record in the random access file is 13 characters
			
			for(int c = 0; c < rows; c++)				//First loop iterates each array index
			{
				boolean found = false;						//Sets flag to false. This variable is used to identify problem input and reject them
				int length = tableData[c][0].length();		//Finds length of the string stored in the table current index
				
				int  j = 0;								//Loop counter
				while(j < tableData[c][0].length())		//Analyses string until
				{
					if(tableData[c][0].substring(j, j+1).equals("("))		//Checks if the user has input form classes in full form; 7A(MC) opposed to just 7A
					{
						tableData[c][0] = tableData[c][0].substring(0, j);	//Removes the latter part if full form has been used
						break;				//Terminate loop
					}
					j++;			//Positive iteration to counter 
				}
				RAF.seek(0);							//Goes to the beginning of the file
								
				for(int i = 0; i < 38; i++)				//Second loop iterates the record number being searched in the file
				{
					if(tableData[c][0] == null)				//Tests if the index in the array storing the table data is blank
					{
						break;								//Terminates the loop if the table is blank
					}
					
					String currentLine = "", sCurrentLine = "";		//currentLine will store each record on each loop and sCurrentLine will look at certain fields of each record
					RAF.seek(i * recordSize);				//File pointer goes to the beginning of the each record on every loop
					
					for(int ct = 0; ct < recordSize; ct++)		//Reads each character of a record one by one
					{
						byte b = RAF.readByte();			//Read one character
						currentLine += (char) b;			//Convert to character from byte
					}
					
					sCurrentLine = currentLine.substring(0,3);		//Reads first part of record
					
					if((sCurrentLine.substring(2,3)).equals("("))	//Tests if the first field is meant to be two characters long; 7A opposed to 13A
					{
						sCurrentLine = currentLine.substring(0,2);		//Takes the first two characters of the record
					}

					if(tableData[c][0].equalsIgnoreCase(sCurrentLine))	//Checks if the reference in the table and file match
					{
						tableData[c][0] = (currentLine.substring(8,13)).trim();		//Assigns reference file data to table array
						found = true;	//Activates found flag
						break;			//Terminates loop when the record and table data is matched
					}
				}
				
				if(!found)
				{
					problemReferences += tableData[c][0] + " ";		//Adds any input problems
					tableData[c][0] = ""; 		//Clears problem index field
				}
			}
			RAF.close();			//Closes the Random Access File
			
			if(!problemReferences.equals(""))		//Tests if there were no problems
			{
				JOptionPane.showMessageDialog(this, "The following classrooms could not be processed because they do not exist.\n" + problemReferences,"Error Message", JOptionPane.WARNING_MESSAGE);	//Outputs problem input
			}
			return tableData;		//Returns array
		}
		catch(FileNotFoundException e)
		{
			Toolkit.getDefaultToolkit().beep();		//Makes error sound
			JOptionPane.showMessageDialog(this, "The FormClassroomLocation.txt notepad file is missing from the current directory. This process cannot function without this file.\nError Code: " + e,"File is Missing!", JOptionPane.ERROR_MESSAGE);	//Output any error if a file is not found
		}
		catch(Exception e)
		{
			Toolkit.getDefaultToolkit().beep();
			JOptionPane.showMessageDialog(this, "An error has occured. Please contact Harris Rasheed to deal with this issue\nError Code: " + e,"Error Message", JOptionPane.ERROR_MESSAGE);	//Output any errors caught
		}
		return null;
	}
//-----------------------------------------------------------------
//This method is used to store recycling statistics information. It updates existing
//data in the random access file. The first parameter is the array with records to 
//be stored and the second parameter is the number of rows in the 2D array. This
//method finds the desired record to be updated, processes it and the moves it to
//the end of the file which allows the next search to be executed faster.
	public void storeRecyStats(String[][] tableData, int rows)
	{
		try
		{
			File file = new File("RecyclingActivityStats.txt");			//Creates object of file
			RandomAccessFile RAF = new RandomAccessFile(file, "rw");	//Creates object of Random Access File

			int recordSize = 8;						//The size of each record in the random access file is 8 characters
			int records = (int)(RAF.length())/recordSize;	//Calculates the number of records in the random access file
			String errorStorage = "| - ";					//Stores erroneous input data
			boolean found = false;							//Creates flag
			
			for(int c = 0; c < rows; c++)
			{
				for(int i = 0; i < records; i++)
				{
					String line = "";
					RAF.seek(i * recordSize);
					
					for(int ct = 0; ct < recordSize; ct++)
					{
						byte b = RAF.readByte();
						line += (char)b;
					}
					
					String roomNo = (line.substring(0,5)).trim();
					int points = Integer.parseInt((line.substring(5,8)).trim());
					
					if(roomNo.equalsIgnoreCase(tableData[c][0]))		//Checks if the String is equal to the delete parameter
					{
						RAF.seek((records - 1) * (recordSize));			//File pointer looks at the last record
						byte[] ba = new byte[recordSize];				//Creates array with the size of one record
						RAF.readFully(ba);								//Reads entire line and places it in the array
						RAF.seek(i * recordSize);						//File pointer looks at the place where the record was found
						RAF.write(ba);									//Overwrites the record location with the last record since it is to be deleted
						RAF.setLength(((records - 1)* (recordSize)));	//Truncates file and removes the last record from the end of the file which has been moved to the deleted record's space
						RAF.seek(RAF.length());							//File pointer looks at the end of the file							
						
						for(int a = roomNo.length(); a < 5; a++)		//Adds spacing to the room number field so that the record size is always of a set length
						{
							roomNo = roomNo + " ";
						}
						
						points += Integer.parseInt(tableData[c][1]);	//Adds points to points field in the record
						String StrPoints = Integer.toString(points);	//Converts points number to string
						
						for(int a = StrPoints.length(); a < 3; a++)		//Adds spacing to the points field so that the record size is always of a set length
						{
							StrPoints = StrPoints + " ";
						}
						RAF.writeBytes(roomNo + StrPoints);				//Writes the room number and points fields together as a record to the file		
						found = true;									//Activates flag
						break;											//Terminates loop
					}
				}
				if(!found && tableData[c][0] != null && tableData[c][0].trim() != "")	//Tests if the flag is still false and if the field is not blank
				{
					errorStorage += tableData[c][0] + " - ";		//Records erroneous input data
				}
			}
			
			if(errorStorage.length() > 4)		//Tests if any erroneous data was input. The variable starts of with 4 characters
			{
				JOptionPane.showMessageDialog(this, "The following classrooms could not be processed because they do not exist.\n" + errorStorage + "|","Error Message", JOptionPane.WARNING_MESSAGE);	//Output any errors caught
			}
			RAF.close();							//Closes Random Access File
		}
		catch(FileNotFoundException e)
		{
			Toolkit.getDefaultToolkit().beep();
			JOptionPane.showMessageDialog(this, "The RecyclingActivityStats.txt notepad file is missing from the current directory. This process cannot function without this file.\nError Code: " + e,"File is Missing!", JOptionPane.ERROR_MESSAGE);	//Output error if a file is not found
		}
		catch(Exception e)
		{
			Toolkit.getDefaultToolkit().beep();
			JOptionPane.showMessageDialog(this, "An error has occured. Please contact Harris Rasheed to deal with this issue\nError Code: " + e,"Error Message", JOptionPane.ERROR_MESSAGE);	//Output any errors caught
		}
	}
//-----------------------------------------------------------------
//This method keeps a log of all recycling statistics that are processed
//The first parameter is the array of data that will be processed, the
//second parameter is the number of rows in the table and the third
//parameter stores the date that was input by the user
	public void logRecyStats(String[][] tableData, int rows, String date)
	{
		try
		{
			File file = new File("RecyclingActivityLog.txt");
			RandomAccessFile RAF = new RandomAccessFile(file, "rw");
	
			int recordSize = 17;						//The set record size is 17 characters in this RAF
			int records = (int)(RAF.length())/recordSize;	//Calculates the number of records in the RAF
			
			RAF.seek(file.length());			//File pointer looks at the end of the file
			
			for(int c = 0; c < rows; c++)
			{	
				if(tableData[c][0] != "")			//Tests if the current table row is blank
				{
					String roomNo = tableData[c][0];		//Assigns current index room number
					String points = tableData[c][1];		//Assigns current index points
					
					for(int i = roomNo.length(); i < 5; i++)	//Adds spacing to make the room number field have set length
					{
						roomNo += " ";
					}
	
					for(int i = points.length(); i < 2; i++)	//Adds spacing to make the points field have set length
					{
						points += " ";
					}				
					RAF.writeBytes(roomNo + points + date);		//Writes the room number, points and date fields together as a record to the file
				}
			}
			RAF.close();			//Closes Random Access File
		}
		catch(FileNotFoundException e)
		{
			Toolkit.getDefaultToolkit().beep();
			JOptionPane.showMessageDialog(this, "The RecyclingActivityLog.txt notepad file is missing from the current directory. This process cannot function without this file.\nError Code: " + e,"File is Missing!", JOptionPane.ERROR_MESSAGE);	//Output any error if a file is not found
		}
		catch(Exception e)
		{
			Toolkit.getDefaultToolkit().beep();
			JOptionPane.showMessageDialog(this, "An error has occured. Please contact Harris Rasheed to deal with this issue\nError Code: " + e,"Error Message", JOptionPane.ERROR_MESSAGE);	//Output any errors caught
		}
	}
//-----------------------------------------------------------------	
//This method is used to execute the appropriate method when the user performs an action event
	public void actionPerformed(ActionEvent ae)
	{
		if(ae.getSource()==save)
		{
			int rows = table.getRowCount();							//Returns number of rows in the table
			int columns = table.getColumnCount();					//Returns number of columns in the table
			String[][] tableData = new String[rows][columns];		//Creates array to store JTable's data
			String date = txtDate.getText();						//Retrieves date from text field
			
			for(int i = 0; i < rows; i++)
			{
				for(int j = 0; j < columns; j++)
				{
					tableData[i][j] = (String) table.getValueAt(i,j);	//Loop which reads all the elements of JTable and stores it in a 2D array
				}
			}
			
			int count = 0;				//Used to count the number of blank rows in the table
			for(int i = 0; i < rows; i++)
			{
				if(tableData[i][0].equals(""))		//Tests if the field is blank
				{
					count++;
				}
			}
			if(count == rows)		//Checks if the entire table is blank
			{
				JOptionPane.showMessageDialog(this, "The table is blank. Please input values to be processed.","Error Message", JOptionPane.ERROR_MESSAGE);	//Output any errors caught
				return;
			}
			referenceFormLocation(tableData, rows);		//Executes code and references form classes with their room numbers
			storeRecyStats(tableData, rows);			//Processes the table and stores it in a random access file
			logRecyStats(tableData, rows, date);		//Keeps a log of statistics that were input and the date
			
			int ch = JOptionPane.showConfirmDialog(this, "Save Successful!\nWould you like to return to the Thursday Recycling Quota screen?", "Save Successful", JOptionPane.YES_NO_OPTION);
			if(ch  == JOptionPane.YES_OPTION)		//Tests if the user has pressed Yes
			{
				FR.setVisible(false);
				thursdayRecyclingQuota TRQ = new thursdayRecyclingQuota("");
				TRQ.FR.setVisible(true);
			}
		}
		if(ae.getSource()==cancel)
		{
			FR.setVisible(false);
			thursdayRecyclingQuota TRQ = new thursdayRecyclingQuota("");
			TRQ.FR.setVisible(true);
		}
		if(ae.getSource()==logOut)
		{
			FR.setVisible(false);
			Program EP = new Program("");
			EP.FR.setVisible(true);
		}
		if(ae.getSource()==exit)
		{
			System.exit(0);
		}
	}
}
//----------------------------------------------------------------------------------------------------------------------
//Lunch Collection Rounds Screen; This is the screen where the user can input data
//from the Thursday Recycling Rota slips used by volunteer recyclers
class lunchCollRounds extends JFrame implements ActionListener
{
	JFrame FR = new JFrame("Recycling Activity Monitoring System - Lunch Collection Rounds");
	Container Obj1 = getContentPane();
	GridBagLayout GBL = new GridBagLayout();
	GridBagConstraints GBC = new GridBagConstraints();
	JMenuBar MB = new JMenuBar();
	JMenu file = new JMenu("File");
	JMenuItem logOut = new JMenuItem("Log out");
	JMenuItem exit = new JMenuItem("Exit");
	JButton save = new JButton("Save");
	JButton btnRecyRegist = new JButton("Recycler Registration");
	JButton cancel = new JButton("Cancel");
	JLabel lblLunchColl = new JLabel("Lunch Collection Rounds");
	JLabel lblDate = new JLabel("Date: ");
	JTextField txtDate = new JTextField(10);
	Color c = new Color(6,69,1);
	Font f = new Font("Comic Sans MS", Font.BOLD, 26);
	String[] colNames = {"Room Number", "Points"};
	morningSkipMonitor MSM = new morningSkipMonitor("");
	JTable table = new JTable(MSM.records(120), colNames);
	JScrollPane scroll = new JScrollPane(table);
	
//-----------------------------------------------------------------
//Constructor for the Lunch Collection Rounds Screen that places components on the Frame
	public lunchCollRounds(String str)
	{
		super(str);
		
		FR.setJMenuBar(MB);
		MB.add(file);
		file.add(logOut);
		file.add(exit);
				
		getContentPane().setLayout(GBL);
		FR.add(Obj1);
		
		GBC.fill = GridBagConstraints.BOTH;
		GBC.anchor = GridBagConstraints.CENTER;
		GBC.gridwidth = 2;
		GBC.gridy = 1;
		GBC.gridx = 1;
		GBC.insets = new Insets(10,10,10,10);
		GBL.setConstraints(lblLunchColl,GBC);
		lblLunchColl.setFont(f);
		lblLunchColl.setHorizontalAlignment(JLabel.CENTER);
		lblLunchColl.setForeground(Color.white);
		getContentPane().add(lblLunchColl);
		
		GBC.gridy = 2;
		GBC.gridwidth = 1;
		GBL.setConstraints(lblDate,GBC);
		lblDate.setForeground(Color.white);
		lblDate.setLabelFor(txtDate);
		getContentPane().add(lblDate);
		
		GBC.gridx = 2;
		GBL.setConstraints(txtDate,GBC);
		getContentPane().add(txtDate);
		morningSkipMonitor MSM = new morningSkipMonitor("");
		txtDate.setText(MSM.systemDateSet());
		
		GBC.gridy = 3;
		GBC.gridx = 1;
		GBC.gridwidth = 2;
		GBL.setConstraints(scroll,GBC);
		getContentPane().add(scroll);
		
		GBC.gridheight = 1;
		GBC.gridy = 12;
		GBC.ipady = 10;
		GBC.ipadx = 100;
		GBL.setConstraints(save,GBC);
		getContentPane().add(save);
		
		GBC.gridy = 14;
		GBL.setConstraints(cancel,GBC);
		getContentPane().add(cancel);
		
		GBC.gridy = 16;
		GBL.setConstraints(btnRecyRegist, GBC);
		getContentPane().add(btnRecyRegist);
		
		getContentPane().setBackground(c);
		FR.setExtendedState(Frame.MAXIMIZED_BOTH);
		
		save.addActionListener(this);
		exit.addActionListener(this);
		logOut.addActionListener(this);
		cancel.addActionListener(this);
		btnRecyRegist.addActionListener(this);
		validate();
	}
//-----------------------------------------------------------------	
//This method is used to read all the recycler names from the random access file
//and returns them in an array so that they can be displayed to the user in a
//input dialog choice box. This is used for recycler attendance and deletion.
	public String[] addRecyclers()
	{
		try
		{
			RandomAccessFile RAF = new RandomAccessFile("RecyclerAttendanceStats.txt", "r");
			int recordSize = 40;
			int records = (int) RAF.length()/(recordSize);					//Calculates the number of records in the RAF
			String[] recyclerNames = new String[records + 1];				//Creates an array for the recycler names
			recyclerNames[0] = "<Recycler Name>";							//The extra index in the array is filled up by this default String
			
			for(int i = 1; i < records + 1; i++)	//Loops until all the random access file records have been read
			{
				String currentLine = "";			//Initialises variable that will store records on every loop
				RAF.seek((i-1) * recordSize);		//File pointer looks at the beginning of each record
					
				for(int ct = 0; ct < recordSize; ct++)		//The loop reads every record one by one 
				{
					byte b = RAF.readByte();
					currentLine += (char)b;
				}
				
				currentLine = currentLine.substring(0,30);		//Reads first field of record
				currentLine = currentLine.trim();				//Removes extra spacing from field
				recyclerNames[i] = currentLine;					//Assigns field to the array
			}
			RAF.close();
			return recyclerNames;			//Returns the array containing recycler names
		}
		catch(FileNotFoundException e)
		{
			Toolkit.getDefaultToolkit().beep();
			JOptionPane.showMessageDialog(this, "The RecyclerAttendanceStats.txt notepad file is missing from the current directory. This process cannot function without this file.\nError Code: " + e,"File is Missing!", JOptionPane.ERROR_MESSAGE);	//Output any error if a file is not found
		}
		catch(Exception e)
		{
			Toolkit.getDefaultToolkit().beep();
			JOptionPane.showMessageDialog(this, "An error has occured. Please contact Harris Rasheed to deal with this issue\nError Code: " + e,"Error Message", JOptionPane.ERROR_MESSAGE);	//Output any errors caught
		}
		return null;
	}
//-----------------------------------------------------------------	
//This method is used to process recycler attendance. When their names are selected by the
//user, their attendance statistics are updated in the random access file so that the tally
//report can be calculated. The first parameter is an array that contains the names of the
//recyclers who attended the rounds and the second parameter is the number of recyclers who attended
	private void processRecyclerAttendance(String[] rNames, int numRecyclers)
	{
		try
		{
			File file = new File("RecyclerAttendanceStats.txt");
			RandomAccessFile RAF = new RandomAccessFile(file, "rw");

			int recordSize = 40;
			int records = (int) RAF.length()/(recordSize);
			
			for(int c = 0; c < numRecyclers; c++)		//Loops until all recyclers' attendance are processed
			{	
				RAF.seek(0);			//File pointer looks at the beginnning of the file
				
				for(int i = 0; i < records; i++)	//Loops until all records are read
				{
					if(rNames[c].equalsIgnoreCase("<Recycler Name>"))	//Tests if the user has tried to pass the default value
					{
						break;					//Terminates current loop if so
					}
					
					String line = "";				//Initialises variable that stores records
					RAF.seek(i * recordSize);		//File pointer looks at the beginning of the file
					
					for(int ct = 0; ct < recordSize; ct++)		//Loop reads each random access file record
					{
						byte b = RAF.readByte();
						line += (char)b;
					}

					String recyName = (line.substring(0,30)).trim();						//Retrieves first field of the record
					int attendance = Integer.parseInt((line.substring(38,40)).trim());		//Retrieves current attendance statistics
					
					if(rNames[c].equalsIgnoreCase(recyName))			//Checks if the record's field is the same as the array's data
					{
						attendance++;										//Adds to the current attendance tally
						RAF.seek((i * (recordSize)) + 38);					//Goes the point field of the record
						String strAttend = Integer.toString(attendance);	//Converts attendance tally to a string so it can be written to an RAF	
						
						for(int d = strAttend.length(); d < 2; d++)		//Loops until the string is of a set size
						{
							strAttend += " ";
						}
						RAF.writeBytes(strAttend);		//Updates the record and overwrites the old attendance tally
						break;
					}
				}
			}
			RAF.close();
		}
		catch(FileNotFoundException e)
		{
			Toolkit.getDefaultToolkit().beep();
			JOptionPane.showMessageDialog(this, "The RecyclerAttendanceStats.txt notepad file is missing from the current directory. This process cannot function without this file.\nError Code: " + e,"File is Missing!", JOptionPane.ERROR_MESSAGE);	//Output any error if a file is not found
		}
		catch(Exception e)
		{
			Toolkit.getDefaultToolkit().beep();
			JOptionPane.showMessageDialog(this, "An error has occured. Please contact Harris Rasheed to deal with this issue\nError Code: " + e,"Error Message", JOptionPane.ERROR_MESSAGE);	//Output any errors caught
		}
	}
//-----------------------------------------------------------------		
//This method is used to keep a log of recycler attendance. This is stored in a random access file
//The first parameter is the names of the recyclers who attended, the second parameter is the
//number of recyclers who attended and the third parameter is the date of their attendace. This method
//is used for the Recycler of the Month Candidate Report
	private void logRecyclerAttendance(String[] rNames, int totalRecyclerNames, String date)
	{
		try
		{
			File file = new File("RecyclerAttendanceLog.txt");
			RandomAccessFile RAF = new RandomAccessFile(file, "rw");

			int recordSize = 40;
			int records = (int) RAF.length()/(recordSize);
			
			for(int c = 0; c < totalRecyclerNames; c++)
			{	
				RAF.seek(file.length());		//File pointer looks at the end of the file
				
				for(int i = 0; i < records; i++)
				{
					if(rNames[c].equalsIgnoreCase("<Recycler Name>"))	//Checks if the default value has been passed
					{
						break;		//Terminates loop if so
					}
					
					for(int j = rNames[c].length(); j < 30; j++)	//Sets length of Recycler names to record's field size
					{
						rNames[c] += " ";
					}
					RAF.writeBytes(rNames[c]+date);		//Writes name and date to the random access file
				}	
			}
			RAF.close();
		}
		catch(FileNotFoundException e)
		{
			Toolkit.getDefaultToolkit().beep();
			JOptionPane.showMessageDialog(this, "The RecyclerAttendanceLog.txt notepad file is missing from the current directory. This process cannot function without this file.\nError Code: " + e,"File is Missing!", JOptionPane.ERROR_MESSAGE);	//Output any error if a file is not found
		}
		catch(Exception e)
		{
			Toolkit.getDefaultToolkit().beep();
			JOptionPane.showMessageDialog(this, "An error has occured. Please contact Harris Rasheed to deal with this issue\nError Code: " + e,"Error Message", JOptionPane.ERROR_MESSAGE);	//Output any errors caught
		}
	}
//-----------------------------------------------------------------	
//This method is used to execute the appropriate method when the user performs an action event
	public void actionPerformed(ActionEvent ae)
	{
		if(ae.getSource()==save)
		{
			int rows = table.getRowCount();
			int columns = table.getColumnCount();
			String date = txtDate.getText();
			String[][] tableData = new String[rows][columns];
			
			for(int i = 0; i < rows; i++)
			{
				for(int j = 0; j < columns; j++)
				{
					tableData[i][j] = (String) table.getValueAt(i,j);	//Loops until all the values of the table have been assigned to a 2D array
				}
			}
			
			int count = 0;				//Used to count the number of blank rows in the table
			for(int i = 0; i < rows; i++)
			{
				if(tableData[i][0].equals(""))		//Tests if the field is blank
				{
					count++;
				}
			}
			if(count == rows)		//Checks if the entire table is blank
			{
				JOptionPane.showMessageDialog(this, "The table is blank. Please input values to be processed.","Error Message", JOptionPane.ERROR_MESSAGE);	//Output any errors caught
				return;
			}
			
			int totalRecyclerNames;
			
			try
			{
				totalRecyclerNames = Integer.parseInt((String)JOptionPane.showInputDialog(null, "How many recyclers participated this week?","Number of Recyclers", 3));		//Prompts the user for the number of recyclers that attended
			}
			catch(Exception e)
			{
				JOptionPane.showMessageDialog(this, "Error! Please input a number.","Error", JOptionPane.ERROR_MESSAGE);
				return;
			}			
			
			morningSkipMonitor MSM = new morningSkipMonitor("");		//Creates object of class in order to access method
			MSM.storeRecyStats(tableData, rows);				//Accesses method of morningSkipMonitor class to store recycling statistics
			Frame frame = new Frame();							//Creates frame for dialog box
			
			String[] recyNames = new String[totalRecyclerNames];		//Creates array to store names of recyclers who attended
			
			for(int i = 0; i < totalRecyclerNames; i++)		//Loops the message box until the appropriate number
			{
				recyNames[i] = (String) JOptionPane.showInputDialog(frame, "Please select a recycler name", "Recycler Attendance " + (i + 1), JOptionPane.PLAIN_MESSAGE, null, addRecyclers(),"<Recycler Name>");
			}
			processRecyclerAttendance(recyNames, totalRecyclerNames);		//Processes recycler attendance
			logRecyclerAttendance(recyNames, totalRecyclerNames, date);		//Logs recycler attendance
		}
		if(ae.getSource()==cancel)
		{
			FR.setVisible(false);
			thursdayRecyclingQuota TRQ = new thursdayRecyclingQuota("");
			TRQ.FR.setVisible(true);
		}
		if(ae.getSource()==btnRecyRegist)
		{
			int ch = JOptionPane.showConfirmDialog(this, "Would you like to go to the Recycler Registration screen without saving your changes?", "Exit without Save Changes?", JOptionPane.YES_NO_OPTION);
			if(ch  == JOptionPane.YES_OPTION)		//Checks if the user pressed Yes
			{
				FR.setVisible(false);
				recyclerRegistration RR = new recyclerRegistration("");
				RR.FR.setVisible(true);
			}
		}
		if(ae.getSource()==logOut)
		{
			FR.setVisible(false);
			Program EP = new Program("");
			EP.FR.setVisible(true);
		}
		if(ae.getSource()==exit)
		{
			System.exit(0);
		}
	}
}
//----------------------------------------------------------------------------------------------------------------------
//Recycler Attendance Report Screen; This is the screen where the user can view
//the recycler attendance report that displays the attendance in tally format.
class recyclerAttendanceReport extends JFrame implements ActionListener
{
	JFrame FR = new JFrame("Recycling Activity Monitoring System - Recycler Attendance Report");
	Container Obj1 = getContentPane();
	GridBagLayout GBL = new GridBagLayout();
	GridBagConstraints GBC = new GridBagConstraints();
	JMenuBar MB = new JMenuBar();
	JMenu file = new JMenu("File");
	JMenuItem logOut = new JMenuItem("Log out");
	JMenuItem exit = new JMenuItem("Exit");
	JButton print = new JButton("Print");
	JButton cancel = new JButton("Cancel");
	JLabel lblRecyAttendRep = new JLabel("Recycler Attendance Report");
	Color c = new Color(6,69,1);
	Font f = new Font("Comic Sans MS", Font.BOLD, 26);
	String[] colNames = {"Recycler", "Form Class", "Attendance Tally"};
	File raf = new File("RecyclerAttendanceStats.txt");
	JTable table = new JTable(loadTable(raf, 40, 30, 38), colNames);
	JScrollPane scroll = new JScrollPane(table);		//Allow a large table to be viewed using a scroll pane
	
//-----------------------------------------------------------------
//Constructor for the Recycler Attendance Report Screen that places components on the Frame
	public recyclerAttendanceReport(String str)
	{
		super(str);
				
		FR.setJMenuBar(MB);
		MB.add(file);
		file.add(logOut);
		file.add(exit);
				
		getContentPane().setLayout(GBL);
		FR.add(Obj1);
		
		GBC.fill = GridBagConstraints.BOTH;
		GBC.anchor = GridBagConstraints.CENTER;
		GBC.gridwidth = 2;
		GBC.gridy = 1;
		GBC.gridx = 1;
		GBC.insets = new Insets(10,10,10,10);
		GBL.setConstraints(lblRecyAttendRep,GBC);
		lblRecyAttendRep.setFont(f);
		lblRecyAttendRep.setHorizontalAlignment(JLabel.CENTER);
		lblRecyAttendRep.setForeground(Color.white);
		getContentPane().add(lblRecyAttendRep);
				
		GBC.gridy = 4;
		GBC.gridx = 1;
		GBC.gridwidth = 2;
		GBL.setConstraints(scroll,GBC);
		getContentPane().add(scroll);
		
		GBC.gridy = 10;
		GBC.ipady = 20;
		GBC.ipadx = 100;
		GBL.setConstraints(print,GBC);
		getContentPane().add(print);
		
		GBC.gridy = 12;
		GBL.setConstraints(cancel,GBC);
		getContentPane().add(cancel);
		
		getContentPane().setBackground(c);
		FR.setExtendedState(Frame.MAXIMIZED_BOTH);
		
		print.addActionListener(this);
		exit.addActionListener(this);
		logOut.addActionListener(this);
		cancel.addActionListener(this);
		validate();
	}
//-----------------------------------------------------------------	
//This method is used to load the table when the Recycler Attendance Report is accessed
//It reads the random access file for data which is output to JTable
	protected String[][] loadTable(File file, int recordSize, int firstField, int secondField)
	{
		try
		{
			RandomAccessFile RAF = new RandomAccessFile(file, "r");		//Creates an object of the Random Access File
			int records = (int) RAF.length()/recordSize;				//Calculates the number of records exist in the file
			String[][] tableData = new String[records][3]; 
			
			for(int c = 0; c < records; c++)				//First loop iterates each array index
			{
				RAF.seek(c * recordSize);			//File pointer goes to the start of each record on every iteration
				String currentLine = "";			//currentLine is used to store each record being searched
				for(int i = 0; i < recordSize; i++)
				{
					byte b = RAF.readByte();		//Reads each character and stores it as a byte
					currentLine += (char) b;		//Converts each character from byte to character and then to string and collects each character on each loop
				}
				
				String first = (currentLine.substring(0,firstField)).trim();			//Reads first field
				String second = (currentLine.substring(firstField,secondField)).trim();	//Reads second field
				String third = (currentLine.substring(secondField,recordSize)).trim();	//Reads third field

				tableData[c][0] = first;				//Assigns first field to 2D array
				tableData[c][1] = second;				//Assigns second field to 2D array
				tableData[c][2] = third;				//Assigns third field to 2D array
			}
			RAF.close();
			quickSort(tableData, 0, records-1, records);		//Performs a quick sort on the array
			return tableData;						//returns 2D array of Random Access file records
		}
		catch(FileNotFoundException e)
		{
			Toolkit.getDefaultToolkit().beep();
			JOptionPane.showMessageDialog(this, "An important system file is missing from the current directory. This process cannot function without this file.\nError Code: " + e,"File is Missing!", JOptionPane.ERROR_MESSAGE);	//Output any error if a file is not found
		}
		catch(Exception e)
		{
			Toolkit.getDefaultToolkit().beep();
			JOptionPane.showMessageDialog(this, "An error has occured. Please contact Harris Rasheed to deal with this issue\nError Code: " + e,"Error Message", JOptionPane.ERROR_MESSAGE);	//Output any errors caught
		}
		return null;
	}
//-----------------------------------------------------------------
//This method uses quick sort to sort the array parameter in descending order according to the third column.
//The first parameter is the array that needs to be sorted, the second parameter is the start of the unsorted
//part of the array's index, the third parameter is the end of the unsorted array's index and the fourth parameter
//is the total number of records being sorted which needs to be monitored in order to know when the sort is completed.
	protected String[][] quickSort(String[][] tableData, int start, int finish, int records)
	{
		int left, right, count = 0;		//left is the most left unsorted index and right is the most right unsorted index, count is used to tell when the sort is completed
		String temp1, temp2, temp3;		//temporary variables used to store the array data when it is being swapped
		left = start;
		right = finish;
		int pos = (left + right)/2;		//the middle position is obtained
		int pivot = Integer.parseInt(tableData[pos][2]);	//this is the middle position of the unsorted part of the array

		while(right > left)		//While both half of the arrays are being sorted. This condition becomes nullified when the right and left variables cross over to the other half of the array		
		{
			while(Integer.parseInt(tableData[left][2]) > pivot)		//Executes loop until it finds an element that is smaller than the pivot in the first half of the array
			{
				left++;
				count++;		//This variable is used to count the number of sorted items
			}

			while(pivot > Integer.parseInt(tableData[right][2]))	//Executes loop until it finds an element that is greater than the pivot in the second half of the array
			{
				right--;
				count++;		//This variable is used to count the number of sorted items
			}

			if(count==records)		//Tests if the entire array has been sorted
			{
				return tableData;	//Returns sorted array
			}
				
			if(left <= right)		//Checks if the left position is smaller than the right position
			{
				temp1 = tableData[left][0];			//Copies Column 1 contents of the array to a temporary variable
				temp2 = tableData[left][1];			//Copies Column 2 contents of the array to a temporary variable
				temp3 = tableData[left][2];			//Copies Column 3 contents of the array to a temporary variable
				tableData[left][0] = tableData[right][0];		//Overwrites the first half of the array index that was found unsorted with the larger element
				tableData[left][1] = tableData[right][1];
				tableData[left][2] = tableData[right][2];
				tableData[right][0] = temp1;			//Overwrites the second half of the array index that was found unsorted with the smaller element
				tableData[right][1] = temp2;
				tableData[right][2] = temp3;
				left++;			//Reduces the size of the array for the next sort sequence
				right--;		//Reduces the size of the array for the next sort sequence
			}
			
			if(start < right)			//Tests if the right boundary of the unsorted array has not reach the start of the sorted array
			{
				quickSort(tableData, start, right, records);	//Recursion of sorting sequence with smaller array parameters
			}
			
			if(left < finish)			//Tests if the left boundary of the unsorted array has not reach the end of the sorted array
			{
				quickSort(tableData, left, finish, records);	//Recursion of sorting sequence with smaller array parameters
			}
		}
		return null;		//Returns null if the sort fails because of file reading problems which would pass an erroneous number of records
	}
//-----------------------------------------------------------------
//This method is used to execute the appropriate method when the user performs an action event
	public void actionPerformed(ActionEvent ae)
	{
		if(ae.getSource()==print)		//Checks if the print button has been pressed
		{
			try
			{
				if (!table.print())		//Checks if the user has cancelled the print job and initiates the print method
				{
					JOptionPane.showMessageDialog(this, "The user has cancelled the print job.", "Cancelled Print Job", JOptionPane.INFORMATION_MESSAGE);
    			}
			}
			catch (java.awt.print.PrinterException e)			//Catches printer exception
			{
				JOptionPane.showMessageDialog(this, "Unable to print due to " + e, "Print Job Error", JOptionPane.ERROR_MESSAGE);
				Toolkit.getDefaultToolkit().beep();
			}
		}
		if(ae.getSource()==cancel)
		{
			FR.setVisible(false);
			menuPage MP = new menuPage("");
			MP.FR.setVisible(true);
		}
		if(ae.getSource()==logOut)
		{
			FR.setVisible(false);
			Program EP = new Program("");
			EP.FR.setVisible(true);
			Toolkit.getDefaultToolkit().beep();		
		}
		if(ae.getSource()==exit)
		{
			System.exit(0);
		}
	}
}
//----------------------------------------------------------------------------------------------------------------------
//Recycling Activity Report Screen; This is the screen where the user can view the recycling
//activity statistics of each classroom in school over the current academic year.
class recyclingActivityReport extends JFrame implements ActionListener
{
	JFrame FR = new JFrame("Recycling Activity Monitoring System - Recycling Activity Report");
	Container Obj1 = getContentPane();
	GridBagLayout GBL = new GridBagLayout();
	GridBagConstraints GBC = new GridBagConstraints();
	JMenuBar MB = new JMenuBar();
	JMenu file = new JMenu("File");
	JMenuItem logOut = new JMenuItem("Log out");
	JMenuItem exit = new JMenuItem("Exit");
	JButton print = new JButton("Print");
	JButton cancel = new JButton("Cancel");
	JLabel lblRecyActivityRep = new JLabel("Recycling Activity Report");
	Color c = new Color(6,69,1);
	Font f = new Font("Comic Sans MS", Font.BOLD, 26);
	String[] colNames = {"Room Number", "Teacher", "Points"};
	JTable table = new JTable(loadTable(), colNames);
	JScrollPane scroll = new JScrollPane(table);
	
//-----------------------------------------------------------------
//Constructor for the Recycling Activity Report Screen that places components on the Frame	
	public recyclingActivityReport(String str)
	{
		super(str);
				
		FR.setJMenuBar(MB);
		MB.add(file);
		file.add(logOut);
		file.add(exit);
				
		getContentPane().setLayout(GBL);
		FR.add(Obj1);
		
		GBC.fill = GridBagConstraints.BOTH;
		GBC.anchor = GridBagConstraints.CENTER;
		GBC.gridwidth = 2;
		GBC.gridy = 1;
		GBC.gridx = 1;
		GBC.insets = new Insets(10,10,10,10);
		GBL.setConstraints(lblRecyActivityRep,GBC);
		lblRecyActivityRep.setFont(f);
		lblRecyActivityRep.setHorizontalAlignment(JLabel.CENTER);
		lblRecyActivityRep.setForeground(Color.white);
		getContentPane().add(lblRecyActivityRep);
				
		GBC.gridy = 4;
		GBC.gridx = 1;
		GBC.gridwidth = 2;
		GBL.setConstraints(scroll,GBC);
		getContentPane().add(scroll);
		
		GBC.gridy = 10;
		GBC.ipady = 20;
		GBC.ipadx = 100;
		GBL.setConstraints(print,GBC);
		getContentPane().add(print);
		
		GBC.gridy = 12;
		GBL.setConstraints(cancel,GBC);
		getContentPane().add(cancel);
		
		getContentPane().setBackground(c);
		FR.setExtendedState(Frame.MAXIMIZED_BOTH);
		
		print.addActionListener(this);
		exit.addActionListener(this);
		logOut.addActionListener(this);
		cancel.addActionListener(this);
		validate();
	}
//-----------------------------------------------------------------	
//This method is used to load the table when the Recycling Activity Report is accessed
//It reads the random access file for data which is output to the JTable
	private String[][] loadTable()
	{
		try
		{
			String[][] tableData = new String[122][3];
			int recordSize = 8;							//Each record in the RAF is 8 characters long
			RandomAccessFile raf = new RandomAccessFile("RecyclingActivityStats.txt", "r");
			int records = (int) raf.length()/recordSize;
			
			for(int c = 0; c < records; c++)		//First loop iterates each array index
			{
				raf.seek(c * recordSize);			//File pointer goes to the start of each record on evert iteration
				String currentLine = "";			//currentLine is used to store each record being searched
				for(int i = 0; i < recordSize; i++)
				{
					byte b = raf.readByte();		//Reads each character and stores it as a byte
					currentLine += (char) b;		//Converts each character from byte to character and then to string and collects each character on each loop
				}
				
				String roomNo = (currentLine.substring(0,5)).trim();		//Takes a substring of the current record to get the room number part
				String points = (currentLine.substring(5,8)).trim();		//Takes a substring of the current record to get the points segment
				
				tableData[c][0] = roomNo;				//Assigns room number to 2D array
				tableData[c][2] = points;				//Assigns points to 2D array
			}
			raf.close();
			referenceTeachersClass(tableData, records);		//Passes tableData to 'referenceTeachersClass' method to reference and fill out the empty second column
			return tableData;
		}
		catch(FileNotFoundException e)
		{
			Toolkit.getDefaultToolkit().beep();
			JOptionPane.showMessageDialog(this, "The RecyclingActivityStats.txt notepad file is missing from the current directory. This process cannot function without this file.\nError Code: " + e,"File is Missing!", JOptionPane.ERROR_MESSAGE);	//Output any error if a file is not found
		}
		catch(Exception e)
		{
			Toolkit.getDefaultToolkit().beep();
			JOptionPane.showMessageDialog(this, "An error has occured. Please contact Harris Rasheed to deal with this issue\nError Code: " + e,"Error Message", JOptionPane.ERROR_MESSAGE);	//Output any errors caught
		}
		return null;
	}
//-----------------------------------------------------------------	
//This method is used to reference the room number of classrooms with
//the teacher who occupies the room. This is to provide more detail on
//the report. The first parameter is the array with an empty column and
//a column of room numbers to be referenced and the second parameter is
//the number of rows in the array.
	private String[][] referenceTeachersClass(String[][] tableData, int rows)
	{
		try
		{
			for(int c = 0; c < rows; c++)
			{		
				RandomAccessFile RAF = new RandomAccessFile("TeacherClassroomPlan.txt", "r");
				int recordSize = 35;					//Each record in the RAF is 35 characters long
				int records = ((int) RAF.length())/recordSize;
				String currentLine = "";				//currentLine is used to store each record of the file
				RAF.seek(0);
						
				for(int i = 0; i < records; i++)
				{
					if(tableData[c][0] == null)			//Checks if there is a blank record in the array/table
					{
						break;							//Terminates the loop if a record is blank
					}
					
					String line = "";				//Initialises variable that stores records
					RAF.seek(i * recordSize);		//File pointer looks at the beginning of each record
					
					for(int ct = 0; ct < recordSize; ct++)		//Reads each record one by one
					{
						byte b = RAF.readByte();
						line += (char)b;
					}

					currentLine = line.substring(0,5).trim();				//Reads first field of the record
					
					if((tableData[c][0].trim()).equalsIgnoreCase(currentLine))	//Checks if the reference in the table and file match
					{
						tableData[c][1] = (line.substring(5,35)).trim();		//Assigns reference file data to the table's current row
						break;								//Terminates current loop
					}
				}
				RAF.close();
			}
			bubbleSort(tableData, rows);		//Bubble sorts array before being presented on the report
			return tableData;					//Returns sorted table
		}
		catch(FileNotFoundException e)
		{
			Toolkit.getDefaultToolkit().beep();
			JOptionPane.showMessageDialog(this, "The TeacherClassroomPlan.txt notepad file is missing from the current directory. This process cannot function without this file.\nError Code: " + e,"File is Missing!", JOptionPane.ERROR_MESSAGE);	//Output any error if a file is not found
		}
		catch(Exception e)
		{
			Toolkit.getDefaultToolkit().beep();
			JOptionPane.showMessageDialog(this, "An error has occured. Please contact Harris Rasheed to deal with this issue\nError Code: " + e,"Error Message", JOptionPane.ERROR_MESSAGE);	//Output any errors caught
		}
		return null;
	}
//-----------------------------------------------------------------
//This method uses the bubble sort algorithm to sort the array parameter in DESCENDING order according to the 3rd column.
//The first parameter is the array that needs to be sorted, the second parameter is the number of records in the array
	protected String[][] bubbleSort(String [][] tableData, int rows)
	{
		for(int top = tableData.length - 1; top > 0; top--)		//Loops and focuses sort on the unsorted part
		{
			for(int upper = 1; upper <= top; upper++)
			{
				int lower = upper - 1;
				if(tableData[lower][2] == null||tableData[upper][2] == null)	//Checks if the table row is blank
				{
					break;
				}
				
				if(Integer.parseInt(tableData[lower][2]) < Integer.parseInt(tableData[upper][2]))	//Compares two indexes of the table
				{
					String temp1 = tableData[upper][0];		//Stores each field in a temporary variable
					String temp2 = tableData[upper][1];
					String temp3 = tableData[upper][2];
					tableData[upper][0] = tableData[lower][0];		//Overwrites the previous index with the larger value
					tableData[upper][1] = tableData[lower][1];
					tableData[upper][2] = tableData[lower][2];
					tableData[lower][0] = temp1;			//Writes the temporary variables' value to the higher array index
					tableData[lower][1] = temp2;
					tableData[lower][2] = temp3;
				}
			}
		}	
		return tableData;		//Returns sorted table
	}
//-----------------------------------------------------------------
//This method is used to execute the appropriate method when the user performs an action event
	public void actionPerformed(ActionEvent ae)
	{
		if(ae.getSource()==print)
		{
			try
			{
				if(!table.print())		//Checks if the user has cancelled the print job
				{
					JOptionPane.showMessageDialog(this, "The user has cancelled the print job.", "Cancelled Print Job", JOptionPane.INFORMATION_MESSAGE);
    			}
			}
			catch(java.awt.print.PrinterException e)			//Catches printer exception
			{
				JOptionPane.showMessageDialog(this, "Unable to print due to " + e, "Print Job Error", JOptionPane.ERROR_MESSAGE);
				Toolkit.getDefaultToolkit().beep();
			}

		}
		if(ae.getSource()==cancel)
		{
			FR.setVisible(false);
			menuPage MP = new menuPage("");
			MP.FR.setVisible(true);
		}
		if(ae.getSource()==logOut)
		{
			FR.setVisible(false);
			Program EP = new Program("");
			EP.FR.setVisible(true);
			Toolkit.getDefaultToolkit().beep();
		}
		if(ae.getSource()==exit)
		{
			System.exit(0);
		}
	}
}
//----------------------------------------------------------------------------------------------------------------------
//Recycler Registration Screen; This is the screen where the user can add a recycler
//to the system which will allow them to process their attendance.
class recyclerRegistration extends JFrame implements ActionListener
{
	JFrame FR = new JFrame("Recycling Activity Monitoring System - Recycler Registration");
	Container Obj1 = getContentPane();
	GridBagLayout GBL = new GridBagLayout();
	GridBagConstraints GBC = new GridBagConstraints();
	JMenuBar MB = new JMenuBar();
	JMenu file = new JMenu("File");
	JMenuItem logOut = new JMenuItem("Log out");
	JMenuItem Exit = new JMenuItem("Exit");
	JButton btnRegister = new JButton("Register");
	JButton btnCancel = new JButton("Cancel");
	JButton btnDelRecy = new JButton ("Delete Recycler Registration");
	JLabel lblRecyName = new JLabel("Recycler Name: ");
	JLabel lblForm = new JLabel("Form: ");
	JLabel lblRecyRegist = new JLabel("Recycler Registration");
	JTextField txtRecyName = new JTextField(30);
	Choice chForm = new Choice();
	Color c = new Color(6,69,1);
	Font f = new Font("Comic Sans MS", Font.BOLD, 22);
	Font lbls = new Font("Comic Sans MS", Font. BOLD, 18);

//-----------------------------------------------------------------
//Constructor for the Recycler Registration Screen that places components on the Frame
	public recyclerRegistration(String str)
	{
		super(str);
		
		FR.setJMenuBar(MB);
		MB.add(file);
		file.add(logOut);
		file.add(Exit);
				
		getContentPane().setLayout(GBL);
		FR.add(Obj1);
		
		GBC.fill = GridBagConstraints.BOTH;
		GBC.anchor = GridBagConstraints.CENTER;
		GBC.gridwidth = 4;
		GBC.gridheight = 2;
		GBC.gridy = 1;
		GBC.gridx = 1;
		GBC.fill = GridBagConstraints.VERTICAL;
		GBC.ipady = 10;
		GBC.insets = new Insets(10,10,10,10);
		GBL.setConstraints(lblRecyRegist,GBC);
		lblRecyRegist.setFont(f);
		lblRecyRegist.setHorizontalAlignment(JLabel.CENTER);
		lblRecyRegist.setForeground(Color.white);
		getContentPane().add(lblRecyRegist);
		
		GBC.gridheight = 1;
		GBC.gridwidth = 1;
		GBC.gridy = 5;
		lblRecyName.setFont(lbls);
		GBL.setConstraints(lblRecyName, GBC);
		lblRecyName.setForeground(Color.white);
		getContentPane().add(lblRecyName);
		
		GBC.gridy = 7;
		lblForm.setFont(lbls);
		GBL.setConstraints(lblForm, GBC);
		lblForm.setForeground(Color.white);
		getContentPane().add(lblForm);
		
		GBC.gridheight = 2;
		GBC.gridy = 9;
		GBC.ipady = 30;
		GBC.ipadx = 150;
		GBL.setConstraints(btnRegister,GBC);
		getContentPane().add(btnRegister);
		
		GBC.gridheight = 1;
		GBC.gridwidth = 2;
		GBC.gridx = 3;
		GBC.gridy = 5;
		GBC.ipady = 0;
		GBC.ipadx = 0;
		lblRecyName.setLabelFor(txtRecyName);
		GBL.setConstraints(txtRecyName, GBC);
		getContentPane().add(txtRecyName);

		GBC.gridy = 7;
		GBC.gridwidth = 2;
		lblForm.setLabelFor(chForm);
		addFormClasses();
		GBL.setConstraints(chForm, GBC);
		getContentPane().add(chForm);						
		
		GBC.gridheight = 2;
		GBC.gridy = 9;
		GBC.ipady = 30;
		GBC.ipadx = 100;
		GBL.setConstraints(btnCancel,GBC);
		getContentPane().add(btnCancel);
		
		GBC.gridy = 11;
		GBC.gridx = 2;
		GBL.setConstraints(btnDelRecy, GBC);
		getContentPane().add(btnDelRecy);
		
		getContentPane().setBackground(c);
		FR.setExtendedState(Frame.MAXIMIZED_BOTH);
		
		btnRegister.addActionListener(this);
		btnCancel.addActionListener(this);
		btnDelRecy.addActionListener(this);
		logOut.addActionListener(this);
		Exit.addActionListener(this);
		validate();
	}
//-----------------------------------------------------------------	
//This method reads the random access file and retrieves the names of form classes.
//They are then added to a choice box.
	private void addFormClasses()
	{
		try
		{
			RandomAccessFile RAF = new RandomAccessFile("FormClassroomLocation.txt", "r");
			int recordSize = 13;			//Each record in this random access file takes up 13 characters
			int records = (int) RAF.length()/recordSize;	//Calculates the number of records in the RAF
			
			for(int i = 0; i < records; i++)
			{
				String line = "";
				RAF.seek(i * recordSize);	//File pointer goes to the beginning of each record
				
				for(int d = 0; d < 8; d++)
				{
					byte b = RAF.readByte();
					line += (char) b;
				}	
			chForm.addItem(line.trim());		//Adds form class to choice box
			}
		}
		catch(Exception e)
		{
			Toolkit.getDefaultToolkit().beep();
			JOptionPane.showMessageDialog(this, "An error has occured. Please contact Harris Rasheed to deal with this issue\nError Code: " + e,"Error Message", JOptionPane.ERROR_MESSAGE);	//Output any errors caught	
		}
	}
//-----------------------------------------------------------------
//This method is used to register a brand new recycler to the system.
//The first parameter is their name and the second parameter is the form
//class that they are in.
	private void registerRecycler(String rName, String formClass)
	{
		try
		{
			RandomAccessFile RAF = new RandomAccessFile("RecyclerAttendanceStats.txt","rw");
			int recordSize = 40;
			int records = (int) RAF.length()/recordSize;
			
			for(int i = 0; i < records; i++)
			{
				String line = "";			//Variable stores each record
				RAF.seek(i * recordSize);	//File pointer looks at the beginning of each record
				
				for(int d = 0; d < 40; d++)		//Reads each record of the array
				{
					byte b = RAF.readByte();
					line += (char) b;
				}
				
				if(line.substring(0,30).trim().equals(rName))		//Tests if the first field matches the existing recycler names
				{
					if(line.substring(30,38).trim().equals(formClass))	//Tests if the form class is also the same
					{
						JOptionPane.showMessageDialog(this, "This recycler already exists.", "Error", JOptionPane.ERROR_MESSAGE);
						return;		//Returns void and gives an error message because the recycler already exists on file
					}
				}
			}
			
			for(int a = rName.length(); a < 30; a++)		//Sets length of String to 30 characters
			{
				rName += " ";
			}
			
			for(int a = formClass.length(); a < 8; a++)		//Sets length of String to 8 characters
			{
				formClass += " ";
			}
			
			String record = rName + formClass + "0 ";	//Adds the recycler attendance tally of zero for the new recycler
			RAF.seek(RAF.length());						//File pointer goes to the end of the file
			RAF.writeBytes(record);						//Writes the record
			JOptionPane.showMessageDialog(this, "Recycler Registered","Registration Successful", JOptionPane.INFORMATION_MESSAGE);
			RAF.close();
		}
		catch(FileNotFoundException e)
		{
			Toolkit.getDefaultToolkit().beep();
			JOptionPane.showMessageDialog(this, "The RecyclerAttendanceStats.txt notepad file is missing from the current directory. This process cannot function without this file.\nError Code: " + e,"File is Missing!", JOptionPane.ERROR_MESSAGE);	//Output error if a file is not found
		}
		catch(Exception e)
		{
			Toolkit.getDefaultToolkit().beep();
			JOptionPane.showMessageDialog(this, "An error has occured. Please contact Harris Rasheed to deal with this issue\nError Code: " + e,"Error Message", JOptionPane.ERROR_MESSAGE);	//Output any errors caught	
		}
	}
//-----------------------------------------------------------------	
//Deletes recycler name from the system. The first parameter is the
//recycler's name that has been selected by the user.
	private void deleteRecycler(String rName)
	{
		try
		{
			File file = new File("RecyclerAttendanceStats.txt");
			RandomAccessFile RAF = new RandomAccessFile(file, "rw");
			int recordSize = 40;
			int records = (int)(RAF.length())/recordSize;
			
			for(int i = 0; i < records; i++)
			{
				String line = "";				//Variable stores each record
				RAF.seek(i * recordSize);		//File pointer goes to the beginning of each line
				
				for(int ct = 0; ct < recordSize; ct++)	//Reads each record
				{
					byte b = RAF.readByte();
					line += (char) b;
				}
				
				String recyName = (line.substring(0,30)).trim();
				
				if(recyName.equals(rName))			//Checks if the String is equal to the delete parameter
				{
					RAF.seek((records - 1) * (recordSize));					//File pointer looks at the last record
					byte[] ba = new byte[recordSize];						//Creates array with the size of one record
					RAF.readFully(ba);										//Reads entire line and places it in the array
					RAF.seek(i * recordSize);								//File pointer looks at the place where the record was found
					RAF.write(ba);											//Overwrites the record location with the last record since it is to be deleted
					RAF.setLength(((records - 1)* (recordSize)));			//Truncates file and removes the last record from the end of the file which has been moved to the deleted record's space
					break;
				}
			}
			RAF.close();
		}		
		catch(FileNotFoundException e)
		{
			Toolkit.getDefaultToolkit().beep();
			JOptionPane.showMessageDialog(this, "The RecyclerAttendanceStats.txt notepad file is missing from the current directory. This process cannot function without this file.\nError Code: " + e,"File is Missing!", JOptionPane.ERROR_MESSAGE);	//Output error if a file is not found
		}
		catch(Exception e)
		{
			Toolkit.getDefaultToolkit().beep();
			JOptionPane.showMessageDialog(this, "An error has occured. Please contact Harris Rasheed to deal with this issue\nError Code: " + e,"Error Message", JOptionPane.ERROR_MESSAGE);	//Output any errors caught	
		}
	}	
//-----------------------------------------------------------------		
//This method is used to execute the appropriate method when the user performs an action event
	public void actionPerformed(ActionEvent ae)
	{
		if(ae.getSource()==btnRegister)
		{
			String recyName = txtRecyName.getText().trim();		//Reads the recycler name input
			String form = chForm.getSelectedItem().trim();		//Reads the form class selected
			registerRecycler(recyName, form);					//Passes above variables to the registerRecycler method
		}
		if(ae.getSource()==btnDelRecy)
		{
			lunchCollRounds LCR = new lunchCollRounds("");
			Frame frame = new Frame();
			String delRecycler = (String) JOptionPane.showInputDialog(frame, "Please select the recycler you want to delete", "Recycler Deletion", JOptionPane.PLAIN_MESSAGE, null, LCR.addRecyclers(),"<Recycler Name>");
			deleteRecycler(delRecycler);
		}
		if(ae.getSource()==btnCancel)
		{
			FR.setVisible(false);
			menuPage MP = new menuPage("");
			MP.FR.setVisible(true);
		}
		if(ae.getSource()==logOut)
		{
			FR.setVisible(false);
			Program EP = new Program("");
			EP.FR.setVisible(true);
		}
		if(ae.getSource()==Exit)
		{
			System.exit(0);
		}
	}
}
//----------------------------------------------------------------------------------------------------------------------
//Recycler of the Month Criterion Screen; This is the screen where the user specify
//a criterion for a month and year for the system to produce a report of viable candidates
//and their attendance statistics during the specified criterion.
class RoMCriterion extends JFrame implements ActionListener
{
	JFrame FR = new JFrame("Recycling Activity Monitoring System - Recycler of the Month Criterion");
	Container Obj1 = getContentPane();
	GridBagLayout GBL = new GridBagLayout();
	GridBagConstraints GBC = new GridBagConstraints();
	JMenuBar MB = new JMenuBar();
	JMenu file = new JMenu("File");
	JMenuItem logOut = new JMenuItem("Log out");
	JMenuItem Exit = new JMenuItem("Exit");
	JButton btnFind = new JButton("Find");
	JButton btnCancel = new JButton("Cancel");
	JButton btnBack = new JButton("Back");
	JLabel lblMonth = new JLabel("Month: ");
	JLabel lblYear = new JLabel("Year: ");
	JLabel lblRecycMonCrit = new JLabel("Recycler of the Month Criterion");
	Choice chMonth = new Choice();
	Choice chYear = new Choice();
	Color c = new Color(6,69,1);
	Font f = new Font("Comic Sans MS", Font.BOLD, 22);
	Font lbls = new Font("Comic Sans MS", Font. BOLD, 18);
	
//-----------------------------------------------------------------
//Constructor for the Recycler of the Month Criterion Screen that places components on the Frame	
	public RoMCriterion(String str)
	{
		super(str);
		
		FR.setJMenuBar(MB);
		MB.add(file);
		file.add(logOut);
		file.add(Exit);
				
		getContentPane().setLayout(GBL);
		FR.add(Obj1);
		
		GBC.fill = GridBagConstraints.BOTH;
		GBC.anchor = GridBagConstraints.CENTER;
		GBC.gridwidth = 4;
		GBC.gridheight = 2;
		GBC.gridy = 1;
		GBC.gridx = 1;
		GBC.fill = GridBagConstraints.VERTICAL;
		GBC.ipady = 50;
		GBL.setConstraints(lblRecycMonCrit,GBC);
		lblRecycMonCrit.setFont(f);
		lblRecycMonCrit.setHorizontalAlignment(JLabel.CENTER);
		lblRecycMonCrit.setForeground(Color.white);
		getContentPane().add(lblRecycMonCrit);
		
		GBC.gridheight = 1;
		GBC.gridwidth = 2;
		GBC.gridy = 5;
		lblMonth.setFont(lbls);
		GBL.setConstraints(lblMonth, GBC);
		lblMonth.setForeground(Color.white);
		getContentPane().add(lblMonth);
		
		GBC.gridy = 7;
		lblYear.setFont(lbls);
		GBL.setConstraints(lblYear, GBC);
		lblYear.setForeground(Color.white);
		getContentPane().add(lblYear);
		
		GBC.gridheight = 2;
		GBC.gridy = 9;
		GBC.ipady = 30;
		GBC.ipadx = 150;
		GBL.setConstraints(btnFind,GBC);
		getContentPane().add(btnFind);
		
		GBC.gridheight = 1;
		GBC.gridx = 3;
		GBC.gridy = 5;
		GBC.ipady = 0;
		GBC.ipadx = 100;
		lblMonth.setLabelFor(chMonth);	
		GBL.setConstraints(chMonth, GBC);
		chMonth.addItem("January");
		chMonth.addItem("February");
		chMonth.addItem("March");
		chMonth.addItem("April");
		chMonth.addItem("May");
		chMonth.addItem("June");
		chMonth.addItem("July");
		chMonth.addItem("August");
		chMonth.addItem("September");
		chMonth.addItem("October");
		chMonth.addItem("November");
		chMonth.addItem("December");
		getContentPane().add(chMonth);

		GBC.gridy = 7;
		lblYear.setLabelFor(chYear);
		GBL.setConstraints(chYear, GBC);
		chYear.addItem("2010");
		chYear.addItem("2011");
		chYear.addItem("2012");
		chYear.addItem("2013");
		chYear.addItem("2014");
		chYear.addItem("2015");
		chYear.addItem("2016");
		chYear.addItem("2017");
		chYear.addItem("2018");
		chYear.addItem("2019");
		chYear.addItem("2020");
		getContentPane().add(chYear);						
		
		GBC.gridheight = 2;
		GBC.gridy = 9;
		GBC.ipady = 30;
		GBC.ipadx = 100;
		GBL.setConstraints(btnCancel,GBC);
		getContentPane().add(btnCancel);
		
		getContentPane().setBackground(c);
		FR.setExtendedState(Frame.MAXIMIZED_BOTH);
		
		btnFind.addActionListener(this);
		btnCancel.addActionListener(this);
		logOut.addActionListener(this);
		Exit.addActionListener(this);
		btnBack.addActionListener(this);
		validate();
	}
//-----------------------------------------------------------------	
//This method is used to execute the appropriate method when the user performs an action event
	public void actionPerformed(ActionEvent ae)
	{
		if(ae.getSource()==btnFind)
		{
			String month = chMonth.getSelectedItem();
			String year = chYear.getSelectedItem();
			FR.setVisible(false);
			RoMCandidateReport RoMCR = new RoMCandidateReport(month, year);
		}
		if(ae.getSource()==btnCancel)
		{
			FR.setVisible(false);
			menuPage MP = new menuPage("");
			MP.FR.setVisible(true);
		}
		if(ae.getSource()==logOut)
		{
			FR.setVisible(false);
			Program EP = new Program("");
			EP.FR.setVisible(true);
		}
		if(ae.getSource()==Exit)
		{
			System.exit(0);
		}
	}
}
//----------------------------------------------------------------------------------------------------------------------
//Recycler of the Month Candidates Report Screen; This is the screen where the user can view
//the recyclers that have been shortlisted for the Recycler of the Month award according to the
//time criterion of the user.
class RoMCandidateReport extends JFrame implements ActionListener
{
	JFrame FR = new JFrame("Recycling Activity Monitoring System - Recycler of the Month Candidate Report");
	Container Obj1 = getContentPane();
	GridBagLayout GBL = new GridBagLayout();
	GridBagConstraints GBC = new GridBagConstraints();
	JMenuBar MB = new JMenuBar();
	JMenu file = new JMenu("File");
	JMenuItem logOut = new JMenuItem("Log out");
	JMenuItem exit = new JMenuItem("Exit");
	JButton print = new JButton("Print");
	JButton cancel = new JButton("Cancel");
	JLabel lblRoMCandidateReport = new JLabel("Recycler of the Month Candidate Report");
	Color c = new Color(6,69,1);
	Font f = new Font("Comic Sans MS", Font.BOLD, 26);
	String[] colNames = {"Recycler Name", "Form Class", "Attendance Tally"};
	JTable table;
	
//-----------------------------------------------------------------
	public RoMCandidateReport(String month, String year)
	{
		table = new JTable(loadTable(month, year), colNames);
		JScrollPane scroll = new JScrollPane(table);
		FR.setJMenuBar(MB);
		MB.add(file);
		file.add(logOut);
		file.add(exit);
				
		getContentPane().setLayout(GBL);
		FR.add(Obj1);
		
		GBC.fill = GridBagConstraints.BOTH;
		GBC.anchor = GridBagConstraints.CENTER;
		GBC.gridwidth = 2;
		GBC.gridy = 1;
		GBC.gridx = 1;
		GBC.insets = new Insets(10,10,10,10);
		GBL.setConstraints(lblRoMCandidateReport,GBC);
		lblRoMCandidateReport.setFont(f);
		lblRoMCandidateReport.setHorizontalAlignment(JLabel.CENTER);
		lblRoMCandidateReport.setForeground(Color.white);
		getContentPane().add(lblRoMCandidateReport);
				
		GBC.gridy = 4;
		GBC.gridx = 1;
		GBC.gridwidth = 2;
		GBL.setConstraints(scroll,GBC);
		getContentPane().add(scroll);
		
		GBC.gridy = 10;
		GBC.ipady = 20;
		GBC.ipadx = 100;
		GBL.setConstraints(print,GBC);
		getContentPane().add(print);
		
		GBC.gridy = 12;
		GBL.setConstraints(cancel,GBC);
		getContentPane().add(cancel);
		
		getContentPane().setBackground(c);
		FR.setExtendedState(Frame.MAXIMIZED_BOTH);
		FR.setVisible(true);
		
		print.addActionListener(this);
		exit.addActionListener(this);
		logOut.addActionListener(this);
		cancel.addActionListener(this);
		validate();
	}
//-----------------------------------------------------------------	
//This method loads the JTable when the class is started up and fills it with values
//The first parameter is the month and the second parameter is the year. These dates
//are the criterion to find the recyclers who attended in that year and month so that
//they can be displayed in the report as candidates for the recycler of the month award
	private String[][] loadTable(String month, String year)
	{
		try
		{
			int recordSize = 40;				//The size of each record is 40 characters long
			RandomAccessFile raf = new RandomAccessFile("RecyclerAttendanceLog.txt", "r");
			int records = (int) raf.length()/recordSize;		//Calculates the number of records exist in the file
			String[][] logData = new String[records][3]; 		//Creates an array to store the data from the random access log file
			
			for(int c = 0; c < records; c++)				//First loop iterates each array index
			{
				raf.seek(c * recordSize);			//File pointer goes to the start of each record on evert iteration
				String currentLine = "";			//currentLine is used to store each record being searched
				for(int i = 0; i < recordSize; i++)
				{
					byte b = raf.readByte();		//Reads each character and stores it as a byte
					currentLine += (char) b;		//Converts each character from byte to character and then to string and collects each character on each loop
				}
				
				String recycName = (currentLine.substring(0,30)).trim();	//Reads the first field
				String attMonth = (currentLine.substring(33,35)).trim();	//Reads the second field
				String attYear = (currentLine.substring(36,40)).trim();		//Reads the third field
				
				if(attMonth.substring(0,1).equals("0"))		//Checks if the month starts with a 0 e.g. 07 is July
				{
					attMonth = attMonth.substring(1,2);		//Takes the main number if it starts with zero
				}
				
				int attMon = Integer.parseInt(attMonth);	//Converts String to integer
				
				switch(attMon)				//Switch case changes the month number to month name
				{
					case 1:
						attMonth = "January";
						break;
						
					case 2:
						attMonth = "February";
						break;
						
					case 3:
						attMonth = "March";
						break;
						
					case 4:
						attMonth = "April";
						break;
						
					case 5:
						attMonth = "May";
						break;
						
					case 6:
						attMonth = "June";
						break;
						
					case 7:
						attMonth = "July";
						break;
						
					case 8:
						attMonth = "August";
						break;
						
					case 9:
						attMonth = "September";
						break;
						
					case 10:
						attMonth = "October";
						break;
						
					case 11:
						attMonth = "November";
						break;
						
					case 12:
						attMonth = "December";
						break;
						
				}
				logData[c][0] = recycName;		//Writes record details to array
				logData[c][1] = attMonth;
				logData[c][2] = attYear;
			}		
			raf.close();
			return candidateSelection(logData, records, month, year);	//returns candidateSelection method to filter out recyclers who attended in different months
		}
		catch(FileNotFoundException e)
		{
			Toolkit.getDefaultToolkit().beep();
			JOptionPane.showMessageDialog(this, "The RecyclerAttendanceLog.txt notepad file is missing from the current directory. This process cannot function without this file.\nError Code: " + e,"File is Missing!", JOptionPane.ERROR_MESSAGE);	//Output any error if a file is not found
		}
		catch(Exception e)
		{
			Toolkit.getDefaultToolkit().beep();
			JOptionPane.showMessageDialog(this, "An error has occured. Please contact Harris Rasheed to deal with this issue\nError Code: " + e,"Error Message", JOptionPane.ERROR_MESSAGE);	//Output any errors caught	
		}
		return null;
	}
//-----------------------------------------------------------------	
//This method is used to filter out all the recycler names who did not attend in the month and
//year specified by the criterion. The first parameter is an array of all the recycler attendance
//stored on file, the second parameter is the number of rows in the array, the third and fourth 
//parameter is the month and year criterion.
	private String[][] candidateSelection(String[][] logData, int records, String month, String year)
	{
		try
		{
			RandomAccessFile RAF = new RandomAccessFile("RecyclerAttendanceStats.txt", "r");
			int recordSize = 40;
			int rowCount = (int) RAF.length()/recordSize;		//Calculates the number of recyclers registered
			int pendingRowCounter = 0;				//Used to keep track of the location of the last record in the array
			RAF.close();
			String[][] currentMonthAttendance = new String[rowCount][3];		//Creates array for the registered recyclers
		
			for(int i = 0; i < rowCount; i++)		//Loops and sets the 3rd column values to zero
			{
				currentMonthAttendance[i][2] = Integer.toString(0);
			}
			System.out.println("Pass 1");
			for(int c = 0; c < records; c++)
			{
				String current = logData[c][0];		//Reads current record first field
				int counter = 0;					//Used to keep track of a double occurred record so that its attendance can be iterated
				boolean doubleOccurence = false;	//Flag used to check if a recycler has participated more than once
System.out.println("Pass 2");
				for(counter = 0; counter < rowCount; counter++)
				{
					if(current.equals(currentMonthAttendance[counter][0]))	//Checks if the current record of the log file matches an existing recycler attendance
					{
						doubleOccurence = true;		//The flag is activated marking the double occurence
						break;
					}
				}
System.out.println("Pass 3");
				if(!doubleOccurence)		//Checks if the current record is a new recycler to the table
				{
					if(logData[c][1].equals(month) && logData[c][2].equals(year))		//Checks if the recycler's attendance matches the criterion specified by the user
					{
						currentMonthAttendance[pendingRowCounter][0] = logData[c][0];			//Writes recycler name to the array
						currentMonthAttendance[pendingRowCounter][2] = Integer.toString(1);		//Sets the recycler attendance to 1
						pendingRowCounter++;	//Adds to row counter
					}
				}
				else
				{
					currentMonthAttendance[counter][2] = Integer.toString(Integer.parseInt(currentMonthAttendance[counter][2]) + 1);  //Adds to recycler attendance if the name already exists in the array
				}
			}
			return referenceFormClass(currentMonthAttendance, records);		//Returns filtered array
		}
		catch(Exception e)
		{
			Toolkit.getDefaultToolkit().beep();
			JOptionPane.showMessageDialog(this, "An error has occureds. Please contact Harris Rasheed to deal with this issue\nError Code: " + e,"Error Message", JOptionPane.ERROR_MESSAGE);	//Output any errors caught	
		}
		return null;
	}
//-----------------------------------------------------------------
//This method is used to reference the recycler names with their form classes.
//The first parameter is the array containing the filtered recycling record.
//The second parameter is the number of rows in the array.
	private String[][] referenceFormClass(String[][] tableData, int rows)
	{
		try
		{
			RandomAccessFile RAF = new RandomAccessFile("RecyclerAttendanceStats.txt", "r");
			
			for(int c = 0; c < rows; c++)
			{		
				int recordSize = 40;							//Set length of each record in the random access file
				int records = (int) RAF.length()/recordSize;	//Calculates the number of records in the random access file
				String currentLine = "";						//currentLine is used to store each record of the file
						
				for(int i = 0; i < records; i++)
				{
					if(tableData[c][0] == null)			//Checks if there is a blank record in the array/table
					{
						break;							//Terminates the loop if a record is blank
					}
					
					String line = "";			//Variable that stores each record
					RAF.seek(i * recordSize);	//File pointer looks at the start of each record
					
					for(int ct = 0; ct < recordSize; ct++)		//Loop reads each record
					{
						byte b = RAF.readByte();
						line += (char)b;
					}

					currentLine = (line.substring(0,30)).trim();				//Reads first record field
					
					if((tableData[c][0].trim()).equalsIgnoreCase(currentLine))	//Checks if the reference in the table and file match
					{
						tableData[c][1] = (line.substring(30,38)).trim();		//Assigns reference file data to table array
						break;
					}
				}
			}
			RAF.close();
			
			recyclingActivityReport RAR = new recyclingActivityReport("");
			RAR.bubbleSort(tableData, rows);	//Bubble sorts the array
			return Patch(tableData, rows);		//Removes empty rows
		}
		catch(FileNotFoundException e)
		{
			Toolkit.getDefaultToolkit().beep();
			JOptionPane.showMessageDialog(this, "The RecyclerAttendanceStats.txt notepad file is missing from the current directory. This process cannot function without this file.\nError Code: " + e,"File is Missing!", JOptionPane.ERROR_MESSAGE);	//Output any error if a file is not found
		}
		catch(Exception e)
		{
			Toolkit.getDefaultToolkit().beep();
			JOptionPane.showMessageDialog(this, "An error has occured. Please contact Harris Rasheed to deal with this issue\nError Code: " + e,"Error Message", JOptionPane.ERROR_MESSAGE);	//Output any errors caught	
		}
		return null;
	}
//-----------------------------------------------------------------
//This method removes the empty rows from the bottom of the 2D array.
	private String[][] Patch(String [][] tableData, int rows)
	{
		int active = 0;
		for(int i = 0; i < rows; i++)
		{
			if(tableData[i][0]!=null)	//Tests if the row is occupied with data
			{
				active++;			//Adds one to the active row counter
			}
		}

		String[][] patch = new String[active][3];		//Creates an array with the specific size of the relevant records
		
		for(int i = 0; i < active; i++)
		{
			for(int d = 0; d < 3; d++)
			{
				patch[i][d] = tableData[i][d];			//Writes relevant records to the new array
			}
		}
		return patch;	//Returns new filtered and sorted array
	}	
//-----------------------------------------------------------------
//This method is used to execute the appropriate method when the user performs an action event
	public void actionPerformed(ActionEvent ae)
	{
		if(ae.getSource()==print)
		{
			try
			{
				if(!table.print())		//Checks if the user has cancelled the print job
				{
					JOptionPane.showMessageDialog(this, "The user has cancelled the print job.", "Cancelled Print Job", JOptionPane.INFORMATION_MESSAGE);
    			}
			}
			catch (java.awt.print.PrinterException e)			//Catches printer exception
			{
				JOptionPane.showMessageDialog(this, "Unable to print due to " + e, "Print Job Error", JOptionPane.ERROR_MESSAGE);
				Toolkit.getDefaultToolkit().beep();
			}
		}
		if(ae.getSource()==cancel)
		{
			FR.setVisible(false);
			RoMCriterion RoMC = new RoMCriterion("");
			RoMC.FR.setVisible(true);
		}
		if(ae.getSource()==logOut)
		{
			FR.setVisible(false);
			Program EP = new Program("");
			EP.FR.setVisible(true);
			Toolkit.getDefaultToolkit().beep();		
		}
		if(ae.getSource()==exit)
		{
			System.exit(0);
		}
	}	
}
//----------------------------------------------------------------------------------------------------------------------
//Teachers & Classrooms Plan Screen; This is the screen where the reference table of the database
//can be viewed. It also allows the user to update data in the table.
class teacherClassPlan extends JFrame implements ActionListener
{
	JFrame FR = new JFrame("Recycling Activity Monitoring System - Teachers & Classrooms Plan");
	Container Obj1 = getContentPane();
	GridBagLayout GBL = new GridBagLayout();
	GridBagConstraints GBC = new GridBagConstraints();
	JMenuBar MB = new JMenuBar();
	JMenu file = new JMenu("File");
	JMenuItem logOut = new JMenuItem("Log out");
	JMenuItem exit = new JMenuItem("Exit");
	JButton print = new JButton("Print");
	JButton save = new JButton("Save");
	JButton cancel = new JButton("Cancel");
	JLabel lblTeacherClass = new JLabel("Teachers & Classrooms Plan");
	Color c = new Color(6,69,1);
	Font f = new Font("Comic Sans MS", Font.BOLD, 26);
	String[] colNames = {"Room Number", "Teacher"};
	File RAF = new File("TeacherClassroomPlan.txt");
	JTable table = new JTable(loadTable(122, 5, 35, RAF), colNames);
	JScrollPane scroll = new JScrollPane(table);

//-----------------------------------------------------------------
//Constructor for the Teachers & Classrooms Plan Screen that places components on the Frame
	public teacherClassPlan(String str)
	{
		super(str);
				
		FR.setJMenuBar(MB);
		MB.add(file);
		file.add(logOut);
		file.add(exit);
				
		getContentPane().setLayout(GBL);
		FR.add(Obj1);
		
		GBC.fill = GridBagConstraints.BOTH;
		GBC.anchor = GridBagConstraints.CENTER;
		GBC.gridwidth = 2;
		GBC.gridy = 1;
		GBC.gridx = 1;
		GBC.insets = new Insets(10,10,10,10);
		GBL.setConstraints(lblTeacherClass,GBC);
		lblTeacherClass.setFont(f);
		lblTeacherClass.setHorizontalAlignment(JLabel.CENTER);
		lblTeacherClass.setForeground(Color.white);
		getContentPane().add(lblTeacherClass);
				
		GBC.gridy = 4;
		GBC.gridx = 1;
		GBC.gridwidth = 2;
		GBL.setConstraints(scroll,GBC);
		getContentPane().add(scroll);
		
		GBC.ipady = 20;
		GBC.ipadx = 100;
		GBC.gridy = 10;
		GBL.setConstraints(save,GBC);
		getContentPane().add(save);
			
		GBC.gridy = 12;

		GBL.setConstraints(print,GBC);
		getContentPane().add(print);
		
		GBC.gridy = 14;
		GBL.setConstraints(cancel,GBC);
		getContentPane().add(cancel);
		
		getContentPane().setBackground(c);
		FR.setExtendedState(Frame.MAXIMIZED_BOTH);
		
		print.addActionListener(this);
		save.addActionListener(this);
		exit.addActionListener(this);
		logOut.addActionListener(this);
		cancel.addActionListener(this);
		validate();
	}
//-----------------------------------------------------------------	
//This method loads JTable on initialisation of the Teachers & Classrooms Plan reference table
	protected String[][] loadTable(int rows, int firstField, int recordSize, File referenceFile)
	{
		try
		{
			String[][] tableData = new String[rows][2];						//Creates array with size to hold data
			RandomAccessFile raf = new RandomAccessFile(referenceFile, "r");	//Creates an object of the Random Access File
			int records = (int) raf.length()/recordSize;
			
			for(int c = 0; c < records; c++)		//First loop iterates each array index
			{
				raf.seek(c * recordSize);			//File pointer goes to the start of each record on evert iteration
				String currentLine = "";			//currentLine is used to store each record being searched
				for(int i = 0; i < recordSize; i++)
				{
					byte b = raf.readByte();
					currentLine += (char) b;
				}
				
				tableData[c][0] = (currentLine.substring(0,firstField)).trim();				//Assigns room number to 2D array
				tableData[c][1] = (currentLine.substring(firstField,recordSize)).trim();	//Assigns teacher name to 2D array
			}
			raf.close();
			return tableData;		//returns 2D array of Random Access file records
		}
		catch(FileNotFoundException e)
		{
			Toolkit.getDefaultToolkit().beep();
			JOptionPane.showMessageDialog(this, "An important system file is missing from the current directory. This process cannot function without this file.\nError Code: " + e,"File is Missing!", JOptionPane.ERROR_MESSAGE);	//Output any error if a file is not found
		}
		catch(Exception e)
		{
			Toolkit.getDefaultToolkit().beep();
			JOptionPane.showMessageDialog(this, "An error has occured. Please contact Harris Rasheed to deal with this issue\nError Code: " + e,"Error Message", JOptionPane.ERROR_MESSAGE);	//Output any errors caught	
		}
		return null;
	}
//-----------------------------------------------------------------
//This method is used to store the data from the table in the
//random access file when the save button is clicked. The first
//parameter is the current table's data.
	protected void saveData(String tableData[][], int firstFieldSize, int secondFieldSize, File file)
	{
		try
		{
			RandomAccessFile raf = new RandomAccessFile(file, "rw");
			int recordSize = firstFieldSize + secondFieldSize;
			int records = (int)(raf.length())/recordSize;
			
			raf.seek(0);		//File pointer looks at the beginning of the file
			
			for(int i = 0; i < records; i++)
			{
				for(int c = tableData[i][0].length(); c < firstFieldSize; c++)	//Sets the length of the room numbers in the array
				{
					tableData[i][0] += " ";
				}
				raf.writeBytes(tableData[i][0]);				//Writes the classroom number to the array
				
				for(int c = tableData[i][1].length(); c < secondFieldSize; c++)	//Sets the length of the teacher names in the array
				{
					tableData[i][1] += " ";
				}
				raf.writeBytes(tableData[i][1]);				//Writes the teacher names to the array
			}
			raf.close();
			JOptionPane.showMessageDialog(this, "Your save is successful!","Save Successful!", JOptionPane.INFORMATION_MESSAGE);
		}								
		
		catch(FileNotFoundException e)
		{
			Toolkit.getDefaultToolkit().beep();
			JOptionPane.showMessageDialog(this, "An important system file is missing from the current directory. This process cannot function without this file.\nError Code: " + e,"File is Missing!", JOptionPane.ERROR_MESSAGE);	//Output any error if a file is not found
		}
		catch(Exception e)
		{
			Toolkit.getDefaultToolkit().beep();
			JOptionPane.showMessageDialog(this, "An error has occured. Please contact Harris Rasheed to deal with this issue\nError Code: " + e,"Error Message", JOptionPane.ERROR_MESSAGE);	//Output any errors caught	
		}
	}
//-----------------------------------------------------------------
//This method is used to execute the appropriate method when the user performs an action event
	public void actionPerformed(ActionEvent ae)
	{
		if(ae.getSource()==print)
		{
			try
			{
				if(!table.print())		//Checks if the user has cancelled the print job
				{
					JOptionPane.showMessageDialog(this, "The user has cancelled the print job.", "Cancelled Print Job", JOptionPane.INFORMATION_MESSAGE);
    			}
			}
			catch(java.awt.print.PrinterException e)			//Catches printer exception
			{
				JOptionPane.showMessageDialog(this, "Unable to print due to " + e, "Print Job Error", JOptionPane.ERROR_MESSAGE);
				Toolkit.getDefaultToolkit().beep();
			}
		}
		if(ae.getSource()==save)
		{
			int rows = table.getRowCount();				//Retrieves number of rows
			int columns = table.getColumnCount();		//Retrieves number of columns
			String[][] tableData = new String[rows][columns];		//Creates array with the size of the JTable
			
			for(int i = 0; i < rows; i++)
			{
				for(int j = 0; j < columns; j++)
				{
					tableData[i][j] = (String) table.getValueAt(i,j); //Reads all the values of JTable and stores it in a 2D array
				}
			}
			saveData(tableData, 5, 30, RAF);		//Executes storeTeacherClass method when the Save button is pressed
		}
		if(ae.getSource()==cancel)
		{
			FR.setVisible(false);
			menuPage MP = new menuPage("");
			MP.FR.setVisible(true);
		}
		if(ae.getSource()==logOut)
		{
			FR.setVisible(false);
			Program EP = new Program("");
			EP.FR.setVisible(true);
			Toolkit.getDefaultToolkit().beep();		
		}
		if(ae.getSource()==exit)
		{
			System.exit(0);
		}
	}
}
//----------------------------------------------------------------------------------------------------------------------
//Form Classroom Locations Screen; This is the screen where the user can view and update the
//reference table that associates form classes with the room numbers that they are located in.
class formClassroomLocation extends JFrame implements ActionListener
{
	JFrame FR = new JFrame("Recycling Activity Monitoring System - Form Classroom Locations");
	Container Obj1 = getContentPane();
	GridBagLayout GBL = new GridBagLayout();
	GridBagConstraints GBC = new GridBagConstraints();
	JMenuBar MB = new JMenuBar();
	JMenu file = new JMenu("File");
	JMenuItem logOut = new JMenuItem("Log out");
	JMenuItem exit = new JMenuItem("Exit");
	JButton print = new JButton("Print");
	JButton save = new JButton("Save");
	JButton cancel = new JButton("Cancel");
	JLabel lblFormClass = new JLabel("Form Classroom Locations");
	Color c = new Color(6,69,1);
	Font f = new Font("Comic Sans MS", Font.BOLD, 26);
	String[] colNames = {"Form Class", "Room Number"};
	File RAF = new File("FormClassroomLocation.txt");
	teacherClassPlan TCP = new teacherClassPlan("");
	JTable table = new JTable(TCP.loadTable(38, 8, 13, RAF), colNames);
	JScrollPane scroll = new JScrollPane(table);

//-----------------------------------------------------------------
//Constructor for the Form Classroom Locations Screen that places components on the Frame
	public formClassroomLocation(String str)
	{
		super(str);
				
		FR.setJMenuBar(MB);
		MB.add(file);
		file.add(logOut);
		file.add(exit);
				
		getContentPane().setLayout(GBL);
		FR.add(Obj1);
		
		GBC.fill = GridBagConstraints.BOTH;
		GBC.anchor = GridBagConstraints.CENTER;
		GBC.gridwidth = 2;
		GBC.gridy = 1;
		GBC.gridx = 1;
		GBC.insets = new Insets(10,10,10,10);
		GBL.setConstraints(lblFormClass,GBC);
		lblFormClass.setFont(f);
		lblFormClass.setHorizontalAlignment(JLabel.CENTER);
		lblFormClass.setForeground(Color.white);
		getContentPane().add(lblFormClass);
				
		GBC.gridy = 4;
		GBC.gridx = 1;
		GBC.gridwidth = 2;
		GBL.setConstraints(scroll,GBC);
		getContentPane().add(scroll);
		
		GBC.ipady = 20;
		GBC.ipadx = 100;
		GBC.gridy = 10;
		GBL.setConstraints(save,GBC);
		getContentPane().add(save);
			
		GBC.gridy = 12;

		GBL.setConstraints(print,GBC);
		getContentPane().add(print);
		
		GBC.gridy = 14;
		GBL.setConstraints(cancel,GBC);
		getContentPane().add(cancel);
		
		getContentPane().setBackground(c);
		FR.setExtendedState(Frame.MAXIMIZED_BOTH);
		
		print.addActionListener(this);
		save.addActionListener(this);
		exit.addActionListener(this);
		logOut.addActionListener(this);
		cancel.addActionListener(this);
		validate();
	}
//-----------------------------------------------------------------
//This method is used to execute the appropriate method when the user performs an action event
	public void actionPerformed(ActionEvent ae)
	{
		if(ae.getSource()==print)
		{
			try
			{
				if(!table.print())		//Checks if the user has cancelled the print job
				{
					JOptionPane.showMessageDialog(this, "The user has cancelled the print job.", "Cancelled Print Job", JOptionPane.INFORMATION_MESSAGE);
    			}
			}
			catch(java.awt.print.PrinterException e)			//Catches printer exception
			{
				JOptionPane.showMessageDialog(this, "Unable to print due to " + e, "Print Job Error", JOptionPane.ERROR_MESSAGE);
				Toolkit.getDefaultToolkit().beep();
			}
		}
		if(ae.getSource()==save)
		{
			int rows = table.getRowCount();
			int columns = table.getColumnCount();
			String[][] tableData = new String[rows][columns];
			
			for(int i = 0; i < rows; i++)
			{
				for(int j = 0; j < columns; j++)
				{
					tableData[i][j] = (String) table.getValueAt(i,j);		//Stores JTable values into a 2D array
				}
			}
			teacherClassPlan TCP = new teacherClassPlan("");
			TCP.saveData(tableData, 8, 5, RAF);
		}
		if(ae.getSource()==cancel)
		{
			FR.setVisible(false);
			menuPage MP = new menuPage("");
			MP.FR.setVisible(true);
		}
		if(ae.getSource()==logOut)
		{
			FR.setVisible(false);
			Program EP = new Program("");
			EP.FR.setVisible(true);
			Toolkit.getDefaultToolkit().beep();		
		}
		if(ae.getSource()==exit)
		{
			System.exit(0);
		}
	}
}
//----------------------------------------------------------------------------------------------------------------------
//Security Settings Screen; This is the screen where the user has the option to change the password
//or assign a new secret question.
class securitySett extends JFrame implements ActionListener
{
	JFrame FR = new JFrame("Recycling Activity Monitoring System - Security Settings");
	Container Obj1 = getContentPane();
	GridBagLayout GBL = new GridBagLayout();
	GridBagConstraints GBC = new GridBagConstraints();
	JMenuBar MB = new JMenuBar();
	JMenu file = new JMenu("File");
	JMenuItem logOut = new JMenuItem("Log out");
	JMenuItem Exit = new JMenuItem("Exit");
	JButton changePassBtn = new JButton("Change Password");
	JButton secretQtionBtn = new JButton("Assign Secret Question");
	JButton btnBack = new JButton("Back");
	JLabel lblSecuritySettings = new JLabel("Security Settings");
	Color c = new Color(6,69,1);
	Font f = new Font("Comic Sans MS", Font.BOLD, 22);
	
//-----------------------------------------------------------------
//Constructor for the Security Settings Screen that places components on the Frame
	public securitySett(String str)
	{
		super(str);
		
		FR.setJMenuBar(MB);
		MB.add(file);
		file.add(logOut);
		file.add(Exit);
				
		getContentPane().setLayout(GBL);
		FR.add(Obj1);
		
		GBC.fill = GridBagConstraints.BOTH;
		GBC.anchor = GridBagConstraints.PAGE_START;
		GBC.gridwidth = 2;
		GBC.gridheight = 2;
		GBC.gridy = 1;
		GBC.gridx = 1;
		GBC.insets = new Insets(10,10,10,10);
		GBC.fill = GridBagConstraints.VERTICAL;
		GBL.setConstraints(lblSecuritySettings,GBC);
		lblSecuritySettings.setFont(f);
		lblSecuritySettings.setHorizontalAlignment(JLabel.CENTER);
		lblSecuritySettings.setForeground(Color.white);
		getContentPane().add(lblSecuritySettings);
		
		GBC.anchor = GridBagConstraints.CENTER;
		GBC.gridy = 3;
		GBC.ipady = 20;
		GBC.ipadx = 100;
		GBL.setConstraints(changePassBtn,GBC);
		getContentPane().add(changePassBtn);
		
		GBC.gridy = 5;
		GBL.setConstraints(secretQtionBtn,GBC);
		getContentPane().add(secretQtionBtn);
		
		GBC.gridy = 20;
		GBC.anchor = GridBagConstraints.PAGE_END;
		GBC.insets = new Insets(250,10,10,10);
		GBL.setConstraints(btnBack,GBC);
		getContentPane().add(btnBack);
		
		getContentPane().setBackground(c);
		FR.setExtendedState(Frame.MAXIMIZED_BOTH);
		
		changePassBtn.addActionListener(this);
		secretQtionBtn.addActionListener(this);
		logOut.addActionListener(this);
		Exit.addActionListener(this);
		btnBack.addActionListener(this);
		validate();
	}
//-----------------------------------------------------------------	
//This method is used to execute the appropriate method when the user performs an action event
	public void actionPerformed(ActionEvent ae)
	{
		if(ae.getSource()==changePassBtn)
		{
			FR.setVisible(false);
			changePass CP = new changePass("");
			CP.FR.setVisible(true);
		}
		if(ae.getSource()==secretQtionBtn)
		{
			FR.setVisible(false);
			secretQtion SQ = new secretQtion("");
			SQ.FR.setVisible(true);
		}
		if(ae.getSource()==btnBack)
		{
			FR.setVisible(false);
			menuPage MP = new menuPage("");
			MP.FR.setVisible(true);
		}
		if(ae.getSource()==logOut)
		{
			FR.setVisible(false);
			Program EP = new Program("");
			EP.FR.setVisible(true);
		}
		if(ae.getSource()==Exit)
		{
			System.exit(0);
		}
	}
}
//----------------------------------------------------------------------------------------------------------------------
//Change Password Screen; This screen allows the user to change the system password.
class changePass extends JFrame implements ActionListener
{
	JFrame FR = new JFrame("Recycling Activity Monitoring System - Change Password");
	Container Obj1 = getContentPane();
	GridBagLayout GBL = new GridBagLayout();
	GridBagConstraints GBC = new GridBagConstraints();
	JMenuBar MB = new JMenuBar();
	JMenu file = new JMenu("File");
	JMenuItem logOut = new JMenuItem("Log out");
	JMenuItem Exit = new JMenuItem("Exit");
	JButton btnSubmit = new JButton("Submit");
	JButton btnCancel = new JButton("Cancel");
	JButton btnBack = new JButton("Back");
	JLabel lblCurrentPass = new JLabel("Current Password: ");
	JLabel lblNewPass = new JLabel("New Password: ");
	JLabel lblNewConfPass = new JLabel("Confirm New Password: ");
	JLabel lblChangePassword = new JLabel("Change Password");
	JPasswordField currentPass = new JPasswordField(20);
	JPasswordField newPass = new JPasswordField(20);
	JPasswordField newConfPass = new JPasswordField(20);
	Color c = new Color(6,69,1);
	Font f = new Font("Comic Sans MS", Font.BOLD, 22);
	
//-----------------------------------------------------------------
//Constructor for the Change Password Screen that places components on the Frame
	public changePass(String str)
	{
		super(str);
		
		FR.setJMenuBar(MB);
		MB.add(file);
		file.add(logOut);
		file.add(Exit);
				
		getContentPane().setLayout(GBL);
		FR.add(Obj1);
		
		GBC.fill = GridBagConstraints.BOTH;
		GBC.anchor = GridBagConstraints.PAGE_START;
		GBC.gridwidth = 4;
		GBC.gridheight = 2;
		GBC.gridy = 1;
		GBC.gridx = 1;
		GBC.insets = new Insets(10,10,10,10);
		GBC.fill = GridBagConstraints.VERTICAL;
		GBL.setConstraints(lblChangePassword,GBC);
		lblChangePassword.setFont(f);
		lblChangePassword.setHorizontalAlignment(JLabel.CENTER);
		lblChangePassword.setForeground(Color.white);
		getContentPane().add(lblChangePassword);
		
		GBC.gridwidth = 2;
		GBC.gridy = 3;
		GBC.gridx = 1;
		GBC.anchor = GridBagConstraints.CENTER;
		GBL.setConstraints(lblCurrentPass, GBC);
		lblCurrentPass.setForeground(Color.white);
		getContentPane().add(lblCurrentPass);
		
		GBC.gridy = 5;
		GBL.setConstraints(lblNewPass, GBC);
		lblNewPass.setForeground(Color.white);
		getContentPane().add(lblNewPass);
		
		GBC.gridy = 7;
		GBL.setConstraints(lblNewConfPass, GBC);
		lblNewConfPass.setForeground(Color.white);
		getContentPane().add(lblNewConfPass);
		
		GBC.gridy = 9;
		GBL.setConstraints(btnSubmit,GBC);
		GBC.ipady = 20;
		GBC.ipadx = 100;
		getContentPane().add(btnSubmit);
		
		GBC.gridx = 3;
		GBC.gridy = 3;
		GBC.ipady = 0;
		GBC.ipadx = 0;
		GBL.setConstraints(currentPass, GBC);
		currentPass.setEchoChar('*');
		lblCurrentPass.setLabelFor(currentPass);
		getContentPane().add(currentPass);
		
		GBC.gridy = 5;
		GBL.setConstraints(newPass, GBC);
		lblNewPass.setLabelFor(newPass);
		getContentPane().add(newPass);
		
		GBC.gridy = 7;
		GBL.setConstraints(newConfPass, GBC);
		lblNewConfPass.setLabelFor(newConfPass);
		getContentPane().add(newConfPass);						
		
		GBC.gridx = 3;
		GBC.gridy = 9;
		GBC.ipady = 20;
		GBC.ipadx = 100;
		GBL.setConstraints(btnCancel,GBC);
		getContentPane().add(btnCancel);
		
		getContentPane().setBackground(c);
		FR.setExtendedState(Frame.MAXIMIZED_BOTH);
		
		btnSubmit.addActionListener(this);
		btnCancel.addActionListener(this);
		logOut.addActionListener(this);
		Exit.addActionListener(this);
		btnBack.addActionListener(this);
		validate();
	}
//-----------------------------------------------------------------	
//This method is used to execute the appropriate method when the user performs an action event
	public void actionPerformed(ActionEvent ae)
	{
		if(ae.getSource()==btnSubmit)
		{
			String currentPassword = (currentPass.getText()).trim();
			String newPassword = (newPass.getText()).trim();
			String confNewPassword = (newConfPass.getText()).trim();
			
			changePassword(currentPassword, newPassword, confNewPassword);
			
			FR.setVisible(false);
			securitySett SP = new securitySett("");
			SP.FR.setVisible(true);
		}
		if(ae.getSource()==btnCancel)
		{
			FR.setVisible(false);
			securitySett SP = new securitySett("");
			SP.FR.setVisible(true);
		}
		if(ae.getSource()==logOut)
		{
			FR.setVisible(false);
			Program EP = new Program("");
			EP.FR.setVisible(true);
		}
		if(ae.getSource()==Exit)
		{
			System.exit(0);
		}
	}
//-----------------------------------------------------------------	
//This method changes the system password. The first parameter is the
//current password of the system. This is used to authorise the password change.
//The second parameter is the new password to be assigned and the third parameter is
//the second input of the new password for verification and to prevent a typo from occuring
	private void changePassword(String currentPass, String newPass, String newPassConf)
	{
		File PasswordStore = new File("SystemSecurity.dat");		//Creates .dat file that stores the program password
		String pass = "";				//Initialises variable to store the password that will be read from the RAF
		
		if((newPass.equals(""))||(newPassConf.equals(""))||(currentPass.equals("")))		//Checks if the user left any field blank
		{
			JOptionPane.showMessageDialog(this, "Error. You have left a mandatory field blank.","Error", JOptionPane.ERROR_MESSAGE);
			return;
		}		
		
		if(!PasswordStore.exists())		//Checks if the file storing the password exists in the same directory
		{
			Toolkit.getDefaultToolkit().beep();
			JOptionPane.showMessageDialog(this, "Error. The file that stores the password does not exist.","Error Message", JOptionPane.ERROR_MESSAGE);
			System.exit(1);				//Shuts down the program because the password file is required for system login and changing the password
		}
		else
		{
			try
			{
				RandomAccessFile RAF = new RandomAccessFile(PasswordStore, "rw");		//Creates object to read Random Access File
				RAF.seek(0);															//Sets pointer to start of file
				
				for(int i = 0; i < 20; i++)
				{
					byte letter = RAF.readByte();			//Reads each character from the first 20 characters of the file
					pass = pass + (char) letter;			//Adds each character of the first 20 in the file to variable 'pass'
				}
				
				pass = pass.trim();							//Removes spaces from the string
				
				if(pass.equals(currentPass))				//Tests if the current system password was correctly input
				{
					if(newPass.equals(newPassConf))			//Tests if the new password is equal to the second input
					{
						RAF.seek(0);											//Sets file pointer to the start of the file
						for(int i = newPass.length(); i < 20; i++)				//Sets String length to 20 characters
						{
							newPass += " ";
						}
						RAF.writeBytes(newPass);								//Add new password to the beginning of the file
						JOptionPane.showMessageDialog(this, "The password was successfully changed.","Change Successful!", JOptionPane.PLAIN_MESSAGE);
					}
					else
					{
						JOptionPane.showMessageDialog(this, "The new password that you entered in both fields do not match. Please try again.","Error", JOptionPane.ERROR_MESSAGE);
					}
				}
				else
				{
					JOptionPane.showMessageDialog(this, "The current password that you entered is incorrect.\nPlease note that this field is case-sensitive.","Error", JOptionPane.ERROR_MESSAGE);
				}
				
				RAF.close();					//Closes RAF
			}
			catch(Exception e)
			{
				Toolkit.getDefaultToolkit().beep();
				JOptionPane.showMessageDialog(this, "An error has occured. Please contact Harris Rasheed to deal with this issue\nError Code: " + e,"Error Message", JOptionPane.ERROR_MESSAGE);	//Output any errors caught	
			}
		}
	}
}
//----------------------------------------------------------------------------------------------------------------------
//Assign Secret Question Screen; This is the screen where the user can change the secret question
//and answer of the system.
class secretQtion extends JFrame implements ActionListener
{
	JFrame FR = new JFrame("Recycling Activity Monitoring System - Assign Secret Question");
	Container Obj1 = getContentPane();
	GridBagLayout GBL = new GridBagLayout();
	GridBagConstraints GBC = new GridBagConstraints();
	JMenuBar MB = new JMenuBar();
	JMenu file = new JMenu("File");
	JMenuItem logOut = new JMenuItem("Log out");
	JMenuItem Exit = new JMenuItem("Exit");
	JButton btnSubmit = new JButton("Submit");
	JButton btnCancel = new JButton("Cancel");
	JButton btnBack = new JButton("Back");
	JLabel lblCurrentPass = new JLabel("Current Password: ");
	JLabel lblSQtion = new JLabel("Secret Question: ");
	JLabel lblSAnswer = new JLabel("Answer: ");
	JLabel lblAssignSQtion = new JLabel("Assign Secret Question");
	JPasswordField currentPass = new JPasswordField(20);
	JTextField secQtion = new JTextField(20);
	JTextField secAnswer = new JTextField(20);
	Color c = new Color(6,69,1);
	Font f = new Font("Comic Sans MS", Font.BOLD, 22);
	
//-----------------------------------------------------------------
//Constructor for the Secret Question Screen that places components on the Frame
	public secretQtion(String str)
	{
		super(str);
		
		FR.setJMenuBar(MB);
		MB.add(file);
		file.add(logOut);
		file.add(Exit);
				
		getContentPane().setLayout(GBL);
		FR.add(Obj1);
		
		GBC.fill = GridBagConstraints.BOTH;
		GBC.anchor = GridBagConstraints.PAGE_START;
		GBC.gridwidth = 4;
		GBC.gridheight = 2;
		GBC.gridy = 1;
		GBC.gridx = 1;
		GBC.insets = new Insets(10,10,10,10);
		GBC.fill = GridBagConstraints.VERTICAL;
		GBL.setConstraints(lblAssignSQtion,GBC);
		lblAssignSQtion.setFont(f);
		lblAssignSQtion.setHorizontalAlignment(JLabel.CENTER);
		lblAssignSQtion.setForeground(Color.white);
		getContentPane().add(lblAssignSQtion);
		
		GBC.gridwidth = 2;
		GBC.gridy = 3;
		GBC.anchor = GridBagConstraints.CENTER;
		GBL.setConstraints(lblCurrentPass, GBC);
		lblCurrentPass.setForeground(Color.white);
		getContentPane().add(lblCurrentPass);
		
		GBC.gridy = 5;
		GBL.setConstraints(lblSQtion, GBC);
		lblSQtion.setForeground(Color.white);
		getContentPane().add(lblSQtion);
		
		GBC.gridy = 7;
		GBL.setConstraints(lblSAnswer, GBC);
		lblSAnswer.setForeground(Color.white);
		getContentPane().add(lblSAnswer);
		
		GBC.gridy = 9;
		GBL.setConstraints(btnSubmit,GBC);
		GBC.ipady = 20;
		GBC.ipadx = 100;
		getContentPane().add(btnSubmit);
		
		GBC.gridx = 3;
		GBC.gridy = 3;
		GBC.ipady = 0;
		GBC.ipadx = 0;
		GBL.setConstraints(currentPass, GBC);
		currentPass.setEchoChar('*');
		lblCurrentPass.setLabelFor(currentPass);
		getContentPane().add(currentPass);
		
		GBC.gridy = 5;
		lblSQtion.setLabelFor(secQtion);		
		GBL.setConstraints(secQtion, GBC);
		getContentPane().add(secQtion);

		GBC.gridy = 7;
		lblSAnswer.setLabelFor(secAnswer);
		GBL.setConstraints(secAnswer, GBC);
		getContentPane().add(secAnswer);						
		
		GBC.gridy = 9;
		GBC.ipady = 20;
		GBC.ipadx = 100;
		GBL.setConstraints(btnCancel,GBC);
		getContentPane().add(btnCancel);
		
		getContentPane().setBackground(c);
		FR.setExtendedState(Frame.MAXIMIZED_BOTH);
		
		btnSubmit.addActionListener(this);
		btnCancel.addActionListener(this);
		logOut.addActionListener(this);
		Exit.addActionListener(this);
		btnBack.addActionListener(this);
		validate();
	}
//-----------------------------------------------------------------	
//This method is used to execute the appropriate method when the user performs an action event
	public void actionPerformed(ActionEvent ae)
	{
		if(ae.getSource()==btnSubmit)
		{
			String currentPassword = (currentPass.getText()).trim();
			String sQtion = (secQtion.getText()).trim();
			String sAnswer = (secAnswer.getText()).trim();
			changeSecretQtion(currentPassword, sQtion, sAnswer);
			FR.setVisible(false);
			securitySett SP = new securitySett("");
			SP.FR.setVisible(true);
		}
		if(ae.getSource()==btnCancel)
		{
			FR.setVisible(false);
			securitySett SP = new securitySett("");
			SP.FR.setVisible(true);
		}
		if(ae.getSource()==logOut)
		{
			FR.setVisible(false);
			Program EP = new Program("");
			EP.FR.setVisible(true);
		}
		if(ae.getSource()==Exit)
		{
			System.exit(0);
		}
	}
//-----------------------------------------------------------------	
//This method changes the secret question of the system	when the user
//selects to do so. The first parameter is the current password of the system.
//This is used to authorise the ssecret question change. The second parameter is the new
//secret question to be assigned and the third parameter is the answer to the new secret question
	private void changeSecretQtion(String currentPass, String sQtion, String sAnswer)
	{
		File PasswordStore = new File("SystemSecurity.dat");		//Creates .dat file that stores the program password
		String pass = "";			//Initialises variable to store the password that will be read from the random access file
		
		if(!PasswordStore.exists())		//Checks if the file storing the password exists in the same directory
		{
			Toolkit.getDefaultToolkit().beep();
			JOptionPane.showMessageDialog(this, "Error. The file that stores the password does not exist.","Error Message", JOptionPane.ERROR_MESSAGE);
			System.exit(1);				//Shuts down the program because the password file is required for system login and changing the password
		}
		else
		{
			try
			{
				RandomAccessFile RAF = new RandomAccessFile(PasswordStore, "rw");		//Creates object to read Random Access File
				RAF.seek(0);															//Sets pointer to start of file
				
				for(int i = 0; i < 20; i++)
				{
					byte letter = RAF.readByte();			//Reads each character from the first 20 characters of the file
					pass = pass + (char) letter;			//Adds each character of the first 20 in the file to variable 'pass'
				}
				
				pass = pass.trim();							//Removes spaces from the string
				
				if(pass.equals(currentPass))
				{
					RAF.seek(20);										//Sets file pointer to the start of the file
					for(int i = sQtion.length(); i < 60; i++)			//Sets length of secret question string to 60 characters
					{
						sQtion += " ";
					}
					RAF.writeBytes(sQtion);								//Add new secret question to the 20th position
					
					RAF.seek(80);
					for(int i = sAnswer.length(); i < 20; i++)			//Sets length of secret answer string to 20 characters
					{
						sAnswer += " ";
					}
					RAF.writeBytes(sAnswer);							//Add new secret answer to the 80th position
					
					JOptionPane.showMessageDialog(this, "The secret question was successfully changed.","Change Successful!", JOptionPane.PLAIN_MESSAGE);
				}
				else if((currentPass == "")||(sQtion == "")||(sAnswer == ""))		//Tests if any fields have been left blank
				{
					JOptionPane.showMessageDialog(this, "Error. You have left a mandatory field blank.","Error", JOptionPane.ERROR_MESSAGE);
				}
				else
				{
					JOptionPane.showMessageDialog(this, "The current password that you entered is incorrect.\nPlease note that this field is case-sensitive.","Error", JOptionPane.ERROR_MESSAGE);
				}
				RAF.close();					//Closes Random Access File
			}
			catch(Exception e)
			{
				Toolkit.getDefaultToolkit().beep();
				JOptionPane.showMessageDialog(this, "An error has occured. Please contact Harris Rasheed to deal with this issue\nError Code: " + e,"Error Message", JOptionPane.ERROR_MESSAGE);	//Output any errors caught	
			}
		}
	}
}
//----------------------------------------------------------------------------------------------------------------------
