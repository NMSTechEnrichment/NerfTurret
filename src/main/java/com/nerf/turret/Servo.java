package com.nerf.turret;

import com.pi4j.wiringpi.Gpio;
import com.pi4j.wiringpi.SoftPwm;

/**
 * A Servo.
 *
 */
public class Servo
{

    /** The GPIO pin, cannot be reset. */
    private final int pin;

     /** The Servo's position position. */
    private int position;

    /**
     * Constructor.
     *
     * @param pin The GPIO pin.
     * @param position The initial position.
     */
    public Servo(int pin, int position)
    {
        this.pin = pin;
        this.position = position;
    }

    /**
     * Initialize the Servo.
     */
    public void init()
    {
        Gpio.wiringPiSetup();

        SoftPwm.softPwmCreate(pin, 0, 1000);
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
        this.position = position;
    }
}
