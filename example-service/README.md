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

# Move the following to root readme later when merged

## Environment variables

The following environment variables can be set to change the behaviour of the service:

### Client

| Name                 | Description                                                            | Default                 |
| -------------------- | ---------------------------------------------------------------------- | ----------------------- |
| `VITE_TRACKER_URL`   | URL of the tracker to gather services from                             | `http://localhost:8080` |
| `TEST_USER_EMAIL`    | User email for client testing. Required to run any playwright tests    |                         |
| `TEST_USER_PASSWORD` | User password for client testing. Required to run any playwright tests |                         |

see `.env.local.example` for an example of how to set these.

### Core / Example service

| Name                       | Description                     | Default                 |
| -------------------------- | ------------------------------- | ----------------------- |
| `SERVICE_INFO_PMPROOT`     | External address of the service | `http://localhost:8080` |
| `SERVICE_INFO_ENVIRONMENT` | The service's environment       |                         |
| `SERVICE_INFO_NAME`        | Name to register the service as |                         |

### Tracker

None
