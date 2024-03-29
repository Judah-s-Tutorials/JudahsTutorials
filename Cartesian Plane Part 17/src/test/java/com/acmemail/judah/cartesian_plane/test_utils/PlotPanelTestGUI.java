package com.acmemail.judah.cartesian_plane.test_utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.awt.AWTException;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Point;
import java.awt.Robot;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.geom.Point2D;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Stream;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.acmemail.judah.cartesian_plane.CPConstants;
import com.acmemail.judah.cartesian_plane.CartesianPlane;
import com.acmemail.judah.cartesian_plane.PlotCommand;
import com.acmemail.judah.cartesian_plane.PlotPointCommand;
import com.acmemail.judah.cartesian_plane.PropertyManager;
import com.acmemail.judah.cartesian_plane.components.PlotPanel;
import com.acmemail.judah.cartesian_plane.graphics_utils.ComponentFinder;
import com.acmemail.judah.cartesian_plane.graphics_utils.GUIUtils;
import com.acmemail.judah.cartesian_plane.input.Command;
import com.acmemail.judah.cartesian_plane.input.Equation;
import com.acmemail.judah.cartesian_plane.input.Exp4jEquation;

/**
 * Creates and manages a GUI containing a PlotPanel.
 * Provides utilities for the test program
 * to access and manipulate the PlotPanel
 * and the Equation it contains.
 * 
 * @author Jack Straub
 */
public class PlotPanelTestGUI
{
    private static final PropertyManager            pmgr            =
        PropertyManager.INSTANCE;
    private final Map<String,JFormattedTextField>   textFieldMap    = 
        new HashMap<>();
    private final Map<String,Supplier<String>>      exprMap         = 
        new HashMap<>();
    
    /** PlotPanel under test. */
    private final PlotPanel         plotPanel;
    /** Combo box containing plot options. */
    private final JComboBox<?>      comboBox;
    /** Button to initiate plot. */
    private final JButton           plotButton;
    /** Robot assistant for simulating keyboard and mouse events. */
    private final RobotAssistant    robotAsst;
    /** Robot object from RobotAssistant. */
    private final Robot             robot;

    /** CartesianPlane simulator, for testing the plot button. */
    private final PlotManager       plotManager = new PlotManager();

    /** Currently loaded equation; may be null. */
    private Equation    equation    = null;
    
    /** Temporary object for limited use by lambdas. */
    private Object  adHocObject1;
    
    /**
     * Assembles and displays the GUI.
     * Performs complete initialization of this object.
     */
    public PlotPanelTestGUI()
    {
        JFrame  frame       = new JFrame( "Plot Panel Test Dialog" );
        JPanel  contentPane = new JPanel( new BorderLayout() );
        plotPanel = new PlotPanel();
        plotPanel.setCartesianPlane( plotManager );
        contentPane.add( plotPanel );
        frame.setContentPane( contentPane );
        frame.pack();
        frame.setVisible( true );
        
        Stream.of( "x=", "y=", "t=", "r=" )
            .forEach( this::getTextField );
        comboBox = getComboBox();
        plotButton = getPlotButton();
        
        exprMap.put( "y=", () -> getEquation().getYExpression() );
        exprMap.put( "x=", () -> getEquation().getXExpression() );
        exprMap.put( "t=", () -> getEquation().getTExpression() );
        exprMap.put( "r=", () -> getEquation().getRExpression() );
        
        robotAsst = makeRobot();
        robot = robotAsst.getRobot();
        robot.setAutoDelay( 100 );
    }
    
    /**
     * Calls the doClick method on the PlotPanel's
     * Plot button. 
     * Executed in the context of the EDT.
     */
    public void clickPlotButton()
    {
        GUIUtils.schedEDTAndWait( () -> plotButton.doClick() );
    }
    
