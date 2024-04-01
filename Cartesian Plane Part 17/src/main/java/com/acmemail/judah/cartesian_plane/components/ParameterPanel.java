package com.acmemail.judah.cartesian_plane.components;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.beans.PropertyChangeEvent;
import java.text.ParseException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;

import javax.swing.BorderFactory;
import javax.swing.InputVerifier;
import javax.swing.JComponent;
import javax.swing.JFormattedTextField;
import javax.swing.JFormattedTextField.AbstractFormatter;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.text.DefaultFormatter;

import com.acmemail.judah.cartesian_plane.CPConstants;
import com.acmemail.judah.cartesian_plane.PropertyManager;
import com.acmemail.judah.cartesian_plane.input.Equation;

/**
 * This panel allows the operator
 * to configure equation parameters
 * associated with plotting on the Cartesian plane.
 * The parameters are:
 * <ul>
 * <li>
 *      <em>Start,</em> an expression indicating
 *      the start of a range for iterating over a plot.
 * </li>
 * <li>
 *      <em>End,</em> an expression indicating
 *      the end of a range for iterating over a plot.
 * </li>
 * <li>
 *      <em>Step,</em> an expression indicating
 *      the incremental value for iterating range.
 * </li>
 * <li>
 *      <em>Precision,</em> an integer that determines
 *      how many decimals are present in the display
 *      of a floating point value in the VariablePanel.
 * </li>
 * <li>
 *      <em>Radius,</em> the name of the variable
 *      used to represent the radius in a polar equation.
 *      Must be a valid identifier,
 *      and included in the list of declared names
 *      for the enclosing equation
 *      (see {@linkplain VariablePanel}).
 * </li>
 * <li>
 *      <em>Theta,</em> the name of the variable
 *      used to represent the angle in a polar equation.
 *      Must be a valid identifier,
 *      and included in the list of declared names
 *      for the enclosing equation
 *      (see {@linkplain VariablePanel}).
 * </li>
 * <li>
 *      <em>Parameter,</em> the name of the variable
 *      used to represent the parameter in a parametric equation.
 *      Must be a valid identifier,
 *      and included in the list of declared names
 *      for the enclosing equation
 *      (see {@linkplain VariablePanel}).
 * </li>
 * </ul>
 * @author Jack Straub
 */
@SuppressWarnings("serial")
public class ParameterPanel extends JPanel
{
    /** Font to use in a text field when its value is committed. */
    private static final Font       committedFont   = 
        UIManager.getFont( "FormattedTextField.font" );
    /** Font to use in a text field when its value is not committed. */
    private static final Font       uncommittedFont =
        committedFont.deriveFont( Font.ITALIC );
    /** Color to use for valid text. */
    private static final Color      validColor      =
        UIManager.getColor( "FormattedTextField.foreground" );
    /** Color to use for invalid text. */
    private static final Color      invalidColor    = Color.RED;

    /** The currently open equation; null if none. */
    private Equation            equation    = null;
    
    private TextFieldDescriptor[]   descArray  = new TextFieldDescriptor[]
    {
        new TextFieldDescriptor(
            () -> getEquation().getRangeStartExpr(),
            e -> getEquation().setRangeStart( (String)e ), 
            s -> parseExpression( s ),
            "Start"
        ),
        new TextFieldDescriptor(
            () -> getEquation().getRangeEndExpr(),
            e -> getEquation().setRangeEnd( (String)e ), 
            s -> parseExpression( s ),
            "End"
        ),
        new TextFieldDescriptor(
            () -> getEquation().getRangeStepExpr(),
            e -> getEquation().setRangeStep( (String)e ), 
            s -> parseExpression( s ),
            "Step"
        ),
        new TextFieldDescriptor(
            () -> getEquation().getPrecision(),
            e -> getEquation().setPrecision( (Integer)e ), 
            s -> parseInt( s ),
            "Prec"
        ),
        new TextFieldDescriptor(
            () -> getEquation().getRadiusName(),
            e -> getEquation().setRadiusName( (String)e ), 
            s -> parseName( s ),
            "Radius"
        ),
        new TextFieldDescriptor(
            () -> getEquation().getThetaName(),
            e -> getEquation().setThetaName( (String)e ), 
            s -> parseName( s ),
            "Theta"
        ),
        new TextFieldDescriptor(
            () -> getEquation().getParamName(),
            e -> getEquation().setParam( (String)e ), 
            s -> parseName( s ),
            "Param"
        ),
    };
    
    private final Map<String,TextFieldDescriptor>   fieldMap    =
        new HashMap<>();
    
    /**
     * Constructor.
     * Fully initializes this panel.
     */
    public ParameterPanel()
    {
        super( new GridLayout( 8, 2 ) );
        fillMap();
        
        Border  border  = BorderFactory.createEmptyBorder( 3, 3, 3, 3 );
        setBorder( border );
        
        add( new JLabel( "Start" ) );
        add( new JLabel( "End" ) );
        add( fieldMap.get( "Start" ).textField );
        add( fieldMap.get( "End" ).textField );
        add( new JLabel( "Step" ) );
        add( new JLabel( "Prec" ) );
        add( fieldMap.get( "Step" ).textField );
        add( fieldMap.get( "Prec" ).textField );
        add( new JLabel( "Radius" ) );
        add( new JLabel( "Theta" ) );
        add( fieldMap.get( "Radius" ).textField );
        add( fieldMap.get( "Theta" ).textField );
        add( new JLabel( "Param" ) );
        add( new JLabel( "" ) );
        add( fieldMap.get( "Param" ).textField );
        add( new JLabel( "" ) );
    }
    
