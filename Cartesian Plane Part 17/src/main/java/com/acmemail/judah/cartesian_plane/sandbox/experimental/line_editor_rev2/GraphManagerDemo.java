package com.acmemail.judah.cartesian_plane.sandbox.experimental.line_editor_rev2;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.util.HashMap;
import java.util.Map;
import java.util.function.DoubleConsumer;
import java.util.function.DoubleSupplier;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JSpinner.DefaultEditor;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;

import com.acmemail.judah.cartesian_plane.components.ColorEditor;
import com.acmemail.judah.cartesian_plane.components.GraphPropertySet;
import com.acmemail.judah.cartesian_plane.components.LinePropertySet;
import com.acmemail.judah.cartesian_plane.graphics_utils.ComponentException;
import com.acmemail.judah.cartesian_plane.graphics_utils.GUIUtils;

public class GraphManagerDemo
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

    private final String        title       = "Show GraphManager Dialog";
    private final JFrame        frame       = new JFrame( title );
    private final Canvas        canvas      = new Canvas();
    private final GraphManager  drawManager = canvas.getDrawManager();
    
    private final Map<String,SpinnerDesc>   descMap = new HashMap<>();
    
    /**
     * Application entry point.
     *
     * @param args command line arguments, not used
     *
    */
    public static void main(String[] args)
    {
        SwingUtilities.invokeLater( () -> new GraphManagerDemo() );
    }
    
    public GraphManagerDemo()
    {
        getDescMap();
        
        frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        JPanel      contentPane     = new JPanel( new BorderLayout() );
        contentPane.add( BorderLayout.CENTER, canvas );
        JScrollPane scrollPane      = new JScrollPane( getControlPanel() );
        contentPane.add( BorderLayout.WEST, scrollPane );
        contentPane.add( BorderLayout.SOUTH, getButtonPanel() );
        
        frame.setContentPane( contentPane );
        contentPane.setFocusable( true );
        
        Dimension   scrollDim   = scrollPane.getPreferredSize();
        Dimension   sbarDim     = 
            scrollPane.getVerticalScrollBar().getPreferredSize();
        Dimension   screenSize  = 
            Toolkit.getDefaultToolkit().getScreenSize();
        scrollDim.height = (int)(.6 * screenSize.height);
        scrollDim.width += sbarDim.width;
        scrollPane.setPreferredSize( scrollDim );

        GUIUtils.center( frame );
        frame.pack();
        frame.setVisible( true );
    }
    
    private void getDescMap()
    {
        LinePropertySet         propSet = null;
        
        makeSpinnerDesc( null, gridUnitLabel, 1 );
        
        propSet = drawManager.getAxis();
        makeSpinnerDesc( propSet, weightLabel, 1 );
        
        propSet = drawManager.getGridLine();
        makeSpinnerDesc( propSet, lpuLabel, 1 );
        makeSpinnerDesc( propSet, weightLabel, 1 );
        
        propSet = drawManager.getTicMajor();
        makeSpinnerDesc( propSet, lpuLabel, .25 );
        makeSpinnerDesc( propSet, weightLabel, 1 );
        makeSpinnerDesc( propSet, lenLabel, 1 );
        
        propSet = drawManager.getTicMinor();
        makeSpinnerDesc( propSet, lpuLabel, .25 );
        makeSpinnerDesc( propSet, weightLabel, 1 );
        makeSpinnerDesc( propSet, lenLabel, 1 );
    }
    
    private JPanel getControlPanel()
    {
        JPanel      panel   = new JPanel();
        BoxLayout   layout  = new BoxLayout( panel, BoxLayout.Y_AXIS );
        Border      border  =
            BorderFactory.createEmptyBorder( 3, 3, 3, 3 );
        panel.setBorder( border );
        panel.setLayout( layout );
        
        panel.add( getControls() );
                
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
    
    private JPanel getControls()
    {
        JPanel      panel   = new JPanel();
        BoxLayout   layout  = new BoxLayout( panel, BoxLayout.Y_AXIS );
        Border      inner   =
            BorderFactory.createBevelBorder( BevelBorder.LOWERED );
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
        JPanel      panel   = new JPanel( new GridLayout( 3, 2, 3, 0 ) );
        SpinnerDesc desc    = descMap.get( gridUnitLabel );
        panel.add( desc.label );
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
        String  type    = propSet.getClass().getSimpleName();
        if ( propSet.hasSpacing() )
        {
            addSpinner( type, lpuLabel, panel );
            ++rows;
        }
        if ( propSet.hasLength() )
        {
            addSpinner( type, lenLabel, panel );
            ++rows;
        }
        if ( propSet.hasStroke() )
        {
            addSpinner( type, weightLabel, panel );
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
        GridLayout  layout  = new GridLayout( rows, 2, 3, 0 );
        panel.setBorder(border);
        panel.setLayout( layout );
        return panel;
    }
    
    private void addSpinner( String type, String label, JPanel panel )
    {
        SpinnerDesc desc    = descMap.get( type + label );
        panel.add( desc.label );
        panel.add( desc.spinner );
    }
    
    private void addDraw( LinePropertySet propSet, JPanel panel )
    {
        JLabel      label       = 
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
        FontEditorDialog    fontDialog  = 
            new FontEditorDialog( frame, propSet );
        JButton editButton  = new JButton( "Edit Font" );
        editButton.addActionListener( e -> showFontDialog( fontDialog ) );
        panel.add( new JLabel( "" ) );
        panel.add( editButton );
    }
    
    private void showFontDialog( FontEditorDialog dialog )
    {
        int result  = dialog.showDialog();
        if ( result == JOptionPane.OK_OPTION )
        {
            GraphPropertySet    propSet = dialog.getPropertySet();
            propSet.apply();
            canvas.repaint();
        }
    }
    
    public void makeSpinnerDesc( 
        LinePropertySet propSet,
        String labelText,
        double step
    )
    {
        SpinnerDesc desc        = 
            new SpinnerDesc( propSet, labelText, step );
        
        String      propSetType = 
            propSet == null ? "" : propSet.getClass().getSimpleName();
        String      key         = propSetType + labelText;
        descMap.put( key, desc );
    }
    
    private class SpinnerDesc
    {
        private static final float      minVal  = 0;
        private static final float      maxVal  = Integer.MAX_VALUE;
        public final JSpinner           spinner;
        public final JLabel             label;
        private final DoubleConsumer    setter;
        private final DoubleSupplier    getter;
        
        public SpinnerDesc(
            LinePropertySet propSet,
            String labelText,
            double step
        )
        {
            DoubleConsumer  tempSetter  = null;
            DoubleSupplier  tempGetter  = null;
            switch ( labelText )
            {
            case weightLabel:
                tempSetter = d -> propSet.setStroke( (float)d );
                tempGetter = () -> propSet.getStroke();
                break;
            case lpuLabel:
                tempSetter = d -> propSet.setSpacing( (float)d );
                tempGetter = () -> propSet.getSpacing();
                break;
            case lenLabel:
                tempSetter = d -> propSet.setLength( (float)d );
                tempGetter = () -> propSet.getLength();
                break;
            case gridUnitLabel:
                tempSetter = d -> drawManager.setGridUnit( (float)d );
                tempGetter = () -> drawManager.getGridUnit();
                break;
            default:
                throw new ComponentException( "Invalid Label" );
            }
            
            setter = tempSetter;
            getter = tempGetter;
            label = new JLabel( labelText, SwingConstants.RIGHT );
            
            double              val     = getter.getAsDouble();
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
}
