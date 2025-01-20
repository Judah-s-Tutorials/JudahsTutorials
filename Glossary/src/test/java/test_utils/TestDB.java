package test_utils;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import com.judahstutorials.glossary.ConnectionMgr;

public class TestDB
{

    public TestDB()
    {
        // TODO Auto-generated constructor stub
    }

    public void initDB() throws SQLException
    {
        ConnectionMgr.selectDatabase( ConnectionMgr.TESTING );
        truncateDefinition();
        truncateSeeAlso();
    }
    
    public void truncateDefinition() throws SQLException
    {
        truncate( "definition" );
    }
    
    public void truncateSeeAlso() throws SQLException
    {
        truncate( "see_also" );
    }
    
    private void truncate( String table ) throws SQLException
    {
        Connection  conn    = ConnectionMgr.getConnection();
        Statement   sql     = conn.createStatement();
        sql.closeOnCompletion();
        sql.execute( "TRUNCATE " + table );
    }
}
