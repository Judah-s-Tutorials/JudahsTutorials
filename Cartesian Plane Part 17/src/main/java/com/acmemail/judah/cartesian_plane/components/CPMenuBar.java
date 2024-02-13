package com.acmemail.judah.cartesian_plane.components;

import java.awt.Desktop;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.stream.Stream;

import javax.swing.AbstractButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JDialog;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;

/**
 * The menu bar for the main window
 * of the Cartesian plane application.
 * 
 * @author Jack Straub
 */
@SuppressWarnings("serial")
public class CPMenuBar extends JMenuBar
{
    /** The owner of this dialog. */
    private final Window        topWindow;
    /** Dialog to configure line properties in the Cartesian plane. */
    private final JDialog       lineDialog;
    /** Dialog to configure graph properties in the Cartesian plane. */
    private final JDialog       graphDialog;
    /** Dialog to show "about application" page. */
    private final AboutDialog   aboutDialog;
    
    /**
     * Constructor.
     * Fully configures the application menu bar.
     * 
     * @param topWindow 
     *      top-level window to use as parent of dialogs
     *      launched by this menu bar; may be null
     */
    public CPMenuBar( Window topWindow )
    {
        this.topWindow = topWindow;
        lineDialog =
            LinePropertiesPanel.getDialog( topWindow );
        graphDialog =
            GraphPropertiesPanel.getDialog( topWindow );
        aboutDialog = new AboutDialog( topWindow );
        
        add( getFileMenu() );
        add( getWindowMenu() );
        add( configHelpMenu() );
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
        
        JMenuItem   open    = new JMenuItem( "Open", KeyEvent.VK_O );
        JMenuItem   save    = new JMenuItem( "Save", KeyEvent.VK_S );
        JMenuItem   saveAs  = new JMenuItem( "Save As", KeyEvent.VK_A );
        JMenuItem   exit    = new JMenuItem( "Exit", KeyEvent.VK_X );
        
        KeyStroke   ctrlS       =
            KeyStroke.getKeyStroke( KeyEvent.VK_S, ActionEvent.CTRL_MASK );
        save.setAccelerator( ctrlS );
        
        open.addActionListener( e -> log( "Open selected" ) );
        save.addActionListener( e -> log( "Save selected" ) );
        saveAs.addActionListener( e -> log( "Save As selected" ) );
        exit.addActionListener( e -> System.exit( 0 ) );
        
        menu.add( open );
        menu.add( save );
        menu.add( saveAs );
        menu.add( exit );
        
        // Not ready to be connected
        open.setEnabled( false );
        save.setEnabled( false );
        saveAs.setEnabled( false );
        
        return menu;
    }
    
    /**
     * Assembles the application's window menu.
     * 
     * @return the application's window menu
     */
    private JMenu getWindowMenu()
    {
        JMenu   menu    = new JMenu( "Window" );
        menu.setMnemonic( KeyEvent.VK_W );
        
        JCheckBoxMenuItem   graphItem   =
            new JCheckBoxMenuItem( "Edit Graph Properties", false );
        JCheckBoxMenuItem   lineItem    =
            new JCheckBoxMenuItem( "Edit Line Properties", false );
        graphItem.addItemListener( e -> 
            graphDialog.setVisible( graphItem.isSelected() )
        );
        lineItem.addItemListener( e -> 
            lineDialog.setVisible( lineItem.isSelected() )
        );
        lineDialog.addComponentListener( new SynchVisible( lineItem ) );
        graphDialog.addComponentListener( new SynchVisible( graphItem ) );
        menu.add( graphItem );
        menu.add( lineItem );
        
        return menu;
    }
    
    /**
     * Assembles the application's help menu.
     * 
     * @return the application's help menu
     */
    private JMenu configHelpMenu()
    {
        JMenu       topicsMenu  = getMathTopicsMenu();
        JMenu       calcsMenu   = getCalculatorsMenu();
        JMenuItem   aboutItem   = new JMenuItem( "About", KeyEvent.VK_A );
        aboutItem.addActionListener( e -> aboutDialog.showDialog( true ) );
        
        JMenu       helpMenu        = new JMenu( "Help" );
        helpMenu.setMnemonic( KeyEvent.VK_H );
        helpMenu.add( topicsMenu );
        helpMenu.add( calcsMenu );
        helpMenu.add( aboutItem );
        return helpMenu;
    }
    
