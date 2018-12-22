package GUIComponents;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;
import javax.swing.event.*;

import Users.Employee;
import Users.Technician;

/**The ticket search panel allows an Employee to search through their
 * associated tickets and display them.  
 * 
 * It uses a splitPane to accomplish this.  A list of the tickets
 * are added to a scrollPane on the left side of the splitPane
 * and a TicketDisplayPanel on the right.  
 * 
 * HINT:  Thanks to how TicketDisplayPanels work, a 
 * submit button will be on the Panel if a tech is logged in so you shouldn't
 * need to add a submit button in this class.
 * 
 * @author ryank
 *
 */
public class ManegmentTab extends JPanel {

	private static final long serialVersionUID = 1L; //for serialization - ignore
	
	//The employee using the panel (so the ticket creator or tech updating it)
	protected Employee empUsingPanel;
	protected LogonRole empUsingPanelRole;
	
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
	
	private JLabel supLabel;
	private JTextField supIDTF;
	
	private JPanel leftPanel; 
	private JPanel rightPanel;
	protected JPanel lrPanel;
	protected JPanel mainPanel;
	
	private JButton submitNewTech;
	private JPanel submitPanel;
	private JPanel activePanel;
	
	ButtonGroup activeBG;
	
	private JRadioButton activeRB;
	private JRadioButton inActiveRB;
	
	//Top Panel which houses Refresh button and filter components
	private JPanel topPanel;
	private JPanel helperTopPanel;	//for esthetics
	private JButton refreshButton;
	private JLabel filterOnLabel;
	private JComboBox<TechSearchFilter> filterOnCB;
	private JLabel searchLbl;
	private JTextField searchButton;
	private JButton nonUserSearch;
	
	/**
	 * This constructor assume the Employee is a user.
	 * @param emp  The Employee logged in
	 * @param role The role of the employee logged in
	 */
	ManegmentTab(Employee emp, LogonRole role)
	{
		empUsingPanel = emp;
		empUsingPanelRole = role;
		
		initComps();	
	}
	
