# Simple Transit System
This is a simple transit system to manage a rapid transportation network. `simple.transitsystem.core` package contains 
the classes for the transit system implementation. `simple.transitsystem.mbta` package contains the classes for creating
and using the transit system for MBTA. 

## Creating the Transit System for MBTA
Transit System requires routes, stops and trips (with stop traversal sequence and direction) data to initialize the 
system. A different combination of MBTA api can be used to read the required resources. In this implementation, 
I read the MBTA routes I am interested about (by using filter) and then for each route, read the rest of 
the dataset in a single GET using nested include feature of jsonapi. This allows me to avoid the N+1 requests to read 
the dataset transit system requires for initialization. Use of nested includes makes the filtering of initial route read
with or without filter less concerning as all it needs is the route id to make the next read. 
See `MbtaTransitSystemInitializer.initialize` method for more details.

`simple.transitsystem.mbta.Mbta` class has a main method to read the data from MBTA api endpoints. It also initializes
and starts the MBTA transit system.

## Running the application
This application requires JDK 11 or higher. After checking out the repo, execute:
````
./mvnw clean compile exec:java -Dexec.mainClass="simple.transitsystem.mbta.Mbta"
````
After successfully running, follow the onscreen messages to use the application. 

MBAT `api-key` is optional to run this application. You may add your `api-key` in `MbtaTransitSystemInitializer.API_KEY`
to avoid rate limit error.





