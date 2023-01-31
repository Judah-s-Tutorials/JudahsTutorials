package com.acmemail.judah.cartesian_plane.sandbox;

import java.io.File;
import java.io.IOException;

public class CreateTempFileDemo
{
    public static void main(String[] args)
    {
        try
        {
            File    tempFile    = File.createTempFile( "CPTest", ".ini" );
            System.out.println( tempFile.getAbsolutePath() );
        }
        catch ( IOException exc )
        {
            exc.printStackTrace();
        }

    }
}
