package com.acmemail.judah.cartesian_plane.sandbox.app;

import java.awt.Color;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import com.acmemail.judah.cartesian_plane.components.GraphPropertiesPanel;
import com.acmemail.judah.cartesian_plane.components.GraphPropertySet;
import com.acmemail.judah.cartesian_plane.components.GraphPropertySetBM;
import com.acmemail.judah.cartesian_plane.components.GraphPropertySetLM;
import com.acmemail.judah.cartesian_plane.components.GraphPropertySetMW;
import com.acmemail.judah.cartesian_plane.components.GraphPropertySetRM;
import com.acmemail.judah.cartesian_plane.components.GraphPropertySetTM;

/**
 * Application to demonstrate
 * the LinePropertiesPanel class
 * from the project ...components package.
 * 
 * @author Jack Straub
 */
public class ShowGraphPropertiesPanel
{
    private static String[]   fontNames =
    {
        Font.DIALOG,
        Font.DIALOG_INPUT,
        Font.MONOSPACED,
        Font.SANS_SERIF,
        Font.SERIF
    };
    private static int      nameInx     = 0;
    
    private static Color[]  fontColors  =
    {
        Color.BLUE,
        Color.CYAN,
        Color.GREEN,
        Color.MAGENTA,
        Color.ORANGE
    };
    private static int      fgColorInx  = 0;
    private static int      bgColorInx  = 1;
    
    private static boolean  isBold      = true;
    private static boolean  isItalic    = false;
    private static int      size        = 6;
    private static boolean  draw        = true;
    private static float    width       = 100;
    
    /**
     * Application entry point.
     *
     * @param args command line arguments, not used
     *
     */
    public static void main(String[] args)
    {
        initPropertyProperties( new GraphPropertySetMW() );
        initPropertyProperties( new GraphPropertySetTM() );
        initPropertyProperties( new GraphPropertySetRM() );
        initPropertyProperties( new GraphPropertySetLM() );
        initPropertyProperties( new GraphPropertySetBM() );
        
        ShowGraphPropertiesPanel demo    = new ShowGraphPropertiesPanel();
        SwingUtilities.invokeLater( () -> demo.buildGUI() );
    }
    
    private static void initPropertyProperties( GraphPropertySet set )
    {
        int     numNames    = fontNames.length;
        int     numColors   = fontColors.length;
        set.setFontName( fontNames[nameInx] );
        nameInx = ++nameInx % numNames;
        set.setBold( (isBold = !isBold) );
        set.setItalic( (isItalic = !isItalic) );
        set.setFontSize( size += 2 );
        set.setFGColor( fontColors[fgColorInx] );
        fgColorInx = ++fgColorInx % numColors;
        set.setBGColor( fontColors[fgColorInx] );
        bgColorInx = ++bgColorInx % numColors;
        set.setFontDraw( draw = !draw );
        set.setWidth( width += 50 );
        set.apply();
    }

    /**
     * Create and show the GUI.
     */
    private void buildGUI()
    {
        String  title   = "Show Graph Properties Panel";
        JFrame  frame   = new JFrame( title );
        frame.setDefaultCloseOperation( JDialog.EXIT_ON_CLOSE );
        
        JPanel  panel   = new JPanel();
        panel.setBorder(
            BorderFactory.createEmptyBorder( 100, 100, 100, 100 )
        );
        JButton button  = new JButton( "Open Dialog" );
        panel.add( button );
        frame.setContentPane( panel );
        frame.setLocation( 200, 200 );
        frame.pack();
        
        JDialog dialog  = GraphPropertiesPanel.getDialog( frame );
        button.addActionListener( e -> dialog.setVisible( true ) );
        frame.setVisible( true );
        dialog.setVisible( true );
    }
}