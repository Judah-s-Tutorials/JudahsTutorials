package com.acmemail.judah.cartesian_plane.sandbox;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.ActionEvent;
import java.io.IOException;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;

public class GreekDialog extends JDialog
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
    
    private Character   choice  = null;
    
    public static void main( String[] args )
    {
        GreekDialog  app = new GreekDialog();
        SwingUtilities.invokeLater( () -> app.buildGUI() );
        app.setVisible( true );
    }
    
    private void buildGUI()
    {
        JPanel      contentPane = new JPanel( new BorderLayout() );
        
        JPanel      panel       = new JPanel();
        BoxLayout   layout      = new BoxLayout( panel, BoxLayout.Y_AXIS );
        panel.setLayout( layout );
        for ( int inx = 0 ; inx < allDescs.length ; inx += 3)
            panel.add( nextRow( inx ) );
        
        Border  border          = 
            BorderFactory.createEmptyBorder(bWidth, bWidth, bWidth, bWidth);
        panel.setBorder( border );
        
        contentPane.add( panel, BorderLayout.CENTER );
        contentPane.add( getFeedbackPanel(), BorderLayout.SOUTH );
        
        setContentPane( contentPane );
        pack();
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
        JButton lower   = new JButton( "" + desc.lower );
        JButton upper   = new JButton( "" + desc.upper );
        panel.add( lower );
        panel.add( upper );
        lower.addActionListener( this::copyAction );
        upper.addActionListener( this::copyAction );
        return panel;
    }
    
    private JPanel getFeedbackPanel()
    {
        JPanel      panel       = new JPanel();
        BoxLayout   layout      = new BoxLayout( panel, BoxLayout.X_AXIS );
        panel.setLayout( layout );
        
        Border      border      =
            BorderFactory.createLineBorder( Color.BLACK, 2 );
        panel.setBorder( border );
        
        JTextField  textField   = new JTextField( 10 );
        JButton     pasteButton = new JButton( "Copy" );
        pasteButton.addActionListener( e -> pasteAction( textField ) );
        panel.add( textField );
        panel.add( pasteButton );
        return panel;
    }
    
    private void copyAction( ActionEvent evt )
    {
        Object  src = evt.getSource();
        if ( src instanceof JButton )
        {
            JButton         button      = (JButton)src;
            String          text        = button.getText();
            StringSelection selection   = new StringSelection( text );
            Clipboard       clipboard   = 
                Toolkit.getDefaultToolkit().getSystemClipboard();
            clipboard.setContents( selection, null );
        }
    }
    
    private void pasteAction( JTextField textField )
    {
        try
        {
            Clipboard   clipboard   = 
                Toolkit.getDefaultToolkit().getSystemClipboard();
            String      toPaste     =
                (String)clipboard.getData( DataFlavor.stringFlavor );
            String      text        = textField.getText() + toPaste;
                textField.setText( text );
        }
        catch ( IOException | UnsupportedFlavorException exc )
        {
            exc.printStackTrace();
        }
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
