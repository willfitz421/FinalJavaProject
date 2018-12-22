package GUIComponents;
import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import Database.DBHelper;
import Tickets.*;
import Users.*;

/**
 * The HelpDeskSystem - Follows a Singleton Design.
 * 
 * @author ryank
 *
 */
public class HelpDeskSystem extends JFrame{

	private static final long serialVersionUID = 1L; //for serialization - ignore
	
	//The ticket/tech/user lists.
	private ArrayList<Ticket> ticketList;
	private ArrayList<Technician> techList;
	private ArrayList<Employee> userList;	
	
	//Employee who is logged in - could be a user/tech/supervisor.
	private LogonRole logonRole;
	private Employee logonEmployee;
	
	//Tabbed Components
	private JTabbedPane mainSystemPanel;
	
	//Everyone can view their profile (but not change it)
	private UserProfilePanel userProfileTab;

	//Users can submit a HW/SW Ticket
	private SWTicketDisplayPanel swSubmissionTab;
	private HWTicketDisplayPanel hwSubmissionTab;
	private TicketSearchPanel searchSubmittedTicketsTab;
	
	//Technician Tabs
	
	//Supervisor Tabs
	private ManegmentTab managmentTab;
	
	//The unique instance of the HelpDesk System
	private static HelpDeskSystem helpDesk;

	/**
	 * The HelpDesk System is set up to be a "singleton" class, 
	 * meaning, it can only be created once.  
	 * @return  The Unique Instance of the HelpDeskSystem
	 */
	protected static HelpDeskSystem getInstance()
	{
		if (helpDesk == null)
			helpDesk = new HelpDeskSystem();
		return helpDesk;
	}
	
	/**
	 * A private HelpDesk constructor so that only
	 * one can ever be created.  Hence why this is a
	 * 'singleton' class.
	 */
	private HelpDeskSystem() 
	{
		
		setTitle("Welcome to the Help Desk System");

		this.setPreferredSize(new Dimension(1200, 800));
		
		//initialize array lists of techs, tickets, users
		initLists();
		
		//Setting up how to handle exiting the system -
		//The files must be saved at that point.
		initCloseOperation();
		
		//We do not set visible to true here - happens
		//at the end of initalizeComponentsForLogon so that
		//the components aren't shown until the logonEmployee
		//is set.
	}


	/**
	 * Retuns the tickets if needed<br>
	 * 
	 * HINT - How to call this method from an outside class:  
	 * 			HelpDeskSystem.getInstance().getTickets()
	 * 
	 * @return The Ticket list
	 */
	public ArrayList<Ticket> getTickets()
	{
		
		return ticketList;
	}

	/***
	 * Returns the technician list if needed
	 * 
	 * 
	 * HINT - How to call this method from an outside class:  
	 * 			HelpDeskSystem.getInstance().getTechnicians()
	 * 
	 * 
	 * @return Returns the Technician List
	 */
	public ArrayList<Technician> getTechnicians()
	{
		return techList;
	}

	/***
	 * Returns the user list if needed
	 * 
	 * 	 
	 * HINT - How to call this method from an outside class:  
	 * 			HelpDeskSystem.getInstance().getUsers()
	 * 
	 * @return Returns the User List
	 */
	public ArrayList<Employee> getUsers()
	{
		return userList;
	}

	/**
	 * Adds a ticket to the ticket list
	 * 
	 *  HINT - How to call this method from an outside class:  
	 * 			HelpDeskSystem.getInstance().addTicket(...)
	 * 
	 * @param ticket Ticket to be added
	 */
	public void addTicket(Ticket ticket)
	{
		ticketList.add(ticket);
	}

	/**
	 * Adds a technician to the Tech List
	 * 
	 * HINT - How to call this method from an outside class:  
	 * 			HelpDeskSystem.getInstance().addTechnician(...)
	 * 
	 * @param tech  Tech to be added
	 */
	public void addTechnician(Technician tech)
	{
		techList.add(tech);
	}

