/**************************
ServMaintConsole.java

This class creates a Service Maintenance Conlsole which allows users to observe 
sensors currently installed in the system. The console also allows a user to 
monitor the operation of the sensors. When a sensor becomes unresponsive, the
user is notified.

**************************/

import TermioPackage.*;
import MessagePackage.*;
import java.util.*;
import java.text.DecimalFormat;

public class ServMaintConsole
{
    public static void main(String args[])
    {
        Termio UserInput = new Termio();    // Termio IO Object
        boolean Done = false;               // Main loop flag
        String Option = null;               // Menu choice from user
        Message Msg = null;                 // Message object
        boolean Error = false;              // Error flag
        ServMaintMonitor Monitor = null;          // The environmental control system monitor

        // Colors for console messages and moving around
        String ANSI_RED = "\u001B[31m";
        String ANSI_GREEN = "\u001B[32m";
        String ANSI_RESET = "\u001B[0m";
        String ANSI_CSI = "\u001b["; // CSI + A - goes up, CSI + K - deletes line

        /////////////////////////////////////////////////////////////////////////////////
        // Get the IP address of the message manager
        /////////////////////////////////////////////////////////////////////////////////

        if ( args.length != 0 )
        {
            // message manager is not on the local system

            Monitor = new ServMaintMonitor( args[0] );

        } else {

            Monitor = new ServMaintMonitor();

        } // if


        // Here we check to see if registration worked. If ef is null then the
        // message manager interface was not properly created.

        if (Monitor.IsRegistered() )
        {
            Monitor.start(); // Here we start the monitoring and control thread

            while (!Done)
            {
                // Here, the main thread continues and provides the main menu

                System.out.println( "\n\n\n\n" );
                System.out.println( "Environmental Control System (ECS) Service Maintenance Console: \n" );

                if (args.length != 0)
                    System.out.println( "Using message manger at: " + args[0] + "\n" );
                else
                    System.out.println( "Using local message manger \n" );

                System.out.println( "To shut the system, press [CTRL] + [C] \n" );
                // System.out.println( "1: List Device Information ");
                // System.out.println( "2: Stop System ");
                // System.out.print( "\n>>>> " );
                // Option = UserInput.KeyboardReadString();

                //////////// option 1 ////////////
                // if ( Option.equals( "1" ) ) {
                    System.out.println( "To go back to the previous menu, press R at any time.\n" );
                    System.out.println( "Service Maintenance Console Device Manager");
                    System.out.println( "Below is a list of all devices connected to your ECS.\n");
                    System.out.println( "     Device                     Device                                    Last                     ");
                    System.out.println( "ID   Name                       Description                               Response (sec)     Status");
                    System.out.println( "---------------------------------------------------------------------------------------------------");

                    int oldDeviceCount = 0;

                    // number formats for printing
                    DecimalFormat idFmt = new DecimalFormat("#00.###");
                    DecimalFormat timeFmt = new DecimalFormat("#0.000");

                    while (true) {
                        Vector<Vector> Devices = Monitor.getDevices();

                        // Gets current time
                        Date currentTime = new Date();

                        // Retrieves vectors from Service Maintenance Monitor
                        Vector<Integer> deviceIds = Devices.get(0);
                        Vector<String> deviceNames = Devices.get(1);
                        Vector<Date> deviceTimes = Devices.get(2);
                        Vector<String> deviceDescs = Devices.get(3);

                        // Number of connected devices
                        int deviceCount = deviceIds.size();

                        // Checks to see if any devices have been added or removed
                        int deviceChange = deviceCount - oldDeviceCount;
                        oldDeviceCount = deviceCount;

                        for (int i = 0; i < deviceCount; i++) {
                            // Get devices last read heart beat
                            Date lastTime = deviceTimes.get(i);

                            // Calculate time in seconds since last read heart beat
                            double diffSec = (currentTime.getTime()-lastTime.getTime())/1000.00;

                            String status = null;

                            // Create status message
                            if ( diffSec < 5.00 ) {
                                status = ANSI_GREEN + "[ OK ]" + ANSI_RESET;
                            } else {
                                status = ANSI_RED + "[ WARNING ]" + ANSI_RESET;
                            }

                            if (i == 0 && deviceChange == 0) {
                                // Move cursor to top device
                                System.out.print(ANSI_CSI + deviceCount + "A");
                            }

                            if (i == 0 && deviceChange > 0) {
                                // Moves curson to position when device is added
                                int combineLine = deviceCount - 1;

                                if (combineLine > 0) {
                                    // Move cursor to top device
                                    System.out.print(ANSI_CSI + combineLine + "A");
                                }
                            }

                            // Clear that line
                            System.out.print(ANSI_CSI + "K");

                            int nameLength = 25;
                            int descLength = 48;

                            int deviceID = deviceIds.get(i);

                            String deviceName = deviceNames.get(i);
                            for (int j =0; j < nameLength - deviceName.length(); j++) {
                                deviceName += " ";
                            }

                            String deviceDesc = deviceDescs.get(i);

                            System.out.printf("%-3.3s  %-25.25s  %-40.40s  %-18.18s %-20.20s\n",idFmt.format(deviceID),
                                deviceName,deviceDesc,timeFmt.format(diffSec),status);
                        }

                        // Updates dashboard every second
                        try {
                            Thread.sleep( 1000 );
                        } catch( Exception e ) {
                            System.out.println( "Service Monitor Sleep error:: " + e );
                        } // catch
                    }
                //}


                // //////////// option X ////////////
                // if ( Option.equalsIgnoreCase( "X" ) )
                // {
                //     // Here the user is done, so we set the Done flag and halt
                //     // the environmental control system. The monitor provides a method
                //     // to do this. Its important to have processes release their queues
                //     // with the message manager. If these queues are not released these
                //     // become dead queues and they collect messages and will eventually
                //     // cause problems for the message manager.
                //     Monitor.Halt();
                //     Done = true;
                //     System.out.println( "\nConsole Stopped... Exit monitor mindow to return to command prompt." );
                //     Monitor.Halt();
            } // while
        } else {
            System.out.println("\n\nUnable start the monitor.\n\n" );
        } // if

    } // main

}