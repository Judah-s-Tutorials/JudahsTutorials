package com.acmemail.judah.cartesian_plane.input;

import java.awt.Component;
import java.io.File;
import java.util.Optional;

import javax.swing.JFileChooser;

final public class EquationFileChooser
{
    private final JFileChooser  chooser;
    private final Component     parent;
    
    public EquationFileChooser()
    {
        this( null );
    }
    
    public EquationFileChooser( Component parent )
    {
        String  userDirProp = System.getProperty( "user.dir" );
        File    userDir     = new File( userDirProp );
        
        this.parent = parent;
        chooser = new JFileChooser( userDir );
    }
    
    public Optional<Equation> openDialog()
    {
        Optional<Equation>  status  = Optional.empty();
        return status;
    }
}
