package com.acmemail.judah.battleship.ui.awt;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.IntStream;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JTextField;
import javax.swing.ListModel;

import com.acmemail.judah.battleship.BasicProvisioner;
import com.acmemail.judah.battleship.components.ComponentFinder;
import com.acmemail.judah.battleship.components.ComponentUtils;
import com.acmemail.judah.battleship.model.ShipType2D;

/**
 * Assistant to interrogate and control
 * a setup-phase dialog.
 * Manages component selection,
 * and makes sure interaction with the GUI
 * takes place on the EDT.
 */
public class SetupAWTTestAssistant extends DialogTestAssistant
{
    /** The dialog underlying the GUI under test. */
    private JDialog                 dialog;
    /** The GUI under test. */
    private SetupPhaseAWT           gui;
    /** True, if the dialog under test was closed with the OK button. */
    private boolean                 closeAction;
    /** Assistant for controlling the add-type dialog. */
    RegisterTypeTestAssistant       typeAssist;
    
    /**
     * Instantiate and show a setup dialog
     * using the a new, empty provisioner.
     * 
     * @throws IllegalStateException if the operation fails
     * 
     * @see SetupAWTTestAssistant#SetupAWTTestAssistant(BasicProvisioner)
     */
    public SetupAWTTestAssistant()
    {
        this( new BasicProvisioner() );
    }

    /**
     * Instantiate and show a setup dialog
     * using the given provisioner.
     * The dialog is started in a separate thread
     * and this constructor returns immediately.
     * The operation can fail if
     * a) searching for dialog components is unsuccessful, or
     * b) the dialog fails to show.
     * 
     * @param provisioner   the given provisioner
     * 
     * @throws IllegalStateException if the operation fails
     */
    public SetupAWTTestAssistant( BasicProvisioner provisioner )
    {
        List<String>    errorList   = new ArrayList<String>();
        ComponentUtils.invokeAndWait( () -> {
            gui = new SetupPhaseAWT( provisioner );
            dialog = ComponentFinder.getDialog( UIConstants.SETUP_WINDOW_TITLE );
            if ( dialog == null )
                throw new IllegalStateException( "GUI dialog not found" );
            List<String> errors = initMap( dialog, s -> s.startsWith( "SDN" ) );
            errorList.addAll( errors );
        });
        
        // Check for errors when trying to locate components
        if ( !errorList.isEmpty() )
        {
            errorList.addFirst( "Component location failure:" );
            String  message = String.join( "\n", errorList );
            throw new IllegalStateException( message );
        }
        
        startGUIThread();
    }
    
    /**
     * Gets the provisioner assigned to the encapsulated application dialog.
     * 
     * @return  the provisioner assigned to the encapsulated application dialog
     */
    public BasicProvisioner getProvisioner()
    {
        BasicProvisioner    result  =
            gui == null ? null : gui.getProvisioner();
        return result;
    }
    
    /**
     * Click the OK button
     * and wait for the dialog to close.
     * 
     * @throws IllegalStateException if the dialog fails to close
     */
    public void clickOK()
    {
        JButton comp    = getJButton( UIConstants.SDN_OK_BUTTON );
        execTerminalAction( dialog, comp::doClick );
    }
    
    /**
     * Click the Cancel button
     * and wait for the dialog to close.
     * 
     * @throws IllegalStateException if the dialog fails to close
     */
    public void clickCancel()
    {
        JButton comp    = getJButton( UIConstants.SDN_CANCEL_BUTTON );
        execTerminalAction( dialog, comp::doClick );
    }
    
    /**
     * Click the JButton with the given name.
     * 
     * @param name  the given name
     * 
     * @throws IllegalStateException 
     *      if a JButton with the given name cannot be found
     */
    public void clickJButton( String name )
    {
        JButton comp    = getJButton( name );
        ComponentUtils.invokeAndWait( comp::doClick );
    }
    
    /**
     * Obtains a list of ShipType2D elements
     * derived from the JList
     * containing to-register items.
     * 
     * @return  the obtained list
     * 
     * @throws IllegalStateException if the operation fails
     */
    public List<ShipType2D> getToRegister()
    {
        List<ShipType2D>    list    = getTypeList( UIConstants.SDN_REG_LIST );
        return list;
    }
    
