# Parameter management platform

PMP is a library for creating, interacting and retrieving database persisted variables used in Nykredit services.

The repository is split into 4 parts:

| Name            | Description                                                                                        |
| --------------- | -------------------------------------------------------------------------------------------------- |
| pmp-core        | The core library used by other services                                                            |
| example-service | An example service controlled by the library                                                       |
| pmp-client      | The web-interface for editing parameters across all active services                                |
| pmp-tracker     | The central server keeping track of all active services, allowing the client to create connections |

## Running The Project

To see the project in action, go to <https://datalogi.net>. To set it up locally, see the guide below.

### With docker compose

To run the project with docker compose, simply run `./full-build-and-run.sh`. This will spin up a client, tracker and 6 services. The client will be available at <http://localhost:4173>.

Dependencies:

- Docker
- Docker compose
- Maven
- Java 11

#### Compose issues

Due to environment issues on certain machines, it may be necessary to manually set `VITE_TRACKER_URL = http://localhost:38429/pmp-tracker/rest` in `pmp-client/.env.local` for the client to properly connect to the tracker. An example file is provided as `pmp-client/.env.local.example`.

on \*nix systems:

```shell
cp pmp-client/.env.local.example pmp-client/.env.local
```

### Manual setip

Running the project has 3 steps. A quick guide is provided below, but for more detailed instructions, see the `README.md` in each of the subfolders.

Dependencies:

- Docker
- Maven
- Java 11
  - The project was developed with java 11, but should be compatible with any later version
- Node
- Npm

1. Start the tracker

   - Requires docker

   ```shell
   cd pmp-tracker
   docker build -t pmp-tracker .
   docker run -p 8080:8080 pmp-tracker
   ```

2. Start the example service

   - Requires maven and java 11

   ```shell
   # From the project root
   mvn clean install -am # -am handles dependencies
   mvn -pl example-service verify exec:java
   ```

3. Start the client

   - Requires node and npm

   ```shell
   cd pmp-client
   npm install
   npm run dev
   ```

Then open <http://localhost:5173>

## Testing

Tests for the core, tracker, and example service are written in JUnit 5. These automatically run during the build steps of those projects. Tests for the client are written with playwright and can be run with `npm run test:e2e` from the client directory.
While build with an end-to-end system, client tests are integration tests, run on mock data.

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
