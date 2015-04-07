/**************************
ServMaintConsole.java

This class creates a Service Maintenance Conlsole which allows users to observe 
sensors currently installed in the system. The console also allows a user to 
monitor the operation of the sensors. When a sensor becomes unresponsive, the
user is notified.

**************************/

import InstrumentationPackage.*;
import MessagePackage.*;
import java.util.*;

class ServMaintMonitor extends Thread
{
  
    private MessageManagerInterface em = null;  // Interface object to the message manager
    private String MsgMgrIP = null;             // Message Manager IP address
    boolean Registered = true;                  // Signifies that this class is registered with an message manager.
    MessageWindow mw = null;                    // This is the message window

    // Vectors to hold heart beat info
    private Vector<Integer> RegisteredIds = new Vector<Integer>(15,1);
    private Vector<String> RegisteredNames = new Vector<String>(15,1);
    private Vector<Date> LastHeartBeat = new Vector<Date>(15,1);
    private Vector<String> RegisteredDesc = new Vector<String>(15,1);



    public ServMaintMonitor()
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
            System.out.println("ServMaintMonitor::Error instantiating message manager interface: " + e);
            Registered = false;

        } // catch

    } //Constructor

    public ServMaintMonitor( String MsgIpAddress )
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
            System.out.println("ServMaintMonitor::Error instantiating message manager interface: " + e);
            Registered = false;

        } // catch

    } // Constructor

    public void run()
    {
        Message Msg = null;             // Message object
        MessageQueue eq = null;         // Message Queue
        int MsgId = 0;                  // User specified message ID
        int Delay = 1000;               // The loop delay (1 second)
        boolean Done = false;           // Loop termination flag

        if (em != null)
        {
            // Now we create the ECS status and message panel
            // Note that we set up two indicators that are initially yellow. This is
            // because we do not know if the temperature/humidity is high/low.
            // This panel is placed in the upper left hand corner and the status
            // indicators are placed directly to the right, one on top of the other

            mw = new MessageWindow("ECS Service Maintenance Console", 0, 0);

            mw.WriteMessage( "Registered with the message manager." );

            try
            {
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
                //mw.WriteMessage("MessageQueue Size: " + qlen);


                for ( int i = 0; i < qlen; i++ )
                {
                    Msg = eq.GetMessage();
                    MsgId = Msg.GetMessageId();
                    //mw.WriteMessage("Msg: " + MsgId);

                    // Handle new component registering to the system
                    if ( MsgId >= 300 && MsgId <= 399  ) {
                        int componentId = MsgId % 100;
                        String nameDescription = Msg.GetMessage();
                        String componentName = nameDescription.split("-")[0];
                        String componentDesc = nameDescription.split("-")[1];

                        if ( componentId >= 1 && componentId <= 33) {
                            componentName += " Sensor";
                        } else if (componentId >= 34 && componentId <= 66) {
                            componentName += " Controller";
                        } else {
                            componentName += " Console";
                        }
                        Date timeCurrent = new Date();

                        // Add values to vectors
                        RegisteredIds.add(componentId);
                        RegisteredNames.add(componentName);
                        LastHeartBeat.add(timeCurrent);
                        RegisteredDesc.add(componentDesc);

                        mw.WriteMessage(componentName + "(" + componentId + ") is connected.");

                    }

                    // Handle heartbeat of component connected to the system
                    if ( MsgId >= 400 && MsgId <= 499  ) {
                        int componentId = MsgId % 100;
                        int index = RegisteredIds.indexOf(componentId);
                        String componentName = RegisteredNames.get(index);

                        // Current time
                        Date timeCurrent = new Date();

                        // Update time
                        LastHeartBeat.set(index,timeCurrent);

                        mw.WriteMessage(componentName + "(" + componentId + ") is alive.");

                    }


                    // Handle disconnections of components from the system
                    if ( MsgId >= 500 && MsgId <= 599  ) {
                        int componentId = MsgId % 100;
                        int index = RegisteredIds.indexOf(componentId);
                        String componentName = RegisteredNames.get(index);
                        

                        // Removes component from lists
                        RegisteredIds.removeElementAt(index);
                        RegisteredNames.removeElementAt(index);
                        LastHeartBeat.removeElementAt(index);

                        mw.WriteMessage(componentName + "(" + componentId + ") is disconnected.");
                    }


                    // If the message ID == 99 then this is a signal that the simulation
                    // is to end. At this point, the loop termination flag is set to
                    // true and this process unregisters from the message manager.

                    if ( Msg.GetMessageId() == 99 )
                    {
                        Done = true;

                        try
                        {
                            em.UnRegister();

                        } // try

                        catch (Exception e)
                        {
                            mw.WriteMessage("Error unregistering: " + e);

                        } // catch

                        mw.WriteMessage( "\n\nSimulation Stopped. \n");

                        // Get rid of the indicators. The message panel is left for the
                        // user to exit so they can see the last message posted.

                        // hi.dispose();
                        // ti.dispose();

                    } // if

                } // for

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


    /***************************************************************************
    * CONCRETE METHOD:: Halt
    * Purpose: This method posts an message that stops the environmental control
    *          system.
    *
    * Arguments: none
    *
    * Returns: none
    *
    * Exceptions: Posting to message manager exception
    *
    ***************************************************************************/

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

    // Used to pass a vectors contianing device information to the console to be displayed
    public Vector<Vector> getDevices() {
        Vector<Vector> devices = new Vector<Vector>(4,1);

        devices.add(RegisteredIds);
        devices.add(RegisteredNames);
        devices.add(LastHeartBeat);
        devices.add(RegisteredDesc);

        return devices;
    }

} // ECSMonitor