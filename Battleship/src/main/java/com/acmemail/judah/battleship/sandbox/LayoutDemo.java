package com.acmemail.judah.battleship.sandbox;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.Border;

import com.acmemail.judah.battleship2D.Grid2D;

public class LayoutDemo
{

    public static void main(String[] args)
    {
        Grid2D  grid    = Grid2D.getHomeGrid();
        GridFrame.getFrame( () -> new Parent( grid ) );

    }
    
    @SuppressWarnings("serial")
    private static class Parent extends JPanel
    {
        private final int       border1Width    = 5;
        private final int       border2Width    = 5;
        private final Color     border2Color    = Color.BLUE;
        
        public Parent( Grid2D grid )
        {
            setLayout( new GridLayout( 1, 2 ) );
            add( getTitleComponent() );
            add( getTitleComponent() );
        }
        
        private JPanel getBorderPanel()
        {
            JPanel  panel   = new JPanel();
            Border  outerBorder = 
                BorderFactory.createEmptyBorder( 
                    border1Width, 
                    border1Width, 
                    border1Width, 
                    border1Width 
                );
            Border  innerBorder =
                BorderFactory.createLineBorder( border2Color, border2Width );
            Border  border      =
                BorderFactory.createCompoundBorder( innerBorder, outerBorder );
            panel.setBorder( border );
            return panel;
            
        }

        private JComponent getTitleComponent()
        {
            // Title and controller should live inside the border
            // The border should be inside a scroll pane
            // The border component should never be resized
            JPanel  borderPanel = getBorderPanel();
            BoxLayout   layout = new BoxLayout( borderPanel, BoxLayout.Y_AXIS );
            borderPanel.setLayout( layout );

            borderPanel.add( getTitle() );
            borderPanel.add( new Controller() );
            
            // The dummy panel is to prevent the border panel from being resized
            JPanel  dummyPanel  = new JPanel();
            dummyPanel.add( borderPanel );
            JScrollPane pane    = new JScrollPane( dummyPanel );

            return pane;
        }

        private static JLabel   getTitle()
        {
            String  titleText   =
                "<HTML><BODY style='font-size: 150%;'>HOME</BODY></HTML>";
            JLabel  title   = new JLabel( titleText );
            return title;
        }
    }

    @SuppressWarnings("serial")
    private static class Controller extends JPanel
    {
        private static final Color  bgColor     = Color.LIGHT_GRAY;
        public Controller()
        {
            setPreferredSize( new Dimension( 350, 275 ) );
        }
        
        @Override
        public void paintComponent( Graphics graphics )
        {
            super.paintComponent( graphics );
            Graphics2D  gtx = (Graphics2D)graphics;
            gtx.setColor( bgColor );
            gtx.fillRect( 0,  0, getWidth(), getHeight() );
        }
    }
}
