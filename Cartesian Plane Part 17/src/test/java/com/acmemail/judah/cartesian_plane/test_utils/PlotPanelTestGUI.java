package com.acmemail.judah.cartesian_plane.test_utils;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.awt.AWTException;
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Robot;
import java.awt.event.InputEvent;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Stream;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import com.acmemail.judah.cartesian_plane.components.PlotPanel;
import com.acmemail.judah.cartesian_plane.graphics_utils.ComponentFinder;
import com.acmemail.judah.cartesian_plane.graphics_utils.GUIUtils;
import com.acmemail.judah.cartesian_plane.input.Exp4jEquation;

public class PlotPanelTestGUI
{
    private final Map<String,JFormattedTextField>   textFieldMap    = 
        new HashMap<>();
    
    /** GUI main window. */
    private final JFrame        frame;
    /** PlotPanel under test. */
    private final PlotPanel     plotPanel;
    /** Combo box containing plot options. */
    private final JComboBox<?>  comboBox;
    /** Button to initiate plot. */
    private final JButton       plotButton;
    /** Robot for simulating keyboard and mouse events. */
    private final Robot         robot;
    
    /**
     * Application entry point.
     *
     * @param args command line arguments, not used
     *
    */
    public static void main(String[] args)
    {
        SwingUtilities.invokeLater( PlotPanelTestGUI::new );
    }
    
    public PlotPanelTestGUI()
    {
        frame = new JFrame( "Plot Panel Test Dialog" );
        JPanel  contentPane = new JPanel( new BorderLayout() );
        plotPanel = new PlotPanel();
        contentPane.add( plotPanel );
        frame.setContentPane( contentPane );
        frame.pack();
        frame.setVisible( true );
        
        Stream.of( "x=", "y=", "t=", "r=" )
            .forEach( this::getTextField );
        comboBox = getComboBox();
        plotButton = getPlotButton();
        
        robot = makeRobot();
        robot.setAutoDelay( 250 );
    }
    
    /**
     * Gets the JFormattedTextField associated
     * with the given label.
     * 
     * @param text  the given label
     * 
     * @return  the JFormattedTextField associated with the given label
     */
    public JFormattedTextField getEquationField( String text )
    {
        JFormattedTextField textField   = textFieldMap.get( text );
        assertNotNull( textField );
        return textField;
    }
    
    /**
     * Gets the combo box that contains the plot options.
     * 
     * @return  the combo box that contains the plot options
     */
    public JComboBox<?> getOptions()
    {
        return comboBox;
    }
    
    /**
     * Gets the button that initiates the plot.
     * 
     * @return  the button that initiates the plot
     */
    public JButton getPlotExecutor()
    {
        return plotButton;
    }
    
    public void type( String textFieldID, int keyCode )
    {
        JFormattedTextField textField   = textFieldMap.get( textFieldID );
        assertNotNull( textField );
        click( textField );
    }
    
    public void type( int keyCode )
    {
        robot.keyPress( keyCode );
        robot.keyRelease( keyCode );
    }
    
    public void type( int... keyCodes )
    {
        Deque<Integer>  stack   = new ArrayDeque<>();
        Arrays.stream( keyCodes )
            .peek( i -> stack.push( i ) )
            .forEach( robot::keyPress);
        while ( !stack.isEmpty() )
            robot.keyRelease( stack.pop() );
    }
    
    public void newEquation()
    {
        GUIUtils.schedEDTAndWait( () -> 
            plotPanel.load( new Exp4jEquation() ) );
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
    
    private Robot makeRobot()
    {
        Robot   robot   = null;
        try
        {
            robot = new Robot();
        }
        catch ( AWTException exc )
        {
            exc.printStackTrace();
            System.exit( 1 );
        }
        
        return robot;
    }
}
