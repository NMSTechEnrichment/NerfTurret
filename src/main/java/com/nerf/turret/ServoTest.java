package com.nerf.turret;


import edu.cmu.ri.createlab.hummingbird.HummingbirdRobot;

import java.io.IOException;

/**
 * As simple test to move servos.
 */
public class ServoTest
{

    public static void main(String[] args) throws IOException, InterruptedException {

        System.out.println("Starting ServoTest");

        HummingbirdRobot hummingbirdRobot = new HummingbirdRobot();

        for(int i=0; i <= 255; i++)
        {
            System.out.println("Setting servo position: " + i);
            hummingbirdRobot.setServoPosition(1, i);
            hummingbirdRobot.setServoPosition(2, i);

            Thread.sleep(30);
        }


        hummingbirdRobot.setServoPosition(1, 0);
        hummingbirdRobot.setServoPosition(2, 0);


        Thread.sleep(1000);

        System.out.println("Exiting ServoTest");
        hummingbirdRobot.disconnect();
    }

}
