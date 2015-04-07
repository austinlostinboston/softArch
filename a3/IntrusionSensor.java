/******************************************************************************************************************
* File:TemperatureSensor.java
* Course: 17655
* Project: Assignment A3
* Copyright: Copyright (c) 2009 Carnegie Mellon University
* Versions:
*	1.0 March 2009 - Initial rewrite of original assignment 3 (ajl).
*
* Description:
*
* This class simulates a temperature sensor. It polls the message manager for messages corresponding to changes in state
* of the heater or chiller and reacts to them by trending the ambient temperature up or down. The current ambient
* room temperature is posted to the message manager.
*
* Parameters: IP address of the message manager (on command line). If blank, it is assumed that the message manager is
* on the local machine.
*
* Internal Methods:
*	float GetRandomNumber()
*	boolean CoinToss()
*   void PostTemperature(MessageManagerInterface ei, float temperature )
*
******************************************************************************************************************/
import InstrumentationPackage.*;
import MessagePackage.*;

import java.util.*;

public class IntrusionSensor
{
	public static void main(String args[])
	{
		String MsgMgrIP;				// Message Manager IP address
		Message Msg = null;				// Message object
		MessageQueue eq = null;			// Message Queue
		int MsgId = 0;					// User specified message ID
		MessageManagerInterface em = null;// Interface object to the message manager
		int	Delay = 2500;				// The loop delay (2.5 seconds)
		boolean Done = false;			// Loop termination flag
		boolean systemON=true;			//flag intrusion system is on/off
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
			MessageWindow mw = new MessageWindow("Intrusion Sensor", WinPosX, WinPosY );

			mw.WriteMessage("Registered with the message manager." );

	    	try
	    	{
				mw.WriteMessage("   Participant id: " + em.GetMyId() );
				mw.WriteMessage("   Registration Time: " + em.GetRegistrationTime() );

			} // try

	    	catch (Exception e)
			{
				mw.WriteMessage("Error:: " + e);

			} // catch
	    	//TODO
			mw.WriteMessage("\nInitializing Door Break Simulation::" );

			//CurrentTemperature = (float)50.00;

			
			
			
			

			/********************************************************************
			** Here we start the main simulation loop
			*********************************************************************/

			mw.WriteMessage("Beginning Simulation... ");


			while ( !Done )
			{
				
				try
				{
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

					//receive message to close sensor
					if ( Msg.GetMessageId() == -70 )
					{
						mw.WriteMessage("Stop sensor" );
						systemON=false;
					} // if

					// start sensor
					if ( Msg.GetMessageId() == 70 )
					{
						mw.WriteMessage("Start sensor" );
						systemON=true;
					} // if
					
					// If the message ID == 99 then this is a signal that the simulation
					// is to end. At this point, the loop termination flag is set to
					// true and this process unregisters from the message manager.

					if ( Msg.GetMessageId() == 99 )
					{
						Done = true;

						try
						{
							em.UnRegister();

				    	} // try

				    	catch (Exception e)
				    	{
							mw.WriteMessage("Error unregistering: " + e);

				    	} // catch

				    	mw.WriteMessage("\n\nSimulation Stopped. \n");

					} // if

				} // for

				// Now we trend the temperature according to the status of the
				// heater/chiller controller.
				if (systemON){
					if (DoorBroken()){
						//TODO
						mw.WriteMessage("   Door Break Detected!" );
						//mw.WriteMessage("   Drift Value Set:: " + D);
						PostDoorBreak( em );
					}
					if (Motion()){
						//TODO
						mw.WriteMessage("   Motion Detected!" );
						//mw.WriteMessage("   Drift Value Set:: " + D);
						PostMotion( em );
					}
					if (WindowBroken()){
						//TODO
						mw.WriteMessage("   Window Break Detected!" );
						//mw.WriteMessage("   Drift Value Set:: " + D);
						PostWindowBreak( em );
					}
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

	private static boolean DoorBroken() {
		//TODO
		if (Math.random()<0.2){
			return true;
		}
		return false;
	}
	
	private static void PostDoorBreak(MessageManagerInterface em) {
		Message msg = new Message( (int) 102);

		// Here we send the message to the message manager.

		try
		{
			em.SendMessage( msg );
			//System.out.println( "Sent Temp Message" );

		} // try

		catch (Exception e)
		{
			System.out.println( "Error Posting Door Break:: " + e );

		} // catch
		
	}

	private static boolean Motion() {
		//TODO
		if (Math.random()<0.2){
			return true;
		}
		return false;
	}
	
	
	private static void PostMotion(MessageManagerInterface em) {
		Message msg = new Message( (int) 103);

		// Here we send the message to the message manager.

		try
		{
			em.SendMessage( msg );
			//System.out.println( "Sent Temp Message" );

		} // try

		catch (Exception e)
		{
			System.out.println( "Error Posting Motion:: " + e );

		} // catch
		
	}
	
	private static boolean WindowBroken() {
		//TODO
		if (Math.random()<0.2){
			return true;
		}
		return false;
	}
	/*
	 * window break 101
	 */
	//TODO
	private static void PostWindowBreak(MessageManagerInterface em) {
		Message msg = new Message( (int) 101);

		// Here we send the message to the message manager.

		try
		{
			em.SendMessage( msg );
			//System.out.println( "Sent Temp Message" );

		} // try

		catch (Exception e)
		{
			System.out.println( "Error Posting Window Break:: " + e );

		} // catch
		
	}

	
} // TemperatureSensor