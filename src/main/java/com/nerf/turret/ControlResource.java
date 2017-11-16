package com.nerf.turret;

import com.google.gson.Gson;
import org.restlet.data.Status;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.resource.Get;
import org.restlet.resource.Post;
import org.restlet.resource.ServerResource;

import java.io.IOException;

/**
 * Web resource for controlling the Nerf Turret.
 *
 */
public class ControlResource extends ServerResource
{

    /** Handles the conversion of objects to/from json. */
    private final Gson gson;

    /**
     * Constructor, instantiates the turret.
     *
     */
    public ControlResource()
    {
        System.out.println("Setting up resource!");
        gson = new Gson();

    }

    /**
     * Get the json representation of the position.
     *
     * @return StringRepresentation containing JSON status.
     */
    @Get("json")
    public JsonRepresentation getPosition()
    {
        Turret turret = (Turret)getContext().getAttributes().get(Turret.IDENTIFIER);
        return new JsonRepresentation(gson.toJson(getTurretFromContext().getPosition()));
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
            Turret.Position position = gson.fromJson(positionRepresentation.getText(), Turret.Position.class);

            getTurretFromContext().setPosition(position);

        }
        catch (IOException e)
        {
            setStatus(Status.CLIENT_ERROR_BAD_REQUEST);
        }

        System.out.println("Position set...");

    }

    /**
     * Get the turret out of the server context.
     *
     * @return The active {@link Turret}.
     */
    private Turret getTurretFromContext()
    {
        return (Turret)getContext().getAttributes().get(Turret.IDENTIFIER);
    }

}
