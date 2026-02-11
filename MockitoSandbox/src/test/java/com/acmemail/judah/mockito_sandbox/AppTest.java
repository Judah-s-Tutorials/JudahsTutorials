package com.acmemail.judah.mockito_sandbox;

import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AppTest
{

    @BeforeAll
    static void setUpBeforeClass() throws Exception
    {
    }

    @BeforeEach
    void setUp() throws Exception
    {
    }

    @Test
    void test()
    {
        App.main( null );
        pause( 3000 );
        App     appMock = new App();
        when ( appMock.get() ).thenReturn( -1 );
        
        System.out.println( appMock.get() );
        
//        robot.keyPress( KeyEvent.VK_ENTER );
//        robot.keyRelease( KeyEvent.VK_ENTER );
    }

    private void pause( long millis )
    {
        try
        {
            Thread.sleep( millis );
        }
        catch ( InterruptedException exc )
        {
            
        }
    }
}
