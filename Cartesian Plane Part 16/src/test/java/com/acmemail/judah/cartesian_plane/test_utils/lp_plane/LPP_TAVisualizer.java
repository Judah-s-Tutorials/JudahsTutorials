package com.acmemail.judah.cartesian_plane.test_utils.lp_plane;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.stream.Stream;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;

import com.acmemail.judah.cartesian_plane.components.LinePropertySet;
import com.acmemail.judah.cartesian_plane.components.PButtonGroup;
import com.acmemail.judah.cartesian_plane.components.PRadioButton;
import com.acmemail.judah.cartesian_plane.graphics_utils.ComponentException;
import com.acmemail.judah.cartesian_plane.test_utils.Utils;

public class LPP_TAVisualizer
{
    private static final File       lppPath     = 
        Utils.getTestDataDir( LPP_TA.LPP_DIR );
    private final File[]            allFiles    = 
        lppPath.listFiles( f -> f.getName().endsWith( ".ser" ) );

    private final LPP_TADetail[]    allDetails  = getAllDetails();
    private final FBDialog      fbDialog;
    private final LPPTestDialog testDialog;
    
    private final PButtonGroup<Class<? extends LinePropertySet>>    buttonGroup = 
        new PButtonGroup<>();
    
    public static void main( String[] args )
    {
        SwingUtilities.invokeLater( () -> new LPP_TAVisualizer() );
    }
    
    public LPP_TAVisualizer()
    {
        testDialog = LPPTestDialog.getDialog();
        
        JFrame      frame   = new JFrame();
        frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        JPanel      pane    = new JPanel( new BorderLayout() );

        pane.add( getCenterPanel(), BorderLayout.CENTER );
        pane.add( getControlPanel(), BorderLayout.SOUTH );
        frame.setContentPane( pane );
        frame.pack();
        frame.setVisible( true );
        
        fbDialog  = new FBDialog( frame );
        
        LPP_TADetail    detail  = allDetails[0];
        buttonGroup.selectIndex( 0 );
        fbDialog.showActualImage( detail.getBufferedImage() );
        fbDialog.showExpectedImage( detail.getBufferedImage() );
        fbDialog.pack();
        fbDialog.setVisible( true );
    }
    
    private JPanel getCenterPanel()
    {
        JPanel      panel   = new JPanel();
        BoxLayout   layout  = new BoxLayout( panel, BoxLayout.Y_AXIS );
        Border      border  =
            BorderFactory.createTitledBorder( "Select Image" );
        panel.setBorder( border );
        panel.setLayout( layout );
        
        Stream.of( allFiles )
            .map( this::getRadioButton )
            .peek( b -> b.addActionListener( this::selectAction ) )
            .peek( buttonGroup::add )
            .forEach( panel::add );
        
        return panel;
    }
    
    private JPanel getControlPanel()
    {
        JPanel  panel   = new JPanel();
        JButton exit    = new JButton( "Exit" );
        exit.addActionListener( e -> System.exit( 0 ) );
        panel.add( exit );
        return panel;
    }
    
    private void selectAction( ActionEvent evt )
    {
        Object  source          = evt.getSource();
        if ( !(source instanceof PRadioButton<?>) )
            throw new ComponentException( "Invalid selection" );
        
        PRadioButton<?> button  = (PRadioButton<?>)source;
        Object          prop    = button.get();
        if ( !(prop instanceof LinePropertySet) )
            throw new ComponentException( "Invalid selection" );
        
        Class<?>    clazz   = prop.getClass();
        PRadioButton<LinePropertySet>   target  =
        testDialog.getRadioButtons().stream()
            .filter( b -> b.getClass() == clazz )
            .findFirst().orElse( null );
        if ( target == null )
            throw new ComponentException( "PRadioButton not found" );
        testDialog.doClick( target );
        
        BufferedImage   image   = testDialog.getPanelImage();
        fbDialog.showActualImage( image );
    }
    
    private PRadioButton<Class<? extends LinePropertySet>>
    getRadioButton( File file )
    {
        LPP_TADetail    detail  = getDetail( file );
        String          text    = file.getName();
        PRadioButton<Class<? extends LinePropertySet>>  button  =
            new PRadioButton<>( detail.getLPPType(), text );
        return button;
    }
    
