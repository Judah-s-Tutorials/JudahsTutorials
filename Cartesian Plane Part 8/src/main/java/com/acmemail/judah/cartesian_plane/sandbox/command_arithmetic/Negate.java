package com.acmemail.judah.cartesian_plane.sandbox.command_arithmetic;

/**
 * This class is part of a 
 * demonstration of the <em>command pattern.</em>
 * It negates a number.
 * It is an example of
 * "a concrete class implementing the command interface."
 * 
 * @author Jack Straub
 */
public class Negate implements OpCommand
{
    private final Operands  operands;
    
    /**
     * Encapsulates a unary arithmetic operation in which
     * the operand is negated.
     * 
     * @param operands  the operands to add
     */
    public Negate( Operands operands )
    {
        this.operands = operands;
    }
    
    @Override
    public void execute()
    {
        operands.negate();
    }
}
