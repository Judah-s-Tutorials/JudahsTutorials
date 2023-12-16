package com.acmemail.judah.cartesian_plane.test_utils.gp_plane;

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

import com.acmemail.judah.cartesian_plane.components.GraphPropertySet;
import com.acmemail.judah.cartesian_plane.components.GraphPropertySetBM;
import com.acmemail.judah.cartesian_plane.components.GraphPropertySetLM;
import com.acmemail.judah.cartesian_plane.components.GraphPropertySetMW;
import com.acmemail.judah.cartesian_plane.components.GraphPropertySetRM;
import com.acmemail.judah.cartesian_plane.components.GraphPropertySetTM;
import com.acmemail.judah.cartesian_plane.components.PButtonGroup;
import com.acmemail.judah.cartesian_plane.components.PRadioButton;
import com.acmemail.judah.cartesian_plane.graphics_utils.ComponentException;
import com.acmemail.judah.cartesian_plane.test_utils.Utils;

/**
 * This is the GraphPropertiesPlane Test Assistant.
 * It generates up to five files
 * containing graphical rendering
 * of the GraphPropertiesPlane,
 * one for for each selected radio button.
 * When you run the generator
 * it will display a dialog
 * containing the GraphPropertiesPanel;
 * throughout the execution of this application
 * you should not attempt to modify
 * the GraphPropertiesPanel GUI.
 * Instead, click one of the options
 * displayed on the left side
 * of this application's GUI;
 * this will force the LinePropertiesPanel
 * into one of five configurations;
 * if you're happy with the display
 * press the Save button.
 * If you wish
 * you can then select a different option.
 * @author Jack Straub
 */
public class GPP_TA
{
    /**
     * Master directory for all GraphPropertiesPnel test data files.
     * This will be a subdirectory of the project test data files; 
     * see {@linkplain Utils#BASE_TEST_DATA_DIR}.
     * The directory should contain five files containing serialized
     * data, with suffix "Image.ser" and prefixes "MainWindow," 
     * "TopMargin," "RightMargin," "BottomMargin" and "LeftMargin." 
     */
    public static final String  GPP_DIR = "GraphPropertiesPanel";

