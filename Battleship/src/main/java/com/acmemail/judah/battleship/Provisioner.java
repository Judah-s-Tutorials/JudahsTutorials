package com.acmemail.judah.battleship;

import java.util.List;

import com.acmemail.judah.battleship.model.ShipType2D;

/**
 * Implementing classes act as a source for provisioning data,
 * such as grid dimension
 * what ship types to declare,
 * and which to deploy.
 */
public interface Provisioner
{
    /**
     * Gets the number of grid rows
     * currently provisioned via this object.
     * Null if provisioning of the property
     * has not been processed.
     * 
     * @return number of grid rows provisioned, null if none
     */
    public Integer getRows();

    /**
     * Gets the number of grid columns
     * currently provisioned via this object.
     * Null if provisioning of the property
     * has not been processed.
     * 
     * @return number of grid columns provisioned, null if none
     */
    public Integer getCols();

    /**
     * Gets an unmodifiable wrapper
     * around the list of ships that must be deployed.
     * 
     * @return the list of ships that must be deployed
     */
    public List<ShipType2D> getToDeploy();
    
    /**
     * Gets an unmodifiable wrapper
     * around the list of types that must be registered.
     * 
     * @return the list of types that must be registered
     */
    public List<ShipType2D> getToRegister();
}

