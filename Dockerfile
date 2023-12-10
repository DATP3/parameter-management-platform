FROM maven:3.9.5-eclipse-temurin-11
EXPOSE 64017 40535
RUN mkdir /opt/app
COPY . /opt/app/
WORKDIR /opt/app
RUN mvn clean install -am
CMD [ "mvn", "-pl", "example-service", "verify", "exec:java" ]
