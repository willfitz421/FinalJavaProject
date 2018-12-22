package GUIComponents;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;
import javax.swing.event.*;
import Tickets.*;
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
public class TicketSearchPanel extends JPanel {

	private static final long serialVersionUID = 1L; //for serialization - ignore
	
	//The employee using the panel (so the ticket creator or tech updating it)
	protected Employee empUsingPanel;
	protected LogonRole empUsingPanelRole;
	
	//for ease including a nothing Selected Panel
	private JLabel nothingSelectedPanel;
			
	private ArrayList<Ticket> filteredTickets;  //A filtered set of tickets (or all tickets
												//to display if no filter is on).
	
	private String[] filteredTicketIdsAsStrings; //The lists of the IDs (as strings) of
												 //the filteredTickets for ease with 
												 //using JLists.
	
	private JList<String> filteredTicketsJList; //A JList of the ticketIDs.  A JList just lets you 
												//list a variable set of items so that when you select them
												//something happens.
											    //FYI:  To figure out which ticket has been selected get the
												//index in the JList that has been selected and retrieve
												//the corresponding ticket from filteredTickets.  EX:
												//int selectedIndex = filteredTicketsJList.getSelectedIndex();
												//Ticket selectedTicket = filteredTickets.get(selectedIndex);
												
	private JScrollPane filteredTicketSP; //A scrollable pane to display the ticket lists
	
	private SWTicketDisplayPanel swTicketDisplayPanel; //A SWTicketDisplayPanel to use to display a SW Ticket
													   //HINT: It might help you to include a HWTicketDisplayPanel too
	
	private HWTicketDisplayPanel hwTicketDisplayPanel;
	
	
	private JSplitPane splitPane;  //A Split Pane so a list can be on the left side
									//and the ticket info on the right.
	
	//Top Panel which houses Refresh button and filter components
	private JPanel topPanel;
	private JPanel helperTopPanel;	//for esthetics
	private JButton refreshButton;
	private JLabel filterOnLabel;
	private JComboBox<TicketSearchFilters> filterOnCB;
	private JLabel searchLbl;
	private JTextField searchButton;
	private JButton nonUserSearch;
	private int textFieldInput;
	
	
	/**
	 * This constructor assume the Employee is a user.
	 * @param emp  The Employee logged in
	 * @param role The role of the employee logged in
	 */
	TicketSearchPanel(Employee emp, LogonRole role)
	{
		empUsingPanel = emp;
		empUsingPanelRole = role;
		
		initComps();	
	}
	
