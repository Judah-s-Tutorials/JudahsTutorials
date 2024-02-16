package com.acmemail.judah.cartesian_plane.sandbox.jtable;

import java.awt.Color;
import java.text.ParseException;

import javax.swing.DefaultCellEditor;
import javax.swing.InputVerifier;
import javax.swing.JComponent;
import javax.swing.JFormattedTextField;
import javax.swing.text.DefaultFormatter;
import javax.swing.text.JTextComponent;

import com.acmemail.judah.cartesian_plane.graphics_utils.ComponentException;

import temp.NameValidator;

@SuppressWarnings("serial")
public class NameEditor extends DefaultCellEditor
{
    public NameEditor()
    {
        super( new JFormattedTextField( new NameFormatter() ) );
        JFormattedTextField textField   =
            (JFormattedTextField)getComponent();
        textField.setInputVerifier( new IdentVerifier() );
        NameFormatter   formatter   = 
            (NameFormatter)textField.getFormatter();
        formatter.setFormattedTextField( textField );
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
    
    private static class NameFormatter extends DefaultFormatter
    {
        private JFormattedTextField fmtField;
        
        @Override
        public String stringToValue( String str )
            throws ParseException
        {
            if ( !NameValidator.isIdentifier( str ) )
            {
                fmtField.setForeground( Color.RED );
                throw new ParseException( "Invalid name", 0 );
            }
            fmtField.setForeground( Color.BLACK );
            return str;
        }
        
        public void setFormattedTextField( JFormattedTextField field )
        {
            fmtField = field;
        }
    }

}
