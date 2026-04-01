package com.acmemail.judah.battleship.util;

import java.awt.Component;

import javax.swing.Icon;

/**
 * Interface extracted from the <em>JOptionPane</em> class.
 * The purpose of this interface 
 * is to facilitate object mocking during testing.
 * To allow mocking during testing,
 * production classes should never refer directly to JOptionPane;
 * instead, they utilize a class that implements this interface.
 * During testing,
 * they must provide a means for the test code
 * to substitute a test version of the concrete class.
 * for example:
 * <pre style="padding-left: 2em;">private static void JOptionPaneI optionPane  = 
 *     new JOPtionPaneS();
 * public static void 
 * setJOptionPaneInterface( JOptionPaneI jOptionPane )
 * {
 *     optionPane = jOptionPane;
 * }</pre>
 * <p>
 * Note: 
 * At the time of initial implementation,
 * not all public methods of JOptionPane
 * were extracted to this interface;
 * additional methods from JOptionPane
 * should be extracted as needed.
 * 
 * @see JOptionPaneS
 * @see <a href="https://site.mockito.org/">The Mockito Website</a>
 */
public interface JOptionPaneI
{
    Object showInputDialog(
        Component parentComponent, 
        Object message, 
        String title, 
        int messageType, 
        Icon icon,
        Object[] selectionValues, 
        Object initialSelectionValue
    );

    String showInputDialog(Component parent, Object message);

    void showMessageDialog(
        Component parent,
        Object message,
        String title,
        int type
    );

}