# CSCI2251-project

### Building the project

To build this project, [Apache Ant](https://ant.apache.org/) should be installed. Ant is a build system that automates 
the process of compilation and the packing of the program into a runnable .jar file. You can either install it and use it 
directly from command line, or use it from an IDE that has a plugin for it. In IntelliJ IDEA, just right click the
build.xml, and click "Add as Ant Build File".
 
To build the project, type `ant jar` (or run the jar target in IDEA), This will both compile the source and generate 
two jars, one for client and one for server. The jars will be stored in the new folder, `build/jar`. You can also run 
them both with ant by running the `runclient` or `runserver` targets.

### Project Structure

The source code is stored in the `src` folder (in IDEA, mark this as the sources root folder).
It is divided into three main packages:

| Package | Description |
|--------|-------------|
| client | Contains code specific to client, including its driver class. |
| server | Contains code specific to server.|
| common | Contains code used by both client and server (such as RentalProperty) |