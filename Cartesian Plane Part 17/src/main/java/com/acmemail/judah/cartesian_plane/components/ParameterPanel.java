package com.acmemail.judah.cartesian_plane.components;

import java.awt.Color;
import java.awt.GridLayout;
import java.text.ParseException;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Stream;

import javax.swing.BorderFactory;
import javax.swing.InputVerifier;
import javax.swing.JComponent;
import javax.swing.JFormattedTextField;
import javax.swing.JFormattedTextField.AbstractFormatter;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.text.DefaultFormatter;

import com.acmemail.judah.cartesian_plane.input.Equation;
import com.acmemail.judah.cartesian_plane.input.Exp4jEquation;

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
    /** The currently open equation; null if none. */
    private Equation            equation    = null;
    
    /** The text field for managing the range-start parameter. */
    private JFormattedTextField start       = 
        getExprTextField( e -> getEquation().setRangeStart( e ) );
    /** The text field for managing the range-end parameter. */
    private JFormattedTextField end         = 
        getExprTextField( e -> getEquation().setRangeEnd( e ) );
    /** The text field for managing the range-step parameter. */
    private JFormattedTextField step        = 
        getExprTextField( e -> getEquation().setRangeStep( e ) );
    /** The text field for managing the precision parameter. */
    private JFormattedTextField precision   = 
        getIntegerTextField( i -> getEquation().setPrecision( i ) );
    /** The text field for managing the radius name parameter. */
    private JFormattedTextField radius      = 
        getNameTextField( n -> getEquation().setRadiusName( n ) );
    /** The text field for managing the theta name parameter. */
    private JFormattedTextField theta       = 
        getNameTextField( n -> getEquation().setThetaName( n ) );
    /** The text field for managing the param name parameter. */
    private JFormattedTextField param       = 
        getNameTextField( n -> getEquation().setParam( n ) );
    
    /**
     * Constructor.
     * Fully initializes this panel.
     */
    public ParameterPanel()
    {
        super( new GridLayout( 8, 2 ) );
        Border  border  = BorderFactory.createEmptyBorder( 3, 3, 3, 3 );
        setBorder( border );
        
        add( new JLabel( "Start" ) );
        add( new JLabel( "End" ) );
        add( start );
        add( end );
        add( new JLabel( "Step" ) );
        add( new JLabel( "Prec" ) );
        add( step );
        add( precision );
        add( new JLabel( "Radius" ) );
        add( new JLabel( "Theta" ) );
        add( radius );
        add( theta );
        add( new JLabel( "Param" ) );
        add( new JLabel( "" ) );
        add( param );
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
        boolean newState    = equation != null;
        if ( newState )
        {
            start.setValue( equation.getRangeStartExpr() );
            end.setValue( equation.getRangeEndExpr() );
            step.setValue( equation.getRangeStep() );
            radius.setValue( equation.getRadiusName() );
            theta.setValue( equation.getThetaName() );
            param.setValue( equation.getParamName() );
            precision.setValue( equation.getPrecision() );
        }
        Stream.of( start, end, step, radius, theta, param, precision )
            .forEach( c -> c.setEnabled( newState ) );
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
     * Gets a JFormattedTextField
     * suitable for managing an integer value.
     * 
     * @param consumer
     * 
     * @return JFormattedTextField configured for integers
     */
    private JFormattedTextField 
    getIntegerTextField( Consumer<Integer> consumer )
    {
        AbstractFormatter   formatter   = 
            new ValueFormatter( Integer::parseInt );
        JFormattedTextField textField   = 
            new JFormattedTextField( formatter );
        textField.setColumns( 5 );
        textField.setHorizontalAlignment( SwingConstants.RIGHT );
        textField.setInputVerifier( new FieldVerifier() );
        
        textField.addPropertyChangeListener( "value", e -> {
            Object  value   = e.getNewValue();
            if ( equation != null  )
                consumer.accept( (Integer)(value) );
        });
        return textField;
    }
    
    /**
     * Gets a JFormattedTextField
     * suitable for managing expressions.
     * 
     * @param consumer
     * 
     * @return JFormattedTextField configured for expressions
     */
    private JFormattedTextField 
    getExprTextField( Consumer<String> consumer )
    {
        AbstractFormatter   formatter   = 
            new ValueFormatter( this::parseExpression );
        JFormattedTextField textField   = 
            new JFormattedTextField( formatter );
        textField.setColumns( 5 );
        textField.setHorizontalAlignment( SwingConstants.RIGHT );
        textField.setInputVerifier( new FieldVerifier() );
        textField.addKeyListener( new PIListener() );
        
        textField.addPropertyChangeListener( "value", e -> {
            Object  value   = e.getNewValue();
            if ( equation != null  )
                consumer.accept( value.toString() );
        });
        return textField;
    }
    
    /**
     * Gets a JFormattedTextField
     * suitable for managing names.
     * 
     * @param consumer
     * 
     * @return JFormattedTextField configured for names
     */
    private JFormattedTextField 
    getNameTextField( Consumer<String> consumer )
    {
        AbstractFormatter   formatter   = new NameFormatter();
        JFormattedTextField textField   = 
            new JFormattedTextField( formatter );
        textField.setColumns( 5 );
        textField.setHorizontalAlignment( SwingConstants.RIGHT );
        textField.setInputVerifier( new FieldVerifier() );
        
        textField.addPropertyChangeListener( "value", e -> {
            if ( equation != null )
                consumer.accept( e.getNewValue().toString() );
        });
        
        return textField;
    }
    
    /**
     * Parses and evaluates a given expression.
     * 
     * @param str   the given expression
     * 
     * @return  the value of the expression
     * 
     * @throws NumberFormatException    
     *      If there is no equation,
     *      or the given expression is invalid.
     */
    private Double parseExpression( String str )
        throws NumberFormatException
    {
        if ( equation == null )
            throw new NumberFormatException( "Invalid equation" );
        Optional<Double>    opt     = equation.evaluate( str );
        if ( opt.isEmpty() )
            throw new NumberFormatException( "Invalid expression" );
        return opt.get();
    }
    
    private static class ValueFormatter extends DefaultFormatter
    {
        private static final long serialVersionUID = 5606928606415501983L;
        private final Function<String,Number>   validator;
        public ValueFormatter( Function<String,Number> validator )
        {
            this.validator = validator;
            setOverwriteMode( false );
        }
        @Override
        public Object stringToValue( String str )
            throws ParseException
        {
            JFormattedTextField fmtField    = getFormattedTextField();
            try
            {
                Number  num = validator.apply( str );
                fmtField.setForeground( Color.BLACK );
            }
            catch ( NumberFormatException exc )
            {
                fmtField.setForeground( Color.RED );
                throw new ParseException( "Invalid name", 0 );
            }
            return str;
        }
    }
    
    private static class NameFormatter extends DefaultFormatter
    {
        public NameFormatter()
        {
            setOverwriteMode( false );
        }
        @Override
        public String stringToValue( String str )
            throws ParseException
        {
            JFormattedTextField fmtField    = getFormattedTextField();
            if ( !NameValidator.isIdentifier( str ) )
            {
                fmtField.setForeground( Color.RED );
                throw new ParseException( "Invalid name", 0 );
            }
            fmtField.setForeground( Color.BLACK );
            return str;
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
