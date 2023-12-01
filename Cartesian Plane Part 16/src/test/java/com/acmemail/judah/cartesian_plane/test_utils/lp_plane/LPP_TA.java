package com.acmemail.judah.cartesian_plane.test_utils.lp_plane;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Stream;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;

import com.acmemail.judah.cartesian_plane.components.LinePropertiesPanel;
import com.acmemail.judah.cartesian_plane.components.LinePropertySet;
import com.acmemail.judah.cartesian_plane.components.LinePropertySetAxes;
import com.acmemail.judah.cartesian_plane.components.LinePropertySetGridLines;
import com.acmemail.judah.cartesian_plane.components.LinePropertySetTicMajor;
import com.acmemail.judah.cartesian_plane.components.LinePropertySetTicMinor;
import com.acmemail.judah.cartesian_plane.components.PButtonGroup;
import com.acmemail.judah.cartesian_plane.components.PRadioButton;
import com.acmemail.judah.cartesian_plane.graphics_utils.ComponentException;

/**
 * This is the LinePropertiesPlane Test Assistant.
 * It generates up to four files
 * containing graphical rendering
 * of the LinePropertiesPlane,
 * one for for each selected radio button.
 * When you run the generator
 * it will display a dialog
 * containing the LinePropertiesPanel;
 * throughout the execution of this application
 * you should not attempt to modify
 * the LinePropertiesPlane GUI.
 * Instead, click one of the options
 * displayed on the left side
 * of this application's GUI;
 * this will force the LinePropertiesPanel
 * into one of four configurations;
 * if you're happy with the display
 * press the Save button.
 * If you wish
 * you can then select a different option.
 * @author Jack Straub
 */
public class LPP_TA
{
    private static final String title   = 
        "LinePropertiesPanel TA";
    private final LPPTestDialog             lppDialog;
    private final PButtonGroup<Descriptor>  buttonGroup = 
        new PButtonGroup<>();        
    private final JLabel                    fileName    = new JLabel();
    private final JLabel                    className   = new JLabel();
    
    /**
     * Application entry point.
     *
     * @param args command line arguments, not used
     *
    */
    public static void main(String[] args)
    {
        SwingUtilities.invokeLater( () -> new LPP_TA() );
    }
    
    public LPP_TA()
    {
        JFrame  frame   = new JFrame( title );
        frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        JPanel  pane    = new JPanel( new BorderLayout() );
        pane.add( getMainPanel(), BorderLayout.CENTER );
        pane.add( getControlPanel(), BorderLayout.SOUTH );
        
        lppDialog = LPPTestDialog.getDialog();
        buttonGroup.selectIndex( 0 );
        
        frame.setLocation( 200, 200 );
        frame.setContentPane( pane );
        frame.pack();
        
        int     dialogXco   = frame.getX() + frame.getWidth() + 15;
        int     dialogYco   = frame.getY();
        lppDialog.setLocation( dialogXco, dialogYco );
        
        frame.setVisible( true );
        lppDialog.setVisible( true );
    }
    
    private JPanel getMainPanel()
    {
        JPanel      panel   = new JPanel();
        BoxLayout   layout  = new BoxLayout( panel, BoxLayout.X_AXIS );
        int         margin  = 15;
        Border      border  = BorderFactory
            .createEmptyBorder( margin, margin, margin, margin );
        
        panel.setBorder( border );
        panel.setLayout( layout );
        panel.add( getOptionPanel() );
        panel.add( getFeedbackPanel() );
        return panel;
    }

    private JPanel getOptionPanel()
    {
        Descriptor[]    descriptors =
        {
            new Descriptor( LinePropertySetAxes.class ),
            new Descriptor( LinePropertySetTicMajor.class ),
            new Descriptor( LinePropertySetTicMinor.class ),
            new Descriptor( LinePropertySetGridLines.class ),
        };
        
        JPanel  panel   = new JPanel( new GridLayout( 4, 2 ) );
        Border  border  = 
            BorderFactory.createTitledBorder( "Choose Image" );
        panel.setBorder( border );
        Stream.of( descriptors )
            .map( d -> new PRadioButton<>( d ) )
            .peek( b -> b.setText( b.get().text ) )
            .peek( panel::add )
            .peek( b -> panel.add( b.get().saved ) )
            .peek( buttonGroup::add )
            .forEach( b -> b.addActionListener( this::selectAction ) );
        return panel;
    }
    
    private void selectAction( ActionEvent evt )
    {
        Object  source  = evt.getSource();
        if ( !(source instanceof PRadioButton<?>) )
            throw new ComponentException( "Not a PRadioButtons" );
        PRadioButton<?> button  = (PRadioButton<?>)source;
        Object          prop    = button.get();
        if ( !(prop instanceof Descriptor) )
            throw new ComponentException( "Invalid PRadioButton" );
        Descriptor  descrip = (Descriptor)prop;
        fileName.setText( descrip.fileName );
        className.setText( descrip.className );
        
        Predicate<PRadioButton<LinePropertySet>>    pred    = 
            b -> descrip.className.equals( b.get().getClass().getSimpleName() );
        PRadioButton<LinePropertySet>   target  =
            lppDialog.getRadioButtons().stream()
                .filter( pred )
                .findFirst().orElse( null );
        if ( target == null )
            throw new ComponentException( "target button not found" );
        lppDialog.doClick( target );
    }

    private JPanel getFeedbackPanel()
    {
        final int margin = 10;
        JPanel      panel   = new JPanel();
        BoxLayout   layout  = new BoxLayout( panel, BoxLayout.Y_AXIS );
        Border      border  = BorderFactory
            .createEmptyBorder( margin, margin, margin, margin );
        panel.setLayout( layout );
        panel.setBorder( border );
        
        fileName.setAlignmentX( Component.CENTER_ALIGNMENT );
        className.setAlignmentX( Component.CENTER_ALIGNMENT );
        
        panel.add( fileName );
        panel.add( className );
        return panel;
    }
    
    private JPanel getControlPanel()
    {
        JPanel  panel   = new JPanel();
        JButton save    = new JButton( "Save" );
        JButton close   = new JButton( "Close" );
        panel.add( save );
        panel.add( close );
        
        save.addActionListener( this::saveAction );
        close.addActionListener( e -> System.exit( 0 ) );
        return panel;
    }
    
    private void saveAction( ActionEvent evt )
    {
        PRadioButton<Descriptor>    button  = 
            buttonGroup.getSelectedButton();
        button.get().saved.setSelected( true );
    }
    
    private static class Descriptor
    {
        public final String     text;
        public final String     fileName;
        public final String     className;
        public final JCheckBox  saved;
        
        public <T extends LinePropertySet> Descriptor( Class<T> clazz )
        {
            final int   count       = 
                LinePropertySet.class.getSimpleName().length();
            
            className   = clazz.getSimpleName();
            text = className.substring( count );
            fileName = text + "Image.ser";
            String  savedText   = "<html><em>saved</em></html>";
            saved = new JCheckBox( savedText );
            saved.setEnabled( false );
        }
    }
}
