java IntrusionAlarmController $1 &
sleep 1
java IntrusionSensor $1 &
sleep 1
java SecurityConsole $1 &
sleep 1
java SecurityMonitor $1 &