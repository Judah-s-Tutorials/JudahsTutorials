package com.acmemail.judah.cartesian_plane.sandbox.plot_process;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.geom.Point2D;
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

/**
 * This application builds on 
 * {@linkplain PlotProcessDemo1}
 * to better simulate how plotting occurs 
 * with a CartesianPlane object.
 * Pushing the GUI's Plot button will generate a plot stream,
 * but the plot won't appear in the GUI
 * until a REDRAW_NP notification 
 * is issued by the NotificationManager;
 * pushing the GUI's Redraw button
 * will cause the notification be issued
 * and the plot stream to be traversed and printed.
 * <p>
 * Note that if you immediately push the redraw button
 * a second time
 * an error will occur,
 * because the same stream can't be traversed more than once.
 * Before pushing the Redraw button a second time
 * you first have to push the Plot button a second time
 * to generate a new plot stream.
 * 
 * @author Jack Straub
 * 
 * @see PlotProcessDemo1
 * @see PlotProcessDemo3
 */
public class PlotProcessDemo2
{
    private static final String endl    = System.lineSeparator();
    private final Equation  equation    = new Exp4jEquation();
    private final JTextArea textArea    = new JTextArea( 15, 20 );
    private Stream<Point2D> stream      = null;
    
    /**
     * Application entry point.
     *
     * @param args command line arguments, not used
     *
    */
    public static void main(String[] args)
    {
        SwingUtilities.invokeLater( PlotProcessDemo2::new );
    }
    
    /**
     * Constructor.
     * Fully initializes the this object.
     */
    public PlotProcessDemo2()
    {
        equation.setYExpression( "x^2" );
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
    
    /**
     * Invoked when the GUI's plot button is pushed.
     * Generates a plot stream
     * by calling Exp4jEquation.yPlot().
     *  
     * @param evt   object accompanying ActionEvent; not used
     */
    private void plotAction( ActionEvent evt )
    {
        stream  = equation.yPlot();
    }
    
    /**
     * Invoked when the GUI's redraw button is pushed.
     * Generates a REDRAW_NP notification
     * via the NotificationManager.
     *  
     * @param evt   object accompanying ActionEvent; not used
     */
    private void redrawAction( ActionEvent evt )
    {
        append( "***** BEGIN PLOT *****" );
        NotificationManager.INSTANCE
            .propagateNotification( CPConstants.REDRAW_NP );
    }
    
    /**
     * Processes a REDRAW_NP notification
     * issued by the NotificationManager.
     * Traverses the stream produced by Exp4jEquation.yPlot()
     * and prints the results.
     *  
     * @param evt   object accompanying ActionEvent; not used
     * 
     * @see #plotAction(ActionEvent)
     */
    private void redraw( NotificationEvent evt )
    {
        try
        {
            if ( stream != null )
                stream.forEach( p -> append( p.toString() ) );
        }
        catch ( IllegalStateException exc )
        {
            String  msg = "Error: a stream can only be traversed once.";
            append( msg );
        }
        append( "****** END PLOT ******" );
    }
    
    /**
     * Appends a line to the end of the GUI's text area.
     * 
     * @param text  the text of the line to append
     */
    private void append( String text )
    {
        textArea.append( text );
        textArea.append( endl );
        int     len     = textArea.getDocument().getLength();
        textArea.setCaretPosition( len );
    }
}
