package com.acmemail.judah.cartesian_plane.test_utils;

import java.awt.Component;

/**
 * Encapsulates a message propagated
 * by a MessageConsumer facility.
 * 
 * @param parent        the parent propagated by the MessageConsumer
 * @param message       the message propagated by the MessageConsumer
 * @param title         the title propagated by the MessageConsumer
 * @param messageType   the messageType propagated by the MessageConsumer
 * 
 * @see MessageArchive
 * @see com.acmemail.judah.cartesian_plane.input.MessageConsumer
 */
public record MessageRec(
    Component parent,
    String message, 
    String title, 
    int messageType
)
{
}
