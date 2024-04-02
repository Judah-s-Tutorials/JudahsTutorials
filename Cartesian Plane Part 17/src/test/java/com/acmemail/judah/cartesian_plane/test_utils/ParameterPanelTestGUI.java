package com.acmemail.judah.cartesian_plane.test_utils;

import java.awt.BorderLayout;
import java.awt.Component;
import java.util.List;
import java.util.stream.Stream;

import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.acmemail.judah.cartesian_plane.components.ParameterPanel;
import com.acmemail.judah.cartesian_plane.input.Equation;

public class ParameterPanelTestGUI extends NamedFTextFieldMgr
{
    /** The ParameterPanel under test. */
    private final ParameterPanel    paramPanel;

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
        
        addSupplier( "Start", () -> getEquation().getRangeStartExpr() );
        addSupplier( "End", () -> getEquation().getRangeEndExpr() );
        addSupplier( "Step", () -> getEquation().getRangeStepExpr() );
        addSupplier( "Prec", () -> 
            String.valueOf( getEquation().getPrecision() )
        );
        addSupplier( "Radius", () -> getEquation().getRadiusName() );
        addSupplier( "Theta", () -> getEquation().getThetaName() );
        addSupplier( "Param", () -> getEquation().getParamName() );
    }
    
    /**
     * Instantiates and loads a new Equation.
     */
    public Equation newEquation()
    {
        Equation    equation    = super.newEquation();
        paramPanel.load( equation );
        setDMModified( false );
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
                    addTextField( text, allTextFields.get( currField++ ) );
            }
        }
    }
}