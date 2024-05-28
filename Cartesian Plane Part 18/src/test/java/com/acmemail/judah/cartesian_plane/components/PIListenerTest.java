package com.acmemail.judah.cartesian_plane.components;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.awt.AWTException;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Robot;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.util.stream.IntStream;

import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import com.acmemail.judah.cartesian_plane.graphics_utils.GUIUtils;

class PIListenerTest
{
    /** Char for miscellaneous testing; can be anything but 'p' or 'i' */
    private static final char   testChar        = 'a';
    /** KeyCode of testChar. */
    private static final int    testCharKeyCode = KeyEvent.VK_A;
    /** Unicode for the Greek letter PI */
    private static final char   pii             = '\u03c0';

    private static JDialog      testDialog;
    private static JTextField   textField;
    private static Robot        robot;
    
    @BeforeAll
    static void setUpBeforeClass() throws Exception
    {
        try
        {
            robot = new Robot();
            robot.setAutoDelay( 100 );
        }
        catch ( AWTException exc )
        {
            exc.printStackTrace();
            System.exit( 1 );
        }
        GUIUtils.schedEDTAndWait( () -> showDialog() );
    }

    @AfterAll
    static void tearDownAfterClass() throws Exception
    {
        GUIUtils.schedEDTAndWait( () -> {
            testDialog.setVisible( false );
            testDialog.dispose();
        });
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 1, 2, 3})
    public void testKeyPressedKeyEventLong( int before )
    {
        enterText( before, 0 );
        enterText( before, 1 );
        enterText( before, 2 );
    }
    
    /**
     * Type 'before' test chars,
     * then "pi" then 'after' test chars.
     * Position the cursor immediately after the "pi"
     * and type control-P.
     * Verify that "pi" is replaced
     * by the Greek letter &Pi;,
     * and that the rest of the text
     * was unaffected.
     * 
     * @param before    number of test chars to type before "pi"
     * @param after     number of test chars to type after "pi"
     */
    private void enterText( int before, int after )
    {
        textField.setText( "" );
        
        StringBuilder   expText = new StringBuilder();
        IntStream.range( 0, before )
            .peek( i -> expText.append( testChar ) )
            .forEach( i -> type( testCharKeyCode ) );
        type( KeyEvent.VK_P );
        type( KeyEvent.VK_I );
        expText.append( pii );
        IntStream.range( 0, after )
            .peek( i -> expText.append( testChar ) )
            .forEach( i -> type( testCharKeyCode ) );
        textField.setCaretPosition( before + 2 );
        typeCtrlP();
        
        assertEquals( expText.toString(), textField.getText() );
    }
    
    /**
     * Simulates typing ^P.
     */
    private void typeCtrlP()
    {
        robot.keyPress( KeyEvent.VK_CONTROL );
        robot.keyPress( KeyEvent.VK_P );
        robot.keyRelease( KeyEvent.VK_P );
        robot.keyRelease( KeyEvent.VK_CONTROL );
    }
    
    /**
     * Simulates typing the given key
     * without any key modifiers.
     * 
     * @param vkCode    the given key
     */
    private void type( int vkCode )
    {
        robot.keyPress( vkCode );
        robot.keyRelease( vkCode );
    }

    /**
     * Builds the test dialog and makes it visible.
     * Simulates a mouse click in the encapsulated text field.
     */
    private static void showDialog()
    {
        testDialog = new JDialog();
        textField = new JTextField( 10 );
        textField.addKeyListener( new PIListener() );
        JPanel  contentPane = new JPanel( new BorderLayout() );
        contentPane.add( textField, BorderLayout.CENTER );
        testDialog.setContentPane( contentPane );
        testDialog.pack();
        testDialog.setVisible( true );
        
        Point       pos     = textField.getLocationOnScreen();
        Dimension   size    = textField.getSize();
        pos.x += size.width / 2;
        pos.y += size.height / 2;
        robot.mouseMove( pos.x, pos.y );
        robot.mousePress( InputEvent.BUTTON1_DOWN_MASK );
        robot.mouseRelease( InputEvent.BUTTON1_DOWN_MASK );
    }
}
