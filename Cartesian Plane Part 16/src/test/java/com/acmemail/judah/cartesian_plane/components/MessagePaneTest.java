package com.acmemail.judah.cartesian_plane.components;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.awt.AWTException;
import java.awt.Component;
import java.awt.Point;
import java.awt.Robot;
import java.awt.event.InputEvent;
import java.awt.geom.Rectangle2D;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.IntStream;

import javax.swing.AbstractButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JScrollPane;
import javax.swing.event.HyperlinkListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.StyleSheet;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.acmemail.judah.cartesian_plane.graphics_utils.ComponentException;
import com.acmemail.judah.cartesian_plane.graphics_utils.ComponentFinder;
import com.acmemail.judah.cartesian_plane.graphics_utils.GUIUtils;
import com.acmemail.judah.cartesian_plane.test_utils.Utils;

public class MessagePaneTest
{
    /** Directory containing resources for this test. */
    private static final String resourceDir     = "MessagePaneTest";
    /** Resource file containing text/plain formatted text. */
    private static final String   plainResource = 
        resourceDir + "/" + "PlainResource.txt";
    /** Expected contents of the text/plain resource file. */
    private static final String   expPlainText  = 
        "This is a text/plain resource.";
    
    /** Resource file containing text/html formatted text. */
    private static final String   htmlResource  = 
        resourceDir + "/" + "HTMLResource.html";
    /** Expected contents of the text/plain resource file. */
    private static final String   expHTMLText   = 
        "This is a text/html resource.";
    
    /** Resource file containing CSS. */
    private static final String   cssResource   = 
        resourceDir + "/" + "CSSResource.css";
    /** Expected contents of the text/plain resource file. */
    private static final String   expCSSRule    = "body.test";
    
    /** Resource file containing hyperlink for testing link logic. */
    private static final String   linkResource  = 
        resourceDir + "/" + "TestLinkResource.html";
    /** Expected contents of the text/plain resource file. */
    private static final String   expLinkText   = 
        "Link To Plain Text";
    
    /** Resource file containing hyperlink for testing link logic. */
    private static final String   linkAResource = 
        resourceDir + "/" + "LinkToResourceA.html";
    /** Expected contents of the link-A resource file. */
    private static final String   expLinkAText  = 
        "Link To Resource B";
    /** Expected contents of the link-B resource file. */
    private static final String   expLinkBText  = 
        "Link To Resource A";
    
    /** Number of rules it generated StyleSheet. */
    private static final int        numCSSRules = 4;
    /** StyleSheet for ad hoc testing; populated in before-all method. */
    private static final StyleSheet styleSheet  = new StyleSheet();
    /** Robot for manipulating mouse for hyperlink testing. */
    private static final Robot      robot       = getRobot();
    /** 
     * MessagePane for ad hoc testing. 
     * Created as needed by test methods.
     * Set to null in before-each method.
     * This field should be set
     * by calling getMessagePane,
     * which ensures that necessary tasks
     * are performed on the EDT.
     * 
     * @see #getMessagePane(Supplier)
     */
    private             MessagePane messagePane = null;
    /**
     * Editor pane for ad hoc testing.
     * Set by method getMessagePane.
     * Set to null in before-each method.
     */
    private             JEditorPane editorPane  = null;
    
    /** 
     * Ad hoc field for the convenience of test methods.
     * Initialized to false in before-each method.
     */
    private boolean     adHocBoolean1;
    /** 
     * Ad hoc field for the convenience of test methods.
     * Initialized to false in before-each method.
     */
    private boolean     adHocBoolean2;
    /** 
     * Ad hoc field for the convenience of test methods.
     * Initialized to null in before-each method.
     */
    private String      adHocString1;
    /** 
     * Ad hoc field for the convenience of test methods.
     * Initialized to null in before-each method.
     */
    private Object      adHocObject1;
    
