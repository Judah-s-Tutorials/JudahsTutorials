package com.acmemail.judah.battleship;

import java.awt.Component;

import javax.swing.Icon;
import javax.swing.JOptionPane;

public class JOptionPaneS implements JOptionPaneI
{
    public JOptionPaneS()
    {
    }

    @Override
    public Object showInputDialog(
        Component parentComponent,
        Object message,
        String title,
        int messageType,
        Icon icon,
        Object[] selectionValues,
        Object initialSelectionValue
    )
    {
        Object  result   = 
            JOptionPane.showInputDialog(
                parentComponent, 
                message, 
                title, 
                messageType, 
                icon, 
                selectionValues, 
                initialSelectionValue
            );
        
        return result;
    }
    
    @Override
    public String showInputDialog( Component parent, Object message )
    {
        String  result  = JOptionPane.showInputDialog( parent, message );
        return result;
    }
    
    @Override
    public void showMessageDialog( 
        Component parent, 
        Object message, 
        String title, 
        int type 
    )
    {
        JOptionPane.showMessageDialog( parent, message, title, type );
    }
}
