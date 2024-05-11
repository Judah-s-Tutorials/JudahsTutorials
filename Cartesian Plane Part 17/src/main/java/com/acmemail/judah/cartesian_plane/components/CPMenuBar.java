package com.acmemail.judah.cartesian_plane.components;

import java.awt.Desktop;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.stream.Stream;

import javax.swing.AbstractButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JDialog;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;

import com.acmemail.judah.cartesian_plane.CPConstants;
import com.acmemail.judah.cartesian_plane.PropertyManager;
import com.acmemail.judah.cartesian_plane.input.Equation;
import com.acmemail.judah.cartesian_plane.input.Exp4jEquation;
import com.acmemail.judah.cartesian_plane.input.FileManager;

/**
 * The menu bar for the main window
 * of the Cartesian plane application.
 * 
 * @author Jack Straub
 */
@SuppressWarnings("serial")
public class CPMenuBar extends JMenuBar
{
    /** 
     * This declaration is just for convenience; it saves
     * having to write "PropertyManager.INSTANCE" over and over.
     */
    private static final PropertyManager pmgr           = 
        PropertyManager.INSTANCE;
    
    /** The owner of this dialog. */
    private final Window        topWindow;
    /** Dialog to configure line properties in the Cartesian plane. */
    private final JDialog       lineDialog;
    /** Dialog to configure graph properties in the Cartesian plane. */
    private final JDialog       graphDialog;
    /** Dialog to show "about application" page. */
    private final AboutDialog   aboutDialog;
    
    /** Currently open equation file; null if none. */
    private File    currFile    = null;
    
    /** 
     * Table for loading/save variable name/value pairs.
     * Must be set at or shortly after instantiation.
     * It is considered a design error if any menu item is activated
     * prior to setting this value.
     */
    private final CPFrame       cpFrame;
    
    /**
     * Constructor.
     * Fully configures the application menu bar.
     * 
     * @param topWindow 
     *      top-level window to use as parent of dialogs
     *      launched by this menu bar; may be null
     */
    public CPMenuBar( Window topWindow )
    {
        this.topWindow = topWindow;
        lineDialog =
            LinePropertiesPanel.getDialog( topWindow );
        graphDialog =
            GraphPropertiesPanel.getDialog( topWindow );
        aboutDialog = new AboutDialog( topWindow );
        
        add( getFileMenu() );
        add( getWindowMenu() );
        add( configHelpMenu() );
        
        if ( topWindow instanceof CPFrame )
            cpFrame = (CPFrame)topWindow;
        else
            cpFrame = null;
    }
    