    @BeforeAll
    public static void beforeAll()
    {
        String  fmt = 
            "body.%2$c { background-color: #%1$06x; font-size: %3$d; }";
        IntStream.range( 0, numCSSRules )
            .mapToObj( i  -> String.format( fmt, i, 'a' + i, i + 10 ) )
            .forEach( styleSheet::addRule );
    }
    
    @BeforeEach
    public void beforEach() throws Exception
    {
        adHocBoolean1 = false;
        adHocBoolean2 = false;
        adHocString1 = null;
        adHocObject1 = null;
        messagePane = null;
        editorPane = null;
    }

    @AfterEach
    public void afterEach() throws Exception
    {
        // Make sure that any dialog created by the preceding test
        // is disposed.
        ComponentFinder.disposeAll();
    }

    @Test
    public void testOfStringStyleSheet()
    {
        String      testString      = 
            "test string: testOfStringStyleSheet";
        getMessagePane( () -> MessagePane.of( testString, styleSheet ) );
        StyleSheet  actStyleSheet   = 
            getStyleSheet( messagePane::getStyleSheet );
        validateMessagePaneText( testString );
        validateMessagePaneType( MessagePane.HTML_TYPE );
        assertEquals( styleSheet, actStyleSheet );
    }

    @Test
    public void testOfStringString()
    {
        String      ruleName    =   "body.test";
        String      rule        =
            ruleName + " { font-size: 10; }";
        String  testString  = "test string: testOfStringString";
        getMessagePane( () -> MessagePane.of( testString, rule ) );
        validateMessagePaneText( testString );
        validateMessagePaneType( MessagePane.HTML_TYPE );
        
        StyleSheet  actSheet    = 
            getStyleSheet( messagePane::getStyleSheet );
        Style       actRule     = actSheet.getRule( ruleName );
        assertNotNull( actRule );
    }

    @Test
    public void testOfString()
    {
        String  testString  = "test string: testOfString";
        getMessagePane( () -> MessagePane.of( testString ) );
        validateMessagePaneText( testString );
        validateMessagePaneType( MessagePane.PLAIN_TYPE );
    }

    @Test
    public void testOfResourceStringPlain()
    {
        getMessagePane( () -> MessagePane.ofResource( plainResource ) );
        validateMessagePaneText( expPlainText );
        validateMessagePaneType( MessagePane.PLAIN_TYPE );
    }

    @Test
    public void testOfResourceStringHTML()
    {
        getMessagePane( () -> MessagePane.ofResource( htmlResource ) );
        validateMessagePaneText( expHTMLText );
        validateMessagePaneType( MessagePane.HTML_TYPE );
    }

    @Test
    public void testOfResourceStringString()
    {
        getMessagePane( () -> 
            MessagePane.ofResource( htmlResource, cssResource )
        );
        validateMessagePaneText( expHTMLText );
        validateMessagePaneType( MessagePane.HTML_TYPE );
        
        StyleSheet  actSheet    = 
            getStyleSheet( messagePane::getStyleSheet );
        Style       actRule     = actSheet.getRule( expCSSRule );
        assertNotNull( actRule );
    }

    @Test
    public void testOfResourceStringString2()
    {
        // same as previous test, but with null css resource
        getMessagePane( () -> 
            MessagePane.ofResource( htmlResource, null )
        );
        validateMessagePaneText( expHTMLText );
        validateMessagePaneType( MessagePane.HTML_TYPE );
    }

    @Test
    public void testSetStyleSheetStyleSheet()
    {
        getMessagePane( () -> MessagePane.ofResource( htmlResource ) );
        
        setStyleSheet( styleSheet, messagePane::setStyleSheet );
        StyleSheet  actStyleSheet   = 
            getStyleSheet( messagePane::getStyleSheet );
        assertEquals( styleSheet, actStyleSheet );
        
        Object      object      = 
            getObject( editorPane::getEditorKit );
        assertTrue( object instanceof HTMLEditorKit );
        HTMLEditorKit   editorKit   = (HTMLEditorKit)object;
        actStyleSheet = getStyleSheet( editorKit::getStyleSheet );
        assertEquals( styleSheet, actStyleSheet );
    }

