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

#
# Script for Recovering from FailDns Chaos Monkey. sudo permission is required
#
# Purpose
#   Run this script to recover from the DNS failure caused by FailDNS Chaos monkey.
#
# Usage Syntax:
#   $0
#     
# STEPS:
#  iptables command is used to un-block the udp/tcp port number 53 which is used for DNS resolution
#  
#  
# Exit Codes:
#  0 - Script execution success and execution result passed
#  101 - Script execution success and execution result failed
#  others - Script execution failed and execution result failed
#


echo ""
date

#Check if the iptables rule for dns failure is already added
existingrule=$(sudo iptables -S OUTPUT | grep tcp | grep port | grep 53 | grep DROP)

if [ -z "$existingrule" ]
then
	echo "No existing iptables rule for DNS failure found, hence exiting without any recovery"
else
	echo "Recovering from DNS failure on the node"
	sudo iptables -D OUTPUT -p tcp --dport 53 -j DROP
	sudo iptables -D OUTPUT -p udp --dport 53 -j DROP
fi

if [ $? = 0 ]
then
    echo "Monkey executed successfully"
else
    echo "Monkey execution returned with error" $?
fi
