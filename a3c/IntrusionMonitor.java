/******************************************************************************************************************
* File:ECSMonitor.java
* Course: 17655
* Project: Assignment A3
* Copyright: Copyright (c) 2009 Carnegie Mellon University
* Versions:
*	1.0 March 2009 - Initial rewrite of original assignment 3 (ajl).
*
* Description:
*
* This class monitors the environmental control systems that control museum temperature and humidity. In addition to
* monitoring the temperature and humidity, the ECSMonitor also allows a user to set the humidity and temperature
* ranges to be maintained. If temperatures exceed those limits over/under alarm indicators are triggered.
*
* Parameters: IP address of the message manager (on command line). If blank, it is assumed that the message manager is
* on the local machine.
*
* Internal Methods:
*	static private void Heater(MessageManagerInterface ei, boolean ON )
*	static private void Chiller(MessageManagerInterface ei, boolean ON )
*	static private void Humidifier(MessageManagerInterface ei, boolean ON )
*	static private void Dehumidifier(MessageManagerInterface ei, boolean ON )
*
******************************************************************************************************************/
import InstrumentationPackage.*;
import MessagePackage.*;
import java.util.*;

public class IntrusionMonitor extends Thread
{
	private MessageManagerInterface em = null;	// Interface object to the message manager
	private String MsgMgrIP = null;				// Message Manager IP address
	private float TempRangeHigh = 100;			// These parameters signify the temperature and humidity ranges in terms
	private float TempRangeLow = 0;				// of high value and low values. The ECSmonitor will attempt to maintain
	private float HumiRangeHigh = 100;			// this temperature and humidity. Temperatures are in degrees Fahrenheit
	private float HumiRangeLow = 0;				// and humidity is in relative humidity percentage.
	boolean Registered = true;					// Signifies that this class is registered with an message manager.
	MessageWindow mw = null;					// This is the message window
	Indicator ti;								// Temperature indicator
	Indicator hi;								// Humidity indicator

	private int ComponentId = 69;				// An id used to identify this as a console to the service maintenance console
	private String ServeMaintName = "Intrusion";		// A name to display in the Service Maintenance Console
	private String ServeMaintDesc = "Displays intrusion related info"; // Description

	//private boolean fireON=true;
	private boolean intrusionON=true;
	
	
	public IntrusionMonitor()
	{
		// message manager is on the local system

		try
		{
			// Here we create an message manager interface object. This assumes
			// that the message manager is on the local machine

			em = new MessageManagerInterface();

		}

		catch (Exception e)
		{
			System.out.println("SecurityMonitor::Error instantiating message manager interface: " + e);
			Registered = false;

		} // catch

	} //Constructor

	public IntrusionMonitor( String MsgIpAddress )
	{
		// message manager is not on the local system

		MsgMgrIP = MsgIpAddress;

		try
		{
			// Here we create an message manager interface object. This assumes
			// that the message manager is NOT on the local machine

			em = new MessageManagerInterface( MsgMgrIP );
		}

		catch (Exception e)
		{
			System.out.println("ECSMonitor::Error instantiating message manager interface: " + e);
			Registered = false;

		} // catch

	} // Constructor

