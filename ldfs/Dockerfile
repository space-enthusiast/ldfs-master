FROM openjdk:21-jdk

EXPOSE 8080

ARG JAR_FILE=build/libs/ldfs-0.0.1-SNAPSHOT.jar

COPY ${JAR_FILE} ldfs.jar

ENTRYPOINT ["java","-jar", "/ldfs.jar"]