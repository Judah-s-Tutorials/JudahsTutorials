package com.acmemail.judah.cartesian_plane.components;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.text.ParseException;
import java.util.function.Predicate;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.InputVerifier;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.text.DefaultFormatter;

import com.acmemail.judah.cartesian_plane.input.Equation;
import com.acmemail.judah.cartesian_plane.input.Result;

public class PlotPanel extends JPanel
{
    private static final String[]   plotTypes   =
    { "YPlot", "XYPlot", "RPlot", "TPlot" };

    private JFormattedTextField xEquals     = 
        new JFormattedTextField( new ExprFormatter( this::setXExpr ) );
    private JFormattedTextField yEquals     = 
        new JFormattedTextField( new ExprFormatter( this::setYExpr ) );
    private JFormattedTextField rEquals     = 
        new JFormattedTextField( new ExprFormatter( this::setRExpr ) );
    private JFormattedTextField tEquals     = 
        new JFormattedTextField( new ExprFormatter( this::setTExpr ) );
    private JComboBox<String>   plots       = new JComboBox<>( plotTypes );
    
    private Equation            equation    = null;
    
    public PlotPanel()
    {
        super();
        BoxLayout   layout  = new BoxLayout( this, BoxLayout.X_AXIS );
        setLayout( layout );
        
        Border  border  = BorderFactory.createEmptyBorder( 3, 3, 3, 3 );
        setBorder( border );
        
        xEquals.setColumns( 15 );
        yEquals.setColumns( 5 );
        rEquals.setColumns( 5 );
        tEquals.setColumns( 5 );
                
        add( getExprPanel() );
        add( getComboPanel() );
    }
    
    public void load( Equation equation )
    {
        this.equation = equation;
        
        ExprVerifier    verifier        = new ExprVerifier();
        xEquals.setInputVerifier( verifier );
        yEquals.setInputVerifier( verifier );
        rEquals.setInputVerifier( verifier );
        tEquals.setInputVerifier( verifier );
        
        xEquals.setValue( equation.getXExpression() );
        yEquals.setValue( equation.getYExpression() );
        rEquals.setValue( equation.getRExpression() );
        tEquals.setValue( equation.getTExpression() );
        plots.setSelectedItem( equation.getPlot() );
    }
    
    private JPanel getExprPanel()
    {
        JPanel      panel   = new JPanel( new GridLayout( 2, 2 ) );
        panel.add( getExprPanel( "x=", xEquals ) );
        panel.add( getExprPanel( "r=", rEquals ) );
        panel.add( getExprPanel( "y=", yEquals ) );
        panel.add( getExprPanel( "t=", tEquals ) );
        return panel;
    }
    
    private JPanel getExprPanel( String text, JComponent comp )
    {
        JPanel      panel   = new JPanel();
        BoxLayout   layout  = new BoxLayout( panel, BoxLayout.X_AXIS );
        panel.setLayout( layout );
        
        JLabel  label   = new JLabel( text );
        panel.add( label );
        panel.add( comp );
        return panel;
    }
    
    private JPanel  getComboPanel()
    {
        JPanel      panel   = new JPanel();
        BoxLayout   layout  = new BoxLayout( panel, BoxLayout.Y_AXIS );
        panel.setLayout( layout );
        
        JButton     button  = new JButton( "Plot" );
        panel.add( plots );
        button.addActionListener( this::plotAction );
        panel.add( button );
        return panel;
    }
    
    private void plotAction( ActionEvent evt )
    {
        String  item    = plots.getSelectedItem().toString().toUpperCase();
        if ( item.equals( "YPLOT" ) )
            equation.yPlot();
    }
    
    private boolean setXExpr( String expr )
    {
        Result  result  = equation.setXExpression( expr );
        return result.isSuccess();
    }
    
    private boolean setYExpr( String expr )
    {
        Result  result  = equation.setYExpression( expr );
        return result.isSuccess();
    }
    
    private boolean setRExpr( String expr )
    {
        Result  result  = equation.setRExpression( expr );
        return result.isSuccess();
    }
    
    private boolean setTExpr( String expr )
    {
        Result  result  = equation.setTExpression( expr );
        return result.isSuccess();
    }
    
    private class ExprVerifier extends InputVerifier
    {
        @Override
        public boolean verify(JComponent comp)
        {
            JFormattedTextField textField   = (JFormattedTextField)comp;
            String              text        = textField.getText();
            boolean             status      = true;
            if ( !text.isEmpty() )
                status =
                    equation.isValidExpression( text );          
            return status;
        }
    }
    @SuppressWarnings("serial")
    private class ExprFormatter extends DefaultFormatter
    {
        private final Predicate<String> pred;
        
        public ExprFormatter( Predicate<String> pred )
        {
            this.pred = pred;
            setOverwriteMode( false );
        }
        
        @Override
        public String stringToValue( String str )
            throws ParseException
        {
            JFormattedTextField fmtField    = getFormattedTextField();
            if ( equation != null && str != null && !str.isEmpty() )
            {
                if ( !pred.test( str ) )
                {
                    fmtField.setForeground( Color.RED );
                    throw new ParseException( "Invalid name", 0 );
                }
            }
            fmtField.setForeground( Color.BLACK );
            return str;
        }
    }}
