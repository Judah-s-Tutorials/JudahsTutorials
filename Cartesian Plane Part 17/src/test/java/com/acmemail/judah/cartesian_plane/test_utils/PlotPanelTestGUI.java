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

public class PlotPanelTestGUI
{
    private static final PropertyManager    pmgr    =
        PropertyManager.INSTANCE;
    private final Map<String,JFormattedTextField>   textFieldMap    = 
        new HashMap<>();
    private final Map<String,Supplier<String>>      exprMap         = 
        new HashMap<>();
    
    /** GUI main window. */
    private final JFrame            frame;
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
    
    private Object  adHocObject1;
    
    public PlotPanelTestGUI()
    {
        frame = new JFrame( "Plot Panel Test Dialog" );
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
    
    public Equation getEquation()
    {
        return equation;
    }
    
    public void clickPlotButton()
    {
        GUIUtils.schedEDTAndWait( () -> plotButton.doClick() );
    }
    
    public void click( String fieldID )
    {
        JFormattedTextField textField   = textFieldMap.get( fieldID );
        assertNotNull( textField );
        click( textField );
    }
    
    public void type( int keyCode )
    {
        robot.keyPress( keyCode );
        robot.keyRelease( keyCode );
    }
    
    /**
     * Type a series of key codes.
     * The keys will be pressed in sequence
     * then released in reverse sequence.
     * The expectation is that the initial key codes
     * are modifiers (shift, control, alt, etc.)
     * and the last key code is for a non-modifier.
     * For example, "type(VK_CONTROL, VK_ALT, VK_C)"
     * would be the equivalent of typing control-alt-C.
     * 
     * @param keyCodes  the series of key codes to type
     */
    public void type( int... keyCodes )
    {
        Deque<Integer>  stack   = new ArrayDeque<>();
        Arrays.stream( keyCodes )
            .peek( i -> stack.push( i ) )
            .forEach( robot::keyPress);
        while ( !stack.isEmpty() )
            robot.keyRelease( stack.pop() );
    }
    
    /**
     * The the sequence of key codes for control-P.
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
    
    public void setPlotType( Command cmd )
    {
        comboBox.setSelectedItem( cmd );
        assertEquals( cmd.toString(), equation.getPlot() );
        assertTrue( pmgr.asBoolean( CPConstants.DM_MODIFIED_PN ) );
    }
    
    /**
     * Clicks the mouse on the given component.
     * 
     * @param comp  the given component
     */
    public void click( JComponent comp )
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
     * Enters a given equation into a text field.
     * The text field is focused and cleared,
     * the equation is pasted into the text field
     * and then ENTER is typed in the text field.
     * 
     * @param fieldID   ID of the text field to process
     * @param equation  the given equation
     */
    public void enterEquation( String fieldID, String equation )
    {
        JFormattedTextField textField   = textFieldMap.get( fieldID );
        click( textField );
        clearText( fieldID );
        paste( equation );
        type( KeyEvent.VK_ENTER );
    }
    
    public void paste( String str )
    {
        robotAsst.paste( str );
    }
    
    public boolean isChangedTextFont( String fieldID )
    {
        JTextField  textField   = textFieldMap.get( fieldID );
        Font        font        = getFont( textField );
        boolean     result      = font.getStyle() == Font.ITALIC;
        return result;
    }
    
    public boolean isValidTextFont( String fieldID )
    {
        JTextField  textField   = textFieldMap.get( fieldID );
        Color       foreground  = getColor( textField );
        boolean     result      = !foreground.equals( Color.RED );
        return result;
    }
    
    public void clearText( String fieldID )
    {
        JFormattedTextField textField   = textFieldMap.get( fieldID );
        GUIUtils.schedEDTAndWait( () -> textField.setText( "" ) );
    }
    
    public String getText( String fieldID )
    {
        JFormattedTextField textField   = textFieldMap.get( fieldID );
            assertNotNull( textField );
        String  text    = getText( textField );
        return text;
    }
    
    public String getExpression( String fieldID )
    {
        Supplier<String>    getter = exprMap.get( fieldID );
        assertNotNull( getter );
        String              expr    = getter.get();
        
        return expr;
    }
    
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
    
    public JFormattedTextField getFocusedField()
    {
        JFormattedTextField textField   = getFocusedTextField();
        return textField;
    }
    
    public Point2D getPlotPoint()
    {
        return plotManager.getPlotPoint();
    }
    
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
    
    public Object getValue( String fieldID )
    {
        JFormattedTextField textField   = textFieldMap.get( fieldID );
        Object              value       = 
            getProperty( () -> textField.getValue() );
        return value;
    }
    
    private JFormattedTextField getFocusedTextField()
    {
        GUIUtils.schedEDTAndWait( () -> {
            adHocObject1 = textFieldMap.values().stream()
                .filter( t -> t.isFocusOwner() )
                .findFirst().orElse( null );
        });
        return (JFormattedTextField)adHocObject1;
    }
    
    private Color getColor( JTextField textField )
    {
        Object  obj = getProperty( () -> textField.getForeground() );
        assertTrue( obj instanceof Color );
        return (Color)obj;
    }
    
    private String getText( JFormattedTextField textField )
    {
        Object  obj = getProperty( () -> textField.getText() );
        assertTrue( obj instanceof String );
        return (String)obj;
    }
    
    private Font getFont( JTextField textField )
    {
        Object  obj = getProperty( () -> textField.getFont() );
        assertTrue( obj instanceof Font );
        return (Font)obj;
    }
    
    private Object getProperty( Supplier<Object> supplier )
    {
        GUIUtils.schedEDTAndWait( () -> adHocObject1 = supplier.get() );
        return adHocObject1;
    }
    
    private JLabel getLabel( String text )
    {
        Predicate<JComponent>   isLabel     =
            c -> (c instanceof JLabel);
        Predicate<JComponent>   hasText     =
            c -> text.equals( ((JLabel)c).getText() );
        Predicate<JComponent>  labelPred   = isLabel.and( hasText );
        JComponent              comp        =
            ComponentFinder.find( frame, labelPred );
        assertNotNull( comp );
        assertTrue( comp instanceof JLabel );
        return (JLabel)comp;
    }
    
    private JComboBox<?> getComboBox()
    {
        JComponent  comp    =
            ComponentFinder.find( frame, c -> (c instanceof JComboBox));
        assertNotNull( comp );
        assertTrue( comp instanceof JComboBox );
        return (JComboBox<?>)comp;
    }
    
    private JButton getPlotButton()
    {
        Predicate<JComponent>   pred    = 
            ComponentFinder.getButtonPredicate( "Plot" );
        JComponent  comp    =
            ComponentFinder.find( frame, pred );
        assertNotNull( comp );
        assertTrue( comp instanceof JButton );
        return (JButton)comp;
    }
    
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
    
    @SuppressWarnings("serial")
    private static class PlotManager extends CartesianPlane
    {
        private Supplier<Stream<PlotCommand>>   supplier;
        private Point2D                         point;
        
        @Override
        public void 
        setStreamSupplier( Supplier<Stream<PlotCommand>> supplier )
        {
            this.supplier = supplier;
        }
        
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
        
        @Override
        public void plotPoint( float xco, float yco )
        {
            float   xcoR    = roundToOneDecimal( xco );
            float   ycoR    = roundToOneDecimal( yco );
            point = new Point2D.Float( xcoR, ycoR );
        }
        
        private float roundToOneDecimal( float toRound )
        {
            float   rounded = Math.abs( toRound ) * 10;
            rounded = (int)(rounded + .5);
            rounded /= 10 * Math.signum( toRound );
            return rounded;
        }
    }
}
