package Tickets;
import java.util.ArrayList;

import Users.Employee;
import Users.Technician;

/**
 * 
 * @author Zach Loch and Cory Bishop with tweaks by Dr. R
 *
 */
public class HardwareTicket extends Ticket 
{
	
	private String hardwareType;
	private String manufacturer;
	private String productNumber;
	private String serialNumber;
	private static final long serialVersionUID = 1L;  //Required for serialization
	
	/**
	 * Constructor
	 * 
	 * @param c creator of the ticket
	 * @param inc incident the ticket describes
	 * @param p priority level of the ticket
	 * @param imp group of people impacted by the incident
	 * @param o current operator working on the problem
	 * @param type type of HW
	 * @param m manufacturer
	 * @param proNum product number
	 * @param serialNum serial number
	 */
	public HardwareTicket(Employee c, String inc, Priority p, Impact imp, Technician o,
			String type, String m, String proNum, String serialNum) {
		super(c, inc, p, imp, o);
		hardwareType = type;
		manufacturer = m;
		productNumber = proNum;
		serialNumber = serialNum;
	}
	
	/**
	 * Use of this constructor is not expected except in one case:
	 * Special Constructor for use with reading in from DB - 
	 * Does not autogenerate the ID - allows user to pass it in.
	 * 
	 * @param id id of ticket
	 *  @param c creator of the ticket
	 * @param inc incident the ticket describes
	 * @param p priority level of the ticket
	 * @param imp group of people impacted by the incident
	 * @param o current operator working on the problem
	 * @param type type of HW
	 * @param m manufacturer
	 * @param proNum product number
	 * @param serialNum serial number
	 * @param dOp Date Opened 
	 * @param dCl  Date Closed
	 * @param clNotes Closing Notes, 
	 * @param isO if the ticket is open
	 */
	public HardwareTicket(int id, Employee c, String inc, Priority p, Impact imp, Technician o,
			String type, String m, String proNum, String serialNum, String dOp, String dCl, String clNotes, 
			boolean isO, ArrayList <String> tNotes, ArrayList <String> uNotes) {
		super(id, c, inc, p, imp, o, dOp, dCl, clNotes, isO, tNotes, uNotes);
		hardwareType = type;
		manufacturer = m;
		productNumber = proNum;
		serialNumber = serialNum;
	}
	
	/**
	 * Gets the HW type
	 * @return The HW type
	 */
	public String getHardwareType() {
		return hardwareType;
	}

	/**
	 * Gets the Manufacaturer
	 * @return The manufacturer
	 */
	public String getManufacturer() {
		return manufacturer;
	}

	/**
	 * Gets the product number
	 * @return The product Number
	 */
	public String getProductNumber() {
		return productNumber;
	}

	/**
	 * The Serial Number
	 * @return The serial Number
	 */
	public String getSerialNumber() {
		return serialNumber;
	}	
	
	public void setHardwareType(String hardwareType) {
		this.hardwareType = hardwareType;
	}

	public void setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer;
	}

	public void setProductNumber(String productNumber) {
		this.productNumber = productNumber;
	}

	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}
	
	/**
	 * Creates a public version of the ticket to be viewed by whoever would like to
	 * @return the ticket in string form
	 */
	@Override
	public String toString() {
		String ls = System.lineSeparator();
		
		return (super.toString() + 
				ls + "Hardware Type: " + this.hardwareType +
				ls + "Manufacturer: " + this.manufacturer +
				ls + "Product Number: " + this.productNumber + 
				ls + "Serial Number: " + this.serialNumber);
	}
}
