package Tickets;
import java.util.ArrayList;

import Users.Employee;
import Users.Technician;

/**
 * SoftwareTicket class extends from the Ticket class
 * @author Zach Loch Cory Bishop with tweaks by Dr. R
 */
public class SoftwareTicket extends Ticket{

	private String operatingSystem;
	private String softwareType;
	private Bits bits;
	
	private static final long serialVersionUID = 1L;  //Required for serialization
	

	/**
	 * SoftwareTicket constructor
	 * @param c creator of the ticket
	 * @param inc incident description
	 * @param p priority level
	 * @param imp impact level
	 * @param o current operator
	 * @param os operating system
	 * @param type the type of software
	 * @param numBits the number of bits
	 */
	public SoftwareTicket(Employee c, String inc, Priority p, Impact imp, Technician o,
			String os, String type, Bits numBits) {
		super(c, inc, p, imp, o);
		operatingSystem = os;
		softwareType = type;
		bits = numBits;
	}

	/**
	 * Use of this constructor is not expected except in one case:
	 * Special Constructor for use with reading in from DB - 
	 * Does not autogenerate the ID - allows user to pass it in.
	 * Does not autogenerate anything else as well (dateOpened, etc.)
	 *
	 * @param id Ticket ID
	 * @param c creator of the ticket
	 * @param inc incident description
	 * @param p priority level
	 * @param imp impact level
	 * @param o current operator
	 * @param os operating system
	 * @param type the type of software
	 * @param numBits the number of bits
	 * @param dOp Date Opened 
	 * @param dCl  Date Closed
	 * @param clNotes Closing Notes, 
	 * @param isO if the ticket is open
	 */
	public SoftwareTicket(int id, Employee c, String inc, Priority p, Impact imp, Technician o,
			String os, String type, Bits numBits, String dOp, String dCl, String clNotes, boolean isO, 
			ArrayList <String> tNotes, ArrayList <String> uNotes) {
		super(id, c, inc, p, imp, o, dOp, dCl, clNotes, isO, tNotes, uNotes);
		operatingSystem = os;
		softwareType = type;
		bits = numBits;
	}

	/**
	 * Gets the bits (32 or 64)
	 * @return The number of bits
	 */
	public Bits getBits() {
		return bits;
	}

	/**
	 * Gets the OS.
	 * @return The OS information
	 */
	public String getOperatingSystem() {
		return operatingSystem;
	}

	/***
	 * Gets the type of software causing the issue.
	 * @return Type of software for which the ticket was opened
	 */
	public String getSoftwareType() {
		return softwareType;
	}

	public void setOperatingSystem(String operatingSystem) {
		this.operatingSystem = operatingSystem;
	}

	public void setSoftwareType(String softwareType) {
		this.softwareType = softwareType;
	}

	public void setBits(Bits bits) {
		this.bits = bits;
	}
	
	/**
	 * creates a public version of the ticket to be viewed by whoever would like to
	 * @return the ticket in string form
	 */
	@Override
	public String toString() {
		return (super.toString() + 
				"\nOS: " + this.operatingSystem +
				"\nSoftware Type: " + this.softwareType);
	}
	
	/**
	 * creates a version of the ticket privy only to technicians, 
	 * which includes the technician notes
	 * @return the ticket in string form, technician notes included.
	 */
	@Override
	public String toStringForTech() {
		String ls = System.lineSeparator();
		return (super.toString() + 
				ls + "OS: " + this.operatingSystem +
				ls + "Software Type: " + this.softwareType);
	}

	
}
