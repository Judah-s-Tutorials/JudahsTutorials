package com.acmemail.judah.cartesian_plane.sandbox;

import java.awt.Component;

import javax.swing.JOptionPane;

/**
 * Demonstrates how, during testing,
 * we might be able
 * to change the way error messages 
 * are delivered.
 * Normal message delivery is via a modal dialog,
 * alternative delivery is via a functional interface
 * substituted by a user.
 */
public class MessageConsumerDemo
{
    /**
     * Functional interface which defines a message consumer.
     * It is designed to be compatible with 
     * JOptionPane.showMessageDialog.
     */
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
    
    /** Default MessageConsumer implementation. */
    private static final MessageConsumer defaultMessageConsumer = 
        JOptionPane::showMessageDialog;
            
    /** The current message consumer. */
    private static volatile MessageConsumer messageConsumer = 
        defaultMessageConsumer;
        
    /**
     * Gets the current MessageConsumer.
     * 
     * @return  the current MessageConsumer
     */
    public static MessageConsumer getMessageConsumer()
    {
        return messageConsumer;
    }
    
    /**
     * Sets the current MessageConsumer.
     * The caller may pass null,
     * in which case a default is applied.
     * 
     * @param consumer  the consumer to set, or null to restore default
     */
    public static void setMessageConsumer( MessageConsumer consumer )
    {
        if ( consumer != null )
            messageConsumer = consumer;
        else
            messageConsumer = defaultMessageConsumer;
    }
    
    /**
     * Posts a message, using the current MessageConsumer,
     * which may be substituted via 
     * {@linkplain #setMessageConsumer(MessageConsumer) }.
     * The default strategy is to post a JOptionPane dialog.
     * 
     * @param title     title to pass to the MessageConsumer
     * @param message   message to pass to the MessageConsumer
     * 
     * @see MessageConsumer
     * @see #getMessageConsumer()
     * @see #setMessageConsumer(MessageConsumer)
     */
    public static void showErrorMessage( String title, String message )
    {
        messageConsumer.postMessage( 
            null, 
            message, 
            title,
            JOptionPane.ERROR_MESSAGE
        );
    }
    
    /**
     * Application entry point.
     * 
     * @param args  command-line arguments, not used
     */
    public static void main( String[] args )
    {
        MessageConsumerDemo.showErrorMessage( "title", "this is a message" );
        
        MessageConsumerDemo.setMessageConsumer( (p0, message, p2, p3 ) -> 
            System.out.println( "Delivered: " + message )
        );
        MessageConsumerDemo.showErrorMessage( "title", "this is a message too" );
        
        // Explicit exit, just in case the EDT is preventing
        // termination.
        System.exit( 0 );
    }
}
