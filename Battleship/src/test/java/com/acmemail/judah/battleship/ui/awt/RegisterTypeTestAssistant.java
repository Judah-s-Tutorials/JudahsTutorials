package com.acmemail.judah.battleship.ui.awt;

import java.util.List;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JTextField;

import com.acmemail.judah.battleship.components.ComponentFinder;
import com.acmemail.judah.battleship.components.ComponentUtils;

/**
 * Assistant to interrogate and control
 * a register-type dialog.
 * Manages component selection,
 * and makes sure interaction with the GUI
 * takes place on the EDT.
 */
public class RegisterTypeTestAssistant extends DialogTestAssistant
{
    /** The JDialog base of the register-type dialog. */
    private JDialog dialog;
    
    /**
     * Default constructor.
     * Must only be invoked after the register-type dialog
     * has been instantiated.
     */
    public RegisterTypeTestAssistant()
    {
        final String    title   = UIConstants.SHIP_TYPE_DIALOG_TITLE;
        ComponentUtils.invokeAndWait( () -> {
            dialog = ComponentFinder.getDialog( title );
            if ( dialog == null )
                throwNotFound( title );
            List<String>    errors  = 
                initMap( dialog, s -> s.startsWith(  "RDN" ) );
            errors.forEach( System.err::println );
        });
    }
    
    /**
     * Gets the JDialog at the root of the
     * register-type dialog.
     * 
     * @return  the encapsulated JDialog
     */
    public JDialog getDialog()
    {
        return dialog;
    }
    
    /**
     * Gets the value of the length component.
     * 
     * @return  the value of the length component
     * 
     * @throws IllegalStateException if the operation can't be completed
     */
    public int getLength()
    {
        int length  = getSpinnerInt( UIConstants.RDN_LENGTH );
        return length;
    }
    
    /**
     * Gets the value of the breadth component.
     * 
     * @return  the value of the breadth component
     * 
     * @throws IllegalStateException if the operation can't be completed
     */
    public int getBreadth()
    {
        int breadth = getSpinnerInt( UIConstants.RDN_BREADTH );
        return breadth;
    }
    
    /**
     * Sets the value of the length component.
     * 
     * @param length    the value to set
     * 
     * @throws IllegalStateException if the operation can't be completed
     */
    public void setLength( int length )
    {
        setSpinnerInt( length, UIConstants.RDN_LENGTH );
    }
    
    /**
     * Sets the value of the breadth component.
     * 
     * @param breadth    the value to set
     * 
     * @throws IllegalStateException if the operation can't be completed
     */
    public void setBreadth( int breadth )
    {
        setSpinnerInt( breadth, UIConstants.RDN_BREADTH );
    }
    
    /**
     * Gets the value of the type-name component.
     * 
     * @return  the value of the type-name component
     * 
     * @throws IllegalStateException if the operation can't be completed
     */
    public String getName()
    {
        JTextField  textField   = getJTextField( UIConstants.RDN_NAME );
        String[]    nameArr     = new String[1];
        ComponentUtils.invokeAndWait( () -> nameArr[0] = textField.getText() );
        return nameArr[0];
    }
    
    /**
     * Sets the value of the type-name component.
     * 
     * @param name    the value to set
     * 
     * @throws IllegalStateException if the operation can't be completed
     */
    public void setName( String name )
    {
        JTextField  textField   = getJTextField( UIConstants.RDN_NAME );
        ComponentUtils.invokeAndWait( () -> textField.setText( name ) );
    }
    
    /**
     * Get a list of messages 
     * from the dialog's message area.
     * 
     * @return  a list of messages
     * 
     * @throws IllegalStateException if the operation cannot be completed
     */
    public List<String> getMessages()
    {
        List<String>    messages    = getMessages( UIConstants.RDN_MESSAGES );
        return messages;
    }
    
    /**
     * Click the OK button
     * and wait for the dialog to close.
     * 
     * @throws IllegalStateException if the dialog fails to close
     */
    public void clickOK()
    {
        JButton comp    = getJButton( UIConstants.RDN_OK_BUTTON );
        execTerminalAction( dialog, comp::doClick );
    }
    
    /**
     * Click the Cancel button
     * and wait for the dialog to close.
     * 
     * @throws IllegalStateException if the dialog fails to close
     */
    public void clickCancel()
    {
        JButton comp    = getJButton( UIConstants.RDN_CANCEL_BUTTON );
        execTerminalAction( dialog, comp::doClick );
    }
}
