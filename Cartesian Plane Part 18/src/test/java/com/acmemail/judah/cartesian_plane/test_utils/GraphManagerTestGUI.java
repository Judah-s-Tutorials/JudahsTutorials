package com.acmemail.judah.cartesian_plane.test_utils;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import com.acmemail.judah.cartesian_plane.components.LinePropertySet;
import com.acmemail.judah.cartesian_plane.components.LinePropertySetGridLines;
import com.acmemail.judah.cartesian_plane.components.Profile;
import com.acmemail.judah.cartesian_plane.sandbox.profile.Canvas;

/**
 * Test GUI for the GraphManager.
 * 
 * width/height = 601
 *     center pixel = 300
 * gridunit = 100
 * gridlines/unit = 1
 *      gridline 100, 200, 400, 500
 * gridlines/unit = 2
 *      gridline 50, 100, 150, 200, 250, 400, 450, 500, 550
 * 
 * @author Jack Straub
 */
public class GraphManagerTestGUI
{
    /** Application frame title. */
    private static final String title       = "GraphManager Test GUI";
    
    /** Application frame. */
    private final JFrame        frame       = new JFrame( title );
    /** Profile used in testing. */
    private final Profile       profile     = new Profile();
    /** Place to draw sample graph. */
    private final Canvas        canvas      = new Canvas( profile );
    
    public GraphManagerTestGUI()
    {
        frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        Dimension   canvasDim   = new Dimension( 513, 513 );
        canvas.setPreferredSize( canvasDim );
        
        JPanel  contentPane = new JPanel( new BorderLayout() );
        contentPane.add( canvas, BorderLayout.CENTER );
        contentPane.add( getControlPanel(), BorderLayout.SOUTH );
        
        frame.setContentPane( contentPane );
        frame.pack();
        frame.setVisible( true );
        
        next( null );
    }
    
    private JPanel getControlPanel()
    {
        JPanel  panel   = new JPanel();
        JButton next    = new JButton( "Next" );
        next.addActionListener( this::next );
        panel.add( next );
        return panel;
    }
    
    private void next( ActionEvent evt )
    {
        profile.setGridUnit( 100 );
        LinePropertySet set = 
            profile.getLinePropertySet( LinePropertySetGridLines.class.getSimpleName() );
        set.setSpacing( 1 );
        canvas.repaint();
    }
}
