package com.acmemail.judah.cartesian_plane.components;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Window;
import java.awt.event.ActionEvent;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 * Dialog containing a {@link FontEditor},
 * used to edit the font properties
 * in some component of the Cartesian Plane graph.
 * The user provides the {@link GraphPropertySet}
 * containing the font properties
 * to be edited.
 * 
 * @author Jack Straub
 */
@SuppressWarnings("serial")
public class FontEditorDialog extends JDialog
{
    /** Title for this dialog. */
    public static final String      DIALOG_TITLE    = "Font Editor";
    /** Test and component name for the OK button. */
    public static final String      OK_LABEL        = "OK";
    /** Test and component name for the Cancel button. */
    public static final String      CANCEL_LABEL    = "Reset";
    /** Test and component name for the Reset button. */
    public static final String      RESET_LABEL     = "Reset";
    
    /** Property set being edited. */
    private final GraphPropertySet  propSet;
    /** Font editor. */
    private final FontEditor        editor;
    /** Final dialog result. */
    private int                     result;
    
    /**
     * Constructor.
     * Configures,
     * but does not display,
     * this dialog.
     * 
     * @param parent    this dialog's parent; may be null
     * @param propSet   
     *      the property set containing the font properties
     *      to be edited
     */
    public FontEditorDialog( Window parent, GraphPropertySet propSet )
    {
        super( parent, DIALOG_TITLE, ModalityType.APPLICATION_MODAL );
        this.propSet = propSet;
        this.editor = new FontEditor();
        
        JPanel  contentPane = new JPanel( new BorderLayout() );
        JPanel  editorPanel = editor.getPanel();
        contentPane.add( editorPanel, BorderLayout.CENTER );
        contentPane.add( getButtonPanel(), BorderLayout.SOUTH );
        setContentPane(contentPane);
        pack();
    }
    
    /**
     * Returns the property set ({@link #propSet})
     * containing the font properties
     * being edited.
     * 
     * @return
     */
    public GraphPropertySet getPropertySet()
    {
        return propSet;
    }
    
    /**
     * Initializes this dialog 
     * from the encapsulated property set ({@link #propSet}).
     * Waits for the dialog to close
     * and returns its final status,
     * JOptionPane.OK_OPTION or JOptionPane.CANCEL_OPTION.
     * 
     * @return  the final status of this dialog
     */
    public int showDialog()
    {
        reset( null );
        setVisible( true );
        return result;
    }
    
    /**
     * Gets the encapsulated FontEditor.
     * 
     * @return  the encapsulated FontEditor
     */
    public FontEditor getFontEditor()
    {
        return editor;
    }
    
    /**
     * Creates a panel containing the control buttons
     * displayed at the bottom of this dialog.
     * 
     * @return  a panel containing this dialog's control buttons
     */
    private JPanel getButtonPanel()
    {
        JPanel  panel           = new JPanel();
        
        JButton okButton        = new JButton( OK_LABEL );
        JButton resetButton     = new JButton( RESET_LABEL );
        JButton canButton       = new JButton( CANCEL_LABEL );
        okButton.setName( OK_LABEL );
        resetButton.setName( RESET_LABEL );
        canButton.setName( CANCEL_LABEL );
        
        okButton.addActionListener( e -> close( JOptionPane.OK_OPTION ) );        
        resetButton.addActionListener( this::reset );
        canButton.addActionListener( 
            e -> close( JOptionPane.CANCEL_OPTION )
        );
        
        panel.add( okButton );
        panel.add( resetButton );
        panel.add( canButton );
        
        return panel;
    }
    
    /**
     * Saves the values of the font property editors
     * to the encapsulated property set.
     * Invoked when the dialog's 
     * OK or Apply buttons are selected.
     */
    private void apply()
    {
        propSet.setFGColor( editor.getColor().orElse( Color.BLACK ) );
        propSet.setFontName( editor.getName() );
        propSet.setFontSize( editor.getSize().orElse( 10 ) );
        propSet.setBold( editor.isBold() );
        propSet.setItalic( editor.isItalic() );        
    }
    
    /**
     * Discards the current value of the dialog's property editors
     * and refreshes them from the given property set.
     * 
     * @param evt   object that accompanies an ActionEvent; not used
     */
    private void reset( ActionEvent evt )
    {
        editor.setColor( propSet.getFGColor() );
        editor.setName( propSet.getFontName() );
        editor.setSize( (int)propSet.getFontSize() );
        editor.setBold( propSet.isBold() );
        editor.setItalic( propSet.isItalic() );
    }
    
    /**
     * Sets the final result of this dialog
     * and closes it.
     * If the final result is JOptionPane.OK_OPTION,
     * the encapsulated property set
     * is updated from the values of the editors.
     * 
     * @param result    the dialog's final result
     */
    private void close( int result )
    {
        if ( result == JOptionPane.OK_OPTION )
            apply();
        this.result = result;
        setVisible( false );
    }
}
