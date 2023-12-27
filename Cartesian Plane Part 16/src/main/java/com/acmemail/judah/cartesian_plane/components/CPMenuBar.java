package com.acmemail.judah.cartesian_plane.components;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.KeyStroke;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.StyleSheet;

import com.acmemail.judah.cartesian_plane.sandbox.SynchVisible;

public class CPMenuBar extends JMenuBar
{
    private static final String newLine = System.lineSeparator();

    private final Window                topWindow;
    private final JDialog               lineDialog;
    private final JDialog               graphDialog;
    private final ModalMessageDialog    modalMessageDialog;
    
    public CPMenuBar( Window topWindow )
    {
        this.topWindow = topWindow;
        lineDialog =
            LinePropertiesPanel.getDialog( topWindow );
        graphDialog   =
            GraphPropertiesPanel.getDialog( topWindow );
        modalMessageDialog = new ModalMessageDialog();
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
        menu.add( exitItem );
        return menu;
    }
    
    private void showDialog( JCheckBoxMenuItem item, JDialog dialog )
    {
        dialog.setVisible( item.isSelected() );
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
        JMenuItem   topicsItem      = new JMenuItem( "Topics" );
        JMenuItem   aboutItem       = new JMenuItem( "About" );
        
        topicsItem.addActionListener( e -> log( "Showing help topics" ) );
        String      about       =
            "Menu Demo 2, Version 1.0.0" + newLine
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
        
        // Create the index submenu
        JMenu       indexMenu       = new JMenu( "Index" );
        JMenuItem   indexItemA  = new JMenuItem( "A-F" );
        JMenuItem   indexItemG  = new JMenuItem( "G-L" );
        JMenuItem   indexItemM  = new JMenuItem( "M-R" );
        JMenuItem   indexItemS  = new JMenuItem( "S-Z" );
        indexItemA.addActionListener( this::actionPerformed );
        indexItemG.addActionListener( this::actionPerformed );
        indexItemM.addActionListener( this::actionPerformed );
        indexItemS.addActionListener( this::actionPerformed );
        indexMenu.add( indexItemA );
        indexMenu.add( indexItemG );
        indexMenu.add( indexItemM );
        indexMenu.add( indexItemS );
        
        // Create the quick-reference submenu
        JMenu       quickRefMenu    = new JMenu( "Quick Reference" );
        JMenuItem   quickRefItem1   = 
            new JMenuItem( "World Domination, How To" );
        JMenuItem   quickRefItem2   = 
            new JMenuItem( "Thermonuclear Annihilation, Avoiding" );
        JMenuItem   quickRefItem3   = 
            new JMenuItem( "Natural Resources, Plundering" );
        quickRefItem1.addActionListener( this::actionPerformed );
        quickRefItem2.addActionListener( this::actionPerformed );
        quickRefItem3.addActionListener( this::actionPerformed );
        quickRefMenu.add( quickRefItem1 );
        quickRefMenu.add( quickRefItem2 );
        quickRefMenu.add( quickRefItem3 );
        
        // Create the principal help menu
        JMenu       menu            = new JMenu( "Help" );
        menu.setMnemonic( KeyEvent.VK_H );
        menu.add( topicsItem );
        menu.add( indexMenu );
        menu.add( quickRefMenu );
        menu.add( aboutItem );
        return menu;
    }
    
    private void actionPerformed( ActionEvent evt )
    {
        Object  source  = evt.getSource();
        if ( source instanceof AbstractButton )
        {
            String  text    = ((AbstractButton)source).getText();
            log( "Selected \"" + text + "\"" );
        }
    }

    private void showModalMessageDialog( String str )
    {
        JOptionPane.showMessageDialog( topWindow, str );
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
    
    private static class ModalMessageDialog extends JDialog
    {
        /** HTML/CSS-aware component for displaying text. */
        private final JEditorPane   textPane    = 
            new JEditorPane( "text/html", "" );
        private final StyleSheet    styleSheet;
        
        /** CSS for configuring body element. */
        private static final String bodyRule    = 
            "body {"
            + "margin-left: 2em;"
            + "font-family: Arial, Helvetica, sans-serif;"
            + " font-size:"
            + " 14;"
            + " min-width: 70em;"
            + " white-space: nowrap;}";

        public ModalMessageDialog()
        {
            JScrollPane scrollPane  = new JScrollPane( textPane );
            Dimension   dim         = new Dimension( 300, 150 );
            scrollPane.setPreferredSize( dim );
            
            HTMLEditorKit   kit         = new HTMLEditorKit();
            textPane.setEditorKit( kit );
            styleSheet  = kit.getStyleSheet();
            styleSheet.addRule( bodyRule );

            JPanel  contentPane = new JPanel( new BorderLayout() );
            contentPane.add( scrollPane, BorderLayout.CENTER );
            setContentPane( contentPane );
            pack();
        }
        
        public void setText( String text )
        {
            textPane.setText( text );
        }
        
        public void setCSS( String css )
        {
//            textPane.sty
        }
    }
}
