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

public class PerformanceTestSensor implements Runnable
{
	Message Msg = null;				// Message object
	MessageQueue eq = null;			// Message Queue
	int MsgId = 0;					// User specified message ID
	MessageManagerInterface em = null;// Interface object to the message manager
	boolean HeaterState = false;	// Heater state: false == off, true == on
	boolean ChillerState = false;	// Chiller state: false == off, true == on
	long curTime;		// Current simulated ambient room temperature
	//float DriftValue;				// The amount of temperature gained or lost
	int	Delay = 2500;				// The loop delay (2.5 seconds)
	boolean Done = false;
	int myID=-1;
	
	int count=0;
	
	public PerformanceTestSensor(String MsgMgrIP, int myID, int count, int d){
		Delay=d;
		this.count=count;
		this.myID=myID;
		
		if ( MsgMgrIP == null )
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
	}
	public void run() {
			
			
		
		if (em != null)
		{

			// We create a message window. Note that we place this panel about 1/2 across
			//System.out.println(count);
			for (int i=0;i<count;i++)
			{
				// Post the current temperature
				curTime=System.currentTimeMillis();
				PostData();
				
				try
				{
					Thread.sleep( Delay );

				} // try

				catch( Exception e )
				{
					
				} // catch

			} // while

		} else {

			System.out.println("Unable to register with the message manager.\n\n" );

		} // if
	}
	
	private void PostData() {
		// Here we create the message.

				Message msg = new Message( myID, String.valueOf(curTime) );

				// Here we send the message to the message manager.

				try
				{
					em.SendMessage( msg );
					//System.out.println( "Sent Temp Message" );

				} // try

				catch (Exception e)
				{
					System.out.println( "Error Posting Temperature:: " + e );

				} // catch
		
	}

	

} // TemperatureSensor