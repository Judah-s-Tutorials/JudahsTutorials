package com.acmemail.judah.cartesian_plane.sandbox.jtable;

import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import com.acmemail.judah.cartesian_plane.sandbox.jtable.panels.State;

public class TableModelDemo1A
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
        TableModel  model   = new DefaultTableModel( data, headers );
        SwingUtilities.invokeLater( () -> new TableModelDemo1( model ) );
    }
    
}
