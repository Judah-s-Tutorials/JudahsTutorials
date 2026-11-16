package com.acmemail.judah.battleship.ui.awt;

import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.function.Predicate;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JList;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;

import com.acmemail.judah.battleship.BattleshipException;
import com.acmemail.judah.battleship.components.ComponentFinder;
import com.acmemail.judah.battleship.components.ComponentUtils;

/**
 * This class provides general assistance
 * for testing application dialogs.
 * It is typically the superclass of a class
 * for testing a specific dialog.
 */
public class DialogTestAssistant
{
    /** Map of component names to the JComponents they identify. */
    private Map<String,JComponent>  jcMap;
    
    /**
     * Default constructor.
     */
    public DialogTestAssistant()
    {
    }
    
    /**
     * Initializes the component map using component names from UIConstants.
     * Attempts to map all names that match the given name filter.
     * 
     * @param dialog        the dialog to search for components
     * @param nameFilter    the given name filter
     * 
     * @return  a list of errors encountered during processing
     */
    public List<String> initMap( JDialog dialog, Predicate<String> nameFilter )
    {
        List<String>    errors  = new ArrayList<>();
        jcMap = getComponentMap( dialog, nameFilter, errors );
        return errors;
    }
    
    /**
     * Get a list of error messages 
     * from the text area component with the given name.
     * 
     * @param name  = the given name, may not be null
     * 
     * @return  a list of error messages
     * 
     * @throws IllegalStateException if the operation cannot be completed
     * @throws NullPointerException if name is null
     */
    public List<String> getMessages( String name )
    {
        Objects.requireNonNull( name, "name" );
        final Class<JTextArea>  clazz       = JTextArea.class;
        JTextArea               textArea    = getComponent( name, clazz );
        String[]                text        = new String[1];
        
        ComponentUtils.invokeAndWait( () -> text[0] = textArea.getText() );
        String[]                linesArr    = text[0].split( "\\r?\\n" );

        List<String>            linesList   = text[0].isBlank() ?
            Collections.emptyList() : Arrays.asList( linesArr );

        return linesList;
    }

    /**
     * Execute an action that should terminate the given dialog,
     * then wait for the dialog to close.
     * 
     * @param dialog    the given dialog
     * @param action    the terminal action to execute
     * 
     * @throws IllegalStateException 
     *      if the dialog fails to close in a timely fashion 
     */
    public void execTerminalAction( JDialog dialog, Runnable action )
    {
        waitForClosedEvent( dialog, action );
    }
    
    /**
     * Execute an action that should open the given dialog,
     * then wait for the dialog to open.
     * 
     * @param dialog        the given dialog
     * @param openAction    the action to execute
     * 
     * @throws IllegalStateException 
     *      if the dialog fails to open in a timely fashion 
     */
    public static void waitForOpenEvent( JDialog dialog, Runnable openAction )
    {
        CountDownLatch  shown = new CountDownLatch( 1 );
        ComponentListener   openListener    =
            new ComponentAdapter() {
                @Override
                public void componentShown( ComponentEvent e ) {
                    shown.countDown();
                }
            };

        try
        {
            dialog.addComponentListener( openListener );
            ComponentUtils.invokeLater( openAction::run );
            if ( !shown.await( 10, TimeUnit.SECONDS) )
            {
                throw new IllegalStateException("Dialog never became visible");
            }
        }
        catch ( InterruptedException exc )
        {
            String  error   = 
                "wait-for-open-event failed with an unexpected exception";
            Thread.currentThread().interrupt();
            throw new IllegalStateException( error, exc );
        }
        finally
        {
            dialog.removeComponentListener( openListener );
        }
    }
    
    /**
     * Executes the given action,
     * and waits for the given dialog to close.
     * This is a noop if the dialog is not visible.
     * Throws an exception if the wait times-out.
     * 
     * @param dialog        the given dialog
     * @param closeAction   the given action
     * 
     * @throws IllegalStateException if the operation cannot be completed
     */
    public void waitForClosedEvent( JDialog dialog, Runnable closeAction )
    {
        CountDownLatch      closed  = new CountDownLatch( 1 );
        ComponentListener   closeListener    =
            new ComponentAdapter() {
                @Override
                public void componentHidden( ComponentEvent e ) {
                    closed.countDown();
                }
        };

        try
        {
            dialog.addComponentListener( closeListener );
            ComponentUtils.invokeLater( closeAction::run );
            if ( !closed.await( 5, TimeUnit.SECONDS) )
            {
                throw new IllegalStateException(" Dialog failed to close" );
            }
        }
        catch ( InterruptedException exc )
        {
            String  error   = 
                "wait-for-close-event failed with an unexpected exception";
            Thread.currentThread().interrupt();
            throw new IllegalStateException( error, exc );
            
        }
        finally
        {
            dialog.removeComponentListener( closeListener );
        }
    }

