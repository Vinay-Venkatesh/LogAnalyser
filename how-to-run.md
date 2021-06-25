## Build the project
1. Run mvn package in the root directory of the project.

## Description
1. Dockerfile         - File to build docker image of LogAnalyser application.
2. docker-compose.yml - To build image and run application and postgres as containers.
3. database.env       - The file which contains database details. (Please do not update this file its requires property changes in application code).
4. logs               - Folder which contains zipped files to parse and save it to database.
5. app.jar            - The LogAnalyser spring boot application.

## Execution Steps
1. Copy LogAnalyser-1.0-SNAPSHOT.jar from target folder to root and rename it to app.jar
2. Start and Run docker container - docker-compose up -d
3. Application container runs on port 8080.
4. Database runs on default port 5432.
5. Launch a browser :
      1. To Upload logData to database - http://localhost:8080/insertLogs
      2. To get count of uri's in database -  http://localhost:8080/getURICount

## Current Scope Of Work
1. Implementation is based on MVC architecture.
2. Connecting to database via hibernate framework ensures better performance when compared to normal JDBC connection.
3. Volume mount to store database container data is a named volume db_data that is created via docker-compose.
4. VOLUME in Dockerfile mounts logs folder in root directory to /home/app-user/logs in container.


## Future Scope Of Work
1. Database container data can be persisted via nfs-mounts/aws efs/azure fileshare or anyother persistence storage.
2. Caching solutions like redis/memcache can be used to maximise the throughput, if there are any frequent data retrievals.
3. The same can be implemented in much more agile languages like python/go with Jenkins CI.
