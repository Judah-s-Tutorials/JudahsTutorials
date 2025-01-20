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
    public static final String  PRODUCTION  = "judah614_JGlossary1";
    public static final String  SANDBOX     = "judah614_JGlossary2";
    public static final String  TESTING     = "judah614_JGlossaryTest";
    private static final String host        = 
        "jdbc:mysql://judahstutorials.com/";
    private static final String user        = "judah614_JackStraub";
    private static final String password    = "Glossary1@01";
    
    private static Connection       conn        = null;
    private static List<Statement>  statements  = new ArrayList<>();
    
    private static String selectedDatabase     = SANDBOX;
    
    private ConnectionMgr()
    {
        // TODO Auto-generated constructor stub
    }
    
    public static void selectDatabase( String database )
    {
        selectedDatabase = database;
    }

    synchronized public static Connection getConnection()
    {
        if ( conn == null )
        {
            try
            {
                String  target  = host + selectedDatabase;
                conn = DriverManager.getConnection( target, user, password );
                conn.setAutoCommit( true );
            }
            catch ( SQLException exc )
            {
                exc.printStackTrace();
            }
        }
        return conn;
    }
    
    synchronized public static PreparedStatement 
    getPreparedStatement( String sql )
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
    
    synchronized public static void closeStatement( Statement statement )
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
    
    synchronized public static void closeAllResources()
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
    
    synchronized public static void closeConnection()
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
            conn = null;
        }
    }
}
