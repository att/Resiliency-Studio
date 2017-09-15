#!/bin/bash
# Script for setting up demo app on linux box with front end, backend

#Start demo frontend service
echo "Starting demo app, please wait... verify nohupdemofe.out for output"
nohup java -jar demo-app2.jar > nohupdemofe.out 2>&1 &

sleep 10 

#Start backend service
echo "Starting backend, please wait... verify nohupdemobe.out for output"
nohup java -jar restBackend.jar > nohupdemobe.out 2>&1 &
sleep 10

echo "Started the demo app, verify the log for success/failure"
