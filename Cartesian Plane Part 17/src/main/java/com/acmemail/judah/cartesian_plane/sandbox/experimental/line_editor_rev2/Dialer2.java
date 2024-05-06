package com.acmemail.judah.cartesian_plane.sandbox.experimental.line_editor_rev2;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.util.HashMap;
import java.util.Map;
import java.util.function.DoubleConsumer;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JSpinner.DefaultEditor;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;

import com.acmemail.judah.cartesian_plane.CPConstants;
import com.acmemail.judah.cartesian_plane.PropertyManager;
import com.acmemail.judah.cartesian_plane.components.ColorEditor;
import com.acmemail.judah.cartesian_plane.components.FontEditor;
import com.acmemail.judah.cartesian_plane.components.GraphPropertySet;
import com.acmemail.judah.cartesian_plane.components.LinePropertySet;
import com.acmemail.judah.cartesian_plane.graphics_utils.ComponentException;

public class Dialer2
{
    private static final String weightLabel     = "Weight";
    private static final String lpuLabel        = "Lines/Unit";
    private static final String lenLabel        = "Length";
    private static final String drawLabel       = "Draw";
    
    private static final String gridUnitLabel   = "Grid Unit";
    private static final String axisLabel       = "Axes";
    private static final String majorTicsLabel  = "Major Tics";
    private static final String minorTicsLabel  = "Minor Tics";
    private static final String gridLinesLabel  = "Grid Lines";
    
    private final PropertyManager   pMgr    = PropertyManager.INSTANCE;

