package com.gmail.johnstraub1954.penrose;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class DragDemo1
{
    /** Unicode for an up-arrow. */
    private static final String         upArrow     = "\u21e7";
    /** Unicode for a down-arrow. */
    private static final String         downArrow   = "\u21e9";
    /** Unicode for a left-arrow. */
    private static final String         leftArrow   = "\u21e6";
    /** Unicode for a right-arrow. */
    private static final String         rightArrow  = "\u21e8";
    /** Unicode for a rotate-left arrow. */
    private static final String         rotateLeft  = "\u21B6";
    /** Unicode for a rotate-right arrow. */
    private static final String         rotateRight = "\u21B7";
    
    private static double       longSide    = 75;
    private PCanvas canvas;
    
    public static void main(String[] args)
    {
        DragDemo1   demo2 = new DragDemo1();
        SwingUtilities.invokeLater( () -> {
            demo2.build();
            demo2.canvas.addShape( new PKite( longSide, 0, 0 ) );
            demo2.canvas.addShape( new PDart( longSide, 100, 0 ) );
            demo2.canvas.addShape( new PKite( longSide, 0, 100 ) );
            demo2.canvas.addShape( new PDart( longSide, 100, 100 ) );
        });
    }
    
    public void build()
    {
        JFrame  frame   = new JFrame( "Dart Demo" );
        frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        JPanel  pane    = new JPanel( new BorderLayout() );
        canvas = new PCanvas();
        pane.add( canvas, BorderLayout.CENTER );
        pane.add( getControlPanel(), BorderLayout.SOUTH );
        frame.setContentPane( pane );
        frame.setLocation( 350, 100 );
        frame.pack();
        
        frame.setVisible( true );
    }
    
    private JPanel getControlPanel()
    {
        final Dimension rigidDim    = new Dimension( 5, 0 );
        JPanel  panel   = new JPanel();
        panel.add( getTranslatePanel() );
        panel.add( Box.createRigidArea( rigidDim ) );
        panel.add( getRotatePanel() );
        panel.add( Box.createRigidArea( rigidDim ) );
        
        JButton exit    = new JButton( "Exit" );
        exit.addActionListener( e -> System.exit( 0 ) );
        panel.add( exit );
        return panel;
    }
    
    private JPanel getTranslatePanel()
    {
        JPanel  panel       = new JPanel( new GridLayout( 2, 3 ) );
        JButton upButton    = new JButton( upArrow );
        JButton downButton  = new JButton( downArrow );
        JButton leftButton  = new JButton( leftArrow );
        JButton rightButton = new JButton( rightArrow );
        
        upButton.addActionListener( e -> 
            action( p -> p.move( 0, -4 ) )
        );
        downButton.addActionListener( e -> 
            action( p -> p.move( 0, 4 ) )
        );
        leftButton.addActionListener( e -> 
            action( p -> p.move( -4, 0 ) )
        );
        rightButton.addActionListener( e -> 
            action( p -> p.move( 4,0 ) )
        );
        
        panel.add( new JLabel( "" ) );
        panel.add( upButton );
        panel.add( new JLabel( "" ) );
        panel.add( leftButton );
        panel.add( downButton );
        panel.add( rightButton );
        return panel;
    }
    
    private JPanel getRotatePanel()
    {
        JPanel  panel       = new JPanel( new GridLayout( 1, 2, 5, 5 ) );
        JButton leftButton  = new JButton( rotateLeft );
        JButton rightButton = new JButton( rotateRight );
        
        leftButton.addActionListener( e -> 
            action( p -> p.rotate( PShape.D36 ) )
        );
        rightButton.addActionListener( e -> 
            action( p -> p.rotate( -PShape.D36 ) )
        );
        
        panel.add( leftButton );
        panel.add( rightButton );
        return panel;
    }
    
    private void action( Consumer<PShape> consumer )
    {
        List<PShape>    list    = canvas.getSelected();
        list.forEach( consumer );
        canvas.repaint();
    }
}
