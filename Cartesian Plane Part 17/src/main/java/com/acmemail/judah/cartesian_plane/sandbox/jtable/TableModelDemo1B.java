package com.acmemail.judah.cartesian_plane.sandbox.jtable;

import java.util.stream.Stream;

import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

import com.acmemail.judah.cartesian_plane.sandbox.jtable.panels.State;

/**
 * Demonstrates to instantiate a JTable from a TableModel.
 * The TableModel itself is constructed using the 
 * DefaultTableModel(Object[] headers, int numRows) constructor,
 * after which the data rows are added one at a time
 * using the addRow(Object[] data) method.
 * 
 * @author Jack Straub
 * 
 * @see TableModelDemo1
 */
public class TableModelDemo1B
{
    /**
     * Application entry point.
     *
     * @param args command line arguments, not used
     *
    */
    public static void main(String[] args)
    {
        String[]    headers = { "State", "Capital", "Population" };
        Object[][]  data    = 
            State.getDataSet( "state", "capital", "population" );
        DefaultTableModel  model   = new DefaultTableModel( headers, 0 );
        Stream.of( data ).forEach( r -> model.addRow( r ) );
        SwingUtilities.invokeLater( () -> new TableModelDemo1( model ) );
    }
    
}
