# Generic Monkey Strategy library

Monkey strategy library(Shell script and Ansible based) to simulate different hardware/software failures. These strategies are generic in nature, so that it can run on different flavors of Linux. Also flexibility to update parameters to run failure on different process or network interface etc.   
	
# Categorization	

1. CHAOS  
2. DOCTOR  

Under each of these categories, below list of monkey strategies are available  


## Chaos strategies ##  

**Ansible Network Latency** - Ansible based strategy to simulate network latency  
**Burn All CPU** - Shell script based strategy to simulate 100% CPU utilization.  
**Burn Singe CPU** - Shell script based strategy to burn single CPU resulting in lower CPU utilization compared to Burn All CPU strategy.  
**Burn IO** - Shell script based strategy to simulate disk utilization.  
**Fail DNS** - Shell script based strategy to simulate failure of DNS servers. This strategy uses iptables to block port 53 for TCP & UDP.  
**Fill Disk** - Shell script based strategy to fill disk with random data, resulting in lower disk space.  
**Network Corruption** - Shell script based strategy that corrupts a large fraction of network packets on specific network interface.  
**Network Latency** - Shell script based strategy to induce network latency. The network latency can be configured.    
**Network Loss** - Shell script based strategy to drop a fraction of network packets on specified network interface.   
**Null Route** - Shell script based strategy to fail request to specific ip address or range. The ip address can be configured.    
**Process Failure** - Shell script based strategy to simulate process failure. The process name is configurable and it can be configured using application, scenario objects.  



## Doctor strategies ##  

**Ansible Recovery Latency** - Ansible based strategy to recover from the network latency caused by Chaos monkey  
**Process Status** - Shell script based strategy to run the status of specific process. This will not cause any failures, but helps in identifying the status of process before or after failure.   
**Recover Burn CPU** - Shell script based strategy to recover from CPU burn caused by Chaos monkey  
**Recover Burn IO** - Shell script based strategy to recover from IO burn caused by Chaos monkey and delete temporary file created during chaos.  
**Recover DNS failure** - Shell script based strategy to recover from DNS failure caused by Chaos monkey  
**Recover Fill Disk** - Shell script based strategy to recover from Fill Disk failure and delete the temporary file created by Chaos monkey  
**Recover Network corruption** - Shell script based strategy to recover network packet corruption caused by Chaos monkey  
**Recover Network Latency** - Shell script based strategy to recover network latency caused by Chaos monkey  
**Recover Network Loss** - Shell script based strategy to recover from network packet loss caused by Chaos monkey  
**Recover Null Route** - Shell script based strategy to recover from null route failure caused by Chaos monkey  
  
