/******************************************************************************************************************
* File: MessageManagerInterface.java
* Course: 17655
* Project: Assignment 3
* Copyright: Copyright (c) 2009 Carnegie Mellon University
* Versions:
*	1.0 February 2009 - Initial rewrite of original assignment 3 (ajl).
*
* Description: This class provides an interface to the message manager for participants (processes), enabling them to
*			   to send and receive messages between participants. A participant is any thread, object, or process,
*			   that instantiates an MessageManagerInterface object - this automatically attempts to register that
*			   entity with the message manager
*
* Parameters: None
*
* Internal Methods: SendMessage - Sends an message to the message manager
*					GetMessageQueue - Gets a participants message queue from the message manager.
*					GetMyId - Gets a participants registration ID
*				    GetRegistrationTime - Gets the point in time when a participant registered with the
*										  message manager
*
******************************************************************************************************************/
package MessagePackage;

import java.rmi.*;
import java.net.*;
import java.util.*;
import java.text.SimpleDateFormat;

public class MessageManagerInterface
{
	private long ParticipantId = -1;				// This processes ID
	private RMIMessageManagerInterface em = null;	// Message manager interface object
	private String DEFAULTPORT = "1099";			// Default message manager port

	/***************************************************************************
	* Exceptions::
	*
	****************************************************************************/

	class SendMessageException extends Exception
	{
		SendMessageException()
		{ super(); }

		SendMessageException(String s)
		{ super(s); }

	} // Exception

	class GetMessageException extends Exception
	{
		GetMessageException()
		{ super(); }

		GetMessageException(String s)
		{ super(s); }

	} // Exception

	class ParticipantAlreadyRegisteredException extends Exception
	{
		ParticipantAlreadyRegisteredException()
		{ super(); }

		ParticipantAlreadyRegisteredException(String s)
		{ super(s); }

	} // Exception

	class ParticipantNotRegisteredException extends Exception
	{
		ParticipantNotRegisteredException()
		{ super(); }

		ParticipantNotRegisteredException(String s)
		{ super(s); }

	} // Exception

	class LocatingMessageManagerException extends Exception
	{
		LocatingMessageManagerException()
		{ super(); }

		LocatingMessageManagerException(String s)
		{ super(s); }

	} // Exception

	class LocalHostIpAddressException extends Exception
	{
		LocalHostIpAddressException()
		{ super(); }

		LocalHostIpAddressException(String s)
		{ super(s); }

	} // Exception

	class RegistrationException extends Exception
	{
		RegistrationException()
		{ super(); }

		RegistrationException(String s)
		{ super(s); }

	} // Exception

	/***************************************************************************
	* CONSTRUCTOR:: MessageManagerInterface()
	* Purpose: This method registers participants with the message manager. This
	*	   	   instantiation should be used when the message manager is on the
	*		   local machine, using the default port (1099).
	*
	* Arguments: None.
	*
	* Returns: None.
	*
	* Exceptions: LocatingMessageManagerException, RegistrationException,
	*			  ParticipantAlreadyRegisteredException
	*
	****************************************************************************/

	public MessageManagerInterface() throws LocatingMessageManagerException, RegistrationException, ParticipantAlreadyRegisteredException
	{
		// First we check to see if the participant is already registered. If not
		// we go on with the registration. If the are, we throw an exception.

		if (ParticipantId == -1)
		{
			try
			{
				em = (RMIMessageManagerInterface) Naming.lookup( "MessageManager" );

			} // try

	    	catch (Exception e)
	    	{
				throw new LocatingMessageManagerException( "Message manager not found on local machine at default port (1099)" );

	    	} // catch

		   	try
		   	{
				ParticipantId = em.Register();

			} // try

			catch (Exception e)
			{
				throw new RegistrationException( "Error registering participant " + ParticipantId );

			} // catch

		} else {

			throw new ParticipantAlreadyRegisteredException( "Participant already registered " + ParticipantId );

		} // if

	} // MessageManagerInterface

	/***************************************************************************
	* CONSTRUCTOR:: MessageManagerInterface( String IPAddress )
	* Purpose: This method registers participants with the message manager at a
	* 		   specified IP address. This instantiation is used  when the
	*		   MessageManager is not on a local machine.
	*
	* Arguments: None.
	*
	* Returns: long integer - the participants id
	*
	* Exceptions: LocatingMessageManagerException, RegistrationException,
	*			  ParticipantAlreadyRegisteredException
	*
	****************************************************************************/

