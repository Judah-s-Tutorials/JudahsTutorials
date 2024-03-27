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

public class PlotProcessDemo4
{
    private static final String endl    = System.lineSeparator();
    private final Equation  equation    = new Exp4jEquation();
    private final JTextArea textArea    = new JTextArea( 15, 20 );
    private final Plotter   cartPlane    = new Plotter();

    
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
    
    private void append( String text )
    {
        textArea.append( text );
        textArea.append( endl );
        int     len     = textArea.getDocument().getLength();
        textArea.setCaretPosition( len );
    }
    
    @SuppressWarnings("serial")
    private class Plotter extends CartesianPlane
    {
        private Supplier<Stream<PlotCommand>>   supplier;
        
        public Plotter()
        {
            NotificationManager.INSTANCE.addNotificationListener(
                CPConstants.REDRAW_NP,
                e -> redraw()
            );
        }
        
        @Override
        public void plotPoint( float xco, float yco )
        {
            Point2D point   = new Point2D.Float( xco, yco );
            append( point.toString() );
        }
        
        @Override
        public void 
        setStreamSupplier( Supplier<Stream<PlotCommand>> supplier )
        {
            this.supplier = supplier;
        }
        
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
