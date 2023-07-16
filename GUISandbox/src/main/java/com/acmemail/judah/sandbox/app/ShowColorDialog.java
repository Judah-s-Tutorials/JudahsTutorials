package com.acmemail.judah.sandbox.app;

import java.awt.Color;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import com.acmemail.judah.sandbox.ColorDialog;
import com.acmemail.judah.sandbox.sandbox.SandboxUtils;

public class ShowColorDialog
{
    private static boolean  running = true;
    public static void main(String[] args)
    {
        ColorDialog dialog  = new ColorDialog();
        dialog.addWindowListener( new WindowAdapter() {
            @Override
            public void windowClosing( WindowEvent evt )
            {
                running = false;
            }
        });
        
        while ( running )
        {
            System.out.println( "start" );
            Color   color   = dialog.showDialog();
            System.out.println( color );
            SandboxUtils.pause( 500 );
            System.out.println( "end" );
        }
        
        System.exit( 0 );
    }

}
