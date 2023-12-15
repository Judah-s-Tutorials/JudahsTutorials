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
        SwingUtilities.invokeLater( () -> new GPP_TA() );
    }
    
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
    
    private static String 
    getSimpleName( PRadioButton<GraphPropertySet> button )
    {
        GraphPropertySet    set     = button.get();
        String              name    = set.getClass().getSimpleName();
        return name;
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
        Descriptor      descrip = button.get();
        BufferedImage   image   = gppDialog.getPanelImage();
        GPP_TADetail    detail  = new GPP_TADetail( descrip.clazz, image );
        saveDetail( detail, descrip.fileName );

        descrip.saved.setSelected( true );
    }
    
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