    private LPP_TADetail[] getAllDetails()
    {
        File[]  allFiles    = 
            lppPath.listFiles( f -> f.getName().endsWith( ".ser" ) );
        if ( allFiles.length == 0 )
        {
            System.out.println( "no test data files found" );
            System.out.println( lppPath.getPath() );
            System.exit( 1 );
        }
        
        LPP_TADetail[]  details =
        Stream.of( allFiles )
            .map( this::getDetail )
            .toArray( LPP_TADetail[]::new );
        return details;
    }
    
//    private PRadioButton<?> getRadioButton( String name )
//    {
//        String  upperName   = name.toUpperCase();
//        Predicate<PRadioButton<LinePropertySet>>    pr?
//        testDialog.getRadioButtons().stream()
//            .map( b -> new Object[] { b, )
//    }
    
    private boolean 
    testName( PRadioButton<LinePropertySet> button, String name )
    {
        String  ucName      = name.toUpperCase();
        String  className   = 
            button.get().getClass().getName().toUpperCase();
        return false;
    }
    
    private LPP_TADetail getDetail( File file )
    {
        LPP_TADetail    detail  = null;
        try (
            FileInputStream fStream = new FileInputStream( file );
            ObjectInputStream oStream = new ObjectInputStream( fStream );
        )
        {
            Object  obj = oStream.readObject();
            if ( !(obj instanceof LPP_TADetail) )
            {
                String  actualName  = obj.getClass().getSimpleName();
                String  message     =
                    "Expected type: int[][], actual type: " + actualName;
                throw new ClassNotFoundException( message );
            }
            detail = (LPP_TADetail)obj;
        }
        catch ( IOException | ClassNotFoundException exc )
        {
            exc.printStackTrace();
            System.exit( 1 );
        }
        
        return detail;
    }
    
    @SuppressWarnings("serial")
    private static class FBDialog extends JDialog
    {
        private static final String title   = "LPP Visualizer Status";
        private final JLabel    actLabel    = new JLabel();
        private final JLabel    expLabel    = new JLabel();
        private final JLabel    statusLabel = new JLabel( "Status" );
        
        public FBDialog( JFrame parent )
        {
            super( parent, title );
            setContentPane( getMainPanel() );
            setLocation( 200, 200 );
            pack();
        }
        
        private JPanel getMainPanel()
        {
            JPanel  panel   = new JPanel( new BorderLayout() );
            panel.add( getExpPanel(), BorderLayout.NORTH );
            panel.add( getActPanel(), BorderLayout.CENTER );
            statusLabel.setHorizontalAlignment( SwingUtilities.CENTER );
            panel.add( statusLabel, BorderLayout.SOUTH );
            
            Border  outer   =
                BorderFactory.createEmptyBorder( 3, 3, 3, 3 );
            Border  inner   = 
                BorderFactory.createLineBorder( Color.BLACK, 3); 
            Border  border  =
                BorderFactory.createCompoundBorder( outer, inner );
            panel.setBorder( border );
            return panel;
        }
        
        private JPanel getExpPanel()
        {
            final String title = "Expected Image";
            Border  outer   = 
                BorderFactory.createEmptyBorder( 5, 5, 2, 5 );
            Border  inner   = 
                BorderFactory.createTitledBorder( title );
            Border  border  =
                BorderFactory.createCompoundBorder( outer, inner );
            JPanel  panel   = new JPanel();
            panel.setBorder( border );
            panel.add( expLabel );
            return panel;
        }
        
        private JPanel getActPanel()
        {
            final String title = "Actual Image";
            Border  outer   = 
                BorderFactory.createEmptyBorder( 2, 5, 5, 5 );
            Border  inner   = 
                BorderFactory.createTitledBorder( title );
            Border  border  =
                BorderFactory.createCompoundBorder( outer, inner );
            JPanel  panel   = new JPanel();
            panel.setBorder( border );
            panel.add( actLabel );
            return panel;
        }
        
        public void showActualImage( BufferedImage image )
        {
            actLabel.setIcon( getIcon( image ) );
            pack();
        }
        
        public void showExpectedImage( BufferedImage image )
        {
            expLabel.setIcon( getIcon( image ) );
            pack();
        }
        
        public void showStatus( String status )
        {
            statusLabel.setText( status );
        }
        
        private static ImageIcon getIcon( BufferedImage image )
        {
            final double scale = .5;
            int         newWidth    = 
                (int)(image.getWidth() * scale + .5);
            int         newHeight   = 
                (int)(image.getHeight() * scale + .5);
            int         hint        = Image.SCALE_REPLICATE;
            Image       scaledImage = 
                image.getScaledInstance( newWidth, newHeight, hint );
            ImageIcon   icon        = new ImageIcon( scaledImage );
            return icon;
        }
    }
}
