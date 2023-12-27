package com.acmemail.judah.cartesian_plane.components;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Window;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.StyleSheet;

import com.acmemail.judah.cartesian_plane.graphics_utils.ComponentException;

/**
 * This class consists of a set of components
 * that can be configured for use in a window.
 * The components can be obtained individually,
 * configured as a JPanel or
 * configured as a JDialog.
 * The components are:
 * <ul>
 * <li>
 * The Editor Pane<br>
 * This is a JEditorPane that is configured 
 * to display HTML text.
 * If you obtain this component
 * (by calling {@linkplain #getEditorPane()})
 * the component will not be 
 * a part of any other container.
 * </li>
 * <li>
 * The Scroll Pane<br>
 * This is a JScrollPane that is configured 
 * with the editor pane as a view.
 * If you obtain this component
 * (by calling {@linkplain #scrollPane()})
 * the component will not be 
 * a part of any other container.
 * </li>
 * <li>
 * The Message Panel<br>
 * This is a JPanel that is configured 
 * with the scroll pane as a child;
 * note that the scroll pane
 * will likely contain the editor pane as a view.
 * If you obtain this component
 * (by calling {@linkplain #getMessagePanel()})
 * the component will not be 
 * a part of any other container.
 * </li>
 * <li>
 * The Message Dialog<br>
 * This is a JDialog that is configured 
 * with the message panel as a content pane.
 * Note that the message panel
 * will contain the scroll pane,
 * which will in turn contain the editor pane as a view.
 * </li>
 * </ul>
 * <p>
 * The goal of this class
 * is to be flexible,
 * but not infinitely so.
 * If you get the dialog component,
 * be aware that it will contain the message panel,
 * and you must not make the message panel
 * a child of another container.
 * Likewise, if you get the message panel
 * and add it to a container,
 * getting the dialog would make the message panel
 * a child of two different containers,
 * which is not allowed.
 * @author Jack Straub
 */
public class HTMLMessagePanel
{
    /** HTML/CSS-aware component for displaying text. */
    private final JEditorPane   editorPane;
    /** Scroll pane; usually with view set to textPane. */
    private final JScrollPane   scrollPane;
    /** Main panel containing JScrollPane. */
    private final JPanel        mainPanel;
    /** Dialog containing this object's message panel. */
    private JDialog             dialog;
    
    /** Default CSS for configuring body element. */
    private static final String bodyRule    = 
        "body {"
        + "margin-left: 2em;"
        + "font-family: Arial, Helvetica, sans-serif;"
        + " font-size:"
        + " 14;"
        + " min-width: 70em;"
        + " white-space: nowrap;}";

    /**
     * Constructor.
     * Creates all components except the dialog.
     * Configures the editor pane
     * with the given text and style sheet; 
     * if styleSheet is null, 
     * a default will be supplied.
     * Does not configure GUI hierarchy;
     * e.g., after construction
     * the scroll pane will have no view,
     * and the main panel will have no child components.
     * 
     * @param text          the given text
     * @param styleSheet    the given style sheet
     * 
     * @see #getMessagePanel()
     * @see #getScrollPane()
     * @see #getDialog(Window, String)
     */
    public HTMLMessagePanel( String text, StyleSheet styleSheet )
    {
        HTMLEditorKit   kit = new HTMLEditorKit();
        mainPanel = new JPanel( new BorderLayout() );
        if ( styleSheet != null )
            kit.setStyleSheet( styleSheet );
        else
        {
            StyleSheet  defStyleSheet   = kit.getStyleSheet();
            defStyleSheet.addRule( bodyRule );
        }
        
        editorPane = new JEditorPane( "text/html", "" );
        scrollPane  = new JScrollPane( editorPane );
        Dimension   dim         = new Dimension( 300, 150 );
        scrollPane.setPreferredSize( dim );
    }
    
    /**
     * Returns a panel with a BorderLayout,
     * and containing the scroll pane.
     * Note that the scroll pane will have
     * the editor pane as a view.
     * <p>
     * It is safe to call this method multiple times.
     * 
     * @return  this object's message panel component
     * 
     * @see #getScrollPane()
     */
    public JPanel getMessagePanel()
    {
        if ( mainPanel.getComponentCount() == 0 )
            mainPanel.add( getScrollPane(), BorderLayout.CENTER );
        return mainPanel;
        
    }
    
    /**
     * Returns the scroll pane component
     * of this class.
     * The scroll pane will have the editor pane
     * as a view. 
     * If you get the scroll pane
     * without getting the message panel,
     * the JScrollPane component will not have a parent,
     * otherwise it will be a child of the message panel.
     * <p>
     * It is safe to call this method multiple times.
     * 
     * @return  this object's scroll pane component
     */
    public JScrollPane getScrollPane()
    {
        if ( scrollPane.getViewport() == null )
            scrollPane.setViewportView( editorPane );
        return scrollPane;
    }
    
    /**
     * Returns the editor pane component
     * of this class.
     * 
     * @return  this object's editor pane
     */
    public JEditorPane getEditorPane()
    {
        return editorPane;
    }
    
    /**
     * Gets the dialog created for this object.
     * If necessary, the object will be created
     * with the given parent and title.
     * <p>
     * It is safe to call this method multiple times,
     * however after the dialog is initially created
     * the given parent and title will be ignored.
     * 
     * @param parent    the given parent
     * @param title     the given title
     * 
     * @return  the dialog created for this object
     */
    public JDialog getDialog( Window parent, String title )
    {
        if ( dialog == null )
            createDialog( parent, title );
        return dialog;
    }
    
    /**
     * Sets the text of this object's editor pane
     * to the given String.
     * 
     * @param text  the given String
     */
    public void setText( String text )
    {
        editorPane.setText( text );
    }
    
    /**
     * Gets the text of this object's editor pane.
     * 
     * @return  the text of this object's editor pane
     */
    public String getText()
    {
        return editorPane.getText();
    }
    
    /**
     * Creates the dialog for this object,
     * using the given parent and title.
     * 
     * @param parent    the given parent
     * @param title     the given title
     */
    private void createDialog( Window parent, String title )
    {
        dialog = new JDialog( parent, title );
        JPanel  mainPanel   = getMessagePanel();
        
        if ( mainPanel.getParent() != null )
        {
            String  msg = 
                "Message panel already a part of another container.";
            throw new ComponentException( msg );
        }
        
        JPanel  controls    = new JPanel();
        JButton close       = new JButton( "Close" );
        close.addActionListener( e -> dialog.setVisible( false ) );
        
        controls.add( close );
        mainPanel.add( controls, BorderLayout.SOUTH );
        dialog.setModal( true );
        dialog.setContentPane( mainPanel );
        dialog.pack();
    }
}
