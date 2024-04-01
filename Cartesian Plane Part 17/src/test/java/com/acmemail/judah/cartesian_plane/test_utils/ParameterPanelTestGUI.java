package com.acmemail.judah.cartesian_plane.test_utils;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.awt.AWTException;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Robot;
import java.awt.event.InputEvent;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Stream;

import javax.swing.JComponent;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.acmemail.judah.cartesian_plane.CPConstants;
import com.acmemail.judah.cartesian_plane.PropertyManager;
import com.acmemail.judah.cartesian_plane.components.ParameterPanel;
import com.acmemail.judah.cartesian_plane.graphics_utils.ComponentFinder;
import com.acmemail.judah.cartesian_plane.graphics_utils.GUIUtils;
import com.acmemail.judah.cartesian_plane.input.Equation;
import com.acmemail.judah.cartesian_plane.input.Exp4jEquation;

public class ParameterPanelTestGUI
{
    private static final PropertyManager            pmgr            =
        PropertyManager.INSTANCE;
    private final Map<String,JFormattedTextField>   textFieldMap    = 
        new HashMap<>();
    private final Map<String,Supplier<String>>      exprMap         = 
        new HashMap<>();

    /** The ParameterPanel under test. */
    private final ParameterPanel    paramPanel;
    /** Robot assistant for simulating keyboard and mouse events. */
    private final RobotAssistant    robotAsst;
    /** Robot object from RobotAssistant. */
    private final Robot             robot;

    /** Currently loaded equation; may be null. */
    private Equation    equation    = null;
    
    public ParameterPanelTestGUI()
    {
        JFrame  frame       = new JFrame( "Parameter Panel Test Frame" );
        JPanel  contentPane = new JPanel( new BorderLayout() );
        paramPanel = new ParameterPanel();
        contentPane.add( paramPanel );
        frame.setContentPane( contentPane );
        frame.pack();
        frame.setVisible( true );
        
        getTextFields();
        
        exprMap.put( "Start", () -> getEquation().getRangeStartExpr() );
        exprMap.put( "End", () -> getEquation().getRangeEndExpr() );
        exprMap.put( "Step", () -> getEquation().getRangeStepExpr() );
        exprMap.put( "Prec", () -> 
            String.valueOf( getEquation().getPrecision() )
        );
        exprMap.put( "Radius", () -> getEquation().getRadiusName() );
        exprMap.put( "Theta", () -> getEquation().getThetaName() );
        exprMap.put( "Param", () -> getEquation().getParamName() );
        
        robotAsst = makeRobot();
        robot = robotAsst.getRobot();
        robot.setAutoDelay( 100 );
    }
    
    /**
     * Instantiates and loads a new Equation.
     */
    public Equation newEquation()
    {
        equation = new Exp4jEquation();
        GUIUtils.schedEDTAndWait( () -> 
            paramPanel.load( equation )
        );
        pmgr.setProperty( CPConstants.DM_MODIFIED_PN, false );
        pmgr.setProperty( CPConstants.DM_OPEN_EQUATION_PN, true );
        pmgr.setProperty( CPConstants.DM_OPEN_FILE_PN, false );
        return equation;
    }

    /**
     * Obtains the text field indicated
     * by the given string identifier
     * and clicks on it using the {@linkplain #click(JComponent)} method.
     * The string identifier must be one of 
     * Start, End, Step, Radius, Theta or Param.
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
     * Obtains the equation currently open in the PlotPanel.
     * 
     * @return  the equation currently open in the PlotPanel
     */
    private Equation getEquation()
    {
        return equation;
    }

    /**
     * Gets the JFormattedTextField adjacent to the
     * JLabel with the given text
     * add adds it to the text field map.
     * 
     * @param text  the given text
     */
    private void getTextFields()
    {
        Component[]                 children        =
            paramPanel.getComponents();
        List<JFormattedTextField>   allTextFields   =
            Stream.of( children )
                .filter( c -> (c instanceof JFormattedTextField) )
                .map( c -> (JFormattedTextField)c )
                .toList();
        
        int currField   = 0;
        for ( Component comp : children )
        {
            if ( comp instanceof JLabel )
            {
                JLabel  label   = (JLabel)comp;
                String  text    = label.getText();
                if ( !text.isEmpty() )
                    textFieldMap.put( text, allTextFields.get( currField++ ) );
            }
        }
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
}
