package com.acmemail.judah.sandbox.app;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.lang.reflect.InvocationTargetException;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import com.acmemail.judah.sandbox.sandbox.ContrastCalculator;

public class ColorContraster
{
    private static final int    RED_MASK    = 0xFF0000;
    private static final int    GREEN_MASK  = 0x00FF00;
    private static final int    BLUE_MASK   = 0x0000FF;
    private static final float  FONT_SIZE   = 20f;
    
    private JFrame      frame;
    private JPanel      leftPanel;
    private JPanel      rightPanel;
    private JTextField  editor;
    
    private ContrastCalculator  contrastCalc    = new ContrastCalculator();
    
    public static void main(String[] args)
        throws InterruptedException, InvocationTargetException
    {
        ColorContraster app = new ColorContraster();
        SwingUtilities.invokeAndWait( () -> app.build() );
    }
    
    private void build()
    {
        frame = new JFrame( "Color Contraster" );
        frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        JPanel      contentPane = new JPanel( new BorderLayout() );
        JPanel      centerPanel = new JPanel( new GridLayout( 1, 2 ) );
        leftPanel = new Display( true );
        rightPanel = new Display( false );
        editor = new JTextField( 10 );
        editor.setText( "0xCCCCCC" );
        
        centerPanel.add( leftPanel );
        centerPanel.add( rightPanel );
        
        contentPane.add( centerPanel, BorderLayout.CENTER );
        contentPane.add( editor, BorderLayout.SOUTH );
        frame.setContentPane( contentPane );
        
        editor.addActionListener( e -> recalculate() );
        
        frame.pack();
        frame.setVisible( true );
    }

    private void recalculate()
    {
        try
        {
            Color   newColor    = Color.decode( editor.getText() );
            contrastCalc.setBaseColor( newColor );
            frame.repaint();
        }
        catch ( NumberFormatException exc )
        {
            editor.setText( "#ERROR" );
        }
    }
    
    private class Display extends JPanel
    {
        private static final String fbString    = 
            "'Twas brillig and the slithy toves";
        private final boolean   isBase;
        
        public Display( boolean isBase )
        {
            this.isBase = isBase;
            
            Dimension   prefSize    = new Dimension( 300, 200 );
            setPreferredSize( prefSize );
        }
        
        @Override
        public void paintComponent( Graphics graphics )
        {
            Graphics2D  gtx         = (Graphics2D)graphics.create();
            int         width       = getWidth();
            int         height      = getHeight();
            Color       fillColor   = contrastCalc.getBaseColor();
            Color       fontColor   = contrastCalc.getContrastColor();
            int         fontXco     = 10;
            int         fontYco     = height / 2 - 10;
            Font        font        = gtx.getFont().deriveFont( FONT_SIZE );
            if ( isBase )
            {
                fillColor = contrastCalc.getContrastColor();
                fontColor = contrastCalc.getBaseColor();
            }
            
            gtx.setColor( fillColor );
            gtx.fillRect( 0, 0, width, height );
            gtx.setFont( font );
            gtx.setColor( fontColor );
            gtx.drawString( fbString, fontXco, fontYco );
        }
    }
}
