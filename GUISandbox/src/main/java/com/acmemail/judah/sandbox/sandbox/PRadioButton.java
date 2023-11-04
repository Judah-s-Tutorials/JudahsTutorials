package com.acmemail.judah.sandbox.sandbox;

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
    private final T property;
    
    public PRadioButton( T property )
    {
        this.property = property;
    }

    @Override
    public T get()
    {
        return property;
    }

}
