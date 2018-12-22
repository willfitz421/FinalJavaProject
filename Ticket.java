package Tickets;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import Users.Employee;
import Users.Technician;
/**
 * Ticket Class
 * Holds all information to be written to a ticket
 * @author Zach Loch and Cory Bishop with tweaks by Dr. R
 */
public class Ticket implements Serializable {
	
	//***Fields***
	private static int maxTicketID = 0; //for ticket ID autogen
	private int idNum;
	private boolean isOpen;
	private Employee creator;
	private String incidence;
	private String dateOpened;
	private String dateClosed;
	private String closingNotes;
	private Priority priority;
	private Impact impact;
	private ArrayList <String> techNotes;
	private ArrayList <String> updateNotes;
	private Technician owner;
	private static final long serialVersionUID = 1L; //Required for serialization
	private static DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");  //used for making pretty timestamps
	
	/**
	 * Allows the DBHelper to set the max ticket ID in use.
	 * @param max  The value to set the max ID (for ticket ID autogeneration)
	 */
	public static void setMaxTicketID(int max)
	{
		maxTicketID = max;
	}
	
	/**
	 * Allows the DBHelper to check the max ticket ID in use.
	 * @return The value of the max ID (for ticket ID autogeneration)
	 */
	public static int getMaxTicketID()
	{
		return maxTicketID;
	}
	
	/**
	 * Constructor - Also assigns the ticket to the passed in owner.
	 * @param c creator of the ticket
	 * @param inc incident the ticket describes
	 * @param p priority level of the ticket
	 * @param imp group of people impacted by the incident
	 * @param o current operator working on the problem
	 */
	public Ticket(Employee c, String inc, Priority p, Impact imp, Technician o) {
		maxTicketID++;
		idNum = maxTicketID;
		isOpen = true;
		creator = c;
		this.incidence = inc;		
		 
	    LocalDateTime now = LocalDateTime.now();  
		dateOpened = dtf.format(now);
		
		this.priority = p;
		impact = imp;
		techNotes = new ArrayList<String>();
		updateNotes = new ArrayList<String>();
		owner = o;		
		dateClosed = "N/A";
		closingNotes = "N/A";

		//Associate the ticket with both the creator
		//and the technician owner.
		creator.addTicket(this);
		owner.addTicket(this);	
	}
	
	/**
	 * Use of this constructor is not expected except in one case:
	 * Special Constructor for use with reading in from DB - 
	 * Does not autogenerate the ID - allows user to pass it in.
	 * Does not autogenerate anything else as well (dateOpened, etc.)
	 *
	 * @param id ID of ticket
	 * @param c creator of the ticket
	 * @param inc incident the ticket describes
	 * @param p priority level of the ticket
	 * @param imp group of people impacted by the incident
	 * @param o current operator working on the problem
	 * @param dOp Date Opened 
	 * @param dCl  Date Closed
	 * @param clNotes Closing Notes, 
	 * @param isO if the ticket is open
	 */
	public Ticket(int id, Employee c, String inc, Priority p, Impact imp, Technician o, 
			String dOp, String dCl, String clNotes, boolean isO, ArrayList <String> tNotes,
			ArrayList <String> uNotes) {
	
		idNum = id;
		isOpen = isO;
		creator = c;
		this.incidence = inc;		
		 
		dateOpened = dOp;
		
		this.priority = p;
		impact = imp;
		techNotes = tNotes;
		updateNotes = uNotes;
		owner = o;		
		dateClosed = dCl;
		closingNotes = clNotes;

		//Associate the ticket with both the creator
		//and the technician owner.
		creator.addTicket(this);
		owner.addTicket(this);	
	}
	
	/**
	 * Get the Employee who created this ticket.
	 * @return the employee who opened the ticket
	 */
	public Employee getCreator() {
		return creator;
	}

	/**
	 * Get whether the ticket is open or not
	 * @return whether the ticket is open or not
	 */
	public boolean isOpen() {
		return isOpen;
	}

	/**
	 * Get the initial description of the incident.
	 * @return Returns the initial description of the incident.
	 */
	public String getIncidence() {
		return incidence;
	}

	/**
	 * Get the date that this ticket was opened.
	 * @return THe date this ticket was opened.
	 */
	public String getDateOpened() {
		return dateOpened;
	}

	/**
	 * Get the date this ticket was closed.
	 * @return Returns the date this ticket was closed.
	 */
	public String getDateClosed() {
		return dateClosed;
	}

	/***
	 * Get the closing notes for the ticket
	 * @return The notes for when the ticket was closed.
	 */
	public String getClosingNotes() {
		return closingNotes;
	}

	/**
	 * Gets the priority of the ticket
	 * @return The priority of the ticket
	 */
	public Priority getPriority() {
		return priority;
	}

	/**
	 * Gets the impact of the ticket
	 * @return The impact
	 */
	public Impact getImpact() {
		return impact;
	}
	
