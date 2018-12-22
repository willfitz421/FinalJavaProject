package GUIComponents;
import java.awt.*;

import javax.swing.*;

import Tickets.Ticket;
import Users.Employee;

/**
 * An abstract class for displaying the information on a ticket in these
 * intended cases:<br>
 * 
 * (a) getting info from a user when creating a ticket, <br> 
 * (b) letting the user view information of an already created ticket, <br>
 * (c) letting a tech view/modify the contents of a ticket (even the tech note)
 *     or reassign the ticket.<br><br>
 *  
 *  This class is abstract so that children (like the SWTicketDisplayPanel which is already
 *  included in the base phase 3 code) can initialize a special panel for 
 *  any additional ticket information required by a HW/SW ticket.  Hence, 
 *  child classes should make use of the "specialPanel" field.   <br><br>
 *  
 *  Hint:  The phase 3 base code accomplishes tasks (a) and (b) for SW tickets.  This is
 *  done by the child class SWTicketDisplayPanel.  Students should create a HWTicketDisplayPanel
 *  to accomplish the rest of tasks (a) and (b).  In fact, that would be the easiest way to 
 *  begin as it will help you learn the code.<br><br>
 *  
 *  Also,  students will have to add all functionality for task (c), meaning, will have to 
 *  add functionality to allow a tech to modify/reassign a ticket or to see tech notes.  
 *  So students/coders could either incorporate extra methods to update this panel for task (c)
 *  or if needed extend this Panel for task (c).  If you think another class entirely distinct from
 *  this one is needed then talk to me and we'll decide together.  Don't reinvent the wheel. ;)
 * 
 * @author ryank
 *
 */
public abstract class TicketDisplayPanel extends JPanel {

	private static final long serialVersionUID = 1L; //for serialization - ignore
	
	//The employee using the panel (so the ticket creator or tech updating it)
	protected Employee employeeUsingPanel;
	protected LogonRole empUsingPanelRole;

	private TicketCreatorPanel ticketCreatorPanel; //upper right hand corner box
												   //for displaying ticket creator info -
												   //private because this info never
												   //needs to be changed.
	
	protected TicketBasicInfoPanel ticketBasicPanel; //upper left hand corner box for 
													 //displaying basic ticket info. 
													 //We need to set/get information on this panel
													 //when modifying/creating tickets, specifically, 
													 //The phase 3 base code needed the 
													 //status/priority that the user
													 //selected when opening a ticket.  
													 //You might need this info too or you 
													 //might need other info from it.
													 
	//Top Panel just groups the two above panels next to each other on a panel.
	private JPanel topPanel; 
	
	//Incident Description Components
	private JPanel incDescrPanel;
	protected JTextArea incDescrTA; //text area
	private JScrollPane incDescrSP; //scroll pane - gotta put a text area in a scroll pane 
									//if you want scroll bars
	
	//default words for a text area when submitting a new ticket
	protected static final String DEFAULT_INC_STR = "Type Incidence Decription Here";
	
	//Special Panel for SW and HW Info
	//Hint:  The SWTicketDisplayPanel sets the specialPanel up with software details.
	//Students should create a HWTicketDisplay panel class in a similar fashion
	//and set up this special panel for the HW needs.
	protected JPanel specialPanel; 

	//Comps for a panel with a submit button
	//HINT:  This submit button is used to create
	//a new SW ticket in the phase 3 base code but can be used for modifying a ticket
	//if new methods are smartly provided.
	protected JButton submitBtn;
	protected JButton submitUpdate;
	protected JButton submitTech;
	protected JButton closeBtn;
	private JPanel submitPanel;
	
