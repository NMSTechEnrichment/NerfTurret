package com.nerf.turret;

import com.google.gson.JsonObject;
import org.restlet.data.MediaType;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;

/**
 * Web resource for controlling the Nerf Turret.
 *
 */
public class ControlResource extends ServerResource
{

    // TODO Instantiate a Turret in here to be controlled.

    /**
     * Get the json representation of a state. Always returns Status:OK.
     *
     * @return StringRepresentation containing JSON status.
     */
    @Get("json")
    public Representation getState()
    {
        JsonObject result = new JsonObject();

        result.addProperty("Status", "OK");

        return new StringRepresentation(result.toString(), MediaType.APPLICATION_ALL_JSON);

    }

    /**
     * Get at the root returns "Hello World!"
     *
     * @return Hello World!
     */
    @Get
    public String toString()
    {
        return "Hello World!";
    }

    // TODO Add PUT resources that accept a command to move the turret.

}
