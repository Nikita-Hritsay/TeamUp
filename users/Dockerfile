# Start with a base image containing Java runtime
FROM openjdk:17-jdk-slim

# Who is maintaining this project
MAINTAINER mykyta.hrytsai

# Add application jar to the image
COPY target/users-0.0.1-SNAPSHOT.jar users-0.0.1-SNAPSHOT.jar

# Start the application
ENTRYPOINT ["java", "-jar", "users-0.0.1-SNAPSHOT.jar"]


# -t - name of the image
# docker build . -t teamUp/users:s1


# -d - detached mode so the logs will be in the docker UI
# docker run -d -p 8080:8080 teamUp/users:s1