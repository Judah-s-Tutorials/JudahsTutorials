package com.acmemail.judah.cartesian_plane.test_utils.fb_comp;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.function.DoubleSupplier;
import java.util.function.Function;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.Border;

import com.acmemail.judah.cartesian_plane.components.Feedback;
import com.acmemail.judah.cartesian_plane.test_utils.Utils;

/**
 * This application
 * allows the test data files
 * associated with feedback components
 * to be traversed and observed.
 * It is mainly of interested
 * to personnel wishing to 
 * verify the accuracy of test data.
 * It is assumed
 * that the test files
 * were produced by {@linkplain FBCompTA}.
 * 
 * @author Jack Straub
 * 
 * @see FBCompTA
 */
public abstract class FBCompTAVisualizer
{
    /** 
     * The root directory for feedback component test data files.
     * The subdirectory provided by the user is assumed to be
     * a child to the root directory.
     * @see #FBCompTAVisualizer(String, Function)
     */
    private static final String baseSubdir      = 
        Utils.BASE_TEST_DATA_DIR + "/" + FBCompTA.FEEDBACK_DIR;
    private final File          subdir;
    private final File[]        allFiles;
    private int                 nextFileInx    = 0; 
    
    private final Feedback      feedback;
    private final JFrame        frame       = new JFrame();
    private final JLabel        expFeedback = new JLabel();
    private final JLabel        actFeedback = new JLabel();
    private final JLabel        testClass   = new JLabel();
    private final JLabel        testDir     = new JLabel();
    private final JLabel        currFile    = new JLabel();
    private final JLabel        currVal     = new JLabel();
    private final JLabel        currWeight  = new JLabel();
    private final JLabel        result      = new JLabel();
    
    private final JButton       next        = new JButton( "Next" );
    
    private BufferedImage       expImage;
    private BufferedImage       actImage;
    private float               value       = 0;
    private float               weight      = 0;
    
    /**
     * Constructor.
     * Initializes all aspects
     * of the application.
     * The user provides the name
     * of the subdirectory
     * in which data files reside;
     * this will be assumed
     * to be a child
     * of the <em>feedback</em> directory
     * which contains all subdirectories
     * associated with feedback control
     * testing data.
     * 
     * @param subdir        the given subdirectory name 
     * @param fbSupplier    the given feedback control supplier
     * 
     * @see #baseSubdir
     */
    public FBCompTAVisualizer(
        String  subdir,
        Function<DoubleSupplier,Feedback> fbSupplier
    )
    {
        File    baseDir     = new File( baseSubdir );
        this.subdir = new File( baseDir, subdir );
        if ( !this.subdir.exists() )
        {
            String  msg = 
                "Test directory doesn't exist: " + this.subdir.getName();
            System.err.println( msg );
            System.exit( 1 );
        }
        allFiles = this.subdir.listFiles( (d,f) -> f.endsWith( ".ser" ) );
        if ( allFiles.length == 0 )
        {
            String  msg = 
                "Directory has no test files: " + this.subdir.getName();
            System.err.println( msg );
            System.exit( 1 );
        }
        
        feedback = fbSupplier.apply( () -> value );
        feedback.setPreferredSize( FBCompTA.COMP_SIZE );
        makeGUI();
        nextFile();
    }
    
    /**
     * Creates and displays the application GUI.
     * It assumes that 
     * the subject feedback control
     * has already been created.
     */
    private void makeGUI()
    {
        String  title   = "Feedback Component Test Data Visualizer";
        frame.setTitle( title );
        frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        JPanel  pane    = new JPanel( new BorderLayout() );
        pane.add( getCenterPanel(), BorderLayout.CENTER );
        pane.add( getControlPanel(), BorderLayout.SOUTH );
        
        frame.setContentPane( pane );
        frame.setLocation( 200, 200 );
        frame.pack();
        frame.setVisible( true );
    }
    
    /**
     * Obtains the panel
     * to be displayed
     * in the central region
     * of the parent content pane.
     * This consists 
     * of the parameter
     * and review panels.
     * 
     * @return  
     *      the panel to be displayed in the central region
     *      of the parent content pane
     */
    private JPanel getCenterPanel()
    {
        JPanel          panel   = new JPanel();
        BoxLayout       layout  = new BoxLayout( panel, BoxLayout.X_AXIS );
        panel.setLayout( layout );
        panel.add( getParameterPanel() );
        panel.add( getReviewPanel() );
        return panel;
    }
    
