package sandbox;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class MouseEventDemo
{
    private static final String             timeFormat      = 
        "HH:mm:ss.SSS";
    private static final DateTimeFormatter  timeFormatter   =
        DateTimeFormatter.ofPattern( timeFormat );

    public static void main(String[] args)
    {
        SwingUtilities.invokeLater( () -> build() );
    }
    
    private static void build()
    {
        JFrame      frame       = new JFrame( "Mouse Event Demo" );
        frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        JPanel      pane        = new JPanel( new BorderLayout() );
        Canvas      canvas      = new Canvas();
        pane.add( canvas, BorderLayout.CENTER );
        
        frame.setContentPane( pane );
        frame.pack();
        frame.setLocation( 600, 100 );
        frame.setVisible( true );
    }

    @SuppressWarnings("serial")
    private static class Canvas extends JPanel
    {
        public Canvas()
        {
            Dimension   dim         = new Dimension( 500, 500 );
            MListener   listener    = new MListener();
            setPreferredSize( dim );
            addMouseListener( listener );
            addMouseMotionListener( listener );
        }
    }
    
    private static class MListener extends MouseAdapter
    {
        @Override
        public void mousePressed( MouseEvent evt )
        {
            log( "pressed", evt );
        }
        
        @Override
        public void mouseReleased( MouseEvent evt )
        {
            log( "released", evt );
        }
        
        @Override
        public void mouseClicked( MouseEvent evt )
        {
            log( "clicked", evt );
        }
        
        @Override
        public void mouseDragged( MouseEvent evt )
        {
            log( "dragged", evt );
        }
        
        private void log( String str, MouseEvent evt )
        {
            int     button  = evt.getButton();
            int     xco     = evt.getX();
            int     yco     = evt.getY();
            String  fmt     = "B%d %s (%d,%d)";
            String  output  = String.format( fmt, button, str, xco, yco );
            log( output );
        }
        
        private void log( String str )
        {
            String  time    = timeFormatter.format( LocalTime.now() );
            System.out.println( time + " " + str );
        }
    }
}
