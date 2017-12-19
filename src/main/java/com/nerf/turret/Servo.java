package com.nerf.turret;

import edu.cmu.ri.createlab.hummingbird.HummingbirdRobot;

import java.util.Optional;

/**
 * A Servo.
 *
 */
public class Servo
{
    /** The minimum position. */
    public static final int MIN_POSITION = 0;

    /** The maximum position. */
    public static final int MAX_POSITION = 225;

    /** The maximum velocity. */
    public static final int MAX_VELOCITY = 10;

    /** The minimum velocity. */
    public static final int MIN_VELOCITY = MAX_VELOCITY * -1;

    /** The name of the servo, cannot be changed. */
    private final String name;

    /** The GPIO pin, cannot be reset. */
    private final int pin;

     /** The Servo's position position. */
    private int position;

    private int velocity = 0;

    /** An initialized {@link HummingbirdRobot} if there is one */
    private final Optional<HummingbirdRobot> hummingbirdRobot;

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
        this.hummingbirdRobot = Optional.ofNullable(hummingbirdRobot);
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
        this.hummingbirdRobot.ifPresent(robot -> robot.setServoPosition(pin, position));
    }

    /**
     * Move based on the current velocity.
     *
     */
    public void move()
    {
        move(position + velocity);
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

    public int getVelocity()
    {
        return velocity;
    }

    /**
     * Set the velocity, keeping it within the min and max velocity limits.
     *
     * @param velocity Velocity to set.
     */
    public void setVelocity(int velocity)
    {
        this.velocity = limit(velocity, MIN_VELOCITY, MAX_VELOCITY);

    }

    /**
     * Limit the given value within the given minimum and maximums.
     *
     * @param value The value to limit.
     * @param minLimit The minimum limit.
     * @param maxLimit The maximum limit.
     * @return The value limited.
     */
    private static int limit(int value, int minLimit, int maxLimit)
    {
        if(value < minLimit)
            return minLimit;
        else if(value > maxLimit)
            return maxLimit;

        return value;
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
