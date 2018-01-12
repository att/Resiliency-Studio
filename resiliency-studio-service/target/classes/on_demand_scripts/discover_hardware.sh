#!/bin/bash

# * BSD License
# *     
# * Copyright (c) 2017, AT&T Intellectual Property.  All other rights reserved.       
# * 
# *  Redistribution and use in source and binary forms, with or without modification, are permitted
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

#
# Script for hardware discovery
#
# Purpose
#   Run this script to Extract hardware details and return os name, ostype, memory, disk, cpu details.
#
# Usage Syntax:
#   $0
#     
# STEPS:
#  Run the command to gather os, ostype, memory, disk, cpu details
#
# Exit Codes:
#  0 - Script execution success and execution result passed
#  101 - Script execution success and execution result failed
#  others - Script execution failed and execution result failed
#
#   Created By : ATT
#   Created On : 07/21/2017
#

cpudetail=""
cpubit=""
osname=""
totmem=""
totdisk=""

cpudetail="$(grep -m 1 'model name' /proc/cpuinfo | cut -d ' ' -f3-)"
cpubit="$(getconf LONG_BIT)"
osname="$(uname -a | awk '{print $1}')"
totmem="$(cat /proc/meminfo | grep -i "MemTotal" | cut -d ' ' -f3- | sed 's/ //g')"
totdisk="$(df -h --total | grep total | awk '{print $2}')"

echo "{"
echo "\"memory\": \"$totmem\","
echo "\"cpu\": \"$cpudetail\","
echo "\"storage\": \"$totdisk"B"\","
echo "\"operatingSystem\": \"$osname\","
echo "\"operatingSystemType\": \"$cpubit"bit"\""
echo "}"

