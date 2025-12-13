package com.gmail.johnstraub1954.weather;

import javax.swing.JOptionPane;

public class Input
{
    private static final String lineSep     = System.lineSeparator();
    private static final String askLocation = 
        "Enter coordinates or address, for example:" + lineSep
        + "47, -121" + lineSep
        + "Paris" + lineSep
        + "Paris, France" + lineSep
        + "Sayville, NY" + lineSep
        + "1600 Pennsylvania Avenue NW, Washington, DC 20500";
    
    public static String askLocation()
    {
        String  result  =   JOptionPane.showInputDialog( null, askLocation );
        if ( result != null && result.isEmpty() )
            result = "sayville";
        return result;
    }
}
