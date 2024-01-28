package com.acmemail.judah.cartesian_plane.test_utils;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.Window;
import java.util.function.Predicate;

import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import com.acmemail.judah.cartesian_plane.components.CPMenuBar;
import com.acmemail.judah.cartesian_plane.graphics_utils.ComponentFinder;
import com.acmemail.judah.cartesian_plane.graphics_utils.GUIUtils;

public class CPMenuBarTestDialog
{
    /** Singleton for this class. */
    private static CPMenuBarTestDialog testDialog  = null;
    
    /** The encapsulated dialog. */
    private final JDialog       dialog;
    /** The encapsulated menu bar. */
    private final CPMenuBar     menuBar;
    /** The encapsulated File menu. */
    private final JMenu         fileMenu;
    /** The encapsulated Window menu. */
    private final JMenu         windowMenu;
    /** The encapsulated Help menu. */
    private final JMenu         helpMenu;
    
    private CPMenuBarTestDialog()
    {
        menuBar = new CPMenuBar( null );
        dialog = makeDialog();
        fileMenu = getMenu( "File" );
        windowMenu = getMenu( "Window" );
        helpMenu = getMenu( "Help" );
    }
    
    public static CPMenuBarTestDialog getTestDialog()
    {
        if ( testDialog == null )
        {
            GUIUtils.schedEDTAndWait( 
                () -> testDialog = new CPMenuBarTestDialog()
            );
        }
        return testDialog;
    }
    
    private JDialog makeDialog()
    {
        String  title       = "Menu Bar Test Dialog";
        JDialog dialog      = new JDialog( (Window)null, title );
        JPanel  contentPane = new JPanel( new BorderLayout() );
        
        String  placeHolder = "CPMenuBar Test Dialog";
        JLabel  centerLabel = new JLabel( placeHolder );
        Font    origFont    = centerLabel.getFont();
        float   origSize    = origFont.getSize();
        float   newSize     = 3 * origSize;
        Font    newFont     = origFont.deriveFont( newSize );
        centerLabel.setFont( newFont );
        contentPane.add( centerLabel, BorderLayout.CENTER );
        contentPane.add( menuBar, BorderLayout.NORTH );
        
        dialog.setContentPane( contentPane );
        dialog.setLocation( 200, 200 );
        dialog.pack();
        dialog.setVisible( true );
        
        return dialog;
    }
    
    private JMenu getMenu( String text )
    {
        Predicate<JComponent>   isMenu  = c -> (c instanceof JMenu);
        Predicate<JComponent>   hasText = 
            c -> text.equals( ((JMenu)c).getText() );
        Predicate<JComponent>   pred    = isMenu.and( hasText );
        JComponent              comp    =
            ComponentFinder.find( dialog, pred );
        assertNotNull( comp );
        assertTrue( comp instanceof JMenu );
        return (JMenu)comp;
    }
}
