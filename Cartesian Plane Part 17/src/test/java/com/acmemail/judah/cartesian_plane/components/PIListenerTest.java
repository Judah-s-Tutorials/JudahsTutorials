package com.acmemail.judah.cartesian_plane.components;

import java.awt.AWTException;
import java.awt.BorderLayout;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.util.stream.IntStream;

import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import com.acmemail.judah.cartesian_plane.graphics_utils.GUIUtils;
import com.acmemail.judah.cartesian_plane.test_utils.Utils;

class PIListenerTest
{
    private static JDialog      testDialog;
    private static JTextField   textField;
    private static Robot        robot;
    
    @BeforeAll
    static void setUpBeforeClass() throws Exception
    {
        GUIUtils.schedEDTAndWait( () -> showDialog() );
        System.out.println( textField.hasFocus() );
        try
        {
            robot = new Robot();
            robot.setAutoDelay( 250 );
        }
        catch ( AWTException exc )
        {
            exc.printStackTrace();
            System.exit( 1 );
        }
    }

    @AfterAll
    static void tearDownAfterClass() throws Exception
    {
        testDialog.setVisible( false );
        testDialog.dispose();
    }

    @BeforeEach
    void setUp() throws Exception
    {
        GUIUtils.schedEDTAndWait( () -> {
            testDialog.requestFocus();
            textField.grabFocus();
            System.out.println( textField.hasFocus() );
        });
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 1, 2, 3})
    public void testKeyPressedKeyEvent( int before )
    {
        GUIUtils.schedEDTAndWait( () -> {
            enterText( before, 0 );
            enterText( before, 1 );
            enterText( before, 2 );
        });
    }
    
    private void enterText( int before, int after )
    {
        textField.setText( "" );
//        IntStream.range( 0, before ).forEach( i -> type( KeyEvent.VK_A ) );
        type( KeyEvent.VK_P );
        type( KeyEvent.VK_P );
        type( KeyEvent.VK_P );
        type( KeyEvent.VK_P );
        type( KeyEvent.VK_P );
        type( KeyEvent.VK_I );
        IntStream.range( 0, after ).forEach( i -> type( KeyEvent.VK_A ) );
        textField.setCaretPosition( before );
        System.out.println( textField.getText() + ", " + textField.getCaretPosition() );
//        typeCtrlP();
        Utils.pause( 1000 );
    }
    
    private void typeCtrlP()
    {
        robot.keyPress( KeyEvent.VK_CONTROL );
        robot.keyPress( KeyEvent.VK_P );
        robot.keyRelease( KeyEvent.VK_P );
        robot.keyRelease( KeyEvent.VK_CONTROL );
    }
    
    private void type( int vkCode )
    {
        robot.keyPress( vkCode );
        robot.keyRelease( vkCode );
        textField.repaint();
    }

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
    }
}
