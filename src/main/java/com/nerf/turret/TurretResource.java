package com.nerf.turret;

import com.google.gson.Gson;
import org.restlet.resource.ServerResource;

/**
 * A resource for interacting with a Turret.
 */
public abstract class TurretResource extends ServerResource
{

    /** Handles the conversion of objects to/from json. */
    private final Gson gson;

    /**
     * Constructor.
     *
     */
    public TurretResource()
    {
        gson = new Gson();
    }

    /**
     * Get the {@link Gson} instance for converting objects to/from json
     *
     * @return The working {@link Gson} instance.
     */
    public Gson getGson()
    {
        return gson;
    }


    /**
     * Get the turret out of the server context.
     *
     * @return The active {@link Turret}.
     */
    public Turret getTurretFromContext()
    {
        return (Turret)getContext().getAttributes().get(Turret.IDENTIFIER);
    }
}
