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
public class Plumber
{
   public static void main( String argv[]) throws Exception
   {
		/****************************************************************************
		* Here we instantiate three filters.
		****************************************************************************/

		SourceFilter Filter1 = new SourceFilter();
		WildPointFilter Filter2 = new WildPointFilter();
		SinkFilter Filter3 = new SinkFilter();
		WildPointSinkFilter Filter4 = new WildPointSinkFilter();

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
		Filter4.Connect(Filter2, 0, 1);

		/****************************************************************************
		* Here we start the filters up. All-in-all,... its really kind of boring.
		****************************************************************************/
		Filter1.start();
		Filter2.start();
		Filter3.start();
		Filter4.start();
   } // main

} // Plumber