	//Update Notes Components - Can opt to exclude if needed via the needUpdateNotesPanel
	//flag of the initComps method.
	protected JPanel updateNotesPanel;
	protected JTextArea updateNotesTA; //text area
	private JScrollPane updateNotesSP;//scroll pane - gotta put a text area in a scroll pane 
									//if you want scroll bars
	protected JPanel techNotesPanel;
	protected JTextArea techNotesTA; //text area
	private JScrollPane techNotesSP;

	
	/**
	 * The constructor to use if initializing the panel for SUBMITTING A NEW TICKET
	 * @param creator The employee creating the ticket.
	 * @param role The role of the employee (should be user if all is well)
	 */
	public  TicketDisplayPanel(Employee creator, LogonRole role)
	{
		this.employeeUsingPanel = creator;
		this.empUsingPanelRole = role;
		
		//We need a submit button to submit a new ticket so
		//we'll flag this to the initComps.
		boolean needSubmitPanel = true;
		
		//Don't include update notes - they don't exist
		//at ticket creation.
		boolean showUpdates = false;
		boolean showTech = false;
		
		initComps(needSubmitPanel, showUpdates, showTech);
	}

	/***
	 * The constructor to use if initializing the panel to display 
	 * an already created ticket's information.  If the loggedInEmp
	 * is the creator of the ticket, then this the TicketDisplayPanel
	 * will not be editable as users should only be able to view
	 * (but not modify) the contents. 
	 * @param ticket  The ticket whose info should be displayed.
	 * @param loggedInEmp  The employee logged in
	 * @param role  The role of the employee
	 */
	public  TicketDisplayPanel(Ticket ticket, Employee loggedInEmp, LogonRole role)
	{
		this.employeeUsingPanel = loggedInEmp;
		this.empUsingPanelRole = role;
		
		//If the person logged in is the creator of the ticket, 
		//then we are letting a user view the ticket contents and 
		//the user should not be able to change anything so
		//set isEditable to false and needSubmitButton is false.
		boolean isEditable = true;
		boolean needSubmitPanel = true;
		boolean needTechPanel = true;
		
		if (empUsingPanelRole == LogonRole.USER)
		{
			isEditable = false;
			needSubmitPanel = false;
			needTechPanel = false;
		}
		
		//init the comps and show updates - since a ticket was passed in, 
		//there are update notes to show.
		boolean showUpdates = true;
		boolean showTech = true;
		
		if(empUsingPanelRole == LogonRole.USER)
			showTech = false;
		
		initComps(needSubmitPanel, showUpdates, showTech);
		
		//set the comps up for displaying an old ticket's info and possibly editing this info
		if(empUsingPanelRole == LogonRole.USER)
			setUpCompsForViewingOldTicketsUser(ticket, isEditable);
		else
			setUpCompsForViewingOldTicketsTech(ticket, isEditable);
	}
	
