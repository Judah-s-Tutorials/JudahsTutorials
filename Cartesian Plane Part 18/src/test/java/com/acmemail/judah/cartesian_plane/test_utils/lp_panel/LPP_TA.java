package com.acmemail.judah.cartesian_plane.test_utils.lp_panel;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.function.Predicate;
import java.util.stream.Stream;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;

import com.acmemail.judah.cartesian_plane.components.LinePropertySet;
import com.acmemail.judah.cartesian_plane.components.LinePropertySetAxes;
import com.acmemail.judah.cartesian_plane.components.LinePropertySetGridLines;
import com.acmemail.judah.cartesian_plane.components.LinePropertySetTicMajor;
import com.acmemail.judah.cartesian_plane.components.LinePropertySetTicMinor;
import com.acmemail.judah.cartesian_plane.components.PButtonGroup;
import com.acmemail.judah.cartesian_plane.components.PRadioButton;
import com.acmemail.judah.cartesian_plane.graphics_utils.ComponentException;
import com.acmemail.judah.cartesian_plane.test_utils.LinePropertySetInitializer;
import com.acmemail.judah.cartesian_plane.test_utils.Utils;

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
    /**
     * Master directory for all LinePropertiesPanel test data files.
     * This will be a subdirectory of the project test data files; 
     * see {@linkplain Utils#BASE_TEST_DATA_DIR}.
     * The directory should contain four files containing serialized
     * data, with suffix "Image.ser" and prefixes "Axes," "GridLines," 
     * "MinorTics" and "MajorTics." 
     */
    public static final String  LPP_DIR = "LinePropertiesPanel";

    /** 
     * Path to master directory 
     * for all LinePropertiesPanel test data files.
     */
    private static final File   lppPath = Utils.getTestDataDir( LPP_DIR );
    /** Title of the application's main window. */
    private static final String title   = "LinePropertiesPanel TA";
    /** Dialog for displaying test data. */
    private final LPPTestDialog lppDialog;
    /** Button group for all application radio buttons. */
    private final PButtonGroup<Descriptor>  buttonGroup = 
        new PButtonGroup<>();        
    /** The label used to display the next file name to use. */
    private final JLabel                    fileName    = new JLabel();
    /** 
     * The label used to display the name of the current class
     * for which test data is being gathered.
     */
    private final JLabel                    className   = new JLabel();
    
    /**
     * Application entry point.
     *
     * @param args command line arguments, not used
     *
    */
    public static void main(String[] args)
    {
        LinePropertySetInitializer.initProperties();
        SwingUtilities.invokeLater( () -> new LPP_TA() );
    }
    
    /**
     * Constructor.
     * Initializes the application GUI.
     * <p>
     * Precondition:
     * Must be invoked on the EDT.
     */
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
    
    /**
     * Gets the main panel
     * for this application's GUI.
     * This consists of two additional panels,
     * one containing the application 
     * radio buttons
     * and one to display feedback
     * regarding the current state
     * of the application.
     * The two sub-panels
     * are arranged horizontally,
     * 
     * @return  the main panel for this application's GUI
     */
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

    /**
     * Gets the panel
     * containing the options 
     * for this application. 
     * This consists of a radio button
     * for each option
     * and a check box indicating whether
     * data for the option 
     * has been saved.
     * The radio button and associated check box
     * for each option
     * are laid out horizontally;
     * the options are then arranged vertically.
     * 
     * @return  the panel containing the options for this application
     */
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
    
    /**
     * Method to execute
     * when an application option
     * (represented as a radio button)
     * is selected.
     * The option in the test dialog
     * is synchronized with the select option,
     * and the feedback label
     * describing the property set being examined
     * is updated.
     * 
     * @param evt   
     *      event object associated with the initiating selection event
     */
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
        String      cName   = descrip.className;
        className.setText( cName );
        fileName.setText( descrip.fileName );
        
        Predicate<PRadioButton<LinePropertySet>>    pred    = 
            b -> cName.equals( getSimpleName( b ) );
        PRadioButton<LinePropertySet>   target  =
            lppDialog.getRadioButtons().stream()
                .filter( pred )
                .findFirst().orElse( null );
        if ( target == null )
            throw new ComponentException( "target button not found" );
        lppDialog.doClick( target );
    }
    
    /**
     * Given a radio button
     * encapsulating a LinePropertySet
     * obtains the class name
     * of the LinePropertySet.
     * 
     * @param button    the given radio button
     * 
     * @return  
     *      the class name of the given button's 
     *      encapsulated LinePropertySet.
     */
    private static String 
    getSimpleName( PRadioButton<LinePropertySet> button )
    {
        LinePropertySet set     = button.get();
        String          name    = set.getClass().getSimpleName();
        return name;
    }

    /**
     * Gets the panel containing 
     * the GUI's feedback labels
     * for displaying the current file name
     * and LinePropertySet class.
     * The labels are laid out vertically.
     * 
     * @return  the panel containing the GUI's feedback labels
     */
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
    
    /**
     * Returns the panel
     * containing the application's controls
     * (the Save and Close buttons).
     * The panel is configured
     * with a FlowLayout,
     * so the buttons will typically
     * be laid out horizontally.
     * 
     * @return  the panel containing the application's controls
     */
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
    
    /**
     * Action method to be executed
     * with the Save button is selected.
     * 
     * @param evt   
     *      event object associated with the selection event;
     *      not used
     */
    private void saveAction( ActionEvent evt )
    {
        PRadioButton<Descriptor>    button  = 
            buttonGroup.getSelectedButton();
        Descriptor      descrip = button.get();
        BufferedImage   image   = lppDialog.getPanelImage();
        LPP_TADetail    detail  = 
            new LPP_TADetail( descrip.clazz, image );
        saveDetail( detail, descrip.fileName );

        descrip.saved.setSelected( true );
    }
    
    /**
     * Saves a given LPP_TADetail object
     * to a file with a given name.
     * 
     * @param detail    the given detail object
     * @param fileName  the given file name
     */
    private static void 
    saveDetail( LPP_TADetail detail, String fileName )
    {
        File    dataFile    = new File( lppPath, fileName );
        try (
            FileOutputStream fStream = new FileOutputStream( dataFile );
            ObjectOutputStream oStream = new ObjectOutputStream( fStream ); 
        )
        {
            oStream.writeObject( detail );
        }
        catch ( IOException exc )
        {
            exc.printStackTrace();
            System.exit( 1 );
        }
    }
    
    /**
     * Simple class to encapsulate the state
     * of an option.
     * This includes:
     * <ul>
     * <li>
     *      The text to display with the option,
     *      for example "TicMajor";
     * </li>
     * <li>
     *      The file name attached to the option,
     *      for example "TicMajorImage.ser";</li>
     * <li>The file name attached to the option;</li>
     * <li>
     *      The class name attached to the option,
     *      for example "LinePropertySetTicMajor";</li>
     * <li>
     *      The class of the LineProperty attached to the option,
     *      for example LinePropertySetTicMajor.class; and
     * </li>
     * <li>
     *      Whether or not the test data
     *      associated with this option has been saved.
     * </li>
     * </ul>
     * @author Jack Straub
     */
    private static class Descriptor
    {
        /** Text associated with a given option. */
        public final String     text;
        /** File name associated with a given option. */
        public final String     fileName;
        /** Class name associated with a given option. */
        public final String     className;
        /** 
         * Indicates whether or not the test data for a given option
         * has been saved.
         */
        public final JCheckBox  saved;
        /** The Class class associated with a given option. */
        public final Class<? extends LinePropertySet>   clazz;
        
        /**
         * Constructor.
         * Initializes all fields for this object.
         * 
         * @param <T>   
         *      type of class associated with
         *      a given option; 
         *      must be a subclass of LinePropertySet
         * @param clazz
         *      Class class associated with a given option.
         */
        public <T extends LinePropertySet> Descriptor( Class<T> clazz )
        {
            final int   count       = 
                LinePropertySet.class.getSimpleName().length();
            
            this.clazz = clazz;
            className   = clazz.getSimpleName();
            text = className.substring( count );
            fileName = text + "Image.ser";
            String  savedText   = "<html><em>saved</em></html>";
            saved = new JCheckBox( savedText );
            saved.setEnabled( false );
        }
    }
}
