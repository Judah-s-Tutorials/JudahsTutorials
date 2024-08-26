package com.acmemail.judah.cartesian_plane.components;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Toolkit;

import javax.swing.JComponent;

import com.acmemail.judah.cartesian_plane.CPConstants;
import com.acmemail.judah.cartesian_plane.GraphManager;
import com.acmemail.judah.cartesian_plane.Profile;
import com.acmemail.judah.cartesian_plane.PropertyManager;

/**
 * Component to display the background of a graph.
 * The background includes the background color,
 * grid lines, tics, 
 * and labels displayed 
 * on the major tics.
 * 
 * @author Jack Straub
 */
@SuppressWarnings("serial")
public class ProfileEditorFeedback extends JComponent
{
    /** 
     * Encapsulates to various drawing operations.
     * It is initialized in the constructor,
     * and utilized in the {@link #paintComponent(Graphics)} method.
     */
    private final GraphManager  drawManager;

    /** 
     * Graphics context.
     * Initialized using a copy of the graphics context
     * every time {@link #paintComponent(Graphics)}.
     */
    private Graphics2D          gtx;
    
    /**
     * Constructor.
     * Initializes all aspects of this components GUI.
     * Specifically,
     * its size is set to 50%
     * or the screen size.
     * 
     * @param profile   
     *      the Profile containing the properties that control the drawing
     *      
     * @see GraphManager
     */
    public ProfileEditorFeedback( Profile profile )
    {
        drawManager = new GraphManager( this.getVisibleRect(), profile );
        
        Dimension   screenSize  = 
            Toolkit.getDefaultToolkit().getScreenSize();
        float       targetWidth     = 
            PropertyManager.INSTANCE.asFloat(CPConstants.MW_WIDTH_PN );
            //(int)(.5 * screenSize.width + .5);
        int         targetHeight    = (int)(.5 * screenSize.height + .5);
        
        Dimension   canvasSize  =
            new Dimension( (int)targetWidth, targetHeight );
        setPreferredSize( canvasSize );
    }
    
    /**
     * Draws the background components of a graph
     * using the given graphics context.
     * 
     * @param   graphics    given graphics context
     */
    @Override
    public void paintComponent( Graphics graphics )
    {
        super.paintComponent( graphics );
        gtx = (Graphics2D)graphics.create();
        
        drawManager.refresh( gtx, this.getVisibleRect() );
        drawManager.drawBackground();
        drawManager.drawGridLines();
        drawManager.drawAxes();
        drawManager.drawMinorTics();
        drawManager.drawMajorTics();
        drawManager.drawText();
        
        gtx.dispose();
    }
}
