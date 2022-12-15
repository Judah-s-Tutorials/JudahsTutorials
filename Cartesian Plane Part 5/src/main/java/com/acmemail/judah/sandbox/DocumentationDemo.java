package com.acmemail.judah.sandbox;

/**
 * This class serves to validate the syntax
 * of Javadoc statements
 * cited in a lecture on documentation.
 * 
 * @author Jack Straub
 *
 */
public class DocumentationDemo
{    
    /**
     * This is another demo method.
     * When invoking this method
     * you <em>must</em> specify criteria 
     * in the following order:
     * <ol>
     * <li>The color you're looking for.</li>
     * <li>The text you're looking for.</li>
     * <li>The style of the text.</li>
     * <li>Exceptions to the rules.</li>
     * </ol>
     */
    public void embeddedHTMLDemo()
    {
    }
    
    /**
     * Demonstrates how you have to be careful
     * using characters that might be interpreted
     * as HTML:
     * 
     * Parameter val must be in the range 0 &lt;= val &lt; limit.
     * 
     * @param   limit   ...
     * @param   val     ...
     */
    public void caveatDemo1( int limit, int val )
    {
    }
    
    /**
     * Demonstrates how to use the @code tag
     * to avoid problems 
     * using characters that might be interpreted
     * as HTML:
     * 
     * {@code Parameter val must be in the range 0 <= val < limit.}
     * 
     * @param   limit   ...
     * @param   val     ...
     */
    public void atCodeDemo1( int limit, int val )
    {
    }
}
