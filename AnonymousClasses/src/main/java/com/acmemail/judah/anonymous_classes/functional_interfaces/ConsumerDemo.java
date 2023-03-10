package com.acmemail.judah.anonymous_classes.functional_interfaces;

/**
 * Simple application to exercise
 * the {@linkplain LocalDispatchService}
 * which demonstrates the use
 * of the <em>Consumer&lt;T&gt;</em> functional interface.
 * 
 * @author Jack Straub
 * 
 * @see LocalDispatchService
 *
 */
public class ConsumerDemo
{
    /**
     * Application entry point.
     * 
     * @param args  command line arguments; not used
     */
    public static void main( String[] args )
    {
        String  src = "accounting@judah.com";
        String  dst = "able.jones@dev.judah.com";
        String  txt = 
            "Re:Expense Report//Please submit your latest expense report.";
        Message message = new Message( src, dst, txt );
        LocalDispatchService.dispatch( 
            message, 
            LocalDispatchService::secureMailer
        );
    }    
}
