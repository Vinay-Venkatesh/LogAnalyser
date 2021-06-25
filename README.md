# LogAnalyser
This application will parse the LTSV format log files and persist in database, this can later be used to integrate with other analytical tools.


# Pre-requistes 
1. Java - 11+
2. Spring boot plugin
3. Maven - 3+
4. Postgres - 13.3
5. docker-engine - 20+
6. docker-compose - 3.x

# Maven build
Run the following in the root directory of the project.
``` mvn package ```

# Docker Commands
## To start the containers 
``` docker-compose up -d ``` 
## To rebuild the application image 
``` docker-compose build ```
## To remove the containers 
``` docer-compose down ```

