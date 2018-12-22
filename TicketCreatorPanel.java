package GUIComponents;
import java.awt.GridLayout;
import javax.swing.*;
import Users.Employee;

/**
 * Panel to show the creator information in a pretty little box
 * when modifying/creating/viewing a ticket.
 * 
 * I chose not to show the hire date on this panel.  It doesn't help the user
 * or tech to know the creator's hire date.
 * 
 * No get/set methods are placed on this panel as it is not meant to
 * allow people to change employee information (see UserProfilePanel
 * for that).
 * 
 * @author ryank
 *
 */
public class TicketCreatorPanel extends JPanel{

	private static final long serialVersionUID = 1L; //for serialization - ignore
	
	private JLabel idLabel; //ID  
	private JTextField idTF;  

	private JLabel fnLabel; //firstName
	private JTextField fnTF;

	private JLabel lnLabel; //lastName
	private JTextField lnTF;
	
	private JLabel deptLabel;  //Department
	private JTextField deptTF;
	
	private JLabel emailLabel; //Email
	private JTextField emailTF;
	
	
	private JLabel phoneLabel; //Phone
	private JTextField phoneTF;


	/**
	 * The construcor
	 * @param emp  The employee who created the current ticket of interest and whose
	 * information we wish to display.
	 */
	public TicketCreatorPanel(Employee emp)
	{
		initComps(emp);
	}
	
	
	/**
	 * Initialize the components - Disable them all so that no one
	 * expects to be able to change them.
	 * @param emp  the empwhose info we'll use to set the component info
	 */
	private void initComps(Employee emp)
	{
		idLabel = new JLabel("Employee ID: ");
		idTF = new JTextField(15);
		idTF.setText("" + emp.getEmpID()); //empID is an int - "tricking" it into a String
		idTF.setEnabled(false);
		
		fnLabel = new JLabel("First Name: ");
		fnTF = new JTextField(15);
		fnTF.setText(emp.getFirstName());
		fnTF.setEnabled(false);

		lnLabel = new JLabel("Last Name: ");
		lnTF = new JTextField(15);
		lnTF.setText(emp.getLastName());
		lnTF.setEnabled(false);

		deptLabel = new JLabel("Department: ");
		deptTF = new JTextField(15);
		deptTF.setText(emp.getDepartment());
		deptTF.setEnabled(false);
		
		emailLabel = new JLabel("Email: ");
		emailTF = new JTextField(15);
		emailTF.setText(emp.getEmailAdd());
		emailTF.setEnabled(false);
		
		phoneLabel = new JLabel("Phone: ");
		phoneTF = new JTextField(15);
		phoneTF.setText(emp.getPhoneNum());	
		phoneTF.setEnabled(false);
		
		setBorder(BorderFactory.createTitledBorder("Creater Info"));
		setLayout(new GridLayout(6,2));
		
		add(idLabel);
		add(idTF);
		
		add(fnLabel);
		add(fnTF);

		add(lnLabel);
		add(lnTF);
		
		add(emailLabel);
		add(emailTF);
		
		add(phoneLabel);
		add(phoneTF);
		
		add(deptLabel);
		add(deptTF);

	}
	
}