    /**
     * Sets a new equation as the current equation.
     * To close the current equation pass null.
     * 
     * @param equation  the new equation or null
     */
    public void load( Equation equation )
    {
        this.equation = equation;
        boolean                         newState    = equation != null;
        Collection<TextFieldDescriptor> values      = fieldMap.values();
        if ( newState )
            values.forEach( 
                f -> f.textField.setValue( f.getter.get() )
            );
        values.stream()
            .map( f -> f.textField )
            .forEach( tf -> tf.setEnabled( newState ) );
    }
    
    private void fillMap()
    {
        Stream.of( descArray )
            .forEach( d -> fieldMap.put( d.label, d ) );
        
        int         right       = SwingConstants.RIGHT;
        PIListener  keyListener = new PIListener();
        Stream.of( "Start", "End", "Step" )
            .map( fieldMap::get )
            .map( d -> d.textField )
            .peek( tf -> tf.setHorizontalAlignment( right ) )
            .forEach( tf -> tf.addKeyListener( keyListener ) );
        fieldMap.get( "Prec" ).textField.setHorizontalAlignment( right );
    }
    
    /**
     * Returns the currently open equation;
     * null if none.
     * 
     * @return  the currently open equation
     */
    private Equation getEquation()
    {
        return equation;
    }
    
    /**
     * Parses and evaluates a given expression.
     * 
     * @param str   the given expression
     * 
     * @return  the value of the expression
     * 
     * @throws IllegalArgumentException    
     *      If there is no equation,
     *      or the given expression is invalid.
     */
    private String parseExpression( String str )
        throws IllegalArgumentException
    {
        if ( equation == null )
            throw new IllegalArgumentException( "Invalid equation" );
        Optional<Double>    opt     = equation.evaluate( str );
        if ( opt.isEmpty() )
            throw new IllegalArgumentException( "Invalid expression" );
        return str;
    }
    
    /**
     * Parses and evaluates a given name.
     * 
     * @param str   the given name
     * 
     * @return  the name
     * 
     * @throws IllegalArgumentException    
     *      if the given name is invalid
     */
    private String parseName( String str )
    {
        if ( !NameValidator.isIdentifier( str ) )
            throw new IllegalArgumentException( "not a valid name" );
        return str;
    }
    
    /**
     * Converts a given string to an integer.
     * 
     * @param str   the given string
     * 
     * @return  the integer
     * 
     * @throws IllegalArgumentException    
     *      if the given string is invalid
     */
    private Integer parseInt( String str )
    {
        int iValue  = Integer.parseInt( str );
        return iValue;
    }
    
    private class TextFieldDescriptor
    {
        public final Supplier<Object>       getter;
        public final Consumer<Object>       setter;
        public final String                 label;
        public final JFormattedTextField    textField;
        public TextFieldDescriptor(
            Supplier<Object> getter, 
            Consumer<Object> setter, 
            Function<String, Object> validator,
            String label
        )
        {
            super();
            this.getter = getter;
            this.setter = setter;
            this.label = label;

            AbstractFormatter   formatter   = 
                new FieldFormatter( validator );
            textField = new JFormattedTextField( formatter );
            textField.setColumns( 5 );
            textField.setInputVerifier( new FieldVerifier() );
            textField.addPropertyChangeListener( this::propertyChange );
        }
        
        private void propertyChange( PropertyChangeEvent evt )
        {
            if ( !evt.getPropertyName().equals( "value" ) )
                return;
            Object  src     = evt.getSource();
            if ( src instanceof JFormattedTextField )
            {
                Object              value   = evt.getNewValue();
                JFormattedTextField comp    = (JFormattedTextField)src;
                comp.setFont( committedFont );
                PropertyManager.INSTANCE
                    .setProperty( CPConstants.DM_MODIFIED_DV, true );
                if ( equation != null  )
                    setter.accept( value );
            }
        }
    }
    
    private static class FieldFormatter extends DefaultFormatter
    {
        private final Function<String,Object>   validator;
        public FieldFormatter( Function<String,Object> validator )
        {
            this.validator = validator;
            setOverwriteMode( false );
        }
        @Override
        public Object stringToValue( String str )
            throws ParseException
        {
            JFormattedTextField fmtField    = getFormattedTextField();
            Object  value   = 0;
            if ( !str.isEmpty() )
            {
                try
                {
                    value = validator.apply( str );
                    fmtField.setForeground( validColor );
                    if ( value.equals( fmtField.getValue() ) )
                        fmtField.setFont( committedFont );
                    else
                        fmtField.setFont( uncommittedFont );
                }
                catch ( IllegalArgumentException exc )
                {
                    fmtField.setFont( uncommittedFont );
                    fmtField.setForeground( invalidColor );
                    throw new ParseException( "Invalid name", 0 );
                }
            }
            return value;
        }
    }
    
    private static class FieldVerifier extends InputVerifier
    {
        @Override
        public boolean verify( JComponent comp )
        {
            JFormattedTextField textField   = (JFormattedTextField)comp;
            String              text        = textField.getText();
            AbstractFormatter   formatter   = textField.getFormatter();
            boolean             status      = false;
            try 
            {
                formatter.stringToValue( text );
                status = true;
            }
            catch ( ParseException exc )
            {
            }
            
            return status;
        }
    }
}
