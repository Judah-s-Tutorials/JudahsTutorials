package com.acmemail.judah.cartesian_plane.sandbox.command_arithmetic;

/**
 * Encapsulates a sequence of numbers 
 * representing operands to an arithmetic operation.
 * <p>
 * This is part of a demonstration
 * of the <em>command pattern;</em>
 * it represents the "request class."
 * </p>
 * 
 * @author Jack Straub
 */
public class Operands
{
    private final double[]  operands;
    private double          result      = 0;
    
    /**
     * Constructor. 
     * Records the given arguments
     * preserving order.
     * Recorded arguments
     * may be accessed by index.
     * 
     * @param args  the arguments to record.
     * 
     * @see #get(int)
     * @see #getNumOperands
     */
    public Operands( double... args )
    {
        int inx = 0;
        operands = new double[args.length];
        for ( double operand : args )
            operands[inx++] = operand;
    }
    
    /**
     * Returns the operand at the given index.
     * The index matches the order
     * in which the operands
     * were originally passed.
     * 
     * @param inx   the given index
     * 
     * @return  the operand at the given index
     * 
     * @throws IndexOutOfBoundsException
     *         if the given index
     *         is outside the valid range of indices
     */
    public double get( int inx )
    {
        return operands[inx];
    }
    
    /**
     * Gets the number of recorded operands.
     * 
     * @return  the number of recorded operands
     * 
     * @throws  IndexOutOfBoundsException
     *          if there are fewer than 
     *          two operands in the sequence
     */
    public int getNumOperands()
    {
        return operands.length;
    }
    
    /**
     * Gets the result of the operation
     * performed on the sequence of operands.
     * 
     * @return  the result of the operation
     *          performed on the sequence of operands
     */
    public double getResult()
    {
        return result;
    }
    
    /**
     * Multiplies the first two operands in the sequence.
     * 
     * @throws  IndexOutOfBoundsException
     *          if there are fewer than 
     *          two operands in the sequence
     */
    public void multiply()
    {
        result = get( 0 ) * get( 1 );
    }
    
    /**
     * Divides the first operand in the sequence
     * by the second.
     * 
     * @throws  IndexOutOfBoundsException
     *          if there are fewer than 
     *          two operands in the sequence
     */
    public void divide()
    {
        result = get( 0 ) / get( 1 );
    }
    
    /**
     * Adds the first two operands in the sequence.
     * 
     * @throws  IndexOutOfBoundsException
     *          if there are fewer than 
     *          two operands in the sequence
     */
    public void add()
    {
        result = get( 0 ) + get( 1 );
    }
    
    /**
     * Subtracts the second operand in the sequence
     * from the first.
     * 
     * @throws  IndexOutOfBoundsException
     *          if there are fewer than 
     *          two operands in the sequence
     */
    public void subtract()
    {
        result = get( 0 ) - get( 1 );
    }
    
    /**
     * Negates the first operand in the sequence.
     * 
     * @throws  IndexOutOfBoundsException
     *          if there is not at least 
     *          one operand in the sequence
     */
    public void negate()
    {
        result = -get( 0 );
    }
}
