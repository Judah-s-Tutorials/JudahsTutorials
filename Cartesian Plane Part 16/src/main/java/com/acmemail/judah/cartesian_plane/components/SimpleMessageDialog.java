package com.acmemail.judah.cartesian_plane.components;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.Window;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.Border;

public class SimpleMessageDialog extends JDialog
{
    private static final int    defWidth    = 300;
    private static final int    defHeight   = 300;
    private static final int    defRows     = 25;
    private static final int    defCols     = 40;
    private static final String defTitle    = "Message";
    private final JTextArea     textArea;
    
    public SimpleMessageDialog( Object text )
    {
        this( null, text, defTitle, defRows, defCols );
    }
    
    public SimpleMessageDialog( Window parent, Object text )
    {
        this( parent, text, defTitle, defRows, defCols );
    }
    
    public SimpleMessageDialog( Window parent, Object text, String title )
    {
        this( parent, text, title, defRows, defCols );
    }
    
    public SimpleMessageDialog( 
        Window parent,
        Object text,
        String title,
        int    rows,
        int    cols
    )
    {
        super( parent, title != null ? title : defTitle );
        setModal( true );
        textArea = new JTextArea( text.toString(), rows, cols );
        textArea.setEditable( false );
        JScrollPane scrollPane  = new JScrollPane( textArea );
        
        JButton close       = new JButton( "Close" );
        close.addActionListener( e -> setVisible( false ) );
        getRootPane().setDefaultButton( close );
        JPanel  buttonPanel = new JPanel();
        buttonPanel.add( close );
        
        JPanel  mainPanel   = new JPanel( new BorderLayout() );
        Border  border      = 
            BorderFactory.createEmptyBorder( 10, 10, 10, 10 );
        mainPanel.setBorder( border );
        mainPanel.add( scrollPane, BorderLayout.CENTER );
        mainPanel.add( buttonPanel, BorderLayout.SOUTH );
        setContentPane( mainPanel );
        pack();
        center();
    }
    
    public void center()
    {
        Dimension   screenDim   = 
            Toolkit.getDefaultToolkit().getScreenSize();
        int         width       = screenDim.width;
        int         height      = screenDim.height;
        int         xco         = (width - getWidth()) / 2;
        int         yco         = (height - getHeight()) / 2;
        setLocation( xco, yco );
    }
    
    public JTextArea getTextArea()
    {
        return textArea;
    }
}
