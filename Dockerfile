FROM openjdk:11
EXPOSE 8080
ADD target/billsToPay.jar billsToPay.jar
ENTRYPOINT ["java","-jar","billsToPay.jar"]