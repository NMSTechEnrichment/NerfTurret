package com.nerf.turret;

import org.restlet.Application;
import org.restlet.Context;
import org.restlet.Server;
import org.restlet.data.Protocol;
import org.restlet.service.CorsService;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

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



        Turret turret = Turret.create();

        HashMap<String, Object> turretAttributeMap = new HashMap<>();
        turretAttributeMap.put(Turret.IDENTIFIER, turret);
        Context context = new Context();
        context.setAttributes(turretAttributeMap);

        Server server = new Server(context, Protocol.HTTP, 80, ControlResource.class);

        CorsService corsService = new CorsService();
        corsService.setAllowedOrigins(new HashSet(Arrays.asList("*")));
        corsService.setAllowedCredentials(true);
        Application application = server.getApplication();
        application.getServices().add(corsService);

        server.start();

//        new Server(context, Protocol.HTTP, 80, ControlResource.class).start();
    }

}
