package com.acmemail.judah.battleship.ui.awt;

import static com.acmemail.judah.battleship.StatusMessages.DUP_SHIP_TYPE;
import static com.acmemail.judah.battleship.ui.awt.UIConstants.BREADTH_LABEL;
import static com.acmemail.judah.battleship.ui.awt.UIConstants.CANCEL_LABEL;
import static com.acmemail.judah.battleship.ui.awt.UIConstants.INVALID_TEXT_COLOR;
import static com.acmemail.judah.battleship.ui.awt.UIConstants.LENGTH_LABEL;
import static com.acmemail.judah.battleship.ui.awt.UIConstants.OK_LABEL;
import static com.acmemail.judah.battleship.ui.awt.UIConstants.SHIP_TYPE_DIALOG_TITLE;
import static com.acmemail.judah.battleship.ui.awt.UIConstants.TYPE_NAME_LABEL;
import static com.acmemail.judah.battleship.ui.awt.UIConstants.VALID_TEXT_COLOR;
import static com.acmemail.judah.battleship.ui.awt.UIConstants.*;

import java.awt.BorderLayout;
import java.awt.Dialog;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.LayoutManager;
import java.awt.Window;
import java.util.List;
import java.util.Objects;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;

import com.acmemail.judah.battleship.BasicProvisioner;
import com.acmemail.judah.battleship.BattleshipException;
import com.acmemail.judah.battleship.model.ShipType2D;

/**
 * Encapsulates a modal dialog to configure and add a ship type
 * to an encapsulated Provisioner's to-register list.
 * The to-register list is updated synchronously
 * when the operator configure a valid type
 * and pushes the OK button.
 */
public class RegisterTypeDialog
{
    /** The encapsulated JDialog.  */
    private final JDialog           dialog;
    /** Component for controlling the length of the new ship type. */
    private final JSpinner          lengthComp      =
        new JSpinner( getNumberModel() );
    /** Component for controlling the breadth of the new ship type. */
    private final JSpinner          breadthComp     =
        new JSpinner( getNumberModel() );
    /** Component for controlling the name of the new ship type. */
    private final JTextField        typeNameComp    = new JTextField( 10 );
    /** Component for storing and displaying error messages. */
    private final TextMessageWidget feedbackComp    = 
        new TextMessageWidget( 3, INVALID_TEXT_COLOR );
    /** The encapsulated provisioner. */
    private final BasicProvisioner  provisioner;
    
    /** The final result of a successful operation. */
    private ShipType2D  result  = null;

    /**
     * Constructor.
     * Configures this item with the given provisioner,
     * the default title,
     * and no owner.
     * 
     * @param provisioner   the given provisioner
     * 
     * @throws NullPointerException if provisioner is null
     */
    public RegisterTypeDialog( BasicProvisioner provisioner )
    {
        this( null, SHIP_TYPE_DIALOG_TITLE, provisioner );
    }
    
    /**
     * Constructor.
     * Configures this item with the given provisioner and title,
     * and no owner.
     * 
     * @param title         the given title, may be null
     * @param provisioner   the given provisioner, must not be null
     * 
     * @throws NullPointerException if provisioner is null
     */
    public RegisterTypeDialog( String title, BasicProvisioner provisioner )
    {
        this( null, title, provisioner );
    }
    
    /**
     * Configures this dialog with the given owner, title, and provisioner.
     * 
     * @param owner the given owner, may be null
     * @param title the given title, may be null
     * @param prov  the given provisioner, may not be null
     * 
     * @throws NullPointerException if prov is null
     */
    public RegisterTypeDialog( Window owner, String title, BasicProvisioner prov )
    {
        Objects.requireNonNull( prov, "prov" );
        String  workingTitle    = title != null ? 
            title : SHIP_TYPE_DIALOG_TITLE;
        this.provisioner = prov;

        Dialog.ModalityType modality    = Dialog.ModalityType.APPLICATION_MODAL;
        dialog = new JDialog( owner, workingTitle, modality );
        
        JPanel  contentPane = new JPanel( new BorderLayout() );
        contentPane.add( getMainPanel(), BorderLayout.CENTER );
        contentPane.add( getButtonPanel(), BorderLayout.SOUTH );
        dialog.setContentPane( contentPane );
        dialog.setLocation( 100, 100 );
        dialog.pack();
        
        lengthComp.setName( RDN_LENGTH );
        breadthComp.setName( RDN_BREADTH );
        typeNameComp.setName( RDN_NAME );
        feedbackComp.getTextArea().setName( RDN_MESSAGES );
    }
    
    /**
     * Displays this dialog.
     * If a new type is successfully configured
     * it is returned,
     * otherwise null is returned.
     * 
     * @return  
     *      the successfully configured type, 
     *      or null if the operation is canceled
     */
    public ShipType2D showDialog()
    {
        result = null;
        typeNameComp.setForeground( VALID_TEXT_COLOR );
        typeNameComp.setText( "" );
        feedbackComp.clear();
        lengthComp.setValue( 1 );
        breadthComp.setValue( 1 );
        dialog.setVisible( true );
        return result;
    }
    