	public void run()
	{
		Message Msg = null;				// Message object
		MessageQueue eq = null;			// Message Queue
		int MsgId = 0;					// User specified message ID
		float CurrentTemperature = 0;	// Current temperature as reported by the temperature sensor
		float CurrentHumidity= 0;		// Current relative humidity as reported by the humidity sensor
		int	Delay = 1000;				// The loop delay (1 second)
		boolean Done = false;			// Loop termination flag
		boolean ON = true;				// Used to turn on heaters, chillers, humidifiers, and dehumidifiers
		boolean OFF = false;			// Used to turn off heaters, chillers, humidifiers, and dehumidifiers

		if (em != null)
		{
			// Now we create the ECS status and message panel
			// Note that we set up two indicators that are initially yellow. This is
			// because we do not know if the temperature/humidity is high/low.
			// This panel is placed in the upper left hand corner and the status
			// indicators are placed directly to the right, one on top of the other

			mw = new MessageWindow("Security Monitoring Console", 0, 0);
			ti = new Indicator ("TEMP UNK", mw.GetX()+ mw.Width(), 0);
			hi = new Indicator ("HUMI UNK", mw.GetX()+ mw.Width(), (int)(mw.Height()/2), 2 );

			mw.WriteMessage( "Registered with the message manager." );

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
				// Here we get our message queue from the message manager

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
				// We are looking for MessageIDs = 1 or 2. Message IDs of 1 are temperature
				// readings from the temperature sensor; message IDs of 2 are humidity sensor
				// readings. Note that we get all the messages at once... there is a 1
				// second delay between samples,.. so the assumption is that there should
				// only be a message at most. If there are more, it is the last message
				// that will effect the status of the temperature and humidity controllers
				// as it would in reality.

				int qlen = eq.GetSize();

				for ( int i = 0; i < qlen; i++ )
				{
					Msg = eq.GetMessage();

					if ( Msg.GetMessageId() == 101 ) // window break reading
					{
						try
						{
							
							if (intrusionON){
								AlarmOn(201);
							}

						} // try

						catch( Exception e )
						{
							mw.WriteMessage("Error getting window break event " + e);

						} // catch

					} // if

					if ( Msg.GetMessageId() == 102 ) // Humidity reading
					{
						try
						{
							if (intrusionON){
								AlarmOn(202);
							}

						} // try

						catch( Exception e )
						{
							mw.WriteMessage("Error getting door break event " + e);

						} // catch

					} // if

					if ( Msg.GetMessageId() == 103 ) // Humidity reading
					{
						try
						{
							if (intrusionON){
								AlarmOn(203);
							}
							

						} // try

						catch( Exception e )
						{
							mw.WriteMessage("Error getting motion event " + e);

						} // catch

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

						hi.dispose();
						ti.dispose();

					} // if

				} // for

				

				// Check humidity and effect control as necessary

				

				// This delay slows down the sample rate to Delay milliseconds

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

	

	private void AlarmOn(int signal) {
		Message msg;

		if ( signal==201 )
		{
			msg = new Message( 200, "W1" );

		} else if (signal == 202){

			msg = new Message( 200, "D1" );

		} else if (signal == 203){

			msg = new Message( 200, "M1" );

		} else{
			msg = new Message( 210, "F" );
		}

		// Here we send the message to the message manager.

		try
		{
			em.SendMessage( msg );

		} // try

		catch (Exception e)
		{
			System.out.println("Error sending alarm message::  " + e);

		} // catch
		
	}

	/***************************************************************************
	* CONCRETE METHOD:: IsRegistered
	* Purpose: This method returns the registered status
	*
	* Arguments: none
	*
	* Returns: boolean true if registered, false if not registered
	*
	* Exceptions: None
	*
	***************************************************************************/

	public boolean IsRegistered()
	{
		return( Registered );

	} // SetTemperatureRange

	public void Halt()
	{
		mw.WriteMessage( "***HALT MESSAGE RECEIVED - SHUTTING DOWN SYSTEM***" );

		// Here we create the stop message.

		Message msg;

		msg = new Message( (int) 99, "XXX" );

		// Here we send the message to the message manager.

		try
		{
			em.SendMessage( msg );

		} // try

		catch (Exception e)
		{
			System.out.println("Error sending halt message:: " + e);

		} // catch

	} // Halt


	

	public void setIntrusionAlarm(boolean b) {
		this.intrusionON=b;
		
	}

	public void startWAlarms() {
		Message msg=new Message(200, "W1");
		try
		{
			em.SendMessage( msg );
		} // try
		catch (Exception e)
		{
			System.out.println("Error sending alarm message::  " + e);
		} // catch
		
	}

	public void startMAlarms() {
		Message msg=new Message(200, "M1");
		try
		{
			em.SendMessage( msg );
		} // try
		catch (Exception e)
		{
			System.out.println("Error sending alarm message::  " + e);
		} // catch
		
	}

	public void startDAlarms() {
		Message msg=new Message(200, "D1");
		try
		{
			em.SendMessage( msg );
		} // try
		catch (Exception e)
		{
			System.out.println("Error sending alarm message::  " + e);
		} // catch
		
	}

	public void stopAllAlarms() {
		
		stopWAlarms();
		stopMAlarms();
		stopDAlarms();
		
	}

	public void stopDAlarms() {
		Message msg=new Message(200, "D0");
		try
		{
			em.SendMessage( msg );
		} // try
		catch (Exception e)
		{
			System.out.println("Error sending alarm message::  " + e);
		} // catch
		
	}

	public void stopMAlarms() {
		Message msg=new Message(200, "M0");
		try
		{
			em.SendMessage( msg );
		} // try
		catch (Exception e)
		{
			System.out.println("Error sending alarm message::  " + e);
		} // catch
		
	}

	public void stopWAlarms() {
		Message msg=new Message(200, "W0");
		try
		{
			em.SendMessage( msg );
		} // try
		catch (Exception e)
		{
			System.out.println("Error sending alarm message::  " + e);
		} // catch
		
	}

	public void startAllAlarms() {
		startDAlarms();
		startWAlarms();
		startMAlarms();
		
		
	}

} // ECSMonitor