    /**
     * Obtains the text field indicated
     * by the given string identifier
     * and clicks on it using the {@linkplain #click(JComponent)} method.
     * The string identifier must be one of 
     * x=, y=, r=, or t=.
     * 
     * @param fieldID   the given string identifier
     */
    public void click( String fieldID )
    {
        JFormattedTextField textField   = textFieldMap.get( fieldID );
        assertNotNull( textField );
        click( textField );
    }
    
    /**
     * Uses a Robot to execute a press and release
     * for the given key code.
     * 
     * @param keyCode   the given key code
     */
    public void type( int keyCode )
    {
        robot.keyPress( keyCode );
        robot.keyRelease( keyCode );
    }
    
    /**
     * Types the sequence of key codes for control-P.
     */
    public void typeCtrlP()
    {
        type( KeyEvent.VK_CONTROL, KeyEvent.VK_P );
    }
    
    /**
     * In the text field with the keyboard focus,
     * find the first occurrence of the characters "pi"
     * (not case-sensitive)
     * and position the caret immediately after.
     * Returns the position of the caret,
     * or -1 if "pi" is not found.
     */
    public int positionAtPI()
    {
        JFormattedTextField textField   = getFocusedTextField();
        assertNotNull( textField );
        String  text        = getText( textField ).toLowerCase();
        int     caretPos    = text.indexOf( "pi" );
        if ( caretPos >= 0 )
            GUIUtils.schedEDTAndWait( () -> {
                textField.setCaretPosition( caretPos + 2 );
            });
        return caretPos;
    }
    
    /**
     * Instantiates and loads a new Equation.
     */
    public Equation newEquation()
    {
        equation = new Exp4jEquation();
        equation.setRangeStart( "1" );
        equation.setRangeEnd( "2" );
        GUIUtils.schedEDTAndWait( () -> 
            plotPanel.load( equation ) );
        pmgr.setProperty( CPConstants.DM_MODIFIED_PN, false );
        pmgr.setProperty( CPConstants.DM_OPEN_EQUATION_PN, true );
        pmgr.setProperty( CPConstants.DM_OPEN_FILE_PN, false );
        return equation;
    }
    
    /**
     * Selects the given plot type
     * in the PlotPanel's combo  box.
     * 
     * @param cmd   the given plot type
     */
    public void setPlotType( Command cmd )
    {
        comboBox.setSelectedItem( cmd );
        assertEquals( cmd.toString(), equation.getPlot() );
        assertTrue( pmgr.asBoolean( CPConstants.DM_MODIFIED_PN ) );
    }
    
    /**
     * Enters a given expression into a text field.
     * The text field is focused and cleared,
     * the equation is pasted into the text field
     * and then ENTER is typed in the text field.
     * <p>
     * Precondition: the expression argument is well formed.
     * <p>
     * Postcondition: the expression argument is committed.
     * 
     * @param fieldID     ID of the text field to paste in
     * @param expression  the given expression
     */
    public void enterEquation( String fieldID, String expression )
    {
        JFormattedTextField textField   = textFieldMap.get( fieldID );
        click( textField );
        clearText( fieldID );
        paste( expression );
        type( KeyEvent.VK_ENTER );
    }
    
    /**
     * Pastes the given string into the given component.
     * The component should be a text field.
     * The text field is not cleared;
     * no assumptions are made about
     * the state of the text field.
     * 
     * @param str   the given string
     */
    public void paste( String str )
    {
        robotAsst.paste( str );
    }
    
    /**
     * Returns true if the text field
     * indicated by the given string identifier
     * has an italic font.
     * 
     * @param fieldID   the given string identifier
     * 
     * @return  
     *      true if the indicated text field
     *      has an italic font
     */
    public boolean isChangedTextFont( String fieldID )
    {
        JTextField  textField   = textFieldMap.get( fieldID );
        Font        font        = getFont( textField );
        boolean     result      = font.getStyle() == Font.ITALIC;
        return result;
    }
    
