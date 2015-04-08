java FireAlarmController $1 &
sleep 2
java FireDetectionSensor $1 &
sleep 2
java SprinklerController $1 &
sleep 2
java SecurityConsole

