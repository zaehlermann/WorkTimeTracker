#!/bin/bash

# Variables
PI_USER=mike
PI_HOST=timetracker.local
PI_PATH=/home/mike/timetracker
JAR_NAME=timetracker-ui-1.0.0.jar

# Copy JAR to Raspberry Pi
scp timetracker-ui/target/$JAR_NAME $PI_USER@$PI_HOST:$PI_PATH

# Override existing service
ssh $PI_USER@$PI_HOST "mv timetracker-ui-1.0-SNAPSHOT.jar timetracker-ui.jar"

# Restart service on Raspberry Pi
ssh $PI_USER@$PI_HOST "sudo systemctl restart timetracker-ui"