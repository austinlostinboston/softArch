%ECHO OFF
%ECHO Starting ECS System
PAUSE
%ECHO ECS Monitoring Console
START "MUSEUM ENVIRONMENTAL CONTROL SYSTEM CONSOLE" /NORMAL java SecurityConsole %1
START "MUSEUM ENVIRONMENTAL CONTROL SYSTEM CONSOLE" /NORMAL java FireDetectionSensor %1
%ECHO Starting Temperature Controller Console
START "TEMPERATURE CONTROLLER CONSOLE" /MIN /NORMAL java FireAlarmController %1
%ECHO Starting Humidity Sensor Console


%ECHO Starting Humidity Sensor Console
START "HUMIDITY SENSOR CONSOLE" /MIN /NORMAL java SprinklerController %1


