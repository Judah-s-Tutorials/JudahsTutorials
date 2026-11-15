package com.acmemail.judah.battleship.ui.awt;

import static com.acmemail.judah.battleship.ui.awt.UIConstants.ADD_DEFAULTS;
import static com.acmemail.judah.battleship.ui.awt.UIConstants.ADD_LABEL;
import static com.acmemail.judah.battleship.ui.awt.UIConstants.ADD_NEW;
import static com.acmemail.judah.battleship.ui.awt.UIConstants.CANCEL_LABEL;
import static com.acmemail.judah.battleship.ui.awt.UIConstants.COL_LABEL;
import static com.acmemail.judah.battleship.ui.awt.UIConstants.COPY_SELECTED;
import static com.acmemail.judah.battleship.ui.awt.UIConstants.DELETE_SELECTED;
import static com.acmemail.judah.battleship.ui.awt.UIConstants.INVALID_TEXT_COLOR;
import static com.acmemail.judah.battleship.ui.awt.UIConstants.OK_LABEL;
import static com.acmemail.judah.battleship.ui.awt.UIConstants.PLAYERS_LABEL;
import static com.acmemail.judah.battleship.ui.awt.UIConstants.ROW_LABEL;
import static com.acmemail.judah.battleship.ui.awt.UIConstants.SDN_CANCEL_BUTTON;
import static com.acmemail.judah.battleship.ui.awt.UIConstants.SDN_COLS;
import static com.acmemail.judah.battleship.ui.awt.UIConstants.SDN_DEP_DEL_BUTTON;
import static com.acmemail.judah.battleship.ui.awt.UIConstants.SDN_DEP_LIST;
import static com.acmemail.judah.battleship.ui.awt.UIConstants.SDN_OK_BUTTON;
import static com.acmemail.judah.battleship.ui.awt.UIConstants.SDN_PLAYERS_ADD_BUTTON;
import static com.acmemail.judah.battleship.ui.awt.UIConstants.SDN_PLAYERS_ADD_TEXT;
import static com.acmemail.judah.battleship.ui.awt.UIConstants.SDN_PLAYERS_DEL_BUTTON;
import static com.acmemail.judah.battleship.ui.awt.UIConstants.SDN_PLAYERS_LIST;
import static com.acmemail.judah.battleship.ui.awt.UIConstants.SDN_REG_ADD_DEF_BUTTON;
import static com.acmemail.judah.battleship.ui.awt.UIConstants.SDN_REG_ADD_NEW_BUTTON;
import static com.acmemail.judah.battleship.ui.awt.UIConstants.SDN_REG_DEL_BUTTON;
import static com.acmemail.judah.battleship.ui.awt.UIConstants.SDN_REG_LIST;
import static com.acmemail.judah.battleship.ui.awt.UIConstants.SDN_ROWS;
import static com.acmemail.judah.battleship.ui.awt.UIConstants.SETUP_WINDOW_TITLE;
import static com.acmemail.judah.battleship.ui.awt.UIConstants.TO_DEPLOY_LABEL;
import static com.acmemail.judah.battleship.ui.awt.UIConstants.TO_REGISTER_LABEL;
import static com.acmemail.judah.battleship.ui.awt.UIConstants.VALID_TEXT_COLOR;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dialog.ModalityType;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.LayoutManager;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import com.acmemail.judah.battleship.BasicProvisioner;
import com.acmemail.judah.battleship.Constants;
import com.acmemail.judah.battleship.StatusMessages;
import com.acmemail.judah.battleship.model.ShipType2D;

/**
 * An instance of this class encapsulates a 
 * {@link BasicProvisioner} document, 
 * and manages dialog that provides access to it.
 * The dialog is modal.
 * Updates made in the dialog are synchronously
 * applied to the encapsulated provisioner.
 */
