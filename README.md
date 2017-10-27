# NerfTurret
An automatic Nerf Gut Turret.

This uses Pi4J to control GPIO on the Pi, see [http://pi4j.com/index.html](http://pi4j.com/index.html) for details, or the javadoc at [http://pi4j.com/apidocs/index.html](http://pi4j.com/apidocs/index.html) for info on how you program it.

## October 27, 2017

- Prototyped Servos and a simple turret that moves up, down, left and right.
- Servo object can be configured to have a GPIO pin and a position and has the functionality to move.
- Turret contains two servos and translates up, down, left, right commands in to movement values.
