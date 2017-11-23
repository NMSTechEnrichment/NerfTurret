package com.nerf.turret;

import com.pi4j.wiringpi.Gpio;
import com.pi4j.wiringpi.SoftPwm;

/**
 * A Servo.
 *
 */
public class Servo
{

    /** The name of the servo, cannot be changed. */
    private final String name;

    /** The GPIO pin, cannot be reset. */
    private final int pin;

     /** The Servo's position position. */
    private int position;

    /**
     * Constructor.
     *
     * @param name The servo name.
     * @param pin The GPIO pin.
     * @param position The initial position.
     */
    public Servo(String name, int pin, int position)
    {
        this.name = name;
        this.pin = pin;
        this.position = position;
    }

    /**
     * Initialize the Servo.
     */
    public void init()
    {
        Gpio.wiringPiSetup();

        SoftPwm.softPwmCreate(pin, 0, 100);
    }


    /**
     * Set the Servo position and move it.
     *
     * @param position The new position to move to.
     */
    public void move(int position)
    {
        this.position = position;
        SoftPwm.softPwmWrite(pin, this.position);

        // TODO This isn't working well with our servos. Figure out why.
    }

    public int getPin()
    {
        return pin;
    }

    public int getPosition()
    {
        return position;
    }

    public void setPosition(int position)
    {
        printLogMessage("Setting position to " + position);
        this.position = position;
    }

    /**
     * Print the given log message along with the name of the servo.
     *
     * @param message Message to print.
     */
    private void printLogMessage(String message)
    {
        System.out.println(name + " servo: " + message);
    }
}
