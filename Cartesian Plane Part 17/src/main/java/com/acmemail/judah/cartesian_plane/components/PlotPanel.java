package com.acmemail.judah.cartesian_plane.components;

import java.awt.Color;
import java.awt.Component;
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
    /** 
     * Configuration parameters for the four types of plot.
     * Only used for the initialization of {@linkplain #exprMap}.
     * @see ExprFormatter
     * @see #getExprMap()
     */
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
    
    /**
     * InputVerifier used for all JFormattedTextFields. Declared
     * as an instance variable so that a single object can be used
     * for all text fields.
     * @see ExprFormatter#ExprFormatter(Function, Supplier, Supplier, Command, String)
     */
    private final ExprVerifier      verifier    = new ExprVerifier();

    /** 
     * List of plot types. Declared as an instance variable for
     * the convenience of the {@linkplain #load(Equation)} method.
     */
    private final JComboBox<Command>    plots   = new JComboBox<>();
    /** 
     * Initiates a plot operation. Declared as an instance variable for
     * the convenience of the {@linkplain #load(Equation)} method.
     */
    private final JButton               plot    = new JButton( "Plot" );
    /**
     * Map to access all configuration data for a plot type;
     * for example, allows search for the text field associated
     * with a plot type.
     */
    private final Map<Command,ExprFormatter>    exprMap = getExprMap();
    /** Currently open equation; null if none. */
    private Equation            equation    = null;
    /** Object on which to draw plots; null if none. */
    private CartesianPlane      cartPlane   = null;

    /**
     * Constructor.
     * Responsible for completing all 
     * panel configuration tasks.
     */
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
    
    /**
     * Setter for the CartesianPlane property.
     * May be null.
     * 
     * @param plane CartesianPlane on which to display plots
     */
    public void setCartesianPlane( CartesianPlane plane )
    {
        this.cartPlane = plane;
    }
    
    /**
     * Loads the given equation.
     * If non-null, 
     * all components are enabled,
     * and given initial values
     * base on the equation.
     * If null,
     * all components are disabled,
     * and all text fields are 
     * set to empty strings.
     * 
     * @param equation  the given equation
     */
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
    
    /**
     * Creates a map of all ExpressionFormatters,
     * using the Command field of each formatter
     * as the key.
     * 
     * @return  map of Commands to ExpressionFormatters
     * 
     * @see #formatters
     */
    private Map<Command,ExprFormatter> getExprMap()
    {
        Map<Command,ExprFormatter>  map   = new HashMap<>();
        Stream.of( formatters )
            .forEach( f -> map.put( f.command, f ) );
        return map;
    }
    
    /**
     * Gets a panel containing the four text fields
     * associated with equation types.
     * 
     * @return
     *      panel containing the four text fields
     *      associated with equation types
     */
    private JPanel getExprPanel()
    {
        JPanel      panel   = new JPanel( new GridLayout( 2, 2 ) );
        panel.add( getExprPanel( Command.XYPLOT ) );
        panel.add( getExprPanel( Command.RPLOT) );
        panel.add( getExprPanel( Command.YPLOT ) );
        panel.add( getExprPanel( Command.TPLOT ) );
        return panel;
    }
    
    /**
     * Configures a panel containing a text field
     * for entering an equation of a particular type
     * and a descriptive label.
     * Configuration is based on an ExprFormatter object
     * associated with the given command.
     * 
     * @param command   the given command
     * 
     * @return  panel containing a text field and descriptive label
     * 
     * @see ExprFormatter
     * @see #formatters
     * @see #exprMap
     */
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
    
    /**
     * Gets a panel consisting of the combo box of plot commands,
     * and the button that initiates the plot operation.
     * 
     * @return  
     *      panel containing the plot command combo box 
     *      and plot button.
     */
    private JPanel  getComboPanel()
    {
        JPanel      panel   = new JPanel();
        BoxLayout   layout  = new BoxLayout( panel, BoxLayout.Y_AXIS );
        panel.setLayout( layout );
        
        exprMap.values().stream()
            .map( f -> f.command )
            .forEach( plots::addItem );
        panel.add( plots );
        panel.add( plot );
        plot.addActionListener( this::plotAction );
        plot.setAlignmentX( Component.CENTER_ALIGNMENT );
        return panel;
    }
    
    /**
     * Executes a plot command.
     * 
     * @param evt   event object passed to action listeners; not used
     */
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
    
    /**
     * Gets the equation currently in use.
     * Null if none.
     * 
     * @return  the equation currently in use
     */
    private Equation getEquation()
    {
        return equation;
    }
    
    /**
     * Subclass of InputVerifier
     * used to validate the expressions
     * entered into the text fields
     * of this panel.
     * 
     * @author Jack Straub
     */
    private class ExprVerifier extends InputVerifier
    {
        /**
         * Validates the string entered into a text field.
         * If the string is empty it is ignored, 
         * and true is returned.
         * Otherwise returns true if the string
         * constitutes a valid expression.
         * 
         * @return 
         *      true if the string in a text field is empty,
         *      or constitutes a valid expression
         */
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
    
    /**
     * Formatter for a text field containing an expression.
     * Encapsulates all configuration data
     * for a text field associated with
     * one of the plot commands: YPLOT, XYPLOT, RPLOT, TPLOT.
     * Configuration data include:
     * <ul>
     * <li>
     * A function to validate an expression and store 
     * in the currently open equation.
     * </li>
     * <li>
     * A function to obtain from the currently open equation
     * the expression associated with a plot command.
     * </li>
     * <li>
     * A function to plot the currently open equation
     * in accordance with the encapsulated plot command.
     * </li>
     * <li>The encapsulated command.</li>
     * <li>A label describing the encapsulated plot command.
     * </ul>
     * @author Jack Straub
     */
    private class ExprFormatter extends DefaultFormatter
    {
        public final Predicate<String>          pred;
        public final Supplier<String>           getter;
        public final Supplier<Stream<Point2D>>  plotter;
        public final Command                    command;
        public final String                     label;
        public final JFormattedTextField        textField;
        
        /**
         * Constructor.
         * Creates and initializes a JFormattedTextField
         * with a formatter (this object)
         * and InputVerifier.
         * Records additional data
         * used to processing an equation
         * associated with a given command.
         * 
         * @param setter
         *      function to validate an expression and, if valid,
         *      copy the expression into the appropriate field
         *      of the currently open equation
         * @param getter
         *      supplier to obtain from the currently open equation
         *      the expression associated with the encapsulated command
         * @param plotter
         *      function to plot the encapsulated command using the
         *      appropriate expression contained in the currently
         *      open equation
         * @param command
         *      plot command associated with this object
         * @param label
         *      label to place on the text field component
         *      encapsulated in this object
         */
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
        
        /**
         * Verifies that the given string
         * comprises a valid expression.
         * If it is not a valid expression
         * the text color of the encapsulated text field
         * is changed to red,
         * and a ParseException is thrown.
         * If it is a valid expression
         * the text color of the encapsulated text field
         * is changed to black,
         * and the given string is returned.
         * 
         * @param str   the given string
         * 
         * @return 
         *      the given string, assuming it constitutes a 
         *      valid expression
         *      
         *  @throws ParseException
         *      if the given string is not a valid expression
         */
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
        
        /**
         * "Converts" the given Object to a String.
         * Since the given object is expected to be a String
         * little conversion is required.
         * The given Object is assumed
         * to constitute a valid expression.
         * The text color of the encapsulated text field
         * is changed explicitly to black,
         * and the given Object is returned as a String.
         * 
         * @param   value   the given Object
         * 
         * @return  the given Object converted to a String
         */
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