    /**
     * Assembles the application's file menu.
     * 
     * @return the application's file menu
     */
    private JMenu getFileMenu()
    {
        JMenu   menu    = new JMenu( "File" );
        menu.setMnemonic( KeyEvent.VK_F );
        
        JMenuItem   newI    = new JMenuItem( "New", KeyEvent.VK_N );
        JMenuItem   open    = new JMenuItem( "Open", KeyEvent.VK_O );
        JMenuItem   save    = new JMenuItem( "Save", KeyEvent.VK_S );
        JMenuItem   saveAs  = new JMenuItem( "Save As", KeyEvent.VK_A );
        JMenuItem   close   = new JMenuItem( "Close", KeyEvent.VK_C );
        JMenuItem   delete  = new JMenuItem( "Delete", KeyEvent.VK_D );
        JMenuItem   exit    = new JMenuItem( "Exit", KeyEvent.VK_X );
        
        KeyStroke   ctrlO       =
            KeyStroke.getKeyStroke( KeyEvent.VK_O, ActionEvent.CTRL_MASK );
        open.setAccelerator( ctrlO );
        
        KeyStroke   ctrlN       =
            KeyStroke.getKeyStroke( KeyEvent.VK_N, ActionEvent.CTRL_MASK );
        newI.setAccelerator( ctrlN );
        
        KeyStroke   ctrlS       =
            KeyStroke.getKeyStroke( KeyEvent.VK_S, ActionEvent.CTRL_MASK );
        save.setAccelerator( ctrlS );
        
        newI.addActionListener( this::newAction );
        open.addActionListener( this::openAction );
        save.addActionListener( this::saveAction );
        saveAs.addActionListener( this::saveAsAction );
        close.addActionListener( this::closeAction );
        delete.addActionListener( this::deleteAction );
        exit.addActionListener( e -> System.exit( 0 ) );
        
        menu.add( newI );
        menu.add( open );
        menu.add( save );
        menu.add( saveAs );
        menu.add( close );
        menu.add( delete );
        menu.add( exit );
        
        save.setEnabled( false );
        pmgr.addPropertyChangeListener(
            CPConstants.DM_MODIFIED_PN, e -> configureSave( save ) );
        pmgr.addPropertyChangeListener(
            CPConstants.DM_OPEN_FILE_PN, e -> configureSave( save ) );
        
        saveAs.setEnabled( false );
        pmgr.addPropertyChangeListener(
            CPConstants.DM_OPEN_EQUATION_PN, e -> {
                boolean hasEquation = 
                    pmgr.asBoolean( CPConstants.DM_OPEN_EQUATION_PN );
                saveAs.setEnabled( hasEquation );
        });

        close.setEnabled( false );
        pmgr.addPropertyChangeListener(
            CPConstants.DM_OPEN_EQUATION_PN, e -> {
                boolean isOpen  = 
                    pmgr.asBoolean( CPConstants.DM_OPEN_EQUATION_PN );
                close.setEnabled( isOpen );
        });
        
        delete.setEnabled( false );
        pmgr.addPropertyChangeListener(
            CPConstants.DM_OPEN_FILE_PN, e -> {
                boolean isOpen  = 
                    pmgr.asBoolean( CPConstants.DM_OPEN_FILE_PN );
                delete.setEnabled( isOpen );
        });
        
        return menu;
    }
    
    /**
     * Disable the save button unless a) there is a file open,
     * and b) data has been modified.
     */
    private void configureSave( JMenuItem save )
    {
        boolean isModified  = pmgr.asBoolean( CPConstants.DM_MODIFIED_PN );
        boolean isOpen      = pmgr.asBoolean( CPConstants.DM_OPEN_FILE_PN );
        save.setEnabled( isModified && isOpen );
    }
    
    /**
     * Assembles the application's window menu.
     * 
     * @return the application's window menu
     */
    private JMenu getWindowMenu()
    {
        JMenu   menu    = new JMenu( "Window" );
        menu.setMnemonic( KeyEvent.VK_W );
        
        JCheckBoxMenuItem   graphItem   =
            new JCheckBoxMenuItem( "Edit Graph Properties", false );
        JCheckBoxMenuItem   lineItem    =
            new JCheckBoxMenuItem( "Edit Line Properties", false );
        graphItem.addItemListener( e -> 
            graphDialog.setVisible( graphItem.isSelected() )
        );
        lineItem.addItemListener( e -> 
            lineDialog.setVisible( lineItem.isSelected() )
        );
        lineDialog.addComponentListener( new SynchVisible( lineItem ) );
        graphDialog.addComponentListener( new SynchVisible( graphItem ) );
        menu.add( graphItem );
        menu.add( lineItem );
        
        return menu;
    }
    
    /**
     * Assembles the application's help menu.
     * 
     * @return the application's help menu
     */
    private JMenu configHelpMenu()
    {
        JMenu       topicsMenu  = getMathTopicsMenu();
        JMenu       calcsMenu   = getCalculatorsMenu();
        JMenuItem   aboutItem   = new JMenuItem( "About", KeyEvent.VK_A );
        aboutItem.addActionListener( e -> aboutDialog.showDialog( true ) );
        
        JMenu       helpMenu        = new JMenu( "Help" );
        helpMenu.setMnemonic( KeyEvent.VK_H );
        helpMenu.add( topicsMenu );
        helpMenu.add( calcsMenu );
        helpMenu.add( aboutItem );
        return helpMenu;
    }
    
