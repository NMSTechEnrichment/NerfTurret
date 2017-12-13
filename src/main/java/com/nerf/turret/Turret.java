package com.nerf.turret;

import edu.cmu.ri.createlab.hummingbird.HummingbirdRobot;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * The Turret.
 *
 */
public class Turret
{

    /** String ot identify turrets in a restlet context map. */
    static final String IDENTIFIER = "Turret";

    /** The pan servo for moving left and right. */
    private final Servo panServo;

    /** The tilt Servo for moving up and down. */
    private final Servo tiltServo;

    /** The servo for pulling the trigger. */
    private final Servo triggerServo;

    /** The auto mode of the Turret. Volatile because it can be accessed from multple Restlet resources.*/
    private volatile boolean auto;

    /** The direction moving while scanning in auto mode, right by default. */
    private ScanDirection direction = ScanDirection.RIGHT;

    /**
     * Constructor.
     *
     * @param panServo The pan servo.
     * @param tiltServo The tilt servo.
     */
    private Turret(Servo panServo, Servo tiltServo, Servo triggerServo)
    {
        this.panServo = panServo;
        this.tiltServo = tiltServo;
        this.triggerServo = triggerServo;
    }

    /**
     * Move the turret to it's initial position.
     *
     */
    public void init()
    {
        panServo.init();
        tiltServo.init();
        triggerServo.init();
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
        panServo.move(panServo.getPosition() - 20);
    }

    /**
     * Move the turret left.
     *
     */
    public void moveLeft()
    {
        panServo.move(panServo.getPosition() + 20);
    }

    public void fire()
    {
        triggerServo.move(100);
        sleep(500);
        triggerServo.move(0);

    }

    /**
     * Sleep the thread for the given duration.
     *
     * @param duration Sleep duration in milliseconds.
     */
    private static void sleep(long duration)
    {
        try {
            Thread.sleep(duration);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Start scanning, if in auto mode.
     */
    public void scan()
    {
        if(!auto)
            return;

        panServo.setPosition(Servo.MIN_POSITION);
        direction = ScanDirection.RIGHT;

        while(auto)
        {
            scanStep();

            // Wait until taking another step.
            sleep(1000);
        }

    }

    /**
     * Perform a scan step.
     */
    public void scanStep()
    {
        if(panServo.getPosition() == Servo.MAX_POSITION)
            direction = ScanDirection.RIGHT;
        else if(panServo.getPosition() == Servo.MIN_POSITION)
            direction = ScanDirection.LEFT;

        switch(direction)
        {
            case LEFT:
                moveLeft();
                break;
            case RIGHT:
                moveRight();
        }
    }

    /**
     * Set the position of the turret.
     *
     * @param position The turret position to set.
     */
    public void setPosition(Position position)
    {
        panServo.move(position.pan);
        tiltServo.move(position.tilt);
    }

    /**
     * Set the auto mode of the Turret and starts the scan.
     *
     * @param auto The auto mode, contains true or false.
     */
    public void setAuto(Auto auto)
    {
        this.auto = auto.auto;

        if(auto.isAuto())
            scan();
    }

    public Auto getAuto()
    {
        return new Auto(auto);
    }
    /**
     * Get the position of the turret.
     *
     * @return The current position.
     */
    public Position getPosition()
    {
        return new Position(panServo.getPosition(), tiltServo.getPosition());
    }

    /**
     * Create a new Turret.
     *
     * @return A new Turret.
     */
    public static Turret create()
    {
        HummingbirdRobot hummingbirdRobot = new HummingbirdRobot();
        Servo panServo = new Servo("Pan",1, 0, hummingbirdRobot);
        Servo tiltServo = new Servo("Tilt",2, 0, hummingbirdRobot);
        Servo triggerServo = new Servo("Trigger",3, 0, hummingbirdRobot);

        return new Turret(panServo, tiltServo, triggerServo);
    }

    /**
     * Control the turret from the command line.
     *
     * @param args Arguments.
     * @throws IOException If the command line input can't be read.
     */
    public static void main(String[] args) throws IOException
    {
        // Create the turret.
        Turret turret = Turret.create();
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

    /**
     * Enum to represent the scan movement direction.
     */
    private enum ScanDirection
    {
        LEFT,
        RIGHT
    }

    /**
     * Represents the {@link Turret} position.
     *
     */
    public static class Position
    {
        /** The pan position. */
        public int pan;

        /** The tilt position. */
        public int tilt;

        /**
         * Constructor.
         *
         * @param pan The pan position.
         * @param tilt The tilt position.
         */
        public Position(int pan, int tilt)
        {
            this.pan = pan;
            this.tilt = tilt;
        }
    }

    /**
     * Represents the auto state of the {@link Turret}.
     *
     */
    public static class Auto
    {
        /** The auto state. */
        public boolean auto;

        /**
         * Constructor.
         *
         * @param auto The auto state.
         */
        public Auto(boolean auto)
        {
            this.auto = auto;
        }

        public boolean isAuto()
        {
            return auto;
        }
    }

    /**
     * Represents a request to pull the trigger.
     *
     */
    public static class Fire
    {
        /** The fire request. */
        public boolean fire;

        public boolean pullTrigger()
        {
            return fire;
        }

    }

}
