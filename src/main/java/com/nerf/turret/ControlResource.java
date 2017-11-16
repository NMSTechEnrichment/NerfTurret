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
     * Get the json representation of a state. Always returns Status:OK.
     *
     * @return StringRepresentation containing JSON status.
     */
    @Get("json")
    public JsonRepresentation getPosition()
    {
        Turret turret = (Turret)getContext().getAttributes().get(Turret.IDENTIFIER);
        return new JsonRepresentation(gson.toJson(getTurretFromContext().getPosition()));
    }

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


    }

    private Turret getTurretFromContext()
    {
        return (Turret)getContext().getAttributes().get(Turret.IDENTIFIER);
    }

}
