%ECHO OFF
%ECHO Starting ECS System
PAUSE
%ECHO ECS Monitoring Console
START "MUSEUM ENVIRONMENTAL CONTROL SYSTEM CONSOLE" /NORMAL java SecurityConsole %1
%ECHO Starting Temperature Controller Console
START "TEMPERATURE CONTROLLER CONSOLE" /MIN /NORMAL java IntrusionSensor %1
%ECHO Starting Humidity Sensor Console


START "TEMPERATURE SENSOR CONSOLE" /MIN /NORMAL java IntrusionAlarmController %1
