package com.acmemail.judah.cartesian_plane.sandbox;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.KeyEvent;

import javax.swing.ButtonGroup;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

/**
 * This is a variation
 * on {@linkplain MenuDemo3},
 * which adds submenus
 * to the help menu.
 * 
 * @author Jack Straub
 * 
 * @see MenuDemo3
 */
public class MenuDemo5
{
    private JTextArea   textArea;
    
    /**
     * Application entry point.
     *
     * @param args command line arguments, not used
     *
    */
    public static void main(String[] args)
    {
        SwingUtilities.invokeLater( () -> new MenuDemo5().build() );
    }
    
    /**
     * Builds all elements of the GUI,
     * including a frame, menu bar and two dialogs.
     * The menu bar is incorporated
     * in the frame,
     * which is initially visible;
     * the dialogs are non-modal
     * and initially invisible.
     * The dialogs can be made visible
     * by selecting them
     * via the frame's <em>Window</em> menu.
     */
    private void build()
    {
        JFrame      frame       = new JFrame( "Menu Demo 4" );
        frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        textArea = getTextArea();
        textArea.setEditable( false );
        JScrollPane scrollPane  = new JScrollPane( textArea );

        JPanel      contentPane = new JPanel( new BorderLayout() );
        contentPane.add( scrollPane, BorderLayout.CENTER );
        contentPane.add( getMenu(), BorderLayout.NORTH );
        frame.setContentPane( contentPane );
        frame.setLocation( 200, 200 );
        frame.pack();
        frame.setVisible( true );
    }
    
    /**
     * Assembles the application's menu bar.
     * 
     * @return the application's menu bar
     */
    private JMenuBar getMenu()
    {
        JMenuBar    menuBar = new JMenuBar();
        menuBar.add( getFileMenu() );
        menuBar.add( getPropertiesMenu() );
        
        return menuBar;
    }
    
    /**
     * Assembles the application's file menu.
     * 
     * @return the application's file menu
     */
    private JMenu getFileMenu()
    {
        JMenu   menu    = new JMenu( "File" );
        menu.setMnemonic( KeyEvent.VK_F );
        JMenuItem   exitItem    = new JMenuItem( "Exit", KeyEvent.VK_E );
        
        exitItem.addActionListener( e -> System.exit( 0 ) );
        
        menu.add( exitItem );
        return menu;
    }
    
    /**
     * Assembles the application's window menu.
     * 
     * @return the application's window menu
     */
    private JMenu getPropertiesMenu()
    {
        JMenu   menu    = new JMenu( "Properties" );
        menu.setMnemonic( KeyEvent.VK_P );
        
        int     medium  = textArea.getFont().getSize();
        int     small   = medium / 2;
        int     large   = medium * 2;
        
        ButtonGroup             sizeGroup   = new ButtonGroup();
        JRadioButtonMenuItem    smallFont   =
            new JRadioButtonMenuItem( "Small Font", false );
        JRadioButtonMenuItem    mediumFont  =
            new JRadioButtonMenuItem( "Medium Font", true );
        JRadioButtonMenuItem    largeFont   =
            new JRadioButtonMenuItem( "Large Font", false );
        sizeGroup.add( smallFont );
        sizeGroup.add( mediumFont );
        sizeGroup.add( largeFont );
        
        smallFont.addActionListener( e -> changeSize( small ) );
        mediumFont.addActionListener( e -> changeSize( medium ) );
        largeFont.addActionListener( e -> changeSize( large ) );
        
        ButtonGroup             familyGroup = new ButtonGroup();
        JRadioButtonMenuItem    dialog      =
            new JRadioButtonMenuItem( "Dialog", true );
        changeFamily( Font.DIALOG );
        JRadioButtonMenuItem    monospaced  =
            new JRadioButtonMenuItem( "Monospaced", false );
        JRadioButtonMenuItem    serif       =
            new JRadioButtonMenuItem( "Serif", false );
        JRadioButtonMenuItem    sansSerif   =
            new JRadioButtonMenuItem( "Sans Serif", false );
        familyGroup.add( dialog );
        familyGroup.add( monospaced );
        familyGroup.add( serif );
        familyGroup.add( sansSerif );
        
        dialog.addActionListener( e -> changeFamily( Font.DIALOG ) );
        monospaced.addActionListener( e -> changeFamily( Font.MONOSPACED ) );
        serif.addActionListener( e -> changeFamily( Font.SERIF ) );
        sansSerif.addActionListener( e -> changeFamily( Font.SANS_SERIF ) );
        
        menu.add( smallFont );
        menu.add( mediumFont );
        menu.add( largeFont );
        menu.add( new JSeparator() );
        menu.add( dialog );
        menu.add( monospaced );
        menu.add( serif );
        menu.add( sansSerif );
        
        return menu;
    }
    
