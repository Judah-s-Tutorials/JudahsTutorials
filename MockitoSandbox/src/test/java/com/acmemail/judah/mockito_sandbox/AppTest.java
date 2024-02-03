package com.acmemail.judah.mockito_sandbox;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AppTest
{
    private final Robot robot   = getRobot();

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
        
        System  sysMock = mock( System.class );
        App     appMock = new App();
        when ( appMock.get() ).thenReturn( -1 );
        
        System.out.println( appMock.get() );
        
//        robot.keyPress( KeyEvent.VK_ENTER );
//        robot.keyRelease( KeyEvent.VK_ENTER );
    }

    private Robot getRobot()
    {
        Robot   robot   = null;
        try
        {
            robot = new Robot();
        }
        catch ( AWTException exc )
        {
            exc.printStackTrace();
            System.exit( 1 );
        }
        return robot;
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
