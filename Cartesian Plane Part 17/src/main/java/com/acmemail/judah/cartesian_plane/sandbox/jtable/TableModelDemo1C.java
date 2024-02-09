package com.acmemail.judah.cartesian_plane.sandbox.jtable;

import java.util.stream.IntStream;

import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

import com.acmemail.judah.cartesian_plane.sandbox.jtable.panels.State;

/**
 * Demonstrates to instantiate a JTable from a TableModel.
 * The TableModel itself is constructed using the 
 * DefaultTableModel(int numRows, int numHeaders) constructor.
 * The final table will have four columns.
 * Three columns (columns 1, 2, 3) of data are obtained 
 * from the us-state-all-data.csv data set.
 * Another column (column 0) containing 
 * dynamically generates identification numbers
 * is formulated.
 * The model's data array is then initialized
 * one cell at a time.
 * 
 * @author Jack Straub
 * 
 * @see TableModelDemo1
 */
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
