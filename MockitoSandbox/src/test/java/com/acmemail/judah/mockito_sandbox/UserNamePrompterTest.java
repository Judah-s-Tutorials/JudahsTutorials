package com.acmemail.judah.mockito_sandbox;

import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class UserNamePrompterTest
{

    @Test
    void testAskForUsername()
    {
        InputDialogInterface    mocker  = 
            Mockito.mock( InputDialogInterface.class );
        UserNamePrompter    prompter    = new UserNamePrompter( mocker );
        when( mocker.showInputDialog("Enter username:") )
            .thenReturn( "Jorge" );
        String  input   = prompter.askForUsername();
        System.out.println( input );
    }

}
