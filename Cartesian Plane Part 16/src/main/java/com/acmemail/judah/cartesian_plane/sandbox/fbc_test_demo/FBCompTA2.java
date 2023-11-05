package com.acmemail.judah.cartesian_plane.sandbox.fbc_test_demo;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;

import com.acmemail.judah.cartesian_plane.components.Feedback;
import com.acmemail.judah.cartesian_plane.components.SpacingFeedback;

public class FBCompTA2
{
    /** The path to the directory for storing test data. */
    private static final String dataPath    =
        "TestDataDir/Feedback/Spacing";

    // #2
    /** The name of the file used to store test data. */
    private static final String dataFile    = "SpacingData.ser";
    /** The path to the file used to store test data. */
    private final File      filePath        = makeFilePath();
    /** The dimensions of the feedback component. */
    private final Dimension compSize        = new Dimension( 100, 25 );
    
    /**
     * Contains the current property value to be applied to the feedback
     * component. This value is updated every time the Next button
     * is pushed.
     * @see #nextActionPerformed(ActionEvent)
     */
    private double          currVal             = 1;
    // #3
    /** The feedback component undergoing test. */
    private final Feedback          feedback    = 
        new SpacingFeedback( () -> currVal );
    
    /**
     * Application entry point.
     *
     * @param args command line arguments, not used
     *
    */
    public static void main(String[] args)
    {
        SwingUtilities.invokeLater( () -> 
            new FBCompTA2()
        );
    }
    
    /**
     * Constructor;
     * creates and shows the GUI.
     */
    public FBCompTA2()
    {
        feedback.setPreferredSize( compSize );
        
        // #1
        String  title   = "Spacing Feedback Component Test Assistant";
        JFrame  frame   = new JFrame( title );
        frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        JPanel  contentPane = new JPanel( new BorderLayout() );
        contentPane.add( getButtonPanel(), BorderLayout.SOUTH );
        
        frame.setContentPane( contentPane );
        frame.pack();
        frame.setLocation( 100, 100 );;
        frame.setVisible( true );
        
        makeComponentDialog( feedback );
//        compDialog.setVisible( true );
    }
    
    /**
     * Gets a File object
     * representing the path
     * to the data file
     * that is expected
     * to be created 
     * by this application.
     * 
     * @return  
     *      File representing the data file
     *      created by this application
     */
    public static File getDataFile()
    {
        String  path    = dataPath + "/" + dataFile;
        File    file    = new File( path );
        return file;
    }
    
    private File makeFilePath()
    {
        File    data    = new File( dataPath );
        File    path    = new File( dataPath, dataFile );
        if ( !data.exists() )
        {
            if ( !data.mkdirs() )
                System.out.println( "make " + dataPath + " failed" );
            else
                System.out.println( "path: " + path.getAbsolutePath() );
        }
        
        return path;
    }
    
    /**
     * Creates the panel
     * containing the buttons
     * for controlling
     * this application.
     * 
     * @return  the panel containing this application's control buttons
     */
    private JPanel getButtonPanel()
    {
        JButton     next        = new JButton( "Next" );
        JButton     save        = new JButton( "Save" );
        JButton     exit        = new JButton( "Exit" );
        
        next.addActionListener( this::nextActionPerformed );
        save.addActionListener( this::saveActionPerformed );
        exit.addActionListener( e -> System.exit( 0 ) );
        
        JPanel      panel       = new JPanel();
        BoxLayout   layout      = new BoxLayout( panel, BoxLayout.X_AXIS );
        panel.setLayout( layout );
        panel.add( next );
        panel.add( save );
        panel.add( exit );
        
        return panel;
    }
    
    /**
     * Method to be invoked
     * when the Next button 
     * is pushed.
     * The value
     * controlling the feedback component
     * is incremented,
     * and the feedback component
     * is repainted.
     * 
     * @param evt   event associated with the button push;
     *              not used.
     */
    private void nextActionPerformed( ActionEvent evt )
    {
        ++currVal;
        feedback.repaint();
    }
    
    /**
     * Method to be invoked
     * when the Save button
     * is pushed.
     * A detail object
     * encapsulating the configuration
     * of the feedback component
     * is created
     * and written to
     * the data file.
     * I/O errors 
     * are recorded to stderr
     * but otherwise ignored.
     * 
     * @param evt   
     *      event object generated when the save button 
     *      is pushed; not used
     */
    private void saveActionPerformed( ActionEvent evt )
    {
        BufferedImage   image   = getBufferedImage();
        FBCompTADetail  detail  = 
            new FBCompTADetail( currVal, -1, image );
        
        try ( 
            FileOutputStream fileStream = 
                new FileOutputStream( filePath );
            ObjectOutputStream outStream = 
                new ObjectOutputStream( fileStream );
        )
        {
            outStream.writeObject( detail );
            String  dest    = filePath.getAbsolutePath();
            System.out.println( "Wrote object " + dest );
        }
        catch ( IOException exc )
        {
            exc.printStackTrace();
        }
    }
    
    /**
     * Obtains a BufferedImage
     * containing the rendered
     * feedback component.
     * 
     * @return 
     *      a BufferedImage containing 
     *      the rendered feedback component
     */
    private BufferedImage getBufferedImage()
    {
        int             type        = BufferedImage.TYPE_INT_RGB;
        BufferedImage   image       = 
            new BufferedImage( compSize.width, compSize.height, type );
        Graphics        graphics    = image.createGraphics(); 
        feedback.paintComponent( graphics );
        return image;
    }
    
    /**
     * Creates a dialog
     * that displays
     * the rendered
     * feedback component.
     * 
     * @param component 
     *      the feedback component to be displayed in the dialog
     */
    private void makeComponentDialog( JComponent component )
    {
        JDialog   compDialog    = new JDialog();
        String      title       = "Component Display";
        compDialog.setTitle( title );
        
        Border  border      =
            BorderFactory.createEmptyBorder( 10, 10, 10, 10 );
        JPanel  contentPane = new JPanel();
        contentPane.setBorder( border );
        contentPane.add( component );
        compDialog.setContentPane( contentPane );
        compDialog.setLocation( 200, 200 );
        compDialog.pack();
        compDialog.setVisible( true );
    }
}
