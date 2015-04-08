In order to operate System C. Follow these steps.

It is important to note that the the service maintenance console only works on a mac machine.

Steps (must be done in this exact order - java files have already been compiled):
1. Create the Message MAnager stubs           - rmic MessageManager.java
2. Start the rmiregistry and message manager  - ./startMsgMgr.sh
3. Start the service maintenance console      - ./startServMaintConsole.sh
4. Please drag the width of your terminal so that the dotted line only takes up one line (or it won't display/operate properly)
5. Start the ECS System                       - ./startESC.sh

How it works:
The service maintenace console listens for connection, heartbeat, and disconnection messages coming from all devices connected to the system. Connection messages register the device and let the console know that the device is connected. Heartbeat messages let the console know that the device is functioning properly from a message sending standpoint. If more than 5 seconds passes without a heartbeat response, the console displays a warning sign letting the user know something is wrong with that device. When a disconnect message is received, the console know's to remove that device from the list.

Connect messages are the only message with content associated to them. The content consists of the devices name and a description of the device which is stored locally on each device. Each device also is manually assigned an ID which is stored locally. The messageID is what tells the console what kind of message it is and who's sending it.

MessageID Logic:
Connection Messages - 3XX
Heartbeat Message   - 4XX
Disconnect Messages - 5XX

XX = the devices ID

Additonal Playing around:
I made a set of scripts to run each System. Feel free to run all of these and see how the console works. Also, kill the running java devices to see how the warning sign appears.