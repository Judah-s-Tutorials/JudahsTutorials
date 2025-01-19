package com.judahstutorials.glossary;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SeeAlso
{
    private static final String             insertString    =
        "INSERT INTO see_also (term_id, url) VALUES (?, ?);";
    private static final PreparedStatement  insertSQL       =
        ConnectionMgr.getPreparedStatement( insertString );
    
    private int     ident;
    private int     termID;
    private String  url;
    
    public SeeAlso( int termID, String see )
    {
        this.termID = termID;
        this.url = see;
    }
    
    public SeeAlso()
    {
        this( 0, "" );
    }
    
    public Integer insert()
    {
        Integer ident   = insert( termID, url );
        return ident;
    }
    
    public static Integer insert( int termID, String url )
    {
        Integer ident   = null;
        try
        {
            insertSQL.setInt( 1, termID );
            insertSQL.setString( 2, url );
            insertSQL.executeUpdate();
            ResultSet   result  = insertSQL.getResultSet();
            ident = result.getInt( "id" );
        }
        catch ( SQLException exc )
        {
            exc.printStackTrace();
        }
        return ident;
    }

    public int getIdent()
    {
        return ident;
    }

    public String getURL()
    {
        return url;
    }

    public void setURL(String url)
    {
        this.url = url;
    }
}
