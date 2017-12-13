package com.nerf.turret;

import org.restlet.data.Status;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.resource.Get;
import org.restlet.resource.Post;

import java.io.IOException;

/**
 * Resource for turning the Turret's auto mode on or of.
 *
 */
public class TriggerResource extends TurretResource
{

    /**
     * Constructor.
     *
     */
    public TriggerResource()
    {
        System.out.println("Creating Fire resource.");
    }


    @Post("json")
    public void fire(JsonRepresentation autoRepresentation)
    {
        System.out.println("Attempting to pull the trigger...");
        try
        {
            Turret.Fire fire = getGson().fromJson(autoRepresentation.getText(), Turret.Fire.class);

            if(fire.pullTrigger())
                getTurretFromContext().fire();

            System.out.println("Trigger pulled.");
        }
        catch (IOException e)
        {
            setStatus(Status.CLIENT_ERROR_BAD_REQUEST);
        }

    }

}