    /**
     * Gets the value of the JSpinner with the given name.
     * 
     * @param  name the given name
     * 
     * @return the integer value of the given JSpinner
     * 
     * @throws IllegalStateException if the operation can't be completed
     */
    public int getSpinnerInt( String name )
    {
        JSpinner    spinner = getJSpinner( name );
        Object[]    oVal    = new Object[1];
        int         iVal    = 0;
        ComponentUtils.invokeAndWait( () -> oVal[0] = spinner.getValue() );
        if ( oVal[0] instanceof Number num )
            iVal = num.intValue();
        else
            throw new IllegalStateException( "JSpinner is wrong type" );
        return iVal;
    }
    
    /**
     * Sets the value of the JSpinner with the given name.
     * 
     * @param name  the given name
     * @param iVal  the value to set
     * 
     * @throws IllegalStateException if the operation can't be completed
     */
    public void setSpinnerInt( int iVal, String name )
    {
        JSpinner    spinner = getJSpinner( name );
        if ( !(spinner.getModel() instanceof SpinnerNumberModel ) )
            throw new IllegalStateException( "JSpinner is wrong type" );
        ComponentUtils.invokeAndWait( () -> spinner.setValue( iVal ) );
    }

    /**
     * Gets the JButton with the given name.
     * 
     * @param name  the given name
     * 
     * @return  the corresponding component
     * 
     * @throws IllegalStateException 
     *      if the corresponding component is the wrong type
     */
    public JButton getJButton( String name )
    {
        final Class<JButton>    clazz   = JButton.class;
        JButton                 comp    = getComponent( name, clazz );
        return comp;
    }
    
    /**
     * Gets the JSpinner with the given name.
     * 
     * @param name  the given name
     * 
     * @return  the corresponding component
     * 
     * @throws IllegalStateException 
     *      if the corresponding component is the wrong type
     */
    public JSpinner getJSpinner( String name )
    {
        final Class<JSpinner>   clazz   = JSpinner.class;
        JSpinner                comp    = getComponent( name, clazz );
        return comp;
    }
    
    /**
     * Gets the JTextField with the given name.
     * 
     * @param name  the given name
     * 
     * @return  the corresponding component
     * 
     * @throws IllegalStateException 
     *      if the corresponding component is the wrong type
     */
    public JTextField getJTextField( String name )
    {
        final Class<JTextField> clazz   = JTextField.class;
        JTextField              comp    = getComponent( name, clazz );
        return comp;
    }
    
    /**
     * Gets the JList with the given name.
     * 
     * @param name  the given name
     * 
     * @return  the corresponding component
     * 
     * @throws IllegalStateException 
     *      if the corresponding component is the wrong type
     */
    public JList<?> getJList( String name )
    {
        @SuppressWarnings("rawtypes")
        final Class<JList>  clazz   = JList.class;
        JList<?>            comp    = getComponent( name, clazz );
        return comp;
    }

    /**
     * Get the component with the given name.
     * 
     * @param name  the given name
     * 
     * @return  the corresponding component
     * 
     * @throws IllegalStateException if the component is not found
     */
    public JComponent getComponent( String name )
    {
        JComponent  comp    = jcMap.get( name );
        if ( comp == null )
        {
            String error    = "Failed to find component: " + name;
            throw new IllegalStateException( error );
        }
        return comp;
    }
    
    /**
     * Obtain a JComponent with a given name and type.
     * Throws and exception 
     * if the component can't be found,
     * or if the component with the given name is the wrong type.
     * 
     * @param <T>       the given type
     * @param name      the given name
     * @param clazz     
     *      the expected {@code class} class for type T
     *      
     * @return  the component with the given name and type
     * 
     * @throws IllegalStateException 
     *      if the component can't be found or it's the wrong type
     */
    public <T extends JComponent> T getComponent( String name, Class<T> clazz )
    {
        // Note: getComponent(String) is guaranteed not to return null
        JComponent  comp    = getComponent( name );
        if ( !clazz.isAssignableFrom( comp.getClass() ) )
            throwWrongType( comp, clazz );
        return clazz.cast( comp );
    }