    /**
     * Configures the "help topics" submenu.
     * 
     * @return  the "math topics" submenu.
     */
    private JMenu getMathTopicsMenu()
    {
        URLDesc[]    siteDescs   =
        {
            new URLDesc( "https://www.mathsisfun.com/", "Math is Fun" ),
            new URLDesc( "https://www.wolframalpha.com/", "Wolfram Alpha" ),
            new URLDesc( "https://www.khanacademy.org/", "Khan Academy" )
        };
        JMenu   menu    = new JMenu( "Math Help" );
        menu.setMnemonic( KeyEvent.VK_M );
        Stream.of( siteDescs )
            .forEach( u -> {
                JMenuItem   item    = new JMenuItem( u.urlDesc );
                item.addActionListener( e -> activateLink( u.url ) );
                menu.add( item );
            });
        
        return menu;
    }
    
    /**
     * Configures the "calculators" submenu.
     * 
     * @return  the "calculators" submenu.
     */
    private JMenu getCalculatorsMenu()
    {
        URLDesc[]    siteDescs   =
        {
            new URLDesc( "https://web2.0calc.com/", "web2.0calc" ),
            new URLDesc( "https://www.desmos.com/", "Desmos" ),
            new URLDesc( "https://www.calculator.net/", "Calculator.net" )
        };
        JMenu   menu    = new JMenu( "Calculators" );
        menu.setMnemonic( KeyEvent.VK_C );
        Stream.of( siteDescs )
            .forEach( u -> {
                JMenuItem   item    = new JMenuItem( u.urlDesc );
                item.addActionListener( e -> activateLink( u.url ) );
                menu.add( item );
            });
        
        return menu;
    }

    /**
     * Opens a URL in the host's default browser.
     * If an error occurs
     * the error message
     * is displayed in a modal dialog;
     * the operation is otherwise ignored.
     * 
     * @param url   the URL to open
     */
    private void activateLink( URL url )
    {
        Desktop desktop = Desktop.getDesktop();
        try
        {
            desktop.browse( url.toURI() );
        } 
        catch ( IOException | URISyntaxException exc )
        {
            exc.printStackTrace();
            JOptionPane.showMessageDialog(
                topWindow, 
                exc.getMessage(),
                "Link Error",
                JOptionPane.ERROR_MESSAGE,
                null
            );
        }
    }
    
    /**
     * Processes the file/new button,
     * which constructs a new equation.
     * If previous work has not been saved
     * the operator has the choice 
     * to cancel the operation.
     * 
     * @param evt   object accompanying action event notification
     */
    private void newAction( ActionEvent evt )
    {
        if ( cpFrame != null )
        {
            Equation    equation    = new Exp4jEquation();
            loadEquation( equation );
            setCurrFile( null );
        }
    }
    
    /**
     * Processes the file/open button,
     * which allows the operator
     * to open an existing equation file.
     * If previous work has not been saved
     * the operator has the choice 
     * to cancel the operation.
     * 
     * @param evt   object accompanying action event notification
     */
    private void openAction( ActionEvent evt )
    {
        if ( cpFrame != null )
        {
            Equation    equation    = FileManager.open();
            if ( equation != null )
            {
                setCurrFile( FileManager.getLastFile() );
                loadEquation( equation );
            }
        }
    }
    
    /**
     * Processes the file/save button,
     * saving the current equation'
     * to the currently open file.
     * 
     * @param evt   object accompanying action event notification
     */
    private void saveAction( ActionEvent evt )
    {
        if ( cpFrame != null && currFile != null )
        {
            Equation    equation    = cpFrame.getEquation();
            FileManager.save( currFile, equation );
            if ( FileManager.getLastResult() )
            {
                setCurrFile( currFile );
                pmgr.setProperty( CPConstants.DM_MODIFIED_PN, false );
            }
        }
    }
    
    /**
     * Processes the file/saveAs button,
     * saving the current equation
     * to a destination file chosen
     * by the operator.
     * 
     * @param evt   object accompanying action event notification
     */
    private void saveAsAction( ActionEvent evt )
    {
        if ( cpFrame != null  )
        {
            Equation    equation    = cpFrame.getEquation();
            FileManager.save( equation );
            if ( FileManager.getLastResult() )
            {
                setCurrFile( FileManager.getLastFile() );
                pmgr.setProperty( CPConstants.DM_MODIFIED_PN, false );
            }
        }
    }
    
