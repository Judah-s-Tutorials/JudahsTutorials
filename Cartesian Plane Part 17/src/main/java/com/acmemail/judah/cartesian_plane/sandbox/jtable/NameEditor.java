package com.acmemail.judah.cartesian_plane.sandbox.jtable;

import java.awt.Color;
import java.awt.event.ActionEvent;

import javax.swing.DefaultCellEditor;
import javax.swing.InputVerifier;
import javax.swing.JComponent;
import javax.swing.JFormattedTextField;
import javax.swing.text.JTextComponent;

import com.acmemail.judah.cartesian_plane.graphics_utils.ComponentException;

@SuppressWarnings("serial")
public class NameEditor extends DefaultCellEditor
{
    public NameEditor()
    {
        super( new JFormattedTextField() );
        JFormattedTextField textField   =
            (JFormattedTextField)getComponent();
        textField.setInputVerifier( new IdentVerifier() );
        textField.addActionListener( this::enterAction );
    }
    
    @Override
    public boolean stopCellEditing()
    {
        Object  oValue  = getCellEditorValue();
        boolean status  = false;
        if ( !(oValue instanceof String) )
            throw new RuntimeException( "eh?" );
        if ( NameValidator.isIdentifier( (String)oValue ) )
            status = super.stopCellEditing();
        return status;
    }
    
    private void enterAction( ActionEvent evt )
    {
        Object  source  = evt.getSource();
        if ( source instanceof JFormattedTextField )
        {
            JFormattedTextField textField   = (JFormattedTextField)source;
            InputVerifier       verifier    = textField.getInputVerifier();
            verifier.verify( textField );
        }
    }
    
    private static class IdentVerifier extends InputVerifier
    {
        @Override
        public boolean verify(JComponent comp)
        {
            if ( !(comp instanceof JTextComponent) )
                throw new ComponentException( "Invalid component" );
            JTextComponent  jtComp  = (JTextComponent)comp;
            String          input   = jtComp.getText();
            boolean         status  = NameValidator.isIdentifier( input );
            
            if ( !status )
                jtComp.setForeground( Color.RED );
            else
                jtComp.setForeground( Color.BLACK );
            
            return status;
        }
    }
}
