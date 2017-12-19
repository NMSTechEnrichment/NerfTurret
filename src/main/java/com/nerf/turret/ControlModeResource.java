package com.nerf.turret;

import org.restlet.data.Status;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.resource.Get;
import org.restlet.resource.Post;

import java.io.IOException;

/**
 * Resource settings the {@link Turret}'s mode of operation.
 *
 */
public class ControlModeResource extends TurretResource
{

    /**
     * Constructor.
     *
     */
    public ControlModeResource()
    {
        System.out.println("Creating ControlMode resource.");
    }

    /**
     * Get the json representation of the control mode.
     *
     * @return StringRepresentation containing JSON control mode.
     */
    @Get("json")
    public JsonRepresentation getVelocity()
    {
        return new JsonRepresentation(getGson().toJson(getTurretFromContext().getTurretControlMode().getControlMode()));
    }

    @Post("json")
    public void setAuto(JsonRepresentation controlModeRepresentation)
    {
        System.out.println("Attempting to set the turret control mode state...");
        try
        {
            Turret.TurretControlMode turretControlMode = getGson().fromJson(controlModeRepresentation.getText(), Turret.TurretControlMode.class);

            getTurretFromContext().setControlMode(turretControlMode);

            System.out.println("Turret control mode set to " + turretControlMode.controlMode);
        }
        catch (IOException e)
        {
            setStatus(Status.CLIENT_ERROR_BAD_REQUEST);
        }

    }

}

