java FireAlarmController $1 &
sleep 1
java FireDetectionSensor $1 &
sleep 1
java SprinklerController $1 &
sleep 1
java SecurityConsole

