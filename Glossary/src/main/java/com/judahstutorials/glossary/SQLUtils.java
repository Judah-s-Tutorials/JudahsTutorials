package com.judahstutorials.glossary;

import java.sql.SQLException;

import javax.swing.JOptionPane;

public class SQLUtils
{

    public SQLUtils()
    {
        // TODO Auto-generated constructor stub
    }
    
    public static void postSQLError( String message )
    {
        final String    title   = "SQL Error";
        final int       type    = JOptionPane.ERROR_MESSAGE;
        String          text    = "SQL error: " + message;
        JOptionPane.showMessageDialog( null, text, title, type, null );
    }
    
    public static void 
    postSQLException( String operation, SQLException exc )
    {
        final String    title   = "SQL Exception";
        final int       type    = JOptionPane.ERROR_MESSAGE;
        StringBuilder   bldr    = new StringBuilder();
        bldr.append( "<html>" )
            .append( "SQL Exception for: " ).append( operation )
            .append( "<br>" )
            .append( exc.getMessage() )
            .append( "</html>" );
        exc.printStackTrace();
        JOptionPane.showMessageDialog( null, bldr, title, type, null );
    }
}
