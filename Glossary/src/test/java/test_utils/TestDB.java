package test_utils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import com.judahstutorials.glossary.ConnectionMgr;
import com.judahstutorials.glossary.Definition;
import com.judahstutorials.glossary.SQLUtils;
import com.judahstutorials.glossary.SeeAlso;

public class TestDB
{
    private static final String     insertDefStr    =
        "INSERT INTO definition (term, seq_num, slug, description ) "
            + "VALUES (?, ?, ?, ?);";
    private static final String     selectDefStr    =
        "SELECT * FROM definition WHERE term = ?";
    private static final String     updateDefStr    = 
        "UPDATE definition SET "
            + "term = ?"
            + " ,seq_num = ?"
            + " ,slug = ?"
            + " ,description = ?"
            + " WHERE id = ?";
    private static final String     deleteDefStr    =
        "DELETE from definition where id = ?";
    private static final String     queryDefByIDStr =
        "SELECT * from definition where id = ?";

    private static final String     insertSAStr     =
        "INSERT INTO see_also (term_id, url) VALUES (?, ?)";
    private static final String     selectSAStr     =
        "SELECT * FROM see_also WHERE term_id = ?";
    private static final String     updateSAStr     = 
        "UPDATE see_also SET "
            + "url = ?"
            + "where id = ?";
    private static final String     deleteSAStr     =
        "DELETE from see_also where id = ?";
    private static final String     deleteSATermStr =
        "DELETE from see_also where term_id = ?";
    
    private final Definition        def1    = new Definition();
    private final Definition        def2    = new Definition();
    private final Definition        def3    = new Definition();
    private final List<Definition>  allDefs = new ArrayList<>();
    
    public TestDB()
    {
        truncate( "definition" );
        truncate( "see_also" );

        initDef( def1, "term1", "slug1", 1, "Desc1" );
        initDef( def2, "term2", "slug2", 1, "Desc2" );
        initDef( def3, "term3", "slug3", 1, "Desc3" );
        
        Stream.of( 1, 2, 3 ).forEach( i -> {
            SeeAlso seeAlso = new SeeAlso( def1.getID(), "url " + i );
            insert( seeAlso );
            def1.addSeeAlso( seeAlso );
        });
        
        Stream.of( 10, 20, 30 ).forEach( i -> {
            SeeAlso seeAlso = new SeeAlso( def2.getID(), "url " + i );
            insert( seeAlso );
            def2.addSeeAlso( seeAlso );
        });
        
        Stream.of( 100, 200, 300 ).forEach( i -> {
            SeeAlso seeAlso = new SeeAlso( def2.getID(), "url " + i );
            insert( seeAlso );
            def3.addSeeAlso( seeAlso );
        });
        
        ConnectionMgr.closeConnection();
    }
    
    public int getNumDefinitions()
    {
        return allDefs.size();
    }
    
    public Definition getDefinition( int inx )
    {
        return allDefs.get( inx );
    }

    public void initDB() throws SQLException
    {
        ConnectionMgr.selectDatabase( ConnectionMgr.TESTING );
        truncate( "definition" );
        truncate( "see_also" );
    }
    
    public boolean insert( Definition def )
    {
        boolean             rVal        = false;
        PreparedStatement   insertSQL   = 
            ConnectionMgr.getPreparedStatement( insertDefStr );
        ResultSet           result      = null;
        try
        {
            insertSQL.setString( 1, def.getTerm() );
            insertSQL.setInt( 2, def.getSeqNum() );
            insertSQL.setString( 3, def.getSlug() );
            insertSQL.setString( 4, def.getDescription() );
            if ( insertSQL.executeUpdate() == 1 )
            {
                result  = insertSQL.getGeneratedKeys();
                if ( !result.next() )
                {
                    String  msg = "Failed to get generated key";
                    throw new SQLException( msg );
                }
                def.setID( result.getInt( 1 ) );
                rVal = true;
            }
        }
        catch ( SQLException exc )
        {
            SQLUtils.postSQLException( "Insert definition", exc );
        }
        finally
        {
            close( result );
//            ConnectionMgr.closeConnection();
        }
        return rVal;
    }
    
    public List<SeeAlso> selectSeeAlsoGivenTermID( int term )
    {
        List<SeeAlso>       list    = new ArrayList<>();
        PreparedStatement   select  =
            ConnectionMgr.getPreparedStatement( selectSAStr );
        ResultSet           result  = null;
        try
        {
            select.setInt( 1, term );
            result = select.executeQuery();
            while ( result.next() )
            {
                int     ident   = result.getInt( "id" );
                String  url     = result.getString( "url" );
                SeeAlso seeAlso = new SeeAlso( ident, term, url );
                list.add( seeAlso );
            }
        }
        catch ( SQLException exc )
        {
            SQLUtils.postSQLException( "Select see_also", exc );
            list = null;
        }
        finally
        {
            close( result );
            ConnectionMgr.closeConnection();
        }
        
        return list;
    }
    
    public Definition queryDefGivenID( Integer ident )
    {
        Definition          def     = null;
        PreparedStatement   sql     = 
            ConnectionMgr.getPreparedStatement( queryDefByIDStr );
        try
        {
            sql.setInt( 1, ident );
            ResultSet   resultSet   = sql.executeQuery();
            if ( resultSet.next() )
                def = new Definition( resultSet );
        }
        catch ( SQLException exc )
        {
            exc.printStackTrace();
        }
        return def;
    }
    
    public boolean insert( List<SeeAlso> list )
    {
        boolean rVal    = true;
        int     count   = list.size();
        for ( int inx = 0 ; inx < count && rVal ; ++inx )
            rVal = insert( list.get( inx ) );
        return rVal;
    }
    
    public boolean insert( SeeAlso seeAlso )
    {
        boolean             rVal        = false;
        ResultSet           result      = null;
        PreparedStatement   insertSASQL =
            ConnectionMgr.getPreparedStatement( insertSAStr );
        try
        {
            insertSASQL.setInt( 1, seeAlso.getTermID() );
            insertSASQL.setString( 2, seeAlso.getURL() );
            if ( insertSASQL.executeUpdate() == 1 )
            {
                result = insertSASQL.getGeneratedKeys();
                if ( !result.next() )
                {
                    String  msg = "Failed to get generated key";
                    throw new SQLException( msg );
                }
                seeAlso.setID( result.getInt( 1 ) );
                rVal = true;
            }
        }
        catch ( SQLException exc )
        {
            SQLUtils.postSQLException( "Insert definition", exc );
        }
        finally
        {
            close( result );
//            ConnectionMgr.closeConnection();
        }
        return rVal;
    }
    
    private void close( ResultSet resultSet )
    {
        try
        {
            resultSet.close();
        }
        catch ( SQLException exc )
        {
            exc.printStackTrace();
        }
    }
    
    private void truncate( String table )
    {
        try
        {
            Connection  conn    = ConnectionMgr.getConnection();
            Statement   sql     = conn.createStatement();
            sql.closeOnCompletion();
            sql.execute( "TRUNCATE " + table );
        }
        catch ( SQLException exc )
        {
            exc.printStackTrace();
            System.exit( 1 );
        }
    }

    private void 
    initDef( 
        Definition def, 
        String term, 
        String slug, 
        int seqNum, 
        String desc
    )
    {
        def.setTerm( term );
        def.setSlug( slug );
        def.setSeqNum( seqNum );
        def.setDescription( desc );
        insert( def );
        allDefs.add( def );
    }
}
