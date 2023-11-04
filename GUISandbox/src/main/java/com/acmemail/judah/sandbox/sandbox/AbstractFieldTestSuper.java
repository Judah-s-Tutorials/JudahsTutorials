package com.acmemail.judah.sandbox.sandbox;

public abstract class AbstractFieldTestSuper
{
    private final double field1;
    private final int    field2;
    private final String field3;
    
    public AbstractFieldTestSuper( double f1, int f2, String f3 )
    {
        field1 = f1;
        field2 = f2;
        field3 = f3;
    }
    
    @Override
    public String toString()
    {
        StringBuilder   bldr    = new StringBuilder();
        bldr.append( "Field 1 = " ).append( field1 ).append( " " )
            .append( "Field 2 = " ).append( field2 ).append( " " )
            .append( "Field 3 = " ).append( field3 );
        return bldr.toString();
    }
}
