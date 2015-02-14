
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
public class PlumberA2
{
   public static void main( String argv[]) throws Exception
   {
		/****************************************************************************
		* Here we instantiate three filters.
		****************************************************************************/

		SourceFilter Filter1 = new SourceFilter();
		TempFilter Filter3 = new TempFilter();
		AltiFilter Filter4 = new AltiFilter();
		CopyFilter Filter2 = new CopyFilter(3);
		MergeFilter Filter5 = new MergeFilter(3);
		SinkFilter Filter6 = new SinkFilter();

		/****************************************************************************
		* Here we connect the filters starting with the sink filter (Filter 1) which
		* we connect to Filter2 the middle filter. Then we connect Filter2 to the
		* source filter (Filter3).
		****************************************************************************/
		Filter6.Connect(Filter5, 0, 0);
		Filter5.Connect(Filter4, 2, 0);
		Filter5.Connect(Filter3, 1, 0);
		Filter5.Connect(Filter2, 0, 2);
		Filter4.Connect(Filter2,0,1);
		Filter3.Connect(Filter2,0,0); // This esstially says, "connect Filter3 input port to Filter2 output port
		Filter2.Connect(Filter1,0,0); // This esstially says, "connect Filter2 intput port to Filter1 output port

		/****************************************************************************
		* Here we start the filters up. All-in-all,... its really kind of boring.
		****************************************************************************/

		Filter1.setName("source thread");
		Filter1.start();
		Filter2.setName("copy thread");
		Filter2.start();
		Filter3.setName("temp thread");
		Filter3.start();
		Filter4.setName("alti thread");
		Filter4.start();
		Filter5.setName("merge thread");
		Filter5.start();
		Filter6.setName("sink thread");
		Filter6.start();
   } // main

} // Plumber