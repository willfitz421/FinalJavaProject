package GUIComponents;

/**
 * Exception to be thrown if the someone logged in as one role (like User)
 * but should log in as another role (like Technician).
 * @author ryank
 *
 */
public class LogonMismatchException extends Exception {

	private static final long serialVersionUID = 1L; //for serialization - ignore
	
	LogonMismatchException(int id, LogonRole t1, LogonRole t2)
	{
		super("Employee with ID " + id + " is trying to logon as " + t1 + 
				" but has role " + t2);
	}
}