	/**
	 * Initializes the components.  We don't always need a submit button on the panel. 
	 * For example, a user may just wish to view the information.  We also
	 * don't always want an update panel
	 * @param needSubmitPanel  If true, include the submit button panel.
	 * @param needUpdateNotesPanel If true, an updates notes panel is included
	 */
	private void initComps(boolean needSubmitPanel, boolean needUpdateNotesPanel,
			boolean needTechNotesPanel)
	{
		setLayout(new GridLayout(4,1,5,5));
		
		//Set the pretty disabled creator information panel - 
		//The components are always disabled because you can't
		//change the ticket creator info
		ticketCreatorPanel = new TicketCreatorPanel(employeeUsingPanel);
		
		//Create the basic ticket panel - Here we use the constructor
		//that takes in no information about a ticket, so if using the panel
		//for something other than ticket creation, call
		//ticketBasicPanel.setTicketInfo(ticket) at some method called after initComps().
		ticketBasicPanel = new TicketBasicInfoPanel();
		
		//Group the left upper corner basic ticket info and 
		//creator info side by side on another panel.
		//This is purely for formatting.
		topPanel = new JPanel(new GridLayout(1,2));
		topPanel.add(ticketBasicPanel);
		topPanel.add(ticketCreatorPanel);
		
		add(topPanel);
		
		//Set up incidence description text areas and scroll pane.
		//By default, the text area says "Enter Incidence Here". 
		//Change the text in some later method called after initComps() if this is
		//not desired.
		incDescrPanel = new JPanel(new BorderLayout());
		incDescrTA = new JTextArea(DEFAULT_INC_STR);
		incDescrTA.setLineWrap(true);
		incDescrSP = new JScrollPane(incDescrTA);	
		incDescrSP.setPreferredSize(new Dimension(1000,100));
		incDescrSP.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		incDescrPanel.add(incDescrSP, BorderLayout.CENTER);
		incDescrPanel.setBorder(BorderFactory.createTitledBorder("Incidence Description"));
		
		add(incDescrPanel);
		
		//if an update notes panel is needed, add it.
		if (needUpdateNotesPanel)
		{
			updateNotesPanel = new JPanel(new BorderLayout());
			updateNotesTA = new JTextArea();	
			updateNotesTA.setLineWrap(true);
			updateNotesSP = new JScrollPane(updateNotesTA);
			updateNotesSP.setPreferredSize(new Dimension(1000,100));
			updateNotesSP.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
			updateNotesPanel.add(updateNotesSP, BorderLayout.CENTER);
			updateNotesPanel.setBorder(BorderFactory.createTitledBorder("Update Notes"));

			add(updateNotesPanel);
			
		}
		
		if (needTechNotesPanel)
		{
			techNotesPanel = new JPanel(new BorderLayout());
			techNotesTA = new JTextArea();	
			techNotesTA.setLineWrap(true);
			techNotesSP = new JScrollPane(techNotesTA);
			techNotesSP.setPreferredSize(new Dimension(1000,100));
			techNotesSP.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
			techNotesPanel.add(techNotesSP, BorderLayout.CENTER);
			techNotesPanel.setBorder(BorderFactory.createTitledBorder("Tech Notes"));

			add(techNotesPanel);
			
			
		}
		
		//HINT: Here you might want an update tech notes panel here if it's needed.

		
		//Make a call to the child class  to set up the special panel
		//and to set up the SubmitButton.
		//This way any components for HW/SW can be taken care of by the child.
		//Child components are expected to add this specialPanel to the 
		//parent Panel.
		initSpecialComps();
		
		//Finally, if we need a submitPanel, add it and set up the processing for the button.
		if (needSubmitPanel)
		{
			submitBtn = new JButton("Submit");
			submitPanel = new JPanel();
			submitPanel.add(submitBtn);
			
			
			if(needUpdateNotesPanel)
			{
				submitUpdate = new JButton("Update Notes Submit");
				submitPanel.add(submitUpdate);
			}
			if(needTechNotesPanel)
			{
				submitTech = new JButton("Tech Notes Submit");
				submitPanel.add(submitTech);
				closeBtn = new JButton("Close Ticket");
				submitPanel.add(closeBtn);
			}
			
			add(submitPanel);
			
			//The child classes are expected to attach an action listener
			//to the submit button. Example:  The button should submit a new SW ticket 
			//when the SWTicketDisplayPanel
			//is used to create a new SW ticket.
			setUpSubmitButton();
			
			//If no technicians are in the system yet, disable the submit button.
			//Don't let any tickets be submitted or modified until techs exist.  
			//(This decision allowed Dr. R to let the first technician in the list
			//be the owner of any newly submitted ticket.)
			if (HelpDeskSystem.getInstance().getTechnicians().size() == 0)
			{
				submitBtn.setEnabled(false);
				submitBtn.setToolTipText("No Technicians in System.  Technicians must exist before "
						+" Tickets can be created.");
			}
		}

	}
	
