/******************************************************************************************************************
* File:HumidityController.java
* Course: 17655
* Project: Assignment A3
* Copyright: Copyright (c) 2009 Carnegie Mellon University
* Versions:
*	1.0 March 2009 - Initial rewrite of original assignment 3 (ajl).
*
* Description:
*
* This class simulates a device that controls a humidifier and dehumidifier. It polls the message manager for message
* ids = 4 and reacts to them by turning on or off the humidifier/dehumidifier. The following command are valid
* strings for controlling the humidifier and dehumidifier:
*
*	H1 = humidifier on
*	H0 = humidifier off
*	D1 = dehumidifier on
*	D0 = dehumidifier off
*
* The state (on/off) is graphically displayed on the terminal in the indicator. Command messages are displayed in
* the message window. Once a valid command is recieved a confirmation message is sent with the id of -5 and the command in
* the command string.
*
* Parameters: IP address of the message manager (on command line). If blank, it is assumed that the message manager is
* on the local machine.
*
* Internal Methods:
*	static private void ConfirmMessage(MessageManagerInterface ei, String m )
*
******************************************************************************************************************/
import InstrumentationPackage.*;
import MessagePackage.*;
import java.util.*;

public class IntrusionAlarmController
{
	public static void main(String args[])
	{
		String MsgMgrIP;					// Message Manager IP address
		Message Msg = null;					// Message object
		MessageQueue eq = null;				// Message Queue
		int MsgId = 0;						// User specified message ID
		MessageManagerInterface em = null;	// Interface object to the message manager
		
		int	Delay = 2500;					// The loop delay (2.5 seconds)
		boolean Done = false;				// Loop termination flag

		boolean wAlarm=false;
		boolean dAlarm=false;
		boolean mAlarm=false;

		int ComponentId = 36;				// An id used to identify this as a console to the service maintenance console
		String ServeMaintName = "Intrusion Alarm";		// A name to display in the Service Maintenance Console
		String ServeMaintDesc = "Triggers security alarm"; // Description
		
		
		/////////////////////////////////////////////////////////////////////////////////
		// Get the IP address of the message manager
		/////////////////////////////////////////////////////////////////////////////////

 		if ( args.length == 0 )
 		{
			// message manager is on the local system

			System.out.println("\n\nAttempting to register on the local machine..." );

			try
			{
				// Here we create an message manager interface object. This assumes
				// that the message manager is on the local machine

				em = new MessageManagerInterface();
			}

			catch (Exception e)
			{
				System.out.println("Error instantiating message manager interface: " + e);

			} // catch

		} else {

			// message manager is not on the local system

			MsgMgrIP = args[0];

			System.out.println("\n\nAttempting to register on the machine:: " + MsgMgrIP );

			try
			{
				// Here we create an message manager interface object. This assumes
				// that the message manager is NOT on the local machine

				em = new MessageManagerInterface( MsgMgrIP );
			}

			catch (Exception e)
			{
				System.out.println("Error instantiating message manager interface: " + e);

			} // catch

		} // if

		// Here we check to see if registration worked. If em is null then the
		// message manager interface was not properly created.

		if (em != null)
		{
			System.out.println("Registered with the message manager." );

			/* Now we create the humidity control status and message panel
			** We put this panel about 2/3s the way down the terminal, aligned to the left
			** of the terminal. The status indicators are placed directly under this panel
			*/

			float WinPosX = 0.0f; 	//This is the X position of the message window in terms
									//of a percentage of the screen height
			float WinPosY = 0.60f;	//This is the Y position of the message window in terms
								 	//of a percentage of the screen height

			MessageWindow mw = new MessageWindow("Intrusion Alarm Controller Status Console", WinPosX, WinPosY);

			// Now we put the indicators directly under the humitity status and control panel

			//Indicator hi = new Indicator ("Humid OFF", mw.GetX(), mw.GetY()+mw.Height());
			//Indicator di = new Indicator ("DeHumid OFF", mw.GetX()+(hi.Width()*2), mw.GetY()+mw.Height());

			mw.WriteMessage("Registered with the message manager." );

	    	try
	    	{
	    		// Attempts initial connection/registration to the system
				em.SendConnect(ComponentId,ServeMaintName,ServeMaintDesc);

				mw.WriteMessage("   Participant id: " + em.GetMyId() );
				mw.WriteMessage("   Registration Time: " + em.GetRegistrationTime() );

			} // try

	    	catch (Exception e)
			{
				System.out.println("Error:: " + e);

			} // catch

			/********************************************************************
			** Here we start the main simulation loop
			*********************************************************************/

			while ( !Done )
			{
				try
				{
					// Here we send a heartbeat to let the system know that it's working
					em.SendHeartBeat(ComponentId);

					eq = em.GetMessageQueue();

				} // try

				catch( Exception e )
				{
					mw.WriteMessage("Error getting message queue::" + e );

				} // catch

				// If there are messages in the queue, we read through them.
				// We are looking for MessageIDs = 4, this is a request to turn the
				// humidifier or dehumidifier on/off. Note that we get all the messages
				// at once... there is a 2.5 second delay between samples,.. so
				// the assumption is that there should only be a message at most.
				// If there are more, it is the last message that will effect the
				// output of the humidity as it would in reality.

				int qlen = eq.GetSize();

				for ( int i = 0; i < qlen; i++ )
				{
					Msg = eq.GetMessage();

					if ( Msg.GetMessageId() == 101 )
					{
						mw.WriteMessage("Alarm!!! Window break!!!" );

					} // if
					
					if ( Msg.GetMessageId() == 102 )
					{
						mw.WriteMessage("Alarm!!! Door break!!!" );

					} // if
					
					if ( Msg.GetMessageId() == 103 )
					{
						mw.WriteMessage("Alarm!!! Motion detected!!!" );

					} // if

					// If the message ID == 99 then this is a signal that the simulation
					// is to end. At this point, the loop termination flag is set to
					// true and this process unregisters from the message manager.

					if ( Msg.GetMessageId() == 99 )
					{
						Done = true;

						try
						{
							// Sends disconnect message when device is intentially stopped
							em.SendDisconnect(ComponentId);
							
							em.UnRegister();

				    	} // try

				    	catch (Exception e)
				    	{
							mw.WriteMessage("Error unregistering: " + e);

				    	} // catch

				    	mw.WriteMessage( "\n\nSimulation Stopped. \n");

						// Get rid of the indicators. The message panel is left for the
						// user to exit so they can see the last message posted.

						//hi.dispose();
						//di.dispose();

					} // if

				} // for

				// Update the lamp status

//				if (wAlarm){
//					mw.WriteMessage("Alarm! Window Break! Alarm!" );
//				}
//				if (dAlarm){
//					mw.WriteMessage("Alarm! Door Break! Alarm!" );
//				}
//				if (mAlarm){
//					mw.WriteMessage("Alarm! Motion Detected! Alarm!" );
//				}
				
				
				try
				{
					Thread.sleep( Delay );

				} // try

				catch( Exception e )
				{
					System.out.println( "Sleep error:: " + e );

				} // catch

			} // while

		} else {

			System.out.println("Unable to register with the message manager.\n\n" );

		} // if

	} // main

	

} // HumidityControllers