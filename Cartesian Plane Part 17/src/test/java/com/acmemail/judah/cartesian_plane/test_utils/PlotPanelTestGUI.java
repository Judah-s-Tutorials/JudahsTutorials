package com.acmemail.judah.cartesian_plane.test_utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.geom.Point2D;
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

import com.acmemail.judah.cartesian_plane.CartesianPlane;
import com.acmemail.judah.cartesian_plane.PlotCommand;
import com.acmemail.judah.cartesian_plane.PlotPointCommand;
import com.acmemail.judah.cartesian_plane.components.PlotPanel;
import com.acmemail.judah.cartesian_plane.graphics_utils.ComponentFinder;
import com.acmemail.judah.cartesian_plane.graphics_utils.GUIUtils;
import com.acmemail.judah.cartesian_plane.input.Command;
import com.acmemail.judah.cartesian_plane.input.Equation;

/**
 * Creates and manages a GUI containing a PlotPanel.
 * Provides utilities for the test program
 * to access and manipulate the PlotPanel
 * and the Equation it contains.
 * 
 * @author Jack Straub
 */
public class PlotPanelTestGUI extends NamedFTextFieldMgr
{    
    /** PlotPanel under test. */
    private final PlotPanel         plotPanel;
    /** Combo box containing plot options. */
    private final JComboBox<?>      comboBox;
    /** Button to initiate plot. */
    private final JButton           plotButton;

    /** CartesianPlane simulator, for testing the plot button. */
    private final PlotManager       plotManager = new PlotManager();
    
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
        
        addSupplier( "y=", () -> getEquation().getYExpression() );
        addSupplier( "x=", () -> getEquation().getXExpression() );
        addSupplier( "t=", () -> getEquation().getTExpression() );
        addSupplier( "r=", () -> getEquation().getRExpression() );
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
     * Instantiates and loads a new Equation.
     */
    public Equation newEquation()
    {
        Equation    equation    = super.newEquation();
        plotPanel.load( equation );
        setDMModified( false );
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
        Equation    equation    = getEquation();
        comboBox.setSelectedItem( cmd );
        assertEquals( cmd.toString(), equation.getPlot() );
        assertTrue( isDMModified() );
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
        addTextField( text, (JFormattedTextField)comp );
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
