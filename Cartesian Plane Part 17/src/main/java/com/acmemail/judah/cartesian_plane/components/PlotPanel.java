package com.acmemail.judah.cartesian_plane.components;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.geom.Point2D;
import java.beans.PropertyChangeEvent;
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
import javax.swing.JFormattedTextField.AbstractFormatter;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.text.DefaultFormatter;

import com.acmemail.judah.cartesian_plane.CPConstants;
import com.acmemail.judah.cartesian_plane.CartesianPlane;
import com.acmemail.judah.cartesian_plane.NotificationManager;
import com.acmemail.judah.cartesian_plane.PlotPointCommand;
import com.acmemail.judah.cartesian_plane.PropertyManager;
import com.acmemail.judah.cartesian_plane.graphics_utils.ComponentException;
import com.acmemail.judah.cartesian_plane.input.Command;
import com.acmemail.judah.cartesian_plane.input.Equation;
import com.acmemail.judah.cartesian_plane.input.Result;
import com.acmemail.judah.cartesian_plane.sandbox.formatted_text.JFormattedTextFieldDemo4;
import com.acmemail.judah.cartesian_plane.sandbox.formatted_text.JFormattedTextFieldDemo5;

/**
 * This class is used to create and manage a "plot panel."
 * The panel consists of:
 * <ul>
 * <li> 
 *      Four text fields expressing
 *      the types of equation we are prepared to plot,
 *      YPLOT, XYPLOT, TPLOT and RPLOT.
 * </li>
 * <li>
 *      A combo box for selecting which equation to plot.
 * </li>
 * <li>
 *      A pushbutton for initiating the plot.
 * </li>
 * </ul>
 * @author Jack Straub
 * 
 * @see JFormattedTextFieldDemo4
 * @see JFormattedTextFieldDemo5
 */
@SuppressWarnings("serial")
public class PlotPanel extends JPanel
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
    private Equation                equation    = null;
    /** Object on which to draw plots; null if none. */
    private CartesianPlane          cartPlane   = null;

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
    private JPanel getComboPanel()
    {
        JPanel      panel   = new JPanel();
        BoxLayout   layout  = new BoxLayout( panel, BoxLayout.Y_AXIS );
        panel.setLayout( layout );
        
        exprMap.values().stream()
            .map( f -> f.command )
            .forEach( plots::addItem );
        panel.add( plots );
        panel.add( plot );
        plots.addActionListener( this::plotsAction );
        plot.addActionListener( this::plotAction );
        plot.setAlignmentX( Component.CENTER_ALIGNMENT );
        return panel;
    }
    
    private void plotsAction( ActionEvent evt )
    {
        Command cmd = (Command)plots.getSelectedItem();
        if ( equation != null )
        {
            equation.setPlot( cmd.toString() );
            markModified();
        }
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
        {
            cartPlane.setStreamSupplier( () ->
                fmt.plotter.get()
                .map( p -> PlotPointCommand.of( p, cartPlane ) )
            );
            NotificationManager.INSTANCE
                .propagateNotification( CPConstants.REDRAW_NP );
        }
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
     * Declare via the PropertyManager that the data
     * in the current equation has changed.
     */
    private static void markModified()
    {
        // Declare that a the data in the currently open equation
        // has been modified.
        PropertyManager pmgr        = PropertyManager.INSTANCE;
        String          dmModified  = CPConstants.DM_MODIFIED_PN;
        pmgr.setProperty( dmModified, true );
    }

    /**
     * Subclass of InputVerifier
     * used to validate the expressions
     * entered into the text fields
     * of this panel.
     * 
     * @author Jack Straub
     */
    private static class ExprVerifier extends InputVerifier
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
            AbstractFormatter   formatter   = textField.getFormatter();
            String              text        = textField.getText();
            boolean             status      = true;
            try
            {
                formatter.stringToValue( text );
            }
            catch ( ParseException exc )
            {
                status = false;
            }
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
        public final Function<String,Result>    setter;
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
            this.setter = setter;
            this.plotter = plotter;
            this.command = command;
            this.label = label;
            pred = s -> getEquation().isValidExpression( s );
            setOverwriteMode( false );
            
            textField = new JFormattedTextField( this );
            textField.setInputVerifier( new ExprVerifier() );
            textField.setColumns( 15 );
            textField.addKeyListener( new PIListener() );
            textField.addPropertyChangeListener( "value", this::commit );
        }
        
        /**
         * Processes a commit operation.
         * A commit operation is signaled
         * by a change to a formatted text field's "value" property.
         * The text field's font is updated 
         * to indicate that a commit has taken place.
         * The corresponding field
         * in the current equation
         * is updated with the committed value.
         * <p>
         * Precondition: 
         * the value of the formatted text field
         * has been validated.
         * If the value is invalid
         * an exception is thrown.
         * <p>
         * Precondition:
         * The accompanying PropertyChangeEvent object
         * indicates that the "value" property has change.
         * If the name of the property is anything else
         * the event is ignored.
         * <p>
         * Postcondition:
         * The text field's font indicates 
         * that its value has been committed.
         * <p>
         * Postcondition:
         * The corresponding field of the current equation
         * has been updated with the committed value.
         * 
         * @param evt   
         *      object identifying the details 
         *      of the PropertyChangeEvent
         *      
         * @throws ComponentException
         *      if the committed value is invalid
         */
        private void commit( PropertyChangeEvent evt )
        {
            if ( !evt.getPropertyName().equals( "value" ) )
                return;
            if ( equation == null )
                return;
            JFormattedTextField comp    = getFormattedTextField();
            String      value           = comp.getValue().toString(); 
            Result      result          = setter.apply( value );
            // The input to the setter should be validated at this point,
            // so if it tests as invalid declare a malfunction.
            if ( !result.isSuccess() )
            {
                String  msg     = "Invalid expression: \"" + value + '"';
                throw new ComponentException( msg );
            }
            
            // If the text field text is not different from the value,
            // set the font style to committed.
            comp.setFont( committedFont );
            
            // Declare that a the data in the currently open equation
            // has been modified.
            markModified();
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
                    fmtField.setForeground( invalidColor );
                    fmtField.setFont( uncommittedFont );
                    throw new ParseException( "Invalid expression", 0 );
                }
                fmtField.setForeground( validColor );
                if ( str.equals( fmtField.getValue() ) )
                    fmtField.setFont( committedFont );
                else
                    fmtField.setFont( uncommittedFont );
            }
            fmtField.setForeground( validColor );
            return str;
        }
    }
}
