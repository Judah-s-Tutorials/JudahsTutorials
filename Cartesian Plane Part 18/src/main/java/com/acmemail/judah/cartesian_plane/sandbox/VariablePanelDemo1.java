package com.acmemail.judah.cartesian_plane.sandbox;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

import com.acmemail.judah.cartesian_plane.components.VariablePanel;
import com.acmemail.judah.cartesian_plane.graphics_utils.ComponentException;
import com.acmemail.judah.cartesian_plane.graphics_utils.ComponentFinder;
import com.acmemail.judah.cartesian_plane.input.Equation;
import com.acmemail.judah.cartesian_plane.input.Exp4jEquation;
import com.acmemail.judah.cartesian_plane.sandbox.utils.ActivityLog;

public class VariablePanelDemo1
{
    private static final String lineBreak   = "<br>";
    private static final String spaces      = 
        "<span style='color:black;'> --- </span>";
    
    /** Activity dialog for displaying feedback. */
    private ActivityLog         log;
    
    private DefaultTableModel   model;
    private JTable              jTable;
    private Equation            equation;
    
    /**
     * Application entry point.
     *
     * @param args command line arguments, not used
     *
    */
    public static void main(String[] args)
    {
        VariablePanelDemo1  demo    = new VariablePanelDemo1();
        SwingUtilities.invokeLater( demo::buildGUI );
    }
    
    private void buildGUI()
    {
        VariablePanel   table   = null;
        equation = new Exp4jEquation();
        IntStream.range( 0, 15 ).forEach( i -> 
            equation.setVar("" + (char)('a' + i), i * 1.1 ) );
        table = new VariablePanel();
        table.load( equation );

        JPanel          cPane   = new JPanel( new BorderLayout() );
        cPane.add( table, BorderLayout.CENTER );
        
        JButton         print   = new JButton( "Print" );
        JButton         exit    = new JButton( "Exit" );
        JPanel          buttons = new JPanel();
        print.addActionListener( this::printAction );
        exit.addActionListener( e -> System.exit( 0 ) );
        buttons.add( print );
        buttons.add( exit );
        cPane.add( buttons, BorderLayout.SOUTH );

        String          title   = "Variable Table Demo 2";
        JFrame          frame   = new JFrame( title );
        frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        frame.setContentPane( cPane );
        frame.pack();
        frame.setLocation( 100, 100 );
        frame.setVisible( true );
        
        log = new ActivityLog();
        Dimension       frameDim    = frame.getPreferredSize();
        int             logXco      = frameDim.width + 105;
        log.setLocation( logXco, 100 );
        log.setVisible( true );
        
        jTable = getTable( frame );
        model = (DefaultTableModel)jTable.getModel();
    }
    
    private void printAction( ActionEvent evt )
    {
        List<String>    tableNames  = getTableNames();
        List<String>    equNames    = getEquationNames();
        int             tableCount  = tableNames.size();
        int             equCount    = equNames.size();
        int             startCount  = Math.min( tableCount, equCount );
        String          tableFmt    = 
            "<span style='color: red;'>%5s</span>";
        String          equFmt      = 
            "<span style='color: green;'>%5s</span>";
        StringBuilder   bldr        = new StringBuilder();

        for ( int inx = 0 ; inx < startCount ; ++inx )
        {
            String  tableName   =
                String.format( tableFmt, tableNames.get( inx ) );
            String  equName     =
                String.format( equFmt, equNames.get( inx ) );
            bldr.append( tableName )
                .append( spaces ).append( equName ).append( "<br>" );
        }
        
        if ( tableCount > startCount )
        {
            String  equName = 
                String.format( equFmt, "-----" );
            for ( int inx = startCount ; inx < tableCount ; ++inx )
            {
                String  tableName   =
                    String.format( tableFmt, tableNames.get( inx ) );
                bldr.append( tableName )
                    .append( spaces ).append( equName ).append( lineBreak );
            }
        }
        
        if ( equCount > startCount )
        {
            String  tableName   = 
                String.format( tableFmt, "-----" );
            for ( int inx = startCount ; inx < tableCount ; ++inx )
            {
                String  equName     =
                    String.format( equFmt, equNames.get( inx ) );
                bldr.append( tableName )
                    .append( spaces ).append( equName ).append( lineBreak );
            }
        }
        bldr.append( "=============" );
        
        log.append( bldr.toString() );
    }
    
    private List<String> getTableNames()
    {
        StringBuilder   bldr    = new StringBuilder();
        List<String>    list     = new ArrayList<String>(); 
        model.getDataVector().stream()
            .peek( v -> bldr.setLength( 0 ) )
            .peek( v -> bldr.append( v.get( 0 ) ) )
            .peek( v -> bldr.append( " = " ) )
            .map( v -> String.format( "%.3f", v.get( 1 ) ) ) 
            .map( bldr::append )
            .map( b -> b.toString() )
            .forEach( list::add );
        list.sort( String::compareTo );
        return list;
    }
    
    private List<String> getEquationNames()
    {
        StringBuilder   bldr    = new StringBuilder();
        List<String>    list    = new ArrayList<>();
        equation.getVars().keySet().stream()
            .peek( s -> bldr.setLength( 0 ) )
            .peek( bldr::append )
            .peek( s -> bldr.append( " = " ) )
            .mapToDouble( s -> equation.getVar( s ).orElse( -999. ) )
            .mapToObj( d -> String.format( "%.3f", d ) )
            .map( bldr::append )
            .map( b -> b.toString() )
            .forEach( list::add );
        list.sort( String::compareTo );
        return list;
    }
    
    private JTable getTable( JFrame frame )
    {
        JComponent  comp    = 
            ComponentFinder.find( frame, c -> (c instanceof JTable) );
        if ( comp == null )
            throw new ComponentException( "JTable not found" );
        if ( !(comp instanceof JTable) )
            throw new ComponentException( "Finder malfunction" );
        return (JTable)comp;
    }
}
