package com.nerf.turret;

import edu.cmu.ri.createlab.hummingbird.HummingbirdRobot;

/**
 * A Servo.
 *
 */
public class Servo
{
    /** The minimum position. */
    private static final int MIN_POSITION = 0;

    /** The maximum position. */
    private static final int MAX_POSITION = 255;

    /** The name of the servo, cannot be changed. */
    private final String name;

    /** The GPIO pin, cannot be reset. */
    private final int pin;

     /** The Servo's position position. */
    private int position;

    /** An initialized {@link HummingbirdRobot}. */
    private final HummingbirdRobot hummingbirdRobot;

    /**
     * Constructor.
     *
     * @param name The servo name.
     * @param pin The GPIO pin.
     * @param position The initial position.
     */
    public Servo(String name, int pin, int position, HummingbirdRobot hummingbirdRobot)
    {
        this.name = name;
        this.pin = pin;
        this.position = position;
        this.hummingbirdRobot = hummingbirdRobot;
    }

    /**
     * Set the Servo position and move it. The value is limited between 0 and 255.
     *
     * @param position The new position to move to.
     */
    public void move(int position)
    {
        int toSet = position;
        if(position < MIN_POSITION)
            toSet = MIN_POSITION;
        else if(position > MAX_POSITION)
            toSet = MAX_POSITION;

        this.position = toSet;
        this.hummingbirdRobot.setServoPosition(pin, toSet);
    }

    /**
     * Move the servo to it's initial position.
     *
     */
    public void init()
    {
        move(position);
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
