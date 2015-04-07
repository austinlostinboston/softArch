%ECHO OFF
%ECHO Starting ECS System
PAUSE

START "SYSTEM A CONSOLE" /NORMAL java SecurityConsole %1

START "INTRUSION SENSOR" /MIN /NORMAL java IntrusionSensor %1

START "INTRUSION CONTROLLER" /MIN /NORMAL java IntrusionAlarmController %1
