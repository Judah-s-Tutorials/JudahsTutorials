package com.acmemail.judah.mockito_sandbox;

public class UserNamePrompter
{
    private final InputDialogInterface  jOptionPane;
    
    public UserNamePrompter()
    {
        this( new InputDialogClass() );
    }
    
    public UserNamePrompter( InputDialogInterface getter )
    {
        jOptionPane = getter;
    }

    public String askForUsername()
    {
        String username = jOptionPane.showInputDialog( "Enter username:" );
        if ( username == null || username.isBlank() )
        {
            throw new IllegalArgumentException( "Username required" );
        }
        return username;
    }}

