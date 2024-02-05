package com.acmemail.judah.cartesian_plane.sandbox.jtable.panels;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collection;
import java.util.List;

public class State
{
    private static String       resDir      = "sandbox";
    private static String       mainCSV     = 
        resDir + "/" + "us-state-all-data.csv";
    private static List<State>  allStates   = null;
    
    private String  name;
    private String  abbreviation;
    private String  capital;
    private double  population;
    private String  zipStart;
    private String  zipEnd;
    private double  latitude;
    private double  longitude;
    
    public State()
    {
        // default constructor
    }
    
    public State( String... fields ) 
        throws NumberFormatException, IndexOutOfBoundsException
    {
        name = fields[0];
        capital = fields[1];
        double  totalPop    = Double.parseDouble( fields[2] );
        population = totalPop / 1e6;
        latitude = Double.parseDouble( fields[3] );
        longitude = Double.parseDouble( fields[4] );
    }
    
    public State( String csv )
        throws NumberFormatException, IllegalArgumentException, 
        NullPointerException
    {
        String[]        fields  = csv.split( "," );
        if ( fields.length != 8 )
            throw new IllegalArgumentException( "Wrong number of fields" );
        name = fields[0];
        abbreviation = fields[1];
        capital = fields[2];
        population = Double.parseDouble( fields[3] );
        zipStart = fields[4];
        zipEnd = fields[5];
        latitude = Double.parseDouble( fields[6] );
        longitude = Double.parseDouble( fields[7] );
    }
    
    public static List<State> getStateList()
    {
        if ( allStates == null )
            buildStateList();
        return allStates;
    }
    
    private static void buildStateList()
    {
        try ( 
            InputStream inStr = getResourceAsStream( mainCSV );
            InputStreamReader inReader = new InputStreamReader( inStr );
            BufferedReader bufReader = new BufferedReader( inReader );
        )
        {
            allStates = bufReader.lines()
                .filter( l -> !l.startsWith( "name" ) )
                .map( l -> new State( l ) )
                .toList();
        }
        catch ( IOException | NumberFormatException exc )
        {
            exc.printStackTrace();
            System.exit( 1 );
        }
    }
    
    public static State findByName( Collection<State> states, String name )
    {
        State   state   = states.stream()
            .filter( s -> name.equalsIgnoreCase( s.getName() ) )
            .findFirst().orElse( null );
        return state;
    }
    
    public static Object[][] getDataSet( String... headers )
    {
        getStateList();
        int         size        = allStates.size();
        int         colCount    = headers.length;
        Object[][]  dataArr     = new Object[size][];
        int         row         = 0;
        for ( State state : allStates )
        {
            int         col     = 0;
            Object[]    nextRow = new Object[colCount];
            for ( String header : headers )
                nextRow[col++] = getField( state, header );
            dataArr[row++] = nextRow;
        }
        return dataArr;
    }
    
    private static Object getField( State state, String name )
    {
        Object  val     = null;
        switch ( name )
        {
        case "name":
            val = state.getName();
            break;
        case "abbreviation":
            val = state.getAbbreviation();
            break;
        case "capital":
            val = state.getCapital();
            break;
        case "population":
            val = state.getPopulation();
            break;
        case "zip_start":
            val = state.getZipStart();
            break;
        case "zip_end":
            val = state.getZipEnd();
            break;
        case "latitude":
            val = state.getLatitude();
            break;
        case "longitude":
            val = state.getLongitude();
            break;
        default:
            val = null;    
        }
        
        return val;
    }
    
    @Override
    public String toString()
    {
        StringBuilder   bldr    = new StringBuilder();
        bldr.append( name ).append( ',' )
        .append( abbreviation ).append( ',' )
        .append( capital ).append( ',' )
        .append( population ).append( ',' )
        .append( zipStart ).append( ',' )
        .append( zipEnd ).append( ',' )
        .append( latitude ).append( ',' )
        .append( longitude );


        return bldr.toString();
    }

    /**
     * @return the name
     */
    public String getName()
    {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name)
    {
        this.name = name;
    }

    /**
     * @return the abbreviation
     */
    public String getAbbreviation()
    {
        return abbreviation;
    }

    /**
     * @param abbreviation the abbreviation to set
     */
    public void setAbbreviation(String abbreviation)
    {
        this.abbreviation = abbreviation;
    }

    /**
     * @return the zipStart
     */
    public String getZipStart()
    {
        return zipStart;
    }

    /**
     * @param zipStart the zipStart to set
     */
    public void setZipStart(String zipStart)
    {
        this.zipStart = zipStart;
    }

    /**
     * @return the zipEnd
     */
    public String getZipEnd()
    {
        return zipEnd;
    }

    /**
     * @param zipEnd the zipEnd to set
     */
    public void setZipEnd(String zipEnd)
    {
        this.zipEnd = zipEnd;
    }

    /**
     * @return the capital
     */
    public String getCapital()
    {
        return capital;
    }

    /**
     * @param capital the capital to set
     */
    public void setCapital(String capital)
    {
        this.capital = capital;
    }

    /**
     * @return the population
     */
    public double getPopulation()
    {
        return population;
    }

    /**
     * @param population the population to set
     */
    public void setPopulation(double population)
    {
        this.population = population;
    }

    /**
     * @return the latitude
     */
    public double getLatitude()
    {
        return latitude;
    }

    /**
     * @param latitude the latitude to set
     */
    public void setLatitude(double latitude)
    {
        this.latitude = latitude;
    }

    /**
     * @return the longitude
     */
    public double getLongitude()
    {
        return longitude;
    }

    /**
     * @param longitude the longitude to set
     */
    public void setLongitude(double longitude)
    {
        this.longitude = longitude;
    }
    
    private static InputStream getResourceAsStream( String resource )
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
