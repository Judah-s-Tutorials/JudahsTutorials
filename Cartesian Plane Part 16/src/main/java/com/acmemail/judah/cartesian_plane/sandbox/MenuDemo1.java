package com.acmemail.judah.cartesian_plane.sandbox;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

/**
 * Application that demonstrates
 * how to create a simple menu.
 * 
 * @author Jack Straub
 * 
 * @see MenuDemo4
 */
public class MenuDemo1
{
    private static final String newLine = System.lineSeparator();
    private JTextArea   textArea;
    
    /**
     * Application entry point.
     *
     * @param args command line arguments, not used
     *
    */
    public static void main(String[] args)
    {
        SwingUtilities.invokeLater( () -> new MenuDemo1().build() );
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
        JFrame      frame       = new JFrame( "Menu Demo 1" );
        frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        textArea = new JTextArea( 24, 80 );
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
        menuBar.add( getEditMenu() );
        menuBar.add( getHelpMenu() );
        
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
        
        JMenuItem   openItem    = new JMenuItem( "Open" );
        JMenuItem   saveItem    = new JMenuItem( "Save" );
        JMenuItem   saveAsItem  = new JMenuItem( "Save As" );
        JSeparator  separator   = new JSeparator();
        JMenuItem   exitItem    = new JMenuItem( "Exit" );
        
        openItem.addActionListener( e -> log( "Open selected" ) );
        saveItem.addActionListener( e -> log( "Save selected" ) );
        saveAsItem.addActionListener( e -> log( "Save As selected" ) );
        exitItem.addActionListener( e -> System.exit( 0 ) );
        
        menu.add( openItem );
        menu.add( saveItem );
        menu.add( saveAsItem );
        menu.add( separator );
        menu.add( exitItem );
        return menu;
    }
    
    private JMenu getEditMenu()
    {
        JMenu       menu        = new JMenu( "Edit" );
        JMenuItem   copyItem    = new JMenuItem( "Copy" );
        JMenuItem   pasteItem   = new JMenuItem( "Paste" );
        JMenuItem   scaleItem   = new JMenuItem( "Scale" );
        JMenuItem   cropItem    = new JMenuItem( "Crop" );
        
        copyItem.addActionListener( e -> log( "Copy selected" ) );
        pasteItem.addActionListener( e -> log( "Paste selected" ) );
        scaleItem.addActionListener( e -> log( "Scale selected" ) );
        cropItem.addActionListener( e -> log( "Crop selected" ) );
        
        menu.add( copyItem );
        menu.add( pasteItem );
        menu.add( scaleItem );
        menu.add( cropItem );
        
        return menu;
    }
    
    /**
     * Assembles the application's help menu.
     * 
     * @return the application's help menu
     */
    private JMenu getHelpMenu()
    {
        JMenu       menu        = new JMenu( "Help" );

        JMenuItem   topicsItem  = new JMenuItem( "Topics" );
        JMenuItem   indexItem   = new JMenuItem( "Index" );
        JMenuItem   aboutItem   = new JMenuItem( "About" );
        
        topicsItem.addActionListener( e -> log( "Showing help topics" ) );
        indexItem.addActionListener( e -> log( "Showing help index" ) );
        String      about       =
            "Menu Demo 1, Version 1.0.0" + newLine
            + "Copyright \u00a9 2026 "
            + "by Solomon Mining Associates, Ltd.";
        aboutItem.addActionListener( e -> 
            JOptionPane.showMessageDialog(
                null,
                about,
                "About This Product",
                JOptionPane.INFORMATION_MESSAGE
            )
        );
        
        menu.add( topicsItem );
        menu.add( indexItem );
        menu.add( aboutItem );
        return menu;
    }
    
    /**
     * Logs a message
     * in the application's
     * text area.
     * 
     * @param message   the message to log
     */
    private void log( String message )
    {
        textArea.append( message + newLine );
    }
}
