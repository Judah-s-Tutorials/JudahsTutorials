package com.acmemail.judah.cartesian_plane.sandbox;

import java.awt.Component;
import java.awt.Container;
import java.awt.Window;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JViewport;
import javax.swing.ListModel;

import com.acmemail.judah.cartesian_plane.input.ItemSelectionDialog;

/**
 * Program to demonstrate
 * how a unit test
 * for the ItemSelectionDialog class
 * might be developed.
 * 
 * @author Jack Straub
 */
public class ItemSelectionDialogTestDemo2
{
    private static JComponent       contentPane;
    private static JScrollPane      scrollPane;
    private static JList<?>         jList;
    private static ListModel<?>     listModel;
    private static List<?>          listItems;
    private static JPanel           buttonPanel;
    private static JButton          okButton;
    private static JButton          cancelButton;
    
    private static final String dialogTitle     = "Dialog Test Demo";
    private static int          dialogStatus    = Integer.MAX_VALUE;
    private static String       dialogItem      = null;
    
    /**
     * Application entry point.
     * 
     * @param args  command line arguments; not used.
     * 
     * @throws InterruptedException
     *      if the Thread class
     *      throws an InterruptedException
     */
    public static void main(String[] args)
        throws InterruptedException
    {
        Thread  showThread  = new Thread( () -> showDialog() );
        showThread.start();
        Thread.sleep( 500 );
        
        getContentPane();
        getScrollPane();
        getList();
        getListItems();
        
        getButtonPanel();
        getOKButton();
        getCancelButton();
        
        int selectedIndex   = 2;
        jList.setSelectedIndex( selectedIndex );
        okButton.doClick();
        showThread.join();

        Object  expItem     = listItems.get( selectedIndex );
        System.out.println( "Expected status: " + selectedIndex );
        System.out.println( "Actual status: " + dialogStatus );
        System.out.println( "Expected item: " + expItem );
        System.out.println( "Actual item: " + dialogItem );
        System.exit( 0 );
    }
    
    /**
     * Finds the first visible JDialog
     * that encapsulates the ItemSelectionDialog
     * and obtains its content pane.
     */
    private static void getContentPane()
    {
        JDialog dialog  = Arrays.stream( Window.getWindows() )
            .filter( w -> w.isVisible() )
            .filter( w -> w instanceof JDialog )
            .map( w -> (JDialog)w  )
            .findFirst()
            .orElse( null );
        
        if ( dialog == null )
            throw new ComponentException( "No dialogs found" );
        if ( !dialogTitle.equals( dialog.getTitle()  ) )
            throw new ComponentException( dialogTitle + ": dialog not found" );
        Container   pane    = dialog.getContentPane();
        
        if ( !(pane instanceof JComponent) )
        {
            String  message = 
                "Content pane invalid type: "
                + pane.getClass().getName();
            throw new ComponentException( message );
        }
        contentPane = (JComponent)pane;
    }
    
    /**
     * Locate the component,
     * nested within a given parent,
     * that satisfies a given predicate.
     * 
     * @param parent    the given parent
     * @param pred      the given predicate
     * 
     * @return  
     *      the first child of <em>parent</em>
     *      that satisfies <em>pred</em>
     */
    private static JComponent 
    getComponent( JComponent parent, Predicate<Component> pred )
    {
        JComponent  comp    = Arrays.stream( parent.getComponents() )
            .filter( w -> w instanceof JComponent )
            .map( w -> (JComponent)w )
            .filter( pred::test )
            .findFirst()
            .orElse( null );
        return comp;
    }
    
    /**
     * Finds the JScrollpane child
     * of the dialog's content pane.
     */
    private static void getScrollPane()
    {
        scrollPane = (JScrollPane)
            getComponent( contentPane, c -> (c instanceof JScrollPane) );
        if ( scrollPane == null )
            throw new ComponentException( "JScrollPane not found" );
    }
    
    /**
     * Gets the JList and ListModel
     * controlled by the JScrollpane.
     */
    private static void getList()
    {
        JViewport       viewport    = scrollPane.getViewport();
        Component       view        = viewport.getView();
        if ( !(view instanceof JList) )
            throw new ComponentException( "JList not found" );
        jList = (JList<?>)viewport.getView();
        listModel = jList.getModel();
    }
    
    /**
     * Gets a list of the items
     * encapsulated in the ItemSelectionDialog.
     */
    private static void getListItems()
    {
        int size    = listModel.getSize();
        listItems = IntStream.range( 0, size )
            .mapToObj( listModel::getElementAt )
            .collect( Collectors.toList() );
    }
    
    /**
     * Finds the JPanel child
     * of the dialog's content pane.
     */
    private static void getButtonPanel()
    {
        buttonPanel = (JPanel)
            getComponent( contentPane, c -> (c instanceof JPanel) );
        if ( buttonPanel == null )
            throw new ComponentException( "ButtonPanel not found" );
    }
    
    /**
     * Gets the OK button from the dialog's button panel.
     */
    private static void getOKButton()
    {
        Predicate<Component>    pred    = c ->
            (c instanceof JButton) && ((JButton)c).getText().equals( "OK" );
        okButton = (JButton)getComponent( buttonPanel, pred );
        if ( okButton == null )
            throw new ComponentException( "OK button not found" );
    }
    
    /**
     * Gets the Cancel button from the dialog's button panel.
     */
    private static void getCancelButton()
    {
        Predicate<Component>    pred    = c ->
            (c instanceof JButton) && 
            ((JButton)c).getText().equals( "Cancel" );
        cancelButton = (JButton)getComponent( buttonPanel, pred );
        if ( cancelButton == null )
            throw new ComponentException( "Cancel button not found" );
    }

    /**
     * Display an ItemSelectionDialog.
     * Record the exit data
     * after it closes.
     */
    private static void showDialog()
    {
        String[]    items   =
        { "Barbara", "Mike", "Natasha", "Boris", "George", "Martha" };
        ItemSelectionDialog dialog  = 
            new ItemSelectionDialog( dialogTitle, items );
        dialogStatus = dialog.show();
        if ( dialogStatus >= 0 )
            dialogItem = items[dialogStatus];
    }
}
