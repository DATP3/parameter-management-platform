# Example Service

A simple service showing how to use PMP in a service

## Running

First, compile and install pmp-core:

```shell
mvn clean install -am
```

Then, run the service:

```shell
mvn -pl example-service verify exec:java
```

This will start a webserver on `localhost:40535`, which simply responds with the value of the parameter with key `person`

Note that _both_ these commands should be run in order to see changes in pmp-core on the example server.
Changes to the example server itself only requires running the second command.

The backing database runs on port 7050, and its web interface on port 7051, and can be accessed with the JDBC URL
`jdbc:h2:tcp://localhost:7050/file:./database`, with `sa` as both username and password.
PMP will create a table named `PARAMETER_MANAGEMENT` that stores all the parameters.
