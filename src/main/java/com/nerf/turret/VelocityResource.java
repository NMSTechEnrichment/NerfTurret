package com.nerf.turret;

import org.restlet.data.Status;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.resource.Get;
import org.restlet.resource.Post;

import java.io.IOException;

/**
 * Resource settings the {@link Turret}'s pan and tilt velocity.
 *
 */
public class VelocityResource extends TurretResource
{

    /**
     * Constructor.
     *
     */
    public VelocityResource()
    {
        System.out.println("Creating VelocityResource resource.");
    }

    /**
     * Get the json representation of the auto state.
     *
     * @return StringRepresentation containing JSON auto status.
     */
    @Get("json")
    public JsonRepresentation getVelocity()
    {
        return new JsonRepresentation(getGson().toJson(getTurretFromContext().getVelocity()));
    }

    @Post("json")
    public void setAuto(JsonRepresentation velocityRepresentation)
    {
        System.out.println("Attempting to set the turret velocity...");
        try
        {
            Turret.Position velocity = getGson().fromJson(velocityRepresentation.getText(), Turret.Position.class);

            getTurretFromContext().setVelocity(velocity);

            System.out.println("Turret velocity set to " + velocity.pan + ", " + velocity.tilt);
        }
        catch (IOException e)
        {
            setStatus(Status.CLIENT_ERROR_BAD_REQUEST);
        }

    }

}