    /**
     * Returns true if the text field
     * indicated by the given string identifier
     * uses a "valid" text color.
     * <p>
     * Precondition: invalid text is displayed in red
     * 
     * @param fieldID   the given string identifier
     * 
     * @return  
     *      true if the indicated text field
     *      has uses a "valid" color
     */
    public boolean isValidTextFont( String fieldID )
    {
        JTextField  textField   = textFieldMap.get( fieldID );
        Color       foreground  = getColor( textField );
        boolean     result      = !foreground.equals( Color.RED );
        return result;
    }
    
    /**
     * Clears the text field with the given string identifier.
     * 
     * @param fieldID   the given string identifier
     */
    public void clearText( String fieldID )
    {
        JFormattedTextField textField   = textFieldMap.get( fieldID );
        GUIUtils.schedEDTAndWait( () -> textField.setText( "" ) );
    }
    
    /**
     * Returns the text of the text field
     * with the given string identifier.
     * 
     * @param fieldID   the given string identifier
     * 
     * @return  the text of the indicated text field
     */
    public String getText( String fieldID )
    {
        JFormattedTextField textField   = textFieldMap.get( fieldID );
            assertNotNull( textField );
        String  text    = getText( textField );
        return text;
    }
    
    /**
     * Gets the expression
     * from the currently open equation
     * indicated by the given field identifier.
     * For example,
     * if the given identifier is "y="
     * the expression given by 
     * equation.getYExpression() will be returned.
     * 
     * @param fieldID   the given field identifier
     * 
     * @return  
     *      the expression from the currently open equation
     *      indicated by the given field identifier
     */
    public String getExpression( String fieldID )
    {
        Supplier<String>    getter = exprMap.get( fieldID );
        assertNotNull( getter );
        String              expr    = getter.get();
        
        return expr;
    }
    
    /**
     * Returns true if the text of the text field
     * with the given string identifier
     * has been committed.
     * 
     * @param fieldID   the given string identifier
     * 
     * @return  
     *      true if the text of the indicated text field
     *      has been committed
     */
    public boolean isCommitted( String fieldID )
    {
        String      text        = getText( fieldID );
        Object      value       = getValue( fieldID );
        boolean     committed   = text.equals( value );
        // if committed, make sure equation matches text field value
        if ( committed )
        {
            Supplier<String>    getter  = exprMap.get( fieldID );
            assertNotNull( getter );
            assertEquals( value, getter.get() );
        }
        return committed;
    }
    
    /**
     * Returns the text field that holds the keyboard focus.
     * If no text field has the focus
     * null is returned.
     * 
     * @return  the text field with the focus, or null if none
     */
    public JFormattedTextField getFocusedField()
    {
        JFormattedTextField textField   = getFocusedTextField();
        return textField;
    }
    
    /**
     * Gets the first plot point
     * from the most recently plotted point stream.
     * 
     * @return  
     *      first plot point from the most recently plotted point stream
     *      
     * @see PlotManager#getPlotPoint()
     */
    public Point2D getPlotPoint()
    {
        return plotManager.getPlotPoint();
    }
    
    public Object getValue( String fieldID )
    {
        JFormattedTextField textField   = textFieldMap.get( fieldID );
        Object              value       = 
            getProperty( () -> textField.getValue() );
        return value;
    }
    
    /**
     * Obtains the equation currently open in the PlotPanel.
     * 
     * @return  the equation currently open in the PlotPanel
     */
    private Equation getEquation()
    {
        return equation;
    }
    
    /**
     * Types a series of key codes.
     * The keys will be pressed in the given sequence
     * then released in reverse sequence.
     * The expectation is that the initial key codes
     * are modifiers (shift, control, alt, etc.)
     * and the last key code is for a non-modifier.
     * For example, "type(VK_CONTROL, VK_ALT, VK_C)"
     * would be the equivalent of typing control-alt-C.
     * 
     * @param keyCodes  the series of key codes to type
     */
    private void type( int... keyCodes )
    {
        Deque<Integer>  stack   = new ArrayDeque<>();
        Arrays.stream( keyCodes )
            .peek( i -> stack.push( i ) )
            .forEach( robot::keyPress);
        while ( !stack.isEmpty() )
            robot.keyRelease( stack.pop() );
    }
    