    private static final File   gppPath = Utils.getTestDataDir( GPP_DIR );
    private static final String title   = "GraphPropertiesPanel TA";
    private final GPPTestDialog gppDialog;
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
        new GPPTestDataInitializer();
        SwingUtilities.invokeLater( () -> new GPP_TA() );
    }
    
    /**
     * Constructor.
     * Fully instantiates 
     * and configures the GUI.
     * <p>
     * Precondition:
     * Must be invoked on the EDT.
     */
    public GPP_TA()
    {
        JFrame  frame   = new JFrame( title );
        frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        JPanel  pane    = new JPanel( new BorderLayout() );
        pane.add( getMainPanel(), BorderLayout.CENTER );
        pane.add( getControlPanel(), BorderLayout.SOUTH );
        
        gppDialog = GPPTestDialog.getDialog();
        buttonGroup.selectIndex( 0 );
        
        frame.setLocation( 200, 200 );
        frame.setContentPane( pane );
        frame.pack();
        
        int     dialogXco   = frame.getX() + frame.getWidth() + 15;
        int     dialogYco   = frame.getY();
        gppDialog.setLocation( dialogXco, dialogYco );
        
        frame.setVisible( true );
        gppDialog.setVisible( true );
    }
    
    /**
     * Creates the GUI's main panel.
     * This consists of two additional panels
     * laid out horizontally.
     * The left panel
     * (the option panel)
     * contains the options
     * that drive the application;
     * the right panel
     * (the feedback panel)
     * describes the current state
     * of the application.
     * 
     * @return  the GUI's main panel
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
     * Returns a panel
     * containing application options
     * laid out in a GridLayout
     * of 5 rows and two columns.
     * The left column
     * contains the application's options
     * (one for each subclass
     * of GraphPropertySet)
     * and the right column
     * indicates whether 
     * an image for that option
     * has been saved.
     * 
     * @return  a panel containing application options
     */
    private JPanel getOptionPanel()
    {
        Descriptor[]    descriptors =
        {
            new Descriptor( GraphPropertySetMW.class ),
            new Descriptor( GraphPropertySetTM.class ),
            new Descriptor( GraphPropertySetRM.class ),
            new Descriptor( GraphPropertySetBM.class ),
            new Descriptor( GraphPropertySetLM.class ),
        };
        
        JPanel  panel   = new JPanel( new GridLayout( 5, 2 ) );
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
     * Creates and returns
     * the feedback panel
     * to be displayed
     * in the GUI's main panel.
     * It consists of two labels
     * laid out vertically;
     * one label describes
     * the current file name,
     * and the other describes
     * the class name
     * associated with the currently selected option.
     * 
     * @return  
     *      the feedback panel to be incorporated into
     *      the GUI's main panel
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
     * Gets the control panel
     * containing the Save and Close buttons.
     * The panel uses a FlowLayout,
     * so the buttons will typically
     * be laid out horizontally.
     * 
     * @return  the GUI's control panel
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
     * Method that is activated
     * when an application option 
     * is selected.
     * The test dialog and feedback window
     * are synchronized 
     * with the selected option.
     * 
     * @param evt   event object associated with the select action
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
        
        Predicate<PRadioButton<GraphPropertySet>>   pred    = 
            b -> cName.equals( getSimpleName( b ) );
        PRadioButton<GraphPropertySet>  target  =
            gppDialog.getRBList().stream()
                .filter( pred )
                .findFirst().orElse( null );
        if ( target == null )
            throw new ComponentException( "target button not found" );
        gppDialog.doClick( target );
    }
    
    /**
     * This is the method that's executed
     * when the Save button is selected.
     * The current state
     * of the test dialog
     * is encapsulated in a GPP_TADetail
     * and written to a file.
     * 
     * @param evt   event object associated with the selection event
     */
    private void saveAction( ActionEvent evt )
    {
        PRadioButton<Descriptor>    button  = 
            buttonGroup.getSelectedButton();
        Descriptor      descrip = button.get();
        BufferedImage   image   = gppDialog.getPanelImage();
        GPP_TADetail    detail  = new GPP_TADetail( descrip.clazz, image );
        saveDetail( detail, descrip.fileName );

        descrip.saved.setSelected( true );
    }
    
    /**
     * This method obtains the simple class name
     * from a given PRadioButton<GraphPropertySet> object.
     * 
     * @param button    the given object
     * 
     * @return  the given object's simple class name
     */
    private static String 
    getSimpleName( PRadioButton<GraphPropertySet> button )
    {
        GraphPropertySet    set     = button.get();
        String              name    = set.getClass().getSimpleName();
        return name;
    }
    
    /**
     * Save a given GPP_TADetail object
     * to a file with a given name.
     * 
     * @param detail    the given detail object
     * @param fileName  the given file name
     */
    private static void 
    saveDetail( GPP_TADetail detail, String fileName )
    {
        File    dataFile    = new File( gppPath, fileName );
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
     * Simple class to encapsulate
     * the data associated with
     * a selected option.
     * The data include:
     * <ul>
     * <li>The text to display on the option's radio button;</li>
     * <li>The name of the file associated with an option;</li>
     * <li>The name of the class represented by an option;</li>
     * <li>
     *      A Boolean that indicates whether 
     *      the details of an option
     *      have been save to a file.
     * </li>
     * </ul>
     * @author Jack Straub
     */
    private static class Descriptor
    {
        public final String     text;
        public final String     fileName;
        public final String     className;
        public final JCheckBox  saved;
        public final Class<? extends GraphPropertySet>   clazz;
        
        public <T extends GraphPropertySet> Descriptor( Class<T> clazz )
        {
            final int   count       = 
                GraphPropertySet.class.getSimpleName().length();
            
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
