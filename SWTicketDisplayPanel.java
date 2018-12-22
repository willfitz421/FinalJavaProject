package GUIComponents;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

import Tickets.*;
import Users.Employee;
import Users.Technician;

/**
 * Extends TicketDisplayPanel so that Software Specific Components can be 
 * added to a Ticket DisplayPanel.
 * @author ryank
 *
 */
public class SWTicketDisplayPanel extends TicketDisplayPanel {

	private static final long serialVersionUID = 1L; //for serialization - ignore
	
	private JPanel helperSWPanel;
	private JLabel swTypeLabel;
	private JTextField swTypeTF;
	private JLabel osLabel;
	private JTextField osTF;
	private JLabel bitsLabel;
	private JComboBox<Bits> bitsCombo;

	/**
	 * This constructor is for creating a new SW Ticket.
	 * @param creator The employee creating the ticket.
	 * @param role The role of the employee (should be user if all is well)
	 */
	public SWTicketDisplayPanel(Employee creator, LogonRole role) {
		super(creator, role);
	}

	/***
	 * The constructor to use  to display 
	 * an already created software ticket's information.  If the loggedInEmp
	 * is the creator of the ticket, then this the TicketDisplayPanel
	 * will not be editable as users should only be able to view
	 * (but not modify) the contents. 
	 * @param ticket  The ticket whose info should be displayed.
	 * @param empLoggedIn  The employee logged in
	 * @param role  The role of the employee
	 */
	public SWTicketDisplayPanel(SoftwareTicket ticket, Employee empLoggedIn, LogonRole role) {
		super(ticket, empLoggedIn, role);
	}

	/**
	 * Initializes the special components specific to a SW Ticket.
	 */
	@Override
	protected void initSpecialComps() {

		//Add SW, OS, Bits Components
		swTypeLabel = new JLabel("Software Type: ");
		swTypeTF = new JTextField(15);

		osLabel = new JLabel("Operating System: ");
		osTF = new JTextField(15);

		bitsLabel = new JLabel("Bits: ");
		bitsCombo = new JComboBox<Bits>(Bits.values());

		specialPanel = new JPanel(new GridLayout(3,2));

		specialPanel.add(swTypeLabel);
		specialPanel.add(swTypeTF);
		specialPanel.add(osLabel);
		specialPanel.add(osTF);
		specialPanel.add(bitsLabel);
		specialPanel.add(bitsCombo);

		helperSWPanel = new JPanel();
		helperSWPanel.add(specialPanel);
		add(helperSWPanel);

	}

	/***
	 * SetsUp the submit button.  If this is being called from the parent, 
	 * then we actually need the button to do something.  Otherwise, 
	 * we don't need it.  
	 */
	@Override
	protected void setUpSubmitButton() {

		//If the role is a User and a submit button needs to be set up, 
		//then the submit button should be used to submit a new SW Ticket.
		if (empUsingPanelRole == LogonRole.USER)
		{
			//When Submit is pressed, ticket fields are validated and then
			//a next ticket is created and assigned to the first 
			//Technician in our list.  Then the autogenerated data
			//is filled into the corresponding fields.
			submitBtn.addActionListener(new ActionListener()
			{
				@Override
				public void actionPerformed(ActionEvent e) {

					submitBtn.setEnabled(false);
					
					String s = "";

					if (incDescrTA.getText().equalsIgnoreCase(DEFAULT_INC_STR))
						s = "Please include an incidence description.";
					else if (osTF.getText().equals(""))
						s = "Please include an OS.";
					else if (swTypeTF.getText().equals(""))
						s = "Please include an Software Type related to this ticket.";


					if (!s.equals(""))
						JOptionPane.showMessageDialog(null, s);
					else
					{
						Technician owner = HelpDeskSystem.getInstance().getTechnicians().get(0);

						//By Default, the first Tech in the queue will get the 
						//ticket assigned to him/her.  THe submit button is disabled
						//till a Technician is in the system so this should be safe...
						SoftwareTicket sw = new SoftwareTicket(employeeUsingPanel,
								incDescrTA.getText(),
								ticketBasicPanel.getPriority(), 
								ticketBasicPanel.getImpact(), 
								owner,
								osTF.getText(),
								swTypeTF.getText(),
								(Bits) bitsCombo.getSelectedItem());

						//Add new ticket to ticket list
						HelpDeskSystem.getInstance().addTicket(sw);

						//Add new ticket to Employee creator
						System.out.println("Ticket Added To System:" + 
									(HelpDeskSystem.getInstance().getTicketByID(sw.getTicketID())==sw));

						//Update the panel so that the autogenerated 
						//data now appears in the fields.
						ticketBasicPanel.setTicketInfo(sw);
					}
				}

			});

		}
	}

