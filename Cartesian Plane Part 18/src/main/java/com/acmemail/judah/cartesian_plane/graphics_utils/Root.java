package com.acmemail.judah.cartesian_plane.graphics_utils;


import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

/**
 * This class encapsulates the frame that is required
 * to assemble the GUI for our project.
 * 
 * @author Jack Straub
 */
public class Root implements Runnable
{
    /** The application frame.  */
    private JFrame  frame       = null;
    /** 
     * The frame's content pane. This window will ultimately
     * encapsulate all the components of our project's GUI.
     */
    private JPanel  contentPane = null;
    /** The window that we will be drawing on. */
    private JPanel  userPanel   = null;
    
    /**
     * Constructor.
     * 
     * @param userPanel	The window that the application will be
     * 					drawing to. Will become a child of the
     * 					content pane.
     */
    public Root( JPanel userPanel )
    {
        this.userPanel = userPanel;
    }
    
    /**
     * Start the event gathering loop and display the frame.
     */
    public void start()
    {
    	/* 
    	 * The invokeLater method displays the window
    	 * and activates the process for managing things
    	 * like button clicks, window resizing and 
    	 * window minimization/maximization.
    	 * The object passed to the method must implement
    	 * Runnable. This will eventually result in this
    	 * object's run method being invoked.
    	 */
        SwingUtilities.invokeLater( this );
    }
    
    /**
     * Indicates whether this instance has been started or not.
     * The instance is considered <em>started</em>
     * if the underlying JFrame is visible.
     * 
     * @return  true if this instance has been started
     * 
     * @see #start()
     */
    public boolean isStarted()
    {
        return frame.isVisible();
    }
    
    /**
     * This method is the place where the initial content
     * of the frame must be configured.
     * Required by the Runnable interface.
     */
    public void run()
    {
        /* Instantiate the frame. */
        frame = new JFrame( "Graphics Frame" );
        
        /* 
         * This will cause your application to be terminated
         * when the frame is closed. If you forget this step,
         * when you close the frame it will disappear,
         * but your application will continue to run.
         */
        frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        
        /* 
         * A layout manager is responsible for fine-tuning the
         * layout of a panel. For now you should consider this
         * to be boilerplate for our application. To learn more
         * about layout managers see the Oracle tutorial at
         * https://docs.oracle.com/javase/tutorial/uiswing/layout/index.html.
         * see the JDK documentation.
         */
        BorderLayout    layout      = new BorderLayout();
        contentPane = new JPanel( layout );
        
        /* Make the ProfileEditorFeedback the center child of the content pane. */
        contentPane.add( userPanel, BorderLayout.CENTER );
        /* Set the content pane in the frame. */
        frame.setContentPane( contentPane );
        /* Initiate frame sizing, positioning etc. */
        frame.pack();
        /* Make the frame visible. */
        frame.setVisible( true );
    }
}