    /**
     * Throws an exception indicating that the given component 
     * was not the expected type.
     * 
     * @param comp      given component
     * @param expected  the expected type
     * 
     * @throws IllegalStateException    always
     */
    public void throwWrongType( JComponent comp, Class<?> expected )
    {
        throwWrongType(
            comp.getName(),
            expected.getSimpleName(),
            comp.getClass()
        );
            
    }
    
    /**
     * Formulate an error message from the name of a component,
     * the expected type, and the actual type;
     * throw the error message in an IllegalStateException.
     * 
     * @param name      the name of the component
     * @param expected  the expected type of the component
     * @param clazz     the actual type of the component
     * 
     * @throws IllegalStateException when invoked
     */
    public void throwWrongType( String name, String expected, Class<?> clazz )
    {
        String  actual  = clazz.getSimpleName();
        String  error   = 
            String.format( "%s: expected %s, was %s", name, expected, actual );
        throw new IllegalStateException( error );
    }
    
    /**
     * Formulate a "not found" error message from the name of a component;
     * throw the error message in an IllegalStateException.
     * 
     * @param name      the name of the component
     * 
     * @throws IllegalStateException when invoked
     */
    public void throwNotFound( String name )
    {
        String  error   = 
            String.format( "%s: component not found", name );
        throw new IllegalStateException( error );
    }
    
    /**
     * Get a list of all component names from {@link UIConstants}
     * that match the given name filter.
     * 
     * @param nameFilter    the given name filter
     * 
     * @return  a list of all component names in UIConstants
     * 
     * @throws BattleshipException if UIConstants cannot be parsed
     */
    public static List<String> getComponentNames( Predicate<Field> nameFilter )
    {
        Class<UIConstants>  clazz   = UIConstants.class;
        Field[]             fields  = clazz.getDeclaredFields();
        List<String>        list    =
            Arrays.stream( fields )
                .filter( DialogTestAssistant::isConstantString )
                .filter( nameFilter::test )
                .map( DialogTestAssistant::getStringValue )
                .filter( Objects::nonNull )
                .toList();
        
        return list;
    }
    
    /**
     * Gets an unmodifiable map of component names
     * and their corresponding components
     * within a given JDialog.
     * If an error occurs
     * it is logged to {@code errors}
     * and then ignored.
     * Attempts will be made to map all names matching the given name filter.
     * 
     * @param dialog        the given JDialog
     * @param nameFilter    the given name filter
     * @param errors        container for returning errors to the caller
     * 
     * @return  
     *      an unmodifiable map of component names
     *      and their corresponding components
     */
    public static Map<String,JComponent> getComponentMap( 
        JDialog dialog, 
        Predicate<String> nameFilter,
        List<String> errors
    )
    {
        Predicate<Field>    fieldFilter = f -> nameFilter.test( f.getName() );
        String  failedToFind    = "Failed to find component: ";
        String  duplicateName   = "Duplicate name: ";
        Map<String,JComponent>  map = new HashMap<>();
        getComponentNames( fieldFilter ).forEach( s -> {
            JComponent  comp    = ComponentFinder.getComponent( dialog, s );
            if ( comp == null )
                errors.add( failedToFind + s );
            else if ( map.containsKey( s ) )
                errors.add( duplicateName + s );
            else
                map.put( s, comp );
        });
        
        return Collections.unmodifiableMap( map );
    }
    
    /**
     * Return true if the given field encapsulates a constant string.
     * 
     * @param field the given field
     * 
     * @return  true if field encapsulates a constant string
     */
    private static boolean isConstantString( Field field )
    {
        int     modifiers   = field.getModifiers();
        boolean result      = false;
        if ( 
            Modifier.isPublic( modifiers )
            && Modifier.isFinal( modifiers )
            && Modifier.isStatic( modifiers )
        )
            result = field.getType() == String.class;
        return result;
    }
    
    /**
     * Get the value of a static String field.
     * Returns null if the given field does not contain
     * a String value.
     * 
     * @param field the given Field
     * 
     * @return  
     *      the value of a static String field, 
     *      or null if field doesn't encapsulate a String value
     * 
     * @throws BattleshipException if the operation fails
     */
    private static String getStringValue( Field field )
    {
        String  result  = null;
        try
        {
            if ( field.get( null ) instanceof String strVal )
                result = strVal;
        }
        catch ( IllegalAccessException | IllegalArgumentException exc )
        {
            throw new BattleshipException( "unexpected exception", exc );
        }
        return result;
    }
}