	/**
	 * Sets the special components up to be viewed.  
	 * Called after initSpecialComps by parent processing so
	 * components already initialized.
	 */
	protected void setSpecialCompsForViewingOldTickets(Ticket ticket, boolean isEditable) {

		SoftwareTicket swTicket = (SoftwareTicket) ticket;
		
		//Add SW, OS, Bits Components
		swTypeTF.setText(swTicket.getSoftwareType());
		osTF.setText(swTicket.getOperatingSystem());
		bitsCombo.setSelectedItem(swTicket.getBits());
		
		//If the ticket's information should only be viewed (and not modified)
		//then disable the needed components.
		
		if(empUsingPanelRole == LogonRole.USER)
		{
			swTypeTF.setEnabled(false);
			osTF.setEnabled(false);
			bitsCombo.setEnabled(false);
		}
		else if(!ticket.isOpen() && empUsingPanelRole != LogonRole.USER)
		{
			swTypeTF.setEnabled(false);
			osTF.setEnabled(false);
			bitsCombo.setEnabled(false);
			submitBtn.setEnabled(false);
			submitUpdate.setEnabled(false);
			submitTech.setEnabled(false);
			closeBtn.setEnabled(false);
		}
		else if (empUsingPanelRole != LogonRole.USER)
		{
			swTypeTF.setEnabled(true);
			osTF.setEnabled(true);
			bitsCombo.setEnabled(true);
			
			submitBtn.setText("Submit Info Change");
			submitBtn.setVisible(true);
			
			submitBtn.addActionListener(new ActionListener()
			{
				@Override
				public void actionPerformed(ActionEvent e) {
					
					swTicket.setPriority(ticketBasicPanel.getPriority());
					
					swTicket.setImpact(ticketBasicPanel.getImpact());
					
					swTicket.setSoftwareType(swTypeTF.getText());
					
					swTicket.setOperatingSystem(osTF.getText());
					
					swTicket.setBits((Bits) bitsCombo.getSelectedItem());
				}
			});
			
			//When Submit is pressed, ticket fields are validated and then
			//a next ticket is created and assigned to the first 
			//Technician in our list.  Then the autogenerated data
			//is filled into the corresponding fields.
			submitTech.addActionListener(new ActionListener()
			{
				@Override
				public void actionPerformed(ActionEvent e) {
					String s = null;
					
					s = JOptionPane.showInputDialog("Technician Notes: ");
					ticket.addTechNotes(s, ticket.getOwner());
					techNotesTA.setText(ticket.getTechNotesString());
				}
			});
			
			submitUpdate.addActionListener(new ActionListener()
			{
				@Override
				public void actionPerformed(ActionEvent e) {
					String s = null;
					
					s = JOptionPane.showInputDialog("Update Notes: ");
					ticket.addUpdateNotes(s, ticket.getOwner());
					updateNotesTA.setText(ticket.getUpdateNotesString());
				}
			});
			
			closeBtn.addActionListener(new ActionListener()
			{
				@Override
				public void actionPerformed(ActionEvent e) {
					
					String cn = "";
					
					cn = JOptionPane.showInputDialog("Please enter closing notes: ");
					ticket.close(cn, ticket.getOwner());
					
					
				}
			});
		}
		
	}


	@Override
	/**
	 * For resetting initial vlaues.
	 * Called by the parent when clearing out form values in resetInitialValues().
	 */
	protected void resetSpecialInitialValues()
	{
		swTypeTF.setText("");
		osTF.setText("");
		bitsCombo.setSelectedIndex(0);
	}
	
	/**
	 * Disables all child-specific components so that they are not editable.
	 */
	@Override
	protected void disableSpecialComps()
	{
		swTypeTF.setEnabled(false);
		osTF.setEnabled(false);
		bitsCombo.setEnabled(false);
	}


}