    /**
     * Gets the text field
     * that currently holds the keyboard focus.
     * If no text field has the focus
     * null is returned.
     * 
     * @return  the text field with the focus, or null if none
     */
    private JFormattedTextField getFocusedTextField()
    {
        GUIUtils.schedEDTAndWait( () -> {
            adHocObject1 = textFieldMap.values().stream()
                .filter( t -> t.isFocusOwner() )
                .findFirst().orElse( null );
        });
        return (JFormattedTextField)adHocObject1;
    }
    
    /**
     * Clicks the mouse on the given component.
     * 
     * @param comp  the given component
     */
    private void click( JComponent comp )
    {
        Point       pos     = comp.getLocationOnScreen();
        Dimension   size    = comp.getSize();
        pos.x += size.width / 2;
        pos.y += size.height / 2;
        robot.mouseMove( pos.x, pos.y );
        robot.mousePress( InputEvent.BUTTON1_DOWN_MASK );
        robot.mouseRelease( InputEvent.BUTTON1_DOWN_MASK );
    }
    
    /**
     * Gets the text color of the given text field.
     * 
     * @param textField the given text field
     * 
     * @return  the text color of the given text field
     */
    private Color getColor( JTextField textField )
    {
        Object  obj = getProperty( () -> textField.getForeground() );
        assertTrue( obj instanceof Color );
        return (Color)obj;
    }
    
    
    /**
     * Gets the text of the given text field.
     * 
     * @param textField the given text field
     * 
     * @return  the text of the given text field
     */
    private String getText( JTextField textField )
    {
        Object  obj = getProperty( () -> textField.getText() );
        assertTrue( obj instanceof String );
        return (String)obj;
    }
    
    
    /**
     * Gets the font of the given text field.
     * 
     * @param textField the given text field
     * 
     * @return  the font of the given text field
     */
    private Font getFont( JTextField textField )
    {
        Object  obj = getProperty( () -> textField.getFont() );
        assertTrue( obj instanceof Font );
        return (Font)obj;
    }
    
    /**
     * Executes the given Supplier
     * in the context of the EDT.
     * Returns the object obtains.
     * 
     * @param supplier  the given Supplier
     * 
     * @return  the object obtained
     */
    private Object getProperty( Supplier<Object> supplier )
    {
        GUIUtils.schedEDTAndWait( () -> adHocObject1 = supplier.get() );
        return adHocObject1;
    }
    
    /**
     * Search the PlotPanel for the JLabel component
     * with the given text.
     * 
     * @param text  the given text
     * 
     * @return  the target JLabel component
     */
    private JLabel getLabel( String text )
    {
        Predicate<JComponent>   isLabel     =
            c -> (c instanceof JLabel);
        Predicate<JComponent>   hasText     =
            c -> text.equals( ((JLabel)c).getText() );
        Predicate<JComponent>  labelPred   = isLabel.and( hasText );
        JComponent              comp        =
            ComponentFinder.find( plotPanel, labelPred );
        assertNotNull( comp );
        assertTrue( comp instanceof JLabel );
        return (JLabel)comp;
    }
    
    /**
     * Gets the JComboBox from the PlotPanel.
     * 
     * @return  the JComboBox from the PlotPanel
     */
    private JComboBox<?> getComboBox()
    {
        JComponent  comp    =
            ComponentFinder.find( plotPanel, c -> (c instanceof JComboBox));
        assertNotNull( comp );
        assertTrue( comp instanceof JComboBox );
        return (JComboBox<?>)comp;
    }
    
