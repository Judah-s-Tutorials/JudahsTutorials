package com.judahstutorials.glossary;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

public class Definition
{
    private static final String     selectString    =
        "SELECT * FROM definition WHERE term = ?";
    private static final String     insertString    =
        "INSERT INTO definition (term, seq_num, slug, description ) "
            + "VALUES (?, ?, ?, ?);";
    private static final String     deleteString    =
        "DELETE from definition where id = ?";
    private static final String     updateString    =
        "UPDATE see_also SET "
            + "term = ?"
            + " ,seq_num = ?"
            + " ,slug = ?"
            + " ,description = ?";
    
    private Integer     ident       = null;
    private String      term        = "";
    private Integer     seqNum      = null;
    private String      slug        = null;
    private String      description = null;

    private transient final 
        List<SeeAlso>   seeAlso     = new ArrayList<>();
    private transient boolean     isError     = false;
    
    public Definition()
    {
        
    }
    
    public Definition( ResultSet resultSet )
    {
        try
        {
            setID( resultSet.getInt( "id" ) );
            setTerm( resultSet.getString( "term" ) );
            setSlug( resultSet.getString( "slug" ) );
            setSeqNum( resultSet.getInt( "seq_num" ) );
            setDescription( resultSet.getString( "description" ) );
            seeAlso.addAll( SeeAlso.getAllFor( getID() ) );
        }
        catch ( SQLException exc )
        {
            exc.printStackTrace();
            System.exit( 1 );
        }
    }
    
    public void commit()
    {
        if ( ident == null )
        {
            PreparedStatement   sql     = 
                ConnectionMgr.getPreparedStatement( insertString );
            insert( sql );
        }
        else
        {
            PreparedStatement   sql     = 
                ConnectionMgr.getPreparedStatement( updateString );
            update( sql );
        }

    }
    
    public 
    Definition( String term, Integer seqNum, String slug, String desc )
    {
        setTerm( term );
        setSeqNum( seqNum );
        setSlug( slug );
        setDescription( desc );
    }
    
    public boolean isError()
    {
        return isError;
    }
    
    public void resetError()
    {
        isError = false;
    }
    
    public void addSeeAlso( SeeAlso seeAlso )
    {
        this.seeAlso.add( seeAlso );
    }
    
    public void setTerm( String term )
    {
        if ( term == null )
            this.term = "";
        else
            this.term = term;
    }
    
    public List<SeeAlso> getSeeAlso()
    {
        return seeAlso;
    }
    
    public static Definition select( String term )
    {
        Definition  def = null;
        try
        {
            PreparedStatement   selectSQL   =
                ConnectionMgr.getPreparedStatement( selectString );
            selectSQL.setString( 1, term );
            ResultSet           resultSet   = selectSQL.executeQuery();
            if ( resultSet.next() )
                def = new Definition( resultSet );
            resultSet.close();
        }
        catch ( SQLException exc )
        {
            SQLUtils.postSQLException( "Select term", exc );
        }
        return def;
    }
    
    public void insert()
    {
        if ( ident != null )
        {
            PreparedStatement   insertSQL   =
                ConnectionMgr.getPreparedStatement( insertString );
            insert( insertSQL );
        }
    }

    public void delete()
    {
        if ( ident != null )
        {
            PreparedStatement   deleteSQL   =
                ConnectionMgr.getPreparedStatement( deleteString );
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
                SQLUtils.postSQLException( "Delete SeeAlso", exc );
            }
        }
    }

    public String getTerm()
    {
        return term;
    }
    
    public String getTermDisplay()
    {
        String  term    = this.term;
        if ( seqNum != null )
            term += "(" + seqNum + ")";
        return term;
    }
    
    public void setSeqNum( Integer seqNum )
    {
        this.seqNum = seqNum;
    }

    public Integer getSeqNum()
    {
        if ( seqNum == null )
            seqNum = 0;
        return seqNum;
    }
    
    public void setSlug( String slug )
    {
        if ( slug == null || slug.isEmpty() )
            this.slug = null;
        else
            this.slug = slug;
    }

    public String getSlug()
    {
        if ( slug == null )
        {
            slug = term;
            if ( seqNum != null && seqNum > 0 )
                slug += "-" + seqNum;
        }
        return slug;
    }
    
    public void setDescription( String desc )
    {
        if ( desc == null )
            description = "";
        else
            description = desc;
    }

    public String getDescription()
    {
        return description;
    }
    
    private Integer insert( PreparedStatement insertSQL )
    {
        try
        {
            insertSQL.setString( 1, getTerm() );
            insertSQL.setInt( 2, getSeqNum() );
            insertSQL.setString( 3, getSlug() );
            insertSQL.setString( 4, description );
            if ( insertSQL.executeUpdate() == 1 )
            {
                ResultSet   result  = insertSQL.getGeneratedKeys();
                if ( !result.next() )
                {
                    throw new Error( "Failed to get generated key" );
                }
                ident = result.getInt( 1 );
                result.close();
            }
        }
        catch ( SQLException exc )
        {
            SQLUtils.postSQLException( "Insert definition", exc );
        }
        return ident;
    }
    
    private Integer update( PreparedStatement updateSQL )
    {
        try
        {
            updateSQL.setInt( 1, getID() );
            updateSQL.setString( 2, getTerm() );
            updateSQL.setInt( 3, getSeqNum() );
            updateSQL.setString( 4, getSlug() );
            updateSQL.setString( 5, description );
            if ( updateSQL.executeUpdate() == 1 )
            {
                ResultSet   result  = updateSQL.getGeneratedKeys();
                if ( !result.next() )
                {
                    throw new Error( "Failed to get generated key" );
                }
                ident = result.getInt( 1 );
                result.close();
            }
        }
        catch ( SQLException exc )
        {
            SQLUtils.postSQLException( "Insert definition", exc );
        }
        return ident;
    }

    public String toString()
    {
        StringBuilder   bldr        = new StringBuilder();
        String          temp        = description.trim();
        int             descLen     = temp.length();
        String          descBegin   = "";
        String          descEnd     = "";
        String          desc        = null;
        if ( descLen <= 10 )
            desc = description;
        else
        {
            descBegin = temp.substring( 0, 5 );
            descEnd = temp.substring( descLen - 5, descLen );
            desc = descBegin + "..." + descEnd;
        }
        bldr.append( "term=" ).append( term );
        bldr.append( ",seqNum=" ).append( seqNum );
        bldr.append( ",slug=" ).append( slug );
        bldr.append( "," ).append( desc );
        return bldr.toString();
    }
    
    public Integer getID()
    {
        return ident;
    }
    
    private void setID( int ident )
    {
        this.ident = ident;
    }
    
    private static void postError( String err )
    {
        JOptionPane.showMessageDialog( null, err );
    }
}