	public MessageManagerInterface( String ServerIpAddress ) throws LocatingMessageManagerException,
	RegistrationException, ParticipantAlreadyRegisteredException
	{
		// Assumes that the message manager is on another machine. The user must provide the IP
		// address of the message manager and the port number

		String EMServer = "//" + ServerIpAddress + ":" + DEFAULTPORT + "/MessageManager";

		if (ParticipantId == -1)
		{
			try
			{
				em = (RMIMessageManagerInterface) Naming.lookup( EMServer );

			} // try

	    	catch (Exception e)
	    	{
				throw new LocatingMessageManagerException( "Message manager not found on machine at:" + ServerIpAddress + "::" + e );

	    	} // catch

		   	try
		   	{
				ParticipantId = em.Register();

			} // try

			catch (Exception e)
			{
				throw new RegistrationException( "Error registering participant " + ParticipantId );

			} // catch

		} else {

			throw new ParticipantAlreadyRegisteredException( "Participant already registered " + ParticipantId );

		} // if

	} // MessageManagerInterface

	/***************************************************************************
	* CONCRETE METHOD:: GetMyId
	* Purpose: This method allows participants to get their participant Id.
	*
	* Arguments: None.
	*
	* Returns: long integer - the participants id
	*
	* Exceptions: ParticipantNotRegisteredException
	*
	****************************************************************************/

	public long GetMyId() throws ParticipantNotRegisteredException
	{
		if (ParticipantId != -1)
		{
			return ParticipantId;

	    } else {

			throw new ParticipantNotRegisteredException( "Participant not registered" );

		} // if

	} // GetMyId

	/***************************************************************************
	* CONCRETE METHOD:: GetRegistrationTime
	* Purpose: This method allows participants to obtain the time of registration.
	*
	* Arguments: None.
	*
	* Returns: String time stamp in the format: yyyy MM dd::hh:mm:ss:SSS
	*											yyyy = year
	*											MM = month
	*											dd = day
	*											hh = hour
	*											mm = minutes
	*											ss = seconds
	*											SSS = milliseconds
	*
	* Exceptions: ParticipantNotRegisteredException
	*
	****************************************************************************/

	public String GetRegistrationTime() throws ParticipantNotRegisteredException
	{
		Calendar TimeStamp = Calendar.getInstance();
		SimpleDateFormat TimeStampFormat = new SimpleDateFormat("yyyy MM dd::hh:mm:ss:SSS");

		if (ParticipantId != -1)
		{
			TimeStamp.setTimeInMillis(ParticipantId);
			return ( TimeStampFormat.format(TimeStamp.getTime()) );

	    } else {

			throw new ParticipantNotRegisteredException( "Participant not registered" );

		} // if

	} // GetRegistrationTime

	/***************************************************************************
	* CONCRETE METHOD:: SendMessage
	* Purpose: This method sends an message to the message manager.
	*
	* Arguments: Message object.
	*
	* Returns: None.
	*
	* Exceptions: ParticipantNotRegisteredException, SendMessageException
	*
	****************************************************************************/

	public void SendMessage( Message evt ) throws ParticipantNotRegisteredException, SendMessageException
	{
		if (ParticipantId != -1)
		{
		   	try
	    	{
				evt.SetSenderId( ParticipantId );
				em.SendMessage( evt );

	    	} // try

			catch (Exception e)
			{
				 throw new SendMessageException( "Error sending message" + e );

			} // catch

		} else {

			throw new ParticipantNotRegisteredException( "Participant not registered" );

		} // if

	} // SendMessage

	/***************************************************************************
	* CONCRETE METHOD:: GetMessage
	* Purpose: This method sends an message to the message manager.
	*
	* Arguments: None.
	*
	* Returns: Message object.
	*
	* Exceptions: ParticipantNotRegisteredException, GetMessageException
	*
	****************************************************************************/

	public MessageQueue GetMessageQueue() throws ParticipantNotRegisteredException, GetMessageException
	{
		MessageQueue eq = null;

		if (ParticipantId != -1)
		{
	    	try
	    	{
				eq = em.GetMessageQueue(ParticipantId);

	    	} // try

	    	catch (Exception e)
	    	{
				 throw new GetMessageException( "Error getting message" + e );

	    	} // catch

	    } else {

			throw new ParticipantNotRegisteredException( "Participant not registered" );

		} // if

		return eq;

	} // GetMessageQueue

	/***************************************************************************
	* CONCRETE METHOD:: UnRegister
	* Purpose: This method is called when the object is no longer used. Essentially
	* this method unregisters participants from the message manager. It is important
	* that participants actively unregister with the message manager. Failure to do
	* so will cause unconnected queues to fill up with messages over time. This
	* will result in a memory leak and eventual failure of the message manager.
	*
	* Arguments: None.
	*
	* Returns: None.
	*
	* Exceptions: None
	*
	****************************************************************************/