    /**
     * Obtains a panel
     * displaying the parameters
     * associated with
     * the most recently processed data file.
     * 
     * @return
     *      a panel displaying the parameters associated with
     *      the most recently processed data file
     */
    private JPanel getParameterPanel()
    {
        String  simpleName  = feedback.getClass().getSimpleName();
        testClass.setText( simpleName );
        String  dirName     = subdir.getName();
        testDir.setText( dirName );
        
        JPanel  classParamPanel = 
            getParameterPanel( "Class: ", testClass );
        JPanel  dirParamPanel   = 
            getParameterPanel( "Test Dir: ", testDir );
        JPanel  fileParamPanel  = 
            getParameterPanel( "Test File: ", currFile );
        JPanel  valParamPanel   = 
            getParameterPanel( "Curr Value:  ", currVal );
        JPanel  wghtParamPanel  = 
            getParameterPanel( "Curr Weight:  ", currWeight );
        
        JPanel      masterPanel = new JPanel();
        BoxLayout   layout      = 
            new BoxLayout( masterPanel, BoxLayout.Y_AXIS );
        masterPanel.setLayout( layout );
        masterPanel.add( classParamPanel );
        masterPanel.add( dirParamPanel );
        masterPanel.add( fileParamPanel );
        masterPanel.add( valParamPanel );
        masterPanel.add( wghtParamPanel );
        
        Border  border  = 
            BorderFactory.createTitledBorder( "Parameters" );
        masterPanel.setBorder( border );
        return masterPanel;
    }
    
    /**
     * Obtains a panel
     * displaying the feedback control
     * in its current state,
     * and the expected and actual images
     * resulting from processing
     * the most recent data file.
     * 
     * @return  
     *      a panel containing the feedback component,
     *      and expected and actual images related to the 
     *      most recently processed data file
     */
    private JPanel getReviewPanel()
    {
        JPanel      panel   = new JPanel();
        BoxLayout   layout  = new BoxLayout( panel, BoxLayout.Y_AXIS );
        panel.setLayout( layout );
          Border  border  = 
          BorderFactory.createTitledBorder( "Review" );
          panel.setBorder( border );

        JPanel  fbPanel = new JPanel();
        fbPanel.add( feedback );
        panel.add( fbPanel );
        
        panel.add( getImagePanel() );

        return panel;
    }
    
    /**
     * Obtains a panel
     * displaying the expected and actual images
     * that result
     * from processing
     * the most recent data file.
     * 
     * @return
     *      a panel displaying the expected and actual images
     *      that result from processing the most recent data file
     */
    private JPanel getImagePanel()
    {
        Border      border  =
            BorderFactory.createEmptyBorder( 10, 10, 10, 10 );
        JPanel      panel   = new JPanel();
        BoxLayout   layout  = new BoxLayout( panel, BoxLayout.X_AXIS );
        panel.setLayout( layout );
        panel.setBorder( border );
        panel.add( getExpectedPanel() );
        panel.add( Box.createRigidArea( new Dimension( 3, 0 ) ) );
        panel.add( getActualPanel() );
        return panel;
    }
    
    /**
     * Constructs the panel
     * that contains the expected image.
     * 
     * @return  the panel that contains the expected image
     */
    private JPanel getExpectedPanel()
    {
        Border      border      = 
            BorderFactory.createLineBorder( Color.BLACK, 1 );
        JPanel      panel       = new JPanel();
        BoxLayout   layout      = new BoxLayout( panel, BoxLayout.Y_AXIS );
        panel.setLayout( layout );
        panel.setBorder( border );
        panel.add( new JLabel( "Expected:" ) );
        panel.add( expFeedback );
        return panel;
    }
    
    /**
     * Constructs the panel
     * that contains the actual image.
     * 
     * @return  the panel that contains the expected image
     */
    private JPanel getActualPanel()
    {
        Border      border      = 
            BorderFactory.createLineBorder( Color.BLACK, 1 );
        JPanel      panel       = new JPanel();
        BoxLayout   layout      = new BoxLayout( panel, BoxLayout.Y_AXIS );
        panel.setLayout( layout );
        panel.setBorder( border );
        panel.add( new JLabel( "Actual:" ) );
        panel.add( actFeedback );
        return panel;
    }

