package com.acmemail.judah.color_primer.sandbox;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;

import com.acmemail.judah.color_primer.util.RangeSlider;

public class RangeSliderTester
{
    private static final Color      BROWN       = new Color( 0xC89D7C );
    private static final String     TITLE       = "Slider Explorer";
    private final RangeSlider       rangeSlider = new RangeSlider( 0, 360 );
    private final JSlider           jSlider     = new JSlider( 0, 100 );
    private final JFrame            frame       = new JFrame( TITLE );
    private final JPanel            contentPane = new JPanel();
    @SuppressWarnings("unused")
    private final RangeSliderTester tester;

    public static void main(String[] args)
    {
        SwingUtilities.invokeLater( RangeSliderTester::new );
    }

    public RangeSliderTester()
    {
        tester = this;         
        frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        
        Border      outerBorder = 
            BorderFactory.createLineBorder( Color.BLACK, 3 );
        Border      innerBorder = 
            BorderFactory.createEmptyBorder( 15, 15, 15, 15 );
        Border      mainBorder  = 
            BorderFactory.createCompoundBorder( outerBorder, innerBorder );
        Border      padding      =
            BorderFactory.createEmptyBorder( 15, 15, 15, 15 );
        Border      border      = 
            BorderFactory.createCompoundBorder( padding, mainBorder );
        BoxLayout   layout      = 
            new BoxLayout( contentPane, BoxLayout.Y_AXIS );
        contentPane.setBorder( border );
        contentPane.setPreferredSize( new Dimension( 300, 200 ) );
        contentPane.setBackground( BROWN );
        contentPane.setLayout( layout );
        
        Component   spacer1 = Box.createRigidArea( new Dimension( 0, 20 ) );
        contentPane.add( Box.createVerticalGlue() );
        contentPane.add( rangeSlider );
        contentPane.add( spacer1 );
        contentPane.add( jSlider );
        contentPane.add( Box.createVerticalGlue() );
        
        rangeSlider.setValue( 90 );
        rangeSlider.setUpperValue( 270 );
        jSlider.setValue( 50 );

        frame.setContentPane( contentPane );
        frame.setLocation( 200, 200 );
        frame.pack();
        frame.setVisible( true );
    }
}
