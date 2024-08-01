FROM openjdk:17
RUN mkdir /app
WORKDIR /app
COPY target/*jar app.jar
ENTRYPOINT ["java", "-jar", "-Dserver.port=8080", "app.jar"]
CMD []
