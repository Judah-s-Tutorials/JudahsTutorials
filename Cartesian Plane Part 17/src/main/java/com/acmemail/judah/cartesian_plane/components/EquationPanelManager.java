package com.acmemail.judah.cartesian_plane.components;

import javax.swing.JPanel;

import com.acmemail.judah.cartesian_plane.input.Equation;
import com.acmemail.judah.cartesian_plane.input.Exp4jEquation;

public class EquationPanelManager
{
    private Equation    equation    = new Exp4jEquation();
    
    private final VariableTable     varTable    = new VariableTable();
    private final JPanel            varPanel    = varTable.getPanel();
    private final ParameterPanel    paramPanel  = new ParameterPanel();
    private final PlotPanel         plotPanel   = new PlotPanel();
    
    public EquationPanelManager()
    {
        setEquation( equation );
    }

    /**
     * @return the equation
     */
    public Equation getEquation()
    {
        return equation;
    }

    /**
     * Sets the equation to display in the GUI.
     * 
     * @param equation the equation to set
     */
    public void setEquation(Equation equation)
    {
        this.equation = equation;
        varTable.load( equation );
        paramPanel.load( equation );
        plotPanel.load( equation );
    }
    
    public VariableTable getVarTable()
    {
        return varTable;
    }

    /**
     * @return the varPanel
     */
    public JPanel getVarPanel()
    {
        return varPanel;
    }

    /**
     * @return the paramPanel
     */
    public ParameterPanel getParamPanel()
    {
        return paramPanel;
    }

    /**
     * @return the equationPanel
     */
    public PlotPanel getPlotPanel()
    {
        return plotPanel;
    }
    
    
}
