package com.acmemail.judah.cartesian_plane.sandbox.jtable;

import java.util.stream.IntStream;

import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

import com.acmemail.judah.cartesian_plane.sandbox.jtable.panels.State;

public class TableModelDemo1C
{
    /**
     * Application entry point.
     *
     * @param args command line arguments, not used
     *
    */
    public static void main(String[] args)
    {
        String[]            headers = 
        { "Seq No.", "State", "Capital", "Population" };
        Object[][]          data    = 
            State.getDataSet( "state", "capital", "population" );
        int                 numCols = headers.length;
        int                 numRows = data.length;
        DefaultTableModel   model   = 
            new DefaultTableModel( numRows, numCols );
        
        model.setColumnIdentifiers( headers );
        IntStream.range( 0, numRows )
            .forEach( inx -> {
                model.setValueAt( inx + 1000, inx, 0 );
                model.setValueAt( data[inx][0], inx, 1 );
                model.setValueAt( data[inx][1], inx, 2 );
                model.setValueAt( data[inx][2], inx, 3 );
            });
        SwingUtilities.invokeLater( () -> new TableModelDemo1( model ) );
    }
}
