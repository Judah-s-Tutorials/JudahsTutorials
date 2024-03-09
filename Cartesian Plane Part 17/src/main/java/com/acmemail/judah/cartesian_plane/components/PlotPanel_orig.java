package com.acmemail.judah.cartesian_plane.components;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.geom.Point2D;
import java.text.ParseException;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Stream;

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

import com.acmemail.judah.cartesian_plane.CPConstants;
import com.acmemail.judah.cartesian_plane.CartesianPlane;
import com.acmemail.judah.cartesian_plane.NotificationManager;
import com.acmemail.judah.cartesian_plane.PlotPointCommand;
import com.acmemail.judah.cartesian_plane.input.Command;
import com.acmemail.judah.cartesian_plane.input.Equation;
import com.acmemail.judah.cartesian_plane.input.Result;

public class PlotPanel_orig extends JPanel
{
    private static final long serialVersionUID = -40771877652622561L;

    private static final Command[]  plotTypes   =
    { Command.YPLOT, Command.XYPLOT, Command.RPLOT, Command.TPLOT };
    
    private final ExprVerifier  verifier    = new ExprVerifier();

    private JFormattedTextField xEquals     = 
        getTextField( s -> getEquation().setXExpression( s ) );
    private JFormattedTextField yEquals     = 
        getTextField( s -> getEquation().setYExpression( s ) );
    private JFormattedTextField rEquals     = 
        getTextField( s -> getEquation().setRExpression( s ) );
    private JFormattedTextField tEquals     = 
        getTextField( s -> getEquation().setTExpression( s ) );
    private JComboBox<Command>   plots      = new JComboBox<>( plotTypes );
    private JButton              plot       = new JButton( "Plot" );
    
    private Equation            equation    = null;
    private CartesianPlane      cartPlane   = null;

    
    public PlotPanel_orig()
    {
        super();
        BoxLayout   layout  = new BoxLayout( this, BoxLayout.X_AXIS );
        setLayout( layout );
        
        Border  border  = BorderFactory.createEmptyBorder( 3, 3, 3, 3 );
        setBorder( border );

        add( getExprPanel() );
        add( getComboPanel() );
        load( null );
    }
    
    public void setCartesianPlane( CartesianPlane plane )
    {
        this.cartPlane = plane;
    }
    
    public void load( Equation equation )
    {
        this.equation = equation;
        
        boolean newState    = equation != null;
        if ( newState )
        {
            xEquals.setValue( equation.getXExpression() );
            yEquals.setValue( equation.getYExpression() );
            rEquals.setValue( equation.getRExpression() );
            tEquals.setValue( equation.getTExpression() );
            String  strPlot = equation.getPlot().toUpperCase();
            Command cmdPlot = Command.valueOf( strPlot );
            plots.setSelectedItem( cmdPlot );
        }
        Stream.of( xEquals, yEquals, rEquals, tEquals, plots, plot )
            .forEach( c -> c.setEnabled( newState ) );
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
        
        panel.add( plots );
        plot.addActionListener( this::plotAction );
        panel.add( plot );
        return panel;
    }
    
    private JFormattedTextField getTextField( Function<String,Result> funk )
    {
        ExprFormatter       formatter   = new ExprFormatter( funk );
        JFormattedTextField textField   = 
            new JFormattedTextField( formatter );
        textField.setInputVerifier( verifier );
        textField.setColumns( 15 );
        
        return textField;
    }
    
    private void plotAction( ActionEvent evt )
    {
        int             inx         = plots.getSelectedIndex();
        Command         command     = plots.getItemAt( inx );
        Stream<Point2D> pointStream;
        switch ( command )
        {
        case YPLOT:
            pointStream = equation.yPlot();
            break;
        case XYPLOT:
            pointStream = equation.xyPlot();
            break;
        case RPLOT:
            pointStream = equation.rPlot();
            break;
        case TPLOT:
            pointStream = equation.tPlot();
            break;
        default:
            pointStream = null;
        }
        
        if ( pointStream != null && cartPlane != null )
            cartPlane.setStreamSupplier( () ->
                pointStream
                .map( p -> PlotPointCommand.of( p, cartPlane ) )
            );
        NotificationManager.INSTANCE
            .propagateNotification( CPConstants.REDRAW_NP );
    }
    
    private Equation getEquation()
    {
        return equation;
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
        private final Predicate<String>         pred;
        private final Function<String,Result>   setter;
        private final Supplier<String>          getter;
        private final String                    label;
        private final JFormattedTextField       textField;
        
        public ExprFormatter( Function<String,Result> setter )
        {
            this( setter, null, null );
//            pred = s -> setter.apply( s ).isSuccess();
//            setOverwriteMode( false );
        }
        
        public ExprFormatter( 
            Function<String,Result> setter,
            Supplier<String> getter,
            String label
        )
        {
            this.setter = setter;
            this.getter = getter;
            this.label = label;
            pred = s -> setter.apply( s ).isSuccess();
            setOverwriteMode( false );
            
            textField = new JFormattedTextField( this );
            textField.setInputVerifier( verifier );
            textField.setColumns( 15 );
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
    }
    
    @SuppressWarnings("serial")
    private class ExprFormatterOrig extends DefaultFormatter
    {
        private final Predicate<String> pred;
        
        public ExprFormatterOrig( Function<String,Result> funk )
        {
            pred = s -> funk.apply( s ).isSuccess();
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
    }
}
