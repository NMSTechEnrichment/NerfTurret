package com.nerf.turret;

import org.restlet.data.Status;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.resource.Get;
import org.restlet.resource.Post;

import java.io.IOException;

/**
 * Web resource for controlling the Nerf Turret's position.
 *
 */
public class ControlResource extends TurretResource
{


    /**
     * Constructor, instantiates the turret.
     *
     */
    public ControlResource()
    {
        System.out.println("Creating up Control resource!");

    }

    /**
     * Get the json representation of the position.
     *
     * @return StringRepresentation containing JSON status.
     */
    @Get("json")
    public JsonRepresentation getPosition()
    {
        return new JsonRepresentation(getGson().toJson(getTurretFromContext().getPosition()));
    }

    /**
     * Set the position.
     *
     * @param positionRepresentation {@link JsonRepresentation} of the position.
     */
    @Post("json")
    public void setPosition(JsonRepresentation positionRepresentation)
    {
        System.out.println("Attempting to set position...");
        try
        {
            Turret.Position position = getGson().fromJson(positionRepresentation.getText(), Turret.Position.class);

            getTurretFromContext().setPosition(position);

        }
        catch (IOException e)
        {
            setStatus(Status.CLIENT_ERROR_BAD_REQUEST);
        }

        System.out.println("Position set...");

    }

}
