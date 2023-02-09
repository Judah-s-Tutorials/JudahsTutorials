package com.acmemail.judah.cartesian_plane.sandbox.command_arithmetic;

/**
 * Application launcher for the arithmetic
 * command pattern demo.
 * Queues up operations,
 * executes them and prints the results.
 * 
 * @author Jack Straub
 *
 */
public class CommandPatternDemo
{
    /**
     * Application entry point.
     * 
     * @param args  command line arguments, not used.
     */
    public static void main(String[] args)
    {
        Invoker invoker = new Invoker();
        Operands    mulOperands = new Operands( 10, 20 );
        Operands    addOperands = new Operands( -5, 120 );
        Operands    divOperands = new Operands( 20, 40 );
        Operands    subOperands = new Operands( -10, -30 );
        Operands    negOperands = new Operands( -125 );
        invoker.addCommand( new Multiply( mulOperands ) );
        invoker.addCommand( new Add( addOperands ) );
        invoker.addCommand( new Divide( divOperands ) );
        invoker.addCommand( new Subtract( subOperands ) );
        invoker.addCommand( new Negate( negOperands ) );
        
        invoker.performOperations();
        
        printBinaryResult( "*", mulOperands );
        printBinaryResult( "+", addOperands );
        printBinaryResult( "/", divOperands );
        printBinaryResult( "-", subOperands );
        printUnaryResult( "-", negOperands );
    }

    /**
     * Print the result of a binary operation
     * for a given sequence of operands.
     * The caller passes a symbol
     * representing the operation,
     * such as "+" for addition.
     * 
     * @param symbol    the symbol representing the operation
     * @param operands  the given sequence of operands
     */
    private static void printBinaryResult( String symbol, Operands operands )
    {
        StringBuilder   bldr    = 
            new StringBuilder().append( operands.get( 0 ) )
                .append( ' ' ) .append( symbol ).append( " " )
                .append( operands.get( 1 ) ).append( " = " )
                .append( operands.getResult() );
        System.out.println( bldr );
    }

    /**
     * Print the result of a unary operation
     * for a given sequence of operands.
     * The caller passes a symbol
     * representing the operation,
     * such as "-" for negation.
     * 
     * @param symbol    the symbol representing the operation
     * @param operands  the given sequence of operands
     */
    private static void printUnaryResult( String symbol, Operands operands )
    {
        StringBuilder   bldr    = 
            new StringBuilder().append( symbol ).append("(")
                .append( operands.get( 0 ) ).append( ") = " )
                .append( operands.getResult() );
        System.out.println( bldr );
    }
}
