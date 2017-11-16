# NerfTurret
An automatic Nerf Turret.

This uses Pi4J to control GPIO on the Pi, see [http://pi4j.com/index.html](http://pi4j.com/index.html) for details, or 
the javadoc at [http://pi4j.com/apidocs/index.html](http://pi4j.com/apidocs/index.html) for info on how you program it.

Uses Restlet as a lightweight web server, see [https://restlet.com/open-source/](https://restlet.com/open-source/) for 
details, or the javadoc at [https://restlet.com/open-source/documentation/user-guide/2.3](https://restlet.com/open-source/documentation/user-guide/2.3) for info on how to use it.

## November 16, 2017

- Restlet Server now serves a web page that has simple slider controls for controlling a turret. Web page uses JavaScript 
and JQuery to create the sliders and collect input from them and send it to the control server.
- Added a ```/control``` resource to the server for getting the current turret position and setting it.

## November 8, 2017

- Added a simple web server using Restlet to serve REST resources that can eventually be used to control the turret 
remotely via the web. Added ```restlet```, ```gson``` and ```json``` jars the ```lib``` directory.
- We need a script to compile the java code on the Pi. Compiling Java is very cumbersome, if we had a full IDE on 
the Pi we could do it using that, but since we are coding on IntelliJ on Windows it needs to be recompiled on the 
Pi.  

To compile, from the project root directory run:
```
javac -cp ./lib/*:./src/main/java/* src/main/java/com/nerf/turret/ControlResrouce.java src/main/java/com/nerf/turret/NerfTurretServer.java 
``` 

And to run it, from the project root directory run:
```
sudo java -cp ./lib/*:./src/main/java/* com.nerf.turret.NerfTurretServer 
```

Ideally we'll compile in to a ```target``` directory next to our ```src``` directory.

## October 27, 2017

- Prototyped Servos and a simple turret that moves up, down, left and right.
- Servo object can be configured to have a GPIO pin and a position and has the functionality to move.
- Turret contains two servos and translates up, down, left, right commands in to movement values.

