package com.acmemail.judah.mockito_sandbox;

import javax.swing.JOptionPane;

public class InputDialogClass implements InputDialogInterface
{

    public InputDialogClass()
    {
        // TODO Auto-generated constructor stub
    }

    @Override
    public String showInputDialog( Object message )
    {
        String  input   = JOptionPane.showInputDialog( message );
        return input;
    }

}
