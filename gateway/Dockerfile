FROM openjdk:11
ADD target/gateway-0.0.1-SNAPSHOT.jar gateway-0.0.1-SNAPSHOT.jar
EXPOSE 9092
ENTRYPOINT ["java", "-jar", "gateway-0.0.1-SNAPSHOT.jar"]