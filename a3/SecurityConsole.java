/******************************************************************************************************************
* File:ECSConsole.java
* Course: 17655
* Project: Assignment 3
* Copyright: Copyright (c) 2009 Carnegie Mellon University
* Versions:
*	1.0 February 2009 - Initial rewrite of original assignment 3 (ajl).
*
* Description: This class is the console for the museum environmental control system. This process consists of two
* threads. The ECSMonitor object is a thread that is started that is responsible for the monitoring and control of
* the museum environmental systems. The main thread provides a text interface for the user to change the temperature
* and humidity ranges, as well as shut down the system.
*
* Parameters: None
*
* Internal Methods: None
*
******************************************************************************************************************/
import TermioPackage.*;
import MessagePackage.*;

public class SecurityConsole
{
	
	
	public static void main(String args[])
	{
		SecurityConsole sc=new SecurityConsole(args);
	}
	
	Termio UserInput = new Termio();	// Termio IO Object
	boolean Done = false;				// Main loop flag
	String Option = null;				// Menu choice from user
	Message Msg = null;					// Message object
	boolean Error = false;				// Error flag
	SecurityMonitor iMonitor = null;
	//FireMonitor fMonitor=null;
	boolean intrusionON=true;
	boolean wON=false;
	boolean dON=false;
	boolean mON=false;
	boolean fireON=false;
	boolean sprinklerON=false;
	String[] args;
	GetOption gp;
	String inputModel="N";
	Thread thread1;
	Thread thread2;

	int ComponentId = 36;				// An id used to identify this as a console to the service maintenance console
	String ServeMaintName = "Intrusion Alarm";		// A name to display in the Service Maintenance Console
	String ServeMaintDesc = "Triggers security alarm"; // Description

	public SecurityConsole(String[] args) {
		// TODO Auto-generated constructor stub
	
		inputModel="N";
		// The environmental control system monitor
		//float TempRangeHigh = (float)100.0;	// These parameters signify the temperature and humidity ranges in terms
		//float TempRangeLow = (float)0.0;	// of high value and low values. The ECSmonitor will attempt to maintain
		//float HumiRangeHigh = (float)100.0;	// this temperature and humidity. Temperatures are in degrees Fahrenheit
		//float HumiRangeLow = (float)0.0;	// and humidity is in relative humidity percentage.

		/////////////////////////////////////////////////////////////////////////////////
		// Get the IP address of the message manager
		/////////////////////////////////////////////////////////////////////////////////
		this.args=args;
 		if ( args.length != 0 )
 		{
			// message manager is not on the local system

			iMonitor = new SecurityMonitor( args[0], this);
			//fMonitor=new FireMonitor(args[0],this);

		} else {

			iMonitor = new SecurityMonitor(this);
			//fMonitor=new FireMonitor(this);

		} // if
 		gp=new GetOption(this);
 		thread1=new Thread(gp);
 		thread1.start();
		// Here we check to see if registration worked. If ef is null then the
		// message manager interface was not properly created.

		if (iMonitor.IsRegistered())
		{
			iMonitor.start(); // Here we start the monitoring and control thread
			//fMonitor.start();
			System.out.println( "\n\n" );
			System.out.println( "Security Control System (ECS) Command Console: \n" );

			if (args.length != 0)
				System.out.println( "Using message manger at: " + args[0] + "\n" );
			else
				System.out.println( "Using local message manger \n" );

			normalOption();
			
		} else {

			System.out.println("\n\nUnable start the monitor.\n\n" );

		} // if

  	} // main

	
	public void normalOption(){
		
	
		if (intrusionON){
			System.out.println( "Security system is working now: \n" );
			System.out.println( "Select an Option: \n" );
			System.out.println( "1: Disarm the intrusion detection system" );
			//System.out.println( "2a: Alarm all the intrusion detection system" );
			//System.out.println( "2w: Alarm window break detection system" );
			//System.out.println( "2d: Alarm door break detection system" );
			//System.out.println( "2m: Alarm motion detection system" );
			//System.out.println( "3a: Silence all the intrusion alarms" );
			//System.out.println( "3w: DisAlarm window break detection system" );
			//System.out.println( "3d: DisAlarm door break detection system" );
			//System.out.println( "3m: DisAlarm motion detection system" );
		}else{
			System.out.println( "Security system is not working now: \n" );
			System.out.println( "Select an Option: \n" );
			//System.out.println( "1: Enable the intrusion detection system" );
			System.out.println( "1: Enable the intrusion detection system" );
			//System.out.println( "2a: Alarm all the intrusion detection system" );
			//System.out.println( "2w: Alarm window break detection system" );
			//System.out.println( "2d: Alarm door break detection system" );
			//System.out.println( "2m: Alarm motion detection system" );
			//System.out.println( "3a: DisAlarm all the intrusion detection system" );
			//System.out.println( "3w: DisAlarm window break detection system" );
			//System.out.println( "3d: DisAlarm door break detection system" );
			//System.out.println( "3m: DisAlarm motion detection system" );
		}
		
//		if (fireON){
//			System.out.println( "Fire alarm is ringing! \n" );
//			System.out.println( "Select an Option: \n" );
//			System.out.println( "4: Stop the fire alarm" );
//		}
		if (sprinklerON){
			System.out.println( "\nSprinkler is working!" );
			System.out.println( "5: Stop sprinkler" );
		}
		
		
		//System.out.println( "3: Alarm the fire detection system" );
		//System.out.println( "4: Disalarm the fire detection system" );
		System.out.println( "\n\nX: Stop System\n" );
		System.out.print( "\n>>>> " );
		
		inputModel="N";
		//new Thread(gp).start();
	}
	
