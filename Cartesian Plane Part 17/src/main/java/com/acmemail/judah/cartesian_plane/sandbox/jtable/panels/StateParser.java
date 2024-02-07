package com.acmemail.judah.cartesian_plane.sandbox.jtable.panels;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class StateParser
{
    private static String   resDir      = "sandbox";
    private static String   mainCSV     = 
        resDir + "/" + "us-state-capitals.csv";
    private static String   popCSV      = 
        resDir + "/" + "us-state-populations.csv";
    private static String   zipCSV      = 
        resDir + "/" + "us-state-zipcodes.csv";
    private static String   allCSV      = "us-state-all-data.csv";
    
    private List<State> states  = new ArrayList<>();
    
    /**
     * Application entry point.
     *
     * @param args command line arguments, not used
     *
    */
    public static void main(String[] args)
    {
        StateParser parser  = new StateParser();
        parser.mainData();
        parser.addPopulationAndAbbreviation();
        parser.addZip();
        parser.states.forEach( System.out::println );
        parser.writeResult();
        System.out.println( parser.states.size() );
    }
    
    private void mainData()
    {
        try ( 
            InputStream inStr = getResourceAsStream( mainCSV );
            InputStreamReader inReader = new InputStreamReader( inStr );
            BufferedReader reader =new BufferedReader( inReader );
        )
        {
            reader.lines()
                .filter( l -> !l.startsWith( "name" ) )
                .map( l -> parseCSVLine( l, 4 ) )
                .map( l -> l.toArray( new String[4] ) )
                .map( o -> new String[] { o[0], o[1], "0", o[2], o[3] } )
                .map( sa -> new State( sa ) )
                .forEach( states::add );
        }
        catch ( 
            IOException 
            | ComponentException 
            | NumberFormatException exc
        )
        {
            exc.printStackTrace();
            System.exit( 1 );
        }
    }
    
    private void addPopulationAndAbbreviation()
    {
        try ( 
            InputStream inStr = getResourceAsStream( popCSV );
            InputStreamReader inReader = new InputStreamReader( inStr );
            BufferedReader reader =new BufferedReader( inReader );
        )
        {
            State   temp    = new State();
            reader.lines()
                .filter( l -> !l.startsWith( "code" ) )
                .map( l -> parseCSVLine( l, 3 ) )
                .map( l -> l.toArray( new String[3] ) )
                .peek( sa -> temp.setAbbreviation( sa[0] ) )
                .peek( sa -> temp.setName( sa[1] ) )
                .map( sa -> sa[2] )
                .mapToInt( Integer::parseInt )
                .peek( temp::setPopulation )
                .mapToObj( d -> State.findByName( states, temp.getName() ) )
                .filter( st -> st != null )
                .forEach( st -> {
                    st.setAbbreviation( temp.getAbbreviation() );
                    st.setPopulation( temp.getPopulation() );
                });
        }
        catch ( 
            IOException 
            | ComponentException 
            | NumberFormatException exc
        )
        {
            exc.printStackTrace();
            System.exit( 1 );
        }
    }
        
    private void addZip()
    {
        try ( 
            InputStream inStr = getResourceAsStream( zipCSV );
            InputStreamReader inReader = new InputStreamReader( inStr );
            BufferedReader reader =new BufferedReader( inReader );
        )
        {
            State   temp    = new State();
            reader.lines()
                .map( l -> parseCSVLine( l, 4 ) )
                .map( l -> l.toArray( new String[4] ) )
                .peek( sa -> temp.setName( sa[0] ) )
                .peek( sa -> temp.setZipStart( sa[2] ) )
                .peek( sa -> temp.setZipEnd( sa[3] ) )
                .map( sa -> sa[0] )
                .map( d -> State.findByName( states, temp.getName() ) )
                .filter( st -> st != null )
                .forEach( st -> {
                    st.setZipStart( temp.getZipStart() );
                    st.setZipEnd( temp.getZipEnd() );
                });
        }
        catch ( 
            IOException 
            | ComponentException 
            | NumberFormatException exc
        )
        {
            exc.printStackTrace();
            System.exit( 1 );
        }
    }
    
    private List<String> parseCSVLine( String line, int expCount )
    {
        StringTokenizer tizer   = new StringTokenizer( line, "," );
        if ( tizer.countTokens() != expCount )
            throw new ComponentException( "Invalid CSV field count" );
        
        List<String>    list    = new ArrayList<>();
        while ( tizer.hasMoreTokens() )
        {
            String  field   = tizer.nextToken().trim();
            list.add( field );
        }
        
        return list;
    }
    
    private void writeResult()
    {
        try (
            FileWriter  fWriter = new FileWriter( allCSV );
            PrintWriter pWriter = new PrintWriter( fWriter );
        )
        {
            String  header  =
                "name,abbrev,capital,population,zip start,zip end,"
                + "latitude,longitude";
            pWriter.println( header );
            states.forEach( pWriter::println );
        }
        catch ( 
            IOException 
            | ComponentException 
            | NumberFormatException exc
        )
        {
            exc.printStackTrace();
            System.exit( 1 );
        }
    }
    
    private InputStream getResourceAsStream( String resource )
    {
        ClassLoader loader      = State.class.getClassLoader();
        InputStream inStream    = loader.getResourceAsStream( resource );
        if ( inStream == null )
        {
            String  msg = "Resource file \"" 
                + resource + "\" not found";
            System.err.println( msg );
            throw new ComponentException( msg );
        }
        return inStream;
    }
}
