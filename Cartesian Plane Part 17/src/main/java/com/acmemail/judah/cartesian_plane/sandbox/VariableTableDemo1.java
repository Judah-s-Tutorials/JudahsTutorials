package com.acmemail.judah.cartesian_plane.sandbox;

import javax.swing.SwingUtilities;

public class VariableTableDemo1
{
    private static final String inFile  = 
        "data/VariableTableData1.csv";
    
    /**
     * Application entry point.
     *
     * @param args command line arguments, not used
     *
    */
    public static void main(String[] args)
    {
        SwingUtilities.invokeLater( () -> openAndDump() );
    }
    
    private static void openAndDump()
    {
        VariableTableA   table   = new VariableTableA( inFile );
        System.out.println( "VariableTableDemo1:" );
        System.out.println( table );
    }
}
