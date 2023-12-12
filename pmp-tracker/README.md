# Tracker

## Build and run

### Pre-requisites

Running the tracker requires docker to be installed on the host system, as well as having the daemon running.

run the following commands in the `pmp-tracker` directory:

compile project:

```sh
mvn clean package
```

build image:

```sh
docker build -t tracker:v1 .
```

run image:

```sh
docker run --rm -it -p 8080:8080 tracker:v1
```

The tracker is then hosted at
<http://localhost:8080/pmp-tracker>
with the endpoints:

`/pmp-tracker/rest/services`

`/pmp-tracker/rest/environments`

To test the endpoints from your terminal, use the following curl commands:

GET servises

```sh
curl --location 'http://localhost:8080/pmp-tracker/rest/services' \
--header 'Authorization: a' \
--header 'pmp-environment: prod'
```

POST services

```sh
curl --location 'http://localhost:8080/pmp-tracker/rest/services' \
--header 'pmp-environment: prod' \
--header 'Content-Type: application/json' \
--data '{
"pmpRoot": "1.2.1.212-testavsdf",
"name": "test-service-2"
}'
```
