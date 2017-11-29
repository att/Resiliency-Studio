## Resiliency Studio ##

### Release Notes ###

This is a sample demo application that can be used for trying out Resiliency Studio features. It is developed using spring-boot framework and is a simple microservice application with a frontend service connects to set of backend microservices via. HAProxy load balancer. Frontend service is Hystrix enabled and will stream hystrix events and can be monitored through Hystrix Dashboard.

User can customize the port number of frontend and backend service by updating property files in the corresponding JARs. 

### Overview ###

* demo-app2.jar - Front end service

* restBackend.jar - Backend service

* demo-app-setup.sh - Automated Script to deploy and run the services

* demo-app-loadgenerator.sh - Simple script to generate basic load on this application

User can run the setup script, execute the load generator script and then onboard this application into Resiliency Studio and play around by running different Resiliency Strategies.