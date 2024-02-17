package com.acmemail.judah.cartesian_plane.components;

import java.awt.Color;
import java.text.ParseException;

import javax.swing.DefaultCellEditor;
import javax.swing.InputVerifier;
import javax.swing.JComponent;
import javax.swing.JFormattedTextField;
import javax.swing.text.DefaultFormatter;
import javax.swing.text.JTextComponent;

@SuppressWarnings("serial")
public class NameEditor extends DefaultCellEditor
{
    public NameEditor()
    {
        super( new JFormattedTextField( new NameFormatter() ) );
        JFormattedTextField textField   =
            (JFormattedTextField)getComponent();
        textField.setInputVerifier( new IdentVerifier() );
    }
    
    @Override
    public boolean stopCellEditing()
    {
        Object  oValue  = getCellEditorValue();
        boolean status  = false;
        if ( NameValidator.isIdentifier( (String)oValue ) )
            status = super.stopCellEditing();
        return status;
    }
    
    private static class IdentVerifier extends InputVerifier
    {
        @Override
        public boolean verify(JComponent comp)
        {
            JTextComponent  jtComp  = (JTextComponent)comp;
            String          input   = jtComp.getText();
            boolean         status  = NameValidator.isIdentifier( input );
            
            return status;
        }
    }
    
    private static class NameFormatter extends DefaultFormatter
    {
        @Override
        public String stringToValue( String str )
            throws ParseException
        {
            JFormattedTextField fmtField    = getFormattedTextField();
            if ( !NameValidator.isIdentifier( str ) )
            {
                fmtField.setForeground( Color.RED );
                throw new ParseException( "Invalid name", 0 );
            }
            fmtField.setForeground( Color.BLACK );
            return str;
        }
    }

}
