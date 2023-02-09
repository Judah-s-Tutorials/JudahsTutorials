package com.acmemail.judah.cartesian_plane.sandbox.command_arithmetic;

/**
 * This class is part of a 
 * demonstration of the <em>command pattern.</em>
 * It subtracts two numbers.
 * It is an example of
 * "a concrete class implementing the command interface."
 * 
 * @author Jack Straub
 */
public class Subtract implements OpCommand
{
    private final Operands  operands;
    private double          result  = 0;
    
    /**
     * Encapsulates a binary arithmetic operation in which
     * the second operand is subtracted from the first operand.
     * 
     * @param operands  the operands to add
     */
    public Subtract( Operands operands )
    {
        this.operands = operands;
    }
    
    @Override
    public void execute()
    {
        operands.subtract();
    }
}