    @Test
    public void testSetStyleSheetString()
    {
        String      ruleName    = "body.test";
        String      rule        = ruleName + " { font-size: 10; }";
        String      testString  = "test string: testSetStyleSheetString";
        getMessagePane( () -> MessagePane.of( testString ) );
        setString( rule, messagePane::setStyleSheet );
        
        StyleSheet  actSheet    = 
            getStyleSheet( messagePane::getStyleSheet );
        Object      actRule     = 
            getObject( ()-> actSheet.getRule( ruleName ) );
        assertTrue( actRule instanceof Style );
        assertNotNull( actRule );
    }

    @Test
    public void testSetStyleSheetFromResource()
    {
        getMessagePane( () -> MessagePane.ofResource( htmlResource ) );
        setString( cssResource, messagePane::setStyleSheetFromResource );
        StyleSheet  actSheet    = 
            getStyleSheet( messagePane::getStyleSheet );
        Object      actRule     = 
            getObject( ()-> actSheet.getRule( expCSSRule ) );
        assertTrue( actRule instanceof Style );
        assertNotNull( actRule );
    }

    @Test
    public void testGetStyleSheet()
    {
        getMessagePane( () -> MessagePane.ofResource( htmlResource ) );
        setStyleSheet( styleSheet, messagePane::setStyleSheet );
        StyleSheet  actSheet    = 
            getStyleSheet( messagePane::getStyleSheet );
        assertEquals( styleSheet, actSheet );
    }

    @Test
    public void testSetText()
    {
        String  testStr = "test string: testSetText";
        getMessagePane( () -> MessagePane.ofResource( plainResource ) );
        setString( testStr, messagePane::setText );
        validateMessagePaneText( testStr );
    }

    @Test
    public void testSetTextFromResource()
    {
        String  testStr = "test string: testSetText";
        getMessagePane( () -> MessagePane.of( testStr ) );
        setString( plainResource, messagePane::setTextFromResource );
        validateMessagePaneText( expPlainText );
    }

    @Test
    public void testGetText()
    {
        getMessagePane( () -> MessagePane.ofResource( plainResource ) );
        String  actText = getString( messagePane::getText );
        assertTrue( actText.contains( expPlainText ) );
    }

    @Test
    public void testSetContentType()
    {
        getMessagePane( () -> MessagePane.ofResource( plainResource ) );
        validateMessagePaneType( MessagePane.PLAIN_TYPE );
        setString( MessagePane.HTML_TYPE, messagePane::setContentType );
        validateMessagePaneType( MessagePane.HTML_TYPE );
    }

    @Test
    public void testGetContentType()
    {
        getMessagePane( () -> MessagePane.ofResource( plainResource ) );
        validateMessagePaneType( MessagePane.PLAIN_TYPE );
        getMessagePane( () -> MessagePane.ofResource( htmlResource ) );
        validateMessagePaneType( MessagePane.HTML_TYPE );
    }
    
    @Test
    public void testAddHyperlinkListener()
    {
        getMessagePane( () -> MessagePane.ofResource( linkResource ) );
        validateMessagePaneType( MessagePane.HTML_TYPE );
        validateMessagePaneText( expLinkText );
        
        addHyperlinkListener( e -> adHocBoolean1 = true );
        addHyperlinkListener( e -> adHocBoolean2 = true );        
        Thread  thread  = showDialog();
        selectLink( expLinkText.length() / 2 );
        closeDialog();
        Utils.join( thread );
        assertTrue( adHocBoolean1 );
        assertTrue( adHocBoolean2 );
    }

