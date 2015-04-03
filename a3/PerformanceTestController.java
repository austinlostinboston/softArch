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

import java.io.IOException;
import java.util.*;

class PerformanceTestController implements Runnable
{
	
	Stat stat=null;
	
	Message Msg = null;				// Message object
	MessageQueue eq = null;			// Message Queue
	int MsgId = 0;					// User specified message ID
	MessageManagerInterface em = null;// Interface object to the message manager
	boolean HeaterState = false;	// Heater state: false == off, true == on
	boolean ChillerState = false;	// Chiller state: false == off, true == on
	float CurrentTemperature;		// Current simulated ambient room temperature
	float DriftValue;				// The amount of temperature gained or lost
	int	Delay = 2500;				// The loop delay (2.5 seconds)
	boolean Done = false;
	int myID=-1;
	
	int count=0;
	long sum=0;
	
	int expire=0;
	int limit=5;
	
	int threads=0;
	
	public PerformanceTestController(String MsgMgrIP, int myID, int d, int limit, Stat ss, int tt){
		Delay=d;
		threads=tt;
		stat=ss;
		this.limit=limit;
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
			while ( !Done )
			{
				// Post the current temperature
				try
				{
					eq = em.GetMessageQueue();

				} // try

				catch( Exception e )
				{
					//mw.WriteMessage("Error getting message queue::" + e );

				} 
				
				int qlen = eq.GetSize();
				if (qlen==0){
					expire++;
					if (expire==limit){
						break;
					}
				}
				for ( int i = 0; i < qlen; i++ )
				{
					Msg = eq.GetMessage();

					if ( Msg.GetMessageId() == myID )
					{
						
						long s=Long.parseLong(Msg.GetMessage());
						long t=System.currentTimeMillis();
						count++;
						sum+=t-s;

					} // if
				} // for

				
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
		
		try {
			stat.add(threads,Delay,count, sum);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		
	}
	
	
	

} // TemperatureSensor