package com.acmemail.judah.cartesian_plane.sandbox;

import java.awt.Dimension;
import java.awt.Frame;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import com.acmemail.judah.cartesian_plane.test_utils.Utils;

class DisposeTest
{
    @BeforeEach
    void setUp() throws Exception
    {
        Frame[] frames  = Frame.getFrames();
        Arrays.stream( frames ).forEach( f -> {
            String  title       = f.getTitle();
            boolean displayable = f.isDisplayable();
            System.out.println( "    " + title + ": " + displayable );
        });
        System.out.println( "==========" );
    }
    
    @AfterEach
    public void afterEach()
    {
        Frame[] frames  = Frame.getFrames();
        Arrays.stream( frames ).forEach( f -> f.dispose() );
        Utils.pause( 500 );
    }

    @ParameterizedTest
    @ValueSource ( ints= { 1, 2, 3, 4, 5 } )
    void test1( int arg )
    {
        showFrame( arg );
        Utils.pause( 500 );
    }

    private static void showFrame( int frameNum )
    {
        try
        {
            String  title   = "Frame: " + frameNum;
            SwingUtilities.invokeAndWait( () -> 
            {
                JFrame  frame   = new JFrame( title );
                frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
                JPanel  panel   = new JPanel();
                panel.setPreferredSize( new Dimension( 300, 300 ) );
                frame.setContentPane( panel );
                frame.pack();
                frame.setVisible( true );
            });
        }
        catch ( InterruptedException | InvocationTargetException exc )
        {
            exc.printStackTrace();
            System.exit( 1 );
        }
    }
}
