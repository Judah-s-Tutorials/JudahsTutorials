package com.acmemail.judah.cartesian_plane.sandbox;

import java.io.File;
import java.util.Map;
import java.util.Set;

import javax.swing.JOptionPane;

import com.acmemail.judah.cartesian_plane.CartesianPlane;
import com.acmemail.judah.cartesian_plane.app.CommandExecutor;
import com.acmemail.judah.cartesian_plane.graphics_utils.Root;
import com.acmemail.judah.cartesian_plane.input.Command;
import com.acmemail.judah.cartesian_plane.input.CommandReader;
import com.acmemail.judah.cartesian_plane.input.Equation;
import com.acmemail.judah.cartesian_plane.input.EquationMap;
import com.acmemail.judah.cartesian_plane.input.ItemSelectionDialog;
import com.acmemail.judah.cartesian_plane.input.ParsedCommand;

public class SelectEquationDemo1
{
    private static final String prompt      = "Enter a command> ";
    private static final String dialogTitle = "Command Input";
    
    /**
     * Application entry point.
     * 
     * @param args  command line arguments; not used
     */
    public static void main(String[] args)
    {
        CartesianPlane  plane   = new CartesianPlane();
        Root            root    = new Root( plane );
        root.start();
        
        CommandExecutor executor    = new CommandExecutor( plane );
        Equation        equation    = getEquation();
        executor.exec( SelectEquationDemo1::getInput, equation );
        
        System.exit( 0 );
    }
    
    private static Equation getEquation()
    {
        Equation    equation    = null;
        File        dir         = new File( "equations" );
        if ( dir.exists() )
        {
            EquationMap.parseEquationFiles( dir );
            Map<String,Equation>    map     = EquationMap.getEquationMap();
            Set<String>             set     = map.keySet();
            String[]                arr     = new String[set.size()];
            set.toArray( arr );
            
            ItemSelectionDialog     dialog  = 
                new ItemSelectionDialog("Select an Equation",  arr );
            int                     choice  = dialog.show();
            
            equation = choice >= 0 ? map.get( arr[choice] ) : null;
        }
        return equation;
    }
    
    /**
     * Prompt the operator for a command.
     * Loop on empty input;
     * generate EXIT cancel.
     * 
     * @return  a ParsedCommand reflecting the operator's input
     */
    private static ParsedCommand getInput()
    {
        ParsedCommand   command = null;
        while ( command == null )
        {
            String  input   =
                JOptionPane.showInputDialog(
                    null, 
                    prompt, 
                    dialogTitle, 
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
}
