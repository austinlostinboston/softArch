java TemperatureController $1 &
sleep 2
java HumidityController $1 &
sleep 2
java TemperatureSensor $1 &
sleep 2
java HumiditySensor $1 &
sleep 2
java ECSConsole $1 &