    /**
     * Gets the JButton from the PlotPanel.
     * 
     * @return  the JButton from the PlotPanel
     */
    private JButton getPlotButton()
    {
        Predicate<JComponent>   pred    = 
            ComponentFinder.getButtonPredicate( "Plot" );
        JComponent  comp    =
            ComponentFinder.find( plotPanel, pred );
        assertNotNull( comp );
        assertTrue( comp instanceof JButton );
        return (JButton)comp;
    }
    
    /**
     * Gets the JFormattedTextField adjacent to the
     * JLabel with the given text
     * add adds it to the text field map.
     * 
     * @param text  the given text
     */
    private void getTextField( String text )
    {
        JLabel                  label   = getLabel( text );
        Predicate<JComponent>   pred    = 
            c -> (c instanceof JFormattedTextField);
        Container               cont    = label.getParent();
        assertNotNull( cont );
        assertTrue( cont instanceof JComponent );
        JComponent              comp    =
            ComponentFinder.find( (JComponent)cont, pred );
        assertNotNull( comp );
        assertTrue( comp instanceof JFormattedTextField );
        textFieldMap.put( text, (JFormattedTextField)comp );
    }
    
    /**
     * Instantiates a RobotAssictant.
     * 
     * @return the instantiate RobotAssistant.
     */
    private RobotAssistant makeRobot()
    {
        RobotAssistant  robot   = null;
        try
        {
            robot = new RobotAssistant();
        }
        catch ( AWTException exc )
        {
            exc.printStackTrace();
            System.exit( 1 );
        }
        
        return robot;
    }
    
    /**
     * CartesianPlane emulator.
     * Use to obtain the results of a plot operation.
     * 
     * @author Jack Straub
     */
    @SuppressWarnings("serial")
    private static class PlotManager extends CartesianPlane
    {
        /**
         * The stream supplier. Set by the client by calling 
         * setStreamSupplier at the beginning of a plot operation.
         * @see #setStreamSupplier(Supplier)
         */
        private Supplier<Stream<PlotCommand>>   supplier;
        /** 
         * The first point obtained from the stream supplied by
         * the stream supplier.
         * @see #setStreamSupplier(Supplier)
         */
        private Point2D                         point;
        
        /**
         * Sets the stream supplier provided by the client.
         */
        @Override
        public void 
        setStreamSupplier( Supplier<Stream<PlotCommand>> supplier )
        {
            this.supplier = supplier;
        }
        
        /**
         * Called by a PlotPointCommand's execute method.
         * @see #getPlotPoint()
         * @see PlotPointCommand
         */
        @Override
        public void plotPoint( float xco, float yco )
        {
            float   xcoR    = roundToOneDecimal( xco );
            float   ycoR    = roundToOneDecimal( yco );
            point = new Point2D.Float( xcoR, ycoR );
        }
        
        /**
         * Gets the first PlotPoint object from the stream
         * supplied by the client.
         * 
         * @return
         *      the first PlotPoint object from the stream
         *      supplied by the client
         *
         * @see #setStreamSupplier(Supplier)
         * @see #plotPoint(float, float)
         */
        public Point2D getPlotPoint()
        {
            Stream<PlotCommand> stream  = supplier.get();
            PlotPointCommand    cmd = 
                stream.filter( c -> (c instanceof PlotPointCommand) )
                .map( c -> (PlotPointCommand)c )
                .findFirst().orElse( null );
            assertNotNull( cmd );
            cmd.execute();
            return point;
        }
        
        /**
         * Rounds the given decimal number
         * to the first decimal place.
         * 
         * @param toRound   the given decimal number
         * 
         * @return
         *      the value of the given decimal number
         *      rounded to one decimal place
         */
        private float roundToOneDecimal( float toRound )
        {
            float   rounded = Math.abs( toRound ) * 10;
            rounded = (int)(rounded + .5);
            rounded /= 10 * Math.signum( toRound );
            return rounded;
        }
    }
}
