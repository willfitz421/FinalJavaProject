package Database;
import Users.*;
import Tickets.*;
import java.util.ArrayList;


import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

/**
 * DBHelper Class - Final Project Phase 3
 * A utility class to perform file reading/writing functions for ticket and technician information.
 * 
 * Written on Sunday, November 18, 2018
 * @author Kathleen Ryan
*/

public class DBHelper 
{
	final private static String TICKET_FILE = "Tickets.txt";	//File object to open the Tickets file
	final private static String TECH_FILE = "Technicians.txt";	//File object to open the Technicians file
	final private static String USER_FILE = "Users.txt";	//File object to open the Users file
	
	/**
	* Writes information for all of the technicians.
	* @param techList ArrayList containing information for all the technicians.
	*/
	public static void writeTechnicianFile(ArrayList<Technician> techList)
	{
        try
        {
            FileOutputStream fos = new FileOutputStream(TECH_FILE);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(techList);
            oos.close();
            fos.close();
        } 
        catch (IOException ioe) 
        {
            ioe.printStackTrace();
        }
	}

	/* Writes information for all of the users.
	* @param ticketList ArrayList containing information for all the users.
	*/
	public static void writeUserFile(ArrayList<Employee> usersList)
	{
        try
        {
            FileOutputStream fos = new FileOutputStream(USER_FILE);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(usersList);
            oos.close();
            fos.close();
        } 
        catch (IOException ioe) 
        {
            ioe.printStackTrace();
        }
	}
	
	/**
	* Writes information for all of the technicians.
	* @param ticketList ArrayList containing information for all the tickets.
	*/
	public static void writeTicketFile(ArrayList<Ticket> ticketList)
	{
        try
        {
            FileOutputStream fos = new FileOutputStream(TICKET_FILE);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(ticketList);
            oos.close();
            fos.close();
        } 
        catch (IOException ioe) 
        {
            ioe.printStackTrace();
        }
	}
	
	
	/**
	* Reads in all information for the technicians into an ArrayList .
	* @return techList ArrayList containing information for all the technicians.
	*/
	@SuppressWarnings("unchecked") //Suppressing some serialization issues
	public static ArrayList<Technician> readInTechnicians()
	{
		ArrayList<Technician> techList = new ArrayList<Technician>();
		FileInputStream fis = null;
		ObjectInputStream ois = null;

        try
        {
            fis = new FileInputStream(TECH_FILE);
            ois = new ObjectInputStream(fis);
            techList = (ArrayList<Technician>) ois.readObject();
    		ois.close();
    		fis.close();
        } 
		catch(FileNotFoundException e)
		{
			System.out.printf("File \"%s\" not found. No Technicians imported." + System.lineSeparator(), TECH_FILE);		
		}
        catch (IOException ioe) 
        {
            ioe.printStackTrace();
        } 
        catch (ClassNotFoundException c) 
        {
            System.out.println("Class not found");
            c.printStackTrace();
        }
        catch(ClassCastException cce)
        {
            System.out.println("Class Casting Problem."); 	
            cce.printStackTrace();        	
        }
        
    	return techList;
	}
	
	/**
	* Reads in all information for the tickets into an ArrayList .
	* @return techList ArrayList containing information for all the tickets.
	*/
	@SuppressWarnings("unchecked")
	public static ArrayList<Ticket> readInTickets()
	{
		ArrayList<Ticket> ticketList = new ArrayList<Ticket>();
		FileInputStream fis = null;
		ObjectInputStream ois = null;
		int maxID = 0; //To Set the Max Ticket ID already used
		
        try
        {
            fis = new FileInputStream(TICKET_FILE);
            ois = new ObjectInputStream(fis);
            ticketList = (ArrayList<Ticket>) ois.readObject();
    		ois.close();
    		fis.close();
        } 
		catch(FileNotFoundException e)
		{
			System.out.printf("File \"%s\" not found. No Tickets imported." + System.lineSeparator(), TICKET_FILE);		
		}
        catch (IOException ioe) 
        {
            ioe.printStackTrace();
        } 
        catch (ClassNotFoundException c) 
        {
            System.out.println("Class not found");
            c.printStackTrace();
        }
        catch(ClassCastException cce)
        {
            System.out.println("Class Casting Problem."); 	
            cce.printStackTrace();        	
        }
        
        //Search for the max ticket ID already used - This helps
        //with the autogeneration of the ticket ID.
        for (int i=0; i < ticketList.size();i++)
        {
        	if (ticketList.get(i).getTicketID() > maxID)
        		maxID = ticketList.get(i).getTicketID();
        }
        
        Ticket.setMaxTicketID(maxID);
        
    	return ticketList;
	}
	
	/**
	* Reads in all information for the users into an ArrayList .
	* @return ArrayList containing information for all the users.
	*/
	@SuppressWarnings("unchecked")
	public static ArrayList<Employee> readInUsers()
	{
		ArrayList<Employee> userList = new ArrayList<Employee>();
		FileInputStream fis = null;
		ObjectInputStream ois = null;
		
        try
        {
            fis = new FileInputStream(USER_FILE);
            ois = new ObjectInputStream(fis);
            userList = (ArrayList<Employee>) ois.readObject();
    		ois.close();
    		fis.close();
        } 
		catch(FileNotFoundException e)
		{
			System.out.printf("File \"%s\" not found. No Users imported." + System.lineSeparator(), USER_FILE);		
		}
        catch (IOException ioe) 
        {
            ioe.printStackTrace();
        } 
        catch (ClassNotFoundException c) 
        {
            System.out.println("Class not found");
            c.printStackTrace();
        }
        catch(ClassCastException cce)
        {
            System.out.println("Class Casting Problem."); 	
            cce.printStackTrace();        	
        }
        
    	return userList;
	}
}
