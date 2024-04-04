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
import com.acmemail.judah.cartesian_plane.CartesianPlane;
import com.acmemail.judah.cartesian_plane.NotificationManager;
import com.acmemail.judah.cartesian_plane.PlotCommand;
import com.acmemail.judah.cartesian_plane.PlotPointCommand;
import com.acmemail.judah.cartesian_plane.input.Equation;
import com.acmemail.judah.cartesian_plane.input.Exp4jEquation;

/**
 * Builds on {@linkplain PlotProcessDemo3}
 * to fully develop a simulation
 * of the process that ends with a plot
 * produced by a CartesianPlane object.
 * It employs a nested class that extends CartesianPlane
 * and overrides the following methods:
 * <ul>
 * <li>
 *      setStreamSupplier( Supplier<Stream<PlotCommand>> supplier )
 *      This method is called when the GUI's Plot button is pushed
 *      passing a Supplier<Stream<PlotCommand>>.
 * </li>
 * <li>
 *      redraw()<br>
 *      This method is called
 *      when the Notification issues a REDRAW_NP notification.
 *      It uses the Supplier<Stream<PlotCommand>>
 *      provided via the setStreamSupplier method
 *      to obtain a Stream<PlotCommand>>.
 *      It traverses the stream of PlotCommands,
 *      finds every PlotCommand that is also
 *      a PlotPointCommand
 *      and invokes the PlotPointCommand's execute method,
 *      causing the CartesianPlanes plotPoint( float xco, float yco )
 *      method to be invoked.
 * </li>
 * <li>
 *      plotPoint( float xco, float yco )
 *      Prints the x- and y-coordinates provided
 *      by a PlotPoint command.
 * </li>
 * </ul>
 * When the Plot button is pushed a 
 * Supplier<Stream<PlotCommand>> object is generated
 * and registered with the CartesianPlane object
 * via its setStreamSupplier method,
 * then generates a REDRAW_NP notification.
 * 
 * @author Jack Straub
 * 
 * @see PlotProcessDemo3
 */
public class PlotProcessDemo4
{
    private static final String endl    = System.lineSeparator();
    private final Equation  equation    = new Exp4jEquation();
    private final JTextArea textArea    = new JTextArea( 15, 20 );
    private final Plotter   cartPlane   = new Plotter();

    
    /**
     * Application entry point.
     *
     * @param args command line arguments, not used
     *
    */
    public static void main(String[] args)
    {
        SwingUtilities.invokeLater( PlotProcessDemo4::new );
    }
    
    /**
     * Constructor.
     * Fully initializes the this object.
     */
    public PlotProcessDemo4()
    {
        equation.setYExpression( "y=x^2" );
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
        frame.setContentPane( contentPane );
        frame.setLocation( 100, 100 );
        frame.pack();
        frame.setVisible( true );
    }
    
    /**
     * Invoked when the GUI's plot button is pushed.
     * It obtains a Stream<Point2D>
     * from an Exp4jEquation object's yPlot() method.
     * The Stream<Point2D> is mapped to a Stream<PlotCommand>.
     * It uses the PlotCommand stream 
     * to create a Supplier<Stream<PlotCommand>> object
     * which it registers with the CartesianPlane.
     * It then generates a REDRAW_NP notification.
     *  
     * @param evt   object accompanying ActionEvent; not used
     */
    private void plotAction( ActionEvent evt )
    {
        Supplier<Stream<PlotCommand>>   streamSupplier  =
            () -> equation.yPlot()
            .map( p -> PlotPointCommand.of( p, cartPlane ) );
        cartPlane.setStreamSupplier( streamSupplier );
        
        append( "***** BEGIN PLOT *****" );
        NotificationManager.INSTANCE
            .propagateNotification( CPConstants.REDRAW_NP );
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
    
    /**
     * Nested class to simulate GUI interaction 
     * with a CaertesianPlane object.
     * 
     * @author Jack Straub
     */
    @SuppressWarnings("serial")
    private class Plotter extends CartesianPlane
    {
        /** Stream supplier for generating plots. */
        private Supplier<Stream<PlotCommand>>   supplier;
        
        /**
         * Constructor.
         * Registers a NotificationListener
         * with the NotificationManager.
         */
        public Plotter()
        {
            NotificationManager.INSTANCE.addNotificationListener(
                CPConstants.REDRAW_NP,
                e -> redraw()
            );
        }
        
        /**
         * Method invoked by a PlotPointCommand's
         * execute method.
         * 
         * @param xco   x-coordinated from PlotPointCommand
         * @param yco   y-coordinated from PlotPointCommand
         */
        @Override
        public void plotPoint( float xco, float yco )
        {
            Point2D point   = new Point2D.Float( xco, yco );
            append( point.toString() );
        }
        
        /**
         * Registers a Supplier<Stream<PlotCommand>>
         * for generating plots.
         * 
         * @param supplier Supplier to register
         */
        @Override
        public void 
        setStreamSupplier( Supplier<Stream<PlotCommand>> supplier )
        {
            this.supplier = supplier;
        }
        
        /**
         * Invoked when a REDRAW_NP notification is issued.
         * Traverses the Stream<PlotCommand> stream
         * obtained using the supplier instance method.
         * Calls the execute method 
         * for every PlotCommand object
         * that is also a PlotPointCommand object.
         */
        public void redraw()
        {
            Stream<PlotCommand> stream  = supplier.get();
            stream.filter( c -> (c instanceof PlotPointCommand) )
                .map( c -> (PlotPointCommand)c )
                .forEach( c -> c.execute() );
            append( "****** END PLOT ******" );
        }
    }
}
