package com.acmemail.judah.cartesian_plane.test_utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import javax.swing.JComponent;
import javax.swing.JFormattedTextField;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.TableModel;

import com.acmemail.judah.cartesian_plane.CartesianPlane;
import com.acmemail.judah.cartesian_plane.components.CPFrame;
import com.acmemail.judah.cartesian_plane.components.NamePanel;
import com.acmemail.judah.cartesian_plane.components.ParameterPanel;
import com.acmemail.judah.cartesian_plane.components.PlotPanel;
import com.acmemail.judah.cartesian_plane.components.VariablePanel;
import com.acmemail.judah.cartesian_plane.graphics_utils.ComponentFinder;
import com.acmemail.judah.cartesian_plane.graphics_utils.GUIUtils;
import com.acmemail.judah.cartesian_plane.input.Equation;

public class CPFrameTestGUI
{
    /** 
     * Default variable name for collecting sample value
     * for VariablePanel class.
     */
    public static final String  DEFAULT_VAR_NAME        = "a";
    
    /** CPFrame under test. */
    private final CPFrame                   cpFrame     =
        new CPFrame();
    
    /** Map of panel type to sample JComponent for that type. */
    private final Map<Class<?>,JComponent>  sampleMap   =
        new HashMap<>();
    
    /** 
     * Map of panel type to Supplier for obtaining sample
     * property value from currently open equation.
     */
    private final Map<Class<?>,Supplier<String>>    supplierMap =
        new HashMap<>();
    
    /** Currently open equation; null if none. */
    private Equation                        equation;

    /** Temporary object for limited use by lambdas. */
    private Object  adHocObject1;

    /**
     * Constructor.
     * Creates and shows the test GUI.
     * Must be invoked invoked from EDT.
     */
    public CPFrameTestGUI()
    {
        Stream.of(
            NamePanel.class, 
            VariablePanel.class, 
            ParameterPanel.class,
            PlotPanel.class
        ).forEach( this::mapComponent );
        
        supplierMap.put( NamePanel.class, () -> equation.getName() );
        supplierMap.put( VariablePanel.class, () -> getValue() );
        supplierMap.put( ParameterPanel.class, () -> 
            equation.getRangeStartExpr() 
        );
        supplierMap.put( PlotPanel.class, () -> 
            equation.getXExpression() 
        );
    }
    
    /**
     * Loads a new equation into the CPFrame under test.
     * Pass null to close any open equation.
     * map
     * @param equation  the new equation, or null if none
     */
    public void newEquation( Equation equation )
    {
        this.equation = equation;
        cpFrame.loadEquation( equation );
    }
    
    /**
     * Calls the CPFrame's getEquation method 
     * and returns the result.
     * 
     * @return the value returned by CPFrame.getEquation.
     */
    public Equation getEquation()
    {
        Object  obj = getProperty( () -> cpFrame.getEquation() );
        assertTrue( obj instanceof Equation );
        return (Equation)obj;
    }
    
    /**
     * Calls the CPFrame's getCartesianPlane method 
     * and returns the result.
     * 
     * @return the value returned by CPFrame.getCartesianPlane.
     */
    public CartesianPlane getCartesianPlane()
    {
        Object  obj = getProperty( () -> cpFrame.getCartesianPlane() );
        assertTrue( obj instanceof CartesianPlane );
        return (CartesianPlane)obj;
    }
    
    /**
     * Determines if all the panels of the CPFrame
     * that are under test should be considered enabled.
     * 
     * @return  true if all panels under test are considered enabled
     */
    public boolean isEnabled()
    {
        boolean result  = sampleMap.values().stream()
            .map( this::isEnabled )
            .filter( b -> !b )
            .findFirst().orElse( true );
        return result;
    }
    
    /**
     * Determines if all the panels of the CPFrame
     * that are under test should be considered disabled.
     * 
     * @return  true if all panels under test are considered disabled
     */
    public boolean isDisabled()
    {
        boolean result  = sampleMap.values().stream()
            .map( this::isEnabled )
            .filter( b -> b )
            .findFirst().orElse( true );
        return result;
    }
    
    /**
     * Get the text from the text field
     * of the sample component
     * associated with the given class.
     * <p>
     * Precondition: 
     * the sample component
     * associated with the given class
     * is type JFormattedTextField.
     * 
     * @param clazz the given class
     * 
     * @return  
     *      the text of the text field
     *      associated with the given class
     */
    public String getText( Class<? extends JPanel> clazz )
    {
        JComponent          comp        = sampleMap.get( clazz );
        assertNotNull( comp );
        assertTrue( comp instanceof JFormattedTextField );
        JFormattedTextField textField   = (JFormattedTextField)comp;
        Object  obj = getProperty( () -> textField.getText() );
        assertTrue( obj instanceof String );
        return (String)obj;
    }
    
