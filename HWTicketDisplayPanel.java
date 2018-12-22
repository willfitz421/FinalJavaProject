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
public class HWTicketDisplayPanel extends TicketDisplayPanel {

	private static final long serialVersionUID = 1L; //for serialization - ignore
	
	private JPanel helperHWPanel;
	private JLabel hardwareTypeLabel;
	private JTextField hwTypeTF;
	private JLabel manufacturerLabel;
	private JTextField mTF;
	private JLabel productNumLabel;
	private JTextField pnTF;
	private JLabel serialNumberLabel;
	private JTextField snTF;
	
	/**
	 * This constructor is for creating a new SW Ticket.
	 * @param creator The employee creating the ticket.
	 * @param role The role of the employee (should be user if all is well)
	 */
	public HWTicketDisplayPanel(Employee creator, LogonRole role) {
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
	public HWTicketDisplayPanel(SoftwareTicket ticket, Employee empLoggedIn, LogonRole role) {
		super(ticket, empLoggedIn, role);
	}

	public HWTicketDisplayPanel(HardwareTicket ticket, Employee empLoggedIn, LogonRole role) {
		super(ticket, empLoggedIn, role);
	}

	/**
	 * Initializes the special components specific to a SW Ticket.
	 */
	@Override
	protected void initSpecialComps() {

		//Add SW, OS, Bits Components
		hardwareTypeLabel = new JLabel("Hardware Type: ");
		hwTypeTF = new JTextField(15);

		manufacturerLabel = new JLabel("Manufacturer: ");
		mTF = new JTextField(15);

		productNumLabel = new JLabel("Product Number: ");
		pnTF = new JTextField(15);
		
		serialNumberLabel = new JLabel("Serial Number: ");
		snTF = new JTextField(15);
		
		specialPanel = new JPanel(new GridLayout(5,2));

		specialPanel.add(hardwareTypeLabel);
		specialPanel.add(hwTypeTF);
		specialPanel.add(manufacturerLabel);
		specialPanel.add(mTF);
		specialPanel.add(productNumLabel);
		specialPanel.add(pnTF);
		specialPanel.add(serialNumberLabel);
		specialPanel.add(snTF);
		
		helperHWPanel = new JPanel();
		helperHWPanel.add(specialPanel);
		add(helperHWPanel);


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
					else if (mTF.getText().equals(""))
						s = "Please include manufacturer.";
					else if (hwTypeTF.getText().equals(""))
						s = "Please include an Hardware Type related to this ticket.";
					else if (pnTF.getText().equals(""))
						s = "Please include a product number.";
					else if (snTF.getText().equals(""))
						s = "Please include a serial number.";


					if (!s.equals(""))
						JOptionPane.showMessageDialog(null, s);
					else
					{
						Technician owner = HelpDeskSystem.getInstance().getTechnicians().get(1);

						//By Default, the first Tech in the queue will get the 
						//ticket assigned to him/her.  THe submit button is disabled
						//till a Technician is in the system so this should be safe...
						HardwareTicket hw = new HardwareTicket(employeeUsingPanel,
								incDescrTA.getText(),
								ticketBasicPanel.getPriority(), 
								ticketBasicPanel.getImpact(), 
								owner,
								mTF.getText(),
								pnTF.getText(),
								hwTypeTF.getText(),
								snTF.getText());
						
						
						
						//Add new ticket to ticket list
						HelpDeskSystem.getInstance().addTicket(hw);

						//Add new ticket to Employee creator
						System.out.println("Ticket Added To System:" + 
									(HelpDeskSystem.getInstance().getTicketByID(hw.getTicketID())==hw));

						//Update the panel so that the autogenerated 
						//data now appears in the fields.
						ticketBasicPanel.setTicketInfo(hw);
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

		HardwareTicket hwTicket = (HardwareTicket) ticket;

		//Add HWType, Man, PN, SN
		hwTypeTF.setText(hwTicket.getHardwareType());
		pnTF.setText(hwTicket.getProductNumber());
		mTF.setText(hwTicket.getManufacturer());
		snTF.setText(hwTicket.getSerialNumber());
		
		//If the ticket's information should only be viewed (and not modified)
		//then disable the needed components.
		
		if(empUsingPanelRole == LogonRole.USER)
		{
			hwTypeTF.setEnabled(false);
			pnTF.setEnabled(false);
			mTF.setEnabled(false);
			snTF.setEnabled(false);
		}
		else if(!ticket.isOpen() && empUsingPanelRole != LogonRole.USER)
		{
			hwTypeTF.setEnabled(false);
			pnTF.setEnabled(false);
			mTF.setEnabled(false);
			snTF.setEnabled(false);
			submitBtn.setEnabled(false);
			submitUpdate.setEnabled(false);
			submitTech.setEnabled(false);
			closeBtn.setEnabled(false);
		}
		else if (empUsingPanelRole != LogonRole.USER)
		{
			hwTypeTF.setEnabled(true);
			pnTF.setEnabled(true);
			mTF.setEnabled(true);
			snTF.setEnabled(true);
			
			submitBtn.setText("Submit Info Change");
			submitBtn.setVisible(true);
			
			submitBtn.addActionListener(new ActionListener()
			{
				@Override
				public void actionPerformed(ActionEvent e) {
					
					hwTicket.setPriority(ticketBasicPanel.getPriority());
					
					hwTicket.setImpact(ticketBasicPanel.getImpact());
					
					hwTicket.setHardwareType(hwTypeTF.getText());
					
					hwTicket.setProductNumber(pnTF.getText());
					
					hwTicket.setManufacturer(mTF.getText());
					
					hwTicket.setSerialNumber(snTF.getText());
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
					updateNotesTA.setText(ticket.getUpdateNotesString());
					
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
		hwTypeTF.setText("");
		pnTF.setText("");
		mTF.setText("");
		snTF.setText("");
	}
	
	/**
	 * Disables all child-specific components so that they are not editable.
	 */
	@Override
	protected void disableSpecialComps()
	{
		hwTypeTF.setEnabled(false);
		pnTF.setEnabled(false);
		mTF.setEnabled(false);
		snTF.setEnabled(false);
	}


}