    @Test
    public void testRemoveHyperLinkListener()
    {
        int                 linkAPos    = expLinkAText.length() / 2;
        int                 linkBPos    = expLinkBText.length() / 2;
        HyperlinkListener   listenerA   = e -> adHocBoolean1 = true;
        HyperlinkListener   listenerB   = e -> adHocBoolean2 = true;
        getMessagePane( () -> MessagePane.ofResource( linkAResource ) );
        validateMessagePaneText( expLinkAText );
        addHyperlinkListener( listenerA );
        addHyperlinkListener( listenerB );
        
        // Clicking the link in this resource should take us
        // to the LinkToResourceB.html, which contains a link
        // back to linkAResource.
        Thread  thread  = showDialog();
        selectLink( linkAPos );
        validateMessagePaneText( expLinkBText );
        assertTrue( adHocBoolean1 );
        assertTrue( adHocBoolean2 );
        
        adHocBoolean1 = false;
        adHocBoolean2 = false;
        removeHyperlinkListener( listenerB );
        selectLink( linkBPos );
        validateMessagePaneText( expLinkAText );
        assertTrue( adHocBoolean1 );
        assertFalse( adHocBoolean2 );
        
        adHocBoolean1 = false;
        adHocBoolean2 = false;
        removeHyperlinkListener( listenerA );
        selectLink( linkAPos );
        validateMessagePaneText( expLinkBText );
        closeDialog();
        Utils.join( thread );
        assertFalse( adHocBoolean1 );
        assertFalse( adHocBoolean2 );
    }

    @Test
    public void testRemoveDefaultHyperlinkListener()
    {
        int                 linkAPos    = expLinkAText.length() / 2;
        int                 linkBPos    = expLinkBText.length() / 2;
        HyperlinkListener   listenerA   = e -> adHocBoolean1 = true;
        HyperlinkListener   listenerB   = e -> adHocBoolean2 = true;
        getMessagePane( () -> MessagePane.ofResource( linkAResource ) );
        validateMessagePaneText( expLinkAText );
        addHyperlinkListener( listenerA );
        addHyperlinkListener( listenerB );
        
        // Clicking the link in this resource should take us
        // to the LinkToResourceB.html, which contains a link
        // back to linkAResource.
        Thread  thread  = showDialog();
        selectLink( linkAPos );
        validateMessagePaneText( expLinkBText );
        assertTrue( adHocBoolean1 );
        assertTrue( adHocBoolean2 );
        
        // Remove the default hyperlink listener. The two local 
        // hyperlink listeners should fire, but without default
        // listener we should stay on page B.
        adHocBoolean1 = false;
        adHocBoolean2 = false;
        removeDefaultHyperlinkListener();
        selectLink( linkBPos );
        closeDialog();
        Utils.join( thread );
        validateMessagePaneText( expLinkBText );
        assertTrue( adHocBoolean1 );
        assertTrue( adHocBoolean2 );
    }

    @Test
    public void testGetEditorPane()
    {
        getMessagePane( () -> MessagePane.ofResource( plainResource ) );
        assertNotNull( editorPane );
    }

    @Test
    public void testGetScrollPane()
    {
        getMessagePane( () -> MessagePane.ofResource( plainResource ) );
        
        Object      spObject    = getObject( messagePane::getScrollPane );
        assertTrue( spObject instanceof JScrollPane );
        JScrollPane scrollPane  = (JScrollPane)spObject;
        
        Object      vObject     = 
            getObject( () -> scrollPane.getViewport().getView() );
        assertTrue( vObject instanceof JEditorPane );
        assertEquals( editorPane, vObject );
    }

    @Test
    public void testGetDialog()
    {
        getMessagePane( () -> MessagePane.ofResource( plainResource ) );
        JScrollPane expScrollPane   = messagePane.getScrollPane();
        
        JDialog     dialog          = messagePane.getDialog( null, "title" );
        Predicate<JComponent>   
                    pred            = j -> (j instanceof JScrollPane );
        JComponent  actScrollPane   = ComponentFinder.find( dialog, pred );
        assertNotNull( actScrollPane );
        assertEquals( expScrollPane, actScrollPane );
    }
    
