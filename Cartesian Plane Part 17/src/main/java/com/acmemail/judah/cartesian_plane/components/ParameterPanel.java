package com.acmemail.judah.cartesian_plane.components;

import java.awt.Color;
import java.awt.GridLayout;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Optional;
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
    
    private JFormattedTextField start       = getExprTextField();
    private JFormattedTextField end         = getExprTextField();
    private JFormattedTextField step        = getDecimalTextField();
    private JFormattedTextField precision   = getIntegerTextField();
    private JFormattedTextField radius      = new JFormattedTextField();
    private JFormattedTextField theta       = new JFormattedTextField();
    private JFormattedTextField param       = new JFormattedTextField();
    
    private Equation            equation    = null;
    
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
        start.setValue( equation.getRangeStart() );
        end.setValue( equation.getRangeEnd() );
        step.setValue( equation.getRangeStep() );
        radius.setValue( equation.getRadiusName() );
        theta.setValue( equation.getThetaName() );
        param.setValue( equation.getParamName() );
        precision.setValue( equation.getPrecision() );
    }
    
    private static JFormattedTextField getDecimalTextField()
    {
        AbstractFormatter   formatter   = 
            new NumberFormatter( Double::parseDouble );
        JFormattedTextField textField   =
            new JFormattedTextField( formatter );
        textField.setHorizontalAlignment( SwingConstants.RIGHT );
        textField.setInputVerifier( new FieldVerifier() );
        return textField;
    }
    
    private static JFormattedTextField getIntegerTextField()
    {
        AbstractFormatter   formatter   = 
            new NumberFormatter( Integer::parseInt );
        JFormattedTextField textField   = 
            new JFormattedTextField( formatter );
        textField.setHorizontalAlignment( SwingConstants.RIGHT );
        return textField;
    }
    
    private static JFormattedTextField getExprTextField()
    {
        AbstractFormatter   formatter   = 
            new NumberFormatter( ParameterPanel::parseExpression );
        JFormattedTextField textField   = 
            new JFormattedTextField( formatter );
        textField.setHorizontalAlignment( SwingConstants.RIGHT );
        return textField;
    }
    
    private static Double parseExpression( String str )
        throws NumberFormatException
    {
        Optional<Double>    opt     = testEquation.evaluate( str );
        if ( opt.isEmpty() )
            throw new NumberFormatException( "Invalid expression" );
        return opt.get();
    }
    
    private static class NumberFormatter extends DefaultFormatter
    {
        private static final long serialVersionUID = 5606928606415501983L;
        private final Function<String,Number>   validator;
        public NumberFormatter( Function<String,Number> validator )
        {
            this.validator = validator;
            setOverwriteMode( false );
        }
        @Override
        public String stringToValue( String str )
            throws ParseException
        {
            JFormattedTextField fmtField    = getFormattedTextField();
            try
            {
                validator.apply( str );
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
