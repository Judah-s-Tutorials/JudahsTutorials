package com.acmemail.judah.sandbox;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.Optional;
import java.util.OptionalInt;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;

public class PropertiesFrame
{
    private static final String STATUS_KEY      =
        "judah.JColorChooserDemo.status";
    
    private final ButtonGroup   lineGroup   = new ButtonGroup();
    private final JColorChooser colorPane   = new JColorChooser();
    private final JDialog       colorDialog =
        JColorChooser.createDialog(
            null, 
            "Choose a Color", 
            true, 
            colorPane, 
            e -> setAndClose( JOptionPane.OK_OPTION ), 
            e -> setAndClose( JOptionPane.CANCEL_OPTION )
        );
    
    private final PropertyManager   pMgr    = PropertyManager.instanceOf();
    
    public static void main(String[] args)
    {
        PropertiesFrame   lines   = new PropertiesFrame();
        SwingUtilities.invokeLater( () -> lines.build() );
    }
    
    public void build()
    {
        JFrame      frame   = new JFrame( "Properties Test" );
        frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        frame.setContentPane( new ContentPane() );
        frame.pack();
        frame.setVisible( true );
    }
    
    private void setAndClose( int choice )
    {
        colorPane.putClientProperty( STATUS_KEY, choice );
        colorDialog.setVisible( false );
    }

    @SuppressWarnings("serial")
    public class ContentPane extends JPanel
    {
        private final BoxLayout layout  = 
            new BoxLayout( this, BoxLayout.X_AXIS );
        private final Border    lineBorder      = 
            BorderFactory.createLineBorder( Color.BLACK, 1 );
        private final Border    titledBorder    = 
            BorderFactory.createTitledBorder( lineBorder, "Lines" );
        private final Border    emptyBorder     =
            BorderFactory.createEmptyBorder( 10, 10, 10, 10 );
        private final Border    border          =
            BorderFactory.createCompoundBorder( emptyBorder, titledBorder );
        
        private final Component horSpace        =
            Box.createRigidArea( new Dimension( 50, 0 ) );
        
        public ContentPane()
        {
            setLayout( layout );
            setBorder( border );
            add( new RadioBoxPanel() );
            add( horSpace );
            add( new PropertiesPanel() );
        }
    }
    
    @SuppressWarnings("serial")
    private class RadioBoxPanel extends JPanel implements ActionListener
    {
        private static final String minorTics   = "Minor Tics";
        private static final String majorTics   = "Major Tics";
        private static final String gridLines   = "Grid Lines";
        private static final String axes        = "Axes";
        
        private static final String[]   labels  =
            { axes, majorTics, minorTics, gridLines };
        
        public RadioBoxPanel()
        {
            super( new GridLayout( 4, 1 ) );
            
            Arrays.stream( labels )
                .map( JRadioButton::new )
                .peek( this::add )
                .peek( b -> b.addActionListener( this ) )
                .forEach( b -> lineGroup.add( b ) );
        }

        @Override
        public void actionPerformed( ActionEvent evt )
        {
            Object  source  = evt.getSource();
            if ( !(source instanceof JRadioButton ) )
                System.err.println( "Expected radio button, got..." );
            else
            {
                String  text    = ((JRadioButton)source).getText();
                System.out.println( "Radio button: " + text );
            }
        }
    }
    
