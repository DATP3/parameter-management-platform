build image:
    docker build -t tracker:v1 .

run image:
    docker run --rm -p 8080:8080 tracker:v1

url:
    http://localhost:8080/pmp-tracker

example requests:
endpoints:
/pmp-tracker/rest/services
/pmp-tracker/rest/environments

GET servises
curl --location 'http://localhost:8080/pmp-tracker/rest/services' \
--header 'Authorization: a' \
--header 'pmp-environment: prod'

POST services
curl --location 'http://localhost:8080/pmp-tracker/rest/services' \
--header 'pmp-environment: prod' \
--header 'Content-Type: application/json' \
--data '{
    "pmpRoot": "1.2.1.212-testavsdf",
    "name": "test-service-2"
}'
