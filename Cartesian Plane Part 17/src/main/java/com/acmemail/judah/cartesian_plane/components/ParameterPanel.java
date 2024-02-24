package com.acmemail.judah.cartesian_plane.components;

import java.awt.Color;
import java.awt.GridLayout;
import java.text.ParseException;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

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

public class ParameterPanel extends JPanel
{
    private static final Equation       testEquation    = 
        new Exp4jEquation();
    
    private Equation            equation    = null;
    
    private JFormattedTextField start       = 
        getExprTextField( this::setStart );
    private JFormattedTextField end         = 
        getExprTextField( this::setEnd );
    private JFormattedTextField step        = 
        getDecimalTextField( this::setStep );
    private JFormattedTextField precision   = 
        getIntegerTextField( this::setPrecision );
    private JFormattedTextField radius      = 
        getNameTextField( this::setRadiusName );
    private JFormattedTextField theta       = 
        getNameTextField( this::setThetaName );
    private JFormattedTextField param       = 
        getNameTextField( this::setParam );
    
    public ParameterPanel()
    {
        super( new GridLayout( 8, 2 ) );
        Border  border  = BorderFactory.createEmptyBorder( 3, 3, 3, 3 );
        setBorder( border );
        
        start.setColumns( 5 );
        end.setColumns( 5 );
        step.setColumns( 5 );
        precision.setColumns( 5 );
        
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
    
    public void load( Equation equation )
    {
        this.equation = equation;
        start.setValue( equation.getRangeStart() );
        end.setValue( equation.getRangeEnd() );
        step.setValue( equation.getRangeStep() );
        radius.setValue( equation.getRadiusName() );
        theta.setValue( equation.getThetaName() );
        param.setValue( equation.getParamName() );
        precision.setValue( equation.getPrecision() );
    }
    
    private JFormattedTextField 
    getDecimalTextField( Consumer<Double> consumer )
    {
        AbstractFormatter   formatter   = 
            new ValueFormatter( Double::parseDouble );
        JFormattedTextField textField   =
            new JFormattedTextField( formatter );
        textField.setHorizontalAlignment( SwingConstants.RIGHT );
        textField.setInputVerifier( new FieldVerifier() );
        
        textField.addPropertyChangeListener( "value", e -> {
            Object  value   = e.getNewValue();
            if ( equation != null  )
                consumer.accept( (Double)(value) );
        });
        return textField;
    }
    
    private JFormattedTextField 
    getIntegerTextField( Consumer<Integer> consumer )
    {
        AbstractFormatter   formatter   = 
            new ValueFormatter( Integer::parseInt );
        JFormattedTextField textField   = 
            new JFormattedTextField( formatter );
        textField.setHorizontalAlignment( SwingConstants.RIGHT );
        textField.setInputVerifier( new FieldVerifier() );
        
        textField.addPropertyChangeListener( "value", e -> {
            Object  value   = e.getNewValue();
            if ( equation != null  )
                consumer.accept( (Integer)(value) );
        });
        return textField;
    }
    
    private JFormattedTextField 
    getExprTextField( Consumer<String> consumer )
    {
        AbstractFormatter   formatter   = 
            new ValueFormatter( ParameterPanel::parseExpression );
        JFormattedTextField textField   = 
            new JFormattedTextField( formatter );
        textField.setHorizontalAlignment( SwingConstants.RIGHT );
        textField.setInputVerifier( new FieldVerifier() );
        
        textField.addPropertyChangeListener( "value", e -> {
            Object  value   = e.getNewValue();
            if ( equation != null  )
                consumer.accept( value.toString() );
        });
        return textField;
    }
    
    private JFormattedTextField 
    getNameTextField( Consumer<String> consumer )
    {
        AbstractFormatter   formatter   = new NameFormatter();
        JFormattedTextField textField   = 
            new JFormattedTextField( formatter );
        textField.setHorizontalAlignment( SwingConstants.RIGHT );
        textField.setInputVerifier( new FieldVerifier() );
        
        textField.addPropertyChangeListener( "value", e -> {
            if ( equation != null )
                consumer.accept( e.getNewValue().toString() );
        });
        
        return textField;
    }
    
    private void setRadiusName( String name )
    {
        equation.setRadiusName( name );
    }
    
    private void setThetaName( String name )
    {
        equation.setThetaName( name );
    }
    
    private void setParam( String name )
    {
        equation.setParam( name );
    }
    
    private void setStart( String val )
    {
        Optional<Double>  dVal    = equation.evaluate( val );
        if ( dVal.isPresent() )
            equation.setRangeStart( dVal.get() );
    }
    
    private void setEnd( String val )
    {
        Optional<Double>  dVal    = equation.evaluate( val );
        if ( dVal.isPresent() )
            equation.setRangeEnd( dVal.get() );
    }
    
    private void setStep( Double val )
    {
        equation.setRangeStep( val );
    }
    
    private void setPrecision( Integer val )
    {
        equation.setPrecision( val );
    }
    
    private static Double parseExpression( String str )
        throws NumberFormatException
    {
        Optional<Double>    opt     = testEquation.evaluate( str );
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
            Number  num = null;
            try
            {
                num = validator.apply( str );
                fmtField.setForeground( Color.BLACK );
            }
            catch ( NumberFormatException exc )
            {
                fmtField.setForeground( Color.RED );
                throw new ParseException( "Invalid name", 0 );
            }
            return num;
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
