# tichuana-tichu

This project is a java implementation of the card game tichu. It consists of a server and a client. It allows four clients
to connect to the server and play the game against each other.

This is a special fork of the project pulling in dependencies that may not be available
in the defualt jdk with gradle.

## Building
To build, simply run ```gradlew build```. This will create a jar under build/libs. Running that jar with 
```java -jar tichuana-tichu.jar```
will start the client.

## Usage

First the server needs to be started. By default it will run on port ```8080```. This can be changed by editing the 
configuration file at ```ch/tichuana/tichu/server/resources/config.properties```.

Once the server is running the four clients can connect to it by entering a username and a password.

## Credits

This application was built as a part of the "IT-Project" module at the University of Applied Sciences and Arts of 
Northwestern Switzerland (FHNW) in the fall semester of 2019.

### Developers

- Philipp Hartmann (e-vents)
- Christian Roth (sekthor)
- Dominik Spillmann (Cykkra)