	public void fireOption(){
		
		if (inputModel.equals("F") || sprinklerON)return;
		
		System.out.println( "Attention! Fire detected! \n" );
		System.out.println( "Attention! Fire detected! \n" );
		System.out.println( "Attention! Fire detected! \n" );

		System.out.println( "Do you want to turn on Sprinkler? \n" );
		System.out.println( "(If no selection in 10s, sprinkler will turn on!) \n" );
		System.out.println( "Select an Option: \n" );
		System.out.println( "1: Yes" );
		System.out.println( "2: No" );
		System.out.print( "\n>>>> " );
		if (thread2==null || !thread2.isAlive()){
			thread2=new Thread(new Timer(this));
			thread2.start();
			
		}
		inputModel="F";
		waiting=true;
	}
	
	

	boolean waiting=false;
	public void selected(String option2) {

		waiting=false;
		if (inputModel.equals("F")){
			////////////option 1 ////////////
			if ( option2.equals( "1" ) )
			{
				System.out.println( "Selection confirmed! Turn on sprinkler!" );
				
				sprinklerOn();
				sprinklerON=true;
				normalOption();
			}
			//////////// option 2 ////////////
	
			else if ( option2.equals( "2" ) )
			{
				System.out.println( "Selection confirmed! Not turn on sprinkler!" );
				sprinklerON=false;
				sprinklerOff();
				normalOption();
			} // if
			else{
				System.out.println( "Wrong input! Select again!" );
				fireOption();
				
			}
			return;
		}
		
		
		//////////// option 1 ////////////

		if ( option2.equals( "1" ) )
		{
			
			if (intrusionON){
				intrusionON=false;
				//dON=false;
				//wON=false;
				//mON=false;
				System.out.println( "Intrusion detection system stops" );
				//iMonitor.stopAllAlarms();
				iMonitor.setIntrusionAlarm(false);
				normalOption();
			}else{
				
				intrusionON=true;
				System.out.println( "Intrusion detection system starts" );
				iMonitor.setIntrusionAlarm(true);
				normalOption();
				
			}
			
		}

		//////////// option 2 ////////////

//		else if ( option2.equals( "2a" ) )
//		{
//		
//			if (!intrusionON){
//				System.out.println( "Intrusion detection system is not working!" );
//				System.out.println( "Please enable the intrusion detection system first!" );
//				normalOption();
//			}else{
//				dON=true;
//				wON=true;
//				mON=true;
//				iMonitor.startAllAlarms();
//				System.out.println( "Manually start all the intrusion alarms!" );
//				normalOption();
//			}
//
//		} // if
//
//		else if ( option2.equals( "2w" ) )
//		{
//		
//			if (!intrusionON){
//				System.out.println( "Intrusion detection system is not working!" );
//				System.out.println( "Please enable the intrusion detection system first!" );
//				normalOption();
//			}else{
//				//dON=true;
//				wON=true;
//				//mON=true;
//				iMonitor.startWAlarms();
//				System.out.println( "Manually start window break alarm!" );
//				normalOption();
//			}
//
//		}
//		else if ( option2.equals( "2m" ) )
//		{
//		
//			if (!intrusionON){
//				System.out.println( "Intrusion detection system is not working!" );
//				System.out.println( "Please enable the intrusion detection system first!" );
//				normalOption();
//			}else{
//				//dON=true;
//				//wON=true;
//				mON=true;
//				iMonitor.startMAlarms();
//				System.out.println( "Manually start motion alarm!" );
//				normalOption();
//			}
//
//		}
//		else if ( option2.equals( "2d" ) )
//		{
//		
//			if (!intrusionON){
//				System.out.println( "Intrusion detection system is not working!" );
//				System.out.println( "Please enable the intrusion detection system first!" );
//				normalOption();
//			}else{
//				dON=true;
//				//wON=true;
//				//mON=true;
//				iMonitor.startDAlarms();
//				System.out.println( "Manually start door break alarm!" );
//				normalOption();
//			}
//
//		}
//		//TODO
//		else if ( option2.equals( "3a" ) )
//		{
//		
//			if (!intrusionON){
//				System.out.println( "Intrusion detection system is not working!" );
//				System.out.println( "Please enable the intrusion detection system first!" );
//				normalOption();
//			}else{
//				dON=true;
//				wON=true;
//				mON=true;
//				iMonitor.stopAllAlarms();
//				System.out.println( "Manually stop all the intrusion alarms!" );
//				normalOption();
//			}
//
//		} // if
//		
//		
//		
//		
//		else if ( option2.equals( "4" ) )
//		{
//		
//			
//			fireON=false;
//			System.out.println( "Fire alarm stops" );
//			fMonitor.stopAlarm(false);
//			normalOption();
//
//		} // if
		
		else if ( option2.equals( "5" ) )
		{
		
			
			sprinklerON=false;
			System.out.println( "Sprinkler stops" );
			sprinklerOff();
			normalOption();

		} // if
		
		//////////// option X ////////////

		else if ( option2.equalsIgnoreCase( "X" ) )
		{
			// Here the user is done, so we set the Done flag and halt
			// the environmental control system. The monitor provides a method
			// to do this. Its important to have processes release their queues
			// with the message manager. If these queues are not released these
			// become dead queues and they collect messages and will eventually
			// cause problems for the message manager.

			thread1.interrupt();
			iMonitor.Halt();
			//fMonitor.Halt();
			Done = true;
			System.out.println( "\nConsole Stopped... Exit monitor mindow to return to command prompt." );
			iMonitor.Halt();
			//fMonitor.Halt();

		} // if
		else{
			System.out.println( "Wrong input! Select again!" );
			normalOption();
		}
	}


	private void sprinklerOff() {
		// TODO Auto-generated method stub
		iMonitor.sprinklerOff();

		
	}


	public void autoSprikler() {
		
		if (waiting){
			waiting=false;
			sprinklerOn();
			
		}
	}


	private void sprinklerOn() {
		// TODO Auto-generated method stub
		iMonitor.sprinklerOn();
		
	}
	
} // ECSConsole

class GetOption implements Runnable{
	
	
	SecurityConsole sc=null;
	public GetOption(SecurityConsole sc) {
		this.sc=sc;
	}

	@Override
	public void run() {
		while(!Thread.interrupted()){
			Termio UserInput = new Termio();	// Termio IO Object
			boolean Done = false;				// Main loop flag
			String Option = null;	
			Option = UserInput.KeyboardReadString();
			sc.selected(Option);
		}
	}
	
}

class Timer implements Runnable{

	
	SecurityConsole sc=null;
	public Timer(SecurityConsole sc) {
		this.sc=sc;
	}
	@Override
	public void run() {
		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		sc.autoSprikler();
	}
	
	
	
}