    public String getValue()
    {
        double  dValue  = getValue( DEFAULT_VAR_NAME );
        String  sValue  = String.valueOf( dValue );
        return sValue;
    }
    
    /**
     * Get from the VariablePanel's JTable
     * the value of the variable with the given name.
     * <p>
     * Precondition:
     * The variable with the given name
     * is present in the JTable.
     * 
     * @param varName   the given name
     * 
     * @return  the value of the variable with the given name
     * 
     * @see #getValueEDT(String)
     */
    public double getValue( String varName )
    {
        Object  prop    = getProperty( () -> getValueEDT( varName ) );
        assertTrue( prop instanceof Double );
        return (double)prop;
    }
    
    /**
     * Determine if the given component
     * is enabled.
     * 
     * @param comp  the given component
     * 
     * @return  
     *      true if the component is enabled
     */
    public boolean isEnabled( JComponent comp )
    {
        Object      obj     = getProperty( () -> comp.isEnabled() );
        assertTrue( obj instanceof Boolean );
        return (boolean)obj;
    }
    
    /**
     * Helper method for {@linkplain #getValue(String)}.
     * Gets the value of the variable with the given name.
     * 
     * @param varName   given name
     * 
     * @return  the value of the variable with the given name
     * 
     * @see #getValue(String)
     */
    private double getValueEDT( String varName )
    {
        JComponent  comp        = sampleMap.get( VariablePanel.class );
        assertNotNull( comp );
        assertTrue( comp instanceof JTable );
        TableModel  model       = ((JTable)comp).getModel();
        int         rows        = model.getRowCount();
        assertEquals( 2, model.getColumnCount() );
        
        int         row         = 
            IntStream.range( 0, rows )
                .filter( r -> varName.equals( model.getValueAt( r, 0 ) ) )
                .findFirst()
                .orElse( -1 );
        
        assertTrue( row >= 0 );
        Object      oVal        = model.getValueAt( row, 1 );
        assertTrue( oVal instanceof Double );
        double      dVal        = (double)oVal;
        return dVal;
    }
    
    /**
     * Gets the value provided by the given supplier.
     * The operation is executed on the EDT.
     * 
     * @param supplier  the given supplier
     * 
     * @return  the value provided by the given supplier
     */
    private Object getProperty( Supplier<Object> supplier )
    {
        GUIUtils.schedEDTAndWait( () -> adHocObject1 = supplier.get() );
        return adHocObject1;
    }
    
    /**
     * Finds the sample component
     * associated with the given class
     * and adds it to the class/component map.
     * 
     * @param clazz the given class
     */
    private void mapComponent( Class<? extends JPanel> clazz )
    {
        JPanel                  panel   = getPanel( clazz );
        JComponent              comp    = null;
        if ( clazz == VariablePanel.class )
            comp = getTable( panel );
        else
            comp = getTextField( panel );
        sampleMap.put( clazz, comp );
    }

    /**
     * Gets the first JFormattedTextField
     * found in the given source.
     * <p>
     * Precondition: 
     * The first text field in the NamePanel
     * is the editor for the equation name property
     * <p>
     * Precondition: 
     * The first text field in the ParamelPanel
     * is the editor for the equation start property
     * <p>
     * Precondition: 
     * The first text field in the PlotPanel
     * is the editor for the equation xExpression property
     * 
     * @param source    the given source
     * 
     * @return  the first JFormattedTextField found in the given source
     */
    private JFormattedTextField getTextField( JPanel source )
    {
        Predicate<JComponent>   pred    = 
            c -> (c instanceof JFormattedTextField );
        JComponent  comp    = ComponentFinder.find( source, pred );
        assertTrue( comp instanceof JFormattedTextField );
        return (JFormattedTextField)comp;
    }
    
    /**
     * Gets the JTable from the VariablePanel.
     * 
     * @return  the JTable from the VariablePanel
     */
    private JTable getTable( JPanel source )
    {
        Predicate<JComponent>   pred    = 
            c -> (c instanceof JTable );
        JComponent  comp    = ComponentFinder.find( source, pred );
        assertTrue( comp instanceof JTable );
        return (JTable)comp;
    }
    
    /**
     * Gets from the CPFrame
     * the component with the type 
     * of the given JPanel subclass.
     * 
     * @param clazz the given JPanel subclass
     * 
     * @return  
     *      the component with the type of the given JPanel subclass
     */
    private JPanel getPanel( Class<? extends JPanel> clazz )
    {
        Predicate<JComponent>   pred    = c -> c.getClass() == clazz;
        JComponent              comp    = 
            ComponentFinder.find( cpFrame, pred );
        assertNotNull( comp );
        assertTrue( comp instanceof JPanel );
        return (JPanel)comp;
    }
}
