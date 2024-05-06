package com.acmemail.judah.cartesian_plane.sandbox.experimental.line_editor_rev2;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import com.acmemail.judah.cartesian_plane.components.FontEditor;
import com.acmemail.judah.cartesian_plane.components.GraphPropertySet;

@SuppressWarnings("serial")
public class FontEditorDialog extends JDialog
{
    private final GraphPropertySet  propSet;
    private final FontEditor        editor;
    private int                     result;
    
    public FontEditorDialog( JFrame frame, GraphPropertySet propSet )
    {
        super( frame, "Font Editor", true );
        this.propSet = propSet;
        this.editor = new FontEditor();
        
        JPanel  contentPane = new JPanel( new BorderLayout() );
        JPanel  editorPanel = editor.getPanel();
        contentPane.add( editorPanel, BorderLayout.CENTER );
        contentPane.add( getButtonPanel() );
        setContentPane(contentPane);
        pack();
    }
    
    public int showDialog()
    {
        setVisible( true );
        return result;
    }
    
    private JPanel getButtonPanel()
    {
        JPanel  panel           = new JPanel();
        
        JButton okButton        = new JButton( "OK" );
        JButton applyButton     = new JButton( "Apply" );
        JButton resetButton     = new JButton( "Reset" );
        JButton canButton       = new JButton( "Cancel" );
        
        okButton.addActionListener( e -> close( JOptionPane.OK_OPTION ) );        
        resetButton.addActionListener( this::reset );
        applyButton.addActionListener( this::apply );
        canButton.addActionListener( e -> close( JOptionPane.OK_OPTION ) );
        
        panel.add( okButton );
        panel.add( applyButton );
        panel.add( resetButton );
        panel.add( canButton );
        
        return panel;
    }
    
    private void apply( ActionEvent evt )
    {
        propSet.setFGColor( editor.getColor().orElse( Color.BLACK ) );
        propSet.setFontName( editor.getName() );
        propSet.setFontSize( editor.getSize().orElse( 10 ) );
        propSet.setBold( editor.isBold() );
        propSet.setItalic( editor.isItalic() );        
    }
    
    private void reset( ActionEvent evt )
    {
        editor.setColor( propSet.getFGColor() );
        editor.setName( propSet.getFontName() );
        editor.setSize( (int)propSet.getFontSize() );
        editor.setBold( propSet.isBold() );
        editor.setItalic( propSet.isItalic() );
    }
    
    private void close( int result )
    {
        if ( result == JOptionPane.OK_OPTION )
            apply( null );
        else
            reset( null );
        this.result = result;
        setVisible( false );
    }
}
