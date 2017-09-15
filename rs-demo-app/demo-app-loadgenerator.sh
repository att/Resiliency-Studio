#!/bin/bash
# Script for generating load on demo application

echo "Generating load on demo app, terminate it once testing is completed"
#Generate_load using watch command, and closed after certain time. Specify timeout value in the command(default set to 10 minutes)
#timeout 600 watch -d -n 1 "curl -s 'http://localhost:7071/searchCityNewHx/?name=Melbourne&country=Australia'" > watchout.log

#Generate_load using while loop with sleep time of 1 seconds, so request is fired every 1 second.
while true;
do
        curl -s 'http://localhost:7071/searchCityNewHx/?name=Melbourne&country=Australia' > curlout.log;
        sleep 1;
done

#Terminate the load generation once the testing is completed.
