package com.acmemail.judah.cartesian_plane.test_utils;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.awt.AWTException;
import java.awt.BorderLayout;
import java.awt.Robot;
import java.util.function.Predicate;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import com.acmemail.judah.cartesian_plane.CPConstants;
import com.acmemail.judah.cartesian_plane.PropertyManager;
import com.acmemail.judah.cartesian_plane.components.VariablePanel;
import com.acmemail.judah.cartesian_plane.graphics_utils.ComponentFinder;
import com.acmemail.judah.cartesian_plane.input.Equation;
import com.acmemail.judah.cartesian_plane.input.Exp4jEquation;

public class VariablePanelTestGUI
{
    /** Reduces typing when accessing PropertyManager singleton. */
    private static final PropertyManager    pMgr    =
        PropertyManager.INSTANCE;

    /** Text on the "+" button. */
    private static final String plusLabel   = "\u2795";
    /** Text on the "-" button. */
    private static final String minusLabel  = "\u2796";

    /** The ParameterPanel under test. */
    private final VariablePanel varPanel;
    
    private final RobotAssistant    robotAsst   = getRobot();
    private final Robot             robot       = robotAsst.getRobot();
    private final JButton           addButton;
    private final JButton           delButton;
    private final JTable            table;
    private final DefaultTableModel tableModel;

    public VariablePanelTestGUI()
    {
        JFrame  frame       = new JFrame( "Parameter Panel Test Frame" );
        JPanel  contentPane = new JPanel( new BorderLayout() );
        varPanel = new VariablePanel();
        contentPane.add( varPanel );
        frame.setContentPane( contentPane );
        frame.pack();
        frame.setVisible( true );
        
        addButton = getButton( plusLabel );
        delButton = getButton( minusLabel );
        table = getTable();
        TableModel  temp    = table.getModel();
        assertTrue( temp instanceof DefaultTableModel );
        tableModel = (DefaultTableModel)temp;
    }
    
    /**
     * Instantiates and loads a new Equation.
     */
    public Equation newEquation()
    {
        Equation    equation    = new Exp4jEquation();
        varPanel.load( equation );
        setDMModified( false );
        return equation;
    }

    /**
     * Sets the DM_MODIFIED_PN property.
     * 
     * @param val   new value of property
     */
    private void setDMModified( boolean val )
    {
        pMgr.setProperty( CPConstants.DM_MODIFIED_PN, val );
    }
    
    private JTable getTable()
    {
        Predicate<JComponent>   pred    = c -> (c instanceof JTable);
        JComponent              comp    = 
            ComponentFinder.find( varPanel, pred );
        assertNotNull( comp );
        assertTrue( comp instanceof JTable );
        return (JTable)comp;
    }

    /**
     * Gets the JButton with the given text.
     * 
     * @param text  the given text
     * 
     * @return the JButton with the given text
     */
    private JButton getButton( String text )
    {
        Predicate<JComponent>   pred    = 
            ComponentFinder.getButtonPredicate( text );
        JComponent              comp    = 
            ComponentFinder.find( varPanel, pred );
        assertNotNull( comp );
        assertTrue( comp instanceof JButton );
        return (JButton)comp;
    }
    
    private RobotAssistant getRobot()
    {
        RobotAssistant  robot   = null;
        try
        {
            robot = new RobotAssistant();
        }
        catch ( AWTException exc )
        {
            fail( "Unable to instantiate RobotAssistant" );
        }
        return robot;
    }
}