public class SetupPhaseAWT
{
    /** Sets alignmentX to CENTER_ALIGNMENT on a JComponent. */
    private static final Consumer<JComponent>   xAlignCenter    =
        jc -> jc.setAlignmentX( Component.CENTER_ALIGNMENT );
    /** Instantiates a small spacer for spacing components in a panel. */
    private static final Supplier<Component>    smallSpacer     =
        () -> Box.createRigidArea( new Dimension( 3, 3 ) );
    /** Instantiates a medium-sized spacer for spacing in a panel. */
    private static final Supplier<Component>    mediumSpacer    =
        () -> Box.createRigidArea( new Dimension( 5, 5 ) );
        
    /** Defines the dimensions of all JLists used in the dialog. */
    private static final Dimension              listSize        =
        new Dimension( 150, 150 );

    /** Default size for the default margin border. */
    private static final int                    margin          = 3;
    /** Default margin border. */
    private static final Border                 defMarginBorder =
        BorderFactory.createEmptyBorder( margin, margin, margin, margin );
    /** Default outer border for construction of compound borders. */
    private static final Border                 defOuterBorder  =
        BorderFactory.createBevelBorder( BevelBorder.RAISED );
    /** Default border. */
    private static final Border                 defBorder       =
        BorderFactory.createCompoundBorder( defOuterBorder, defMarginBorder );
    
    /** Scrolled list of types to deploy. */
    private final ScrolledList<FriendlyType>    toDeployItems;
    /** Scrolled list of types to register. */
    private final ScrolledList<FriendlyType>    toRegisterItems;
    /** Scrolled list of player names. */
    private final ScrolledList<String>          playerItems;

    /** The dialog serving as the root of the GUI (*/
    private final JDialog               mainWindow;
    /** The encapsulated provisioner. */
    private final BasicProvisioner      provisioner;
    /** Spinner component that controls the number of grid rows. */
    private final JSpinner              rowComp     =
        new JSpinner( getNumberModel() );
    /** Spinner component that controls the number of grid columns. */
    private final JSpinner              colComp     =
        new JSpinner( getNumberModel() );
    /** Component for storing and displaying error messages. */
    private final TextMessageWidget     errorComp   = 
        new TextMessageWidget( 3, 0 );
    /** 
     * Dialog for entering parameters for a new ship type. 
     * Automated tests may depend on this component being instantiated
     * during construction.
     */
    private final RegisterTypeDialog    newTypeDialog;

    /** Indicates whether the dialog was dismissed with OK or Cancel. */
    private boolean isOK    = false;

    /**
     * Constructor.
     * Instantiates this object using the given provisioner,
     * a default title,
     * and no owner.
     * 
     * @param provisioner the given provisioner.
     */
    public SetupPhaseAWT( BasicProvisioner provisioner )
    {
        this( null, SETUP_WINDOW_TITLE, provisioner );
    }
    
    /**
     * Constructor.
     * Instantiates this object using the given title and provisioner,
     * and no owner.
     * 
     * @param title         the given title
     * @param provisioner   the given provisioner
     */
    public SetupPhaseAWT( String title, BasicProvisioner provisioner )
    {
        this( null, title, provisioner );
    }
    
