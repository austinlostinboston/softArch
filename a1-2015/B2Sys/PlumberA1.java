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
public class PlumberA1
{
   public static void main(String argv[]) throws Exception
   {
		/****************************************************************************
		* Here we instantiate three filters.
		****************************************************************************/
		ArrayList<Integer> hs=new ArrayList<Integer>();
		hs.add(0);
		hs.add(1);
		hs.add(2);
		hs.add(3);
		hs.add(4);
		hs.add(5);


		SourceFilter Filter1 = new SourceFilter("../DataSets/FlightData.dat");
		MiddleFilter Filter2 = new MiddleFilter();
		SinkFilter Filter3 = new SinkFilter("OutputA.dat",hs);

		/****************************************************************************
		* Here we connect the filters starting with the sink filter (Filter 1) which
		* we connect to Filter2 the middle filter. Then we connect Filter2 to the
		* source filter (Filter3).
		****************************************************************************/
		
		Filter3.Connect(Filter2,0,0); // This esstially says, "connect Filter3 input port to Filter2 output port
		Filter2.Connect(Filter1,0,0); // This esstially says, "connect Filter2 intput port to Filter1 output port

		/****************************************************************************
		* Here we start the filters up. All-in-all,... its really kind of boring.
		****************************************************************************/

		Filter1.setName("source thread");
		Filter1.start();
		Filter2.setName("Middle thread");
		Filter2.start();
		Filter3.setName("Sink thread");
		Filter3.start();
   } // main

} // Plumber