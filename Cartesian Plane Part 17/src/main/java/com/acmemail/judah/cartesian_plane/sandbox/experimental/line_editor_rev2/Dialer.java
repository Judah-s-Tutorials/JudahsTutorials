package com.acmemail.judah.cartesian_plane.sandbox.experimental.line_editor_rev2;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.util.HashMap;
import java.util.Map;
import java.util.function.DoubleConsumer;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;

import com.acmemail.judah.cartesian_plane.CPConstants;
import com.acmemail.judah.cartesian_plane.PropertyManager;

public class Dialer
{
    private static final String gridUnit        = "Grid Unit";
    private static final String axesWeight      = "Axes Weight";
    
    private static final String majorTicsPU     = "Major Tics/Unit";
    private static final String majorTicsWeight = "Major Tics Weight";
    private static final String majorTicsLen    = "Major Tics Length";
    
    private static final String minorTicsPU     = "Minor Tics/Unit";
    private static final String minorTicsWeight = "Minor Tics Weight";
    private static final String minorTicsLen    = "Minor Tics Length";
    
    private static final String gridLinesPU     = "Grid Lines/Unit";
    private static final String gridLinesWeight = "GridLinesWeight";
    
    private final PropertyManager   pMgr    = PropertyManager.INSTANCE;

    private final JFrame        frame       = new JFrame( "Dialer" );
    private final Canvas        canvas      = new Canvas();
    private final DrawManager   drawManager = canvas.getDrawManager();
    
    private final Map<String,SpinnerDesc>   descMap = getDescMap();
    
    /**
     * Application entry point.
     *
     * @param args command line arguments, not used
     *
    */
    public static void main(String[] args)
    {
        SwingUtilities.invokeLater( () -> new Dialer() );
    }
    
    public Dialer()
    {
        frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        JPanel  contentPane     = new JPanel( new BorderLayout() );
        contentPane.add( BorderLayout.CENTER, canvas );
        contentPane.add( BorderLayout.SOUTH, getControlPanel() );
        
        frame.setContentPane( contentPane );
        contentPane.setFocusable( true );

        Dimension   screen  = 
            Toolkit.getDefaultToolkit().getScreenSize();
        Dimension   size    = frame.getPreferredSize();
        int         xco     = screen.width / 2 - size.width / 2;
        int         yco     = screen.height / 2 - size.height / 2 - 100;
        frame.setLocation( xco, yco );
        frame.pack();
        frame.setVisible( true );
    }
    
    private Map<String,SpinnerDesc> getDescMap()
    {
        Map<String,SpinnerDesc> map = new HashMap<>();
        SpinnerDesc desc    = null;
        
        desc = new SpinnerDesc(
            d -> drawManager.setGridUnit( (float)d ), 
            gridUnit, 
            CPConstants.GRID_UNIT_PN, 
            1
        );
        map.put( gridUnit, desc );
        
        desc = new SpinnerDesc(
            d -> drawManager.setGridLineLPU( (float)d ), 
            gridLinesPU, 
            CPConstants.GRID_LINE_LPU_PN, 
            1
        );
        map.put( gridLinesPU, desc );
        
        desc = new SpinnerDesc(
            d -> drawManager.setGridLineWeight( (float)d ), 
            gridLinesWeight, 
            CPConstants.GRID_LINE_WEIGHT_PN, 
            1
        );
        map.put( gridLinesWeight, desc );
        
        desc = new SpinnerDesc(
            d -> drawManager.setTicMajorMPU( (float)d ), 
            majorTicsPU, 
            CPConstants.TIC_MAJOR_MPU_PN, 
            .25
        );
        map.put( majorTicsPU, desc );
        
        desc = new SpinnerDesc(
            d -> drawManager.setTicMajorLength( (float)d ), 
            majorTicsLen, 
            CPConstants.TIC_MAJOR_LEN_PN, 
            1
        );
        map.put( majorTicsLen, desc );
        
        desc = new SpinnerDesc(
            d -> drawManager.setTicMajorWeight( (float)d ), 
            majorTicsWeight, 
            CPConstants.TIC_MAJOR_WEIGHT_PN, 
            1
        );
        map.put( majorTicsWeight, desc );
        
        desc = new SpinnerDesc(
            d -> drawManager.setTicMinorMPU( (float)d ), 
            minorTicsPU, 
            CPConstants.TIC_MINOR_MPU_PN, 
            .25
        );
        map.put( minorTicsPU, desc );
        
        desc = new SpinnerDesc(
            d -> drawManager.setTicMinorLength( (float)d ), 
            minorTicsLen, 
            CPConstants.TIC_MINOR_LEN_PN, 
            1
        );
        map.put( minorTicsLen, desc );
        
        desc = new SpinnerDesc(
            d -> drawManager.setTicMinorWeight( (float)d ), 
            minorTicsWeight, 
            CPConstants.TIC_MINOR_WEIGHT_PN, 
            1
        );
        map.put( minorTicsWeight, desc );
        
        return map;
    }
    