    /**
     * Obtains a list of ShipType2D elements
     * derived from the JList
     * containing to-deploy items.
     * 
     * @return  the obtained list
     * 
     * @throws IllegalStateException if the operation fails
     */
    public List<ShipType2D> getToDeploy()
    {
        List<ShipType2D>    list    = getTypeList( UIConstants.SDN_DEP_LIST );
        return list;
    }
    
    /**
     * Obtains a list of Strings
     * derived from the JList
     * containing player names.
     * 
     * @return  the obtained list
     * 
     * @throws IllegalStateException if the operation fails
     */
    public List<String> getPlayers()
    {
        JList<?>        jList   = getJList( UIConstants.SDN_PLAYERS_LIST );
        ListModel<?>    model   = jList.getModel();
        List<String>    players = new ArrayList<>();
        ComponentUtils.invokeAndWait( () ->
            IntStream.range( 0, model.getSize() )
            .mapToObj( model::getElementAt )
            .map( Object::toString )
            .forEach( players::add )
        );
            
        return players;
    }
    
    /**
     * Select the ship types to register at the given indices.
     * 
     * @param indices   the given indices
     */
    public void selectToRegister( int... indices )
    {
        selectItems( UIConstants.SDN_REG_LIST, indices );
    }
    
    /**
     * Select the ship types to deploy at the given indices.
     * 
     * @param indices   the given indices
     */
    public void selectToDeploy( int... indices )
    {
        selectItems( UIConstants.SDN_DEP_LIST, indices );
    }
    
    /**
     * Select the players at the given indices.
     * 
     * @param indices   the given indices
     */
    public void selectPlayers( int... indices )
    {
        selectItems( UIConstants.SDN_PLAYERS_LIST, indices );
    }
    
    /**
     * Select the items in the given list at the given indices.
     * 
     * @param listName  the given list
     * @param indices   the given indices
     * 
     * @throws IllegalStateException if the operation can't be completed
     */
    private void selectItems ( String listName, int... indices )
    {
        JList<?>        jList   = getJList( listName );
        ComponentUtils.invokeAndWait( () -> {
            jList.clearSelection();
            jList.setSelectedIndices( indices );
        });
    }

    /**
     * Gets the value of the num-rows component.
     * 
     * @return  the value of the num-rows component
     * 
     * @throws IllegalStateException if the operation fails
     */
    public int getNumRows()
    {
        int numRows = getSpinnerInt( UIConstants.SDN_ROWS );
        return numRows;
    }
    
    /**
     * Sets the value of the num-rows component.
     * 
     * @param   newRows new value for num-rows component
     * 
     * @throws IllegalStateException if the operation fails
     */
    public void setNumRows( int newRows )
    {
        setSpinnerInt( newRows, UIConstants.SDN_ROWS );
    }
    
    /**
     * Gets the value of the num-columns component.
     * 
     * @return  the value of the num-columns component
     * 
     * @throws IllegalStateException if the operation fails
     */
    public int getNumCols()
    {
        int numCols = getSpinnerInt( UIConstants.SDN_COLS );
        return numCols;
    } 
    
    /**
     * Sets the value of the num-cols component.
     * 
     * @param   newCols new value for num-columns component
     * 
     * @throws IllegalStateException if the operation fails
     */
    public void setNumCols( int newCols )
    {
        setSpinnerInt( newCols, UIConstants.SDN_COLS );
    }
    
    /**
     * Populate the add-player text field.
     * 
     * @param   name    name to place in the text field
     * 
     * @throws IllegalStateException    if the operation fails
     */
    public void putPlayerText( String name )
    {
        JTextField  textField   = 
            getJTextField( UIConstants.SDN_PLAYERS_ADD_TEXT );
        ComponentUtils.invokeAndWait( () -> textField.setText( name ) );
    }
    
    /**
     * Activate the add-player text field listeners.
     * 
     * @throws IllegalStateException    if the operation fails
     */
    public void activatePlayerText()
    {
        JTextField  textField   = 
            getJTextField( UIConstants.SDN_PLAYERS_ADD_TEXT );
        ComponentUtils.invokeAndWait( () -> textField.postActionEvent() );
    }
    