    /**
     * Constructor.
     * Instantiates this object using the given title, provisioner,
     * and owner.
     * 
     * @param title     the given title, may be null
     * @param prov      the given provisioner, may not be null
     * @param owner     the given owner, may be null
     * 
     * @throws NullPointerException if prov is null
     * 
     * @throws IllegalStateException if instantiation cannot be completed
     */
    public SetupPhaseAWT( Window owner, String title, BasicProvisioner prov )
    {
        Objects.requireNonNull( prov, "prov" );
        this.provisioner = prov; 
        newTypeDialog = new RegisterTypeDialog( prov );
        toRegisterItems = 
            new ScrolledList<>( getFriendlyList( prov.getToRegister() ) );
        toDeployItems = 
            new ScrolledList<>( getFriendlyList( prov.getToDeploy() ) );
        playerItems = 
            new ScrolledList<>( prov.getPlayers() );
        errorComp.getTextArea().setName( UIConstants.SDN_MESSAGES );
        String          workingTitle    = title != null ? 
            title : SETUP_WINDOW_TITLE;
        Integer         numRows         = prov.getRows();
        Integer         numCols         = prov.getCols();
        // If not already set, set the number of rows and columns
        // to their default values.
        if ( numRows == null )
            numRows = Constants.DEF_NUM_ROWS;
        if ( numCols == null )
            numCols = Constants.DEF_NUM_COLS;
        
        if ( rowComp.getModel() instanceof SpinnerNumberModel numModel )
        {
            int minParam    = ((Number)numModel.getMinimum()).intValue();
            int maxParam    = ((Number)numModel.getMaximum()).intValue();
            numRows = Math.min( numRows, maxParam );
            numRows = Math.max( numRows, minParam );
            numCols = Math.min( numCols, maxParam );
            numCols = Math.max( numCols, minParam );
        }
        else
            throw new IllegalStateException( "Invalid spinners" );

        rowComp.setValue( numRows );
        colComp.setValue( numCols );
        prov.setCols( numCols );
        prov.setRows( numRows );

        ModalityType    modality    = ModalityType.APPLICATION_MODAL;
        mainWindow = new JDialog( owner, workingTitle, modality );
        
        JPanel  contentPane = new JPanel( new BorderLayout() );
        contentPane.add( getWestPanel(), BorderLayout.WEST );
        contentPane.add( getCenterPanel(), BorderLayout.CENTER );
        contentPane.add( getSouthPanel(), BorderLayout.SOUTH );
        
        mainWindow.setContentPane( contentPane );
        mainWindow.pack();
    }

    /**
     * Makes this dialog visible.
     * Doesn't return until the dialog is dismissed.
     * 
     * @return  true, if the dialog was dismissed with the OK button
     */
    public boolean showDialog()
    {
        mainWindow.setVisible( true );
        return isOK;
    }
    
    /**
     * Gets the encapsulated provisioner.
     * 
     * @return  the encapsulated provisioner
     */
    public BasicProvisioner getProvisioner()
    {
        return provisioner;
    }
    
    /**
     * Configures the west panel of the content pane's border-layout
     * 
     * @return  a configured JPanel
     */
    private JPanel getWestPanel()
    {
        JPanel          panel       = new JPanel();
        LayoutManager   layout      = new BoxLayout( panel, BoxLayout.Y_AXIS );
        panel.setBorder( defMarginBorder );
        panel.setLayout( layout );
        panel.add( getSpinnerPanel() );
        panel.add( getPlayersPanel() );

        adjustChildren( panel, xAlignCenter );
        return panel;
    }
    
    /**
     * Gets a panel encapsulating the spinners
     * that control the number of grid rows and column.
     * 
     * @return  the configured panel
     */
    private JPanel getSpinnerPanel()
    {
        LayoutManager   layout  = new GridLayout( 2, 2, 2, 2 );
        JPanel          panel   = new JPanel( layout );
        rowComp.addChangeListener( e -> 
            provisioner.setRows( (Integer)rowComp.getValue() )
        );
        colComp.addChangeListener( e -> 
            provisioner.setCols( (Integer)colComp.getValue() )
        );
        rowComp.setName( SDN_ROWS );
        colComp.setName( SDN_COLS );
        
        panel.setBorder( defBorder );
        panel.add( new JLabel( ROW_LABEL, JLabel.RIGHT ) );
        panel.add( rowComp );
        panel.add( new JLabel( COL_LABEL, JLabel.RIGHT ) );
        panel.add( colComp );

        return wrapNonExpanding( panel );
    }
    
