package com.acmemail.judah.sandbox;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Window;

import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JDialog;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class ColorDialog extends JDialog
{
    private static final int    OK_STATUS       = 0;
    private static final int    CANCEL_STATUS   = 1;
    private static final String DEF_TITLE       = "Color Chooser";
    private static final Color  DEF_COLOR       = Color.WHITE;
    
    private JColorChooser   chooser;
    private Color           initColor   = DEF_COLOR;
    private int             finalStatus = CANCEL_STATUS;

    public ColorDialog()
    {
        this( null, DEF_TITLE, DEF_COLOR );
    }
    
    public ColorDialog( Window parent, String title, Color initColor )
    {
        super( parent, title );
        if ( initColor != null )
            this.initColor = initColor;
        
        setDefaultCloseOperation( JDialog.HIDE_ON_CLOSE );
        setModal( true );
        setContentPane( getContentPanel() );
        pack();
    }
    
    public Color showDialog()
    {
        setVisible( true );
        chooser.setColor( initColor );
        Color color = finalStatus == OK_STATUS ? chooser.getColor() : null;
        return color;
    }
    
    private void setAndClose( int status )
    {
        finalStatus = status;
        System.out.println( finalStatus );
        if ( finalStatus == OK_STATUS )
            initColor = chooser.getColor();
        setVisible( false );
    }
    
    private JPanel getContentPanel()
    {
        JPanel  panel   = new JPanel( new BorderLayout() );
        chooser = new JColorChooser( initColor );
        panel.add( chooser, BorderLayout.CENTER );
        panel.add( getButtonPanel(), BorderLayout.SOUTH );
        return panel;
    }
    
    private JPanel getButtonPanel()
    {
        JButton okButton        = new JButton( "OK" );
        JButton cancelButton    = new JButton( "Cancel" );
        JButton resetButton     = new JButton( "Reset" );
        JPanel  panel           = new JPanel();
        
        panel.add( okButton );
        panel.add( cancelButton );
        panel.add( resetButton );
        
        okButton.addActionListener( e -> setAndClose( OK_STATUS ) );
        cancelButton.addActionListener( e -> setAndClose( CANCEL_STATUS ) );
        resetButton.addActionListener( e -> chooser.setColor( initColor ) );
        
        return panel;
    }
}
