package com.acmemail.judah.anonymous_classes.functional_interfaces;

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
    
    public Message( String source, String destination, String content )
    {
        this.source = source;
        this.destination = destination;
        this.content = content;
    }

    /**
     * @return the encoding
     */
    public String getEncoding()
    {
        return encoding;
    }

    /**
     * @param encoding the encoding to set
     */
    public void setEncoding(String encoding)
    {
        this.encoding = encoding;
    }

    /**
     * @return the dateTimeUTC
     */
    public String getDateTimeUTC()
    {
        return dateTimeUTC;
    }

    /**
     * @param dateTimeUTC the dateTimeUTC to set
     */
    public void setDateTimeUTC(String dateTimeUTC)
    {
        this.dateTimeUTC = dateTimeUTC;
    }

    /**
     * @return the assembledBy
     */
    public String getAssembledBy()
    {
        return assembledBy;
    }

    /**
     * @param assembledBy the assembledBy to set
     */
    public void setAssembledBy(String assembledBy)
    {
        this.assembledBy = assembledBy;
    }

    /**
     * @return the source
     */
    public String getSource()
    {
        return source;
    }

    /**
     * @return the destination
     */
    public String getDestination()
    {
        return destination;
    }

    /**
     * @return the content
     */
    public String getContent()
    {
        return content;
    }

    /**
     * @return the checksumAlgo
     */
    public String getChecksumAlgo()
    {
        return checksumAlgo;
    }

    /**
     * @param checksumAlgo the checksumAlgo to set
     */
    public void setChecksumAlgo(String checksumAlgo)
    {
        this.checksumAlgo = checksumAlgo;
    }

    /**
     * @return the checksum
     */
    public String getChecksum()
    {
        return checksum;
    }

    /**
     * @param checksum the checksum to set
     */
    public void setChecksum(String checksum)
    {
        this.checksum = checksum;
    }
}
