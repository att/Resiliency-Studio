#!/bin/bash

# *******************************************************************************
# *   BSD License
# *    
# *   Copyright (c) 2017, AT&T Intellectual Property.  All other rights reserved.
# *    
# *   Redistribution and use in source and binary forms, with or without modification, are permitted
# *   provided that the following conditions are met:
# *    
# *   1. Redistributions of source code must retain the above copyright notice, this list of conditions
# *      and the following disclaimer.
# *   2. Redistributions in binary form must reproduce the above copyright notice, this list of
# *      conditions and the following disclaimer in the documentation and/or other materials provided
# *      with the distribution.
# *   3. All advertising materials mentioning features or use of this software must display the
# *      following acknowledgement:  This product includes software developed by the AT&T.
# *   4. Neither the name of AT&T nor the names of its contributors may be used to endorse or
# *      promote products derived from this software without specific prior written permission.
# *    
# *   THIS SOFTWARE IS PROVIDED BY AT&T INTELLECTUAL PROPERTY ''AS IS'' AND ANY EXPRESS OR
# *   IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
# *   MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT
# *   SHALL AT&T INTELLECTUAL PROPERTY BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
# *   SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
# *   PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;  LOSS OF USE, DATA, OR PROFITS;
# *   OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
# *   CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN
# *   ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH
# *   DAMAGE.
# *******************************************************************************

# Script for simulating network loss on specified network interface. sudo permission is required
#
# Purpose
#   Run this script to simulate network loss on specific network interface. Update the network interface and loss percentage as per requirement.
#
# Usage Syntax:
#   $0
#     
# STEPS:
#  Run the unix netem command to simulate the network loss.
#  Specify the network interface and loss percentage for the simulation.
#  
#  
# Exit Codes:
#  0 - Script execution success and execution result passed
#  101 - Script execution success and execution result failed
#  others - Script execution failed and execution result failed
#


netinterface="eth0"      #Specify the network interface name for network loss. 
losspercent=7 			 #Specify the loss percentage on interface
corrpercent=25 			 #Specify the correlation percentage with previous packet loss

echo ""
date

#Check if the network loss command is already running
existingrule=$(sudo tc -s qdisc | grep netem | grep loss | grep $netinterface)

if [ -z "$existingrule" ]
then
	echo "Simulating network loss on network interface $netinterface with loss percentage of $losspercent  " 
	# Drops 7% of packets, with 25% correlation with previous packet loss
	# 7% is high, but it isn't high enough that TCP will fail entirely
	sudo tc qdisc add dev "$netinterface" root netem loss "$losspercent"% "$corrpercent"%
else
	echo "Network loss monkey is already running, hence no need to re-run. Increase loss percentage for more network loss"
fi

if [ $? = 0 ]
then
    echo "Monkey executed successfully"
else
    echo "Monkey execution returned with error" $?
fi



