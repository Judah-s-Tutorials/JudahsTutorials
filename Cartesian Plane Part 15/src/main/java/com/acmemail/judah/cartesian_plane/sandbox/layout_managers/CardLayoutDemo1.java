package com.acmemail.judah.cartesian_plane.sandbox.layout_managers;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import com.acmemail.judah.cartesian_plane.sandbox.SandboxUtils;

public class CardLayoutDemo1
{
    private static final char HEARTS    = '\u2665';
    private static final char SPADES    = '\u2660';
    private static final char CLUBS     = '\u2663';
    private static final char DIAMONDS  = '\u2666';
    
    public static void main(String[] args)
    {
        SwingUtilities.invokeLater( () -> makeGUI() );
    }

    private static void makeGUI()
    {
        JFrame  frame       = new JFrame( "GridLayout Demo 1" );
        frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
 
        CardLayout  layout      = new CardLayout();
        JPanel      cardPanel   = new JPanel( layout );
        cardPanel.add( getCard( "J", SPADES ) );
        cardPanel.add( getCard( "10", HEARTS ) );
        cardPanel.add( getCard( "Q", DIAMONDS ) );
        cardPanel.add( getCard( "A", CLUBS ) );
        cardPanel.add( getCard( "5", HEARTS ) );

        JButton rotate      = new JButton( "Shift" );
        rotate.addActionListener( e -> layout.next( cardPanel ) );
        
        JPanel  mainPanel   = new JPanel( new BorderLayout() );
        mainPanel.add( cardPanel, BorderLayout.CENTER );
        mainPanel.add( rotate, BorderLayout.SOUTH );
        frame.setContentPane( mainPanel );
        frame.pack();
        SandboxUtils.center( frame );
        frame.setVisible( true );
    }
    
    private static JComponent getCard( String text, char suit )
    {
        String  label   = 
            "<html><center>" + text + "<br>" + suit + "</center></html>";
        JLabel  card    = new JLabel( label );
        Color   color   =
            suit == DIAMONDS || suit == HEARTS ?
            Color.RED : Color.BLACK;
        Font    font    = card.getFont().deriveFont( 64f );
        card.setFont( font );
        card.setHorizontalAlignment( SwingConstants.CENTER );
        card.setForeground( color );
        return card;
    }
}
