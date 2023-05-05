package com.acmemail.judah.cartesian_plane.sandbox;

import java.awt.BorderLayout;
import java.awt.Dialog;
import java.io.File;
import java.util.Map;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import com.acmemail.judah.cartesian_plane.CartesianPlane;
import com.acmemail.judah.cartesian_plane.app.CommandExecutor;
import com.acmemail.judah.cartesian_plane.app.DialogInputApp;
import com.acmemail.judah.cartesian_plane.graphics_utils.Root;
import com.acmemail.judah.cartesian_plane.input.Command;
import com.acmemail.judah.cartesian_plane.input.CommandReader;
import com.acmemail.judah.cartesian_plane.input.Equation;
import com.acmemail.judah.cartesian_plane.input.EquationMap;
import com.acmemail.judah.cartesian_plane.input.ParsedCommand;

public class JDialogDemo
{
    public static final int OK_STATUS       = 0;
    public static final int CANCEL_STATUS   = 1;
        
    private final Map<String,Equation>   equationMap;

    private final JDialog dialog;
    private JList<String> jList;

    private int closeStatus     = -1;
    private int selectedItem    = -1;
    
    public static void main(String[] args)
    {
        JDialogDemo demo    = new JDialogDemo();
        demo.setVisible( false );
        System.out.println( demo.closeStatus );
        System.out.println( demo.selectedItem );
        
        if ( demo.getStatus() == OK_STATUS )
        {
            Equation    equation    = demo.getSelectedEquation();
            process( equation );
        }
        System.exit( 0 );
    }
    
    private static void process( Equation equation )
    {
        CartesianPlane  plane   = new CartesianPlane();
        Root            root    = new Root( plane );
        root.start();
        
        CommandExecutor executor    = new CommandExecutor( plane );
        executor.exec( JDialogDemo::getInput, equation );
    }
    
    private static ParsedCommand getInput()
    {
        ParsedCommand   command = null;
        while ( command == null )
        {
            String  input   =
                JOptionPane.showInputDialog(
                    null, 
                    "Enter a command", 
                    "Command Processor", 
                    JOptionPane.QUESTION_MESSAGE
                );
            if ( input == null )    // exit if operator cancels
                command = new ParsedCommand( Command.EXIT, "", "" );
            else if ( input.isEmpty() )  // keep looping on empty input
                ;
            else
                command = CommandReader.parseCommand( input );
        }
        return command;
    }

    public JDialogDemo()
    {
        File    file    = new File( "equations" );
        System.out.println( "exists = " + file.exists() );
        EquationMap.parseEquationFiles( file );
        equationMap = EquationMap.getEquationMap();
        
        dialog  = new JDialog( (Dialog)null, "JDialog Demo" );
        dialog.setModal( true );
        dialog.setDefaultCloseOperation( JDialog.DISPOSE_ON_CLOSE );
        dialog.setContentPane( new ContentPane() );
        dialog.pack();
    }
    
    public int getStatus()
    {
        return closeStatus;
    }
    
    public Equation getSelectedEquation()
    {
        Equation    equation    = null;
        if ( selectedItem >= 0 )
        {
            String  name    = jList.getSelectedValue();
            equation = equationMap.get( name );
        }
        return equation;
    }
    
    public void setVisible( boolean visible )
    {
        dialog.setVisible( true );
    }
    
    private class ContentPane extends JPanel
    {
        public ContentPane()
        {
            super( new BorderLayout() );
            add( new ListPanel(), BorderLayout.CENTER );
            add( new ButtonPanel(), BorderLayout.SOUTH );
        }
    }
    
    private class ButtonPanel extends JPanel
    {
        public ButtonPanel()
        {
            JButton okButton        = new JButton( "OK" );
            JButton cancelButton    = new JButton( "Cancel" );
            add( okButton );
            add( cancelButton );
            
            okButton.addActionListener( e -> setAndClose( OK_STATUS ) );
            cancelButton
                .addActionListener( e -> setAndClose( CANCEL_STATUS ) );
        }
    }

    private class ListPanel extends JPanel
    {
        public ListPanel()
        {
            Set<String> names   = equationMap.keySet();
            String[]    labels  = names.toArray( new String[0] );
            jList = new JList<String>( labels );
            if ( labels.length > 0 )
                jList.setSelectedIndex( 0 );
            add( jList );
            jList.addListSelectionListener( 
                e -> selectedItem = e.getFirstIndex()
            );
        }
    }
    
    private void setAndClose( int closeStatus )
    {
        this.closeStatus = closeStatus;
        dialog.setVisible( false );
    }
}