    private void changeSize( float size )
    {
        Font    newFont = textArea.getFont().deriveFont( size );
        textArea.setFont( newFont );
    }
    
    private void changeFamily( String family )
    {
        Font    font    = textArea.getFont();
        int     size    = font.getSize();
        int     style   = font.getStyle();
        Font    newFont = new Font( family, style, size );
        textArea.setFont( newFont );
    }
    
    private JTextArea getTextArea()
    {
        final String    endl    = System.lineSeparator();
        JTextArea   textArea    = new JTextArea( 24, 80 );
        textArea.append( "Wynken, Blynken, and Nod one night" + endl );
        textArea.append( "    Sailed off in a wooden shoe--" + endl );
        textArea.append( "Sailed on a river of crystal light," + endl );
        textArea.append( "    Into a sea of dew." + endl );
        textArea.append( "\"Where are you going, and what do you wish?\"" + endl );
        textArea.append( "    The old moon asked of the three." + endl );
        textArea.append( "\"We have come to fish for the herring fish" + endl );
        textArea.append( "That live in this beautiful sea;" + endl );
        textArea.append( "Nets of silver and gold have we!\"" + endl );
        textArea.append( "                  Said Wynken," + endl );
        textArea.append( "                  Blynken," + endl );
        textArea.append( "                  And Nod." + endl );
        textArea.append( "                  " + endl );
        textArea.append( "The old moon laughed and sang a song," + endl );
        textArea.append( "    As they rocked in the wooden shoe," + endl );
        textArea.append( "And the wind that sped them all night long" + endl );
        textArea.append( "    Ruffled the waves of dew." + endl );
        textArea.append( "The little stars were the herring fish" + endl );
        textArea.append( "    That lived in that beautiful sea--" + endl );
        textArea.append( "\"Now cast your nets wherever you wish--" + endl );
        textArea.append( "    Never afeard are we!\"" + endl );
        textArea.append( "    So cried the stars to the fishermen three:" + endl );
        textArea.append( "                  Wynken," + endl );
        textArea.append( "                  Blynken," + endl );
        textArea.append( "                  And Nod." + endl );
        textArea.append( "" + endl );
        textArea.append( "All night long their nets they threw" + endl );
        textArea.append( "   To the stars in the twinkling foam---" + endl );
        textArea.append( "Then down from the skies came the wooden shoe," + endl );
        textArea.append( "   Bringing the fishermen home;" + endl );
        textArea.append( "'T was all so pretty a sail it seemed" + endl );
        textArea.append( "   As if it could not be," + endl );
        textArea.append( "And some folks thought 't was a dream they 'd dreamed" + endl );
        textArea.append( "   Of sailing that beautiful sea---" + endl );
        textArea.append( "   But I shall name you the fishermen three:" + endl );
        textArea.append( "                     Wynken," + endl );
        textArea.append( "                     Blynken," + endl );
        textArea.append( "                     And Nod." + endl );
        textArea.append( "                     " + endl );
        textArea.append( "Wynken and Blynken are two little eyes," + endl );
        textArea.append( "   And Nod is a little head," + endl );
        textArea.append( "And the wooden shoe that sailed the skies" + endl );
        textArea.append( "   Is a wee one's trundle-bed." + endl );
        textArea.append( "So shut your eyes while mother sings" + endl );
        textArea.append( "   Of wonderful sights that be," + endl );
        textArea.append( "And you shall see the beautiful things" + endl );
        textArea.append( "   As you rock in the misty sea," + endl );
        textArea.append( "   Where the old shoe rocked the fishermen three:" + endl );
        textArea.append( "                     Wynken," + endl );
        textArea.append( "                     Blynken," + endl );
        textArea.append( "                     And Nod." + endl );
        textArea.setCaretPosition( 0 );
        return textArea;
    }
}
