package com.acmemail.judah.anonymous_classes.functional_interfaces;

public class ConsumerDemo
{
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