    /**
     * Processes the file/close button,
     * closing the current equation.
     * 
     * @param evt   object accompanying action event notification
     */
    private void closeAction( ActionEvent evt )
    {
        if ( cpFrame != null  )
        {
            cpFrame.loadEquation(null);
            setCurrFile( null );
            pmgr.setProperty( CPConstants.DM_MODIFIED_PN, false );
            pmgr.setProperty( CPConstants.DM_OPEN_EQUATION_PN, false );
        }
    }
    
    /**
     * Processes the file/saveAs button,
     * saving the current equation
     * to a destination file chosen
     * by the operator.
     * 
     * @param evt   object accompanying action event notification
     */
    private void deleteAction( ActionEvent evt )
    {
        if ( currFile != null )
        {
            currFile.delete();
            pmgr.setProperty( CPConstants.DM_MODIFIED_PN, false );
            pmgr.setProperty( CPConstants.DM_OPEN_EQUATION_PN, false );
            loadEquation( null );
            setCurrFile( null );
        }
    }
    
    /**
     * Sets the value of currFile, 
     * and updates DM_OPEN_FILE_PN.
     * If currFile is null,
     * sets DM_OPEN_FILE_PN to false,
     * else sets it to true.
     * 
     * @param file  the file to update currFile; may be null
     */
    private void setCurrFile( File file )
    {
        currFile = file;
        boolean hasFile = file != null;
        pmgr.setProperty( CPConstants.DM_OPEN_FILE_PN, hasFile );
    }
    
    /**
     * Sets the value of equation, 
     * and updates DM_OPEN_EQUATION_PN.
     * If equation is null,
     * sets DM_OPEN_EQUATION_PN to false,
     * else sets it to true.
     * 
     * @param file  the file to update currFile; may be null
     */
    private void loadEquation( Equation equation )
    {
        if ( cpFrame != null )
        {
            boolean hasData = equation != null;
            cpFrame.loadEquation( equation );
            pmgr.setProperty( CPConstants.DM_OPEN_EQUATION_PN, hasData );
            pmgr.setProperty( CPConstants.DM_MODIFIED_PN, false );
        }
    }
    
    /**
     * ComponentListener for synchronizing the state of a toggle button
     * with the visibility of a window.
     * The window is typically a dialog.
     * For example,
     * if the line properties dialog loses visibility,
     * the associated menu checkbox item
     * will be set to false.
     * 
     * @author Jack Straub
     */
    private static class SynchVisible extends ComponentAdapter
    {
        /** The button associated with this object. */
        private final AbstractButton    toggle;
        
        /**
         * Constructor.
         * Establishes the button whose state
         * is to be synchronized
         * with the visibility of some window.
         * 
         * @param toggle
         *      the button whose state is to be synchronized
         *      with the visibility of some window
         */
        public SynchVisible( AbstractButton toggle )
        {
            this.toggle = toggle;
        }
        
        /**
         * Method to be invoked when a window loses visibility.
         */
        @Override
        public void componentHidden( ComponentEvent evt )
        {
            toggle.setSelected( false );
        }
        
        /**
         * Method to be invoked when a window becomes visible.
         */
        @Override
        public void componentShown( ComponentEvent evt )
        {
            toggle.setSelected( true );
        }
    }
    
    /**
     * Establishes a correspondence
     * between a description of a URL,
     * and an object of type <em>class URL.</em>
     * The description is typically used
     * as the text of a menu item,
     * and the object is used to activate the URL
     * when the menu item is selected.
     * 
     * @author Jack Straub
     */
    private static class URLDesc
    {
        /** String representation of the encapsulated URL. */
        public final String urlDesc;
        /** Object representation of the encapsulated URL. */
        public final URL    url;
        
        /**
         * Constructor.
         * Establishes the description of the encapsulated URL
         * and the associated URL object.
         * 
         * @param urlStr    the URL object, formatted as a string
         * @param desc      the description of the URL
         */
        public URLDesc( String urlStr, String desc )
        {
            URL temp    = null;
            try
            {
                temp = new URL( urlStr );
            }
            catch ( MalformedURLException exc )
            {
                exc.printStackTrace();
                System.exit( 1 );
            }
            url = temp;
            urlDesc = desc;
        }
    }
}