	public void UnRegister() throws ParticipantNotRegisteredException, RegistrationException
	{
		if (ParticipantId != -1)
		{
		   	try
		   	{
				em.UnRegister(ParticipantId);

		   	} // try

		   	catch (Exception e)
		   	{
				throw new RegistrationException( "Error unregistering" + e );

		    } // catch

	    } else {

			throw new ParticipantNotRegisteredException( "Participant not registered" );

		} // if

	} // UnRegister


	/*=================================================================================

				Team IAP Additions to the MessageManagerInterface

=================================================================================*/



    /***************************************************************************
	* CONCRETE METHOD:: SendConnect
	* Purpose: This method posts a registration message that will signal the
	*		   		the service maintenance console that this item is connected to 
	*		   		the system as well as send the the monitor device information
	*				like name and description.
	*
	* Arguments: int msgID - a two digit integer value uniquely indentifying 
	* 				the sensor/controller/console. It is important to note the
	*				"frequencies" that are used to broadcast connecting, heart
	*				beats, and disconnecting on the system. 
	*
	*				-- Two Digit Id's --
	*				01-33: reserved for Sensors
	*				34-66: reserved for controllers
	*				67-99: reserved for consoles
	*				
	*				-- MessageId's (Frequencies) --
	*				1XX: connecting to the network
	*				2XX: heart beats on the network
	*				300: disconnecting from the network
	*
	*			 String text - a short message containing the device name and
	*							description.
							Format: deviceName - deviceDescription
	*
	* Returns: none
	*
	* Exceptions: Posting to message manager exception
	*
	***************************************************************************/
	public void SendConnect(int componentId, String name, String description) {
        // Here we create a registration message
        int connectId = 100 + componentId;

        Message msg = new Message(connectId,name+"-"+description);

        // Attempts to send initial connection message
        try {
            this.SendMessage( msg );

        } catch (Exception e) {
            System.out.println("Error connecting to the system  " + e);

        } // catch
    } // SendConnect


    /***************************************************************************
	* CONCRETE METHOD:: SendHeartBeat
	* Purpose: This method posts a heartbeat to the service maintenance monitor
	*				letting it know that it is operating properly.
	*
	* Arguments: int msgID - a two digit integer value uniquely indentifying 
	* 				the sensor/controller/console. It is important to note the
	*				"frequencies" that are used to broadcast connecting, heart
	*				beats, and disconnecting on the system. 
	*
	*				-- Two Digit Id's --
	*				01-33: reserved for Sensors
	*				34-66: reserved for controllers
	*				67-99: reserved for consoles
	*				
	*				-- MessageId's (Frequencies) --
	*				1XX: connecting to the network
	*				2XX: heart beats on the network
	*				300: disconnecting from the network
	*
	*
	* Returns: none
	*
	* Exceptions: Posting to message manager exception
	*
	***************************************************************************/
    public void SendHeartBeat(int componentId) {
        // Here we create a registration message
        int heartBeatId = 200 + componentId;

        Message msg = new Message(heartBeatId);

        // Attempts to send initial connection message
        try {
            this.SendMessage( msg );

        } catch (Exception e) {
            System.out.println("Error sending heartbeat to the system  " + e);

        } // catch
    } // SendHeartBeat


    /***************************************************************************
	* CONCRETE METHOD:: SendDisconnect
	* Purpose: This method posts a disconnect message that will signal the
	*		   the service maintenance console that it is being taken offline and
	*		   not to stay up late waiting up for it.
	*		   the system
	*
	* Arguments: int msgID - a two digit integer value uniquely indentifying 
	* 				the sensor/controller/console. It is important to note the
	*				"frequencies" that are used to broadcast connecting, heart
	*				beats, and disconnecting on the system. 
	*
	*				-- Two Digit Id's --
	*				01-33: reserved for Sensors
	*				34-66: reserved for controllers
	*				67-99: reserved for consoles
	*				
	*				-- MessageId's (Frequencies) --
	*				1XX: connecting to the network
	*				2XX: heart beats on the network
	*				300: disconnecting from the network
	*
	*
	* Returns: none
	*
	* Exceptions: Posting to message manager exception
	*
	***************************************************************************/
    public void SendDisconnect(int componentId) {
        // Here we create a unregister message
        int disconnectId = 300 + componentId;

        Message msg = new Message(disconnectId);

        // Attempts to send message telling the system that it's being unregistered
        try {
            this.SendMessage( msg );
        } catch (Exception e) {
            System.out.println("Error disconnecting from the system  " + e);

        } // catch
    } // SendDisconnect

} // MessageManagerInterface