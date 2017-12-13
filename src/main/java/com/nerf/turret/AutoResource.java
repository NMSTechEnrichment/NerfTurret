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
public class AutoResource extends TurretResource
{

    /**
     * Constructor.
     *
     */
    public AutoResource()
    {
        System.out.println("Creating Auto mode resource.");
    }

    /**
     * Get the json representation of the auto state.
     *
     * @return StringRepresentation containing JSON auto status.
     */
    @Get("json")
    public JsonRepresentation getAuto()
    {
        Turret turret = (Turret)getContext().getAttributes().get(Turret.IDENTIFIER);
        return new JsonRepresentation(getGson().toJson(getTurretFromContext().getAuto()));
    }

    @Post("json")
    public void setAuto(JsonRepresentation autoRepresentation)
    {
        System.out.println("Attempting to set auto state...");
        try
        {
            Turret.Auto auto = getGson().fromJson(autoRepresentation.getText(), Turret.Auto.class);

            getTurretFromContext().setAuto(auto);

            System.out.println("Auto state set to " + auto.auto);
        }
        catch (IOException e)
        {
            setStatus(Status.CLIENT_ERROR_BAD_REQUEST);
        }

    }

}

