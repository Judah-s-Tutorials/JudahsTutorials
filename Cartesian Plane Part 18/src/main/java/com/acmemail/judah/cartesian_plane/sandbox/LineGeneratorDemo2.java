package com.acmemail.judah.cartesian_plane.sandbox;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import com.acmemail.judah.cartesian_plane.LineGenerator;
import com.acmemail.judah.cartesian_plane.sandbox.utils.ActivityLog;

public class LineGeneratorDemo2
{
    private int     dim     = 601;
    private int     lpu     = 1;
    private int     gpu     = 601;
    private int     stroke  = 1;
    
    private final JFrame        frame       = 
        new JFrame( "LineGenerator Demo 2" );
    private final Canvas        canvas      = new Canvas();
    private final JTextField    dimText     = new JTextField( 6 );
    private final JTextField    lpuText     = new JTextField( 6 );
    private final JTextField    gpuText     = new JTextField( 6 );
    private final JTextField    strokeText  = new JTextField( 6 );
    private final ActivityLog   log         = new ActivityLog();

    
    /**
     * Application entry point.
     *
     * @param args command line arguments, not used
     *
    */
    public static void main(String[] args)
    {
        SwingUtilities.invokeLater( LineGeneratorDemo2::new );
    }
    
    private LineGeneratorDemo2()
    {
        frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        JPanel      contentPane = new JPanel( new BorderLayout() );
        
        canvas.setPreferredSize( new Dimension( dim, dim ) );        
        contentPane.add( canvas, BorderLayout.CENTER );
        contentPane.add( getControlPanel(), BorderLayout.SOUTH );
        frame.setContentPane( contentPane );
        frame.pack();
        frame.setVisible( true );
        
        int     logXco  = frame.getX() + frame.getWidth() + 10;
        int     logYco  = frame.getY();
        log.setLocation( logXco, logYco );
        log.setVisible( true );
    }
    
    private JPanel getButtonPanel()
    {
        JPanel  panel   = new JPanel();
        JButton revise  = new JButton( "Revise" );
        JButton exit    = new JButton( "Exit" );
        revise.addActionListener( this::revise );
        exit.addActionListener( e -> System.exit( 0 ) );
        panel.add( revise );
        panel.add( exit );
        return panel;
    }
    
    private JPanel getControlPanel()
    {
        JPanel      panel   = new JPanel();
        BoxLayout   layout  = new BoxLayout( panel, BoxLayout.Y_AXIS );
        panel.setLayout( layout );
        
        panel.add( getEditPanel() );
        panel.add( getButtonPanel() );
        return panel;
    }
    
    private JPanel getEditPanel()
    {
        JPanel      panel   = new JPanel();
        BoxLayout   layout  = new BoxLayout( panel, BoxLayout.X_AXIS );
        panel.setLayout( layout );
        panel.add( new JLabel( "Dim: " ) );
        panel.add( dimText );
        panel.add( new JLabel( "GPU: " ) );
        panel.add( gpuText );
        panel.add( new JLabel( "LPU: " ) );
        panel.add( lpuText );
        panel.add( new JLabel( "Stroke: " ) );
        panel.add( strokeText );
        
        dimText.setText( "" + dim );
        gpuText.setText( "" + gpu );
        lpuText.setText( "" + lpu );
        strokeText.setText( "" + stroke );
        
        return panel;
    }
    
    private int getTextValue( JTextField textField )
    {
        int     value   = -1;
        try
        {
            value = Integer.parseInt( textField.getText() );
            return value;
        }
        catch ( NumberFormatException exc )
        {
            // ignore
        }
        return value;
    }
    
    private void revise( ActionEvent evt )
    {
        frame.setVisible( false );
        dim = getTextValue( dimText );
        gpu = getTextValue( gpuText );
        lpu = getTextValue( lpuText );
        canvas.setPreferredSize( new Dimension( dim, dim ) );
        frame.pack();
        frame.setVisible( true );
        analyzeBitmap();
    }
    
    private void analyzeBitmap()
    {
        BufferedImage   bitmap      = 
            new BufferedImage( dim, dim, BufferedImage.TYPE_INT_RGB );
        Graphics        graphics    = bitmap.getGraphics();
        canvas.paintComponent( graphics );
        logHorizontalLines( bitmap );
        logVerticalLines( bitmap );
    }
    
    private void logHorizontalLines( BufferedImage bitmap )
    {
        int     midPoint        = dim / 2 + 1;
        int     sampleOffset    = gpu / lpu / 2;  
        int     yco             = midPoint - sampleOffset;

        int     beginLine       = -1;
        int     endLine         = -1;
        log.append( ">>> Horizontal <<<" );
        for ( int xco = 0 ; xco < dim ; ++xco )
        {
            if ( (bitmap.getRGB( xco, yco ) & 0xFFFFFF) == 0 )
            {
                if ( beginLine == -1 )
                    beginLine = xco;
                endLine = xco;
            }
            else
            {
                if ( beginLine > -1 )
                {
                    String  str = "x=" + beginLine;
                    if ( endLine != beginLine )
                        str += "-" + endLine;
                    log.append( str );
                    beginLine = -1;
                    endLine = -1;
                }
            }
        }
        log.append( ">>>>>> <<<<<<" );
    }
    
    private void logVerticalLines( BufferedImage bitmap )
    {
        int     midPoint        = dim / 2 + 1;
        int     sampleOffset    = gpu / lpu / 2;  
        int     xco             = midPoint - sampleOffset;

        int     beginLine       = -1;
        int     endLine         = -1;
        log.append( ">>> Vertical <<<" );
        for ( int yco = 0 ; yco < dim ; ++yco )
        {
            if ( (bitmap.getRGB( xco, yco ) & 0xFFFFFF) == 0 )
            {
                if ( beginLine == -1 )
                    beginLine = yco;
                endLine = yco;
            }
            else
            {
                if ( beginLine > -1 )
                {
                    String  str = "y=" + beginLine;
                    if ( endLine != beginLine )
                        str += "-" + endLine;
                    log.append( str );
                    beginLine = -1;
                    endLine = -1;
                }
            }
        }
        log.append( ">>>>>> <<<<<<" );
        log.append( ">>>>>> <<<<<<" );
    }
    
    private class Canvas extends JPanel
    {
        private static final long serialVersionUID = 1L;

        public void paintComponent( Graphics graphics )
        {
            Graphics2D  gtx     = (Graphics2D)graphics.create();
            Rectangle2D rect    = getBounds();
            gtx.setColor( Color.WHITE );
            gtx.fillRect( 0, 0, getWidth(), getHeight() );
            
            gtx.setColor( Color.BLACK );
            int             lineWidth   = getTextValue( strokeText );
            gtx.setStroke( new BasicStroke( lineWidth ) );
            LineGenerator   lineGen     = 
                new LineGenerator( rect, gpu, lpu );
            lineGen.forEach( gtx::draw );
            gtx.dispose();
        }
    }
}
