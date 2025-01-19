package com.judahstutorials.glossary;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Definition
{
    private static final String             insertString    =
        "INSERT INTO see_also (term, seq_num, slug, description ) "
            + "VALUES (?, ?, ?, ?);";
    private static final PreparedStatement  insertSQL       =
        ConnectionMgr.getPreparedStatement( insertString );
    
    private String      term        = "";
    private Integer     seqNum      = null;
    private String      slug        = null;
    private String      description = null;

    private Integer     ident       = null;
    
    public Definition()
    {
        
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

    public Integer getSeqNum()
    {
        if ( seqNum == null )
            seqNum = 0;
        return seqNum;
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

    public String getDescription()
    {
        return description;
    }
    
    public Integer insert()
    {
        Integer ident   = null;
        if ( this.ident == null )
        {
            try
            {
                insertSQL.setString( 1, getTerm() );
                insertSQL.setInt( 2, getSeqNum() );
                insertSQL.setString( 3, getSlug() );
                insertSQL.setString( 4, description );
                if ( insertSQL.executeUpdate() == 1 )
                {
                    ResultSet   result  = insertSQL.getResultSet();
                    ident = result.getInt( "id" );
                }
            }
            catch ( SQLException exc )
            {
                exc.printStackTrace();
            }
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
}
