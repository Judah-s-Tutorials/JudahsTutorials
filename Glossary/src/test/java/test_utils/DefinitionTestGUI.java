package test_utils;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.awt.Window;
import java.util.function.Predicate;
import java.util.function.Supplier;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import com.judahstutorials.glossary.SeeAlso;
import com.judahstutorials.glossary.Controls.MainFrame;
import com.judahstutorials.glossary.Controls.SeeAlsoPanel;

public class DefinitionTestGUI
{
    private static DefinitionTestGUI    testGUI;
    private final SeeAlsoPanel          seeAlsoPanel;
    private final JFormattedTextField   seeAlsoNewField;
    private final JList<SeeAlso>        seeAlsoJList;
    private final JButton               seeAlsoDeleteButton;
    
    private final JFrame                mainFrame;
    private final JPanel                mainContentPane;
    private final JFormattedTextField   termIDText;
    private final JFormattedTextField   termText;
    private final JFormattedTextField   seqNumText;
    private final JFormattedTextField   slugText;
    private final JTextArea             descrText;

    private final JButton               newButton;
    private final JButton               queryButton;
    private final JButton               deleteButton;
    private final JButton               commitButton;
    private final JButton               cancelButton;
    private final JButton               exitButton;

    private DefinitionTestGUI()
    {
        mainFrame = getMainFrame();
        mainContentPane = (JPanel)mainFrame.getContentPane();
        seeAlsoPanel = getSeeAlsoPanelByName( MainFrame.SEE_ALSO_PANEL );
        seeAlsoNewField = 
            getTextFieldByName( SeeAlsoPanel.SEE_ALSO_NEW_TEXT );
        seeAlsoDeleteButton = 
            getButtonByName( SeeAlsoPanel.DELETE_BUTTON );
        seeAlsoJList = 
            (JList<SeeAlso>)getJListByName( SeeAlsoPanel.SEE_ALSO_JLIST );

        termIDText = getTextFieldByName( MainFrame.TERM_ID_FIELD );
        termText = getTextFieldByName( MainFrame.TERM_FIELD );
        seqNumText = getTextFieldByName( MainFrame.SEQ_NUM_FIELD );
        slugText = getTextFieldByName( MainFrame.SLUG_FIELD );
        descrText = getTextAreaByName( MainFrame.DESC_FIELD );

        newButton = getButtonByName( MainFrame.NEW_BUTTON );
        queryButton = getButtonByName( MainFrame.QUERY_BUTTON );
        deleteButton = getButtonByName( MainFrame.DELETE_BUTTON );
        commitButton = getButtonByName( MainFrame.COMMIT_BUTTON );
        cancelButton = getButtonByName( MainFrame.CANCEL_BUTTON );
        exitButton = getButtonByName( MainFrame.EXIT_BUTTON );
    }
    
    public static DefinitionTestGUI getDefinitionTestGUI()
    {
        if ( testGUI != null )
            ;
        else if ( SwingUtilities.isEventDispatchThread() )
            testGUI = new DefinitionTestGUI();
        else
            GUIUtils.schedEDTAndWait( () -> 
                testGUI = new DefinitionTestGUI()
            );
        return testGUI;
    }
    
    public void clickNewButton()
    {
        click( newButton );
    }
    
    public void clickQueryButton()
    {
        click( queryButton );
    }
    
    public void clickDeleteButton()
    {
        click( deleteButton );
    }
    
    public void clickCommitButton()
    {
        click( commitButton );
    }
    
    public void clickCancelButton()
    {
        click( cancelButton );
    }
    
    public void clickExitButton()
    {
        click( exitButton );
    }
    
    public void clickSeeAlsoDeleteButton()
    {
        click( seeAlsoDeleteButton );
    }
    
    public Integer getTermID()
    {
        Integer intID   = getIntFromTextProperty( 
            () -> termIDText.getValue()
        );
        return intID;
    }
    
    public String getTermText()
    {
        String  text    = getStringProperty( () -> termText.getText() );
        return text;
    }
    
    public void setTermValue( String text )
    {
        setProperty( () -> termText.setValue( text ) );
    }
    
    public Integer getSeqNum()
    {
        Integer intID   = getIntFromTextProperty( 
            () -> seqNumText.getValue()
        );
        return intID;
    }
    
    public void setSeqNum( Integer seqNum )
    {
        setProperty( () -> seqNumText.setValue( seqNum ) );
    }
    
    public String getSlugText()
    {
        String  text    = getStringProperty( () -> slugText.getText() );
        return text;
    }
    
    public void setSlugValue( String text )
    {
        setProperty( () -> termText.setValue( text ) );
    }
    
    public String getDescriptionText()
    {
        String  text    = getStringProperty( () -> descrText.getText() );
        return text;
    }
    
    public void setNewURLValue( String text )
    {
        setProperty( () -> seeAlsoNewField.setValue( text ) );
    }
    
    public boolean isGUIEnabled()
    {
        boolean enabled = isEnabled( mainContentPane );
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
        try
        {
            intVal = Integer.parseInt( text );
        }
        catch ( NumberFormatException exc )
        {
            fail( exc );
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

    private SeeAlsoPanel getSeeAlsoPanelByName( String name )
    {
        JComponent  comp    = getComponent( name );
        assertTrue( comp instanceof SeeAlsoPanel );
        return (SeeAlsoPanel)comp;
    }

    private JTextArea getTextAreaByName( String name )
    {
        JComponent  comp    = getComponent( name );
        assertTrue( comp instanceof JTextArea );
        return (JTextArea)comp;
    }

    private JFormattedTextField getTextFieldByName( String name )
    {
        JComponent  comp    = getComponent( name );
        assertTrue( comp instanceof JFormattedTextField );
        return (JFormattedTextField)comp;
    }

    private JButton getButtonByName( String name )
    {
        JComponent  comp    = getComponent( name );
        assertTrue( comp instanceof JButton );
        return (JButton)comp;
    }

    private JList<?> getJListByName( String name )
    {
        JComponent  comp    = getComponent( name );
        assertTrue( comp instanceof JList<?> );
        return (JList<?>)comp;
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
