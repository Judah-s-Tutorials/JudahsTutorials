package com.acmemail.judah.cartesian_plane.sandbox.layout_managers;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.util.stream.Stream;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

import com.acmemail.judah.cartesian_plane.sandbox.SandboxUtils;

public class BorderLayoutDemo3
{
    private static final String endl        = System.lineSeparator();
    private static JTextArea    console; 
    private static JTextArea    editor;
    
    public static void main(String[] args)
    {
        SwingUtilities.invokeLater( () -> buildGUI() );
    }

    private static void buildGUI()
    {
        JFrame  frame   = new JFrame( "FlowLayout Demo" );
        frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        JPanel  panel   = new JPanel( new BorderLayout() );
        
        panel.add( getMenuBar(), BorderLayout.NORTH );
        panel.add( getEditor(), BorderLayout.CENTER );
        panel.add( getConsole(), BorderLayout.SOUTH );
        panel.add( getLeftPanel(), BorderLayout.WEST );
        
        frame.setContentPane( panel );
        frame.pack();
        SandboxUtils.center( frame );
        frame.setVisible( true );
        
        console.append( newLine( "GUI initialized..." ) );
    }
    
    private static JComponent getLeftPanel()
    {
        JPanel      panel   = new JPanel();// new GridLayout( 3, 1 ) );
        BoxLayout   layout  = new BoxLayout( panel, BoxLayout.Y_AXIS );
        panel.setLayout( layout );
//        panel.setBackground( Color.LIGHT_GRAY );
        
        JButton     save    = new JButton( "Save" );
        JButton     clear   = new JButton( "Clear" );
        JButton     exit    = new JButton( "Exit" );
        
        panel.add( save );
        panel.add( clear );
        panel.add( exit );
        
        save.addActionListener( e -> 
            console.append( newLine( "Saving text..." ) )
        );
        clear.addActionListener( e -> {
            editor.setText( "" );
            console.append( newLine( "Clearing text..." ) );
        });
        exit.addActionListener( e -> {
            console.append( newLine( "Exiting..." ) );
            System.exit( 0 );
        });
        return panel;
    }
    
    private static JComponent getMenuBar()
    {
        // There's a lot more than this to making a menu bar, but for
        // the purpose of this demo it will suffice.
        JMenuBar    menuBar = new JMenuBar();
        Stream.of( "File", "Edit", "Window", "Help" )
            .map( JMenu::new )
            .forEach( menuBar::add );
        return menuBar;
    }
    
    private static JComponent getConsole()
    {
        JScrollPane pane    = new JScrollPane();
        console = new JTextArea( 5, 50 );
        pane.setViewportView( console );
        console.setBackground( Color.LIGHT_GRAY );
        return pane;
    }
    
    private static JComponent getEditor()
    {
        editor = new JTextArea( 15, 20 );
        JScrollPane pane    = new JScrollPane();
        Font        font    = editor.getFont().deriveFont( 16f );
        pane.setViewportView( editor );
        editor.setBackground( Color.WHITE );
        editor.setFont( font );
        StringBuilder   bldr    = new StringBuilder()
            .append( newLine( "The sun was shining on the sea," ) )
            .append( newLine( "Shining with all his might;" ) )
            .append( newLine( "He did his very best to make" ) )
            .append( newLine( "The billows smooth and brght -" ) )
            .append( newLine( "And this was odd, because it was" ) )
            .append( newLine( "The middle of the night." ) );
        editor.setText( bldr.toString() );
        return pane;
    }
    
    private static String newLine( String text )
    {
        return text + endl;
    }
}
