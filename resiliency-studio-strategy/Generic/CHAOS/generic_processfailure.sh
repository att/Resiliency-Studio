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

# Script for Generic process failure to kill the specific process
#
# Purpose
#   Run this script to kill the specified process
#
# Usage Syntax:
#   $0 $1
#     where:
#       $1 is the process name passed from the scenario object from RS UI. Choose the component, process name while creating the scenario
#     
# STEPS:
#  Identifies the matching process and kills the process
#    
# Exit Codes:
#  0 - Script execution success and execution result passed
#  101 - Script execution success and execution result failed
#  others - Script execution failed and execution result failed
#


if [ -z "$1" ]
then
    echo "No Process Name supplied as arguments, please configure appropriate component, process under server and scenario creation"
exit 1
fi

processname="$1"

echo ""
date 
echo "Retrieving process status" $processname
pcount=$(ps -ef | grep -v grep | grep -v bash | grep "$processname" | wc -l)

if [ "$pcount" = 0 ]
then
	echo "No matching process running on this server"
else
	echo "Identified total of $pcount process, listing the matching process details"
	ps -ef | grep -v grep | grep -v bash | grep "$processname" 
	echo "Terminating the matching process"
	pid=$(ps -ef | grep -v grep | grep -v bash | grep "$processname" | awk '{print $2}')
	kill -9 $pid
fi

if [ $? = 0 ]
then
	echo "Monkey executed successfully"
else
    echo "Monkey execution returned with error" $?
fi
