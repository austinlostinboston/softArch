java TemperatureController $1 &
sleep 1
java HumidityController $1 &
sleep 1
java TemperatureSensor $1 &
sleep 1
java HumiditySensor $1 &
sleep 1
java ECSConsole $1 &