	/**
	 * Adds a user to the User list
	 * 
	 *  HINT - How to call this method from an outside class:  
	 * 			HelpDeskSystem.getInstance().addUser(...)
	 * 
	 * @param e Employee to be added
	 */
	public void addUser(Employee e)
	{
		userList.add(e);
	}
	
	/**
	 * Searches techList for a technician with a given ID.  
	 * Returns null if the tech is not found so callers
	 * must check of the returned object is null and
	 * deal with what to do if it is null.
	 * 
	 * HINT - How to call this method from an outside class:  
	 * 			HelpDeskSystem.getInstance().getTechByID(...)
	 * 
	 * 
	 * @param techID ID of tech to search for
	 * @return null (if the technician is not found) or the technician with the given ID.
	 */
	protected Technician getTechByID(int techID)
	{
		for(int n = 0; n < techList.size(); n++)
		{
			if(techList.get(n).getEmpID() == techID)
			{
				return techList.get(n);
			}
		}
		return null;
	}

	/**
	 * Searches ticketList for a technician with a given ID.
	 * Returns null if the ticket is not found so callers
	 * must check of the returned object is null and
	 * deal with what to do if it is null.
	 * HINT - How to call this method from an outside class:  
	 * 			HelpDeskSystem.getInstance().getTicketByID(...)
	 * 
	 * @param ticketID id of ticket to search for
	 * @return null (if the ticket is not found) or the ticket with the given ID.
	 */
	protected  Ticket getTicketByID(int ticketID)
	{
		for(int n = 0; n < ticketList.size(); n++)
		{
			if(ticketList.get(n).getTicketID() == ticketID)
			{
				return ticketList.get(n);
			}
		}
		return null;
	}

	/**
	 * Searches uesrList for a user with a given ID.
	 * Returns null if the user is not found so callers
	 * must check of the returned object is null and
	 * deal with what to do if it is null.
	 * 
	 * HINT - How to call this method from an outside class:  
	 * 			HelpDeskSystem.getInstance().getUserByID(...)
	 * 
	 * @param empID id of user to search for
	 * @return Returns null (if the user is not found) or the user with the given ID.
	 */
	protected  Employee getUserByID(int empID)
	{
		for(int n = 0; n < userList.size(); n++)
		{
			if(userList.get(n).getEmpID() == empID)
			{
				return userList.get(n);
			}
		}
		return null;
	}

