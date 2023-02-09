package com.acmemail.judah.cartesian_plane.sandbox.command_arithmetic;

/**
 * This class is part of a 
 * demonstration of the <em>command pattern.</em>
 * It multiplies two numbers.
 * It is an example of
 * "a concrete class implementing the command interface."
 * 
 * @author Jack Straub
 */
public class Multiply implements OpCommand
{
    private final Operands  operands;
    
    /**
     * Encapsulates a binary arithmetic operation in which
     * the first operand is multiplied by the second operand.
     * 
     * @param operands  the operands to multiply
     */
    public Multiply( Operands operands )
    {
        this.operands = operands;
    }
    
    @Override
    public void execute()
    {
        operands.multiply();
    }
}