    /**
     * Clicks the add-player button.
     * 
     * @throws IllegalStateException    if the operation fails
     */
    public void clickPlayerAdd()
    {
        JButton addButton   = getJButton( UIConstants.SDN_PLAYERS_ADD_BUTTON );
        ComponentUtils.invokeAndWait( () -> addButton.doClick() );
    }
    
    /**
     * Determine whether or not the test dialog is visible.
     * 
     * @return  true if the test dialog is visible
     */
    public boolean isVisible()
    {
        boolean[]   result  = new boolean[1];
        ComponentUtils.invokeAndWait( () -> result[0] = dialog.isVisible() );
        return result[0];
    }
    
    /**
     * Returns the result obtained when the dialog is dismissed.
     * Only valid if the dialog has been dismissed.
     * 
     * @return  
     *      true if the dialog was dismissed with the OK button,
     *      false otherwise
     */
    public boolean getCloseAction()
    {
        return closeAction;
    }
    
    /**
     * Get a list of error messages from the error-message component.
     * 
     * @return  a list of error messages
     */
    public List<String> getMessages()
    {
        List<String>    linesList   = getMessages( UIConstants.SDN_MESSAGES );
        return linesList;
    }
    
    /**
     * Gets a JFrame with the given title.
     * 
     * @param title the given title
     * 
     * @return  the JFrame with the given title, or null if not found
     * 
     * @throws IllegalStateException if JFrame cannot be found
     */
    public static JFrame getJFrame( String title )
    {
        JFrame[]            frameArr    = new JFrame[1]; 
        ComponentUtils.invokeAndWait( () -> 
            frameArr[0] = ComponentFinder.getFrame( title )
        );
        return frameArr[0];
    }
    
    /**
     * Gets a JDialog with the given title.
     * Returns null if not found.
     * 
     * @param title the given title
     * 
     * @return  the JDialog with the given title, or null if not found
     */
    public static JDialog getJDialog( String title )
    {
        JDialog[]   dialogArr    = new JDialog[1]; 
        ComponentUtils.invokeAndWait( () -> 
            dialogArr[0] = ComponentFinder.getDialog( title )
        );
        return dialogArr[0];
    }
    
    /**
     * Starts the GUI dialog
     * and waits for it to become visible.
     * 
     * @throws IllegalStateException    if the operation fails
     */
    private void startGUIThread()
    {
        waitForOpenEvent( dialog, () -> closeAction = gui.showDialog() );
    }
    
    /**
     * Run the new-type dialog 
     * to add a new ship type to the toRegister list,
     * and push OK.
     * The client provides a ShipType2D
     * which is used to supply arguments
     * to {@link #addShipTypeOK(String, int, int)};
     * it is not otherwise used.
     * A list of messages generated by the operation
     * is returned (should be empty).
     * 
     * @param type  provided ShipType2D
     * 
     * @return a list of messages generated by the operation
     * 
     * @throws IllegalStateException if the operation fails
     */
    public List<String> addShipType( ShipType2D type )
    {
        List<String>    messages    = 
            addShipTypeOK( type.typeName(), type.length(), type.breadth() );
        return messages;
    }

    /**
     * Run the new-type dialog 
     * to add a new ship type to the toRegister list,
     * and push OK.
     * If the OK button does not terminate the dialog,
     * an IllegalStateException is thrown.
     * A list of messages generated by the operation
     * is returned (should be empty).
     * 
     * @param name      the name of the new type
     * @param length    the length of the new type
     * @param breadth   the breadth of the new type
     * 
     * @return a list of messages generated by the operation
     * 
     * @throws IllegalStateException if the operation fails
     */
    public List<String> addShipTypeOK( String name, int length, int breadth )
    {
        final String expectedNotVisible = "Dialog visible when it should not be";
        startAddDialog();
        
        JDialog typeDialog  = typeAssist.getDialog();
        // Set the parameters in the new-type dialog
        typeAssist.setLength( length );
        typeAssist.setBreadth( breadth );
        typeAssist.setName( name );
        
        // push the new-type dialog OK button...
        // don't wait for the termination event this time, because
        // the ship-type dialog is already waiting
        typeAssist.clickOK();
        if ( typeDialog.isVisible() )
            throw new IllegalStateException( expectedNotVisible );
        List<String>    messages    = typeAssist.getMessages();
        return messages;
    }
    