    @Test
    public void ofTextResourceGoWrong()
    {
        Class<ComponentException>   clazz   = ComponentException.class;
        assertThrows ( clazz,
            () -> MessagePane.ofResource( "no such resource" )
        );
    }
    
    @Test
    public void textFromResourceGoWrong()
    {
        getMessagePane( () -> MessagePane.of( "Text" ) );
        Class<ComponentException>   clazz   = ComponentException.class;
        assertThrows ( clazz,
            () -> messagePane.setTextFromResource( "no such resource" )
        );
    }
    
    @Test
    public void ofCSSResourceGoWrong()
    {
        Class<ComponentException>   clazz   = ComponentException.class;
        assertThrows ( clazz,
            () -> MessagePane.ofResource( htmlResource, "no such css" )
        );
    }
    
    @Test
    public void cssFromResourceGoWrong()
    {
        getMessagePane( () -> MessagePane.of( "Text" ) );
        Class<ComponentException>   clazz   = ComponentException.class;
        assertThrows ( clazz,
            () -> messagePane.setStyleSheetFromResource( "no such css" )
        );
    }
    
    /**
     * Creates a new MessagePane on the EDT
     * using a given supplier.
     * Sets the messagePane and editorPane instance variables.
     * 
     * @param supplier  the given supplier
     */
    private void getMessagePane( Supplier<MessagePane> supplier )
    {
        GUIUtils.schedEDTAndWait( () -> {
            messagePane = supplier.get();
            editorPane = messagePane.getEditorPane();
        });
    } 
    
    /**
     * Gets the String returned by a given supplier.
     * The supplier is invoked on the EDT.
     * 
     * @param supplier  the given supplier
     * 
     * @return  the String returned by the given supplier
     */
    private String getString( Supplier<String> supplier )
    {
        GUIUtils.schedEDTAndWait( () ->
            adHocString1 = supplier.get()
        );
        
        return adHocString1;
    }
    
    /**
     * Invokes a given consumer with a given string.
     * The consumer is invoked on the EDT.
     * 
     * @param str       the given string
     * @param consumer  the given consumer
     */
    private void setString( String str, Consumer<String> consumer )
    {
        GUIUtils.schedEDTAndWait( () -> consumer.accept( str ) );
    }
    
    /**
     * Gets the object returned by a given supplier.
     * 
     * @param supplier  the given supplier
     * 
     * @return  the object returned by the given supplier
     */
    private Object getObject( Supplier<Object> supplier )
    {
        GUIUtils.schedEDTAndWait( () ->
            adHocObject1 = supplier.get()
        );

        return adHocObject1;
    }
    
    /**
     * Gets the StyleSheet returned by a given supplier.
     * The supplier is invoked on the EDT.
     * 
     * @param supplier  the given supplier
     * 
     * @return  the StyleSheet returned by the given supplier
     */
    private StyleSheet getStyleSheet( Supplier<StyleSheet> supplier )
    {
        GUIUtils.schedEDTAndWait( () -> adHocObject1 = supplier.get() );
    
        return (StyleSheet)adHocObject1;
    }
    
    /**
     * Invokes a given consumer with a given StyleSheet.
     * The consumer is invoked on the EDT.
     * 
     * @param styleSheet    the given StyleSheet
     * @param consumer      the given consumer
     */
    private void setStyleSheet( 
        StyleSheet styleSheet, 
        Consumer<StyleSheet> consumer
    )
    {
        GUIUtils.schedEDTAndWait( () ->
            consumer.accept( styleSheet )
        );
    }
    
    /**
     * Adds the given HyperlinkListener to the MessagePane.
     * The operation is performed on the EDT.
     * 
     * @param listener  the given HyperlinkListener
     */
    public void addHyperlinkListener( HyperlinkListener listener )
    {
        GUIUtils.schedEDTAndWait( () -> 
            messagePane.addHyperlinkListener( listener )
        );
    }
    