	/**
	 *  This method initialize components of the System based on the
	 *  logon credentials. This method also sets the logonEmployee and 
	 *  logonRole field information.
	 *  
	 *  HINT:  To add more panels/tabs, you need to add code to this method.
	 *  
	 * @param loggedInEmp  The Employee who logged on.
	 * @param role  The LogonRole of the Employee who logged on.
	 */
	protected void initializeCompsForLogon(Employee loggedInEmp, LogonRole role)
	{
		logonEmployee = loggedInEmp;
		logonRole = role;
		
		//The tabbed panel to which we add individual tabs
		mainSystemPanel = new JTabbedPane();
		
		//When new tabs are selected, we may have to reset some
		//values. Example: After submitting a ticket, the
		//values of the submitted ticket must be cleared
		//when the user leaves the tab so that new tickets
		//can be submitted.
		mainSystemPanel.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent e) {
            
	            JTabbedPane pane = (JTabbedPane) e.getSource();
	            
	            if (logonRole == LogonRole.USER)
	            {
	            	//For a user the second pane (index 1) is the 
	            	//swSubmissionTab and it should be refreshed 
	            	//in case the user submits more than one ticket.
	            	if(pane.getSelectedIndex() == 1)
	            	{
	            		swSubmissionTab.resetInitialValues();
	            	}
	            }
			}
			
		});
		
		//all users can view their profile information so always 
		//add a userProfileTab
		userProfileTab = new UserProfilePanel(logonEmployee, logonRole);
		mainSystemPanel.add("View Profile", userProfileTab);
		
		//If the logon is a user, we need a tab to submit sw tickets
		//and to view submitted tickets.
		//HINT: WIll need to add a hwSubmission tab for a user.
		//HINT:  Add else-if's to deal with what tabs
        //should show for other logons
		if (logonRole == LogonRole.USER)
		{
			//add the tab to submit a SW ticket
			swSubmissionTab = new SWTicketDisplayPanel(logonEmployee, logonRole);
			mainSystemPanel.add("Submit SW Ticket", swSubmissionTab);	
			
			
			//HINT:  Add a hwSubmission tab here.
			hwSubmissionTab = new HWTicketDisplayPanel(logonEmployee, logonRole);
			mainSystemPanel.add("Submit HW Ticket", hwSubmissionTab);
			
			//Add a tab to view submitted tickets.
		    searchSubmittedTicketsTab = new TicketSearchPanel(logonEmployee, logonRole);
		    mainSystemPanel.addTab("View Created Tickets", searchSubmittedTicketsTab);
		    
		}
		else if(logonRole == LogonRole.TECHNICIAN)
		{
			searchSubmittedTicketsTab = new TicketSearchPanel(logonEmployee, logonRole);
			mainSystemPanel.addTab("View Created Tickets", searchSubmittedTicketsTab);
			
		}
		else if(logonRole == LogonRole.SUPERVISOR)
		{
			searchSubmittedTicketsTab = new TicketSearchPanel(logonEmployee, logonRole);
			mainSystemPanel.addTab("View Created Tickets", searchSubmittedTicketsTab);
			
			managmentTab = new ManegmentTab(logonEmployee, logonRole);
			mainSystemPanel.addTab("Managment", managmentTab);
			
			
		}
		
		//Add the mainSystemPanel to the HelpDeskSystem JFrame
		add (mainSystemPanel);
		
		pack();
		
		setVisible(true);
	}
	
	
	/**
	 * Method to initialize the DB Helper Lists.
	 */
	private void initLists()
	{
		//Get the list of tickets and technicians list from DBHelper
		//We can't just use these because we need all the references to
		//work out nicely.  So if I change a ticket in the list of
		//a technician, it has to change in the list of tickets in
		//the help desk system
		ArrayList<Ticket> ticketDBList = DBHelper.readInTickets();
		ArrayList<Technician> techDBList = DBHelper.readInTechnicians();
		ArrayList<Employee> userDBList = DBHelper.readInUsers();
		
		//The list of tickets/techs/users that will be housed in the HelpDeskSystem till exiting the system.
		ticketList = new ArrayList<Ticket>();
		techList = new ArrayList<Technician>();
		userList = new ArrayList<Employee>();
		
		//Create new objects for all technicians in the DB
		for (int i = 0; i < techDBList.size(); i++)
		{
			Technician tech = new Technician(techDBList.get(i).getEmpID(), 
					techDBList.get(i).getFirstName(), 
					techDBList.get(i).getLastName(), 
					techDBList.get(i).getHireDate(), 
					techDBList.get(i).getPhoneNum(), 
					techDBList.get(i).getEmailAdd(), 
					techDBList.get(i).getSuperID(), 
					techDBList.get(i).isActive(),
					techDBList.get(i).isSupervisor());
			
			techList.add(tech);
		}

		//Create new objects for all users in the DB
		for (int i = 0; i < userDBList.size(); i++)
		{
			Employee emp = new Employee(userDBList.get(i).getEmpID(), 
					userDBList.get(i).getFirstName(), 
					userDBList.get(i).getLastName(), 
					userDBList.get(i).getHireDate(), 
					userDBList.get(i).getPhoneNum(), 
					userDBList.get(i).getEmailAdd(),
					userDBList.get(i).getDepartment());
			
			userList.add(emp);
		}
		
		//Create new ticket objects and connect the new tech/user objects to new ticket objects 
		for (int i = 0; i < ticketDBList.size(); i++)
		{
			
			Employee c = this.getUserByID(ticketDBList.get(i).getCreator().getEmpID());
			Technician tech = this.getTechByID(ticketDBList.get(i).getOwner().getEmpID());
			
			Ticket t;
			
			if (ticketDBList.get(i) instanceof SoftwareTicket)
			{
				t = new SoftwareTicket(ticketDBList.get(i).getTicketID(),
					c, 
					ticketDBList.get(i).getIncidence(), 
					ticketDBList.get(i).getPriority(), 
					ticketDBList.get(i).getImpact(), 
					tech,
					((SoftwareTicket) ticketDBList.get(i)).getSoftwareType(), 
					((SoftwareTicket) ticketDBList.get(i)).getOperatingSystem(), 
					((SoftwareTicket) ticketDBList.get(i)).getBits(),
					ticketDBList.get(i).getDateOpened(),
					ticketDBList.get(i).getDateClosed(), 
					ticketDBList.get(i).getClosingNotes(),
					ticketDBList.get(i).isOpen(),
					ticketDBList.get(i).getTechNotesArrayList(),
					ticketDBList.get(i).getUpdateNotesArrayList());
					
				ticketList.add(t);
			}
			else if(ticketDBList.get(i) instanceof HardwareTicket)
			{
				t = new HardwareTicket(ticketDBList.get(i).getTicketID(),
						c, 
						ticketDBList.get(i).getIncidence(), 
						ticketDBList.get(i).getPriority(), 
						ticketDBList.get(i).getImpact(), 
						tech,
						((HardwareTicket) ticketDBList.get(i)).getHardwareType(),
						((HardwareTicket) ticketDBList.get(i)).getManufacturer(),
						((HardwareTicket) ticketDBList.get(i)).getProductNumber(),
						((HardwareTicket) ticketDBList.get(i)).getSerialNumber(),
						ticketDBList.get(i).getDateOpened(),
						ticketDBList.get(i).getDateClosed(), 
						ticketDBList.get(i).getClosingNotes(),
						ticketDBList.get(i).isOpen(),
						ticketDBList.get(i).getTechNotesArrayList(),
						ticketDBList.get(i).getUpdateNotesArrayList());
				
				ticketList.add(t);
			}			

		}

		/*Technician tech = new Technician(12, 
				"Will", 
				"APple", 
				"11/22/12", 
				"997-234-2432", 
				"willsapple.com", 
				213, 
				true,
				false);
		
		techList.add(tech);*/
	}
	
	/**
	 * Save to the DB when Helpdesk is closed.
	 */
	private void initCloseOperation()
	{	
		addWindowListener(new WindowAdapter()
		{
		    public void windowClosing(WindowEvent e)
		    {
				//Overwrites the Ticket and Technician files with the new information
				DBHelper.writeTechnicianFile(techList);
				DBHelper.writeTicketFile(ticketList);
				DBHelper.writeUserFile(userList);
				System.out.println("Saved Files.  Exiting System.");
				System.exit(0);
		    }
		});				
	}
	


	/**
	 * Typical toString method.
	 */
	public String toString()
	{
		String ls = System.lineSeparator();
		
		String logonRoleStr;
		
		if(logonRole != null)
		{ 
			logonRoleStr = logonRole.toString();
		}
		else
		{
			logonRoleStr = "logon role not set yet"; 
		}

		String logonEmpStr;
		
		if(this.logonEmployee != null)
		{ 
			logonEmpStr = logonEmployee.toString();
		}
		else
		{
			logonEmpStr = "logon employee not set yet"; 
		}
		
		
		String s = "************Logon Info:************" + ls;
		s+= "Logon Role: " + logonRoleStr + ls;
		s+=  logonEmpStr + ls;
		
		
	    s += ls + "************Users:************" + ls;
		for(int i=0; i < userList.size(); i++)
		{
			s += userList.get(i);
			s += ls + ls;
		}
		
		s += ls + "*************Technicians:************" + ls;
		for(int i=0; i < techList.size(); i++)
		{
			s += techList.get(i);
			s += ls + ls;
		}
		
		s += ls+ "************Tickets:******************" + ls;
		for(int i=0; i < ticketList.size(); i++)
		{
			s += ticketList.get(i);
			s += ls + ls;
		}
		
		return s;
	}
}
