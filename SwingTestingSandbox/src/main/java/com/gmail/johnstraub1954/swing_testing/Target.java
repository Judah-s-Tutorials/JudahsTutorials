package com.gmail.johnstraub1954.swing_testing;

import java.io.File;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

public class Target
{
    public static final String NAME_TITLE  = "Get a Name Dialog";
    private static final JFileChooser chooser = new JFileChooser();

    public static void save()
    {
        int status  = chooser.showSaveDialog( null );
        if ( status == JFileChooser.APPROVE_OPTION )
        {
            try
            {
                File    file    = chooser.getSelectedFile();
                file.createNewFile();
            }
            catch ( IOException exc )
            {
                exc.printStackTrace();
                System.exit( 1 );
            }
        }
    }
    
    public static String getName()
    {
        String  name    =
            JOptionPane.showInputDialog( "Enter a name", NAME_TITLE );
        return name;
    }
}
