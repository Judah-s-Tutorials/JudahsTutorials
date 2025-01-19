package com.judahstutorials.glossary;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ConnectionMgr
{
    private static final String host        = 
        "jdbc:mysql://judahstutorials.com/judah614_JGlossary2";
    private static final String user        = "judah614_JackStraub";
    private static final String password    = "Glossary1@01";
    
    private static Connection       conn        = null;
    private static List<Statement>  statements  = new ArrayList<>();
    
    public ConnectionMgr()
    {
        // TODO Auto-generated constructor stub
    }

    public static Connection getConnection()
    {
        if ( conn == null )
        {
            try
            {
                conn = DriverManager.getConnection( host, user, password );
                conn.setAutoCommit( true );
            }
            catch ( SQLException exc )
            {
                exc.printStackTrace();
            }
        }
        return conn;
    }
    
    public static PreparedStatement getPreparedStatement( String sql )
    {
        final int returnKey = Statement.RETURN_GENERATED_KEYS;
        PreparedStatement   statement   = null;
        try
        {
            Connection  conn    = getConnection();
            if ( conn != null )
                statement = conn.prepareStatement( sql, returnKey );
        }
        catch ( SQLException exc )
        {
            exc.printStackTrace();
            System.exit( 1 );
        }
        return statement;
    }
    
    public static void closeStatement( Statement statement )
    {
        try
        {
            statement.close();
            statements.remove( statement );
        }
        catch ( SQLException exc )
        {
            exc.printStackTrace();
        }
    }
    
    public static void closeAllResources()
    {
        try
        {
            for ( Statement state : statements )
                state.close();
        }
        catch ( SQLException exc )
        {
            exc.printStackTrace();
        }
    }
    
    public static void closeConnection()
    {
        if ( conn != null )
        {
            try
            {
                closeAllResources();
                conn.close();
            }
            catch ( SQLException exc )
            {
                exc.printStackTrace();
            }
        }
    }
}