	private void initComps()
	{
		setLayout(new BorderLayout());
			
		mainPanel = new JPanel(new GridLayout(3,1));

		//Refresh Button -> If changes are made to the tickets in the 
		//list, this will reload the list so that the changes are visible
		//after selection.
		refreshButton = new JButton("Refresh");
		
		//When we need to refresh the list, set 
		//filter to none and refresh so all are visible again.
		refreshButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				filterOnCB.setSelectedItem(TechSearchFilter.NONE);
				setFilters();
				rightPanel.removeAll();
				leftPanel.removeAll();
			}
			
		});
	
		helperTopPanel = new JPanel();
		submitPanel = new JPanel();
		filterOnLabel = new JLabel("       Filter By:"); //Spacing weird to push the filter options farther 
														 //to the right from the Refresh Button

		//Now set up what can be filtered on.
		//First get the filters for the appropriate role (User/Tech).
		//HINT: For technicians/supervisors need to 
		//update the if-statement in TicketSearchFilters.getFiltersForRole...
		//If you don't, appropriateFilters will be null.		
		TechSearchFilter[] appropriateFilters = TechSearchFilter.getFiltersForRole();
		
		//Set the combo bbox to contain the filters and autoselect "None" at start.
		filterOnCB = new JComboBox<TechSearchFilter>(appropriateFilters);
		filterOnCB.setSelectedItem(TicketSearchFilters.NONE);
		
		//When someone selects a filter, filter accordingly.
		filterOnCB.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {	
				
				//When someone selects a filter, filter accordingly, ie, 
				//re-initialzize the lists with the filtered tickets
				//and put the nothing selected panel on the right side of the pane again.
				setFilters();	
			}
			
		});
		
		//Add the filter comps and refresh button to the helper panel.
		helperTopPanel.add(refreshButton);
		
		helperTopPanel.add(filterOnLabel);
		helperTopPanel.add(filterOnCB);
		
		//add helper Panel to top panel for esthetics
		topPanel = new JPanel();		
		topPanel.add(helperTopPanel);
		
		rightPanel = new JPanel();
		rightPanel = new JPanel(new GridLayout(4,2));
		
		leftPanel = new JPanel(new GridLayout(4,2));
		lrPanel = new JPanel(new GridLayout(1,2));
		
		activeRB = new JRadioButton("Active", true);
		inActiveRB = new JRadioButton("Not Active");
		
		
		//Set up tap panel (refresh/filter) and the split Pane.
		add(topPanel, BorderLayout.NORTH);
		add(mainPanel, BorderLayout.CENTER);
		
			searchLbl = new JLabel("ID: ");
			searchButton = new JTextField(10);
			nonUserSearch = new JButton("Search");
			searchButton.setBackground(Color.GRAY);
			searchButton.setEnabled(false);
			nonUserSearch.setEnabled(false);
			
			helperTopPanel.add(searchLbl);
			helperTopPanel.add(searchButton);
			helperTopPanel.add(nonUserSearch);
			
			activePanel = new JPanel();
			
			submitNewTech = new JButton("Submit");
			
			nonUserSearch.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					
					setFilters();
					
				} 	 	
			});
			
			submitNewTech.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
				
					createNewEmployee();
				} 	 	
			});
		
			setFilters();
		
	}
	
	/**
	 * Determines which tickets  meet the filter requirements.
	 * @return The tickets that meet the filter requirements.
	 */
	private Technician setFilters()
	{
		//First set all possible tickets and whittle them down
		//to the ones that meet the filter and should be returned.
		ArrayList<Technician> allPossibleTech = new ArrayList<Technician>();
		Technician techToReturn = null;
		
		allPossibleTech = HelpDeskSystem.getInstance().getTechnicians();
		
		searchButton.setBackground(Color.GRAY);
		searchButton.setEnabled(false);
		nonUserSearch.setEnabled(false);
		
		
		if(filterOnCB.getSelectedItem() == TechSearchFilter.NONE)
		{
			lrPanel.setVisible(false);
			searchButton.setEnabled(false);
			nonUserSearch.setEnabled(false);
		}
		else if (filterOnCB.getSelectedItem() == TechSearchFilter.CREATE)
		{
			submitNewTech.setVisible(true);
			initEmployeeComps();
		}
		else if (filterOnCB.getSelectedItem() == TechSearchFilter.SEARCH)
		{
			searchButton.setBackground(Color.WHITE);
			searchButton.setEnabled(true);
			nonUserSearch.setEnabled(true);
			submitNewTech.setVisible(false);
			
			String techIDstr = searchButton.getText();
			int techID = 0;
			
			if(techIDstr.length() > 0)
			{
				techID = Integer.parseInt(techIDstr);
				initializeInformationForEmployee(HelpDeskSystem.getInstance().getTechByID(techID));
			}
		}

		return techToReturn;
	}
	
	/**
	 * Initializes all the components that would show on the panel
	 * for any employee.
	 */
	private void initEmployeeComps()
	{
		lrPanel.removeAll();
		leftPanel.removeAll();
		rightPanel.removeAll();
		mainPanel.removeAll();
		submitPanel.removeAll();	
		
		idLabel = new JLabel("Employee ID (integer): ");
		idTF = new JTextField(15);

		fnLabel = new JLabel("First Name: ");
		fnTF = new JTextField(15);

		lnLabel = new JLabel("Last Name: ");
		lnTF = new JTextField(15);
		
		deptLabel = new JLabel("Department: ");
		deptTF = new JTextField(15);
		deptTF.setEnabled(false);
		deptTF.setText("Help Desk");

		emailLabel = new JLabel("Email: ");
		emailTF = new JTextField(15);

		supLabel = new JLabel("Supervisor ID: ");
		supIDTF = new JTextField(15);
		
		phoneLabel = new JLabel("Phone: ");
		phoneTF = new JTextField(15);
		
		hireLabel = new JLabel("Hire Date: ");
		hireTF = new JTextField(15);
		
		leftPanel.setBorder(BorderFactory.createTitledBorder("Employee Info"));
		
		leftPanel.add(idLabel);
		leftPanel.add(idTF);
		
		leftPanel.add(fnLabel);
		leftPanel.add(fnTF);

		leftPanel.add(lnLabel);
		leftPanel.add(lnTF);

		leftPanel.add(hireLabel);
		leftPanel.add(hireTF);

		rightPanel.setBorder(BorderFactory.createTitledBorder("Contact Info"));
		rightPanel.add(emailLabel);
		rightPanel.add(emailTF);
		
		rightPanel.add(supLabel);
		rightPanel.add(supIDTF);
		
		rightPanel.add(phoneLabel);
		rightPanel.add(phoneTF);

		rightPanel.add(deptLabel);
		rightPanel.add(deptTF);
	
		lrPanel.add(leftPanel);
		lrPanel.add(rightPanel);
		
		mainPanel.add(lrPanel);
		submitPanel.add(submitNewTech);		
		mainPanel.add(submitPanel);
		
		
		lrPanel.setVisible(true);
		
			
	}
	
	/**
	 * Creates a new Employee/Technician/Supervisor based on
	 * the values contained in the UserProfilePanel's components
	 * and the role field of the UserProfilePanel. 
	 * @return Newly created Employee (User/Technician/Supervisor)
	 */
	public void createNewEmployee() {
		
		Technician techHold = null;
		
		//The try catch block notifies the user if any invalid inputs
		//are on the Panel:  empID and supID should be integers and 
		// and all other fields (phone, email, etc) have been filled out.
		try {
				techHold = new Technician(Integer.parseInt(idTF.getText()), 
						this.fnTF.getText(),
						this.lnTF.getText(),
						this.hireTF.getText(),
						this.phoneTF.getText(),
						this.emailTF.getText(),
						Integer.parseInt(this.supIDTF.getText()),
						false,
						false);
				HelpDeskSystem.getInstance().addTechnician(techHold);
				
		}
		catch(Exception ex)
		{
			JOptionPane.showMessageDialog(null, "Enter an integer for any ID fields. " +
						"All other fields may not be blank.");
			
		}	
	}
	
	private void initializeInformationForEmployee(Technician emp)
	{
		lrPanel.removeAll();
		leftPanel.removeAll();
		rightPanel.removeAll();
		mainPanel.removeAll();
		submitPanel.removeAll();	
		
		idTF.setText("" + emp.getEmpID());
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
		
		supIDTF.setText("" + emp.getSuperID());
		supIDTF.setEnabled(false);
	
		activeRB.setSelected(emp.isActive());
		inActiveRB.setSelected(emp.isActive());
		activeRB.setEnabled(false);  
		inActiveRB.setEnabled(false);

		leftPanel.setBorder(BorderFactory.createTitledBorder("Employee Info"));
		
		leftPanel.add(idLabel);
		leftPanel.add(idTF);
		
		leftPanel.add(fnLabel);
		leftPanel.add(fnTF);

		leftPanel.add(lnLabel);
		leftPanel.add(lnTF);

		leftPanel.add(hireLabel);
		leftPanel.add(hireTF);

		rightPanel.setBorder(BorderFactory.createTitledBorder("Contact Info"));
		rightPanel.add(emailLabel);
		rightPanel.add(emailTF);
		
		rightPanel.add(supLabel);
		rightPanel.add(supIDTF);
		
		rightPanel.add(phoneLabel);
		rightPanel.add(phoneTF);

		rightPanel.add(deptLabel);
		rightPanel.add(deptTF);
	
		lrPanel.add(leftPanel);
		lrPanel.add(rightPanel);
		
		mainPanel.add(lrPanel);
		submitPanel.add(submitNewTech);		
		mainPanel.add(submitPanel);
		
		activePanel.add(activeRB);
		activePanel.add(inActiveRB);
		mainPanel.add(activePanel);
		
		lrPanel.setVisible(true);
		}
	
}
