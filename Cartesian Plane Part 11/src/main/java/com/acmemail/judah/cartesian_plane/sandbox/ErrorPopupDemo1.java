package com.acmemail.judah.cartesian_plane.sandbox;

import java.util.List;

import com.acmemail.judah.cartesian_plane.input.Result;

/**
 * Demonstrates how to display a dialog
 * containing an error message.
 * 
 * @see Utils#showResultPopup(Result)
 * @see Utils#showUsageDialog()
 * 
 * @author Jack Straub
 */
public class ErrorPopupDemo1
{
    /**
     * Application entry point.
     * 
     * @param args  command line arguments; not used
     */
    public static void main(String[] args)
    {
        List<String>    messages    =
            List.of( "invalid this", "invalid that", "invalid other" );
        Result  result  = new Result( false, messages );
        Utils.showResultPopup (result );
    }
}
