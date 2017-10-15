FROM ubuntu:16.04

MAINTAINER Sergey Butorin

# Updating packages
RUN apt-get -y update --fix-missing

# Install JDK
RUN apt-get install -y openjdk-8-jdk-headless

RUN apt-get install -y maven

ENV WORK /opt/forum_db
ADD src/ $WORK/src/
ADD pom.xml $WORK/

WORKDIR $WORK/
RUN mvn package

EXPOSE 80

CMD java -jar target/highload_server-1.0-SNAPSHOT.jar