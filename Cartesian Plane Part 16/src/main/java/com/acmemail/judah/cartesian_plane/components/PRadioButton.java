package com.acmemail.judah.cartesian_plane.components;

import java.util.function.Supplier;

import javax.swing.JRadioButton;

/**
 * An object of this class
 * is a JRadioButton that is associated
 * with a value of a given type.
 * The associated value
 * is established by the constructor
 * and is immutable.
 * 
 * @author Jack Straub
 * 
 * @param <T>   the given type
 * 
 * @see PButtonGroup
 */
@SuppressWarnings("serial")
public class PRadioButton<T>
    extends JRadioButton
    implements Supplier<T>
{
    /** Value encapsulated by this object. */
    private final T property;
    
    /**
     * Constructor.
     * 
     * @param property  value encapsulated by this object
     */
    public PRadioButton( T property )
    {
        this.property = property;
    }

    /**
     * Satisfies <em>get()</em>
     * as specified by <em>Supplier&lt;T&gt;</em>.
     */
    @Override
    public T get()
    {
        return property;
    }
}