    /**
     * Gets a panel containing those controls
     * used to configure the players list.
     * 
     * @return  the configured dialog
     */
    private JPanel getPlayersPanel()
    {
        JPanel          panel       = new JPanel();
        LayoutManager   layout      = new BoxLayout( panel, BoxLayout.Y_AXIS );
        panel.setLayout( layout );
        panel.setBorder( defBorder );

        JButton         delete      = new JButton( DELETE_SELECTED );
        delete.setName( SDN_PLAYERS_DEL_BUTTON );
        delete.addActionListener( e -> deleteFromPlayers() );
        playerItems.getList().setName( SDN_PLAYERS_LIST );
        playerItems.setPreferredSize( listSize );
        
        JPanel  addPanel    = getAddPlayerPanel();
        panel.add( new JLabel( PLAYERS_LABEL ) );
        panel.add( playerItems.getScrolledList() );
        panel.add( smallSpacer.get() );
        panel.add( delete );
        panel.add( smallSpacer.get() );
        panel.add( addPanel );
        adjustChildren( panel, xAlignCenter );

        return wrapNonExpanding( panel );
    }
    
    /**
     * Gets the sub-panel containing those controls
     * that add a name to the players list.
     * 
     * @return  the configured dialog
     */
    private JPanel getAddPlayerPanel()
    {
        Function<String,String>     validator   = text -> {
            String  result  = null;
            if ( !provisioner.getPlayers().contains( text ) )
            {
                provisioner.addPlayer( text );
                playerItems.append( text );
            }
            else
                result = StatusMessages.DUP_PLAYER;
            return result;
        };

        JTextField          textField   = new JTextField( 10 );
        JButton             button      = new JButton( ADD_LABEL );
        
        textField.setName( SDN_PLAYERS_ADD_TEXT );
        button.setName( SDN_PLAYERS_ADD_BUTTON );
        
        JPanel              panel       = new JPanel();
        LayoutManager       layout      = 
            new BoxLayout( panel, BoxLayout.X_AXIS );
        panel.setLayout( layout );
        panel.add( textField );
        panel.add( smallSpacer.get() );
        panel.add( button );
        
        button.setEnabled( false );
        
        TextChangeListener  textListener    = 
            new TextChangeListener( textField, button, validator );
        textField.getDocument().addDocumentListener( textListener );
        textField.addActionListener( textListener );
        button.addActionListener( textListener );
        
        return panel;
    }

    /**
     * Gets the panel that occupies the center of the dialog.
     * 
     * @return  the configured dialog
     */
    private JPanel getCenterPanel()
    {
        JPanel       separator      = getCopierPanel();
        JPanel          panel       = new JPanel();
        LayoutManager   layout      = new BoxLayout( panel, BoxLayout.X_AXIS );
        panel.setBorder( defMarginBorder );
        panel.setLayout( layout );
        panel.add( getToRegisterPanel() );
        panel.add( separator );
        panel.add( getToDeployPanel() );
        
        JPanel          masterPanel     = new JPanel();
        LayoutManager   masterLayout    = 
            new BoxLayout( masterPanel, BoxLayout.Y_AXIS );
        masterPanel.setLayout( masterLayout );
        masterPanel.setBorder( defMarginBorder );
        masterPanel.add( panel );
        masterPanel.add( getButtonPanel() );
        return masterPanel;
    }
    
    /**
     * Gets a panel containing those controls
     * used to configure the to-register list.
     * 
     * @return  the configured dialog
     */
    private JPanel getToRegisterPanel()
    {
        toRegisterItems.setPreferredSize( listSize );
        JPanel                      panel       = new JPanel();
        LayoutManager               layout      =
            new BoxLayout( panel, BoxLayout.Y_AXIS );
        panel.setLayout( layout );
        panel.setBorder( defBorder );
        toRegisterItems.getList().setName( SDN_REG_LIST );
        
        panel.add( new JLabel( TO_REGISTER_LABEL ) );
        panel.add( toRegisterItems.getScrolledList() );
        panel.add( smallSpacer.get() );
        panel.add( getToRegisterButtonPanel() );
        adjustChildren( panel, xAlignCenter );

        return wrapNonExpanding( panel );
    }
    