	public void setPriority(Priority priority) {
		this.priority = priority;
	}

	public void setImpact(Impact impact) {
		this.impact = impact;
	}

	/**
	 * Returns the ID Number of the ticket.
	 * @return ID Number of the ticket.
	*/
	public int getTicketID()
	{
		return idNum;
	}
	
	/**
	 * Returns the technician who owns the ticket.
	 * @return Technician who owns the ticket.
	*/
	public Technician getOwner()
	{
		return owner;
	}
	
	/**
	 * Closes the ticket
	 * @param closeNotes Closing notes for the ticket.
	 * @param closer  The technician who closed the ticket (since
	 * other technicians can make changes besides the owner).
	*/
	public void close(String closeNotes, Technician closer)
	{
	    LocalDateTime now = LocalDateTime.now();  
		dateClosed = dtf.format(now);
		
		closingNotes = closeNotes;
		isOpen = false;
		
		addUpdateNotes("Ticket Closed - " + closeNotes, closer);
	}
	

	/**
	 * Changes the operator currently working on the problem in these 3 steps:
	 * removes the ticket from the old owner's assigned ticket list,
	 * makes t the new owner, and adds the ticket to the new owner's assigned ticket list.
	 * @param t the new technician
	 */
	public void reassignTo(Technician t) 
	{
		this.owner.removeTicket(this);
		this.owner = t;
		t.addTicket(this);
	}
	
	
	/**
	 * makes a new entry to the technician notes part of a ticket
	 * @param n the new entry
	 * @param t the technician who made the entry (since
	 * other technicians can make changes besides the owner).
	 */
	public void addTechNotes(String n, Technician t) 
	{
		
	    LocalDateTime now = LocalDateTime.now();  
		
		techNotes.add( dtf.format(now) + " - " + n + " - " + 
				t.getFirstName() + " " + t.getLastName() + "\n");
	}	
	
	
	/**
	 * makes a new entry to the update notes part of a ticket
	 * @param n the new entry
	 * @param t the technician who made the entry (since
	 * other technicians can make changes besides the owner).
	 */
	public void addUpdateNotes(String n, Technician t)  
	{
	    LocalDateTime now = LocalDateTime.now();  
		
		updateNotes.add( dtf.format(now)  + " - " + n + " - " + 
		t.getFirstName() + " " + t.getLastName() + "\n");
	}
	

	
	/***
	 * Helper function to return a string with the update notes (one on each line). 
	 * @return A string with the update notes (one on each line). 
	 */
	public String getUpdateNotesString()
	{
		String s = "***Begin Update Notes***";
		String ls = System.lineSeparator();
		
		for(int i = 0; i < this.updateNotes.size();i++)
		{
			s += ls + updateNotes.get(i);
		}
	
		s = s + ls + "***End Update Notes***";
		
		return s;
	}

	public String getTechNotesString()
	{
		String s = "***Begin Tech Notes***";
		String ls = System.lineSeparator();
		
		for(int i = 0; i < this.techNotes.size();i++)
		{
			s += ls + techNotes.get(i);
		}
	
		s = s + ls + "***End Tech Notes***";
		
		return s;
	}
	
	public ArrayList <String> getTechNotesArrayList()
	{
		return techNotes;
	}
	
	public ArrayList <String> getUpdateNotesArrayList()
	{
		return updateNotes;
	}
	
	/**
	 * creates a public version of the ticket to be viewed by whoever would like to
	 * @return the ticket in string form
	 */
	public String toString() {
		String ls = System.lineSeparator();
		
		String status = "OPEN";
		
		if (!isOpen)
			status = "CLOSED";
		
		return("Ticket Data: " +
				ls + "Ticket ID: " + this.idNum +
				ls+ "Status: " + status +
				ls+ "Ticket Creator Data:" + ls + this.creator.toString() +
				ls+ "Incident Description: " + ls + this.incidence +
				ls+ "Opened: " + this.dateOpened +
				ls+ "Closing Notes:" + this.closingNotes + 
				ls+ "Closed: " + this.dateClosed + 
				ls+ "Priority: " + this.priority +
				ls+ "Impact: " + this.impact + 
				ls+ "Update Notes: " + ls+ this.getUpdateNotesString() +
				ls+ "Current Owner Details:" + ls + this.owner.toString());
	}
	
	/**
	 * creates a version of the ticket privy only to technicians, 
	 * which includes the technician notes.  The GUI will make sure
	 * this is only called by a technician.
	 * @return the ticket in string form, technician notes included.
	 */
	public String toStringForTech() 
	{
		String ls = System.lineSeparator();
		return (this.toString() + 
				ls+ "Technician Notes: " + ls + getTechNotesString());
	}
	
	public void changeStatus()
	{
		this.isOpen = !isOpen;
	}
	
}
