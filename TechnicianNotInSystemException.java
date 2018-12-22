package GUIComponents;

/***
 * Exception to be thrown if a tech logons without supervisor
 * adding him/her to the system yet.
 * 
 * @author ryank
 *
 */
public class TechnicianNotInSystemException extends Exception {
	
	private static final long serialVersionUID = 1L; //for serialization - ignore
	
	TechnicianNotInSystemException(int techID)
	{
		super("Technician with ID " + techID + " is not in the System yet.");
	}
}
