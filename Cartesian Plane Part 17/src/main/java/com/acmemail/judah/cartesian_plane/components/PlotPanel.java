package com.acmemail.judah.cartesian_plane.components;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.geom.Point2D;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;
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

@SuppressWarnings("serial")
public class PlotPanel extends JPanel
{
    private final ExprFormatter[]   formatters  = new ExprFormatter[]
    {
        new ExprFormatter(
            s -> getEquation().setXExpression( s ),
            () -> getEquation().getXExpression(),
            () -> getEquation().xyPlot(),
            Command.XYPLOT,
            "x="
        ),
        new ExprFormatter(
            s -> getEquation().setYExpression( s ),
            () -> getEquation().getYExpression(),
            () -> getEquation().yPlot(),
            Command.YPLOT,
            "y="
        ),
        new ExprFormatter(
            s -> getEquation().setRExpression( s ),
            () -> getEquation().getRExpression(),
            () -> getEquation().rPlot(),
            Command.RPLOT,
            "r="
        ),
        new ExprFormatter(
            s -> getEquation().setTExpression( s ),
            () -> getEquation().getTExpression(),
            () -> getEquation().tPlot(),
            Command.TPLOT,
            "t="
        ),
    };
    
    private final ExprVerifier      verifier    = new ExprVerifier();

    private final JComboBox<Command>    plots   = new JComboBox<>();
    private final JButton               plot    = new JButton( "Plot" );
    private final Map<Command,ExprFormatter>    exprMap = getExprMap();
    private Equation            equation    = null;
    private CartesianPlane      cartPlane   = null;

    public PlotPanel()
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
        exprMap.values().stream().forEach( fmt -> {
            JFormattedTextField textField   = fmt.textField;
            String              text        =
                newState ? fmt.getter.get() : "";
            textField.setValue( text );
            textField.setEnabled( newState );
        });
        if ( newState )
        {
            String  strPlot = equation.getPlot().toUpperCase();
            Command cmdPlot = Command.valueOf( strPlot );
            plots.setSelectedItem( cmdPlot );
        }
        Stream.of( plots, plot )
            .forEach( c -> c.setEnabled( newState ) );
    }
    
    private Map<Command,ExprFormatter> getExprMap()
    {
        Map<Command,ExprFormatter>  map   = new HashMap<>();
        Stream.of( formatters )
            .forEach( f -> map.put( f.command, f ) );
        return map;
    }
    
    private JPanel getExprPanel()
    {
        JPanel      panel   = new JPanel( new GridLayout( 2, 2 ) );
        panel.add( getExprPanel( Command.XYPLOT ) );
        panel.add( getExprPanel( Command.RPLOT) );
        panel.add( getExprPanel( Command.YPLOT ) );
        panel.add( getExprPanel( Command.TPLOT ) );
        return panel;
    }
    
    private JPanel getExprPanel( Command command )
    {
        ExprFormatter   fmt     = exprMap.get( command );
        JPanel          panel   = new JPanel();
        BoxLayout       layout  = new BoxLayout( panel, BoxLayout.X_AXIS );
        panel.setLayout( layout );
        
        JLabel  label   = new JLabel( fmt.label );
        panel.add( label );
        panel.add( fmt.textField );
        return panel;
    }
    
    private JPanel  getComboPanel()
    {
        JPanel      panel   = new JPanel();
        BoxLayout   layout  = new BoxLayout( panel, BoxLayout.Y_AXIS );
        panel.setLayout( layout );
        
        exprMap.values().stream()
            .map( f -> f.command )
            .forEach( plots::addItem );
        panel.add( plots );
        plot.addActionListener( this::plotAction );
        panel.add( plot );
        return panel;
    }
    
    private void plotAction( ActionEvent evt )
    {
        int             inx         = plots.getSelectedIndex();
        Command         command     = plots.getItemAt( inx );
        ExprFormatter   fmt         = exprMap.get( command );
        Stream<Point2D> pointStream = fmt.plotter.get();

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
        public boolean verify( JComponent comp )
        {
            JFormattedTextField textField   = (JFormattedTextField)comp;
            String              text        = textField.getText();
            boolean             status      = true;
            if ( !text.isEmpty() )
                status = equation.isValidExpression( text );          
            return status;
        }
    }
    
    private class ExprFormatter extends DefaultFormatter
    {
        public final Predicate<String>          pred;
        public final Supplier<String>           getter;
        public final Supplier<Stream<Point2D>>  plotter;
        public final Command                    command;
        public final String                     label;
        public final JFormattedTextField        textField;
        
        public ExprFormatter( 
            Function<String,Result>   setter,
            Supplier<String>          getter,
            Supplier<Stream<Point2D>> plotter,
            Command                   command,
            String                    label
        )
        {
            this.getter = getter;
            this.plotter = plotter;
            this.command = command;
            this.label = label;
            pred = s -> setter.apply( s ).isSuccess();
            setOverwriteMode( false );
            
            textField = new JFormattedTextField( this );
            textField.setInputVerifier( verifier );
            textField.setColumns( 15 );
            textField.addPropertyChangeListener( e -> {
                if ( equation != null )
                    setter.apply( textField.getValue().toString() );
            });
        }
        
        @Override
        public Object stringToValue( String str )
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
        
        @Override
        public String valueToString( Object value )
        {
            String  rval    = value != null ? value.toString() : "";
            JFormattedTextField fmtField    = getFormattedTextField();
            fmtField.setForeground( Color.BLACK );
            return rval;
        }
    }
}
