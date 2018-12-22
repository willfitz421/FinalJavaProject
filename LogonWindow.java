package GUIComponents;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import Users.Employee;
import Users.Technician;

/**
 * Logon Window -
 * 
 * The main is in this class as well.  It starts the Logon Window which starts the HelpDeskSystem
 * @author ryank
 *
 */
public class LogonWindow extends JFrame {

	private static final long serialVersionUID = 1L; //for serialization - ignore
	
	private JLabel idLbl;
	private JTextField idTF;
	private JPanel idPanel;	
	private JRadioButton userRB;
	private JRadioButton techRB;
	private JRadioButton supRB;
	private ButtonGroup roleBG;
	private JPanel bgPanel;
	private JPanel bgHelperPanel;
	private JButton logonBtn;
	private JPanel logonPanel;
	
	@SuppressWarnings("unused")
	public static void main(String[] args)  
	{	
		//spawns the logonWindow wish Spawns the HelpDeskSystem so this is 
		//all we need in the main.
		LogonWindow logonWin = new LogonWindow();
	}
	
	/**
	 * LogonWindow Constructor
	 */
	public LogonWindow()
	{
		setTitle("Help Desk Logon");
		setLayout(new BorderLayout());

		initComponents();
		pack();
		setResizable(false);
		setLocationRelativeTo(null);
		setVisible(true);
	}
		
	/**
	 * Initialize the components
	 */
	private void initComponents()
	{
		//ID textfield
		idLbl = new JLabel("Employee ID: ", JLabel.LEFT);
		idTF = new JTextField(15);

		idPanel = new JPanel();
		idPanel.setLayout(new FlowLayout());
		idPanel.add(idLbl);
		idPanel.add(idTF);

		//Panel for choosing your logonRole.
		userRB = new JRadioButton("User");
		techRB = new JRadioButton("Technician", true); //default to Tech being selected
		supRB = new JRadioButton("Supervisor");

		roleBG = new ButtonGroup();
		roleBG.add(userRB);		
		roleBG.add(techRB);
		roleBG.add(supRB);
		
		bgPanel = new JPanel();
		bgPanel.setLayout(new GridLayout(3,1));
		bgPanel.add(userRB);
		bgPanel.add(techRB);
		bgPanel.add(supRB);
		
		//Helper panel so that buttons are centered - purely for display
		bgHelperPanel = new JPanel();
		bgHelperPanel.add(bgPanel);
		
		//button to log in
		logonBtn = new JButton("logon");
		logonBtn.addActionListener(new ActionListener() {

			
			/**
			 * When the logon occurs, the id is checked,
			 * If it is not in the system, then you can add
			 * yourself in unless you are a Tech. Supervisors
			 * must add technicinas to the system before they
			 * can use the system.
			 */
			@Override
			public void actionPerformed(ActionEvent e) {
				
				LogonRole role;
				
		
				if (userRB.isSelected())
					role = LogonRole.USER;
				else if (techRB.isSelected())
					role =  LogonRole.TECHNICIAN;
				else
					role = LogonRole.SUPERVISOR;
				
				int id;
				
				try
				{
				    //read in the ID
					id = Integer.parseInt(idTF.getText());
					
					//start the HelpDeskSystem
					HelpDeskSystem hds = HelpDeskSystem.getInstance();
					
					//Determine if the loggedInEmployee exists or should be added
					Employee loggedInEmployee = determineLoggedInEmployee(role,id);
					//A non-null employee could have been returned but the field values
					//could be blank if the user hit the "X" on the log in window.
					//Don't let processing continue in this case.
					if (loggedInEmployee != null)
					{
						if (loggedInEmployee.getDepartment() == null  ||
								loggedInEmployee.getEmailAdd() == null ||
								loggedInEmployee.getFirstName() == null ||
								loggedInEmployee.getLastName() == null ||
								loggedInEmployee.getHireDate() == null ||
								loggedInEmployee.getPhoneNum() == null)
						{
							loggedInEmployee = null;
							JOptionPane.showMessageDialog(null, "Logon Failed: A text box was left empty.");
						}				
							
						else if (loggedInEmployee.getDepartment().equals("") ||
								loggedInEmployee.getEmailAdd().equals("") ||
								loggedInEmployee.getFirstName().equals("") ||
								loggedInEmployee.getLastName().equals("") ||
								loggedInEmployee.getHireDate().equals("") ||
								loggedInEmployee.getPhoneNum().equals(""))
						{
							loggedInEmployee = null;
							JOptionPane.showMessageDialog(null, "Logon Failed: A text box was left empty.");
						}
						
						
					}
					
					if (loggedInEmployee != null)
					{
						
						//Init the HelpDeskSystem with the correct components for the 
						//role of the user who logged in
						hds.initializeCompsForLogon(loggedInEmployee, role);
					
						//show the HelpDeskSystem 
						hds.setVisible(true);
					}
					
					//dispose of the log in window
					dispose();
					
					//Helpful - printout all contents of the HelpDeskSystem 
					//so that we know what we are starting with when we begin.
					System.out.println("Printing the DB contents after login..." + System.lineSeparator());
					System.out.println(hds.toString());
				}
				catch(LogonMismatchException ex)
				{
					//If someone is in the system as one role (like a User)
					//but tries to log in as another role (like a supervisor), 
					//then a LogonMismatchException is thrown - catch it and
					//tell them to retry.
					JOptionPane.showMessageDialog(null, "Try to re-logon: " + ex.getMessage());
				}
				catch(TechnicianNotInSystemException ex)
				{
					//If tech is not in system, tell them to get a supervisor
					//to add them first.
					JOptionPane.showMessageDialog(null, ex.getMessage() + 
							System.lineSeparator() +
							"A supervisor must add you to the system before logging in.");			
				}
				catch(NumberFormatException ex)
				{
					//If someone puts in a non-number ID, yell.
					JOptionPane.showMessageDialog(null, "Enter a valid integer as an ID to logon.");
				}
				catch(Exception ex)
				{
					//For any other error just flag the user of a failed logon.
					//Then print the stack trace.
					JOptionPane.showMessageDialog(null, "Logon Failed: " + ex);
					ex.printStackTrace();
				}
			}
			
		});
		
		logonPanel = new JPanel();
		logonPanel.add(logonBtn);
		
		add(idPanel,BorderLayout.NORTH);
		add(bgHelperPanel,BorderLayout.CENTER);
		add(logonPanel,BorderLayout.SOUTH);
	}
	
