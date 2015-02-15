import java.util.HashSet;

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
	   	HashSet<Integer> hs=new HashSet<Integer>();
		hs.add(0);
		hs.add(1);
		hs.add(2);
		hs.add(3);
		hs.add(4);
		hs.add(5);

		HashSet<Integer> hs1=new HashSet<Integer>();
		hs1.add(0);
		hs1.add(3);
		
		
		SourceFilter Filter1 = new SourceFilter("SubSetA.dat");
		SourceFilter Filter2 = new SourceFilter("SubSetB.dat");

		MergeSourceFilter Filter3 = new MergeSourceFilter(2);
		LessThan10KFilter Filter4 = new LessThan10KFilter();
		SortFilter Filter5 = new SortFilter();
		//CopyFilter Filter5 = new CopyFilter(2);
		
		SinkFilter Filter6 = new SinkFilter("LESS10K.dat",hs);
		WildPointFilter Filter7 = new WildPointFilter();
		SinkFilter Filter8 = new SinkFilter("OUTPUTC.dat",hs);
		SinkFilter Filter9=new SinkFilter("wild.dat",hs1);


		/****************************************************************************
		* Here we connect the filters starting with the sink filter (Filter 1) which
		* we connect to Filter2 the middle filter. Then we connect Filter2 to the
		* source filter (Filter3).
		****************************************************************************/
		Filter9.Connect(Filter7, 0, 1);
		Filter8.Connect(Filter7, 0, 0);
		Filter7.Connect(Filter5, 0, 0);
		Filter6.Connect(Filter4, 0, 1);
		
		Filter5.Connect(Filter4, 0, 0);
		
		Filter4.Connect(Filter3, 0, 0);
		Filter3.Connect(Filter1, 0, 0);
		Filter3.Connect(Filter2, 1, 0);

		/****************************************************************************
		* Here we start the filters up. All-in-all,... its really kind of boring.
		****************************************************************************/

		Filter1.setName("sourceA thread");
		Filter1.start();
		Filter2.setName("sourceB thread");
		Filter2.start();
		Filter3.setName("Merge thread");
		Filter3.start();
		Filter4.setName("Sort thread");
		Filter4.start();
		Filter5.setName("Copy thread");
		Filter5.start();
		Filter6.setName("LessThan10K thread");
		Filter6.start();
		Filter7.setName("wildPoint thread");
		Filter7.start();
		Filter8.setName("Sink thread");
		Filter8.start();
		Filter9.setName("Sink thread");
		Filter9.start();
   } // main

} // Plumber