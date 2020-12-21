# Airports App - 2020
This web application offers search functionality for any flight, airport or airline in the world
## Build the project
To build this project you need Maven. You can download the Maven tool by accessing the link below:
[Download Maven]( http://maven.apache.org)

1. Change directory to your project root folder in command line.
   
2. Clean compilation products:

        mvn clean

3. If you are looking to package the project, then you should run:

        mvn package

4. Compile:

        mvn compile

5. Execute:

        mvn exec:java -Dexec.mainClass=ro.siit.airports.AirportsApplication

Once started, the application should be available at:

http://localhost:8080/airports-app/
    