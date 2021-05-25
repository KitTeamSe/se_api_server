# SE API SERVER DOCKERFILE

# Start with a base image containing Java runtime
FROM openjdk:11

# Add Author info
LABEL maintainer="se@kumoh.ac.kr"

# Add a volume to
VOLUME /var/se-api-server

# Make port 8074 available to the world outside this container
EXPOSE 8074

# The application's jar file
ARG JAR_FILE=build/libs/apiserver-0.0.1-SNAPSHOT.jar

# Add the application's jar to the container
ADD ${JAR_FILE} run-se-api-server.jar

# Add Timezone
ENV TZ=Asia/Seoul

# Run the jar file
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/run-se-api-server.jar"]