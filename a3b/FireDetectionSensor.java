/******************************************************************************************************************
* File:FireDetectionSensor.java
* Course: 17655
* Project: Assignment A3
* Copyright: Copyright (c) 2009 Carnegie Mellon University
* Versions:
*	1.0 March 2009 - Initial rewrite of original assignment 3 (ajl).
*
* Description:
*
* This class simulates a Fire DetectionSensor sensor.
*
* Parameters: IP address of the message manager (on command line). If blank, it is assumed that the message manager is
* on the local machine.
*
* Internal Methods:
*	boolean FireDetection()
*   void postFireDetection(MessageManagerInterface ei)
*
******************************************************************************************************************/
import InstrumentationPackage.*;
import MessagePackage.*;

import java.util.*;

public class FireDetectionSensor
{
	public static void main(String args[])
	{
		String MsgMgrIP;				// Message Manager IP address
		Message Msg = null;				// Message object
		MessageQueue eq = null;			// Message Queue
		int MsgId = 0;					// User specified message ID
		MessageManagerInterface em = null;// Interface object to the message manager
		
		//boolean ChillerState = false;	// Chiller state: false == off, true == on
		//float CurrentTemperature;		// Current simulated ambient room temperature
		//float DriftValue;				// The amount of temperature gained or lost
		int	Delay = 2500;				// The loop delay (2.5 seconds)
		boolean Done = false;			// Loop termination flag

		int ComponentId = 01;				// An id used to identify this as a console to the service maintenance console
		String ServeMaintName = "Fire";		// A name to display in the Service Maintenance Console
		String ServeMaintDesc = "Detects fire and sends signal"; // Description

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

		// Here we check to see if registration worked. If ef is null then the
		// message manager interface was not properly created.


		if (em != null)
		{

			// We create a message window. Note that we place this panel about 1/2 across
			// and 1/3 down the screen

			float WinPosX = 0.5f; 	//This is the X position of the message window in terms
								 	//of a percentage of the screen height
			float WinPosY = 0.3f; 	//This is the Y position of the message window in terms
								 	//of a percentage of the screen height
			//TODO
			MessageWindow mw = new MessageWindow("Fire Detection Sensor", WinPosX, WinPosY );

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
				mw.WriteMessage("Error:: " + e);

			} // catch
	    	//TODO
			mw.WriteMessage("\nInitializing Fire Detection Simulation::" );

			//CurrentTemperature = (float)50.00;

			
			
			
			

			/********************************************************************
			** Here we start the main simulation loop
			*********************************************************************/

			mw.WriteMessage("Beginning Simulation... ");


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
				// We are looking for MessageIDs = -5, this means the the heater
				// or chiller has been turned on/off. Note that we get all the messages
				// at once... there is a 2.5 second delay between samples,.. so
				// the assumption is that there should only be a message at most.
				// If there are more, it is the last message that will effect the
				// output of the temperature as it would in reality.

				int qlen = eq.GetSize();

				for ( int i = 0; i < qlen; i++ )
				{
					Msg = eq.GetMessage();

					

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

				    	mw.WriteMessage("\n\nSimulation Stopped. \n");

					} // if

				} // for


				if (FireDetection()){
					
					mw.WriteMessage("   Fire Detected!" );
					
					
					
					PostFireDetection( em );
				}
				
				try
				{
					Thread.sleep( Delay );

				} // try

				catch( Exception e )
				{
					mw.WriteMessage("Sleep error:: " + e );

				} // catch

			} // while

		} else {

			System.out.println("Unable to register with the message manager.\n\n" );

		} // if

	} // main

	private static boolean FireDetection() {
		//TODO
		if (Math.random()<0.2){
			return true;
		}
		return false;
	}

	private static void PostFireDetection(MessageManagerInterface em) {
		Message msg = new Message( (int) 111);

		// Here we send the message to the message manager.

		try
		{
			em.SendMessage( msg );
			//System.out.println( "Sent Temp Message" );

		} // try

		catch (Exception e)
		{
			System.out.println( "Error Posting FireDetection:: " + e );

		} // catch
		
	}
	
} // FireDetectionSensor