    /**
     * Helper method for {@linkplain #getParameterPanel()}.
     * Formulates a panel 
     * containing the label of a descriptor
     * (e.g. "Data File: ")
     * and the associated descriptor
     * (e.g. fileDescriptor).
     * 
     * @param idStr text to display in the label
     * @param right descriptor to display
     * 
     * @return
     *      panel containing the label of a descriptor
     *      and the associated descriptor
     */
    private JPanel getParameterPanel( String idStr, JLabel right )
    {
        JPanel      panel   = new JPanel();
        BoxLayout   layout  = new BoxLayout( panel, BoxLayout.X_AXIS );
        panel.setLayout( layout );
        
        int     margin  = 3;
        Border  border  = 
            BorderFactory.createEmptyBorder( 
                margin,
                margin,
                margin, 
                margin
            );
        panel.setBorder( border );
        
        JLabel  label   = new JLabel( idStr, SwingConstants.RIGHT );
        panel.add( label );
        panel.add( right );
        
        return panel;
    }
    
    /**
     * Obtains a panel
     * containing the application controls.
     * This consists of the label
     * noting the success/failure 
     * of processing 
     * the most recent data file,
     * a button to read the next data file
     * and an exit button.
     * 
     * @return  a panel containing the application controls
     */
    private JPanel getControlPanel()
    {
        JButton exit    = new JButton( "Exit" );
        next.addActionListener( e -> nextFile() );
        exit.addActionListener( e -> System.exit( 0 ) );
        
        JPanel      panel   = new JPanel();
        panel.add( result );
        panel.add( Box.createRigidArea( new Dimension( 25, 0 ) ) );
        panel.add( next );
        panel.add( exit );
        return panel;
    }
    
    /**
     * If possible, 
     * obtains an image of the feedback component
     * as it is currently displayed,
     * and sets it in the 
     * actFeedback label.
     * If the component is not displayable,
     * makes a correctly initialized blak imate
     * and sets it in the actFeedback label.
     */
    private void getActualImage()
    {
        int             width       = FBCompTA.COMP_SIZE.width;
        int             height      = FBCompTA.COMP_SIZE.height;
        int             type        = expImage.getType();
        actImage = new BufferedImage( width, height, type );
        if ( feedback.getWidth() > 0)
        {
            Graphics        graphics    = actImage.createGraphics();
            feedback.paintComponent( graphics );
        }
        
        ImageIcon       icon        = new ImageIcon( actImage );
        actFeedback.setIcon( icon );
    }
    
    /**
     * Gets and parses the next data file
     * from the list
     * of data files.
     * If all data files
     * have been processed,
     * the request is ignored.
     * If the last file is read
     * the "next" button
     * is disabled.
     * 
     * @see #allFiles
     * @see #nextFileInx
     * @see #getActualImage()
     */
    private void nextFile()
    {
        final String    fmt = "%3.1f";
        if ( nextFileInx < allFiles.length )
        {
            File            nextFile    = allFiles[nextFileInx++];
            FBCompTADetail  detail      = getDetail( nextFile );
            value = (float)detail.getPropertyValue();
            weight = (float)detail.getWeight();
            String          strWeight   = String.format( fmt, weight );
            String          strValue    = String.format( fmt, value );
            currVal.setText( strValue );
            currWeight.setText( strWeight );
            currFile.setText( nextFile.getName() );
            expImage = detail.getBufferedImage();
            
            feedback.setWeight( weight );
            expFeedback.setIcon( new ImageIcon( expImage ) );
            feedback.repaint();
            getActualImage();
            frame.pack();
            if ( nextFileInx == allFiles.length )
                next.setEnabled( false );
            
            if ( Utils.equals( expImage, actImage ) )
                result.setText( "Success" );
            else
                result.setText( "Fail" );
            result.repaint();
            System.out.println( result.getText() );
        }
    }
    
    /**
     * Reads the given data file.
     * If there's an I/O error
     * a stack trace is printed
     * and the application is terminated.
     * 
     * @param file  the given data file.
     * 
     * @return  the given data file.
     * 
     * @see #nextFile()
     */
    private FBCompTADetail getDetail( File file )
    {
        FBCompTADetail  detail  = null;
        try (
            FileInputStream fileStream = new FileInputStream( file );
            ObjectInputStream inStream = 
                new ObjectInputStream( fileStream );
        )
        {
            Object  obj     = inStream.readObject();
            if ( !(obj instanceof FBCompTADetail) )
            {
                String  className   = obj.getClass().getName();
                String  error       = 
                    "Unexpected object type: " + className;
                System.err.println( error );
                System.exit( 1 );
            }
            detail = (FBCompTADetail)obj;
        }
        catch ( IOException | ClassNotFoundException exc )
        {
            exc.printStackTrace();
            System.exit( 1 );
        }
        return detail;
    }
}
