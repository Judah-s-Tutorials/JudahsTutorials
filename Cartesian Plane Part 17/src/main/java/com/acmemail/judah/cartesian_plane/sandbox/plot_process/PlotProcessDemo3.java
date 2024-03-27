package com.acmemail.judah.cartesian_plane.sandbox.plot_process;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.geom.Point2D;
import java.util.function.Supplier;
import java.util.stream.Stream;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

import com.acmemail.judah.cartesian_plane.CPConstants;
import com.acmemail.judah.cartesian_plane.NotificationEvent;
import com.acmemail.judah.cartesian_plane.NotificationManager;
import com.acmemail.judah.cartesian_plane.input.Equation;
import com.acmemail.judah.cartesian_plane.input.Exp4jEquation;

public class PlotProcessDemo3
{
    private static final String endl    = System.lineSeparator();
    private final Equation  equation    = new Exp4jEquation();
    private final JTextArea textArea    = new JTextArea( 15, 20 );
    private Supplier<Stream<Point2D>> streamSupplier      = null;
    
    /**
     * Application entry point.
     *
     * @param args command line arguments, not used
     *
    */
    public static void main(String[] args)
    {
        SwingUtilities.invokeLater( PlotProcessDemo3::new );
    }
    
    public PlotProcessDemo3()
    {
        equation.setYExpression( "y=x^2" );
        equation.setRangeStart( "-1" );
        equation.setRangeEnd( "1" );
        equation.setRangeStep( "1" );
        
        JFrame      frame       = new JFrame( "Plot Process Demo" );
        JScrollPane scrollPane  = new JScrollPane( textArea );
        textArea.setEditable( false );
        
        JButton     plot        = new JButton( "Plot" );
        JButton     redraw      = new JButton( "Redraw" );
        JButton     exit        = new JButton( "Exit" );
        plot.addActionListener( this::plotAction );
        redraw.addActionListener( this::redrawAction );
        exit.addActionListener( e -> System.exit( 1 ) );
        
        JPanel      buttonPanel = new JPanel();
        buttonPanel.add( plot );
        buttonPanel.add( redraw );
        buttonPanel.add( exit );
        
        JPanel      contentPane = new JPanel( new BorderLayout() );
        contentPane.add( scrollPane, BorderLayout.CENTER );
        contentPane.add( buttonPanel, BorderLayout.SOUTH );
        frame.setContentPane( contentPane );
        frame.setLocation( 100, 100 );
        frame.pack();
        frame.setVisible( true );

        NotificationManager.INSTANCE.addNotificationListener(
            CPConstants.REDRAW_NP,
            this::redraw
        );
    }
    
    private void plotAction( ActionEvent evt )
    {
        streamSupplier = () -> equation.yPlot();
    }
    
    private void redraw( NotificationEvent evt )
    {
        if ( streamSupplier != null )
        {
            Stream<Point2D> stream  = streamSupplier.get();
            stream.forEach( p -> append( p.toString() ) );
        }
        append( "****** END PLOT ******" );
    }
    
    private void redrawAction( ActionEvent evt )
    {
        append( "***** BEGIN PLOT *****" );
        NotificationManager.INSTANCE
            .propagateNotification( CPConstants.REDRAW_NP );
    }
    
    private void append( String text )
    {
        textArea.append( text );
        textArea.append( endl );
        int     len     = textArea.getDocument().getLength();
        textArea.setCaretPosition( len );
    }
}
