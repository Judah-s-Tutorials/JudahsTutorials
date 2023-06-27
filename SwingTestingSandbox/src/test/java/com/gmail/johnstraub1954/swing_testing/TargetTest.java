package com.gmail.johnstraub1954.swing_testing;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.util.function.Predicate;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TargetTest
{
    private final String    tempDirStr  = 
        System.getProperty( "java.io.tmpdir" );
    private final String    testFileName    = "FileManagerTest.tmp";
    private final String    testFilePath    =
        tempDirStr + File.separator + testFileName;;
    private final File      testFile        = new File( testFilePath );
    
    private String  actName     = null;

    @BeforeEach
    public void beforeEach() throws Exception
    {
        if ( testFile.exists() )
            testFile.delete();
        actName = null;
    }

    @Test
    public void testSaveOK() throws InterruptedException
    {
        assertFalse( testFile.exists() );
        Thread      thread          = start( () -> Target.save() );
        JButton     saveButton      = getButton( "Save" );
        JTextField  textField       = getTextField();
        SwingUtilities.invokeLater( () -> textField.setText( testFilePath ) );
        SwingUtilities.invokeLater( () -> saveButton.doClick() );
        thread.join();
        assertTrue( testFile.exists() );
    }

    @Test
    public void testSaveCancel() throws InterruptedException
    {
        assertFalse( testFile.exists() );
        Thread      thread          = start( () -> Target.save() );
        JButton     cancelButton    = getButton( "Cancel" );
        SwingUtilities.invokeLater( () -> cancelButton.doClick() );
        thread.join();
        assertFalse( testFile.exists() );
    }

    @Test
    public void testGetNameOK() throws InterruptedException
    {
        String      expName         = "Tweedle Dee";
        Thread      thread          = start( () -> getName() );
        JButton     okButton        = getButton( "OK" );
        JTextField  textField       = getTextField();
        SwingUtilities.invokeLater( () -> textField.setText( expName ) );
        SwingUtilities.invokeLater( () -> okButton.doClick() );
        thread.join();
        assertEquals( expName, actName );
    }

    @Test
    public void testGetNameCancel() throws InterruptedException
    {
        String      expName         = "Tweedle Dee";
        Thread      thread          = start( () -> getName() );
        JButton     okButton        = getButton( "OK" );
        JTextField  textField       = getTextField();
        SwingUtilities.invokeLater( () -> textField.setText( expName ) );
        SwingUtilities.invokeLater( () -> okButton.doClick() );
        thread.join();
        assertEquals( expName, actName );
    }
    
    private JTextField getTextField()
    {
        JComponent  textField   = 
            ComponentFinder.find( c -> (c instanceof JTextField) );
        assertNotNull( textField );
        assertTrue( textField instanceof JTextField );
        return (JTextField)textField;
    }
    
    private JButton getButton( String text )
    {
        Predicate<JComponent>   isButton    = c -> (c instanceof JButton);
        Predicate<JComponent>   hasText     =
            c -> text.equals( ((JButton)c).getText() );
        Predicate<JComponent>   pred        = isButton.and( hasText );
        JComponent              button      = 
            ComponentFinder.find( pred );
        assertNotNull( button );
        assertTrue( button instanceof JButton );
        return (JButton)button;
    }

    private Thread start( Runnable runner ) throws InterruptedException
    {
        Thread  thread  = new Thread( runner );
        thread.start();
        Thread.sleep( 500 );
        return thread;
    }
    
    private void getName()
    {
        actName = Target.getName();
    }
}
