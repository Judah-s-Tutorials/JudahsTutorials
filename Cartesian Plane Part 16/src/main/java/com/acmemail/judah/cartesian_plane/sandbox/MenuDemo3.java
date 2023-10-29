package com.acmemail.judah.cartesian_plane.sandbox;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;

/**
 * Application that demonstrates
 * how to create a simple menu.
 * 
 * @author Jack Straub
 * 
 * @see MenuDemo4
 */
public class MenuDemo3
{
    private static final String newLine = System.lineSeparator();
    private JTextArea   textArea;
    private JDialog     graphDialog;
    private JDialog     lineDialog;
    
    /**
     * Application entry point.
     *
     * @param args command line arguments, not used
     *
    */
    public static void main(String[] args)
    {
        SwingUtilities.invokeLater( () -> new MenuDemo3().build() );
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
        JFrame      frame       = new JFrame( "Menu Demo 3" );
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
        
        graphDialog = getDialog( 
            frame,
            "Set Graph Properties", 
            "GRAPH PROPERTIES"
        );
        graphDialog.setLocation( 100, 100 );
        
        lineDialog = getDialog( 
            frame,
            "Set Line Properties", 
            "LINE PROPERTIES"
        );
        lineDialog.setLocation( 300, 100 );
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
        menuBar.add( getWindowMenu() );
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
        menu.setMnemonic( KeyEvent.VK_F );
        
        JMenuItem   openItem    = new JMenuItem( "Open", KeyEvent.VK_O );
        JMenuItem   saveItem    = new JMenuItem( "Save", KeyEvent.VK_S );
        JMenuItem   saveAsItem  = new JMenuItem( "Save As", KeyEvent.VK_A );
        JSeparator  separator   = new JSeparator();
        JMenuItem   exitItem    = new JMenuItem( "Exit", KeyEvent.VK_E );
        
        KeyStroke   ctrlS       =
            KeyStroke.getKeyStroke( KeyEvent.VK_S, ActionEvent.CTRL_MASK );
        saveItem.setAccelerator( ctrlS );
        
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
    
    /**
     * Assembles the application's edit menu.
     * 
     * @return  the application's edit menu
     */
    private JMenu getEditMenu()
    {
        JMenu       menu        = new JMenu( "Edit" );
        menu.setMnemonic( KeyEvent.VK_E );
        JMenuItem   copyItem    = 
            new JMenuItem( "Copy", KeyEvent.VK_C );
        JMenuItem   pasteItem   = 
            new JMenuItem( "Paste", KeyEvent.VK_P );
        JMenuItem   scaleItem   = 
            new JMenuItem( "Scale", KeyEvent.VK_L );
        JMenuItem   cropItem    = 
            new JMenuItem( "Crop", KeyEvent.VK_R );
        
        copyItem.addActionListener( e -> log( "Copy selected" ) );
        pasteItem.addActionListener( e -> log( "Paste selected" ) );
        scaleItem.addActionListener( e -> log( "Scale selected" ) );
        cropItem.addActionListener( e -> log( "Crop selected" ) );
        
        KeyStroke   ctrlL       =
            KeyStroke.getKeyStroke( KeyEvent.VK_L, ActionEvent.CTRL_MASK );
        scaleItem.setAccelerator( ctrlL );
        KeyStroke   ctrlR       =
            KeyStroke.getKeyStroke( KeyEvent.VK_R, ActionEvent.CTRL_MASK );
        cropItem.setAccelerator( ctrlR );

        menu.add( copyItem );
        menu.add( pasteItem );
        menu.add( scaleItem );
        menu.add( cropItem );
        
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
        menu.add( graphItem );
        menu.add( lineItem );
        
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
        menu.setMnemonic( KeyEvent.VK_H );

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
    
    /**
     * Creates a non-modal dialog
     * containing text
     * and a button.
     * Pushing the button
     * closes the dialog.
     * 
     * @param owner the owner of the dialog
     * @param title the dialog title
     * @param text  the text to display in the dialog
     * 
     * @return  the created dialog
     */
    private JDialog getDialog( JFrame owner, String title, String text )
    {
        JDialog dialog      = new JDialog( owner, title );
        JPanel  contentPane = new JPanel( new BorderLayout() );
        String  labelText   = 
            "<html><p style="
            + "'font-size: 300%;'>" 
            + text
            + "</p></html>";
        JLabel  label       = new JLabel( labelText );
        JPanel  centerPanel = new JPanel();
        Border  outer       =
            BorderFactory.createBevelBorder( BevelBorder.RAISED );
        Border  inner       = 
            BorderFactory.createEmptyBorder( 15, 15, 15, 15 );
        Border  border      =
            BorderFactory.createCompoundBorder( outer, inner );
        centerPanel.setBorder( border );
        centerPanel.add( label );
        contentPane.add( centerPanel, BorderLayout.CENTER );
        
        JPanel  buttonPanel = new JPanel();
        JButton closeButton = new JButton( "Close" );
        closeButton.addActionListener( e -> dialog.setVisible( false ) );
        buttonPanel.add( closeButton );
        contentPane.add( buttonPanel, BorderLayout.SOUTH );
        dialog.setContentPane( contentPane );
        dialog.pack();
        
        return dialog;
    }
}
