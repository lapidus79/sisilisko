Sisilisko - The smooth Gecko!
=============================


### What

Sisilisko is a Geckoboard style application implemented in Akka & Play (Java flavour).

Basically you have an Akka ActorSytem with dashboards which in turn have widgets and they all run as actors. An enduser initiates a websocket connection and a ClientActor is created, this actor in turn subscribes to a dashboard(s) and then starts to receive updates of changes in dashboard & widget actor states.

Currently dashboard and widget conf data is stored in a blocking SQL database. For interacting this resource a round robin router strategy has been used with a dedicated execution context.

The app also contains simple REST endpoints for dashboard/widget CRUD management.

The Frontend is a simple redux application that mimics Geckoboards.

### Why

Just to get familiar with akka and trying to figure out how it all works.

### Running in development mode

* Java 8
* SBT
* Node 8+

Fire up the backend "sbt run  -Dplay.evolutions.db.default.autoApply=true  -DdropAndSeed=true". Then actually start the backend application by triggering http://localhost:9000 once in your browser.
Continue by firing up the frontend by switching to the ui sub directory run "npm install" and then "npm start"
Open up the frontend in http://localhost:8080


### Next steps

* Drop the blocking sql database and use Akka Persistence instead
* Rewrite everything in scala / possibly implement in lagom instead
