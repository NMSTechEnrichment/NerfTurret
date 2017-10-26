package com.nerf.turret;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * The Turret.
 *
 */
public class Turret
{

    /** The pan servo for moving left and right. */
    private final Servo panServo;

    /** The tilt Servo for moving up and down. */
    private final Servo tiltServo;

    /**
     * Constructor.
     *
     * @param panServo The pan servo.
     * @param tiltServo The tilt servo.
     */
    private Turret(Servo panServo, Servo tiltServo)
    {
        this.panServo = panServo;
        this.tiltServo = tiltServo;
    }

    /**
     * Initialize the Turret.
     *
     */
    public void init()
    {
        this.panServo.init();
        this.tiltServo.init();
    }

    /**
     * Move the turret up.
     *
     */
    public void moveUp()
    {
        tiltServo.move(tiltServo.getPosition() + 5);
    }

    /**
     * Move the turret down.
     *
     */
    public void moveDown()
    {
        tiltServo.move(tiltServo.getPosition() - 5);
    }

    /**
     * Move the turret right.
     *
     */
    public void moveRight()
    {
        panServo.move(panServo.getPosition() + 5);
    }

    /**
     * Move the turret left.
     *
     */
    public void moveLeft()
    {
        panServo.move(panServo.getPosition() - 5);
    }

    /**
     * Create a new Turret.
     *
     * @return A new Turret.
     */
    public static Turret create()
    {
        Servo panServo = new Servo(15, 0);
        Servo tiltServo = new Servo(16, 0);

        return new Turret(panServo, tiltServo);
    }

    public static void main(String[] args) throws IOException
    {
        // Create the turret.
        Turret turret = Turret.create();
        // Initialize it
        turret.init();

        // Read the keyboard for movement.
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String input = "";

        while(!"q".equals(input))
        {
            input = br.readLine();

            if("w".equals(input)) {
                System.out.println("Moving Up.");
                turret.moveUp();
            }
            else if("s".equals(input)) {
                System.out.println("Moving Down.");
                turret.moveDown();
            }
            else if("a".equals(input)) {
                System.out.println("Moving Left.");
                turret.moveLeft();
            }
            else if("d".equals(input)) {
                System.out.println("Moving Right.");
                turret.moveRight();
            }
        }

        System.out.println("Quitting.");

    }

}
