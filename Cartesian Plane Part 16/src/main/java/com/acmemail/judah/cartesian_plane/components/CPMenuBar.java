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

public class CPMenuBar extends JMenuBar
{
    private final Window        topWindow;
    private final JDialog       lineDialog;
    private final JDialog       graphDialog;
    private final JDialog       aboutDialog;
    
    public CPMenuBar( Window topWindow )
    {
        this.topWindow = topWindow;
        lineDialog =
            LinePropertiesPanel.getDialog( topWindow );
        graphDialog =
            GraphPropertiesPanel.getDialog( topWindow );
        aboutDialog = new AboutDialog( topWindow ).getDialog();
        
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
        aboutItem.addActionListener( e -> aboutDialog.setVisible( true ) );
        
        JMenu       helpMenu        = new JMenu( "Help" );
        helpMenu.setMnemonic( KeyEvent.VK_H );
        helpMenu.add( topicsMenu );
        helpMenu.add( calcsMenu );
        helpMenu.add( aboutItem );
        return helpMenu;
    }
    
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
    
    private static void log( String str )
    {
        System.out.println( str );
    }
    
    private static class SynchVisible extends ComponentAdapter
    {
        private final AbstractButton    toggle;
        
        public SynchVisible( AbstractButton toggle )
        {
            this.toggle = toggle;
        }
        
        @Override
        public void componentHidden( ComponentEvent evt )
        {
            toggle.setSelected( false );
        }
        @Override
        public void componentShown( ComponentEvent evt )
        {
            toggle.setSelected( true );
        }
    }
    
    private static class URLDesc
    {
        public final String urlDesc;
        public final URL    url;
        
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
