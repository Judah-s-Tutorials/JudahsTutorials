package com.judahstutorials.glossary;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SeeAlso
{
    private static final String             insertString    =
        "INSERT INTO see_also (term_id, url) VALUES (?, ?)";
    private static final String             deleteString    =
        "DELETE from see_also where id = ?";
    private static final String             updateString    =
        "UPDATE see_also SET "
            + "url = ?"
            + "where id = ?";
    private static final String             listForStr      =
        "SELECT * FROM see_also WHERE term_id = ? ";
    private static final String             deleteForStr    =
        "DELETE from see_also where term_id = ?";
    
    private Integer ident;
    private Integer termID;
    private String  url;
    
    private transient boolean   delete  = false;
    private transient boolean   update  = false;
    private transient boolean   error   = false;
    
    public SeeAlso( Integer ident, Integer termID, String url )
    {
        setID( ident );
        setTermID( termID );
        setURL( url );
    }
    
    public SeeAlso( Integer termID, String url )
    {
        this( null, termID, url );
    }
    
    public SeeAlso()
    {
        this( null, null, "" );
    }
    
    public void markForDelete( boolean delete )
    {
        this.delete = delete;
    }
    
    public boolean isError()
    {
        return error;
    }
    
    public boolean isMarkedForDelete()
    {
        return delete;
    }
    
    public void markForUpdate( boolean update )
    {
        this.update = update;
    }
    
    public boolean isMarkedForUpdate()
    {
        return update;
    }
    
    public void update()
    {
        if ( ident != null )
        {
            PreparedStatement  updateSQL    =
                ConnectionMgr.getPreparedStatement( updateString );
            update( updateSQL );
        }
    }
    
    public void delete()
    {
        try
        {
            PreparedStatement   deleteSQL   =
                ConnectionMgr.getPreparedStatement( deleteString );
            deleteSQL.setInt( 1, ident );
            if ( deleteSQL.executeUpdate() != 1 )
                SQLUtils.postSQLError( "Delete failure" );

        }
        catch ( SQLException exc )
        {
            SQLUtils.postSQLException( "Delete for term", exc );
        }
    }
    
    private void update( PreparedStatement updateSQL )
    {
        try
        {
            updateSQL.setString( 1, url );
            updateSQL.setInt( 2, ident );
            updateSQL.executeUpdate();
        }
        catch ( SQLException exc )
        {
            SQLUtils.postSQLException( "Update SeeAlso", exc );
        }
    }
    
    public static void deleteAll( int termID )
    {
        try
        {
            PreparedStatement   deleteSQL   =
                ConnectionMgr.getPreparedStatement( deleteForStr );
            deleteSQL.setInt( 1, termID );
            deleteSQL.executeUpdate();
        }
        catch ( SQLException exc )
        {
            SQLUtils.postSQLException( "Delete for term", exc );
        }
    }

    private void delete( PreparedStatement deleteSQL )
    {
        try
        {
            deleteSQL.setInt( 1, ident );
            if ( deleteSQL.executeUpdate() == 1 )
                ident = null;
            else
                SQLUtils.postSQLError( "Delete SeeAlso failure" );
        }
        catch ( SQLException exc )
        {
            exc.printStackTrace();
            SQLUtils.postSQLException( "Delete SeeAlso", exc );
        }
    }
    
    public void insert()
    {
        if ( ident == null )
        {
            PreparedStatement  insertSQL       =
                ConnectionMgr.getPreparedStatement( insertString );
            insert( insertSQL );
        }
    }
    
    private void insert( PreparedStatement insertSQL )
    {
        ident = null;
        try
        {
            insertSQL.setInt( 1, termID );
            insertSQL.setString( 2, url );
            if ( insertSQL.executeUpdate() == 1 )
            {
                ResultSet   key = insertSQL.getGeneratedKeys();
                if ( !key.next() )
                {
                    throw new Error( "Failed to get generated key" );
                }
                ident = key.getInt( 1 );
                key.close();
            }
        }
        catch ( SQLException exc )
        {
            exc.printStackTrace();
        }
    }
    
    public static SeeAlso insert( int termID, String url )
    {
        SeeAlso             seeAlso     = new SeeAlso( termID, url );
        PreparedStatement   insertSQL   =
            ConnectionMgr.getPreparedStatement( insertString );
        seeAlso.insert( insertSQL );
        return seeAlso;
    }
    
    public static void commit( List<SeeAlso> list )
    {
        PreparedStatement   insertSQL   =
            ConnectionMgr.getPreparedStatement( insertString );
        PreparedStatement   updateSQL   =
            ConnectionMgr.getPreparedStatement( updateString );
        PreparedStatement   deleteSQL   =
            ConnectionMgr.getPreparedStatement( deleteString );
        for ( SeeAlso next : list )
        {
            if ( next.isMarkedForDelete() )
                next.delete( deleteSQL );
            else if ( next.getID() == null )
                next.insert( insertSQL );
            else if ( next.isMarkedForUpdate() )
            {
                next.update( updateSQL );
                next.markForUpdate( false );
            }
        }
    }

    public Integer getID()
    {
        return ident;
    }
    
    public void setID( Integer ident )
    {
        this.ident = ident;
    }
    
    public Integer getTermID()
    {
        return termID;
    }
    
    public void updateTermID( Integer termID )
    {
        setTermID( termID );
        markForUpdate( true );
    }

    public String getURL()
    {
        return url;
    }

    public void updateURL( String url )
    {
        setURL( url );
        markForUpdate( true );
    }
    
    public String toString()
    {
        String  rval    = url;
        if ( isMarkedForDelete() )
        {
            StringBuilder   bldr    = new StringBuilder();
            bldr.append( "<HTML>" )
                .append( '\u2421' )
                .append( "<font color='red'>" )
                .append( url )
                .append( "</font>")
                .append( "</HTML>" );
            rval = bldr.toString();
        }
        return rval;
    }
    
    public static List<SeeAlso> getAllFor( int termID )
    {
        PreparedStatement   listForSQL  =
            ConnectionMgr.getPreparedStatement( listForStr );
        List<SeeAlso>       list        = new ArrayList<>();
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
    
    private void setTermID( Integer termID )
    {
        this.termID = termID;
    }

    private void setURL( String url )
    {
        this.url = url;
    }
}
