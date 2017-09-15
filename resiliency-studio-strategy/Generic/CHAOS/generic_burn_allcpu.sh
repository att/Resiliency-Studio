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
# Script for Generic Burn CPU to utilize 100% on all cpu processors
#
# Purpose
#   Run this script to simulate 100% cpu utilization
#
# Usage Syntax:
#   $0
#     
# STEPS:
#  Dynamic shell script will be generated with openssl speed command to simulate cpu utilization
#  This dynamic shell script will be executed
#  
# Exit Codes:
#  0 - Script execution success and execution result passed
#  101 - Script execution success and execution result failed
#  others - Script execution failed and execution result failed
#


scriptName="/tmp/rscpuutilization.sh"   #Specify the script name used for simulating the burn cpu. This should be same as below dynamic script name.

#Check if the burncpu script is already running
pcount=$(ps -ef | grep -v grep | grep bash | grep "$scriptName" | wc -l)

echo ""
if [ "$pcount" = 0 ]
then
#Dynamically create the shell script with openssl speed command 
#Use -multi option to run openssl command on all processors
cat << EOF > /tmp/rscpuutilization.sh
#!/bin/bash
while true;
	do 
		openssl speed -multi $(cat /proc/cpuinfo | grep processor | wc -l);
	done
EOF

echo "Burn CPU is running and it will be utilizing 100% on all cpu cores/processors" 

nohup /bin/bash /tmp/rscpuutilization.sh </dev/null >/dev/null 2>&1 &
#/bin/bash /tmp/rscpuutilization.sh
else
	echo "BurnCPU Chaos Monkey is already running, hence run after its completed"
fi

if [ $? = 0 ]
then
    echo "Monkey executed successfully"
else
    echo "Monkey execution returned with error" $?
fi


