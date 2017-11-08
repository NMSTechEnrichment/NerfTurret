package com.nerf.turret;

import org.restlet.Server;
import org.restlet.data.Protocol;

/**
 * Main class for starting a server.
 *
 */
public class NerfTurretServer
{

    /**
     * Main method. Starts a server on port 80 and registers the ControlResource.
     *
     * @param args Arguments.
     * @throws Exception If it blows up.
     */
    public static void main(String[] args) throws Exception
    {
        new Server(Protocol.HTTP, 80, ControlResource.class).start();
    }

}