    /**
     * The sub-panel containing the buttons used to
     * interact with the to-register list.
     * 
     * @return the configured panel
     */
    private JPanel getToRegisterButtonPanel()
    {
        JPanel          panel       = new JPanel( new GridLayout( 3, 1, 2, 2 ) );
        JButton         delete      = new JButton( DELETE_SELECTED );
        JButton         defaults    = new JButton( ADD_DEFAULTS );
        JButton         addNew      = new JButton( ADD_NEW );
        
        delete.setName( SDN_REG_DEL_BUTTON );
        defaults.setName( SDN_REG_ADD_DEF_BUTTON );
        addNew.setName( SDN_REG_ADD_NEW_BUTTON );
        
        defaults.addActionListener( e -> addDefaults( toRegisterItems ) );
        addNew.addActionListener( e -> addNewType() );
        delete.addActionListener( e -> deleteFromToRegister() );
        
        panel.setBorder( defMarginBorder );
        panel.add( delete );
        panel.add( defaults );
        panel.add( addNew );
        return panel;
    }
    
    /**
     * Gets a panel containing those controls
     * used to configure the to-deploy list.
     * 
     * @return  the configured dialog
     */
    private JPanel getToDeployPanel()
    {
        toDeployItems.setPreferredSize( listSize );
        JPanel                      panel       = new JPanel();
        LayoutManager               layout      =
            new BoxLayout( panel, BoxLayout.Y_AXIS );
        panel.setLayout( layout );
        panel.setBorder( defBorder );

        JButton             delete          = new JButton( DELETE_SELECTED );
        delete.addActionListener( e -> deleteFromToDeploy() );
        toDeployItems.getList().setName( SDN_DEP_LIST );
        delete.setName( SDN_DEP_DEL_BUTTON );

        panel.add( new JLabel( TO_DEPLOY_LABEL ) );
        panel.add( toDeployItems.getScrolledList() );
        panel.add( smallSpacer.get() );
        panel.add( delete );
        panel.add( smallSpacer.get() );
        adjustChildren( panel, xAlignCenter );

        return wrapNonExpanding( panel );
    }

    /**
     * Configures a panel to contain the OK and Cancel buttons.
     * 
     * @return  the configured panel
     */
    private JPanel getButtonPanel()
    {
        JPanel          panel   = new JPanel();
        LayoutManager   layout  = new BoxLayout( panel, BoxLayout.X_AXIS );
        panel.setLayout( layout );
        panel.setBorder( defMarginBorder );
        
        JButton         okay    = new JButton( OK_LABEL );
        JButton         cancel  = new JButton( CANCEL_LABEL );
        okay.setName( SDN_OK_BUTTON );
        cancel.setName( SDN_CANCEL_BUTTON );
        okay.addActionListener( e -> closeDialog( true ) );
        cancel.addActionListener( e -> closeDialog( false ) );
        panel.add( okay );
        panel.add( mediumSpacer.get() );
        panel.add( cancel );
        
        return panel;
    }
    
    /**
     * Configures the south panel of the main dialog.
     * 
     * @return  the configured panel
     */
    private JPanel getSouthPanel()
    {
        JPanel      panel       = errorComp.getPanel();
        panel.setBorder( defBorder );
        return panel;
    }
    
