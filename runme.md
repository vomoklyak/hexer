# Hexer
Application for directory checksum computation.
Technologies: Java 8.
Build tool - Maven


### Maven setup
To build project (with tests):
mvn clean install

To build project (without tests):
mvn clean install -Dmaven.test.skip=true

To run unit tests:
mvn verify


### Run Application
To run application
1. Build application mvn clean install
2. Copy jar file to folder (convenient for you).
3. Create output folder here.
4. Run application from command line java -jar fileJarName.jar directoryPath

Example:
create folder hexer on disk d 
copy hexer-1.0-SNAPSHOT.jar into /d/hexer/
create folder /d/output/
run command line from /d/hexer/  java -jar hexer-1.0-SNAPSHOT.jar directoryPath
open result file /d/output/output.dat 


### Run Application from IDE
Run com.hexer.Application.main method.
You can use input/output folders in the project root directory for input/output. 


### Application configuration
You can use JVM parameters to configure the application
Also you can set variables in your OS (permanently).

To specify custom input folder:
-Dinput

To specify custom output folder:
-Doutput

To specify custom read buffer size:
-Dreader.buffer.size

To specify custom digest algorithm:
-Ddigest.algorithm

Example:
Read from /d/input/, write to /d/output/out.dat, with buffer size 1024, with digest algorithm SHA-256
java -Dinput=/d/input/ -Doutput=/d/output/output.dat -Dreader.buffer.size=1024 -Ddigest.algorithm=SHA-256 -jar hexer-1.0-SNAPSHOT.jar