	private void initComps()
	{
		setLayout(new BorderLayout());
			
		//Split Pane will house a list on the left
		//and a TicketDisplayPanel on the right.
		splitPane = new JSplitPane();
		
		//Until the user selects something, have the right
		//of the pane just say "No tickets selected."
		nothingSelectedPanel =  new JLabel("No tickets Selected");
		nothingSelectedPanel.setHorizontalAlignment(SwingConstants.CENTER);
		splitPane.setRightComponent(nothingSelectedPanel);
		splitPane.setResizeWeight(0.15);
		splitPane.setDividerLocation(.15);

		//Refresh Button -> If changes are made to the tickets in the 
		//list, this will reload the list so that the changes are visible
		//after selection.
		refreshButton = new JButton("Refresh");
		
		//When we need to refresh the list, set 
		//filter to none and refresh so all are visible again.
		refreshButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				filterOnCB.setSelectedItem(TicketSearchFilters.NONE);
				initLists();
				if(empUsingPanelRole != LogonRole.USER)
				{
				splitPane.setRightComponent(nothingSelectedPanel);
				searchButton.setBackground(Color.GRAY);
				searchButton.setText("");
				searchButton.setEnabled(false);
				nonUserSearch.setEnabled(false);
				}
			}
			
		});
	
		helperTopPanel = new JPanel();
		
		
		filterOnLabel = new JLabel("       Filter By:"); //Spacing weird to push the filter options farther 
														 //to the right from the Refresh Button

		//Now set up what can be filtered on.
		//First get the filters for the appropriate role (User/Tech).
		//HINT: For technicians/supervisors need to 
		//update the if-statement in TicketSearchFilters.getFiltersForRole...
		//If you don't, appropriateFilters will be null.		
		TicketSearchFilters[] appropriateFilters = TicketSearchFilters.getFiltersForRole(empUsingPanelRole);
		
		//Set the combo bbox to contain the filters and autoselect "None" at start.
		filterOnCB = new JComboBox<TicketSearchFilters>(appropriateFilters);
		filterOnCB.setSelectedItem(TicketSearchFilters.NONE);
		
		//When someone selects a filter, filter accordingly.
		filterOnCB.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {	
				
				//When someone selects a filter, filter accordingly, ie, 
				//re-initialzize the lists with the filtered tickets
				//and put the nothing selected panel on the right side of the pane again.
				initLists();	
				splitPane.setRightComponent(nothingSelectedPanel);
			}
			
		});
		
		//Add the filter comps and refresh button to the helper panel.
		helperTopPanel.add(refreshButton);
		
		helperTopPanel.add(filterOnLabel);
		helperTopPanel.add(filterOnCB);
		
		//add helper Panel to top panel for esthetics
		topPanel = new JPanel();		
		topPanel.add(helperTopPanel);
		
		//All Comps are set - ok to initalize lists.
		
		//Set up tap panel (refresh/filter) and the split Pane.
		add(topPanel, BorderLayout.NORTH);
		add(splitPane, BorderLayout.CENTER);
		
		if(empUsingPanelRole != LogonRole.USER)
		{
			searchLbl = new JLabel("ID: ");
			searchButton = new JTextField(10);
			nonUserSearch = new JButton("Search");
			searchButton.setBackground(Color.GRAY);
			searchButton.setEnabled(false);
			nonUserSearch.setEnabled(false);
			
			helperTopPanel.add(searchLbl);
			helperTopPanel.add(searchButton);
			helperTopPanel.add(nonUserSearch);
			
			nonUserSearch.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					
					//with refresh button, call init lists and check to see if text field is blank/full
					initLists();
				} 	 	
			});
		}
		
		initLists();
		
	}
	
	/**
	 * Initialize the filter lists.
	 */
	private void initLists()
	{	
		//filteredTickets will contain only those tickets that meet the filter requirements.
		//if(empUsingPanelRole == LogonRole.TECHNICIAN || empUsingPanelRole == LogonRole.SUPERVISOR)
			//filteredTickets = setFilters();
		//else

		filteredTickets = setFilters();
		
		//Get the strings of the filteredTickets.  
		//This is just becuase we need these strings to know what
		//to display on the left side of the split pane.  
		filteredTicketIdsAsStrings = new String[filteredTickets.size()];

		for (int i = 0; i < filteredTickets.size(); i++)
		{
			filteredTicketIdsAsStrings[i] = "" + filteredTickets.get(i).getTicketID();
		}

		//JList need to know what to display so give them the string of the
		//filtered ticket IDs.
		filteredTicketsJList = new JList<String>(filteredTicketIdsAsStrings);
		
		//Only allow one ticket to be selected.
		filteredTicketsJList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		//Allow 20 tickets to be visible before scrolling kicks in.
		filteredTicketsJList.setVisibleRowCount(20);
		
		//add the List to a scrollable pane in case the list gets long.
		filteredTicketSP = new JScrollPane(filteredTicketsJList);		
		
		//set the preferred size of the scrollable pane in case
		//that helps us achieve a nice width.
		filteredTicketSP.setPreferredSize(new Dimension(15,100));
		
		//Set the left component with the scrollable pane with the filtered tickets.
		splitPane.setLeftComponent(filteredTicketSP);
		
		//Set up the JList with a ListSelectionListener that Listens
		//for when soemthing is selected.
		filteredTicketsJList.addListSelectionListener(new ListSelectionListener() 
		{
			@Override
			public void valueChanged(ListSelectionEvent arg0) {

				//When a user selects a ticket ID from the list, figure out
				//which ticket index was selected and retrieve the 
				//ticket information corresponding to this index.
				int selectedIndex = filteredTicketsJList.getSelectedIndex();
				Ticket selectedTicket = filteredTickets.get(selectedIndex);

				//if this is a SW ticket, use the SWTicketDisplayPanel.
				//HINT - When you have the hwTicketDisplayPanel
				//coded, add an "else" statement.
				if (selectedTicket instanceof SoftwareTicket)
				{
					//Create a SWTicket Display and show it in the right of the split pane.
					swTicketDisplayPanel = new SWTicketDisplayPanel((SoftwareTicket) selectedTicket, 
																		empUsingPanel, empUsingPanelRole);
					
					splitPane.setRightComponent(swTicketDisplayPanel);
				}
				else if (selectedTicket instanceof HardwareTicket)
				{
					//Create a HWTicket Display and show it in the right of the split pane.
					hwTicketDisplayPanel = new HWTicketDisplayPanel((HardwareTicket) selectedTicket,
																empUsingPanel, empUsingPanelRole);
					
					splitPane.setRightComponent(hwTicketDisplayPanel);
				}
			}

		});
			
	}
	
	/**
	 * Determines which tickets  meet the filter requirements.
	 * @return The tickets that meet the filter requirements.
	 */
	private ArrayList<Ticket> setFilters()
	{
		//First set all possible tickets and whittle them down
		//to the ones that meet the filter and should be returned.
		ArrayList<Ticket> allPossibleTickets = new ArrayList<Ticket> ();
		ArrayList<Ticket> ticketsToReturn = new ArrayList<Ticket> ();
		ArrayList<Technician> allPossibleTech = new ArrayList<Technician>();
		
		//If the person logged in is a user, they can only see
		//their own tickets, but a tech could see anyone's.
		if (empUsingPanelRole == LogonRole.USER)
		{
			allPossibleTickets = empUsingPanel.getTickets();
		}
		else 
		{ 
			allPossibleTickets = HelpDeskSystem.getInstance().getTickets();
			allPossibleTech = HelpDeskSystem.getInstance().getTechnicians();
		}
		
		
		//We'll add to ticketsToReturn the ones from allPossibleTickets that match our
		//search filters.  
		//BE CAREFULL NEVER TO DELETE directly from allPossibleTickets 
		//because that could change the lists that later will be written to the DB.
		//Don't want to do that.  We just want to get the right ones to
		//view without deleting the wrong ones from the system.
		Ticket currentTicket;
		
		
		if (filterOnCB.getSelectedItem() == TicketSearchFilters.CLOSED)
		{
			if(empUsingPanelRole != LogonRole.USER)
			{
			searchButton.setBackground(Color.GRAY);
			searchButton.setEnabled(false);
			nonUserSearch.setEnabled(false);
			}
			for (int i = 0; i < allPossibleTickets.size(); i++)
			{
				currentTicket = allPossibleTickets.get(i);
				if (!currentTicket.isOpen())
				{
					ticketsToReturn.add(currentTicket);
				}
			}
		}
		else if (filterOnCB.getSelectedItem() == TicketSearchFilters.OPEN)
		{
			//Get all open ones
			if(empUsingPanelRole != LogonRole.USER)
			{
			searchButton.setBackground(Color.GRAY);
			searchButton.setEnabled(false);
			nonUserSearch.setEnabled(false);
			}
			for (int i = 0; i < allPossibleTickets.size(); i++)
			{
				currentTicket = allPossibleTickets.get(i);
				if (currentTicket.isOpen())
				{	
					ticketsToReturn.add(currentTicket);
				}
			}
		}
		else if (filterOnCB.getSelectedItem() == TicketSearchFilters.HARDWARE)
		{
			if(empUsingPanelRole != LogonRole.USER)
			{
			searchButton.setBackground(Color.GRAY);
			searchButton.setEnabled(false);
			nonUserSearch.setEnabled(false);
			}
			for (int i = 0; i < allPossibleTickets.size(); i++)
			{
				currentTicket = allPossibleTickets.get(i);
				if (currentTicket instanceof HardwareTicket)
				{
					ticketsToReturn.add(currentTicket);
				}
			}
		}
		else if(filterOnCB.getSelectedItem() ==  TicketSearchFilters.SOFTWARE)
		{
			if(empUsingPanelRole != LogonRole.USER)
			{
			searchButton.setBackground(Color.GRAY);
			searchButton.setEnabled(false);
			nonUserSearch.setEnabled(false);
			}
			for(int i = 0; i < allPossibleTickets.size(); i++)
			{
				currentTicket = allPossibleTickets.get(i);
				if(currentTicket instanceof SoftwareTicket)
				{
					ticketsToReturn.add(currentTicket);
				}
			}
		}
		else if(filterOnCB.getSelectedItem() == TicketSearchFilters.MY_TICKETS)
		{
			searchButton.setBackground(Color.GRAY);
			searchButton.setEnabled(false);
			nonUserSearch.setEnabled(false);
			
			for(int i = 0; i < allPossibleTickets.size(); i++)
			{
				currentTicket = allPossibleTickets.get(i);
				if(currentTicket.getOwner() == empUsingPanel)
				{
					ticketsToReturn.add(currentTicket);
				}
			}
		}
		//Need to make technicians if this is to work!!!
		else if(filterOnCB.getSelectedItem() == TicketSearchFilters.TECHNICIAN)
		{
			searchButton.setBackground(Color.WHITE);
			searchButton.setEnabled(true);
			nonUserSearch.setEnabled(true);
			
			String inputStr = searchButton.getText();
			
			if(inputStr.length() > 0)
			{
			int input = Integer.parseInt(inputStr);
			Technician hold;
			
			for(int i = 1; i < allPossibleTickets.size(); i++)
				{
					currentTicket = allPossibleTickets.get(i);
					hold = currentTicket.getOwner();
					if(hold.getEmpID() == input)
					{
						ticketsToReturn.add(currentTicket);
					}
				}
			}
			else
				ticketsToReturn = allPossibleTickets;
			
		}
		else if(filterOnCB.getSelectedItem() == TicketSearchFilters.TICKET)
		{
			
			searchButton.setBackground(Color.WHITE);
			searchButton.setEnabled(true);
			nonUserSearch.setEnabled(true);
			
			//ticketsToReturn = ticketSearchID(24);
			
			String input = searchButton.getText();
			if(input.length() > 0)
			{
				textFieldInput = Integer.parseInt(input);
				ticketsToReturn.add(HelpDeskSystem.getInstance().getTicketByID(textFieldInput));
			}
			else
				ticketsToReturn = allPossibleTickets;
				
		}
		else 
		{
			//By default, get all of them if there are no filters.	
			ticketsToReturn = allPossibleTickets;
			
		}

		return ticketsToReturn;
	}

}