    /**
     * Gets the main panel that constitutes the dialog.
     * @return  main panel that constitutes the dialog
     */
    private JPanel getMainPanel()
    {
        JPanel          panel   = new JPanel();
        LayoutManager   layout  = new BoxLayout( panel, BoxLayout.Y_AXIS );
        panel.setLayout( layout );

        panel.add( getDataPanel() );
        panel.add( getFeedbackPanel() );
        return panel;
    }
    
    /**
     * Gets the dialog's feedback panel.
     * 
     * @return  the dialog's feedback panel
     */
    private JPanel getFeedbackPanel()
    {
        JPanel          panel   = feedbackComp.getPanel();
        int             margin  = 3;
        Border          inner   =
            BorderFactory.createEmptyBorder( margin, margin, margin, margin );
        Border          outer   =
            BorderFactory.createBevelBorder( BevelBorder.RAISED );
        Border          border  =
            BorderFactory.createCompoundBorder( outer, inner );
        panel.setBorder( border );
        return panel;
    }
    
    /**
     * Gets the panel used to configure the ship type's parameters.
     * 
     * @return  panel used to configure the ship type's parameters
     */
    private JPanel getDataPanel()
    {
        LayoutManager   gridLayout  = new GridLayout( 3, 2, 2, 2 );
        JPanel          gridPanel   = new JPanel( gridLayout );
        gridPanel.add( getRightJLabel( LENGTH_LABEL) );
        gridPanel.add( lengthComp );
        gridPanel.add( getRightJLabel( BREADTH_LABEL ) );
        gridPanel.add( breadthComp );
        gridPanel.add( getRightJLabel( TYPE_NAME_LABEL ) );
        gridPanel.add( typeNameComp );
        
        // Adding an extra panel keeps the grid from expanding
        // across the dialog.
        LayoutManager   parentLayout    = new FlowLayout( FlowLayout.LEFT );
        JPanel          parent          = new JPanel( parentLayout );
        parent.add( gridPanel );
        return parent;
    }
    
    /**
     * Convenience method to instantiate a right-justified JLabel.
     * 
     * @param strLabel  the text of the label
     * 
     * @return  the right-justified JLabel
     */
    private static JLabel getRightJLabel( String strLabel )
    {
        JLabel  label   = new JLabel( strLabel, JLabel.RIGHT );
        return label;
    }

    /**
     * Gets the panel that contains the OK and Cancel buttons.
     * 
     * @return  the panel that contains the OK and Cancel buttons
     */
    private JPanel getButtonPanel()
    {
        LayoutManager   layout          = new FlowLayout( FlowLayout.CENTER );
        JPanel          panel           = new JPanel( layout );
        JButton         okButton        = new JButton( OK_LABEL );
        JButton         cancelButton    = new JButton( CANCEL_LABEL );
        okButton.addActionListener( e -> resolve() );
        cancelButton.addActionListener( e -> dialog.setVisible( false ) );
        panel.add( okButton );
        panel.add( cancelButton );
        
        okButton.setName( UIConstants.RDN_OK_BUTTON );
        cancelButton.setName( UIConstants.RDN_CANCEL_BUTTON );
        
        return panel;
    }
    
    /**
     * Validates the parameters that constitute the new type
     * when the OK button is pressed.
     * If valid,
     * adds the new type to the encapsulated provisioner
     * and dismisses this dialog.
     */
    private void resolve()
    {
        try
        {
            String      name    = typeNameComp.getText().trim();
            validateName( name );
            int         length  = getIntValue( lengthComp );
            int         breadth = getIntValue( breadthComp );
            ShipType2D  type    = new ShipType2D( name, length, breadth, null );
            provisioner.addToRegister( type );
            result = type;
            dialog.setVisible( false );
        }
        catch ( BattleshipException exc )
        {
            feedbackComp.append( exc.getMessage() + "\n" );
            typeNameComp.setForeground( INVALID_TEXT_COLOR );
        }
    }
    
    /**
     * Validates the name for the proposed new type.
     *
     * @param name  the name to validate
     * 
     * @throws BattleshipException if the name is not valid
     */
    private void validateName( String name )
    {
        String              upperName   = name.toUpperCase();
        List<ShipType2D>    registered  = provisioner.getToRegister();
        String              dupe        =
            registered.stream()
                .map( ShipType2D::typeName )
                .map( String::toUpperCase )
                .filter( n -> n.equals( upperName ) )
                .findFirst().orElse( null );
        if ( dupe != null )
            throw new BattleshipException( DUP_SHIP_TYPE );
    }
    
    /**
     * Gets the integer value from a given JSpinner.
     * Returns -1 if an integer value can't be obtained.
     * 
     * @param spinner   the given JSpinner
     * 
     * @return  the integer value of the JSpinner, or -1 if none.
     */
    private static int getIntValue( JSpinner spinner )
    {
        Object  oVal    = spinner.getValue();
        int     iVal    = -1;
        if ( oVal instanceof Number nVal )
        {
            iVal = nVal.intValue();
        }
        return iVal;
    }
    
    /**
     * Gets a SpinnerNumberModel for encapsulation in a JSpinner.
     * Using this method ensures that all JSpinners
     * share a similar configuration.
     * 
     * @return  a SpinnerNumberModel for encapsulation in a JSpinner
     */
    private static SpinnerNumberModel getNumberModel()
    {
        SpinnerNumberModel    model   = new SpinnerNumberModel( 1, 1, 100, 1 );
        return model;
    }
}