	/***
	 * Initializes components for displaying the contents of an already created ticket
	 * (and possibly for editing if isEditable is set to true but this would be up to
	 * students to test and check and use strategically).
	 * @param ticket The ticket whose information is to be viewed.
	 * @param isEditable If true, the editable components should be disabled.
	 */
	private void setUpCompsForViewingOldTicketsTech(Ticket ticket, boolean isEditable)
	{
		//Set the ticket basics panel to show id/status/etc.
		//There is no need to make any changes to the 
		//ticketCreatorPanel as the ticket's creator info
		//is always populated and never editable.
		ticketBasicPanel.setTicketInfo(ticket);
		
		incDescrTA.setText(ticket.getIncidence());
		updateNotesTA.setText(ticket.getUpdateNotesString());
		techNotesTA.setText(ticket.getTechNotesString());
		
		if(!ticket.isOpen())
		{
			
			incDescrTA.setEnabled(false);

			updateNotesTA.setEnabled(false);

			techNotesTA.setEnabled(false);

			//Let the child class set up special components for viewing a ticket.
			//and let them know if the desire is to only make the components viewable.
			setSpecialCompsForViewingOldTickets(ticket, isEditable);

			//If the ticket's information should only be viewed (and not modified)
			//then disable the needed components.
			ticketBasicPanel.disableAll();
		}
		else
		{
			//Show the incidence info and disable it - it can never be changed.

			incDescrTA.setEnabled(false);

			updateNotesTA.setEnabled(false);

			techNotesTA.setEnabled(false);

			//Let the child class set up special components for viewing a ticket.
			//and let them know if the desire is to only make the components viewable.
			setSpecialCompsForViewingOldTickets(ticket, isEditable);

			//If the ticket's information should only be viewed (and not modified)
			//then disable the needed components.
		}
	}
	
	private void setUpCompsForViewingOldTicketsUser(Ticket ticket, boolean isEditable)
	{
		//Set the ticket basics panel to show id/status/etc.
		//There is no need to make any changes to the 
		//ticketCreatorPanel as the ticket's creator info
		//is always populated and never editable.
		ticketBasicPanel.setTicketInfo(ticket);
		
		//Show the incidence info and disable it - it can never be changed.
		incDescrTA.setText(ticket.getIncidence());
		incDescrTA.setEnabled(false);
		
		updateNotesTA.setText(ticket.getUpdateNotesString());
		updateNotesTA.setEnabled(false);
		
		
		//Let the child class set up special components for viewing a ticket.
		//and let them know if the desire is to only make the components viewable.
		setSpecialCompsForViewingOldTickets(ticket, isEditable);
		
		//If the ticket's information should only be viewed (and not modified)
		//then disable the needed components.
		ticketBasicPanel.disableAll();
		

	}
	
	/**
	 * Resets ticket entries to default values.  
	 * This was helpful when letting a user create new tickets.
	 * After doing so, the fields needed to be cleared so that 
	 * another ticket could be resubmitted.
	 */
	public void resetInitialValues()
	{
		//There are no update notes when creating 
		//a ticket so we don't need to reset the
		//update notes text area.
		ticketBasicPanel.resetInitialValues();
		incDescrTA.setText(DEFAULT_INC_STR);
		resetSpecialInitialValues();
	}
	
	
	/***
	 * Child classes should implement initSpecialComps()
	 * so that it initializes the specialPanel as well as any child-specific components.
	 * The child class must add these special components to the special panel
	 * and also add the special panel to the parent panel by calling "add(specialPanel)".
	 */
	protected abstract void initSpecialComps();
	
	/***
	 * Child classes should implement setUpSubmitButton() so that it
	 * behaves according to the needs of the child.
	 */
	protected abstract void setUpSubmitButton();
	
	/***
	 * Initializes child-specific components for displaying the contents of an already created ticket
	 * (and possibly for editing if isEditable is set to true but this would be up to
	 * students to test and check and use strategically).
	 * @param ticket The ticket whose information is to be viewed.
	 * @param isEditable If true, the editable components should be disabled.
	 */
	protected abstract void setSpecialCompsForViewingOldTickets(Ticket ticket, boolean isEditable);
	
	/**
	 * Disables all child-specific components so that they are not editable.
	 */
	protected abstract void disableSpecialComps();	
	
	/**
	 * Resets child-specific ticket entries to default values - called by resetInitialValues().  
	 */
	protected abstract void resetSpecialInitialValues();
}
