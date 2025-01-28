package test_utils;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.awt.Window;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;
import java.util.function.Supplier;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.text.JTextComponent;

import com.judahstutorials.glossary.Definition;
import com.judahstutorials.glossary.Controls.MainFrame;
import com.judahstutorials.glossary.Controls.SeeAlsoPanel;

public class MainFrameTestGUI
{
    private static MainFrameTestGUI     testGUI;
    private final JFrame                mainFrame;
    private final JPanel                mainContentPane;
    
    private final Map<String,JComponent>    componentMap    = new HashMap<>();

    private MainFrameTestGUI()
    {   
        mainFrame = getMainFrame();
        mainContentPane = (JPanel)mainFrame.getContentPane();
        getSeeAlsoPanelByName( MainFrame.SEE_ALSO_PANEL );
        getTextFieldByName( SeeAlsoPanel.SA_NEW_TEXT );
        getButtonByName( SeeAlsoPanel.SA_DELETE_BUTTON );
        getJListByName( SeeAlsoPanel.SA_JLIST );

        getTextFieldByName( MainFrame.TERM_ID_FIELD );
        getTextFieldByName( MainFrame.TERM_FIELD );
        getTextFieldByName( MainFrame.SEQ_NUM_FIELD );
        getTextFieldByName( MainFrame.SLUG_FIELD );
        getTextAreaByName( MainFrame.DESC_FIELD );

        getButtonByName( MainFrame.NEW_BUTTON );
        getButtonByName( MainFrame.QUERY_BUTTON );
        getButtonByName( MainFrame.DELETE_BUTTON );
        getButtonByName( MainFrame.COMMIT_BUTTON );
        getButtonByName( MainFrame.CANCEL_BUTTON );
        getButtonByName( MainFrame.EXIT_BUTTON );
    }
    
    public static MainFrameTestGUI getMainFrameTestGUI()
    {
        if ( testGUI != null )
            ;
        else if ( SwingUtilities.isEventDispatchThread() )
            testGUI = new MainFrameTestGUI();
        else
            GUIUtils.schedEDTAndWait( () -> 
                testGUI = new MainFrameTestGUI()
            );
        return testGUI;
    }
    
    public boolean isVisible()
    {
        return mainFrame.isVisible();
    }
    
    public void click( String name )
    {
        JComponent  comp    = componentMap.get( name );
        assertNotNull( comp );
        assertTrue( comp instanceof JButton );
        click( (JButton)comp );
    }
    
    public Integer getIDFromText( String name )
    {
        JComponent  comp    = componentMap.get( name );
        assertNotNull( comp );
        assertTrue( comp instanceof JFormattedTextField );
        JFormattedTextField termIDText  = (JFormattedTextField)comp;
        Integer intID   = getIntFromTextProperty( 
            () -> termIDText.getText()
        );
        return intID;
    }
    
    public String getText( String name )
    {
        Supplier<Object>    getter  = null;
        JComponent          comp    = componentMap.get( name );
        assertNotNull( comp );
        assertTrue( comp instanceof JTextComponent );
        if ( comp instanceof JFormattedTextField )
            getter = () -> ((JFormattedTextField)comp).getValue();
        else
            getter = () -> ((JTextComponent)comp).getText();
        String              text    = getStringProperty( getter );
        return text;
    }
    
    public void setText( String name, String text )
    {
        Runnable    consumer    = null;
        JComponent  comp        = componentMap.get( name );
        assertNotNull( comp );
        assertTrue( comp instanceof JTextComponent );
        if ( comp instanceof JFormattedTextField )
            consumer = () -> ((JFormattedTextField)comp).setValue( text );
        else
            consumer = () -> ((JTextComponent)comp).setText( text );
        setProperty( consumer );
    }
    
    public void setValue( String name, String value )
    {
        JComponent  comp        = componentMap.get( name );
        assertNotNull( comp );
        assertTrue( comp instanceof JFormattedTextField );
        setProperty( () -> ((JFormattedTextField)comp).setValue( value ) );
    }
    
