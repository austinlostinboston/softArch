%ECHO OFF
%ECHO Starting ECS System
PAUSE
%ECHO ECS Monitoring Console
START "TEMPERATURE SENSOR CONSOLE" /MIN /NORMAL java ServMaintConsole %1



START "SYSTEM B CONSOLE" /NORMAL java SecurityConsole %1

START "FIRE DETECTION SENSOR" /MIN /NORMAL java FireDetectionSensor %1

START "FIRE ALARM CONTROLLER" /MIN /NORMAL java FireAlarmController %1

START "SPRINKLER CONTROLLER" /MIN /NORMAL java SprinklerController %1

START "INTRUSION SENSOR" /MIN /NORMAL java IntrusionSensor %1

START "INTRUSION CONTROLLER" /MIN /NORMAL java IntrusionAlarmController %1

%ECHO ECS Monitoring Console
START "MUSEUM ENVIRONMENTAL CONTROL SYSTEM CONSOLE" /NORMAL java ECSConsole %1
%ECHO Starting Temperature Controller Console
START "TEMPERATURE CONTROLLER CONSOLE" /MIN /NORMAL java TemperatureController %1
%ECHO Starting Humidity Sensor Console
START "HUMIDITY CONTROLLER CONSOLE" /MIN /NORMAL java HumidityController %1
START "TEMPERATURE SENSOR CONSOLE" /MIN /NORMAL java TemperatureSensor %1
%ECHO Starting Humidity Sensor Console
START "HUMIDITY SENSOR CONSOLE" /MIN /NORMAL java HumiditySensor %1