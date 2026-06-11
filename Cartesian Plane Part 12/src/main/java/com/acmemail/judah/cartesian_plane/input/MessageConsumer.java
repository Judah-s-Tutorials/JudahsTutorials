package com.acmemail.judah.cartesian_plane.input;

import java.awt.Component;

/**
 * Functional interface which defines a message consumer.
 * It is designed to be compatible with
 * {@link javax.swing.JOptionPane#showMessageDialog(Component, Object, String, int)}.
 *
 * @author Jack Straub
 */
@FunctionalInterface
public interface MessageConsumer
{
    /**
     * Consumes a message.
     * The explicit details are implementation specific.
     * 
     * @param parent        parent Component, when used with a GUI
     * @param message       the message to consume
     * @param title         the title, when used with a GUI
     * @param messageType   
     *      the type of the message; 
     *      when used with a GUI,
     *      controls the appearance of the user interface
     */
    void postMessage(
        Component parent,
        String  message,
        String title,
        int messageType
    );
}
