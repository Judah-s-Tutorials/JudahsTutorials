package com.acmemail.judah.cartesian_plane;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.SwingUtilities;

import org.junit.jupiter.api.Test;

import com.acmemail.judah.color_primer.util.ComponentFinder;

class ComponentFinderTest
{
    private static final String NOT_PRESENT = "_not present";
    private static JFrame       frame;
    private static List<String> allNames;
    private static List<String> notPresentNames;
    private static List<String> frameNames;
    private static List<String> notFrameNames;
    
    public ComponentFinderTest()
        throws InvocationTargetException, InterruptedException
    {
        allNames = new ArrayList<>();
        notPresentNames = new ArrayList<>();
        frameNames = new ArrayList<>();
        notFrameNames = new ArrayList<>();
        
        invokeAndWait( () -> frame = init() );
        System.out.println( allNames.size() );  
    }
    
    private static void invokeAndWait( Runnable runner )
    {
        try
        {
            SwingUtilities.invokeAndWait( runner );
        }
        catch ( InterruptedException | InvocationTargetException exc )
        {
            exc.printStackTrace();
        }
    }
    
    private JFrame init()
    {
        JFrame  frameAlt        = new JFrame();
        String  frameAltName    = "JFrame Alt";
        frameAlt.setName( frameAltName );
        frameNames.add( frameAltName );
        notFrameNames.add( frameAltName + NOT_PRESENT );
        // Realize the frame (create its native peer) so that
        // isDisplayable() returns true; ComponentFinder only
        // searches displayable frames.
        frameAlt.pack();

        JFrame  frame1          = new JFrame();
        String  frameName1      = "JFrame 1";
        frame1.setName( frameName1 );
        frameNames.add( frameName1 );
        notFrameNames.add( frameName1 + NOT_PRESENT );

        JPanel  contentPane = new JPanel();
        frame1.setContentPane( contentPane );
        String  name            = "ContentPane";
        addName( contentPane, name );

        JPanel  parent  = contentPane;
        for ( char panelID = '1' ; panelID < '5' ; ++panelID )
            parent = makeTestPanel( parent, panelID );

        // Realize the frame (create its native peer) so that
        // isDisplayable() returns true; ComponentFinder only
        // searches displayable frames.
        frame1.pack();
        return frame1;
    }
    
    private JPanel makeTestPanel( JPanel parent, char panelID )
    {
        JPanel  panel       = new JPanel();
        String  panelName   = "panel " + panelID;
        addName( panel, panelName );
        for ( char buttonID = 'a' ; buttonID < 'c' ; ++buttonID )
        {
            JButton button      = new JButton();
            String  buttonName  = "button " + panelID + buttonID;
            addName( button, buttonName );
            panel.add( button );
        }
        
        JSlider slider  = new JSlider();
        addName( slider, "Slider_" + panelID );
        panel.add( slider );
        
        JPanel  altPanel    = new JPanel();
        addName( altPanel, panel + "panelID" + "Alt" );
        panel.add( altPanel );
        
        parent.add( panel );
        return panel;
    }
    
    private void addName( JComponent comp, String name )
    {
        comp.setName( name );
        allNames.add( name );
        notPresentNames.add( name + NOT_PRESENT );
    }

    @Test
    void testGetComponentByNameString()
    {
        ComponentFinder finder  = new ComponentFinder( frame );
        for ( String name : allNames )
        {
            JComponent  component   = finder.getComponentByName( name );
            assertNotNull( component, name );
        }
        for ( String name : notPresentNames )
        {
            JComponent  component   = finder.getComponentByName( name );
            assertNull( component, name );
        }
    }
    
    @Test
    public void testGetFrameByName()
    {
        for ( String name : frameNames )
        {
            JFrame  component   = 
                ComponentFinder.getJFrameByName( name );
            assertNotNull( component, name );
        }
        for ( String name : notFrameNames )
        {
            JFrame  component   = 
                ComponentFinder.getJFrameByName( name );
            assertNull( component, name );
        }
    }
    @Test
    void testGlobalGetComponentByNameString()
    {
        for ( String name : allNames )
        {
            JComponent  component   = 
                ComponentFinder.globalGetComponentByName( name );
            assertNotNull( component, name );
        }
        for ( String name : notPresentNames )
        {
            JComponent  component   = 
                ComponentFinder.globalGetComponentByName( name );
            assertNull( component, name );
        }
    }
}