    private final JFrame        frame       = new JFrame( "Dialer 2" );
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
        SwingUtilities.invokeLater( () -> new Dialer2() );
    }
    
    public Dialer2()
    {
        frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        JPanel  contentPane     = new JPanel( new BorderLayout() );
        contentPane.add( BorderLayout.CENTER, canvas );
        contentPane.add( BorderLayout.WEST, getControlPanel() );
        contentPane.add( BorderLayout.SOUTH, getButtonPanel() );
        
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
            CPConstants.GRID_UNIT_PN, 
            1
        );
        map.put( gridUnitLabel, desc );
        
        desc = new SpinnerDesc(
            d -> drawManager.setAxisWeight( (float)d ),
            CPConstants.AXIS_WEIGHT_PN,
            1
        );
        map.put( axisLabel + weightLabel, desc );
        
        desc = new SpinnerDesc(
            d -> drawManager.setGridLineLPU( (float)d ), 
            CPConstants.GRID_LINE_LPU_PN, 
            1
        );
        map.put( gridLinesLabel + lpuLabel, desc );
        
        desc = new SpinnerDesc(
            d -> drawManager.setGridLineWeight( (float)d ), 
            CPConstants.GRID_LINE_WEIGHT_PN, 
            1
        );
        map.put( gridLinesLabel + weightLabel, desc );
        
        desc = new SpinnerDesc(
            d -> drawManager.setTicMajorMPU( (float)d ), 
            CPConstants.TIC_MAJOR_MPU_PN, 
            .25
        );
        map.put( majorTicsLabel + lpuLabel, desc );
        
        desc = new SpinnerDesc(
            d -> drawManager.setTicMajorLength( (float)d ), 
            CPConstants.TIC_MAJOR_LEN_PN, 
            1
        );
        map.put( majorTicsLabel + lenLabel, desc );
        
        desc = new SpinnerDesc(
            d -> drawManager.setTicMajorWeight( (float)d ), 
            CPConstants.TIC_MAJOR_WEIGHT_PN, 
            1
        );
        map.put( majorTicsLabel + weightLabel, desc );
        
        desc = new SpinnerDesc(
            d -> drawManager.setTicMinorMPU( (float)d ), 
            CPConstants.TIC_MINOR_MPU_PN, 
            .25
        );
        map.put( minorTicsLabel + lpuLabel, desc );
        
        desc = new SpinnerDesc(
            d -> drawManager.setTicMinorLength( (float)d ), 
            CPConstants.TIC_MINOR_LEN_PN, 
            1
        );
        map.put( minorTicsLabel + lenLabel, desc );
        
        desc = new SpinnerDesc(
            d -> drawManager.setTicMinorWeight( (float)d ), 
            CPConstants.TIC_MINOR_WEIGHT_PN, 
            1
        );
        map.put( minorTicsLabel + weightLabel, desc );
        
        return map;
    }
    
    private JPanel getControlPanel()
    {
        JPanel      panel   = new JPanel();
        BoxLayout   layout  = new BoxLayout( panel, BoxLayout.Y_AXIS );
        Border      inner   =
            BorderFactory.createLineBorder( Color.BLACK );
        Border      outer   =
            BorderFactory.createEmptyBorder( 3, 3, 3, 3 );
        Border      border  =
            BorderFactory.createCompoundBorder( outer, inner );
        panel.setBorder( border );
        panel.setLayout( layout );
        
        panel.add( getSpinnerPanel() );
                
        return panel;
    }
    
    private JPanel getButtonPanel()
    {
        JPanel      panel   = new JPanel();
        JButton     exit    = new JButton( "Exit" );
        panel.add( exit );
        exit.addActionListener( e -> System.exit( 0 ) );
        return panel;
    }
    
    private JPanel getSpinnerPanel()
    {
        JPanel      panel   = new JPanel();
        BoxLayout   layout  = new BoxLayout( panel, BoxLayout.Y_AXIS );
        Border      inner   =
            BorderFactory.createBevelBorder( BevelBorder.RAISED );
        Border      outer   =
            BorderFactory.createEmptyBorder( 3, 3, 3, 3 );
        Border      border  =
            BorderFactory.createCompoundBorder( outer, inner );
        panel.setBorder( border );
        panel.setLayout( layout );
        
        panel.add( getGridPanel() );
        
        JPanel  axisPanel    =
            getPropertyPanel( axisLabel, drawManager.getAxis() );
        panel.add( axisPanel );
        
        JPanel  gridLinePanel    =
            getPropertyPanel( gridLinesLabel, drawManager.getGridLine() );
        panel.add( gridLinePanel );

        JPanel  majorTicPanel    =
            getPropertyPanel( majorTicsLabel, drawManager.getTicMajor() );
        panel.add( majorTicPanel );
        
        JPanel  minorTicPanel    =
            getPropertyPanel( minorTicsLabel, drawManager.getTicMinor() );
        panel.add( minorTicPanel );
        
        return panel;
    }
    
    private JPanel getGridPanel()
    {
        GraphPropertySet    propSet = drawManager.getMainWindow();
        JPanel      panel   = new JPanel( new GridLayout( 3, 2 ) );
        SpinnerDesc desc    = descMap.get( gridUnitLabel );
        JLabel      text    = new 
            JLabel( gridUnitLabel, SwingConstants.RIGHT );
        panel.add( text );
        panel.add( desc.spinner );
        addColorEditor( propSet, panel );
        addFontEditor( propSet, panel );
        return panel;
    }
    
    private JPanel 
    getPropertyPanel( String title, LinePropertySet propSet )
    {
        int     rows    = 0;
        JPanel  panel   = new JPanel();
        if ( propSet.hasSpacing() )
        {
            addSpinner( title, lpuLabel, panel );
            ++rows;
        }
        if ( propSet.hasLength() )
        {
            addSpinner( title, lenLabel, panel );
            ++rows;
        }
        if ( propSet.hasStroke() )
        {
            addSpinner( title, weightLabel, panel );
            ++rows;
        }
        if ( propSet.hasDraw() )
        {
            addDraw( propSet, panel );
            ++rows;
        }
        if ( propSet.hasColor() )
        {
            addColorEditor( propSet, panel );
            ++rows;
        }
        
        Border      lineBorder  = 
            BorderFactory.createLineBorder( Color.BLACK );
        Border      border      = 
            BorderFactory.createTitledBorder( lineBorder, title );
        GridLayout  layout  = new GridLayout( rows, 2 );
        panel.setBorder(border);
        panel.setLayout( layout );
        return panel;
    }
    
    private void addSpinner( String title, String label, JPanel panel )
    {
        SpinnerDesc desc    = descMap.get( title + label );
        JSpinner    spinner = desc.spinner;
        JLabel      text   = 
            new JLabel( label, SwingConstants.RIGHT );
        panel.add( text );
        panel.add( spinner );
    }
    
    private void addDraw( LinePropertySet propSet, JPanel panel )
    {
        JLabel      label   = 
            new JLabel( drawLabel, SwingConstants.RIGHT );
        boolean     val         = propSet.getDraw();
        JCheckBox   checkBox    = new JCheckBox( "", val );
        panel.add( label );
        panel.add( checkBox );
        checkBox.addItemListener( e -> {
            propSet.setDraw( checkBox.isSelected() );
            canvas.repaint();
        });
    }
    
    private void addColorEditor( LinePropertySet propSet, JPanel panel )
    {
        ColorEditor colorEditor = new ColorEditor();
        colorEditor.setColor( propSet.getColor() );
        panel.add( colorEditor.getColorButton() );
        panel.add( colorEditor.getTextEditor() );
        colorEditor.addActionListener( e -> {
            propSet.setColor( colorEditor.getColor().orElse( null ) );
            canvas.repaint();
        });
    }
    
    private void addColorEditor( GraphPropertySet propSet, JPanel panel )
    {
        ColorEditor colorEditor = new ColorEditor();
        colorEditor.setColor( propSet.getBGColor() );
        panel.add( colorEditor.getColorButton() );
        panel.add( colorEditor.getTextEditor() );
        colorEditor.addActionListener( e -> {
            propSet.setBGColor( colorEditor.getColor().orElse( null ) );
            canvas.repaint();
        });
    }
    
    private void addFontEditor( GraphPropertySet propSet, JPanel panel )
    {
        FontEditor  fontEditor  = new FontEditor();
        fontEditor.setBold( propSet.isBold() );
        fontEditor.setItalic( propSet.isItalic() );
        fontEditor.setSize( (int)propSet.getFontSize() );
        fontEditor.setName( propSet.getFontName() );
        fontEditor.setColor( propSet.getFGColor() );
        JButton editButton  = new JButton( "Edit Font" );
        panel.add( new JLabel( "" ) );
        panel.add( editButton );
    }
    
    private class SpinnerDesc
    {
        private static final float      minVal  = 0;
        private static final float      maxVal  = Integer.MAX_VALUE;
        public final JSpinner           spinner;
        
        public SpinnerDesc(
            DoubleConsumer setter, 
            String property,
            double step
        )
        {
            super();
            
            double              val     = pMgr.asFloat( property );
            SpinnerNumberModel  model   = 
                new SpinnerNumberModel( val, minVal, maxVal, step );
            spinner = new JSpinner( model );
            
            JComponent  editor          = spinner.getEditor();
            if ( !(editor instanceof DefaultEditor) )
                throw new ComponentException( "Unexpected editor type" );
            JTextField  textField   = 
                ((DefaultEditor)editor).getTextField();
            textField.setColumns( 6 );
            
            model.addChangeListener( e -> {
                float   value   = model.getNumber().floatValue();
                setter.accept( value );
                canvas.repaint();
            });
        }
    }
    
    private class FontDialog extends JDialog
    {
        public FontDialog( GraphPropertySet propSet )
        {
            super( frame, "Font Editor", true );
        }
    }
}
