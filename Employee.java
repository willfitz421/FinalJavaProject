package Users;
import java.io.Serializable;
import java.util.ArrayList;

import Tickets.Ticket;

/**
	The Employee Class
	Holds all general information for a basic employee.
	@author Zach Loch and Cory Bishop with some Dr. Ryan updates
*/
public class Employee implements Serializable
{
	protected int empID;	
	protected String firstName;
	protected String lastName;
	protected String hireDate;
	protected String department;
	protected String phoneNum;	
	protected String emailAdd;	
	protected ArrayList<Ticket> associatedTickets;	//ArrayList of tickets associated to the Employee
											//Those opened by an employee who is just 
											//a user or assigned to an employee who is a technician
	
	protected static final long serialVersionUID = 1L; //Required for serialization
	
	/**
	 * Default constructor.
	 * @param id Employee ID number.
	 * @param first First name.
	 * @param last Last name. 
	 * @param hireD Employee's hire date.
	 * @param phone Phone number.
	 * @param email Email address.
	 * @param department Department
	*/
	public Employee(int id, String first, String last, String hireD, 
			        String phone, String email, String department)
	{
		this.empID = id;
		this.firstName = first;
		this.lastName = last;
		this.hireDate = hireD;
		this.phoneNum = phone;
		this.emailAdd = email;
		this.department = department;
		this.associatedTickets = new ArrayList<Ticket>();
	}

	/**
		Returns the Employee ID number.
		@return Employee ID number.
	*/
	public int getEmpID() 
	{
		return empID;
	}

	/**
		Returns the employee's first name.
		@return Employee's first name.
	*/
	public String getFirstName() 
	{
		return firstName;
	}

	/**
		Returns the employee's last name.
		@return Employee's last name.
	*/
	public String getLastName() 
	{
		return lastName;
	}

	/**
		Returns the employee's department.
		@return Employee's department.
	*/
	public String getDepartment() 
	{
		return department;
	}


	/**
		Returns the employee's hire date.
		@return Employee's hire date.
	*/
	public String getHireDate() 
	{
		return hireDate;
	}

	/**
		Returns the employee's phone number.
		@return Employee's phone number.
	*/
	public String getPhoneNum() 
	{
		return phoneNum;
	}

	/**
		Returns the employee's email address.
		@return Employee's email address.
	*/
	public String getEmailAdd() 
	{
		return emailAdd;
	}
	
	/**
		Returns ArrayList of all Tickets associated with the Employee 
		(so opened by a User or assigned to a technician).
		@return ArrayList of Tickets associated with the Employee.
	*/
	public ArrayList<Ticket> getTickets() 
	{
		return associatedTickets;
	}

	/**
	 * Add a ticket associated with the Employee 
	 *	(so opened by a User or assigned to a technician).
	 * @param ticket Ticket to assign to the technician
	 */
	public void addTicket(Ticket ticket) 
	{
		associatedTickets.add(ticket);
	}
	
	/**
	 	Returns a string with all of the employee's information.
	 	@return String with all Employee information.
	*/
	public String toString()
	{
		String ls = System.lineSeparator();
		return ("Employee ID: " + empID 
			  + ls + "Name: " + firstName + " " + lastName
			  + ls + "Hire Date: " + hireDate
			  + ls + "Phone Number: " + phoneNum
			  + ls + "Email Address: " + emailAdd);
	}
	
}