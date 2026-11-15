package com.acmemail.judah.battleship.ui.awt;

import java.awt.BorderLayout;
import java.awt.Color;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

/**
 * This class encapsulates a scrolled text component
 * that displays messages oriented as lines.
 * The client can set the number of rows and columns to display,
 * and the color of the text;
 * the defaults are {@code rows = 3}, {@code columns = 0},
 * and {@code color = black}.
 * The {@link #append(String)} method automatically
 * inserts line separators as needed.
 * The {@link #getLines()} method
 * converts the lines in the text component
 * into a list of strings.
 * <p>
 * An object of this class
 * incorporates Swing components,
 * so it should be instantiated on the EDT.
 */
public class TextMessageWidget
{
    /** The default number of rows. */
    private static final int    DEF_ROWS        = 3;
    /** The default number of columns. */
    private static final int    DEF_COLS        = 0;
    /** The default text color. */
    private static final Color  DEF_COLOR       = Color.BLACK;
    /** Text component where messages are written. */
    private final JTextArea     messageArea;
    /** Scroll pane encapsulating message area. */
    private final JScrollPane   scrollPane;
    
    /**
     * Default constructor.
     * Sets all properties to their defaults.
     */
    public TextMessageWidget()
    {
        this( DEF_ROWS, DEF_COLS, DEF_COLOR );
    }
    
    /**
     * Constructor.
     * Sets the number-rows property to the given value,
     * and defaults the number-cols and text-color properties.
     * 
     * @param rows  the given number of rows
     */
    public TextMessageWidget( int rows )
    {
        this( rows, DEF_COLS, DEF_COLOR );
    }
    
    /**
     * Constructor.
     * Sets the number-rows and number-columns properties
     * to the given values,
     * and defaults the text-color property.
     * 
     * @param rows  the given number of rows
     * @param cols  the given number of columns
     */
    public TextMessageWidget( int rows, int cols )
    {
        this( rows, cols, DEF_COLOR );
    }
    
    /**
     * Constructor.
     * Sets the number-rows and text-color properties
     * to the given values,
     * and defaults the number-columns property.
     * 
     * @param rows  the given number of rows
     * @param textColor the given text color
     */
    public TextMessageWidget( int rows, Color textColor )
    {
        this( rows, DEF_COLS, textColor );
    }
    
    /**
     * Constructor.
     * Sets the number-rows, number-columns and text-color properties
     * to the given values.
     * 
     * @param rows  the given number of rows
     * @param cols  the given number of columns
     * @param textColor the given text color
     */
    public TextMessageWidget( int rows, int cols, Color textColor )
    {
        messageArea  = new JTextArea( rows, cols );
        messageArea.setEditable( false );
        scrollPane = new JScrollPane( messageArea );
        if ( textColor != null )
            setTextColor( textColor );
    }
    
    /**
     * Returns a JPanel with a BorderLayout,
     * with the scroll pane/text components
     * in the center position.
     * 
     * @return  a JPanel containing the encapsulated components
     */
    public JPanel getPanel()
    {
        JPanel  panel   = new JPanel( new BorderLayout() );
        panel.add( scrollPane, BorderLayout.CENTER );
        return panel;
    }
    
    /**
     * Returns the encapsulated JScrollPane.
     * 
     * @return  the encapsulated JScrollPane
     */
    public JScrollPane getScrollPane()
    {
        return scrollPane;
    }
    
    /**
     * Returns the encapsulated text component.
     * 
     * @return  the encapsulated text component
     */
    public JTextArea getTextArea()
    {
        return messageArea;
    }
    
    /**
     * Sets the text color to the given value.
     * 
     * @param color the given value; may not be null
     * 
     * @throws NullPointerException if color is null
     */
    public void setTextColor( Color color )
    {
        Objects.requireNonNull( color, "color" );
        messageArea.setForeground( color );
    }
    
    /**
     * Sets the number of rows to the given value.
     * 
     * @param rows  the given value
     */
    public void setNumRows( int rows )
    {
        messageArea.setRows( rows );
    }
    
    /**
     * Sets the number of columns to the given value.
     *
     * @param cols  the given value
     */
    public void setNumCols( int cols )
    {
        messageArea.setRows( cols );
    }
    
    /**
     * Gets the lines in the encapsulated text component
     * as a list of Strings,
     * one element per line,
     * with the line separators omitted.
     * 
     * @return  the lines in the text component as a list of Strings
     */
    public List<String> getLines()
    {
        String          text        = messageArea.getText();
        String[]        linesArr    = text.split( "\\r?\\n" );
        List<String>    linesList   = Arrays.asList( linesArr );
        return linesList;
    }
    
    /**
     * Appends the given message to the text 
     * of the encapsulated text component.
     * If necessary,
     * it will be preceded by a new-line
     * to separated from the previous message.
     * 
     * @param message   the given message; may not be null
     * 
     * @throws NullPointerException if message is  null
     */
    public void append( String message )
    {
        Objects.requireNonNull( message, "message" );
        String  actMessage = messageArea.getText().isEmpty() ?
            message : "\n" + message;
        messageArea.append( actMessage );
    }
    
    public void clear()
    {
        messageArea.setText( "" );
    }
}
