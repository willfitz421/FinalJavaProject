package GUIComponents;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
import Users.*;

/***
 * User Profile Panel is used in two ways:<br>
 * 
 * 1) To collect information on Users/Techs/Supervisors who are not yet
 *    in the system. <br>
 *    
 * 2) To display information about one's profile to any users. <br>
 * 
 * 3) If you are strategic, you can use this for allowing a supervisor 
 *    to add techs and promote them to supervisors.
 *
 * @author ryank
 *
 */
public class UserProfilePanel extends JPanel {
	
	private static final long serialVersionUID = 1L; //for serialization - ignore

	private LogonRole role;  //The panel includes a few more fields for a technician/supervisor.
	
	private JLabel idLabel; //ID label
	private JTextField idTF; //ID TextField

	private JLabel fnLabel; //First Name
	private JTextField fnTF;
	
	private JLabel lnLabel;//LastName
	private JTextField lnTF;
	
	private JLabel deptLabel;  //Department
	private JTextField deptTF;
	
	private JLabel emailLabel; //Email
	private JTextField emailTF;
	
	
	private JLabel phoneLabel; //Phone
	private JTextField phoneTF;

	private JLabel hireLabel; //Hire Date
	private JTextField hireTF;
	
	private JLabel supIDLabel; //Sup ID
	private JTextField supIDTF;
	
	private JPanel leftPanel; 
	private JPanel rightPanel;
	protected JPanel lrPanel;
	protected JPanel mainPanel;
	
	private JLabel activeLabel;
	private JPanel activePanel;
		
	private JRadioButton activeRB;
	private JRadioButton inActiveRB;
	
	ButtonGroup activeBG;


	/**
	 * This constructor was really provided to work with the LogonWindow
	 * to allow new Users and Supervisors to be added at logon.
	 * @param role The role of the person logging
	 */
	public UserProfilePanel(LogonRole role)
	{
		this.role = role;	
		initEmployeeComps();
		
		if (role == LogonRole.SUPERVISOR)
		{
			//If this is a supervisor creating a profile at logon
			//then assume then set addingNewTech to true and 
			//makingEdits to false.  This allows us certain
			//fields to stay enabled so that we can 
			//input values into them.
			initTechComponents(true, false);
		}
		add(mainPanel);
	}

	/**
	 * Constructor for viewing profile information for
	 * a given Employee (but not editing it)
	 * @param emp Employee whose profile information is desired
	 * @param role  Role of Employee
	 */
	public UserProfilePanel(Employee emp, LogonRole role)
	{
		this.role = role;	
		initEmployeeComps();
		
		if (role != LogonRole.USER)
		{
			initTechComponents(false, false);
			mainPanel.add(activePanel);
		}
	
		add(mainPanel);
		initializeInformationForEmployee(emp, role, false);
	}

	/**
	 * Initializes all the components that would show on the panel
	 * for any employee.
	 */
	private void initEmployeeComps()
	{
		idLabel = new JLabel("Employee ID (integer): ");
		idTF = new JTextField(15);

		fnLabel = new JLabel("First Name: ");
		fnTF = new JTextField(15);

		lnLabel = new JLabel("Last Name: ");
		lnTF = new JTextField(15);
		
		deptLabel = new JLabel("Department: ");
		deptTF = new JTextField(15);

		emailLabel = new JLabel("Email: ");
		emailTF = new JTextField(15);

		phoneLabel = new JLabel("Phone: ");
		phoneTF = new JTextField(15);
		
		hireLabel = new JLabel("Hire Date: ");
		hireTF = new JTextField(15);
		
		leftPanel = new JPanel(new GridLayout(4,2));
		leftPanel.setBorder(BorderFactory.createTitledBorder("Employee Info"));

		leftPanel.add(idLabel);
		leftPanel.add(idTF);
		
		leftPanel.add(fnLabel);
		leftPanel.add(fnTF);

		leftPanel.add(lnLabel);
		leftPanel.add(lnTF);

		leftPanel.add(hireLabel);
		leftPanel.add(hireTF);

		rightPanel = new JPanel();
		rightPanel = new JPanel(new GridLayout(4,2));
		rightPanel.setBorder(BorderFactory.createTitledBorder("Contact Info"));
		rightPanel.add(emailLabel);
		rightPanel.add(emailTF);
		
		
		rightPanel.add(phoneLabel);
		rightPanel.add(phoneTF);

		rightPanel.add(deptLabel);
		rightPanel.add(deptTF);
	
		lrPanel = new JPanel(new GridLayout(1,2));
		lrPanel.add(leftPanel);
		lrPanel.add(rightPanel);
		
		mainPanel = new JPanel(new GridLayout(3,1));
		mainPanel.add(lrPanel);
	}
	
