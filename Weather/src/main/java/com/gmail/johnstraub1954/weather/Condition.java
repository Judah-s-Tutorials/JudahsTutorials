package com.gmail.johnstraub1954.weather;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import javax.swing.Icon;
import javax.swing.ImageIcon;

import org.json.JSONObject;

public class Condition
{
    private final String    text;
    private final Icon      icon;
    private final int       code;
    
    public Condition( JSONObject condition )
    {
        text = condition.getString( "text" );
        code = condition.getInt( "code" );
        ImageIcon   tempIcon    = null;
        String      iconURL     = "https:" + condition.getString( "icon" );
        try
        {
            URI uri = new URI( iconURL );
            URL url = uri.toURL();
            tempIcon = new ImageIcon( url );
        }
        catch ( URISyntaxException | IOException exc  )
        {
            exc.printStackTrace();
//            System.exit( 1 );
        }
        icon = tempIcon;
//        if ( icon != null )
//            JOptionPane.showMessageDialog(
//                null, 
//                "My Icon", 
//                "My Title", 
//                JOptionPane.INFORMATION_MESSAGE, 
//                icon
//            );
//        System.out.println( this );
    }
    
    public String toString()
    {
        StringBuilder   bldr        = new StringBuilder();
        String          iconStat    = icon == null ? "non-null" : "null";
        bldr.append( "text:" ).append( text ).append( "," );
        bldr.append( "icon:" ).append( iconStat ).append( "," );
        bldr.append( "code:" ).append( code );
        return bldr.toString();
    }
}
