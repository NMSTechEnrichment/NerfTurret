package com.nerf.turret;

import com.nerf.facerecognition.FaceRecognitionService;
import org.restlet.Application;
import org.restlet.Component;
import org.restlet.Context;
import org.restlet.Restlet;
import org.restlet.data.Protocol;
import org.restlet.resource.Directory;
import org.restlet.routing.Router;

import java.net.URI;
import java.util.HashMap;

/**
 * Main class for starting a server.
 *
 */
public class NerfTurretServer
{

    /**
     * Main method. Starts a server on port 80 and registers the ControlResource.
     *
     * @param args Arguments, -d to start in "disconnected" mode (no hummingbird robot connected).
     * @throws Exception If it blows up.
     */
    public static void main(String[] args) throws Exception
    {
        // Create a disconnected turret if the command line argument was there.
        Turret turret;
        if(args.length == 1)
        {
            if("-d".equals(args[0]))
                turret = Turret.createDisconnected();
            else
            {
                System.out.println("Command line argument " + args[0] + " not recognized, creating a turret connected to a HummingbirdRobot");
                turret = Turret.create();
            }
        }
        else
            turret = Turret.create();

        // Create a component, this manages the web application.
        Component component = new Component();
        component.getServers().add(Protocol.HTTP, 80);
        component.getClients().add(Protocol.FILE);

        // Create an application for serving files.
        Application application = new Application()
        {
            @Override
            public Restlet createInboundRoot()
            {
                // Get the absolute path as a URI to resolve the files.
                String rootURI = "file://" + URI.create(NerfTurretServer.class.getResource("/web").getFile()).toString();
                return new Directory(getContext(), rootURI);
            }
        };

        // Setting up the attributes in the context to be used in the server.
        HashMap<String, Object> turretAttributeMap = new HashMap<>();
        turretAttributeMap.put(Turret.IDENTIFIER, turret);
        Context context = new Context();
        context.setAttributes(turretAttributeMap);

        // Create an application for controlling the turret.
        Application turretControlApplication = new Application()
        {
            @Override
            public Restlet createInboundRoot()
            {
                // Route calls to the application to create a new resource.
                Router router = new Router(context);
                router.attach("", ControlResource.class );
                return router;
            }
        };

        // Create an application for controlling the turret's auto mode.
        Application turretAutoApplication = new Application()
        {
            @Override
            public Restlet createInboundRoot()
            {
                // Route calls to the application to create a new resource.
                Router router = new Router(context);
                router.attach("", AutoResource.class );
                return router;
            }
        };

        // Create an application for triggering the turret.
        Application triggerApplication = new Application()
        {
            @Override
            public Restlet createInboundRoot()
            {
                // Route calls to the application to create a new resource.
                Router router = new Router(context);
                router.attach("", TriggerResource.class );
                return router;
            }
        };

        // Create an application for triggering the turret.
        Application controlModeApplication = new Application()
        {
            @Override
            public Restlet createInboundRoot()
            {
                // Route calls to the application to create a new resource.
                Router router = new Router(context);
                router.attach("", ControlModeResource.class );
                return router;
            }
        };

        // Create an application for triggering the turret.
        Application velocityApplication = new Application()
        {
            @Override
            public Restlet createInboundRoot()
            {
                // Route calls to the application to create a new resource.
                Router router = new Router(context);
                router.attach("", VelocityResource.class );
                return router;
            }
        };

        // create the service for face recognition
        // TODO pass address of actual face recognition service
        FaceRecognitionService faceContext = new FaceRecognitionService(10, "10.4.48.239", 8080);
        HashMap<String, Object> faceAttributeMap = new HashMap<>();
        faceAttributeMap.put(FaceRecognitionService.IDENTIFIER, faceContext);
        context.setAttributes(faceAttributeMap);
        Application faceApplication = new Application()
        {
            @Override
            public Restlet createInboundRoot()
            {
                Router router = new Router(context);
                router.attach("", FaceRecognitionService.WebResource.class);
                return router;
            }
        };

        // Attach the applications to the component and start it
        component.getDefaultHost().attach("/web", application); // /web for the files.
        component.getDefaultHost().attach("/control", turretControlApplication); // the turret.
        component.getDefaultHost().attach("/auto", turretAutoApplication); // the turret's auto mode.
        component.getDefaultHost().attach("/trigger", triggerApplication); // the turret's trigger.
        component.getDefaultHost().attach("/controlmode", controlModeApplication); // the turret's control mode.
        component.getDefaultHost().attach("/velocity", velocityApplication); // the turret's velocity.
        component.getDefaultHost().attach("/face", faceApplication);
        component.start();
    }
}