	/**
	 * Initializes the tech portion of the UserProfilePanel
	 * @param forAddingNewTechs True if this panel will be used for making adding new technicians
	 * @param forMakingEdits False if this panel will be used for making edits to anyone's user profile
	 */
	private void initTechComponents(boolean forAddingNewTechs, boolean forMakingEdits)
	{

		//A supervisor ID is editable if we are adding a new
		//supervisor or technician.
		supIDLabel = new JLabel("Supervisor ID (integer): ");
		supIDTF = new JTextField(15);
		supIDTF.setEnabled(forAddingNewTechs);
		
		//The department is never editable.
		deptTF.setText(Technician.DEPARTMENT);
		deptTF.setEnabled(false); 
		rightPanel.add(supIDLabel);
		rightPanel.add(supIDTF);
		
		//Radio Buttons are only editable if a supervisor 
		//is making edits to a Technician.
		activeLabel = new JLabel("Status:");
		activePanel = new JPanel();
		activeRB = new JRadioButton("Active", true);
		activeRB.setEnabled(forMakingEdits);  
		inActiveRB = new JRadioButton("Not Active");
		inActiveRB.setEnabled(forMakingEdits);
		activeBG = new ButtonGroup();
		activeBG.add(activeRB);
		activeBG.add(inActiveRB);
		activePanel.add(activeLabel);
		activePanel.add(activeRB);
		activePanel.add(inActiveRB);
		
	}
	
	/**
	 * Method to populate the textfields of the panel with information
	 * for a specific Employee.
	 * NOTE:  THIS MUST BE CALLED AFTER initEmployeeComps() and
	 * initTechComponents() as the components would be null otherwise
	 * and so we would mot yet be able to set their internal inforamtion.
	 * @param emp The Employee whose information should be shown
	 * @param role The role of the Employee
	 * @param forMakingEdits True if the use of the UserProfilePanel will
	 * be for making edits.
	 */
	private void initializeInformationForEmployee(Employee emp, LogonRole role, 
			boolean forMakingEdits)
	{
		idTF.setText(""+emp.getEmpID());
		idTF.setEnabled(false);

		fnTF.setText(emp.getFirstName());
		fnTF.setEnabled(false);
		
		lnTF.setText(emp.getLastName());
		lnTF.setEnabled(false);

		phoneTF.setText(emp.getPhoneNum());
		phoneTF.setEnabled(false);
		
		deptTF.setText(emp.getDepartment());
		deptTF.setEnabled(false);
		
		emailTF.setText(emp.getEmailAdd());
		emailTF.setEnabled(false);
		
		hireTF.setText(emp.getHireDate());
		hireTF.setEnabled(false);

		if (role != LogonRole.USER)
		{
			Technician temp = (Technician) emp;
			
			supIDTF.setText("" + temp.getSuperID());
			supIDTF.setEnabled(forMakingEdits);
	
			activeRB.setSelected(temp.isActive());
			inActiveRB.setSelected(!temp.isActive());
			activeRB.setEnabled(forMakingEdits);  
			inActiveRB.setEnabled(forMakingEdits);

		}
	}
	
	/**
	 * Sets the ID Text Field Value - Included so that the ID could be set when 
	 * the UserProfilePanel is used to create a new user at logon.
	 * @param id  ID entered into the system at logon.
	 */
	public void setID(int id)
	{
		this.idTF.setText("" + id);
	}
	
	public int getInt()
	{
		return Integer.parseInt(idTF.getText());
	}
	
	/**
	 * Creates a new Employee/Technician/Supervisor based on
	 * the values contained in the UserProfilePanel's components
	 * and the role field of the UserProfilePanel. 
	 * @return Newly created Employee (User/Technician/Supervisor)
	 */
	public Employee createNewEmployee() {
		
		//The try catch block notifies the user if any invalid inputs
		//are on the Panel:  empID and supID should be integers and 
		// and all other fields (phone, email, etc) have been filled out.
		try {
			if (role == LogonRole.USER)
			{ 
				return new Employee(Integer.parseInt(idTF.getText()), 
						this.fnTF.getText(),
						this.lnTF.getText(),
						this.hireTF.getText(),
						this.phoneTF.getText(),
						this.emailTF.getText(),
						this.deptTF.getText());
			}
			else if (role == LogonRole.SUPERVISOR){
				return new Technician(Integer.parseInt(idTF.getText()), 
						this.fnTF.getText(),
						this.lnTF.getText(),
						this.hireTF.getText(),
						this.phoneTF.getText(),
						this.emailTF.getText(),
						Integer.parseInt(this.supIDTF.getText()),
						true, 
						true);
			}
			else 
			{
				return new Technician(Integer.parseInt(idTF.getText()), 
						this.fnTF.getText(),
						this.lnTF.getText(),
						this.hireTF.getText(),
						this.phoneTF.getText(),
						this.emailTF.getText(),
						Integer.parseInt(this.supIDTF.getText()),
						true, 
						true);
			}
		}
		catch(Exception ex)
		{
			JOptionPane.showMessageDialog(null, "Enter an integer for any ID fields. " +
						"All other fields may not be blank.");
			return null;
		}
	}
	
	
	
}