    /**
     * Configures the "help topics" submenu.
     * 
     * @return  the "math topics" submenu.
     */
    private JMenu getMathTopicsMenu()
    {
        URLDesc[]    siteDescs   =
        {
            new URLDesc( "https://www.mathsisfun.com/", "Math is Fun" ),
            new URLDesc( "https://www.wolframalpha.com/", "Wolfram Alpha" ),
            new URLDesc( "https://www.khanacademy.org/", "Khan Academy" )
        };
        JMenu   menu    = new JMenu( "Math Help" );
        menu.setMnemonic( KeyEvent.VK_M );
        Stream.of( siteDescs )
            .forEach( u -> {
                JMenuItem   item    = new JMenuItem( u.urlDesc );
                item.addActionListener( e -> activateLink( u.url ) );
                menu.add( item );
            });
        
        return menu;
    }
    
    /**
     * Configures the "calculators" submenu.
     * 
     * @return  the "calculators" submenu.
     */
    private JMenu getCalculatorsMenu()
    {
        URLDesc[]    siteDescs   =
        {
            new URLDesc( "https://web2.0calc.com/", "web2.0calc" ),
            new URLDesc( "https://www.desmos.com/", "Desmos" ),
            new URLDesc( "https://www.calculator.net/", "Calculator.net" )
        };
        JMenu   menu    = new JMenu( "Calculators" );
        menu.setMnemonic( KeyEvent.VK_C );
        Stream.of( siteDescs )
            .forEach( u -> {
                JMenuItem   item    = new JMenuItem( u.urlDesc );
                item.addActionListener( e -> activateLink( u.url ) );
                menu.add( item );
            });
        
        return menu;
    }

    /**
     * Opens a URL in the host's default browser.
     * If an error occurs
     * the error message
     * is displayed in a modal dialog;
     * the operation is otherwise ignored.
     * 
     * @param url   the URL to open
     */
    private void activateLink( URL url )
    {
        Desktop desktop = Desktop.getDesktop();
        try
        {
            desktop.browse( url.toURI() );
        } 
        catch ( IOException | URISyntaxException exc )
        {
            exc.printStackTrace();
            JOptionPane.showMessageDialog(
                topWindow, 
                exc.getMessage(),
                "Link Error",
                JOptionPane.ERROR_MESSAGE,
                null
            );
        }
    }
    
    /**
     * Temporary facility to simulate 
     * as-of-yet implemented functionality.
     * Prints a given string to stdout.
     * 
     * @param str   the given string
     */
    private static void log( String str )
    {
        System.out.println( str );
    }
    
    /**
     * ComponentListener for synchronizing the state of a toggle button
     * with the visibility of a window.
     * The window is typically a dialog.
     * For example,
     * if the line properties dialog loses visibility,
     * the associated menu checkbox item
     * will be set to false.
     * 
     * @author Jack Straub
     */
    private static class SynchVisible extends ComponentAdapter
    {
        /** The button associated with this object. */
        private final AbstractButton    toggle;
        
        /**
         * Constructor.
         * Establishes the button whose state
         * is to be synchronized
         * with the visibility of some window.
         * 
         * @param toggle
         *      the button whose state is to be synchronized
         *      with the visibility of some window
         */
        public SynchVisible( AbstractButton toggle )
        {
            this.toggle = toggle;
        }
        
        /**
         * Method to be invoked when a window loses visibility.
         */
        @Override
        public void componentHidden( ComponentEvent evt )
        {
            toggle.setSelected( false );
        }
        
        /**
         * Method to be invoked when a window becomes visible.
         */
        @Override
        public void componentShown( ComponentEvent evt )
        {
            toggle.setSelected( true );
        }
    }
    
    /**
     * Establishes a correspondence
     * between a description of a URL,
     * and an object of type <em>class URL.</em>
     * The description is typically used
     * as the text of a menu item,
     * and the object is used to activate the URL
     * when the menu item is selected.
     * 
     * @author Jack Straub
     */
    private static class URLDesc
    {
        /** String representation of the encapsulated URL. */
        public final String urlDesc;
        /** Object representation of the encapsulated URL. */
        public final URL    url;
        
        /**
         * Constructor.
         * Establishes the description of the encapsulated URL
         * and the associated URL object.
         * 
         * @param urlStr    the URL object, formatted as a string
         * @param desc      the description of the URL
         */
        public URLDesc( String urlStr, String desc )
        {
            URL temp    = null;
            try
            {
                temp = new URL( urlStr );
            }
            catch ( MalformedURLException exc )
            {
                exc.printStackTrace();
                System.exit( 1 );
            }
            url = temp;
            urlDesc = desc;
        }
    }
}