    /**
     * Gets the panel that contains the "Copy-To" controls.
     * 
     * @return  the configured panel
     */
    private JPanel getCopierPanel()
    {
        Image       image   = CopySelectedImage.getImage( 75, 20, 0 );
        ImageIcon   arrow   = new ImageIcon( image );
        JLabel      label   = new JLabel( arrow );
        JButton     button  = new JButton( COPY_SELECTED );
        button.setName( UIConstants.SDN_COPY_BUTTON );
        button.addActionListener(  e -> copySelected()  );

        JPanel          panel   = new JPanel();
        LayoutManager   layout  = new BoxLayout( panel, BoxLayout.Y_AXIS );
        panel.setLayout( layout );
        panel.add( mediumSpacer.get() );
        panel.add( label );
        panel.add( button );
        panel.add( mediumSpacer.get() );
        
        adjustChildren( panel, xAlignCenter );        
        return panel;
    }
    
    /**
     * Sets the dialog result to the given value
     * and dismisses the dialog.
     * 
     * @param result    the given result
     */
    private void closeDialog( boolean result )
    {
        isOK = result;
        mainWindow.setVisible( false );
    }

    /**
     * Remove from the selected items from the players list.
     */
    private void deleteFromPlayers()
    {
        List<String>    list    = playerItems.getSelectedItems();
        list.forEach( provisioner::removePlayer );
        playerItems.deleteSelected();
    }

    /**
     * Remove from the selected items from the to-deploy list.
     */
    private void deleteFromToDeploy()
    {
        List<FriendlyType>  list    = toDeployItems.getSelectedItems();
        list.stream()
            .map( FriendlyType::type )
            .forEach( provisioner::removeFromDeploy );
        toDeployItems.deleteSelected();
    }
    
    /**
     * Remove from the selected items from the to-register list.
     */
    private void deleteFromToRegister()
    {
        List<FriendlyType>  toRegisterSel = toRegisterItems.getSelectedItems();

        toRegisterSel.forEach( r -> {
            ShipType2D  type    = r.type();
            while ( provisioner.getToDeploy().contains( type ) )
            {
                provisioner.removeFromDeploy( type );
                toDeployItems.removeElement( r );
            }
        });
        
        toRegisterSel.stream()
            .map( FriendlyType::type )
            .forEach( provisioner::removeFromRegister );
        toRegisterItems.deleteSelected();
    }
    
    /**
     * Wraps a panel inside another panel with a FlowLayout,
     * which will prevent it from expanding
     * when the window is resized.
     *
     * @param panel the panel to wrap
     *
     * @return  the wrapping panel
     */
    private static JPanel wrapNonExpanding( JPanel panel )
    {
        JPanel  masterPanel = new JPanel();
        masterPanel.add( panel );
        return masterPanel;
    }

    /**
     * Convenience routine to set a configuration parameter
     * on every child component of a given parent component.
     *
     * @param parent    the given parent
     * @param adjuster  the logic to adjust the children
     */
    private static void adjustChildren( 
        JComponent parent, 
        Consumer<JComponent> adjuster
    )
    {
        Arrays.stream( parent.getComponents() )
            .filter( c -> c instanceof JComponent )
            .map( c -> (JComponent)c )
            .forEach( adjuster::accept );
    }
    
    /**
     * Gets a SpinnerNumberModel to encapsulate in a JSpinner.
     * Using this method ensures that every numeric JSpinner
     * is configured the same way.
     * 
     * @return  the configured spinner model
     */
    private static SpinnerNumberModel getNumberModel()
    {
        SpinnerNumberModel    model   = 
            new SpinnerNumberModel( 10, 10, 10_000, 1 );
        return model;
    }
    
    /**
     * Obtains a list of default ShipType2Ds
     * (Battleship, Submarine, etc.},
     * encapsulates each in a {@link FriendlyType},
     * and adds the FriendlyType to the given ScrolledList.
     * 
     * @param scrolledList  the given ScrolledList
     */
    private void addDefaults( ScrolledList<FriendlyType> scrolledList )
    {
        List<ShipType2D>    defaultTypes    = BasicProvisioner.getDefaultTypes();
        List<ShipType2D>    registered      = provisioner.getToRegister();
        defaultTypes.stream()
            .filter( t -> !registered.contains( t ) )
            .forEach( t -> {
                scrolledList.append( new FriendlyType( t ) );
                provisioner.addToRegister( t );
            });
    }
    
