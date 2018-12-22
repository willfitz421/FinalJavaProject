package Tickets;
/**
 * Enum for Ticket Impact
 * @author ryank
 *
 */

public enum Impact 
{
	PERSON("Person"), GROUP("Group"), TEAM("Team"),
	DEPARTMENT("Department"), COMPANY("Company"), CLIENT("Client");
	
	private String printString;
	
	/**
	 * Constructor provided so that each enum has a prettier
	 * String easier for printing.
	 * @param printVersion A version of the enum that can be used
	 * for nicer printing
	 */
	Impact(String printVersion)
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
