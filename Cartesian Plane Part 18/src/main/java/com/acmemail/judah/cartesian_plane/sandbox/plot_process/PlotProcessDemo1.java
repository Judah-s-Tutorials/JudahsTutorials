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

import com.acmemail.judah.cartesian_plane.input.Equation;
import com.acmemail.judah.cartesian_plane.input.Exp4jEquation;

/**
 * Application to demonstrate how to set up and equation
 * and plot it using Exp4jEquation.yPlot().
 * 
 * @author Jack Straub
 * 
 * @see PlotProcessDemo2
 */
public class PlotProcessDemo1
{
    private static final String endl    = System.lineSeparator();
    private final Equation  equation    = new Exp4jEquation();
    private final JTextArea textArea    = new JTextArea( 15, 20 );

    
    /**
     * Application entry point.
     *
     * @param args command line arguments, not used
     *
    */
    public static void main(String[] args)
    {
        SwingUtilities.invokeLater( PlotProcessDemo1::new );
    }
    
    /**
     * Constructor.
     * Fully initializes the this object.
     */
    public PlotProcessDemo1()
    {
        equation.setYExpression( "x^2" );
        equation.setRangeStart( "-1" );
        equation.setRangeEnd( "1" );
        equation.setRangeStep( "1" );
        
        JFrame      frame       = new JFrame( "Plot Process Demo" );
        JScrollPane scrollPane  = new JScrollPane( textArea );
        textArea.setEditable( false );
        
        JButton     plot        = new JButton( "Plot" );
        JButton     exit        = new JButton( "Exit" );
        plot.addActionListener( this:: plotAction );
        exit.addActionListener( e -> System.exit( 1 ) );
        
        JPanel      buttonPanel = new JPanel();
        buttonPanel.add( plot );
        buttonPanel.add( exit );
        
        JPanel      contentPane = new JPanel( new BorderLayout() );
        contentPane.add( scrollPane, BorderLayout.CENTER );
        contentPane.add( buttonPanel, BorderLayout.SOUTH );
        printInitializationData();
        
        frame.setContentPane( contentPane );
        frame.setLocation( 100, 100 );
        frame.pack();
        frame.setVisible( true );
    }
    
    /**
     * Prints the data that Exp4jEquation.yPlot()
     * will use to generate a stream of points.
     */
    private void printInitializationData()
    {
        append( "Initialization data:" );
        append( "y=" + equation.getYExpression() );
        append( "From: " + equation.getRangeStartExpr() );
        append( "To: " + equation.getRangeEndExpr() );
        append( "Step: " + equation.getRangeStepExpr() );
    }
    
    /**
     * Activated when the Plot button is pushed.
     * Obtains and prints a stream of points
     * using Exp4jEquation.yPlot().
     *  
     * @param evt   object accompanying ActionEvent; not used
     */
    private void plotAction( ActionEvent evt )
    {
        Stream<Point2D> stream  = equation.yPlot();
        append( "*****BEGIN  PLOT *****" );
        stream.forEach( p -> append( p.toString() ) );
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