	/***
	 * Sets who is logged in, ie, sets the loggedInEmployee field.
	 * @param logonRole  Role selected at login.
	 * @param empID  ID selected at login.
	 * @throws LogonMismatchException  Thrown if an already existing employee logins in via
	 * wrong role.
	 * @throws TechnicianNotInSystemException Thrown if a new technician attempts to log in
	 * without first being added to the System by a Supervisor.
	 * @return Returns the employee who logged in (who may have just been created)
	 */
	private Employee determineLoggedInEmployee(LogonRole logonRole, int empID) 
			throws LogonMismatchException, TechnicianNotInSystemException
	{
		Employee loggedInEmployee = null;
		
		if (logonRole == LogonRole.USER)
		{
			loggedInEmployee = HelpDeskSystem.getInstance().getUserByID(empID);
			
			//Make sure the empID is not associated with a Tech/Supervisor
			if (loggedInEmployee == null)
			{
				Technician temp = HelpDeskSystem.getInstance().getTechByID(empID);
				LogonRole realRole = LogonRole.TECHNICIAN;
				
				if (temp != null)
				{
					if (temp.isSupervisor());
						realRole = LogonRole.SUPERVISOR;
					
					throw new LogonMismatchException(empID, logonRole, realRole);
				}
				else
				{
					
					System.out.println("Adding New User at logon");					
					JFrame frame = new JFrame();
					UserProfilePanel userProfile = new UserProfilePanel(logonRole);
				    userProfile.setID(empID);
				    
				    while (loggedInEmployee == null)
				    {
				    	JOptionPane.showMessageDialog(frame, userProfile, "New User Information", 
				    			JOptionPane.PLAIN_MESSAGE, null);
				    	loggedInEmployee = userProfile.createNewEmployee();
				    }
				   
				    HelpDeskSystem.getInstance().addUser(loggedInEmployee);
				}
			}
		
		}
		else if (logonRole == LogonRole.TECHNICIAN)
		{
			loggedInEmployee = HelpDeskSystem.getInstance().getTechByID(empID);
			
			//If the employee is logging in as a tech but is a supervisor, then yell.
			if (loggedInEmployee != null && ((Technician) loggedInEmployee).isSupervisor())
			{
				throw new LogonMismatchException(empID, logonRole,  LogonRole.SUPERVISOR);
			}
			
			//Make sure empID not associated with User 
			if (loggedInEmployee == null)
			{
				Employee temp = HelpDeskSystem.getInstance().getUserByID(empID);
				
				if (temp != null)
				{
					throw new LogonMismatchException(empID, logonRole, LogonRole.USER);
				}
				else
				{
					throw new TechnicianNotInSystemException(empID);
				}
			}
		}
		else if (logonRole == LogonRole.SUPERVISOR)
		{
			loggedInEmployee = HelpDeskSystem.getInstance().getTechByID(empID);
			
			//If the employee is logging in as a tech but is a supervisor, then yell.
			if (loggedInEmployee != null && !((Technician) loggedInEmployee).isSupervisor())
			{
				throw new LogonMismatchException(empID, logonRole,  LogonRole.TECHNICIAN);
			}
			
			//Make sure empID not associated with User 
			if (loggedInEmployee == null)
			{
				Employee temp = HelpDeskSystem.getInstance().getUserByID(empID);
				
				if (temp != null)
				{
					throw new LogonMismatchException(empID, logonRole, LogonRole.USER);
				}
				else
				{
					System.out.println("Adding New Supervisor at logon.");					
					JFrame frame = new JFrame();
					UserProfilePanel userProfile = new UserProfilePanel(logonRole);
					userProfile.setID(empID);
					
				    while (loggedInEmployee == null)
				    {
				    	JOptionPane.showMessageDialog(frame, userProfile, "New Supervisor Information", 
				    			JOptionPane.PLAIN_MESSAGE, null);

				    	loggedInEmployee = userProfile.createNewEmployee();
				    }
				    HelpDeskSystem.getInstance().addTechnician((Technician) loggedInEmployee);
				}
			}

		}
	
		return loggedInEmployee;
	}

}
