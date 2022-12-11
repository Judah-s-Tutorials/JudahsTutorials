package com.acmemail.judah.sandbox;

public class InnerClassDemo
{
    private double      datum;
    private DatumRoot   fourthRoot;
    
    public static void main( String[] args )
    {
        InnerClassDemo  demo    = new InnerClassDemo( 16 );
        System.out.println( demo.fourthRoot.getRoot() );
    }
    
    public InnerClassDemo( double datum )
    {
        this.datum = datum;
        fourthRoot = new DatumRoot( 4 );
    }
    
    private void printMessage( String msg )
    {
        System.out.println( "Message: " + msg );
    }
    
    private class DatumRoot
    {
        private double  radicand;
        
        private DatumRoot( double radicand )
        {
            this.radicand = radicand;
        }
        
        private double getRoot()
        {
            printMessage( "calculating root" );
            double  root    = Math.pow( datum, 1 / radicand );
            return root;
        }
    }
}
