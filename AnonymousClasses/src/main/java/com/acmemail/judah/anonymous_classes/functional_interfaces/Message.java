package com.acmemail.judah.anonymous_classes.functional_interfaces;

/**
 * The class is used
 * as part of a demonstration
 * of how to use a <em>Consumer</em> functional interface.
 * It encapsulates a message
 * to be sent to any of 
 * a variety of destinations.
 * 
 * @author Jack Straub
 */
public class Message
{
    private final String    source;
    private final String    destination;
    private final String    content;
    private String          encoding;
    private String          dateTimeUTC;
    private String          assembledBy;
    private String          checksumAlgo;
    private String          checksum;
    
    /**
     * Constructor.
     * Establishes the source,
     * destination and content
     * of a message to be transmitted.
     * 
     * @param source        the source of the message
     * @param destination   the destination of the message
     * @param content       the content of the message
     */
    public Message( String source, String destination, String content )
    {
        this.source = source;
        this.destination = destination;
        this.content = content;
    }

    /**
     * Gets the format
     * in which this message was encoded,
     * e.g. "utf-16".
     * 
     * @return the format in which this message was encoded
     */
    public String getEncoding()
    {
        return encoding;
    }

    /**
     * Sets the format
     * in which this message was encoded,
     * e.g. "utf-16".
     * 
     * @param encoding the message encoding
     */
    public void setEncoding(String encoding)
    {
        this.encoding = encoding;
    }

    /**
     * Gets the time/date stamp
     * of this message in UTC.
     * 
     * @return the time/date stamp of this message in UTC
     */
    public String getDateTimeUTC()
    {
        return dateTimeUTC;
    }

    /**
     * Gets the time/date stamp
     * of this message in UTC.
     * 
     * @param dateTimeUTC the date/time stamp for this message
     */
    public void setDateTimeUTC(String dateTimeUTC)
    {
        this.dateTimeUTC = dateTimeUTC;
    }

    /**
     * Gets the name of the facility
     * that assembled/formatted this message.
     * 
     * @return name of the facility that assembled/formatted this message
     */
    public String getAssembledBy()
    {
        return assembledBy;
    }

    /**
     * Sets the name of the facility
     * that assembled/formatted this message.
     *
     * @param assembledBy the name of the assembling facility
     */
    public void setAssembledBy(String assembledBy)
    {
        this.assembledBy = assembledBy;
    }

    /**
     * Gets the source of the message.
     * 
     * @return the source of this message
     */
    public String getSource()
    {
        return source;
    }

    /**
     * Gets the destination of this message.
     * 
     * @return the destination of this message
     */
    public String getDestination()
    {
        return destination;
    }

    /**
     * Gets the content of this message.
     * 
     * @return the content of this message
     */
    public String getContent()
    {
        return content;
    }

    /**
     * Gets the name of the algorithm
     * used to generate the checksum
     * for this message. 
     * 
     * @return 
     *      the name of the algorithm used to generate the checksum
     *      for this message
     */
    public String getChecksumAlgo()
    {
        return checksumAlgo;
    }

    /**
     * Sets the name of the algorithm
     * used to generate the checksum
     * for this message. 
     * 
     * @param checksumAlgo the name of the checksum algorithm
     */
    public void setChecksumAlgo(String checksumAlgo)
    {
        this.checksumAlgo = checksumAlgo;
    }

    /**
     * Gets the checksum for this message.
     * 
     * @return the checksum for this message
     */
    public String getChecksum()
    {
        return checksum;
    }

    /**
     * Sets the checksum for this message.
     * 
     * @param checksum the checksum to set
     */
    public void setChecksum(String checksum)
    {
        this.checksum = checksum;
    }
}