    /**
     * Start an add-type dialog 
     * and dismiss it by pressing the cancel button.
     * 
     * @return  
     *      list of error messages associated with starting and canceling
     *      the add-type dialog.
     */
    public List<String>  addShipTypeCancel()
    {
        final String expectedNotVisible = "Dialog visible when it should not be";
        startAddDialog();
        
        JDialog typeDialog  = typeAssist.getDialog();
        // push the new-type dialog cancel button...
        // don't wait for the termination event this time, because
        // the ship-type dialog is already waiting
        typeAssist.clickCancel();
        if ( typeDialog.isVisible() )
            throw new IllegalStateException( expectedNotVisible );
        List<String>    messages    = typeAssist.getMessages();
        return messages;
    }
    
    /**
     * Starts the add-type dialog
     * and waits for it to become visible.
     * 
     * @throws IllegalStateException if the task fails
     */
    private void startAddDialog()
    {
        // assumption: the JDialog behind the add-new action is created
        // when the SetupAWT dialog is instantiated.
        final String notFound           = "Add-new dialog not found";
        final String expectedNotVisible = "Dialog visible when it should not be";
        final String expectedVisible    = "Dialog not visible when it should be";
        
        // Find the add-type dialog, verify it is in the correct state
        // (not visible).
        typeAssist  = new RegisterTypeTestAssistant();
        JDialog dialog  = typeAssist.getDialog();
        if ( dialog == null )
            throw new IllegalStateException( notFound );
        if ( dialog.isVisible() )
            throw new IllegalStateException( expectedNotVisible );
        
        // Click the add-new button, and wait for the new-type dialog
        // to become visible.
        JButton             addButton           = 
            getJButton( UIConstants.SDN_REG_ADD_NEW_BUTTON );
        waitForOpenEvent( dialog, addButton::doClick );
        if ( !dialog.isVisible() )
            throw new IllegalStateException( expectedVisible );
    }
    
    /**
     * Compiles a list of type ShipType2D
     * from a JList with the given name.
     * The corresponding JList must contain 
     * either ShipType2D or FriendlyType elements.
     * This method sets up the task
     * to be executed on the EDT,
     * then defers execution to {@link #getTypeList(JList)};
     * 
     * @param name  the given name
     * 
     * @return  the compiled list
     * 
     * @throws IllegalStateException 
     *     if the JList can't be found or is the wrong type
     *     
     * @see #getTypeList(JList)
     */
    private List<ShipType2D> getTypeList( String name )
    {
        List<List<ShipType2D>>  temp    = new ArrayList<>();
        JList<?>                jList   = getJList( name );
        ComponentUtils.invokeAndWait( () -> temp.add( getTypeList( jList ) ) );
        return temp.getFirst();
    }
    
    /**
     * Compile a list of ship types
     * from the corresponding JList.
     * This method complements 
     * {@link SetupAWTTestAssistant#getTypeList(String) }.
     * It must be invoked from the EDT.
     * 
     * @param jList the corresponding JList
     * 
     * @return  the compiled list
     */
    @SuppressWarnings("unchecked")
    private List<ShipType2D> getTypeList( JList<?> jList )
    {
        List<ShipType2D>    typeList    = null;
        ListModel<?>        model       = jList.getModel();
        List<?>             initList    =
            IntStream.range( 0, model.getSize() )
                .mapToObj( model::getElementAt )
                .toList();
        Object              item0       =
            initList.isEmpty() ? null : initList.getFirst();
        if ( item0 == null )
            typeList = Collections.emptyList();
        else if ( item0 instanceof ShipType2D )
            typeList = (List<ShipType2D>)initList;
        else if ( !(item0 instanceof FriendlyType) )
        {
            String  friendlyTypeName    = FriendlyType.class.getSimpleName(); 
            String  listName            = jList.getName();
            throwWrongType( listName, friendlyTypeName, item0.getClass() );
        }
        else
        {
            typeList = initList.stream()
                .map( FriendlyType.class::cast )
                .map( FriendlyType::type )
                .toList();
        }
        return typeList;
    }
}