    public void setValue( String name, Integer value )
    {
        JComponent  comp        = componentMap.get( name );
        assertNotNull( comp );
        assertTrue( comp instanceof JFormattedTextField );
        setProperty( () -> ((JFormattedTextField)comp).setValue( value ) );
    }
    
    public boolean isGUIEnabled()
    {
        boolean enabled = isEnabled( mainContentPane );
        return enabled;
    }
    
    public boolean isEnabled( String name )
    {
        JComponent  comp        = componentMap.get( name );
        assertNotNull( comp );
        boolean     enabled     = isEnabled( comp );
        return enabled;
    }
    
    private String getStringProperty( Supplier<Object> supplier )
    {
        Object  object  = getProperty( supplier );
        assertTrue( object instanceof String );
        return (String)object;
    }
    
    private boolean isEnabled( JComponent comp )
    {
        boolean enabled = getBooleanProperty( () -> comp.isEnabled() );
        return enabled;
    }
    
    private boolean getBooleanProperty( Supplier<Object> supplier )
    {
        Object  object  = getProperty( supplier );
        assertTrue( object instanceof Boolean );
        return (boolean)object;
    }
    
    private Integer getIntFromTextProperty( Supplier<Object> supplier )
    {
        String  text    = getStringProperty( supplier );
        Integer intVal  = null;
        if ( text != null && !text.isEmpty() )
        {
            try
            {
                intVal = Integer.parseInt( text );
            }
            catch ( NumberFormatException exc )
            {
                fail( exc );
            }
        }
        return intVal;
    }
    
    private void setProperty( Runnable runner )
    {
        GUIUtils.schedEDTAndWait( runner );        
    }
    
    private Object getProperty( Supplier<Object> supplier )
    {
        Object[]    object  = new Object[1];
        GUIUtils.schedEDTAndWait( () -> object[0] = supplier.get() );
        assertNotNull( object[0] );
        return object[0];
    }

    private void click( JButton button )
    {
        GUIUtils.schedEDTAndWait( () -> button.doClick() );
    }
    
    private JFrame getMainFrame()
    {
        boolean             canBeFrame      = true;
        boolean             canBeDialog     = false;
        boolean             mustBeVisible   = true;
        ComponentFinder     finder          =
            new ComponentFinder( canBeDialog, canBeFrame, mustBeVisible );
        Predicate<Window>   pred            = w -> true;
        Window              window          = finder.findWindow( pred );
        assertNotNull( window );
        assertTrue( window instanceof JFrame );
        return (JFrame)window;
    }

    private void getSeeAlsoPanelByName( String name )
    {
        JComponent  comp    = getComponent( name );
        assertTrue( comp instanceof SeeAlsoPanel );
        componentMap.put( name, comp );
    }

    private void getTextAreaByName( String name )
    {
        JComponent  comp    = getComponent( name );
        assertTrue( comp instanceof JTextArea );
        componentMap.put( name, comp );
    }

    private void getTextFieldByName( String name )
    {
        JComponent  comp    = getComponent( name );
        assertTrue( comp instanceof JFormattedTextField );
        componentMap.put( name, comp );
    }

    private void getButtonByName( String name )
    {
        JComponent  comp    = getComponent( name );
        assertTrue( comp instanceof JButton );
        componentMap.put( name, comp );
    }

    private void getJListByName( String name )
    {
        JComponent  comp    = getComponent( name );
        assertTrue( comp instanceof JList<?> );
        componentMap.put( name, comp );
    }
    
    private JComponent getComponent( String name )
    {
        Predicate<JComponent>   pred    = c -> name.equals( c.getName() );
        JComponent              comp    = 
            ComponentFinder.find( mainContentPane, pred );
        assertNotNull( comp );
        return comp;
    }
}
