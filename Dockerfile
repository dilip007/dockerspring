FROM openjdk:11-jre
WORKDIR /app
COPY . /app
EXPOSE 80
CMD ["/usr/bin/java", "-jar", "-Dspring.profiles.active=default", "dockerspring-0.0.1-SNAPSHOT.jar"]