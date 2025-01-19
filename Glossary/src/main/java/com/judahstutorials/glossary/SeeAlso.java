package com.judahstutorials.glossary;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SeeAlso
{
    private static final String             insertString    =
        "INSERT INTO see_also (term_id, url) VALUES (?, ?);";
    private static final PreparedStatement  insertSQL       =
        ConnectionMgr.getPreparedStatement( insertString );
    private static final String             listForStr      =
        "SELECT * FROM see_also WHERE term_id =? ";
    private static final PreparedStatement  listForSQL      =
        ConnectionMgr.getPreparedStatement( listForStr );
    
    private int     ident;
    private int     termID;
    private String  url;
    
    public SeeAlso( int ident, int termID, String see )
    {
        this.ident = ident;
        this.termID = termID;
        this.url = see;
    }
    
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
            if ( insertSQL.executeUpdate() == 1 )
            {
                ResultSet   key = insertSQL.getGeneratedKeys();
                if ( !key.next() )
                {
                    throw new Error( "Failed to get generated key" );
                }
                ident = key.getInt( 1 );
            }

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
    
    public String toString()
    {
        return url;
    }
    
    public static List<SeeAlso> getAllFor( int termID )
    {
        List<SeeAlso>   list    = new ArrayList<>();
        try
        {
            listForSQL.setInt( 1, termID);
            ResultSet   resultSet   = listForSQL.executeQuery();
            while ( resultSet.next() )
            {
                int     ident       = resultSet.getInt( "id" );
                int     termIdent   = resultSet.getInt( "term_id" );
                String  url         = resultSet.getString( "url" );
                SeeAlso rec         = new SeeAlso( ident, termIdent, url );
                list.add( rec );
            }
            resultSet.close();
        }
        catch ( SQLException exc )
        {
            exc.printStackTrace();
            System.exit( 1 );
        }
        return list;
    }
}
