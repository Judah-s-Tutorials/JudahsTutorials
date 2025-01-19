package com.judahstutorials.glossary;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JOptionPane;

public class Definition
{
    private static final String             insertString    =
        "INSERT INTO definition (term, seq_num, slug, description ) "
            + "VALUES (?, ?, ?, ?);";
    private static final PreparedStatement  insertSQL       =
        ConnectionMgr.getPreparedStatement( insertString );
    
    private Integer     ident       = null;
    private String      term        = "";
    private Integer     seqNum      = null;
    private String      slug        = null;
    private String      description = null;

    private boolean     isError     = false;
    
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
        }
        catch ( SQLException exc )
        {
            exc.printStackTrace();
            System.exit( 1 );
        }
    }
    
    public 
    Definition( String term, String seqNum, String slug, String desc )
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
    
    public void setTerm( String term )
    {
        if ( term == null )
            this.term = "";
        else
            this.term = term;
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
    
    public void setSeqNum( String seqNum )
    {
        if ( seqNum == null || seqNum.isEmpty() )
            this.seqNum = null;
        else
        {
            try
            {
                this.seqNum = Integer.parseInt( seqNum );
            }
            catch ( NumberFormatException exc )
            {
                String  err = "\"" + seqNum + "\" not valid sequence #";
                postError( err );
                isError = true;
                this.seqNum = null;
            }
        }
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
    
    private void setID( int ident )
    {
        this.ident = ident;
    }
    
    private static void postError( String err )
    {
        JOptionPane.showMessageDialog( null, err );
    }
}