    /**
     * Copies the selected items from the to-register list
     * to the to-deploy list.
     */
    private void copySelected()
    {
        List<FriendlyType>  selected    = toRegisterItems.getSelectedItems();
        selected.stream()
            .map( FriendlyType::type )
            .forEach( t -> {
                provisioner.addToDeploy( t );
                toDeployItems.append( new FriendlyType( t ) );
            });
    }
    
    /**
     * Shows the new-ship-type dialog.
     * If a new ship type is successfully configured,
     * the new-ship-type dialog adds the configured type
     * to the provisioner's to-register list,
     * and this method encapsulates the type
     * in a {@link FriendlyType}
     * and adds it to the GUI's to-register scrolled list.
     */
    private void addNewType()
    {
        // The new-type dialog does all validation (such as checking
        // for duplicate names) and updates the provisioner if the
        // operation runs to completion.
        ShipType2D  type    = newTypeDialog.showDialog();
        if ( type != null )
            toRegisterItems.append( new FriendlyType( type ) );
    }
    
    /**
     * Converts a given list of ShipType2Ds to {@link FriendlyType}s.
     * 
     * @param listIn    the given list of ship types
     * 
     * @return  the converted list
     */
    private static List<FriendlyType> getFriendlyList( List<ShipType2D> listIn )
    {
        List<FriendlyType>  listOut = 
            listIn.stream()
            .map( FriendlyType::new )
            .toList();
        return listOut;
    }
    
    /**
     * Encapsulates the logic to add a new item to the players list.
     * It's responsibilities include 
     * enabling the add-player button when the associated text is changed,
     * disabling the add-player button when a player name is committed,
     * validating the player name before it's committed,
     * and setting the color of the text
     * depending on whether the most recent validation succeeded.
     */
    private class TextChangeListener 
        implements DocumentListener, ActionListener
    {
        /** The text field in which the operator enters a player name. */
        private final JTextField    textField;
        /** The add button used to commit a player name. */
        private final JButton       addButton;
        /** The logic to validate a player name before it is committed. */
        private final Function<String,String> validator;
        
        /**
         * Constructor.
         *  
         * @param textField     the text field for editing the player name
         * @param addButton     the add button for committing the player name
         * @param validator     
         *      the logic to validate the name prior to committing it.
         */
        public TextChangeListener(
            JTextField textField, 
            JButton addButton, 
            Function<String, String> validator
        )
        {
            super();
            this.textField = textField;
            this.addButton = addButton;
            this.validator = validator;
        }

        @Override
        public void insertUpdate(DocumentEvent e)
        {
            processChange();
        }

        @Override
        public void removeUpdate(DocumentEvent e)
        {
            processChange();
        }

        @Override
        public void changedUpdate(DocumentEvent e)
        {
            processChange();
        }
        
        @Override
        public void actionPerformed( ActionEvent evt )
        {
            // The add button and text field action events both land here.
            // A text field action event activates the add button,
            // and the add button action event validates and commits
            // the player name.
            Object  source  = evt.getSource();
            if ( source == textField )
                addButton.doClick();
            else
            {
                String  result  = validator.apply( textField.getText() );
                if ( result == null )
                    textField.setText( "" );
                else
                {
                    textField.setForeground( INVALID_TEXT_COLOR );
                    errorComp.append( "\n" + result );
                }
            }
        }
        
        /**
         * Sets the color of the text to "valid"
         * with every edit
         * (the {@link #actionPerformed(ActionEvent)} method
         * manages the invalid logic.)
         * when necessary).
         */
        private void processChange()
        {
            String  text    = textField.getText();
            addButton.setEnabled( !text.isBlank() );
            textField.setForeground( VALID_TEXT_COLOR );
        }
    }
}
