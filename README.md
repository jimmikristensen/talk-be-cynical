# Be Cynical

This project contains my presentation and demo applications for the talk Be Cynical. The presentation is about cynical software and is heavily inspired by the book [Release It! Second Edition](https://pragprog.com/book/mnee2/release-it-second-edition) by [Michael Nygard](https://www.michaelnygard.com/).

## Talk Abstract

Testing distributed applications, such as microservices, can be difficult. In production, when network is involved, and you have multiple integration points, all sorts of bad things can happen to your newly shipped microservice.

In this talk I will take you through and show:

* a demo REST-based microservice built with Micronaut, Testcontainers, Mountebank and Circuit Breakers.
* different scenarios that shows why it is difficult to test microservices and why it’s important that we think destructive as engineers.
* why it’s important that your application is cynical to protect itself from failures. demonstrate how to test different scenarios that can happen to your microservice in production, such as slow response, network errors, timeouts, invalid response, unexpected response, etc.
* how all this can be automated through continuous integration.

## Project Atchitecture

This architecture of the demo system used in the presentation looks like below:

![container diagram](https://raw.githubusercontent.com/jimmikristensen/talk-be-cynical/master/containers.png)

## Project Structure

* __api-gateway__: Micronaut/GraphQL API gateway for downstream service and the main focus of the presentation
* __api-gateway-hystrix__: The same as the service described above, but this contains an implementation for Hystrix with hystrix commands for the two depended-on components (down-stream service)
* __content-api__: Micronaut REST service as a down-stream from the gateway
* __recommendation-api__ Micronaut REST service as the second down-stream service from the gateway
* __redis_volumes__: Contains the storage for the two Redis caches used by the content and recommendation API
* __loadtest__: Contains the config for creating and artillery.io load test 
  * ```artillery run load_config.yml```
* __mountebank__: Keeps the Mountebank imposters (test doubles) used in the presentation
* __Presentation.key__: The keynote presentation
* __Presentation.pdf__: The presentation in PDF format
* __containers.png__: Image of the demo system
* __docker-compose.yml__: Docker compose file that will start up the demo system (the 3 Micronaut applications and two Redis caches)

## Recommended Reading

I couldn't have created this presentation without the inspiration from [Michael Nygard](https://www.michaelnygard.com/) and his great book [Release It! Second Edition](https://pragprog.com/book/mnee2/release-it-second-edition) - go get the [book here](https://pragprog.com/book/mnee2/release-it-second-edition) and read it! I promise you won't be disappointed.