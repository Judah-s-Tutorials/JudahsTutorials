package com.acmemail.judah.cartesian_plane.sandbox;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.util.stream.Stream;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import com.acmemail.judah.cartesian_plane.components.GraphManager;
import com.acmemail.judah.cartesian_plane.components.LinePropertySet;
import com.acmemail.judah.cartesian_plane.components.PButtonGroup;
import com.acmemail.judah.cartesian_plane.components.PRadioButton;
import com.acmemail.judah.cartesian_plane.components.Profile;
import com.acmemail.judah.cartesian_plane.graphics_utils.ComponentException;
import com.acmemail.judah.cartesian_plane.sandbox.utils.ActivityLog;
import com.acmemail.judah.cartesian_plane.sandbox.utils.LineSegment;

public class GraphManagerDemo1
{
    private static final String axes        = "LinePropertySetAxes";
    private static final String gridLines   = "LinePropertySetGridLines";
    private static final String ticMajor    = "LinePropertySetTicMajor";
    private static final String ticMinor    = "LinePropertySetTicMinor";

    private int     dim     = 601;
    private int     lpu     = 1;
    private int     gpu     = 601;
    private int     len     = 20;
    private int     stroke  = 1;
    
    private final JFrame        frame       = 
        new JFrame( "LineGenerator Demo 2" );
    private final Profile       profile     = new Profile();
    private final Canvas        canvas      = new Canvas( profile );
    private final JTextField    dimText     = new JTextField( 6 );
    private final JTextField    lpuText     = new JTextField( 6 );
    private final JTextField    gpuText     = new JTextField( 6 );
    private final JTextField    lenText     = new JTextField( 6 );
    private final JTextField    strokeText  = new JTextField( 6 );
    private final ActivityLog   log         = new ActivityLog();

    private final PButtonGroup<LinePropertySet> buttonGroup =
        new PButtonGroup<>();
    
    /**
     * Application entry point.
     *
     * @param args command line arguments, not used
     *
    */
    public static void main(String[] args)
    {
        SwingUtilities.invokeLater( GraphManagerDemo1::new );
    }
    
    private GraphManagerDemo1()
    {
        profile.getMainWindow().setBGColor( Color.WHITE );
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
        revise( null );
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
        panel.add( getSelectionPanel() );
        panel.add( getButtonPanel() );
        return panel;
    }
    
    private JPanel getSelectionPanel()
    {
        JPanel      panel       = new JPanel();
        BoxLayout   layout      = new BoxLayout( panel, BoxLayout.X_AXIS );
        panel.setLayout( layout );
        
        int         prefixLen   = 
            LinePropertySet.class.getSimpleName().length(); 
        String[]    allLineSets =
        { axes, gridLines, ticMinor, ticMajor };
        
        Stream.of( allLineSets )
            .map( s -> 
                new PRadioButton<>( 
                    profile.getLinePropertySet( s ),
                    s.substring( prefixLen )
                )
            )
            .peek( buttonGroup::add )
            .forEach( panel::add );
        buttonGroup.selectIndex( 0 );
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
        panel.add( new JLabel( "Length: " ) );
        panel.add( lenText );
        panel.add( new JLabel( "Stroke: " ) );
        panel.add( strokeText );
        
        dimText.setText( "" + dim );
        gpuText.setText( "" + gpu );
        lpuText.setText( "" + lpu );
        strokeText.setText( "" + stroke );
        lenText.setText( "" + len );
        
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
        dim = getTextValue( dimText );
        gpu = getTextValue( gpuText );
        lpu = getTextValue( lpuText );
        len = getTextValue( lenText );
        stroke = getTextValue( strokeText );
        profile.setGridUnit( gpu );
        LinePropertySet selectedSet = buttonGroup.getSelectedProperty();
        selectedSet.setStroke( stroke );
        selectedSet.setLength( len );
        selectedSet.setSpacing( lpu );
        canvas.setPreferredSize( new Dimension( dim, dim ) );
        frame.pack();
        canvas.repaint();
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
    
    private void logVerticalLines( BufferedImage bitmap )
    {
        LinePropertySet set     = buttonGroup.getSelectedProperty();
        int             rgb     = set.getColor().getRGB() & 0xffffff;
        int             mid     = dim / 2 + 1;
        log.append( ">>> Vertical <<<" );
        for ( int xco = 0 ; xco < dim ; ++xco )
        {
            if ( (bitmap.getRGB( xco, mid ) & 0xFFFFFF) == rgb )
            {
                LineSegment lineSeg = 
                    new LineSegment( new Point( xco, mid ), bitmap );
                log.append( lineSeg.toString() );
                xco += lineSeg.getBounds().width;
            }
        }
        log.append( ">>>>>> <<<<<<" );
    }
    
    private void logHorizontalLines( BufferedImage bitmap )
    {
        LinePropertySet set     = buttonGroup.getSelectedProperty();
        int             rgb     = set.getColor().getRGB() & 0xffffff;
        int             mid     = dim / 2 - 10;
        log.append( ">>> Horizontal <<<" );
        for ( int yco = 0 ; yco < dim ; ++yco )
        {
            if ( (bitmap.getRGB( mid, yco ) & 0xFFFFFF) == rgb )
            {
                LineSegment lineSeg = 
                    new LineSegment( new Point( mid, yco ), bitmap );
                log.append( lineSeg.toString() );
                yco += lineSeg.getBounds().height;
            }
        }
        log.append( ">>>>>> <<<<<<" );
    }
    
    private class Canvas extends JPanel
    {
        private static final long serialVersionUID = 1L;
        
        /** 
         * Encapsulates to various drawing operations.
         * It is initialized in the constructor,
         * and utilized in the {@link #paintComponent(Graphics)} method.
         */
        private final GraphManager  drawManager;
        
        public Canvas( Profile profile )
        {
            drawManager = new GraphManager( this, profile );
        }


        public void paintComponent( Graphics graphics )
        {
            drawManager.refresh( (Graphics2D)graphics );
            LinePropertySet currSet = buttonGroup.getSelectedProperty();
            String          setName = currSet.getClass().getSimpleName();
            switch ( setName )
            {
            case axes:
                drawManager.drawAxes();
                break;
            case gridLines:
                drawManager.drawGridLines();
                break;
            case ticMinor:
                drawManager.drawMinorTics();
                break;
            case ticMajor:
                drawManager.drawMajorTics();
                break;
            default:
                throw new ComponentException( "Invalid class name" );
            }
        }
    }
}
