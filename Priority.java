package Tickets;
/**
 * Enum for Ticket Priority
 * @author ryank
 *
 */
public enum Priority 
{
	VERY_HIGH("Very High"), HIGH("High"), MAJOR_INCONVENIENCE("Major Inconvenience"),
	MINOR_INCONVENIENCE("Minor Inconvenience"), LOW("Low");
	
	
	private String printString;
	
	/**
	 * Constructor provided so that each enum has a prettier
	 * String easier for printing.
	 * @param printVersion A version of the enum that can be used
	 * for nicer printing
	 */
	Priority(String printVersion)
	{
		printString= printVersion;
	}

	/**
	 * Returns a version of the enum that can be used
	 * for nicer printing.
	 * @return a A string with a version of the enum that can be used
	 * for nicer printing
	 */
	@Override
	public String toString() {
		return printString;
	}
}
