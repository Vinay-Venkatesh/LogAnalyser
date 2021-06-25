FROM openjdk:11-jre-slim

# Create a user which will be used for running the service
# The GID and UID should map to an unprivileged user on the host
RUN addgroup --gid 65535 app-user
RUN adduser --uid 65535 --shell /bin/bash --gid 65535 app-user

# copy the packaged jar file into our container
COPY app.jar /home/app-user/app.jar
VOLUME /home/app-user/logs

WORKDIR /home/app-user
USER app-user

# set the startup command to execute the jar
CMD ["java", "-jar", "app.jar"]

EXPOSE 8080
