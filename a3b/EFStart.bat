%ECHO OFF
%ECHO Starting ECS System
PAUSE

START "SYSTEM B CONSOLE" /NORMAL java SecurityConsole %1

START "FIRE DETECTION SENSOR" /MIN /NORMAL java FireDetectionSensor %1

START "FIRE ALARM CONTROLLER" /MIN /NORMAL java FireAlarmController %1

START "SPRINKLER CONTROLLER" /MIN /NORMAL java SprinklerController %1


