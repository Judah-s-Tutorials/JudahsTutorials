package com.acmemail.judah.cartesian_plane.sandbox.experimental.line_editor_rev1;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;

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
    private static final String gridUnit    = "Grid Unit";
    private static final String majorTicsPU = "Major Tics/Unit";
    private static final String minorTicsPU = "Minor Tics/Unit";
    private static final String gridLinesPU = "Grid Lines/Unit";
    
    private final PropertyManager   pMgr    = PropertyManager.INSTANCE;

    private final JFrame        frame       = new JFrame( "Dialer" );
    private final Canvas       canvas      = new Canvas();
    private final DrawManager  drawManager = canvas.getDrawManager();
    
    /** Label to accompany the GridUnit component. */
    private final JLabel                gridUnitLabel   = 
        new JLabel( gridUnit, SwingConstants.RIGHT );
    
    /** Label to accompany the MajorTics component. */
    private final JLabel                majorTicLabel       = 
        new JLabel( majorTicsPU, SwingConstants.RIGHT );
    
    /** Label to accompany the MinorTic component. */
    private final JLabel                minorTicLabel       = 
        new JLabel( minorTicsPU, SwingConstants.RIGHT );
    
    /** Label to accompany the GridLine component. */
    private final JLabel                gridLineLabel       = 
        new JLabel( gridLinesPU, SwingConstants.RIGHT );

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
        
        JPanel      left    = new JPanel( new GridLayout( 2, 2, 3, 1 ) );   
        left.add( gridUnitLabel );
        left.add( getGridUnitSpinner() );
        left.add( majorTicLabel );
        left.add( getMajorTicSpinner() );
        
        JPanel      right   = new JPanel( new GridLayout( 2, 2, 3, 1 ) );   
        right.add( gridLineLabel );
        right.add( getGridLineSpinner() );
        right.add( minorTicLabel );
        right.add( getMinorTicSpinner() );
        
        panel.add( left );
        panel.add( Box.createRigidArea( new Dimension( 10, 0  ) ) );
        panel.add( right );
        
        JPanel bogus = new JPanel();
//        bogus.add( panel );
        left.setAlignmentX( Component.LEFT_ALIGNMENT );
        right.setAlignmentX( Component.LEFT_ALIGNMENT );
        bogus.setAlignmentX( Component.LEFT_ALIGNMENT );

        return panel;
    }
    
    private JSpinner getGridUnitSpinner()
    {
        double              dProp   = 
            pMgr.asFloat( CPConstants.GRID_UNIT_PN );
        SpinnerNumberModel  model   =
            new SpinnerNumberModel( dProp, 0, Integer.MAX_VALUE, 1 );
        JSpinner            spinner = new JSpinner( model );
        model.addChangeListener( e -> {
            float               value   = model.getNumber().floatValue();
            drawManager.setGridUnit( value );
            canvas.repaint();
        });
        return spinner;
    }
    
    private JSpinner getMajorTicSpinner()
    {
        double              dProp   = 
            pMgr.asFloat( CPConstants.TIC_MAJOR_MPU_PN );
        SpinnerNumberModel  model   =
            new SpinnerNumberModel( dProp, 0, Integer.MAX_VALUE, .25 );
        JSpinner            spinner = new JSpinner( model );
        model.addChangeListener( e -> {
            float               value   = model.getNumber().floatValue();
            drawManager.setTicMajorMPU( value );
            canvas.repaint();
        });
        return spinner;
    }
    
    private JSpinner getMinorTicSpinner()
    {
        double              dProp   = 
            pMgr.asFloat( CPConstants.TIC_MINOR_MPU_PN );
        SpinnerNumberModel  model   =
            new SpinnerNumberModel( dProp, 0, Integer.MAX_VALUE, .25 );
        JSpinner            spinner = new JSpinner( model );
        model.addChangeListener( e -> {
            float               value   = model.getNumber().floatValue();
            drawManager.setTicMinorMPU( value );
            canvas.repaint();
        });
        return spinner;
    }
    
    private JSpinner getGridLineSpinner()
    {
        double              dProp   = 
            pMgr.asFloat( CPConstants.GRID_LINE_LPU_PN );
        SpinnerNumberModel  model   =
            new SpinnerNumberModel( dProp, 0, Integer.MAX_VALUE, 1 );
        JSpinner            spinner = new JSpinner( model );
        model.addChangeListener( e -> {
            float               value   = model.getNumber().floatValue();
            drawManager.setGridLineLPU( value );
            canvas.repaint();
        });
        return spinner;
    }
}
