package com.acmemail.judah.tesseract_sandbox;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.Window;
import java.util.stream.Stream;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;

@SuppressWarnings("serial")
public class FontNameSelector extends JDialog
{
    private static final String title   = "Font Name Selector";
    private String  selected    = null;
    
    public FontNameSelector( Window parent )
    {
        super( parent, title, ModalityType.APPLICATION_MODAL );
        
        GraphicsEnvironment env     = 
            GraphicsEnvironment.getLocalGraphicsEnvironment();
        Font[]              fonts   = env.getAllFonts();
        DefaultListModel<String>model   = new DefaultListModel<>();
        Stream.of( fonts )
            .map( f -> f.getFamily() )
            .forEach( model::addElement );
        
        JList<String>   list = new JList<>( model );
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        list.setSelectedIndex(0);
        list.setVisibleRowCount(5);
        JScrollPane scrollPane  = new JScrollPane( list );
        
        JPanel  contentPane = new JPanel( new BorderLayout() );
        contentPane.add( scrollPane, BorderLayout.CENTER );
        contentPane.add( getControlPanel( list ), BorderLayout.SOUTH );
        
        setContentPane( contentPane );
        pack();
    }
    
    public String open()
    {
        setVisible( true );
        return selected;
    }
    
    private JPanel getControlPanel( JList<String> list )
    {
        JPanel  panel   = new JPanel();
        JButton okay    = new JButton( "OK" );
        JButton cancel  = new JButton( "Cancel" );
        okay.addActionListener( e -> {
            selected = (String)list.getSelectedValue();
            setVisible( false );
        });
        cancel.addActionListener( e -> {
            selected = null;
            setVisible( false );
        });
        panel.add( okay );
        panel.add( cancel );
        return panel;
    }
}