    /**
     * Removes the given HyperlinkListener from the MessagePane.
     * The operation is performed on the EDT.
     * 
     * @param listener  the given HyperlinkListener
     */
    public void removeHyperlinkListener( HyperlinkListener listener )
    {
        GUIUtils.schedEDTAndWait( () -> 
            messagePane.removeHyperlinkListener( listener )
        );
    }
    
    /**
     * Removes the default HyperlinkListener from the MessagePane.
     * The operation is performed on the EDT.
     */
    public void removeDefaultHyperlinkListener()
    {
        GUIUtils.schedEDTAndWait( () -> 
            messagePane.removeDefaultHyperlinkListener()
        );
    }
    
    /**
     * Validates the actual MessagePane type
     * against the given expected type.
     * GUI components are accessed on the EDT.
     * 
     * @param expType   the given expected type
     */
    private void validateMessagePaneType( String expType )
    {
        assertNotNull( messagePane );
        String  actType = getString( messagePane::getContentType );
        assertEquals( expType, actType );
    }
    
    /**
     * Validates the actual JEditorPane text
     * against the given expected text.
     * GUI components are accessed on the EDT.
     * 
     * @param expText   the given expected text
     */
    private void validateMessagePaneText( String expText )
    {
        assertNotNull( editorPane );
        String  actText = getString( editorPane::getText );
        assertTrue( actText.contains( expText ) );
    }
    
    /**
     * Positions the mouse at the given text position
     * in the JDialog/JEditorPane and clicks the mouse.
     * <p>
     * Precondition: the dialog is showing.
     * 
     * @param textPos   the given text position
     */
    private void selectLink( int textPos )
    {
        GUIUtils.schedEDTAndWait( () -> {
            try
            {
                Rectangle2D rect        = 
                    editorPane.modelToView2D( textPos );
                Point       screenPos   = editorPane.getLocationOnScreen();
                int         xco         = (int)rect.getX() + screenPos.x;
                int         yco         = (int)rect.getY() + screenPos.y;
                robot.mouseMove( xco, yco );
            }
            catch ( BadLocationException exc )
            {
                fail( exc );
            }
        });
        Utils.pause( 500 );
        robot.mousePress( InputEvent.BUTTON1_DOWN_MASK );
        robot.mouseRelease( InputEvent.BUTTON1_DOWN_MASK );
        Utils.pause( 500 );
    }
    
    /**
     * Starts a new thread to make the test dialog visible.
     * Note that the dialog is modal.
     * When the dialog is closed
     * the encapsulating thread is terminated. 
     * This method is intended
     * to be called
     * from the EDT.
     * 
     * @return the ID of the Thread used to make the dialog visible
     */
    private Thread showDialog()
    {
        JDialog dialog  = messagePane.getDialog( null, "MessagePane Test" );
        Thread  thread  = new Thread( () ->
            GUIUtils.schedEDTAndWait( () -> dialog.setVisible( true ) )
        );
        thread.start();
        Utils.pause( 125 );
        return thread;
    }
    
    private void closeDialog()
    {
        GUIUtils.schedEDTAndWait( () -> {
            Predicate<JComponent>   pred    = 
                ComponentFinder.getButtonPredicate( "Close" );
            JDialog     dialog  = messagePane.getDialog( null, "" );
            JComponent  comp    = ComponentFinder.find( dialog, pred );
            assertNotNull( comp );
            assertTrue( comp instanceof AbstractButton );
            ((AbstractButton)comp).doClick();
        });
    }

    /**
     * Convenience routine to instantiate a Robot
     * and perform exception handling.
     * If instantiating the Robot
     * throws an exception
     * the application is terminated.
     * 
     * @return  the instantiated robot
     */
    private static Robot getRobot()
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
}