    private JPanel getControlPanel()
    {
        JPanel      panel   = new JPanel();
        Border      inner   =
            BorderFactory.createLineBorder( Color.BLACK );
        Border      outer   =
            BorderFactory.createEmptyBorder( 3, 3, 3, 3 );
        Border      border  =
            BorderFactory.createCompoundBorder( outer, inner );
        panel.setBorder( border );
        
        JButton     exit    = new JButton( "Exit" );
        exit.addActionListener( e -> System.exit( 0 ) );
        panel.add( getSpinnerPanel() );
        panel.add( exit );
                
        return panel;
    }
    
    private JPanel getSpinnerPanel()
    {
        JPanel      panel   = new JPanel();
        BoxLayout   layout  = new BoxLayout( panel, BoxLayout.X_AXIS );
        Border      inner   =
            BorderFactory.createBevelBorder( BevelBorder.RAISED );
        Border      outer   =
            BorderFactory.createEmptyBorder( 3, 3, 3, 3 );
        Border      border  =
            BorderFactory.createCompoundBorder( outer, inner );
        panel.setBorder( border );
        panel.setLayout( layout );
        
        panel.add( 
            getSpinnerPanel( gridUnit, gridLinesPU, gridLinesWeight )
        );
        panel.add( 
            getSpinnerPanel( majorTicsPU, majorTicsLen, majorTicsWeight )
        );
        panel.add( 
            getSpinnerPanel( minorTicsPU, minorTicsLen, minorTicsWeight )
        );
        
        return panel;
    }
    
    private JPanel 
    getSpinnerPanel( String... names )
    {
        JPanel  panel   = new JPanel( new GridLayout( 3, 2 ) );
        long    count   = Stream.of( names )
            .map( descMap::get )
            .filter( d -> d != null )
            .peek( d -> panel.add( d.label ) )
            .peek( d -> panel.add( d.spinner ) )
            .count();
        
        IntStream.range( (int)count, 3 )
            .peek( i -> panel.add( new JLabel() ) )
            .forEach( i -> panel.add( new JLabel() ) );
        
        System.out.println( count );
        return panel;
    }
    
    private class SpinnerDesc
    {
        private static final float      minVal  = 0;
        private static final float      maxVal  = Integer.MAX_VALUE;
        public final DoubleConsumer     setter;
        public final JLabel             label;
        public final JSpinner           spinner;
        
        public SpinnerDesc(
            DoubleConsumer setter, 
            String labelText, 
            String property,
            double step
        )
        {
            super();
            this.setter = setter;
            
            double              val     = pMgr.asFloat( property );
            SpinnerNumberModel  model   = 
                new SpinnerNumberModel( val, minVal, maxVal, step );
            spinner = new JSpinner( model );
            label = new JLabel( labelText, SwingConstants.RIGHT );
            
            model.addChangeListener( e -> {
                float   value   = model.getNumber().floatValue();
                setter.accept( value );
                canvas.repaint();
            });
        }
    }
}
