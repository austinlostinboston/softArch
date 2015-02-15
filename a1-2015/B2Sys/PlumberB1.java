import java.util.*;
/******************************************************************************************************************
* File:Plumber.java
* Course: 17655
* Project: Assignment 1
* Copyright: Copyright (c) 2003 Carnegie Mellon University
* Versions:
*	1.0 November 2008 - Sample Pipe and Filter code (ajl).
*
* Description:
*
* This class serves as an example to illstrate how to use the PlumberTemplate to create a main thread that
* instantiates and connects a set of filters. This example consists of three filters: a source, a middle filter
* that acts as a pass-through filter (it does nothing to the data), and a sink filter which illustrates all kinds
* of useful things that you can do with the input stream of data.
*
* Parameters: 		None
*
* Internal Methods:	None
*
******************************************************************************************************************/
public class PlumberB1
{
   public static void main( String argv[]) throws Exception
   {
		/****************************************************************************
		* Here we instantiate three filters.
		****************************************************************************/
		// Build a listArray that allows you to customize what pieces of information you
		// Include in your output.
		/*
			0 - TimeDate
			1 - Velocity
			2 - Altitude
			3 - Pressure
			4 - Temperature
			5 - Attitude
		*/
			// Defines the output order for a file
		ArrayList<Integer> timeTempAltPress = new ArrayList<Integer>();
		timeTempAltPress.add(0); // makes time the 1st column
		timeTempAltPress.add(4); // makes temp the 2nd column
		timeTempAltPress.add(2); // makes altitue the 3rd column
		timeTempAltPress.add(3); // makes pressure the 4th column

		// Defines the order of the WildPoints.dat file
		ArrayList<Integer> timePress = new ArrayList<Integer>();
		timePress.add(0); // makes time the the 1st column
		timePress.add(3); // makes pressure the 2nd column


		SourceFilter Filter1 = new SourceFilter("../DataSets/FlightData.dat");
		
		WildPointFilter Filter2 = new WildPointFilter();
		TempFilter Filter3 = new TempFilter();
		AltiFilter Filter4 = new AltiFilter();
		SinkFilter Filter5 = new SinkFilter("OutputB.dat",timeTempAltPress);
		SinkFilter Filter6 = new SinkFilter("WildPoints.dat",timePress);

		/****************************************************************************
		* Here we connect the filters starting with the sink filter (Filter 1) which
		* we connect to Filter2 the middle filter. Then we connect Filter2 to the
		* source filter (Filter3).
		****************************************************************************/
		/* The second and third arguments for the Connect command specify number of 
		 * input and output ports, respectively. Number format explained below.
		 * 0 - 1 port
		 * 1 - 2 ports
		 * 2 - 3 ports
		*/ 
		Filter2.Connect(Filter1, 0, 0);
		Filter3.Connect(Filter2, 0, 0);
		Filter4.Connect(Filter3, 0, 0);
		
		Filter5.Connect(Filter4, 0, 0);
		Filter6.Connect(Filter2, 0, 1);
		
		
		

		/****************************************************************************
		* Here we start the filters up. All-in-all,... its really kind of boring.
		****************************************************************************/
		Filter1.start();
		Filter2.start();
		Filter3.start();
		Filter4.start();
		Filter5.start();
		Filter6.start();
		
   } // main

} // Plumber