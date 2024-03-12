package com.acmemail.judah.cartesian_plane.sandbox;

import java.awt.GridLayout;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;

public class GreekPanel
{
    private static final int    bWidth  =10;
    private static final Desc[] allDescs = {
        new Desc("alpha",0x03b1,0x0391),
        new Desc("beta",0x03b2,0x0392),
        new Desc("gamma",0x03b3,0x0393),
        new Desc("delta",0x03b4,0x0394),
        new Desc("epsilon",0x03b5,0x0395),
        new Desc("zeta",0x03b6,0x0396),
        new Desc("eta",0x03b7,0x0397),
        new Desc("theta",0x03b8,0x0398),
        new Desc("iota",0x03b9,0x0399),
        new Desc("kappa",0x03ba,0x039a),
        new Desc("lambda",0x03bb,0x039b),
        new Desc("mu",0x03bc,0x039c),
        new Desc("nu",0x03bd,0x039d),
        new Desc("xi",0x03be,0x039e),
        new Desc("omicron",0x03bf,0x039f),
        new Desc("pi",0x03c0,0x03a0),
        new Desc("rho",0x03c1,0x03a1),
        new Desc("sigma",0x03c3,0x03a3),
        new Desc("tau",0x03c4,0x03a4),
        new Desc("upsilon",0x03c5,0x03a5),
        new Desc("phi",0x03c6,0x03a6),
        new Desc("chi",0x03c7,0x03a7),
        new Desc("psi",0x03c8,0x03a8),
        new Desc("omega",0x03c9,0x03a9),
    };
    
    public static void main( String[] args )
    {
        GreekPanel  app = new GreekPanel();
        SwingUtilities.invokeLater( () -> app.buildGUI() );
    }
    
    private void buildGUI()
    {
        JFrame      frame   = new JFrame( "Greek Panel" );
        frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        JPanel      panel   = new JPanel();
        BoxLayout   layout  = new BoxLayout( panel, BoxLayout.Y_AXIS );
        panel.setLayout( layout );
        for ( int inx = 0 ; inx < allDescs.length ; inx += 3)
            panel.add( nextRow( inx ) );
        
        Border  border  = 
            BorderFactory.createEmptyBorder(bWidth, bWidth, bWidth, bWidth);
        panel.setBorder( border );
        
        frame.setContentPane( panel );
        frame.pack();
        frame.setVisible( true );
    }
    
    public JPanel nextRow( int num )
    {
        JPanel  panel   = new JPanel( new GridLayout( 1, 3 )  );
        panel.add( newPanel( allDescs[num] ) );
        panel.add( newPanel( allDescs[num +1 ] ) );
        panel.add( newPanel( allDescs[num + 2] ) );
        return panel;
    }
    
    public JPanel newPanel( Desc desc )
    {
        JPanel  panel   = new JPanel( new GridLayout( 1, 3 ) );
        Border  border  = BorderFactory.createEmptyBorder( 5, 5, 5, 5 );
        panel.setBorder( border );
        
        String  name    = String.format( "%-8s", desc.name );
        panel.add( new JLabel( name ) ); 
        panel.add( new JButton( "" + desc.lower ) );
        panel.add( new JButton( "" + desc.upper ) );
        return panel;
    }
    
    private static class Desc
    {
        public final String name;
        public final char   upper;
        public final char   lower;
        public Desc( String name, int lower, int upper )
        {
            this.name = name;
            this.lower = (char)lower;
            this.upper = (char)upper;
        }
    }
}