    @SuppressWarnings("serial")
    private class PropertiesPanel extends JPanel 
//        implements ChangeListener
    {
        private static final int    defVal      = 1;
        private static final int    defMin      = 0;
        private static final int    defMax      = 500;
        private static final int    defStep     = 1;
        
        private static final String stroke      = "Stroke";
        private static final String length      = "Length";
        private static final String spacing     = "Spacing";
        private static final String color       = "Color";
        
        private final JSpinner      strokeSpinner   = getDefSpinner();
        private final JSpinner      lengthSpinner   = getDefSpinner();
        private final JSpinner      spacingSpinner  = getDefSpinner(); 
        
        private final JTextField    colorField      = new JTextField();
        private final JButton       colorButton     = new JButton( color );
        
        private final StrokeFeedback  strokeFB      = 
            new StrokeFeedback( strokeSpinner, null );
        private final ColorFeedback  colorFB        = 
            new ColorFeedback( colorField );
        private final LengthFeedback lengthFB       = 
            new LengthFeedback( lengthSpinner, null );
        private final SpacingFeedback   spacingFB   = 
            new SpacingFeedback( spacingSpinner, null );
        
        public PropertiesPanel()
        {
            super( new GridLayout( 4, 3, 5, 3 ) );
            
            add( new JLabel( stroke, SwingConstants.RIGHT ) );
            add( strokeSpinner );
            add( strokeFB );
            strokeSpinner.addChangeListener( e -> strokeFB.repaint() );
            
            add( new JLabel( length, SwingConstants.RIGHT ) );
            add( lengthSpinner );
            add( lengthFB );
            lengthSpinner.addChangeListener( e -> lengthFB.repaint() );
            
            add( new JLabel( spacing, SwingConstants.RIGHT ) );
            add( spacingSpinner );
            add( spacingFB );
            spacingSpinner.addChangeListener( e -> spacingFB.repaint() );
            
            add( colorButton );
            add( colorField );
            add( colorFB );
            colorField.addActionListener( e -> colorFB.repaint() );
            colorButton.addActionListener( e -> showColorDialog() );
        }
        
        private JSpinner getDefSpinner()
        {
            SpinnerNumberModel  spinnerModel    =
                new SpinnerNumberModel( defVal, defMin, defMax, defStep );
            JSpinner            spinner = new JSpinner( spinnerModel );
            return spinner;
        }

        public void showColorDialog()
        {
            colorDialog.setVisible( true );
            Object  objStatus   = colorPane.getClientProperty( STATUS_KEY );
            if ( objStatus == null || !(objStatus instanceof Integer) )
                throw new RuntimeException( "Color error: " + objStatus );
            int intStatus       = (int)objStatus;
            System.out.println( intStatus );
            if ( intStatus == JOptionPane.OK_OPTION )
            {
                Color       color   = colorPane.getColor();
                String      text    = Feedback.getText( color );
                colorField.setText( text );
                colorFB.repaint();
            }
        }
    }
    
    private class PropertySet
    {
        private final String    major;
        private OptionalInt     stroke;
        private OptionalInt     length;
        private OptionalInt     spacing;
        private Optional<Color> color;
        
        public PropertySet( String major )
        {
            this.major = major;
            stroke = pMgr.getAsInt( major, PropertyManager.STROKE );
            length = pMgr.getAsInt( major, PropertyManager.LENGTH );
            spacing = pMgr.getAsInt( major, PropertyManager.SPACING );
            color = pMgr.getAsColor( major, PropertyManager.COLOR );
        }
        
        public void apply()
        {
            if ( hasStroke() )
                pMgr.put( major, PropertyManager.STROKE, stroke.getAsInt() );
            if ( hasLength() )
                pMgr.put( major, PropertyManager.LENGTH, length.getAsInt() );
            if ( hasSpacing() )
                pMgr.put( major, PropertyManager.SPACING, spacing.getAsInt() );
            if ( hasColor() )
                pMgr.put( major, PropertyManager.COLOR, color.get() );
        }
        
        public boolean hasStroke()
        {
            return stroke.isPresent();
        }
        
        public boolean hasLength()
        {
            return length.isPresent();
        }
        
        public boolean hasSpacing()
        {
            return spacing.isPresent();
        }
        
        public boolean hasColor()
        {
            return color.isPresent();
        }
        
        public int getStroke()
        {
            return stroke.orElse( -1 );
        }
        
        public int getLength()
        {
            return stroke.orElse( -1 );
        }
        
        public int getSpacing()
        {
            return stroke.orElse( -1 );
        }
        
        public Color getColor()
        {
            return color.orElse( null );
        }
        
        public void setStroke( int stroke )
        {
            if ( hasStroke() )
                this.stroke = OptionalInt.of( stroke );
        }
        
        public void setLength( int length )
        {
            if ( hasLength() )
                this.length = OptionalInt.of( length );
        }
        
        public void setSpacing( int spacing )
        {
            if ( hasSpacing() )
                this.spacing = OptionalInt.of( spacing );
        }
        
        public void setColor( Color color )
        {
            if ( hasColor() )
                this.color = Optional.of( color );
        }
    }
}
