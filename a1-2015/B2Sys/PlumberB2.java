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
public class PlumberB2
{
   public static void main( String argv[]) throws Exception
   {
		/****************************************************************************
		* Here we instantiate three filters.
		****************************************************************************/
		ArrayList<Integer> hs =new ArrayList<Integer>();
		hs.add(0);
		hs.add(1);
		hs.add(2);
		hs.add(3);
		hs.add(4);
		hs.add(5);

		ArrayList<Integer> hs1 =new ArrayList<Integer>();
		hs1.add(0);
		hs1.add(3);


		SourceFilter Filter1 = new SourceFilter("../DataSets/FlightData.dat");
		CopyFilter Filter2=new CopyFilter(4);
		WildPointFilter Filter5 = new WildPointFilter();
		TempFilter Filter3 = new TempFilter();
		AltiFilter Filter4 = new AltiFilter();
		MergeFilter Filter6=new MergeFilter(4);
		SinkFilter Filter8 = new SinkFilter("OutputB.dat",hs);
		SinkFilter Filter7 = new SinkFilter("WildPoints.dat",hs1);

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
		Filter4.Connect(Filter2, 0, 2);
		
		Filter5.Connect(Filter2, 0, 1);
		Filter6.Connect(Filter2, 0, 3);
		Filter6.Connect(Filter3, 1, 0);
		Filter6.Connect(Filter4, 2, 0);
		Filter6.Connect(Filter5, 3, 0);
		Filter7.Connect(Filter5, 0, 1);
		Filter8.Connect(Filter6, 0, 0);
		
		

		/****************************************************************************
		* Here we start the filters up. All-in-all,... its really kind of boring.
		****************************************************************************/
		Filter1.start();
		Filter2.start();
		Filter3.start();
		Filter4.start();
		Filter5.start();
		Filter6.start();
		Filter7.start();
		Filter8.start();
   } // main

} // Plumber